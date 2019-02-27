package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.NailService;

import java.util.List;

public interface NailServiceDao extends GenericDao<NailService, Long> {
    List<NailService> getGroupServices(Long storeId) throws Exception;
    List<NailService> getServices(Long groupId, Long storeId) throws Exception;
}