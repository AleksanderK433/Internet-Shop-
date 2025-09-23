package com.shop.internetshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // MAILPIT
        mailSender.setHost("localhost");
        mailSender.setPort(1025);

        // Bez autoryzacji dla Mailpit
        mailSender.setUsername("");
        mailSender.setPassword("");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");  // ZMIENIONE na false
        props.put("mail.smtp.starttls.enable", "false");  // ZMIENIONE na false
        props.put("mail.debug", "true");

        return mailSender;
    }
}