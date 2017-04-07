package com.easysoft.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="email_plan")
public class EmailPlan extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    private String subject;
    private String emailTemplate;
    private Date startDate;
    private Date endDate;
    private String cmsTop;
    private String cmsBottom;
    private String model1;
    private String model2;
    private String model3;
    private String model4;
    private String model5;
    private String model6;
    private String model7;
    private String model8;
    private String model9;
    
    private Site site;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column (name = "cmsTop")
    public String getCmsTop() {
        return cmsTop;
    }

    public void setCmsTop(String cmsTop) {
        this.cmsTop = cmsTop;
    }

    @Column (name = "cmsBottom")
    public String getCmsBottom() {
        return cmsBottom;
    }

    public void setCmsBottom(String cmsBottom) {
        this.cmsBottom = cmsBottom;
    }

    public String getModel1() {
        return model1;
    }

    public void setModel1(String model1) {
        this.model1 = model1;
    }

    public String getModel2() {
        return model2;
    }

    public void setModel2(String model2) {
        this.model2 = model2;
    }

    public String getModel3() {
        return model3;
    }

    public void setModel3(String model3) {
        this.model3 = model3;
    }

    public String getModel4() {
        return model4;
    }

    public void setModel4(String model4) {
        this.model4 = model4;
    }

    public String getModel5() {
        return model5;
    }

    public void setModel5(String model5) {
        this.model5 = model5;
    }

    public String getModel6() {
        return model6;
    }

    public void setModel6(String model6) {
        this.model6 = model6;
    }

    public String getModel7() {
        return model7;
    }

    public void setModel7(String model7) {
        this.model7 = model7;
    }

    public String getModel8() {
        return model8;
    }

    public void setModel8(String model8) {
        this.model8 = model8;
    }

    public String getModel9() {
        return model9;
    }

    public void setModel9(String model9) {
        this.model9 = model9;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
