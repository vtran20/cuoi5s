package com.easysoft.ecommerce.service.component.impl;

import com.easysoft.ecommerce.controller.Constants;
import com.easysoft.ecommerce.controller.SessionUtil;
import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.PromotionClass;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.component.CommerceSupport;
import com.easysoft.ecommerce.service.promotion.Award;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.PromotionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Process applying promo code. Recalculating price of entire order.
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplyPromoCodeComponent extends CommerceSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyPromoCodeComponent.class);

    @Override
    public void execute(SessionObject sessionObject, Messages errors) throws Exception {

        if (sessionObject != null) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            User user = SessionUtil.loadUser(sessionObject);
            /*Reset shipping discount before apply promotion again*/
            sessionObject.getOrder().setShippingDiscountPrice(0l);
            /*Get All Promotions are setting up for the site*/
            List<Promotion> promotions = ServiceLocatorHolder.getServiceLocator().getPromotionDao().findActiveByOrder("promoType", "SHIPPING", null, site.getId());
            for (Promotion promotion: promotions) {
                PromotionClass promotionClass = null;
                if (promotion != null) {
                    promotionClass = ServiceLocatorHolder.getServiceLocator().getPromotionClassDao().getPromotionClass(promotion.getId(), "Y");
                }
                try {
                    //Apply discount to the order
                    if (promotionClass != null && !StringUtils.isEmpty(promotionClass.getClassName())) {
                        Class c = null;
                        c = Class.forName(promotionClass.getClassName());
                        Award award = (Award) c.newInstance();
                        award.execute(sessionObject, user, promotion);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promotion.getPromoCode(), e);
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promotion.getPromoCode(), e);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promotion.getPromoCode(), e);
                    e.printStackTrace();
                } catch (Exception e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promotion.getPromoCode(), e);
                    e.printStackTrace();
                }
            }

            //Get promotion record base on promotion code
            String promoCode = sessionObject.getOrder().getPromoCode();
            if (!StringUtils.isEmpty(promoCode)) {
                Promotion promotion = ServiceLocatorHolder.getServiceLocator().getPromotionDao().getValidPromotion(promoCode);
                PromotionClass promotionClass = null;
                if (promotion != null) {
                    promotionClass = ServiceLocatorHolder.getServiceLocator().getPromotionClassDao().getPromotionClass(promotion.getId(), "Y");
                }
                try {
                    //Apply discount to the order
                    if (promotionClass != null && !StringUtils.isEmpty(promotionClass.getClassName())) {
                        Class c = null;
                        c = Class.forName(promotionClass.getClassName());
                        Award award = (Award) c.newInstance();
                        award.execute(sessionObject, user, promotion);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promoCode, e);
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promoCode, e);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promoCode, e);
                    e.printStackTrace();
                } catch (Exception e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promoCode, e);
                    e.printStackTrace();
                }
            }

            //Apply promotion for partner if any.

            String partnerPromoCode = "PARTNER_";
            if (user != null) {
                partnerPromoCode += user.getPartnerStatus() +"_"+user.getPartner()+"_"+user.getPartnerLevel();
            }
            if (!StringUtils.isEmpty(partnerPromoCode)) {
                Promotion promotion = ServiceLocatorHolder.getServiceLocator().getPromotionDao().getValidPromotion(partnerPromoCode);
                PromotionClass promotionClass = null;
                if (promotion != null) {
                    promotionClass = ServiceLocatorHolder.getServiceLocator().getPromotionClassDao().getPromotionClass(promotion.getId(), "Y");
                }
                try {
                    //Apply discount to the order
                    if (promotionClass != null && !StringUtils.isEmpty(promotionClass.getClassName())) {
                        Class c = null;
                        c = Class.forName(promotionClass.getClassName());
                        Award award = (Award) c.newInstance();
                        award.execute(sessionObject, user, promotion);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + partnerPromoCode, e);
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + partnerPromoCode, e);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + partnerPromoCode, e);
                    e.printStackTrace();
                } catch (Exception e) {
                    LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + partnerPromoCode, e);
                    e.printStackTrace();
                }
            }


        }
    }

}
