package com.noslen.messageservice.controller;

import com.noslen.messageservice.model.Connected;
import com.noslen.messageservice.model.Message;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnect(SessionConnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = headerAccessor.getNativeHeader("username").get(0);
        if (!Connected.USERS.containsValue(username)) {
            Connected.USERS.put(sessionId, username);
            System.out.println(">>>>>> Connected User Event <<<<<<");
            Connected.USERS.forEach((key, value) -> System.out.println("SessionId: " + key + " Username: " + value));
            simpMessagingTemplate.convertAndSend("/topic/connectedUsers", new HashSet<>(Connected.USERS.values()));
        }
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
//        String username = Connected.USERS.get(sessionId);
        Connected.USERS.remove(sessionId);
        simpMessagingTemplate.convertAndSend("/topic/connectedUsers", new HashSet<>(Connected.USERS.values()));
        System.out.println(">>>>>> Disconnected User Event <<<<<<");
        Connected.USERS.forEach((key, value) -> System.out.println("SessionId: " + key + " Username: " + value));
    }

//
////    @MessageMapping("/register")
//    @SendToUser("/queue/newMember")
//    public Set<String> registerUser(@Payload String webChatUsername){
//// get sessionId from StompHeaderAccessor
////        String sessionId = connectEventListener.getSessionId();
////        // add the user to the connected user list
////        connectEventListener.getConnectedUsers().put(sessionId, webChatUsername);
////        simpleMessagingTemplate.convertAndSend("/topic/newMember", webChatUsername);
////        System.out.println("<<<< registerUser message: ");
////        connectEventListener.getConnectedUsers().keySet().forEach(System.out::println);
//        return new HashSet<>(Connected.USERS.values());
//    }

    @MessageMapping("/connectedUsers")
    @SendToUser("/queue/connectedUsers")
    public Set<String> getConnectedUsers() {
        System.out.println("<<<< getConnectedUsers message: ");
        Connected.USERS.values().forEach(System.out::println);
        return new HashSet<>(Connected.USERS.values());
    }

//    @MessageMapping("/unregister")
//    @SendTo("/topic/disconnectedUser")
//    public String unregisterUser(String webChatUsername) {
//        connectedUsers.remove(webChatUsername);
//        System.out.println("<<<< unregisterUser message: ");
//        connectedUsers.forEach(System.out::println);
//        return webChatUsername;
//    }

    @MessageMapping("/message")
    public void greeting(Message message) {
        System.out.println("greeting message");
        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/msg", message);
    }
}
