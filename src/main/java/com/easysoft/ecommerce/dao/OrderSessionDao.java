package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Order;
import com.easysoft.ecommerce.model.OrderSession;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.session.SessionObject;

import java.io.IOException;

public interface OrderSessionDao extends GenericDao<OrderSession, Long> {

    SessionObject getOrderSession (Long orderId, Long userId, Site site) throws IOException, ClassNotFoundException;
    SessionObject getOrderSession (Long orderId, Site site) throws IOException, ClassNotFoundException;
    Order getOrder (Long orderId, Long userId, Site site);
    void createOrUpdateOrderSession (SessionObject sessionObject, Order order) throws IOException, ClassNotFoundException;

}
