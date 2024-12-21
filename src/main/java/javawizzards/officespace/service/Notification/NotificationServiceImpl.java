package javawizzards.officespace.service.Notification;

import javawizzards.officespace.entity.Notification;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.Notification.NotificationType;
import javawizzards.officespace.repository.NotificationRepository;
import javawizzards.officespace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//TODO
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, 
                                  UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void sendSystemNotification(String message, NotificationType type, List<UUID> userIds) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setNotificationType(type);
        notification.setNotificationDate(LocalDateTime.now());

        Set<User> users = new HashSet<>();
        for (UUID userId : userIds) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for id: " + userId));
            users.add(user);
        }

        notification.setUsers(users);

        notificationRepository.save(notification);
    }

    

    @Override
    public List<Notification> getUserNotifications(UUID userId) {
        return notificationRepository.findByUsers_IdAndReadFalse(userId);
    }

    @Override
    public void markNotificationAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.markAsRead();
        notificationRepository.save(notification);
    }

}
