package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Keyword;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;

import java.util.List;

public interface KeywordDao extends GenericDao<Keyword, Long> {

    Keyword getKeywordByGroup(String keyword, String group, Site site);

}