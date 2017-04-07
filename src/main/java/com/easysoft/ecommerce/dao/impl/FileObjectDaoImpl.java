package com.easysoft.ecommerce.dao.impl;

import org.springframework.stereotype.Repository;

import com.easysoft.ecommerce.dao.FileObjectDao;
import com.easysoft.ecommerce.model.FileObject;

@Repository
public class FileObjectDaoImpl extends GenericDaoImpl<FileObject, Long> implements FileObjectDao {
}
