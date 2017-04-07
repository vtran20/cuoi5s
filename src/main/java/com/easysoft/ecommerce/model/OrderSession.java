package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * This entity is used to store sessionObject.
 *
 * User: vtran
 * Date: Jun 1, 2010
 * Time: 5:46:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name="order_session")
public class OrderSession  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private Order order;

    private byte[] orderSessionData;

    private Date updatedDate;

    @Id
    @Column (name = "order_id")
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "order_id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Basic(fetch=FetchType.LAZY)
    @Lob @Column(name="user_session_data")
    public byte[] getOrderSessionData() {
        return orderSessionData;
    }

    public void setOrderSessionData(byte[] orderSessionData) {
        this.orderSessionData = orderSessionData;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
