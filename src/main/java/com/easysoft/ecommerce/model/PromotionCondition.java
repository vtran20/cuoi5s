package com.easysoft.ecommerce.model;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * 
 * User: vtran
 * Date: Jun 3, 2010
 * Time: 5:20:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name = "promotion_to_condition")
public class PromotionCondition extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private Promotion promotion;
    private ConditionClass condition;
    private String expression;
    private int sequence;
    private String logical; //This will store: AND, OR or null.
    

    @ManyToOne
    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    @ManyToOne
    public ConditionClass getCondition() {
        return condition;
    }

    public void setCondition(ConditionClass condition) {
        this.condition = condition;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getLogical() {
        return logical;
    }

    public void setLogical(String logical) {
        this.logical = logical;
    }
}
