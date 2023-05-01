<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}">
<c:set value="${fn:replace(uri,'/news/','')}" var="uri"/>
    <spring:eval expression="serviceLocator.getMenuDao().findUniqueBy('uri', 'news/index.html', site.id)" var="newsMenu"/>
    <c:url var="newsCategoryUrl" value="/news/index.html"/>
    <spring:eval expression="serviceLocator.getNewsDao().findUniqueBy('uri', uri, site.id)" var="news"/>
    <html>
<head>
    <title>${news.title}</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<!--=== Breadcrumbs ===-->
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left">${newsMenu.name}</h1>
        <%--<ul class="pull-right breadcrumb">--%>
            <%--<li><a href="index.html">Home</a></li>--%>
            <%--<li><a href="${newsCategoryUrl}">${newsMenu.name}</a></li>--%>
            <%--<li class="active">${news.title}</li>--%>
        <%--</ul>--%>
    </div>
</div><!--/breadcrumbs-->
<!--=== End Breadcrumbs ===-->

<c:if test="${!empty news}">
    <spring:eval expression="serviceLocator.newsDao.getNewsCategories(site, news.id, 'Y')" var="newsCategories"/>
    <c:if test="${!empty newsCategories}">
        <c:set var="newsCategory" value="${newsCategories[0]}"/>
        <c:if test="${!empty newsCategory}">
            <spring:eval expression="serviceLocator.newsCategoryDao.getParentNewsCategory(site, newsCategory.id, 'Y')" var="parentNewsCategory"/>
        </c:if>
    </c:if>

    <c:url value="/news/index.html" var="url"/>
    <spring:eval expression="serviceLocator.newsCategoryDao.getRootNewsCategories(site, 'Y')" var="newsCategories"/>

    <!--=== Content Part ===-->
    <div class="container content">
        <div class="row">
            <c:set var="classRightContent" value="col-md-12"/>
            <c:if test="${!empty newsCategories}">
                <!-- Begin Sidebar Menu -->
                <div class="col-md-3">
                    <ul class="list-group sidebar-nav-v1 fa-fixed" id="sidebar-nav">
                        <c:forEach var="otherNewsCategory" items="${newsCategories}">
                            <spring:eval expression="serviceLocator.newsCategoryDao.getSubNewsCategories(site, otherNewsCategory, 'Y')" var="subNewsCategories"/>
                            <c:choose>
                                <c:when test="${!empty subNewsCategories}">
                                    <c:set var="sideBarStatus" value=""/>
                                    <c:set var="parentStatus" value=""/>
                                    <c:set var="collapseIn" value=""/>
                                    <c:if test="${!empty parentNewsCategory && parentNewsCategory.id == otherNewsCategory.id}">
                                        <c:set var="sideBarStatus" value="collapsed"/>
                                        <c:set var="parentStatus" value="active"/>
                                        <c:set var="collapseIn" value="in"/>
                                    </c:if>
                                    <li class="list-group-item list-toggle ${parentStatus}"><a class="${sideBarStatus}" data-toggle="collapse" data-parent="#sidebar-nav" href="#cat_${otherNewsCategory.id}">${otherNewsCategory.name}</a>
                                            <%--Get SubNewsCategory--%>
                                        <c:if test="${! empty subNewsCategories}">
                                            <ul id="cat_${otherNewsCategory.id}" class="collapse ${collapseIn}">
                                                <c:forEach items="${subNewsCategories}" var="subNewsCategory">
                                                    <c:url value="/news/c/${subNewsCategory.uri}" var="newsCategoryUrl"/>
                                                    <c:set var="newsStatus" value=""/>
                                                    <c:if test="${!empty newsCategory && newsCategory.id == subNewsCategory.id}">
                                                        <c:set var="newsStatus" value="active"/>
                                                    </c:if>
                                                    <li class="${newsStatus}"><a href="${newsCategoryUrl}"><i class="fa fa-bars"></i> <c:out value="${subNewsCategory.name}"/></a></li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="categoryStatus" value=""/>
                                    <c:if test="${!empty newsCategory && newsCategory.id == otherNewsCategory.id}">
                                        <c:set var="categoryStatus" value="active"/>
                                    </c:if>
                                    <c:url value="/news/c/${otherNewsCategory.uri}" var="newsCategoryUrl"/>
                                    <li class="list-group-item ${categoryStatus}"><a href="${newsCategoryUrl}">${otherNewsCategory.name}</a></li>
                                </c:otherwise>
                            </c:choose>



                        </c:forEach>
                    </ul>
                </div>
                <!-- End Sidebar Menu -->
                <c:set var="classRightContent" value="col-md-9"/>
            </c:if>

            <!-- Begin Content -->
            <div class="${classRightContent}">
                <spring:eval expression="site.getSiteParamsMap().get('DATE_FORMAT')" var="format"/>
                <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).dateToString(news.createdDate, format)" var="createdDate"/>
                <div class="blog margin-bottom-40">
                    <h2><a href="#">${news.title}</a></h2>
                    <div class="blog-post-tags">
                        <ul class="list-unstyled list-inline blog-info">
                            <li><i class="fa fa-calendar"></i> ${createdDate}</li>
                        </ul>
                        <%--<ul class="list-unstyled list-inline blog-tags">--%>
                            <%--<li>--%>
                                <%--<i class="fa fa-tags"></i>--%>
                                <%--<a href="#">Technology</a>--%>
                                <%--<a href="#">Education</a>--%>
                                <%--<a href="#">Internet</a>--%>
                                <%--<a href="#">Media</a>--%>
                            <%--</li>--%>
                        <%--</ul>--%>
                    </div>
                    <h5>${news.shortDescription}</h5>
                    <c:if test="${!empty news.thumbImg}">
                        <div class="blog-img">
                            <img src='${news.thumbImg}?op=scale_850x' alt=""/>
                        </div>
                    </c:if>
                    <div class="margin-bottom-20"></div>
                        ${news.content}
                </div>

                <spring:eval
                        expression="serviceLocator.getNewsDao().findActiveByOrder(null, T(java.lang.Long).valueOf('0'), T(java.lang.Long).valueOf('10'), 'createdDate desc', site.id)"
                        var="newses"/>
                <c:if test="${!empty newses}">
                    <div class="headline">
                        <h3>${newsMenu.name}</h3>
                    </div>
                    <ul class="list-unstyled">
                        <c:forEach var="otherNews" items="${newses}">
                            <c:url value="/news/${otherNews.uri}" var="newsurl"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).dateToString(otherNews.createdDate, format)" var="createdDate"/>
                            <li><a href="${newsurl}">${otherNews.title}</a> <i class="fa fa-calendar"></i> ${createdDate}</li>
                        </c:forEach>
                    </ul>
                </c:if>

            </div>
            <!-- End Content -->
        </div>
    </div><!--/container-->
    <!--=== End Content Part ===-->
</c:if>
</body>
</html>
</app:cache>