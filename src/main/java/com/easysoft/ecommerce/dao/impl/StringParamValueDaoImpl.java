package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.StringParamDao;
import com.easysoft.ecommerce.dao.StringParamValueDao;
import com.easysoft.ecommerce.model.StringParam;
import com.easysoft.ecommerce.model.StringParamValue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StringParamValueDaoImpl extends GenericDaoImpl<StringParamValue, Long> implements StringParamValueDao {

    @SuppressWarnings("unchecked")
    public List<StringParamValue> getStringParamValues (String stringParam, String language) {
        return (List<StringParamValue>) getSessionFactory().getCurrentSession().createQuery(
                "select v from StringParamValue v join v.stringParam c where c.key = :stringParam and v.language = :language order by v.sequence")
                .setParameter("stringParam", stringParam)
                .setParameter("language", language)
                .setCacheable(true)
                .list();

    }

    @SuppressWarnings("unchecked")
    public StringParamValue getStringParamValue (String stringParam, String language, String stringParamKey) {
        return (StringParamValue) getSessionFactory().getCurrentSession().createQuery(
                "select v from StringParamValue v join v.stringParam c where c.key = :stringParam and v.language = :language and v.key = :stringParamKey")
                .setParameter("stringParam", stringParam)
                .setParameter("language", language)
                .setParameter("stringParamKey", stringParamKey)
                .uniqueResult();
    }

}
