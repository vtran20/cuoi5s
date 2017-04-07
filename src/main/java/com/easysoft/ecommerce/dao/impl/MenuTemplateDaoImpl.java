package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.MenuDao;
import com.easysoft.ecommerce.dao.MenuTemplateDao;
import com.easysoft.ecommerce.model.Menu;
import com.easysoft.ecommerce.model.MenuTemplate;
import com.easysoft.ecommerce.model.Site;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuTemplateDaoImpl extends GenericDaoImpl<MenuTemplate, Long> implements MenuTemplateDao {

    @Override
    public List<MenuTemplate> getMenuTemplatesNotUsed(Site site) {
        return this.getSessionFactory().getCurrentSession().createQuery("select mt from MenuTemplate mt where mt.uri not in (select m.uri from Menu m where m.site.id = :siteId and m.menuTemplate = 'Y') order by sequence").
                setParameter("siteId", site.getId()).list();
    }
}