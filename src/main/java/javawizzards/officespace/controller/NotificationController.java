package javawizzards.officespace.controller;

import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.service.Email.EmailService;
import javawizzards.officespace.service.Notification.NotificationService;
import javawizzards.officespace.entity.Notification;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.Notification.NotificationType;
import javawizzards.officespace.repository.NotificationRepository;
import javawizzards.officespace.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final EmailService emailService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public NotificationController(EmailService emailService, NotificationService notificationService, UserRepository userRepository, NotificationRepository notificationRepository) {
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    @PostMapping("/send-email")
    public ResponseEntity<Response<?>> sendEmail(){
        Response<?> response = null;

        try{
            this.emailService.SendEmail("didicko033@gmail.com","Email Sender Test", "testa e minal uspeshno");
            response = new Response<>();
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    public void sendNotification(String message, NotificationType type, UUID userId) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setNotificationType(type);
        notification.setNotificationDate(LocalDateTime.now());

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found for id: " + userId));

        Set<User> users = new HashSet<>();
        users.add(user);
        notification.setUsers(users);

        notificationRepository.save(notification);
    }


    @GetMapping("/notifications/{userId}")
    public ResponseEntity<Response<List<Notification>>> getUserNotifications(@PathVariable UUID userId) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            return ResponseEntity.ok(new Response<>(notifications, HttpStatus.OK, "Notifications fetched successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

    @PatchMapping("/read/{notificationId}")
    public ResponseEntity<Response<String>> markNotificationAsRead(@PathVariable UUID notificationId) {
        try {
            notificationService.markNotificationAsRead(notificationId);
            return ResponseEntity.ok(new Response<>("Notification marked as read successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

}
