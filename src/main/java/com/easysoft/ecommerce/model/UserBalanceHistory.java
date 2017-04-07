package com.easysoft.ecommerce.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "users_balance_history")
public class UserBalanceHistory extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String name;
    private User user;
    private Long amount;
    public UserBalanceHistory() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
