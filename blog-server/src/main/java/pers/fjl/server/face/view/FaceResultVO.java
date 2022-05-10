package pers.fjl.server.face.view;

import lombok.Data;

import java.util.List;

/**
 * 类说明： 人脸识别用户结果
 */
@Data
public class FaceResultVO {
    // (value = "人脸图片的唯一标识")
    private String faceToken;
    // (value = "用户结果集")
    private List<FaceUserInfoVO> userList;
}
