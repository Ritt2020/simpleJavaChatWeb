package cn.bupt.xmlchatroom.Servlet;

import cn.bupt.xmlchatroom.Chatroom;
import cn.bupt.xmlchatroom.Object.User;
import cn.bupt.xmlchatroom.Util.XmlUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取在线用户API
 */
@WebServlet(name = "userStateServlet", value = "/getUsers")
public class UserStateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        List<User> onlineUsers = new ArrayList<>();
        for(User u:Chatroom.users){
            if(u.isOnline())
                onlineUsers.add(u);
        }
        XmlUtil xmlUtil = new XmlUtil();
        out.write(xmlUtil.writeUsersToXml(onlineUsers).asXML());
        out.close();
    }
}
