package com.studyplatform.client.studyplatformclient.utils;

import com.studyplatform.client.studyplatformclient.model.User;
import java.util.Random;

public class UserSession {
    private static UserSession instance;
    private User user;
    private String avatarPath;

    private UserSession(User user) {
        this.user = user;
        this.avatarPath = getRandomAvatar();
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

    public String getAvatarPath() {
        return avatarPath;
    }

    public static void cleanUserSession() {
        instance = null;
    }

    private String getRandomAvatar() {
        String[] avatars = {
                "/images/dino111.png",
                "/images/dino222.png",
                "/images/paw.png",
                "/images/paw_yellow.png",
                "/images/paw_red.png",
                "/images/paw_blue.png"
        };
        return avatars[new Random().nextInt(avatars.length)];
    }
}