<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}">
<html>
<c:set value="${fn:replace(uri,'.html','')}" var="temp"/>
<c:set value="${fn:split(temp,'/')}" var="ids"/>
<c:set value="${ids[fn:length(ids)-1]}" var="id"/>
<spring:eval expression="serviceLocator.getVideoDao().findById(T(java.lang.Long).valueOf(id))" var="video"/>
<c:set value="http://${site.domain}${uri}?${pageContext.request.queryString}" var="videoUrl"/>
<head>
    <title>${video.name}</title>
    <meta name="description" content="${video.name}"/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>

    <meta property="og:site_name" content="cuoi5s.com">
    <meta property="og:url" content="${videoUrl}">
    <meta property="og:title" content="<c:out value="${video.name}"/>">
    <meta property="og:image" content="${video.mediumImgUrl}">
    <meta property="og:description" content="Share để mọi người cùng xem. Xem thêm tại http://${site.domain}">
    <meta property="og:type" content="video">
    <meta property="og:video" content="http://www.youtube.com/v/${video.videoId}?autohide=1&amp;version=3">
    <meta property="og:video:type" content="application/x-shockwave-flash">

    <%--<meta content="video.movie" property="og:type"/>--%>
</head>

<body>
<div class="container content">
<div class="row blog-page">
<!-- Left Sidebar -->
<div class="col-md-8">
    <!--Blog Post-->
    <div class="blog margin-bottom-40">
        <div class="blog-img margin-bottom-20">
            <div class="responsive-video" id="iframe-youtube">
                <%--<iframe src="http://player.vimeo.com/video/47911018" frameborder="0" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>--%>
                    <iframe id="ytplayer" type="text/html" src="http://www.youtube.com/embed/${video.videoId}?autoplay=1&showinfo=0&iv_load_policy=3&ps=docs&enablejsapi=1&version=3&wmode=transparent" frameborder="0"></iframe>
                <script>
                    function getFrameID(id){
                        var elem = document.getElementById(id);
                        if (elem) {
                            if(/^iframe$/i.test(elem.tagName)) return id; //Frame, OK
                            // else: Look for frame
                            var elems = elem.getElementsByTagName("iframe");
                            if (!elems.length) return null; //No iframe found, FAILURE
                            for (var i=0; i<elems.length; i++) {
                                if (/^https?:\/\/(?:www\.)?youtube(?:-nocookie)?\.com(\/|$)/i.test(elems[i].src)) break;
                            }
                            elem = elems[i]; //The only, or the best iFrame
                            if (elem.id) return elem.id; //Existing ID, return it
                            // else: Create a new ID
                            do { //Keep postfixing `-frame` until the ID is unique
                                id += "-frame";
                            } while (document.getElementById(id));
                            elem.id = id;
                            return id;
                        }
                        // If no element, return null.
                        return null;
                    }

                    // Define YT_ready function.
                    var YT_ready = (function() {
                        var onReady_funcs = [], api_isReady = false;
                        /* @param func function     Function to execute on ready
                         * @param func Boolean      If true, all qeued functions are executed
                         * @param b_before Boolean  If true, the func will added to the first
                         position in the queue*/
                        return function(func, b_before) {
                            if (func === true) {
                                api_isReady = true;
                                while (onReady_funcs.length) {
                                    // Removes the first func from the array, and execute func
                                    onReady_funcs.shift()();
                                }
                            } else if (typeof func == "function") {
                                if (api_isReady) func();
                                else onReady_funcs[b_before?"unshift":"push"](func);
                            }
                        }
                    })();
                    // This function will be called when the API is fully loaded
                    function onYouTubePlayerAPIReady() {YT_ready(true)}

                    // Load YouTube Frame API
                    (function() { // Closure, to not leak to the scope
                        var s = document.createElement("script");
                        s.src = (location.protocol == 'https:' ? 'https' : 'http') + "://www.youtube.com/player_api";
                        var before = document.getElementsByTagName("script")[0];
                        before.parentNode.insertBefore(s, before);
                    })();
                    var player; //Define a player object, to enable later function calls, without
                    // having to create a new class instance again.

                    // Add function to execute when the API is ready
                    YT_ready(function(){
                        var frameID = getFrameID("iframe-youtube");
                        if (frameID) { //If the frame exists
                            player = new YT.Player(frameID, {
                                events: {
                                    "onError": onErrorProcess,
                                    "onStateChange": onPlayerStateChange
                                }
                            });
                        }
                    });

                    // Example: function stopCycle, bound to onStateChange
                    function onErrorProcess(event) {
                        if(event.data == 100 || event.data == 101 || event.data == 150)
                        {
                            $.getJSON( '/video/check-video-status.json?status='+event.data+'&videoId=${video.videoId}', function( data ) {
                            });
                        }
                    }
                    function onPlayerStateChange(event) {
                        if (event.data == YT.PlayerState.ENDED)
                        {
                            $('#like-share-modal').modal();
                        }
                    }
                </script>
            </div>
            <div class="modal fade bs-example-modal-sm" id="like-share-modal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-md">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                            <h3 id="myLargeModalLabel" class="modal-title">Chúc bạn một ngày vui vẻ</h3>
                        </div>
                        <div class="modal-body">
                            <h3>Like để được cập nhật thêm các video vui nhộn khác</h3>
                            <div class="likebutton"><iframe src="//www.facebook.com/plugins/like.php?href=http://www.facebook.com/cuoi5s&amp;width=95px&amp;layout=button_count&amp;action=like&amp;show_faces=false&amp;share=false&amp;height=21" scrolling="no" frameborder="0" allowtransparency="true" height=20></iframe></div>
                            <h3>Share để mọi người cùng xem</h3>
                            <%--<div class="fb-share-button" data-href="${videoUrl}" data-type="button_count"></div>--%>
                            <%--<fb:share-button href="https://developers.facebook.com/docs/plugins/" type="button_count"></fb:share-button>--%>
                            <div><button class="btn btn-block btn-facebook-inversed rounded" onclick="window.open('http://www.facebook.com/share.php?u=${videoUrl}','_blank','width=550,height=300,resizable=yes')"><i class="fa fa-facebook"></i> Share on Facebook</button></div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- End Small Modal -->

        </div>
        <ul class="list-unstyled list-inline blog-info pull-right">
            <li><i class="fa fa-calendar"></i> Đăng ngày: <fmt:formatDate pattern="dd/MM/yyyy" value="${video.updatedDate}"/></li>
            <%--<li><i class="fa fa-eye"></i> ${video.viewCount} views</li>--%>
        </ul>
        <div class="fb-like" data-href="${videoUrl}" data-layout="button" data-action="like" data-show-faces="false" data-share="true"></div>
        <h2>${video.name}</h2>
        <div class="blog-img margin-bottom-20">
            <div class="fb-like-box" data-href="http://www.facebook.com/cuoi5s" data-colorscheme="light" data-show-faces="false" data-header="true" data-stream="false" data-show-border="true"></div>
        </div>
    <%--<div id="cat-pro-con-share" class="addthis_toolbox addthis_default_style">--%>
            <%--<a class="addthis_button_preferred_1"></a>--%>
            <%--<a class="addthis_button_facebook_like" fb:like:layout="button_count"></a>--%>
            <%--&lt;%&ndash;<a class="addthis_button_google"></a>&ndash;%&gt;--%>
            <%--<a class="addthis_button_google_plusone" g:plusone:size="medium"></a>--%>
            <%--<a class="addthis_button_zingme"></a>--%>
            <%--<a class="addthis_button_email"></a>--%>
            <%--<a class="addthis_button_compact"></a>--%>
            <%--<a class="addthis_counter addthis_bubble_style"></a>--%>
        <%--</div>--%>

        <div class="fb-comments" data-href="${videoUrl}" data-numposts="5" data-colorscheme="light"></div>
        <spring:eval expression="serviceLocator.getVideoDao().findCategoriesByVideoId(id)" var="cats"/>
        <c:if test="${empty cats || fn:length(cats) == 0}">
            <c:set value="54" var="catId"/>
        </c:if>
        <c:if test="${!empty cats && fn:length(cats) > 0}">
            <c:set value="${cats[0].id}" var="catId"/>
        </c:if>
        <app:cache key="related-video-list${catId}">
        <spring:eval expression="serviceLocator.getVideoDao().getVideosByCategory(catId, 0, T(com.easysoft.ecommerce.controller.Constants).NUMBER_VIDEO_VIEW_RETURN, 'updatedDate', true)" var="videos"/>
        <div class="headline headline-md"><h2>Video liên quan</h2></div>
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
        </app:cache>

    </div>
    <!--End Blog Post-->

</div>
    <!-- End Left Sidebar -->

    <%--<spring:eval expression="serviceLocator.getVideoDao().getMostViewVideos(site, T(com.easysoft.ecommerce.controller.Constants).NUMBER_VIDEO_RETURN_RIGHT_COLUMN, T(com.easysoft.ecommerce.controller.Constants).NUMBER_DAYS_FOR_MOST_VIEW)" var="videos"/>--%>
    <spring:eval expression="serviceLocator.getVideoDao().getVideosByCategory(T(java.lang.Long).valueOf(catId), 0, T(com.easysoft.ecommerce.controller.Constants).NUMBER_VIDEO_RETURN_RIGHT_COLUMN, 'viewCount', true)" var="videos"/>

    <h:rightsidebar videos="${videos}" catId="${catId}"/>

</div><!--/row-->
</div>
<script>
    var init = ${numberVideosView};
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