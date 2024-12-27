package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.NotificationType;
import com.sdaproject.api20216146.model.Notification;
import com.sdaproject.api20216146.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(Long userId, String message) {
        try {
            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/notifications", message);
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUserId(userId);
            notification.setSent(true);
            notification.setType(NotificationType.PUSH_NOTIFICATION);
            notificationRepository.save(notification);
            logger.info("Notification sent to user " + userId + ": " + message);
        } catch (Exception e) {
            logger.error("Failed to send notification to user " + userId + ": " + e.getMessage());
        }
    }
    
    

    public List<Notification> getUserNotifications(Long userId) {
        try {
            return notificationRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error fetching notifications for user {}: {}", userId, e.getMessage());
            return List.of();
        }
    }


    public Notification getNotificationById(Long notificationId) {
        try {
            return notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        } catch (Exception e) {
            logger.error("Error fetching notification with ID {}: {}", notificationId, e.getMessage());
            return null;
        }
    }


    public void markNotificationsAsRead(Long userId) {
        try {
            List<Notification> notifications = notificationRepository.findByUserId(userId);
            notifications.forEach(notification -> {
                if (!notification.isSent()) {
                    notification.setSent(true);
                }
            });
            notificationRepository.saveAll(notifications);
            logger.info("Marked all notifications as read for userId: {}", userId);
        } catch (Exception e) {
            logger.error("Error marking notifications as read for user {}: {}", userId, e.getMessage());
        }
    }
}
