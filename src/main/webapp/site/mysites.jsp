<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.site.list"/></title>
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

<div class="container content">
<div class="row">
<!-- Begin Sidebar Menu -->
    <jsp:include page="leftnav.jsp"/>
<!-- End Sidebar Menu -->

<!-- Begin Content -->
<div class="col-md-9">
    <div class="panel panel-red margin-bottom-40">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-globe"></i> <fmt:message key="site.website.list"/></h3>
        </div>
        <c:if test="${!empty sessionObject.USER_ID}">
            <spring:eval expression="serviceLocator.siteDao.getSitesByUser(T(java.lang.Long).valueOf(sessionObject.USER_ID))" var="sites" />
            <spring:eval expression="serviceLocator.userDao.findById(T(java.lang.Long).valueOf(sessionObject.USER_ID))" var="currentUser" />
            <c:if test="${!empty sites}">
                <div class="panel-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th><fmt:message key="site.primary.url"/></th>
                            <%--<th class="hidden-xs"><fmt:message key="site.website.info"/></th>--%>
                            <th><fmt:message key="site.website.expired"/></th>
                            <th class="hidden-xs"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach varStatus="thisSite" items="${sites}">
                            <tr>
                                <td>${thisSite.index+1}</td>
                                <c:choose>
                                    <c:when test="${!empty thisSite.current.domain}">
                                        <td>
                                        <a target="_blank" href="http://${thisSite.current.domain}">${thisSite.current.domain}</a><br>
                                            <a target="_blank" href="http://${thisSite.current.subDomain}">${thisSite.current.subDomain}</a>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>
                                            <a target="_blank" href="http://${thisSite.current.subDomain}">${thisSite.current.subDomain}</a>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                                <%--<td class="hidden-xs">${thisSite.current.name}</td>--%>
                                <%--<spring:eval expression="serviceLocator.getStrongEncryptor().decrypt(currentUser.password)" var="password" />--%>
                                <%--<c:set var="code" value="domain=${thisSite.current.subDomain}&j_username=${currentUser.username}&j_password=${password}"/>--%>
                                <%--<spring:eval expression="T(com.easysoft.ecommerce.service.impl.URLUTF8Encoder).encode(serviceLocator.getStrongEncryptor().encrypt(code))" var="encryptText" />--%>
                                <spring:eval expression="serviceLocator.getProductDao().findUniqueBy('model','HOSTING', site.id)" var="product"/>
                                <td>
                                    <spring:eval expression="site.siteParamsMap.get('DATE_FORMAT')" var="dateFormat"/>
                                    <fmt:formatDate pattern="${dateFormat}" value="${thisSite.current.endDate}"/>
                                    <div class="btn-group hidden-sm hidden-md hidden-lg">
                                        <a class="btn-u btn-u-small margin-bottom-5" href="/site/data/general-information.html?thisSiteId=${thisSite.current.id}"><i class="fa fa-pencil"></i> Update Website</a>
                                        <a class="btn-u btn-u-small margin-bottom-5" href="/site/checkout/addtocart.html?productId=${product.id}&thisSiteId=${thisSite.current.id}"><i class="fa fa-refresh"></i> <fmt:message key="site.site.renew"/></a>
                                        <%--<button data-toggle="dropdown" class="btn-u dropdown-toggle" type="button" aria-expanded="true">--%>
                                            <%--Action--%>
                                            <%--<i class="fa fa-angle-down"></i>--%>
                                        <%--</button>--%>
                                        <%--<ul role="menu" class="dropdown-menu">--%>
                                            <%--<li><a href="/site/checkout/addtocart.html?productId=${product.id}&thisSiteId=${thisSite.current.id}"><i class="fa fa-refresh"></i> <fmt:message key="site.site.renew"/></a></li>--%>
                                            <%--<li class="divider"></li>--%>
                                            <%--<li><a target="_blank" href="http://${thisSite.current.subDomain}/site/go-to-sitemanager.html?code=${encryptText}"><i class="fa fa-globe"></i> <fmt:message key="site.site.admin"/></a></li>--%>
                                            <%--<li><a href="/site/modules.html?thisSiteId=${thisSite.current.id}"><i class="fa fa-cubes"></i> <fmt:message key="site.module.list"/></a></li>--%>
                                        <%--</ul>--%>
                                    </div>
                                </td>
                                <td class="hidden-xs">
                                    <div class="btn-group">
                                        <a class="btn-u btn-u-small margin-bottom-5" href="/site/data/general-information.html?thisSiteId=${thisSite.current.id}"><i class="fa fa-pencil"></i> Update Website</a>
                                        <a class="btn-u btn-u-small margin-bottom-5" href="/site/checkout/addtocart.html?productId=${product.id}&thisSiteId=${thisSite.current.id}"><i class="fa fa-refresh"></i> <fmt:message key="site.site.renew"/></a>
                                    </div>
                                    <%--<div class="btn-group">--%>
                                        <%--<button data-toggle="dropdown" class="btn-u dropdown-toggle" type="button" aria-expanded="true">--%>
                                            <%--Action--%>
                                            <%--<i class="fa fa-angle-down"></i>--%>
                                        <%--</button>--%>
                                        <%--<ul role="menu" class="dropdown-menu">--%>
                                            <%--<li><a href="/site/checkout/addtocart.html?productId=${product.id}&thisSiteId=${thisSite.current.id}"><i class="fa fa-refresh"></i> <fmt:message key="site.site.renew"/></a></li>--%>
                                            <%--<li class="divider"></li>--%>
                                            <%--<li><a target="_blank" href="http://${thisSite.current.subDomain}/site/go-to-sitemanager.html?code=${encryptText}"><i class="fa fa-globe"></i> <fmt:message key="site.site.admin"/></a></li>--%>
                                            <%--<li><a href="/site/modules.html?thisSiteId=${thisSite.current.id}"><i class="fa fa-cubes"></i> <fmt:message key="site.module.list"/></a></li>--%>
                                        <%--</ul>--%>
                                    <%--</div>--%>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </c:if>
    </div>
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
