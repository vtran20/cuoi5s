package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Menu;
import com.easysoft.ecommerce.model.Site;

import java.util.List;


public interface MenuDao extends GenericDao<Menu, Long> {
    List<Menu> getRootMenus(Site site);

    List<Menu> getSubMenus(Site site, Menu menu);

    List<Menu> getRootMenus(Site site, String active);
    List<Menu> getRootMenus(Site site, String active, String headerMenu);

    List<Menu> getSubMenus(Site site, Menu menu, String active);
    List<Menu> getSubMenus(Site site, Menu menu, String active, String headerMenu);

    Menu getParentMenu(Site site, Long menuId);
    Menu getParentMenu(Site site, Long menuId, String active);

    Menu getMenu(Site site, Long menuId);
    Menu getMenu(Site site, Long menuId, String active);
    Menu getMenu(Site site, String uri);
    Menu getMenu(Site site, String uri, String active);

    Menu getMenuFromRow(Site site, Long rowId, String active);
    Menu getMenuFromPartContent(Site site, Long partContentId, String active);

}