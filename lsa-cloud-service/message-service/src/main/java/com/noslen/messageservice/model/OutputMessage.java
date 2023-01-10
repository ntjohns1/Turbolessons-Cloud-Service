package com.noslen.messageservice.model;

public class OutputMessage extends Message {

    private String time;

    public OutputMessage(final String sender, final String text, final String time) {
        setSender(sender);
        setText(text);
        this.time = time;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) { this.time = time; }
}
