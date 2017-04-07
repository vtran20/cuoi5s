<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>

<title><fmt:message key="category.categories"/></title>
<div class="page-header">
    <a class="btn btn-xs btn-info" title="<fmt:message key="category.add"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="category.add"/>" data-page="/admin/catalog/category_form.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="category.add"/>
    </a>
</div><!-- /.page-header -->

<div class="row">
<div class="col-xs-12">
<h:frontendmessage _messages="${messages}"/>
<div  id="message_alert"></div>

<!-- PAGE CONTENT BEGINS -->
<spring:eval expression="serviceLocator.categoryDao.getRootCategories(site, false)" var="rootCategories"/>
<div class="row">
    <div class="col-sm-6">
        <div class="page-header">
            <h4><fmt:message key="category.categories"/></h4>
        </div>
        <div class="dd" id="nestable">
            <ol class="dd-list">
                <c:forEach items="${rootCategories}" varStatus="parentCat">
                    <li class="dd-item dd2-item" data-id="${parentCat.current.id}">
                        <div class="dd-handle dd2-handle">
                            <i class="normal-icon ace-icon fa fa-bars blue bigger-130"></i>
                            <i class="drag-icon ace-icon fa fa-arrows bigger-125"></i>
                        </div>
                        <div class="dd2-content"><a href="#/admin/catalog/categories" class="view_products" id="${parentCat.current.id}"><c:out value="${parentCat.current.name}"/></a>
                            <div class="pull-right">
                                <fmt:message key="common.status"/>: <input id="active_${parentCat.current.id}" class="global-toggle-checkbox" type="checkbox" name="active" value="${parentCat.current.id}" ${parentCat.current.active == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/catalog/${parentCat.current.id}/activate_category.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                <a class="blue" data-page="/admin/catalog/category_form.html?id=${parentCat.current.id}" data-target="#modal-form" data-toggle="modal" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="common.edit"/>"><i class="ace-icon fa fa-pencil bigger-130"></i></a> |
                                <a class="red global_show-confirm" readonly="readonly" lang='<fmt:message key="category.do.you.want.to.delete"/>' hreflang="/admin/catalog/category_delete.html?id=${parentCat.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="common.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash bigger-130"></i></a>
                            </div>
                        </div>
                            <%--Get Category--%>
                        <c:set var="parentCatId" value="${parentCat.current.id}"/>
                        <spring:eval expression="serviceLocator.categoryDao.findByOrder('parentCategory.id', T(java.lang.Long).valueOf(parentCatId), 'sequence', site.id)" var="categories"/>
                        <c:if test="${! empty categories}">
                            <ol class="dd-list">
                            <c:forEach items="${categories}" varStatus="category" var="currentCategory">
                                    <li class="dd-item dd2-item" data-id="${category.current.id}">
                                        <div class="dd-handle dd2-handle">
                                            <i class="normal-icon ace-icon fa fa-bars blue bigger-130"></i>
                                            <i class="drag-icon ace-icon fa fa-arrows bigger-125"></i>
                                        </div>
                                        <div class="dd2-content"><a href="#/admin/catalog/categories" class="view_products" id="${category.current.id}">${category.current.name}</a>
                                            <div class="pull-right">
                                                <fmt:message key="common.status"/>: <input id="active_${category.current.id}" class="global-toggle-checkbox" type="checkbox" name="active" value="${category.current.id}" ${category.current.active == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/catalog/${category.current.id}/activate_category.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                                <a class="edit-menu blue" data-page="/admin/catalog/category_form.html?id=${category.current.id}" data-target="#modal-form" data-toggle="modal" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="common.edit"/>"><i class="ace-icon fa fa-pencil bigger-130"></i></a> |
                                                <a class="red global_show-confirm" readonly="readonly" lang='<fmt:message key="category.do.you.want.to.delete"/>' hreflang="/admin/catalog/category_delete.html?id=${category.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="common.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash bigger-130"></i></a>
                                            </div>
                                        </div>

                                            <%--Get Subcategory --%>
                                        <c:set var="categoryId" value="${category.current.id}"/>
                                        <spring:eval expression="serviceLocator.categoryDao.findByOrder('parentCategory.id', T(java.lang.Long).valueOf(categoryId), 'sequence', site.id)" var="subCategories"/>
                                        <c:if test="${! empty subCategories}">
                                            <ol class="dd-list">
                                            <c:forEach items="${subCategories}" varStatus="subCategory">
                                                <li class="dd-item dd2-item" data-id="${subCategory.current.id}">
                                                    <div class="dd-handle dd2-handle">
                                                        <i class="normal-icon ace-icon fa fa-bars blue bigger-130"></i>
                                                        <i class="drag-icon ace-icon fa fa-arrows bigger-125"></i>
                                                    </div>
                                                    <div class="dd2-content"><a href="#/admin/catalog/categories" class="view_products" id="${subCategory.current.id}">${subCategory.current.name}</a>
                                                        <div class="pull-right">
                                                            <fmt:message key="common.status"/>: <input id="active_${subCategory.current.id}" class="global-toggle-checkbox" type="checkbox" name="active" value="${subCategory.current.id}" ${subCategory.current.active == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/catalog/${subCategory.current.id}/activate_category.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"> |
                                                            <a class="edit-menu blue" hreflang="/admin/catalog/category_form.html?id=${subCategory.current.id}" href="#menuform" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="common.edit"/>"><i class="ace-icon fa fa-pencil bigger-130"></i></a> |
                                                            <a class="red global_show-confirm" readonly="readonly" lang='<fmt:message key="category.do.you.want.to.delete"/>' hreflang="/admin/catalog/category_delete.html?id=${subCategory.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="common.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash bigger-130"></i></a>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:forEach>
                                            </ol>
                                        </c:if>

                                    </li>
                            </c:forEach>
                            </ol>
                        </c:if>
                    </li>
                </c:forEach>
            </ol>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="page-header">
            <h4><fmt:message key="category.product.categories"/></h4>
        </div>
    <%--Table--%>
        <div>
            <table id="products-table" class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Model</th>
                    <th>Action</th>
                </tr>
                </thead>

                <tfoot>
                <tr>
                    <th>Name</th>
                    <th>Model</th>
                    <th>Action</th>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div><!-- PAGE CONTENT ENDS -->

<h:form_modal/>
<script type="text/javascript">

    $('[data-rel=popover]').popover({container:'body'});

    $(".view_products").on("click", function() {
        object = $(this);
        table = $('#products-table').dataTable( {
            "destroy": true,
            "processing": true,
            "serverSide": false,
            "ajax": {
                "url": "/admin/catalog/get_product_from_category.html",
                "type": "GET",
                "data" : function ( d ) {
                    d.catId = object.attr("id");
                }
            },
            "columns": [
                { "data": "name" },
                { "data": "model" },
//                { "data": "active" },
                {
                    "class":          "details-control",
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": "",
                    render: function ( data, type, row ) {
                        // Combine the first and last names into a single table field
                        return "<a class='btn btn-xs btn-info' href='#/admin/catalog/product?id="+data.id+"' data-url='/admin/catalog/products'><i class='ace-icon fa fa-pencil bigger-120'></i></a>" + " " +
                                "<a class='btn btn-xs btn-danger show-confirm' data-url='/admin/catalog/products' title='<fmt:message key='common.delete'/>' lang='<fmt:message key='common.doyouwanttodeletethiscontent'/>' hreflang='/admin/catalog/deleteproduct.html?id="+data.id+"&csrf=<sec:authentication property='details.csrf'/>'><i class='ace-icon fa fa-trash-o bigger-120'></i></a>";
                    }
                }
            ],
            //add product id to tr.id
            "rowCallback": function( row, data ) {
                $(row).attr('product_id',data.id);
            }
        } );
    });

    $("table#products-table").delegate("a.show-confirm", "click", function(){
        var object = $(this);
        BootstrapDialog.show({
            type:BootstrapDialog.TYPE_DANGER,
            closeByBackdrop: false,
            closeByKeyboard: false,
            title:'Xác Nhận',
            message:object.attr("lang"),
            buttons:[
                {
                    label:'Yes',
                    action:function (dialog) {
                        $.ajax({
                            type: "GET",
                            url: object.attr("hreflang"),
                            data: $("#form").serialize(), // serializes the form's elements.
                            success: function(data)
                            {
                                object.closest('tr').remove();
                                $("#message_alert").html(data);
                                $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                    $("#message_alert").alert('close');
                                });
                            }
                        });
                        dialog.close();
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

    $('.dd').nestable({group:1});

    $('.dd-handle a').on('mousedown', function(e){
        e.stopPropagation();
    });
    $('.dd').on('change', function(e){
        var list   = e.length ? e : $(e.target);
        if (list.nestable('getDragItem').attr('data-id') == null) return;
        if (window.JSON) {
            $.ajax({
                type: "GET",
                url: '/admin/catalog/category_reorder.html',
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
    //Default nestable will be collapsed
    $('#nestable').nestable('collapseAll');
</script>