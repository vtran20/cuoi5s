<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="forgot.password"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="popup"/>
</head>

<body>
<h1><fmt:message key="forgot.password"/></h1>

<div class="forgotpasswordform">
    <c:if test="${!empty sent}">
        <p>
            Mật khẩu tạm đã được gởi vào email của bạn. Bạn phải thay đổi mật khẩu khi đăng nhập thành công.
        </p>
    </c:if>
    <c:if test="${empty sent}">
        <form action="/user/reset_password.html" method="post">
            <p>
                <fmt:message key="forgot.password.message"/>
            </p>
            <br>
            <!-- user Name -->
            <label for="userName" class="glo-tex-blk">Email:</label><br>
            <input class="formField" id="userName" name="userName" type="text" size="20" maxlength="40"/>
            <c:if test="${!empty messages}">
                <spring:eval expression="messages.getErrorMessage('email_invalid')" var="email_invalid"/>
                <c:if test="${!empty email_invalid}">
                    <br><span class="error">${email_invalid}</span>
                </c:if>
            </c:if>
            <br/>
            <label for="userName" class="glo-tex-blk"><fmt:message key="forgot.password.captcha.label"/></label><br>
            <spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SITE_KEY')" var="siteKey"/>
            <spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SECRET_KEY')" var="siteSecret"/>
            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).createSToken(siteSecret)" var="encryptedString"/>
            <div class="g-recaptcha" data-sitekey="${siteKey}" data-stoken="${encryptedString}"></div>
            <br/>
            <br/>
            <input id="use-for-but-send-pwd" type="submit" value="Send"/>

        </form>
    </c:if>
</div>

</body>
</html>
