package com.easysoft.ecommerce.service;


public interface SEOService {

    String id2uri(Class<?> entityClass, Long id, Long siteId) throws Exception;
    Object uri2object(String uri, Long siteId) throws Exception;

    String getSEOName(String name) throws Exception;
}
