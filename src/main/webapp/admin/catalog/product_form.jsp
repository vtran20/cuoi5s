<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<script>
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
                var url = "/admin/catalog/product_insert.html"; // the script where you handle the form input.
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        document.location.href="#/admin/catalog/product?id="+data.id;
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            }
        });

    });
    $('[data-rel=popover]').popover({container:'body'});

</script>

<div class="row">
    <div class="col-xs-12">
        <div  id="modal_message_alert"></div>
        <form name="form" id="form" action="#" method="post">
            <h:csrf/>
            <div class="form-group">
                <label class="control-label" for="name"><fmt:message key="product.name"/></label>

                <div class="controls">
                    <input name="name" class="required input-xlarge" id="name" type="text" value="" maxlength="255"/>
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
