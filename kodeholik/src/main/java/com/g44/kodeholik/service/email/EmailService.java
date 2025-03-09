package com.g44.kodeholik.service.email;

public interface EmailService {
    public void sendEmailResetPassword(String to, String subject, String username, String link);

    public void sendEmailLoginGoogle(String to, String subject, String username, String password);

    public void sendEmailAddUser(String to, String subject, String username, String password);

    public void sendEmailNotifyExam30Minutes(String to, String subject, String username, String date, String code);

    public void sendEmailNotifyExam5Minutes(String to, String subject, String username, String date, String code);

}
