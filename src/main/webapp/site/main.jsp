<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.create.new.website"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<h:authenticate request="${pageContext.request}" response="${pageContext.response}"/>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.create.new.website"/></h1>
    </div>
</div>
<spring:eval expression="serviceLocator.templateDao.findActiveByOrder('site.id', site.id, 'sequence')" var="templates"/>
<div class="container content">
<div class="row">
<!-- Begin Sidebar Menu -->
    <jsp:include page="leftnav.jsp"/>
<!-- End Sidebar Menu -->

<!-- Begin Content -->
<div class="col-md-9">
    <!-- Thumbnails v1 -->
    <c:choose>
        <c:when test="${fn:length(templates) == 1}">
            <c:set value="${templates[0]}" var="template"/>
            <c:redirect url="/site/select-template.html?templateId=${template.id}"/>
        </c:when>
        <c:otherwise>
            <h:sitetemplates column="3"/>
        </c:otherwise>
    </c:choose>
    <!-- End Thumbnails v1 -->

</div>
<!-- End Content -->
</div>
</div>

<script type="text/javascript">
    //Sidebar Navigation Toggle
    jQuery('.list-toggle').on('click', function() {
        jQuery(this).toggleClass('active');
    });
</script>
</body>
</html>
