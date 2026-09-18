package com.kingroly.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendOtpEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Codice di Sicurezza 2FA (In-Game Ad Engine)");
        message.setText("Il tuo codice di accesso è: " + code + "\nQuesto codice scadrà tra 5 minuti.");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Errore nell'invio della mail: " + e.getMessage());
            // Fallback al mock log se l'SMTP non è configurato
            System.out.println("MOCK EMAIL -> A: " + to + " | OTP: " + code);
        }
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Reset Password (In-Game Ad Engine)");
        message.setText("Usa questo token per reimpostare la tua password: " + resetToken + "\nIl token scadrà tra 15 minuti.");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Errore nell'invio della mail di reset: " + e.getMessage());
            System.out.println("MOCK EMAIL -> A: " + to + " | RESET TOKEN: " + resetToken);
        }
    }
}
