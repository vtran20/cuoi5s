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
//    $(function() {
//        jQuery('#play_order').preventDoubleSubmit();
//    });
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
        <h:basketserviceitems items="${items}" isBasketPage="false"/>
    </section>
    <div class="row invoice-info">
        <div class="col-sm-4">
            <div class="tag-box tag-box-v3">
                <h2><i class="fa fa-user"></i> <fmt:message key="review.billing.information"/> (<a
                        href="/site/checkout/basket.html"><fmt:message key="review.edit"/></a>)</h2>
                <ul class="list-unstyled">
                    <li>${billingAddress.FIRST_NAME}&nbsp;${billingAddress.LAST_NAME}</li>
                    <li>${billingAddress.ADDRESS_LINE}</li>
                    <li>${billingAddress.CITY}, ${billingAddress.STATE} ${billingAddress.ZIP_CODE}</li>
                    <li>${billingAddress.PHONE}</li>
                    <li>${billingAddress.EMAIL}</li>
                </ul>
            </div>
        </div>
        <%--<div class="col-sm-4">--%>
            <%--<div class="tag-box tag-box-v3">--%>
                <%--<h2><i class="fa fa-truck"></i> <fmt:message key="review.shipping.address"/> (<a--%>
                        <%--href="/site/checkout/basket.html"><fmt:message key="review.edit"/></a>)</h2>--%>
                <%--<ul class="list-unstyled">--%>
                    <%--<li>${shippingAddress.LAST_NAME}&nbsp;${shippingAddress.FIRST_NAME}</li>--%>
                    <%--<li>${shippingAddress.ADDRESS_LINE}</li>--%>
                    <%--<c:if test="${!empty shippingAddress.ADDRESS_LINE}">--%>
                        <%--<li>${shippingAddress.DISTRICT}</li>--%>
                    <%--</c:if>--%>
                    <%--<c:set value='${shippingAddress.CITY}' var="city"/>--%>
                    <%--<spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValue('CITY', serviceLocator.locale, city)" var="shippingCity"/>--%>
                    <%--<li>${shippingCity.value}</li>--%>
                    <%--<li>${shippingAddress.PHONE}</li>--%>
                <%--</ul>--%>
            <%--</div>--%>
        <%--</div>--%>
        <div class="col-sm-4">
            <%--<div class="tag-box tag-box-v3">--%>
                <%--<c:if test="${!empty sessionObject.ORDER.PAYMENT_METHOD_ID}">--%>
                    <%--<h2><i class="fa fa-money"></i> <fmt:message key="review.billing.method"/></h2>--%>
                    <%--<spring:eval expression="serviceLocator.getPaymentProviderSiteDao().findById(sessionObject.ORDER.PAYMENT_METHOD_ID, site.id)" var="paymentMethod"/>--%>
                    <%--<div><b>${paymentMethod.name}</b></div>--%>
                    <%--<c:if test="${!empty paymentMethod.description}">--%>
                        <%--<% pageContext.setAttribute("newLine", "\n"); %>--%>
                        <%--<c:set value="${fn:replace(paymentMethod.description, newLine, '<br>')}" var="paymentDescription"/>--%>
                        <%--<div>${paymentDescription}1</div>--%>
                    <%--</c:if>--%>
                <%--</c:if>--%>
                <%--<c:if test="${!empty sessionObject.ORDER.SHIPPING_METHOD_ID}">--%>
                    <%--<h2><i class="fa fa-truck"></i> <fmt:message key="review.shipping.method"/></h2>--%>
                    <%--<spring:eval expression="serviceLocator.getShippingSiteDao().findById(sessionObject.ORDER.SHIPPING_METHOD_ID, site.id)" var="shippingType"/>--%>
                    <%--<div>${shippingType.name}</div>--%>
                <%--</c:if>--%>
            <%--</div>--%>
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
                                href="/site/checkout/removepromocode.html"><fmt:message key="review.remove"/></a>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <form id="promo_code" method="post" action="/site/checkout/applypromocode.html">
                            <input type="hidden" name="dest" value="/site/checkout/review"/>
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
                        <spring:eval expression="sessionObject.getOrder().getOriginalSubTotal()" var="originalSubTotal"/>
                        <c:if test="${subPrice != originalSubTotal}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(originalSubTotal,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="originalSubTotalPrice"/>
                            <div class="total-result-in line-through">
                                <span>${originalSubTotalPrice}</span>
                            </div>
                        </c:if>
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
                        <spring:eval expression="sessionObject.getShippingFee()" var="shippingPrice"/>
                        <spring:eval expression="sessionObject.getOrder().getShippingDiscountPrice()" var="shippingPriceDiscount"/>
                        <%--Shipping price > 0, Check discount for shipping fee--%>
                        <c:if test="${!empty shippingPrice && shippingPrice > 0}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>
                            <h4><fmt:message key="shipping.fee"/>:</h4>
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
                        <%--<c:if test="${empty shippingPrice || shippingPrice <= 0}">--%>
                        <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>--%>
                        <%--<div class="total-result-in">--%>
                        <%--<span class="text-right">${shipping_Price}</span><br>--%>
                        <%--</div>--%>
                        <%--</c:if>--%>
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
                <form:form id="play_order" action="/site/checkout/payment.html" commandName="command" method="post">
                    <div style="width: 97%;" class="che-ord-rev-place-order-container">
                            <%--Double click prevent http://pitchpublish.com/blog/?p=62--%>
                        <%--<button type="submit" class="btn-u btn-u-sea-shop"><fmt:message key="review.place.order"/></button>--%>
                    </div>
                </form:form>

                <spring:eval expression="site.siteParamsMap.get('PAYPAL_CLIENT_ID')" var="clientId"/>
                <script src="https://www.paypal.com/sdk/js?client-id=${clientId}"></script>
                <div id="paypal-button-container"></div>
                <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).getMoneyValue()" var="moneyValue"/>
                <script>
                    paypal.Buttons({
                        createOrder: function(data, actions) {
                            // Set up the transaction
                            return actions.order.create({
                                purchase_units: [{
                                    amount: {
                                        value: '${moneyValue}'
                                    }
                                }]
                            });
                        },
                        onApprove: function(data, actions) {
                            // Capture the funds from the transaction
                            return actions.order.capture().then(function(details) {
                                // Show a success message to your buyer
                                console.log (details)
                                console.log (data)
                                // Call your server to save the transaction
                                return fetch('/site/checkout/payment.html', {
                                    method: 'post',
                                    headers: {
                                        'content-type': 'application/json'
                                    },
                                    body: JSON.stringify({
                                        orderID: data.orderID
                                    })
                                }).then(function(res) {
                                    console.log(res)
                                    document.location.href = res.url
                                });
                            });
                        }
                    }).render('#paypal-button-container');
                </script>
            </div>
        </div>
    </div>

</div>

<%--Popup should put here--%>
<div id="dialog" title="Quà tặng" style="display: none;">
    <p><fmt:message key="dialog.help.gift"/></p>
</div>
<div id="dialog-promo-code" title="Mã giảm giá" style="display: none;">
    <p><fmt:message key="dialog.help.promocode"/></p>
</div>

</body>
</html>
