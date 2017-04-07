<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}|${pageContext.request.queryString}">
<html>
<c:set value="${fn:replace(uri,'.html','')}" var="temp"/>
<c:set value="${fn:split(temp,'/')}" var="ids"/>
<c:set value="${ids[fn:length(ids)-1]}" var="id"/>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>
<spring:eval expression="params.getParameter('albumId')" var="albumId"/>
<spring:eval expression="serviceLocator.albumDao.findById(T(java.lang.Long).valueOf(albumId))" var="album"/>
<c:set value="100" var="numPerPage"/>
<c:set value="${param.page}" var="currPage"/>
<c:if test="${empty currPage || currPage <= 1}">
    <c:set value="0" var="currPage"/>
</c:if>
<c:if test="${!(empty currPage || currPage <= 1)}">
    <c:set value="${currPage - 1}" var="currPage"/>
</c:if>


<head>
    <title><c:out value="${site.name} | ${album.name}"/></title>
    <meta name="description" content="Gallery"/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left">${album.name}</h1>
        <ul class="pull-right breadcrumb">
            <li><a href="/index.html">Home</a></li>
            <li><a href="/page/gallery.html">Gallery</a></li>
            <li class="active">${album.name}</li>
        </ul>
    </div>
</div>

<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<spring:eval expression="serviceLocator.albumImageDao.findAlbumImages(album.id, T(java.lang.Long).valueOf(currPage)*T(java.lang.Long).valueOf(numPerPage), numPerPage, 'id', site.id, true)" var="albumImages"/>
<c:set var="imgLength" value="${fn:length(albumImages)}"/>
<style>
    .carousel-inner > .item > img {
        width: 100%;
    }
</style>
<div class="container content">
    <div class="row">
        <c:if test="${!empty albumImages}">
            <div class="col-md-1"></div>
            <div class="col-md-10">
                <div id="myCarousel-1" class="carousel slide carousel-v1">
                    <div class="carousel-inner">
                        <c:forEach items="${albumImages}" var="image" varStatus="imageIndex">
                            <c:set var="active" value=""/>
                            <c:if test="${imageIndex.index == 0}">
                                <c:set var="active" value="active"/>
                            </c:if>
                            <div class="item ${active}">
                                <img src="${imageServer}/get/${image.uri}.jpg?op=scale|1200" alt="">
                                <c:if test="${!empty image.description}">
                                    <div class="carousel-caption">
                                        <p>${image.description}</p>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="carousel-arrow">
                        <a class="left carousel-control" href="#myCarousel-1" data-slide="prev">
                            <i class="fa fa-angle-left"></i>
                        </a>
                        <a class="right carousel-control" href="#myCarousel-1" data-slide="next">
                            <i class="fa fa-angle-right"></i>
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-md-1"></div>

        </c:if>
    </div>
</div>
</body>
</html>
</app:cache>