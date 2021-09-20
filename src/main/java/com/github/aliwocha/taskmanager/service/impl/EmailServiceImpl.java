package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendEmail(String to, String subject, String message, boolean isHtmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            messageHelper.setText(message, isHtmlContent);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setFrom(emailFrom);

            LOG.info("Sending email to " + to);
            mailSender.send(mimeMessage);
            LOG.info("Sending email succeeded");
        } catch(MessagingException e) {
            LOG.error("Failed to send email", e);
        }
    }
}
