<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<c:set value="${template.cmsFooter}" var="cmsFooter"/>
<sec:authorize url="/admin/page/index.html">
<c:set value="${template.preCmsFooter}" var="cmsFooter"/>
<c:set value="true" var="loggedin"/>
</sec:authorize>

<footer class="footer" id="cms-footer">
${cmsFooter}
</footer>

<c:if test="${'Y' != site.defaultSite}">
<div class="container">
    <div class="row">
        <div class="span12">
            <c:choose>
                <c:when test="${site.parentSite.siteType == 3}">
                    <c:set var="ownerDomain" value="${site.parentSite.domain}"/>
                    <c:set var="ownerName" value="${site.parentSite.domain}"/>
                </c:when>
                <c:when test="${site.siteType == 3}">
                    <c:set var="ownerDomain" value="${site.domain}"/>
                    <c:set var="ownerName" value="${site.domain}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="ownerDomain" value="${site.parentSite.domain}"/>
                    <c:set var="ownerName" value="WebPhatTai."/>
                </c:otherwise>
            </c:choose>
        <%--Designed by <a href="http://${ownerDomain}">${ownerName}</a> | <a href="/admin/login.html"><fmt:message key='site.login.admin'/></a>--%>
            <span class="pull-right">Designed by <a href="http://${ownerDomain}">${ownerName}</a></span>
            <c:if test="${loggedin != 'true'}">
            <span class="pull-right"><a href="/admin/login.html">Đăng Nhập Admin</a>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
            </c:if>
        </div>
    </div>
</div>
</c:if>
<%--<section>--%>
    <%--<div class="container" style="background: gray;">--%>
        <%--<div class="row">--%>
            <%--<div class="span12">--%>
                <%--<span class="pull-right">Design by <a href="http://webphattai.com">webphattai&copy;</a></span>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</section>--%>
