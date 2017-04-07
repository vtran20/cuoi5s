<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="siteSupportId" value="${param.id}"/>
<c:if test="${! empty siteSupportId}">
    <spring:eval expression="serviceLocator.siteSupportDao.findById(T(java.lang.Long).valueOf(siteSupportId))" var="siteSupport"/>
</c:if>

<script>
    $('[data-rel=popover]').popover({container:'body'});

    $(function () {

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

        $("#form").submit(function() {
            var form = $( "#form" );
            if (form.valid()) {
                var url = "/admin/sites/savesitesupport.html"; // the script where you handle the form input.
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

    });
</script>

<div class="row">
    <div class="col-xs-12">
        <div id="message_alert"></div>
        <form name="form" id="form" action="/admin/sites/savesitesupport.html" method="post">
            <input type="hidden" name="id" value="${siteSupportId}"/>
            <h:csrf/>
            <div class="form-group">
                <label class="control-label" for="name"><fmt:message key="setting.support.service.support"/>*</label>

                <div class="controls">
                    <input name="name" class="required input-xlarge" id="name" type="text" value="${siteSupport.name}" maxlength="50" autofocus/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="chatId"><fmt:message key="setting.support.chat.id"/></label>

                <div class="controls">
                    <input name="chatId" class="input-xlarge" id="chatId" type="text" value="${siteSupport.chatId}" maxlength="25"/>
                    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="setting.support.chatid.example"/>" title="">?</span>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="chatType"><fmt:message key="setting.support.chat.type"/></label>

                <div class="controls">
                    <h:stringparamselector name="chatType" stringParam="CHAT_TYPE" defaultValue="${siteSupport.chatType}" includeTitle="Chọn Dịch Vụ"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="phone"><fmt:message key="setting.support.phonenumber"/></label>
                <div class="controls">
                    <input name="phone" class="input-xlarge" id="phone" type="text" value="${siteSupport.phone}" maxlength="20"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="timeAvailable"><fmt:message key="setting.support.support.time"/></label>
                <div class="controls">
                    <input name="timeAvailable" class="input-xlarge" id="timeAvailable" type="text" value="${siteSupport.timeAvailable}" maxlength="100"/>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm btn-primary" type="submit">
                    <i class="ace-icon fa fa-check"></i>
                    <fmt:message key="common.save.changes"/>
                </button>
            </div>
        </form>
    </div>
</div>
