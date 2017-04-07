<%@ page import="com.easysoft.ecommerce.controller.Constants" %>
<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}">
    <c:set value="${fn:replace(uri,'.html','')}" var="temp"/>
    <c:set value="${fn:split(temp,'/')}" var="ids"/>
    <c:set value="${ids[fn:length(ids)-1]}" var="id"/>
    <spring:eval expression="serviceLocator.getCategoryDao().findById(T(java.lang.Long).valueOf(id))" var="cat"/>
    <html>
<head>
    <title>${cat.name}</title>
    <meta name="description" content="${cat.name}"/>
    <meta name="keywords" content=""/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<spring:eval expression="serviceLocator.getVideoDao().getVideosByCategory(T(java.lang.Long).valueOf(id), 0, T(com.easysoft.ecommerce.controller.Constants).NUMBER_VIDEO_RETURN, 'updatedDate', true)" var="videos"/>

<div class="container content">
<div class="row blog-page">
    <div class="col-md-8">
        <div class="headline headline-md"><h2>${cat.name}</h2></div>
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
        <button class="btn-u" onclick="loadMoreNewVideos()">Xem thêm</button>
    </div>
    <spring:eval expression="serviceLocator.getVideoDao().getVideosByCategory(T(java.lang.Long).valueOf(id), 0, T(com.easysoft.ecommerce.controller.Constants).NUMBER_VIDEO_LOAD_MORE_RIGHT_COLUMN, 'viewCount', true)" var="videos"/>
    <h:rightsidebar videos="${videos}" catId="${id}"/>
</div>
</div>

<script>
    var init = ${numberVideos};
    var loadMore = ${numberVideosMore};
    var page = 1;
    function loadMoreNewVideos() {
        var url = "/load-more-new-videos.json";
        start = init + ((page-1)*loadMore);
        url = url+"?start="+start+ "&catId=" + '${id}';
        page = page + 1;
        loadMoreNewVideosOnPage(url);
//        $.getJSON( url, function( data ) {
//            for(var i in data) {
//                url = "/video/"+data[i].uri+"/"+data[i].id+".html?videoId="+data[i].videoId;
//                div = $("<div class='col-sm-4'/>");
//                div1 = $("<div class='thumbnail-style'/>");
//                div1.append("<a href='"+url+"'><img alt='"+data[i].name+"' src='"+data[i].mediumImgUrl+"' class='img-responsive'></a>");
//                ul = $("<ul class='list-unstyled list-inline blog-info'>");
////                ul.append("<li><i class='fa fa-eye'></i> "+data[i].viewCount+" views</li>");
//                div1.append(ul);
//                div1.append("<p title='"+data[i].name+"'>"+data[i].name+"</p>");
//                div.append(div1);
//                $('#load-more-videos').append(div);
//            }
//        });

    }

</script>
</body>
</html>
</app:cache>