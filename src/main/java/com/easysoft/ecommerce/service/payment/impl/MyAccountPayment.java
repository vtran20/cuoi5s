package com.easysoft.ecommerce.service.payment.impl;

import com.easysoft.ecommerce.controller.SessionUtil;
import com.easysoft.ecommerce.model.Order;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.UserBalanceHistory;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.payment.PaymentSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.util.Map;

/**
 * Pay from my account balance
 *
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyAccountPayment extends PaymentSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAccountPayment.class);


    /**
     *
     * @return url of payment gateway.
     */
    @Override
    public String createRequestUrl(SessionObject sessionObject, Site site) throws Exception {
        return "/checkout/receipt.html";
    }

    /**
     * Check returned data.
     *
     * @param params contain params that return from payment gateway
     * @return true if is correct
     */
    public boolean verifyResponseUrl(SessionObject sessionObject, Map params, Site site) throws Exception {
        String sOrderId = (String) sessionObject.getOrder().get("ORDER_ID");
        if (!StringUtils.isEmpty(sOrderId)) {
            Order order = null;
            sOrderId = sOrderId.replace(site.getSiteCode(), site.getSiteCode());
            if (StringUtils.isNumeric(sOrderId)) {
                //subtract balance total order amount
                User user = SessionUtil.loadUser(sessionObject);
                Long newBalance = user.getBalance() - sessionObject.getOrder().getTotalPrice();
                user.setBalance(newBalance);
                ServiceLocatorHolder.getServiceLocator().getUserDao().merge(user);
                //insert user balance history
                UserBalanceHistory userBalanceHistory = new UserBalanceHistory();
                userBalanceHistory.setAmount(-sessionObject.getOrder().getTotalPrice());
                userBalanceHistory.setUser(user);
                userBalanceHistory.setName("Charge amount of money paid for order: " + sOrderId + " and new balance is: " + newBalance);
                ServiceLocatorHolder.getServiceLocator().getUserBalanceHistoryDao().persist(userBalanceHistory);

                order = ServiceLocatorHolder.getServiceLocator().getOrderSessionDao().getOrder(Long.valueOf(sOrderId), sessionObject.getUserId(), site);
                ServiceLocatorHolder.getServiceLocator().getOrderService().changeOrderStatus(order, Order.PAID, Long.valueOf(sOrderId));
                return true;
            }
        }
        return false;
    }
}
