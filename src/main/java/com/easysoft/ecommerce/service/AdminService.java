package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.util.Messages;

import java.util.List;
import java.util.Map;


public interface AdminService {
    /**
     * This method is used for insert or update menu.
     *
     * @param entity :  Menu information
     * @param parentId : Menu parent Id
     * @return
     * @throws Exception
     */
    Messages createOrUpdateMenu(Menu entity, Long parentId) throws Exception;

    /**
     * This method is used for:
     * 1. Update/insert page.
     * 2. Update menu information.
     * 3. Update preContent, preHeader, preFooter if any. Note: pass null if don't want to update.
     *
     * This method is called when design menu page.
     *
     * @param preContent
     * @param preHeader
     * @param preFooter
     * @param menu
     * @return
     */
    Messages createOrUpdatePage(String preContent, String preHeader, String preFooter, Menu menu);

    /**
     * This method is used for:
     * 1. Update/insert page.
     * 2. Update preContent, preHeader, preFooter if any. Note: pass null if don't want to update.
     *
     * This method is called when design a regular page.
     *
     * @param preContent
     * @param preHeader
     * @param preFooter
     * @return
     */
    Messages createOrUpdatePage(String preContent, String preHeader, String preFooter);

    Messages createOrUpdateNewsCategory(NewsCategory entity, Long parentId);

    /**
     * Publish page by copy preContent to Content column, preHeader to Header and preFooter to Footer column.
     *
     * @param page
     * @return
     */
    Messages publishPage (Menu page);

    /**
     * This method is used for:
     * 1. Update/insert news.
     * 2. Note: Update pre-Content.
     *
     * This method is called when design a regular page.
     *
     * @param news
     * @param map
     * @return
     */
    Messages createOrUpdateNews(News news, Map map);
    public Messages copyNews(Long id);
    /**
     * Insert new or update Video.
     *
     * @param videos list video
     * @param categoryIds which associate with video.
     * @return
     */
    Messages createOrUpdateVideos(List<Video> videos, String categoryIds);

    /**
     * Publish news by copy pre-Content to Content column.
     *
     * @param news
     * @return
     */
    Messages publishNews (News news);

    Messages deleteMenu(Long id);

    Messages createOrUpdatePartContent(SiteMenuPartContent entity, Long menuId);

    Messages createOrUpdateRow(Row entity, Long menuId, Long widgetTemplateId);
}

