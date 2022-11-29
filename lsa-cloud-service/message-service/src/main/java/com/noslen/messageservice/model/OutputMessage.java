package com.noslen.messageservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "messages")
public class OutputMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String sender;
    private String senderId;
    private String text;
    private String time;

    public OutputMessage() {
    }

    public OutputMessage(final String sender, final String senderId, final String text, final String time) {

        this.sender = sender;
        this.senderId = senderId;
        this.text = text;
        this.time = time;
    }

    public Integer getId() {
        return id;
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

    public String getSenderId() {
        return senderId;
    }
}
