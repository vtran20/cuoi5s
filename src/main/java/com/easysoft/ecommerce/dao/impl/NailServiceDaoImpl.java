package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NailServiceDao;
import com.easysoft.ecommerce.model.NailService;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NailServiceDaoImpl extends GenericDaoImpl<NailService, Long> implements NailServiceDao {

    @Override
    public List<NailService> getGroupServices(Long storeId) throws Exception {
        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT s FROM " + getPersistentClass().getName() + " s where s.group is null and s.store.id = :storeId ORDER BY s.sequence asc")
                .setParameter("storeId", storeId).list();
    }

    @Override
    public List<NailService> getServices(Long groupId, Long storeId) throws Exception {
        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT s FROM " + getPersistentClass().getName() + " s where s.group.id = :groupId and s.store.id = :storeId ORDER BY s.sequence asc")
                .setParameter("groupId", groupId)
                .setParameter("storeId", storeId).list();
    }
}