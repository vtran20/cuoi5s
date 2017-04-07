<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<c:set value="${fn:replace(uri,'/admin/page/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'index.html','')}" var="uri"/>
<c:set value="${fn:replace(uri,'index.wpt','')}" var="uri"/>
<html>
<head>
    <title>${site.description}</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<c:choose>
    <c:when test="${! empty uri}">
        <spring:eval expression="serviceLocator.getPageDao().getPageByMenuURI(site, uri, 'true')" var="page"/>
    </c:when>
    <c:otherwise>
        <spring:eval expression="serviceLocator.getPageDao().getHomePage(site, 'true')" var="page"/>
    </c:otherwise>
</c:choose>
<c:if test="${! empty page}">
    <c:set value="${page.content}" var="content"/>
</c:if>

    <%--Using this page object if it is admin mode--%>
<sec:authorize url="/admin/page/index.html">
    <c:choose>
        <c:when test="${! empty uri}">
            <spring:eval expression="serviceLocator.getPageDao().getPageByMenuURI(site, uri, 'false')" var="page"/>
        </c:when>
        <c:otherwise>
            <spring:eval expression="serviceLocator.getPageDao().getHomePage(site, 'false')" var="page"/>
        </c:otherwise>
    </c:choose>
    <c:if test="${! empty page}">
        <c:set value="${page.preContent}" var="content"/>
    </c:if>
</sec:authorize>

<c:choose>
    <c:when test="${! empty page}">
        <c:choose>
            <c:when test="${! empty content}">
                ${content}
            </c:when>
            <c:otherwise>
                Thiết kế giao diện tại đây.
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        Thiết kế giao diện tại đây.
    </c:otherwise>
</c:choose>

</body>
</html>
