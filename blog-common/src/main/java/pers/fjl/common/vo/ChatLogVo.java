package pers.fjl.common.vo;

import lombok.Data;
import pers.fjl.common.po.ChatLog;

import java.io.Serializable;

@Data
public class ChatLogVo extends ChatLog implements Serializable{
    private Integer type;
}
