package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.model.Order;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.session.SessionObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface OrderService {

    SessionObject getOrderSession (Long orderId, Long userId) throws IOException, ClassNotFoundException;
    Order createOrder (SessionObject sessionObject) throws IOException, ClassNotFoundException;
    Order createServiceOrder(SessionObject sessionObject) throws IOException, ClassNotFoundException;

    boolean changeOrderStatus(Order order, String orderStatus, Long orderId) throws IOException, ClassNotFoundException;
}

