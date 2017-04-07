package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Index;

import javax.persistence.*;
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
@Table (name = "refinement_value")
public class RefinementValue extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private Refinement refinement;
    private String refinementKey;
    private String refinementValue;
    private int sequence;
    private String active;
    private Site site;
    private Category category;
//    private Catalog catalog;


    @ManyToOne (cascade= CascadeType.ALL)
    public Refinement getRefinement() {
        return refinement;
    }

    public void setRefinement(Refinement refinement) {
        this.refinement = refinement;
    }

    @ManyToOne (cascade= CascadeType.ALL)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getRefinementKey() {
        return refinementKey;
    }

    public void setRefinementKey(String refinementKey) {
        this.refinementKey = refinementKey;
    }

    public String getRefinementValue() {
        return refinementValue;
    }

    public void setRefinementValue(String refinementValue) {
        this.refinementValue = refinementValue;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Column(name="active", length = 1)
    @Index(name = "activeIndex")
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @ManyToOne (cascade= CascadeType.ALL)
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

//    @ManyToOne (cascade= CascadeType.ALL)
//    public Catalog getCatalog() {
//        return catalog;
//    }
//
//    public void setCatalog(Catalog catalog) {
//        this.catalog = catalog;
//    }
}
