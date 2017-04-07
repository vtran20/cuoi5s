package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.*;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import com.easysoft.ecommerce.util.YoutubeService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class will be used for the new layout and support for multi-sites.
 */
@Controller
@RequestMapping("/admin/video")
public class VideoAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoAdminController.class);

    @Autowired
    private ServiceLocator serviceLocator;
    @Autowired
    private VideoService videoService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private VideoDao videoDao;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT"));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(value = "import/{id}.json", method = RequestMethod.GET)
    public
    @ResponseBody
    Map importVideo(@PathVariable String id, HttpServletRequest request) {
        Map result = new HashMap();
        try {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            String categoryIds = request.getParameter("categoryIds");

            Video video = videoDao.findUniqueBy("videoId", id, site.getId());
            String name = request.getParameter("name");
            if (video == null) {
                //import into to site
                ObjectMapper mapper = new ObjectMapper();
                Map response = null;
                response = mapper.readValue(new URL("https://www.googleapis.com/youtube/v3/videos?key="+ YoutubeService.apiKey+"&part=snippet,statistics,contentDetails&id="+id).openStream(), HashMap.class);
                List <Video> listVideos = WebUtil.convertFromJson(response, site, null);
                if (listVideos != null) {
                    //always have 1 video
                    List<Video> newVideos = new ArrayList<Video>();
                    for (Video v : listVideos) {
                        if (!StringUtils.isEmpty(name)) {
                            v.setName(name);
                            v.setUri(WebUtil.getURI(name));
                        }
                        newVideos.add(v);
                        result.put("video", v);
                    }
                    adminService.createOrUpdateVideos(newVideos, categoryIds);
                }
            } else {
                result.put("video", video);
            }
            result.put("status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "fail:"+e.getMessage());
        }
        return result;
    }
    @RequestMapping(value = "import.json", method = RequestMethod.GET)
    public
    @ResponseBody
    Map importVideos(HttpServletRequest request) {
        Map result = new HashMap();
        try {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            String videoIds = request.getParameter("ids");
            String categoryIds = request.getParameter("categoryIds");
            if (StringUtils.isNotBlank(videoIds)) {
                if (StringUtils.isNotBlank(videoIds)) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map response = null;
                    response = mapper.readValue(new URL("https://www.googleapis.com/youtube/v3/videos?key="+ YoutubeService.apiKey+"&part=snippet,statistics,contentDetails&id="+videoIds).openStream(), HashMap.class);
                    List <Video> listVideos = WebUtil.convertFromJson(response, site, null);
                    if (listVideos != null) {
                        List<Video> newVideos = new ArrayList<Video>();
                        for (Video v : listVideos) {
                            Video video = videoDao.findUniqueBy("videoId", v.getVideoId(), site.getId());
                            if (video == null) {
                                newVideos.add(v);
                            }
                        }
                        adminService.createOrUpdateVideos(newVideos, categoryIds);
                    }

                }
            }

            result.put("status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "fail:"+e.getMessage());
        }
        return result;
    }
    @RequestMapping(value = "importLive.json", method = RequestMethod.GET)
    public
    @ResponseBody
    Map importLiveVideos(HttpServletRequest request) {
        Map result = new HashMap();
        try {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            String videoIds = request.getParameter("ids");
            String name = request.getParameter("name");
            if (StringUtils.isNotBlank(videoIds)) {
                if (StringUtils.isNotBlank(videoIds)) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map response = null;
                    response = mapper.readValue(new URL("https://www.googleapis.com/youtube/v3/videos?key="+ YoutubeService.apiKey+"&part=snippet,statistics,contentDetails&id="+videoIds).openStream(), HashMap.class);
                    List <Video> listVideos = WebUtil.convertFromJson(response, site, name);
                    if (listVideos != null) {
                        List<Video> newVideos = new ArrayList<Video>();
                        for (Video v : listVideos) {
                            Video video = videoDao.findUniqueBy("videoId", v.getVideoId(), site.getId());
                            if (video == null) {
                                v.setLive("Y");
                                newVideos.add(v);
                            }
                        }
                        adminService.createOrUpdateVideos(newVideos, null);
                    }

                }
            }

            result.put("status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "fail:"+e.getMessage());
        }
        return result;
    }

    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/video/" + action;
    }


    /**
     * ************************************************************************************************
     * Implement Import Video
     * *************************************************************************************************
     */

    @RequestMapping(value = {"video_import.html", "video_update.html"}, method = RequestMethod.GET)
    public String videoForm() throws Exception {
        return "admin/video/video_import";
    }

    @RequestMapping(value = {"videos.html"}, method = RequestMethod.GET)
    public String getVideos() throws Exception {
        return "admin/video/videos";
    }

    /**
     * This method is called when import the new video
     */
    @RequestMapping(value = "video_import.html", method = RequestMethod.POST)
    @CSRFProtection
    public ModelAndView insertVideo(@Valid String videoIds, @Valid Long[] categoryId, BindingResult result, HttpServletRequest request) throws Exception {



        Map map = new HashMap();
//        map.put("messages", categoryService.createOrUpdate(entity, variant, categoryId));

        return new ModelAndView("admin/video/video_import", map);
    }


    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public VideoService getVideoService() {
        return videoService;
    }

    public VideoDao getVideoDao() {
        return videoDao;
    }

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }
}
