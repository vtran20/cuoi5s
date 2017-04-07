package com.easysoft.ecommerce.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: vtran
 * Date: Jun 1, 2010
 * Time: 5:46:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name="user_session")
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userSessionId;

    private byte[] userSessionData;

    private Date updatedDate = new Date();

    @Id
    @Column(name = "user_session_id", length = 50)
    public String getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }

    @Basic(fetch=FetchType.LAZY)
    @Lob
    @Column(name="user_session_data")
    public byte[] getUserSessionData() {
        return userSessionData;
    }

    public void setUserSessionData(byte[] userSessionData) {
        this.userSessionData = userSessionData;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
