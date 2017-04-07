package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ConditionClassDao;
import com.easysoft.ecommerce.dao.RowDao;
import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.Row;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RowDaoImpl extends GenericDaoImpl<Row, Long> implements RowDao {
}
