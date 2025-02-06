package com.g44.kodeholik.service.email;

public interface EmailService {
    public void sendEmailResetPassword(String to, String subject, String username, String link);
}
