package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.service.ServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/news")
public class NewsController {

    private ServiceLocator serviceLocator;

    @Autowired
    public NewsController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    /**
     * List all news
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("index.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("/news/index");
    }
    @RequestMapping("{title}.html")
    public ModelAndView newsDetail() throws Exception {
        return new ModelAndView("/news/detail");
    }
    @RequestMapping("{type}/{title}.html")
    public ModelAndView newsCategory() throws Exception {
        return new ModelAndView("/news/category");
    }
//    @RequestMapping("{action}.html")
//    public ModelAndView action(@PathVariable String action) throws Exception {
//        return new ModelAndView("/news/"+action);
//    }

}
