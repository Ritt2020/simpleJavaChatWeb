package cn.bupt.xmlchatroom;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * 初始化时读取用户信息
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 在 Web 应用启动时执行的代码
        Chatroom.init();
        System.out.println("Web 应用已启动，读取用户信息");
    }

}


