package com.easysoft.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="site_support")
public class SiteSupport extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String chatId;
    private String chatType;
    private String timeAvailable;
    private String showFooter;
    private float sequence;
    private Site site;

    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 25)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(length = 25)
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Column(length = 25)
    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    @Column(length = 100)
    public String getTimeAvailable() {
        return timeAvailable;
    }

    public void setTimeAvailable(String timeAvailable) {
        this.timeAvailable = timeAvailable;
    }

    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getShowFooter() {
        return convertActiveFlag(showFooter);
    }

    public void setShowFooter(String showFooter) {
        this.showFooter = convertActiveFlag(showFooter);
    }
}
