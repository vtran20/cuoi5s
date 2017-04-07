package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ConditionClassDao;
import com.easysoft.ecommerce.dao.RelatedProductDao;
import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.ProductToProduct;
import com.easysoft.ecommerce.model.Promotion;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ConditionClassDaoImpl extends GenericDaoImpl<ConditionClass, Long> implements ConditionClassDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<ConditionClass> getConditionClasses (Long promotionId, boolean isActive) {
        List <ConditionClass> results;
        if (isActive) {
            results = this.getSessionFactory().getCurrentSession().createQuery(" select a from ConditionClass a join a.promotions p where p.id = :promotionId and a.active = :active").
                    setParameter("promotionId", promotionId)
                    .setParameter("active",isActive)
                    .setCacheable(true)
                    .list();
        } else {
            results = this.getSessionFactory().getCurrentSession().createQuery(" select a from ConditionClass a join a.promotions p where p.id = :promotionId and a.active = :active").
                    setParameter("promotionId", promotionId)
                    .setCacheable(true)
                    .list();
        }
        return results;

    }

}
