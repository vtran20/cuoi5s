<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>Đăng ký email</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="no_leftnav"/>
</head>

<body>
<script type="text/javascript">
    $(function() {

        $("#subscribe").validate({
            messages: {
                email:  {
                    required: "<fmt:message key="login.please.enter.email"/>",
                    email: "<fmt:message key="login.email.incorrect"/>"
                },
                firstName:  "<fmt:message key="billing.shipping.please.enter.firstname"/>",
                lastName:  "<fmt:message key="billing.shipping.please.enter.lastname"/>"
            }
        });
    });

</script>
<div class='page-body body-with-border page-body-float-left'>

    <div id="glo-body-content">

        <div class="che-bas-header-both">
            <c:if test="${empty email}">
            <div class="cms-content">
                <h2>Đăng ký nhận thông tin giảm giá</h2>
            Để nhận thông tin các sản phẩm giảm giá, các đợt khuyến mãi và các sản phẩm mới. Vui lòng nhập thông tin dưới đây:
                <p>
                    <form id="subscribe" name="/marketing/subscribe.html">
                <div>
                <table width="400" align="left" style="border: 0 solid #B6B7AF;" >
                    <tbody><tr>
                        <td style="border: 0 solid #B6B7AF;"><fmt:message key="login.lastname"/></td>
                        <td style="border: 0 solid #B6B7AF;"><input type="text" name="lastName" class="required" value=""></td>
                    </tr>
                    <tr>
                        <td style="border: 0 solid #B6B7AF;"><fmt:message key="login.firstname"/></td>
                        <td style="border: 0 solid #B6B7AF;"><input type="text" name="firstName" class="required" value=""></td>
                    </tr>
                    <tr>
                        <td style="border: 0 solid #B6B7AF;"><fmt:message key="login.email"/></td>
                        <td style="border: 0 solid #B6B7AF;"><input type="text" name="email" class="required email" value=""></td>
                    </tr>
                    </tbody></table>
                </div>
                <div class="clear"></div>
                <br>
                <br>
                    <button type="submit" name="btnG" class="submit-button" >Subscribe</button>

                    </form>
            </div>
            </c:if>
            <c:if test="${email.optin == 'Y'}">
            <div class="cms-content">
                <h2>Đăng ký nhận thông tin giảm giá</h2>
            Hệ thống đã đăng ký thành công.
            </div>
            </c:if>
            <c:if test="${email.optin == 'N'}">
                <div class="cms-content">
                    <h2>Email <b>${email.email}</b> đã được xóa khỏi hệ thống</h2>
                    Nếu quý khách muốn tiếp tục nhận thông tin các sản phẩm giảm giá, các đợt khuyến mãi và thông tin về sản phẩm mới<br>
                    Vui lòng nhấn vào đây <a href="/marketing/subscribe.html?email=${email.email}&optin=Y">${email.email}</a>
                </div>
            </c:if>
            <div class="che-bas-continue-shopping">
                <span><a href="/index.html" tabindex="-1"><fmt:message key="basket.continue.shopping"/></a></span>
            </div>
        </div>

    </div>

</div>
</body>
</html>
