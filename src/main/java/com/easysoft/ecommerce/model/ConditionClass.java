package com.easysoft.ecommerce.model;


import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table (name="condition_class")
public class ConditionClass extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private String name;
    private String className;
    private String expression;
    /*html use for generating controlling expression on front-end*/
    private String html;
    /*Handle action when click submit*/
    private String springAction;
    private String active;
    private List<PromotionCondition> promotions;

    public ConditionClass() {
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

    @OneToMany(mappedBy="promotion")
    public List<PromotionCondition> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionCondition> promotions) {
        this.promotions = promotions;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
