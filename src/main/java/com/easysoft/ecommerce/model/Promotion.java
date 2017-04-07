package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table (name="promotion")
public class Promotion extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private String name;
    private String promoCode;
    private String description;

    private Date startDate;
    private Date endDate;
    private String active;

    private List<PromotionCondition> conditions;
    private PromotionClass promoClass;

    private String promoParam1;
    private String promoParam2;
    private String promoParam3;
    private String promoParam4;
    private String promoParam5;
    private Site site;
    private String promoType;

    public Promotion() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    @OneToMany(mappedBy="condition")
    public List<PromotionCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<PromotionCondition> conditions) {
        this.conditions = conditions;
    }

    @Column (unique = true, nullable = false)
    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    @ManyToOne
    public PromotionClass getPromoClass() {
        return promoClass;
    }

    public void setPromoClass(PromotionClass promoClass) {
        this.promoClass = promoClass;
    }

    public String getPromoParam1() {
        return promoParam1;
    }

    public void setPromoParam1(String promoParam1) {
        this.promoParam1 = promoParam1;
    }

    public String getPromoParam2() {
        return promoParam2;
    }

    public void setPromoParam2(String promoParam2) {
        this.promoParam2 = promoParam2;
    }

    public String getPromoParam3() {
        return promoParam3;
    }

    public void setPromoParam3(String promoParam3) {
        this.promoParam3 = promoParam3;
    }

    public String getPromoParam4() {
        return promoParam4;
    }

    public void setPromoParam4(String promoParam4) {
        this.promoParam4 = promoParam4;
    }

    public String getPromoParam5() {
        return promoParam5;
    }

    public void setPromoParam5(String promoParam5) {
        this.promoParam5 = promoParam5;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }
}
