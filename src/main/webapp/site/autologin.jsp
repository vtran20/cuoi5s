<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>Auto Login</title>
</head>

<body onload="document.form.submit()">
                <form class="form-horizontal" name="form" id="form" method="post" action="/admin/j_spring_security_check">
                    <input name="domain" type="hidden" value="${domain}"/>
                    <input name="j_username" type="hidden" value="${j_username}"/>
                    <input name="j_password" type="hidden" value="${j_password}"/>
                </form>
</body>
</html>