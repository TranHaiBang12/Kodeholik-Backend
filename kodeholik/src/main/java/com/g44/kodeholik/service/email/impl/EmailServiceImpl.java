package com.g44.kodeholik.service.email.impl;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
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
import com.g44.kodeholik.service.openai.OpenAIService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    private final OpenAIService openAIService;

    @Value("${spring.application.fe-url}")
    private String feLink;

    @Async("emailTaskExecutor")
    private void sendEmail(String to, String subject, Context context, String template) {
        String htmlContent = templateEngine.process(template, context);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            ClassPathResource imageResource = new ClassPathResource("templates/images/logo/kodeholik_logo.png");
            helper.addInline("kodeholik-logo", imageResource);

            javaMailSender.send(message);
        } catch (Exception e) {
            log.info(e.getMessage());
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
        context.setVariable("link", feLink + "/exam");
        sendEmail(to, subject, context, "exam-noti-30");
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailNotifyExam5Minutes(String to, String subject, String username, String date, String code) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("date", date);
        context.setVariable("code", code);
        context.setVariable("link", feLink + "/exam");
        sendEmail(to, subject, context, "exam-noti-5");
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailRemindLearning(String to, String subject, String username, String content) {
        Context context = new Context();
        content = openAIService.generateContentEmailReminder(content);
        context.setVariable("username", username);
        context.setVariable("content", content);
        sendEmail(to, subject, context, "learn-reminder");
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailCompleteCourse(String to, String subject, String username, String courseName, String startDate,
            String endDate, int totalDays) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("courseName", courseName);
        context.setVariable("startDate", startDate);
        context.setVariable("endDate", endDate);
        context.setVariable("totalDays", totalDays);
        context.setVariable("link", feLink + "/courses");
        sendEmail(to, subject, context, "course-complete");
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailNotifyExamResult(String to, String subject, String username, String title, String startDate,
            double totalGrade, Map<String, Double> questionResults) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("title", title);
        context.setVariable("startDate", startDate);
        context.setVariable("totalGrade", totalGrade);
        context.setVariable("questionResults", questionResults);
        context.setVariable("link", feLink + "/exam");
        sendEmail(to, subject, context, "exam-result-noti");
    }

}
