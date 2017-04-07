<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="siteContactId" value="${param.id}"/>
<c:if test="${! empty siteContactId}">
    <spring:eval expression="serviceLocator.siteContactDao.findById(T(java.lang.Long).valueOf(siteContactId))" var="siteContact"/>
</c:if>

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
            var url = "/admin/sites/savesitecontact.html"; // the script where you handle the form input.
            var form = $( "#form" );
            if (form.valid()) {
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#message_alert').html(data);
                        $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#message_alert").alert('close');
                        });
                    }
                });
//            $('#modal-form').modal('hide'); //Close after submit
                return false; // avoid to execute the actual submit of the form.
            }
        });

    });
</script>

<div class="row">
    <div class="col-xs-12">
        <div id="message_alert"></div>
        <form name="form" id="form" action="#" method="post">
            <input type="hidden" name="id" value="${siteContactId}"/>
            <h:csrf/>
                <div class="form-group">
                    <label class="control-label" for="name"><fmt:message key="setting.company.info.personal.info"/>*</label>

                    <div class="">
                        <input name="name" class="required input-xlarge" id="name" type="text" value="${siteContact.name}" maxlength="50" autofocus/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label" for="address_1"><fmt:message key="setting.contactus.address"/>*</label>

                    <div class="controls">
                        <input name="address_1" class="required input-xlarge" id="address_1" type="text" value="${siteContact.address_1}" maxlength="50"/>
                    </div>
                </div>
                <%--<div class="form-group">--%>
                <%--<label class="control-label" for="district"><fmt:message key="setting.contactus.district"/></label>--%>

                <%--<div class="controls">--%>
                <%--<input name="district" class="input-xlarge" id="district" type="text" value="${siteContact.district}"/>--%>
                <%--</div>--%>
                <%--</div>--%>
                <div class="form-group">
                    <label class="control-label" for="city"><fmt:message key="setting.contactus.city"/></label>
                    <div class="controls">
                        <input name="city" class="input-xlarge" id="city" type="text" value="${siteContact.city}" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label" for="state"><fmt:message key="setting.contactus.state"/></label>
                    <div class="controls">
                        <%--<h:stringparamselector name="state" stringParam="USA_STATE" defaultValue="${siteContact.state}" includeTitle="Select State"/>--%>
                            <input name="state" class="input-xlarge" id="state" type="text" value="${siteContact.state}" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label" for="zipCode"><fmt:message key="setting.contactus.zipcode"/></label>
                    <div class="controls">
                        <input name="zipCode" class="input-xlarge" id="zipCode" type="text" value="${siteContact.zipCode}" maxlength="10"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label" for="phone"><fmt:message key="setting.contactus.phonenumber"/></label>

                    <div class="controls">
                        <input name="phone" class="input-xlarge" id="phone" type="text" value="${siteContact.phone}" maxlength="25"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label" for="email"><fmt:message key="setting.contactus.email"/></label>

                    <div class="">
                        <input name="email" class="input-xlarge" id="email" type="text" value="${siteContact.email}" maxlength="100"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label" for="fax"><fmt:message key="setting.contactus.fax"/></label>

                    <div class="controls">
                        <input name="fax" class="input-xlarge" id="fax" type="text" value="${siteContact.fax}" maxlength="25"/>
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