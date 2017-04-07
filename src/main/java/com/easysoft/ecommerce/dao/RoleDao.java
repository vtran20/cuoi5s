package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Role;

import java.util.List;

public interface RoleDao extends GenericDao<Role, Long> {
    List<Role> findChildrenRoles (String role);
}