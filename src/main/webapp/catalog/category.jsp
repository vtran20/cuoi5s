<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>

<app:cache key="${uri}|${pageContext.request.queryString}">
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>

<c:set value="${fn:replace(uri,'/category/','')}" var="lastid"/>
<c:set value="${fn:replace(lastid,'/index.html','')}" var="lastid"/>
<c:set value="${fn:split(lastid,'/')}" var="lastids"/>
<c:set value="${lastids[0]}" var="categoryuri"/>

<c:if test="${! empty categoryuri}">
    <c:set value="${fn:split(categoryuri,'-')}" var="list"/>
    <c:set value="${list[fn:length(list)-1]}" var="categoryId"/>
    <spring:eval expression="serviceLocator.getCategoryDao().findById(T(java.lang.Long).valueOf(categoryId))" var="category"/>
    <%--TODO: how to get correct catalog from category--%>
    <spring:eval expression="serviceLocator.getCatalogDao().getCatalogFromCategory(category.id, true)" var="catalogs"/>
    <c:if test="${fn:length(catalogs) > 0}">
        <spring:eval expression="catalogs.get(0)" var="catalog"/>
    </c:if>
</c:if>

<html>
<head>
    <title>${category.name}</title>
    <meta name="description" content="${category.metaDescription}"/>
    <meta name="keywords" content="${category.metakeyword}"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<div class="leftnav">
    <%--<h:leftnav params="${params}" catalog="${catalog}" cat="${category}"/>--%>
</div>
<div class='page-body body-with-border page-body-float-left'>
<div id="thumbnail-height">

<spring:eval expression="serviceLocator.getProductDao().countProductBySubCategory(category.id)" var="prodOfCategory" />
<%--Category doesn't have product--%>
<c:if test="${prodOfCategory <= 0}">

    <script  type="text/javascript">
        $(function() {

            //hover on thumbnail image on category
            $(".cat-thu-product-all").hover(function () {
                $(this).addClass("hover-dark");
            }, function () {
                $(this).removeClass("hover-dark");
            });

        });

    </script>

    <div class="catalog-category-breadcrumb">
        <h:breadcrumb parCat="${catalog}" cat="${category}"/>
    </div>
    <div class="clr"></div>

    <spring:eval expression="serviceLocator.getCategoryService().getSubCategories(category.id)" var="subcategories" />

    <c:forEach items="${subcategories}" var="subCategory">
        <spring:eval expression="serviceLocator.getSearchService().getProductsBySubCategoryUsingLucene(subCategory.id.toString(), site, null, 0, 4, '', true)" var="mapResult" />
        <c:if test="${mapResult.products != null && !empty mapResult.products}">
            <div class="cat-cat-subcat-pag">
                <c:set value="${fn:replace(uri,'index.html', '')}" var="url"/>
                <c:set value="${url}${subCategory.uri}-${subCategory.id}" var="url"/>
                <c:url value="${url}/index.html" var="url"/>
                <span><a href="${url}"><c:out value="${subCategory.name}" escapeXml="true"/> </a></span>
            </div>

            <div class="clr"></div>

            <div class="cat-thu-row cat-thu-row-all">
                <c:forEach items="${mapResult.products}" var="product" >
                    <h:productthumbnail product="${product}" parCat="${catalog}" cat="${category}" subCat="${subCategory}"/>
                </c:forEach>
                <div class="glo-clr"><!--  --></div>

            </div>

            <div class="clr"></div>
        </c:if>
    </c:forEach>

    <div class="clr"></div>

</c:if>
<%--Category has products--%>
<c:if test="${prodOfCategory > 0}">
    <spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>
    <spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString)" var="queryString"/>
    <div class="catalog-category-breadcrumb">
        <h:breadcrumb parCat="${catalog}" cat="${category}"/>
    </div>

    <div class="clr"></div>

    <c:set value="${param.page}" var="start"/>
    <c:if test="${empty start}">
        <c:set value="0" var="start"/>
    </c:if>
    <c:if test="${start > 0}">
        <c:set value="${(start-1) * 20}" var="start"/>
    </c:if>

    <c:set value="${param.sortField}" var="sortField"/>
    <c:if test="${empty sortField}">
        <c:set value="" var="sortField"/>
    </c:if>
    <c:set value="${param.reverse}" var="reverse"/>
    <c:if test="${empty reverse}">
        <c:set value="false" var="reverse"/>
    </c:if>
    <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getRefinementMap(command.refinement, params)" var="refinementMap"/>
    <spring:eval expression="serviceLocator.getSearchService().getProductsBySubCategoryUsingLucene(category.id.toString(), site, refinementMap, start, 20, sortField, reverse)" var="mapResult" />
    <c:if test="${mapResult.products != null && !empty mapResult.products}">

    <div class="cat-thu-pagnation">
        <div class="cat-glo-pagnation glo-form">
        <h1><c:out value="${category.name}" escapeXml="true"/></h1>
        <table class="sortOptionsTable">
            <tbody><tr>
                <td><label for="sortItems"><fmt:message key="category.sort.by"/></label></td>
                <td>
                    <form name="sortItemsForm" method="get" action="/" id="sortItemsForm">
                        <select name="sortItems" id="searchResultsSort">
                            <spring:eval expression="params.exclude('option').exclude('sortField').exclude('reverse').toString()" var="query"/>
                            <option value="${browseruri}?${query}"><fmt:message key="category.select"/></option>
                            <spring:eval expression="params.add('sortField','price').add('reverse','false').add('option','sort-price-false').toString()" var="query"/>
                            <option value="${browseruri}?${query}"><fmt:message key="category.price.low.to.high"/></option>
                            <spring:eval expression="params.add('sortField','price').add('reverse','true').add('option','sort-price-true').toString()" var="query"/>
                            <option value="${browseruri}?${query}"><fmt:message key="category.price.high.to.low"/></option>
                        </select>
                    </form>
                </td>
            </tr>
        </tbody></table>

            <script type="text/javascript">

                var myselect = document.getElementById("searchResultsSort");
                var option = '${param.option}';
                myselect.options[0].selected = true;
                for (var i=0; i<myselect.options.length; i++){
                    if (option != "" && myselect.options[i].value.indexOf('${param.option}') >= 0){
                       myselect.options[i].selected = true;
                    }
                }
            </script>

            <%--<spring:eval expression="serviceLocator.getProductDao().countProductBySubCategory(catId3, systemContext.getSite())" var="totalProducs" />--%>

            <h:pagination totalItems="${mapResult.count}" params="${queryString}"/>

            <div class="glo-clr"><!--  --></div>

        </div>

    </div>

    <div class="subcategory-height"></div>

    <div class="clr"></div>

    <h:productlist products="${mapResult.products}"  parCat="${catalog}" cat="${category}"/>

    <div class="clr"></div>

    <div class="cat-thu-pagnation">
    <div class="cat-glo-pagnation glo-form">

        <h:pagination totalItems="${mapResult.count}" params="${queryString}"/>

        <div class="glo-clr"><!--  --></div>
    </div>
</div>

    <div class="clr"></div>

    </c:if>

</c:if>
</div>
</div>
</body>
</html>
</app:cache>