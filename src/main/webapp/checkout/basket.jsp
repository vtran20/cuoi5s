<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="basket.page"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>

</head>
<body>
<script src="/themes/m3x/plugins/jquery-steps/build/jquery.steps.js" type="text/javascript"></script>

<!--=== Breadcrumbs v4 ===-->
<div class="breadcrumbs-v4">
    <div class="container">
        <h1><fmt:message key="basket.shopping.bag"/></h1>
        <ul class="breadcrumb-v4-in">
            <li><a href="index.html">Home</a></li>
            <li><a href="/"><fmt:message key="basket.continue.shopping"/></a></li>
            <li class="active"><fmt:message key="basket.shopping.bag"/></li>
        </ul>
    </div><!--/end container-->
</div>
<!--=== End Breadcrumbs v4 ===-->

<c:set var="items" value="${sessionObject.ORDER.ITEMS}"/>
<c:set var="order" value="${sessionObject.ORDER}"/>
<!--=== Content Medium Part ===-->
<div class="content-md margin-bottom-30">
<div class="container">
<c:choose>
<c:when test="${!empty items}">
<h:frontendmessage _messages="${messages}"/>
<form class="shopping-cart" name="checkout_form" action="/checkout/review.html" method="POST">
<div>
<div class="header-tags">
    <div class="overflow-h">
        <h2><fmt:message key="billing.and.shipping.address"/></h2>
            <%--<p>Review &amp; edit your product</p>--%>
        <i class="rounded-x fa fa-check"></i>
    </div>
</div>
<section>
    <h:basketitems items="${items}" sessionObject="${sessionObject}" isBasketPage="true"/>
</section>
<div class="header-tags">
    <div class="overflow-h">
        <h2><fmt:message key="billing.info.header"/></h2>
            <%--<p>Shipping and address infot</p>--%>
        <i class="rounded-x fa fa-home"></i>
    </div>
</div>
<c:set var="billingAddress" value="${sessionObject.ADDRESSES.BILLING_ADDRESS}"/>
<c:set var="shippingAddress" value="${sessionObject.ADDRESSES.SHIPPING_ADDRESS}"/>
<c:set var="order" value="${sessionObject.ORDER}"/>
<section class="billing-info">
    <div class="row">
        <div class="col-md-6 md-margin-bottom-40">
            <h2 class="title-type"><fmt:message key="billing.info.header"/></h2>
            <div class="billing-info-inputs checkbox-list">

                <div class="row">
                    <div class="col-sm-6">
                        <input id="firstNameForBilling" type="text" placeholder="<fmt:message key="billing.shipping.firstname"/>" name="firstNameForBilling" class="form-control required" value="${billingAddress.FIRST_NAME}" tabindex="10" maxlength="50"/>
                        <input id="emailAddressForBilling" type="text" placeholder="<fmt:message key="billing.shipping.email"/>" name="emailAddressForBilling" class="form-control email" value="${billingAddress.EMAIL}" tabindex="12" maxlength="100">
                    </div>
                    <div class="col-sm-6">
                        <input id="lastNameForBilling" type="text" placeholder="<fmt:message key="billing.shipping.lastname"/>" name="lastNameForBilling" class="form-control required" value="${billingAddress.LAST_NAME}" tabindex="11" maxlength="50">
                        <input id="phoneForBilling" type="tel" placeholder="<fmt:message key="billing.shipping.phone"/>" name="phoneForBilling" class="form-control required" value="${billingAddress.PHONE}" tabindex="13" maxlength="20">
                    </div>
                </div>
                <input id="address1ForBilling" type="text" placeholder="<fmt:message key="billing.shipping.address"/>" name="address1ForBilling" class="form-control required" value="${billingAddress.ADDRESS_LINE}" tabindex="14">
                <h:stringparamselector name="cityForBilling" stringParam="CITY" defaultValue="${billingAddress.CITY}" includeTitle="Chọn Tỉnh/Thành Phố" styleClass="form-control required" tabindex="15"/>
                    <%--<div class="row">--%>
                    <%--<div class="col-sm-6">--%>
                    <%--&lt;%&ndash;<input id="city" type="text" placeholder="City" name="city" class="form-control required">&ndash;%&gt;--%>
                    <%--</div>--%>
                    <%--<div class="col-sm-6">--%>
                    <%--<input id="zip" type="text" placeholder="Zip/Postal Code" name="zip" class="form-control required">--%>
                    <%--</div>--%>
                    <%--</div>--%>

                <label class="checkbox text-left">
                    <input type="checkbox" name="shipToBillingAddress" tabindex="16" ${(empty order.SHIPPING_TO_BILLING || order.SHIPPING_TO_BILLING == true)?'checked':''} onclick="clickOnCheckBox(this);"/>
                    <i></i>
                    <fmt:message key="billing.shipping.shippingisbilling"/>
                </label>
            </div>
        </div>

        <div class="col-md-6">
            <h2 class="title-type" id="shipping-element-header"><fmt:message key="shipping.info.header"/></h2>
            <div class="billing-info-inputs checkbox-list" id="shipping-element-detail">
                <div class="row">
                    <div class="col-sm-6">
                        <input id="firstNameForShipping" type="text" placeholder="<fmt:message key="billing.shipping.firstname"/>" name="firstNameForShipping" class="form-control required" value="${shippingAddress.FIRST_NAME}" tabindex="17">
                        <input id="emailAddressForShipping" type="text" placeholder="<fmt:message key="billing.shipping.email"/>" name="emailAddressForShipping" class="form-control email" value="${shippingAddress.EMAIL}" tabindex="19">
                    </div>
                    <div class="col-sm-6">
                        <input id="lastNameForShipping" type="text" placeholder="<fmt:message key="billing.shipping.lastname"/>" name="lastNameForShipping" class="form-control required" value="${shippingAddress.LAST_NAME}" tabindex="18">
                        <input id="phoneForShipping" type="tel" placeholder="<fmt:message key="billing.shipping.phone"/>" name="phoneForShipping" class="form-control required" value="${shippingAddress.PHONE}" tabindex="20">
                    </div>
                </div>
                <input id="address1ForShipping" type="text" placeholder="<fmt:message key="billing.shipping.address"/>" name="address1ForShipping" class="form-control required" value="${shippingAddress.ADDRESS_LINE}" tabindex="21">
                <h:stringparamselector name="cityForShipping" stringParam="CITY" defaultValue="${shippingAddress.CITY}" includeTitle="Chọn Tỉnh/Thành Phố" styleClass="form-control required"  tabindex="22"/>

            </div>
        </div>
    </div>
</section>

<div class="header-tags">
    <div class="overflow-h">
        <h2><fmt:message key="paymentmethod.info.section"/></h2>
            <%--<p>Select Payment method</p>--%>
        <i class="rounded-x fa fa-credit-card"></i>
    </div>
</div>
<section>
    <div class="row">
        <spring:eval expression="serviceLocator.getPaymentProviderSiteDao().findAll(site.id)"
                     var="paymentProviders"/>
        <div class="col-md-6 md-margin-bottom-50">
            <c:if test="${!empty paymentProviders}">
            <h2 class="title-type"><fmt:message key="paymentmethod.info.header"/></h2>
            <!-- Accordion -->
            <div class="accordion-v2">
                <div class="panel-group" id="accordion">
                    <c:forEach varStatus="paymentProvider" items="${paymentProviders}">
                        <c:if test="${empty order.PAYMENT_METHOD_ID}">
                            <c:if test="${paymentProvider.first}">
                                <c:set var="checked" value="checked"/>
                            </c:if>
                            <c:if test="${!paymentProvider.first}">
                                <c:set var="checked" value=""/>
                            </c:if>
                        </c:if>
                        <c:if test="${!empty order.PAYMENT_METHOD_ID}">
                            <c:if test="${order.PAYMENT_METHOD_ID == paymentProvider.current.id}">
                                <c:set var="checked" value="checked"/>
                            </c:if>
                            <c:if test="${order.PAYMENT_METHOD_ID != paymentProvider.current.id}">
                                <c:set var="checked" value=""/>
                            </c:if>
                        </c:if>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h5 class="panel-title">
                                        <%--<a data-toggle="collapse" data-parent="#accordion" href="#collapse${paymentProvider.current.id}">--%>
                                    <input id="payment_id_${paymentProvider.current.id}" class="che-payment-method required" name="paymentProviderId" type="radio"
                                           value="${paymentProvider.current.id}" ${checked}>
                                    <label for="payment_id_${paymentProvider.current.id}">${paymentProvider.current.name}</label>
                                        <%--</a>--%>
                                </h5>
                            </div>
                            <c:if test="${!empty paymentProvider.current.description}">
                                <div id="collapseOne" class="panel-collapse collapse in">
                                    <div class="panel-body cus-form-horizontal">
                                        <div class="form-group">
                                            <% pageContext.setAttribute("newLine", "\n"); %>
                                            <c:set value="${fn:replace(paymentProvider.current.description, newLine, '<br>')}" var="paymentDescription"/>
                                            <label class="col-xs-12 no-col-space control-label">${paymentDescription}</label>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
            </c:if>
            <!-- End Accordion -->
        </div>

        <spring:eval expression="serviceLocator.getShippingSiteDao().findAll(site.id)" var="shippingTypes"/>
        <div class="col-md-6">
            <c:if test="${!empty shippingTypes}">
            <h2 class="title-type"><fmt:message key="shippingmethod.info.header"/></h2>
            <!-- Accordion -->
            <div class="accordion-v2">
                <div class="panel-group" id="accordion-v2">
                    <c:forEach varStatus="shippingType" items="${shippingTypes}">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h5 class="panel-title">
                                    <input class="required" id="shipping-method" name="shippingCode" type="radio" value="${shippingType.current.id}" checked>
                                    <label for="shipping-method">${shippingType.current.name}</label>
                                </h5>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            </c:if>
            <!-- End Accordion -->
        </div>
    </div>
</section>

<div class="coupon-code">
    <div class="row">
        <div class="col-sm-4 sm-margin-bottom-30">
                <%--<h3>Discount Code</h3>--%>
                <%--<p>Enter your coupon code</p>--%>
                <%--<input class="form-control margin-bottom-10" name="code" type="text">--%>
                <%--<button type="button" class="btn-u btn-u-sea-shop">Apply Coupon</button>--%>
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
        </div>
    </div>
</div>
</div>
</form>
</c:when>
<c:otherwise>
    <p><fmt:message key="basket.no.items.in.your.basket"/></p>
    <span><a href="/" tabindex="-1"><fmt:message key="basket.continue.shopping"/></a></span>
</c:otherwise>

</c:choose>

</div><!--/end container-->
</div>
<!--=== End Content Medium Part ===-->








<%--For data showing on this page, we have to use form Session Object. For the logic (if, while), we can use from database--%>
<script type="text/javascript">
    function clickOnCheckBox(checkbox) {
        if (checkbox.checked) {
            $('#shipping-element-header').hide();
            $('#shipping-element-detail').hide();
        } else {
            $('#shipping-element-header').show();
            $('#shipping-element-detail').show();
        }
    }
    function activeGiftBox(thisCheckBox, giftbox) {
        var flag = thisCheckBox.checked;
        $.get(
                "/checkout/"+giftbox+"/checkgiftbox.html", // your page to get
                { flag: flag}, // some params to pass it
                function(data) {
//                        window.opener.window.location.reload(true);
                });
    }
    $( document ).ready(function() {
        //Hide or Show shipping address
        var theSameAddress = '${(empty order.SHIPPING_TO_BILLING || order.SHIPPING_TO_BILLING == true)?'true':''}';
        if (theSameAddress == 'true') {
            $('#shipping-element-header').hide();
            $('#shipping-element-detail').hide();
        }

            //Checkout wizard
        var form = $(".shopping-cart");
        form.validate({
            errorPlacement: function errorPlacement(error, element) { element.before(error); },
            rules: {
                confirm: {
                    equalTo: "#password"
                }
            }
        });
        form.children("div").steps({
            headerTag: ".header-tags",
            bodyTag: "section",
            transitionEffect: "fade",
            onStepChanging: function (event, currentIndex, newIndex) {
                // Allways allow previous action even if the current form is not valid!
                if (currentIndex > newIndex)
                {
                    return true;
                }
                form.validate().settings.ignore = ":disabled,:hidden";
                return form.valid();
            },
            onFinishing: function (event, currentIndex) {
                form.validate().settings.ignore = ":disabled,:hidden";
                return form.valid();
            },
            onFinished: function (event, currentIndex) {
                document.checkout_form.submit();
            },
            labels: {
//                cancel: "Cancel",
//                current: "current step:",
//                pagination: "none",
                finish: "Kiểm Tra Đơn Hàng",
                next: "Tiếp Tục",
                previous: "Về Trước",
                loading: "Loading ..."
            }
        });
    });
</script>
</body>
</html>
