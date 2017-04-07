<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="totalItems" required="true" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="params" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.util.QueryString"%>

<%--<c:set value="211" var="totalItems"/>--%>
<c:set value="${param.page}" var="currPage"/>
<c:if test="${empty currPage}">
    <c:set value="1" var="currPage"/>
</c:if>
<c:set value="${param.itemsPerPage}" var="itemsPerPage"/>
<c:if test="${empty itemsPerPage}">
    <c:set value="20" var="itemsPerPage"/>
</c:if>
<c:set value="4" var="range"/>
<c:set value="${totalItems/itemsPerPage}" var="totalPage"/>
<spring:eval expression="T(java.lang.Math).ceil(totalPage)" var="totalPage"/>
<spring:eval expression="T(java.lang.Math).round(totalPage)" var="totalPage"/>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI()" var="browseruri"/>

<c:if test="${totalItems > 20}">
<div class="pagination-container">
<%--Previous--%>
<c:if test="${currPage > 1}">
    <spring:eval expression="params.add('page',currPage - 1).toString()" var="query"/>
    <div class="pagination" style="display:inline;"><a href="${browseruri}?${query}"><fmt:message key="pagination.previous"/></a></div>&nbsp;&nbsp;
</c:if>
<%--First--%>
<c:if test="${currPage != 1}">
    <spring:eval expression="params.exclude('page').toString()" var="query"/>
    <div class="pagination" style="display:inline;"><a href="${browseruri}?${query}"><fmt:message key="pagination.first"/></a></div>&nbsp;&nbsp;
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
        <div class="pagination" style="display:inline; font-weight:bold; font-size:13px; background-color:#E7E7E7; padding:1px 3px 1px 3px">${page.index}</div>&nbsp;&nbsp;
    </c:if>
    <c:if test="${!(currPage == page.index)}">
        <spring:eval expression="params.add('page',page.index).toString()" var="query"/>
        <a href="${browseruri}?${query}" class="pagination">${page.index}</a>&nbsp;&nbsp;
    </c:if>
</c:forEach>

<%--Last--%>
<c:if test="${currPage != totalPage}">
    <spring:eval expression="params.add('page',totalPage).toString()" var="query"/>
    <div class="pagination" style="display:inline;"><a href="${browseruri}?${query}"><fmt:message key="paginationlast"/></a></div>&nbsp;&nbsp;
</c:if>
<%--Next--%>
<c:if test="${currPage * itemsPerPage < totalItems}">
    <c:set value="${currPage + 1}" var="nextPage"/>
    <spring:eval expression="params.add('page', nextPage).toString()" var="query"/>
    <div class="pagination" style="display:inline;"><a href="${browseruri}?${query}"><fmt:message key="pagination.next"/></a></div>&nbsp;&nbsp;
</c:if>
</div>
</c:if>