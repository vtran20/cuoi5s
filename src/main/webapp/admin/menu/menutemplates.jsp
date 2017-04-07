<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="menu.template"/></title>
</head>

<body>
<spring:eval expression="serviceLocator.menuTemplateDao.getMenuTemplatesNotUsed(site)" var="menus"/>

<%--Table--%>
<div class="row-fluid">
    <form name="form" id="form" action="#" method="post">
    <div class="span12">
        <!-- Portlet: Member List -->
        <div class="box" id="box-0">
            <div  id="modal_message_alert"></div>
            <h4 class="box-header round-top"><fmt:message key="menu.template.list"/>
                <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                        class="icon-cog"></i></a>
            </h4>

            <div class="box-container-toggle">
                <div class="box-content">
                    <c:if test="${!empty menus}">

                        <table cellpadding="0" cellspacing="0" border="0"
                               class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                            <thead>
                            <tr>
                                <th><fmt:message key="menu.template.select"/></th>
                                <th><fmt:message key="menu.name"/></th>
                                <th><fmt:message key="menu.template.uri"/></th>
                                <th><fmt:message key="menu.template.description"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <h:csrf/>
                            <c:forEach items="${menus}" varStatus="menu" var="currentMenu">
                                <tr>
                                    <td><input type="checkbox" name="menutemplateId" value="${currentMenu.id}"></td>
                                    <td><c:out value="${currentMenu.name}"/></td>
                                    <td class="center">
                                        <c:url var="pageURI" value="/${currentMenu.uri}"/>
                                        ${pageURI}
                                    </td>
                                    <td class="center">
                                        ${currentMenu.description}
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${empty menus}">
                        <fmt:message key="menu.template.all.menu.templates.were.used"/>
                    </c:if>
                </div>
            </div>
        </div>
        <!--/span-->
    </div>
    <div class="modal-footer">
        <button class="btn btn-sm btn-primary" type="submit">
            <i class="ace-icon fa fa-check"></i>
            <fmt:message key="common.save.changes"/>
        </button>
    </div>
    </form>
</div>
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
                var url = "/admin/menu/addmenutemplate.html"; // the script where you handle the form input.
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#modal_message_alert').html(data);
                        $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#modal_message_alert").alert('close');
                        });
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            }
        });

    });

</script>
</body>
</html>
