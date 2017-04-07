<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString)" var="queryString"/>

<%--<app:cache key="rewritten_uri:${uri}|${queryString}|page:search.jsp">--%>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>

<spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getRefinementMap(command.refinement, queryString)" var="refinementMap"/>
<spring:eval expression="serviceLocator.getSearchService().getAutoCompleteKeywords(String keyword, Site site, int startPos, int maxResult)(command.keyword, command.categoryId, site, refinementMap, command.startPos, command.maxResult, command.sortField, command.reverse, true)" var="searchResult" />
<spring:eval expression="searchResult['products']" var="products" />

<div class="leftnav">
    <h:leftnav params="${params}" prodIds="${searchResult['productIds']}"/>
</div>

<div class='page-body body-with-border page-body-float-left'>


<div id="thumbnail-height">

    <div class="catalog-category-breadcrumb">
        <h:breadcrumb keyword="${command.keyword}"/>
    </div>

    <div class="clr"></div>

    <c:if test="${products != null && !empty products}">

    <div class="cat-thu-pagnation">
        <div class="cat-glo-pagnation glo-form">
        <h1><fmt:message key="search.result"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span><fmt:message key="search.found.message">
                <fmt:param value="${searchResult['count']}"/>
                <fmt:param value="${fn:escapeXml(command.keyword)}"/>
            </fmt:message></span></h1>
        <table class="sortOptionsTable">
            <tbody><tr>
                <%--This used for sort--%>
                <spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>
                <%--update current page base on filter--%>
                <c:if test="${command.page <= 1}">
                <spring:eval expression="params.exclude('page')" var="params"/>
                </c:if>

                <td><label for="sortItemsForm"><fmt:message key="search.sort.by"/></label></td>
                <td>
                    <form name="sortItemsForm" method="get" action="/" id="sortItemsForm">
                        <select name="sortItems" id="searchResultsSort">
                            <spring:eval expression="params.exclude('option').exclude('sortField').exclude('reverse').toString()" var="query"/>
                            <option value="${browseruri}?${query}"><fmt:message key="search.select"/></option>
                            <spring:eval expression="params.add('sortField','price').add('reverse','false').add('option','sort-price-false').toString()" var="query"/>
                            <option value="${browseruri}?${query}"><fmt:message key="search.price.low.to.high"/></option>
                            <spring:eval expression="params.add('sortField','price').add('reverse','true').add('option','sort-price-true').toString()" var="query"/>
                            <option value="${browseruri}?${query}"><fmt:message key="search.price.high.to.low"/></option>
                        </select>
                    </form>
                </td>
            </tr>
        </tbody></table>

            <script type="text/javascript">
                var myselect = document.getElementById("searchResultsSort")
                var option = '${fn:escapeXml(param.option)}';
                myselect.options[0].selected = true;
                for (var i=0; i<myselect.options.length; i++){
                    if (option != "" && myselect.options[i].value.indexOf('${fn:escapeXml(param.option)}') >= 0){
                       myselect.options[i].selected = true;
                    }
                }
            </script>

            <h:pagination totalItems="${searchResult['count']}" params="${queryString}"/>

            <div class="glo-clr"><!--  --></div>

        </div>

    </div>

    <div class="subcategory-height"></div>

    <div class="clr"></div>

    <h:productlist products="${products}"/>

    <div class="clr"></div>

    <div class="cat-thu-pagnation">
    <div class="cat-glo-pagnation glo-form">

        <h:pagination totalItems="${searchResult['count']}" params="${queryString}"/>

        <div class="glo-clr"><!--  --></div>
    </div>
</div>

    <div class="clr"></div>

    </c:if>

    <c:if test="${empty products}">
        <div class="cat-thu-pagnation">
            <div class="cat-glo-pagnation glo-form">
            <h1><fmt:message key="search.result"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span><fmt:message key="search.notfound.message">
                    <fmt:param value="${fn:escapeXml(command.keyword)}"/>
                </fmt:message></span></h1>

                <div class="glo-clr"><!--  --></div>

            </div>

        </div>
    </c:if>
</div>
</div>
</body>
</html>
<%--</app:cache>--%>