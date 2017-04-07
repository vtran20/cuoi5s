<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<script>
    function updateReOrder (event, ui) {
        var item = $(ui.item).attr('id');
        var list = $('#datatable tbody').sortable('toArray').toString();
        window.location.href = '/admin/setting/reorder.html?currentItem='+item+'&orderList='+list;
    }
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
                                //Reload the current tab
                                var $link = $('li.active a[data-toggle="tab"]');
                                $link.parent().removeClass('active');
                                var tabLink = $link.attr('href');
                                $('#form-tab a[href="' + tabLink + '"]').tab('show');
                            }
                        });
                        dialog.close();
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

<spring:eval expression="serviceLocator.paymentProviderSiteDao.findAllOrder('sequence', site.id)" var="paymentMethods"/>
<div class="page-header">
    <a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="setting.add.new.site.contact"/>" data-page="/admin/sites/payment_method_template.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="common.add"/>
    </a>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
    <div class="row">
    <div class="col-xs-12">
<c:if test="${!empty paymentMethods}">
    <table id="simple-table" class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th><fmt:message key="setting.payment.method.name"/></th>
        <th></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach items="${paymentMethods}" varStatus="productVariant" var="currentPaymentMethod">
    <tr id="${productVariant.current.id}-s${productVariant.current.sequence}">
        <td>
            <c:out value="${currentPaymentMethod.name}"/><br>
        </td>
        <td>
                           <div class="hidden-sm hidden-xs btn-group">
                               <a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" role="button" data-toggle="modal" data-title="<fmt:message key="setting.update.payment.provider"/>" data-page="/admin/sites/payment_method_form.html?id=${productVariant.current.id}" data-target="#modal-form">
                                   <i class="ace-icon fa fa-pencil bigger-120"></i>
                               </a>

                               <a class="btn btn-xs btn-danger show-confirm" title="<fmt:message key="common.delete"/>" lang='<fmt:message key="setting.site.do.you.want.delete.paymnet"/>' hreflang="/admin/sites/deletepaymentprovider.html?id=${productVariant.current.id}&csrf=<sec:authentication property="details.csrf"/>">
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
                                           <a class="tooltip-success" data-rel="tooltip" title="<fmt:message key="common.edit"/>"  role="button" data-toggle="modal" data-title="<fmt:message key="setting.update.site.contact"/>" data-page="/admin/sites/contactus_form.html?id=${productVariant.current.id}" data-target="#modal-form">
                                                                                           <span class="green">
                                                                                               <i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
                                                                                           </span>
                                           </a>
                                       </li>
                                       <li>
                                           <a class="tooltip-error show-confirm" data-rel="tooltip" title="<fmt:message key="common.delete"/>"  lang='<fmt:message key="setting.contactus.do.you.want.delete.contact"/>' hreflang="/admin/sites/deletesitecontact.html?id=${productVariant.current.id}&csrf=<sec:authentication property="details.csrf"/>">
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
        <c:if test="${empty paymentMethods}">
            <fmt:message key="common.is.empty"/>
        </c:if>
    </div><!-- /.span -->
                   </div><!-- /.row -->
                   </div>
               </div>

<h:form_modal/>
