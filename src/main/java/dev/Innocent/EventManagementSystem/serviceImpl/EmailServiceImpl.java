package dev.Innocent.EventManagementSystem.serviceImpl;

import dev.Innocent.EventManagementSystem.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    public static final String TEST_CONTENT="welcome to Arsenal";
    public static final String TEST_SUBJECT="Confirmation message";
    public static final String REG_CONTENT = "Click here to verify your email";
    public static final String REG_SUBJECT ="Registration message";

    public static final String PAY_CONTENT = "Your funds have been successfully transferred.";
    public static final String PAY_CONTENT2 = "Your funds have NOT been successfully transferred.";
    public static final String PAY_SUBJECT = "Funds transferred successfully";



    @Override
    public void sendEmail(String toEmail, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();


        mailMessage.setTo(toEmail);

        mailMessage.setSubject(subject);

        mailMessage.setText(content);

        javaMailSender.send(mailMessage);

    }
}
