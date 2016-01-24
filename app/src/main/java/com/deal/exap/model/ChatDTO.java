package com.deal.exap.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mayur on 10-01-2016.
 */
public class ChatDTO implements Serializable {


    private Partner partner;

    private List<MessageDTO> messageList;

    private boolean status;

    private String message;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public List<MessageDTO> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageDTO> messageList) {
        this.messageList = messageList;
    }
}
