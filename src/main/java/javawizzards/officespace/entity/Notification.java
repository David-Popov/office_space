package javawizzards.officespace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javawizzards.officespace.enumerations.Notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String message;
    
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private boolean read = false;

    @ManyToMany
    @JoinTable(
        name = "user_notifications", 
        joinColumns = @JoinColumn(name = "notification_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id") 
    )
    private Set<User> users = new HashSet<>();

    @NotNull
    private LocalDateTime notificationDate = LocalDateTime.now();

    public void markAsRead() {
        this.read = true;
    }
}
