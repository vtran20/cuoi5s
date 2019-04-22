<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.data.about.us"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<h:authenticate request="${pageContext.request}" response="${pageContext.response}"/>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.data.general.info"/></h1>
        <ul class="pull-right breadcrumb">
            <li><a href="/">Home</a></li>
            <li><a href="/site/mysites.html">My Sites</a></li>
            <li class="active"><fmt:message key="site.data.general.info"/></li>
        </ul>
    </div>
</div>
<div class="container content">
<div class="row">
<!-- Begin Sidebar Menu -->
    <jsp:include page="leftnav.jsp"/>
<!-- End Sidebar Menu -->
    <%--String sSite = so.getString("UPDATE_CURRENT_SITE_ID");--%>
    <%--if (StringUtils.isNumeric(sSite)) {--%>
    <%--Long siteId = Long.valueOf(sSite);--%>
    <%--Site thisSite = serviceLocator.getSiteDao().findById(siteId);--%>
    <spring:eval expression="sessionObject.getString('UPDATE_CURRENT_SITE_ID')" var="siteId"/>
    <fmt:parseNumber var = "siteId" type = "number" value = "${siteId}" integerOnly = "true" />
    <spring:eval expression="serviceLocator.getSiteDao().findById(siteId)" var="thisSite"/>
    <spring:eval expression="serviceLocator.menuDao.getMenu(thisSite, '', 'Y')" var="homeMenu"/>
    <spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(homeMenu.id, 'Y')" var="menuRows"/>
    <c:forEach var="row" items="${menuRows}">
        <c:if test="${fn:contains(row.title, 'About Us')}">
            <c:set var="aboutUsRow" value="${row}"/>
            <spring:eval expression="serviceLocator.siteMenuPartContentDao.getContentParts(aboutUsRow.id, 'Y')" var="partContents"/>
            <c:if test="${fn:length(partContents) > 0}">
                <c:set var="aboutUsContent" value="${partContents[0]}"/>
            </c:if>
        </c:if>
    </c:forEach>
<!-- Begin Content -->
<div class="col-md-9 col-xs-12">
    <div class="row">
        <!-- Begin Sidebar Menu -->
        <div class="col-md-6">
            <div class="panel panel-red margin-bottom-40">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.data.followus"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal">
                        <div class="form-group col-lg-12">
                            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'FACEBOOK_FAN_PAGE', thisSite.id)" var="facebook"/>
                            <label for="facebook" class="control-label"><fmt:message key="site.data.followus.facebook"/></label>
                            <input id="facebook" type="text" placeholder="<fmt:message key="site.data.followus.facebook"/>" name="title" class="form-control site_param_field" value="${facebook.value}" data-key="FACEBOOK_FAN_PAGE">
                        </div>
                        <div class="form-group col-lg-12">
                            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'TWITTER_FAN_PAGE', thisSite.id)" var="twitter"/>
                            <label for="twitter" class="control-label"><fmt:message key="site.data.followus.twitter"/></label>
                            <input id="twitter" type="text" placeholder="<fmt:message key="site.data.followus.twitter"/>" name="title" class="form-control site_param_field" value="${twitter.value}" data-key="TWITTER_FAN_PAGE">
                        </div>
                        <div class="form-group col-lg-12">
                            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'PINTEREST_FAN_PAGE', thisSite.id)" var="pinterest"/>
                            <label for="pinterest" class="control-label"><fmt:message key="site.data.followus.pinterest"/></label>
                            <input id="pinterest" type="text" placeholder="<fmt:message key="site.data.followus.pinterest"/>" name="title" class="form-control site_param_field" value="${pinterest.value}" data-key="PINTEREST_FAN_PAGE">
                        </div>
                        <div class="form-group col-lg-12">
                            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'INSTAGRAM_FAN_PAGE', thisSite.id)" var="instagram"/>
                            <label for="instagram" class="control-label"><fmt:message key="site.data.followus.instagram"/></label>
                            <input id="instagram" type="text" placeholder="<fmt:message key="site.data.followus.instagram"/>" name="title" class="form-control site_param_field" value="${instagram.value}" data-key="INSTAGRAM_FAN_PAGE">
                        </div>
                        <div class="form-group col-lg-12">
                            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'YOUTUBE_FAN_PAGE', thisSite.id)" var="youtube"/>
                            <label for="youtube" class="control-label"><fmt:message key="site.data.followus.youtube"/></label>
                            <input id="youtube" type="text" placeholder="<fmt:message key="site.data.followus.youtube"/>" name="title" class="form-control site_param_field" value="${youtube.value}" data-key="YOUTUBE_FAN_PAGE">
                        </div>
                        <div class="form-group col-lg-12">
                            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'YELP_FAN_PAGE', thisSite.id)" var="yelp"/>
                            <label for="yelp" class="control-label"><fmt:message key="site.data.followus.yelp"/></label>
                            <input id="yelp" type="text" placeholder="<fmt:message key="site.data.followus.yelp"/>" name="title" class="form-control site_param_field" value="${yelp.value}" data-key="YELP_FAN_PAGE">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End Content -->
</div>
</div>

<script type="text/javascript">
    $(".site_param_field").focus(function () {
        var field = $(this)
        field.closest('.form-group').find('em').remove();
    })

    $(".site_param_field").change(function () {
        var field = $(this)
        $.ajax({
            type: "GET",
            url: '/site/data/update_site_param.html?siteId=${thisSite.id}&key='+field.data("key")+'&value='+field.val(),
            success: function(data)
            {
                if (data == "ok") {
                    //Display update success message
                    field.after("<em class='save-success'><fmt:message key='site.data.save.successfully'/></em>")
                }
            }
        });
    });
</script>
</body>
</html>
