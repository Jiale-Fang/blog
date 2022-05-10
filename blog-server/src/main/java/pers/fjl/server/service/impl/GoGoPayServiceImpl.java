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
    }

    // 接收GOGO支付完成的回调通知方法，在该函数中主要用于商户自己根据支付完成处理相应的数据逻辑
    public String callBackNotify(NotifyParamsVO notifyParamsVO, HttpServletResponse response) {

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
