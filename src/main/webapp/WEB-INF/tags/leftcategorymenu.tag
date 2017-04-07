<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%--<%@ attribute name="params" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.util.QueryString"%>--%>
<%--<%@ attribute name="prodIds" required="false" rtexprvalue="true" type="java.lang.String"%>--%>
<%--<%@ attribute name="catalog" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Catalog"%>--%>
<%--<%@ attribute name="cat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>--%>
<%--<%@ attribute name="subCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>--%>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>
<%--Left Category menu--%>

    <spring:eval expression="serviceLocator.getCatalogDao().getAllCatalogsBySite(systemContext.site)"
                     var="catalogs"/>
    <c:if test="${fn:length(catalogs) > 0}">
    <ul id="left-menu" class="left-menu">
    <c:forEach items="${catalogs}" var="icatalog">
        <c:url var="url" value="/catalog/${icatalog.uri}-${icatalog.id}/index.html"/>
        <li>
            <a class="m${icatalog.id}" href="${url}">${icatalog.name}</a>
            <spring:eval expression="serviceLocator.getCategoryDao().getRootCategoriesByCatalog(icatalog.id)" var="categories"/>
            <c:if test="${fn:length(categories) > 0}">
            <ul id="left-menu-${icatalog.id}">
            <c:forEach items="${categories}" var="category">
                <spring:eval expression="serviceLocator.getCategoryDao().findActiveByOrder('parentCategory.id', category.id, 'sequence')" var="subcategories"/>
                <%--If category has subcateogry--%>
                <c:if test="${fn:length(subcategories) > 0}">
                    <c:url var="url1" value="/category/${category.uri}-${category.id}/index.html"/>
                    <li><a href="${url1}">${category.name}</a>
                        <ul>
                            <c:forEach items="${subcategories}" var="subCategory">

                                <spring:eval expression="serviceLocator.getProductDao().countProductBySubCategory(subCategory.id)" var="prodCount"/>
                                <%--If have products, show subcategory--%>
                                <c:if test="${prodCount > 0}">
                                <c:url var="url2"
                                       value="/category/${category.uri}-${category.id}/${subCategory.uri}-${subCategory.id}/index.html"/>
                                <li><a href="${url2}">${subCategory.name}</a></li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if>
                    <%--If category doesn't has subcateogry. Check to see it has products or not--%>
                <c:if test="${fn:length(subcategories) <= 0}">
                    <spring:eval expression="serviceLocator.getProductDao().countProductBySubCategory(category.id)" var="prodCount"/>
                    <%--If have products, show subcategory. If not, don't show--%>
                    <c:if test="${prodCount > 0}">
                        <c:url var="url1" value="/category/${category.uri}-${category.id}/index.html"/>
                        <li><a href="${url1}">${category.name}</a></li>
                    </c:if>
                </c:if>

            </c:forEach>
            </ul>
            </c:if>
        </li>

        </c:forEach>
    </ul>
    </c:if>

    <br>
