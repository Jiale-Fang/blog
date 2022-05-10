package pers.fjl.server.face.view;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类说明： 人脸识别用户信息
 */
@Data
public class FaceUserInfoVO{

    private String userId;

    private String userInfo;

    // (value = "匹配度")
    private Double score;
}
