<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" media="all" href="/wro/${version}admin_css.css"/>
    <spring:eval expression="site.siteParamsMap.get('THEME')" var="theme"/>
    <link rel="stylesheet" type="text/css" media="all" href="/wro/${version}${theme}_css.css"/>
    <script src="/wro/${version}admin_js.js" type="text/javascript"></script>
    <title>System Administrator: <decorator:title/></title>

    <spring:eval expression="site.siteParamsMap.get('LOGO_ICO')" var="ico"/>
    <link rel="SHORTCUT ICON" href="${ico}" title="${site.domain}">

</head>

<body>
<div class="container_16"><!-- Containing Block -->

    <!-- Start Header -->
    <div class="grid_16" id="topnav">
        <div><h1>Site: <c:out value="${site.domain}"/></h1></div>
        <div style="float: right;"><h3><a class="logout" href="#"><fmt:message key="site.logout"/></a></h3></div>
    </div>
    <!-- End Header -->
    <div class="glo-clr"></div>
    <!-- Start Body -->
    <div class="grid_16" id="body" style="width:100%">
        <!-- Start left Navigation -->
        <div class="grid_3" id="toc" style="width:10%; border-top: 2px solid rgb(255, 255, 255); float: left;">
            <script>
                $(function () {
                    var index = parseInt($.cookie("leftNavIndex"));
                    $("#accordion").accordion({
                        autoHeight:false,
                        navigation:true,
                        active:index
                    });

                    $('.logout').click(function () {
                        window.location.href = "/j_spring_security_logout";
                    });

                    $('#accordion h3').click(function () {
                        var index = $("#accordion h3").index(this);
                        $.cookie("leftNavIndex", index, { path:'/admin'});
                    });


                });
            </script>

            <div class="demo">
                <%--<div id="menu_leftnav"><sec:authorize url="/admin/index.html"><a href="/admin/index.html">Menu</a></sec:authorize></div>--%>
                <div id="accordion">
                    <sec:authorize ifAnyGranted="ROLE_SYSTEM_ADMIN">
                        <h3><a href="#">System</a></h3>
                        <ul>
                            <sec:authorize url="/admin/system/index.html">
                                <li><a href="/admin/system/index.html">Maintain</a></li>
                            </sec:authorize>
                            <sec:authorize url="/admin/sites/index.html">
                                <li><a href="/admin/sites/index.html">Sites</a></li>
                            </sec:authorize>
                            <sec:authorize url="/admin/caches/index.html">
                                <li><a href="/admin/caches/index.html">Caches</a></li>
                            </sec:authorize>
                        </ul>
                    </sec:authorize>
                    <sec:authorize ifAnyGranted="ROLE_SITE_ADMIN">
                        <h3><a href="#">Users</a></h3>
                        <ul>
                            <sec:authorize url="/admin/users/index.html">
                                <li><a href="/admin/users/index.html">Users</a></li>
                            </sec:authorize>
                        </ul>
                        <h3><a href="#">Catalogs</a></h3>
                        <ul>
                            <sec:authorize url="/admin/categories/index.html">
                                <li><a href="/admin/categories/index.html">Categories</a></li>
                            </sec:authorize>
                            <sec:authorize url="/admin/products/index.html">
                                <li><a href="/admin/products/index.html">Products</a></li>
                            </sec:authorize>
                        </ul>
                        <h3><a href="#">Content</a></h3>
                        <ul>
                            <sec:authorize url="/admin/content/treeview.html">
                                <li><a href="/admin/content/treeview.html">Assets</a></li>
                            </sec:authorize>
                            <sec:authorize url="/admin/content/treeview.html">
                                <li><a href="/admin/content/contents.html">Cms Content</a></li>
                            </sec:authorize>
                        </ul>
                        <h3><a href="#">Utility</a></h3>
                        <ul>
                            <sec:authorize url="/admin/utility/stringparam.html">
                                <li><a href="/admin/utility/stringparam.html">String Param</a></li>
                            </sec:authorize>
                        </ul>
                    </sec:authorize>
                    <sec:authorize ifAnyGranted="ROLE_SITE_CONTENT_ADMIN">
                        <h3><a href="#">Menus</a></h3>
                        <ul>
                            <sec:authorize url="/admin/menu/index.html">
                                <li><a href="/admin/menu/index.html">Menus</a></li>
                            </sec:authorize>
                        </ul>
                        <h3><a href="#">Pages</a></h3>
                        <ul>
                            <sec:authorize url="/admin/page/index.html">
                                <li><a href="/admin/page/index.html">Pages</a></li>
                            </sec:authorize>
                            <sec:authorize url="/admin/page/index.html">
                                <li><a href="/admin/page/index.html">directoris</a></li>
                            </sec:authorize>
                        </ul>
                        <h3><a href="#">Configuration</a></h3>
                        <ul>
                            <sec:authorize url="/admin/config/index.html">
                                <li><a href="/admin/config/index.html">Site setup</a></li>
                            </sec:authorize>
                        </ul>
                    </sec:authorize>
                    <sec:authorize ifAnyGranted="ROLE_MANAGE_ORDER">
                        <h3><a href="#">Manage Order</a></h3>
                        <ul>
                            <sec:authorize url="/admin/order/index.html">
                                <li><a href="/admin/order/index.html">Orders</a></li>
                            </sec:authorize>
                        </ul>
                    </sec:authorize>

                    <h3><a class="logout" href="#"><fmt:message key="site.logout"/></a></h3>
                </div>

            </div>

        </div>
        <!-- End left navigation -->

        <!-- Start Content -->
        <div class="grid_13" id="pagecontent">
            <decorator:body/></div>
        <!-- End Content -->

    </div>
    <!-- End Body -->
    <div class="glo-clr"></div>
    <!-- Start Footer -->
    <div class="grid_16" style="width:100%">
        Copyright 2010
    </div>
    <!-- End Footer -->

</div>
<!-- End Containing Block -->

</body>
</html>
