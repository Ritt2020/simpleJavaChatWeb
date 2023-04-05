package cn.bupt.xmlchatroom.Object;

import java.util.Date;

/**
 * 消息对象
 */
public class Message {
    private final long id;
    private int senderId;
    private int receiverId;
    private final String senderName;
    private final String receiverName;

    private final boolean isPrivate;

    private final String emoji;
    private final Date timestamp;
    private String content;

    private final String color;

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getEmoji() {
        return emoji;
    }

    public Message(int senderId, int receiverId, String senderName, String receiverName, String content) {
        this.id = System.currentTimeMillis(); // 生成当前时间的时间戳作为消息ID
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName =senderName;
        this.receiverName = receiverName;
        this.timestamp = new Date();
        this.content = content;
        this.emoji = "";
        this.isPrivate = false;
        this.color = "default";
    }

    public Message(int senderId, int receiverId,String senderName,String receiverName, String content, boolean isPrivate, String emoji, String color) {
        this.id = System.currentTimeMillis(); // 生成当前时间的时间戳作为消息ID
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName =senderName;
        this.receiverName = receiverName;
        this.timestamp = new Date();
        this.content = content;
        this.emoji = emoji;
        this.isPrivate = isPrivate;
        this.color = color;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getColor() {
        return color;
    }

    public Message(long id, int senderId, String senderName, String receiverName, int receiverId, Date timestamp, String content, boolean isPrivate, String emoji, String color) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName =senderName;
        this.receiverName = receiverName;
        this.timestamp = timestamp;
        this.content = content;
        this.isPrivate = isPrivate;
        this.emoji = emoji;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

