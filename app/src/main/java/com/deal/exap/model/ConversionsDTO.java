package com.deal.exap.model;


import java.io.Serializable;

public class ConversionsDTO implements Serializable {

    private String message;
    private String user;
    private String user_ara;
    private String user_id;
    private String image;
    private String timestamp;
    private String unread;

    public String getUser_ara() {
        return user_ara;
    }

    public void setUser_ara(String user_ara) {
        this.user_ara = user_ara;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }
}
