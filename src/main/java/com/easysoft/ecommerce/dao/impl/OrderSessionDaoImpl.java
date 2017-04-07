package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.OrderSessionDao;
import com.easysoft.ecommerce.model.Order;
import com.easysoft.ecommerce.model.OrderSession;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.UserSession;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.thoughtworks.xstream.XStream;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Date;
import java.util.List;

@Repository
public class OrderSessionDaoImpl extends GenericDaoImpl<OrderSession, Long> implements OrderSessionDao {

    public SessionObject getOrderSession (Long orderId, Site site) throws IOException, ClassNotFoundException {

        OrderSession orderSession = (OrderSession) this.getSessionFactory().getCurrentSession()
        		.createQuery("select a from OrderSession a join a.order b where a.orderId = :orderId and b.site.id = :siteId")
                .setParameter("orderId", orderId).setLong("siteId", site.getId()).uniqueResult();

        SessionObject orderObject = null;
        if (orderSession != null) {
//            XStream xstream = new XStream();
//            Object obj = xstream.fromXML(orderSession.getOrderSessionData());
            byte[] data = orderSession.getOrderSessionData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            Object obj =  is.readObject();

            if (obj instanceof SessionObject) {
                orderObject = (SessionObject) obj;
            }
        }
        return orderObject;
    }
    public SessionObject getOrderSession (Long orderId, Long userId, Site site) throws IOException, ClassNotFoundException {

        if (userId != null && userId > 0) {
            OrderSession orderSession = (OrderSession) this.getSessionFactory().getCurrentSession()
                    .createQuery("select a from OrderSession a join a.order b where b.userId = :userId and b.id = :orderId and b.site.id = :siteId")
                    .setParameter("userId", userId)
                    .setParameter("orderId", orderId)
                    .setParameter("siteId", site.getId()).uniqueResult();

            SessionObject orderObject = null;
            if (orderSession != null) {
//            XStream xstream = new XStream();
//            Object obj = xstream.fromXML(orderSession.getOrderSessionData());

                byte[] data = orderSession.getOrderSessionData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                Object obj =  is.readObject();

                if (obj instanceof SessionObject) {
                    orderObject = (SessionObject) obj;
                }
            }
            return orderObject;
        } else {
            return getOrderSession(orderId, site);
        }
    }

    public Order getOrder (Long orderId, Long userId, Site site) {
        if (userId != null && userId > 0) {
            return  (Order) this.getSessionFactory().getCurrentSession()
                    .createQuery("select a from Order a where a.userId = :userId and a.id = :orderId and a.site.id = :siteId")
                    .setParameter("userId", userId)
                    .setParameter("orderId", orderId)
                    .setLong("siteId", site.getId()).uniqueResult();
        } else {
            return getOrder(orderId, site);
        }

    }

    public Order getOrder (Long orderId, Site site) {
        return  (Order) this.getSessionFactory().getCurrentSession()
                .createQuery("select a from Order a  where a.id = :orderId and a.site.id=:siteId")
                .setParameter("orderId", orderId)
                .setLong("siteId", site.getId()).uniqueResult();

    }

    public void createOrUpdateOrderSession (SessionObject sessionObject, Order order) throws IOException, ClassNotFoundException {
        if (sessionObject == null) return;

        if (order != null && order.getId() != null && order.getId() > 0) {
            OrderSession orderSession = findUniqueBy("order.id", order.getId());

//            XStream xstream = new XStream();
//            String data = xstream.toXML(sessionObject);
            //Convert to binary object to support jdk1.7

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(sessionObject);
            byte data[] = out.toByteArray();

            if (orderSession == null) {
                orderSession = new OrderSession();
                orderSession.setOrderId(order.getId());
                orderSession.setOrder(order);
                orderSession.setOrderSessionData(data);
                orderSession.setUpdatedDate(new Date());
                persist(orderSession);
            }
            else {
                orderSession.setOrderSessionData(data);
                orderSession.setUpdatedDate(new Date());
                merge(orderSession);
            }
        } else {
            throw new IllegalArgumentException("Cannot create or update OrderSession");
        }
    }

}
