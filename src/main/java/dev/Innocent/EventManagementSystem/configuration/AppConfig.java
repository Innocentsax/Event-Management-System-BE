package dev.Innocent.EventManagementSystem.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Properties;

@Configuration
@Getter
@Setter
@Slf4j
public class AppConfig {

    @Value("${spring.mail.username}")
    private String emailUserName;

    @Value("${spring.mail.password}")
    private String emailPassword;


    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }


    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("in-v3.mailjet.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailUserName);
        mailSender.setPassword(emailPassword);



        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.starttls.enable","true");

        return mailSender;
    }
}
