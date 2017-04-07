package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.UserRoleDao;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.UserRole;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRoleDaoImpl extends GenericDaoImpl<UserRole, Long> implements UserRoleDao {

    @Override
    public List<UserRole> findUserRoles(Long userId, Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from UserRole u where u.user.id = :userId and u.site.id = :siteId")
                .setParameter("userId", userId)
                .setParameter("siteId", site.getId());
        return query.list();
    }
    @Override
    public List<UserRole> findUserRoles(Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from UserRole u where u.site.id = :siteId")
                .setParameter("siteId", site.getId());
        return query.list();
    }

}
