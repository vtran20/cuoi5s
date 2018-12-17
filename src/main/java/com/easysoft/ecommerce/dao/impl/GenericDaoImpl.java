package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.GenericDao;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;


/**
 * A Generic DAO class.
 *
 * @param <T> entity type
 * @param <ID> primary key type
 */
@Transactional
public abstract class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    private Class<T> persistentClass;

    private SessionFactory sessionFactory;

    /**
     * Default constructor.
     */
    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    /**
     * Returns the FullTextSession object to be used in searching based on Lucene indexes
     * @return FullTextSession object
     */
    public FullTextSession getFullTextSession(){
    	FullTextSession fullTextEntityManager = Search.getFullTextSession(sessionFactory.getCurrentSession());
        return fullTextEntityManager;
    }

    public Class<T> getPersistentClass() {
        return this.persistentClass;
    }

    public void flush() {
    	this.sessionFactory.getCurrentSession().flush();
    }

    public void clear() {
    	this.sessionFactory.getCurrentSession().clear();
    }

    // FIND
    @SuppressWarnings("unchecked")
	public T findById(ID id, Long siteId) {
        return (T) findUniqueBy ("id", id, siteId);
    }
	public T findByIdByStore(ID id, Long storeId) {
        return (T) findUniqueByStore ("id", id, storeId);
    }
    @SuppressWarnings("unchecked")
	public T findById(ID id) {
        return (T) this.sessionFactory.getCurrentSession().get(getPersistentClass(), id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return findObjectOrAttributeBy(null, null, null, null, null, null);
    }
    @SuppressWarnings("unchecked")
    public List<T> findAll(Long siteId) {
        return findObjectOrAttributeBy(null, null, null, null, null, siteId);
    }

    public List<T> findAllByStore(Long storeId) {
        return findObjectOrAttributeByStore(null, null, null, null, null, storeId);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAllOrder(String orderAttr) {
        return findObjectOrAttributeBy(null, null, null, null, orderAttr, null);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAllOrder(String orderAttr, Long siteId) {
        return findObjectOrAttributeBy(null, null, null, null, orderAttr, siteId);
    }

    /**
     *
     * @param attributeName which column want to return (only 1 column), if null, return all column
     * @param column ex:  'column' in (1,2,3)
     * @param ids ex: List of id
     * @param startPosition
     * @param maxResult
     * @param orderByAttr
     * @return
     */
    public List<T> findObjectInBy(String attributeName, String column, List<Long> ids, Integer startPosition, Integer maxResult,
			String orderByAttr, Long siteId) {
        String ejbqlString = "select " + (attributeName != null ? "o." + attributeName : "o") + " from " + getPersistentClass().getName() + " o ";
        String criteria = "";
        if (!StringUtils.isEmpty(column) && ids != null && ids.size() > 0) {
                criteria = " o."+column+" in ( :ids ) ";
        }

        if (siteId != null && siteId > 0) {
            if (!StringUtils.isEmpty(criteria)) {
                criteria += " and ";
            }
            criteria += " o.site.id = :siteId";
        }

        if (StringUtils.isNotBlank(criteria)) ejbqlString += " where " + criteria;

        if (StringUtils.isNotBlank(orderByAttr)) ejbqlString += " order by o." + orderByAttr;
        Query query = this.sessionFactory.getCurrentSession().createQuery(ejbqlString);

        if (siteId != null && siteId > 0) {
            query.setParameter("siteId", siteId);
        }

        if (!StringUtils.isEmpty(column) && ids != null && ids.size() > 0) {
            query.setParameterList("ids", ids);
        }

        if (startPosition != null) query.setFirstResult(startPosition);
        if (maxResult != null) query.setMaxResults(maxResult);

        return query.list();
    }

    public List<T> findObjectInBy(String attributeName, String column, String ids, Integer startPosition, Integer maxResult, String orderByAttr, Long siteId) {
        List<Long> idList = null;
        if (StringUtils.isNotBlank(ids)) {
            String[] array = ids.split(",");
            idList = new ArrayList<Long>();
            for (String a : array) {
                if (StringUtils.isNotBlank(a)) {
                    idList.add(Long.valueOf(a.trim()));
                }
            }
        }
        return this.findObjectInBy(attributeName, column, idList, startPosition, maxResult, orderByAttr, siteId);
    }
    @SuppressWarnings("rawtypes")
	private List findObjectOrAttributeBy(String attributeName, Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult,
			String orderByAttr, Long siteId) {
        List result;
        String ejbqlString = "select " + (attributeName != null ? "o." + attributeName : "o") + " from " + getPersistentClass().getName() + " o ";
        List<Object> paramValues = new ArrayList<Object>();

        String criteria = "";
        if (criteriaValues != null && !criteriaValues.isEmpty()) {
            int index = 1;
            for (Map.Entry<String, Object> entry : criteriaValues.entrySet()) {
                String propertyName = entry.getKey();
                Object value = entry.getValue();

                String criterion;
                if (value != null) {
                    criterion = " o." + propertyName + " = ? ";
                    paramValues.add(value);
                    index ++;
                } else {
                    criterion = " o." + propertyName + " is null ";
                }

                if (!StringUtils.isEmpty(criteria)) {
                    criteria += " and ";
                }
                criteria += criterion;
            }
        }

        if (siteId != null && siteId > 0) {
            if (!StringUtils.isEmpty(criteria)) {
                criteria += " and ";
            }
            criteria += " o.site.id = :siteId";
        }

        if (StringUtils.isNotBlank(criteria)) ejbqlString += " where " + criteria;

        if (StringUtils.isNotBlank(orderByAttr)) ejbqlString += " order by o." + orderByAttr;
        Query query = this.sessionFactory.getCurrentSession().createQuery(ejbqlString);
        for (int i = 0; i < paramValues.size(); i++) {
            query.setParameter(i, paramValues.get(i));
        }

        if (siteId != null && siteId > 0) {
            query.setParameter("siteId", siteId);
        }

        if (startPosition != null) query.setFirstResult(startPosition);
        if (maxResult != null) query.setMaxResults(maxResult);

        result = query.list();

        return result;
    }
    @SuppressWarnings("rawtypes")
	private List findObjectOrAttributeByStore(String attributeName, Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult,
			String orderByAttr, Long storeId) {
        List result;
        String ejbqlString = "select " + (attributeName != null ? "o." + attributeName : "o") + " from " + getPersistentClass().getName() + " o ";
        List<Object> paramValues = new ArrayList<Object>();

        String criteria = "";
        if (criteriaValues != null && !criteriaValues.isEmpty()) {
            int index = 1;
            for (Map.Entry<String, Object> entry : criteriaValues.entrySet()) {
                String propertyName = entry.getKey();
                Object value = entry.getValue();

                String criterion;
                if (value != null) {
                    criterion = " o." + propertyName + " = ? ";
                    paramValues.add(value);
                    index ++;
                } else {
                    criterion = " o." + propertyName + " is null ";
                }

                if (!StringUtils.isEmpty(criteria)) {
                    criteria += " and ";
                }
                criteria += criterion;
            }
        }

        if (storeId != null && storeId > 0) {
            if (!StringUtils.isEmpty(criteria)) {
                criteria += " and ";
            }
            criteria += " o.store.id = :storeId";
        }

        if (StringUtils.isNotBlank(criteria)) ejbqlString += " where " + criteria;

        if (StringUtils.isNotBlank(orderByAttr)) ejbqlString += " order by o." + orderByAttr;
        Query query = this.sessionFactory.getCurrentSession().createQuery(ejbqlString);
        for (int i = 0; i < paramValues.size(); i++) {
            query.setParameter(i, paramValues.get(i));
        }

        if (storeId != null && storeId > 0) {
            query.setParameter("storeId", storeId);
        }

        if (startPosition != null) query.setFirstResult(startPosition);
        if (maxResult != null) query.setMaxResults(maxResult);

        result = query.list();

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<T> findBy(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult) {
        return findObjectOrAttributeBy(null, criteriaValues, startPosition, maxResult, null, null);
    }
    @SuppressWarnings("unchecked")
    public List<T> findBy(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, Long siteId) {
        return findObjectOrAttributeBy(null, criteriaValues, startPosition, maxResult, null, siteId);
    }

    @SuppressWarnings("unchecked")
    public List<T> findByOrder(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, String orderAttr) {
        return findObjectOrAttributeBy(null, criteriaValues, startPosition, maxResult, orderAttr, null);
    }
    @SuppressWarnings("unchecked")
    public List<T> findByOrder(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, String orderAttr, Long siteId) {
        return findObjectOrAttributeBy(null, criteriaValues, startPosition, maxResult, orderAttr, siteId);
    }
    @SuppressWarnings("unchecked")
    public List<T> findActiveByOrder(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, String orderAttr, Long siteId) {
        if (criteriaValues != null) {
            criteriaValues.put("active", "Y");
        } else {
            criteriaValues = new HashMap<String, Object>();
            criteriaValues.put("active", "Y");
        }
        return findObjectOrAttributeBy(null, criteriaValues, startPosition, maxResult, orderAttr, siteId);
    }

    @SuppressWarnings("unchecked")
    public T findUniqueBy(Map<String, Object> criteriaValues) {
        List<T> result = findObjectOrAttributeBy(null, criteriaValues, null, 1, null, null);
        return result.isEmpty() ? null : result.get(0);
    }
    @SuppressWarnings("unchecked")
    public T findUniqueBy(Map<String, Object> criteriaValues, Long siteId) {
        List<T> result = findObjectOrAttributeBy(null, criteriaValues, null, 1, null, siteId);
        return result.isEmpty() ? null : result.get(0);
    }
    public T findUniqueByStore(Map<String, Object> criteriaValues, Long storeId) {
        List<T> result = findObjectOrAttributeByStore(null, criteriaValues, null, 1, null, storeId);
        return result.isEmpty() ? null : result.get(0);
    }

    public List<T> findBy(String propName, Object propVal) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        return findBy(criteriaValues, null, null);
    }
    public List<T> findBy(String propName, Object propVal, Long siteId) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        return findBy(criteriaValues, null, null, siteId);
    }

    public List<T> findByOrder(String propName, Object propVal, String orderAttr) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        return findByOrder(criteriaValues, null, null, orderAttr);
    }
    public List<T> findActiveByOrder(String propName, Object propVal, String orderAttr) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        criteriaValues.put("active", "Y" );
        return findByOrder(criteriaValues, null, null, orderAttr);
    }
    public List<T> findByOrder(String propName, Object propVal, String orderAttr, Long siteId) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        return findByOrder(criteriaValues, null, null, orderAttr, siteId);
    }
    public List<T> findActiveByOrder(String propName, Object propVal, String orderAttr, Long siteId) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        criteriaValues.put("active", "Y" );
        return findByOrder(criteriaValues, null, null, orderAttr, siteId);
    }

    public T findUniqueBy(String propName, Object propVal) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        return findUniqueBy(criteriaValues);
    }
    public T findUniqueBy(String propName, Object propVal, Long siteId) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        return findUniqueBy(criteriaValues, siteId);
    }
    public T findUniqueByStore(String propName, Object propVal, Long storeId) {
        Map<String, Object> criteriaValues = new Hashtable<String, Object>();
        if (StringUtils.isNotBlank(propName)) {
            criteriaValues.put(propName, propVal );
        }
        return findUniqueByStore(criteriaValues, storeId);
    }
    public T findFirstItemOrderByCreatedDate(Map <String, Object>criteriaValues, Long siteId) {
        List<T> result = findObjectOrAttributeBy(null, criteriaValues, null, 1, "createdDate desc", siteId);
        return result.isEmpty() ? null : result.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<Object> findAttributeBy(String attribute, Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult) {
        return findObjectOrAttributeBy(attribute, criteriaValues, startPosition, maxResult, null, null);
    }
    @SuppressWarnings("unchecked")
    public List<Object> findAttributeBy(String attribute, Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, Long siteId) {
        return findObjectOrAttributeBy(attribute, criteriaValues, startPosition, maxResult, null, siteId);
    }

    // PERSIST - Should use
    public void persist(T object) {
    	this.sessionFactory.getCurrentSession().persist(object);
    }

    public void merge(T object) {
    	this.sessionFactory.getCurrentSession().merge(object);
    }

    // REMOVE
    @SuppressWarnings("unchecked")
	public void remove(T object) {
        if (!this.sessionFactory.getCurrentSession().contains(object)) {
            // if object isn't managed by EM, load it into EM
            object = (T) this.sessionFactory.getCurrentSession().merge(object);
        }
        // object is now a managed object so it can be removed.
        this.sessionFactory.getCurrentSession().delete(object);
    }

    ////////////////Support Multisite/////////////////
    public Float getMaxSequence (Long siteId) {
        Object result = this.sessionFactory.getCurrentSession()
                .createQuery("select max (o.sequence) from " + getPersistentClass().getName() + " o where o.site.id = :siteId")
                .setParameter("siteId", siteId).uniqueResult();
        if (result != null) {
            return new Float(result+"");
        } else {
            return 0f;
        }
    }
    public Float getMaxSequenceBy (String field, Long id) {
        Object result = this.sessionFactory.getCurrentSession()
                .createQuery("select max (o.sequence) from " + getPersistentClass().getName() + " o where o."+field+" = :id")
                .setParameter("id", id).uniqueResult();
        if (result != null) {
            return new Float(result+"");
        } else {
            return 0f;
        }
    }

    @Override
    public Long count() {
        return (Long) this.sessionFactory.getCurrentSession().createCriteria(getPersistentClass()).setProjection(Projections.rowCount()).uniqueResult();
    }
    @Override
    public Long count(String active) {
        if ("Y".equals(active)) {
            return (Long) this.sessionFactory.getCurrentSession()
                    .createQuery("select count(*) from " + getPersistentClass().getName() + " o where o.active = :active")
                    .setParameter("active", active).uniqueResult();
        } else {
            return count();
        }
    }

    @Override
    public Long count(Long siteId) {
        return count("Y", siteId);
    }
    @Override
    public Long count(String active, Long siteId) {
        if ("Y".equals(active)) {
            return (Long)this.sessionFactory.getCurrentSession()
                    .createQuery("select count(*) from " + getPersistentClass().getName() + " o where o.active = :active and o.site.id = :siteId")
                    .setParameter("active", active)
                    .setParameter("siteId", siteId).uniqueResult();
        } else {
            return (Long)this.sessionFactory.getCurrentSession()
                    .createQuery("select count(*) from " + getPersistentClass().getName() + " o where o.site.id = :siteId")
                    .setParameter("siteId", siteId).uniqueResult();
        }
    }
}
