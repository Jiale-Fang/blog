package pers.fjl.common.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fjl.common.po.ChatLog;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChatLogVo extends ChatLog implements Serializable{
    private Integer type;

    /**
     * 转换后的时间
     */
    private String formatTime;
}
