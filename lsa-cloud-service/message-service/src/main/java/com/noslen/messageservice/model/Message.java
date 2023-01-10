package com.noslen.messageservice.model;

public class Message {

    private String sender;
    private String to;
    private String text;

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    };

}
