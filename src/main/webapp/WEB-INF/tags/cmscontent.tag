<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="name" required="true" rtexprvalue="true" type="java.lang.String" %>
<app:cache key="${name}">
<spring:eval expression="serviceLocator.cmsAreaContentDao.getCmsAreaContent(systemContext.site, name)"
             var="cmsContent"/>
    <c:if test="${!empty cmsContent}">
        ${cmsContent.content}
    </c:if>
</app:cache>

