package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.controller.admin.form.FileSystemForm;
import com.easysoft.ecommerce.dao.CmsAreaContentDao;
import com.easysoft.ecommerce.dao.CmsAreaDao;
import com.easysoft.ecommerce.model.CmsArea;
import com.easysoft.ecommerce.model.CmsAreaContent;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * @deprecated
 */
@Controller
@RequestMapping("/admin/content")
public class ContentAdminController {

    private CmsAreaDao cmsAreaDao;
    private CmsAreaContentDao cmsAreaContentDao;

    @Autowired
    public ContentAdminController(CmsAreaDao cmsAreaDao, CmsAreaContentDao cmsAreaContentDao) {
        this.cmsAreaDao = cmsAreaDao;
        this.cmsAreaContentDao = cmsAreaContentDao;

    }

    /***************************************************************************************************
     *  Implement Images Management
     ***************************************************************************************************/

    /**
     * View images tree
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "treeview.html", method = RequestMethod.GET)
    public ModelAndView treeView() throws Exception {
        return new ModelAndView("admin/content/treeview");
    }

    @RequestMapping(value = "viewfile.html", method = RequestMethod.GET)
    public ModelAndView viewFile(@Valid FileSystemForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("admin/content/viewfile_ajax", "command", form);
    }


    /***************************************************************************************************
     *  Implement Content Management
     ***************************************************************************************************/

    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "contents.html", method = RequestMethod.GET)
    public String contents() throws Exception {
//        List<CmsArea> contents = this.cmsAreaDao.getAllCmsAreaBySite(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite());
//        return new ModelAndView("admin/content/contents", "contents", contents);
        return "admin/content/contents";
    }

    @RequestMapping(value = "{cmsAreaId}/editcmsarea.html", method = RequestMethod.GET)
    public ModelAndView editCmsArea(@PathVariable Long cmsAreaId) throws Exception {
        CmsArea cmsArea = this.cmsAreaDao.findById(cmsAreaId);
        return new ModelAndView("admin/content/cmsareaform", "cmsArea", cmsArea);
    }

    /**
     * This will be call when save the new CmsArea
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "insertcmsarea.html", method = RequestMethod.PUT)
    public ModelAndView insertCmsArea(@Valid CmsArea entity, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            //TODO: show error messages
        }
        Map map = new HashMap();
        map.put("cmsArea", entity);
        map.put("messages", createOrUpdateCmsArea(entity));
        return new ModelAndView("admin/content/cmsareaform", map);
    }

    /**
     * This will be called when save to update the CmsArea
     *
     * @param id
     * @param entity
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{id}/updatecmsarea.html", method = RequestMethod.POST)
    public ModelAndView updateCmsArea(@PathVariable Long id, @Valid CmsArea entity, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            //TODO: show error messages
        }
        entity.setId(id);
        Map map = new HashMap();
        map.put("cmsArea", cmsAreaDao.findById(id));
        map.put("messages", createOrUpdateCmsArea(entity));
        return new ModelAndView("admin/content/cmsareaform", map);
    }

    private Messages createOrUpdateCmsArea(CmsArea entity) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (entity.getId() == null) {
            Date date = new Date();
            entity.setCreatedDate(date);
            entity.setSite(site);
            cmsAreaDao.persist(entity);
            messages.addInfo("New CmsArea is created");
        } else {
            //Update CmsArea
            CmsArea original = cmsAreaDao.findById(entity.getId());
            if (!entity.getCmsName().equals(original.getCmsName())) {
                original.setCmsName(entity.getCmsName());
                messages.addInfo("Cms Name is saved");
            }
            if (!entity.getTitle().equals(original.getTitle())) {
                original.setTitle(entity.getTitle());
                messages.addInfo("Cms title is saved");
            }
            if (entity.getPath() != null && !entity.getPath().equals(original.getPath())) {
                original.setPath(entity.getPath());
                messages.addInfo("Path is saved");
            }
            if (entity.getMetaDescription() != null && !entity.getMetaDescription().equals(original.getMetaDescription())) {
                original.setMetaDescription(entity.getMetaDescription());
                messages.addInfo("Meta Description is saved");
            }
            if (entity.getMetaKeyword() != null && !entity.getMetaKeyword().equals(original.getMetaKeyword())) {
                original.setMetaKeyword(entity.getMetaKeyword());
                messages.addInfo("Meta Keyword is saved");
            }

            if (messages.isEmpty()) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            } else {
                entity.setSite(site);
                cmsAreaDao.merge(entity);
            }
        }
        return messages;
    }


    /***************************************************************************************************
     *  Implement CMS Area Content Management
     ***************************************************************************************************/

    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{cmsAreaId}/cmsareacontents.html", method = RequestMethod.GET)
    public ModelAndView cmsAreaContents(@PathVariable Long cmsAreaId) throws Exception {
//        return new ModelAndView("admin/content/contents", "contents", contents);
//        List<CmsAreaContent> contents = this.cmsAreaContentDao.findBy("cmsArea", cmsAreaId);
        return new ModelAndView("admin/content/cmsareacontents", "cmsAreaId", cmsAreaId);
    }

    @RequestMapping(value = "{cmsAreaContentId}/editcmsareacontent.html", method = RequestMethod.GET)
    public ModelAndView editCmsAreaContent(@PathVariable Long cmsAreaContentId) throws Exception {
        CmsAreaContent cmsAreaContent = this.cmsAreaContentDao.findById(cmsAreaContentId);
        return new ModelAndView("admin/content/cmsareacontentform", "cmsAreaContent", cmsAreaContent);
    }

    /**
     * This will be call when save the new CmsAreaContent
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "insertcmsareacontent.html", method = RequestMethod.PUT)
    public ModelAndView insertCmsAreaContent(@Valid CmsAreaContent entity, @Valid Long cmsAreaId, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            //TODO: show error messages
        }
        Map map = new HashMap();
        map.put("cmsAreaContent", entity);
        map.put("messages", createOrUpdateCmsAreaContent(entity, cmsAreaId));
        return new ModelAndView("admin/content/cmsareacontentform", map);
    }

    /**
     * This will be called when save to update the CmsAreaContent
     *
     * @param id
     * @param entity
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{id}/updatecmsareacontent.html", method = RequestMethod.POST)
    public ModelAndView updateCmsAreaContent(@PathVariable Long id, @Valid CmsAreaContent entity, @Valid Long cmsAreaId, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            //TODO: show error messages
        }
        entity.setId(id);
        Map map = new HashMap();
        map.put("cmsAreaContent", cmsAreaContentDao.findById(id));
        map.put("messages", createOrUpdateCmsAreaContent(entity, cmsAreaId));
        return new ModelAndView("admin/content/cmsareacontentform", map);
    }

    private Messages createOrUpdateCmsAreaContent(CmsAreaContent entity, Long cmsAreaId) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (entity.getId() == null) {
            Date date = new Date();
            entity.setCreatedDate(date);
            if (cmsAreaId > 0) {
                entity.setCmsArea(cmsAreaDao.findById(cmsAreaId));
            }
            cmsAreaContentDao.persist(entity);
            messages.addInfo("New CmsArea Content is created");
        } else {
            //Update CmsArea Content
            CmsAreaContent original = cmsAreaContentDao.findById(entity.getId());
            if (entity.getStartDate() != null && !entity.getStartDate().equals(original.getStartDate())) {
                original.setStartDate(entity.getStartDate());
                messages.addInfo("Cms Content Start Date is saved");
            }
            if (entity.getEndDate() != null && !entity.getEndDate().equals(original.getEndDate())) {
                original.setEndDate(entity.getEndDate());
                messages.addInfo("Cms Content End Date is saved");
            }

            if (entity.getActive() != null && entity.getActive().equals(original.getActive())) {
                if ("Y".equals(entity.getActive())) {
                    original.setActive("Y");
                    messages.addInfo("CmsAreaContent is activated");
                } else {
                    original.setActive("N");
                    messages.addInfo("CmsAreaContent is deactivated");
                }
            }
            if (entity.getContent() != null && !entity.getContent().equals(original.getContent())) {
                original.setContent(entity.getContent());
                messages.addInfo("Content is saved");
            }

            if (messages.isEmpty()) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            } else {
                if (cmsAreaId > 0) {
                    entity.setCmsArea(cmsAreaDao.findById(cmsAreaId));
                }
                cmsAreaContentDao.merge(entity);
            }
        }
        return messages;
    }
}
