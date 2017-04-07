package com.easysoft.ecommerce.service;

import java.util.Map;


public interface
        MailService {

    void sendEmailFromTemplateContent(final String from, final String to, final String[] cc, final String[] bcc, final String subject, final Map map, final String emailTemplateContent);

    void sendEmail(final String from, final String to, final String[] cc, final String[] bcc, final String subject, final Map map, final String emailTemplate);

    void sendEmail(final String from, final String to, final String[] cc, final String[] bcc, final String subject, final String body);

    void sendEmail(final String from, final String to, final String[] cc, final String[] bcc, final String subject, final String body, final boolean isHtml);
}

