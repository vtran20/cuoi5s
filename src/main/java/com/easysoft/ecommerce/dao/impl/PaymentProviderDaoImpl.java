package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.PaymentProviderDao;
import com.easysoft.ecommerce.model.PaymentProvider;
import com.easysoft.ecommerce.model.Site;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentProviderDaoImpl extends GenericDaoImpl<PaymentProvider, Long> implements PaymentProviderDao {


    @Override
    public List<PaymentProvider> findAllPaymentProviders(String active, String orderBy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("active", active);
        if ("Y".equals(active)) {
            return findByOrder(map, null, null, orderBy);
        } else {
            return findByOrder(null, null, null, orderBy);
        }
    }

    @Override
    public List<PaymentProvider> getPaymentProvidersNotUsed(Site site) {
        return this.getSessionFactory().getCurrentSession().createQuery("select pp from PaymentProvider pp where pp.active = 'Y' and pp.id not in (select p.paymentProvider.id from PaymentProviderSite p where p.site.id = :siteId ) order by pp.sequence").
                setParameter("siteId", site.getId()).list();
    }
    @Override
    public PaymentProvider getPaymentProviderSelected(Long paymentMethodSiteId) {
        return (PaymentProvider) this.getSessionFactory().getCurrentSession().createQuery("select pp from PaymentProvider pp join pp.paymentProviderSites s where pp.active = 'Y' and s.id = :paymentMethodSiteId").
                setParameter("paymentMethodSiteId", paymentMethodSiteId).uniqueResult();
    }
    @Override
    public PaymentProvider getPaymentProviderForSite(Long paymentId, Long siteId) {
        return (PaymentProvider) this.getSessionFactory().getCurrentSession().createQuery("select pp from PaymentProvider pp join pp.paymentProviderSites s where pp.active = 'Y' and s.id = :paymentId and s.site.id = :siteId").
                setParameter("paymentId", paymentId).
                setParameter("siteId",siteId).uniqueResult();
    }
}