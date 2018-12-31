package com.easysoft.ecommerce.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table (name="nail_customer_payment")

public class NailCustomerPayment extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private NailCustomer nailCustomer;
    private NailStore store;

    private Long totalPrice;
    private Long servicePrice;
    private Long taxPrice;
    private Long tipPrice;
    private String credit;
    private String cash;
    private String check;
    private String giftcard;
    private Long creditAmount;
    private Long cashAmount;
    private Long checkAmount;
    private Long giftcardAmount;
    private String giftcardNumber;

    private String serviceSession;

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    @ManyToOne
    @JsonIgnore
    public NailCustomer getNailCustomer() {
        return nailCustomer;
    }

    public void setNailCustomer(NailCustomer nailCustomer) {
        this.nailCustomer = nailCustomer;
    }

    public Long getServicePrice() {
        return servicePrice;
    }

    @ManyToOne
    @JsonIgnore
    public NailStore getStore() {
        return store;
    }

    public void setStore(NailStore store) {
        this.store = store;
    }

    public void setServicePrice(Long servicePrice) {
        this.servicePrice = servicePrice;
    }

    public Long getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(Long taxPrice) {
        this.taxPrice = taxPrice;
    }

    public Long getTipPrice() {
        return tipPrice;
    }

    public void setTipPrice(Long tipPrice) {
        this.tipPrice = tipPrice;
    }

    @Column(name = "isCredit", nullable = true, length = 1)
    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    @Column(name = "isCash", nullable = true, length = 1)
    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    @Column(name = "isCheck", nullable = true, length = 1)
    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    @Column(name = "isGiftcard", nullable = true, length = 1)
    public String getGiftcard() {
        return giftcard;
    }

    public void setGiftcard(String giftcard) {
        this.giftcard = giftcard;
    }

    public String getGiftcardNumber() {
        return giftcardNumber;
    }

    public void setGiftcardNumber(String giftcardNumber) {
        this.giftcardNumber = giftcardNumber;
    }

    @Lob
    public String getServiceSession() {
        return serviceSession;
    }

    public void setServiceSession(String serviceSession) {
        this.serviceSession = serviceSession;
    }

    public Long getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Long creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Long getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(Long checkAmount) {
        this.checkAmount = checkAmount;
    }

    public Long getGiftcardAmount() {
        return giftcardAmount;
    }

    public void setGiftcardAmount(Long giftcardAmount) {
        this.giftcardAmount = giftcardAmount;
    }

    /////////////////////////Transient Attribute////////////////////////
    private Long customerId;

    @Transient
    public Long getCustomerId() {
        return customerId != null ? customerId: (nailCustomer != null && nailCustomer.getId() != null)? nailCustomer.getId() : 0;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
