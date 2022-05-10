package pers.fjl.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pers.fjl.common.utils.JsonLongSerializer;

import java.time.LocalDateTime;

/**
 * 聊天语音
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "音频")
public class VoiceVO {

    /**
     * 消息类型
     */
    @ApiModelProperty(name = "textType", value = "消息类型", required = true, dataType = "Integer")
    private Integer textType;

    /**
     * 文件
     */
    @ApiModelProperty(name = "file", value = "文件", required = true, dataType = "MultipartFile")
    private MultipartFile file;

    /**
     * 发送者id
     */
    @JsonSerialize(using = JsonLongSerializer.class )
    @ApiModelProperty(name = "sender", value = "发送者", required = true, dataType = "Long")
    private Long sender;


    /**
     * 用户id
     */
    @JsonSerialize(using = JsonLongSerializer.class )
    @ApiModelProperty(name = "receiver", value = "发送者", required = true, dataType = "Long")
    private Long receiver;

    private String toName;

    private String fromName;

    /**
     * 聊天内容
     */
    @ApiModelProperty(name = "content", value = "聊天内容", required = true, dataType = "String")
    private String content;


}
