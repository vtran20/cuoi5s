<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}">
<html>
<c:set value="${fn:substring(uri, 1, fn:length(uri))}" var="uri"/>
<%--<c:set value="${fn:split(temp,'/')}" var="ids"/>--%>
<%--<c:set value="${ids[fn:length(ids)-1]}" var="id"/>--%>
<spring:eval expression="serviceLocator.getAlbumImageDao().getFirstImageOfAlbum(site.id)" var="albumImages"/>
<spring:eval expression="serviceLocator.getMenuDao().getMenu(site, uri, 'true')" var="menu"/>

<head>
    <title><c:out value="${site.name} | ${menu.name}"/></title>
    <meta name="description" content="<c:out value="${site.name} | ${menu.name}"/>"/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>

<%--<link rel="stylesheet" href="//htmlstream.com/preview/unify-v1.6-production/assets/plugins/cube-portfolio/cubeportfolio/css/cubeportfolio.css">--%>
<%--<script type="text/javascript" src="//htmlstream.com/preview/unify-v1.6-production/assets/plugins/cube-portfolio/cubeportfolio/js/jquery.cubeportfolio.min.js"></script>--%>

<c:if test="${fn:length(albumImages) == 1}">
    <c:redirect url="/page/album.html?albumId=${albumImages[0].album.id}"/>
</c:if>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left">Gallery</h1>
        <ul class="pull-right breadcrumb">
            <li><a href="index.html">Home</a></li>
            <li class="active">Gallery</li>
        </ul>
    </div>
</div>

<div class="container content">
    <div class="gallery-page">
        <div class="row margin-bottom-20">

            <spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>
            <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
            <c:set value="params.id" var="albumId"/>
                <ul class="list-unstyled row portfolio-box-v1">
                    <c:forEach items="${albumImages}" var="image">
                    <li class="col-md-3 col-sm-6">
                        <img class="img-responsive" src="${imageServer}/get/${image.uri}.jpg?op=scale_x225&op=crop_0,0,300,225" alt="">
                        <div class="portfolio-box-v1-in">
                            <%--<h3>Ahola Company</h3>--%>
                            <p>${image.album.name}</p>
                            <a class="btn-u btn-u-sm btn-brd btn-brd-hover btn-u-light" href="/page/album.html?albumId=${image.album.id}"><fmt:message key="album.view.album"/></a>
                        </div>
                    </li>
                    </c:forEach>
                </ul>
        </div>

    </div><!--/gallery-page-->
</div>

</body>
</html>
</app:cache>