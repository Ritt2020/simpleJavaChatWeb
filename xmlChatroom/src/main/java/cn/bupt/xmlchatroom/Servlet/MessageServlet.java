package cn.bupt.xmlchatroom.Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 添加消息API
 */
@WebServlet(name = "messageServlet", value = "/sendMessage")
public class MessageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求参数
        String toUser = request.getParameter("userSelect");
        String content = request.getParameter("content");
        String color = request.getParameter("color");
        boolean isPrivate = Boolean.parseBoolean(request.getParameter("isPrivate"));
        String emoji = request.getParameter("emoji");

        System.out.println(toUser);
        System.out.println(content);
        System.out.println(color);
        System.out.println(isPrivate);
        System.out.println(emoji);
    }
}
