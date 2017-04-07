package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ConditionClassDao;
import com.easysoft.ecommerce.dao.PromotionClassDao;
import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.PromotionClass;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PromotionClassDaoImpl extends GenericDaoImpl<PromotionClass, Long> implements PromotionClassDao {

    @SuppressWarnings("unchecked")
    @Override
    public PromotionClass getPromotionClass (Long promotionId, String isActive) {
        PromotionClass promotionClass;
        if ("Y".equals(isActive)) {
            promotionClass = (PromotionClass) this.getSessionFactory().getCurrentSession().createQuery(" select a from PromotionClass a join a.promotions p where p.id = :promotionId and a.active = :active").
                    setParameter("promotionId", promotionId)
                    .setParameter("active",isActive)
                    .setCacheable(true)
                    .uniqueResult();
        } else {
            promotionClass = (PromotionClass) this.getSessionFactory().getCurrentSession().createQuery(" select a from ConditionClass a join a.promotions p where p.id = :promotionId and a.active = :active").
                    setParameter("promotionId", promotionId)
                    .setCacheable(true)
                    .uniqueResult();
        }
        return promotionClass;

    }

}
