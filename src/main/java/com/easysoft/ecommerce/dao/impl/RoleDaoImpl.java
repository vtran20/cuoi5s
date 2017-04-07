package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.RoleDao;
import com.easysoft.ecommerce.model.Role;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role, Long> implements RoleDao {
    @Override
    public List<Role> findChildrenRoles (String role) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select r from Role r where r.role = :role")
        .setParameter("role", role);
        return query.list();
    }
}
