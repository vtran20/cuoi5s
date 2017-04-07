package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.AlbumImage;

import java.util.List;
import java.util.Map;

public interface AlbumImageDao extends GenericDao<AlbumImage, Long> {
    List<AlbumImage> findAlbumImages(Long albumId, Integer startPosition, Integer maxResult, String orderByAttr, Long siteId, boolean active);
    List<AlbumImage> getFirstImageOfAlbum(Long siteId);
    Long countAlbumImage(Long albumId, Long siteId, boolean active);

}