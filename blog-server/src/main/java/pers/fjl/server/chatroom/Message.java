package pers.fjl.server.chatroom;

import lombok.Data;

/**
 * DTO类，用来存放聊天的消息
 * @author BoBo
 *
 */
@Data
public class Message {
	private String toName;
	private String message;
}
