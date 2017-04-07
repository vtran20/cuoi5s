package com.easysoft.ecommerce.dao;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class DaoFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("rawtypes")
    public GenericDao<?,?> getDao(Class<?> entityClass) {
        GenericDao<?,?> result = null;
        Map<String, GenericDao> daos = applicationContext.getBeansOfType(GenericDao.class);
        for (Map.Entry<String, GenericDao> entry :  daos.entrySet()) {
            if (entry.getValue().getPersistentClass() == entityClass) {
                result = entry.getValue();
            }
        }
        return result;
    }
}
