<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="newsCategory" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.model.NewsCategory"%>
<%@ attribute name="newses" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="params" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.util.QueryString"%>
<%@ attribute name="numberOfNewses" required="true" rtexprvalue="true" type="java.lang.String"%>
<c:if test="${!empty newsCategory}">
    <spring:eval expression="serviceLocator.newsCategoryDao.getParentNewsCategory(site, newsCategory.id, 'Y')" var="parentNewsCategory"/>
</c:if>
<!--=== Content Part ===-->
<div class="container content">
    <div class="row">
        <c:set var="classRightContent" value="col-md-12"/>
        <spring:eval expression="serviceLocator.newsCategoryDao.getRootNewsCategories(site, 'Y')" var="newsCategories"/>
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
            <c:if test="${!empty newses}">
                <c:forEach items="${newses}" varStatus="news" var="currentNews">
                    <c:url value="/news/${currentNews.uri}" var="newsurl"/>
                    <div class="funny-boxes funny-boxes-top-sea">
                        <div class="row">
                            <div class="col-md-4 funny-boxes-img">
                                <a href="${newsurl}">
                                    <c:if test="${!empty currentNews.thumbImg}">
                                        <img class="img-responsive" src="${currentNews.thumbImg}?op=scale_300&op=crop_0,0,300,200" alt="">
                                    </c:if>
                                    <c:if test="${empty currentNews.thumbImg}">
                                        <img class="img-responsive" src="http://placehold.it/300x200" alt="">
                                    </c:if>
                                </a>
                            </div>
                            <div class="col-md-8">
                                <h2><a href="${newsurl}">${currentNews.title}</a></h2>
                                    <%--ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT")--%>
                                <spring:eval expression="site.getSiteParamsMap().get('DATE_FORMAT')" var="format"/>
                                <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).dateToString(currentNews.createdDate, format)" var="createdDate"/>
                                <ul class="list-unstyled list-inline">
                                    <li><i class="fa fa-calendar"></i> ${createdDate}</li>
                                </ul>
                                <p>${currentNews.shortDescription}</p>
                                <a href="${newsurl}" class="btn-u"><fmt:message key="common.read.more"/></a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>

            <h:paginationbootstrap3x totalItems="${numberOfNewses}" params="${params}" column="1" numPerPage="10"/>


        </div>
        <!-- End Content -->
    </div>
</div><!--/container-->
<!--=== End Content Part ===-->
<script type="text/javascript">
    $(document).ready(function () {
        jQuery('.list-toggle').on('click', function() {
            jQuery(this).toggleClass('active');
        });
    });
</script>
