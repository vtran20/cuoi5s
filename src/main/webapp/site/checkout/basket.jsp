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
<form class="shopping-cart" name="checkout_form" action="/site/checkout/review.html" method="POST">
<div>
<div class="header-tags">
    <div class="overflow-h">
        <h2><fmt:message key="billing.and.shipping.address"/></h2>
            <%--<p>Review &amp; edit your product</p>--%>
        <i class="rounded-x fa fa-check"></i>
    </div>
</div>
<section>
    <h:basketserviceitems items="${items}" isBasketPage="true"/>
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
                        <input id="firstNameForBilling" type="text" placeholder="<fmt:message key="billing.shipping.firstname"/>" name="firstNameForBilling" class="form-control required" value="${billingAddress.FIRST_NAME}" tabindex="10">
                        <input id="emailAddressForBilling" type="text" placeholder="<fmt:message key="billing.shipping.email"/>" name="emailAddressForBilling" class="form-control required email" value="${billingAddress.EMAIL}" tabindex="12">
                    </div>
                    <div class="col-sm-6">
                        <input id="lastNameForBilling" type="text" placeholder="<fmt:message key="billing.shipping.lastname"/>" name="lastNameForBilling" class="form-control required" value="${billingAddress.LAST_NAME}" tabindex="11">
                        <input id="phoneForBilling" type="tel" placeholder="<fmt:message key="billing.shipping.phone"/>" name="phoneForBilling" class="form-control required" value="${billingAddress.PHONE}" tabindex="13">
                    </div>
                    <div class="col-sm-12">
                        <input id="address1ForBilling" type="text" placeholder="<fmt:message key="billing.shipping.address"/>" name="address1ForBilling" class="form-control required" value="${billingAddress.ADDRESS_LINE}" tabindex="14">
                    </div>
                    <div class="col-sm-4">
                        <input id="cityForBilling" type="text" placeholder="<fmt:message key="billing.shipping.city"/>" name="cityForBilling" class="form-control required" value="${billingAddress.CITY}" tabindex="15" maxlength="50"/>
                    </div>
                    <div class="col-sm-4">
                        <h:stringparamselector name="stateForBilling" stringParam="USA_STATE" lang="en_US" defaultValue="${billingAddress.STATE}" includeTitle="Select State" styleClass="form-control required" tabindex="16"/>
                    </div>
                    <div class="col-sm-4">
                        <input id="zipCodeForBilling" type="text" placeholder="<fmt:message key="billing.shipping.zipCode"/>" name="zipCodeForBilling" class="form-control required" value="${billingAddress.ZIP_CODE}" tabindex="17" maxlength="50"/>
                    </div>
                </div>
                    <%--<div class="row">--%>
                    <%--<div class="col-sm-6">--%>
                    <%--&lt;%&ndash;<input id="city" type="text" placeholder="City" name="city" class="form-control required">&ndash;%&gt;--%>
                    <%--</div>--%>
                    <%--<div class="col-sm-6">--%>
                    <%--<input id="zip" type="text" placeholder="Zip/Postal Code" name="zip" class="form-control required">--%>
                    <%--</div>--%>
                    <%--</div>--%>

                <label class="checkbox text-left" style="display: none">
                    <input type="checkbox" name="shipToBillingAddress" tabindex="16" ${(empty order.SHIPPING_TO_BILLING || order.SHIPPING_TO_BILLING == true)?'checked':''} onclick="clickOnCheckBox(this);"/>
                    <i></i>
                    <fmt:message key="billing.shipping.shippingisbilling"/>
                </label>
            </div>
        </div>

    </div>
</section>

<%--<div class="header-tags">--%>
    <%--<div class="overflow-h">--%>
        <%--<h2><fmt:message key="paymentmethod.info.section"/></h2>--%>
            <%--&lt;%&ndash;<p>Select Payment method</p>&ndash;%&gt;--%>
        <%--<i class="rounded-x fa fa-credit-card"></i>--%>
    <%--</div>--%>
<%--</div>--%>
<%--<section>--%>
    <%--<div class="row">--%>
        <%--<div class="col-md-6 md-margin-bottom-50">--%>
            <%--<spring:eval expression="serviceLocator.getPaymentProviderSiteDao().findAll(site.id)"--%>
                         <%--var="paymentProviders"/>--%>
            <%--<c:if test="${!empty paymentProviders}">--%>
            <%--<h2 class="title-type"><fmt:message key="paymentmethod.info.header"/></h2>--%>
            <%--<!-- Accordion -->--%>
            <%--<div class="accordion-v2">--%>
                <%--<div class="panel-group" id="accordion">--%>
                    <%--<c:forEach varStatus="paymentProvider" items="${paymentProviders}">--%>
                        <%--<c:if test="${empty order.PAYMENT_METHOD_ID}">--%>
                            <%--<c:if test="${paymentProvider.first}">--%>
                                <%--<c:set var="checked" value="checked"/>--%>
                            <%--</c:if>--%>
                            <%--<c:if test="${!paymentProvider.first}">--%>
                                <%--<c:set var="checked" value=""/>--%>
                            <%--</c:if>--%>
                        <%--</c:if>--%>
                        <%--<c:if test="${!empty order.PAYMENT_METHOD_ID}">--%>
                            <%--<c:if test="${order.PAYMENT_METHOD_ID == paymentProvider.current.id}">--%>
                                <%--<c:set var="checked" value="checked"/>--%>
                            <%--</c:if>--%>
                            <%--<c:if test="${order.PAYMENT_METHOD_ID != paymentProvider.current.id}">--%>
                                <%--<c:set var="checked" value=""/>--%>
                            <%--</c:if>--%>
                        <%--</c:if>--%>
                        <%--<div class="panel panel-default">--%>
                            <%--<div class="panel-heading">--%>
                                <%--<h5 class="panel-title">--%>
                                        <%--&lt;%&ndash;<a data-toggle="collapse" data-parent="#accordion" href="#collapse${paymentProvider.current.id}">&ndash;%&gt;--%>
                                    <%--<input id="payment_id_${paymentProvider.current.id}" class="che-payment-method required" name="paymentProviderId" type="radio"--%>
                                           <%--value="${paymentProvider.current.id}" ${checked}>--%>
                                    <%--<label for="payment_id_${paymentProvider.current.id}">${paymentProvider.current.name}--%>
                                        <%--<c:if test="${paymentProvider.current.merchantId == 'MY_ACCOUNT'}">--%>
                                            <%--<spring:eval expression="T(com.easysoft.ecommerce.controller.SessionUtil).loadUser(pageContext.request, pageContext.response)" var="user"/>--%>
                                            <%--<c:set var="balenceMoney" value="${user.balance}"/>--%>
                                            <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(balenceMoney,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toStringKeepZero()" var="balance"/>--%>
                                            <%--(${balance})--%>
                                            <%--<span class="hide" id="balenceMoney">${balenceMoney}</span>--%>
                                        <%--</c:if>--%>
                                    <%--</label>--%>
                                        <%--&lt;%&ndash;</a>&ndash;%&gt;--%>
                                <%--</h5>--%>
                            <%--</div>--%>
                            <%--<c:if test="${!empty paymentProvider.current.description}">--%>
                                <%--<div id="collapseOne" class="panel-collapse collapse in">--%>
                                    <%--<div class="panel-body cus-form-horizontal">--%>
                                        <%--<div class="form-group">--%>
                                            <%--<% pageContext.setAttribute("newLine", "\n"); %>--%>
                                            <%--<c:set value="${fn:replace(paymentProvider.current.description, newLine, '<br>')}" var="paymentDescription"/>--%>
                                            <%--<label class="col-xs-12 no-col-space control-label">${paymentDescription}</label>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</c:if>--%>
                        <%--</div>--%>
                    <%--</c:forEach>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--</c:if>--%>
            <%--<!-- End Accordion -->--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</section>--%>

<div class="coupon-code">
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
                    <h3><fmt:message key="review.promo.code"/></h3>
                    <h:frontendmessage _messages="${promo_messages}"/>
                    <input type="text" maxlength="20" size="20" value="" name="pc" id="che-bas-promoCode" class="form-control margin-bottom-10" placeholder="<fmt:message key="please.enter.promo.code"/>">
                    <%--We cannot use form here because it had other form outside--%>
                    <a class="btn-u btn-u-sea-shop" onclick="applyDiscount()"><fmt:message key="review.apply"/></a>
                </c:otherwise>
            </c:choose>

        <%--<h3><fmt:message key="discount.code"/></h3>--%>
            <%--<h:frontendmessage _messages="${promo_messages}"/>--%>
            <%--<input class="form-control margin-bottom-10" name="pc" type="text">--%>
            <%--<button type="button" id="apply_discount" onclick="applyDiscount()" class="btn-u btn-u-sea-shop"><fmt:message key="discount.apply"/></button>--%>
        </div>
        <div class="col-sm-4 col-sm-offset-4">
            <h:totalorder sessionObject="${sessionObject}"/>
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
<h:form_modal/>

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

    function applyDiscount() {
        if ($("input[name=pc]").val() != "") {
            location.href="http://webphattai.com/site/checkout/applypromocode.html?promoCode="+$('input[name=pc]').val() +"&dest=/site/checkout/basket";
        }
//        return false;
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
