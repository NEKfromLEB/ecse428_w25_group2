package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public boolean sendNotification(String message) {
        if (message == null || message.trim().isEmpty()) {
            logger.error("Failed to send notification: message is null or empty");
            return false;
        }
        try {
            // For now, just log the message
            logger.info("Notification sent: {}", message);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send notification: {}", e.getMessage());
            return false;
        }
    }
} 