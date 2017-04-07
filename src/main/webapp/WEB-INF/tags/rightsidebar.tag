<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="videos" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="catId" required="false" rtexprvalue="true" type="java.lang.String"%>
<%--<app:cache key="rightsidebar-most-view-video-list${catId}">--%>
<!-- Right Sidebar -->
<div class="col-md-4 magazine-page">
    <div class="headline headline-md"><h4>Like để cập nhật những video vui nhộn.</h4></div>
    <div class="fb-like-box" data-href="http://www.facebook.com/cuoi5s" data-colorscheme="light" data-show-faces="true" data-header="true" data-stream="false" data-show-border="true"></div>

    <!-- Posts -->
    <div class="posts margin-bottom-40" id="load-more-most-view-videos">
        <div class="headline headline-md"><h2>Video Nhiều người xem</h2></div>
        <c:forEach items="${videos}" var="video">
            <c:url var="url" value="/video/${video.uri}/${video.id}.html?videoId=${video.videoId}"/>
            <dl class="dl-horizontal">
                <dt><a href="${url}"><img src="${video.thumbImgUrl}" alt="${video.name}" /><c:if test="${!empty video.duration}"><div class="duration">${video.duration}</div></c:if></a></dt>
                <dd>
                    <p><a href="${url}">${video.name}</a></p>
                    <ul class="list-unstyled list-inline blog-info">
                        <%--<li><i class="fa fa-eye"></i> ${video.viewCount} views</li>--%>
                            <%--<li><i class="fa fa-pencil"></i> Diana Anderson</li>--%>
                            <%--<li><i class="fa fa-comments"></i> <a href="#">24 Comments</a></li>--%>
                    </ul>
                </dd>
            </dl>
        </c:forEach>
    </div><!--/posts-->
    <!-- End Posts -->
    <button class="btn-u" onclick="loadMoreMostViewVidoes()">Xem thêm</button>

</div>
<!-- End Right Sidebar -->
    <script>
        var initRight = ${numberVideosRightColumn};
        var loadMoreRight = ${numberVideosRightColumnMore};
        var pageRight = 1;
        function loadMoreMostViewVidoes() {
            var url = "/load-more-most-video-videos.json";
            startRight = initRight + ((pageRight-1)*loadMoreRight);
            if ('${catId}' != '') {
                url = url+"?start="+startRight + "&catId=" + '${catId}';
            } else {
                url = url+"?start="+startRight;
            }
            pageRight = pageRight + 1;
            $.getJSON( url, function( data ) {
                for(var i in data) {
                    url = "/video/"+data[i].uri+"/"+data[i].id+".html?videoId="+data[i].videoId;
                    duration = "";
                    if (data[i].duration != null) {
                        duration = "<div class='duration'>"+data[i].duration+"</div>";
                    }
                    dl = $("<dl class='dl-horizontal'/>");
                    dl.append("<dt><a href='"+url+"'><img src='"+data[i].thumbImgUrl+"' alt='"+data[i].name+"' />"+duration+"</a></dt>");

                    dd = $("<dd/>");
                    dd.append("<p><a href='"+url+"'>"+data[i].name+"</a></p>");
                    ul = $("<ul class='list-unstyled list-inline blog-info'>");
//                    ul.append("<li><i class='fa fa-eye'></i> "+data[i].viewCount+" views</li>");
                    dd.append(ul);
                    dl.append(dd);
                    $('#load-more-most-view-videos').append(dl);
                }
            });

        }

    </script>
<%--</app:cache>--%>