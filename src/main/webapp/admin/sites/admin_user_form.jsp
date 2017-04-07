<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="userId" value="${param.id}"/>
<c:if test="${! empty userId}">
    <spring:eval expression="serviceLocator.userDao.findById(T(java.lang.Long).valueOf(userId))" var="adminUser"/>
</c:if>

<script>
    $('[data-rel=popover]').popover({container:'body'});

    $(function () {

        $("#form").validate({
            rules:{
                email:{
                    required:true,
                    email:true
                }
            },
            messages:{
                "email":{
                    required:"<fmt:message key="site.register.emailisrequired"/>",
                    email:"<fmt:message key="site.register.emailisinvalid"/>"
                }
            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });

        <%--$.validator.addMethod("comparePassword", function(value, element) {--%>
            <%--return $('#newPassword').val() == $('#reNewPassword').val()--%>
        <%--}, "<fmt:message key="site.register.password.and.repassword.different"/>");--%>

        $("#form1").validate({
            rules:{
                email:{
                    required:true,
                    email:true
                }
//                oldPassword:{
//                    required:true
//                    minlength:4
//                }
//                newPassword:{
//                    required:true,
//                    minlength:4
//                },
//                reNewPassword:{
//                    required:true,
//                    minlength:4
////                    comparePassword: true
//                }
            },
            messages:{
                "email":{
                    required:"<fmt:message key="site.register.emailisrequired"/>",
                    email:"<fmt:message key="site.register.emailisinvalid"/>"
                }
                <%--oldPassword:{--%>
                    <%--required:"<fmt:message key="site.register.passwordrequired"/>",--%>
                    <%--&lt;%&ndash;minlength:jQuery.format("<fmt:message key="site.register.passwordminimumrequired"/>")&ndash;%&gt;--%>
                <%--}--%>
                <%--newPassword:{--%>
                    <%--required:"<fmt:message key="site.register.passwordrequired"/>",--%>
                    <%--minlength:jQuery.format("<fmt:message key="site.register.passwordminimumrequired"/>")--%>
                <%--},--%>
                <%--reNewPassword:{--%>
                    <%--required:"<fmt:message key="site.register.passwordrequired"/>",--%>
                    <%--minlength:jQuery.format("<fmt:message key="site.register.passwordminimumrequired"/>")--%>
                <%--}--%>
            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });

        $("#form").submit(function() {
            var form = $( "#form" );
            if (form.valid()) {
                url = "/admin/sites/saveadminuser.html"; // the script where you handle the form input.
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#message_alert').html(data);
                        $("#message_alert").fadeTo(20000, 500).slideUp(500, function(){
                            $("#message_alert").alert('close');
                        });
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            } else {
                return false;
            }
        });
//        $("button#changepassword").on("click", function(){
        $("#form1").submit(function() {
            var form1 = $( "#form1" );
            alert(1);
            alert (form1.valid());
            if (form1.valid()) {
                url = "/admin/sites/changepassword.html"; // the script where you handle the form input.
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form1").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#message_alert').html(data);
                        $("#message_alert").fadeTo(20000, 500).slideUp(500, function(){
                            $("#message_alert").alert('close');
                        });
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            } else {
                return false;
            }
        });

    });
</script>
<c:choose>
    <c:when test="${!empty adminUser}">
        <div class="row">
            <div class="col-xs-12">
                <div id="message_alert"></div>
                <p>Xin vui lòng nhập email của tài khoản. Trong trường hợp tài khoản chưa tồn tại, chúng tôi sẽ tạo tài khoản mới</p>
                <form name="form1" id="form1" action="#" method="post">
                        <%--<input type="hidden" name="id" value="${userId}"/>--%>
                    <h:csrf/>
                    <div class="form-group">
                        <label class="control-label" for="email"><fmt:message key="site.admin.user.email"/>*</label>
                        <div class="controls">
                            <input name="email" class="input-xlarge" id="email" type="text" value="${adminUser.username}" maxlength="50" autofocus placeholder="<fmt:message key="site.admin.user.email"/>" readonly=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="oldPassword" class="control-label"><fmt:message key="site.register.old.password"/></label>
                        <div class="controls">
                            <input type="password" class="input-xlarge" id="oldPassword" name="oldPassword" maxlength="40" placeholder="<fmt:message key="site.register.old.password"/>" value="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="newPassword" class="control-label"><fmt:message key="site.register.new.password"/></label>
                        <div class="controls">
                            <input type="password" class="input-xlarge" id="newPassword" name="newPassword" maxlength="40" placeholder="<fmt:message key="site.register.new.password"/>" value="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="reNewPassword" class="control-label"><fmt:message key="site.register.new.repassword"/></label>
                        <div class="controls">
                            <input type="password" class="input-xlarge" id="reNewPassword" name="reNewPassword" maxlength="40" placeholder="<fmt:message key="site.register.new.repassword"/>" value="">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-sm btn-primary" type="button" id="changepassword">
                            <i class="ace-icon fa fa-check"></i>
                            <fmt:message key="common.save.changes"/>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="row">
            <div class="col-xs-12">
                <div id="message_alert"></div>
                <p>Xin vui lòng nhập email của tài khoản. Trong trường hợp tài khoản chưa tồn tại, chúng tôi sẽ tạo tài khoản mới</p>
                <form name="form" id="form" action="#" method="post">
                        <%--<input type="hidden" name="id" value="${userId}"/>--%>
                    <h:csrf/>
                    <div class="form-group">
                        <label class="control-label" for="email"><fmt:message key="site.admin.user.email"/>*</label>
                        <div class="controls">
                            <input name="email" class="input-xlarge" id="email" type="text" value="" maxlength="50" autofocus placeholder="<fmt:message key="site.admin.user.email"/>"/>
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
    </c:otherwise>
</c:choose>
