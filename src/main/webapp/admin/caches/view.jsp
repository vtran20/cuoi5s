<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<html>
<head>
<title>${cache.name}</title>
<meta name="decorator" content="admin" />
</head>

<body>
<h1 class="page_title">${cache.name}</h1>
<p>You can manage cache using following functions.</p>
<c:url value="/admin/caches/clean.html" var="cleanCacheUrl">
  <c:param name="name" value="${cache.name}"/>
</c:url>

<p>Cache Size: ${cache.size}; Memory Size: <spring:eval expression="cache.calculateInMemorySize()"/>; Disk Size: <spring:eval expression="cache.getDiskStoreSize()"/>;</p>

<form:form action="${cleanCacheUrl}" method="post"><input type="submit" value="Clean" /></form:form>


<table>

<tr style="width:100%">
<th>Key</th>
<th>Value</th>
</tr>

<c:forEach items="${cache.keys}" var="key">
  <tr>
    <td><spring:eval expression="key.toString()" var="s"/><c:out escapeXml="true" value="${s}"/></td>
    <td><spring:eval expression="cache.get(key)" var="value"/><c:if test="${not empty value}"><spring:eval expression="value.toString()" var="s"/><c:out escapeXml="true" value="${s}"/></c:if></td>
  </tr>
</c:forEach>

</table>

</body>
</html>
