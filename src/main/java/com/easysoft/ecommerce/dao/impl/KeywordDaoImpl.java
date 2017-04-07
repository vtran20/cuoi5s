package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.KeywordDao;
import com.easysoft.ecommerce.dao.UserDao;
import com.easysoft.ecommerce.model.Keyword;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeywordDaoImpl extends GenericDaoImpl<Keyword, Long> implements KeywordDao {

    @Override
    public Keyword getKeywordByGroup(String keyword, String groupBy, Site site) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select u from Keyword u where u.keyword = :keyword  and u.groupBy = :groupBy and u.site.id = :siteId")
        .setParameter("keyword", keyword)
        .setParameter("groupBy", groupBy)
        .setParameter("siteId", site.getId());

        List temp = query.list();
        if (temp != null && temp.size() == 1) {
            return (Keyword) temp.get(0);
        } else {
            return null;
        }
    }
}