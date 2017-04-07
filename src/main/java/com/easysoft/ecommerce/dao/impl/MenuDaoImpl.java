package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.MenuDao;
import com.easysoft.ecommerce.model.Menu;
import com.easysoft.ecommerce.model.Site;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuDaoImpl extends GenericDaoImpl<Menu, Long> implements MenuDao {

    @Override
    public List<Menu> getRootMenus(Site site) {
        return getRootMenus(site, "Y");
    }

    @Override
    public List<Menu> getRootMenus(Site site, String active) {
        return getRootMenus(site, active, "Y");
    }

    @Override
    public List<Menu> getRootMenus(Site site, String active, String headerMenu) {
        if ("Y".equals(active)) {
            if ("Y".equals(headerMenu)) {
                return this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.parentMenu is null and a.active='Y' and a.headerMenu ='Y' order by sequence asc").
                        setParameter("siteId", site.getId()).list();
            } else {
                return this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.parentMenu is null and a.active='Y' order by sequence asc").
                        setParameter("siteId", site.getId()).list();
            }
        } else {
            if ("Y".equals(headerMenu)) {
                return this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.parentMenu is null and a.headerMenu ='Y' order by sequence asc").
                        setParameter("siteId", site.getId()).list();
            } else {
                return this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.parentMenu is null order by sequence asc").
                        setParameter("siteId", site.getId()).list();
            }
        }
    }

    @Override
    public List<Menu> getSubMenus(Site site, Menu menu) {
        return getSubMenus(site, menu, "Y");
    }

    @Override
    public List<Menu> getSubMenus(Site site, Menu menu, String active) {
        return getSubMenus(site, menu, active, "Y");
    }

    @Override
    public List<Menu> getSubMenus(Site site, Menu menu, String active, String headerMenu) {
        if ("Y".equals(active)) {
            if ("Y".equals(headerMenu)) {
                return this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.parentMenu.id = :menuId and a.active='Y' and a.headerMenu ='Y' order by sequence asc").
                        setParameter("siteId", site.getId()).
                        setParameter("menuId", menu.getId()).list();
            } else {
                return this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.parentMenu.id = :menuId and a.active='Y' order by sequence asc").
                        setParameter("siteId", site.getId()).
                        setParameter("menuId", menu.getId()).list();
            }
        } else {
            if ("Y".equals(headerMenu)) {
                return this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.parentMenu.id = :menuId and a.headerMenu ='Y' order by sequence asc").
                        setParameter("siteId", site.getId()).
                        setParameter("menuId", menu.getId()).list();
            } else {
                return this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.parentMenu.id = :menuId order by sequence asc").
                        setParameter("siteId", site.getId()).
                        setParameter("menuId", menu.getId()).list();
            }
        }
    }

    @Override
    public Menu getParentMenu(Site site, Long menuId) {
        return getParentMenu(site, menuId, "Y");
    }

    @Override
    public Menu getParentMenu(Site site, Long menuId, String active) {
        List <Menu> menus;
        if ("Y".equals(active)) {
            menus = this.getSessionFactory().getCurrentSession().createQuery("select a.parentMenu from Menu a  where a.site.id = :siteId and a.id = :menuId and a.parentMenu.active = 'Y'").
                    setParameter("siteId", site.getId()).setLong("menuId", menuId).list();
        } else {
            menus = this.getSessionFactory().getCurrentSession().createQuery("select a.parentMenu from Menu a  where a.site.id = :siteId and a.id = :menuId").
                    setParameter("siteId", site.getId()).setLong("menuId", menuId).list();
        }

        if (menus != null && menus.size() > 0) {
            return menus.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Menu getMenu(Site site, Long menuId) {
        return getMenu(site, menuId, "Y");
    }

    @Override
    public Menu getMenu(Site site, Long menuId, String active) {
        List <Menu> menus;
        if ("Y".equals(active)) {
            menus = this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.id = :menuId and a.active='Y'").
                    setParameter("siteId", site.getId()).
                    setParameter("menuId", menuId).list();
        } else {
            menus = this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.id = :menuId").
                    setParameter("siteId", site.getId()).
                    setParameter("menuId", menuId).list();
        }
        if (menus != null && menus.size() > 0) {
            return menus.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Menu getMenu(Site site, String uri) {
        return getMenu(site, uri, "Y");
    }

    @Override
    public Menu getMenu(Site site, String uri, String active) {
        List <Menu> menus;
        if ("Y".equals(active)) {
            menus = this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.uri = :uri and a.active='Y'").
                    setParameter("siteId", site.getId()).
                    setParameter("uri", uri).list();
        } else {
            menus = this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a where a.site.id = :siteId and a.uri = :uri").
                    setParameter("siteId", site.getId()).
                    setParameter("uri", uri).list();
        }

        if (menus != null && menus.size() > 0) {
            return menus.get(0);
        } else {
            return null;
        }
    }

    public Menu getMenuFromRow(Site site, Long rowId, String active) {
        Menu menu;
        if ("Y".equals(active)) {
            menu = (Menu) this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a join a.rows r where a.site.id = :siteId and r.id = :rowId and a.active='Y'").
                    setParameter("siteId", site.getId()).
                    setParameter("rowId", rowId).uniqueResult();
        } else {
            menu = (Menu) this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a join a.rows r where a.site.id = :siteId and r.id = :rowId").
                    setParameter("siteId", site.getId()).
                    setParameter("rowId", rowId).uniqueResult();
        }

        return menu;
    }

    public Menu getMenuFromPartContent(Site site, Long partContentId, String active) {
        Menu menu;
        if ("Y".equals(active)) {
            menu = (Menu) this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a join a.rows r join r.partContents c where a.site.id = :siteId and c.id = :partContentId and a.active='Y'").
                    setParameter("siteId", site.getId()).
                    setParameter("partContentId", partContentId).uniqueResult();
        } else {
            menu = (Menu) this.getSessionFactory().getCurrentSession().createQuery("select a from Menu a join a.rows r join r.partContents c where a.site.id = :siteId and c.id = :partContentId").
                    setParameter("siteId", site.getId()).
                    setParameter("partContentId", partContentId).uniqueResult();
        }

        return menu;
    }

}