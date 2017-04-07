package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.AlbumDao;
import com.easysoft.ecommerce.model.Album;
import com.easysoft.ecommerce.model.Site;
import org.springframework.stereotype.Repository;

@Repository
public class AlbumDaoImpl extends GenericDaoImpl<Album, Long> implements AlbumDao {
    public Album getAlbumFromImage(Site site, Long albumImageId, String active) {
        Album menu;
        if ("Y".equals(active)) {
            menu = (Album) this.getSessionFactory().getCurrentSession().createQuery("select a from Album a join a.images r where a.site.id = :siteId and r.id = :albumImageId and a.active='Y'").
                    setParameter("siteId", site.getId()).
                    setParameter("albumImageId", albumImageId).uniqueResult();
        } else {
            menu = (Album) this.getSessionFactory().getCurrentSession().createQuery("select a from Album a join a.images r where a.site.id = :siteId and r.id = :albumImageId").
                    setParameter("siteId", site.getId()).
                    setParameter("albumImageId", albumImageId).uniqueResult();
        }

        return menu;
    }

}
