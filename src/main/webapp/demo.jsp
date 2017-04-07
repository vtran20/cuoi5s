<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<head>
    <%--<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>--%>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Demo</title>

    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_css.css" type="text/css"/>
    <%--header--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_header-default.css" type="text/css"/>
    <%--Footer--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_footer-v1.css" type="text/css"/>
    <link rel="stylesheet" href="/themes/m3x/css/demo.css">
    <%--(red, blue, orange, light, purple, aqua, brown, dark-blue, light-green, green)--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_${template.templateColorCode}.css" type="text/css"/>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_${template.skinColor}-skin.css" type="text/css"/>
    <script src="/wro/${version}${template.templateCssCode}_js.js" type="text/javascript"></script>
    <c:choose>
    <c:when test="${language == 'vi-VN'}"><script src='//www.google.com/recaptcha/api.js?hl=vi'></script></c:when>
    <c:otherwise><script src='//www.google.com/recaptcha/api.js'></script></c:otherwise>
    </c:choose>
<h:head/>
</head>
<body>
<div class="wrapper">
<script>
    $(document).ready(function(){
        $("ul.list-unstyled li a").click(function(){
             var object = $(this);
            newStyle = object.attr("data-style");
            $("ul.list-unstyled li a").removeClass("active");
            object.addClass("active");
            $("#webpage-iframe").removeClass();
            $("#webpage-iframe").addClass(newStyle);
        });
    });

</script>
    <div class="header">
        <div class="container">
            <!-- Topbar -->
            <div class="topbar">
                <div class="col-sm-6 style-switcher">
                    <div class="iframe-top-title"><fmt:message key="demo.support.device"/>: </div>
                    <ul class="list-unstyled">
                        <li><a class="active" href="javascript:void(0)" data-style="desktop"><i class="fa fa-desktop fa-2x"></i></a></li>
                        <li><a href="javascript:void(0)" data-style="tablet-flat"><i class="fa fa-tablet fa-2x fa-rotate-90"></i></a></li>
                        <li><a href="javascript:void(0)" data-style="tablet-stand"><i class="fa fa-tablet fa-2x"></i></a></li>
                        <li><a href="javascript:void(0)" data-style="mobile-flat"><i class="fa fa-mobile fa-2x fa-rotate-90"></i></a></li>
                        <li><a href="javascript:void(0)" data-style="mobile-stand"><i class="fa fa-mobile fa-2x"></i></a></li>
                    </ul>
                </div>
            </div>
            <!-- End Topbar -->

        </div><!--/end container-->
    </div>
    <div id="webpage-iframe" class="desktop">
        <iframe src="//${site.subDomain}" id="demoIframe"></iframe>
    </div>
</div>

</body>
</html>