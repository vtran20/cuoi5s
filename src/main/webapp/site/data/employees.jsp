<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.data.employees.information"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<h:authenticate request="${pageContext.request}" response="${pageContext.response}"/>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.data.general.info"/></h1>
        <ul class="pull-right breadcrumb">
            <li><a href="/">Home</a></li>
            <li><a href="/site/mysites.html">My Sites</a></li>
            <li class="active"><fmt:message key="site.data.general.info"/></li>
        </ul>
    </div>
</div>
<div class="container content">
<div class="row">
<!-- Begin Sidebar Menu -->
    <jsp:include page="leftnav.jsp"/>
<!-- End Sidebar Menu -->
    <c:if test="${empty store}">
        <spring:eval expression="serviceLocator.getNailStoreDao().findAll(sessionObject.UPDATE_CURRENT_SITE_ID)" var="stores"/>
        <c:if test="${fn:length(stores) >= 1}">
            <c:set var="store" value="${stores[0]}"/>
            <spring:eval expression="serviceLocator.getNailEmployeeDao().findAllByStore(store.id)" var="employees"/>
        </c:if>
    </c:if>
    <!-- Begin Content -->
<div class="col-md-9">
    <div class="row margin-bottom-20">
        <div class="col-md-12">
            <button type="button" class="btn-u btn-u-light-green"  data-toggle="modal" data-target="#modal-form" data-title="<fmt:message key="site.data.add.employee"/>" data-page="/site/data/employee_form.html?storeId=${store.id}&isService=true"><i class="fa fa-plus"></i> <fmt:message key="site.data.add.employee"/></button>
        </div>
    </div>
    <div class="row">
        <!--Striped Rows-->
            <div class="col-md-12">
                <div class="panel panel-sea margin-bottom-40">
                    <div class="panel-heading">
                        <div>
                            <h3 class="panel-title">
                                <fmt:message key="site.data.employees.list"/>
                            </h3>
                        </div>
                    </div>
                    <c:if test="${fn:length(employees) > 0}">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Phone</th>
                                <th>Email</th>
                                <th>Status</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${employees}" var="employee" varStatus="itemStatus">
                            <tr>
                                <td>${itemStatus.index + 1}</td>
                                <td>${employee.firstName} ${employee.lastName}</td>
                                <td>(${fn:substring(employee.phone, 0, 3)}) ${fn:substring(employee.phone, 3, 6)}-${fn:substring(employee.phone, 6, fn:length(employee.phone))}</td>
                                <td>${employee.email}</td>
                                <td>${employee.active == 'Y'? 'Active' : '<span style="color:red">Deactive</span>'}</td>
                                <td>
                                    <a class="" data-toggle="modal" data-target="#modal-form" data-title="<fmt:message key="site.data.edit.employee"/>" data-page="/site/data/employee_form.html?storeId=${store.id}&id=${employee.id}"><i class="icon-sm fa fa-pencil" title="<fmt:message key="site.data.edit.employee"/>"></i></a>
                                    <a class="show-confirm" data-title="<fmt:message key="site.data.delete.employee"/>" data-delete="/site/data/delete_employee.html?id=${employee.id}&storeId=${store.id}"><i class="icon-sm fa fa-trash" title="<fmt:message key="site.data.delete.employee"/>"></i></a>
                                </td>
                            </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
            <%--<c:if test="${itemStatus.index > 0 && (itemStatus.index + 1) % 2 == 0}">--%>
                <%--<div class="clearfix visible-xs-block" style="clear: both"></div>--%>
            <%--</c:if>--%>
        <!--End Striped Rows-->

    </div>
</div>
<!-- End Content -->
</div>
</div>
<h:front_modal modalSize="modal-md"/>
<script type="text/javascript">
    //Sidebar Navigation Toggle
    jQuery('.list-toggle').on('click', function() {
        jQuery(this).toggleClass('active');
    });
    $(function () {
        $("body").on("click", ".show-confirm", function () {
            var object = $(this);
            BootstrapDialog.show({
                type:BootstrapDialog.TYPE_DANGER,
                closeByBackdrop: false,
                closeByKeyboard: false,
                title:'<fmt:message key="common.confirm.title"/>',
                message:object.attr("data-title"),
                buttons:[
                    {
                        label:'Yes',
                        action:function (dialog) {
                            $.ajax({
                                type: "GET",
                                url: object.attr("data-delete"),
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
    })

</script>
</body>
</html>
