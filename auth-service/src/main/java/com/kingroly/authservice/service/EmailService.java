package com.kingroly.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for sending automated emails.
 * 
 * DESIGN PATTERN: Service Layer.
 * Encapsulates the business logic related to external email communication (SMTP).
 * By injecting 'JavaMailSender', we abstract the underlying mailing protocol.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    /**
     * Sends the 6-digit One-Time Password (OTP) code to the advertiser for 2FA.
     * 
     * @param to The recipient email address.
     * @param code The 6-digit OTP code to send.
     */
    public void sendOtpEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("2FA Security Code (In-Game Ad Engine)");
        message.setText("Your access code is: " + code + "\nThis code will expire in 5 minutes.");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending the email: " + e.getMessage());
            // Fallback to mock log if SMTP is not configured
            System.out.println("MOCK EMAIL -> A: " + to + " | OTP: " + code);
        }
    }

    /**
     * Sends the secure password reset token to the advertiser.
     * 
     * @param to The recipient email address.
     * @param resetToken The generated UUID token for password reset.
     */
    public void sendPasswordResetEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Reset Password (In-Game Ad Engine)");
        message.setText("Use this token to reset your password: " + resetToken + "\nThe token will expire in 15 minutes.");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending the reset email: " + e.getMessage());
            System.out.println("MOCK EMAIL -> A: " + to + " | RESET TOKEN: " + resetToken);
        }
    }
}
