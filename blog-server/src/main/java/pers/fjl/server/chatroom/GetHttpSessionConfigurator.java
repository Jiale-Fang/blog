package pers.fjl.server.chatroom;


import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if (httpSession != null) {
            String username = (String) httpSession.getAttribute("username");
            System.out.println("test" + username);
            //将该httpsession对象存到配置对象里
            sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
        } else {
            System.out.println("modifyHandshake 获取到null session");
        }
    }
}
