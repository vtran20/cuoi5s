<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}">
<html>
<head>
    <title>${site.description} - Kênh cập nhật các video vui nhộn giúp thư giản mỗi ngày</title>
    <meta name="description" content="Kênh cập nhật các video vui nhộn giúp thư giản mỗi ngày"/>
    <meta name="keywords" content=""/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<c:set value="${fn:replace(uri,'/','')}" var="uri"/>
<c:set value="54" var="catId"/>

<div class="container content">
    <div class="row blog-page">
    <div class="col-md-8">
        <spring:eval expression="serviceLocator.getVideoDao().findActiveByOrder('live', 'Y', null, null)" var="videos"/>
        <c:if test="${!empty videos}">
            <div class="headline headline-md"><h3>Trực tiếp bóng đá</h3></div>
            <div class="row">
                <c:forEach items="${videos}" var="video" varStatus="object">
                    <div class="col-sm-6">
                        <div class="thumbnail-style">
                            <c:url var="url" value="/video/${video.uri}/${video.id}.html?videoId=${video.videoId}"/>
                            <a href="${url}"><img alt="${video.name}" src="${video.mediumImgUrl}" class="img-responsive"></a>
                                <%--<c:if test="${!empty video.duration}">--%>
                                <%--<div class="duration">${video.duration}</div>--%>
                                <%--</c:if>--%>
                            <ul class="list-unstyled list-inline blog-info">
                                    <%--<li><i class="fa fa-calendar"></i> <fmt:formatDate pattern="dd/MM/yyyy" value="${video.updatedDate}"/></li>--%>
                                    <%--<li><i class="fa fa-eye"></i> ${video.viewCount} views</li>--%>
                                    <%--<li><i class="fa fa-pencil"></i> Diana Anderson</li>--%>
                                    <%--<li><i class="fa fa-comments"></i> <a href="#">24 Comments</a></li>--%>
                            </ul>
                            <p title="${video.name}">${video.name}</p>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <spring:eval expression="serviceLocator.getVideoDao().getVideosByCategory(catId, 0, T(com.easysoft.ecommerce.controller.Constants).NUMBER_VIDEO_RETURN, 'updatedDate', true)" var="videos"/>
        <div class="headline headline-md"><h2>Thư giãn mỗi ngày</h2></div>
        <div class="row margin-bottom-20" id="load-more-videos">
            <c:forEach items="${videos}" var="video" varStatus="object">
                <%--<c:if test="${object.index == 3}">--%>
                    <%--<div class="col-sm-4">--%>
                        <%--<div class="thumbnail-style" style="height: 210px;">--%>
                            <%--<ins class="adsbygoogle"--%>
                                 <%--style="display:inline-block;width:200px;height:200px"--%>
                                 <%--data-ad-client="ca-pub-1359923176278381"--%>
                                 <%--data-ad-slot="8522679956"></ins>--%>
                            <%--<script>--%>
                                <%--(adsbygoogle = window.adsbygoogle || []).push({});--%>
                            <%--</script>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</c:if>--%>
                <h:video_thumb video="${video}"/>
            </c:forEach>
        </div>
        <button class="btn-u" onclick="loadMoreNewVideos()">Xem thêm videos mới</button>
    </div>
    <%--<spring:eval expression="serviceLocator.getVideoDao().getMostViewVideos(site, T(com.easysoft.ecommerce.controller.Constants).NUMBER_VIDEO_RETURN_RIGHT_COLUMN, T(com.easysoft.ecommerce.controller.Constants).NUMBER_DAYS_FOR_MOST_VIEW)" var="videos"/>--%>
    <spring:eval expression="serviceLocator.getVideoDao().getVideosByCategory(catId, T(com.easysoft.ecommerce.controller.Constants).NUMBER_DAYS_FOR_MOST_VIEW, 0, T(com.easysoft.ecommerce.controller.Constants).NUMBER_VIDEO_RETURN_RIGHT_COLUMN, 'Y', 'viewCount', true)" var="videos"/>
    <h:rightsidebar videos="${videos}" catId="${catId}"/>
</div>
</div>
<script>
    var init = ${numberVideos};
    var loadMore = ${numberVideosMore};
    var page = 1;
    function loadMoreNewVideos() {
        var url = "/load-more-new-videos.json";
        start = init + ((page-1)*loadMore);
        url = url+"?start="+start+ "&catId=" + '${catId}';
        page = page + 1;
        loadMoreNewVideosOnPage(url);
    }

</script>
</body>
</html>
</app:cache>