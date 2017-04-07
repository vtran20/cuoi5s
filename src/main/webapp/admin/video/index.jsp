<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="category.categories"/></title>
    <meta name="decorator" content="admin_new"/>
</head>

<body>

<script>
    function updateReOrder (event, ui) {
        var item = $(ui.item).attr('id');
        var list = $('#datatable tbody').sortable('toArray').toString();
        window.location.href = '/admin/catalog/category_reorder.html?currentItem='+item+'&orderList='+list;
    }

    $(document).ready(function () {
        $(".edit-category, .new-category").click(function () {
            $("#categoryform").load($(this).attr("hreflang"));
        });

    });
</script>

<spring:eval expression="serviceLocator.categoryDao.getRootCategories(site, false)" var="rootCategories"/>

 <!-- Bread Crumb Navigation -->
<div class="span10">
    <div>
        <ul class="breadcrumb">
            <li>
                <a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span>
            </li>
            <li class="active"><fmt:message key="category.categories"/></li>
        </ul>
    </div>

    <div class="row-fluid">
        <div class="span2 action-btn round-all">
            <a class="new-category" hreflang="/admin/catalog/category_form.html" href="#categoryform">
                <div><i class="icon-plus-sign"></i></div>
                <div><strong><fmt:message key="category.categories"/></strong></div>
            </a>
        </div>
    </div>

    <h:frontendmessage _messages="${messages}"/>

    <%--Table--%>
    <div class="row-fluid">
        <div class="span12">
            <!-- Portlet: Member List -->
            <div class="box" id="box-0">
                <h4 class="box-header round-top"><fmt:message key="category.categories"/>
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>

                <div class="box-container-toggle">
                    <div class="box-content">
                        <c:if test="${!empty rootCategories}">

                            <table cellpadding="0" cellspacing="0" border="0"
                                   class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                <thead>
                                <tr>
                                    <th><fmt:message key="category.name"/></th>
                                    <th><fmt:message key="common.status"/></th>
                                    <th><fmt:message key="common.actions"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${rootCategories}" var="currentCategory">

                                    <tr id="${currentCategory.id}-s${currentCategory.sequence}">
                                        <td><c:out value="${currentCategory.name}"/></td>
                                        <td class="center">
                                            <c:set value="${currentCategory.active}" var="active"/>
                                            <c:if test="${fn:startsWith(active, 'Y')}">
                                                <span class="label label-success"><fmt:message key="common.active.status"/></span>
                                            </c:if>
                                            <c:if test="${!fn:startsWith(active, 'Y')}">
                                                <span class="label label-inverse"><fmt:message key="common.inactive.status"/></span>
                                            </c:if>

                                        </td>
                                        <c:set var="siteUrl" value="/catalog/${currentCategory.uri}"/>
                                        <td class="center">
                                            <a class="btn btn-success" target="_blank" href="${siteUrl}">
                                                <i class="icon-zoom-in icon-white"></i>
                                                <fmt:message key="common.view"/>
                                            </a>
                                            <a class="btn btn-info edit-category"
                                               hreflang="/admin/catalog/category_form.html?id=${currentCategory.id}" href="#categoryform">
                                                <i class="icon-edit icon-white"></i>
                                                <fmt:message key="common.edit"/>
                                            </a>
                                            <a class="btn btn-danger show-confirm" readonly="readonly"
                                               lang='<fmt:message key="category.do.you.want.to.delete"/>'
                                               href="/admin/catalog/category_delete.html?id=${currentCategory.id}&csrf=<sec:authentication property="details.csrf"/>">
                                                <i class="icon-trash icon-white"></i>
                                                <fmt:message key="common.delete"/>
                                            </a>
                                        </td>
                                    </tr>
                                    <%--Get SubCategory level 2--%>
                                    <spring:eval expression="serviceLocator.categoryDao.findByOrder('parentCategory.id', currentCategory.id, 'sequence')" var="level2Categories"/>
                                    <c:if test="${! empty level2Categories}">
                                        <c:forEach items="${level2Categories}" varStatus="level2Category">

                                            <tr id="${currentCategory.id}-${level2Category.current.id}-s${level2Category.current.sequence}">
                                                <td><c:out value="---- ${level2Category.current.name}"/></td>
                                                <td class="center">
                                                    <c:set value="${level2Category.current.active}" var="active"/>
                                                    <c:if test="${fn:startsWith(active, 'Y')}">
                                                        <span class="label label-success"><fmt:message key="common.active.status"/></span>
                                                    </c:if>
                                                    <c:if test="${!fn:startsWith(active, 'Y')}">
                                                        <span class="label label-inverse"><fmt:message key="common.inactive.status"/></span>
                                                    </c:if>

                                                </td>
                                                <c:set var="siteUrl" value="/${level2Category.current.uri}"/>
                                                <td class="center">
                                                    <a class="btn btn-success" target="_blank" href="${siteUrl}">
                                                        <i class="icon-zoom-in icon-white"></i>
                                                        <fmt:message key="common.view"/>
                                                    </a>
                                                    <a class="btn btn-info edit-category"
                                                       hreflang="/admin/catalog/category_form.html?id=${level2Category.current.id}" href="#categoryform">
                                                        <i class="icon-edit icon-white"></i>
                                                        <fmt:message key="common.edit"/>
                                                    </a>
                                                    <a class="btn btn-danger show-confirm" readonly="readonly"
                                                       lang='<fmt:message key="category.do.you.want.to.delete"/>'
                                                       href="/admin/catalog/category_delete.html?id=${level2Category.current.id}&csrf=<sec:authentication property="details.csrf"/>">
                                                        <i class="icon-trash icon-white"></i>
                                                        <fmt:message key="common.delete"/>
                                                    </a>
                                                </td>
                                            </tr>

                                            <%--Get SubCategory level 3--%>
                                            <spring:eval expression="serviceLocator.categoryDao.findByOrder('parentCategory.id', level2Category.current.id, 'sequence')" var="level3Categories"/>
                                            <c:if test="${! empty level3Categories}">
                                                <c:forEach items="${level3Categories}" varStatus="level3Category">

                                                    <tr id="${currentCategory.id}-${level2Category.current.id}-${level3Category.current.id}-s${level3Category.current.sequence}">
                                                        <td><c:out value="-------- ${level3Category.current.name}"/></td>
                                                        <td class="center">
                                                            <c:set value="${level3Category.current.active}" var="active"/>
                                                            <c:if test="${fn:startsWith(active, 'Y')}">
                                                                <span class="label label-success"><fmt:message key="common.active.status"/></span>
                                                            </c:if>
                                                            <c:if test="${!fn:startsWith(active, 'Y')}">
                                                                <span class="label label-inverse"><fmt:message key="common.inactive.status"/></span>
                                                            </c:if>

                                                        </td>
                                                        <c:set var="siteUrl" value="/${level3Category.current.uri}"/>
                                                        <td class="center">
                                                            <a class="btn btn-success" target="_blank" href="${siteUrl}">
                                                                <i class="icon-zoom-in icon-white"></i>
                                                                <fmt:message key="common.view"/>
                                                            </a>
                                                            <a class="btn btn-info edit-category"
                                                               hreflang="/admin/catalog/category_form.html?id=${level3Category.current.id}" href="#categoryform">
                                                                <i class="icon-edit icon-white"></i>
                                                                <fmt:message key="common.edit"/>
                                                            </a>
                                                            <a class="btn btn-danger show-confirm" readonly="readonly"
                                                               lang='<fmt:message key="category.do.you.want.to.delete"/>'
                                                               href="/admin/catalog/category_delete.html?id=${level3Category.current.id}&csrf=<sec:authentication property="details.csrf"/>">
                                                                <i class="icon-trash icon-white"></i>
                                                                <fmt:message key="common.delete"/>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>


                                        </c:forEach>
                                    </c:if>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty rootCategories}">
                            <fmt:message key="category.is.empty"/>
                        </c:if>

                    </div>
                </div>
            </div>
            <!--/span-->
        </div>
    </div>

    <%--Form--%>
    <div class="row-fluid" id="categoryform">
    </div>
    <h:reloadform id="categoryform" url="/admin/catalog/category_form.html"/>

    <!--/span-->
</div>
</body>
</html>