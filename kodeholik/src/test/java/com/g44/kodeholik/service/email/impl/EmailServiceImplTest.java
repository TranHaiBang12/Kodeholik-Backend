package com.g44.kodeholik.service.email.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.g44.kodeholik.exception.EmailSendingException;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

        @Mock
        private JavaMailSender javaMailSender;

        @Mock
        private TemplateEngine templateEngine;

        @InjectMocks
        private EmailServiceImpl emailServiceImpl;

        @Mock
        private MimeMessage mimeMessage;

        @Mock
        private MimeMessageHelper mimeMessageHelper;

        @BeforeEach
        public void setUp() {
                doNothing().when(javaMailSender).send(any(MimeMessage.class));
        }

        @Test
        public void testSendEmailResetPassword() {

                String username = "test";
                String link = "test-link";

                MimeMessage mimeMessage = mock(MimeMessage.class);
                when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

                // Mock TemplateEngine đúng template name
                when(templateEngine.process(eq("reset-password"),
                                any(Context.class)))
                                .thenReturn("<html>Email Content</html>");

                // Gọi phương thức cần test
                emailServiceImpl.sendEmailResetPassword(
                                "test@example.com",
                                "Reset Password",
                                username,
                                link);

                verify(javaMailSender, times(1))
                                .send(any(MimeMessage.class));
        }

        @Test
        public void testSendEmailLoginGoogle() {

                String username = "test";
                String password = "test";

                MimeMessage mimeMessage = mock(MimeMessage.class);
                when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

                // Mock TemplateEngine đúng template name
                when(templateEngine.process(eq("login-google"),
                                any(Context.class)))
                                .thenReturn("<html>Email Content</html>");

                // Gọi phương thức cần test
                emailServiceImpl.sendEmailLoginGoogle("test@example.com",
                                "Reset Password",
                                username,
                                password,
                                "test@example.com");

                verify(javaMailSender, times(1))
                                .send(any(MimeMessage.class));
        }

        @Test
        public void testSendEmailAddUser() {
                String username = "test";
                String password = "test";

                MimeMessage mimeMessage = mock(MimeMessage.class);
                when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

                // Mock TemplateEngine đúng template name
                when(templateEngine.process(eq("add-user"),
                                any(Context.class)))
                                .thenReturn("<html>Email Content</html>");

                // Gọi phương thức cần test
                emailServiceImpl.sendEmailAddUser("test@example.com",
                                "Reset Password",
                                username,
                                password);

                verify(javaMailSender, times(1))
                                .send(any(MimeMessage.class));
        }

        @Test
        public void testSendEmailNotifyExam30Minutes() {
                String username = "test";
                String date = "03-10-2025, 16:25";
                String code = "test";

                MimeMessage mimeMessage = mock(MimeMessage.class);
                when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

                // Mock TemplateEngine đúng template name
                when(templateEngine.process(eq("exam-noti-30"),
                                any(Context.class)))
                                .thenReturn("<html>Email Content</html>");

                // Gọi phương thức cần test
                emailServiceImpl.sendEmailNotifyExam30Minutes("test@example.com",
                                "Reset Password",
                                username,
                                date, code, 0L);

                verify(javaMailSender, times(1))
                                .send(any(MimeMessage.class));
        }

        @Test
        public void testSendEmailNotifyExam5Minutes() {
                String username = "test";
                String date = "03-10-2025, 16:25";
                String code = "test";

                MimeMessage mimeMessage = mock(MimeMessage.class);
                when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

                // Mock TemplateEngine đúng template name
                when(templateEngine.process(eq("exam-noti-5"),
                                any(Context.class)))
                                .thenReturn("<html>Email Content</html>");

                // Gọi phương thức cần test
                emailServiceImpl.sendEmailNotifyExam5Minutes("test@example.com",
                                "Reset Password",
                                username,
                                date, code);

                verify(javaMailSender, times(1))
                                .send(any(MimeMessage.class));
        }

}