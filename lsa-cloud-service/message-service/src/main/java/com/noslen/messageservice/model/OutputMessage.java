package com.noslen.messageservice.model;

public class OutputMessage {

    private String sender;
    private String text;
    private String time;

    public OutputMessage(final String sender, final String text, final String time) {

        this.sender = sender;
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }
}
