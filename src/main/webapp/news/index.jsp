<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}|${pageContext.request.queryString}">
    <c:set value="${fn:replace(uri,'/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'index.html','')}" var="uri"/>
<spring:eval expression="serviceLocator.getMenuDao().findUniqueBy('uri', 'news/index.html', site.id)" var="newsMenu"/>
<html>
<head>
    <title>${newsMenu.name}</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<!--=== Breadcrumbs ===-->
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left">${newsMenu.name}</h1>
        <ul class="pull-right breadcrumb">
            <li><a href="index.html">Home</a></li>
            <li class="active">${newsMenu.name}</li>
        </ul>
    </div>
</div><!--/breadcrumbs-->
<!--=== End Breadcrumbs ===-->
<c:set value="${param.page}" var="currPage"/>
<c:if test="${empty currPage || currPage <= 1}">
    <c:set value="0" var="currPage"/>
</c:if>
<c:if test="${!(empty currPage || currPage <= 1)}">
    <c:set value="${currPage - 1}" var="currPage"/>
</c:if>
<spring:eval expression="serviceLocator.getNewsDao().findActiveByOrder(null, T(java.lang.Long).valueOf(currPage)*T(java.lang.Long).valueOf('10'), T(java.lang.Long).valueOf('10'), 'createdDate desc', site.id)" var="newses"/>
<spring:eval expression="serviceLocator.getNewsCategoryDao().findNewscategoriesHavingNews(site.id)" var="newsCategories"/>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>
<spring:eval expression="serviceLocator.getNewsDao().count('Y', site.id)" var="numberOfNewses"/>

<h:newslist newsCategory="${null}" newses="${newses}" params="${params}" numberOfNewses="${numberOfNewses}"/>
</body>
</html>
</app:cache>