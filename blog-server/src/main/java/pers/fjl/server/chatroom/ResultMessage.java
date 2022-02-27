package pers.fjl.server.chatroom;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务端发送到客户端的message
 */
@Data
@Accessors(chain = true)
public class ResultMessage {
//    private boolean isSystem;   //系统消息
    private Integer mesType;     //1：系统消息；2：文本消息；4：发在私聊的图片消息；5：发在群聊的图片消息
    private String fromName;
    private Object message;
}
