<!DOCTYPE>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.create.new.website"/></title>
    <meta name="decorator" content="admin_new"/>
</head>

<body>
<div class="span10">
    <div>
        <ul class="breadcrumb">
            <li>
                <a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span>
            </li>
            <li class="active"><fmt:message key="site.create.new.website"/></li>
        </ul>
    </div>


    <!-- Portlet: Create Website -->
    <div class="box">
        <h4 class="box-header round-top"><fmt:message key="site.create.new.website"/>
            <a class="box-btn" title="close"><i class="icon-remove"></i></a>
            <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
            <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i class="icon-cog"></i></a>
        </h4>

        <div class="box-container-toggle">
            <div class="box-content">
                <h:frontendmessage _messages="${messages}"/>

                <div class="accordion" id="accordion2">
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne" id="step1">
                                <fmt:message key="create.website.website.information"/>
                            </a>
                        </div>
                        <div id="collapseOne" class="accordion-body collapse">
                            <div class="accordion-inner" id="create-site-form">
                            </div>
                        </div>
                        <h:reloadform id="create-site-form" url="/admin/sites/form.html"/>
                    </div>
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo" id="step2">
                                <fmt:message key="create.website.site.setting"/>
                            </a>
                        </div>
                        <div id="collapseTwo" class="accordion-body collapse">
                            <div class="accordion-inner" id="site-setting">
                            </div>
                        </div>
                        <h:reloadform id="site-setting" url="/admin/sites/site_setting.html"/>
                    </div>
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseThree" id="step3">
                                <fmt:message key="create.website.template.color"/>
                            </a>
                        </div>
                        <div id="collapseThree" class="accordion-body collapse">
                            <div class="accordion-inner" id="select-site-template">
                            </div>
                        </div>
                        <h:reloadform id="select-site-template" url="/admin/sites/templateandcolor.html"/>
                    </div>
                </div>
                <hr>
                <br>

            </div>
        </div>
        <!--/span-->
    </div>
    <!-- Portlet Create Website -->
</div>

<script>
    $(document).ready(function () {
        <spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
        var uri = '${uri}';
        if (${fn:endsWith(uri,'sites/update.html')}) {
//            $("#step1").collapse('hide');
//            $("#step2").collapse({
//                toggle: true
//
//            });
        }

    });
</script>

</body>
</html>