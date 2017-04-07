package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.condition.Condition;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import com.easysoft.ecommerce.util.YoutubeService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URL;
import java.util.*;

@Controller
@RequestMapping()
public class VideoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoController.class);
    private ServiceLocator serviceLocator;

    public static Map<String, Video> videosCache = Collections.synchronizedMap(new LinkedHashMap<String, Video>());


    @Autowired
    public VideoController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }


    @RequestMapping("/video/{uri}/{id}.html")
    public ModelAndView viewVideo(@PathVariable String uri, @PathVariable Long id, @RequestParam(required = false, value = "") final String videoId, HttpServletRequest request) throws Exception {
        updateViewCountVideo(videoId);
        return new ModelAndView("/video/view");
    }

    @RequestMapping("/live-video/{uri}.html")
    public ModelAndView viewLiveVideo() throws Exception {
        return new ModelAndView("/viewlivevideo");
    }

//    @RequestMapping("/category/{catUri}/{id}.html")
//    public ModelAndView category(@PathVariable String catUri, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        return new ModelAndView("/video/category");
//    }

    @RequestMapping(value = {"/load-more-new-videos.json"}, method = RequestMethod.GET)
    public
    @ResponseBody
    List<Video> getNextPageNewVideo(HttpServletRequest request) throws Exception {
        List <Video> videos = null;
        String s = request.getParameter("start");
        String catId = request.getParameter("catId");
        try {
            if (StringUtils.isEmpty(s) || "0".equals(s)) return null;
            int start = Integer.parseInt(s);
            if (!StringUtils.isEmpty(catId)) {
                videos = serviceLocator.getVideoDao().getVideosByCategory(Long.parseLong(catId), start, Constants.NUMBER_VIDEO_LOAD_MORE, "updatedDate", true);
            } else {
                videos = serviceLocator.getVideoDao().findByOrder(null, start, Constants.NUMBER_VIDEO_LOAD_MORE, "updatedDate desc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }
    @RequestMapping(value = {"/load-more-most-video-videos.json"}, method = RequestMethod.GET)
    public
    @ResponseBody
    List<Video> getNextPageMostViewVideo(HttpServletRequest request) throws Exception {
        List <Video> videos = null;
        String s = request.getParameter("start");
        String catId = request.getParameter("catId");
        try {
            if (StringUtils.isEmpty(s) || "0".equals(s)) return null;
            int start = Integer.parseInt(s);
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            if (!StringUtils.isEmpty(catId)) {
                videos = serviceLocator.getVideoDao().getVideosByCategory(Long.parseLong(catId), Constants.NUMBER_DAYS_FOR_MOST_VIEW, start, Constants.NUMBER_VIDEO_RETURN_RIGHT_COLUMN, "Y", "viewCount", true);
            } else {
                videos = serviceLocator.getVideoDao().getNextMostViewVideos(site, Constants.NUMBER_VIDEO_LOAD_MORE_RIGHT_COLUMN, Constants.NUMBER_DAYS_FOR_MOST_VIEW, start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }

    @RequestMapping(value = {"/video/check-video-status.json"}, method = RequestMethod.GET)
    public
    @ResponseBody
    String checkVideoStatus(HttpServletRequest request) throws Exception {
        String videoId = request.getParameter("videoId");
        try {
            if (StringUtils.isEmpty(videoId)) return "";
            String status = request.getParameter("status");
            ObjectMapper mapper = new ObjectMapper();
            Map response = null;
            response = mapper.readValue(new URL("https://www.googleapis.com/youtube/v3/videos?key="+ YoutubeService.apiKey+"&part=snippet,statistics,contentDetails&id="+videoId).openStream(), HashMap.class);
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            List <Video> listVideos = WebUtil.convertFromJson(response, site, null);
            //deactivate this video because it is removed from youtube
            if (listVideos == null || "100".equals(status) || "101".equals(status) || "150".equals(status)) {
                Video video = serviceLocator.getVideoDao().findUniqueBy("videoId", videoId);
                if (video != null) {
                    video.setActive("N");
                    video.setUpdatedDate(new Date());
                    serviceLocator.getVideoDao().merge(video);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    private void updateViewCountVideo (String videoId) {
        if (!StringUtils.isEmpty(videoId)) {
            Video v = videosCache.get(videoId);
            if (v != null) {
                v.setViewCount(v.getViewCount()+1);
            } else {
                v = new Video();
                v.setVideoId(videoId);
                v.setViewCount(v.getViewCount()+1);
                videosCache.put(videoId, v);
            }
        }
    }

    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/video/{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "video/" + action;
    }

}
