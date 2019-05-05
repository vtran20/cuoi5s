<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<div class="page-header">
    <a class="btn btn-xs btn-info" data-title="<fmt:message key="product.add"/>" href="#modal-form" role="button" data-toggle="modal" data-page="/admin/catalog/product_form.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="product.add"/>
    </a>
</div><!-- /.page-header -->
<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-sm-12">
                <input type="text" placeholder="Tìm Kiếm Sản Phẩm ..." class="col-xs-10 col-sm-5" id="search-data" autocomplete="off" maxlength="50"> <a href="#/admin/catalog/products" id="search-product" class="btn btn-sm btn-success"><i class='ace-icon fa fa-search'></i></a>
            </div>
        </div>
        <hr>
        <div  id="message_alert"></div>
        <div class="row">
            <div class="col-xs-12">
                <div>
                <%--Table--%>
                <table id="products-table" class="table table-striped table-bordered table-hover">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Model</th>
                        <th>Active</th>
                        <th>Action</th>
                    </tr>
                    </thead>

                    <tfoot>
                    <tr>
                        <th>Name</th>
                        <th>Model</th>
                        <th>Active</th>
                        <th>Action</th>
                    </tr>
                    </tfoot>
                </table>
                </div>
                <!--/span-->
            </div>
        </div>
    </div>
</div>
<h:form_modal/>
<script type="text/javascript">
    $( document ).ready(function() {
//        alert (1);
//        $('#search-product').trigger( "click" );
//        document.getElementById("search-product").click();
        $( '#search-product' ).animate({
            // properties to animate
        }, 300, function () {
            $('#search-product').click();
        });
    });

    $('#search-data').keypress(function(e){
        if (e.keyCode == 13 || e.which == 13) {
            $('#search-product').click();
        }
    });

    $("#search-product").on("click", function() {
        table = $('#products-table').dataTable( {
            "destroy": true,
            "processing": true,
            "serverSide": false,
            "ajax": {
                "url": "/admin/catalog/search_product.html",
                "type": "GET",
                "data" : function ( d ) {
                    d.keyword = $("#search-data").val();
                }
            },
            "columns": [
                { "data": "name" },
                { "data": "model" },
                { "data": "active" },
                {
                    "class":          "details-control",
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": "",
                    render: function ( data, type, row ) {
                        // Combine the first and last names into a single table field
                        return "<a class='btn btn-xs btn-info' href='#/admin/catalog/product?id="+data.id+"' data-url='/admin/catalog/products'><i class='ace-icon fa fa-pencil bigger-120'></i></a>" + " " +
                                "<a class='btn btn-xs btn-danger show-confirm' data-url='/admin/catalog/products' title='<fmt:message key='common.delete'/>' lang='<fmt:message key='product.do.you.want.delete.this.product'/>' hreflang='/admin/catalog/deleteproduct.html?id="+data.id+"&csrf=<sec:authentication property='details.csrf'/>'><i class='ace-icon fa fa-trash-o bigger-120'></i></a>";
                    }
                }
            ],
            //add product id to tr.id
            "rowCallback": function( row, data ) {
                $(row).attr('product_id',data.id);
            }
        } );

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
    })

//    var scripts = [null,"/admin/assets/admin_wpt/js/jquery.dataTables.min.js", null]
//    $('.page-content-area').ace_ajax('loadScripts', scripts, function() {
//        //inline scripts related to this page
//        $(document).ready(function() {
//            $('#products-table').dataTable( {
//                "processing": true,
//                "serverSide": true,
////            "ajax": "/admin/catalog/search_product.html",
//                "ajax": {
//                    "url": "/admin/catalog/search_product.html",
//                    "type": "POST"
//                },
//                "columns": [
//                    { "data": "name" },
//                    { "data": "model" }
//                ]
//            } );
//        });
//    });
</script>
