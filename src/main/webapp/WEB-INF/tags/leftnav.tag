<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="params" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.util.QueryString"%>
<%@ attribute name="prodIds" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="parCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="cat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="subCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>
<%--Implement leftnav for Session/category/subcategory/product page--%>
<style>
</style>
<%--Search category--%>
<c:if test="${!fn:contains(browseruri, '/search.html') && !fn:contains(browseruri, '/giam-gia.html') }">

    <h:leftcategorymenu/>

</c:if>

<%--Implement Filter finder for SubCategory--%>
<c:if test="${subCat != null && command != null}">
<spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getRefinementMap(command.refinement, params)" var="refinementMap"/>
<spring:eval expression="serviceLocator.getSearchService().countProductsBySubCategoryUsingLucene(subCat.id.toString(), site, refinementMap, true)" var="totalProducs" />
<c:if test="${totalProducs > 0}">
<div class="catalog-nav">
<!-- Load the default catalog for the site -->
<div class="left-nav-head"><fmt:message key="filter.narrow.by"/></div>

<ul class="refinements">

<li>
    <%--update current page base on filter--%>
    <c:if test="${command.page <= 1}">
    <spring:eval expression="params.exclude('page')" var="params"/>
    </c:if>

    <div class="glo-clr"></div>
    <c:set value="${totalProducs}" var="itemsShowed"/>
    <c:if test="${totalProducs >= 20}">
        <c:set value="20" var="itemsShowed"/>
    </c:if>
    <div class="total-items">
        <fmt:message key="filter.show.number.item.in.total">
            <fmt:param value="${itemsShowed}"/>
            <fmt:param value="${totalProducs}"/>
        </fmt:message></div>
            <%--Clear all filters--%>
    <c:if test="${fn:contains(params, 'refinement') || fn:contains(params, 'categoryId')}">
    <div>
        <spring:eval expression="new com.easysoft.ecommerce.util.QueryString('keyword',command.keyword)" var="queryString"/>
        <a href="${browseruri}?${queryString}" class="clear-all"><fmt:message key="filter.clear.all"/></a>
    </div>
    </c:if>
</li>
<!-- pass in common_refine_options as refinementType to get both unique refinements and common ones. -->
<!-- For unique ones only, pass in refine_options as refinementType -->
<!-- Note these are the refinements that are still available. -->

<spring:eval expression="serviceLocator.getRefinementDao().getAllRefinements(site, subCat.id)" var="refinements"/>
<c:if test="${!empty refinements}">

    <spring:eval expression="serviceLocator.getSearchService().countRefinementMap(subCat.id, site, refinementMap, true)" var="refinementsMapCount" />
    <c:forEach var="refinement" items="${refinements}" varStatus="status">
        <c:set var="refinementSelected" value="refinement=${refinement.key.refinementColumn}"/>
        <c:set var="refinementMapCount" value="${refinementsMapCount[refinement.key.refinementColumn]}"/>
    <c:if test="${! empty refinementMapCount}">
    <li class="section">
        <div class="type">
            <a href="#" class="refine-cat">${refinement.key.name}</a>
            <c:if test="${fn:contains(params, refinementSelected)}">
                <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
                <spring:eval expression="gParams.exclude(refinement.key.refinementColumn).exclude('refinement',refinement.key.refinementColumn).toString()" var="query"/>
                <a href="${browseruri}?${query}" class="clear"><fmt:message key="filter.clear"/></a>
            </c:if>
            <div class="glo-clr"></div>
        </div>
        <div class="options">
                <%--Show the selected refinements--%>
                <spring:eval expression="params.getParameter(refinement.key.refinementColumn)" var="refinementValueSelected" />
                <c:if test="${fn:contains(params, refinementSelected)}">
                    <c:forEach items="${refinement.value}" var="refinementValue">
                        <c:if test="${refinementValue.refinementKey == refinementValueSelected}">
                            <span class="selected">
                                ${refinementValue.refinementValue} (${totalProducs})
                            </span>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${!fn:contains(params, refinementSelected)}">
                    <ul>
                    <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
                    <spring:eval expression="gParams.exclude('page')" var="gParams"/>
                    <c:set value="${browseruri}?${gParams}" var="prevUrl"/>
                    <c:forEach items="${refinement.value}" var="refinementValue" varStatus="refValue">
                    <c:if test="${! empty refinementMapCount[refinementValue.refinementKey]}">
                    <li class="refinement">
                        <c:if test="${fn:endsWith(prevUrl, '&') || fn:endsWith(prevUrl, '?')}">
                        <a href="${prevUrl}refinement=${refinement.key.refinementColumn}&${refinement.key.refinementColumn}=${refinementValue.refinementKey}">${refinementValue.refinementValue}(${refinementMapCount[refinementValue.refinementKey]})</a>
                        </c:if>
                        <c:if test="${!fn:endsWith(prevUrl, '&') && !fn:endsWith(prevUrl, '?')}">
                        <a href="${prevUrl}&refinement=${refinement.key.refinementColumn}&${refinement.key.refinementColumn}=${refinementValue.refinementKey}">${refinementValue.refinementValue}(${refinementMapCount[refinementValue.refinementKey]})</a>
                        </c:if>
                    </li>
                    </c:if>
                    </c:forEach>
                    </ul>
                </c:if>

        </div>

    </li>
    </c:if>
    </c:forEach>

</c:if>

</ul>

</div>

</c:if>
</c:if>

<%--Implement Filter finder for category--%>
<c:if test="${cat != null && subCat == null && command != null}">
<spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getRefinementMap(command.refinement, params)" var="refinementMap"/>
<spring:eval expression="serviceLocator.getSearchService().countProductsBySubCategoryUsingLucene(cat.id.toString(), site, refinementMap, true)" var="totalProducs" />
<c:if test="${totalProducs > 0}">
<div class="catalog-nav">
<!-- Load the default catalog for the site -->
<div class="left-nav-head"><fmt:message key="filter.narrow.by"/></div>

<ul class="refinements">

<li>
    <%--update current page base on filter--%>
    <c:if test="${command.page <= 1}">
    <spring:eval expression="params.exclude('page')" var="params"/>
    </c:if>

    <div class="glo-clr"></div>
    <c:set value="${totalProducs}" var="itemsShowed"/>
    <c:if test="${totalProducs >= 20}">
        <c:set value="20" var="itemsShowed"/>
    </c:if>
    <div class="total-items">
        <fmt:message key="filter.show.number.item.in.total">
            <fmt:param value="${itemsShowed}"/>
            <fmt:param value="${totalProducs}"/>
        </fmt:message></div>
            <%--Clear all filters--%>
    <c:if test="${fn:contains(params, 'refinement') || fn:contains(params, 'categoryId')}">
    <div>
        <spring:eval expression="new com.easysoft.ecommerce.util.QueryString('keyword',command.keyword)" var="queryString"/>
        <a href="${browseruri}?${queryString}" class="clear-all"><fmt:message key="filter.clear.all"/></a>
    </div>
    </c:if>
</li>
<!-- pass in common_refine_options as refinementType to get both unique refinements and common ones. -->
<!-- For unique ones only, pass in refine_options as refinementType -->
<!-- Note these are the refinements that are still available. -->

<spring:eval expression="serviceLocator.getRefinementDao().getAllRefinements(site, cat.id)" var="refinements"/>
<c:if test="${!empty refinements}">

    <spring:eval expression="serviceLocator.getSearchService().countRefinementMap(cat.id, site, refinementMap, true)" var="refinementsMapCount" />
    <c:forEach var="refinement" items="${refinements}" varStatus="status">
        <c:set var="refinementSelected" value="refinement=${refinement.key.refinementColumn}"/>
        <c:set var="refinementMapCount" value="${refinementsMapCount[refinement.key.refinementColumn]}"/>
    <c:if test="${! empty refinementMapCount}">
    <li class="section">
        <div class="type">
            <a href="#" class="refine-cat">${refinement.key.name}</a>
            <c:if test="${fn:contains(params, refinementSelected)}">
                <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
                <spring:eval expression="gParams.exclude(refinement.key.refinementColumn).exclude('refinement',refinement.key.refinementColumn).toString()" var="query"/>
                <a href="${browseruri}?${query}" class="clear"><fmt:message key="filter.clear"/></a>
            </c:if>
            <div class="glo-clr"></div>
        </div>
        <div class="options">
                <%--Show the selected refinements--%>
                <spring:eval expression="params.getParameter(refinement.key.refinementColumn)" var="refinementValueSelected" />
                <c:if test="${fn:contains(params, refinementSelected)}">
                    <c:forEach items="${refinement.value}" var="refinementValue">
                        <c:if test="${refinementValue.refinementKey == refinementValueSelected}">
                            <span class="selected">
                                ${refinementValue.refinementValue} (${totalProducs})
                            </span>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${!fn:contains(params, refinementSelected)}">
                    <ul>
                    <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
                    <spring:eval expression="gParams.exclude('page')" var="gParams"/>
                    <c:set value="${browseruri}?${gParams}" var="prevUrl"/>
                    <c:forEach items="${refinement.value}" var="refinementValue" varStatus="refValue">
                    <c:if test="${! empty refinementMapCount[refinementValue.refinementKey]}">
                    <li class="refinement">
                        <c:if test="${fn:endsWith(prevUrl, '&') || fn:endsWith(prevUrl, '?')}">
                        <a href="${prevUrl}refinement=${refinement.key.refinementColumn}&${refinement.key.refinementColumn}=${refinementValue.refinementKey}">${refinementValue.refinementValue}(${refinementMapCount[refinementValue.refinementKey]})</a>
                        </c:if>
                        <c:if test="${!fn:endsWith(prevUrl, '&') && !fn:endsWith(prevUrl, '?')}">
                        <a href="${prevUrl}&refinement=${refinement.key.refinementColumn}&${refinement.key.refinementColumn}=${refinementValue.refinementKey}">${refinementValue.refinementValue}(${refinementMapCount[refinementValue.refinementKey]})</a>
                        </c:if>
                    </li>
                    </c:if>
                    </c:forEach>
                    </ul>
                </c:if>

        </div>

    </li>
    </c:if>
    </c:forEach>

</c:if>

</ul>

</div>

</c:if>
</c:if>


<%--Implement Filter finder for search--%>
<c:if test="${fn:contains(browseruri, '/search.html') || fn:contains(browseruri, '/giam-gia.html')}">
<spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getRefinementMap(command.refinement, params)" var="refinementMap"/>
<spring:eval expression="serviceLocator.getSearchService().count(command.keyword, command.categoryId, site, refinementMap, true)" var="totalProducs" />
<c:if test="${totalProducs > 0}">
<spring:eval expression="serviceLocator.getSearchService().countProductsBySubCategoryMap(command.keyword, site, refinementMap)" var="subcategories"/>
    <%--<spring:eval expression="serviceLocator.getCategoryDao().getCategoriesByProdIds(prodIds)" var="subcategories"/>--%>

<div class="catalog-nav">
<!-- Load the default catalog for the site -->
<div class="left-nav-head"><fmt:message key="filter.narrow.by"/></div>

<ul class="refinements">

<li>
    <%--update current page base on filter--%>
    <c:if test="${command.page <= 1}">
    <spring:eval expression="params.exclude('page')" var="params"/>
    </c:if>

    <div class="glo-clr"></div>
    <c:set value="${totalProducs}" var="itemsShowed"/>
    <c:if test="${totalProducs >= 20}">
        <c:set value="20" var="itemsShowed"/>
    </c:if>
    <div class="total-items">
        <fmt:message key="filter.show.number.item.in.total">
            <fmt:param value="${itemsShowed}"/>
            <fmt:param value="${totalProducs}"/>
        </fmt:message></div>
    <%--Clear all filters--%>
    <c:if test="${fn:contains(params, 'refinement') || fn:contains(params, 'categoryId')}">
    <div>
        <spring:eval expression="new com.easysoft.ecommerce.util.QueryString('keyword',command.keyword)" var="queryString"/>
        <a href="${browseruri}?${queryString}" class="clear-all"><fmt:message key="filter.clear.all"/></a>
    </div>
    </c:if>
</li>
<c:if test="${fn:length(subcategories) > 0}">

    <li class="section">
    <div class="type">
        <a href="#" class="refine-cat"><fmt:message key="filter.category"/></a>
        <c:if test="${fn:contains(params, 'categoryId')}">
            <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
            <spring:eval expression="gParams.exclude('categoryId').toString()" var="query"/>
            <a href="${browseruri}?${query}" class="clear"><fmt:message key="filter.clear"/></a>
        </c:if>
        <div class="glo-clr"></div>
    </div>
    <div class="options">
        <ul>
            <spring:eval expression="params.getParameter('categoryId')" var="categoryId"/>
            <c:if test="${!empty categoryId}">
            <c:forEach items="${subcategories}" var="cat">
                <c:if test="${cat[0].id == categoryId}">
                    <span class="selected">${cat[0].name} (${cat[1]})</span>
                </c:if>
            </c:forEach>
            </c:if>
            <c:if test="${empty categoryId}">
            <c:forEach items="${subcategories}" var="cat">
            <li class="">
                <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
                <spring:eval expression="gParams.exclude('page')" var="gParams"/>
                <c:set value="${browseruri}?${gParams}" var="prevUrl"/>
                <spring:eval expression="gParams.addAndKeepOrgParam('categoryId',cat[0].id).toString()" var="query"/>
                <c:set value="categoryId=${cat[0].id}" var="catString"/>
                <%--<c:if test="${fn:contains(prevUrl, catString)}">--%>
                <%--<span class="selected">${cat[0].name} (${cat[1]})</span>--%>
                <%--</c:if>--%>
                <c:if test="${!fn:contains(prevUrl, catString)}">
                    <a href="${browseruri}?${query}">${cat[0].name} (${cat[1]})</a>
                </c:if>
            </li>
            </c:forEach>
            </c:if>
        </ul>
    </div>
</li>
</c:if>
<!-- pass in common_refine_options as refinementType to get both unique refinements and common ones. -->
<!-- For unique ones only, pass in refine_options as refinementType -->
<!-- Note these are the refinements that are still available. -->

<spring:eval expression="serviceLocator.getRefinementDao().getRefinements(site)" var="refinements"/>
<c:if test="${!empty refinements}">
    <spring:eval expression="serviceLocator.getSearchService().countRefinementMap(command.keyword, command.categoryId, site, refinementMap, true)" var="refinementsMapCount" />

<c:forEach var="refinement" items="${refinements}" varStatus="status">
    <c:set var="refinementSelected" value="refinement=${refinement.key.refinementColumn}"/>
    <c:set var="refinementMapCount" value="${refinementsMapCount[refinement.key.refinementColumn]}"/>
<c:if test="${! empty refinementMapCount}">
<li class="section">
    <div class="type">
        <a href="#" class="refine-cat">${refinement.key.name}</a>
        <c:if test="${fn:contains(params, refinementSelected)}">
            <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
            <spring:eval expression="gParams.exclude(refinement.key.refinementColumn).exclude('refinement',refinement.key.refinementColumn).toString()" var="query"/>
            <a href="${browseruri}?${query}" class="clear"><fmt:message key="filter.clear"/></a>
        </c:if>
        <div class="glo-clr"></div>
    </div>
    <div class="options">
            <%--Show the selected refinements--%>
            <spring:eval expression="params.getParameter(refinement.key.refinementColumn)" var="refinementValueSelected" />
            <c:if test="${fn:contains(params, refinementSelected)}">
                <c:forEach items="${refinement.value}" var="refinementValue">
                    <c:if test="${refinementValue.refinementKey == refinementValueSelected}">
                        <span class="selected">
                            ${refinementValue.refinementValue} (${totalProducs})
                        </span>
                    </c:if>
                </c:forEach>
            </c:if>
            <c:if test="${!fn:contains(params, refinementSelected)}">
            <ul>
                <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
                <spring:eval expression="gParams.exclude('page')" var="gParams"/>
                <c:set value="${browseruri}?${gParams}" var="prevUrl"/>
                <c:forEach items="${refinement.value}" var="refinementValue" varStatus="refValue">
                <c:if test="${! empty refinementMapCount[refinementValue.refinementKey]}">
                <li class="refinement">
                    <a href="${prevUrl}&refinement=${refinement.key.refinementColumn}&${refinement.key.refinementColumn}=${refinementValue.refinementKey}">${refinementValue.refinementValue}(${refinementMapCount[refinementValue.refinementKey]})</a>
                </li>
                </c:if>    
                </c:forEach>
            </ul>
            </c:if>

    </div>

</li>
</c:if>
</c:forEach>
</c:if>
</ul>

</div>

</c:if>
<c:if test="${totalProducs <= 0}">
    <h:leftcategorymenu/>
</c:if>
</c:if>

<script type="text/javascript">
    $(function() {

        $(".section div.type").each(function(i, selected) {
            $(".refinements .type a.refine-cat").eq(i).trigger('click');
        });
    });
</script>
