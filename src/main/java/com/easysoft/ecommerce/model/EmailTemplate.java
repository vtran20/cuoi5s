package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "email_template")
public class EmailTemplate extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    public static int OWNER_GROUP = 0;
    public static int PARTNER_GROUP = 1;
    public static int COMMERCE_GROUP = 2;

    private String templateName;
    private String templateFile;
    private String templateContent;
    private String subject;
    private String locale;
    private String defaultEmail;
    private int emailGroup;
    private int sequence;
    private List<EmailSite> emails;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    @Column(nullable = true, length = 5000)
    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @OneToMany(mappedBy = "emailTemplate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<EmailSite> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailSite> emails) {
        this.emails = emails;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Column(name = "isDefault", nullable = true, length = 1)
    public String getDefaultEmail() {
        return convertActiveFlag(defaultEmail);
    }

    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = convertActiveFlag(defaultEmail);
    }

    public int getEmailGroup() {
        return emailGroup;
    }

    public void setEmailGroup(int emailGroup) {
        this.emailGroup = emailGroup;
    }
}
