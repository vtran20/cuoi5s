package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.PromotionDao;
import com.easysoft.ecommerce.model.Promotion;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class PromotionDaoImpl extends GenericDaoImpl<Promotion, Long> implements PromotionDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<Promotion> getPromotions(String isActive) {
        List<Promotion> results;
        if ("Y".equals(isActive)) {
            results = this.getSessionFactory().getCurrentSession().createQuery(" select a from Promotion a where a.startDate <= :today and :today <= a.endDate and a.active = :active").
                    setParameter("today", new Date())
                    .setParameter("active", isActive)
                    .list();
        } else {
            results = this.getSessionFactory().getCurrentSession().createQuery(" select a from Promotion a where a.startDate <= :today and :today <= a.endDate").
                    setParameter("today", new Date())
                    .setCacheable(true)
                    .list();
        }
        return results;
    }

    public Promotion getValidPromotion(String promoCode) {
        return (Promotion) this.getSessionFactory().getCurrentSession().createQuery(" select a from Promotion a where ((a.startDate <= :today and :today <= a.endDate) or a.endDate is null) and a.active = 'Y' and a.promoCode = :promoCode").
                setParameter("today", new Date())
                .setParameter("promoCode", promoCode)
                .setCacheable(true)
                .uniqueResult();
    }
}
