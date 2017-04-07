package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.PromotionClass;

import java.util.List;

public interface PromotionClassDao extends GenericDao<PromotionClass, Long> {
    PromotionClass getPromotionClass (Long promotionId, String isActive);
}