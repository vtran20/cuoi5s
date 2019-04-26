package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "site_headerfooter")
public class SiteHeaderFooter extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    String logoText;
    String sloganText;
    String logoImg;
    String crop;
    String logoAltImg;
    String useLogoImg;
    String footerHeaderDisplay;
    String footerHeader;
    String footerContent;
    String footerSupportDisplay;
    String footerSupportHeader;
    String footerContactDisplay;
    String footerContactHeader;
    String footerUsefulLinkDisplay;
    String footerUsefulLinkHeader;
    String footerNewsLinkDisplay;
    String footerNewsLinkHeader;

    private Site site;

    private Date updatedDate;

    public SiteHeaderFooter() {
        super();
    }

    @Column(name = "logo_text", length = 300)
    public String getLogoText() {
        return logoText;
    }

    public void setLogoText(String logoText) {
        this.logoText = logoText;
    }

    @Column(name = "slogan_text", length = 300)
    public String getSloganText() {
        return sloganText;
    }

    public void setSloganText(String sloganText) {
        this.sloganText = sloganText;
    }

    @Column(name = "logo_img", length = 300)
    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    @Column(name = "logo_alt_img", length = 300)
    public String getLogoAltImg() {
        return logoAltImg;
    }

    public void setLogoAltImg(String logoAltImg) {
        this.logoAltImg = logoAltImg;
    }

    @Column(name = "use_logo_img", nullable = true, length = 1)
    public String getUseLogoImg() {
        return useLogoImg;
    }

    public void setUseLogoImg(String useLogoImg) {
        this.useLogoImg = useLogoImg;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Column(length = 30)
    public String getFooterHeader() {
        return footerHeader;
    }

    public void setFooterHeader(String footerHeader) {
        this.footerHeader = footerHeader;
    }

    @Column(length = 2000)
    public String getFooterContent() {
        return footerContent;
    }

    public void setFooterContent(String footerContent) {
        this.footerContent = footerContent;
    }

    @OneToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getFooterHeaderDisplay() {
        return convertActiveFlag(footerHeaderDisplay);
    }

    public void setFooterHeaderDisplay(String footerHeaderDisplay) {
        this.footerHeaderDisplay = convertActiveFlag(footerHeaderDisplay);
    }

    public String getFooterSupportDisplay() {
        return convertActiveFlag(footerSupportDisplay);
    }

    public void setFooterSupportDisplay(String footerSupportDisplay) {
        this.footerSupportDisplay = convertActiveFlag(footerSupportDisplay);
    }

    public String getFooterSupportHeader() {
        return footerSupportHeader;
    }

    public void setFooterSupportHeader(String footerSupportHeader) {
        this.footerSupportHeader = footerSupportHeader;
    }

    public String getFooterContactDisplay() {
        return convertActiveFlag(footerContactDisplay);
    }

    public void setFooterContactDisplay(String footerContactDisplay) {
        this.footerContactDisplay = convertActiveFlag(footerContactDisplay);
    }

    public String getFooterContactHeader() {
        return footerContactHeader;
    }

    public void setFooterContactHeader(String footerContactHeader) {
        this.footerContactHeader = footerContactHeader;
    }

    public String getFooterUsefulLinkDisplay() {
        return convertActiveFlag(footerUsefulLinkDisplay);
    }

    public void setFooterUsefulLinkDisplay(String footerUsefulLinkDisplay) {
        this.footerUsefulLinkDisplay = convertActiveFlag(footerUsefulLinkDisplay);
    }

    public String getFooterUsefulLinkHeader() {
        return footerUsefulLinkHeader;
    }

    public void setFooterUsefulLinkHeader(String footerUsefulLinkHeader) {
        this.footerUsefulLinkHeader = footerUsefulLinkHeader;
    }

    @Column(nullable = true, length = 1)
    public String getFooterNewsLinkDisplay() {
        return convertActiveFlag(footerNewsLinkDisplay);
    }

    public void setFooterNewsLinkDisplay(String footerNewsLinkDisplay) {
        this.footerNewsLinkDisplay = convertActiveFlag(footerNewsLinkDisplay);
    }

    @Column(length = 30)
    public String getFooterNewsLinkHeader() {
        return footerNewsLinkHeader;
    }

    public void setFooterNewsLinkHeader(String footerNewsLinkHeader) {
        this.footerNewsLinkHeader = footerNewsLinkHeader;
    }
}
