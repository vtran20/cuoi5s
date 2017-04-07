<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>

<spring:eval expression="serviceLocator.getMenuDao().getRootMenus(site)" var="menus"/>
<%--&lt;%&ndash;Using this menu list if it is admin mode&ndash;%&gt;--%>
<%--<sec:authorize url="/admin/page/index.html">--%>
    <%--<spring:eval expression="serviceLocator.getMenuDao().getRootMenus(site, 'N')"--%>
                 <%--var="menus"/>--%>
<%--</sec:authorize>--%>

<div class="nav-collapse">
    <ul class="nav pull-right">
        <c:forEach var="menu" items="${menus}">
            <c:set var="siteUrl" value="/${menu.uri}"/>
            <%--Convert url by using correct language. Ex: contact-us.html -> lien-he.html --%>
            <c:url value="${siteUrl}" var="siteUrl"/>
            <c:if test="${menu.homePage == 'Y'}">
                <c:set var="siteUrl" value="/"/>
            </c:if>
            <c:url value="${siteUrl}" var="idForMenu"/>
            <c:set value="${fn:replace(idForMenu,'/','')}" var="idForMenu"/>
            <c:set value="${fn:replace(idForMenu,'.','-')}" var="idForMenu"/>

            <spring:eval expression="serviceLocator.getMenuDao().getSubMenus(site, menu, 'Y')"
                         var="subMenus"/>
            <%--<sec:authorize url="/admin/page/index.html">--%>
                <%--<spring:eval expression="serviceLocator.getMenuDao().getSubMenus(site, menu, 'N')"--%>
                             <%--var="subMenus"/>--%>
            <%--</sec:authorize>--%>
            <c:choose>
                <c:when test="${! empty subMenus}">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">${menu.name} <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li class="" id="${idForMenu}-submenu"><a href="${siteUrl}"><c:out value="${menu.name}"/></a></li>
                            <c:forEach var="submenu" items="${subMenus}" varStatus="status">
                                <c:set var="subURL" value="/${submenu.uri}"/>
                                <%--<li class="divider"></li>--%>
                                <c:url value="${subURL}" var="subURL"/>
                                <c:set value="${subURL}" var="idForSubMenu"/>
                                <c:set value="${fn:replace(idForSubMenu,'/','')}" var="idForSubMenu"/>
                                <c:set value="${fn:replace(idForSubMenu,'.','-')}" var="idForSubMenu"/>
                                <li id="${idForSubMenu}-submenu"><a href="${subURL}">${submenu.name}</a></li>
                            </c:forEach>
                        </ul>
                    </li>
                </c:when>

                <c:otherwise>
                    <li class="" id="${idForMenu}-menu"><a href="${siteUrl}"><c:out value="${menu.name}"/></a></li>
                </c:otherwise>
            </c:choose>

            <c:if test="${! empty subMenus}">

            </c:if>

        </c:forEach>

    </ul>
    <%--Active menu that is selected--%>
    <c:url var="uri" value="${uri}"/>
    <c:set value="${fn:replace(uri,'/','')}" var="uri"/>
    <c:set value="${fn:replace(uri,'.','-')}" var="uri"/>
    <script type="text/javascript">
        var currentMenu = "${uri}-menu";
        var currentSubMenu = "${uri}-submenu";
        $(document).ready(function () {
            $("#" + currentMenu).addClass("active");
            $("#" + currentSubMenu).closest("li.dropdown").addClass("active");

        });

    </script>
</div>
