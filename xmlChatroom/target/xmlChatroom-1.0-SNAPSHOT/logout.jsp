<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // 清除所有session中的内容
  session.invalidate();

  // 重定向到login.jsp
  response.sendRedirect("login.jsp");
%>
