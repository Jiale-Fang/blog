package pers.fjl.server.chatroom;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultMessage {
    private boolean isSystem;   //系统消息
    private String fromName;
    private Object message;
}
