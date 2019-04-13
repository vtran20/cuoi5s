<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="billing.shipping.info"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<h:authenticate request="${pageContext.request}" response="${pageContext.response}"/>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.create.new.website"/></h1>
    </div>
</div>
<script>
    $(function () {
        $("#form").validate({
            rules:{
                firstName:"required",
                lastName:"required",
                phone:"required"

            },

            messages:{
                firstName:"<fmt:message key="site.register.firstnameisrequied"/>",
                lastName:"<fmt:message key="site.register.lastnameisrequired"/>",
                phone:"<fmt:message key="site.register.phoneisrequired"/>"

            },
            errorPlacement: function(error, element) {
                if (element.attr("name") == "partner") {
                    error.insertAfter("#errormessagehere");
                } else {
                    error.insertAfter(element);
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
<div class="container content">
<div class="row">
<!-- Begin Sidebar Menu -->
    <jsp:include page="leftnav.jsp"/>
<!-- End Sidebar Menu -->
    <spring:eval expression="T(com.easysoft.ecommerce.controller.SessionUtil).loadUser(pageContext.request, pageContext.response)" var="user"/>
<!-- Begin Content -->
<div class="col-md-9">
    <div class="row">
        <!-- Begin Sidebar Menu -->
        <div class="col-md-8">
            <div class="panel panel-red margin-bottom-40">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.account.information"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal" role="form" action="/site/update_account.html" id="form" method="post">
                        <input name="id" type="hidden" value="${user.id}">
                        <h:frontendmessage _messages="${messages}"/>
                        <div class="form-group">
                            <label for="firstName" class="col-lg-3 control-label"><fmt:message key="site.register.firstName"/></label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control" id="firstName" name="firstName" maxlength="50" placeholder="<fmt:message key="site.register.firstName"/>" value="${user.firstName}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lastName" class="col-lg-3 control-label"><fmt:message key="site.register.lastName"/></label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control" id="lastName" name="lastName" maxlength="50" placeholder="<fmt:message key="site.register.lastName"/>" value="${user.lastName}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="phone" class="col-lg-3 control-label"><fmt:message key="site.register.phone"/></label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control" id="phone" name="phone" maxlength="20" placeholder="<fmt:message key="site.register.phone"/>" value="${user.phone}">
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
        <div class="col-md-4">
            <div class="panel panel-red margin-bottom-40">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="user.balance.information"/></h3>
                </div>
                <div class="panel-body">

                    <div class="input-group margin-bottom-20">
                        <c:set var="balenceMoney" value="${user.balance}"/>
                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(balenceMoney,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toStringKeepZero()" var="balance"/>
                        <h3>${balance}</h3>
                    </div>

                    <form class="form-horizontal" role="login" action="#" id="login" method="post">
                        <h:frontendmessage _messages="${error}"/>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <input type="text" value="" placeholder="<fmt:message key="site.deposit.money"/>" maxlength="20" name="deposit" id="deposit" class="form-control">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">

                            </div>
                            <div class="col-md-6">
                                <button type="button" class="btn-u btn-u-red pull-right"><fmt:message key="site.deposit.money"/></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End Content -->
</div>
</div>

<script type="text/javascript">
    //Sidebar Navigation Toggle
    jQuery('.list-toggle').on('click', function() {
        jQuery(this).toggleClass('active');
    });
</script>
</body>
</html>
