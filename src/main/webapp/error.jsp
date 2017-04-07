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
    <title><fmt:message key="error.page.not.found"/></title>
    <%--<meta name="decorator" content="no_leftnav"/>--%>

    <!--[if lt IE 7.]>
             <style>
                 .widget-ie6png,.ie6png { behavior: url(/iepngfix.htc); }
             </style>
         <![endif]-->
    <link rel="stylesheet" href="<spring:theme code='styleSheet' arguments='${version}'/>" type="text/css"/>
    <script src="<spring:theme code='javascript' arguments='${version}'/>" type="text/javascript"></script>

</head>
<body>
<%!
boolean printExceptionMessage(Throwable t, javax.servlet.jsp.JspWriter out) throws java.io.IOException {

    if(t != null) {
        out.println("<tr><td class=\"genericerror\">");
        out.println("Exception message: " + org.apache.commons.lang.StringEscapeUtils.escapeHtml(t.getMessage()) + "\n<br/>&nbsp;");
        out.println("</td></tr>");
        if(t instanceof javax.servlet.ServletException) {

            Throwable rootCause = ((javax.servlet.ServletException)t).getRootCause();

            if(rootCause != null) {

                out.println("<tr><td class=\"genericerror\">");
                out.println("<b>Root cause:</b>");
                out.println("</td></tr>");

                printExceptionMessage(rootCause, out);
            }

        } else if(t instanceof javax.servlet.jsp.JspException) {

            Throwable rootCause = ((javax.servlet.jsp.JspException)t).getRootCause();

            if(rootCause != null) {

                out.println("<tr><td class=\"genericerror\">");
                out.println("<b>Root cause:</b>");
                out.println("</td></tr>");

                printExceptionMessage(rootCause, out);
            }

        } else if(t instanceof Exception) {

            Throwable rootCause = t.getCause();

            if(rootCause != null) {

                out.println("<tr><td class=\"genericerror\">");
                out.println("<b>Root cause:</b>");
                out.println("</td></tr>");

                printExceptionMessage(rootCause, out);
            }

        }/* else if(t instanceof NumberFormatException) {

            Throwable rootCause = ((NumberFormatException)t).getCause();

            if(rootCause != null) {

                out.println("<tr><td class=\"genericerror\">");
                out.println("<b>Root cause:</b>");
                out.println("</td></tr>");

                printExceptionMessage(rootCause, out);
            }

        }
*/
    } else {

        return false;

    }

    return true;

}

boolean printException(Throwable t, javax.servlet.jsp.JspWriter out) throws java.io.IOException {

    if(t != null) {
        java.io.StringWriter stringWriter = new java.io.StringWriter();

        t.printStackTrace(new java.io.PrintWriter(stringWriter));

        out.print(org.apache.commons.lang.StringEscapeUtils.escapeHtml(stringWriter.getBuffer().toString()));

        if(t instanceof javax.servlet.ServletException) {

            Throwable rootCause = ((javax.servlet.ServletException)t).getRootCause();

            if(rootCause != null) {

                out.println("\nRoot cause: \n");

                printException(rootCause, out);


            }

        } else if(t instanceof javax.servlet.jsp.JspException) {

            Throwable rootCause = ((javax.servlet.jsp.JspException)t).getRootCause();

            if(rootCause != null) {

                out.println("\nRoot cause: \n");

                printException(rootCause, out);

            }

        }

    } else {

        return false;

    }

    return true;

}


%>
<div class="common-template-standard">
<div class="header">
    <!-- Load the default catalog for the site -->
    <div class="common-header-wrapper">
        <div class="common-header">

            <!-- Navigational Header Begins -->
            <div class="logo-container">
                <spring:eval expression="siteParam.get('LOGO_IMAGE')" var="logo"/>
                <a href="/"><img title="Cung Shopping Logo" src="${logo}" border="0"
                                         alt="Cung Shopping store"/></a>
                <br><span style="padding-left:2px"><%--TODO: Slogan--%></span>

            </div>
            <div class="links-search-container">
                <div class="signin-container">
                    <a class="sign-in-link" href="">Sign In</a></div>
                <div class="signin-container">
                    <a class="sign-in-link" href="/checkout/basket.html">Cart</a></div>
                <div class="clr"></div>

                <div class="cart-search-keyword-container">
                    <c:url value="/search.html" var="searchUrl" />
                    <form action="${searchUrl}" method="get" name="searchForm">
                        <input class="keyword-text" id="keyword-id" name="keyword" type="text" value="" size="12" autocomplete="off"/>

                        <div class="search-button button">
                            <button type="submit" name="btnG" id="search-button"><fmt:message key="search.button"/></button>
                        </div>
                        <div class="clr"></div>
                    </form>
                </div>
            </div>
            <div class="clr"></div>
        </div>
    </div>
</div>

<div class='page-body body-with-border page-body-float-left'>

    <div id="content" class="error-page">
	<h2 class="error-h2">Trang web bạn tìm không tồn tại</h2>
	<div>
		<p style="padding-bottom: 8px;"><strong>Xin lỗi, chúng tôi không thể tìm thấy trang mà bạn yêu cầu trên hệ thống</strong>.</p>
		<p style="padding-left: 20px;">
            <ul class="error_message">
               <li>Bạn có thể gõ sai địa chỉ mình cần đến. Vui lòng kiểm tra lại địa chỉ bạn muốn truy cập.</li>
               <li>Nếu bạn theo một liên kết từ một trang web khác, vui lòng thông báo lại quản trị viên để cập nhật lại địa chỉ.</li>
            </ul>
		<p>&nbsp;</p>
		<p>Vui lòng nhấn <a href="javascript:history.back();">vào đây</a> để quay về trang trước</a></p>
	</div>
    </div>

    <div style="display:none">
        <%out.println("<!--");%>
    <%=request.getAttribute("javax.servlet.error.request_uri") + (org.apache.commons.lang.StringUtils.isEmpty(request.getQueryString())?"":"?"+request.getQueryString())%><br><br>
    <%=request.getAttribute("javax.servlet.error.message")%><br><br>
    <%=request.getAttribute("javax.servlet.error.exception_type")%><br><br>

    <%

        if(!printExceptionMessage(exception, out)) {
            if(!printExceptionMessage((Exception)request.getAttribute("exception"), out)) {
                out.println("<tr><td class=\"genericerror\">Exception details were not provided<br/><br/></td></tr>");
            }
        }

    %>
<%out.println("-->");%>        
    </div>    
</div>

<div class="clr"><!-- --></div>

<div class="footer"><!-- Footer Begins -->

    <div class="common-footer">
        <div class="clr"></div>
    </div>

    <!-- footer_nav Ends -->
</div>


<%--<h:cmscontent name="footer_pannel"/>--%>
</div>

</body>

</html>

