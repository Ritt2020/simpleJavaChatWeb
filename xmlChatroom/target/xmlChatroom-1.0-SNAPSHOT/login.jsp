<%@ page import="cn.bupt.xmlchatroom.Util.XmlUtil" %>
<%@ page import="cn.bupt.xmlchatroom.Chatroom" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Chatroom.incrementVisit();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>欢迎光临XML聊天室</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-top: 50px;
        }
        h1 {
            text-align: center;
        }
        label {
            display: inline-block;
            width: 80px;
            text-align: right;
            margin-right: 10px;
        }
        input {
            display: inline-block;
            margin-bottom: 10px;
        }
        hr {
            border: 0;
            height: 1px;
            width: 100%;
            background-color: black;
            margin-bottom: 30px;
        }
        form {
            text-align: center;
        }
        #submit_button {
            margin-top: 20px;
            margin-bottom: 20px;
        }
    </style>
    <script>
        function validateForm() {
            var nickname = document.forms["login"]["nickname"].value;
            var password = document.forms["login"]["password"].value;

            if (nickname === "") {
                alert("请输入昵称");
                return false;
            }
            if (password === "") {
                alert("请输入口令");
                return false;
            }
            // 创建XMLHttpRequest对象
            var xhr = new XMLHttpRequest();

            // 发送请求
            xhr.open("POST", "doLogin");
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send("nickname=" + nickname + "&password=" + password);

            // 处理响应
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // 处理成功响应
                    alert("登录成功");
                    window.location.href = "/index.jsp";
                } else if (xhr.readyState === 4 && xhr.status !== 200) {
                    // 处理失败响应
                    alert("登录失败，请检查输入");
                }
            };
            return false;
        }
    </script>
</head>
<body>
<h1>欢迎光临XML聊天室</h1>
<hr>
<form name="login" action="doLogin" method="post" onsubmit="return validateForm()">
    <label for="nickname">昵称:</label>
    <input type="text" id="nickname" name="nickname"><br>
    <label for="password">口令:</label>
    <input type="password" id="password" name="password"><br>
    <input type="submit" value="登录" id="submit_button">
</form>
<p>新用户请选择<a href="register.jsp">注册新用户</a></p>
<p>你是聊天室的第 ${Chatroom.visitCount} 位访客</p>
</body>
</html>
