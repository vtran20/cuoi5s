url<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:if test="${! empty order}">
    <spring:eval expression="serviceLocator.getOrderService().getOrderSession(order.id, null)" var="orderSession"/>
    <spring:eval expression="orderSession.getOrder().getItems()" var="items"/>
</c:if>
<html>
<head>
    <title><fmt:message key="receipt.order.confirm.success"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
    <spring:eval expression="site.siteParamsMap.get('GOOGLE_ANALYSIS_ACCOUNT')" var="googleAnalysisAccount"/>
    <c:if test="${!empty googleAnalysisAccount && !empty order}">
        <script type="text/javascript">

            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', '${googleAnalysisAccount}']);
            _gaq.push(['_trackPageview']);
            _gaq.push(['_addTrans',
                '${order.id}',           // order ID - required
                '',  // affiliation or store name
                '${orderSession.ORDER.TOTAL_PRICE}',          // total - required
                '${orderSession.ORDER.TAX_PRICE}',           // tax
                '${orderSession.ORDER.SHIPPING_PRICE}',              // shipping
                '${orderSession.ADDRESSES.SHIPPING_ADDRESS.CITY}',       // city
                '${orderSession.ADDRESSES.SHIPPING_ADDRESS.STATE}',     // state or province
                '${orderSession.ADDRESSES.SHIPPING_ADDRESS.COUNTRY}'             // country
            ]);

            // add item might be called for every item in the shopping cart
            // where your ecommerce engine loops through each item in the cart and
            // prints out _addItem for each
            <c:forEach var="item" items="${items}">
            _gaq.push(['_addItem',
                '${site.siteCode}${order.id}',           // order ID - required
                '${item.MODEL_NUMBER}',           // SKU/code - required
                '${item.NAME}',        // product name
                '',   // category or variation
                '${item.PRICE}',          // unit price - required
                '${item.QUANTITY}'               // quantity - required
            ]);
            </c:forEach>

            _gaq.push(['_trackTrans']); //submits transaction to the Analytics servers

            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
            })();

        </script>
    </c:if>
    <h:head/>
</head>

<body>
<c:if test="${!empty order}">
    <c:set var="orderId" value="${order.id}"/>
    <c:set var="items" value="${orderSession.ORDER.ITEMS}"/>
    <c:set var="order" value="${orderSession.ORDER}"/>
    <c:set var="billingAddress" value="${orderSession.ADDRESSES.BILLING_ADDRESS}"/>
    <c:set var="shippingAddress" value="${orderSession.ADDRESSES.SHIPPING_ADDRESS}"/>
    <!--=== Breadcrumbs v4 ===-->
    <div class="breadcrumbs-v4 margin-bottom-20">
        <div class="container">
            <h1><fmt:message key="receipt.order.confirm.success.detail"/></h1>
            <ul class="breadcrumb-v4-in">
                <li><a href="index.html">Home</a></li>
                <li><a href="/"><fmt:message key="basket.continue.shopping"/></a></li>
                <li class="active"><fmt:message key="receipt.order.confirm.success.detail"/></li>
            </ul>
        </div><!--/end container-->
    </div>
    <!--=== End Breadcrumbs v4 ===-->
    <div class="container shopping-cart margin-bottom-30">
        <h:frontendmessage _messages="${messages}"/>
        <div class="row margin-bottom-5">
            <div class="col-sm-12 result-category">
                <h3>Confirm Order Number: <b>${orderId}</b></h3>
            </div>
        </div>
        <hr>
        <section>
            <h:basketserviceitems items="${items}" isBasketPage="false"/>
        </section>
        <div class="row invoice-info">
            <div class="col-sm-4">
                <div class="tag-box tag-box-v3">
                    <h2><i class="fa fa-user"></i> <fmt:message key="review.billing.information"/></h2>
                    <ul class="list-unstyled">
                        <li>${billingAddress.FIRST_NAME}&nbsp;${billingAddress.LAST_NAME}</li>
                        <li>${billingAddress.ADDRESS_LINE}</li>
                        <li>${billingAddress.CITY}, ${billingAddress.STATE} ${billingAddress.ZIP_CODE}</li>
                        <li>${billingAddress.PHONE}</li>
                        <li>${billingAddress.EMAIL}</li>
                    </ul>
                </div>
            </div>
            <div class="col-sm-4">
                <%--<div class="tag-box tag-box-v3">--%>
                    <%--<c:if test="${!empty orderSession.ORDER.PAYMENT_METHOD_ID}">--%>
                        <%--<h2><i class="fa fa-money"></i> <fmt:message key="review.billing.method"/></h2>--%>
                        <%--<spring:eval expression="serviceLocator.getPaymentProviderSiteDao().findById(orderSession.ORDER.PAYMENT_METHOD_ID, site.id)" var="paymentMethod"/>--%>
                        <%--<div>${paymentMethod.name}</div>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${!empty orderSession.ORDER.SHIPPING_METHOD_ID}">--%>
                        <%--<h2><i class="fa fa-truck"></i> <fmt:message key="review.shipping.method"/></h2>--%>
                        <%--<spring:eval expression="serviceLocator.getShippingSiteDao().findById(orderSession.ORDER.SHIPPING_METHOD_ID, site.id)" var="shippingType"/>--%>
                        <%--<div>${shippingType.name}</div>--%>
                    <%--</c:if>--%>
                <%--</div>--%>
            </div>

        </div>

        <div class="coupon-code margin-bottom-40">
            <div class="row">
                <div class="col-sm-4 sm-margin-bottom-30">
                </div>
                <div class="col-sm-4 col-sm-offset-4">
                    <ul class="list-inline total-result">
                        <li>
                            <h4><fmt:message key="basket.subtotal"/>:</h4>
                            <spring:eval expression="orderSession.getOrder().getSubPriceTotal()" var="subPrice"/>
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
                        <spring:eval expression="orderSession.getOrder().getSubPriceDiscountTotal()" var="subTotalDiscount"/>
                        <c:if test="${!empty subTotalDiscount && subTotalDiscount > 0}">
                            <li>
                                <h4><fmt:message key="subordertotal-discount"/>:</h4>
                                <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subTotalDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalDiscount"/>
                                <div class="total-result-in">
                                    <span class="text-right">-${subTotalDiscount}</span>
                                </div>
                            </li>
                        </c:if>
                        <%--<li>--%>
                            <%--<h4><fmt:message key="shipping.fee"/>:</h4>--%>
                            <%--<spring:eval expression="orderSession.getShippingFee()" var="shippingPrice"/>--%>
                            <%--<spring:eval expression="orderSession.getOrder().getShippingDiscountPrice()" var="shippingPriceDiscount"/>--%>
                                <%--&lt;%&ndash;Shipping price > 0, Check discount for shipping fee&ndash;%&gt;--%>
                            <%--<c:if test="${!empty shippingPrice && shippingPrice > 0}">--%>
                                <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>--%>
                                <%--<div class="total-result-in">--%>
                                    <%--<span class="text-right">${shipping_Price}</span>--%>
                                <%--</div>--%>
                                <%--<c:if test="${shippingPriceDiscount > 0}">--%>
                                    <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPriceDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price_Discount"/>--%>
                                    <%--<h4><fmt:message key="basket.discount.shipping.fee"/>:</h4>--%>
                                    <%--<div class="total-result-in">--%>
                                        <%--<span class="text-right">-${shipping_Price_Discount}</span><br>--%>
                                    <%--</div>--%>
                                <%--</c:if>--%>
                            <%--</c:if>--%>
                                <%--&lt;%&ndash;Shipping price is 0, don't care discount for shipping fee&ndash;%&gt;--%>
                            <%--<c:if test="${empty shippingPrice || shippingPrice <= 0}">--%>
                                <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>--%>
                                <%--<div class="total-result-in">--%>
                                    <%--<span class="text-right">${shipping_Price}</span><br>--%>
                                <%--</div>--%>
                            <%--</c:if>--%>
                        <%--</li>--%>
                        <li>
                            <h4><fmt:message key="basket.tax"/>:</h4>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(orderSession.getOrder().getTaxPrice(),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="tax_Price"/>
                            <div class="total-result-in">
                                <span>${tax_Price}</span>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li class="total-price">
                            <h4><fmt:message key="basket.ordertotal"/>:</h4>
                            <spring:eval expression="orderSession.getOrder().getTotalPrice()" var="price"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="totalPrice"/>
                            <div class="total-result-in">
                                <span>${totalPrice}</span>
                            </div>
                        </li>
                    </ul>
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
</c:if>


</body>
</html>