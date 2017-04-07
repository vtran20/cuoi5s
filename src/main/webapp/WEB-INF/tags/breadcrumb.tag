<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="parCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="cat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="subCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="product" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Product"%>
<%@ attribute name="keyword" required="false" rtexprvalue="true" type="java.lang.String"%>

<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<%--Breadcrumb of category and product pages--%>
<c:if test="${fn:startsWith(uri,'/catalog/') or fn:startsWith(uri,'/category/') or fn:startsWith(uri,'/product/')}">

<%--Home page--%>
<c:set value="/" var="homeurl"/>
<a href="${homeurl}"><fmt:message key="breadcrumb.home"/></a>&nbsp;&gt;&nbsp;

<%-- Parent Category    --%>
<c:if test="${!empty parCat}">
    <c:if test="${empty cat and empty subCat and empty product}">
       <b><c:out value="${parCat.name}" escapeXml="true"/></b>
    </c:if>
    <c:if test="${not empty cat or not empty subCat or not empty product}">
        <c:set value="/category/${parCat.uri}-${parCat.id}" var="path"/>
        <a href="${path}/index.html"><c:out value="${parCat.name}" escapeXml="true"/></a>&nbsp;&gt;&nbsp;
    </c:if>
</c:if>

<%--Category    --%>
<c:if test="${!empty cat}">
    <c:if test="${empty subCat and empty product}">
       <b><c:out value="${cat.name}" escapeXml="true"/></b>
    </c:if>
    <c:if test="${not empty subCat or not empty product}">
        <c:set value="/category/${cat.uri}-${cat.id}" var="path"/>
        <a href="${path}/index.html"><c:out value="${cat.name}" escapeXml="true"/></a>&nbsp;&gt;&nbsp;
    </c:if>
</c:if>

<%--Sub Category    --%>
<c:if test="${!empty subCat}">
    <c:if test="${empty product}">
       <b><c:out value="${subCat.name}" escapeXml="true"/></b>
    </c:if>
    <c:if test="${not empty product}">
        <c:set value="${path}/${subCat.uri}-${subCat.id}" var="path"/>
        <a href="${path}/index.html"><c:out value="${subCat.name}" escapeXml="true"/></a>&nbsp;&gt;&nbsp;
    </c:if>
</c:if>

<%--Product detail   --%>
<c:if test="${!empty product}">
    <b><c:out value="${product.name}" escapeXml="true"/></b>
</c:if>

</c:if>

<c:if test="${fn:startsWith(uri,'/search.html')}">
    <%--Home page--%>
    <c:set value="/" var="homeurl"/>
    <a href="${homeurl}"><fmt:message key="breadcrumb.home"/></a>&nbsp;&gt;&nbsp;

    <b><c:out value="${keyword}" escapeXml="true"/></b>
</c:if>

<c:if test="${fn:startsWith(uri,'/giam-gia.html')}">
    <%--Home page--%>
    <c:set value="/" var="homeurl"/>
    <a href="${homeurl}"><fmt:message key="breadcrumb.home"/></a>&nbsp;&gt;&nbsp;

    <b><fmt:message key="discount.breacrumb"/></b>
</c:if>