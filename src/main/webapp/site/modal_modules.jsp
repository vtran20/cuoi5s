<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<%--<app:cache key="${uri}|${pageContext.request.queryString}">--%>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString)" var="params"/>
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
<spring:eval expression="T(com.easysoft.ecommerce.controller.SessionUtil).isLoggedIn(pageContext.request, pageContext.response)" var="isLoggedIn"/>
<h:modules clientSiteId="${clientSiteId}" isLoggedIn="${isLoggedIn}" popup="Y"/>
