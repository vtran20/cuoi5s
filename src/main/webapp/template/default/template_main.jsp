<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><decorator:title/></title>
    <decorator:head/>
    <link rel="stylesheet" href="<spring:theme code='styleSheet' arguments='${version}'/>" type="text/css"/>
    <script src="<spring:theme code='javascript' arguments='${version}'/>" type="text/javascript"></script>

    <h:head/>
    
</head>
<body>
<%--TODO:http://viralpatel.net/blogs/2010/10/spring-3-mvc-themes-tutorial-example.html--%>
<div class="common-template-standard">
    <div class="header">
        <!-- Load the default catalog for the site -->
        <div class="common-header-wrapper">
            <div class="common-header">

                <!-- Navigational Header Begins -->
                <div class="logo-container">
                    <spring:eval expression="site.siteParamsMap.get('LOGO_IMAGE')" var="logo"/>
                    <a href="/"><img title="Cung Shopping Logo" src="${logo}" border="0"
                                     alt="Cung Shopping store"/></a>
                    <br><span style="padding-left:2px"><%--TODO: Slogan--%></span>
                </div>
                <div class="links-search-container">
                    <div class="signin-container">
                        <c:if test="${!empty sessionObject.USER_NAME}">
                            <a class="sign-in-link" href="/user/logout.html"><fmt:message key="template.sign.out"/></a>
                        </c:if>
                        <c:if test="${empty sessionObject.USER_NAME}">
                            <a class="sign-in-link" href="/user/login.html"><fmt:message key="template.sign.in"/></a>
                        </c:if>
                    </div>
                    <div class="signin-container">
                        <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getItemsInCart(sessionObject)" var="totalItemsInCart"/>
                        <a class="sign-in-link" href="/checkout/basket.html"><img src="/res/assets/images/cart.png" border="0" title='<fmt:message key="template.cart"/>'></a> (${totalItemsInCart})
                    </div>
                    <div class="clr"></div>

                    <div class="cart-search-keyword-container">
                        <c:url value="/search.html" var="searchUrl"/>
                        <form action="${searchUrl}" method="get" name="searchForm">
                            <label>
                                <input class="keyword-text" id="keyword-id" name="keyword" type="text" value=""
                                       size="12"
                                       autocomplete="off"/>
                            </label>

                            <div class="search-button button">
                                <%--<img id="search_button" class="show-on" src="<spring:theme code='global.search.button'/>" value="Search"/>--%>
                                <button type="submit" name="btnG" id="search-button"><fmt:message key="search.button"/></button>
                            </div>
                            <div class="clr"></div>
                        </form>
                    </div>

                </div>


                <div class="clr"></div>
            </div>
            <%--<app:cache key="${site.domain}">--%>
            <h:headermenu/>
            <%--</app:cache>--%>
        </div>
    </div>

    <div class="leftnav">
        <spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
        <spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)"
                     var="params"/>
        <spring:eval expression="params.toString()" var="paramsStr"/>
        <app:cache key="rewritten_uri:${uri}|page:template_main.jsp|section:leftnav|params:${paramsStr}">
            <h:leftnav params="${params}"/>
        </app:cache>
    </div>

    <div class='page-body body-with-border page-body-float-left'>
        <decorator:body/>
    </div>

    <div class="clr"><!-- --></div>

    <h:floatingbar/>

    <div class="footer">
        <!-- Footer Begins -->

            <h:cmscontent name="footer"/>

        <!-- Footer Ends  -->
    </div>

    <%--<h:cmscontent name="footer_pannel"/>--%>
</div>

</body>

</html>