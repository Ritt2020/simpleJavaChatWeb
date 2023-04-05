package cn.bupt.xmlchatroom.WebSocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;


/**
 * WebSocket配置
 */
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator{
    //修改HttpSession注入
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put(HttpSession.class.getName(), httpSession);
        super.modifyHandshake(config, request, response);
    }

    @Override
    public boolean checkOrigin(String originHeaderValue) {
        //允许一切跨域访问
        return true;
    }
}
