package com.studyplatform.client.studyplatformclient.model;

public class Activity {
    private String action;
    private String time;

    public Activity() {
    }

    public Activity(String action, String time) {
        this.action = action;
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}