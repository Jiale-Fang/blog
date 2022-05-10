package pers.fjl.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pers.fjl.common.dto.EmailDTO;
import pers.fjl.common.po.Order;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.NotifyParamsVO;
import pers.fjl.common.vo.PayParamsVO;
import pers.fjl.server.dao.OrderDao;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.service.GoGoPayService;
import pers.fjl.server.utils.BeanCopyUtils;
import pers.fjl.server.utils.HttpUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static pers.fjl.common.constant.RabbitMQConst.EMAIL_EXCHANGE;

@Service
public class GoGoPayServiceImpl implements GoGoPayService {

    @Value("${pay.appId}")
    private String appId;

    @Value("${pay.appSecret}")
    private String appSecret;

    @Value("${pay.notifyUrl}")
    private String notifyUrl;

    @Value("${pay.returnUrl}")
    private String returnUrl;

    @Resource
    private OrderDao orderDao;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserDao userDao;

    // 支付时调用该方法，设置好传入参数PayParams
    public void pay(String uid, HttpServletResponse response) {
        String idStr = IdWorker.getIdStr(PayParamsVO.class);
        PayParamsVO payParamsVO = new PayParamsVO();
        payParamsVO.setPayId(idStr);
        payParamsVO.setPrice("2.88");   //价格
        payParamsVO.setIsHtml(1);
        payParamsVO.setType("1");    //支付类型
        payParamsVO.setParam(uid);   //用户id
        payParamsVO.setNotifyUrl(notifyUrl);
        payParamsVO.setReturnUrl(returnUrl);
        //计算并设置签名sign
        payParamsVO.setSign(generateOrderSign(payParamsVO, appId, appSecret));
        String paramsString = JSONObject.toJSONString(payParamsVO);
        String apiUrl = "https://www.gogozhifu.com/shop/api/createOrder";
        //发起的goPost请求里需要设置请求头App-Id和App-Secret
        String result = HttpUtils.goPost(apiUrl, paramsString, appId, appSecret);

        // 如果使用了GOGO支付收银台页面，直接在跳转的页面完成支付即可
        if (payParamsVO.getIsHtml() == 1) {
            response.setContentType("text/html; charset=utf-8");
        } else {
            // 如果是使用自定义JSON模式，这里需要用户自己来对数据做处理，根据返回的支付数据自定义收款页面
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(result);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    // 接收GOGO支付完成的回调通知方法，在该函数中主要用于商户自己根据支付完成处理相应的数据逻辑
    public String callBackNotify(NotifyParamsVO notifyParamsVO, HttpServletResponse response) {
        if (null != notifyParamsVO) {
            String sign = generateNotifySign(notifyParamsVO, appId, appSecret);
            if (sign.equals(notifyParamsVO.getSign())) {
                // 在这里根据商户自己需求完成后续逻辑操作
                // 订单数据更新操作...
                Order order = BeanCopyUtils.copyObject(notifyParamsVO, Order.class);
                order.setUid(Long.parseLong(notifyParamsVO.getParam()));
                orderDao.insert(order);
                // 成功完成后正确返回
                response.setStatus(HttpServletResponse.SC_OK);
                User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUid, order.getUid()).select(User::getEmail));
                if (Objects.nonNull(user)){
                    // 发送邮箱信息
                    EmailDTO emailDTO = EmailDTO.builder()
                            .email(user.getEmail())
                            .subject("博客整合资料")
                            .content("资料笔记链接为：" + returnUrl + "。后续还会不断更新，有问题请联系本邮箱。")
                            .build();
                    rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, null, new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
                }

                return "success";
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "error";
    }

    //生成回调通知的签名sign
    private static String generateNotifySign(NotifyParamsVO params, String appId, String appSecret) {
        if (!StringUtils.isEmpty(appSecret)) {
            StringBuilder sb = new StringBuilder();
            sb.append(appId);
            sb.append(params.getPayId());
            sb.append(params.getParam());
            sb.append(params.getType());
            sb.append(params.getPrice());
            sb.append(params.getReallyPrice());
            sb.append(appSecret);
            return md5(sb.toString());
        }
        return "";
    }

    //生成下单签名sign
    private static String generateOrderSign(PayParamsVO params, String appId, String appSecret) {
        if (!StringUtils.isEmpty(appId) && !StringUtils.isEmpty(appSecret)) {
            StringBuilder sb = new StringBuilder();
            sb.append(appId);
            sb.append(params.getPayId());
            sb.append(params.getParam());
            sb.append(params.getType());
            sb.append(params.getPrice());
            sb.append(appSecret);
            return md5(sb.toString());
        }
        return "";
    }

    // md5加密
    private static String md5(String data) {
        String ret = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                temp = temp.toLowerCase();
                if (temp.length() == 1) {
                    sb.append("0");
                }
                sb.append(temp);
            }

            ret = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
