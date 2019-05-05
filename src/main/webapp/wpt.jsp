<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}">
<c:set value="${fn:replace(uri,'/content/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'index.html','')}" var="uri"/>

<html>
<head>
    <title>${site.name}</title>
    <meta name="description" content="${site.description}"/>
    <meta name="keywords" content=""/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>

<spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(uri, site, 'Y')" var="rows"/>
<c:if test="${! empty rows}">
    <c:forEach var="row" items="${rows}">
        <spring:eval expression="serviceLocator.getContentService().merge(row)" var="htmlContent"/>
        ${htmlContent}
    </c:forEach>
</c:if>
</app:cache>
<spring:eval expression="serviceLocator.templateDao.findActiveByOrder('site.id', site.id, 'sequence')" var="templates"/>
<c:if test="${fn:length(templates) > 1}">
    <div class="container content">
        <div class="row">
            <div class="col-md-12">
                <!-- Thumbnails v1 -->
                <h:sitetemplates column="4"/>
            </div>
        </div>
    </div>
</c:if>
</body>
</html>
