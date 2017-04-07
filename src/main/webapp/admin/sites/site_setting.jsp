<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<title>Thiết Lập Cấu Hình Website</title>
<div class="page-header">
    <h4>Thiết Lập Cấu Hình Website</h4>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-sm-12">
                <!-- #section:elements.tab -->
                <div  id="message_alert"></div>
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="form-tab">
                        <li class="active">
                            <a data-toggle="tab" href="#home">
                                <i class="ace-icon fa fa-home"></i>
                                Ngôn Ngữ
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#template_format">
                                <i class="ace-icon fa fa-desktop"></i>
                                Định Dạng Website
                            </a>
                        </li>
                        <li>
                            <a data-toggle=tab href="#payment_method" data-tab-url="/admin/sites/payment_methods.html" data-tab-always-refresh="true">
                                <i class="ace-icon fa fa-money"></i>
                                Phương Thức Thanh Toán
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#shipping_method" data-tab-url="/admin/sites/shipping_form.html" data-tab-always-refresh="true">
                                <i class="ace-icon fa fa-truck"></i>
                                Phương Thức Vận Chuyển
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#tax">
                                <i class="ace-icon fa fa-flag"></i>
                                Thuế
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#social_connect">
                                <i class="ace-icon fa fa-facebook-square"></i>
                                Mạng Xã Hội
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#google_analytics">
                                <i class="ace-icon fa fa-google-plus-square"></i>
                                Google Analytics
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="home" class="tab-pane fade in active">
                            <form name="form_site_setting" id="form_site_setting" class="form-horizontal" action="#" method="post">
                                <h:csrf/>
                                <fieldset>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="language"><fmt:message key="site.form.site.language"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('LANGUAGE')" var="language"/>
                                            <select id="language" class="required" name="LANGUAGE">
                                                <option value="en_US" <c:if test="${language == 'en_US'}">selected</c:if>>English US</option>
                                                <option value="vi_VN" <c:if test="${language == 'vi_VN'}">selected</c:if>>Vietnam</option>
                                            </select>
                                            <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="site.form.language.explain"/>" title="">?</span>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="dateformat"><fmt:message key="site.form.site.date.format"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('DATE_FORMAT')" var="dateformat"/>
                                            <select id="dateformat" class="required" name="DATE_FORMAT">
                                                <option value="MM/dd/yyyy" <c:if test="${dateformat == 'MM/dd/yyyy'}">selected</c:if>>English US (MM/dd/yyyy)</option>
                                                <option value="dd/MM/yyyy" <c:if test="${dateformat == 'dd/MM/yyyy'}">selected</c:if>>Vietnam (dd/MM/yyyy)</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="moneyformat"><fmt:message key="site.form.site.money.format"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('CURRENCY_FORMAT')" var="moneyformat"/>
                                            <select id="moneyformat" class="required" name="CURRENCY_FORMAT">
                                                <option value="#0.00" <c:if test="${moneyformat == '#0.00'}">selected</c:if>>USD</option>
                                                <option value="#,###,##0" <c:if test="${moneyformat == '#,###,##0'}">selected</c:if>>VND</option>
                                            </select>
                                        </div>
                                    </div>
                                        <div class="modal-footer">
                                            <button class="btn btn-sm btn-primary pull-left" type="submit">
                                                <i class="ace-icon fa fa-check"></i>
                                                <fmt:message key="common.save.changes"/>
                                            </button>
                                        </div>
                                </fieldset>
                            </form>
                        </div>
                        <div id="template_format" class="tab-pane fade">
                            <form name="template_update" id="template_update" class="form-horizontal" action="#" method="post">
                                <spring:eval expression="serviceLocator.siteTemplateDao.findUniqueBy('site.id', site.id)" var="siteTemplate"/>
                                <input name="id" type="hidden" value="${site.id}"/>

                                <fieldset>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="templateColorCode"><fmt:message key="site.form.site.color"/></label>
                                        <div class="col-sm-10 forms">
                                            <style>
                                                .list-unstyled {
                                                    padding-left: 0;
                                                    list-style: none;
                                                }
                                                .style-switcher li {
                                                    width: 26px;
                                                    height: 26px;
                                                    cursor: pointer;
                                                    background: #c00;
                                                    margin: 0 5px 5px 0;
                                                    display: inline-block;
                                                    border-radius: 10% !important;
                                                    border: solid 2px #FFF;
                                                }
                                                ul {
                                                    padding: 0;
                                                    margin: 0 0 9px 0px;
                                                }
                                                .style-switcher li.theme-green {
                                                    background: #72c02c;
                                                }
                                                .style-switcher li.theme-blue {
                                                    background: #3498db;
                                                }
                                                .style-switcher li.theme-orange {
                                                    background: #e67e22;
                                                }
                                                .style-switcher li.theme-red {
                                                    background: #e74c3c;
                                                }
                                                .style-switcher li.theme-light {
                                                    background: #ecf0f1;
                                                }
                                                .style-switcher li.theme-purple {
                                                    background: #9b6bcc;
                                                }
                                                .style-switcher li.theme-aqua {
                                                    background: #27d7e7;
                                                }
                                                .style-switcher li.theme-brown {
                                                    background: #9c8061;
                                                }
                                                .style-switcher li.theme-dark-blue {
                                                    background: #4765a0;
                                                }
                                                .style-switcher li.theme-light-green {
                                                    background: #79d5b3;
                                                }
                                                .style-switcher li.theme-dark-red {
                                                    background: #a10f2b;
                                                }
                                                .style-switcher li.theme-teal {
                                                    background: #18ba9b;
                                                }
                                                .style-switcher li:hover, .style-switcher li.theme-active {
                                                    border: solid 2px red;
                                                }
                                            </style>
                                            <input name="templateColorCode" type="hidden" value="${siteTemplate.templateColorCode}" id="templateColorCode"/>
                                            <div class="col-sm-10 forms style-switcher animated fadeInRight" style="display: block;">
                                                <ul class="list-unstyled" id="color-group">
                                                    <li class="theme-green <c:if test="${siteTemplate.templateColorCode == 'green'}">theme-active</c:if>" data-style="green" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-blue <c:if test="${siteTemplate.templateColorCode == 'blue'}">theme-active</c:if>" data-style="blue" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-orange <c:if test="${siteTemplate.templateColorCode == 'orange'}">theme-active</c:if>" data-style="orange" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-red <c:if test="${siteTemplate.templateColorCode == 'red'}">theme-active</c:if>" data-style="red" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-light <c:if test="${siteTemplate.templateColorCode == 'light'}">theme-active</c:if>" data-style="light" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-purple <c:if test="${siteTemplate.templateColorCode == 'purple'}">theme-active</c:if>" data-style="purple" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-aqua <c:if test="${siteTemplate.templateColorCode == 'aqua'}">theme-active</c:if>" data-style="aqua" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-brown <c:if test="${siteTemplate.templateColorCode == 'brown'}">theme-active</c:if>" data-style="brown" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-dark-blue <c:if test="${siteTemplate.templateColorCode == 'dark-blue'}">theme-active</c:if>" data-style="dark-blue" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-light-green <c:if test="${siteTemplate.templateColorCode == 'light-green'}">theme-active</c:if>" data-style="light-green" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-dark-red <c:if test="${siteTemplate.templateColorCode == 'dark-red'}">theme-active</c:if>" data-style="dark-red" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                    <li class="theme-teal <c:if test="${siteTemplate.templateColorCode == 'teal'}">theme-active</c:if>" data-style="teal" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="fullWide"><fmt:message key="site.form.site.layout"/></label>
                                        <div class="col-sm-10 forms">
                                            <select id="fullWide" name="fullWide" lang="<fmt:message key="site.form.do.you.want.change.layout"/>">
                                                <option value="Y" <c:if test="${siteTemplate.fullWide == 'Y'}">selected</c:if>><fmt:message key="site.form.site.layout.full.screen"/></option>
                                                <option value="N" <c:if test="${siteTemplate.fullWide != 'Y'}">selected</c:if>><fmt:message key="site.form.site.layout.boxed"/></option>
                                            </select>
                                        </div>
                                    </div>
                                    <%--<div class="form-group">--%>
                                        <%--<label class="col-sm-2 control-label no-padding-right" for="headerFix"><fmt:message key="site.form.site.header.fixed"/></label>--%>
                                        <%--<div class="col-sm-10 forms">--%>
                                            <%--<select id="headerFix" name="headerFix" lang="<fmt:message key="site.form.do.you.want.change.header.fix"/>">--%>
                                                <%--<option value="Y" <c:if test="${siteTemplate.headerFix == 'Y'}">selected</c:if>>Fix Header</option>--%>
                                                <%--<option value="N" <c:if test="${siteTemplate.headerFix != 'Y'}">selected</c:if>>No Fix Header</option>--%>
                                            <%--</select>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="skinColor"><fmt:message key="site.form.site.skin.color"/></label>
                                        <div class="col-sm-10 forms">
                                            <select id="skinColor" name="skinColor" lang="<fmt:message key="site.form.do.you.want.change.skin.color"/>">
                                                <option value="light" <c:if test="${siteTemplate.skinColor != 'light'}">selected</c:if>><fmt:message key="site.form.skin.color.light"/></option>
                                                <option value="dark" <c:if test="${siteTemplate.skinColor == 'dark'}">selected</c:if>><fmt:message key="site.form.skin.color.dark"/></option>
                                            </select>
                                        </div>
                                    </div>

                                </fieldset>
                            </form>


                        </div>
                        <div id="payment_method" class="tab-pane fade">
                            <p>Loading</p>
                        </div>
                        <div id="shipping_method" class="tab-pane fade">
                            <p>Loading</p>
                        </div>
                        <div id="tax" class="tab-pane fade">
                            <form name="form_google_analytics" id="form_tax" class="form-horizontal" action="#" method="post">
                                <h:csrf/>
                                <fieldset>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="moneyformat"><fmt:message key="site.form.site.tax.percent"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('VAT_TAX')" var="taxPercent"/>
                                            <input name="VAT_TAX" class="input-xlarge" maxlength="250" id="name" type="text" value="${taxPercent}" autofocus/> %
                                            (ví dụ: 10%)
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button class="btn btn-sm btn-primary pull-left" type="submit">
                                            <i class="ace-icon fa fa-check"></i>
                                            <fmt:message key="common.save.changes"/>
                                        </button>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                        <div id="social_connect" class="tab-pane fade">
                            <form name="form_social_connect" id="form_social_connect" class="form-horizontal" action="#" method="post">
                                <h:csrf/>
                                <fieldset>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="moneyformat"><fmt:message key="site.form.site.facebook.fanpage"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('FACEBOOK_FAN_PAGE')" var="facebookFanPage"/>
                                            <input name="FACEBOOK_FAN_PAGE" class="input-xxlarge" maxlength="250" id="name" type="text" value="${facebookFanPage}" autofocus/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="moneyformat"><fmt:message key="site.form.site.google.fanpage"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('GOOGLE_FAN_PAGE')" var="googleFanPage"/>
                                            <input name="GOOGLE_FAN_PAGE" class="input-xxlarge" maxlength="250" id="name" type="text" value="${googleFanPage}" autofocus/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="moneyformat"><fmt:message key="site.form.site.youtube.fanpage"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('YOUTUBE_FAN_PAGE')" var="youtubeFanPage"/>
                                            <input name="YOUTUBE_FAN_PAGE" class="input-xxlarge" maxlength="250" id="name" type="text" value="${youtubeFanPage}" autofocus/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="moneyformat"><fmt:message key="site.form.site.twitter.fanpage"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('TWITTER_FAN_PAGE')" var="twitterFanPage"/>
                                            <input name="TWITTER_FAN_PAGE" class="input-xxlarge" maxlength="250" id="name" type="text" value="${twitterFanPage}" autofocus/>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button class="btn btn-sm btn-primary pull-left" type="submit">
                                            <i class="ace-icon fa fa-check"></i>
                                            <fmt:message key="common.save.changes"/>
                                        </button>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                        <div id="google_analytics" class="tab-pane fade">
                            <form name="form_google_analytics" id="form_google_analytics" class="form-horizontal" action="#" method="post">
                                <h:csrf/>
                                <fieldset>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right" for="moneyformat"><fmt:message key="site.form.site.google.analytics"/></label>
                                        <div class="col-sm-10 forms">
                                            <spring:eval expression="site.siteParamsMap.get('GOOGLE_ANALYSIS_ACCOUNT')" var="googleAnalytics"/>
                                            <input name="GOOGLE_ANALYSIS_ACCOUNT" class="input-xxlarge" maxlength="250" id="name" type="text" value="${googleAnalytics}" autofocus/>
                                            (ví dụ: UA-50848317-2)
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button class="btn btn-sm btn-primary pull-left" type="submit">
                                            <i class="ace-icon fa fa-check"></i>
                                            <fmt:message key="common.save.changes"/>
                                        </button>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $('[data-rel=popover]').popover({container:'body'});

    $(function () {

        $("#form_site_setting").submit(function() {
            var url = "/admin/sites/sp_update.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form_site_setting").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });
        $("#form_social_connect").submit(function() {
            var url = "/admin/sites/sp_update.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form_social_connect").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });
        $("#form_google_analytics").submit(function() {
            var url = "/admin/sites/sp_update.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form_google_analytics").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });
        $("#form_tax").submit(function() {
            var url = "/admin/sites/sp_update.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form_tax").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });

        $("ul#color-group li").on('click',function() {
            var object = $(this);
            var originalColor = '${siteTemplate.templateColorCode}';
            $("input#templateColorCode").val($(this).attr("data-style"));
            BootstrapDialog.confirm(object.attr("lang"), function(result){
                if(result) {
                    var url = "/admin/sites/template_update.html"; // the script where you handle the form input.
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: $("#template_update").serialize(), // serializes the form's elements.
                        success: function(data)
                        {
                            $('#message_alert').html(data);
                            $("ul#color-group li").removeClass("theme-active");
                            object.addClass("theme-active");
                            $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                $("#message_alert").alert('close');
                            });
                        }
                    });
                    return false; // avoid to execute the actual submit of the form.
                }else {
                    $("input#templateColorCode").val(originalColor);
                }
            });
        });

        $("#fullWide").on('change', function() {
            var object = $(this);
            BootstrapDialog.confirm(object.attr("lang"), function(result){
                if(result) {
                    var url = "/admin/sites/template_update.html"; // the script where you handle the form input.
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: $("#template_update").serialize(), // serializes the form's elements.
                        success: function(data)
                        {
                            $('#message_alert').html(data);
                            $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                $("#message_alert").alert('close');
                            });
                        }
                    });
                    return false; // avoid to execute the actual submit of the form.
                }
            });
        });
        $("#headerFix").on('change', function() {
            var object = $(this);
            BootstrapDialog.confirm(object.attr("lang"), function(result){
                if(result) {
                    var url = "/admin/sites/template_update.html"; // the script where you handle the form input.
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: $("#template_update").serialize(), // serializes the form's elements.
                        success: function(data)
                        {
                            $('#message_alert').html(data);
                            $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                $("#message_alert").alert('close');
                            });
                        }
                    });
                    return false; // avoid to execute the actual submit of the form.
                }
            });
        });
        $("#skinColor").on('change', function() {
            var object = $(this);
            BootstrapDialog.confirm(object.attr("lang"), function(result){
                if(result) {
                    var url = "/admin/sites/template_update.html"; // the script where you handle the form input.
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: $("#template_update").serialize(), // serializes the form's elements.
                        success: function(data)
                        {
                            $('#message_alert').html(data);
                            $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                $("#message_alert").alert('close');
                            });
                        }
                    });
                    return false; // avoid to execute the actual submit of the form.
                }
            });
        });


    });

    var remoteTabsPluginLoaded = new RemoteTabs();

</script>