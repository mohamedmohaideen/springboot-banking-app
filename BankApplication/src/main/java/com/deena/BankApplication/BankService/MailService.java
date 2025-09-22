package com.deena.BankApplication.BankService;

import com.deena.BankApplication.DTO.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();

        if (mailBody.to() != null) message.setTo(mailBody.to());
        if (mailBody.cc() != null) message.setCc(mailBody.cc());
        if (mailBody.bcc() != null) message.setBcc(mailBody.bcc());

        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());
        message.setSentDate(mailBody.sentDate());
        if (mailBody.replyTo() != null) message.setReplyTo(mailBody.replyTo());

        javaMailSender.send(message);
    }
}
