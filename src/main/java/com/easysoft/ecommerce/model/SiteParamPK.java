package com.easysoft.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: vtran
 * Date: Jun 1, 2010
 * Time: 5:23:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class SiteParamPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long siteId;
    private String key;

    public SiteParamPK() {
    }

    public SiteParamPK(Long siteId, String key) {
        this.siteId = siteId;
        this.key = key;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteParamPK)) return false;

        SiteParamPK that = (SiteParamPK) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (siteId != null ? !siteId.equals(that.siteId) : that.siteId != null) return false;

        return true;
    }

    public int hashCode() {
        int result = siteId != null ? siteId.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }
}