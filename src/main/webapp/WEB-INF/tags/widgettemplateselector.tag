<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="name" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="defaultValue" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="widgetType" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="includeTitle" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="styleClass" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="tabindex" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="isMultiSelect" required="false" rtexprvalue="true" type="java.lang.String"%>
<spring:eval expression="serviceLocator.widgetTemplateDao.getWidgetTemplateByType(widgetType, 'Y')" var="widgetTemplates"/>
<c:if test="${!empty tabindex}">
    <c:set var="tabInd" value="tabindex='${tabindex}'"/>
</c:if>

<c:if test="${!empty isMultiSelect && isMultiSelect == 'Y'}">
    <c:set var="multiSelect" value="multiple"/>
</c:if>
<select name="${name}" class="${styleClass}" ${tabInd} ${multiSelect} style="width: 100%;max-width: 300px;min-width: 300px;">
    <c:if test="${! empty includeTitle}">
        <option value="">${includeTitle}</option>
    </c:if>
    <c:forEach items="${widgetTemplates}" var="template">
        <c:choose>
            <c:when test="${!empty defaultValue && fn:contains(defaultValue, template.id)}">
                <option value="${template.id}" selected lang="${template.imageUrl}">${template.name}</option>
            </c:when>
            <c:otherwise>
                <option value="${template.id}" lang="${template.imageUrl}">${template.name}</option>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</select>

