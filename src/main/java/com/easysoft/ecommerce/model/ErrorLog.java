package com.easysoft.ecommerce.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table (name="error_log")

public class ErrorLog extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String rootCause;
    private String dataSession;
    private Long storeId;
    private Long siteId;

    @Column(nullable = false, length = 500)
    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    @Lob
    public String getDataSession() {
        return dataSession;
    }

    public void setDataSession(String dataSession) {
        this.dataSession = dataSession;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }
}
