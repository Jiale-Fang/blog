package pers.fjl.server.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.GroupChatVo;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.MessageUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/{username}", configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class ChatEndpoint {

    //用map管理endpoint对象
    private static Map<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    //声明session对象，通过该对象可以发送消息给指定用户
    private Session session;

    //声明HttpSession,该对象存了用户名
    private HttpSession httpSession;

    private String username;

    @Resource
    private UserService userServiceAuto;

    private static UserService userService;

    @PostConstruct
    public void init() {
        userService = this.userServiceAuto;  //将注入的对象交给静态对象管理
    }

    /**
     * 连接建立时被调用
     *
     * @param session
     * @param config
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session, EndpointConfig config) {
        System.out.println("开启连接");
        this.session = session;
        //获取httpSession对象
//        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//        this.httpSession = httpSession;
//        String username = (String) httpSession.getAttribute("username");
        System.out.println("--onopen--" + username);
        this.username = username;
        //将当前对象存到容器里
        onlineUsers.put(username, this);

        //将当前在线用户的用户名推送给所以客户端
        //1.获取消息
        String message = MessageUtils.getMessage(true, "online", this.username);
        //2.调用方法进行系统消息推送
        broadcastAllUsers(message);
    }

    private void broadcastAllUsers(String message) {
        try {
            // 将消息推送给所有客户端
            Set<String> username = onlineUsers.keySet();
            for (String name : username) {
                ChatEndpoint chatEndpoint = onlineUsers.get(name);
                chatEndpoint.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            System.out.println("出现异常");
            e.printStackTrace();
        }
    }

    private Set<String> getNames() {
        return onlineUsers.keySet();
    }

    /**
     * 收到客户端发来消息
     *
     * @param message 消息对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        log.info("服务端收到客户端发来的消息: {}", message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message mess = mapper.readValue(message, Message.class);
            if (!mess.getToName().equals("group")) {  // 代表不是群聊
                // 获取要发送的用户的用户名
                String toName = mess.getToName();
                String data = mess.getMessage();
                // 发送给客户端的message
                String messageToSend = MessageUtils.getMessage(false, this.username, data);
                onlineUsers.get(toName).session.getBasicRemote().sendText(messageToSend);
            } else {
                // 调用service查出该用户的昵称和头像
                User user = userService.selectByUsername(this.username);
                GroupChatVo groupChatVo = new GroupChatVo();
                BeanUtils.copyProperties(user, groupChatVo);
                System.out.println("----------群聊消息-----------"+user);
                System.out.println("----------群聊消息-----------"+groupChatVo);
                groupChatVo.setContent(mess.getMessage());
                groupChatVo.setType(3);
                String groupMess = MessageUtils.getMessage(true, null, groupChatVo);
                //调用方法进行群聊消息推送
                broadcastAllUsers(groupMess);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 链接关闭时调用
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        log.info("有用户断开了, id为:{}", session.getId());
        //从容器追踪删除指定用户
        onlineUsers.remove(username);
        System.out.println(username);
        //1.获取消息
        String message = MessageUtils.getMessage(true, "offline", username);
        //2.调用方法进行系统消息推送
        broadcastAllUsers(message);
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

}
