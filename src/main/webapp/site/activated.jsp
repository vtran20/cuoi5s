<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.activated.account"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.activated.account"/></h1>
        <ul class="pull-right breadcrumb">
            <li><a href="/">Home</a></li>
            <li class="active"><fmt:message key="site.activated.account"/></li>
        </ul>
    </div>
</div>

<%--Start Content--%>
<div class="container content">
    <h:frontendmessage _messages="${messages}"/>
    <div class="title-box">
        <div class="title-box-text">Cảm ơn <span class="color-green">Quý Khách</span> đã sử dụng dịch vụ của chúng tôi
        </div>
        <p>Chúng tôi mong muốn mang đến sự thích thú và dễ dàng trong công việc thiết kế web đầy nhàm chán và khó khăn.
            Công việc của bạn là nội dung website, chúng tôi sẽ giúp bạn phần còn lại.</p>
    </div>

    <%--<div class="margin-bottom-40"></div>--%>
    <h:sitetemplates column="4"/>
</div>
<%--End Content--%>
</body>
</html>
