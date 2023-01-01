package com.noslen.messageservice.controller;

import com.noslen.messageservice.model.Message;
import com.noslen.messageservice.model.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

//    @Autowired
//    MessageRepo messageRepo;

    private SimpMessagingTemplate template;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(final Message message) throws Exception {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println(message.getSender() + ", " + message.getText());
        return new OutputMessage(message.getSender(), message.getText(), time);
    }



}
