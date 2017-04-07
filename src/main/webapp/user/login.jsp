<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>Đăng nhập hệ thống</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<script type="text/javascript">
    $(function() {

        $(function() {

            $("#loginForm").validate({
                messages: {
                    userName:  {
                        required: "<fmt:message key="login.please.enter.email"/>",
                        email: "<fmt:message key="login.email.incorrect"/>"
                    },
                    password:  "<fmt:message key="login.please.enter.password"/>"
                }
            });

            $("#createAccountForm").validate({
                messages: {
                    userName:  {
                        required: "<fmt:message key="login.please.enter.email"/>",
                        email: "<fmt:message key="login.email.incorrect"/>"
                    },
                    password:  "<fmt:message key="login.please.enter.password"/>",
                    repassword:  "<fmt:message key="login.please.enter.repassword"/>",
                    firstName:  "<fmt:message key="billing.shipping.please.enter.firstname"/>",
                    lastName:  "<fmt:message key="billing.shipping.please.enter.lastname"/>"
                }
            });

        });

    });
</script>
<div class='page-body body-with-border page-body-float-left'>

    <div id="page-content-wrapper">
        <div id="page-content">

            <div id="checkout-login">

                <!-- Sign In -->
                <div class="account-login-content">
                    <div class="header"><fmt:message key="login.do.you.have.account"/></div>

                    <div class="account-login-area">

                        <div class="account-login-wrapper-left">
                            <div class="account-login-header-left"><fmt:message key="login.yes.i.have.an.account"/></div>
                            <div class="account-login-container-left">

                                <div class="static-message"><fmt:message key="login.enter.username.password"/></div>
                                <c:set var="error" value="${error}"/>
                                <c:if test="${!empty error}">
                                    <div class="error"><fmt:message key="${error}"/></div>
                                </c:if>
                                <form id="loginForm" name="loginForm" method="post" action="/user/login.html"
                                      class="formset">
                                    <input name="source" value="${source}" type="hidden"/>
                                    <div class="row">
                                        <div class="label"><label for="email"><fmt:message key="login.email"/></label><span
                                                class="asterik">*</span></div>
                                        <div class="field"><input type="text" maxlength="80" name="userName"
                                                                  class="required email checkout-shippingform"></div>
                                    </div>

                                    <div class="row">
                                        <div class="label"><label for="password"><fmt:message key="login.password"/></label><span
                                                class="asterik">*</span></div>
                                        <div class="field"><input type="password" maxlength="15" name="password"
                                                                  class="required checkout-shippingform"></div>
                                    </div>
                                    <div class="row">
                                        <div><!-- --></div>
                                        <div class="forgotpw">
                                            <a href="/user/forgotpassword.html" class="forgotpassword"><fmt:message key="login.forgot.password"/></a></div>
                                    </div>

                                    <div class="row">
                                        <div><!-- --></div>
                                        <div class="button">
                                        <button type="submit" name="btnG" class="submit-button"><fmt:message key="template.sign.in"/></button>
                                        </div>
                                    </div>
                                    <div class="openid-login bold">
                                        <div class="openid-login-lable"><fmt:message key="login.login.by.openid.account"/></div>
                                        <a href="/user/openidlogin.html?op=Google&source=${source}"
                                           class="openid-login-link"><img
                                                src="/themes/default/images/common/buttons/login-google.gif"/> </a>
                                        <a href="/user/openidlogin.html?op=Yahoo&source=${source}"
                                           class="openid-login-link"><img
                                                src="/themes/default/images/common/buttons/login-yahoo.gif"/></a>
                                    </div>
                                </form>

                            </div>
                        </div>


                        <div class="account-login-wrapper-right">
                            <div class="account-login-header-right"><fmt:message key="login.i.dont.have.an.account"/></div>
                            <div class="account-login-container-right">

                                <form id="createAccountForm" name="createAccountForm" method="post"
                                      action="/user/new_account.html" class="formset">
                                    <input name="source" value="${source}" type="hidden"/>
                                    <div class="static-message"><fmt:message key="login.register.account"/></div>
                                    <c:set var="error_new_account" value="${error_new_account}"/>
                                    <c:if test="${!empty error_new_account}">
                                        <div class="error"><fmt:message key="${error_new_account}"/></div>
                                    </c:if>

                                    <div class="row">
                                        <div class="label"><label for="email"><fmt:message key="login.email"/></label><span
                                                class="asterik">*</span></div>
                                        <div class="field"><input id="email" type="text" maxlength="80" name="userName"
                                                                  class="required email checkout-shippingform"></div>
                                    </div>

                                    <div class="row">
                                        <div class="label"><label for="password"><fmt:message key="login.password"/></label><span
                                                class="asterik">*</span></div>
                                        <div class="field"><input id="password" type="password" maxlength="15"
                                                                  name="password"
                                                                  class="required checkout-shippingform"></div>
                                    </div>

                                    <div class="row">
                                        <div class="label"><label for="repassword"><fmt:message key="login.enter.repassword"/></label><span
                                                class="asterik">*</span></div>
                                        <div class="field"><input id="repassword" type="password" maxlength="15"
                                                                  name="repassword"
                                                                  class="required checkout-shippingform"></div>
                                    </div>

                                    <div class="row">
                                        <div class="label"><label for="lastName"><fmt:message key="login.lastname"/></label><span
                                                class="asterik">*</span></div>
                                        <div class="field"><input id="lastName" type="type" maxlength="15"
                                                                  name="lastName"
                                                                  class="required checkout-shippingform"></div>
                                    </div>

                                    <div class="row">
                                        <div class="label"><label for="firstName"><fmt:message key="login.firstname"/></label><span
                                                class="asterik">*</span></div>
                                        <div class="field"><input id="firstName" type="text" maxlength="15"
                                                                  name="firstName"
                                                                  class="required checkout-shippingform"></div>
                                    </div>

                                    <div class="row">
                                        <div><!-- --></div>
                                        <div class="button">
                                            Tạo tài khoản nghĩa là bạn đã đọc và chấp nhận các <a href="/content/dieu-khoan-su-dung.html">điểu khoản và quy định</a> của ${site.domain}
                                        </div>
                                    <div class="row">
                                        <div><!-- --></div>
                                        <div class="button">
                                            <button type="submit" name="btnG" class="submit-button"><fmt:message key="template.create.account"/></button>
                                        </div>
                                    </div>

                                </form>

                            </div>
                        </div>

                        <div style="clear: both;"><!-- --></div>
                    </div>
                </div>

            </div>
            <!-- End Sign In --></div>
    </div>
</div>
</body>
</html>
