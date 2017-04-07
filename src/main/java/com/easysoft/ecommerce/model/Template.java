package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "template")
public class Template extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    private Date updatedDate;

    private String name;
    private String imageUrl;
    //This is jsp decorator define code
    private String templateCode;
    //This is css code is gotten from wro.xml
    private String templateCssCode;
    //This is model string help to identify site template. It should be templateCode_templateCssCode
    private String templateModel;
    private String crop;

    private String active;
    private float sequence;
    private List<SiteTemplate> siteTemplates;
    private Site siteSample;
    private Site site;

    @Column(nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = true, length = 255)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(nullable = true, length = 25)
    public String getTemplateModel() {
        return templateModel;
    }

    public void setTemplateModel(String templateModel) {
        this.templateModel = templateModel;
    }

    @Column(nullable = true, length = 25)
    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    @Column(nullable = true, length = 25)
    public String getTemplateCssCode() {
        return templateCssCode;
    }

    public void setTemplateCssCode(String templateCssCode) {
        this.templateCssCode = templateCssCode;
    }

    @OneToMany(mappedBy = "template")
    public List<SiteTemplate> getSiteTemplates() {
        return siteTemplates;
    }

    public void setSiteTemplates(List<SiteTemplate> siteTemplates) {
        this.siteTemplates = siteTemplates;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Column(name = "active", nullable = true, length = 1)
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
    }

    @ManyToOne
    public Site getSiteSample() {
        return siteSample;
    }

    public void setSiteSample(Site siteSample) {
        this.siteSample = siteSample;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
    @Column(nullable = true, length = 20)
    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

}
