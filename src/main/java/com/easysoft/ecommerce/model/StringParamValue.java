package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) // Using @Cache instead @Cacheable to avoid HHH-5303
@Table (name="string_param_value")
public class StringParamValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private StringParam stringParam;
    private String key;
    private String language;
    private String value;
    private int sequence;

    public StringParamValue() {
    }

    public StringParamValue( String key, String language, String value) {
        this.key = key;
        this.language = language;
        this.value = value;
    }

    @ManyToOne
    @JoinColumn(name = "string_param_id")
    public StringParam getStringParam() {
        return stringParam;
    }

    public void setStringParam(StringParam stringParam) {
        this.stringParam = stringParam;
    }

    @Id
    @Column (name = "string_param_key")
    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }

    @Id
    @Column (name = "language", length = 5)
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Column (name = "string_param_value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
