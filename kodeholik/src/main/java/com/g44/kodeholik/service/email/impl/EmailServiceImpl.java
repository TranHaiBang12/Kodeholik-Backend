package com.g44.kodeholik.service.email.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.g44.kodeholik.exception.EmailSendingException;
import com.g44.kodeholik.service.email.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailResetPassword(String to, String subject, String username, String link) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("link", link);

        String htmlContent = templateEngine.process("email-template", context);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException("Loi gui email", "Loi gui email");
        }
    }

}
