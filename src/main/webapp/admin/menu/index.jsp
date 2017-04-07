<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>
<title>Menu và Trang Web</title>
<div class="page-header">
    <a class="btn btn-xs btn-info" title="<fmt:message key="menu.add.new.menu"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="menu.form.menu"/>" data-page="/admin/menu/form.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="menu.add.new.menu"/>
    </a>
    <a class="btn btn-xs btn-info" title="<fmt:message key="menu.add.new.external.menu"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="menu.form.menu"/>" data-page="/admin/menu/form.html?external=E" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="menu.add.new.external.menu"/>
    </a>
    <a class="btn btn-xs btn-info" title="<fmt:message key="add.menu.from.library"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="menu.form.menu"/>" data-page="/admin/menu/menutemplates.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="add.menu.from.library"/>
    </a>
</div><!-- /.page-header -->

<div class="row">
<div class="col-xs-12">
<h:frontendmessage _messages="${messages}"/>
<div  id="message_alert"></div>

<c:if test="${not empty site}">
    <spring:eval expression="serviceLocator.menuDao.getRootMenus(site, 'N', 'N')" var="menus"/>
</c:if>

<!-- PAGE CONTENT BEGINS -->
<div class="row">
    <div class="col-sm-12">
        <div class="dd" id="nestable">
            <ol class="dd-list">
                <c:forEach items="${menus}" varStatus="menu" var="currentMenu">
                <c:set var="disableClass" value="dd-handle"/>
                <c:if test="${menu.current.homePage == 'Y'}">
                    <c:set var="disableClass" value=""/>
                </c:if>
                <li class="dd-item dd2-item" data-id="${currentMenu.id}">
                    <div class="${disableClass} dd2-handle">
                        <i class="normal-icon ace-icon fa fa-bars blue bigger-130"></i>
                        <i class="drag-icon ace-icon fa fa-arrows bigger-125"></i>
                    </div>
                    <c:set var="pagePreviewUrl" value="/content/${menu.current.uri}"/>
                    <c:set var="designUrl" value="#/admin/design/index?menuId=${menu.current.id}"/>
                    <c:if test="${menu.current.homePage == 'Y'}">
                        <c:set var="pagePreviewUrl" value="/"/>
                        <c:set var="designUrl" value="#/admin/design/index?menuId=${menu.current.id}"/>
                    </c:if>
                    <c:if test="${menu.current.menuTemplate == 'Y'}">
                        <c:set var="pagePreviewUrl" value="/${menu.current.uri}"/>
                        <c:set var="designUrl" value="${menu.current.designUrl}"/>
                    </c:if>
                    <c:if test="${menu.current.menuTemplate == 'E'}">
                        <c:set var="pagePreviewUrl" value="${menu.current.uri}"/>
                        <c:set var="designUrl" value="#"/>
                    </c:if>
                    <c:url value="${pagePreviewUrl}" var="pagePreviewUrl"/>
                    <div class="dd2-content"><a class="" data-url="/admin/sites/index" href="${designUrl}"><c:out value="${menu.current.name}"/></a>
                        <div class="pull-right">
                            <fmt:message key="menu.header.menu"/>: <input id="headerMenu_${menu.current.id}" class="global-toggle-checkbox" type="checkbox" name="headerMenu" value="${menu.current.id}" ${menu.current.headerMenu == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${menu.current.id}/activate_header_menu.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                            <fmt:message key="menu.status"/>: <input id="active_${menu.current.id}" class="global-toggle-checkbox" type="checkbox" name="active" value="${menu.current.id}" ${menu.current.active == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${menu.current.id}/activate_menu.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                            <fmt:message key="menu.displaybreadcrumb"/>: <input id="displayBreadcrumb_${menu.current.id}" class="global-toggle-checkbox" type="checkbox" name="displayBreadcrumb" value="${menu.current.id}" ${menu.current.displayBreadcrumb == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${menu.current.id}/activate_breadcrumb.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                            <a class="green" target="_blank" href="${pagePreviewUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.preview.page"/>"><i class="normal-icon ace-icon fa fa-eye green bigger-130"></i></a> |
                            <a class="" data-url="/admin/sites/index" href="${designUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.design.page"/>"><i class="ace-icon fa fa-desktop bigger-130"></i></a> |
                            <a class="edit-menu blue" data-page="/admin/menu/form.html?id=${menu.current.id}" data-target="#modal-form" data-toggle="modal" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.edit"/>"><i class="ace-icon fa fa-pencil bigger-130"></i></a> |
                            <a class="red global_show-confirm" readonly="readonly" lang='<fmt:message key="menu.do.you.delete.this.menu"/>' hreflang="/admin/menu/delete.html?id=${menu.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash bigger-130"></i></a>
                        </div>
                    </div>
                    <%--Get Submenu--%>
                    <spring:eval expression="serviceLocator.menuDao.getSubMenus(site, currentMenu, 'N', 'N')" var="subMenus"/>
                    <c:if test="${! empty subMenus}">
                    <c:forEach items="${subMenus}" varStatus="subMenu" var="currentSubMenu">
                    <ol class="dd-list">
                        <li class="dd-item dd2-item" data-id="${subMenu.current.id}">
                            <div class="dd-handle dd2-handle">
                                <i class="normal-icon ace-icon fa fa-bars blue bigger-130"></i>
                                <i class="drag-icon ace-icon fa fa-arrows bigger-125"></i>
                            </div>
                            <c:set var="pagePreviewUrl" value="/content/${subMenu.current.uri}"/>
                            <c:set var="designUrl" value="#/admin/design/index?menuId=${subMenu.current.id}"/>
                            <c:if test="${subMenu.current.homePage == 'Y'}">
                                <c:set var="pagePreviewUrl" value="/"/>
                                <c:set var="designUrl" value="#/admin/design/index?menuId=${subMenu.current.id}"/>
                            </c:if>
                            <c:if test="${subMenu.current.menuTemplate == 'Y'}">
                                <c:set var="pagePreviewUrl" value="/${subMenu.current.uri}"/>
                                <c:set var="designUrl" value="${subMenu.current.designUrl}"/>
                            </c:if>
                            <c:if test="${subMenu.current.menuTemplate == 'E'}">
                                <c:set var="pagePreviewUrl" value="${subMenu.current.uri}"/>
                                <c:set var="designUrl" value="#"/>
                            </c:if>
                            <c:url value="${pagePreviewUrl}" var="pagePreviewUrl"/>
                            <div class="dd2-content"><a class="" data-url="/admin/sites/index" href="${designUrl}">${subMenu.current.name}</a>
                                <div class="pull-right">
                                    <fmt:message key="menu.header.menu"/>: <input id="headerMenu_${subMenu.current.id}" class="global-toggle-checkbox" type="checkbox" name="headerMenu" value="${subMenu.current.id}" ${subMenu.current.headerMenu == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${subMenu.current.id}/activate_header_menu.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                    <fmt:message key="menu.status"/>: <input id="active_${subMenu.current.id}" class="global-toggle-checkbox" type="checkbox" name="active" value="${subMenu.current.id}" ${subMenu.current.active == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${subMenu.current.id}/activate_menu.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                    <fmt:message key="menu.displaybreadcrumb"/>: <input id="displayBreadcrumb_${subMenu.current.id}" class="global-toggle-checkbox" type="checkbox" name="displayBreadcrumb" value="${subMenu.current.id}" ${subMenu.current.displayBreadcrumb == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${subMenu.current.id}/activate_breadcrumb.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                    <a class="green" target="_blank" href="${pagePreviewUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.preview.page"/>"><i class="normal-icon ace-icon fa fa-eye green bigger-130"></i></a> |
                                    <a class="" data-url="/admin/sites/index" href="${designUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.design.page"/>"><i class="ace-icon fa fa-desktop bigger-130"></i></a> |
                                    <a class="edit-menu blue" data-page="/admin/menu/form.html?id=${subMenu.current.id}" data-target="#modal-form" data-toggle="modal" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.edit"/>"><i class="ace-icon fa fa-pencil bigger-130"></i></a> |
                                    <a class="red global_show-confirm" readonly="readonly" lang='<fmt:message key="menu.do.you.delete.this.menu"/>' hreflang="/admin/menu/delete.html?id=${subMenu.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash bigger-130"></i></a>
                                </div>
                            </div>

                                <%--Get Submenu level 2--%>
                            <spring:eval expression="serviceLocator.menuDao.getSubMenus(site, currentSubMenu, 'N', 'N')" var="subLevel2Menus"/>
                            <c:if test="${! empty subLevel2Menus}">
                                <c:forEach items="${subLevel2Menus}" varStatus="subMenuLevel2">
                                    <ol class="dd-list">
                                        <li class="dd-item dd2-item" data-id="${subMenuLevel2.current.id}">
                                            <div class="dd-handle dd2-handle">
                                                <i class="normal-icon ace-icon fa fa-bars blue bigger-130"></i>
                                                <i class="drag-icon ace-icon fa fa-arrows bigger-125"></i>
                                            </div>
                                            <c:set var="pagePreviewUrl" value="/content/${subMenuLevel2.current.uri}"/>
                                            <c:set var="designUrl" value="#/admin/design/index?menuId=${subMenuLevel2.current.id}"/>
                                            <c:if test="${subMenuLevel2.current.homePage == 'Y'}">
                                                <c:set var="pagePreviewUrl" value="/"/>
                                                <c:set var="designUrl" value="#/admin/design/index?menuId=${subMenu.current.id}"/>
                                            </c:if>
                                            <c:if test="${subMenuLevel2.current.menuTemplate == 'Y'}">
                                                <c:set var="pagePreviewUrl" value="/${subMenuLevel2.current.uri}"/>
                                                <c:set var="designUrl" value="${subMenuLevel2.current.designUrl}"/>
                                            </c:if>
                                            <c:if test="${subMenuLevel2.current.menuTemplate == 'E'}">
                                                <c:set var="pagePreviewUrl" value="${subMenuLevel2.current.uri}"/>
                                                <c:set var="designUrl" value="#"/>
                                            </c:if>
                                            <c:url value="${pagePreviewUrl}" var="pagePreviewUrl"/>
                                            <div class="dd2-content"><a class="" data-url="/admin/sites/index" href="${designUrl}">${subMenuLevel2.current.name}</a>
                                                <div class="pull-right">
                                                    <fmt:message key="menu.header.menu"/>: <input id="headerMenu_${subMenuLevel2.current.id}" class="global-toggle-checkbox" type="checkbox" name="headerMenu" value="${subMenuLevel2.current.id}" ${subMenuLevel2.current.headerMenu == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${subMenuLevel2.current.id}/activate_header_menu.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                                    <fmt:message key="menu.status"/>: <input id="active_${subMenuLevel2.current.id}" class="global-toggle-checkbox" type="checkbox" name="active" value="${subMenuLevel2.current.id}" ${subMenuLevel2.current.active == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${subMenuLevel2.current.id}/activate_menu.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                                    <fmt:message key="menu.displaybreadcrumb"/>: <input id="displayBreadcrumb_${subMenuLevel2.current.id}" class="global-toggle-checkbox" type="checkbox" name="displayBreadcrumb" value="${subMenuLevel2.current.id}" ${subMenuLevel2.current.displayBreadcrumb == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/menu/${subMenuLevel2.current.id}/activate_breadcrumb.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                                    <a class="green" target="_blank" href="${pagePreviewUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.preview.page"/>"><i class="normal-icon ace-icon fa fa-eye green bigger-130"></i></a> |
                                                    <a class="" data-url="/admin/sites/index" href="${designUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.design.page"/>"><i class="ace-icon fa fa-desktop bigger-130"></i></a> |
                                                    <a class="edit-menu blue" hreflang="/admin/menu/form.html?id=${subMenuLevel2.current.id}" href="#menuform" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.edit"/>"><i class="ace-icon fa fa-pencil bigger-130"></i></a> |
                                                    <a class="red global_show-confirm" readonly="readonly" lang='<fmt:message key="menu.do.you.delete.this.menu"/>' hreflang="/admin/menu/delete.html?id=${subMenuLevel2.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="menu.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash bigger-130"></i></a>
                                                </div>
                                            </div>
                                        </li>
                                    </ol>
                                </c:forEach>
                            </c:if>

                        </li>
                    </ol>
                    </c:forEach>
                    </c:if>
                </li>
                </c:forEach>
            </ol>
        </div>
    </div>

    <div class="col-sm-4">
    </div>
</div><!-- PAGE CONTENT ENDS -->

<h:form_modal/>
<script type="text/javascript">
    $('[data-rel=popover]').popover({container:'body'});
    $('.dd').nestable({group:1, maxDepth:3});

    $('.dd-handle a').on('mousedown', function(e){
        e.stopPropagation();
    });
    $('.dd').on('change', function(e){
        var list   = e.length ? e : $(e.target);
        if (list.nestable('getDragItem').attr('data-id') == null) return;
        if (window.JSON) {
            $.ajax({
                type: "GET",
                url: '/admin/menu/reorder.html',
                data: {currentItem: list.nestable('getDragItem').attr('data-id'), orderList:window.JSON.stringify(list.nestable('serialize'))},
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
        } else {
            alert('JSON browser support required for this demo.');
        }
        //e.stopPropagation();
    });
    $('#nestable').nestable('collapseAll');

</script>