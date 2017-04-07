<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%--<app:cache key="headermenu">--%>

<div class="" id="topnav">
    <ul class="clearfix">
        <spring:eval expression="serviceLocator.getCatalogDao().getAllCatalogsBySite(systemContext.site)"
                     var="catalogs"/>
        <c:forEach items="${catalogs}" var="catalog">
            <spring:eval expression="serviceLocator.getCategoryDao().getRootCategoriesByCatalog(catalog.id)"
                         var="categories"/>
            <c:if test="${fn:length(categories) > 0}">
                <c:url var="url" value="/catalog/${catalog.uri}-${catalog.id}/index.html"/>

                <li class="tab">
                    <div class="left"></div>
                    <ul>
                        <li class="primary">
                            <h3><a href="${url}">${catalog.name}</a></h3>

                            <div class="menubar" style="display: none;">
                                <div class="menu clearfix cols-1" style="left: 131px;">
                                    <ul class="column last">

                                        <c:forEach items="${categories}" varStatus="category">
                                            <spring:eval
                                                    expression="serviceLocator.getCategoryService().getSubCategories(category.current.id)"
                                                    var="subcategories"/>
                                            <c:if test="${fn:length(subcategories) > 0}">
                                                <c:url var="url1"
                                                       value="/category/${category.current.uri}-${category.current.id}/index.html"/>
                                                <li class="sub-heading ${category.index == 0?"first":""}"><h4><a
                                                        href="${url1}">${category.current.name}</a></h4></li>
                                                <c:forEach items="${subcategories}" var="subCategory">
                                                    <c:url var="url2"
                                                           value="/category/${category.current.uri}-${category.current.id}/${subCategory.uri}-${subCategory.id}/index.html"/>
                                                    <li><a href="${url2}" name="">${subCategory.name}</a></li>
                                                </c:forEach>
                                            </c:if>

                                            <%--If category doesn't has subcateogry. Check to see it has products or not--%>
                                            <c:if test="${fn:length(subcategories) <= 0}">
                                                <spring:eval
                                                        expression="serviceLocator.getProductDao().countProductBySubCategory(category.current.id)"
                                                        var="prodCount"/>
                                                <%--If have products, show subcategory. If not, don't show--%>
                                                <c:if test="${prodCount > 0}">
                                                    <c:url var="url1"
                                                           value="/category/${category.current.uri}-${category.current.id}/index.html"/>
                                                    <li class="sub-heading ${category.index == 0?"first":""}"><h4><a
                                                            href="${url1}">${category.current.name}</a></h4></li>
                                                </c:if>
                                            </c:if>

                                        </c:forEach>

                                    </ul>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <div class="right">

                    </div>
                </li>
            </c:if>
        </c:forEach>
        <li>
            <ul>
                <li class="primary">
                    <h3>
                        <button type="submit" name="btnG" onClick="location.href='/giam-gia.html?keyword=discount'"
                                id="discount-button">GIẢM GIÁ
                        </button>
                    </h3>
                </li>
            </ul>
        </li>

    </ul>
</div>
<%--</app:cache>--%>
