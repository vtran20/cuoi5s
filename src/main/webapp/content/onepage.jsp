<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<%--<c:if test="${fn:contains(param.type,'preview')}"></c:if>--%>
<app:cache key="${uri}">
<c:set value="${fn:replace(uri,'/content/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'index.html','')}" var="uri"/>
    <spring:eval expression="serviceLocator.getMenuDao().getMenu(site, uri, 'true')" var="menu"/>
    <html>
    <head>
        <title><c:out value="${menu.name}"/></title>
        <meta name="description" content="<c:out value="${menu.name} | ${menu.description}"/>"/>
        <meta name="keywords" content=""/>
        <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
        <meta name="decorator" content="${template.templateCode}"/>
    </head>

    <body>
    <c:if test="${menu.displayBreadcrumb == 'Y'}">
        <div class="breadcrumbs">
            <div class="container">
                <h1 class="pull-left">${menu.name}</h1>
            </div>
        </div>
    </c:if>
    <spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(uri, site, 'Y')" var="rows"/>
        <c:if test="${! empty rows}">
            <c:forEach var="row" items="${rows}">
                <spring:eval expression="serviceLocator.getContentService().merge(row)" var="htmlContent"/>
                ${htmlContent}
            </c:forEach>
        </c:if>
    </body>
    </html>
</app:cache>
