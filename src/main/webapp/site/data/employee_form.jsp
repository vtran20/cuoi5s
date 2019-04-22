<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="storeId" value="${param.storeId}"/>
<c:if test="${!empty param.id}">
    <c:set var="id" value="${param.id}"/>
    <spring:eval expression="serviceLocator.getNailEmployeeDao().findById(T(java.lang.Long).valueOf(id))" var="employee"/>
</c:if>
<style>
    /*.form-group div.row {*/
        /*width: 84px;*/
        /*height: 44px;*/
        /*float: left;*/
        /*border: 2px solid #fff;*/
        /*margin: 2px 2px 2px 2px;*/
        /*text-align: center;*/
    /*}*/
    .form-group div.row img {
        border: 2px solid #fff;
    }
    .form-group div.row img:hover {
        border: 2px solid red;
    }
    .form-group div.row img.select-active {
        border: solid 2px red;
    }
</style>
<div class="row">
    <div id="modal_message_alert"></div>
    <form class="margin-bottom-0" role="form" id="formGroup" action="#">
        <input type="hidden" name="storeId" value="${storeId}">
        <input type="hidden" name="id" value="${id}">
        <div class="col-md-12">
            <div class="form-group">
                <label for="firstName"><fmt:message key="site.data.employee.firstName"/></label>
                <input type="text" class="form-control required" name="firstName" id="firstName" value="${employee.firstName}" placeholder="<fmt:message key="site.data.employee.firstName"/>"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="form-group">
                <label for="lastName"><fmt:message key="site.data.employee.lastName"/></label>
                <input type="text" class="form-control required" name="lastName" id="lastName" value="${employee.lastName}" placeholder="<fmt:message key="site.data.employee.lastName"/>"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="form-group">
                <label for="phone" class="control-label"><fmt:message key="site.data.address.phone"/></label>
                <c:if test="${!empty employee.phone}">
                    <c:set var="phoneFormat" value="(${fn:substring(employee.phone, 0, 3)}) ${fn:substring(employee.phone, 3, 6)}-${fn:substring(employee.phone, 6, fn:length(employee.phone))}"/>
                </c:if>
                <input type="text" class="form-control" id="phone" name="phone" maxlength="20" placeholder="(xxx) xxx-xxxx" value="${phoneFormat}">
            </div>
        </div>
        <div class="col-md-12">
            <div class="form-group">
                <label for="email" class="control-label"><fmt:message key="site.data.employee.email"/></label>
                <input type="text" class="form-control" id="email" name="email" maxlength="20" placeholder="<fmt:message key="site.data.employee.email"/>" value="${employee.email}">
            </div>
        </div>
        <div class="col-md-12">
            <div class="form-group">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" id="active" name="active" ${employee.active == 'N'? '' : 'checked'}> <fmt:message key="site.data.employee.status"/>
                    </label>
                </div>
            </div>
            <%--<div class="form-group">--%>
                <%--<label for="active" class="control-label"><fmt:message key="site.data.employee.status"/></label>--%>
                <%--<input type="text" class="form-control" id="active" name="active" maxlength="20" placeholder="<fmt:message key="site.data.employee.status"/>" value="${employee.active}">--%>
            <%--</div>--%>
        </div>
        <div class="col-md-12">
            <button type="button" class="btn-u btn-u-default" data-dismiss="modal">Close</button>
            <button type="button" id="submit-group" class="btn-u btn-u-primary"><fmt:message key="common.save.changes"/></button>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        $("#formGroup").validate({
            messages:{
                firstName:"<fmt:message key="booking.enter.firstName"/>",
                lastName:"<fmt:message key="booking.enter.lastName"/>"
            },
            ignore:"", //To allow validation of hidden elements, override the ignore and set it to empty string:
            rules: {
//                firstName: "required",
//                lastName: "required"
            },
            //ignore:"", //To allow validation of hidden elements, override the ignore and set it to empty string:
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });
        $('#phone').usPhoneFormat({
            format: '(xxx) xxx-xxxx'
        });
        $('body').on('click', '#submit-group', function() {
            var url = "/site/data/update_employee.html"; // the script where you handle the form input.
            var form = $( "#formGroup" );
            if (form.valid()) {

                var formData = $('#formGroup').serialize();
                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData, // serializes the form's elements.
                    success: function(data)
                    {
                        $('#modal_message_alert').html(data);
                        $("#modal_message_alert").fadeTo(5000, 500).slideUp(500, function(){
                            $("#modal_message_alert").alert('close');
                        });
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            }
        })
    })
</script>