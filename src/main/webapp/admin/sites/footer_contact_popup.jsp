<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="serviceLocator.siteContactDao.findByOrder('showFooter', 'N', 'sequence', site.id)" var="siteContacts"/>
<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div id="modal_message_alert"></div>
            <div class="col-xs-12">
                <c:if test="${!empty siteContacts}">
                    <table id="simple-table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th><fmt:message key="setting.contactus.contact.list"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${siteContacts}" varStatus="contact" var="currentContact">
                            <tr id="${contact.current.id}-s${contact.current.sequence}">
                                <td>
                                    <c:out value="${currentContact.name}"/><br>
                                    <c:out value="${currentContact.address_1}"/>, <c:out value="${currentContact.city}"/>, <c:out
                                        value="${currentContact.state}"/>&nbsp;<c:out value="${currentContact.zipCode}"/>. Email: <c:out
                                        value="${currentContact.email}"/>, P: <c:out value="${currentContact.phone}"/>, F: <c:out
                                        value="${currentContact.fax}"/>
                                </td>
                                <td class="center">
                                    <input id="showFooter_${currentContact.id}" class="showFooter-toggle-checkbox" type="checkbox" name="showFooter" value="${currentContact.id}" ${currentContact.showFooter == 'Y'?'checked':''} data-src="/admin/sites/${currentContact.id}/showContactInFooter.html?csrf=<sec:authentication property='details.csrf'/>" data-header="<fmt:message key='common.confirm.title'/>" lang='<fmt:message key="common.doyouwantaddcontacttofooter"/>' confirm-success="<fmt:message key='common.data.saved.success'/>" confirm-fail="<fmt:message key='common.data.saved.fail'/>">
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty siteContacts}">
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