package javawizzards.officespace.controller;

import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.service.Email.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
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
}
