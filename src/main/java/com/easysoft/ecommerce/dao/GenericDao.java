package com.easysoft.ecommerce.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A generic DAO interface.
 *
 * @param <T> entity type
 * @param <ID> primary key type
 */
public interface GenericDao<T, ID extends Serializable> {

	Class<T> getPersistentClass();

    /**
     * Loads read-only (detached) entity by ID.
     *
     * @param id ID
     * @return entity
     *
     * @throws Exception if any error happen
     */
    T findById(ID id);
    T findById(ID id, Long siteId);
    T findByIdByStore(ID id, Long storeId);

    /**
     * @deprecated Should use findAll(Long siteId)
     * @return
     */
    List<T> findAll();
    List<T> findAll(Long siteId);
    List<T> findAllByStore(Long storeId);
    /**
     * @deprecated Should use findAllOrder(String orderAttr, Long siteId)
     * @return
     */
    List<T> findAllOrder(String orderAttr);
    List<T> findAllOrder(String orderAttr, Long siteId);
    /**
     * Should use findBy(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, Long siteId);
     * @return
     */
    List<T> findBy(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult);
    List<T> findBy(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, Long siteId);
    /**
     * Should use findBy(String propName, Object propVal, Long siteId);
     * @return
     */
    List<T> findBy(String propName, Object propVal);
    List<T> findBy(String propName, Object propVal, Long siteId);
    /**
     * @deprecated Should use findByOrder(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, String orderAttr, Long siteId);
     * @return
     */
    List<T> findByOrder(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, String orderAttr);
    List<T> findByOrder(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, String orderAttr, Long siteId);
    List<T> findActiveByOrder(Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, String orderAttr, Long siteId);
    /**
     * @deprecated Should use findByOrder(String propName, Object propVal, String orderAttr, Long siteId);
     * @return
     */
    List<T> findByOrder(String propName, Object propVal, String orderAttr);
    List<T> findActiveByOrder(String propName, Object propVal, String orderAttr);
    List<T> findByOrder(String propName, Object propVal, String orderAttr, Long siteId);
    List<T> findActiveByOrder(String propName, Object propVal, String orderAttr, Long siteId);
    /**
     * @deprecated Should use findUniqueBy(Map<String, Object> criteriaValues, Long siteId);
     * @return
     */
    T findUniqueBy(Map<String, Object> criteriaValues);
    T findUniqueBy(Map<String, Object> criteriaValues, Long siteId);
    T findUniqueByStore(Map<String, Object> criteriaValues, Long storeId);

    T findUniqueBy(String propName, Object propVal);
    T findUniqueBy(String propName, Object propVal, Long siteId);
    T findUniqueByStore(String propName, Object propVal, Long storeId);

    /**
     * The function is like findUniqueBy but findFirstItemOrderByCreatedDate will sort by created date and get the first item.
     *
     * @param criteriaValues
     * @param siteId
     * @return
     */
    T findFirstItemOrderByCreatedDate(Map <String, Object>criteriaValues, Long siteId);
    /**
     * @deprecated Should use findAttributeBy(String attribute, Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, Long siteId);
     * @return
     */
    List<Object> findAttributeBy(String attribute, Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult);
    List<Object> findAttributeBy(String attribute, Map<String, Object> criteriaValues, Integer startPosition, Integer maxResult, Long siteId);
    List<T> findObjectInBy(String attributeName, String column, List<Long> ids, Integer startPosition, Integer maxResult, String orderByAttr, Long siteId);
    List<T> findObjectInBy(String attributeName, String column, String ids, Integer startPosition, Integer maxResult, String orderByAttr, Long siteId);

    /**
     * @deprecated Should use count (Long siteId);
     * @return
     */
    Long count ();
    Long count (Long siteId);
    /**
     * @deprecated Should use count (String active, Long siteId);
     * @return
     */
    Long count (String active);
    Long count (String active, Long siteId);
    /**
     * Store <code>object</code> in the database.
     *
     * @param object the instance to save in the database
     */
    public void persist(T object);

    public void merge(T object);

    /**
     * Remove <code>object</code> from the database.
     *
     * @param object the object to be removed from the database
     */
    public void remove(T object);

    /**
     * Taken from the EntityManager documentation, Synchronize the persistence
     * context to the underlying database.
     *
     */
    public void flush();

    /**
     * Taken from the EntityManager documentation: Clear the persistence
     * context, causing all managed entities to become detached. Changes made to
     * entities that have not been flushed to the database will not be
     * persisted.
     *
     */
    public void clear();

    //////////////////////////Support for multi-site///////////////////////////////
    /**
     * Get Max sequence
     * @return max sequence number
     */
    public Float getMaxSequence (Long siteId);
    public Float getMaxSequenceBy (String field, Long id);
}
