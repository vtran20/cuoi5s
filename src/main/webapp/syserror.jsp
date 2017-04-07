<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%--<spring:eval expression="serviceLocator.getSiteDao().getSiteByServerName(pageContext.request.serverName)" var="site" />--%>

<html>
<head>
    <title>Không tìm thấy trang web</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="error"/>
    <!-- CSS Global Compulsory -->
    <link rel="stylesheet" href="/themes/m3x/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/themes/m3x/css/style.css">
    <!-- CSS Page Style -->
    <link rel="stylesheet" href="/themes/m3x/css/pages/page_error4_404.css">
    <style>
        .error-v4 h1 {
            color: #585f69;
        }
        .error-v4 span.sorry {
            color: #585f69;
        }
        .btn-u.btn-brd.btn-u-light {
            color: #585f69;
            border-color: #585f69;
        }
        .sticky-footer .copyright-space {
            color: #585f69;
        }
    </style>
</head>

<body style="background-image: url('/assets/images/bg.gif'); background-color: #cccccc;">
<!--=== Error V4 ===-->
<div class="container content">
    <!--Error Block-->
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="error-v4">
                <%--<a href="#"><img src="http://htmlstream.com/preview/unify-v1.6-production/assets/img/logo2-default.png" alt=""></a>--%>
                <h1>404</h1>
                <%--<span class="sorry">Sorry, the page you were looking for could not be found!</span>--%>
                <span class="sorry">Xin lỗi, Chúng tôi không tìm thấy trang web quý khách yêu cầu!</span>
                <div class="row">
                    <div class="col-md-6 col-md-offset-3">
                        <a class="btn-u btn-brd btn-u-light" href="/"> Quay Lại Trang Chủ</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div><!--/container-->
<!--End Error Block-->

<!--=== Sticky Footer ===-->
<div class="container sticky-footer">
    <p class="copyright-space">
        2015 &copy; WebPhatTai.com
    </p>
</div>
</body>
</html>