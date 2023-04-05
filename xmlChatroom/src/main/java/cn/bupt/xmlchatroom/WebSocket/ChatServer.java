package cn.bupt.xmlchatroom.WebSocket;

import cn.bupt.xmlchatroom.Chatroom;
import cn.bupt.xmlchatroom.Object.Message;
import cn.bupt.xmlchatroom.Object.User;
import cn.bupt.xmlchatroom.Util.UserUtil;
import cn.bupt.xmlchatroom.Util.XmlUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * WebSocket 服务器端点，配置GetHttpSessionConfigurator用于获取HttpSession内容
 */
@ServerEndpoint(value="/websocket/{userId}", configurator = GetHttpSessionConfigurator.class)
public class ChatServer {
    //用于处理定时任务
    private ScheduledFuture<?> scheduledFuture;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private Session session;
    private User user;

    // 存储在线客户端的 Map
    public static ConcurrentHashMap<Integer, ChatServer> clients = new ConcurrentHashMap<>();


    // 当 WebSocket 连接成功时调用此方法
    @OnOpen
    public void onOpen(@PathParam("userId") int id , Session session, EndpointConfig config){
        this.session = session;
        // 获取当前用户的session
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        // 获得当前用户信息
        int userId = (int) httpSession.getAttribute("userId");
        if(userId != id){//比对信息
            onClose();
        }else{
            // 将当前用户存到在线用户列表中
            clients.put(id, this);
            // 如果用户已存在，设置在线状态并发送加入聊天室的消息
            if(Chatroom.getUserFromId(id) != null){
                this.user = Chatroom.getUserFromId(id);
                assert this.user != null;
                this.user.setOnline(true);
                 //发送消息通知
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(-1, -1,"管理员","大家", "***"+this.user.getNickname() + "***加入了聊天室"));
                Chatroom.addMessages(messages);
                GroupSending(messages);
            }else{
                onClose();
            }
        }
        // 初始化计时器
        resetTimer();
    }

    // 当 WebSocket 连接关闭时调用此方法
    @OnClose
    public void onClose(){
        // 移除的用户信息
        this.user.setOnline(false);
        //发送消息通知
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(-1, -1,"管理员","大家", "***"+this.user.getNickname() + "***离开了聊天室"));
        Chatroom.addMessages(messages);
        // 通知除了自己的所有在线用户，当前用户下线
        GroupSendingOthers(messages);
        clients.remove(this.user.getId());
    }

    //接收客户端的message，判断是否有接收人而选择进行广播还是指定发送
    @OnMessage
    public void onMessage(String message){
        //读取XML
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new StringReader(message));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootElement = document.getRootElement();
        // 读取各个子元素的值并保存到变量中
        String userSelect = rootElement.element("userSelect").getText();
        String content = rootElement.element("content").getText();
        String color = rootElement.element("color").getText();
        boolean isPrivate = Boolean.parseBoolean(rootElement.element("isPrivate").getText());
        String emoji = rootElement.element("emoji").getText();
        int receiverId;
        //处理消息，格式化为Message格式
        if(userSelect.equals("大家"))
            receiverId = -1;
        else
            receiverId = UserUtil.getUserIdFromName(Chatroom.users, userSelect);
        Message messageToSend = new Message(this.user.getId(), receiverId, this.user.getNickname(), userSelect, content, isPrivate, emoji, color);
        List<Message> messages = new ArrayList<>();
        messages.add(messageToSend);
        Chatroom.addMessages(messages);
        if(!isPrivate){//发送给所有人
            GroupSending(messages);
        }else{
            appointSending(receiverId, messages);
            appointSending(this.user.getId(), messages);
        }
        // 重置定时器
        resetTimer();
    }

    //连接出错
    @OnError
    public void onError(@PathParam("userId") String userid,Session session,Throwable throwable){
    }

    //群发
    public void GroupSending(List<Message> messages) {
        XmlUtil xmlUtil = new XmlUtil();
        for (Integer id : clients.keySet()) {
            try {
                clients.get(id).session.getBasicRemote().sendText(xmlUtil.writeMessagesToXml(messages).asXML());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //群发（自己除外）
    public void GroupSendingOthers(List<Message> messages) {
        XmlUtil xmlUtil = new XmlUtil();
        for (Integer id : clients.keySet()) {
            if(id == this.user.getId())
                continue;
            try {
                clients.get(id).session.getBasicRemote().sendText(xmlUtil.writeMessagesToXml(messages).asXML());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //发送给指定用户
    public static void appointSending(int id, List<Message> messages) {
        try {
            XmlUtil xmlUtil = new XmlUtil();
            clients.get(id).session.getBasicRemote().sendText(xmlUtil.writeMessagesToXml(messages).asXML());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //重置定时
    private void resetTimer() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        scheduledFuture = executorService.schedule(() -> {
            try {
                session.close();
                clients.remove(this.user.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 10, TimeUnit.MINUTES);
    }

}
