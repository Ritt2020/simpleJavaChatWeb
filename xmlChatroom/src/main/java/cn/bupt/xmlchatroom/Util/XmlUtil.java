package cn.bupt.xmlchatroom.Util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import cn.bupt.xmlchatroom.Object.*;

/**
 * XmlUtil工具类
 */
public class XmlUtil {
    //用户列表转换为XML
    public Document writeUsersToXml(List<User> users) {
        // 创建一个XML文档对象
        Document document = DocumentHelper.createDocument();
        // 添加根节点
        Element root = document.addElement("users");
        // 遍历用户列表，将每个用户转换为XML格式并添加到根节点下
        for (User user : users) {
            Element userElement = root.addElement("user");
            userElement.addElement("id").setText(String.valueOf(user.getId()));
            userElement.addElement("nickname").setText(user.getNickname());
            userElement.addElement("password").setText(user.getPassword());
        }
       return document;
    }
    //消息列表转换为XML
    public Document writeMessagesToXml(List<Message> messages) {
        // 创建一个XML文档对象
        Document document = DocumentHelper.createDocument();
        // 添加根节点
        Element root = document.addElement("messages");
        // 遍历消息列表，将每个消息转换为XML格式并添加到根节点下
        for (Message message : messages) {
            Element messageElement = root.addElement("message");
            messageElement.addElement("id").setText(String.valueOf(message.getId()));
            messageElement.addElement("senderId").setText(String.valueOf(message.getSenderId()));
            messageElement.addElement("receiverId").setText(String.valueOf(message.getReceiverId()));
            messageElement.addElement("senderName").setText(message.getSenderName());
            messageElement.addElement("receiverName").setText(message.getReceiverName());
            messageElement.addElement("timestamp").setText(String.valueOf(message.getTimestamp().getTime()));
            messageElement.addElement("content").setText(message.getContent());
            messageElement.addElement("isPrivate").setText(message.isPrivate()?"true":"false");
            messageElement.addElement("emoji").setText(message.getEmoji());
            messageElement.addElement("color").setText(message.getColor());
        }
        return document;
    }
    //消息列表写入文件
    public void writeMessagesToXmlFile(List<Message> messages, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 指定编码格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            // 指定编码格式为 UTF-8
            Charset charset = StandardCharsets.UTF_8;
            // 创建一个Writer对象，用于写入XML文件
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);
            // 创建一个XMLWriter对象，指定输出格式和文件路径
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 写入XML文档并关闭流
            xmlWriter.write(writeMessagesToXml(messages));
            xmlWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //用户列表写入文件
    public void writeUsersToXmlFile(List<User> users, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 指定编码格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            // 指定编码格式为 UTF-8
            Charset charset = StandardCharsets.UTF_8;
            // 创建一个Writer对象，用于写入XML文件
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);
            // 创建一个XMLWriter对象，指定输出格式和文件路径
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 写入XML文档并关闭流
            xmlWriter.write(writeUsersToXml(users));
            xmlWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //用户列表读取
    public List<User> readUsersFromXml(Document document) {
        List<User> users = new ArrayList<>();
        // 获取根节点
        Element root = document.getRootElement();
        // 获取所有用户节点
        List<Element> userElements = root.elements("user");
        // 遍历用户节点，解析为User对象并添加到用户列表中
        for (Element userElement : userElements) {
            int id = Integer.parseInt(userElement.elementText("id"));
            String nickname = userElement.elementText("nickname");
            String password = userElement.elementText("password");
            User user = new User(id, nickname, password);
            users.add(user);
        }
        return users;
    }
    //消息列表读取
    public List<Message> readMessagesFromXml(Document document) {
        List<Message> messages = new ArrayList<>();
        // 获取根节点
        Element root = document.getRootElement();
        // 获取所有消息节点
        List<Element> messageElements = root.elements("message");
        // 遍历消息节点，解析为Message对象并添加到消息列表中
        for (Element messageElement : messageElements) {
            long id = Long.parseLong(messageElement.elementText("id"));
            int senderId = Integer.parseInt(messageElement.elementText("senderId"));
            int receiverId = Integer.parseInt(messageElement.elementText("receiverId"));
            String senderName = messageElement.elementText("senderName");
            String receiverName = messageElement.elementText("receiverName");
            Date timestamp = new Date(Long.parseLong(messageElement.elementText("timestamp")));
            String content = messageElement.elementText("content");
            boolean isPrivate = Boolean.parseBoolean(messageElement.elementText("isPrivate"));
            String emoji = messageElement.elementText("emoji");
            String color = messageElement.elementText("color");
            Message message = new Message(id, senderId,senderName,receiverName, receiverId, timestamp, content, isPrivate, emoji, color);
            messages.add(message);
        }
        return messages;
    }
    //用户列表文件读取
    public List<User> readUsersFromXmlFile(String filePath) {
        try {
            // 创建一个SAXReader对象
            SAXReader reader = new SAXReader();
            // 读取XML文件到Document对象
            File file = new File(filePath);
            if(!file.exists()){
                return new ArrayList<>();
            }
            // 使用UTF-8字符集创建InputStreamReader
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            // 使用InputStreamReader读取文件
            Document document = reader.read(inputStreamReader);
            return readUsersFromXml(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //消息列表文件读取
    public List<Message> readMessagesFromXmlFile(String filePath){
        try {
            // 创建一个SAXReader对象
            SAXReader reader = new SAXReader();
            // 读取XML文件到Document对象
            File file = new File(filePath);
            if(!file.exists()){
                return new ArrayList<>();
            }
            // 使用UTF-8字符集创建InputStreamReader
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            // 使用InputStreamReader读取文件
            Document document = reader.read(inputStreamReader);
            return readMessagesFromXml(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

