package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;

import java.util.List;

public interface UserDao extends GenericDao<User, Long> {

    List<User> findByUsername(String username);

    List<User> findClientUserByUsername(String username, Site site);

    List<User> getClientUsers(Site site);

    List<User> getAdminUsers(Site site);

    User getClientUser(String username, String password, Site site);

    User getUserWithTemporaryPassword(String username, String tempPassword, Site site);

    User getClientUser(String username, Site site) throws IllegalAccessException;

    User getAdminUser(String username, String password, Site site);

    User getPartnerFromUser(User user);

    List<User> getAdminUsers(Long siteId);
}