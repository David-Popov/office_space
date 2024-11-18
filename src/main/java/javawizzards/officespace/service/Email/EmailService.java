package javawizzards.officespace.service.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String host;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void SendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(host);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendPlainTextEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(host);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(host);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true indicates HTML

        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String body, File attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(host);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        helper.addAttachment(attachment.getName(), attachment);

        mailSender.send(message);
    }

    public void sendEmailWithInlineImage(String to, String subject, String htmlBody, File imageFile, String imageContentId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(host);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        helper.addInline(imageContentId, imageFile);

        mailSender.send(message);
    }

    public void sendEmailToMultipleRecipients(String[] to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(host);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
