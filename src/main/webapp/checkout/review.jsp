<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="review.recheck.order"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<link rel="stylesheet" href="/themes/m3x/css/pages/page_invoice.css">
<script type="text/javascript">
    $(function() {
        jQuery('#play_order').preventDoubleSubmit();
    });
</script>

<c:set var="items" value="${sessionObject.ORDER.ITEMS}"/>
<c:set var="order" value="${sessionObject.ORDER}"/>
<c:set var="billingAddress" value="${sessionObject.ADDRESSES.BILLING_ADDRESS}"/>
<c:set var="shippingAddress" value="${sessionObject.ADDRESSES.SHIPPING_ADDRESS}"/>

<!--=== Breadcrumbs v4 ===-->
<div class="breadcrumbs-v4 margin-bottom-20">
    <div class="container">
        <h1><fmt:message key="review.recheck.order"/></h1>
        <ul class="breadcrumb-v4-in">
            <li><a href="index.html">Home</a></li>
            <li><a href="/"><fmt:message key="basket.continue.shopping"/></a></li>
            <li class="active"><fmt:message key="review.recheck.order"/></li>
        </ul>
    </div><!--/end container-->
</div>
<!--=== End Breadcrumbs v4 ===-->
<div class="container shopping-cart margin-bottom-30">
    <h:frontendmessage _messages="${messages}"/>
    <section>
        <h:basketitems items="${items}" sessionObject="${sessionObject}" isBasketPage="false"/>
    </section>
    <div class="row invoice-info">
        <div class="col-sm-4">
            <div class="tag-box tag-box-v3">
                <h2><i class="fa fa-user"></i> <fmt:message key="review.billing.information"/> (<a
                        href="/checkout/basket.html"><fmt:message key="review.edit"/></a>)</h2>
                <ul class="list-unstyled">
                    <li>${billingAddress.LAST_NAME}&nbsp;${billingAddress.FIRST_NAME}</li>
                    <li>${billingAddress.ADDRESS_LINE}</li>
                    <c:if test="${!empty billingAddress.DISTRICT}">
                        <li>${billingAddress.DISTRICT}</li>
                    </c:if>
                    <c:set value='${billingAddress.CITY}' var="city"/>
                    <spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValue('CITY', serviceLocator.locale, city)" var="billingCity"/>
                    <li>${billingCity.value}</li>
                    <li>${billingAddress.PHONE}</li>
                    <li>${billingAddress.EMAIL}</li>
                </ul>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="tag-box tag-box-v3">
                <h2><i class="fa fa-truck"></i> <fmt:message key="review.shipping.address"/> (<a
                        href="/checkout/basket.html"><fmt:message key="review.edit"/></a>)</h2>
                <ul class="list-unstyled">
                    <li>${shippingAddress.LAST_NAME}&nbsp;${shippingAddress.FIRST_NAME}</li>
                    <li>${shippingAddress.ADDRESS_LINE}</li>
                    <c:if test="${!empty shippingAddress.ADDRESS_LINE}">
                        <li>${shippingAddress.DISTRICT}</li>
                    </c:if>
                    <c:set value='${shippingAddress.CITY}' var="city"/>
                    <spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValue('CITY', serviceLocator.locale, city)" var="shippingCity"/>
                    <li>${shippingCity.value}</li>
                    <li>${shippingAddress.PHONE}</li>
                </ul>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="tag-box tag-box-v3">
                <c:if test="${!empty sessionObject.ORDER.PAYMENT_METHOD_ID}">
                    <h2><i class="fa fa-money"></i> <fmt:message key="review.billing.method"/></h2>
                    <spring:eval expression="serviceLocator.getPaymentProviderSiteDao().findById(sessionObject.ORDER.PAYMENT_METHOD_ID, site.id)" var="paymentMethod"/>
                    <div><b>${paymentMethod.name}</b></div>
                    <c:if test="${!empty paymentMethod.description}">
                        <% pageContext.setAttribute("newLine", "\n"); %>
                        <c:set value="${fn:replace(paymentMethod.description, newLine, '<br>')}" var="paymentDescription"/>
                        <div>${paymentDescription}1</div>
                    </c:if>
                </c:if>
                <c:if test="${!empty sessionObject.ORDER.SHIPPING_METHOD_ID}">
                    <h2><i class="fa fa-truck"></i> <fmt:message key="review.shipping.method"/></h2>
                    <spring:eval expression="serviceLocator.getShippingSiteDao().findById(sessionObject.ORDER.SHIPPING_METHOD_ID, site.id)" var="shippingType"/>
                    <div>${shippingType.name}</div>
                </c:if>
            </div>
        </div>
    </div>

    <div class="coupon-code margin-bottom-40">
        <div class="row">
            <div class="col-sm-4 sm-margin-bottom-30">
                <c:set var="promoCode" value="${sessionObject.ORDER.PROMO_CODE}"/>
                <c:choose>
                    <c:when test="${!empty promoCode}">
                        <p>
                            <fmt:message key="message.promocode.using"/>&nbsp;&nbsp;<b>${promoCode}</b>&nbsp;&nbsp;<a
                                href="/checkout/removepromocode.html"><fmt:message key="review.remove"/></a>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <form id="promo_code" method="post" action="/checkout/applypromocode.html">
                            <h3><fmt:message key="review.promo.code"/></h3>
                            <p><fmt:message key="please.enter.promo.code"/></p>
                            <h:frontendmessage _messages="${promo_messages}"/>
                            <input type="text" maxlength="20" size="20" value="" name="promoCode" id="che-bas-promoCode" class="form-control margin-bottom-10">
                            <button type="submit" class="btn-u btn-u-sea-shop"><fmt:message key="review.apply"/></button>
                        </form>
                    </c:otherwise>
                </c:choose>


            </div>
            <div class="col-sm-4 col-sm-offset-4">
                <ul class="list-inline total-result">
                    <li>
                        <h4><fmt:message key="basket.subtotal"/>:</h4>
                        <spring:eval expression="sessionObject.getOrder().getSubPriceTotal()" var="subPrice"/>
                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalPrice"/>
                        <div class="total-result-in">
                            <span>${subTotalPrice}</span>
                        </div>
                    </li>
                    <spring:eval expression="sessionObject.getOrder().getSubPriceDiscountTotal()" var="subTotalDiscount"/>
                    <c:if test="${!empty subTotalDiscount && subTotalDiscount > 0}">
                        <li>
                            <h4><fmt:message key="subordertotal-discount"/>:</h4>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subTotalDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalDiscount"/>
                            <div class="total-result-in">
                                <span class="text-right">-${subTotalDiscount}</span>
                            </div>
                        </li>
                    </c:if>
                    <li>
                        <h4><fmt:message key="shipping.fee"/>:</h4>
                        <spring:eval expression="sessionObject.getShippingFee()" var="shippingPrice"/>
                        <spring:eval expression="sessionObject.getOrder().getShippingDiscountPrice()" var="shippingPriceDiscount"/>
                        <%--Shipping price > 0, Check discount for shipping fee--%>
                        <c:if test="${!empty shippingPrice && shippingPrice > 0}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>
                            <div class="total-result-in">
                                <span class="text-right">${shipping_Price}</span>
                            </div>
                            <c:if test="${shippingPriceDiscount > 0}">
                                <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPriceDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price_Discount"/>
                                <h4><fmt:message key="basket.discount.shipping.fee"/>:</h4>
                                <div class="total-result-in">
                                    <span class="text-right">-${shipping_Price_Discount}</span><br>
                                </div>
                            </c:if>
                        </c:if>
                        <%--Shipping price is 0, don't care discount for shipping fee--%>
                        <c:if test="${empty shippingPrice || shippingPrice <= 0}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>
                            <div class="total-result-in">
                                <span class="text-right">${shipping_Price}</span><br>
                            </div>
                        </c:if>
                    </li>
                    <li>
                        <h4><fmt:message key="basket.tax"/>:</h4>
                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(sessionObject.getOrder().getTaxPrice(),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="tax_Price"/>
                        <div class="total-result-in">
                            <span>${tax_Price}</span>
                        </div>
                    </li>
                    <li class="divider"></li>
                    <li class="total-price">
                        <h4><fmt:message key="basket.ordertotal"/>:</h4>
                        <spring:eval expression="sessionObject.getOrder().getTotalPrice()" var="price"/>
                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="totalPrice"/>
                        <div class="total-result-in">
                            <span>${totalPrice}</span>
                        </div>
                    </li>
                </ul>
                <form:form id="play_order" action="/checkout/payment.html" commandName="command" method="post">
                    <div style="width: 97%;" class="che-ord-rev-place-order-container">
                            <%--Double click prevent http://pitchpublish.com/blog/?p=62--%>
                        <button type="submit" class="btn-u btn-u-sea-shop"><fmt:message key="review.place.order"/></button>
                    </div>
                </form:form>

            </div>
        </div>
    </div>

</div>


    <%--<div class='page-body body-with-border page-body-float-left'>--%>

    <%--<div id="glo-body-content">--%>

        <%--<div id="che-ord-rev-checkout-cont">--%>
            <%--<div id="che-ord-rev-checkout">--%>
                <%--<div class="che-ord-rev-header">--%>
                    <%--<div class="billing-global-header"><h1><fmt:message key="checkout.lable.header"/></h1></div>--%>
                    <%--<div class="checkout-step">--%>
                      <%--<ul id="steps"><li id="stepDesc0" class=""><fmt:message key="billing.and.shipping.address"/></li><li id="stepDesc1" class="current"><fmt:message key="review.order"/></li><li id="stepDesc2" class=""><fmt:message key="order.successfully"/></li></ul>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="clr"><!--  --></div>--%>
                <%--<div id="che-ord-rev-checkout-inner">--%>
                    <%--<h1><fmt:message key="review.recheck.order"/></h1>--%>

                    <%--<div class="message">--%>
                        <%--<h:frontendmessage _messages="${messages}"/>--%>
                    <%--</div>--%>

                    <%--<div id="che-ord-rev-top">--%>

                        <%--<div class="che-ord-rev-text"><fmt:message key="review.please.recheck.before.place.order"/></div>--%>

                        <%--<div class="clr"><!--  --></div>--%>

                        <%--<h:basketitems items="${items}" sessionObject="${sessionObject}" isBasketPage="false"/>--%>

                        <%--<div class="clr"><!--  --></div>--%>

                        <%--<div id="che-bas-promo-update-wrapper">--%>
                            <%--<c:set var="promoCode" value="${sessionObject.ORDER.PROMO_CODE}"/>--%>
                            <%--<c:if test="${!empty promoCode}">--%>
                                <%--<div class="promo-code-message">--%>
                                    <%--<fmt:message key="message.promocode.using"/>&nbsp;&nbsp;<b>${promoCode}</b>&nbsp;&nbsp;<a--%>
                                        <%--href="/checkout/removepromocode.html"><fmt:message key="review.remove"/></a>--%>
                                <%--</div>--%>
                            <%--</c:if>--%>
                            <%--<c:if test="${empty promoCode}">--%>
                            <%--<form id="promo_code" method="post" action="/checkout/applypromocode.html">--%>
                                <%--<div class="che-bas-promo-container">--%>
                                    <%--<div class="" id="che-bas-promo-code-block">--%>

                                        <%--<span class="glo-bold"><fmt:message key="review.promo.code"/> <a href="#" name="#dialog-promo-code"--%>
                                                                              <%--class="che-add-help-link"></a></span>--%>
                                        <%--<input type="text" maxlength="20" size="20" value="" name="promoCode"--%>
                                               <%--id="che-bas-promoCode"--%>
                                               <%--class="glo-form che-bas-promo-code-input">--%>
                                        <%--<button type="submit" name="btnG" class="submit-button"><fmt:message key="review.apply"/></button>--%>
                                        <%--&lt;%&ndash;<c:if test="${!empty messages}">&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<spring:eval expression="messages.getErrorMessage('promocode_invalid')" var="message"/>&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<br><br><span class="error">${message}</span>&ndash;%&gt;--%>
                                        <%--&lt;%&ndash;</c:if>&ndash;%&gt;--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<div class="clr"><!--  --></div>--%>
                            <%--</form>--%>
                            <%--</c:if>--%>
                        <%--</div>--%>

                        <%--<h:cmscontent name="customer-service"/>--%>

                        <%--<div id="che-ord-rev-order-information-wrapper">--%>
                            <%--<div id="che-ord-rev-order-information">--%>

                                <%--<div id="che-ord-rev-billto">--%>
                                    <%--<div class="che-ord-rev-order-info-header signinold"><fmt:message key="review.billing.information"/> (<a--%>
                                            <%--href="/checkout/billing_shipping.html"><fmt:message key="review.edit"/></a>)--%>
                                    <%--</div>--%>
                                    <%--<ul>--%>
                                        <%--<li>${billingAddress.LAST_NAME}&nbsp;${billingAddress.FIRST_NAME}</li>--%>
                                        <%--<li>${billingAddress.ADDRESS_LINE}</li>--%>
                                        <%--<li>${billingAddress.DISTRICT}</li>--%>
                                        <%--<c:set value='${billingAddress.CITY}' var="city"/>--%>
                                        <%--<spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValue('CITY', serviceLocator.locale, city)" var="billingCity"/>--%>
                                        <%--<li>${billingCity.value}</li>--%>
                                        <%--<li>${billingAddress.PHONE}</li>--%>
                                        <%--<li>${billingAddress.EMAIL}</li>--%>
                                    <%--</ul>--%>
                                <%--</div>--%>
                                <%--<div id="che-ord-rev-paymentinfo">--%>
                                    <%--<div class="che-ord-rev-order-info-header signinold"><fmt:message key="review.shipping.address"/> (<a--%>
                                            <%--href="/checkout/billing_shipping.html"><fmt:message key="review.edit"/></a>)--%>
                                    <%--</div>--%>
                                    <%--<ul>--%>
                                        <%--<li>${shippingAddress.LAST_NAME}&nbsp;${shippingAddress.FIRST_NAME}</li>--%>
                                        <%--<li>${shippingAddress.ADDRESS_LINE}</li>--%>
                                        <%--<li>${shippingAddress.DISTRICT}</li>--%>
                                        <%--<c:set value='${shippingAddress.CITY}' var="city"/>--%>
                                        <%--<spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValue('CITY', serviceLocator.locale, city)" var="shippingCity"/>--%>
                                        <%--<li>${shippingCity.value}</li>--%>
                                        <%--<li>${shippingAddress.PHONE}</li>--%>
                                    <%--</ul>--%>
                                    <%--<!--  end bottom payment -->--%>
                                <%--</div>--%>
                                <%--<div id="che-ord-rev-ordertotal">--%>
                                    <%--<div class="che-ord-rev-order-info-header"><fmt:message key="review.total.entire.order"/> (<a--%>
                                            <%--href="/checkout/basket.html"><fmt:message key="review.edit"/></a>)</div>--%>
                                    <%--<dl id="review-order-summary">--%>
                                        <%--<dt><fmt:message key="review.subtotal"/></dt>--%>
                                        <%--<spring:eval expression="sessionObject.getOrder().getSubPriceTotal()" var="subPrice"/>--%>
                                        <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalPrice"/>--%>
                                        <%--<dd><span class="js-merchandise">${subTotalPrice}</span></dd>--%>
                                        <%--<dd class="clr"><!--  --></dd>--%>
                                        <%--&lt;%&ndash;Order discount&ndash;%&gt;--%>
                                        <%--<spring:eval expression="sessionObject.getOrder().getSubPriceDiscountTotal()" var="subTotalDiscount"/>--%>
                                        <%--<c:if test="${!empty subTotalDiscount && subTotalDiscount > 0}">--%>
                                            <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subTotalDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalDiscount"/>--%>
                                            <%--<dt class="discount-row"><fmt:message key="subordertotal-discount"/></dt>--%>
                                            <%--<dd class="discount-row">-${subTotalDiscount}</dd>--%>
                                            <%--<dd class="clr"><!--  --></dd>--%>
                                        <%--</c:if>--%>
                                        <%--<dt id="giftBox1"><fmt:message key="review.gift.fee"/></dt>--%>
                                        <%--<dd id="giftBox2"><span class="js-giftBoxFee"></span></dd>--%>
                                        <%--<dd id="giftBox3" class="clr"><!--  --></dd>--%>
                                        <%--<dt><fmt:message key="review.shipping.fee"/></dt>--%>

                                        <%--<spring:eval expression="sessionObject.getShippingFee()" var="shippingPrice"/>--%>
                                        <%--<spring:eval expression="sessionObject.getOrder().getShippingDiscountPrice()" var="shippingPriceDiscount"/>--%>
                                        <%--&lt;%&ndash;Shipping price > 0, Check discount for shipping fee&ndash;%&gt;--%>
                                        <%--<c:if test="${!empty shippingPrice && shippingPrice > 0}">--%>
                                            <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>--%>
                                            <%--<dd><span class="js-orderShippingAmount">${shipping_Price}</span><br></dd>--%>
                                            <%--<c:if test="${shippingPriceDiscount > 0}">--%>
                                                <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPriceDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price_Discount"/>--%>
                                                <%--<dt class="discount-row"> <fmt:message key="review.discount.shipping.fee"/></dt>--%>
                                                <%--<dd class="discount-row">-${shipping_Price_Discount}</dd>--%>
                                            <%--</c:if>--%>
                                        <%--</c:if>--%>
                                        <%--&lt;%&ndash;Shipping price is 0, don't care discount for shipping fee&ndash;%&gt;--%>
                                        <%--<c:if test="${empty shippingPrice || shippingPrice <= 0}">--%>
                                            <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>--%>
                                            <%--<dd><span class="js-orderShippingAmount">${shipping_Price}</span></dd>--%>
                                        <%--</c:if>--%>

                                        <%--<spring:eval--%>
                                                <%--expression="T(com.easysoft.ecommerce.util.Money).valueOf(sessionObject.getOrder().getTaxPrice(),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"--%>
                                                <%--var="tax_Price"/>--%>
                                        <%--<dd class="clr"></dd>--%>
                                        <%--<dt id="salesTax1"><fmt:message key="review.tax"/></dt>--%>
                                        <%--<dd id="salesTax2"><span class="js-salesTax">${tax_Price}</span></dd>--%>
                                        <%--<dd id="salesTax3" class="clr"><!--  --></dd>--%>
                                    <%--</dl>--%>
                                    <%--<spring:eval expression="sessionObject.getOrder().getTotalPrice()" var="price"/>--%>
                                    <%--<spring:eval--%>
                                            <%--expression="T(com.easysoft.ecommerce.util.Money).valueOf(price, site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"--%>
                                            <%--var="totalPrice"/>--%>
                                    <%--<div id="che-sin-grand-total-wrapper">--%>
                                        <%--<div><fmt:message key="review.total.entire.order"/>:</div>--%>
                                        <%--<span class="js-orderGrandTotalAmount che-sin-grand-total">${totalPrice}</span>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<div class="clr"><!--  --></div>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<div class="clr"><!--  --></div>--%>

                    <%--</div>--%>

                    <%--<div class="clr"><!--  --></div>--%>
                    <%--<form:form id="play_order" action="/checkout/payment.html" commandName="command"--%>
                               <%--method="post">--%>
                        <%--<div style="width: 97%;" class="che-ord-rev-place-order-container">--%>
                                <%--&lt;%&ndash;Double click prevent http://pitchpublish.com/blog/?p=62&ndash;%&gt;--%>
                            <%--<button type="submit" name="btnG" class="submit-button"><fmt:message key="review.place.order"/></button>--%>
                        <%--</div>--%>
                    <%--</form:form>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>

    <%--</div>--%>
<%--</div>--%>

<%--Popup should put here--%>
<div id="dialog" title="Quà tặng" style="display: none;">
    <p><fmt:message key="dialog.help.gift"/></p>
</div>
<div id="dialog-promo-code" title="Mã giảm giá" style="display: none;">
    <p><fmt:message key="dialog.help.promocode"/></p>
</div>

</body>
</html>
