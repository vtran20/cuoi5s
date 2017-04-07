<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="paymentMethodId" value="${param.id}"/>
<c:if test="${! empty paymentMethodId}">
    <spring:eval expression="serviceLocator.paymentProviderSiteDao.findById(T(java.lang.Long).valueOf(paymentMethodId))" var="paymentMethod"/>
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
                var url = "/admin/sites/update_payment_method.html"; // the script where you handle the form input.
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $("#modal-form").modal('hide');
                        //Reload the current tab
                        var $link = $('li.active a[data-toggle="tab"]');
                        $link.parent().removeClass('active');
                        var tabLink = $link.attr('href');
                        $('#form-tab a[href="' + tabLink + '"]').tab('show');
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
        <form name="form" id="form" action="#" method="post">
            <input type="hidden" name="id" value="${paymentMethodId}"/>
            <h:csrf/>
            <div class="form-group">
                <label class="control-label" for="name"><fmt:message key="payment.provider.name"/></label>

                <div class="controls">
                    <input name="name" class="required input-xxlarge" maxlength="50" id="name" type="text" value="${paymentMethod.name}" autofocus/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="description"><fmt:message key="payment.provider.description"/></label>

                <div class="controls">
                    <textarea name="description" class="input-xxlarge" id="description">${paymentMethod.description}</textarea>
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
