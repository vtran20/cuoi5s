package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// Using @Cache instead @Cacheable to avoid HHH-5303
@Table(name = "string_param")
public class StringParam extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private String key;
    private String name;
    private String description;
    private int sequence;

    private List<StringParamValue> stringParamValues;

    @OneToMany(mappedBy = "stringParam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<StringParamValue> getStringParamValues() {
        return stringParamValues;
    }

    public void setStringParamValues(List<StringParamValue> stringParamValues) {
        this.stringParamValues = stringParamValues;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="key_param" ,nullable = false, length = 50)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

}
