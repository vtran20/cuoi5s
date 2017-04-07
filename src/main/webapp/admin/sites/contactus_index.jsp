<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<title><fmt:message key="setting.contact.us"/></title>
<script>
//    BootstrapDialog.TYPE_DEFAULT,
//            BootstrapDialog.TYPE_INFO,
//            BootstrapDialog.TYPE_PRIMARY,
//            BootstrapDialog.TYPE_SUCCESS,
//            BootstrapDialog.TYPE_WARNING,
//            BootstrapDialog.TYPE_DANGER
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
//                        dialog.close();
                        $.ajax({
                            type: "GET",
                            url: object.attr("hreflang"),
                            data: $("#form").serialize(), // serializes the form's elements.
                            success: function(data)
                            {
//                                dialog.setMessage(data);
//                                dialog.close();
//                                alert(data);
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
    <spring:eval expression="serviceLocator.siteContactDao.findAll(site.id)" var="siteContacts"/>
</c:if>
<div class="page-header">
    <%--<h4>Cập Nhật Thông Tin Liên Hệ</h4>--%>
        <a class="btn btn-xs btn-info" title="<fmt:message key="common.add.new"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="setting.add.new.site.contact"/>" data-page="/admin/sites/contactus_form.html" data-target="#modal-form">
            <i class="fa fa-plus"></i> <fmt:message key="common.add.new"/>
        </a>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <c:if test="${!empty siteContacts}">
            <table id="simple-table" class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th><fmt:message key="setting.contactus.contact.us.name"/></th>
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
                        <td>
                            <div class="hidden-sm hidden-xs btn-group">
                                <a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" role="button" data-toggle="modal" data-title="<fmt:message key="setting.update.site.contact"/>" data-page="/admin/sites/contactus_form.html?id=${contact.current.id}" data-target="#modal-form"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
                                <a class="btn btn-xs btn-danger show-confirm" title="<fmt:message key="common.delete"/>" lang='<fmt:message key="setting.contactus.do.you.want.delete.contact"/>' hreflang="/admin/sites/deletesitecontact.html?id=${contact.current.id}&csrf=<sec:authentication property="details.csrf"/>"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>
                            </div>
                            <div class="hidden-md hidden-lg">
                                <div class="inline pos-rel">
                                    <button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
                                        <i class="ace-icon fa fa-cog icon-only bigger-110"></i>
                                    </button>
                                    <ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
                                        <li>
                                            <a class="tooltip-success" data-rel="tooltip" title="<fmt:message key="common.edit"/>"  role="button" data-toggle="modal" data-title="<fmt:message key="setting.update.site.contact"/>" data-page="/admin/sites/contactus_form.html?id=${contact.current.id}" data-target="#modal-form">
                                                                                           <span class="green">
                                                                                               <i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
                                                                                           </span>
                                            </a>
                                        </li>
                                        <li>
                                            <a class="tooltip-error show-confirm" data-rel="tooltip" title="<fmt:message key="common.delete"/>"  lang='<fmt:message key="setting.contactus.do.you.want.delete.contact"/>' hreflang="/admin/sites/deletesitecontact.html?id=${contact.current.id}&csrf=<sec:authentication property="details.csrf"/>">
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
        <c:if test="${empty siteContacts}">
            <fmt:message key="contact.is.empty"/>
        </c:if>
    </div><!-- /.span -->
</div><!-- /.row -->

<h:form_modal/>
