package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.controller.Constants;
import com.easysoft.ecommerce.dao.VideoDao;
import com.easysoft.ecommerce.model.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class VideoDaoImpl extends GenericDaoImpl<Video, Long> implements VideoDao {

    public List<Video> getMostViewVideos (Site site,int numberReturnItem, int numberDay) throws Exception {
        if (numberReturnItem <= 0) numberReturnItem = Constants.NUMBER_VIDEO_RETURN;
        if (numberDay <= 0) numberDay = Constants.NUMBER_DAYS_FOR_MOST_VIEW;
        Calendar currentDate = Calendar.getInstance();
        Date endDate = currentDate.getTime();
        currentDate.add(Calendar.DAY_OF_YEAR, 0-numberDay);
        Date startDate = currentDate.getTime();
        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT c FROM Video c where c.active = 'Y' and c.updatedDate between :startDate and :now ORDER BY c.viewCount desc ")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("now", endDate, new TimestampType()).setMaxResults(numberReturnItem).list();
    }
    public List<Video> getNextMostViewVideos (Site site, int numberReturnItem, int numberDay, int start) throws Exception {
        if (numberReturnItem <= 0) numberReturnItem = Constants.NUMBER_VIDEO_RETURN;
        if (numberDay <= 0) numberDay = Constants.NUMBER_DAYS_FOR_MOST_VIEW;
        Calendar currentDate = Calendar.getInstance();
        Date endDate = currentDate.getTime();
        currentDate.add(Calendar.DAY_OF_YEAR, 0-numberDay);
        Date startDate = currentDate.getTime();
        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT c FROM Video c where c.active = 'Y' and c.updatedDate between :startDate and :now ORDER BY c.viewCount desc ")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("now", endDate, new TimestampType()).setMaxResults(numberReturnItem).setFirstResult(start).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Video> getVideosByCategory(Long categoryId, int startPos, int maxResult, String active, String sortField, boolean reverse) {
        String query = "";
        if ("Y".equals(active)) {
            query = "select a from Video a join a.categories b where a.active = :active and b.active = :active and b.id = :catId ORDER BY  :orderField";
        } else {
            query = "select a from Video a join a.categories b where b.id = :catId ORDER BY  :orderField";
        }
        query = query.replaceFirst(":orderField", buildSortString(sortField, reverse));
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery(query).
                    setParameter("active", active).
                    setParameter("catId", categoryId).
                    setFirstResult(startPos).setMaxResults(maxResult).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery(query).
                    setParameter("catId", categoryId).
                    setFirstResult(startPos).setMaxResults(maxResult).list();
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Video> getVideosByCategory(Long categoryId, int numberDay, int startPos, int maxResult, String active, String sortField, boolean reverse) {

        if (maxResult <= 0) maxResult = Constants.NUMBER_VIDEO_RETURN;
        if (numberDay <= 0) numberDay = Constants.NUMBER_DAYS_FOR_MOST_VIEW;
        Calendar currentDate = Calendar.getInstance();
        Date endDate = currentDate.getTime();
        currentDate.add(Calendar.DAY_OF_YEAR, 0-numberDay);
        Date startDate = currentDate.getTime();

        String query = "";
        if ("Y".equals(active)) {
            query = "select a from Video a join a.categories b where a.active = :active and b.active = :active and b.id = :catId and a.updatedDate between :startDate and :now ORDER BY  :orderField";
        } else {
            query = "select a from Video a join a.categories b where b.id = :catId and a.updatedDate between :startDate and :now ORDER BY  :orderField";
        }
        query = query.replaceFirst(":orderField", buildSortString(sortField, reverse));
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery(query).
                    setParameter("active", active).
                    setParameter("catId", categoryId).
                    setParameter("startDate", startDate, new TimestampType()).
                    setParameter("now", endDate, new TimestampType()).
                    setFirstResult(startPos).setMaxResults(maxResult).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery(query).
                    setParameter("catId", categoryId).
                    setParameter("startDate", startDate, new TimestampType()).
                    setParameter("now", endDate, new TimestampType()).
                    setFirstResult(startPos).setMaxResults(maxResult).list();
        }
    }

    @Override
    public List<Video> getVideosByCategory(Long categoryId, int startPos, int maxResult, String sortField, boolean reverse) {
        return getVideosByCategory(categoryId, startPos, maxResult, "Y", sortField, reverse);
    }

    @Override
    public void removeVideoCategory(Long id) {
        Video cat = this.findById(id);
        List<Category> categories = findCategoriesByVideoId(id);
        cat.removeVideoCategory(categories);

    }

    @Override
    public List<Category> findCategoriesByVideoId (Long id) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select c from Category c join c.videos o where o.id =:id")
                .setLong("id", id);
        return query.list();

    }

    private String buildSortString(String sortField, boolean reverse) {
        StringBuffer sort = new StringBuffer();
        if (StringUtils.isEmpty(sortField)) {
            sort.append("a.sequence asc");
        } else {
            if (reverse) {
                sort.append("a.").append(sortField).append(" desc");
            } else {
                sort.append("a.").append(sortField).append(" asc");
            }
        }
        return sort.toString();
    }

}
