package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table (name="global_config")
public class GlobalConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;
    private String value;

    public GlobalConfig() {
    }

    public GlobalConfig(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Id
    @Column (name = "param_key")
    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }

    @Column (name = "param_value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
