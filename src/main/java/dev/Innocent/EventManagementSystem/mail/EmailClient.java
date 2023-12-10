package dev.Innocent.EventManagementSystem.mail;

import dev.Innocent.EventManagementSystem.configuration.AppConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

//import javax.mail.MessagingException;

@Component
@Slf4j
public class EmailClient {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("support@resavation.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (MessagingException | MailSendException e ) {
            log.error("Email error: {}", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
