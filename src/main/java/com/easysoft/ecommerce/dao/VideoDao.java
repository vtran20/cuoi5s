package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.*;

import java.util.List;

public interface VideoDao extends GenericDao<Video, Long> {

    List<Video> getMostViewVideos (Site site, int numberReturnItem, int numberDay) throws Exception;
    List<Video> getNextMostViewVideos (Site site, int numberReturnItem, int numberDay, int start) throws Exception;
    List<Video> getVideosByCategory(Long categoryId, int startPos, int maxResult, String active, String sortField, boolean reverse);
    List<Video> getVideosByCategory(Long categoryId, int startPos, int maxResult, String sortField, boolean reverse);
    List<Video> getVideosByCategory(Long categoryId, int numberDay, int startPos, int maxResult, String active, String sortField, boolean reverse);
    void removeVideoCategory(Long videoId);
    List<Category> findCategoriesByVideoId (Long videoId) ;
}