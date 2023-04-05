<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>XML聊天室新用户注册</title>
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
            var nickname = document.forms["registration"]["nickname"].value;
            var password = document.forms["registration"]["password"].value;
            var confirm_password = document.forms["registration"]["confirm_password"].value;

            if (nickname === "") {
                alert("请输入昵称");
                return false;
            }
            if (password === "") {
                alert("请输入口令");
                return false;
            }
            if (confirm_password === "") {
                alert("请再次输入口令");
                return false;
            }
            if (password !== confirm_password) {
                alert("两次输入的口令不一致");
                return false;
            }
            // 创建XMLHttpRequest对象
            var xhr = new XMLHttpRequest();

            // 发送请求
            xhr.open("POST", "doRegister?nickname=" + nickname + "&password=" + password);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send();

            // 处理响应
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // 处理成功响应
                    alert("注册成功");
                    window.location.href = "/login.jsp";
                } else if (xhr.readyState === 4 && xhr.status !== 200) {
                    // 处理失败响应
                    alert("注册失败，可能是昵称已经占用");
                }
            };
            return false;
        }
    </script>
</head>
<body>
<h1>XML聊天室新用户注册</h1>
<hr>
<form name="registration" action="doRegister" method="post" onsubmit="return validateForm()">
    <label for="nickname">昵称:</label>
    <input type="text" id="nickname" name="nickname"><br>
    <label for="password">口令:</label>
    <input type="password" id="password" name="password"><br>
    <label for="confirm_password">口令:</label>
    <input type="password" id="confirm_password" name="confirm_password"><br>
    <input type="submit" value="用户注册" id="submit_button">
</form>
<a href="login.jsp">登录聊天室</a>

</body>
</html>

