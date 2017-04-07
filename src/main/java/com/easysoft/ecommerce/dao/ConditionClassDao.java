package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.UserSession;
import com.easysoft.ecommerce.model.session.SessionObject;

import java.io.IOException;
import java.util.List;

public interface ConditionClassDao extends GenericDao<ConditionClass, Long> {
    List<ConditionClass> getConditionClasses (Long promotionId, boolean isActive);
}