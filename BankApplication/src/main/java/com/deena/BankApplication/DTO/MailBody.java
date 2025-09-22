package com.deena.BankApplication.DTO;

import lombok.Builder;

import java.util.Date;

@Builder
public record MailBody(
        String[] cc,
        String[] bcc,
        String[] to,
        Date sentDate,
        String subject,
        String text,
        String replyTo
) {}

