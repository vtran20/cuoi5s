package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table (name="promotion_class")
public class PromotionClass extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String name;
    private String className;
    /*html use for generating controlling expression on front-end*/
    private String html;
    /*Handle action when click submit. Ex: /promo/savepromo.html*/
    private String springAction;
    private String active;
    private List<Promotion> promotions;

    public PromotionClass() {
        super();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    @OneToMany(mappedBy="promoClass", cascade= CascadeType.PERSIST, fetch= FetchType.LAZY)
    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

}
