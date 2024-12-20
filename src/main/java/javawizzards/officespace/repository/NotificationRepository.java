package javawizzards.officespace.repository;

import javawizzards.officespace.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUsers_IdAndReadFalse(UUID userId); 
}
