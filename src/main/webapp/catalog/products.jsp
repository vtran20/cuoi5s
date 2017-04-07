<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}|${pageContext.request.queryString}">
    <spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString)"
                 var="params"/>
    <spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>
    <html>
    <head>
        <title>Sản Phẩm</title>
        <meta name="description" content=""/>
        <meta name="keywords" content=""/>
        <meta name="decorator" content="${template.templateCode}"/>
    </head>

    <body>
    <c:set value="18" var="itemPerPage"/>
    <c:set value="${param.page}" var="start"/>
    <c:if test="${empty start}">
        <c:set value="0" var="start"/>
    </c:if>
    <c:if test="${start > 0}">
        <c:set value="${(start-1) * itemPerPage}" var="start"/>
    </c:if>

    <c:set value="${param.sortField}" var="sortField"/>
    <c:if test="${empty sortField}">
        <c:set value="" var="sortField"/>
    </c:if>
    <c:set value="${param.reverse}" var="reverse"/>
    <c:if test="${empty reverse}">
        <c:set value="false" var="reverse"/>
    </c:if>

    <div class="breadcrumbs-v4">
        <div class="container">
                <%--<span class="page-name">Product Filter Page</span>--%>
            <h1>Sản Phẩm</h1>
        </div>
        <!--/end container-->
    </div>
    <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getRefinementMap(command.refinement, params)" var="refinementMap"/>
    <spring:eval expression="serviceLocator.getSearchService().getAllProducts(null, site, refinementMap, start, itemPerPage, sortField, reverse, true)" var="mapResult"/>
    <c:choose>
        <c:when test="${mapResult.products != null && !empty mapResult.products}">
            <div class="content container">
                <div class="row">
                    <div class="col-md-3 filter-by-block md-margin-bottom-60">
                        <h:refinement params="${params}"/>
                    </div>

                    <div class="col-md-9">
                        <div class="row margin-bottom-5">
                            <div class="col-sm-7 result-category">
                                <h2>Sản Phẩm</h2>
                                <small class="shop-bg-red badge-results">${mapResult.count} <fmt:message
                                        key="category.product"/></small>
                            </div>
                            <div class="col-sm-5">
                                <ul class="list-inline clear-both">
                                        <%--<li class="grid-list-icons">--%>
                                        <%--<a href="shop-ui-filter-list.html"><i class="fa fa-th-list"></i></a>--%>
                                        <%--<a href="shop-ui-filter-grid.html"><i class="fa fa-th"></i></a>--%>
                                        <%--</li>--%>
                                    <spring:eval expression="params.exclude('option').exclude('sortField').exclude('reverse').toString()" var="query"/>
                                    <spring:eval expression="params.add('sortField','price').add('reverse','false').add('option','sort-price-false').toString()" var="query1"/>
                                    <spring:eval expression="params.add('sortField','price').add('reverse','true').add('option','sort-price-true').toString()" var="query2"/>
                                    <li class="sort-list-btn">
                                        <h3><fmt:message key="category.sort.by"/></h3>
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-default dropdown-toggle"
                                                    data-toggle="dropdown">
                                                <c:choose>
                                                    <c:when test="${fn:contains(pageContext.request.queryString, 'sort-price-false')}">
                                                        <fmt:message key="category.price.low.to.high"/>
                                                    </c:when>
                                                    <c:when test="${fn:contains(pageContext.request.queryString, 'sort-price-true')}">
                                                        <fmt:message key="category.price.high.to.low"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:message key="category.select"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <span class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="${browseruri}?${query}"><fmt:message key="category.select"/></a></li>
                                                <li><a href="${browseruri}?${query1}"><fmt:message key="category.price.low.to.high"/></a></li>
                                                <li><a href="${browseruri}?${query2}"><fmt:message key="category.price.high.to.low"/></a></li>
                                            </ul>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <!--/end result category-->

                        <div class="filter-results">
                                <h:productlist products="${mapResult.products}"/>
                        </div>
                        <!--/end filter resilts-->
                            <h:paginationbootstrap3x totalItems="${mapResult.count}" params="${params}" numPerPage="${itemPerPage}" column="3"/>
                    </div>
                </div>
                <!--/end row-->
            </div>
        </c:when>
        <c:otherwise>
            <div class="content container">
                <div class="row">
                    <div class="col-md-3 filter-by-block md-margin-bottom-60">
                            <h:refinement params="${params}"/>
                    </div>

                    <div class="col-md-9">
                        <div class="row margin-bottom-5">
                            <div class="col-sm-12 result-category">
                                <h2>Sản Phẩm</h2>
                                <small class="shop-bg-red badge-results">${mapResult.count} <fmt:message
                                        key="category.product"/></small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    </body>
    </html>

</app:cache>