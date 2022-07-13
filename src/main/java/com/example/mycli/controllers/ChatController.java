//package com.example.mycli.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@CrossOrigin(origins = "*")
//public class ChatController {
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public Message receiveMessage(@Payload Message message){
//        return message;
//    }
//
//    @MessageMapping("/private-message")
//    public Message recMessage(@Payload Message message){
//        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
//        System.out.println(message.toString());
//        return message;
//    }
//}
