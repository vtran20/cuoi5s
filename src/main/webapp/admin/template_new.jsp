<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><decorator:title/>: Management System</title>

    <link rel="stylesheet" href="/wro/${version}admin_css.css" type="text/css"/>
    <script src="/wro/${version}admin_js.js" type="text/javascript"></script>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <spring:eval expression="site.siteParamsMap.get('LOGO_ICO')" var="ico"/>
    <link rel="SHORTCUT ICON" href="${ico}" title="${site.domain}">

</head>

<body>

<!-- Top navigation bar -->
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="/admin">WEBPHATTAI</a>
            <%--<div class="btn-group pull-right">--%>
            <%--<a class="btn dropdown-toggle" data-toggle="dropdown" href="http://wbpreview.com/previews/WB00958H8/index.html#">--%>
            <%--<i class="icon-user"></i> John Doe--%>
            <%--<span class="caret"></span>--%>
            <%--</a>--%>
            <%--<ul class="dropdown-menu">--%>
            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Profile</a></li>--%>
            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Settings</a></li>--%>
            <%--<li><a class="cookie-delete" href="http://wbpreview.com/previews/WB00958H8/index.html#">Delete Cookies</a></li>--%>
            <%--<li class="divider"></li>--%>
            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/login.html">Logout</a></li>--%>
            <%--</ul>--%>
            <%--</div>--%>
            <div class="nav-collapse">
                <ul class="nav">
                    <%--<li class="dropdown">--%>
                        <%--<a href="http://wbpreview.com/previews/WB00958H8/index.html#" class="dropdown-toggle"--%>
                           <%--data-toggle="dropdown">--%>
                            <%--Messages <span class="label label-info">100</span>--%>
                            <%--<b class="caret"></b>--%>
                        <%--</a>--%>
                        <%--<ul class="dropdown-menu">--%>
                            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Message 1</a></li>--%>
                            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Another message</a></li>--%>
                            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Something else message</a>--%>
                            <%--</li>--%>
                            <%--<li class="divider"></li>--%>
                            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Older messages...</a></li>--%>
                        <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="dropdown">--%>
                        <%--<a href="http://wbpreview.com/previews/WB00958H8/index.html#" class="dropdown-toggle"--%>
                           <%--data-toggle="dropdown">--%>
                            <%--Settings--%>
                            <%--<b class="caret"></b>--%>
                        <%--</a>--%>
                        <%--<ul class="dropdown-menu">--%>
                            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Personal Info</a></li>--%>
                            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Preferences</a></li>--%>
                            <%--<li><a href="http://wbpreview.com/previews/WB00958H8/index.html#">Alerts</a></li>--%>
                            <%--<li><a class="cookie-delete" href="http://wbpreview.com/previews/WB00958H8/index.html#">Delete--%>
                                <%--Cookies</a></li>--%>
                        <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="dropdown">--%>
                        <%--<a href="http://wbpreview.com/previews/WB00958H8/index.html#" class="dropdown-toggle"--%>
                           <%--data-toggle="dropdown">--%>
                            <%--Theme--%>
                            <%--<b class="caret"></b>--%>
                        <%--</a>--%>
                        <%--<ul class="dropdown-menu">--%>
                            <%--<li><a class="theme-switch-default"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Default</a></li>--%>
                            <%--<li><a class="theme-switch-amelia"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Amelia</a></li>--%>
                            <%--<li><a class="theme-switch-cerulean"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Cerulean</a></li>--%>
                            <%--<li><a class="theme-switch-journal"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Journal</a></li>--%>
                            <%--<li><a class="theme-switch-readable"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Readable</a></li>--%>
                            <%--<li><a class="theme-switch-simplex"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Simplex</a></li>--%>
                            <%--<li><a class="theme-switch-slate"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Slate</a></li>--%>
                            <%--<li><a class="theme-switch-spacelab"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Spacelab</a></li>--%>
                            <%--<li><a class="theme-switch-spruce"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Spruce</a></li>--%>
                            <%--<li><a class="theme-switch-superhero"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">Superhero</a></li>--%>
                            <%--<li><a class="theme-switch-united"--%>
                                   <%--href="http://wbpreview.com/previews/WB00958H8/index.html#">United</a></li>--%>
                        <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li><a href="/"><i class="icon-white icon-wrench"></i> <fmt:message key="template.admin.design"/></a></li>--%>
                    <li><a href="#"><i class="icon-white icon-question-sign"></i> <fmt:message key="template.admin.help"/></a></li>
                    <li><a href="/admin/j_spring_security_logout"><i class="icon-white icon-off"></i> <fmt:message key="template.admin.logout"/></a></li>
                </ul>
            </div>
            <!--/.nav-collapse -->
        </div>
    </div>
</div>
<!-- Main Content Area | Side Nav | Content -->
<div class="container-fluid">
<div class="row-fluid">
    <!-- Side Navigation -->
    <div class="span2">
        <div class="member-box round-all">
            <a><img src="" class="member-box-avatar"></a>
        <span>
            <strong>Administrator</strong><br>
            <sec:authentication property="principal.username" var="username"/>
            <a>${username}</a><br>
            <span class="member-box-links"><a href="#"><fmt:message key="template.admin.profile"/></a> | <a href="/admin/j_spring_security_logout"><fmt:message key="template.admin.logout"/></a></span>
        </span>
        </div>
        <div class="sidebar-nav">
            <div class="" >
                <ul class="nav nav-tabs nav-stacked">
                    <sec:authorize ifAnyGranted="ROLE_SITE_CONTENT_ADMIN">
                        <li class="nav-header"><fmt:message key="template.admin.dashboard"/></li>
                        <spring:eval expression="serviceLocator.siteTemplateDao.findUniqueBy('site.id',site.id)" var="website"/>
                        <c:if test="${empty website}">
                            <li><a href="/admin/sites/create.html"><i class="icon-file"></i> <fmt:message key="template.admin.create.new.website"/></a></li>
                        </c:if>
                        <c:if test="${! empty website}">
                            <li><a href="/admin/sites/create.html"><i class="icon-edit"></i> <fmt:message key="template.admin.update.website"/></a></li>
                        </c:if>
                        <li><a href="/admin/sites/header.html"><i class="icon-list"></i> <fmt:message key="template.admin.header"/></a></li>
                        <li><a href="/admin/sites/footer.html"><i class="icon-list"></i> Footer</a></li>
                        <li class=""><a href="/admin/menu/index.html"><i class="icon-list-alt"></i> <fmt:message key="template.admin.menus"/></a></li>
                        <%--<li class=""><a href="/admin/page/index.html"><i class="icon-list-alt"></i> <fmt:message key="template.admin.pages"/></a></li>--%>
                        <li class=""><a href="/admin/gallery/index.html"><i class="icon-list-alt"></i> <fmt:message key="template.admin.gallery"/></a></li>

                        <li class="nav-header"><fmt:message key="template.admin.news"/></li>
                        <li class=""><a href="/admin/news/create.html"><i
                                class="icon-file"></i> <fmt:message key="template.admin.create.news"/></a></li>
                        <li class=""><a href="/admin/news/news.html"><i
                                class="icon-align-justify"></i> <fmt:message key="template.admin.news.list"/></a></li>
                        <li class=""><a href="/admin/news/index.html"><i
                                class="icon-list-alt"></i> <fmt:message key="template.admin.news.category"/></a></li>
                    </sec:authorize>
                    <sec:authorize ifAnyGranted="ROLE_SITE_VIDEO_ADMIN">
                        <li class="nav-header"><fmt:message key="template.admin.videos.manage"/></li>
                        <li class=""><a href="/admin/video/video_import.html"><i
                                class="icon-file"></i> <fmt:message key="template.admin.youtube.video.import"/></a></li>
                    </sec:authorize>
                    <sec:authorize ifAnyGranted="ROLE_SYSTEM_ADMIN">
                        <li class="nav-header"><fmt:message key="template.admin.categories"/></li>
                        <li class=""><a href="/admin/catalog/product_create.html"><i
                                class="icon-file"></i> <fmt:message key="template.admin.create.product"/></a></li>
                        <li class=""><a href="/admin/catalog/products.html"><i
                                class="icon-align-justify"></i> <fmt:message key="template.admin.products.list"/></a></li>
                        <li class=""><a href="/admin/catalog/index.html"><i
                                class="icon-list-alt"></i> <fmt:message key="template.admin.category"/></a></li>
                    </sec:authorize>
                    <sec:authorize ifAnyGranted="ROLE_SITE_CONTENT_ADMIN">
                        <li class="nav-header"><fmt:message key="template.admin.setting"/></li>
                        <li><a href="/admin/sites/contactus_index.html"><i class="icon-wrench"></i> <fmt:message key="template.admin.contact.us"/></a></li>
                        <li><a href="/admin/sites/support_index.html"><i class="icon-headphones"></i> <fmt:message key="template.admin.customer.service"/></a></li>
                        <li><a href="/admin/sites/setting/question_answer_index.html"><i class="icon-question-sign"></i> <fmt:message key="template.admin.question.answer"/></a></li>
                        <li class="nav-header"><fmt:message key="template.admin.images"/></li>
                        <li class=""><a href="/admin/images/index.html"><i
                                class="icon-upload"></i> <fmt:message key="template.admin.upload.images"/></a></li>
                        <li class=""><a href="/admin/images/view.html"><i
                                class="icon-picture"></i> <fmt:message key="template.admin.view.images"/></a></li>
                    </sec:authorize>

                <%--<li class="nav-header">Thiết lập Website</li>--%>
                    <%--<li class="nav-header">Settings</li>--%>
                    <%--<li><a class="sidenav-style-1" href="http://wbpreview.com/previews/WB00958H8/index.html#"><i--%>
                            <%--class="icon-align-left"></i> Side Menu Style 1</a></li>--%>
                    <%--<li><a class="sidenav-style-2" href="http://wbpreview.com/previews/WB00958H8/index.html#"><i--%>
                            <%--class="icon-align-right"></i> Side Menu Style 2</a></li>--%>
                    <%--<li><a href="/admin/j_spring_security_logout"><i class="icon-off"></i> Logout</a>--%>
                    </li>
                </ul>
            </div>
        </div>
        <!--/.well -->
    </div>
    <!--/span-->

    <decorator:body/>

    <!--/row-->


    <div id="box-config-modal" class="modal hide fade in" style="display: none;">
        <div class="modal-header">
            <button class="close" data-dismiss="modal">×</button>
            <h3>Adjust widget</h3>
        </div>
        <div class="modal-body">
            <p>This part can be customized to set box content specifix settings!</p>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary" data-dismiss="modal"><fmt:message key="common.save.changes"/></a>
            <a href="#" class="btn" data-dismiss="modal"><fmt:message key="common.cancel"/></a>
        </div>
    </div>
</div>
<!--/.fluid-container-->
<footer>
    <hr>
    <p class="pull-right">© WEBPHATTAI 2013</p>
</footer>

    <!-- javascript Templates
   ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<c:url var="uri" value="${uri}"/>
<script type="text/javascript">
    $(document).ready(function () {
        $('div.sidebar-nav ul.nav li a').each(function(index) {
            if ($(this).attr("href") == '${uri}') {
                $(this).closest("li").addClass("active");
            } else {
                //TODO
            }
        });
    });

</script>

<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE8+ -->
<!--[if gte IE 8]>
<script src="/admin/assets/admin_new/js/jquery.xdr-transport.js"></script>
<![endif]-->
</div>

</body>
</html>
