<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="billing.shipping.info"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="no_leftnav"/>
</head>

<body>
<script type="text/javascript">
    $(function() {
        $("input[id=ship-different]").click(function() {
            $("#widget-shipping-address").show();
            $("input[name=firstNameForShipping]").addClass('required');
            $("input[name=lastNameForShipping]").addClass('required');
            $("input[name=address1ForShipping]").addClass('required');
            $("select[name=cityForShipping]").addClass('required');
            $("input[name=phoneForShipping]").addClass('required');
        });
        $("input[id=ship-billing]").click(function() {
            $("#widget-shipping-address").hide();
            $("input[name=firstNameForShipping]").removeClass('required');
            $("input[name=lastNameForShipping]").removeClass('required');
            $("input[name=address1ForShipping]").removeClass('required');
            $("select[name=cityForShipping]").removeClass('required');
            $("input[name=phoneForShipping]").removeClass('required');
        });

        $("input[id=ship-different]").each(function() {
            if ($(this).is(":checked")) {
                $("#widget-shipping-address").show();
                $("input[name=firstNameForShipping]").addClass('required');
                $("input[name=lastNameForShipping]").addClass('required');
                $("input[name=address1ForShipping]").addClass('required');
                $("select[name=cityForShipping]").addClass('required');
                $("input[name=phoneForShipping]").addClass('required');
            } else {
                $("#widget-shipping-address").hide();
                $("input[name=firstNameForShipping]").removeClass('required');
                $("input[name=lastNameForShipping]").removeClass('required');
                $("input[name=address1ForShipping]").removeClass('required');
                $("select[name=cityForShipping]").removeClass('required');
                $("input[name=phoneForShipping]").removeClass('required');
            }
        });

        $(function() {

            $("#paymentForm").validate({
                messages: {
                    firstNameForBilling:  "<fmt:message key="billing.shipping.please.enter.firstname"/>",
                    lastNameForBilling:  "<fmt:message key="billing.shipping.please.enter.lastname"/>",
                    address1ForBilling:  "<fmt:message key="billing.shipping.please.enter.address"/>",
                    cityForBilling:  "<fmt:message key="billing.shipping.please.enter.city"/>",
                    phoneForBilling:  "<fmt:message key="billing.shipping.please.enter.phonenumber"/>",
                    emailAddressForBilling:  {
                        required: "<fmt:message key="billing.shipping.please.enter.email"/>",
                        email: "<fmt:message key="billing.shipping.email.incorrect"/>"
                    },
                    firstNameForShipping:  "<fmt:message key="billing.shipping.please.enter.firstname"/>",
                    lastNameForShipping:  "<fmt:message key="billing.shipping.please.enter.lastname"/>",
                    address1ForShipping:  "<fmt:message key="billing.shipping.please.enter.address"/>",
                    cityForShipping:  "<fmt:message key="billing.shipping.please.enter.city"/>",
                    phoneForShipping:  "<fmt:message key="billing.shipping.please.enter.phonenumber"/>"
                }

            });

        });

    });
</script>

<c:set var="billingAddress" value="${sessionObject.ADDRESSES.BILLING_ADDRESS}"/>
<c:set var="shippingAddress" value="${sessionObject.ADDRESSES.SHIPPING_ADDRESS}"/>
<c:set var="order" value="${sessionObject.ORDER}"/>
<div class='page-body body-with-border page-body-float-left'>

<div id="glo-body-border">
<div id="glo-body-container">
<div id="glo-body-container-inner">
<div id="glo-body-content">
<!-- Make sure all of the information for every item in the basket has been brought up to date -->
<!-- Payment Info BEGINS -->
<!-- Page Starts -->

<div id="che-sin-checkout">
<a class="che-app-anchor" name="che-app-anchor"></a>

<div id="che-sin-checkout-content">
<div class="billing-global-header"><h1><fmt:message key="checkout.lable.header"/></h1></div>
<div class="checkout-step">
  <ul id="steps"><li id="stepDesc0" class="current"><fmt:message key="billing.and.shipping.address"/></li><li id="stepDesc1" class=""><fmt:message key="review.order"/></li><li id="stepDesc2" class=""><fmt:message key="order.successfully"/></li></ul>
</div>
<%--<img id="che-sin-breadcrumb" src="/themes/default/images/common/label/che-sin-checkout-step-one.gif"--%>
     <%--alt="Billing and Shipping">--%>

<div class="clr"><!--  --></div>

<div id="che-sin-column-left">
<div id="che-sin-left-header"><h2><fmt:message key="billing.shipping.header"/></h2></div>
<div id="che-sin-left-content">

<form id="paymentForm" action="/checkout/review.html" method="post">

<a name="BillingAddressAnchor"></a>

<!-- Billing Address -->
<div class="che-sin-section">

    <span class="glo-required-items-bold che-sin-required-fields-msg">(*) <fmt:message key="field.required"/></span>
    <%--<img class="che-sin-section-header" src="/themes/default/images/common/label/tex-che-billing-address.gif"--%>
         <%--alt="1. Billing Address">--%>
    <h2 class="che-sin-section-header"><fmt:message key="billing.info.header"/></h2>
    <br>

    <div id="widget-billing-address" class="che-sin-form-area">

        <dl id="che-sin-billing-address" class="glo-form">
            <!--  removed nick name field -->
            <!--  removed Salutation drop down  -->
            <dt class="dt-field">&nbsp;</dt>
            <dd>&nbsp;</dd>

            <dt class="dt-field">&nbsp;</dt>
            <dd>&nbsp;</dd>

            <dt class="dt-field"><label for="firstNameForBilling"><fmt:message
                    key="billing.shipping.firstname"/>*</label></dt>
            <dd>
                <input class="required medWidthInput preAuth" name="firstNameForBilling" id="firstNameForBilling" type="text"
                       value="${billingAddress.FIRST_NAME}" size="30" maxlength="40">
            </dd>


            <dt class="dt-field"><label for="lastNameForBilling"><fmt:message
                    key="billing.shipping.lastname"/>*</label></dt>
            <dd>
                <input id="lastNameForBilling" class="required medWidthInput preAuth" name="lastNameForBilling" type="text"
                       value="${billingAddress.LAST_NAME}" size="30"
                       maxlength="40">
            </dd>


            <dt class="dt-field"><label for="address1ForBilling"><fmt:message
                    key="billing.shipping.address"/>*</label></dt>
            <dd>
                <input id="address1ForBilling" class="required medWidthInput preAuth" name="address1ForBilling" type="text"
                       value="${billingAddress.ADDRESS_LINE}" size="30"
                       maxlength="40">
                <%--<label class="error" for="address1ForBilling" style="display:none;">Please enter your username</label>--%>
            </dd>

            <dt class="dt-field"><label for="districtForBilling" class="che-sin-optional"><fmt:message
                    key="billing.shipping.district"/></label>
            </dt>
            <dd>
                <input id="districtForBilling" class="medWidthInput" name="districtForBilling" type="text"
                       value="${billingAddress.DISTRICT}" size="30" maxlength="40">
            </dd>

            <dt class="dt-field"><label><fmt:message key="billing.shipping.city"/>*</label>
            </dt>
            <dd>
                <%--<input id="cityForBilling" class="required medWidthInput preAuth" name="cityForBilling" type="text"--%>
                       <%--value="${billingAddress.CITY}" size="30"--%>
                       <%--maxlength="40">--%>
                <h:stringparamselector name="cityForBilling" stringParam="CITY" defaultValue="${billingAddress.CITY}" includeTitle="Chọn Tỉnh/Thành Phố"/>
            </dd>

            <dt class="dt-field"><label for="phoneForBilling" class="che-sin-optional"><fmt:message
                    key="billing.shipping.phone"/>*</label></dt>
            <dd>
                <input id="phoneForBilling" class="required medWidthInput" name="phoneForBilling" type="text"
                       value="${billingAddress.PHONE}" size="30" maxlength="14">
            </dd>

            <dt class="dt-field"><label for="emailAddressForBilling"><fmt:message
                    key="billing.shipping.email"/>*</label></dt>
            <dd>
                <input id="emailAddressForBilling" class="required email wideWidthInput" name="emailAddressForBilling" type="text"
                       value="${billingAddress.EMAIL}" size="40" maxlength="40">
            </dd>

            <dd class="clr"><!--  --></dd>
        </dl>
        <div class="clr"><!--  --></div>
    </div>
</div>
<!-- END Billing Address -->


<!-- Shipping Address -->
<div class="che-sin-section">
    <h2 class="che-sin-section-header"><fmt:message key="shipping.info.header"/></h2>
    <br><br>
    <input name="task" type="hidden" value="">

    <div class="che-sin-form-area che-sin-ship-options glo-form">
        <dl id="che-sin-shipping-address-option" class="glo-form">

            <dt class="dt-field">&nbsp;</dt>
            <dd>
            <span><input id="ship-billing" name="shipToBillingAddress" type="radio"
                         value="true" ${(empty order.SHIPPING_TO_BILLING || order.SHIPPING_TO_BILLING == true)?'checked':''}>
                <label for="ship-billing" class="radiol"><fmt:message
                        key="billing.shipping.shippingisbilling"/>.</label>
            </span>
            </dd>
            <dt class="dt-field">&nbsp;</dt>
            <dd>
            <span><input id="ship-different" name="shipToBillingAddress" type="radio"
                         value="false" ${(!empty order.SHIPPING_TO_BILLING && order.SHIPPING_TO_BILLING != true)?'checked':''}>
                <label for="ship-different" class="radiol"><fmt:message
                        key="billing.shipping.shippingisnotbilling"/>.</label>
            </span>
            </dd>
        </dl>
        <div class="clr"><!--  --></div>
    </div>
    <div id="widget-shipping-address" class="che-sin-form-area" style="display: none;">

        <dl id="che-sin-shipping-address" class="glo-form">

            <dt class="dt-field"><label for="firstNameForShipping"><fmt:message
                    key="billing.shipping.firstname"/>*</label></dt>
            <dd>
                <input id="firstNameForShipping" class="medWidthInput preAuth" name="firstNameForShipping" type="text"
                       value="${shippingAddress.FIRST_NAME}" size="30"
                       maxlength="40">
            </dd>
            <dt class="dt-field"><label for="lastNameForShipping"><fmt:message
                    key="billing.shipping.lastname"/>*</label></dt>
            <dd>
                <input id="lastNameForShipping" class="medWidthInput preAuth" name="lastNameForShipping" type="text"
                       value="${shippingAddress.LAST_NAME}" size="30"
                       maxlength="40">
            </dd>
            <dt class="dt-field"><label for="address1ForShipping"><fmt:message
                    key="billing.shipping.address"/>*</label></dt>
            <dd>
                <input id="address1ForShipping" class="medWidthInput preAuth" name="address1ForShipping" type="text"
                       value="${shippingAddress.ADDRESS_LINE}" size="30"
                       maxlength="40">
            </dd>
            <dt class="dt-field"><label for="districtForShipping" class="che-sin-optional"><fmt:message
                    key="billing.shipping.district"/></label>
            </dt>
            <dd>
                <input id="districtForShipping" class="medWidthInput" name="districtForShipping" type="text"
                       value="${shippingAddress.DISTRICT}" size="30" maxlength="40">
            </dd>
            <dt class="dt-field"><label for="cityForShipping"><fmt:message key="billing.shipping.city"/>*</label>
            </dt>
            <dd>
                <%--<input id="cityForShipping" class="medWidthInput preAuth" name="cityForShipping" type="text"--%>
                       <%--value="${shippingAddress.CITY}" size="30" maxlength="40">--%>
                    <h:stringparamselector name="cityForShipping" stringParam="CITY" defaultValue="${shippingAddress.CITY}" includeTitle="Chọn Tỉnh/Thành Phố"/>
            </dd>

            <dt class="dt-field"><label for="phoneForShipping" class="che-sin-optional"><fmt:message
                    key="billing.shipping.phone"/>*</label></dt>
            <dd>
                <input id="phoneForShipping" class="medWidthInput" name="phoneForShipping" type="text" value="${shippingAddress.PHONE}"
                       size="30" maxlength="14">
            </dd>
        </dl>
        <div class="clr"><!--  --></div>
    </div>

</div>

<!-- END Shipping Address -->

<!-- Shipping Method and Gift Options -->
<div class="che-sin-section">
    <h2 class="che-sin-section-header"><fmt:message key="shippingmethod.info.header"/></h2> <div class="che-sin-section-header"><a href="/content/phuong-thuc-van-chuyen.html" target="_blank">(Hướng dẫn)</a></div>

    <br>
    <br>
    <div class="che-sin-form-area che-sin-ship-options glo-form">

        <dl id="che-sin-shipping-method" class="glo-form">

            <spring:eval expression="serviceLocator.getShippingTypeDao().getAllShippingTypes('Y')"
                         var="shippingTypes"/>
            <c:forEach varStatus="shippingType" items="${shippingTypes}">
                <c:if test="${empty order.SHIPPING_METHOD}">
                    <c:if test="${shippingType.first}">
                        <c:set var="checked" value="checked"/>
                    </c:if>
                    <c:if test="${!shippingType.first}">
                        <c:set var="checked" value=""/>
                    </c:if>
                </c:if>
                <c:if test="${!empty order.SHIPPING_METHOD}">
                    <c:if test="${order.SHIPPING_METHOD == shippingType.current.shippingTypeCode}">
                        <c:set var="checked" value="checked"/>
                    </c:if>
                    <c:if test="${order.SHIPPING_METHOD != shippingType.current.shippingTypeCode}">
                        <c:set var="checked" value=""/>
                    </c:if>
                </c:if>
                <dt class="dt-field">&nbsp;</dt>
                <dd>
                    <span>
                    <input class="required"
                           id="shipping-method-${shippingType.current.shippingTypeCode}" name="shippingCode" type="radio"
                           value="${shippingType.current.shippingTypeCode}" ${checked}>
                    <label for="shipping-method-${shippingType.current.shippingTypeCode}">
                            ${shippingType.current.shippingTypeName}
                    </label>
                    </span>
                </dd>
            </c:forEach>

        </dl>

    </div>


</div>
<!-- End Shipping Method and Gift Options -->

<!-- Payment Method -->
<div class="che-sin-section">
    <h2 class="che-sin-section-header"><fmt:message key="paymentmethod.info.header"/></h2> <div class="che-sin-section-header"><a href="/content/phuong-thuc-thanh-toan.html" target="_blank">(Hướng dẫn)</a></div>
    <br>
    <br>

    <div class="che-sin-form-area che-sin-ship-options glo-form">

        <dl id="che-sin-payment-method" class="glo-form">

            <spring:eval expression="serviceLocator.getPaymentProviderDao().findAllPaymentProviders('Y', 'sequence')"
                         var="paymentProviders"/>
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
                <dt class="dt-field">&nbsp;</dt>
                <dd>
                    <span>
                    <input id="payment_id_${paymentProvider.current.id}" class="che-payment-method required" name="paymentProviderId" type="radio"
                           value="${paymentProvider.current.id}" ${checked}>
                    <label for="payment_id_${paymentProvider.current.id}">
                            ${paymentProvider.current.name}
                    </label>
                    </span>
                </dd>
            </c:forEach>

        </dl>

    </div>


</div>
<!-- End Payment Method -->


<div id="che-sin-review-order" class="che-sin-section-no-mtop">
    <div class="che-sin-review-order">
        <%--<input type="image" src="/themes/default/images/common/buttons/but-che-review-order.gif" alt="Review Order"--%>
               <%--class="glo-but-css-off">--%>
            <button type="submit" name="btnG" class="submit-button"><fmt:message key="review.your.order"/></button>
        <br><br><fmt:message key="billing.shipping.your.order.not.complete.until.confirm"/>
    </div>
</div>

</form>


</div>
<div id="che-sin-left-bottom"><!--  --></div>
</div>

<div id="che-sin-column-right">

    <div id="che-sin-ordertotals">
        <div class="che-sin-right-box-top"><!--  --></div>
        <div class="che-sin-right-box-content">
            <div class="che-sin-right-box-content-content">
                <h2><fmt:message key="review.total.entire.order"/></h2>


                <div class="js-orderSummaryArea">


                    <dl id="che-sin-order-summary">
                        <dt><fmt:message key="billing.shipping.subtotal"/></dt>
                        <spring:eval expression="sessionObject.getOrder().getSubPriceTotal()" var="subPrice"/>
                        <spring:eval
                                expression="T(com.easysoft.ecommerce.util.Money).valueOf(subPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"
                                var="subTotalPrice"/>
                        <dd><span class="js-merchandise">${subTotalPrice}</span></dd>
                        <dd class="clr"><!--  --></dd>
                        <%--Order discount--%>
                        <spring:eval expression="sessionObject.getOrder().getSubPriceDiscountTotal()" var="subTotalDiscount"/>
                        <c:if test="${!empty subTotalDiscount && subTotalDiscount > 0}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subTotalDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalDiscount"/>
                            <dt class="discount-row"><fmt:message key="subordertotal-discount"/></dt>
                            <dd class="discount-row">-${subTotalDiscount}</dd>
                            <dd class="clr"><!--  --></dd>
                        </c:if>

                        <dt id="giftBox1"><fmt:message key="billing.shipping.gift.fee"/></dt>
                        <dd id="giftBox2"><span class="js-giftBoxFee"></span></dd>
                        <dd class="clr" id="giftBox3"><!--  --></dd>

                        <dt><fmt:message key="billing.shipping.fee"/></dt>
                        <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getShippingFee(sessionObject)" var="shippingPrice"/>
                        <spring:eval expression="sessionObject.getOrder().getShippingDiscountPrice()" var="shippingPriceDiscount"/>
                        <%--Shipping price > 0, Check discount for shipping fee--%>
                        <c:if test="${!empty shippingPrice && shippingPrice > 0}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>
                            <dd><span class="js-orderShippingAmount">${shipping_Price}</span><br></dd>
                            <c:if test="${shippingPriceDiscount > 0}">
                                <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPriceDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price_Discount"/>
                                    <dl class="discount-row">
                                        <dt><fmt:message key="billing.shipping.discount.shipping.fee"/></dt>
                                        <dd>-${shipping_Price_Discount}</dd>
                                    </dl>
                            </c:if>
                        </c:if>
                        <%--Shipping price is 0, don't care discount for shipping fee--%>
                        <c:if test="${empty shippingPrice || shippingPrice <= 0}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>
                            <dd><span class="js-orderShippingAmount">${shipping_Price}</span></dd>
                        </c:if>
                        <dd class="clr"></dd>
                        <spring:eval
                                expression="T(com.easysoft.ecommerce.util.Money).valueOf(sessionObject.getOrder().getTaxPrice(),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"
                                var="tax_Price"/>
                        <dt id="giftBox1"><fmt:message key="billing.shipping.tax"/></dt>
                        <dd id="giftBox2"><span class="js-giftBoxFee">${tax_Price}</span></dd>
                        <dd class="clr" id="giftBox3"><!--  --></dd>

                        <dd class="clr"></dd>

                    </dl>

                    <spring:eval expression="sessionObject.getOrder().getTotalPrice()" var="price"/>
                    <spring:eval
                            expression="T(com.easysoft.ecommerce.util.Money).valueOf(price,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"
                            var="totalPrice"/>
                    <div id="che-sin-grand-total-wrapper">
                        <div><fmt:message key="billing.shipping.totalorder"/></div>
                        <span class="js-orderTotalAmount che-sin-grand-total">${totalPrice}</span><br>

                    </div>

                </div>
            </div>
        </div>
        <div class="che-sin-right-box-bottom"><!--  --></div>
    </div>

    <h:cmscontent name="customer-service"/>

    <div></div>
    <div>


    </div>

    <!-- Express Credit Message -->
    <div id="che-cre-box-right"><!--  --></div>
    <!-- /Express Credit Message -->

</div>

<div class="clr"><!--  --></div>
</div>
</div>

<!-- END Page -->
</div>
<div class="clr"><!--  --></div>
</div>
</div>
</div>
</div>
</body>
</html>
