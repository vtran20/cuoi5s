<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script>
$(".show-confirm").click(function () {
    var object = $(this);
    BootstrapDialog.show({
        type:BootstrapDialog.TYPE_DANGER,
        closeByBackdrop: false,
        closeByKeyboard: false,
        title:'Xác Nhận',
        message:object.attr("lang"),
        buttons:[
            {
                label:'Yes',
                action:function (dialog) {
                    $.ajax({
                        type: "GET",
                        url: object.attr("hreflang"),
                        data: $("#form").serialize(), // serializes the form's elements.
                        success: function(data)
                        {
                            window.location.reload(true);
                        }
                    });
                }
            },
            {
                label:'No',
                action:function (dialog) {
                    dialog.close();
                }
            }
        ]
    });
});

</script>

<c:if test="${not empty site}">
    <spring:eval expression="serviceLocator.siteSupportDao.findAll(site.id)" var="siteSupports"/>
</c:if>

<div class="page-header">
    <a class="btn btn-xs btn-info" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="setting.support.add.new.customer.service"/>" data-page="/admin/sites/support_form.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="common.add.new"/>
    </a>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div class="row">
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
                                <td>
                                    <div class="hidden-sm hidden-xs btn-group">
                                        <a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" role="button" data-toggle="modal" data-title="<fmt:message key="setting.support.update.customer.serice"/>" data-page="/admin/sites/support_form.html?id=${support.current.id}" data-target="#modal-form">
                                            <i class="ace-icon fa fa-pencil bigger-120"></i>
                                        </a>

                                        <a class="btn btn-xs btn-danger show-confirm" title="<fmt:message key="common.delete"/>" lang='<fmt:message key="setting.do.you.want.delete.support"/>' hreflang="/admin/sites/deletesitesupport.html?id=${support.current.id}&csrf=<sec:authentication property="details.csrf"/>">
                                            <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                        </a>
                                    </div>
                                    <div class="hidden-md hidden-lg">
                                        <div class="inline pos-rel">
                                            <button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
                                                <i class="ace-icon fa fa-cog icon-only bigger-110"></i>
                                            </button>
                                            <ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
                                                <li>
                                                    <a class="tooltip-success" data-rel="tooltip" title="<fmt:message key="common.edit"/>"  role="button" data-toggle="modal" data-title="<fmt:message key="setting.update.site.contact"/>" data-page="/admin/sites/support_form.html?id=${support.current.id}" data-target="#modal-form">
                                                                                           <span class="green">
                                                                                               <i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
                                                                                           </span>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a class="tooltip-error show-confirm" data-rel="tooltip" title="<fmt:message key="common.delete"/>"  lang='<fmt:message key="setting.do.you.want.delete.support"/>' hreflang="/admin/sites/deletesitesupport.html?id=${support.current.id}&csrf=<sec:authentication property="details.csrf"/>">
                                                                                           <span class="red">
                                                                                               <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                                                                           </span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
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

<h:form_modal/>
