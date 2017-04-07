<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}|${pageContext.request.queryString}">
    <c:set value="${fn:replace(uri,'/news/c/','')}" var="uri"/>
<html>
<head>
    <title>${site.description}</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<spring:eval expression="serviceLocator.getNewsCategoryDao().findUniqueBy('uri', uri, site.id)" var="newsCategory"/>
<c:url var="newsCategoryUrl" value="/news/index.html"/>
<spring:eval expression="serviceLocator.getMenuDao().findUniqueBy('uri', 'news/index.html', site.id)" var="newsMenu"/>
<c:set value="${param.page}" var="currPage"/>
<c:if test="${empty currPage || currPage <= 1}">
    <c:set value="0" var="currPage"/>
</c:if>
<c:if test="${!(empty currPage || currPage <= 1)}">
    <c:set value="${currPage - 1}" var="currPage"/>
</c:if>
<spring:eval expression="serviceLocator.getNewsCategoryDao().findNewscategoriesHavingNews(site.id)" var="newsCategories"/>
<spring:eval expression="serviceLocator.getNewsDao().findNewsByNewsCategory(uri, null, T(java.lang.Long).valueOf(currPage)*T(java.lang.Long).valueOf('10'), T(java.lang.Long).valueOf('10'), 'createdDate desc', site.id)" var="newses"/>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>
<spring:eval expression="serviceLocator.getNewsDao().countActiveNewsByNewsCategory(uri, null, site.id)" var="numberOfNewses"/>
<!--=== Breadcrumbs ===-->
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left">${newsMenu.name}</h1>
        <ul class="pull-right breadcrumb">
            <li><a href="index.html">Home</a></li>
            <li><a href="${newsCategoryUrl}">${newsMenu.name}</a></li>
            <li class="active">${newsCategory.name}</li>
        </ul>
    </div>
</div><!--/breadcrumbs-->

<h:newslist newsCategory="${newsCategory}" newses="${newses}" params="${params}" numberOfNewses="${numberOfNewses}"/>

</body>
</html>
</app:cache>