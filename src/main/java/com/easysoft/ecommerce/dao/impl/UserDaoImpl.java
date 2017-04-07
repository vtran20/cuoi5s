package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.UserDao;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {

    @Override
    public List<User> findByUsername(String username) {
        if (StringUtils.isEmpty(username)) return null;
        List<User> items = findBy("username", username);
        return items;
    }

    @Override
    public List<User> findClientUserByUsername(String username, Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from User u join u.userRoles r where r.site.id = :siteId and u.username = :userName")
                .setParameter("siteId", site.getId())
                .setParameter("userName", username);
        return query.list();
    }

    @Override
    public List<User> getClientUsers(Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from User u join u.userRoles r where r.site.id = :siteId and u.siteAdmin = 'N' and u.siteUser = 'Y'")
                .setParameter("siteId", site.getId());

        return query.list();
    }

    @Override
    public List<User> getAdminUsers(Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from User u join u.userRoles r where r.site.id = :siteId and u.siteAdmin = 'Y' and u.siteUser = 'N'")
                .setParameter("siteId", site.getId());

        return query.list();
    }

    @Override
    public User getClientUser(String username, String password, Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from User u join u.userRoles r where u.username = :userName  and u.password = :password and r.site.id = :siteId and u.siteAdmin = 'N' and u.siteUser = 'Y'")
                .setParameter("userName", username)
                .setParameter("password", password)
                .setParameter("siteId", site.getId());

        List temp = query.list();
        if (temp != null && temp.size() == 1) {
            return (User) temp.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getUserWithTemporaryPassword(String username, String tempPassword, Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from User u join u.userRoles r where u.username = :userName  and u.tempPassword = :tempPassword and r.site.id = :siteId and u.siteAdmin = 'N' and u.siteUser = 'Y'")
                .setParameter("userName", username)
                .setParameter("tempPassword", tempPassword)
                .setParameter("siteId", site.getId());

        List temp = query.list();
        if (temp != null && temp.size() == 1) {
            return (User) temp.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getClientUser(String username, Site site) throws IllegalAccessException {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from User u join u.userRoles r where u.username = :userName and r.site.id = :siteId and u.siteAdmin = 'N' and u.siteUser = 'Y'")
                .setParameter("userName", username)
                .setParameter("siteId", site.getId());

        List temp = query.list();
        if (temp != null && !temp.isEmpty()) {
            if (temp.size() == 1) {
                return (User) temp.get(0);
            } else {
                throw new IllegalAccessException("Cannot have 2 client accounts have the same username in the same site");
            }
        } else {
            return null;
        }
    }

    @Override
    public User getAdminUser(String username, String password, Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from User u join u.userRoles r where u.username = :userName  and u.password = :password and r.site.id = :siteId and u.siteAdmin = 'Y' and u.siteUser = 'N'")
                .setParameter("userName", username)
                .setParameter("password", password)
                .setParameter("siteId", site.getId());

        List temp = query.list();
        if (temp != null && temp.size() == 1) {
            return (User) temp.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getPartnerFromUser(User user) {
        BigInteger userId = (BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select distinct id from users where id in (select user_id from user_role where site_id in (select parentSite_id from site where id in (select site_id from user_role where user_id=:userId)))")
                .setParameter("userId", user.getId()).uniqueResult();
        if (userId != null) {
            return findById(userId.longValue());
        } else {
            return null;
        }
    }

    @Override
    public List<User> getAdminUsers(Long siteId) {
        return getSessionFactory().getCurrentSession().createQuery("select distinct u from User u where u.id in (select ur.user.id from UserRole ur where ur.site.id =:siteId)")
                .setParameter("siteId", siteId).list();
    }

}