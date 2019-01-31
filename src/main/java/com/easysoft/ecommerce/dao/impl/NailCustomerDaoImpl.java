package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NailCustomerDao;
import com.easysoft.ecommerce.model.NailCustomer;
import com.easysoft.ecommerce.model.NailCustomerService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class NailCustomerDaoImpl extends GenericDaoImpl<NailCustomer, Long> implements NailCustomerDao {

    @Override
    public List<NailCustomer> findSuggestionCustomers(String phone, String email, Long storeId, int maxResult) {
        if (StringUtils.isNotEmpty(phone)) {
            return getSessionFactory().getCurrentSession()
                    .createQuery("SELECT c FROM " + getPersistentClass().getName() + " c where c.store.id = :storeId and c.phone like :phone ORDER BY c.phone asc")
                    .setParameter("storeId", storeId)
                    .setParameter("phone", phone+"%")
                    .setFirstResult(0)
                    .setMaxResults(maxResult)
                    .list();
        } else if (StringUtils.isNotEmpty(email)) {
            return getSessionFactory().getCurrentSession()
                    .createQuery("SELECT c FROM " + getPersistentClass().getName() + " c where c.store.id = :storeId and c.email like :email ORDER BY c.email asc")
                    .setParameter("storeId", storeId)
                    .setParameter("email", email+"%")
                    .setFirstResult(0)
                    .setMaxResults(maxResult)
                    .list();
        }
        return null;
    }

    @Override
    public List<NailCustomer> getCheckedInCustomersByDate(Date startDate, Date endDate, Long storeId) throws Exception {
        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT c FROM " + getPersistentClass().getName() + " c where c.checkIn between :startDate and :endDate and c.store.id = :storeId")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("endDate", endDate, new TimestampType())
                .setParameter("storeId", storeId).list();
    }

}