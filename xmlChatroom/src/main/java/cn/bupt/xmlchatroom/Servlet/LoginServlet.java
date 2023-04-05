package cn.bupt.xmlchatroom.Servlet;

import cn.bupt.xmlchatroom.Object.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cn.bupt.xmlchatroom.*;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * 登录后端API
 */
@WebServlet(name = "loginServlet", value = "/doLogin")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取请求参数
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        // 遍历用户列表，查找是否存在对应用户
        boolean userExists = false;
        for (User user : Chatroom.users)
            if (user.getNickname().equals(nickname) && user.getPassword().equals(password)) {
                userExists = true;
                saveUserInfoToSession(request, user);
                break;
            }

        // 根据查找结果返回对应的状态码
        if (userExists) {
            response.setStatus(HttpServletResponse.SC_OK); // 返回状态码 200
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 返回状态码 400
        }
    }
    private void saveUserInfoToSession(HttpServletRequest request, User user){
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("nickname", user.getNickname());
    }
}
