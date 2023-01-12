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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatController {


//    @Autowired
//    SessionDisconnectEventListener disconnectEventListener;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Map<String, Long> lastHeartbeat;

    private final Integer HEARTBEAT_TIMEOUT = 10000;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        lastHeartbeat = new ConcurrentHashMap<>();
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
        }


    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        // mark user as disconnected
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        Connected.USERS.remove(sessionId);
        System.out.println(">>>>>> Disconnected User Event <<<<<<");
        Connected.USERS.forEach((key, value) -> System.out.println("SessionId: " + key + " Username: " + value));
        // send a message to notify others that the user is disconnected
    }



//    @MessageMapping("/register")
//    @SendToUser("/queue/newMember")
//    public Set<String> registerUser(@Payload String webChatUsername){
//// get sessionId from StompHeaderAccessor
//        String sessionId = connectEventListener.getSessionId();
//        // add the user to the connected user list
//        connectEventListener.getConnectedUsers().put(sessionId, webChatUsername);
//        simpleMessagingTemplate.convertAndSend("/topic/newMember", webChatUsername);
//        System.out.println("<<<< registerUser message: ");
//        connectEventListener.getConnectedUsers().keySet().forEach(System.out::println);
//        return connectEventListener.getConnectedUsers().keySet();
//    }

    @MessageMapping("/connectedUsers")
    @SendToUser("/queue/connectedUsers")
    public Set<String> getConnectedUsers(String username) {
        System.out.println("<<<< getConnectedUsers message: ");
        Connected.USERS.keySet().forEach(System.out::println);
        return Connected.USERS.keySet();
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
        simpMessagingTemplate.convertAndSendToUser(message.getSender(), "/msg", message);
    }

//    @MessageMapping("/heartbeat")
//    public void receiveHeartbeat(@Payload String sessionId) {
//        lastHeartbeat.put(sessionId, System.currentTimeMillis());
//        System.out.println("id:" + sessionId + ", " + "time: " + System.currentTimeMillis());
//    }


//    @Scheduled(fixedDelay = 10000) // check every 10 seconds
//    public void checkForDisconnectedUsers() {
//        long currentTime = System.currentTimeMillis();
//        for (Map.Entry<String, Long> entry : lastHeartbeat.entrySet()) {
//            String sessionId = entry.getKey();
//            long lastHeartbeatTime = entry.getValue();
//            if (currentTime - lastHeartbeatTime > HEARTBEAT_TIMEOUT) {
//                String disconnectedUsername = Connected.USERS.get(sessionId);
//                Connected.USERS.remove(sessionId);
//                // mark user as disconnected
//
//                // send a message to notify others that the user is disconnected
//            }
//        }
//    }


}
