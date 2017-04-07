package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "site_template")
public class SiteTemplate extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    //This is jsp decorator define code. This information can get from Template table. We add it here for accessing easier.
    private String templateCode;
    //This is css code is gotten from wro.xml. This information can get from Template table. We add it here for accessing easier.
    private String templateCssCode;
    //This is css code is gotten from one of these colors (red, blue, orange, light, purple, aqua, brown, dark-blue, light-green, green)
    private String templateColorCode;
    private String fullWide;
    private String headerFix;
    private String skinColor;
    private String headerType;
    private String footerType;

    private String preCmsHeader;
    private String cmsHeader;
    private String preCmsFooter;
    private String cmsFooter;

    private Template template;
    private Site site;

    private Date updatedDate;

    public SiteTemplate() {
        super();
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateCssCode() {
        return templateCssCode;
    }

    public void setTemplateCssCode(String templateCssCode) {
        this.templateCssCode = templateCssCode;
    }

    @Column(nullable = true, length = 25)
    public String getTemplateColorCode() {
        return templateColorCode;
    }

    public void setTemplateColorCode(String templateColorCode) {
        this.templateColorCode = templateColorCode;
    }

    @Column(nullable = true)
    @Lob
    public String getCmsHeader() {
        return cmsHeader;
    }

    public void setCmsHeader(String cmsHeader) {
        this.cmsHeader = cmsHeader;
    }

    @Column(nullable = true)
    @Lob
    public String getCmsFooter() {
        return cmsFooter;
    }

    public void setCmsFooter(String cmsFooter) {
        this.cmsFooter = cmsFooter;
    }

    @Column(nullable = true)
    @Lob
    public String getPreCmsHeader() {
        return preCmsHeader;
    }

    public void setPreCmsHeader(String preCmsHeader) {
        this.preCmsHeader = preCmsHeader;
    }

    @Column(nullable = true)
    @Lob
    public String getPreCmsFooter() {
        return preCmsFooter;
    }

    public void setPreCmsFooter(String preCmsFooter) {
        this.preCmsFooter = preCmsFooter;
    }

    @ManyToOne
    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @OneToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getFullWide() {
        return convertActiveFlag(fullWide);
    }

    public void setFullWide(String fullWide) {
        this.fullWide = convertActiveFlag(fullWide);
    }

    public String getHeaderFix() {
        return convertActiveFlag(headerFix);
    }

    public void setHeaderFix(String headerFix) {
        this.headerFix = convertActiveFlag(headerFix);
    }

    public String getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    @Column(nullable = true, length = 5)
    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    @Column(nullable = true, length = 5)
    public String getFooterType() {
        return footerType;
    }

    public void setFooterType(String footerType) {
        this.footerType = footerType;
    }
}
