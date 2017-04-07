<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:if test="${not empty site}">
    <spring:eval expression="serviceLocator.menuDao.findByOrder('usefulLink', 'N', 'sequence', site.id)" var="menus"/>
</c:if>
<div class="row">
    <div class="col-xs-12">
    <div id="modal_message_alert"></div>
    <!-- Portlet: Member List -->
            <div class="box" id="box-0">
                <h4 class="box-header round-top"><fmt:message key="menu.menu.list"/>
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>

                <div class="box-container-toggle">
                    <div class="box-content">
                        <c:if test="${!empty menus}">

                            <table cellpadding="0" cellspacing="0" border="0"
                                   class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                <thead>
                                <tr>
                                    <th><fmt:message key="menu.menu.name"/></th>
                                    <th><fmt:message key="menu.status"/></th>
                                    <th><fmt:message key="general.click.checkbox.if.you.want.show.the.link.in.footer"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${menus}" varStatus="menu" var="currentMenu">
                                    <tr class="ui-state-disabled">
                                        <td><c:out value="${menu.current.name}"/></td>
                                        <td class="center">
                                            <c:set value="${menu.current.active}" var="active"/>
                                            <c:if test="${fn:startsWith(active, 'Y')}">
                                                <span class="label label-success"><fmt:message key="menu.active"/></span>
                                            </c:if>
                                            <c:if test="${!fn:startsWith(active, 'Y')}">
                                                <span class="label label-inverse"><fmt:message key="menu.inactive"/></span>
                                            </c:if>

                                        </td>
                                        <c:set var="pagePreviewUrl" value="/content/${menu.current.uri}"/>
                                        <c:set var="designUrl" value="/admin/design/index.html?menuId=${menu.current.id}"/>
                                        <c:if test="${menu.current.homePage == 'Y'}">
                                            <c:set var="pagePreviewUrl" value="/"/>
                                            <c:set var="designUrl" value="/admin/design/index.html?menuId=${menu.current.id}"/>
                                        </c:if>
                                        <c:if test="${menu.current.menuTemplate == 'Y'}">
                                            <c:set var="pagePreviewUrl" value="/${menu.current.uri}"/>
                                            <c:set var="designUrl" value="${menu.current.designUrl}?menuId=${menu.current.id}"/>
                                        </c:if>
                                        <c:if test="${menu.current.menuTemplate == 'E'}">
                                            <c:set var="pagePreviewUrl" value="${menu.current.uri}"/>
                                            <c:set var="designUrl" value="#"/>
                                        </c:if>
                                        <td class="center">
                                            <input id="usefullink_${menu.current.id}" class="usefullink-toggle-checkbox" type="checkbox" name="usefullink" value="${menu.current.id}" ${menu.current.usefulLink == 'Y'?'checked':''} data-src="/admin/sites/${menu.current.id}/activateusefullink.html?csrf=<sec:authentication property="details.csrf"/>" lang='<fmt:message key="common.doyouwantaddusefullinktofooter"/>' data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>">
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty menus}">
                            <fmt:message key="menus.is.empty"/>
                        </c:if>

                    </div>
                </div>
            </div>
            <!--/span-->
        </div>
    </div>
<script type="text/javascript">
    //Show confirm and process check/uncheck checkbox
    $(".usefullink-toggle-checkbox").on("click", function() {
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
