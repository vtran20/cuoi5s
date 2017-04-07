package com.easysoft.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="email_server")
public class EmailServer extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    private String smtp;
    private int port;
    private String sender;
    private String userName;
    private String password;
    private String encoding;
    private int emailsPerSend;
    private int emailsPerDay;

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getEmailsPerSend() {
        return emailsPerSend;
    }

    public void setEmailsPerSend(int emailsPerSend) {
        this.emailsPerSend = emailsPerSend;
    }

    public int getEmailsPerDay() {
        return emailsPerDay;
    }

    public void setEmailsPerDay(int emailsPerDay) {
        this.emailsPerDay = emailsPerDay;
    }
}
