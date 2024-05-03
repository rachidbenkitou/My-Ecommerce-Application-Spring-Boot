package com.benkitoucoders.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketService(final SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMessage(final String topicSuffix) {
        simpMessagingTemplate.convertAndSend("/topic/" + topicSuffix, "New order added!");
    }

}
