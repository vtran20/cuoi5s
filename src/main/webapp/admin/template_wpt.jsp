<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>System Administrator: <decorator:title/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <!--[if !IE]> -->
    <%--<link rel="stylesheet" href="/admin/assets/admin_wpt/css/pace.min.css" />--%>
    <%--<script data-pace-options='{ "ajax": true, "document": true, "eventLag": false, "elements": false }' src="/admin/assets/admin_wpt/js/pace.min.js"></script>--%>
    <!-- <![endif]-->

    <link rel="stylesheet" href="/wro/${version}admin_wpt_css.css" type="text/css"/>
    <link rel="stylesheet" href="/admin/assets/admin_wpt/css/jquery.dataTables.min.css">
    <%--<link rel="stylesheet" href="/admin/assets/admin_wpt/css/dataTables.bootstrap.css">--%>

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/admin/assets/admin_wpt/css/ace-part2.min.css"/>
    <![endif]-->
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/admin/assets/admin_wpt/css/ace-ie.min.css"/>
    <![endif]-->
    <!-- inline styles related to this page -->

    <!-- ace settings handler - is used for left menu -->
    <script src="/admin/assets/admin_wpt/js/ace-extra.min.js"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="/admin/assets/admin_wpt/js/html5shiv.js"></script>
    <script src="/admin/assets/admin_wpt/js/respond.min.js"></script>
    <![endif]-->

    <!-- basic scripts -->
    <!--[if !IE]> -->
    <script type="text/javascript">
        window.jQuery || document.write("<script src='/admin/assets/admin_wpt/js/jquery.min.js'>" + "<" + "/script>");
    </script>
    <!-- <![endif]-->
    <!--[if IE]>
    <script type="text/javascript">
        window.jQuery || document.write("<script src='/admin/assets/admin_wpt/js/jquery1x.min.js'>" + "<" + "/script>");
    </script>
    <![endif]-->
    <script type="text/javascript">
        if ('ontouchstart' in document.documentElement) document.write("<script src='/admin/assets/admin_wpt/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
    </script>

    <script src="/wro/${version}admin_wpt_js.js" type="text/javascript"></script>

    <link  href="https://cdn.rawgit.com/fengyuanchen/cropper/v2.1.0/dist/cropper.min.css" rel="stylesheet">
    <script src="https://cdn.rawgit.com/fengyuanchen/cropper/v2.1.0/dist/cropper.min.js"></script>

    <!--[if lte IE 8]>
    <script src="/admin/assets/admin_wpt/js/excanvas.min.js"></script>
    <![endif]-->

    <script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/2.9.0/moment.min.js"></script>
    <!-- Include Date Range Picker -->
    <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/1/daterangepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/1/daterangepicker-bs3.css" />

    <%--Adding font for VN--%>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300italic&subset=latin,vietnamese,greek,cyrillic,cyrillic-ext,greek-ext,latin-ext' rel='stylesheet' type='text/css'>
</head>

<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default">
<script type="text/javascript">
    try {
        ace.settings.check('navbar', 'fixed')
    } catch (e) {
    }
</script>

<div class="navbar-container" id="navbar-container">
<!-- #section:basics/sidebar.mobile.toggle -->
<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
    <span class="sr-only">Toggle sidebar</span>

    <span class="icon-bar"></span>

    <span class="icon-bar"></span>

    <span class="icon-bar"></span>
</button>

<!-- /section:basics/sidebar.mobile.toggle -->
<div class="navbar-header pull-left">
    <!-- #section:basics/navbar.layout.brand -->
    <a href="/admin" class="navbar-brand">
        <small>
            <i class="fa fa-desktop"></i>
            Quản Lý Website
        </small>
    </a>

    <!-- /section:basics/navbar.layout.brand -->

    <!-- #section:basics/navbar.toggle -->

    <!-- /section:basics/navbar.toggle -->
</div>

<!-- #section:basics/navbar.dropdown -->
<spring:eval expression="serviceLocator.contactUsDao.findBy('read', 'N', site.id)" var="contactMessages" />
<spring:eval expression="serviceLocator.orderDao.countOrders('NEW_ORDER', null, null, site)" var="numberOrder" />
<div class="navbar-buttons navbar-header pull-right" role="navigation">
<ul class="nav ace-nav">
    <li class="purple">
        <a href="#/admin/order/index" data-url="/admin/order/index">
            <i class="ace-icon fa fa-shopping-cart"></i>
            <span class="badge badge-important">${numberOrder}</span>
        </a>

    </li>
    <li class="green">
        <a data-toggle="dropdown" class="dropdown-toggle" href="#" aria-expanded="false">
            <i class="ace-icon fa fa-envelope"></i>
            <span class="badge badge-success">${fn:length(contactMessages)}</span>
        </a>

        <ul class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
            <li class="dropdown-header">
                <i class="ace-icon fa fa-envelope-o"></i>
                ${fn:length(contactMessages)} <fmt:message key="contact.message"/>
            </li>
            <c:if test="${!empty contactMessages}">
                <li class="dropdown-content">
                <ul class="dropdown-menu dropdown-navbar">
                <c:forEach items="${contactMessages}" var="message">
                    <li id="message_${message.id}">
                    <span class="msg-body">
                        <span class="msg-title">
                            <span class="blue">${message.firstName}:</span>
                            ${message.sendersEmail}
                        </span>
                        <span class="msg-time">
                            <c:set value="${message.message}" var="textMessage"/>
                            <c:set value="${fn:length(message.message)}" var="contentLength"/>
                            <c:if test="${contentLength > 100}">
                                <c:set value="${fn:substring(message.message, 0, 100)} ..." var="textMessage"/>
                            </c:if>
                            <span>${textMessage}</span>
                        </span>
                        <a href="#" class="show-confirm">Delete</a>
                    </span>
                    </li>
                </c:forEach>
                </ul>
                </li>
            </c:if>
            <li class="dropdown-footer">
                <a href="#/admin/customer/index" data-url="/admin/customer/index">
                    <fmt:message key="customer.view.all"/>
                    <i class="ace-icon fa fa-arrow-right"></i>
                </a>
            </li>
        </ul>
    </li>
    <%--<!-- #section:basics/navbar.user_menu -->--%>
<li class="light-blue">
    <a href="/admin/j_spring_security_logout"><i class="ace-icon fa fa-power-off"></i>
        <fmt:message key="template.admin.logout"/></a>
    </a>
</li>

<%--<!-- /section:basics/navbar.user_menu -->--%>
</ul>
</div>

<!-- /section:basics/navbar.dropdown -->
</div>
<!-- /.navbar-container -->
</div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
<script type="text/javascript">
    try {
        ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
</script>

<!-- #section:basics/sidebar -->
<div id="sidebar" class="sidebar responsive menu-min">
<script type="text/javascript">
    try {
        ace.settings.check('sidebar', 'fixed')
    } catch (e) {
    }
</script>
<div class="sidebar-shortcuts" id="sidebar-shortcuts">
    <div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
        <button class="btn btn-success">
            <i class="ace-icon fa fa-signal"></i>
        </button>

        <button class="btn btn-info">
            <i class="ace-icon fa fa-pencil"></i>
        </button>

        <!-- #section:basics/sidebar.layout.shortcuts -->
        <button class="btn btn-warning">
            <i class="ace-icon fa fa-users"></i>
        </button>

        <button class="btn btn-danger">
            <i class="ace-icon fa fa-cogs"></i>
        </button>

        <!-- /section:basics/sidebar.layout.shortcuts -->
    </div>

    <div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
        <span class="btn btn-success"></span>

        <span class="btn btn-info"></span>

        <span class="btn btn-warning"></span>

        <span class="btn btn-danger"></span>
    </div>
</div>
<!-- /.sidebar-shortcuts -->
<ul class="nav nav-list">
<li class="">
    <a href="/admin">
        <i class="menu-icon fa fa-home"></i>
        <span class="menu-text"> Dashboard </span>
    </a>

    <b class="arrow"></b>
</li>

<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-cogs"></i>
        <span class="menu-text"> Cấu Hình</span>
        <b class="arrow fa fa-angle-down"></b>
    </a>
    <b class="arrow"></b>
    <ul class="submenu">
        <li class="">
            <a href="#/admin/sites/form" data-url="/admin/sites/form">
                <i class="menu-icon fa fa-caret-right"></i>
                Thông Tin WebSite
            </a>
            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="#/admin/sites/site_setting" data-url="/admin/sites/site_setting">
                <i class="menu-icon fa fa-caret-right"></i>
                Thiết Lập Cấu Hình
            </a>
            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="#/admin/sites/contactus_index" data-url="/admin/sites/contactus_index">
                <i class="menu-icon fa fa-caret-right"></i>
                Thông Tin Liên Hệ
            </a>

            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="#/admin/sites/support_index" data-url="/admin/sites/support_index">
                <i class="menu-icon fa fa-caret-right"></i>
                Hỗ Trợ Trực Tuyến
            </a>
            <b class="arrow"></b>
        </li>
    </ul>
</li>
<li class="">
    <a href="#/admin/sites/admin_user_index" data-url="/admin/sites/admin_user_index">
        <i class="menu-icon fa fa-user"></i>
        <span class="menu-text"> Quản Lý Tài Khoản </span>
    </a>
    <b class="arrow"></b>
</li>
<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-desktop"></i>
        <span class="menu-text"> Thiết Kế WebSite</span>

        <b class="arrow fa fa-angle-down"></b>
    </a>
    <b class="arrow"></b>
    <ul class="submenu">
        <li class="">
            <a href="#/admin/sites/header" data-url="/admin/sites/header">
                <i class="menu-icon fa fa-caret-right"></i>
                Header (Logo)
            </a>
            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="#/admin/menu/index" data-url="/admin/menu/index">
                <i class="menu-icon fa fa-caret-right"></i>
                Menu và Trang Web
            </a>
            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="#/admin/sites/footer" data-url="/admin/sites/footer">
                <i class="menu-icon fa fa-caret-right"></i>
                Footer
            </a>
            <b class="arrow"></b>
        </li>

    </ul>
</li>

<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-file-text-o"></i>
        <span class="menu-text"> Nội Dung</span>

        <b class="arrow fa fa-angle-down"></b>
    </a>
    <b class="arrow"></b>
    <ul class="submenu">
        <li class="">
            <a href="#" class="dropdown-toggle">
                <i class="menu-icon fa fa-caret-right"></i>
                Trang Tin Tức
                <b class="arrow fa fa-angle-down"></b>
            </a>
            <b class="arrow"></b>
            <ul class="submenu">
                <li class="">
                    <a href="#/admin/news/create" data-url="/admin/news/create">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Tạo Tin Mới
                    </a>
                    <b class="arrow"></b>
                </li>
                <li class="">
                    <a href="#/admin/news/news" data-url="/admin/news/news">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Danh Sách Tin
                    </a>
                    <b class="arrow"></b>
                </li>
                <li class="">
                    <a href="#/admin/news/index" data-url="/admin/news/index">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Danh Mục Tin
                    </a>
                    <b class="arrow"></b>
                </li>
            </ul>
        </li>
        <li class="">
            <a href="#/admin/gallery/index" data-url="/admin/gallery/index">
                <i class="menu-icon fa fa-caret-right"></i>
                Gallery (Album)
            </a>
            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="#/admin/images/view" data-url="/admin/images/view">
                <i class="menu-icon fa fa-caret-right"></i>
                Thư Viện Hình Ảnh
            </a>
            <b class="arrow"></b>
        </li>
    </ul>
</li>

<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-cube"></i>
        <span class="menu-text"> Sản Phẩm </span>
        <b class="arrow fa fa-angle-down"></b>
    </a>

    <b class="arrow"></b>

    <ul class="submenu">
        <%--<li class="">--%>
            <%--<a href="#/admin/catalog/product" data-url="/admin/catalog/product">--%>
                <%--<i class="menu-icon fa fa-caret-right"></i>--%>
                <%--Thêm Sản Phẩm--%>
            <%--</a>--%>

            <%--<b class="arrow"></b>--%>
        <%--</li>--%>

        <li class="">
            <a href="#/admin/catalog/products" data-url="/admin/catalog/products">
                <i class="menu-icon fa fa-caret-right"></i>
                Danh Sách Sản Phẩm
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="#/admin/catalog/categories" data-url="/admin/catalog/categories">
                <i class="menu-icon fa fa-caret-right"></i>
                Danh Mục Sản Phẩm
            </a>

            <b class="arrow"></b>
        </li>

    </ul>
</li>
<li class="">
    <a href="#/admin/order/index" data-url="/admin/order/index">
    <i class="menu-icon fa fa-shopping-cart"></i>
        <span class="menu-text"> Đơn Hàng </span>
    </a>
    <b class="arrow"></b>
</li>

<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-users"></i>
        <span class="menu-text"> Khách Hàng</span>
        <b class="arrow fa fa-angle-down"></b>
    </a>
    <b class="arrow"></b>
    <ul class="submenu">
        <li class="">
            <a href="#/admin/customer/index" data-url="/admin/customer/index">
                <i class="menu-icon fa fa-caret-right"></i>
                Tin Nhắn
            </a>
            <b class="arrow"></b>
        </li>
    </ul>
</li>
<sec:authorize ifAnyGranted="ROLE_SITE_PARTNER_ADMIN">
<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-random"></i>
        <span class="menu-text"> Partner</span>
        <b class="arrow fa fa-angle-down"></b>
    </a>
    <b class="arrow"></b>
    <ul class="submenu">
        <li class="">
            <a href="#/admin/partner/site_client" data-url="/admin/partner/site_client">
                <i class="menu-icon fa fa-caret-right"></i>
                Site Client
            </a>
            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="#/admin/partner/site_template" data-url="/admin/partner/site_template">
                <i class="menu-icon fa fa-caret-right"></i>
                Site Templates
            </a>
            <b class="arrow"></b>
        </li>
    </ul>
</li>
</sec:authorize>
</ul>
<!-- /.nav-list -->

<!-- #section:basics/sidebar.layout.minimize -->
<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
    <i class="ace-icon fa fa-angle-double-right" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
</div>

<!-- /section:basics/sidebar.layout.minimize -->
<script type="text/javascript">
    try {
        ace.settings.check('sidebar', 'collapsed')
    } catch (e) {
    }
</script>
</div>

<!-- /section:basics/sidebar -->
<div class="main-content">
    <%--<div class="main-content-inner">--%>
    <%--</div>--%>
        <div class="breadcrumbs" id="breadcrumbs">
            <%--<script type="text/javascript">--%>
                <%--try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}--%>
            <%--</script>--%>
            <ul class="breadcrumb">
                <li>
                    <a href="/admin"><i class="ace-icon fa fa-home home-icon"></i></a>
                </li>
            </ul><!-- /.breadcrumb -->
        </div>
        <div class="page-content">
            <%--<decorator:body/>--%>
            <div class="page-content-area" id="page-content-area" data-ajax-content="true">
                <!-- ajax content goes here -->
            </div><!-- /.page-content-area -->
        </div>
</div>
<!-- /.main-content -->

<div class="footer">
    <div class="footer-inner">
        <!-- #section:basics/footer -->
        <div class="footer-content">
						<span class="bigger-120">
							<span class="blue bolder">WebPhatTai</span>
							&copy; 2015
						</span>
        </div>

        <!-- /section:basics/footer -->
    </div>
</div>

<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
    <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a>
</div>
<!-- /.main-container -->
<script>
    $('[data-ajax-content=true]').ace_ajax({
        content_url: function(hash) {
            //hash is the value from document url hash
            //take "url" param and return the relevant url to load
            fullUrl = "";
            if (hash) {
                arr = hash.split("?");
                if (arr.length > 1) {
                    fullUrl = arr[0] + ".html?"+arr[1];
                } else {
                    fullUrl = hash+".html";
                }
            }
            return fullUrl;
        }
        ,default_url: '/admin/dashboard'//default url
//        ,loading_icon: "fa-cog fa-2x blue"

    });
</script>
</body>
</html>
