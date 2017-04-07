package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.JobsDao;
import com.easysoft.ecommerce.dao.UserDao;
import com.easysoft.ecommerce.model.Jobs;
import com.easysoft.ecommerce.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobsDaoImpl extends GenericDaoImpl<Jobs, Long> implements JobsDao {


}