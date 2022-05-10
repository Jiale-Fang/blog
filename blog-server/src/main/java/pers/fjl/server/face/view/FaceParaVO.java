package pers.fjl.server.face.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.List;

/**
 * 类说明： 人脸识别参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceParaVO {

    private String userId;

    private String userInfo;

    private String groupId;

    private String faceImgUrl;

}
