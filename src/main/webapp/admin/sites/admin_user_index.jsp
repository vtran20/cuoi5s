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
        title:'<fmt:message key="common.confirm.title"/>',
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
    <spring:eval expression="serviceLocator.userDao.getAdminUsers(site.id)" var="adminUsers"/>
</c:if>

<div class="page-header">
    <a class="btn btn-xs btn-info" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="site.add.new.user"/>" data-page="/admin/sites/admin_user_form.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="site.add.new.user"/>
    </a>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-xs-12">
                <c:if test="${!empty adminUsers}">
                    <table id="simple-table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th><fmt:message key="site.admin.user.username"/></th>
                            <th><fmt:message key="site.admin.user.firstname"/></th>
                            <th><fmt:message key="site.admin.user.lastname"/></th>
                            <th></th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${adminUsers}" var="currentadminUser">
                            <tr>
                                <td>
                                    <c:out value="${currentadminUser.username}"/><br>
                                </td>
                                <td>
                                    <c:out value="${currentadminUser.firstName}"/><br>
                                </td>
                                <td>
                                    <c:out value="${currentadminUser.lastName}"/><br>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <%--<a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" role="button" data-toggle="modal" data-title="<fmt:message key="admin.user.change.password"/>" data-page="/admin/sites/admin_user_form.html?id=${currentadminUser.id}" data-target="#modal-form">--%>
                                            <%--<i class="ace-icon fa fa-pencil bigger-120"></i>--%>
                                        <%--</a>--%>
                                        <c:if test="${fn:length(adminUsers) > 1}">
                                            <a class="btn btn-xs btn-danger show-confirm" title="<fmt:message key="common.delete"/>" lang='<fmt:message key="setting.do.you.want.delete.admin.account"/>' hreflang="/admin/sites/deletesiteadminuser.html?id=${currentadminUser.id}&csrf=<sec:authentication property="details.csrf"/>">
                                                <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                            </a>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty adminUsers}">
                    <fmt:message key="common.is.empty"/>
                </c:if>
            </div><!-- /.span -->
        </div><!-- /.row -->
    </div>
</div>

<h:form_modal/>
