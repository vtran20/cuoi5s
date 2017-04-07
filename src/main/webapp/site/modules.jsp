<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<%--<app:cache key="${uri}|${pageContext.request.queryString}">--%>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString)" var="params"/>
<html>
<head>
<title>Modules</title>
<meta name="description" content="Thư viện modules và ứng dụng"/>
<meta name="keywords" content="Modules, Plugins"/>
<meta name="decorator" content="${template.templateCode}"/>
</head>
<body>
<c:set value="${param.thisSiteId}" var="clientSiteId"/>
<c:if test="${(empty clientSiteId || clientSiteId <= 0) && !empty sessionObject.USER_ID}">
    <spring:eval expression="serviceLocator.siteDao.getSitesByUser(T(java.lang.Long).valueOf(sessionObject.USER_ID))" var="sites" />
    <c:if test="${!empty sites && fn:length(sites) == 1}">
        <c:set value="${sites[0].id}" var="clientSiteId"/>
    </c:if>
</c:if>
<c:if test="${empty clientSiteId}">
    <c:set value="0" var="clientSiteId"/>
</c:if>
<div class="breadcrumbs">
    <div class="container">
        <c:choose>
            <c:when test="${clientSiteId > 0}">
                <spring:eval expression="serviceLocator.getSiteDao().findById(T(java.lang.Long).valueOf(clientSiteId))" var="clientSite" />
                <h1 class="pull-left">Modules: ${clientSite.subDomain} </h1>
            </c:when>
            <c:otherwise>
                <h1 class="pull-left">Modules</h1>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<spring:eval expression="T(com.easysoft.ecommerce.controller.SessionUtil).isLoggedIn(pageContext.request, pageContext.response)" var="isLoggedIn"/>
<h:modules clientSiteId="${clientSiteId}" isLoggedIn="${isLoggedIn}"/>
</body>
</html>

<%--</app:cache>--%>