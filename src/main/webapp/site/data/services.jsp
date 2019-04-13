<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.data.services.information"/></title>
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
        </c:if>
    </c:if>
    <spring:eval expression="serviceLocator.getNailServiceDao().getGroupServices(store.id)" var="groups"/>

    <spring:eval expression="sessionObject.getString('UPDATE_CURRENT_SITE_ID')" var="siteId"/>
    <fmt:parseNumber var = "siteId" type = "number" value = "${siteId}" integerOnly = "true" />
    <spring:eval expression="serviceLocator.getSiteDao().findById(siteId)" var="thisSite"/>

    <!-- Begin Content -->
<div class="col-md-9">
    <div class="row margin-bottom-20">
        <div class="col-md-12">
            <button type="button" class="btn-u btn-u-blue" data-toggle="modal" data-target="#modal-form" data-title="<fmt:message key="site.data.add.edit.group"/>" data-page="/site/data/service_form.html?storeId=${store.id}"><i class="fa fa-plus"></i> <fmt:message key="site.data.add.group"/></button>
            <button type="button" class="btn-u btn-u-light-green"  data-toggle="modal" data-target="#modal-form" data-title="<fmt:message key="site.data.add.edit.service"/>" data-page="/site/data/service_form.html?storeId=${store.id}&isService=true"><i class="fa fa-plus"></i> <fmt:message key="site.data.add.service"/></button>
        </div>
    </div>
    <div class="row">
        <!--Striped Rows-->
        <c:forEach items="${groups}" var="group" varStatus="itemStatus">
            <spring:eval expression="serviceLocator.getNailServiceDao().getServices(group.id, store.id)" var="services"/>
            <div class="col-md-6">
                <div class="panel panel-sea margin-bottom-40">
                    <div class="panel-heading">
                        <div>
                            <h3 class="panel-title">${group.name}
                                <span style="float: right" class="float-right">
                                    <a class="margin-left-5" data-toggle="modal" data-target="#modal-form" data-title="<fmt:message key="site.data.add.service"/>" data-page="/site/data/service_form.html?storeId=${store.id}&groupId=${group.id}"><i class="fa fa-plus" title="Add Service"></i></a>
                                    <a class="margin-left-5" data-toggle="modal" data-target="#modal-form" data-title="<fmt:message key="site.data.add.edit.group"/>" data-page="/site/data/service_form.html?storeId=${store.id}&id=${group.id}"><i class="fa fa-pencil" title="Edit Group"></i></a>
                                    <a class="margin-left-5 show-confirm" data-title="<fmt:message key="site.data.delete.group.service"/>" data-delete="/site/data/delete_service.html?id=${group.id}&storeId=${store.id}"><i class="fa fa-trash btn-xs" title="Delete Group"></i></a>
                                </span>
                            </h3>
                        </div>
                    </div>
                    <c:if test="${fn:length(services) > 0}">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Service</th>
                                <th>Price</th>
                                <th>Minutes</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${services}" var="service" varStatus="item">
                                <tr>
                                    <td>${item.index}</td>
                                    <td>${service.name}</td>
                                    <spring:eval
                                            expression="T(com.easysoft.ecommerce.util.Money).valueOf((service.price/100),thisSite.siteParamsMap.get('CURRENCY'), thisSite.siteParamsMap.get('CURRENCY_FORMAT')).getMoneyValue()"
                                            var="servicePrice"/>
                                    <td>$${servicePrice}</td>
                                    <td>${service.minutes}</td>
                                    <td>
                                        <a class="" data-toggle="modal" data-target="#modal-form" data-title="<fmt:message key="site.data.add.edit.group"/>" data-page="/site/data/service_form.html?storeId=${store.id}&groupId=${group.id}&id=${service.id}"><i class="icon-sm fa fa-pencil" title="Edit Service"></i></a>
                                        <a class="show-confirm" data-title="<fmt:message key="site.data.delete.group.service"/>" data-delete="/site/data/delete_service.html?id=${service.id}&storeId=${store.id}"><i class="icon-sm fa fa-trash" title="Delete Service"></i></a>
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
        </c:forEach>
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
