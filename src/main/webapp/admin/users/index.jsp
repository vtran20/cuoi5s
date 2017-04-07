<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<html>
<head>
<title>Users</title>
<meta name="decorator" content="admin" />
</head>

<body>
<h1 class="page_title">Users</h1>
<p>You can manage existing users or create new users using following functions.</p>

<h2>Existing Users</h2>

<table>
<tr style="width:100%">
<th>Username</th>
<th>Email</th>
<th>Site</th>
<th style="width:10%">Delete</th></tr>

<sec:authentication property="principal.site" var="site"/>
<c:if test="${empty site}">
<spring:eval expression="serviceLocator.userDao.findAll()" var="items" />
</c:if>
<c:if test="${not empty site}">
<spring:eval expression="serviceLocator.userDao.findBy('site',site)" var="items" />
</c:if>

<c:forEach items="${items}" var="item">
  <tr>
    <td><a href="/admin/users/${item.id}/index.html">${item.username}</a></td>
    <td>${item.email}</td>
    <td>${item.site.name}</td>
    <td><form:form id="delete${item.id}" action="/admin/users/${item.id}/index.html" method="delete"><input type="submit" value="Delete" /></form:form></td>
  </tr>
</c:forEach>

</table>

<h2>New User</h2>
<p><a href="/admin/users/form.html">Create New User</a></p>

</body>
</html>