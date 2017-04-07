<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString)" var="queryString"/>
<%--<app:cache key="rewritten_uri:${uri}|${queryString}|page:search.jsp">--%>

<html>
<head>
<title><fmt:message key="search.button"/>: ${fn:escapeXml(command.keyword)}</title>
<meta name="description" content="<fmt:message key="search.button"/>: ${fn:escapeXml(command.keyword)}"/>
<meta name="keywords" content="<fmt:message key="search.button"/>: ${fn:escapeXml(command.keyword)}"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>


<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>
<spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>
<%--This use for pagination--%>
<c:choose>
    <c:when test="${command.maxResult <= 0}">
        <c:set value="18" var="itemPerPage"/>
    </c:when>
    <c:otherwise>
        <c:set value="${command.maxResult}" var="itemPerPage"/>
    </c:otherwise>
</c:choose>
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

    <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getRefinementMap(command.refinement, queryString)" var="refinementMap"/>
    <spring:eval expression="serviceLocator.getSearchService().search(command.keyword, command.categoryId, site, refinementMap, command.startPos, itemPerPage, command.sortField, command.reverse, true)" var="mapResult" />
    <spring:eval expression="mapResult['products']" var="products" />

<div class="breadcrumbs-v4">
    <div class="container">
        <%--<span class="page-name">Product Filter Page</span>--%>
        <h1><fmt:message key="search.result"/></h1>
    </div><!--/end container-->
</div>

<c:choose>
<c:when test="${mapResult.products != null && !empty mapResult.products}">
    <div class="content container">
        <div class="row">
            <div class="col-md-3 filter-by-block md-margin-bottom-60">
                <h:refinement params="${params}" isSearch="Y"/>
            </div>

            <div class="col-md-9">
                <div class="row margin-bottom-5">
                    <div class="col-sm-7 result-category">
                        <h2><fmt:message key="search.result"/></h2>
                        <small class="shop-bg-red badge-results"><fmt:message key="search.found.message">
                            <fmt:param value="${mapResult['count']}"/>
                            <fmt:param value="${fn:escapeXml(command.keyword)}"/>
                        </fmt:message></small>
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
                                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
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
                </div><!--/end result category-->

                <div class="filter-results">
                    <h:productlist products="${mapResult.products}"  parCat="${parCategory}" cat="${category}" subCat="${subCategory}"/>
                </div><!--/end filter resilts-->
                <h:paginationbootstrap3x totalItems="${mapResult.count}" params="${params}" numPerPage="${itemPerPage}" column="3"/>
            </div>
        </div><!--/end row-->
    </div>
</c:when>
<c:otherwise>
    <div class="content container">
        <div class="row">
            <div class="col-md-3 filter-by-block md-margin-bottom-60">
                <h:refinement params="${params}" isSearch="Y"/>
            </div>

            <div class="col-md-9">
                <div class="row margin-bottom-5">
                    <div class="col-sm-12 result-category">
                        <h2><fmt:message key="search.result"/></h2>
                        <small class="shop-bg-red badge-results"><fmt:message key="search.result"/>
                               <fmt:message key="search.notfound.message">
                                   <fmt:param value="${fn:escapeXml(command.keyword)}"/>
                               </fmt:message></small>
                       </div>
                   </div>
                <form action="/search.html" method="get" name="searchForm">
                    <div class="search-open">
                        <div class="input-group animated fadeInDown">
                            <input type="text" class="form-control" placeholder="Search" name="keyword" maxlength="20">
                                    <span class="input-group-btn">
                                        <button class="btn-u" type="submit">Go</button>
                                    </span>
                        </div>
                    </div>
                </form>

            </div>
           </div>
       </div>
   </c:otherwise>
   </c:choose>






   <%--<div class="leftnav">--%>
    <%--<h:leftnav params="${params}" prodIds="${searchResult['productIds']}"/>--%>
<%--</div>--%>

<%--<div class='page-body body-with-border page-body-float-left'>--%>


<%--<div id="thumbnail-height">--%>

    <%--<div class="catalog-category-breadcrumb">--%>
        <%--<h:breadcrumb keyword="${command.keyword}"/>--%>
    <%--</div>--%>

    <%--<div class="clr"></div>--%>

    <%--<c:if test="${products != null && !empty products}">--%>

    <%--<div class="cat-thu-pagnation">--%>
        <%--<div class="cat-glo-pagnation glo-form">--%>
        <%--<h1><fmt:message key="search.result"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
            <%--<span><fmt:message key="search.found.message">--%>
                <%--<fmt:param value="${searchResult['count']}"/>--%>
                <%--<fmt:param value="${fn:escapeXml(command.keyword)}"/>--%>
            <%--</fmt:message></span></h1>--%>
        <%--<table class="sortOptionsTable">--%>
            <%--<tbody><tr>--%>
                <%--&lt;%&ndash;This used for sort&ndash;%&gt;--%>
                <%--<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>--%>
                <%--&lt;%&ndash;update current page base on filter&ndash;%&gt;--%>
                <%--<c:if test="${command.page <= 1}">--%>
                <%--<spring:eval expression="params.exclude('page')" var="params"/>--%>
                <%--</c:if>--%>

                <%--<td><label for="sortItemsForm"><fmt:message key="search.sort.by"/></label></td>--%>
                <%--<td>--%>
                    <%--<form name="sortItemsForm" method="get" action="/" id="sortItemsForm">--%>
                        <%--<select name="sortItems" id="searchResultsSort">--%>
                            <%--<spring:eval expression="params.exclude('option').exclude('sortField').exclude('reverse').toString()" var="query"/>--%>
                            <%--<option value="${browseruri}?${query}"><fmt:message key="search.select"/></option>--%>
                            <%--<spring:eval expression="params.add('sortField','price').add('reverse','false').add('option','sort-price-false').toString()" var="query"/>--%>
                            <%--<option value="${browseruri}?${query}"><fmt:message key="search.price.low.to.high"/></option>--%>
                            <%--<spring:eval expression="params.add('sortField','price').add('reverse','true').add('option','sort-price-true').toString()" var="query"/>--%>
                            <%--<option value="${browseruri}?${query}"><fmt:message key="search.price.high.to.low"/></option>--%>
                        <%--</select>--%>
                    <%--</form>--%>
                <%--</td>--%>
            <%--</tr>--%>
        <%--</tbody></table>--%>

            <%--<script type="text/javascript">--%>
                <%--var myselect = document.getElementById("searchResultsSort")--%>
                <%--var option = '${fn:escapeXml(param.option)}';--%>
                <%--myselect.options[0].selected = true;--%>
                <%--for (var i=0; i<myselect.options.length; i++){--%>
                    <%--if (option != "" && myselect.options[i].value.indexOf('${fn:escapeXml(param.option)}') >= 0){--%>
                       <%--myselect.options[i].selected = true;--%>
                    <%--}--%>
                <%--}--%>
            <%--</script>--%>

            <%--<h:pagination totalItems="${searchResult['count']}" params="${queryString}"/>--%>

            <%--<div class="glo-clr"><!--  --></div>--%>

        <%--</div>--%>

    <%--</div>--%>

    <%--<div class="subcategory-height"></div>--%>

    <%--<div class="clr"></div>--%>

    <%--<h:productlist products="${products}"/>--%>

    <%--<div class="clr"></div>--%>

    <%--<div class="cat-thu-pagnation">--%>
    <%--<div class="cat-glo-pagnation glo-form">--%>

        <%--<h:pagination totalItems="${searchResult['count']}" params="${queryString}"/>--%>

        <%--<div class="glo-clr"><!--  --></div>--%>
    <%--</div>--%>
<%--</div>--%>

    <%--<div class="clr"></div>--%>

    <%--</c:if>--%>

    <%--<c:if test="${empty products}">--%>
        <%--<div class="cat-thu-pagnation">--%>
            <%--<div class="cat-glo-pagnation glo-form">--%>
            <%--<h1><fmt:message key="search.result"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
                <%--<span><fmt:message key="search.notfound.message">--%>
                    <%--<fmt:param value="${fn:escapeXml(command.keyword)}"/>--%>
                <%--</fmt:message></span></h1>--%>

                <%--<div class="glo-clr"><!--  --></div>--%>

            <%--</div>--%>

        <%--</div>--%>
    <%--</c:if>--%>
<%--</div>--%>
<%--</div>--%>
</body>
</html>
<%--</app:cache>--%>