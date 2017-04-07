package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.MenuDao;
import com.easysoft.ecommerce.model.Menu;
import com.easysoft.ecommerce.model.MenuTemplate;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value="/admin/menu")
public class MenuAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuAdminController.class);

    @Autowired
    private MenuDao menuDao;
//    @Autowired
//    private PageDao pageDao;
    @Autowired
    private ServiceLocator serviceLocator;


    /***************************************************************************************************
     *  Implement Menu Management
     ***************************************************************************************************/
    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/menu/" + action;
    }

    @RequestMapping(value = "insert.html,update.html,addmenutemplate.html", method = RequestMethod.GET)
    public String updateGetMethod() throws Exception {
        return "admin/menu/index";
    }

    @RequestMapping(value = "{menuId}/index.html", method = RequestMethod.GET)
    public ModelAndView editForm(@PathVariable Long menuId) throws Exception {
        Menu menu = menuDao.findById(menuId);
        if (menu != null) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            Map map = new HashMap();
            map.put("menu", menuDao.findById(menuId));
            return new ModelAndView("admin/menu/form", map);
        } else {
            return new ModelAndView("admin/menu/form");
        }
    }

    /**
     * This method is called when insert a menu from admin
     */
    @RequestMapping(value = {"insert.html","update.html"}, method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String insert(@Valid Menu entity, @Valid Long parentId, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            //TODO: show error messages
        }
        if (!entity.isEmptyId()) {
            //Clear Cache
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromMenu(null, entity.getId()));
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
        }
        return serviceLocator.getAdminService().createOrUpdateMenu(entity, parentId).toString();
    }

    /**
     * This will be called when delete a Menu
     */
    @RequestMapping(value = "delete.html", method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String delete(@Valid Long id) {
        Messages messages = new Messages();
        try {
            if (id > 0) {
                //Clear Cache
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromMenu(null, id));
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
                messages = serviceLocator.getAdminService().deleteMenu (id);
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            messages.addError("ERROR: Cannot delete parent menu. Please delete children menus first");
        }
        return messages.toString();
    }

    /**
     * This method is called when insert a menu template
     */
    @RequestMapping(value = "addmenutemplate.html", method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String addMenuTemplate(HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        String []menuTemplates = request.getParameterValues("menutemplateId");
        if (menuTemplates != null) {
            for (String menuTemplate : menuTemplates) {
                Long menuTemplateId = Long.parseLong(menuTemplate);
                if (menuTemplateId > 0) {
                    MenuTemplate mt = serviceLocator.getMenuTemplateDao().findById(menuTemplateId);
                    if (mt != null) {
                        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
                        float maxSequence = serviceLocator.getMenuDao().getMaxSequence(site.getId());
                        Menu menu = new Menu();
                        menu.setActive("Y");
                        menu.setSequence(maxSequence+1f);
                        menu.setUpdatedDate(new Date());
                        menu.setMenuTemplate("Y");
                        menu.setName(mt.getName());
                        menu.setUri(mt.getUri());
                        menu.setDesignUrl(mt.getDesignUrl());
                        menu.setSite(site);
                        menuDao.persist(menu);
                        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                    }
                }
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }


    @RequestMapping(value = "reorder.html", method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String  reOrder(HttpServletRequest request, @Valid Integer currentItem,  @Valid String orderList) throws Exception {
        Messages messages = new Messages();

        ObjectMapper objectMapper = new ObjectMapper();
        List<HashMap> arrayLists = null;//new ArrayList<HashMap>();
        arrayLists = objectMapper.readValue(orderList, ArrayList.class);
        Map before = null;
        Map current = null;
        Map after = null;
        Map parentLevel = null;
        boolean done = false;
        if (arrayLists == null) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            return messages.toString();
        }
        for (int i = 0; i<arrayLists.size(); i++) {
            if (!done) {
                Map itemLevel1 = arrayLists.get(i);
                Integer idLevel1 = (Integer) itemLevel1.get("id");
                if (idLevel1 != null && idLevel1.equals(currentItem)) {
                    //item before
                    if (i - 1 >= 0) before = arrayLists.get(i-1);
                    //current item
                    current = itemLevel1;
                    //item after that
                    if (i+1 < arrayLists.size()) after = arrayLists.get(i+1);
                    //parent item is always null
                    done = true;
                }
                //Check and see if this menu has children
                if (itemLevel1.get("children") != null) {
                    List<HashMap> children1 = (List<HashMap>) itemLevel1.get("children");
                    for (int j = 0; j<children1.size(); j++) {
                        if (!done) {
                            Map itemLevel2 = children1.get(j);
                            Integer idLevel2 = (Integer) itemLevel2.get("id");
                            if (idLevel2 != null && idLevel2.equals(currentItem)) {
                                //item before
                                if (j - 1 >= 0) before = children1.get(j-1);
                                //current item
                                current = itemLevel2;
                                //item after that
                                if (j+1 < children1.size()) after = children1.get(j+1);
                                //parent item
                                parentLevel = itemLevel1;
                                done = true;
                            }
                            //Check and see if this menu has children
                            if (itemLevel2.get("children") != null) {
                                List<HashMap> children2 = (List<HashMap>) itemLevel2.get("children");
                                for (int k = 0; k<children2.size(); k++) {
                                    if (!done) {
                                        Map itemLevel3 = children2.get(k);
                                        Integer idLevel3 = (Integer) itemLevel3.get("id");
                                        if (idLevel3 != null && idLevel3.equals(currentItem)) {
                                            //item before
                                            if (k - 1 >= 0) before = children2.get(k-1);
                                            //current item
                                            current = itemLevel3;
                                            //item after that
                                            if (k+1 < children2.size()) after = children2.get(k+1);
                                            //parent item
                                            parentLevel = itemLevel2;
                                            done = true;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    }

                }
            } else {
                break;
            }
        }

        Menu menuBefore = null;
        Menu menuCurrent = null;
        Menu menuAfter = null;
        Menu menuParent = null;

        if (current != null) menuCurrent = menuDao.findById(Long.valueOf((Integer) current.get("id")));
        if (menuCurrent == null) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            return messages.toString();
        }
        if (before != null) menuBefore = menuDao.findById(Long.valueOf((Integer) before.get("id")));
        if (after != null) menuAfter = menuDao.findById(Long.valueOf((Integer) after.get("id")));
        if (parentLevel != null) menuParent = menuDao.findById(Long.valueOf((Integer) parentLevel.get("id")));

        float sequence = menuCurrent.getSequence();
        if (menuBefore != null) {
            if (menuAfter != null) {
                sequence = (menuBefore.getSequence() + menuAfter.getSequence()) / 2;
            } else {
                sequence = menuBefore.getSequence() + 0.5f; // add 0.5 in the case it is the last menu in the same level.
            }
        } else {
            if (menuAfter != null) {
                sequence = menuAfter.getSequence() - 0.5f;
            } else {
                //Do nothing
            }
        }

        boolean isChange = false;
        if ((menuParent != null && menuCurrent.getParentMenu() == null) || //move menu to be chile
                (menuParent == null && menuCurrent.getParentMenu() != null) || //move menu to be parent
                (menuParent != null && menuCurrent.getParentMenu() != null && !menuParent.getId().equals(menuCurrent.getParentMenu().getId()))) { // menu has a different parent
            menuCurrent.setParentMenu(menuParent);
            isChange = true;
        }
        if (sequence != menuCurrent.getSequence()) {
            menuCurrent.setSequence(sequence);
            isChange = true;
        }

        if (isChange) {
            menuDao.merge(menuCurrent);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromMenu(null, menuCurrent.getId()));
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
        } else {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }

    @RequestMapping(value = "{id}/activate_menu.html", method = {RequestMethod.POST, RequestMethod.GET})
    @CSRFProtection
    public @ResponseBody String activeMenu(@PathVariable Long id, @Valid String flag) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();

        if (id != null && id > 0) {
            Menu menu = serviceLocator.getMenuDao().findById(id, site.getId());
            if (menu != null) {
                if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
                    menu.setActive("Y");
                } else {
                    menu.setActive("N");
                }
                this.serviceLocator.getMenuDao().merge(menu);
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromMenu(null, menu.getId()));
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
                return "ok";
            }
        }
        return "fail";
    }
    @RequestMapping(value = "{id}/activate_header_menu.html", method = {RequestMethod.POST, RequestMethod.GET})
    @CSRFProtection
    public @ResponseBody String selectAsHeaderMenu(@PathVariable Long id, @Valid String flag) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();

        if (id != null && id > 0) {
            Menu menu = serviceLocator.getMenuDao().findById(id, site.getId());
            if (menu != null) {
                if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
                    menu.setHeaderMenu("Y");
                } else {
                    menu.setHeaderMenu("N");
                }
                this.serviceLocator.getMenuDao().merge(menu);
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromMenu(null, menu.getId()));
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
                return "ok";
            }
        }
        return "fail";
    }

    @RequestMapping(value = "{menuId}/activate_breadcrumb.html", method = RequestMethod.GET)
    @CSRFProtection
    public @ResponseBody String activateBreadcrumb(@PathVariable Long menuId, @Valid String flag) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (menuId != null && menuId > 0) {
            Menu menu = serviceLocator.getMenuDao().findById(menuId, site.getId());
            if (menu != null) {
                if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
                    menu.setDisplayBreadcrumb("Y");
                } else {
                    menu.setDisplayBreadcrumb("N");
                }
                menuDao.merge(menu);
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromMenu(null, menu.getId()));
                return "ok";
            }
        }
        return "fail";
    }

    public MenuDao getMenuDao() {
        return menuDao;
    }

//    public PageDao getPageDao() {
//        return pageDao;
//    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
}