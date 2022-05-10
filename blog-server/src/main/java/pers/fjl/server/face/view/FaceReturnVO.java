package pers.fjl.server.face.view;

import lombok.Data;

/**
 * 类说明： 人脸识别返回结果对象
 */
@Data
public class FaceReturnVO {
    // (value = "执行结果代码")
    private String errorCode;
    // (value = "返回消息")
    private String errorMsg;
    // (value = "时间戳")
    private Double timestamp;
    // (value = "用户结果")
    private FaceResultVO result;
}
