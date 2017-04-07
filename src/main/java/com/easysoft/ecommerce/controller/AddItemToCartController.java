package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.component.RunProcessComponent;
import com.easysoft.ecommerce.service.payment.Payment;
import com.easysoft.ecommerce.util.*;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping()
public class AddItemToCartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddItemToCartController.class);
    private ServiceLocator serviceLocator;

    @Autowired
    public AddItemToCartController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @RequestMapping(value = "/checkout/basket.html", method = RequestMethod.GET)
    public ModelAndView basket(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionObject so = SessionUtil.load(request, response);
        Messages messages = new Messages();
        RunProcessComponent process = new RunProcessComponent();
        process.runProcessComponent(so, "checkout-basket", messages);

        return new ModelAndView("/checkout/basket", "messages", messages);
    }

    @RequestMapping(value = "/checkout/addtocart.html", method = RequestMethod.POST)
    public ModelAndView addItemToCart(@Valid AddToCartForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {

//        Validator validator = new InventoryValidator();
//        validator.validate(form, result);
//        if (result.hasErrors()) {
//            Product product = this.serviceLocator.getProductDao().getProduct(form.getProductId());
//            ProductVariant productVariant = this.serviceLocator.getProductVariantDao().findById(form.getProductVariantId());
//            ModelAndView mav = new ModelAndView("/product/"+product.getUri()+"/index.html");
//            mav.getModel().putAll(result.getModel());
//            return mav;
//        }
        Messages messages = new Messages();
        Product product = this.serviceLocator.getProductDao().getProduct(form.getProductId());
        ProductVariant productVariant = this.serviceLocator.getProductVariantDao().findById(form.getProductVariantId());
        SessionObject so = SessionUtil.load(request, response);
        if (productVariant != null && productVariant.getInventory() > 0) {
            if (product != null) {
                List<ItemMap> items = so.getOrder().getItems();
                if (isVariantExistedInCart(productVariant, items)) {
                    for (ItemMap it : items) {
                        if (it.getProductVariantId().longValue() == form.getProductVariantId().longValue()) {
                            it.setQuantity(it.getQuantity() + form.getQuantity());
                            String arr[] = {product.getName(), product.getModel(), String.valueOf(it.getQuantity())};
                            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.variants.existed", arr, LocaleContextHolder.getLocale()));
                        }
                    }
                } else {

                    ItemMap item = new ItemMap();
                    item.setId(product.getId());
                    item.setName(product.getName());
                    item.setItemUri(product.getUri());
                    item.setWeight(product.getWeight());
                    item.setProductVariantId(form.getProductVariantId());
                    item.setModelNumber(product.getModel());
                    item.setColor(productVariant.getColorName());
                    item.setSize(productVariant.getSizeName());
                    item.setQuantity(form.getQuantity());
                    ProductFile image = serviceLocator.getProductFileDao().getDefaultImage(product.getId(), "PRODUCT_FILE_IMAGE");
                    if (image != null) {
                        item.setImageUrl(image.getUri());
                    }
                    so.getOrder().addItem(item);
                }
                for (ItemMap it : items) {
                    if (productVariant.getId().longValue() == it.getProductVariantId().longValue() && it.getQuantity() > productVariant.getInventory()) {
                        it.setQuantity(productVariant.getInventory());
                        String arr[] = {String.valueOf(it.getQuantity()), product.getName(), product.getModel()};
                        messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.lager", arr, LocaleContextHolder.getLocale()));
                        break;
                    }
                }


                SessionUtil.save(so);
                RunProcessComponent process = new RunProcessComponent();
                process.runProcessComponent(so, "checkout-basket", messages);

            }
        } else {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.empty", null, LocaleContextHolder.getLocale()));
        }

        return new ModelAndView("/checkout/basket", "messages", messages);
    }

    @RequestMapping(value = "/checkout/addtocart.html", method = RequestMethod.GET)
    public String addItemToCart() throws Exception {
        return "/checkout/basket";
    }

    private boolean isVariantExistedInCart(ProductVariant productVariant, List<ItemMap> items) {
        for (ItemMap it : items) {
            if (it.getProductVariantId().longValue() == productVariant.getId().longValue()) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping(value = "/checkout/updatecart.html", method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView addItemToCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        List<ItemMap> items = so.getOrder().getItems();
        Messages messages = new Messages();
        boolean isUpdate = false;
        for (int i = 0; i < items.size(); i++) {
            ItemMap item = items.get(i);
            //set gift flag
//            String gift = request.getParameter("giftwrap_" + i);
//            if (WebUtil.isChecked(gift)) {
//                item.setGiftBox("true");
//            } else {
//                item.setGiftBox("false");
//            }
            String quantity = request.getParameter("quantity_" + item.getProductVariantId());
            if (!StringUtils.isEmpty(quantity)) {
                if (StringUtils.isNumeric(quantity)) {
                    Integer qty = Integer.valueOf(quantity);
                    if (qty > 0) {
                        ProductVariant productVariant = this.serviceLocator.getProductVariantDao().findById(item.getProductVariantId());
                        Product product = this.serviceLocator.getProductDao().findById(item.getId());
                        if (productVariant != null && productVariant.getInventory() > 0) {
                            if (qty > productVariant.getInventory()) {
                                item.setQuantity(productVariant.getInventory());
                                String arr[] = {String.valueOf(productVariant.getInventory()), item.getName(), product.getModel()};
                                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.lager", arr, LocaleContextHolder.getLocale()));
                            } else {
                                item.setQuantity(qty);
                            }
                        } else {
                            messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.empty", null, LocaleContextHolder.getLocale()));
                        }
                        //update weight in case have any change.
                        item.setWeight(product.getWeight());
                    } else {
                        items.remove(i);//remove if user enter negative number
                    }
                } else {
                    items.remove(i);//remove if user leave blank field
                }
                isUpdate = true;
            }
        }
        if (isUpdate) {
            SessionUtil.save(so);
            RunProcessComponent process = new RunProcessComponent();
            process.runProcessComponent(so, "checkout-basket", messages);
        }
        return new ModelAndView("/checkout/basket", "messages", messages);
    }

    @RequestMapping(value = "/checkout/{index}/checkgiftbox.html")
    public String updateGiftbox(@PathVariable Integer index, HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionObject so = SessionUtil.load(request, response);
        List<ItemMap> items = so.getOrder().getItems();
        String check = request.getParameter("flag");

        if (items.size() > index) {
            if (!StringUtils.isEmpty(check) && check.equalsIgnoreCase("true")) {
                items.get(index).setGiftBox("true");
            } else {
                items.get(index).setGiftBox("false");
            }

        }

        return "redirect:/checkout/basket.html";
    }

    @RequestMapping("/checkout/{index}/deleteItemInCart.html")
    public ModelAndView deleteItemInCart(@PathVariable Integer index, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        so.getOrder().removeItem(index);
        SessionUtil.save(so);
        return new ModelAndView("redirect:/checkout/basket.html");
    }

    @RequestMapping("/checkout/billing_shipping.html")
    public String billingShipping(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        Site site = serviceLocator.getSystemContext().getSite();
        //set default promo code for each order
        so.getOrder().setDefaultPromoCode(site.getSiteParamsMap().get("DEFAULT_PROMO_CODE"));
        if (so.getOrder().getItems() == null || so.getOrder().getItems().isEmpty()) {
            return "/checkout/basket";
        } else {
//            if (StringUtils.isEmpty(so.getUserName())) {
//                return "redirect:/user/login.html?source=/checkout/billing_shipping";
//            }
        }
        return "/checkout/billing_shipping";
    }

    @RequestMapping(value = "/checkout/review.html", method = RequestMethod.POST)
    public ModelAndView saveUserAndReviewOrder(@Valid BillingShippingForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);

        //set billing address
        so.getAddresses().getBillingAddress().setFirstName(form.getFirstNameForBilling());
        so.getAddresses().getBillingAddress().setLastName(form.getLastNameForBilling());
        so.getAddresses().getBillingAddress().setStreet(form.getAddress1ForBilling());
        so.getAddresses().getBillingAddress().setDistrict(form.getDistrictForBilling());
        so.getAddresses().getBillingAddress().setCity(form.getCityForBilling());
        so.getAddresses().getBillingAddress().setState(form.getStateForBilling());
        so.getAddresses().getBillingAddress().setZipCode(form.getZipCodeForBilling());
        so.getAddresses().getBillingAddress().setCountry(form.getCountryForBilling());
        so.getAddresses().getBillingAddress().setPhone(form.getPhoneForBilling());
        so.getAddresses().getBillingAddress().setEmail(form.getEmailAddressForBilling());

        if (form.isShipToBillingAddress()) {
            //set shipping address from billing
            so.getAddresses().getShippingAddress().setFirstName(form.getFirstNameForBilling());
            so.getAddresses().getShippingAddress().setLastName(form.getLastNameForBilling());
            so.getAddresses().getShippingAddress().setStreet(form.getAddress1ForBilling());
            so.getAddresses().getShippingAddress().setDistrict(form.getDistrictForBilling());
            so.getAddresses().getShippingAddress().setCity(form.getCityForBilling());
            so.getAddresses().getShippingAddress().setState(form.getStateForBilling());
            so.getAddresses().getShippingAddress().setZipCode(form.getZipCodeForBilling());
            so.getAddresses().getShippingAddress().setCountry(form.getCountryForBilling());
            so.getAddresses().getShippingAddress().setPhone(form.getPhoneForBilling());
        } else {
            //set a different shipping address
            so.getAddresses().getShippingAddress().setFirstName(form.getFirstNameForShipping());
            so.getAddresses().getShippingAddress().setLastName(form.getLastNameForShipping());
            so.getAddresses().getShippingAddress().setStreet(form.getAddress1ForShipping());
            so.getAddresses().getShippingAddress().setDistrict(form.getDistrictForShipping());
            so.getAddresses().getShippingAddress().setCity(form.getCityForShipping());
            so.getAddresses().getShippingAddress().setState(form.getStateForShipping());
            so.getAddresses().getShippingAddress().setZipCode(form.getZipCodeForShipping());
            so.getAddresses().getShippingAddress().setCountry(form.getCountryForShipping());
            so.getAddresses().getShippingAddress().setPhone(form.getPhoneForShipping());
        }
        //Create new user: TODO: Will support later.
//        if (!StringUtils.isEmpty(so.getString("USERNAME_TEMP")) && !StringUtils.isEmpty(so.getString("PASSWORD_TEMP"))) {
//
//            User user = serviceLocator.getUserDao().getClientUser(so.getString("USERNAME_TEMP"), so.getString("PASSWORD_TEMP"), serviceLocator.getSystemContext().getSite());
//            if (user != null) {
//                throw new IllegalAccessException("This user is exsiting in our system");
//            }
//            user = new User();
//            setUserAttributes(so, user);
//            user.setUsername((String) so.get("USERNAME_TEMP"));
//            user.setPassword((String) so.get("PASSWORD_TEMP"));
//            user.setBlocked("N");
//            user.setSiteAdmin("N");
//            user.setSiteUser("Y");
//            //TODO: user customer instead
//            //user.setSite(serviceLocator.getSystemContext().getSite());
//
//            //create new user
//            serviceLocator.getUserService().createOrUpdate(user);
//            //set username before remove user temp. Next time, we will go through checkout process without login again.
//            so.setUserName((String) so.get("USERNAME_TEMP"));
//            so.setUserId(user.getId());
//
//            //remove temp username if create successfully.
//            so.remove("USERNAME_TEMP");
//            so.remove("PASSWORD_TEMP");
//        } else { //old account, need to update
//            User user = serviceLocator.getUserDao().getClientUser(so.getUserName(), serviceLocator.getSystemContext().getSite());
//            if (user != null) {
//                setUserAttributes(so, user);
//                //update user
//                serviceLocator.getUserService().createOrUpdate(user);
//            }
//        }
        so.getOrder().setShippingMethod(form.getShippingCode());
        so.getOrder().setPaymentMethod(form.getPaymentProviderId());
        so.getOrder().set("SHIPPING_TO_BILLING", form.isShipToBillingAddress());
        RunProcessComponent process = new RunProcessComponent();
        Messages message = new Messages();
        process.runProcessComponent(so, "checkout-shipping", message);
        SessionUtil.save(so);
        return new ModelAndView("/checkout/review", "messages", message);
    }

    private void setUserAttributes(SessionObject so, User user) {
        user.setEmail(so.getAddresses().getBillingAddress().getEmail());
        //Billing address
        user.setFirstName(so.getAddresses().getBillingAddress().getFirstName());
        user.setLastName(so.getAddresses().getBillingAddress().getLastName());
        user.setAddress_1(so.getAddresses().getBillingAddress().getStreet());
        user.setDistrict(so.getAddresses().getBillingAddress().getDistrict());
        user.setCity(so.getAddresses().getBillingAddress().getCity());
        user.setState(so.getAddresses().getBillingAddress().getState());
        user.setZipCode(so.getAddresses().getBillingAddress().getZipCode());
        user.setCountry(so.getAddresses().getBillingAddress().getCountry());
        user.setPhone(so.getAddresses().getBillingAddress().getPhone());

        //Shipping address
        user.setFirstNameShipping(so.getAddresses().getShippingAddress().getFirstName());
        user.setLastNameShipping(so.getAddresses().getShippingAddress().getLastName());
        user.setAddress_1Shipping(so.getAddresses().getShippingAddress().getStreet());
        user.setDistrictShipping(so.getAddresses().getShippingAddress().getDistrict());
        user.setCityShipping(so.getAddresses().getShippingAddress().getCity());
        user.setStateShipping(so.getAddresses().getShippingAddress().getState());
        user.setZipCodeShipping(so.getAddresses().getShippingAddress().getZipCode());
        user.setCountryShipping(so.getAddresses().getShippingAddress().getCountry());
        user.setPhoneShipping(so.getAddresses().getShippingAddress().getPhone());

    }

    @RequestMapping(value = "/checkout/review.html")
    public String reviewOrderGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        if (so.getOrder().getItems() == null || so.getOrder().getItems().isEmpty()) {
            return "/checkout/basket";
        } else {
//            if (StringUtils.isEmpty(so.getUserName())) {
//                return "redirect:/user/login.html?source=/checkout/review";
//            }
        }
        return "/checkout/review";
    }

    /**
     * This method will place order. Here is what the method will do:
     * 1.  Create order on Order and Order_Session tables
     * 2.  Store the order status is New Order. (Notes: New Order: order just created, have not paid yet)
     * 3.  Decrease inventory of the product. In case, customers have not paid for this order, the order will be cancelled by admin
     * and rollback quantity of product in inventory.
     * 4.  Redirect to Payment Provider
     * 5.  Send email to the client said that order was created. But not make a payment.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkout/payment.html")
    public String payment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        if (so.getOrder().getItems() == null || so.getOrder().getItems().isEmpty()) {
            return "/checkout/basket";
        } else if ("GET".equalsIgnoreCase(request.getMethod())) {
            return "/checkout/review";
        }

        /* 1.  Create order on Order and Order_Session tables
         * 2.  Store the order status is New Order. (Notes: New Order: order just created, have not paid yet)
         * 3.  Decrease inventory of the product. In case, customers have not paid for this order, the order will be cancelled by admin and rollback quantity of product in inventory.
         * 4.  Redirect to Payment Provider
         * */

        Order order = serviceLocator.getOrderService().createOrder(so);
        so.getOrder().set("ORDER_ID", String.valueOf(order.getId()));
        //Send email to user
        Site site = serviceLocator.getSystemContext().getSite();
        SessionObject orderObject = serviceLocator.getOrderService().getOrderSession(order.getId(), so.getUserId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("site", site);
        map.put("siteParam", site.getSiteParamsMap());
        map.put("order", order);
        map.put("orderObject", orderObject);
        for (ItemMap item : orderObject.getOrder().getItems()) {
            item.set("FINAL_PRICE_ITEM_EMAIL", Money.valueOf(String.valueOf(item.getFinalPriceItem()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
            item.set("TOTAL_PRICE_ITEM_EMAIL", Money.valueOf(String.valueOf(item.getFinalPriceItem() * item.getQuantity()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
            if (item.getPriceItemPromoDiscount() > 0) {
                item.set("PRICE_ITEM_PROMO_DISCOUNT_EMAIL", Money.valueOf(String.valueOf(item.getPriceItemPromoDiscount() * item.getQuantity()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
            }
        }
        orderObject.getOrder().set("SUB_TOTAL_PRICE_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getSubPriceTotal()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        orderObject.getOrder().set("TOTAL_PRICE_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getTotalPrice()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        if (orderObject.getOrder().getTaxPrice() != null) {
            orderObject.getOrder().set("TAX_PRICE_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getTaxPrice()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        } else {
            orderObject.getOrder().set("TAX_PRICE_EMAIL", Money.valueOf("0", site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        }
        orderObject.getOrder().set("SHIPPING_PRICE_EMAIL", Money.valueOf(String.valueOf(orderObject.getShippingFee()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        //shipping discount
        if (orderObject.getOrder().getShippingDiscountPrice() != null) {
            orderObject.getOrder().set("SHIPPING_PRICE_DISCOUNT_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getShippingDiscountPrice()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        } else {
            orderObject.getOrder().set("SHIPPING_PRICE_DISCOUNT_EMAIL", "");
        }
        //billing discount
        if (orderObject.getOrder().getSubPriceDiscountTotal() != null) {
            orderObject.getOrder().set("SUB_TOTAL_PRICE_DISCOUNT_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getSubPriceDiscountTotal()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        } else {
            orderObject.getOrder().set("SUB_TOTAL_PRICE_DISCOUNT_EMAIL", "");
        }

        EmailSite emailSite = serviceLocator.getEmailSiteDao().getEmailInfor(site.getId(), "orderconfirm");
        if (emailSite != null) {
            EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().findById(emailSite.getEmailTemplateId());
            String subject = emailTemplate.getSubject().replaceAll("[BRAND_NAME]", site.getName());
            String[] bcc = null;
            if (!StringUtils.isEmpty(site.getSiteParam("CONTACT_US"))) {
                bcc = site.getSiteParam("CONTACT_US").split(",");
            }
            if (StringUtils.isEmpty(order.getEmail())) {
                if (bcc != null && bcc.length > 0) {
                    serviceLocator.getMailService().sendEmail(emailSite.getSendFrom(), bcc[0], null, bcc, subject, map, emailTemplate.getTemplateFile());
                }
            } else {
                serviceLocator.getMailService().sendEmail(emailSite.getSendFrom(), order.getEmail(), null, bcc, subject, map, emailTemplate.getTemplateFile());
            }
        } else {
            EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("orderconfirm", serviceLocator.getLocale().toString());
            String subject = emailTemplate.getSubject().replaceAll("[BRAND_NAME]", site.getName());
            String[] bcc = null;
            if (!StringUtils.isEmpty(site.getSiteParam("CONTACT_US"))) {
                bcc = site.getSiteParam("CONTACT_US").split(",");
            }
            String from = site.getSiteParamsMap().get("CONTACT_US");
            if (StringUtils.isEmpty(from)) {
                from = "info@webphattai.com";
            }
            if (StringUtils.isEmpty(order.getEmail())) {
                if (bcc != null && bcc.length > 0) {
                    serviceLocator.getMailService().sendEmail(from, bcc[0], null, bcc, subject, map, emailTemplate.getTemplateFile());
                }
            } else {
                serviceLocator.getMailService().sendEmail(from, order.getEmail(), null, bcc, subject, map, emailTemplate.getTemplateFile());
            }
        }

        //Remove session object
        so.getOrder().setItems(null);

        Long providerId = so.getOrder().getPaymentMethod();
        PaymentProvider paymentProvider = serviceLocator.getPaymentProviderDao().getPaymentProviderSelected(providerId);//PaymentUtil.getProvider(providerId);
        Class c = Class.forName(paymentProvider.getProviderClass());
        Payment payment = (Payment) c.newInstance();
        String url = payment.createRequestUrl(so, serviceLocator.getSystemContext().getSite());
        if (StringUtils.isNotEmpty(url) && url.contains("?")) {
            url += "&orderId="+order.getId();
        } else {
            url += "?orderId="+order.getId();
        }
        so.setOrder(null);
        SessionUtil.save(so);
        return "redirect:" + url;
    }

    /** This action will be called after make a payment. It will callback to this method.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkout/receipt.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView receipt(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        Site site = serviceLocator.getSystemContext().getSite();
        //for testing only
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String o = request.getParameter("orderId");
            return new ModelAndView("/checkout/receipt", "order", serviceLocator.getOrderSessionDao().getOrder(new Long(o), so.getUserId(), site));
        }
        //forward to home page if don't have order_id
        if (so.getOrder() != null && !StringUtils.isNumeric(so.getOrder().get("ORDER_ID") + "")) {
            return new ModelAndView("/index");
        }
        //Payment provider call back after successfully payment.
        Long providerId = so.getOrder().getPaymentMethod();
        PaymentProvider paymentProvider = PaymentUtil.getProvider(providerId);
        Class c = Class.forName(paymentProvider.getProviderClass());
        Payment payment = (Payment) c.newInstance();
        Map map = request.getParameterMap();
        Map mapResponse = new HashMap();
        if (map != null) {
            //iterate through the java.util.Map and display posted parameter values
            //the keys of the Map.Entry objects ae type String; the values are type String[],
            //or String array
            for (Object o : map.entrySet()) {
                Map.Entry me = (Map.Entry) o;
                String key = (String) me.getKey();
                String value = "";
                String[] arr = (String[]) me.getValue();
                for (String anArr : arr) {
                    //print commas after multiple values,
                    //except for the last one
                    if (StringUtils.isEmpty(value)) {
                        value = anArr;
                    } else {
                        value += "," + anArr;
                    }

                }
                mapResponse.put(key, value);
            }
        }
        //flag = true, mean you complete payment.
        boolean flag = payment.verifyResponseUrl(so, mapResponse, serviceLocator.getSystemContext().getSite());
        //if reponse return is correct, update order to be paid status.
        String sOrderId = (String) so.getOrder().get("ORDER_ID");
        if (!StringUtils.isEmpty(sOrderId)) {
            Order order = null;
            sOrderId = sOrderId.replace(site.getSiteCode(), site.getSiteCode());
            if (StringUtils.isNumeric(sOrderId)) {
                order = serviceLocator.getOrderSessionDao().getOrder(Long.valueOf(sOrderId), so.getUserId(), site);
                if (flag) {  //if paid
                    order.setStatus("PAID");
                    serviceLocator.getOrderDao().merge(order);
                }
            }
            //Remove order information out of session object
            SessionUtil.clearSession(so.getId());
            return new ModelAndView("/checkout/receipt", "order", order);
        } else {
            return new ModelAndView("/index");
        }
    }

    @RequestMapping(value = "/checkout/applypromocode.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView applyPromoCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        if (so.getOrder().getItems() == null || so.getOrder().getItems().isEmpty()) {
            return new ModelAndView("/checkout/basket");
        }/* else {
            if (StringUtils.isEmpty(so.getUserName())) {
                return new ModelAndView("redirect:/user/login.html?source=/checkout/applypromocode");
            }
        }*/
        Messages message = new Messages();
        String promoCode = request.getParameter("promoCode");
        if (StringUtils.isEmpty(promoCode)) {
            message.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.promocode.required", null, LocaleContextHolder.getLocale()));
        } else {
            Promotion promotion = serviceLocator.getPromotionDao().getValidPromotion(promoCode);
            if (promotion != null) {
                so.getOrder().setPromoCode(promoCode);
                RunProcessComponent process = new RunProcessComponent();
                process.runProcessComponent(so, "checkout-applyPromoCode", message);
            } else {
                String[] p = new String[1];
                p[0] = promoCode;
                message.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.promocode.invalid", p, LocaleContextHolder.getLocale()));
            }
        }
        return new ModelAndView("/checkout/review", "promo_messages", message);
    }

    @RequestMapping(value = "/checkout/removepromocode.html")
    public ModelAndView removePromoCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        PromotionUtil.removePromotionDiscount(so);
        RunProcessComponent process = new RunProcessComponent();
        process.runProcessComponent(so, "checkout-basket", null);

        return new ModelAndView("/checkout/review");
    }

}
