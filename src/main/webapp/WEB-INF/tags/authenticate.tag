<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>
<%@ attribute name="request" required="true" rtexprvalue="true" type="javax.servlet.http.HttpServletRequest"%>
<%@ attribute name="response" required="true" rtexprvalue="true" type="javax.servlet.http.HttpServletResponse"%>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<c:url var="redirect" value="https://${domain}/site/login.html?source=${uri}"/>
<spring:eval expression="T(com.easysoft.ecommerce.controller.SessionUtil).isLoggedIn(request, response)" var="isLoggedIn"/>
<c:if test="${isLoggedIn != 'true'}">
    <c:redirect url="${redirect}"/>
</c:if>




