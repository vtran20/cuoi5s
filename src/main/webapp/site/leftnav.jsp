<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<c:if test="${uri == '/site/main.html' || uri == '/site/select-template.html'}"><c:set var="main" value="active"/></c:if>
<c:if test="${fn:startsWith(uri, '/site/select-template.html')}"><c:set var="selectTemplate" value="active"/></c:if>
<c:if test="${uri == '/site/mysites.html'}"><c:set var="mysite" value="active"/></c:if>
<c:if test="${fn:contains('/site/myprofile.html,/site/update_account.html', uri)}"><c:set var="myprofile" value="active"/></c:if>
<div class="col-md-3">
    <ul class="list-group sidebar-nav-v1" id="sidebar-nav">
        <li class="list-group-item ${main}"><a href="/site/main.html"><fmt:message key="site.create.new.website"/></a></li>
        <%--<li class="list-group-item ${selectTemplate}"><a href="/site/select-template.html?templateId=64"><fmt:message key="site.create.new.website"/></a></li>--%>
        <li class="list-group-item ${mysite}"><a href="/site/mysites.html"><fmt:message key="site.site.list"/></a></li>
        <li class="list-group-item ${myprofile}"><a href="/site/myprofile.html"><fmt:message key="site.account.information"/></a></li>
    </ul>
</div>
