package pers.fjl.common.vo;

import lombok.Data;

@Data
public class PayParamsVO {
    private String price; //必填
    private String type; //必填
    private String payId; //必填
    private String sign; //必填
    private String param; //选填
    private String notifyUrl; //选填
    private String returnUrl; //选填
    private Integer isHtml; //选填
}
