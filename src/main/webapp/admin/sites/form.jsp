<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<title>Thông Tin Website</title>
<div class="page-header">
    <h4>Thông Tin Website</h4>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div  id="message_alert"></div>
        <form name="form" id="form" class="form-horizontal" action="/admin/sites/update.html" method="post">
            <h:csrf/>
            <input name="id" type="hidden" value="${site.id}"/>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" ><fmt:message key="site.secondary.url"/></label>

                <div class="col-sm-9">
                    <c:set var="domain" value="${site.subDomain}"/>
                    <label class="control-label no-padding-right" >${domain}</label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" ><fmt:message key="site.primary.url"/></label>
                <div class="col-sm-9">
                    <input name="domain" class="input-xlarge" id="domain" type="text" value="${site.domain}" maxlength="255"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="name"><fmt:message key="site.form.title"/></label>
                <div class="col-sm-9">
                    <input name="name" class="required input-xlarge" id="name" type="text" value="${site.name}" maxlength="255"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="description"><fmt:message key="site.form.description"/></label>
                <div class="col-sm-9">
                    <textarea name="description" class="input-xlarge" id="description" rows="3">${site.description}</textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="contactUsEmail"><fmt:message key="site.form.contact.email"/></label>
                <div class="col-sm-9">
                    <spring:eval expression="site.siteParamsMap.get('CONTACT_US')" var="contactUsEmail"/>
                    <input name="contactUsEmail" class="required input-xlarge" id="contactUsEmail" type="text" value="${contactUsEmail}" maxlength="255"/>
                    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="site.form.contact.email.explain"/>" title="">?</span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="contactUsPhone"><fmt:message key="site.form.contact.phone"/></label>
                <div class="col-sm-9">
                    <spring:eval expression="site.siteParamsMap.get('PHONE_CONTACT')" var="contactUsPhone"/>
                    <input name="contactUsPhone" class="required input-xlarge" id="contactUsPhone" type="text" value="${contactUsPhone}" maxlength="20"/>
                    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="site.form.contact.phone.explain"/>" title="">?</span>
                </div>
            </div>

            <div class="clearfix form-actions">
                <div class="col-md-offset-3 col-md-9">
                    <button type="submit" class="btn btn-primary"><fmt:message key="common.save.changes"/></button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    $('[data-rel=popover]').popover({container:'body'});

    // this is the id of the form
    $("#form").submit(function() {
        var form = $( "#form" );
        if (form.valid()) {
            var url = "/admin/sites/update.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form").serialize(), // serializes the form's elements.
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

    $("#form").validate({
        messages:{
            <%--name:"<fmt:message key="menu.name.required"/>"--%>
        },
        highlight:function (label) {
            $(label).closest('.form-group').removeClass('success');
            $(label).closest('.form-group').addClass('error');
        },
        success:function (label) {
            $(label).closest('.form-group').removeClass('error');
            $(label).closest('.form-group').addClass('success');
        }
    });
</script>

