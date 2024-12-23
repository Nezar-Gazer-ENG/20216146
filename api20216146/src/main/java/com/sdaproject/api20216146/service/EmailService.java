package com.sdaproject.api20216146.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendOtpEmail(String email, String otp) {
        String subject = "Your OTP for Email Verification";
        String body = "Your OTP for verifying your email is: " + otp + "\n\nIf you didn't request this, please ignore this email.";
        sendEmail(email, subject, body);
    }
}
