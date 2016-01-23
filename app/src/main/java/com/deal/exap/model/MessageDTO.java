package com.deal.exap.model;

import java.io.Serializable;

/**
 * Created by YusataInfotech on 1/22/2016.
 */
public class MessageDTO implements Serializable {
    private String message;
    private String user_id;
    private String timestamp;
    private String userImg;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return userImg;
    }

    public void setImage(String userImg) {
        this.userImg = userImg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
