package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.entity.Result;
import pers.fjl.common.vo.NotifyParamsVO;
import pers.fjl.common.vo.PayParamsVO;
import pers.fjl.server.service.GoGoPayService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Api("支付接口")
@RestController
@CrossOrigin
@RequestMapping("/pay")
public class GoGoPayController {

    @Resource
    private GoGoPayService goGoPayService;

    // 支付成功后，GOGO支付会通知开发者设置的notifyUrl
    @GetMapping("/weChatPay/{uid}")
    public void weChatPay(@PathVariable("uid") String uid ,HttpServletResponse response) {
        goGoPayService.pay(uid, response);
    }


    // 支付成功后，GOGO支付会通知开发者设置的notifyUrl
    @PostMapping("/notifyUrl")
    public String notifyUrl(@RequestBody NotifyParamsVO notifyParamsVO, HttpServletResponse response) {
        return goGoPayService.callBackNotify(notifyParamsVO, response);
    }

}
