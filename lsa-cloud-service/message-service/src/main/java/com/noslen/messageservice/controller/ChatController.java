package com.noslen.messageservice.controller;

import com.noslen.messageservice.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Set<String> connectedUsers;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        connectedUsers = new HashSet<>();
    }


//    @MessageMapping("/register")
//    @SendToUser("/queue/newMember")
//    public Set<String> registerUser(String webChatUsername) {
//        if (!connectedUsers.contains(webChatUsername)) {
//            connectedUsers.add(webChatUsername);
//            simpMessagingTemplate.convertAndSend("/topic/newMember", webChatUsername);
//            return connectedUsers;
//        } else {
//            return new HashSet<>();
//        }
//    }

    @MessageMapping("/newMember")
    @SendToUser
    public Set<String> registerUser(@Payload String webChatUsername){
            connectedUsers.add(webChatUsername);
            simpMessagingTemplate.convertAndSend("/topic/newMember", webChatUsername);
            System.out.println("<<<< registerUser message: ");
            connectedUsers.forEach(System.out::println);
            return connectedUsers;
    }

    @MessageMapping("/connectedUsers")
    @SendToUser("/queue/connectedUsers")
    public Set<String> getConnectedUsers(String username) {
        connectedUsers.add(username);
        System.out.println("<<<< getConnectedUsers message: ");
        connectedUsers.forEach(System.out::println);
        return connectedUsers;
    }

    @MessageMapping("/unregister")
    @SendTo("/topic/disconnectedUser")
    public String unregisterUser(String webChatUsername) {
        connectedUsers.remove(webChatUsername);
        System.out.println("<<<< unregisterUser message: ");
        connectedUsers.forEach(System.out::println);
        return webChatUsername;
    }

    @MessageMapping("/message")
    public void greeting(Message message) {
        System.out.println("greeting message");
        simpMessagingTemplate.convertAndSendToUser(message.getSender(), "/msg", message);
    }

}
