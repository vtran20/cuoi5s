<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List"%>

<c:forEach items="${items}" var="item">
 <p>
     <a href="TODO">${item.name} - ${item.id} - ${item.metaKeyword} - ${item.displayPricePromo}</a>
 </p>
</c:forEach>