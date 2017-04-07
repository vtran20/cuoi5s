<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mạng Chia Sẻ</title>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_css.css" type="text/css"/>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_${template.templateColorCode}.css" type="text/css"/>
    <h:head/>
    <meta content="Tìm kiếm các video đang trực tiếp trên youtube. Các trận đấu bóng đá giải ngoại hạng Anh, Tây Ban Nha, Đức, Ý và các kênh giải trí khác" name="description">
    <style>
        div.container.content {min-height: 400px;}
        .search-block {
            margin-bottom: 10px;
            padding: 20px 0 28px;
            border-bottom: 1px solid #eee;
        }
    </style>
</head>

<body>
<div id="fb-root"></div>
<script>(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&appId=6753221261&version=v2.0";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>
<div class="wrapper">

<div class="header">
<!-- Topbar -->
<div class="topbar">
    <div class="container">
        <!-- Topbar Navigation -->
        <ul class="loginbar pull-left">
            <li><a href="/"><img id="logo-header" src="/assets/images/live_youtube_logo.png" alt="Youtube live"></a></li>
            <li><div class="fb-like" data-href="http://mangchiase.com" data-layout="button_count" data-action="like" data-show-faces="false" data-share="true"></div></li>
        </ul>
        <!-- End Topbar Navigation -->
    </div>
</div>
<!-- End Topbar -->
</div>

    <!--=== Search Block ===-->
    <div class="search-block">
        <div class="container">
            <div class="col-md-6 col-md-offset-3">
                <h2>Tìm kiếm video đang trực tiếp trên youtube</h2>
                <div class="input-group">
                    <input type="text" name="query" id="query" class="form-control" placeholder="Nhập nội dung cần tìm: Manchester, The voice kids ..." maxlength="50">
                    <span class="input-group-btn">
                        <button class="btn-u" type="button" id="search-button"><i class="fa fa-search"></i></button>
                    </span>
                </div>
            </div>
        </div>
    </div><!--/container-->
    <!--=== End Search Block ===-->


    <div class="container content">
    <div class="row blog-page" id="search-result">
    <div class="col-md-12">

    </div>
    <!-- Right Sidebar -->
    <%--<div class="col-md-3 magazine-page">--%>
        <%--<div class="headline headline-md"><h4>Like để cập nhật những video vui nhộn.</h4></div>--%>
            <%--<div class="fb-like-box" data-href="http://www.facebook.com/cuoi5s" data-colorscheme="light" data-show-faces="false" data-header="true" data-stream="false" data-show-border="true"></div>--%>
    <%--</div>--%>
    <!-- End Right Sidebar -->
    </div>
    </div>

    <!--=== Copyright ===-->
    <div class="copyright">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <p>
                        2014 &copy; Mạng Chia Sẻ. ALL Rights Reserved.
                        <a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a>
                    </p>
                </div>
                <div class="col-md-6">
                    <a href="http://cuoi5s.com">
                        <img class="pull-right" id="logo-footer" src="http://cuoi5s.com/themes/m3x/img/cuoi-5s.png" alt="">
                    </a>
                    <%--<div class="fb-like-box" data-href="http://www.facebook.com/cuoi5s" data-colorscheme="light" data-show-faces="false" data-header="true" data-stream="false" data-show-border="true"></div>--%>
                </div>
            </div>
        </div>
    </div><!--/copyright-->
    <!--=== End Copyright ===-->
</div><!--/End Wrapepr-->

<script language="javascript">

</script>
<script src="/wro/${version}${template.templateCssCode}_js.js" type="text/javascript"></script>

<script type="text/javascript">
    jQuery(document).ready(function() {
        App.init();
//        App.initSliders();
//        Index.initParallaxSlider();
//        App.initBxSlider();
//        PortfolioPage.init();
    });

//    $(function () {
//
//    });

</script>
<!--[if lt IE 9]>
<script src="/themes/m3x/plugins/respond.js"></script>
<![endif]-->

</body>
</html>
</app:cache>