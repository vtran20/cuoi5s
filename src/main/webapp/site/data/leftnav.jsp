<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<c:if test="${uri == '/site/data/general-information.html' || uri == '/site/data/create-update-store.html'}"><c:set var="store" value="active"/></c:if>
<c:if test="${uri == '/site/data/about-us.html' || uri == '/site/data/update_aboutus.html'}"><c:set var="aboutus" value="active"/></c:if>
<c:if test="${uri == '/site/data/services.html' || uri == '/site/data/update_services.html'}"><c:set var="services" value="active"/></c:if>
<c:if test="${uri == '/site/data/employees.html'}"><c:set var="employees" value="active"/></c:if>
<c:if test="${uri == '/site/data/gallery.html' || uri == '/site/data/update_gallery.html'}"><c:set var="gallery" value="active"/></c:if>
<c:if test="${uri == '/site/data/setting.html'}"><c:set var="setting" value="active"/></c:if>
<%--<c:if test="${fn:contains('/site/myprofile.html,/site/update_account.html', uri)}"><c:set var="myprofile" value="active"/></c:if>--%>
<spring:eval expression="serviceLocator.getNailStoreDao().findAll(sessionObject.UPDATE_CURRENT_SITE_ID)" var="stores"/>
<div class="col-md-3">
    <ul class="list-group sidebar-nav-v1" id="sidebar-nav">
        <li class="list-group-item ${store}"><a href="/site/data/general-information.html"><fmt:message key="site.data.general.information"/></a></li>
        <c:if test="${!empty stores}">
            <li class="list-group-item ${aboutus}"><a href="/site/data/about-us.html"><fmt:message key="site.data.about.us"/></a></li>
            <li class="list-group-item ${services}"><a href="/site/data/services.html"><fmt:message key="site.data.services"/></a></li>
            <li class="list-group-item ${employees}"><a href="/site/data/employees.html"><fmt:message key="site.data.employees"/></a></li>
            <li class="list-group-item ${gallery}"><a href="/site/data/gallery.html"><fmt:message key="site.data.gallery"/></a></li>
            <li class="list-group-item ${setting}"><a href="/site/data/setting.html"><fmt:message key="site.data.setting"/></a></li>
        </c:if>
    </ul>
</div>
