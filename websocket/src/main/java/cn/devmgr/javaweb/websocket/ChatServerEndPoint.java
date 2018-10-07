package cn.devmgr.javaweb.websocket;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * 很简单的一个websocket例子，最简单的聊天室（没有登录）
 */
@ServerEndpoint(value="/chat", configurator=ChatServerEndPointConfigurator.class)
public class ChatServerEndPoint {

    /**
     * 存放了所有当前连接的session
     */
    private Set<Session> userSessions = Collections.synchronizedSet(new HashSet<Session>());

    /**
     * 每个websocket client连接进来时会调用的事件
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        userSessions.add(userSession);
        broadcastMessage("{\"user\": \"system\", \"message\": \"欢迎光临. " + userSession.getId() + "\"}");
    }

    /**
     * 每个websocket clent断开时会调用的事件
     * @param userSession the userSession which is opened.
     */
    @OnClose
    public void onClose(Session userSession) {
        userSessions.remove(userSession);

        broadcastMessage("{\"user\": \"system\", \"message\": \"再见 " + userSession.getId() + "\"}");
    }

    /**
     * 收到client发过了的消息时的事件
     * @param message The text message
     * @param userSession The session of the client
     */
    @OnMessage
    public void onMessage(String message, Session userSession) {
        System.out.println("Message Received: " + message);
        for (Session session : userSessions) {
            System.out.println("Sending to " + session.getId());
            session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 这个方法给每个连接的客户端都发送一个消息
     * @param message
     */
    private void broadcastMessage(String message){
        for (Session session : userSessions) {
            //System.out.println("Sending to " + session.getId());
            session.getAsyncRemote().sendText(message);
        }
    }
}
