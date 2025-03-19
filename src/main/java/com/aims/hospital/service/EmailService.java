package com.aims.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailServiceInterface{
    private JavaMailSender mailSender;
    @Autowired
    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification - AIMS");
        message.setText(
                "your OTP for AIMS is "+
                        otp
                + "is valid for 5 minutes"
        );
        mailSender.send(message);
    }
}
