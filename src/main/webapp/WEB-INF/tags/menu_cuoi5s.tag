<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="menu_cuoi5s">
    <ul class="nav navbar-nav">
        <spring:eval expression="serviceLocator.getMenuDao().getRootMenus(site)" var="menus"/>
        <c:forEach var="menu" items="${menus}">
            <c:set var="siteUrl" value="/content/${menu.uri}"/>
            <%--Convert url by using correct language. Ex: contact-us.html -> lien-he.html --%>
            <c:if test="${menu.homePage == 'Y'}">
                <c:set var="siteUrl" value="/"/>
            </c:if>
            <c:if test="${menu.menuTemplate == 'Y'}">
                <c:set var="siteUrl" value="/${menu.uri}"/>
            </c:if>
            <c:if test="${menu.menuTemplate == 'E'}">
                <c:set var="siteUrl" value="${menu.uri}"/>
            </c:if>
            <c:url value="${siteUrl}" var="siteUrl"/>
            <c:url value="${siteUrl}" var="idForMenu"/>
            <c:set value="${fn:replace(idForMenu,'/','')}" var="idForMenu"/>
            <c:set value="${fn:replace(idForMenu,'.','-')}" var="idForMenu"/>
            <c:choose>
                <%--Product Menu--%>
                <c:when test="${fn:contains(menu.uri, 'catalog/')}">
                    <spring:eval expression="serviceLocator.categoryDao.getRootCategories(site, true)" var="parentCategories"/>
                    <c:choose>
                        <%--If having categories--%>
                        <c:when test="${fn:length(parentCategories) > 0}">
                            <li class="dropdown top-menu">
                            <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"> ${menu.name}</a>
                            <%--Parent Category--%>
                                <ul class="dropdown-menu">
                                <c:forEach var="parentCat" items="${parentCategories}" varStatus="parentCatStatus">
                                <spring:eval expression="serviceLocator.getCategoryDao().findActiveByOrder('parentCategory.id', parentCat.id, 'sequence')" var="categories"/>
                                    <c:choose>
                                        <c:when test="${fn:length(categories) > 0}">
                                            <li class="dropdown-submenu" id="${idForMenu}-menu">
                                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"> ${parentCat.name}</a>
                                                <%--Category--%>
                                                <ul class="dropdown-menu">
                                                <c:forEach var="category" items="${categories}" varStatus="categoryStatus">
                                                    <spring:eval expression="serviceLocator.getCategoryDao().findActiveByOrder('parentCategory.id', category.id, 'sequence')" var="subCategories"/>
                                                    <c:choose>
                                                        <c:when test="${!empty subCategories}">
                                                            <li class="dropdown-submenu">
                                                                <a href="javascript:void(0);"><c:out value="${category.name}"/></a>
                                                                <ul class="dropdown-menu">
                                                                    <c:forEach var="subCategory" items="${subCategories}" varStatus="subCategoryStatus">
                                                                        <c:url var="url3" value="/category/${parentCat.uri}-${parentCat.id}/${category.uri}-${category.id}/${subCategory.uri}-${subCategory.id}.html"/>
                                                                        <c:set value="${fn:replace(url3,'/','')}" var="tempSiteUrl"/>
                                                                        <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                                                                        <li id="${tempSiteUrl}_"><a href="${url3}">${subCategory.name}</a> </li>
                                                                    </c:forEach>
                                                                </ul>
                                                            </li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:url var="url2" value="/category/${parentCat.uri}-${parentCat.id}/${category.uri}-${category.id}.html"/>
                                                            <c:set value="${fn:replace(url2,'/','')}" var="tempSiteUrl"/>
                                                            <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                                                            <li class="" id="${tempSiteUrl}_"><a href="${url2}"> <c:out value="${category.name}"/></a></li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                                </ul>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <c:url var="url1" value="/category/${parentCat.uri}-${parentCat.id}.html"/>
                                            <c:set value="${fn:replace(url1,'/','')}" var="tempSiteUrl"/>
                                            <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                                            <li class="" id="${tempSiteUrl}_"><a href="${url1}"> <c:out value="${parentCat.name}"/></a></li>
                                        </c:otherwise>
                                    </c:choose>
                            </c:forEach>
                                </ul>
                            </li>
                        </c:when>
                        <%--Don't have categoies--%>
                        <c:otherwise>
                            <c:set value="${fn:replace(siteUrl,'/','')}" var="tempSiteUrl"/>
                            <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                            <li class="top-menu" id="${tempSiteUrl}_"><a href="${siteUrl}"> <c:out value="${menu.name}"/></a></li>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <%--News Menu--%>
                <c:when test="${fn:contains(menu.uri, 'news/')}">
                    <spring:eval expression="serviceLocator.newsCategoryDao.getRootNewsCategories(site, 'Y')" var="parentCategories"/>
                    <c:choose>
                        <%--If having categories--%>
                        <c:when test="${fn:length(parentCategories) > 0}">
                            <li class="dropdown top-menu">
                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"> ${menu.name}</a>
                                    <%--Parent Category--%>
                                <ul class="dropdown-menu">
                                    <c:forEach var="parentCat" items="${parentCategories}" varStatus="parentCatStatus">
                                        <spring:eval expression="serviceLocator.newsCategoryDao.getSubNewsCategories(site, parentCat, 'Y')" var="categories"/>
                                        <c:choose>
                                            <c:when test="${fn:length(categories) > 0}">
                                                <li class="dropdown-submenu" id="${idForMenu}-menu">
                                                    <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"> ${parentCat.name}</a>
                                                        <%--Category--%>
                                                    <ul class="dropdown-menu">
                                                        <c:forEach var="category" items="${categories}" varStatus="categoryStatus">
                                                            <spring:eval expression="serviceLocator.newsCategoryDao.getSubNewsCategories(site, category, 'Y')" var="subCategories"/>
                                                            <c:choose>
                                                                <c:when test="${!empty subCategories}">
                                                                    <li class="dropdown-submenu">
                                                                        <a href="javascript:void(0);"><c:out value="${category.name}"/></a>
                                                                        <ul class="dropdown-menu">
                                                                            <c:forEach var="subCategory" items="${subCategories}" varStatus="subCategoryStatus">
                                                                                <c:url value="/news/c/${subCategory.uri}" var="newsCategoryUrl3"/>
                                                                                <c:set value="${fn:replace(newsCategoryUrl3,'/','')}" var="tempSiteUrl"/>
                                                                                <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                                                                                <li id="${tempSiteUrl}_"><a href="${newsCategoryUrl3}">${subCategory.name}</a> </li>
                                                                            </c:forEach>
                                                                        </ul>
                                                                    </li>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:url value="/news/c/${category.uri}" var="newsCategoryUrl2"/>
                                                                    <c:set value="${fn:replace(newsCategoryUrl2,'/','')}" var="tempSiteUrl"/>
                                                                    <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                                                                    <li class="" id="${tempSiteUrl}_"><a href="${newsCategoryUrl2}"> <c:out value="${category.name}"/></a></li>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </ul>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:url value="/news/c/${parentCat.uri}" var="newsCategoryUrl"/>
                                                <c:set value="${fn:replace(newsCategoryUrl,'/','')}" var="tempSiteUrl"/>
                                                <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                                                <li class="" id="${tempSiteUrl}_"><a href="${newsCategoryUrl}"> <c:out value="${parentCat.name}"/></a></li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:when>
                        <%--Don't have categoies--%>
                        <c:otherwise>
                            <c:set value="${fn:replace(siteUrl,'/','')}" var="tempSiteUrl"/>
                            <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                            <li class="top-menu" id="${tempSiteUrl}_"><a href="${siteUrl}"> <c:out value="${menu.name}"/></a></li>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <%--Other Menu--%>
                <c:otherwise>
                    <spring:eval expression="serviceLocator.getMenuDao().getSubMenus(site, menu, 'Y')" var="subMenus"/>
                    <c:choose>
                        <c:when test="${! empty subMenus}">
                            <li class="dropdown top-menu">
                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"> ${menu.name}</a>
                                <ul class="dropdown-menu">
                                    <%--<c:set value="${fn:replace(siteUrl,'/','')}" var="tempSiteUrl"/>--%>
                                    <%--<c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>--%>
                                    <%--<li class="" id="${tempSiteUrl}_"><a href="${siteUrl}"> <c:out value="${menu.name}"/></a></li>--%>
                                    <c:forEach var="submenu" items="${subMenus}" varStatus="status">

                                        <spring:eval expression="serviceLocator.getMenuDao().getSubMenus(site, submenu, 'Y')" var="sub2Menus"/>
                                        <c:choose>
                                            <c:when test="${!empty sub2Menus}">
                                                <li class="dropdown-submenu">
                                                    <a href="javascript:void(0);"><c:out value="${submenu.name}"/></a>
                                                    <ul class="dropdown-menu">
                                                        <c:forEach var="sub2menu" items="${sub2Menus}">
                                                            <c:set var="subURL" value="/content/${sub2menu.uri}"/>
                                                            <c:if test="${sub2menu.homePage == 'Y'}">
                                                                <c:set var="subURL" value="/"/>
                                                            </c:if>
                                                            <c:if test="${sub2menu.menuTemplate == 'Y'}">
                                                                <c:set var="subURL" value="/${sub2menu.uri}"/>
                                                            </c:if>
                                                            <c:if test="${sub2menu.menuTemplate == 'E'}">
                                                                <c:set var="subURL" value="${sub2menu.uri}"/>
                                                            </c:if>
                                                            <%--<li class="divider"></li>--%>
                                                            <c:url value="${subURL}" var="subURL"/>
                                                            <c:set value="${fn:replace(subURL,'/','')}" var="tempSiteUrl"/>
                                                            <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                                                            <li id="${tempSiteUrl}_"><a href="${subURL}"> ${sub2menu.name}</a></li>
                                                        </c:forEach>
                                                    </ul>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="subURL" value="/content/${submenu.uri}"/>
                                                <c:if test="${submenu.homePage == 'Y'}">
                                                    <c:set var="subURL" value="/"/>
                                                </c:if>
                                                <c:if test="${submenu.menuTemplate == 'Y'}">
                                                    <c:set var="subURL" value="/${submenu.uri}"/>
                                                </c:if>
                                                <c:if test="${submenu.menuTemplate == 'E'}">
                                                    <c:set var="subURL" value="${submenu.uri}"/>
                                                </c:if>
                                                <%--<li class="divider"></li>--%>
                                                <c:url value="${subURL}" var="subURL"/>
                                                <c:set value="${fn:replace(subURL,'/','')}" var="tempSiteUrl"/>
                                                <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                                                <li id="${tempSiteUrl}_"><a href="${subURL}"> ${submenu.name}</a></li>
                                            </c:otherwise>
                                        </c:choose>



                                    </c:forEach>
                                </ul>
                            </li>
                        </c:when>

                        <c:otherwise>
                            <c:set value="${fn:replace(siteUrl,'/','')}" var="tempSiteUrl"/>
                            <c:set value="${fn:replace(tempSiteUrl,'.','-')}" var="tempSiteUrl"/>
                            <li class="top-menu" id="${tempSiteUrl}_"><a href="${siteUrl}"> <c:out value="${menu.name}"/></a></li>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <spring:eval expression="serviceLocator.getSiteProductServiceDao().getSiteProductService('ECOMMERCE', site.id)" var="ecommerceService"/>
        <c:if test="${!empty ecommerceService}">
            <!-- Search Block -->
            <li>
            <i class="search fa fa-search search-btn"></i>
            <form action="/search.html" method="get" name="searchForm">
            <div class="search-open">
                <div class="input-group animated fadeInDown">
                    <input type="text" class="form-control" placeholder="<fmt:message key="site.search.field"/>" name="keyword" maxlength="50">
                                    <span class="input-group-btn">
                                        <button class="btn-u" type="submit"><fmt:message key="site.search.go"/></button>
                                    </span>
                </div>
            </div>
            </form>
        </li>
            <!-- End Search Block -->
        </c:if>
    </ul>
</app:cache>
<%--Active menu that is selected--%>
<c:url var="uri" value="${uri}"/>
<c:set value="${fn:replace(uri,'/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'.','-')}" var="uri"/>
<script type="text/javascript">
    var currentMenu = "${uri}";
    $(document).ready(function () {
        $("#" + currentMenu + "_").addClass("active");
        $("#" + currentMenu + "_").closest("li.dropdown-submenu").addClass("active");
        $("#" + currentMenu + "_").closest("li.top-menu").addClass("active");
    });
</script>
