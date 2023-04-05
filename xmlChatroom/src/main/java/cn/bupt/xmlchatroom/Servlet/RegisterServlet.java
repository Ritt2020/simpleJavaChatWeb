package cn.bupt.xmlchatroom.Servlet;

import cn.bupt.xmlchatroom.Chatroom;
import cn.bupt.xmlchatroom.Object.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import cn.bupt.xmlchatroom.Util.*;

/**
 * 注册API
 */
@WebServlet(name = "registerServlet", value = "/doRegister")
public class RegisterServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求参数
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        // 创建一个User对象
        User user = new User(nickname, password);

        // 添加到用户列表
        if (Chatroom.addUser(user)) {
            response.setStatus(HttpServletResponse.SC_OK); // 返回状态码 200
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 返回状态码 400
        }
    }

}
