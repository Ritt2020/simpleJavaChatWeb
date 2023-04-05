package cn.bupt.xmlchatroom;

import java.util.List;
import cn.bupt.xmlchatroom.Object.*;
import cn.bupt.xmlchatroom.Util.XmlUtil;

/**
 * Chatroom 保存一些配置和静态方法
 */
public class Chatroom {
    public static List<User> users;

    public static int visitCount;

    //初始化方法
    public static void init(){
        visitCount = 0;
        XmlUtil xmlUtil = new XmlUtil();
        users = xmlUtil.readUsersFromXmlFile(Config.user_file_path);
    }

    //配置
    public static class Config{
        //用户文件路径
        public static String user_file_path = "D:\\chatroom\\users.xml";
        //消息文件路径
        public static String message_file_path = "D:\\chatroom\\messages.xml";
    }

    //添加用户
    public static boolean addUser(User user){
        for(User u:users){
            if(u.getNickname().equals(user.getNickname())){
                return false;
            }
        }
        users.add(user);
        XmlUtil xmlUtil = new XmlUtil();
        xmlUtil.writeUsersToXmlFile(users, Config.user_file_path);
        return true;
    }

    //添加一条消息保存
    public static void addMessages(List<Message> messageList){
        XmlUtil xmlUtil = new XmlUtil();
        List<Message> messages = xmlUtil.readMessagesFromXmlFile(Config.message_file_path);
        messages.addAll(messageList);
        xmlUtil.writeMessagesToXmlFile(messages, Config.message_file_path);
    }
    // ID获取用户
    public static User getUserFromId(int id){
        for(User u:users){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }
    //添加1访问
    public static void incrementVisit(){
        visitCount = visitCount + 1;
    }
}
