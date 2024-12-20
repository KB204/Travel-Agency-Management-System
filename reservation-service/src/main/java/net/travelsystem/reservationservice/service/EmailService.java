package net.travelsystem.reservationservice.service;

import lombok.extern.slf4j.Slf4j;
import net.travelsystem.reservationservice.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailSender);
            mailMessage.setTo(emailDetails.to());
            mailMessage.setSubject(emailDetails.subject());
            mailMessage.setText(emailDetails.body());
            mailSender.send(mailMessage);
            log.info("Email was sent successfully");
        } catch (MailException ex) {
            log.error("Failed to send Email",ex);
        }
    }
}
