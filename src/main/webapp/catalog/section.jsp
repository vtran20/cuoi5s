<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}|${pageContext.request.queryString}">
<c:set value="${fn:replace(uri,'/catalog/','')}" var="lastid"/>
<c:set value="${fn:split(lastid,'/')}" var="lastids"/>
<c:set value="${lastids[0]}" var="cataloguri"/>
<c:if test="${! empty cataloguri}">
    <c:set value="${fn:split(cataloguri,'-')}" var="list"/>
    <c:set value="${list[fn:length(list)-1]}" var="catalogId"/>
    <spring:eval expression="serviceLocator.getCatalogDao().findById(T(java.lang.Long).valueOf(catalogId))" var="catalog"/>
</c:if>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>

<html>
<head>
<title>${catalog.name}</title>
<meta name="description" content="${catalog.description}"/>
<meta name="keywords" content="${catalog.description}"/>
<meta name="decorator" content="no_leftnav" />
</head>

<body>

<div class="leftnav">
    <h:leftnav params="${params}" parCat="${catalog}"/>
</div>

<div class='page-body body-with-border page-body-float-left'>

<div id="thumbnail-height">

            <div class="catalog-category-breadcrumb">

                    <h:breadcrumb parCat="${catalog}" />

            </div>
            <div class="catalog-thumbnail">
                <!--Start CMS content-->
                            <%--<h:cmsdynamiccontent name="section Page 1"/>--%>
                <spring:eval expression="serviceLocator.cmsAreaContentDao.getCmsAreaDynamicContent(systemContext.site, 'section Page 1', T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI())" var="cmsContent" />
                <c:if test="${cmsContent == null or empty cmsContent}">

                    <spring:eval expression="serviceLocator.getCategoryDao().getAllCategoriesFromCatalog(catalog.id, true)" var="categories"/>
                    <c:if test="${fn:length(categories) > 0}">
                        <c:set var="category" value="${categories[0]}"/>
                        <c:set var="url" value="/category/${category.uri}-${category.id}/index.html"/>
                        <c:redirect url="${url}"/>
                    </c:if>

                </c:if>

                <!--End CMS content-->
                <div class="entity-thumbnail-container">
                    <div class="ql-thumbnail">

                    </div>
                </div>
                <div class="clr"></div>
            </div>
        </div>
    </div>
</body>
</html>
</app:cache>