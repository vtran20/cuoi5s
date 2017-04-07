package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.controller.admin.form.OrderFilterForm;
import com.easysoft.ecommerce.dao.OrderDao;
import com.easysoft.ecommerce.dao.OrderSessionDao;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/order")
public class OrdersAdminController {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSessionDao orderSessionDao;
    @Autowired
    private ServiceLocator serviceLocator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT"));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/order/" + action;
    }

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request) throws Exception {
        OrderFilterForm orderFilterForm = new OrderFilterForm();
        orderFilterForm.setOrderStatus(new String[]{"NEW_ORDER", "PAID", "SHIPPING"});
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -15);
        orderFilterForm.setStartDate(cal.getTime());
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, 1);
        orderFilterForm.setEndDate(cal1.getTime());
        return new ModelAndView("admin/order/index", "orderFilter", orderFilterForm);
    }

    @RequestMapping("findorders.html")
    public @ResponseBody Map findOrders(HttpServletRequest request, @Valid OrderFilterForm orderFilterForm) throws Exception {
        Map result = new HashMap();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        List orders = orderDao.findOrders(orderFilterForm, site);
        result.put("draw", 1);
        result.put("recordsTotal", orders.size());
        result.put("recordsFiltered", orders.size());
        result.put("data", orders);
        return result;
    }

    @RequestMapping(value={"changeorderstatus.html"}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody String changeOrderStatus(
            @RequestParam(value = "orderStatus", required = true) String orderStatus,
            @RequestParam(value = "orderId", required = true) Long orderId
            ) throws Exception {
        Order order = this.orderDao.findById(orderId);
        boolean isUpdate = false;
        if (order != null) {
            //For orders from site owner/partner
            isUpdate = serviceLocator.getOrderService().changeOrderStatus(order, orderStatus, orderId);
        }
        Messages messages = new Messages();
        if (isUpdate) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        } else {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("order.change.status.invalid", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }
}
