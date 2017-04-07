<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="messages" required="true" rtexprvalue="true" type="java.util.List" %>

<c:if test="${!empty messages}">
    <div class="alert alert-success">
        <c:forEach items="${messages}" var="message">
            ${message}<br>
        </c:forEach>
    </div>
</c:if>