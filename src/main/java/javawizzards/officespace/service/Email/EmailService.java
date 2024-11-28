package javawizzards.officespace.service.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

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

    public void sendRegistrationEmail(String to, String username, String loginUrl) throws MessagingException {
        String htmlTemplate = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome to OfficeSpace</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        color: #333333;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                    }
                    .header {
                        background-color: #2563eb;
                        color: white;
                        padding: 30px;
                        text-align: center;
                    }
                    .content {
                        background-color: #ffffff;
                        padding: 30px;
                        border-radius: 5px;
                        margin-top: 20px;
                    }
                    .footer {
                        text-align: center;
                        padding: 20px;
                        color: #666666;
                        font-size: 12px;
                    }
                    .button {
                        display: inline-block;
                        padding: 12px 24px;
                        background-color: #2563eb;
                        color: white;
                        text-decoration: none;
                        border-radius: 5px;
                        margin: 20px 0;
                    }
                    .highlight {
                        color: #2563eb;
                        font-weight: bold;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Welcome to OfficeSpace!</h1>
                    </div>
                    
                    <div class="content">
                        <h2>Hello %s,</h2>
                        
                        <p>Thank you for registering with OfficeSpace! We're excited to have you join our platform.</p>
                        
                        <p>Your account has been successfully created and you can now access all features of our platform:</p>
                        
                        <ul>
                            <li>Browse and book office spaces</li>
                            <li>Manage your reservations</li>
                            <li>Access your booking history</li>
                            <li>View detailed space information</li>
                        </ul>

                        <p>To get started, simply click the button below to log in to your account:</p>
                        
                        <center>
                            <a href="%s" class="button">Log In to OfficeSpace</a>
                        </center>

                        <p>If you have any questions or need assistance, our support team is always here to help. You can reach us at <span class="highlight">support@officespace.com</span>.</p>
                        
                        <p>Best regards,<br>The OfficeSpace Team</p>
                    </div>
                    
                    <div class="footer">
                        <p>This email was sent to %s. If you didn't register for an OfficeSpace account, please ignore this email.</p>
                        <p>&copy; 2024 OfficeSpace. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username, loginUrl, to);

        sendHtmlEmail(to, "Welcome to OfficeSpace!", htmlTemplate);
    }

    public void sendReservationConfirmation(
            String to,
            String username,
            String roomName,
            String date,
            String startTime,
            String endTime,
            String building,
            String floor,
            String totalPrice,
            List<String> roomResources,
            String confirmationNumber,
            String reservationUrl
    ) throws MessagingException {
        String resourcesList = roomResources.stream()
                .map(resource -> "<li>" + resource + "</li>")
                .collect(Collectors.joining("\n"));

        String htmlTemplate = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Reservation Confirmation - OfficeSpace</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background-color: #2563eb;
                            color: white;
                            padding: 30px;
                            text-align: center;
                        }
                        .content {
                            background-color: #ffffff;
                            padding: 30px;
                            border-radius: 5px;
                            margin-top: 20px;
                        }
                        .reservation-details {
                            background-color: #f8fafc;
                            padding: 20px;
                            border-radius: 5px;
                            margin: 20px 0;
                        }
                        .footer {
                            text-align: center;
                            padding: 20px;
                            color: #666666;
                            font-size: 12px;
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 24px;
                            background-color: #2563eb;
                            color: white;
                            text-decoration: none;
                            border-radius: 5px;
                            margin: 20px 0;
                        }
                        .highlight {
                            color: #2563eb;
                            font-weight: bold;
                        }
                        .detail-row {
                            display: flex;
                            justify-content: space-between;
                            margin: 10px 0;
                            padding: 5px 0;
                            border-bottom: 1px solid #e2e8f0;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Reservation Confirmed!</h1>
                        </div>
                       \s
                        <div class="content">
                            <h2>Hello %s,</h2>
                           \s
                            <p>Your reservation has been successfully confirmed. Here are your booking details:</p>
                           \s
                            <div class="reservation-details">
                                <div class="detail-row">
                                    <strong>Room:</strong>
                                    <span>%s</span>
                                </div>
                                <div class="detail-row">
                                    <strong>Date:</strong>
                                    <span>%s</span>
                                </div>
                                <div class="detail-row">
                                    <strong>Time:</strong>
                                    <span>%s - %s</span>
                                </div>
                                <div class="detail-row">
                                    <strong>Building:</strong>
                                    <span>%s</span>
                                </div>
                                <div class="detail-row">
                                    <strong>Floor:</strong>
                                    <span>%s</span>
                                </div>
                                <div class="detail-row">
                                    <strong>Total Price:</strong>
                                    <span class="highlight">%s</span>
                                </div>
                            </div>
                
                            <p>Room Resources:</p>
                            <ul>
                                %s
                            </ul>
                
                            <p>Important Information:</p>
                            <ul>
                                <li>Please arrive 5 minutes before your scheduled time</li>
                                <li>Keep your confirmation number: <span class="highlight">%s</span></li>
                                <li>Check-in at the reception desk upon arrival</li>
                            </ul>
                
                            <center>
                                <a href="%s" class="button">View Reservation Details</a>
                            </center>
                
                            <p>Need to make changes? You can manage your reservation through your account dashboard or contact us at <span class="highlight">support@officespace.com</span>.</p>
                           \s
                            <p>Best regards,<br>The OfficeSpace Team</p>
                        </div>
                       \s
                        <div class="footer">
                            <p>This email was sent to %s</p>
                            <p>Cancellation policy: Free cancellation up to 24 hours before the reservation.</p>
                            <p>&copy; 2024 OfficeSpace. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                    username,
                    roomName,
                    date,
                    startTime,
                    endTime,
                    building,
                    floor,
                    totalPrice,
                    resourcesList,
                    confirmationNumber,
                    reservationUrl,
                    to
                );
        
            sendHtmlEmail(to, "Reservation Confirmation - OfficeSpace", htmlTemplate);
        }
}
