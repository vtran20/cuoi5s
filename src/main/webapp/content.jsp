<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}|${pageContext.request.queryString}">
<spring:eval expression="serviceLocator.cmsAreaContentDao.getPageContent(site, T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI())" var="cmsContent" />
<spring:eval expression="serviceLocator.cmsAreaDao.getCmsArea(site, T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI())" var="cmsArea" />

<html>
<head>
<title>${cmsArea.title}</title>
<meta name="description" content="${cmsArea.metaDescription}" />
<meta name="keywords" content="${cmsArea.metaKeyword}" />
<meta name="decorator" content="no_leftnav" />
</head>

<body>

<div class="leftnav">
    <ul id="left-menu" class="left-menu">
        <li>
            <a class="m1" href="/content/cau-hoi-thuong-gap.html">Câu hỏi thường gặp</a>
        </li>
        <li>
            <a class="m2" href="/content/huong-dan-mua-hang.html">Hướng dẫn mua hàng</a>
        </li>
        <li>
            <a class="m3" href="/content/phuong-thuc-thanh-toan.html">Phương thức thanh toán</a>
        <%--<ul id="left-menu-2">--%>
            <%--<li><a href="/content/phuong-thuc-thanh-toan.html#1">Chuyển khoản</a></li>--%>
            <%--<li><a href="/content/phuong-thuc-thanh-toan.html#2">Ngân lượng</a></li>--%>
            <%--<li><a href="/content/phuong-thuc-thanh-toan.html#3">Bảo kim</a></li>--%>
            <%--</ul>--%>
        </li>
        <li>
            <a class="m4" href="/content/phuong-thuc-van-chuyen.html">Phương thức vận chuyển</a>
        </li>
        <li>
            <a class="m5" href="/content/doi-tra-hang.html">Đổi/trả hàng</a>
        </li>
    </ul>
</div>

<div class='page-body body-with-border page-body-float-left'>

    <div id="thumbnail-height">

        <div class="catalog-category-breadcrumb">

            <c:set value="/" var="homeurl"/>
            <a href="${homeurl}"><fmt:message key="breadcrumb.home"/></a>&nbsp;&gt;&nbsp;${cmsArea.title}
        </div>
        <div class="catalog-thumbnail">
            <!--Start CMS content-->


            <style>
                .cms-content {
                    margin: 10px 10px 10px 10px;
                }
                .cms-content h1, .cms-content h2, .cms-content h3, .cms-content h4, .cms-content h5, .cms-content h6 {
                    color:#DF0403;
                }
                .cms-content h2 {
                    padding: 10px 0 10px 0;
                }
                .cms-content h3 {
                    padding: 10px 0 10px 0;
                }
                .cms-content p {
                    padding: 0 0 15px 0;
                    line-height: 20px;
                }
                .cms-content ul {
                    display: block;
                    list-style-type: square;
                    padding: 0 0 10px 40px;
                    line-height: 20px;
                }

            </style>
            ${cmsContent.content}
            <div class="clr"></div>
        </div>
    </div>
</div>


</body>
</html>
</app:cache>