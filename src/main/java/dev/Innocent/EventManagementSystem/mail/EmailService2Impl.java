package dev.Innocent.EventManagementSystem.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EmailService2Impl implements EmailService2 {

    private final EmailClient emailClient;

    @Autowired
    public EmailService2Impl(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    @Override
    public void sendMail(String email, String subject, String message) {
        emailClient.sendSimpleMessage(email, subject, message);
    }

}
