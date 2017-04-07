package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.VideoDao;
import com.easysoft.ecommerce.service.CategoryService;
import com.easysoft.ecommerce.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoServiceImpl.class);



    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private VideoDao videoDao;


    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public VideoDao getVideoDao() {
        return videoDao;
    }
}
