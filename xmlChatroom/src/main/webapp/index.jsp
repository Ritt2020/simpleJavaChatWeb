<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    HttpSession httpSession = request.getSession(false);
    if (httpSession == null || httpSession.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>XML聊天室</title>
    <script src="https://cdn.staticfile.org/jquery/3.5.1/jquery.js"></script>
    <style>
        body {
            margin: 0;
            padding: 0;
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        main {
            flex-grow: 1;
            display: flex;
            flex-direction: row;
            height: 100%;
        }

        .chat-box {
            flex-grow: 1;
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        .message-container {
            flex-grow: 1;
            overflow: auto;
            padding: 1rem;
        }

        .message {
            margin-bottom: 0.5rem;
        }

        .user-input {
            display: flex;
            flex-direction: row;
            align-items: center;
            padding: 0.5rem;
            background-color: #e1e1e1;
        }

        .user-input input[type="text"] {
            flex-grow: 8;
            margin-right: 0.5rem;
            padding: 0.5rem;
            font-size: 1.1rem;
        }

        .user-input input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 0.5rem;
            cursor: pointer;
            font-size: 1.1rem;
        }

        .user-list {
            width: 10%;
            height: 100%;
            background-color: #e1e1e1;
            overflow: auto;
            padding: 1rem;
        }

        .user-list ul {
            list-style: none;
            padding: 0;
        }

        .user-list li {
            margin-bottom: 0.5rem;
        }
        #user-select {
            flex-grow: 1;
            margin-right: 0.5rem;
            padding: 0.5rem;
            font-size: 1.1rem;
        }
    </style>
</head>
<body onload="getUserList();setInterval(getUserList,1000)">
<main>
    <div class="chat-box">
        <div class="message-container" style="height: 800px; overflow-y: scroll;">
        </div>
        <div class="user-input">
            <%=session.getAttribute("nickname")%> 对：
            <select name="user-select" id="user-select">
                <option value="大家">大家</option>
            </select>
            说
            <input type="text" placeholder="">
            <input type="submit" onclick="sendMyMessage()">
        </div>
        <div class="user-input">
            喜欢的色彩：
            <select name="color" id="color">
                <option value="default">默认</option>
                <option value="red">红色</option>
                <option value="blue">蓝色</option>
                <option value="yellow">黄色</option>
            </select>&nbsp;&nbsp;&nbsp;&nbsp;
            悄悄话：
            <input name="private" id="private" type="checkbox" value="" />&nbsp;&nbsp;&nbsp;
            表情：
            <select name="emoji" id="emoji">
                <option value="">无</option>
                <option value="微笑着">微笑</option>
                <option value="愤怒着">愤怒</option>
                <option value="高兴着">高兴</option>
            </select>&nbsp;&nbsp;&nbsp;&nbsp;
            <button id="logoutButton">离开聊天室</button>
        </div>
    </div>
    <div class="user-list" style="height: 800px; overflow-y: scroll;">
        <p>在线列表</p>
        <ul>
        </ul>
    </div>
</main>
<script>
    let socket;
    let previousUserList = [];
    let nickname = '<%=session.getAttribute("nickname")%>';
    $(document).ready(function () {
        openSocket();
    });
    function openSocket() {
        // 获取当前URL
        const currentUrl = window.location.host;
        // 将http或https替换为相应的WebSocket协议
        const wsProtocol = currentUrl.startsWith("https") ? "wss" : "ws";
        // 获取用户ID
        const userId = <%=session.getAttribute("userId")%>;
        // 构建WebSocket URL
        const socketUrl = wsProtocol+'://'+currentUrl+'/websocket/' + userId;
        console.log(socketUrl);
        if(socket!=null){
            socket.close();
            socket=null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function() {
            console.log("websocket已打开");
        };
        // 接收到消息时触发
        socket.onmessage = function(event) {
            // 从消息中提取文本内容
            let xml = event.data;
            let parser = new DOMParser();
            let xmlDoc = parser.parseFromString(xml, 'text/xml');
            let content = xmlDoc.getElementsByTagName('content')[0].textContent;
            let senderName =  xmlDoc.getElementsByTagName('senderName')[0].textContent;
            let receiverName =  xmlDoc.getElementsByTagName('receiverName')[0].textContent;
            let isPrivate =  xmlDoc.getElementsByTagName('isPrivate')[0].textContent;
            let emoji =  xmlDoc.getElementsByTagName('emoji')[0].textContent;
            let color =  xmlDoc.getElementsByTagName('color')[0].textContent;
            // 创建新的消息元素
            let messageElem = document.createElement('div');
            messageElem.classList.add('message');
            let contentElem = document.createElement('p');
            if(receiverName == nickname){
                receiverName = "我";
            }
            if(senderName == nickname){
                senderName = "我";
            }
            let privateStr = "";
            if(isPrivate == true){
                privateStr = "悄悄地";
            }
            contentElem.textContent = senderName + privateStr + emoji + "对" + receiverName + "说：" + content;
            // 根据color值设置contentElem元素的颜色
            if(color === 'default'){
            } else if (color === 'red') {
                contentElem.style.color = 'red';
            } else if (color === 'blue') {
                contentElem.style.color = 'blue';
            }else if (color === 'yellow') {
                contentElem.style.color = 'yellow';
            }
            messageElem.appendChild(contentElem);
            // 获取消息容器并添加新的消息元素
            let containerElem = document.querySelector('.message-container');
            containerElem.appendChild(messageElem);
            // 将滚动条位置设置为容器的滚动高度，以保持滚动条始终在底部
            containerElem.scrollTop = containerElem.scrollHeight;
        };
        //关闭事件
        socket.onclose = function() {
            alert("可能是您关闭了窗口或者十分钟无活动，您被踢出聊天室");
            window.location.href = "/login.jsp";
        };
        //发生了错误事件
        socket.onerror = function() {
        }
    }
    function sendMessage(userSelect, content, color, isPrivate, emoji) {
        let str = '<message><userSelect>'+userSelect+'</userSelect><content>'+content+'</content><color>'+color+'</color><isPrivate>'+isPrivate+'</isPrivate><emoji>'+emoji+'</emoji></message>';
        socket.send(str);
    }
    function getUserList() {
        let xhr = new XMLHttpRequest();
        xhr.open('POST', '/getUsers');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onload = function() {
            if (xhr.status === 200) {
                let userList = xhr.responseXML.getElementsByTagName('user');
                let userListContainer = document.querySelector('.user-list ul');
                let newUserList = [];
                for (let i = 0; i < userList.length; i++) {
                    let user = userList[i];
                    let username = user.getElementsByTagName('nickname')[0].textContent;
                    newUserList.push(username);
                }
                if (JSON.stringify(newUserList) !== JSON.stringify(previousUserList)) {
                    userListContainer.innerHTML = '';
                    let selectBox = document.getElementById('user-select');
                    selectBox.innerHTML = '<option value="大家">大家</option>'; // 清空下拉框选项列表
                    for (let i = 0; i < userList.length; i++) {
                        let user = userList[i];
                        let username = user.getElementsByTagName('nickname')[0].textContent;
                        let listItem = document.createElement('li');
                        listItem.textContent = username;
                        userListContainer.appendChild(listItem);
                        if(username !== nickname){
                            // 向下拉框选项列表添加新选项
                            let optionItem = document.createElement('option');
                            optionItem.value = username;
                            optionItem.textContent = username;
                            selectBox.appendChild(optionItem);
                        }
                        previousUserList = newUserList;
                    }
                }
            }
        };
        xhr.send();
    }

    //发送消息
    function sendMyMessage() {
        const userSelect = document.querySelector('#user-select').value;
        const content = document.querySelector('.user-input input[type="text"]').value;
        const color = document.querySelector('#color').value;
        const isPrivate = document.querySelector('input[name="private"]').checked;
        const emoji = document.querySelector('#emoji').value;
        sendMessage(userSelect, content, color, isPrivate, emoji);
    }

    // 获取相关元素
    const userSelect = document.getElementById('user-select');
    const privateCheckbox = document.getElementById('private');

    // 为 user-select 添加 change 事件监听器
    userSelect.addEventListener('change', function() {
        // 如果选中的值为"大家"，禁用悄悄话复选框
        if (userSelect.value === '大家') {
            privateCheckbox.disabled = true;
            privateCheckbox.checked = false;
        } else {
            // 否则，启用悄悄话复选框
            privateCheckbox.disabled = false;
        }
    });

    // 初始化悄悄话复选框的状态
    if (userSelect.value === '大家') {
        privateCheckbox.disabled = true;
    }

    // 获取按钮元素
    const logoutButton = document.getElementById('logoutButton');
    // 为按钮添加点击事件监听器
    logoutButton.addEventListener('click', function() {
        // 弹出确认对话框，询问用户是否要离开聊天室
        const confirmLogout = confirm('您确定要离开聊天室吗？');

        // 如果用户点击了确认按钮
        if (confirmLogout) {
            // 跳转到logout.jsp
            window.location.href = 'logout.jsp';
        }
    });




</script>
</body>
</html>
