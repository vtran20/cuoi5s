package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.controller.admin.form.OrderFilterForm;
import com.easysoft.ecommerce.model.Order;
import com.easysoft.ecommerce.model.Site;

import java.util.Date;
import java.util.List;

public interface OrderDao extends GenericDao<Order, Long> {
    List<Order> findOrders (OrderFilterForm orderFilter, Site site);
    Long countOrders (String status, Date startDate, Date endDate, Site site);
}
