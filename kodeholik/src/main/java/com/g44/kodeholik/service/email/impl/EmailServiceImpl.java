package com.g44.kodeholik.service.email.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.g44.kodeholik.exception.EmailSendingException;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.email.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Async("emailTaskExecutor")
    private void sendEmail(String to, String subject, Context context, String template) {
        String htmlContent = templateEngine.process(template, context);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException("Error sending email", "Error sending email");
        }
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailResetPassword(String to, String subject, String username, String link) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("link", link);
        sendEmail(to, subject, context, "reset-password");

    }

    @Async("emailTaskExecutor")

    @Override
    public void sendEmailLoginGoogle(String to, String subject, String username, String password, String email) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("password", password);
        context.setVariable("email", email);
        sendEmail(to, subject, context, "login-google");

    }

    @Async("emailTaskExecutor")

    @Override
    public void sendEmailAddUser(String to, String subject, String username, String password) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("password", password);
        sendEmail(to, subject, context, "add-user");
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailNotifyExam30Minutes(String to, String subject, String username, String date, String code,
            long duration) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("date", date);
        context.setVariable("code", code);
        context.setVariable("duration", duration + " minutes");
        sendEmail(to, subject, context, "exam-noti-30");
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailNotifyExam5Minutes(String to, String subject, String username, String date, String code) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("date", date);
        context.setVariable("code", code);
        sendEmail(to, subject, context, "exam-noti-5");
    }

}
