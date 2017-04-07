package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Promotion;

import java.util.List;

public interface PromotionDao extends GenericDao<Promotion, Long> {
    List<Promotion> getPromotions (String isActive);
    Promotion getValidPromotion (String promoCode);
}