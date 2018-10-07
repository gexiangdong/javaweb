package cn.devmgr.javaweb.websocket;


import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * 配置
 */
public class ChatServerEndPointConfigurator extends Configurator {

    private ChatServerEndPoint chatServer = new ChatServerEndPoint();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass)
            throws InstantiationException {
        return (T)chatServer;
    }
}