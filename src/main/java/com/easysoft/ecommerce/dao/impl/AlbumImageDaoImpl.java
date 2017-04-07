package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.AlbumImageDao;
import com.easysoft.ecommerce.model.AlbumImage;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AlbumImageDaoImpl extends GenericDaoImpl<AlbumImage, Long> implements AlbumImageDao {
    public List<AlbumImage> findAlbumImages(Long albumId, Integer startPosition, Integer maxResult, String orderByAttr, Long siteId, boolean active) {
        List result = null;
        String hql = null;
        Query query = null;
        if (albumId > 0) {
            if (active) {
                hql = "select n from AlbumImage n where n.album.id =:albumId and n.site.id =:siteId and n.active = 'Y' ";
            } else {
                hql = "select n from AlbumImage n where n.album.id =:albumId and n.site.id =:siteId  ";
            }
            if (StringUtils.isNotBlank(orderByAttr)) hql += " order by n." + orderByAttr;
            query = getSessionFactory().getCurrentSession().createQuery(hql)
                    .setLong("albumId", albumId)
                    .setLong("siteId", siteId);
            if (startPosition != null) query.setFirstResult(startPosition);
            if (maxResult != null) query.setMaxResults(maxResult);

            result = query.list();
        }
        return result;
    }

    @Override
    public List<AlbumImage> getFirstImageOfAlbum(Long siteId) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select i from AlbumImage i where i.id in (select min(ii.id) from AlbumImage ii join ii.album a where a.active = 'Y' and ii.active = 'Y' and ii.site.id =:siteId group by ii.album.id)")
                    .setLong("siteId", siteId);
        return query.list();
    }


    public Long countAlbumImage(Long albumId, Long siteId, boolean active) {
        Long result = 0l;
        String hql = null;
        Query query = null;
        if (albumId > 0) {
            if (active) {
                hql = "select count(n) from AlbumImage n where n.album.id =:albumId and n.site.id =:siteId and n.active = 'Y' ";
            } else {
                hql = "select count(n) from AlbumImage n where n.album.id =:albumId and n.site.id =:siteId  ";
            }
            query = getSessionFactory().getCurrentSession().createQuery(hql)
                    .setLong("albumId", albumId)
                    .setLong("siteId", siteId);

            result = (Long) query.uniqueResult();
        }
        return result;
    }

}
