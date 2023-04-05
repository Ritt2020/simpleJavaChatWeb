package cn.bupt.xmlchatroom.Util;

import cn.bupt.xmlchatroom.Object.User;

import java.util.List;

/**
 * 用户工具类
 */
public class UserUtil {
    //用户昵称查询ID
    public static int getUserIdFromName(List<User> userList, String name){
        for(User user:userList){
            if(user.getNickname().equals(name)){
                return user.getId();
            }
        }
        return 0;
    }
}
