<!DOCTYPE html>
<%@ page import="com.easysoft.ecommerce.controller.SessionUtil" %>
<%@ page import="com.easysoft.ecommerce.model.session.SessionObject" %>
<%@ page import="com.thoughtworks.xstream.XStream" %>
<%@ page import="com.easysoft.ecommerce.service.ServiceLocatorHolder" %>
<%@ page import="net.sf.ehcache.CacheManager" %>
<%@ page import="net.sf.ehcache.Ehcache" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easysoft.ecommerce.model.*" %>
<%@ page import="org.apache.commons.beanutils.BeanUtils" %>
<%@ page import="java.lang.reflect.InvocationTargetException" %>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<html>
<head>
<title>View Session Object</title>
</head>

<body>
<c:if test="${mode == 'importproduct'}">
    <spring:eval expression="serviceLocator.getJobsService().importProducts()" var="messages" />
    <p>Import products completed. Here is detail information.</p>
    <c:forEach items="${messages}" var="message">
        ${message}<br>
    </c:forEach>
</c:if>
<c:if test="${mode == 'importinventory'}">
    <spring:eval expression="serviceLocator.getJobsService().importInventory()" var="messages" />
    <p>Import inventory completed. Here is detail information.</p>
    <c:forEach items="${messages}" var="message">
        ${message}<br>
    </c:forEach>
</c:if>
<c:if test="${mode == 'updateprice'}">
    <spring:eval expression="serviceLocator.getJobsService().runPromoPriceJob()" var="messages" />
    <p>Update price/promotion completed. Here is detail information.</p>
    <c:forEach items="${messages}" var="message">
        ${message}<br>
    </c:forEach>
</c:if>
<c:if test="${mode == 'relatedproduct'}">
    <spring:eval expression="serviceLocator.getJobsService().importRelatedProduct()" var="messages" />
    <p>Import related product completed. Here is detail information.</p>
    <c:forEach items="${messages}" var="message">
        ${message}<br>
    </c:forEach>
</c:if>
<c:if test="${mode == 'moreimage'}">
    <spring:eval expression="serviceLocator.getJobsService().importMoreImagesProduct()" var="messages" />
    <p>Import more image product completed. Here is detail information.</p>
    <c:forEach items="${messages}" var="message">
        ${message}<br>
    </c:forEach>
</c:if>
<c:if test="${mode == 'sequenceproduct'}">
    <spring:eval expression="serviceLocator.getJobsService().importSequenceProduct()" var="messages" />
    <p>Import sequence product completed. Here is detail information.</p>
    <c:forEach items="${messages}" var="message">
        ${message}<br>
    </c:forEach>
</c:if>

<c:if test="${mode == 'autoemail'}">
    <spring:eval expression="serviceLocator.getJobsService().autoEmailSending()" var="messages" />
    <p>Emails were sent.</p>
</c:if>
<c:if test="${mode == 'marketingemail'}">
    <spring:eval expression="serviceLocator.getJobsService().marketingEmailSending()" var="messages" />
    <p>Emails were sent.</p>
</c:if>
<c:if test="${mode == 'filteremail'}">
    <spring:eval expression="serviceLocator.getJobsService().filterValidEmail()" var="messages" />
    <p>Filter Emails were completed.</p>
</c:if>
<c:if test="${mode == 'copyproducts'}">
    <%
        List<Product> products = ServiceLocatorHolder.getServiceLocator().getProductDao().findAll(2l);
        Site site = ServiceLocatorHolder.getServiceLocator().getSiteDao().findById(2l);
        Site thisSite = ServiceLocatorHolder.getServiceLocator().getSiteDao().findById(47l);
//        ServiceLocatorHolder.getServiceLocator().getSiteService().copyTemplates(products, thisSite);

    %>
</c:if>


<c:if test="${mode == 'buildindex'}">
<%
    ServiceLocatorHolder.getServiceLocator().getProductDao().updateProductPrice();
    ServiceLocatorHolder.getServiceLocator().getJobsService().rebuildIndex(Product.class);
    ServiceLocatorHolder.getServiceLocator().getJobsService().rebuildIndex(ProductVariant.class);
%>
    <p>Build index completed.</p>
</c:if>
<c:if test="${mode == 'buildkeyword'}">
<%
    ServiceLocatorHolder.getServiceLocator().getJobsService().rebuildIndex(Keyword.class);
%>
    <p>Build keyword index completed.</p>
</c:if>
<c:if test="${mode == 'updateexpired'}">
<%
    ServiceLocatorHolder.getServiceLocator().getJobsService().updateExpiredDateForDemoSite();
%>
    <p>Expired Date of demo sites were updated.</p>
</c:if>

<%
    CacheManager cacheManager = ServiceLocatorHolder.getServiceLocator().getCacheManager();
    Ehcache cache = cacheManager.getEhcache("CacheTag");
    Ehcache commonCache = cacheManager.getEhcache("CommonCache");
    if (cache != null) {
        cache.removeAll();
        commonCache.removeAll();
    }

    String dbUrl = System.getenv("DB_URL");
    String dbUsername = System.getenv("DB_USERNAME");
    String dbPassword = System.getenv("DB_PASSWORD");
    System.out.println("DB URL: " + dbUrl);
    System.out.println("DB Username: " + dbUsername);
    System.out.println("DB Password: " + dbPassword);
%>
<p>Clear cache completed.</p>
<fmt:message key="common.saving.error.message"/>
</body>
</html>
