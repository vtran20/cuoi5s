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

        $("#unsubscribe").validate({
            messages: {
                optin:  {
                    required: "<fmt:message key="login.please.enter.optin"/>"
                }
            }
        });
    });

</script>
<div class='page-body body-with-border page-body-float-left'>

    <div id="glo-body-content">

        <div class="che-bas-header-both">
            <c:if test="${email.optin == 'Y'}">
            <div class="cms-content">
                <h2>Hủy đăng ký nhận thông tin giảm giá</h2>
            Chúng tôi rất muốn tiếp tục gởi đến quý khách thông tin các sản phẩm giảm giá, các đợt khuyến mãi và thông tin về sản phẩm mới. Tuy nhiên, nếu quý khách không muốn tiếp tục nhận email nữa, xin vui lòng hủy đăng ký dưới đây:
                <p>
                    <form id="unsubscribe" name="/marketing/unsubscribe.html">
                        <input type="hidden" name="email" value="${email.email}">
                        <input type="checkbox" name="optin" value="N" class="required">&nbsp; Tôi không muốn nhận thông tin khuyến mãi. Vui lòng xóa <b>${email.email}</b> khỏi danh sách emails.
                <br>
                <br>
                    <button type="submit" name="btnG" class="submit-button" >Unsubscribe</button>

                    </form>
                </p>
            </div>
            </c:if>
            <c:if test="${email.optin != 'Y'}">
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
