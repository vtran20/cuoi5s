<!DOCTYPE>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="setting.setting.site"/></title>
    <meta name="decorator" content="admin_new"/>
</head>

<body>
<div class="span10">
    <div>
        <ul class="breadcrumb">
            <li>
                <a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span>
            </li>
            <li class="active"><fmt:message key="setting.setting.site"/></li>
        </ul>
    </div>


    <!-- Portlet: Create Website -->
    <div class="box">
        <h4 class="box-header round-top"><fmt:message key="setting.setting.site"/>
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
                            <a class="accordion-toggle disabled" data-toggle="collapse" data-parent="#accordion2"
                               href="#collapseOne">
                                Thông tin liên hệ.
                            </a>
                        </div>
                        <div id="collapseOne" class="accordion-body in collapse" style="height: auto; ">
                            <div class="accordion-inner" id="sitesetting-contact-us">
                            </div>
                        </div>
                        <h:reloadform id="sitesetting-contact-us" url="/admin/sites/setting/contactus.html"/>
                    </div>
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2"
                               href="#collapseTwo">
                                Step 2: Select template for website.
                            </a>
                        </div>
                        <div id="collapseTwo" class="accordion-body collapse" style="height: 0;">
                            <div class="accordion-inner" id="select-site-template">
                            </div>
                        </div>
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

</body>
</html>