<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<html>
<head>
    <title>Website is out of service</title>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_css.css" type="text/css"/>
        <%--(red, blue, orange, light, purple, aqua, brown, dark-blue, light-green, green)--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_${template.templateColorCode}.css" type="text/css"/>
    <link rel="stylesheet" href="/themes/m3x/css/theme-skins/dark.css" type="text/css"/>
    <script src="/wro/${version}${template.templateCssCode}_js.js" type="text/javascript"></script>
    <meta name="description" content="Website is out of service"/>
    <meta name="keywords" content=""/>
</head>
<body>


<!--=== Error V3 ===-->
<div class="container content">
    <!-- Error Block -->
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="error-v3">
                <h2>404</h2>
                <p>Sorry, the page you were looking for could not be found!</p>
            </div>
        </div>
    </div>
    <!-- End Error Block -->

    <!-- Begin Service Block V2 -->
    <div class="row service-block-v2">
        <div class="col-md-4">
            <div class="service-block-in service-or">
                <div class="service-bg"></div>
                <i class="icon-bulb"></i>
                <h4>Not what you were looking for?</h4>
                <p>If the page is not what you are looking for, try searching the page on the search page and find out new things.</p>
                <a class="btn-u btn-brd btn-u-light" href="http://${site.domain}"> Discover More</a>
            </div>
        </div>

        <div class="col-md-4">
            <div class="service-block-in service-or">
                <div class="service-bg"></div>
                <i class="icon-directions"></i>
                <h4>Possible cause of the problem</h4>
                <p>The page you requested could not be found. However, the requested resource may be available again in the future.</p>
                <a class="btn-u btn-brd btn-u-light" href="http://${site.domain}"> Go back to Home Page</a>
            </div>
        </div>

        <div class="col-md-4">
            <div class="service-block-in service-or">
                <div class="service-bg"></div>
                <i class="icon-users"></i>
                <h4>Contact us</h4>
                <p>If you have a problem with the website, please contact us, our support team will help you to solve the problem.</p>
                <a class="btn-u btn-brd btn-u-light" href="http://${site.domain}"> Contact Us</a>
            </div>
        </div>
    </div>
    <!-- End Service Block V2 -->
</div>
<!--=== End Error-V3 ===-->

<!--=== Sticky Footer ===-->
<div class="container sticky-footer">
    <p class="copyright-space">
        <jsp:useBean id="now" class="java.util.Date" />
        <fmt:formatDate var="year" value="${now}" pattern="yyyy" />
        ${year} &copy; ${site.domain}. ALL Rights Reserved.
    </p>
</div>
<!--=== End Sticky-Footer ===-->
<script type="text/javascript">
    $.backstretch([
        "/themes/m3x/img/blur/img1.jpg"
    ])
</script>
</body>
</html>