<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="totalItems" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="params" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.util.QueryString" %>
<%@ attribute name="numPerPage" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="column" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<c:set value="${param.page}" var="currPage"/>
<c:if test="${empty currPage}">
    <c:set value="1" var="currPage"/>
</c:if>

<c:if test="${empty column}">
    <c:set value="4" var="column"/>
</c:if>
<%--Default using itemsPerPage from parameter: priority order will be parameter -> setting from taglib -> default value = 20--%>
<c:set value="${param.ipp}" var="itemsPerPage"/>
<c:if test="${empty itemsPerPage}">
    <c:if test="${empty numPerPage}">
        <c:set value="20" var="itemsPerPage"/>
    </c:if>
    <c:if test="${! empty numPerPage}">
        <c:set value="${numPerPage}" var="itemsPerPage"/>
    </c:if>
</c:if>
<c:set value="${totalItems/itemsPerPage}" var="totalPage"/>
<spring:eval expression="T(java.lang.Math).ceil(totalPage)" var="totalPage"/>
<spring:eval expression="T(java.lang.Math).round(totalPage)" var="totalPage"/>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>

<%--Show if totalItems > itemsPerPage--%>
<c:if test="${totalItems > itemsPerPage}">
    <div class="text-center">
        <ul class="pagination pagination-v2">
                <%--Previous--%>
            <spring:eval expression="params.add('page',currPage - 1).toString()" var="query"/>
            <c:if test="${currPage == 1}">
                <%--<li class="disabled"><a href="#"><fmt:message key="pagination.previous"/></a></li>--%>
            </c:if>
            <c:if test="${currPage > 1}">
                <li class=""><a href="${browseruri}?${query}"><i class="fa fa-angle-left"></i></a></li>
            </c:if>

                <%--1 2 3 4 5 6 7--%>
            <c:if test="${currPage > 4}">
                <c:set value="${currPage - 4}" var="begin"/>
            </c:if>
            <c:if test="${!(currPage > 4)}">
                <c:set value="1" var="begin"/>
            </c:if>

            <c:if test="${totalPage > currPage + 4}">
                <c:set value="${currPage + 4}" var="end"/>
            </c:if>
            <c:if test="${!(totalPage > currPage + 4)}">
                <c:set value="${totalPage}" var="end"/>
            </c:if>

            <c:forEach begin="${begin}" end="${end}" varStatus="page">
                <c:if test="${currPage == page.index}">
                    <li class="active"><a href="#">${page.index}</a></li>
                </c:if>
                <c:if test="${!(currPage == page.index)}">
                    <spring:eval expression="params.add('page',page.index).toString()" var="query"/>
                    <li class=""><a href="${browseruri}?${query}">${page.index}</a></li>
                </c:if>
            </c:forEach>

                <%--Next--%>
            <c:if test="${currPage * itemsPerPage < totalItems}">
                <c:set value="${currPage + 1}" var="nextPage"/>
                <spring:eval expression="params.add('page', nextPage).toString()" var="query"/>
                <li class=""><a href="${browseruri}?${query}"><i class="fa fa-angle-right"></i></a></li>
            </c:if>
            <c:if test="${currPage * itemsPerPage >= totalItems}">
                <%--<li class="disabled"><a href="#"><fmt:message key="pagination.next"/></a></li>--%>
            </c:if>
        </ul>
    </div>
</c:if>