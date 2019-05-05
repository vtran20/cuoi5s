package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.controller.SessionUtil;
import com.easysoft.ecommerce.dao.OrderDao;
import com.easysoft.ecommerce.dao.OrderSessionDao;
import com.easysoft.ecommerce.dao.PaymentProviderDao;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.OrderService;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.promotion.Award;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSessionDao orderSessionDao;
    @Autowired
    private PaymentProviderDao paymentProviderDao;
    @Autowired
    private ServiceLocator serviceLocator;

    @Override
    public SessionObject getOrderSession (Long orderId, Long userId) throws IOException, ClassNotFoundException {
        return orderSessionDao.getOrderSession(orderId, userId, ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite());
    }
    @Override
    public Order createOrder(SessionObject sessionObject) throws IOException, ClassNotFoundException {
        Order order = buildOrderAttribute(sessionObject);
        Long providerId = sessionObject.getOrder().getPaymentMethod();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (providerId > 0) {
            PaymentProviderSite provider = serviceLocator.getPaymentProviderSiteDao().findById(providerId, site.getId());
            order.setProvider(provider);
        }
        order.setOrderType(2);
        orderDao.persist(order);
        orderSessionDao.createOrUpdateOrderSession(sessionObject, order);
        //decrease inventory
        List <ItemMap> items = sessionObject.getOrder().getItems();
        for (ItemMap item: items) {
            Long varId = item.getProductVariantId();
            if (varId != null && varId > 0) {
                ProductVariant variant = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findById(varId);
                if (variant != null) {
                    variant.setInventory(variant.getInventory() - item.getQuantity());
                }
            }
        }
        return order; 
    }

    @Override
    public Order createServiceOrder(SessionObject sessionObject) throws IOException, ClassNotFoundException {
        Order order = buildOrderAttribute(sessionObject);
        Long providerId = sessionObject.getOrder().getPaymentMethod();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (providerId > 0) {
            PaymentProviderSite provider = serviceLocator.getPaymentProviderSiteDao().findById(providerId, site.getId());
            order.setProvider(provider);
        }
        //calculate commission if any
        Long subTotal = sessionObject.getOrder().getSubPriceTotal();
        Long originalSubTotal = sessionObject.getOrder().getOriginalSubTotal();
        if (originalSubTotal > subTotal) {
            order.setCommission(originalSubTotal - subTotal);
        }
        order.setOrderType(1);
        orderDao.persist(order);
        orderSessionDao.createOrUpdateOrderSession(sessionObject, order);

        //Update service end date if order was paid.
        if ("PAID".equalsIgnoreCase(order.getStatus())) {
            updateServiceDate(sessionObject);
        }
        return order;
    }

    private boolean updateServiceDate (SessionObject sessionObject) {
        boolean isUpdate = false;
        List<ItemMap> items = sessionObject.getOrder().getItems();

        for (ItemMap item: items) {
            Long varId = item.getProductVariantId();
            if (varId != null && varId > 0 && !item.isChildItem()) {
                ProductVariant variant = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findById(varId);
                if (variant != null) {
                    Long thisSiteId = item.getLong("SITE_ID");
                    if (thisSiteId > 0) {
                        Site thisSite = serviceLocator.getSiteDao().findById(thisSiteId);
                        Long productId = item.getId();
                        SiteProductService service = serviceLocator.getSiteProductServiceDao().getSiteProductService(productId, thisSiteId);
                        if (service == null) {
                            //Create new service
                            service = new SiteProductService();
                            service.setStartDate(thisSite.getStartDate());
                            //calculate endDate, get the endDate from site table
                            Integer years = item.getQuantity();
                            Calendar endDate = Calendar.getInstance();
                            endDate.setTime(thisSite.getEndDate());
                            endDate.add(Calendar.YEAR, years);
                            service.setEndDate(endDate.getTime());
                            service.setActive("Y");
                            service.setProductVariant(variant);
                            service.setSite(thisSite);
                            Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().findById(productId);
                            service.setProduct(product);
                            serviceLocator.getSiteProductServiceDao().persist(service);
                            //update thisSite
                            thisSite.setEndDate(service.getEndDate());
                            thisSite.setUpdatedDate(new Date());
                            serviceLocator.getSiteDao().merge(thisSite);

                        } else {
                            //update service
                            service.setStartDate(thisSite.getStartDate());
                            //calculate endDate, get the endDate from site table
                            Integer years = item.getQuantity();
                            Calendar endDate = Calendar.getInstance();
                            endDate.setTime(thisSite.getEndDate());
                            endDate.add(Calendar.YEAR, years);
                            service.setEndDate(endDate.getTime());
                            service.setActive("Y");
                            service.setProductVariant(variant);
                            service.setSite(thisSite);
                            Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().findById(productId);
                            service.setProduct(product);
                            serviceLocator.getSiteProductServiceDao().merge(service);
                            //update thisSite
                            thisSite.setEndDate(service.getEndDate());
                            thisSite.setUpdatedDate(new Date());
                            serviceLocator.getSiteDao().merge(thisSite);
                        }
                    }
                }
            }
        }
        isUpdate = true;

        return isUpdate;
    }
    @Override
    public boolean changeOrderStatus(Order order, String orderStatus, Long orderId) throws IOException, ClassNotFoundException {
        boolean isUpdate = false;
        Site site = serviceLocator.getSystemContext().getSite();
        if (order.getOrderType().equals(Order.OWNER_PARTNER)) {
            //if order is paid. Status changed from NEW_ORDER to PAID
            SessionObject sessionObject = orderSessionDao.getOrderSession(orderId, site);
            List<ItemMap> items = sessionObject.getOrder().getItems();
            User user = SessionUtil.loadUser(sessionObject);
            //get partner from the customer site;
            User partnerUser = null;
            if ("PAID".equals(orderStatus)) {
                //If current status is NEW_ORDER. Note: We don't allow order is cancelled is changed to PAID or NEW_ORDER
                if ("NEW_ORDER".equals(order.getStatus())) {

                    //START Transfer commission to partner if any.
                    //If user is not partner
                    if (user != null) {
                        //If user (not a partner) order
                        if (!"Y".equals(user.getPartnerStatus())) {
                            partnerUser = serviceLocator.getUserDao().getPartnerFromUser (user);
                            String partnerPromoCode = null;
                            if (partnerUser != null) {
                                partnerPromoCode = "PARTNER_" + partnerUser.getPartnerStatus() +"_"+partnerUser.getPartner()+"_"+partnerUser.getPartnerLevel();
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
                                            award.execute(sessionObject, partnerUser, promotion);
                                        }
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //Calculate commission for partner
                                items = sessionObject.getOrder().getItems();
                                if (items != null) {
                                    long discountForPartner = 0;
                                    long totalPriceForOwner = 0;
                                    for (ItemMap item : items) {
                                        /*
                                        We need to update price of product to make sure the price is the last one. Avoiding some case,
                                        users add items to cart and in that time, the items have a discount price. But now, the discount
                                        is expired
                                        */
                                        if (item.getProductVariantId() != null && item.getProductVariantId() > 0) {
                                            discountForPartner += (item.getQuantity() * item.getPriceItemPromoDiscount());
                                            totalPriceForOwner += (item.getQuantity() * item.getFinalPriceItem()) - (item.getQuantity() * item.getPriceItemPromoDiscount());
                                        }
                                    }
                                    UserBalanceHistory userBalanceHistory = new UserBalanceHistory();
                                    long newBalance = 0l;
                                    //client pay from partner site, partner can keep commission, we will subtract a mount of money that need to pay to main site from balance account.
                                    if (site.getSiteType().equals(Site.PARTNER)) {
                                        newBalance = (partnerUser != null ? partnerUser.getBalance() : 0) - totalPriceForOwner;
                                        userBalanceHistory.setAmount(-totalPriceForOwner);
                                        userBalanceHistory.setName("Charge amount of money paid for order: " + orderId + " and new balance is: " + newBalance);
                                        //client pay from main site, partner can keep commission, we will transfer commission to balance account.
                                    } if (site.getSiteType().equals(Site.OWNER)) {
                                        newBalance = (partnerUser != null ? partnerUser.getBalance() : 0) + discountForPartner;
                                        userBalanceHistory.setAmount(discountForPartner);
                                        userBalanceHistory.setName("Commission amount of money is added from order: " + orderId + " and new balance is: " + newBalance);
                                    }
                                    order.setCommission(discountForPartner);
                                    userBalanceHistory.setUser(partnerUser);
                                    serviceLocator.getUserBalanceHistoryDao().persist(userBalanceHistory);
                                    partnerUser.setBalance(newBalance);
                                    serviceLocator.getUserDao().merge(partnerUser);
                                }
                            }
                        } else { //partner paid for their client on partner site
                            if (Site.PARTNER.equals(site.getSiteType())) {
                                //Update new balance
                                Long newBalance = user.getBalance() - sessionObject.getOrder().getTotalPrice();
                                user.setBalance(newBalance);
                                serviceLocator.getUserDao().merge(user);
                                //insert user balance history
                                UserBalanceHistory userBalanceHistory = new UserBalanceHistory();
                                userBalanceHistory.setAmount(-sessionObject.getOrder().getTotalPrice());
                                userBalanceHistory.setUser(user);
                                userBalanceHistory.setName("Charge amount of money paid for order: " + orderId + " and new balance is: " + newBalance);
                                serviceLocator.getUserBalanceHistoryDao().persist(userBalanceHistory);
                            } else {
                                //Don't need to do anything.
                            }
                        }
                        //END Transfer commission to partner if any.
                    }

                    for (ItemMap item: items) {
                        Long varId = item.getProductVariantId();
                        if (varId != null && varId > 0 && !item.isChildItem()) {
                            ProductVariant variant = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findById(varId);
                            if (variant != null) {
                                Long thisSiteId = item.getLong("SITE_ID");
                                if (thisSiteId > 0) {
                                    Site thisSite = serviceLocator.getSiteDao().findById(thisSiteId);
                                    Long productId = item.getId();
                                    SiteProductService service = serviceLocator.getSiteProductServiceDao().getSiteProductService(productId, thisSiteId);
                                    if (service == null) {
                                        //Create new service
                                        service = new SiteProductService();
                                        service.setStartDate(thisSite.getStartDate());
                                        //calculate endDate, get the endDate from site table
                                        Integer months = item.getQuantity();
                                        Calendar endDate = Calendar.getInstance();
                                        endDate.setTime(thisSite.getEndDate());
                                        endDate.add(Calendar.MONTH, months);
                                        service.setEndDate(endDate.getTime());
                                        service.setActive("Y");
                                        service.setProductVariant(variant);
                                        service.setSite(thisSite);
                                        Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().findById(productId);
                                        service.setProduct(product);
                                        serviceLocator.getSiteProductServiceDao().persist(service);
                                        //update thisSite
                                        thisSite.setEndDate(service.getEndDate());
                                        thisSite.setUpdatedDate(new Date());
                                        serviceLocator.getSiteDao().merge(thisSite);

                                    } else {
                                        //update service
                                        service.setStartDate(thisSite.getStartDate());
                                        //calculate endDate, get the endDate from site table
                                        Integer months = item.getQuantity();
                                        Calendar endDate = Calendar.getInstance();
                                        endDate.setTime(thisSite.getEndDate());
                                        endDate.add(Calendar.MONTH, months);
                                        service.setEndDate(endDate.getTime());
                                        service.setActive("Y");
                                        service.setProductVariant(variant);
                                        service.setSite(thisSite);
                                        Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().findById(productId);
                                        service.setProduct(product);
                                        serviceLocator.getSiteProductServiceDao().merge(service);
                                        //update thisSite
                                        thisSite.setEndDate(service.getEndDate());
                                        thisSite.setUpdatedDate(new Date());
                                        serviceLocator.getSiteDao().merge(thisSite);
                                    }
                                }
                            }
                        }
                    }
                    order.setStatus(orderStatus);
                    order.setUpdatedDate(new Date());
                    this.orderDao.merge(order);
                    isUpdate = true;
                }
            } else if ("CANCELLED".equals(orderStatus)) {
                /*If we are changing from PAID to CANCELLED*/
                if ("PAID".equals(order.getStatus())) {

                    //START subtract commission to partner if any.
                    //If user is not partner
                    if (user != null) {
                        //User is not a partner. Get partner of them if any
                        if (!"Y".equals(user.getPartnerStatus())) {
                            partnerUser = serviceLocator.getUserDao().getPartnerFromUser (user);
                            String partnerPromoCode = null;
                            if (partnerUser != null) {
                                partnerPromoCode = "PARTNER_" + partnerUser.getPartnerStatus() +"_"+partnerUser.getPartner()+"_"+partnerUser.getPartnerLevel();
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
                                            award.execute(sessionObject, partnerUser, promotion);
                                        }
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //Calculate commission for partner
                                items = sessionObject.getOrder().getItems();
                                if (items != null) {
                                    long discountForPartner = 0;
                                    long totalPriceForOwner = 0;
                                    for (ItemMap item : items) {
                                        /*
                                        We need to update price of product to make sure the price is the last one. Avoiding some case,
                                        users add items to cart and in that time, the items have a discount price. But now, the discount
                                        is expired
                                        */
                                        if (item.getProductVariantId() != null && item.getProductVariantId() > 0) {
                                            discountForPartner += (item.getQuantity() * item.getPriceItemPromoDiscount());
                                            totalPriceForOwner += (item.getQuantity() * item.getFinalPriceItem()) - (item.getQuantity() * item.getPriceItemPromoDiscount());
                                        }
                                    }
                                    //TODO: When we cancel, we need to return money after charge number of day used.
                                    UserBalanceHistory userBalanceHistory = new UserBalanceHistory();
                                    long newBalance = 0l;
                                    //client pay from partner site, partner can keep commission, we will subtract a mount of money that need to pay to main site from balance account.
                                    if (site.getSiteType().equals(Site.PARTNER)) {
                                        newBalance = (partnerUser != null ? partnerUser.getBalance() : 0) + totalPriceForOwner;
                                        userBalanceHistory.setAmount(totalPriceForOwner);
                                        userBalanceHistory.setName("Return amount is subtracted for order (because of cancel order): "+orderId +" and new balance is: " + newBalance);
                                        //client pay from main site, partner can keep commission, we will transfer commission to balance account.
                                    } if (site.getSiteType().equals(Site.OWNER)) {
                                        newBalance = (partnerUser != null ? partnerUser.getBalance() : 0) - discountForPartner;
                                        userBalanceHistory.setAmount(-discountForPartner);
                                        userBalanceHistory.setName("Commission amount is subtracted from order (because of cancel order): "+orderId +" and new balance is: " + newBalance);
                                    }
                                    order.setCommission(0l);
                                    userBalanceHistory.setUser(partnerUser);
                                    serviceLocator.getUserBalanceHistoryDao().persist(userBalanceHistory);
                                    partnerUser.setBalance(newBalance);
                                    serviceLocator.getUserDao().merge(partnerUser);
                                }
                            }
                        } else { //partner paid for their client
                            if (Site.PARTNER.equals(site.getSiteType())) {
                                //Update new balance
                                Long newBalance = user.getBalance() + sessionObject.getOrder().getTotalPrice();
                                user.setBalance(newBalance);
                                serviceLocator.getUserDao().merge(user);
                                //insert user balance history
                                UserBalanceHistory userBalanceHistory = new UserBalanceHistory();
                                userBalanceHistory.setAmount(-sessionObject.getOrder().getTotalPrice());
                                userBalanceHistory.setUser(user);
                                userBalanceHistory.setName("Charge amount of money paid for order: " + orderId + " and new balance is: " + newBalance);
                                serviceLocator.getUserBalanceHistoryDao().persist(userBalanceHistory);
                            }
                        }
                    }
                    //END subtract commission to partner if any.

                    //rollback expired date
                    for (ItemMap item: items) {
                        Long varId = item.getProductVariantId();
                        if (varId != null && varId > 0 && !item.isChildItem()) {
                            ProductVariant variant = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findById(varId);
                            if (variant != null) {
                                Long thisSiteId = item.getLong("SITE_ID");
                                if (thisSiteId > 0) {
                                    Site thisSite = serviceLocator.getSiteDao().findById(thisSiteId);
                                    Long productId = item.getId();
                                    SiteProductService service = serviceLocator.getSiteProductServiceDao().getSiteProductService(productId, thisSiteId);
                                    if (service != null) {
                                        //update service table
                                        Integer months = item.getQuantity();
                                        Calendar endDate = Calendar.getInstance();
                                        endDate.setTime(thisSite.getEndDate());
                                        endDate.add(Calendar.MONTH, -months);
                                        service.setEndDate(endDate.getTime());
                                        serviceLocator.getSiteProductServiceDao().merge(service);
                                        //update thisSite
                                        thisSite.setEndDate(service.getEndDate());
                                        thisSite.setUpdatedDate(new Date());
                                        serviceLocator.getSiteDao().merge(thisSite);
                                    }
                                }
                            }
                        }
                    }
                    /*If we are changing from NEW_ORDER to CANCELLED*/
                }
                order.setStatus(orderStatus);
                order.setUpdatedDate(new Date());
                this.orderDao.merge(order);
                isUpdate = true;
            }
            //For orders from client sites
        } else if (order.getOrderType() == Order.CLIENT) {
            //if cancel order
            if ("CANCELLED".equals(orderStatus) || "RETURNED".equals(orderStatus)) {
                SessionObject orderObject = orderSessionDao.getOrderSession(orderId, serviceLocator.getSystemContext().getSite());
                List<ItemMap> items = orderObject.getOrder().getItems();
                for (ItemMap item : items) {
                    Long productVariantId = item.getProductVariantId();
                    ProductVariant v = serviceLocator.getProductVariantDao().findById(productVariantId);
                    if (v != null) {
                        int i = v.getInventory() + item.getQuantity();
                        v.setInventory(i);
                        serviceLocator.getProductVariantDao().merge(v);
                    }
                }
            }
            order.setStatus(orderStatus);
            order.setUpdatedDate(new Date());
            this.orderDao.merge(order);
            isUpdate = true;
        }
        return isUpdate;
    }

    private Order buildOrderAttribute (SessionObject sessionObject) {
        Order order =  new Order();
        order.setUserId(sessionObject.getUserId());
        order.setFirstName(sessionObject.getAddresses().getBillingAddress().getFirstName());
        order.setLastName(sessionObject.getAddresses().getBillingAddress().getLastName());
        order.setAddress_1(sessionObject.getAddresses().getBillingAddress().getStreet());
        order.setDistrict(sessionObject.getAddresses().getBillingAddress().getDistrict());
        order.setCity(sessionObject.getAddresses().getBillingAddress().getCity());
        order.setState(sessionObject.getAddresses().getBillingAddress().getState());
        order.setZipCode(sessionObject.getAddresses().getBillingAddress().getZipCode());
        order.setPhone(sessionObject.getAddresses().getBillingAddress().getPhone());

        order.setFirstNameShipping(sessionObject.getAddresses().getShippingAddress().getFirstName());
        order.setLastNameShipping(sessionObject.getAddresses().getShippingAddress().getLastName());
        order.setAddress_1Shipping(sessionObject.getAddresses().getShippingAddress().getStreet());
        order.setDistrictShipping(sessionObject.getAddresses().getShippingAddress().getDistrict());
        order.setCityShipping(sessionObject.getAddresses().getShippingAddress().getCity());
        order.setStateShipping(sessionObject.getAddresses().getShippingAddress().getState());
        order.setZipCodeShipping(sessionObject.getAddresses().getShippingAddress().getZipCode());
        order.setPhoneShipping(sessionObject.getAddresses().getShippingAddress().getPhone());

        order.setEmail(sessionObject.getAddresses().getBillingAddress().getEmail());
        String thirdPartyOrderNumber = sessionObject.getOrder().getString("THIRD_PARTY_ORDER_NUMBER");
        if (StringUtils.isNotEmpty(thirdPartyOrderNumber)) {
            order.setStatus("PAID");
            order.setThirdPartyOrderNumber(thirdPartyOrderNumber);
        } else {
            order.setStatus("NEW_ORDER");
        }
        order.setTotalPrice(sessionObject.getOrder().getTotalPrice());
        order.setUpdatedDate(new Date());
        order.setSite(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite());

        return order;
    }
}
