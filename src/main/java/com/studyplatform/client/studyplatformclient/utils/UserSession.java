package com.studyplatform.client.studyplatformclient.utils;

import com.studyplatform.client.studyplatformclient.model.User;

public class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static void setSession(User user) {
        instance = new UserSession(user);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public static void cleanUserSession() {
        instance = null;
    }
}