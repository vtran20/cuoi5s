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

            $("#AccountForm").validate({
                messages: {
                    firstName:  "<fmt:message key="billing.shipping.please.enter.firstname"/>",
                    lastName:  "<fmt:message key="billing.shipping.please.enter.lastname"/>",
                    address_1:  "<fmt:message key="billing.shipping.please.enter.address"/>",
                    city:  "<fmt:message key="billing.shipping.please.enter.city"/>",
                    phone:  "<fmt:message key="billing.shipping.please.enter.phonenumber"/>",
                    email:  {
                        required: "<fmt:message key="billing.shipping.please.enter.email"/>",
                        email: "<fmt:message key="billing.shipping.email.incorrect"/>"
                    }
                }
            });

        });
</script>

<div class="leftnav">
    <ul id="left-menu" class="left-menu">
        <li>
            <a class="m1" href="/user/main.html">Thông tin cá nhân</a>
        </li>
        <%--<li>--%>
            <%--<a class="m2" href="/user/change_account.html">Thay đổi tài khoản</a>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a class="m3" href="/user/list_orders.html">Đơn đặt hàng</a>--%>
        <%--</li>--%>
    </ul>
</div>

<div class='page-body body-with-border page-body-float-left'>

    <div id="thumbnail-height">

        <div class="catalog-category-breadcrumb">

            <c:set value="/" var="homeurl"/>
            <a href="${homeurl}"><fmt:message key="breadcrumb.home"/></a>&nbsp;&gt;&nbsp;${cmsArea.title}
        </div>
        <div class="catalog-thumbnail">

            <!-- Billing Address -->
            <div class="che-sin-section" style="border:0">

                <span class="glo-required-items-bold che-sin-required-fields-msg">(*) <fmt:message key="field.required"/></span>
                <%--<img class="che-sin-section-header" src="/themes/default/images/common/label/tex-che-billing-address.gif"--%>
                <%--alt="1. Billing Address">--%>
                <h2 class="che-sin-section-header">Thông tin cá nhân</h2>
                <%--<div class="clr"><!--  --></div>--%>
                <%--<br>--%>
                <%--<ul>--%>
                    <%--<li class="error-msg">lkdsjklfds</li>--%>
                    <%--<li class="error-msg">lkdsjklfds</li>--%>
                    <%--<li class="error-msg">lkdsjklfds</li>--%>
                    <%--<li class="error-msg">lkdsjklfds</li>--%>
                <%--</ul>--%>
                <br>
                <c:set var="userName" value="${sessionObject.USER_NAME}"/>
                <spring:eval expression="serviceLocator.getUserDao().getClientUser(userName, site)" var="billingAddress"/>
                <form id="AccountForm" action="/user/change_account.html" method="post">
                    <input name="id" type="hidden" value="${billingAddress.id}"/>
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
                            <input class="required medWidthInput preAuth" name="firstName" id="firstNameForBilling" type="text"
                                   value="${billingAddress.firstName}" size="30" maxlength="40">
                        </dd>


                        <dt class="dt-field"><label for="lastNameForBilling"><fmt:message
                                key="billing.shipping.lastname"/>*</label></dt>
                        <dd>
                            <input id="lastNameForBilling" class="required medWidthInput preAuth" name="lastName" type="text"
                                   value="${billingAddress.lastName}" size="30"
                                   maxlength="40">
                        </dd>


                        <dt class="dt-field"><label for="address1ForBilling"><fmt:message
                                key="billing.shipping.address"/>*</label></dt>
                        <dd>
                            <input id="address1ForBilling" class="required medWidthInput preAuth" name="address_1" type="text"
                                   value="${billingAddress.address_1}" size="30"
                                   maxlength="40">
                        </dd>

                        <dt class="dt-field"><label for="districtForBilling" class="che-sin-optional"><fmt:message
                                key="billing.shipping.district"/></label>
                        </dt>
                        <dd>
                            <input id="districtForBilling" class="medWidthInput" name="district" type="text"
                                   value="${billingAddress.district}" size="30" maxlength="40">
                        </dd>

                        <dt class="dt-field"><label><fmt:message key="billing.shipping.city"/>*</label>
                        </dt>
                        <dd>
                            <%--<input id="cityForBilling" class="required medWidthInput preAuth" name="cityForBilling" type="text"--%>
                            <%--value="${billingAddress.CITY}" size="30"--%>
                            <%--maxlength="40">--%>
                            <h:stringparamselector name="city" stringParam="CITY" defaultValue="${billingAddress.city}" includeTitle="Chọn Tỉnh/Thành Phố"/>
                        </dd>

                        <dt class="dt-field"><label for="phoneForBilling" class="che-sin-optional"><fmt:message
                                key="billing.shipping.phone"/>*</label></dt>
                        <dd>
                            <input id="phoneForBilling" class="required medWidthInput" name="phone" type="text"
                                   value="${billingAddress.phone}" size="30" maxlength="14">
                        </dd>

                        <dt class="dt-field"><label for="emailAddressForBilling"><fmt:message
                                key="billing.shipping.email"/>*</label></dt>
                        <dd>
                            <input id="emailAddressForBilling" class="required email wideWidthInput" name="email" type="text"
                                   value="${billingAddress.email}" size="40" maxlength="40">
                        </dd>

                        <dd class="clr"><!--  --></dd>
                    </dl>
                    <div class="clr"><!--  --></div>
                </div>
                    <div id="update-user" class="che-sin-section-no-mtop">
                        <div class="update-user">
                                <button type="submit" name="btnG" class="submit-button">Cập nhật</button>
                        </div>
                    </div>

                </form>
            </div>
            <!-- END Billing Address -->


            <div class="clr"></div>
        </div>
    </div>
</div>

</body>
</html>
