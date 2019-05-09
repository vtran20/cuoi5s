<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.register.account"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<h:authenticate request="${pageContext.request}" response="${pageContext.response}"/>
<script>
    $(function () {
        $.validator.addMethod("siteCodeFormat", function(value, element) {
            reg = /^[a-z0-9._-]+$/i;
            return reg.test(value)
        }, "<fmt:message key="site.register.sitecode.format.invalid"/>");

        $("#form").validate({
            rules:{
                siteCode:{
                    required:true,
                    siteCodeFormat:true,
                    remote:"/site/checkwebsite.html"
                },
                captcha:"required"

            },

            messages:{
                siteCode:{
                    required:"<span><fmt:message key="site.register.sitecodeisrequired"/></span>",
                    remote:jQuery.format("<fmt:message key="site.register.sitecodeinused"/>")
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
            $("p#subdomain b").text($(this).attr("value"));
        });

    });

    //onload call.
    $(document).ready(function() {
        var code = $('#form input[name="siteCode"]');
        $("p#subdomain b").text($(code).attr("value"));
    });
</script>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.create.new.website"/></h1>
    </div>
</div>
<c:choose>
    <c:when test="${!empty sessionObject.templateId}">
        <spring:eval expression="serviceLocator.getTemplateDao().findById(T(java.lang.Long).valueOf(sessionObject.templateId))" var="sampleTemplate"/>
    </c:when>
    <c:otherwise>
        <spring:eval expression="serviceLocator.getTemplateDao().findUniqueBy('templateModel', 'M001P', site.id)" var="sampleTemplate"/>
    </c:otherwise>
</c:choose>
<div class="container content">
    <div class="row">
        <jsp:include page="leftnav.jsp"/>
        <!-- Begin Sidebar Menu -->
        <div class="col-md-7">
            <div class="panel panel-red margin-bottom-40">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.create.new.website"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal" role="form" action="/site/create-site.html" id="form" method="post">
                        <h:frontendmessage _messages="${messages}"/>
                        <input name="templateId" type="hidden" value="${sessionObject.templateId}">
                        <div class="form-group">
                            <label for="siteCode" class="col-lg-3 control-label"><fmt:message key="site.register.sitecode"/></label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control" name="siteCode" id="siteCode" maxlength="50" placeholder='<fmt:message key="site.subdomain.example"/>' value="${sessionObject.siteCode}" autofocus>
                                <%--<spring:eval expression="site.getSiteParam('FREE_SITE_DOMAIN')" var="siteUrl"/>--%>
                                <%--<c:if test="${empty siteUrl}">--%>
                                    <%--<spring:eval expression="systemContext.getGlobalConfig('free.site.domain')" var="siteUrl"/>--%>
                                <%--</c:if>--%>
                                <%--<p class="help-block" id="subdomain"><fmt:message key="your.website"/>: <b></b>.${siteUrl}</p>--%>
                            </div>
                        </div>
                        <%--<div class="form-group">--%>
                            <%--<label class="col-lg-3 control-label"><fmt:message key="site.register.templateCode"/></label>--%>
                            <%--<div class="col-lg-9">--%>
                                <%--<p class="help-block"><b>${sampleTemplate.templateModel}</b></p>--%>
                            <%--</div>--%>
                        <%--</div>--%>
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
                                <button type="submit" class="btn-u btn-u-red"><fmt:message key="site.create.new.website"/></button>
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
