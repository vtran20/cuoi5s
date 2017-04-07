<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>
<script>
$('[data-rel=popover]').popover({container:'body'});
</script>

<spring:eval expression="serviceLocator.newsCategoryDao.getRootNewsCategories(site, 'N')" var="newsCategories"/>
<div class="page-header">
    <%--<h4>Cập Nhật Thông Tin Liên Hệ</h4>--%>
    <a class="btn btn-xs btn-info" title="<fmt:message key="common.add.new"/>" href="#modal-form" role="button" data-toggle="modal" data-page="/admin/news/form.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="news.add.news.category"/>
    </a>
</div><!-- /.page-header -->

<!-- Bread Crumb Navigation -->
<div class="row">
<div class="col-xs-12">
    <h:frontendmessage _messages="${messages}"/>
    <%--Table--%>
    <div class="row">
        <div class="col-xs-12">
            <!-- Portlet: Member List -->
            <div class="box" id="box-0">
                <h4 class="box-header round-top"><fmt:message key="news.categories.list"/>
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>

                <div class="box-container-toggle">
                    <div class="box-content">
                        <c:if test="${!empty newsCategories}">

                            <table cellpadding="0" cellspacing="0" border="0"
                                   class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                <thead>
                                <tr>
                                    <th><fmt:message key="news.category.name"/></th>
                                    <th><fmt:message key="news.category.status"/></th>
                                    <th><fmt:message key="news.category.actions"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${newsCategories}" varStatus="newsCategory" var="currentNewsCategory">

                                    <tr id="${newsCategory.current.id}-s${newsCategory.current.sequence}">
                                        <td><c:out value="${newsCategory.current.name}"/></td>
                                        <td class="center">
                                            <c:set value="${newsCategory.current.active}" var="active"/>
                                            <c:if test="${fn:startsWith(active, 'Y')}">
                                                <span class="label label-success"><fmt:message key="news.category.active.status"/></span>
                                            </c:if>
                                            <c:if test="${!fn:startsWith(active, 'Y')}">
                                                <span class="label label-inverse"><fmt:message key="news.category.inactive.status"/></span>
                                            </c:if>

                                        </td>
                                        <c:set var="siteUrl" value="/news/${newsCategory.current.uri}"/>
                                        <td class="center">
                                            <a class="btn btn-xs btn-success" target="_blank" href="${siteUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.category.view"/>"><i class="normal-icon ace-icon fa fa-eye bigger-120"></i></a>
                                            <a class="btn btn-xs btn-info" data-toggle="modal" data-page="/admin/news/form.html?id=${newsCategory.current.id}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.category.edit"/>" data-target="#modal-form"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
                                            <a class="btn btn-xs btn-danger global_show-confirm" lang='<fmt:message key="news.category.do.you.want.to.delete"/>' hreflang="/admin/news/delete.html?id=${newsCategory.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.category.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>

                                        </td>
                                    </tr>
                                    <%--Get SubNewsCategory--%>
                                    <spring:eval expression="serviceLocator.newsCategoryDao.getSubNewsCategories(site, currentNewsCategory, 'N')" var="subNewsCategories"/>
                                    <c:if test="${! empty subNewsCategories}">
                                        <c:forEach items="${subNewsCategories}" varStatus="subNewsCategory">

                                            <tr id="${newsCategory.current.id}-${subNewsCategory.current.id}-s${subNewsCategory.current.sequence}">
                                                <td><c:out value="---- ${subNewsCategory.current.name}"/></td>
                                                <td class="center">
                                                    <c:set value="${subNewsCategory.current.active}" var="active"/>
                                                    <c:if test="${fn:startsWith(active, 'Y')}">
                                                        <span class="label label-success"><fmt:message key="news.category.active.status"/></span>
                                                    </c:if>
                                                    <c:if test="${!fn:startsWith(active, 'Y')}">
                                                        <span class="label label-inverse"><fmt:message key="news.category.inactive.status"/></span>
                                                    </c:if>

                                                </td>
                                                <c:set var="siteUrl" value="/${subNewsCategory.current.uri}"/>
                                                <td class="center">
                                                    <a class="btn btn-xs btn-success" target="_blank" href="${siteUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.category.view"/>"><i class="normal-icon ace-icon fa fa-eye bigger-120"></i></a>
                                                    <a class="btn btn-xs btn-info" data-toggle="modal" data-page="/admin/news/form.html?id=${subNewsCategory.current.id}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.category.edit"/>" data-target="#modal-form"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
                                                    <a class="btn btn-xs btn-danger global_show-confirm" lang='<fmt:message key="news.category.do.you.want.to.delete"/>' hreflang="/admin/news/delete.html?id=${subNewsCategory.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.category.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty newsCategories}">
                            <fmt:message key="news.category.is.empty"/>
                        </c:if>

                    </div>
                </div>
            </div>
            <!--/span-->
        </div>
    </div>

    <!--/span-->
</div>
</div>
<h:form_modal/>
