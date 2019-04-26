<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>
<c:if test="${! empty param.menuId}">
    <c:set var="menuId" value="${param.menuId}"/>
    <spring:eval expression="serviceLocator.getMenuDao().findById(T(java.lang.Long).valueOf(menuId))" var="menu"/>
</c:if>

<title><fmt:message key="admin.design.web.page"/></title>
<div class="page-header">
    <h4><fmt:message key="admin.design.web.page"/></h4>
</div><!-- /.page-header -->
<script>
    var remoteTabsPluginLoaded;
    $('.design-partcontent').click(function (e) {
        var obj = $(this);
        var thisTab = $('a[href="#tab'+obj.attr("data-id")+'"]');
        if (thisTab.length) {
            //open existing tab
            thisTab.tab('show');
        } else {
            // create the tab
            $('<li><a data-toggle="tab" href="#tab'+obj.attr("data-id")+'" data-tab-url="'+obj.attr("data-page")+'" data-tab-always-refresh="true">'+obj.attr("data-title")+' <button type="button" class="tab-close-icon-size close" data-dismiss="alert"> <i class="ace-icon fa fa-times"></i></button></a></li>').appendTo('#form-tab');

            // create the tab content
            $('<div class="tab-pane fade" id="tab'+obj.attr("data-id")+'">loading...</div>').appendTo('.tab-content');
            // enable all remote data tabs
//            if (remoteTabsPluginLoaded == null || remoteTabsPluginLoaded === undefined) {
//                alert(1);
                remoteTabsPluginLoaded = new RemoteTabs();
//            }
            // make the new tab active
            $('#form-tab li a:last').tab('show');
        }
    });

    /**
     * Remove a Tab
     */
    $('#form-tab').on('click', ' li button.close', function() {
        var tabId = $(this).parents('li').children('a').attr('href');
        $(this).parents('li').remove('li');
        $(tabId).remove();
        // enable all remote data tabs
//        var remoteTabsPluginLoaded = new RemoteTabs();
        // make the first tab active
        $('#form-tab li a:first').tab('show');
    });

    $(function () {
        <%--Sort table--%>
        // Sortable rows
        $('#simple-table').sortable({
            containerSelector: 'table',
            itemPath: '> tbody',
            itemSelector: 'tr',
            delay: 200,
            placeholder: '<tr class="placeholder"/>',
            onDrop: function  ($item, container, _super) {
                var result = "";
                var container1 = $("table#simple-table tbody tr");
                for (i = 0; i < container1.size(); i++) {
                    if (result=='') {
                        result = $(container1[i]).attr("id")
                    } else {
                        result += ","+$(container1[i]).attr("id")
                    }

                }
                _super($item, container);
                <%--Submit to server and reload page--%>
                $.ajax({
                    type: "GET",
                    url: '/admin/design/reorderRow.html',
                    data: {currentItem: $item.attr("id"), orderList:result},
                    success: function(data)
                    {
                        $('#message_alert').html(data);
                        $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#message_alert").alert('close');
                        });
                    }
                });
            }
        });
    });
</script>
<c:if test="${not empty site}">
    <spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(T(java.lang.Long).valueOf(menuId), 'N')" var="menuRows"/>
</c:if>

<div class="row">
<div class="col-xs-12">
<div class="row">
<div class="col-sm-12">
<!-- #section:elements.tab -->
<div  id="message_alert"></div>
<div class="tabbable">
<ul class="nav nav-tabs" id="form-tab">
    <li class="active">
        <a data-toggle="tab" href="#home">
            <i class="ace-icon fa fa-list"></i>
            Nội Dung: ${menu.name}
        </a>
    </li>
</ul>
<div class="tab-content">
<div id="home" class="tab-pane fade in active">
    <div class="page-header">
        <a class="btn btn-xs btn-info" title="<fmt:message key="common.add.new.row"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="common.add.new.row"/>" data-page="/admin/design/row_form.html?menuId=${menuId}&widgetType=page" data-target="#modal-form">
            <i class="fa fa-plus"></i> <fmt:message key="common.add.new.row"/>
        </a>
        <a class="btn btn-xs btn-info" title="<fmt:message key="common.add.new.productlist.row"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="common.add.new.productlist.row"/>" data-page="/admin/design/row_form.html?menuId=${menuId}&widgetType=product" data-target="#modal-form">
            <i class="fa fa-plus"></i> <fmt:message key="common.add.new.productlist.row"/>
        </a>
        <a class="btn btn-xs btn-info" title="<fmt:message key="common.add.new.news.row"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="common.add.new.news.row"/>" data-page="/admin/design/row_form.html?menuId=${menuId}&widgetType=news" data-target="#modal-form">
            <i class="fa fa-plus"></i> <fmt:message key="common.add.new.news.row"/>
        </a>
    </div><!-- /.page-header -->

    <div class="row">
        <div class="col-xs-12">
            <c:if test="${!empty menuRows}">
                <table id="simple-table" class="table table-striped table-bordered table-hover sorted_table">
                    <thead>
                    <tr>
                        <th><fmt:message key="admin.design.web.row.list"/></th>
                        <th><fmt:message key="admin.design.web.row.show.header"/></th>
                        <th></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${menuRows}" varStatus="row" var="currentRow">
                        <spring:eval expression="serviceLocator.siteMenuPartContentDao.getWidgetTemplate(T(java.lang.Long).valueOf(currentRow.id))" var="currWidgetTemplate"/>

                        <tr id="${row.current.id}">
                            <td>
                                <c:out value="${currentRow.title}"/>
                            </td>
                            <td>
                                <input class="global-toggle-checkbox" type="checkbox" name="showTitle" value="${row.current.id}" ${row.current.showTitle == 'Y'?'checked':''} lang='<fmt:message key="do.you.want.to.show.row.header.title"/>' data-src="/admin/design/change_status_show_row_title.html?rowId=${row.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"/></td>
                            </td>
                            <td>
                                <div class="hidden-sm hidden-xs btn-group">
                                    <c:if test="${currWidgetTemplate.type == 'product'}">
                                        <a class="btn btn-xs btn-success design-partcontent" title="<fmt:message key="design.part.content"/>" role="button" data-title="${row.current.title}" data-id="${row.current.id}" data-page="/admin/design/contentproduct_index.html?id=${row.current.id}"><i class="ace-icon fa fa-desktop bigger-120"></i></a>
                                        <a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" role="button" data-toggle="modal" data-title="<fmt:message key="common.update"/>" data-page="/admin/design/row_form.html?menuId=${menuId}&id=${row.current.id}&widgetType=product" data-target="#modal-form"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
                                    </c:if>
                                    <c:if test="${currWidgetTemplate.type == 'news'}">
                                        <a class="btn btn-xs btn-success design-partcontent" title="<fmt:message key="design.part.content"/>" role="button" data-title="${row.current.title}" data-id="${row.current.id}" data-page="/admin/design/contentnews_index.html?id=${row.current.id}"><i class="ace-icon fa fa-desktop bigger-120"></i></a>
                                        <a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" role="button" data-toggle="modal" data-title="<fmt:message key="common.update"/>" data-page="/admin/design/row_form.html?menuId=${menuId}&id=${row.current.id}&widgetType=news" data-target="#modal-form"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
                                    </c:if>
                                    <c:if test="${currWidgetTemplate.type == 'page'}">
                                        <a class="btn btn-xs btn-success design-partcontent" title="<fmt:message key="design.part.content"/>" role="button" data-title="${row.current.title}" data-id="${row.current.id}" data-page="/admin/design/content_index.html?id=${row.current.id}"><i class="ace-icon fa fa-desktop bigger-120"></i></a>
                                        <a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" role="button" data-toggle="modal" data-title="<fmt:message key="common.update"/>" data-page="/admin/design/row_form.html?menuId=${menuId}&id=${row.current.id}&widgetType=page" data-target="#modal-form"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
                                    </c:if>
                                    <a class="btn btn-xs btn-danger global_show-confirm" title="<fmt:message key="common.delete"/>" lang='<fmt:message key="common.do.you.want.to.delete.this.content"/>' hreflang="/admin/design/deleterow.html?id=${row.current.id}&csrf=<sec:authentication property="details.csrf"/>"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty menuRows}">
                <fmt:message key="common.is.empty"/>
            </c:if>
        </div><!-- /.span -->
    </div><!-- /.row -->
</div> <%--tab home--%>
    <%--<div id="tab1" class="tab-pane fade">tab1 content</div>--%>
</div>
</div>
</div>
</div>
</div>
</div>
<h:form_modal/>
<h:image_modal/>
<script>
    $('[data-rel=popover]').popover({container:'body'});

    <%--function activeContent(thisCheckBox, id) {--%>
        <%--var answer = confirm("Do you want to show/unshow this menu?");--%>
        <%--var flag = thisCheckBox.checked;--%>
        <%--if (answer){--%>
            <%--$.get(--%>
                    <%--"/admin/design/"+id+"/activate_partcontent.html",--%>
                    <%--{ flag: flag, csrf: <sec:authentication property="details.csrf"/>}, // some params to pass it--%>
                    <%--function(data) {--%>
                        <%--if ("ok" == data) {--%>
                            <%--alert ("Action completed successfully!")--%>
                        <%--} else {--%>
                            <%--alert ("Action fail!")--%>
                            <%--document.getElementById('active_'+id).checked = !flag--%>
                        <%--}--%>
                    <%--});--%>
        <%--} else {--%>
            <%--document.getElementById('active_'+id).checked = !flag;--%>
        <%--}--%>
    <%--}--%>

</script>
