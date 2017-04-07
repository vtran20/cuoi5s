<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="app" uri="/WEB-INF/tlds/app.tld" %>
<%@ taglib prefix="log" uri="http://www.slf4j.org/taglib/tld" %>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager" %>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>
<spring:eval expression="T(com.easysoft.ecommerce.service.ServiceLocatorHolder).serviceLocator" var="serviceLocator" />
<spring:eval expression="T(org.springframework.util.StringUtils).toLanguageTag(locale)" var="language" />
<spring:eval expression="serviceLocator.getSiteDao().getSiteByServerName(pageContext.request.serverName)" var="site" />
<spring:eval expression="site.getSiteParamsMap()" var="siteParam" />

<html>
<head>
    <title>System Error</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <%--<meta name="decorator" content="error"/>--%>
</head>

<body>
Please click here to clear your cookie <a href="/">Home Page</a>
<script type="text/javascript">
    function clearCookie () {
//        alert (getCookie("USER_SESSION_ID"));
        window.location.href = "/site/clear-session.html?usid="+getCookie("USER_SESSION_ID");
    }

    function getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1);
            if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
        }
        return "";
    }

</script>
<%--Hello--%>
<%--<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>--%>
<%--${site.domain}--%>
<%--${exception}--%>
</body>
</html>