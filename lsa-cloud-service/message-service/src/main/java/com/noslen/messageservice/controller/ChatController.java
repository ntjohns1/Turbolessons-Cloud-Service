package com.noslen.messageservice.controller;

import com.noslen.messageservice.model.Message;
import com.noslen.messageservice.model.OutputMessage;
import com.noslen.messageservice.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    @Autowired
    MessageRepo messageRepo;

    @MessageMapping("/chat")
    @SendTo("/ws/messages")
    public OutputMessage send(final Message message) throws Exception {

        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage outputMessage = new OutputMessage(message.getSender(), message.getText(), time);
        messageRepo.save(outputMessage);
        return outputMessage;
    }

}
