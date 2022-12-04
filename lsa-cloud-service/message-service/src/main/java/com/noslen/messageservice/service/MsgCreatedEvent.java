package com.noslen.messageservice.service;

import com.noslen.messageservice.model.Msg;
import org.springframework.context.ApplicationEvent;

public class MsgCreatedEvent extends ApplicationEvent {
    public MsgCreatedEvent(Msg source) {
        super(source);
    }
}
