<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="template.admin.pages"/></title>
    <meta name="decorator" content="admin_new"/>
</head>

<body>

<script>
//    function updateReOrder (event, ui) {
//        var item = $(ui.item).attr('id');
//        var list = $('#datatable tbody').sortable('toArray').toString();
//        window.location.href = '/admin/page/reorder.html?currentItem='+item+'&orderList='+list;
//    }

    $(document).ready(function () {
        $(".edit-page, .new-page").click(function () {
            $("#pageform").load($(this).attr("hreflang"));
        });

    });
</script>

<c:if test="${not empty site}">
    <%--<spring:eval expression="serviceLocator.pageDao.getAllPagesNotAssociateWithMenu(site)" var="pages"/>--%>
</c:if>

<!-- Bread Crumb Navigation -->
<div class="span10">
    <div>
        <ul class="breadcrumb">
            <li>
                <a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span>
            </li>
            <li class="active"><fmt:message key="template.admin.pages"/></li>
        </ul>
    </div>

    <div class="row-fluid">
        <div class="span2 action-btn round-all">
            <a class="new-page" hreflang="/admin/page/form.html" href="#pageform">
                <div><i class="icon-plus-sign"></i></div>
                <div><strong><fmt:message key="page.add.new.page"/></strong></div>
            </a>
        </div>
        <%--<div class="span2 action-btn round-all">--%>
            <%--<a href="/admin/page/pagetemplates.html" data-toggle="modal" data-target="#myModal">--%>
                <%--<div><i class="icon-th-list"></i></div>--%>
                <%--<div><strong>Add Page from Library</strong></div>--%>
            <%--</a>--%>
        <%--</div>--%>
    </div>

    <h:frontendmessage _messages="${messages}"/>

    <%--Table--%>
    <div class="row-fluid">
        <div class="span12">
            <!-- Portlet: Member List -->
            <div class="box" id="box-0">
                <h4 class="box-header round-top"><fmt:message key="page.page.list"/>
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>

                <div class="box-container-toggle">
                    <div class="box-content">
                        <c:if test="${!empty pages}">

                            <table cellpadding="0" cellspacing="0" border="0"
                                   class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                <thead>
                                <tr>
                                    <th><fmt:message key="page.page.name"/></th>
                                    <th><fmt:message key="page.status"/></th>
                                    <th><fmt:message key="page.actions"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${pages}" varStatus="page" var="currentPage">
                                    <tr id="${page.current.id}-s${page.current.sequence}">
                                        <td><c:out value="${page.current.title}"/></td>
                                        <td class="center">
                                            <c:set value="${page.current.active}" var="active"/>
                                            <c:if test="${fn:startsWith(active, 'Y')}">
                                                <span class="label label-success"><fmt:message key="page.active.status"/></span>
                                            </c:if>
                                            <c:if test="${!fn:startsWith(active, 'Y')}">
                                                <span class="label label-inverse"><fmt:message key="page.inactive.status"/></span>
                                            </c:if>

                                        </td>
                                        <td class="center">
                                            <c:set var="pagePreviewUrl" value="/content/${page.current.uri}?type=preview"/>
                                            <c:set var="designUrl" value="/admin/design/index.html?menuId=${page.current.id}"/>
                                            <a class="btn btn-success" target="_blank" href="${pagePreviewUrl}">
                                                <i class="icon-eye-open icon-white"></i>
                                                <fmt:message key="menu.preview.page"/>
                                            </a>
                                            <a class="btn btn-primary" target="_blank" href="${designUrl}">
                                                <i class="icon-zoom-in icon-white"></i>
                                                <fmt:message key="menu.design.page"/>
                                            </a>
                                            <a class="btn btn-info edit-page"
                                               hreflang="/admin/page/form.html?id=${page.current.id}" href="#pageform">
                                                <i class="icon-edit icon-white"></i>
                                                <fmt:message key="page.edit"/>
                                            </a>
                                            <a class="btn btn-danger show-confirm" readonly="readonly"
                                               lang='<fmt:message key="page.do.you.want.delete.page"/>'
                                               href="/admin/page/delete.html?id=${page.current.id}&csrf=<sec:authentication property="details.csrf"/>">
                                                <i class="icon-trash icon-white"></i>
                                                <fmt:message key="page.delete"/>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty pages}">
                            <fmt:message key="pages.is.empty"/>
                        </c:if>

                    </div>
                </div>
            </div>
            <!--/span-->
        </div>
    </div>

    <%--Form--%>
    <div class="row-fluid" id="pageform">
    </div>
    <h:reloadform id="pageform" url="/admin/page/form.html"/>

    <!--/span-->
</div>

</body>
</html>