package javawizzards.officespace.service.Notification;

import javawizzards.officespace.entity.Notification;
import javawizzards.officespace.enumerations.Notification.NotificationType;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void sendSystemNotification(String message, NotificationType type, List<UUID> userIds);
    
    List<Notification> getUserNotifications(UUID userIds);

    void markNotificationAsRead(UUID notificationId);

}
