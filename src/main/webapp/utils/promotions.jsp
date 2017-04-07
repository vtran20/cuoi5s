<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<html>
<head>
<title>Create data for SITE and SITE_PARAM tables</title>
</head>

<body>
<spring:eval expression="serviceLocator.getPromotionDao().getPromotions(true)" var="promotions" />
<c:forEach items="${promotions}" var="promotion">
<%--<c:if test="${}"--%>
<spring:eval expression="serviceLocator.getPromotionConditionDao().findBy('promotion.id',promotion.id)" var="promotionConditions" />
    <spring:eval expression="new org.springframework.expression.spel.standard.SpelExpressionParser()" var="parser"/>
    <spring:eval expression="parser.parseExpression('1==2 and 1==1').getValue()" var="vari"/>
    ${vari}
(2==2) && (1==1) && (6<=1) && ('MODEL' == 'HOSTING')
<c:forEach items="${promotionConditions}" var="promotionCondition">
    <h1><c:out value="${promotionCondition.expression}" escapeXml="true" /></h1>

    <%--<spring:eval expression="parser.parseExpression(3000 > 5000).getValue()" var="vari"/>--%>
    <%--${vari}--%>
    <%----%>
    <%--<br>--%>
    <%--<c:out value="${site.domain}" escapeXml="true" />--%>
    <%--&lt;%&ndash;<br>&ndash;%&gt;--%>
    <%--&lt;%&ndash;<c:out value="${site.isDefault}" escapeXml="true" />&ndash;%&gt;--%>
    <%--<br>--%>
    <%--<c:out value="${site.siteCode}" escapeXml="true" />--%>
    <p>
</c:forEach>
</c:forEach>    
</body>
</html>
