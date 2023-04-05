package cn.bupt.xmlchatroom.Object;

/**
 * 用户对象
 */
public class User {
    private final int id;
    private String nickname;
    private String password;

    private boolean isOnline;

    public User(String nickname, String password) {
        this.id = (int) (System.currentTimeMillis() / 1000); // 生成当前时间的时间戳作为用户ID
        this.nickname = nickname;
        this.password = password;
        this.isOnline = false;
    }

    public User(int id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.isOnline = false;
    }

    public int getId() {
        return id;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

