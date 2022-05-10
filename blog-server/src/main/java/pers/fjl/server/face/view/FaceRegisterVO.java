package pers.fjl.server.face.view;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 人脸注册
 */
@Data
public class FaceRegisterVO {

    /**
     * code
     */
    @NotBlank(message = "人脸图片url不能为空")
    @ApiModelProperty(name = "faceImgUrl", value = "faceImgUrl", required = true, dataType = "String")
    private String faceImgUrl;
}
