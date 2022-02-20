package com.example.chatserver.controller;

import com.example.chatserver.model.Message;
import com.example.chatserver.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashSet;

@Controller
public class ChatController {
    private final MessageRepository messageRepository;


    @Autowired
    private SimpMessagingTemplate template;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @MessageMapping("/all/{username}")
    public HashSet<Message> messages(@DestinationVariable String username){
        HashSet<Message> messages = messageRepository.findAllBySenderName(username);
        template.convertAndSendToUser(username,"/private", messages);
        return messages;
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private Message receivePublicMessage(@Payload Message message){
        return messageRepository.save(message);
    }

    @MessageMapping("/private-message")
    public Message receivePrivateMessage(@Payload Message message){
        Message msg = messageRepository.save(message);
        template.convertAndSendToUser(msg.getReceiverName(),"/private",msg);
        return msg;
    }
}

