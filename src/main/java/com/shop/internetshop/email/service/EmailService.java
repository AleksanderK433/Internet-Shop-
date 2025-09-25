package com.shop.internetshop.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Value("${SUPPORT_EMAIL:noreply@internetshop.local}")
    private String fromEmail;

    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
        try {
            logger.info("[MAILPIT] Wysylanie emaila...");
            logger.info("[MAILPIT] Do: {}", to);
            logger.info("[MAILPIT] Od: {}", fromEmail);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);  // DODANE - ustaw nadawcę
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            emailSender.send(message);

            logger.info("[MAILPIT] Email wyslany pomyslnie!");
            logger.info("[MAILPIT] Sprawdz: http://localhost:8025");

        } catch (MessagingException e) {
            logger.error("[MAILPIT] Blad wysylania: {}", e.getMessage());
            logger.error("[MAILPIT] Czy Mailpit jest uruchomiony? docker run -d --name mailpit -p 1025:1025 -p 8025:8025 axllent/mailpit");
            throw e;
        }
    }
}