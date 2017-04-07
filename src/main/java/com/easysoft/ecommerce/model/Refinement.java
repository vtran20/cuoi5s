package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table (name="refinement")
public class Refinement extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    /*Refinement name*/
    private String name;
    /*Column that the refinement will filter on*/
    private String refinementColumn;
    /*Refinement is RANGE or STRING, currently, we only support 2 types*/
    private String refinementType;
    private String active;
    private int sequence;
    /*Refinement is GLOBAL, SITE, CATALOG, CATEGORY.
    Ex: if refinement is GLOBAL, it occur on all search, category pages that don't care site, catalog, category. The bound of
    scope will be decreased order by GLOBAL, SITE, CATALOG, CATEGORY */
    private String refinementScope;

    private List<RefinementValue> refinementValues;

    public Refinement() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefinementColumn() {
        return refinementColumn;
    }

    public void setRefinementColumn(String refinementColumn) {
        this.refinementColumn = refinementColumn;
    }

    public String getRefinementType() {
        return refinementType;
    }

    public void setRefinementType(String refinementType) {
        this.refinementType = refinementType;
    }

    @Column(name="active", length = 1)
    @Index(name = "activeIndex")
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRefinementScope() {
        return refinementScope;
    }

    public void setRefinementScope(String refinementScope) {
        this.refinementScope = refinementScope;
    }

    @OneToMany(mappedBy="refinement", fetch=FetchType.LAZY)
    public List<RefinementValue> getRefinementValues() {
        return refinementValues;
    }

    public void setRefinementValues(List<RefinementValue> refinementValues) {
        this.refinementValues = refinementValues;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
