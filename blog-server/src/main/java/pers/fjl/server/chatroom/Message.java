package pers.fjl.server.chatroom;

import lombok.Data;

/**
 * DTO类，用来存放聊天的消息（服务端接收的message实体）
 *
 */
@Data
public class Message {
	private String toName;
	private String message;
	private Integer mesType;  //1：系统消息；2：文本消息；4：发在私聊的图片消息；5：发在群聊的图片消息
}
