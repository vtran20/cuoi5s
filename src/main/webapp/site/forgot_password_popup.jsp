<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<!DOCTYPE html>
<head>
    <title><fmt:message key="site.forget.password"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.forget.password"/></h1>
    </div>
</div>

<script>
    $(function () {
        $("#form").validate({
            rules:{
                email:{
                    required:true,
                    email:true
                },
                captcha:"required"

            },

            messages:{
                email:{
                    required:"<fmt:message key="site.register.emailisrequired"/>",
                    email:"<fmt:message key="site.register.emailisinvalid"/>",
                    remote:jQuery.format("<fmt:message key="site.register.emailinused"/>")
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
<div class="container content margin-top-20">
    <div class="row">
        <div class="col-md-7">
        <div class="panel panel-red margin-bottom-40">
            <div class="panel-heading">
                <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.forget.password"/></h3>
            </div>
            <div class="panel-body">
                <form class="form-horizontal" role="form" action="/site/reset_password.html" id="form" method="post">
                    <h:frontendmessage _messages="${messages}"/>
                    <div class="form-group">
                        <label for="userName" class="col-lg-3 control-label">Email</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="userName" name="userName" maxlength="100" placeholder="Email">
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
                            <button type="submit" class="btn-u btn-u-red"><fmt:message key="site.forgot.password.send"/></button>
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
