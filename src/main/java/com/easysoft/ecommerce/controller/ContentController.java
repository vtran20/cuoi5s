package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.PromotionCondition;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.condition.Condition;
import com.easysoft.ecommerce.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping()
public class ContentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentController.class);
    private ServiceLocator serviceLocator;

    @Autowired
    public ContentController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @RequestMapping("/content/{uri}.html")
    public ModelAndView content() throws Exception {
        return new ModelAndView("/content/page");
    }

}
