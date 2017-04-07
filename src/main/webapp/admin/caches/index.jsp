<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<html>
<head>
<title>Caches</title>
<meta name="decorator" content="admin" />
</head>

<body>
<h1 class="page_title">Caches</h1>
<p>You can manage caches using following functions.</p>

<h2>Caches</h2>

<table>

<tr style="width:100%">
<th>Name</th>
<th>Items</th>
<th>Memory Size</th>
<th>Disk Size</th>
<th style="width:10%">Clear Cache</th>
<th style="width:10%">Statistics</th>
<th>Hits/Misses</th>
<th style="width:10%">Clear Statistics</th>
</tr>

<spring:eval expression="serviceLocator.cacheManager.cacheNames" var="cacheNames" />
<c:forEach items="${cacheNames}" var="cacheName">
  <spring:eval expression="serviceLocator.cacheManager.getEhcache(cacheName)" var="cache" />
  <c:url value="/admin/caches/view.html" var="viewCacheUrl">
    <c:param name="name" value="${cache.name}"/>
  </c:url>
  <c:url value="/admin/caches/clean.html" var="cleanCacheUrl">
    <c:param name="name" value="${cache.name}"/>
  </c:url>
  <c:url value="/admin/caches/statistics.html" var="statisticsUrl">
    <c:param name="name" value="${cache.name}"/>
  </c:url>
  <c:url value="/admin/caches/action.html" var="actionUrl"/>

  <tr>
    <td><a href="${viewCacheUrl}">${cache.name}</a></td>
    <td>${cache.size}</td>
    <td><spring:eval expression="cache.calculateInMemorySize()/1024"/>KB</td>
    <td><spring:eval expression="cache.getDiskStoreSize()/1024"/>KB</td>
    <td><form:form action="${cleanCacheUrl}" method="post"><input type="submit" value="Clear" /></form:form></td>
    <td><form:form action="${statisticsUrl}" method="post">
    <c:if test="${cache.statisticsEnabled}"><input type="submit" value="Disable" /></c:if>
    <c:if test="${not cache.statisticsEnabled}"><input type="submit" value="Enable" /></c:if>
    </form:form></td>
    <td><spring:eval expression="cache.statistics.cacheHits"/>/<spring:eval expression="cache.statistics.cacheMisses"/></td>
    <td><form:form action="${statisticsUrl}" method="delete"><input type="submit" value="Clear" /></form:form></td>
  </tr>
</c:forEach>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><form:form action="${actionUrl}" method="post"><input type="hidden" name="action" value="cleanCache" /><input type="submit" value="Clear All" /></form:form></td>
    <td>
    <form:form action="${actionUrl}" method="post"><input type="hidden" name="action" value="enableStatistics" /><input type="submit" value="Enable All" /></form:form>
    <form:form action="${actionUrl}" method="post"><input type="hidden" name="action" value="disableStatistics" /><input type="submit" value="Disable All" /></form:form>
    </td>
    <td>&nbsp;</td>
    <td><form:form action="${actionUrl}" method="post"><input type="hidden" name="action" value="cleanStatistics" /><input type="submit" value="Clear All" /></form:form></td>
  </tr>

</table>

</body>
</html>
