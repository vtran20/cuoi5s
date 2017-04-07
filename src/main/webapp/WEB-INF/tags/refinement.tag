<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="params" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.util.QueryString"%>
<%@ attribute name="isSearch" required="false" rtexprvalue="true" type="java.lang.String"%>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>

<h1><fmt:message key="filter.narrow.by"/></h1>
<c:if test="${isSearch == 'Y'}">
    <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getRefinementMap(command.refinement, params)" var="refinementMap"/>
    <spring:eval expression="serviceLocator.getSearchService().countProductsBySubCategoryMap(command.keyword, site, refinementMap)" var="subcategories"/>
    <c:if test="${fn:length(subcategories) > 0}">
        <div class="panel-group" id="accordion-v2">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion-v2" href="#collapseTwo">
                            <fmt:message key="filter.category"/>
                            <i class="fa fa-angle-down"></i>
                        </a>
                    </h2>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse in">
                    <div class="panel-body">
                        <ul class="list-unstyled checkbox-list">
                                <%--<c:if test="${fn:contains(params, 'categoryId')}">--%>
                            <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
                                <%--<spring:eval expression="gParams.exclude('categoryId').toString()" var="query"/>--%>
                                <%--<a href="${browseruri}?${query}" class="clear"><fmt:message key="filter.clear"/></a>--%>
                                <%--</c:if>--%>
                            <spring:eval expression="params.getParameter('categoryId')" var="categoryId"/>
                            <c:if test="${!empty categoryId}">
                                <c:forEach items="${subcategories}" var="cat">
                                    <c:if test="${cat[0].id == categoryId}">
                                        <li>
                                            <label class="checkbox">
                                                <spring:eval expression="gParams.exclude('categoryId').toString()" var="query"/>
                                                <input type="checkbox" name="checkbox" checked onclick="processFilter(this)" value="${browseruri}?${query}">
                                                <i></i>
                                                    ${cat[0].name}
                                                <small><a href="#">(${cat[1]})</a></small>
                                            </label>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty categoryId}">
                                <c:forEach items="${subcategories}" var="cat">
                                    <li>
                                        <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
                                        <spring:eval expression="gParams.exclude('page')" var="gParams"/>
                                        <c:set value="${browseruri}?${gParams}" var="prevUrl"/>
                                        <spring:eval expression="gParams.addAndKeepOrgParam('categoryId',cat[0].id).toString()" var="query"/>
                                        <c:set value="categoryId=${cat[0].id}" var="catString"/>
                                            <%--<c:if test="${fn:contains(prevUrl, catString)}">--%>
                                            <%--<span class="selected">${cat[0].name} (${cat[1]})</span>--%>
                                            <%--</c:if>--%>
                                        <c:if test="${!fn:contains(prevUrl, catString)}">
                                            <label class="checkbox">
                                                <input type="checkbox" name="checkbox" onclick="processFilter(this)" value='${browseruri}?${query}'>
                                                <i></i>
                                                    ${cat[0].name}
                                                <small><a href="#">(${cat[1]})</a></small>
                                            </label>
                                        </c:if>
                                    </li>
                                </c:forEach>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</c:if>

<div class="panel-group" id="accordion-v4">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h2 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion-v4" href="#collapseFour">
                    <fmt:message key="filter.by.price"/>
                    <i class="fa fa-angle-down"></i>
                </a>
            </h2>
        </div>
        <div id="collapseFour" class="panel-collapse collapse in">
            <div class="panel-body">
                <div class="slider-snap" id="price_filter"></div>
                <p class="">
                    <span id="skip-value-lower"></span> - <span id="skip-value-upper"></span>
                </p>
            </div>
        </div>
        <spring:eval expression="params.clone()" var="gParams"/> <%--clone params instance--%>
        <spring:eval expression="gParams.exclude('page')" var="gParams"/>
        <spring:eval expression="gParams.exclude('refinement')" var="gParams"/>
        <spring:eval expression="gParams.exclude('price')" var="gParams"/>
        <c:set value="${browseruri}?${gParams}" var="prevUrl"/>
        <c:if test="${fn:endsWith(prevUrl, '&') || fn:endsWith(prevUrl, '?')}">
            <c:set value="${prevUrl}refinement=price&price=" var="filterUrl"/>
        </c:if>
        <c:if test="${!fn:endsWith(prevUrl, '&') && !fn:endsWith(prevUrl, '?')}">
            <c:set value="${prevUrl}&refinement=price&price=" var="filterUrl"/>
        </c:if>
        <spring:eval expression="params.getParameter('price')" var="priceRange" />
        <c:set value="1000" var="startPrice"/>
        <c:set value="10000000" var="endPrice"/>
        <c:if test="${!empty priceRange && fn:contains(priceRange, '-')}">
            <c:set value="${fn:split(priceRange, '-')}" var="prices"/>
            <c:if test="${fn:length(prices) > 1}">
                <c:set value="${prices[0]}" var="startPrice"/>
                <c:set value="${prices[1]}" var="endPrice"/>
            </c:if>
        </c:if>
        <script>
            function processFilter(object) {
                window.location.href = object.value;
            }

            $("#price_filter").noUiSlider({
                connect: true,
                behaviour: 'tap',
                start: [ ${startPrice}, ${endPrice} ],
                range: {
                    // Starting at 500, step the value by 500,
                    // until 4000 is reached. From there, step by 1000.
                    'min': [ 0, 50000 ],
                    '30%': [ 1000000, 200000 ],
                    '50%': [ 2000000, 500000 ],
                    'max': [ 10000000 ]
                },
                format: wNumb({decimals: 0})
            });
            $("#price_filter").Link('lower').to($("#skip-value-lower"),null, wNumb({decimals: 3,thousand: '.',postfix: ' VND'}));
            $("#price_filter").Link('upper').to($("#skip-value-upper"),null, wNumb({decimals: 3,thousand: '.',postfix: ' VND'}));
            $("#price_filter").on({
                change: function(){
                    var url = $("#price_filter").val();
                    window.location.href='${filterUrl}'+url[0]+'-'+url[1];
                }
            });
        </script>
    </div>
</div><!--/end panel group-->
<c:if test="${fn:contains(params, 'refinement') || fn:contains(params, 'categoryId')}">
    <div>
        <spring:eval expression="new com.easysoft.ecommerce.util.QueryString('keyword',command.keyword)" var="queryString"/>
        <a href="${browseruri}?${queryString}" class="btn-u btn-brd btn-brd-hover btn-u-lg btn-u-sea-shop btn-block"><fmt:message key="filter.clear.all"/></a>
    </div>
</c:if>
