package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.AlbumDao;
import com.easysoft.ecommerce.dao.AlbumImageDao;
import com.easysoft.ecommerce.model.Album;
import com.easysoft.ecommerce.model.AlbumImage;
import com.easysoft.ecommerce.model.NewsCategory;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.*;

@Controller
@RequestMapping(value = "/admin/gallery")
public class GalleryAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GalleryAdminController.class);

    @Autowired
    private AlbumDao albumDao;
    @Autowired
    private AlbumImageDao albumImageDao;
    @Autowired
    private ServiceLocator serviceLocator;

    @RequestMapping("index.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("admin/gallery/index");
    }
    @RequestMapping("{action}.html")
    public ModelAndView action(@PathVariable String action) throws Exception {
        return new ModelAndView("admin/gallery/"+action);
    }

    @RequestMapping(value = {"update.html","insert.html","save.html"}, method = RequestMethod.GET)
    public String updateOrInsert() throws Exception {
        return "admin/gallery/index";
    }

    /**
     * This will be called when delete a album
     */
    @RequestMapping(value = "delete.html", method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String delete(@Valid Long id) {
        Messages messages = new Messages();
        try {
            if (id > 0) {
                Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
                Album album = albumDao.findById(id, site.getId());
                if (album != null) {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromGallery(null));
                    albumDao.remove(album);
                    //TODO: Remove images in the Image Server as well.
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                } else {
                    messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            messages.addError("ERROR: Cannot delete parent album.");
        }
        return messages.toString();
    }

    /**
     * This method is called when insert a album
     */
    @RequestMapping(value = {"insert.html","update.html"}, method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String insertOrUpdate(@Valid Album entity, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            //TODO: show error messages
        }
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromGallery(null));
        return updateOrInsertAlbum(entity).toString();
    }

    private Messages updateOrInsertAlbum (Album album) {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (album != null) {
            if (!album.isEmptyId()) { // Update page
                Album originalAlbum =  albumDao.findById(album.getId(), site.getId());
                boolean isChange = false;
                if (originalAlbum != null) {
                    if ((originalAlbum.getName() == null && album.getName() != null) ||
                            (originalAlbum.getName() != null && album.getName() != null && !originalAlbum.getName().equals(album.getName()))) {
                        originalAlbum.setName(album.getName().trim());
                        originalAlbum.setUri(generateAlbumUri(album.getName().trim(), album.getId()));
                        isChange = true;
                    }

                    if (album.getDescription() != null && originalAlbum.getDescription() != null && !album.getDescription().equals(originalAlbum.getDescription())) {
                        originalAlbum.setName(album.getName());
                        isChange = true;
                    }

                    if (album.getUri() != null && originalAlbum.getUri() != null && !album.getUri().equals(originalAlbum.getUri())) {
                        originalAlbum.setUri(album.getUri());
                        isChange = true;
                    }
                    if (album.getActive() != null && originalAlbum.getActive() != null && !album.getActive().equals(originalAlbum.getActive())) {
                        originalAlbum.setActive(album.getActive());
                        isChange = true;
                    }

                    if (isChange) {
                        originalAlbum.setUpdatedDate(new Date());
                        albumDao.merge(originalAlbum);
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                    }
                }
            } else { // Insert new page
                album.setSite(site);
                if (StringUtils.isNotEmpty(album.getName())) {
                    album.setName(album.getName().trim());
                    album.setUri(generateAlbumUri(album.getName().trim(), null));
                }
                album.setUpdatedDate(new Date());
                album.setId(null);
                Float maxSequence = albumDao.getMaxSequence(site.getId());
                album.setSequence(maxSequence + 1f);
                albumDao.persist(album);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }

        }
        return messages;
    }

    private String generateAlbumUri(String albumName, Long albumId) {
        if (StringUtils.isNotEmpty(albumName)) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            String uriNonHTM = WebUtil.getURI(albumName);
            uriNonHTM = uriNonHTM.toLowerCase();
            String uri = uriNonHTM + ".html";
            int count = 1;
            if (albumId != null && albumId > 0) { // old category
                Album album = albumDao.findById(albumId, site.getId());
                if (album.getUri() != null && !album.getUri().equals(uriNonHTM+".html")) {
                    while (isExistAlbumURI(site, uriNonHTM + ".html")) {
                        uriNonHTM = uriNonHTM + "-" + count;
                        uri = uriNonHTM + ".html";
                        count++;
                    }
                } else {
                    //keep the current uri
                }
            } else { // new album
                while (isExistAlbumURI(site, uriNonHTM + ".html")) {
                    uriNonHTM = uriNonHTM + "-" + count;
                    uri = uriNonHTM + ".html";
                    count++;
                }
            }
            return uri;
        } else {
            return "";
        }

    }
    private boolean isExistAlbumURI(Site site, String uri) {
        List albums = albumDao.findBy("uri", uri, site.getId());
        return albums != null && albums.size() > 0;
    }

    /**
     * This method is called when insert images to an album
     * Note: Adding headers="Accept=*\/*\" for fixing HTTP Error 406 Not acceptable
     */
    @RequestMapping(value = "insert_image.html", headers="Accept=*/*", method = RequestMethod.GET)
    @CSRFProtection
    public @ResponseBody Map ajaxInsertImage(HttpServletRequest request) throws Exception {
        AlbumImage albumImage;
        String uri = request.getParameter("uri");
        String albumId = request.getParameter("albumId");
        String imageServer = serviceLocator.getSystemContext().getGlobalConfig("image.server");
        Map <String, Object>map = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(albumId)) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            Long id = Long.parseLong(albumId);
            Album album = albumDao.findById(id, site.getId());
            if (!StringUtils.isEmpty(uri) && album != null) {
                if (!StringUtils.isEmpty(uri)) {
                    albumImage = new AlbumImage();
                    albumImage.setUri(uri);
                    albumImage.setActive("Y");
                    albumImage.setFileName(request.getParameter("imageName") != null ? request.getParameter("imageName") : "");
                    albumImage.setUpdatedDate(new Date());
                    albumImage.setAlbum(album);
                    albumImage.setSite(site);
                    albumImageDao.persist(albumImage);
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromAlbum(null, album.getId()));
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromGallery(null));
                    map.put("success", false);
                    map.put("id", albumImage.getId());
                    map.put("uri", albumImage.getUri());
                    map.put("delete_url", imageServer+"/images/remove.json?name="+albumImage.getUri()+"&key="+URLEncoder.encode(WebUtil.encrypt(albumImage.getUri()), "UTF-8")+"&path="+site.getSiteCode());
                }
            } else {
                map.put("success", false);
            }
        } else {
            map.put("success", false);
        }
        return map;
    }
    /**
     * This method is called when update description to an album
     */
    @RequestMapping(value = "update_image.html", method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String updateAlbumImage(HttpServletRequest request, AlbumImage entity) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        boolean isChange = false;
        if (entity != null && !entity.isEmptyId()) { // Update AlbumImage
            AlbumImage originalAlbumImage =  albumImageDao.findById(entity.getId(), site.getId());
            if (originalAlbumImage != null) {
                if ((originalAlbumImage.getDescription() == null && entity.getDescription() != null) ||
                        (originalAlbumImage.getDescription() != null && entity.getDescription() != null && !originalAlbumImage.getDescription().equals(entity.getDescription()))) {
                    originalAlbumImage.setDescription(entity.getDescription().trim());
                    isChange = true;
                }
            }
            if (isChange) {
                albumImageDao.merge(originalAlbumImage);
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromAlbumImage(null, originalAlbumImage.getId()));
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                return messages.toString();

            }
        }
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        return messages.toString();
    }

    /**
     * This will be called when delete a Menu
     */
    @RequestMapping(value = "deleteimage.html", method = RequestMethod.GET)
    @CSRFProtection
    public @ResponseBody String deleteImage(@Valid Long id) {
        try {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            if (id > 0) {
                AlbumImage albumImage = albumImageDao.findById(id, site.getId());
                if (albumImage != null) {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromAlbumImage(null, albumImage.getId()));
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromGallery(null));
                    albumImageDao.remove(albumImage);
                }
                return "ok";
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "fail";
        }
        return "fail";
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    /**
     * TODO: Need to support if need
     * @param request
     * @param currentItem
     * @param orderList
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "reorder.html", method = RequestMethod.GET)
    public ModelAndView reOrder(HttpServletRequest request, @Valid String currentItem,  @Valid String orderList) throws Exception {
        Messages messages = new Messages();

        if (!WebUtil.reOrderValidation(orderList, currentItem)) {
            messages.addError("Reorder is invalid");
            return new ModelAndView("admin/gallery/index", "messages", messages);
        }

        /*Group parent and sub*/
        List<String> parents = new ArrayList<String>();
        List<String> subs = new ArrayList<String>();
        String []array = orderList.split(",");
        for (String s : array) {
            if (s.split("-").length == 2) {
                parents.add(s);
            } else if (s.split("-").length == 3) {
                //only add sub in the same group
                if (currentItem.split("-")[0].equals(s.split("-")[0])) {
                    subs.add(s);
                }
            }
        }
        /*Reorder for parent*/
        Float seq = 0f;
        if (currentItem.split("-").length == 2) {
            seq = WebUtil.getSequence(parents, currentItem);
            if (seq > 0) {
                String [] currItem = currentItem.split("-");
                Album album = albumDao.findById(Long.valueOf(currItem[0]));
                album.setSequence(seq);
                albumDao.merge(album);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.order.change.successfully", null, LocaleContextHolder.getLocale()));
            }

        } else if (currentItem.split("-").length == 3) {
            seq = WebUtil.getSequence(subs, currentItem);
            if (seq > 0) {
                String [] currItem = currentItem.split("-");
                Album album = albumDao.findById(Long.valueOf(currItem[1]));
                album.setSequence(seq);
                albumDao.merge(album);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.order.change.successfully", null, LocaleContextHolder.getLocale()));
            }
        }
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromGallery(null));
        return new ModelAndView("admin/gallery/index", "messages", messages);
    }

}
