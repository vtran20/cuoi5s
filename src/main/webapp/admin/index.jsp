<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>Dashboard</title>
    <meta name="decorator" content="admin_wpt"/>
</head>

<body>

<!-- #section:basics/content.breadcrumbs -->
<div class="breadcrumbs" id="breadcrumbs">
    <script type="text/javascript">
        try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
    </script>

    <ul class="breadcrumb">
        <li>
            <i class="ace-icon fa fa-home home-icon"></i>
            <a href="/admin">Home</a>
        </li>
        <li class="active">Dashboard</li>
    </ul><!-- /.breadcrumb -->

    <!-- #section:basics/content.searchbox -->
    <div class="nav-search" id="nav-search">
        <form class="form-search">
								<span class="input-icon">
									<input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off">
									<i class="ace-icon fa fa-search nav-search-icon"></i>
								</span>
        </form>
    </div><!-- /.nav-search -->

    <!-- /section:basics/content.searchbox -->
</div>

<!-- /section:basics/content.breadcrumbs -->
<div class="page-content">
<div class="page-header">
    <h1>
        Dashboard
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            overview &amp; stats
        </small>
    </h1>
</div><!-- /.page-header -->

</div><!-- /.page-content -->


<!-- Bread Crumb Navigation -->
<%--<div class="span10">--%>
    <%--<div>--%>
        <%--<ul class="breadcrumb">--%>
            <%--<li>--%>
                <%--<a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span>--%>
            <%--</li>--%>
            <%--<li class="active">Website</li>--%>
        <%--</ul>--%>
    <%--</div>--%>
    <%--<spring:eval expression="serviceLocator.siteTemplateDao.findUniqueBy('site.id',site.id)" var="website"/>--%>
    <%--<c:if test="${empty website}">--%>
        <%--<div class="row-fluid">--%>
            <%--<div class="span4"></div>--%>
            <%--<div class="span4 action-btn round-all">--%>
                <%--<a class="create-site" hreflang="/admin/sites/site.html" href="/admin/sites/create.html">--%>
                    <%--<div><i class="icon-file"></i></div>--%>
                    <%--<div><strong>Tạo website</strong></div>--%>
                <%--</a>--%>
            <%--</div>--%>
            <%--<div class="span4"></div>--%>
        <%--</div>--%>
        <%--<div class="alert alert-success">--%>
            <%--<a class="close" data-dismiss="alert" href="#">×</a>--%>
            <%--<strong>Xin chào!</strong> Hãy bắt đầu tạo website cho bạn.--%>
        <%--</div>--%>
    <%--</c:if>--%>

    <%--<!--/span-->--%>
<%--</div>--%>


</body>
</html>