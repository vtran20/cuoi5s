package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ErrorLogDao;
import com.easysoft.ecommerce.model.ErrorLog;
import org.springframework.stereotype.Repository;

@Repository
public class ErrorLogDaoImpl extends GenericDaoImpl<ErrorLog, Long> implements ErrorLogDao {
}
