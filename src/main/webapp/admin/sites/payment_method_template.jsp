<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<script>
    function updateReOrder(event, ui) {
        var item = $(ui.item).attr('id');
        var list = $('#datatable tbody').sortable('toArray').toString();
        window.location.href = '/admin/setting/reorder.html?currentItem=' + item + '&orderList=' + list;
    }
    //    BootstrapDialog.TYPE_DEFAULT,
    //            BootstrapDialog.TYPE_INFO,
    //            BootstrapDialog.TYPE_PRIMARY,
    //            BootstrapDialog.TYPE_SUCCESS,
    //            BootstrapDialog.TYPE_WARNING,
    //            BootstrapDialog.TYPE_DANGER
    $("#choose_payment_providers").click(function () {
        var object = $(this);
        var paymentIds = '';
        $('#payment_method_table input:checked').each(function () {
            if (paymentIds == '') {
                paymentIds = this.value;
            } else {
                paymentIds += ',' + this.value;
            }
        });

        $.ajax({
            type:"POST",
            url:"/admin/sites/select_payment_providers.html?paymentProviderIds=" + paymentIds,
            success:function (data) {
                $("#modal-form").modal('hide');
                //Reload the current tab
                var $link = $('li.active a[data-toggle="tab"]');
                $link.parent().removeClass('active');
                var tabLink = $link.attr('href');
                $('#form-tab a[href="' + tabLink + '"]').tab('show');
            }
        });

    });

</script>

<spring:eval expression="serviceLocator.paymentProviderDao.getPaymentProvidersNotUsed(site)" var="paymentMethods"/>
<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-xs-12">
                <c:if test="${!empty paymentMethods}">
                    <table id="payment_method_table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th><fmt:message key="setting.contactus.contact.us.name"/></th>
                            <th></th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${paymentMethods}" varStatus="paymentMethod" var="currentPaymentMethod">
                            <tr id="${currentPaymentMethod.id}-s${currentPaymentMethod.sequence}">
                                <td>
                                    <c:out value="${currentPaymentMethod.name}"/><br>
                                </td>
                                <td>
                                    <input type="checkbox" name="paymentProviderId" value="${currentPaymentMethod.id}">
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="modal-footer">
                        <button class="btn btn-sm btn-primary" type="button" id="choose_payment_providers">
                            <i class="fa fa-plus"></i> <fmt:message key="common.add"/>
                        </button>
                    </div>
                </c:if>
                <c:if test="${empty paymentMethods}">
                    <fmt:message key="common.is.empty"/>
                </c:if>
            </div>
            <!-- /.span -->
        </div>
        <!-- /.row -->
    </div>
</div>
