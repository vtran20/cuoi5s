<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="name" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="stringParam" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="defaultValue" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="includeTitle" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="styleClass" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="tabindex" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="isMultiSelect" required="false" rtexprvalue="true" type="java.lang.String"%>
<spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValues(stringParam, serviceLocator.locale)" var="stringParamValues"/>
<spring:eval expression="serviceLocator.getStringParamDao().findBy('key', stringParam)" var="sParam"/>
<c:if test="${!empty tabindex}">
    <c:set var="tabInd" value="tabindex='${tabindex}'"/>
</c:if>
<c:if test="${!empty isMultiSelect && isMultiSelect == 'Y'}">
    <c:set var="multiSelect" value="multiple"/>
</c:if>
<select name="${name}" class="${styleClass}" ${tabInd} ${multiSelect}>
    <c:if test="${! empty includeTitle}">
        <option value="">${includeTitle}</option>
    </c:if>
    <c:forEach items="${stringParamValues}" var="stringParam">
        <c:choose>
            <c:when test="${!empty defaultValue && fn:contains(defaultValue, stringParam.key)}">
                <option value="${stringParam.key}" selected>${stringParam.value}</option>
            </c:when>
            <c:otherwise>
                <option value="${stringParam.key}">${stringParam.value}</option>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</select>

