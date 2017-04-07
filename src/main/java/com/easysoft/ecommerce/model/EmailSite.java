package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;

/**
 * This entity is used to store email information for each site.
 *
 * User: vtran
 * Date: Jun 1, 2010
 * Time: 5:46:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name="email_site")
public class EmailSite implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long emailTemplateId;

    private EmailTemplate emailTemplate;

    private Long siteId;

    private Site site;

    private String sendFrom;
    private String sendTo;
    private String cc;
    private String bcc;

    @Id
    @Column (name = "site_id")
    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    @Id
    @Column (name = "email_template_id")
    public Long getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(Long emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    @ManyToOne
    @JoinColumn (name = "email_template_id")
    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    @ManyToOne
    @JoinColumn (name = "site_id")
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Column (name = "sendFrom", length = 50)
    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    @Column (name = "sendTo", length = 1000)
    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    @Column (name = "cc", length = 1000)
    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    @Column (name = "bcc", length = 1000)
    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }
}
