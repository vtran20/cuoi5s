<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="_messages" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.util.Messages" %>
<c:if test="${!empty _messages}">
    <spring:eval expression="_messages.getInfos()" var="infos"/>
    <spring:eval expression="_messages.getWarnings()" var="warnings"/>
    <spring:eval expression="_messages.getErrors()" var="errors"/>

    <c:if test="${!empty infos}">
        <div class="alert alert-success">
            <c:forEach items="${infos}" var="value">
                ${value} <br>
            </c:forEach>
        </div>
    </c:if>
    <c:if test="${!empty warnings}">
        <div class="alert alert-danger">
        <c:forEach items="${warnings}" var="value">
            ${value}<br>
        </c:forEach>
        </div">
    </c:if>
    <c:if test="${!empty errors}">
        <div class="alert alert-danger">
            <c:forEach items="${errors}" var="value">
                ${value}<br>
            </c:forEach>
        </div>
    </c:if>

</c:if>