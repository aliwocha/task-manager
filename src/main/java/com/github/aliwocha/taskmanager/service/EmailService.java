package com.github.aliwocha.taskmanager.service;

public interface EmailService {

    void sendEmail(String to, String subject, String email, boolean isHtmlContent);
}
