package com.easysoft.ecommerce.service.promotion.impl;

import com.easysoft.ecommerce.controller.Constants;
import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.promotion.AwardSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Award:
 * + Product
 * + $ off -> product price 50000, 30000$ off mean, the price has to pay to be 20000
 * + % off -> product price 50000, 10% off mean, the price has to pay to be 45000
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class PartnerAward extends AwardSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerAward.class);

    @Override
    public String execute(SessionObject sessionObject, User user, Promotion promotion) throws Exception {
        if (sessionObject != null) {

            List <ItemMap> items = sessionObject.getOrder().getParentItems();
            String type = promotion.getPromoParam1();
            String firstYear = promotion.getPromoParam2();
            String nextComingYear = promotion.getPromoParam3();
            for (ItemMap parentItem : items) {
                String discount = "";
                if (isValidPromoCode(sessionObject, parentItem, user, promotion)) {
                    if (parentItem.get("SITE_ID") != null && StringUtils.isNumeric(parentItem.get("SITE_ID") + "")) {
                        Site thisSite = ServiceLocatorHolder.getServiceLocator().getSiteDao().findById((Long) parentItem.get("SITE_ID"));
                        if (thisSite != null) {
                            int years = getDiffYears(thisSite.getCreatedDate(), thisSite.getEndDate());
                            if (years == 0) {
                                discount = firstYear;
                            } else {
                                discount = nextComingYear;
                            }
                            if (!StringUtils.isEmpty(type)) {
                                Long priceItem = parentItem.getFinalPriceItem();
                                if ("%".equals(type)) {
                                    if (StringUtils.isNumeric(discount)) {
                                        Long lDiscount = Long.valueOf(discount);
                                        Long disc = (priceItem * lDiscount) / 100;
                                        parentItem.setPriceItemPromoDiscount(disc);
                                    }
                                } else if ("$".equals(type)) {
                                    if (StringUtils.isNumeric(discount)) {
                                        parentItem.setPriceItemPromoDiscount(Long.valueOf(discount));
                                    }
                                }
                            }
                            //get all children item (modules)
                            List<ItemMap> childItems = sessionObject.getOrder().getChildrentItems(parentItem.getId(), 1, thisSite.getId());
                            for (ItemMap childItem : childItems) {
                                if (!StringUtils.isEmpty(type)) {
                                    Long priceItem = childItem.getFinalPriceItem();
                                    if ("%".equals(type)) {
                                        if (StringUtils.isNumeric(discount)) {
                                            Long lDiscount = Long.valueOf(discount);
                                            Long disc = (priceItem * lDiscount) / 100;
                                            childItem.setPriceItemPromoDiscount(disc);
                                        }
                                    } else if ("$".equals(type)) {
                                        if (StringUtils.isNumeric(discount)) {
                                            childItem.setPriceItemPromoDiscount(Long.valueOf(discount));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //do nothing
                }
            }


        }
        return "";
    }

    /**
     * Return 0 mean the first year, return 1 means next coming year.
     *
     * @param first
     * @param last
     * @return
     */
    private int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        a.add(Calendar.DAY_OF_YEAR, Constants.TRIAL_USE); //ignore trial time
        Calendar b = getCalendar(last);
        b.add(Calendar.DAY_OF_YEAR, 1); // add one more day to make sure if it is 1 year we start calculate the next year.
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff == 0? 0 : 1;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

}
