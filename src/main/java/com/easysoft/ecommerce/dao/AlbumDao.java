package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Album;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.Video;

import java.util.List;

public interface AlbumDao extends GenericDao<Album, Long> {
    Album getAlbumFromImage(Site site, Long albumImageId, String active);
}