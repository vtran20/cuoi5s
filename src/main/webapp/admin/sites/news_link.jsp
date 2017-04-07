<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:if test="${not empty site}">
    <spring:eval expression="serviceLocator.newsDao.findByOrder(null, null, 'sequence', site.id)" var="newses"/>
</c:if>
<div class="row">
    <div class="col-xs-12">
    <div id="modal_message_alert"></div>
    <!-- Portlet: Member List -->
            <div class="box" id="box-0">
                <h4 class="box-header round-top"><fmt:message key="news.news.list"/>
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>

                <div class="box-container-toggle">
                    <div class="box-content">
                        <c:if test="${!empty newses}">

                            <table cellpadding="0" cellspacing="0" border="0"
                                   class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                <thead>
                                <tr>
                                    <th><fmt:message key="news.news.title"/></th>
                                    <th><fmt:message key="news.status"/></th>
                                    <th><fmt:message key="general.click.checkbox.if.you.want.show.the.link.in.footer"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${newses}" varStatus="news">
                                    <tr class="ui-state-disabled">
                                        <td><c:out value="${news.current.title}"/></td>
                                        <td class="center">
                                            <c:set value="${news.current.active}" var="active"/>
                                            <c:if test="${fn:startsWith(active, 'Y')}">
                                                <span class="label label-success"><fmt:message key="news.active"/></span>
                                            </c:if>
                                            <c:if test="${!fn:startsWith(active, 'Y')}">
                                                <span class="label label-inverse"><fmt:message key="news.inactive"/></span>
                                            </c:if>

                                        </td>
                                        <td class="center">
                                            <input id="newslink_${news.current.id}" class="newslink-toggle-checkbox" type="checkbox" name="footerLink" value="${news.current.id}" ${news.current.footerLink == 'Y'?'checked':''} data-src="/admin/sites/${news.current.id}/activatenewslink.html?csrf=<sec:authentication property="details.csrf"/>" lang='<fmt:message key="common.doyouwantaddusefullinktofooter"/>' data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>">
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty newses}">
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
    $(".newslink-toggle-checkbox").on("click", function() {
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
