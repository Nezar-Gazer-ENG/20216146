package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.NotificationType;
import com.sdaproject.api20216146.model.Notification;
import com.sdaproject.api20216146.repository.NotificationRepository;
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

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(String destination, String message) {
        try {
            messagingTemplate.convertAndSend(destination, message);
            logger.info("Notification sent to destination: {} with message: {}", destination, message);

            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setType(NotificationType.PUSH_NOTIFICATION);
            notification.setUserId(1L);
            notification.setSent(true);
            notificationRepository.save(notification);

        } catch (Exception e) {
            logger.error("Failed to send notification to destination: {}", destination, e);
        }
    }
}
