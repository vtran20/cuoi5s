<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<!DOCTYPE html>
<head>
    <title><fmt:message key="site.register.account"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.register.account"/></h1>
    </div>
</div>

<script>
    $(function () {
        $.validator.addMethod("comparePassword", function(value, element) {
            return $('#password').val() == $('#repassword').val()
        }, "<fmt:message key="site.register.password.and.repassword.different"/>");

        $("#form").validate({
            rules:{
                siteCode:{
                    required:true,
                    remote:"/site/checkwebsite.html"
                },
                firstName:"required",
                lastName:"required",
                email:{
                    required:true,
                    email:true,
                    remote:"/site/checkemailused.html"
                },
                password:{
                    required:true,
                    minlength:4
                },
                repassword:{
                    required:true,
                    minlength:4,
                    comparePassword: true
                },
                captcha:"required"

            },

            messages:{
                siteCode:{
                    required:"<span><fmt:message key="site.register.sitecodeisrequired"/></span>",
                    remote:jQuery.format("<fmt:message key="site.register.sitecodeinused"/>")
                },
                firstName:"<fmt:message key="site.register.firstnameisrequied"/>",
                lastName:"<fmt:message key="site.register.lastnameisrequired"/>",
                email:{
                    required:"<fmt:message key="site.register.emailisrequired"/>",
                    email:"<fmt:message key="site.register.emailisinvalid"/>",
                    remote:jQuery.format("<fmt:message key="site.register.emailinused"/>")
                },
                password:{
                    required:"<fmt:message key="site.register.passwordrequired"/>",
                    minlength:jQuery.format("<fmt:message key="site.register.passwordminimumrequired"/>")
                },
                repassword:{
                    required:"<fmt:message key="site.register.passwordrequired"/>",
                    minlength:jQuery.format("<fmt:message key="site.register.passwordminimumrequired"/>"),
                    comparePassword: "<fmt:message key="site.register.password.and.repassword.different"/>"
                },
                captcha:"<fmt:message key="site.register.captchaisrequired"/>"

            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });

        $('#form input[name="siteCode"]').blur(function () {
            $("p.help-block b").text($(this).attr("value"));
        });

    });

    //onload call.
    $(document).ready(function() {
        var code = $('#form input[name="siteCode"]');
        $("p.help-block b").text($(code).attr("value"));
    });
</script>
<style>
    .form-group.has-error em.invalid {
        display: block;
        margin-top: 6px;
        padding: 0 1px;
        font-style: normal;
        /*font-size: 11px;*/
        line-height: 15px;
        color: #a94442;
    }
</style>
<div class="container content">
    <div class="row">
        <!-- Begin Sidebar Menu -->
        <div class="col-md-7">
            <div class="panel panel-red margin-bottom-40">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.register.account"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal" role="form" action="/site/dang-ky.html" id="form" method="post">
                        <h:frontendmessage _messages="${messages}"/>
                        <div class="form-group">
                            <label for="email" class="col-lg-3 control-label">Email</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control" id="email" name="email" maxlength="100" placeholder="Email" value="${entity.email[0]}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password" class="col-lg-3 control-label"><fmt:message key="site.register.password"/></label>
                            <div class="col-lg-9">
                                <input type="password" class="form-control" id="password" name="password" maxlength="40" placeholder="<fmt:message key="site.register.password"/>" value="${entity.password[0]}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="repassword" class="col-lg-3 control-label"><fmt:message key="site.register.repassword"/></label>
                            <div class="col-lg-9">
                                <input type="password" class="form-control" id="repassword" name="repassword" maxlength="40" placeholder="<fmt:message key="site.register.repassword"/>" value="${entity.repassword[0]}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-3 control-label"></label>
                            <div class="col-lg-9">
                                <spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SITE_KEY')" var="siteKey"/>
                                <spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SECRET_KEY')" var="siteSecret"/>
                                <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).createSToken(siteSecret)" var="encryptedString"/>
                                <div class="g-recaptcha" data-sitekey="${siteKey}" data-stoken="${encryptedString}"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-offset-3 col-lg-9">
                                <button type="submit" class="btn-u btn-u-red"><fmt:message key="site.register.register"/></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div class="panel panel-red margin-bottom-40">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.login"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal" role="login" action="/site/login.html" id="login" method="post">
                        <input name="rememberMe" type="hidden" value="Y"/>
                        <h:frontendmessage _messages="${error}"/>
                        <div class="input-group margin-bottom-20">
                            <span class="input-group-addon"><i class="fa fa-user"></i></span>
                            <input type="text" name="userName" placeholder="Email" class="form-control">
                        </div>
                        <div class="input-group margin-bottom-20">
                            <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                            <input type="password" name="password" placeholder="<fmt:message key="site.register.password"/>" class="form-control">
                        </div>
                        <div class="row">
                            <div class="col-md-6">

                            </div>
                            <div class="col-md-6">
                                <button type="submit" class="btn-u btn-u-red pull-right"><fmt:message key="site.login"/></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>