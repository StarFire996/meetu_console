package com.meetu.threadlocal;

import com.meetu.community.domain.User;

public class UserThreadLocal {
	// 声明一个ThreadLocal
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

    // 把用户信息放到ThreadLocal
    public static void set(User user) {
        userThreadLocal.set(user);
    }

    // 从ThreadLocal中获取用户信息
    public static User get() {
        return userThreadLocal.get();
    }
}
