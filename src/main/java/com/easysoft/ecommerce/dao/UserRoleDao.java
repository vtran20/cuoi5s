package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.UserRole;

import java.util.List;

public interface UserRoleDao extends GenericDao<UserRole, Long> {

    List<UserRole> findUserRoles(Long userId, Site site);
    List<UserRole> findUserRoles(Site site);
}