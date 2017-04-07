<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="serviceLocator.siteSupportDao.findByOrder('showFooter', 'N', 'sequence', site.id)" var="siteSupports"/>
<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div id="modal_message_alert"></div>
            <div class="col-xs-12">
                <c:if test="${!empty siteSupports}">
                    <table id="simple-table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th><fmt:message key="setting.support.support.list"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${siteSupports}" varStatus="support" var="currentSupport">
                            <tr id="${support.current.id}-s${support.current.sequence}">
                                <td>
                                    <c:out value="${currentSupport.name}"/><br>
                                    <fmt:message key="setting.support.chat.id"/>: <c:out value="${currentSupport.chatId}"/>, <fmt:message key="setting.support.chat.type"/>: <c:out value="${currentSupport.chatType}"/>,
                                    <fmt:message key="setting.support.phonenumber"/>: <c:out value="${currentSupport.phone}"/>, <fmt:message key="setting.support.support.time"/>: <c:out value="${currentSupport.timeAvailable}"/>
                                </td>
                                <td class="center">
                                    <input id="showFooter_${currentSupport.id}" class="showFooter-toggle-checkbox" type="checkbox" name="showFooter" value="${currentSupport.id}" ${currentSupport.showFooter == 'Y'?'checked':''} data-src="/admin/sites/${currentSupport.id}/showSupportInFooter.html?csrf=<sec:authentication property='details.csrf'/>" data-header="<fmt:message key='common.confirm.title'/>" lang='<fmt:message key="common.doyouwantaddsupporttofooter"/>' confirm-success="<fmt:message key='common.data.saved.success'/>" confirm-fail="<fmt:message key='common.data.saved.fail'/>">
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty siteSupports}">
                    <fmt:message key="common.is.empty"/>
                </c:if>
            </div><!-- /.span -->
        </div><!-- /.row -->
    </div>
</div>
<script type="text/javascript">
    //Show confirm and process check/uncheck checkbox
    $(".showFooter-toggle-checkbox").on("click", function() {
        var object = $(this);
        var flag = object.prop( "checked" );
        BootstrapDialog.show({
            type:BootstrapDialog.TYPE_DANGER,
            closeByBackdrop: false,
            closeByKeyboard: false,
            title:object.attr("data-header"),
            message:object.attr("lang"),
            buttons:[
                {   label:'Yes',
                    action:function (dialog) {
                        $.get(object.attr("data-src"), { flag: flag}, // some params to pass it
                                function(data) {
                                    $('#modal_message_alert').html(data);
                                    $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                        $("#modal_message_alert").alert('close');
                                        //don't want reload when the modal closed
                                        //$('#modal_message_alert').html("");
                                        //release modal
                                        $('#modal-form').removeData('bs.modal');
                                    });
//                                    if ("ok" == data) {
//                                        BootstrapDialog.show({
//                                            type:BootstrapDialog.TYPE_INFO,
//                                            message:object.attr("confirm-success")
//                                        })
//                                    } else {
//                                        BootstrapDialog.show({
//                                            type:BootstrapDialog.TYPE_DANGER,
//                                            message:object.attr("confirm-fail")
//                                        })
//                                        object.prop("checked", !flag)
//                                    }
                                });
                        dialog.close();
                    }
                },
                {
                    label:'No',
                    action:function (dialog) {
                        object.prop("checked", !flag);
                        dialog.close();
                    }
                }
            ]
        });

    });
</script>