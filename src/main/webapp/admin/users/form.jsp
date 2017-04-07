<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>New User</title>
    <meta name="decorator" content="admin"/>
</head>

<body>

<sec:authentication property="principal.site" var="site"/>
<c:if test="${empty site}">
    <spring:eval expression="serviceLocator.siteDao.findAll()" var="sites"/>
</c:if>
<c:if test="${not empty site}">
    <spring:eval expression="serviceLocator.siteDao.findBy('id',site.id)" var="sites"/>
</c:if>

<c:if test="${not empty command.id}">
    <c:set var="title">Update User</c:set>
    <c:set var="formurl">/admin/users/${command.id}/index.html</c:set>
    <c:set var="action">Update</c:set>
</c:if>
<c:if test="${empty command.id}">
    <c:set var="title">New User</c:set>
    <c:set var="formurl">/admin/users/index.html</c:set>
    <c:set var="action">Add</c:set>
</c:if>

<h1 class="page_title">${title}</h1>
<form:form id="form" action="${formurl}" commandName="command" method="post">
    <table>
        <tr>
            <td>Username</td>
            <td><form:input path="username"/></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><form:input path="password"/></td>
        </tr>
            <%--<tr><td>Site</td><td><form:select path="site.id"><form:options items="${sites}" itemValue="id" itemLabel="name"/></form:select></td></tr>--%>
            <%--<sec:authorize ifAnyGranted="ROLE_SYSTEM_ADMIN"><tr><td>System Admin</td><td><form:checkbox path="systemAdmin" /></td></tr></sec:authorize>--%>
            <%--<tr><td>Blocked</td><td><form:checkbox path="blocked" /></td></tr>--%>
        <tr>
            <td>Email</td>
            <td><form:input path="email"/></td>
        </tr>

        <tr>
            <td>First Name</td>
            <td><form:input path="firstName"/></td>
        </tr>
        <tr>
            <td>Last Name</td>
            <td><form:input path="lastName"/></td>
        </tr>
        <tr>
            <td>Address</td>
            <td><form:input path="address_1"/></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><form:input path="address_2"/></td>
        </tr>
        <tr>
            <td>District</td>
            <td><form:input path="district"/></td>
        </tr>
        <tr>
            <td>City</td>
            <td><form:input path="city"/></td>
        </tr>
        <tr>
            <td>State</td>
            <td><form:input path="state"/></td>
        </tr>
        <tr>
            <td>Zip Code</td>
            <td><form:input path="zipCode"/></td>
        </tr>
        <tr>
            <td>Country</td>
            <td><form:input path="country"/></td>
        </tr>
        <tr>
            <td>Phone</td>
            <td><form:input path="phone"/></td>
        </tr>

    </table>
    <input type="submit" value="${action}"/>
</form:form>

<script type="text/javascript" src="/wro/all.js"></script>
<script type="text/javascript">
    $().ready(function () {
        // validate signup form on keyup and submit
        $("#form").validate({
            rules:{
                username:"required",
                password:"required",
                email:"required"
            }});
    });
</script>

</body>
</html>
