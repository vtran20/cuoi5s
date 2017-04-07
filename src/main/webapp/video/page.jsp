<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}">
<html>
<head>
    <title></title>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>


    <%--<meta content="video.movie" property="og:type"/>--%>
</head>

<body>
<div class="container content">
    <div class="row blog-page">
        <div class="col-md-9">
            <div class="blog margin-bottom-40">
                <div class="row">
                    <div class="headline headline-md"><h2>Lịch thi đấu World Cup 2014 Brazil</h2></div>
                    <div class="fb-like" data-href="http://cuoi5s.com/lich-thi-dau-world-cup-2014-brazil.wpt" data-layout="standard" data-action="like" data-show-faces="true" data-share="true"></div>
                    <div data-type=timetable data-id="23128" id="wgt-23128" class="tap-sport-tools" style="width:800px; height:auto;"></div>
                    <div id="wgt-ft-23128" style="width:796px;"><p>Football results provided by <a href="http://www.whatsthescore.com/football/international/world-cup/" target="_blank" rel="nofollow"><img src="http://medias.whatsthescore.com/upload/logo-s.png" alt="whatsthescore.com" /></a></p></div><style type="text/css">#wgt-ft-23128  {background:#FFFFFF !important;color:#484848 !important;text-decoration:none !important;padding:4px 2px !important;margin:0 !important;}#wgt-ft-23128 * {font:10px Arial !important;}#wgt-ft-23128 a {color:#484848 !important;}#wgt-ft-23128 img {vertical-align:bottom !important;height:15px !important;}</style><script type="text/javascript" src="http://tools.whatsthescore.com/load.min.js?207"></script>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="blog margin-bottom-40">
                <div class="row">
                    <div class="fb-like-box" data-href="http://www.facebook.com/cuoi5s" data-colorscheme="light" data-show-faces="false" data-header="true" data-stream="false" data-show-border="true"></div>
                </div>
                <!--div class="row">
                    <div class="headline headline-md"><h2>Bảng xếp hạng</h2></div>
                    <div data-type=standing data-id="23133" id="wgt-23133" class="tap-sport-tools" style="width:300px; height:auto;"></div>
                    <div id="wgt-ft-23133" style="width:296px;"><p>Widget powered by <a target="_blank" rel="nofollow" href="http://www.whatsthescore.com">WhatstheScore.com</a></p></div><style type="text/css">#wgt-ft-23133  {background:#FFFFFF !important;color:#484848 !important;text-decoration:none !important;padding:4px 2px !important;margin:0 !important;}#wgt-ft-23133 * {font:10px Arial !important;}#wgt-ft-23133 a {color:#484848 !important;}#wgt-ft-23133 img {vertical-align:bottom !important;height:15px !important;}</style><script type="text/javascript" src="http://tools.whatsthescore.com/load.min.js?207"></script>
                </div-->
            </div>

        </div>

    </div>
    <!--/row-->
</div>

</body>
</html>
</app:cache>