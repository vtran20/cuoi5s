<!DOCTYPE>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<html>
<head>
<title>Sites</title>
<meta name="decorator" content="admin" />
</head>

<body>
<h1 class="page_title">Sites</h1>
<p>You can manage existing sites or create new sites using following functions.</p>

<h2>Existing Sites</h2>

<table>
<tr style="width:100%">
<th>Name</th>
<th>Domain(s)</th>
<th>Default Site</th>
<th style="width:10%"><fmt:message key="common.delete"/></th></tr>
<spring:eval expression="serviceLocator.siteDao.findAll()" var="items" />
<c:forEach items="${items}" var="item">
  <tr>
    <td><a href="/admin/sites/${item.id}/index.html">${item.name}</a></td>
    <td>${item.domain}</td>
    <td>${item.default}</td>
    <td><form:form id="delete${item.id}" action="/admin/sites/${item.id}/index.html" method="delete"><input type="hidden" name="csrf" value="<sec:authentication property='details.csrf'/>"/><input type="submit" value="Delete" /></form:form></td>
  </tr>
</c:forEach>

</table>

<h2>New Site</h2>
<p><a href="/admin/sites/form.html">Create New Site</a></p>

</body>
</html>