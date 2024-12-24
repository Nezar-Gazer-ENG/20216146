package com.sdaproject.api20216146.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     *
     * @param destination the destination path
     * @param message     the message content
     */
    public void sendNotification(String destination, String message) {
        try {
            messagingTemplate.convertAndSend(destination, message);
            logger.info("Notification sent to destination: {} with message: {}", destination, message);
        } catch (Exception e) {
            logger.error("Failed to send notification to destination: {}", destination, e);
        }
    }

    /**
     * Sends a notification to the default topic.
     *
     * @param message the message content
     */
    public void sendNotification(String message) {
        sendNotification("/topic/default", message);
    }

    /**
     
      @param message 
     */
    public void sendBroadcastNotification(String message) {
        try {
            messagingTemplate.convertAndSend("/topic/broadcast", message);
            logger.info("Broadcast notification sent with message: {}", message);
        } catch (Exception e) {
            logger.error("Failed to send broadcast notification", e);
        }
    }
}
