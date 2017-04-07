<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<!DOCTYPE html>
<head>
    <title><fmt:message key="site.change.password"/></title>
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
        $.validator.addMethod("comparePassword", function(value, element) {
            return $('#password').val() == $('#repassword').val()
        }, "<fmt:message key="site.register.password.and.repassword.different"/>");

        $("#form").validate({
            rules:{
                password:{
                    required:true,
                    minlength:4
                },
                repassword:{
                    required:true,
                    minlength:4,
                    comparePassword: true
                }
            },

            messages:{
                password:{
                    required:"<fmt:message key="site.register.passwordrequired"/>",
                    minlength:jQuery.format("<fmt:message key="site.register.passwordminimumrequired"/>")
                },
                repassword:{
                    required:"<fmt:message key="site.register.passwordrequired"/>",
                    minlength:jQuery.format("<fmt:message key="site.register.passwordminimumrequired"/>"),
                    comparePassword: "<fmt:message key="site.register.password.and.repassword.different"/>"
                }
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
                <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.change.password"/></h3>
            </div>
            <div class="panel-body">
                <form class="form-horizontal" role="form" action="/site/change_password.html" id="form" method="post">
                    <h:frontendmessage _messages="${messages}"/>
                    <div class="form-group">
                        <label for="password" class="col-lg-3 control-label"><fmt:message key="site.change.new.password"/></label>
                        <div class="col-lg-9">
                            <input type="password" class="form-control" id="password" name="password" maxlength="100" placeholder="<fmt:message key="site.change.new.password"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-lg-3 control-label"><fmt:message key="site.change.new.repassword"/></label>
                        <div class="col-lg-9">
                            <input type="password" class="form-control" id="repassword" name="repassword" maxlength="40" placeholder="<fmt:message key="site.change.new.repassword"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-offset-3 col-lg-9">
                            <button type="submit" class="btn-u btn-u-red"><fmt:message key="common.save.changes"/></button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>


        <%--<div class="col-md-6">--%>
        <%--<c:if test="${!empty sent}">--%>
            <%--<p>--%>
                <%--Mật khẩu tạm đã được gởi vào email của bạn. Bạn phải thay đổi mật khẩu khi đăng nhập thành công.--%>
            <%--</p>--%>
        <%--</c:if>--%>
        <%--<c:if test="${empty sent}">--%>
            <%--<form id="form" action="/user/reset_password.html" method="post">--%>
                <%--<p>--%>
                    <%--<fmt:message key="forgot.password.message"/>--%>
                <%--</p>--%>
                <%--<br>--%>
                <%--<!-- user Name -->--%>
                <%--<label for="userName" class="glo-tex-blk">Email:</label><br>--%>
                <%--<input class="form-control required email" id="userName" name="userName" type="text" size="30" maxlength="50"/>--%>
                <%--<c:if test="${!empty messages}">--%>
                    <%--<spring:eval expression="messages.getErrorMessage('email_invalid')" var="email_invalid"/>--%>
                    <%--<c:if test="${!empty email_invalid}">--%>
                        <%--<br><span class="error">${email_invalid}</span>--%>
                    <%--</c:if>--%>
                <%--</c:if>--%>
                <%--<br/>--%>
                <%--<spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SITE_KEY')" var="siteKey"/>--%>
                <%--<spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SECRET_KEY')" var="siteSecret"/>--%>
                <%--<spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).createSToken(siteSecret)" var="encryptedString"/>--%>
                <%--<div class="g-recaptcha" data-sitekey="${siteKey}" data-stoken="${encryptedString}"></div>--%>
                <%--<br/>--%>
                <%--<button class="btn-u" type="submit">Send</button>--%>

            <%--</form>--%>
        <%--</c:if>--%>
        <%--</div>--%>
    </div>
</div>
</body>
</html>
