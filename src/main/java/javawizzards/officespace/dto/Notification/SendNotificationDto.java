package javawizzards.officespace.dto.Notification;

import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.enumerations.Notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendNotificationDto {
    @NotNull(message = "Id can't be null")
    @JsonProperty("Id")
    private UUID Id;

    @NotNull(message = "UserId can't be null")
    @JsonProperty("userId")
    private UUID userId;

    @NotNull(message = "Message can't be null")
    @JsonProperty("message")
    private String message;

    @NotNull(message = "NotificationType can't be null")
    @JsonProperty("notificationType")
    private NotificationType notificationType;

    @NotNull(message = "NotificationDate can't be null")
    @JsonProperty("notificationDate")
    private LocalDateTime notificationDate;
}
