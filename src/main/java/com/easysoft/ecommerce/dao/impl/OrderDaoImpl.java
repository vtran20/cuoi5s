package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.controller.admin.form.OrderFilterForm;
import com.easysoft.ecommerce.dao.OrderDao;
import com.easysoft.ecommerce.model.CmsAreaContent;
import com.easysoft.ecommerce.model.Order;
import com.easysoft.ecommerce.model.Site;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDaoImpl extends GenericDaoImpl<Order, Long> implements OrderDao {

    public List<Order> findOrders (OrderFilterForm orderFilter, Site site) {
        StringBuffer sql = new StringBuffer("select o from Order o where o.site.id = ?");
        if (!StringUtils.isEmpty(orderFilter.getCity())) {
            sql.append(" and o.cityShipping = ? ");
        }
        if (orderFilter.getStartDate() != null) {
            sql.append(" and o.createdDate >= ? ");
        }
        if (orderFilter.getEndDate() != null) {
            sql.append(" and o.createdDate <= ? ");
        }
        if (orderFilter.getOrderStatus() != null && orderFilter.getOrderStatus().length > 0) {
            sql.append(" and ( ");
            String temp = "";
            for (String orderStatus : orderFilter.getOrderStatus()) {
                if (StringUtils.isEmpty(temp)) {
                    temp = "o.status = ? ";
                } else {
                    temp += "or o.status = ? ";
                }
            }
            sql.append(temp);
            sql.append(" ) ");
        }
        sql.append(" order by o.createdDate");
        Query query = this.getSessionFactory().getCurrentSession().createQuery(sql.toString());
        int index = 0;

        query.setLong(index++, site.getId());
        if (!StringUtils.isEmpty(orderFilter.getCity())) {
            query.setString(index++, orderFilter.getCity());
        }
        if (orderFilter.getStartDate() != null) {
            query.setDate(index++, orderFilter.getStartDate());
        }
        if (orderFilter.getEndDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderFilter.getEndDate());
            calendar.add(Calendar.DAY_OF_YEAR, 1); //add 1 day to make sure it include all order in the current day
            query.setDate(index++, calendar.getTime());
        }
        if (orderFilter.getOrderStatus()!= null && orderFilter.getOrderStatus().length > 0) {
            for (String orderStatus : orderFilter.getOrderStatus()) {
                query.setString(index++, orderStatus);
            }
        }

        return query.list();
    }

    @Override
    public Long countOrders(String status, Date startDate, Date endDate, Site site) {
        StringBuffer sql = new StringBuffer("select count(o) from Order o where o.site.id = ?");
        if (startDate != null) {
            sql.append(" and o.createdDate >= ? ");
        }
        if (endDate != null) {
            sql.append(" and o.createdDate <= ? ");
        }
        if (status != null) {
            sql.append(" and o.status = ? ");
        }
        Query query = this.getSessionFactory().getCurrentSession().createQuery(sql.toString());
        int index = 0;
        query.setLong(index++, site.getId());
        if (startDate != null) {
            query.setDate(index++, startDate);
        }
        if (endDate != null) {
            query.setDate(index++, endDate);
        }
        if (status != null) {
            query.setString(index++, status);
        }

        return (Long) query.uniqueResult();
    }

}
