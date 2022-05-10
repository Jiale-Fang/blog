package pers.fjl.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyParamsVO {
    private String payId;
    private String param;
    private String type;
    private String price;
    private String reallyPrice;
    private String sign;
}
