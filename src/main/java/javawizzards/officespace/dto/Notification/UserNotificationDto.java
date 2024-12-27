package javawizzards.officespace.dto.Notification;

import javawizzards.officespace.entity.Notification;
import javawizzards.officespace.enumerations.Notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationDto {
    private UUID id;
    private String message;
    private NotificationType notificationType;
    private boolean read;
    private LocalDateTime notificationDate;

    public static UserNotificationDto fromEntity(Notification notification) {
        return UserNotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .notificationType(notification.getNotificationType())
                .read(notification.isRead())
                .notificationDate(notification.getNotificationDate())
                .build();
    }
}
