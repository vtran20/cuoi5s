<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.login"/></title>
    <link href="/admin/assets/css/bootstrap.css" rel="stylesheet">
    <link href="/admin/assets/css/bootstrap-responsive.css" rel="stylesheet">

    <script src="/admin/assets/js/jquery-1.7.1.min.js" type="text/javascript"></script>
    <script src="/admin/assets/js/bootstrap.min.js" type="text/javascript"></script>

    <%--jQuery Validate--%>
    <script src="/admin/assets/admin_new/js/jquery.validate.min.js"></script>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js" type="text/javascript"></script>
    <![endif]-->

    <style type="text/css">
        body {
            padding-top: 160px;
            padding-bottom: 40px;
        }

        label.error {
            padding-top: 5px;
            vertical-align: middle;
            color: #B94A48;
        }
    </style>
    <script>
        $(function () {

            $("#admin_login").validate({
//            rules: {
//                name: "required"
//                uri: "required"
//            },

                messages:{
                    <%--name:"<fmt:message key="menu.name.required"/>"--%>
                },
                highlight:function (label) {
                    $(label).closest('.control-group').removeClass('success');
                    $(label).closest('.control-group').addClass('error');
                },
                success:function (label) {
                    $(label).closest('.control-group').removeClass('error');
                    $(label).closest('.control-group').addClass('success');
                }
            });

        });
    </script>

</head>

<body>
<%--<h3>Login with Username and Password</h3>--%>

<%--<form action="/j_spring_security_check" method="post">--%>
<%--<table>--%>
<%--<tr>--%>
<%--<td>Users:</td>--%>
<%--<td><input type='text' name='j_username'/></td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td>Password:</td>--%>
<%--<td><input type='password' name='j_password'/></td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td colspan="2"><input name="submit" type="submit"/></td>--%>
<%--</tr>--%>
<%--</table>--%>
<%--</form>--%>


<div class="container-fluid">

    <div class="row-fluid">
        <div class="rơw show-grid">
            <div class="span4"></div>
            <div class="span4 well">
                <h3>WEBPHATTAI.COM</h3>

                <p><fmt:message key="login.admin.site"/></p>
                <hr>
                <form id="admin_login" class="form-horizontal" name="form" method="post"
                      action="/admin/j_spring_security_check">
                    <input name="domain" type="hidden" value="${pageContext.request.serverName}"/>

                    <fieldset>
                        <c:if test="${not empty param.login_error}">
                            <div class="alert alert-danger">
                                    ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}
                            </div>
                        </c:if>
                        <div class="control-group">
                            <label class="control-label" for="userName"><fmt:message key="login.email"/></label>

                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-user"></i></span><input name="j_username"
                                                                                                class="input-medium required email"
                                                                                                id="userName" size="16"
                                                                                                type="text"
                                                                                                placeholder="Email" autofocus>
                                </div>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="password"><fmt:message key="login.password"/></label>

                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-lock"></i></span><input name="j_password"
                                                                                                class="input-medium required"
                                                                                                id="password" size="16"
                                                                                                type="password"
                                                                                                placeholder="Password">
                                </div>
                                <spring:eval expression="systemContext.getGlobalConfig('main.site.url')" var="siteUrl"/>
                                <p class="help-block"><a href="/site/forgot_password_popup.html"><fmt:message key="login.forgot.password"/></a>&nbsp;<a href="${siteUrl}/site/dang-ky.html"><fmt:message key="login.register"/></a></p>

                            </div>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary"><span class="add-on"><i
                                    class="icon-check icon-white"></i></span> <fmt:message key="login.submit"/>
                            </button>
                        </div>
                    </fieldset>
                </form>

            </div>
            <div class="span4"></div>
        </div>

    </div>
    <!-- end #content -->
</div>


</body>
</html>