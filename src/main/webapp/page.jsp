<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<c:set value="${fn:replace(uri,'/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'index.html','')}" var="uri"/>
<c:set value="${fn:replace(uri,'index.wpt','')}" var="uri"/>
<%--<sec:authorize ifNotGranted="ROLE_SITE_CONTENT_ADMIN">--%>
<app:cache key="${uri}">
    <spring:eval expression="serviceLocator.getPageDao().getPageByMenuURI(site, uri, 'true')" var="page"/>
    <c:if test="${empty page}">
        <spring:eval expression="serviceLocator.getPageDao().getPageByURI(site, uri, 'true')" var="page"/>
    </c:if>
    <html>
<head>
    <title><c:out value="${page.title}"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <%--<meta property="og:site_name" content="cuoi5s.com">--%>
    <%--<meta property="og:url" content="http://${site.domain}/${page.uri}">--%>
    <%--<meta property="og:title" content="<c:out value="${page.title}"/>">--%>
    <%--<meta property="og:image" content="http://gazztoday.com/wp-content/uploads/2014/04/fifa-world-cup-wallpaper-hd.jpg">--%>
    <%--<meta property="og:description" content="Share để mọi người cùng xem. Xem thêm tại http://${site.domain}">--%>
    <%--<meta content="article" property="og:type"/>--%>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<%--User--%>

    <c:choose>
        <c:when test="${! empty page}">
            <c:set value="${page.content}" var="content"/>
            <c:choose>
                <c:when test="${! empty content}">
                    ${content}
                </c:when>
                <c:otherwise>
                    <section>
                        <div class="container"><fmt:message key="page.content.is.updating"/></div>
                    </section>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <section>
                <div class="container"><fmt:message key="page.content.is.updating"/></div>
            </section>
        </c:otherwise>
    </c:choose>
</body>
</html>
</app:cache>
<%--</sec:authorize>--%>


    <%--Admin Design--%>
<%--<sec:authorize ifAnyGranted="ROLE_SITE_CONTENT_ADMIN">--%>
        <%--<html>--%>
        <%--<head>--%>
            <%--<title>${site.description}</title>--%>
            <%--<meta name="description" content=""/>--%>
            <%--<meta name="keywords" content=""/>--%>
            <%--<spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>--%>
            <%--<meta name="decorator" content="${template.templateCode}"/>--%>
        <%--</head>--%>

        <%--<body>--%>
        <%--<spring:eval expression="serviceLocator.getPageDao().getPageByMenuURI(site, uri, 'false')" var="page"/>--%>
            <%--<c:if test="${empty page}">--%>
                <%--<spring:eval expression="serviceLocator.getPageDao().getPageByURI(site, uri, 'false')" var="page"/>--%>
            <%--</c:if>--%>
            <%--<c:choose>--%>
                <%--<c:when test="${! empty page}">--%>
                    <%--<c:set value="${page.preContent}" var="content"/>--%>
                    <%--<c:choose>--%>
                        <%--<c:when test="${! empty content}">--%>
                            <%--${content}--%>
                        <%--</c:when>--%>
                        <%--<c:otherwise>--%>
                            <%--<section>--%>
                                <%--<div class="container"><fmt:message key="page.content.is.updating"/></div>--%>
                            <%--</section>--%>
                        <%--</c:otherwise>--%>
                    <%--</c:choose>--%>
                <%--</c:when>--%>
                <%--<c:otherwise>--%>
                    <%--<section>--%>
                        <%--<div class="container"><fmt:message key="page.content.is.updating"/></div>--%>
                    <%--</section>--%>
                <%--</c:otherwise>--%>
            <%--</c:choose>--%>

        <%--</body>--%>
        <%--</html>--%>
<%--</sec:authorize>--%>