<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="rowId" value="${param.rowId}"/>

<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-sm-12">
                <input type="text" placeholder="Tìm Kiếm Sản Phẩm ..." class="col-xs-10 col-sm-5" id="search-data" autocomplete="off"> <a href="#/admin/design/index" id="search-product" class="btn btn-sm btn-success"><i class='ace-icon fa fa-search'></i></a>
            </div>
        </div>
        <hr>
        <div  id="modal_message_alert"></div>
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
<script type="text/javascript">
    $('#search-data').keypress(function(e){
        if (e.keyCode == 13 || e.which == 13) {
            $('#search-product').click();
        }
    });

    <%--TODO: Search and exclude the products were selected--%>
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
                        return "<input class='selectProduct-toggle-checkbox' type='checkbox' name='selectProduct' value='"+data.id+"' data-src='/admin/design/select_product.html?rowId=${rowId}&productId="+data.id+"&csrf=<sec:authentication property='details.csrf'/>' data-header='<fmt:message key='common.confirm.title'/>' lang='<fmt:message key='common.doyouwantaddthisproduct'/>' confirm-success='<fmt:message key='common.data.saved.success'/>' confirm-fail='<fmt:message key='common.data.saved.fail'/>'>";
                    }
                }
            ],
            //add product id to tr.id
            "rowCallback": function( row, data ) {
                $(row).attr('product_id',data.id);
            }
        } );

//        $(".selectProduct-toggle-checkbox").on("click", function() {
        $("#products-table").delegate(".selectProduct-toggle-checkbox", "click", function(){
            var object = $(this);
            var flag = object.prop( "checked" );
            BootstrapDialog.show({
                type:BootstrapDialog.TYPE_DANGER,
                closeByBackdrop: false,
                closeByKeyboard: false,
                title:object.attr("data-header"),
                message:object.attr("lang"),
                buttons:[
                    {   label:'Yes',
                        action:function (dialog) {
                            $.get(object.attr("data-src"), { flag: flag}, // some params to pass it
                                    function(data) {
                                        if ("ok" == data) {
                                            BootstrapDialog.show({
                                                type:BootstrapDialog.TYPE_INFO,
                                                message:object.attr("confirm-success")
                                            })
                                        } else {
                                            BootstrapDialog.show({
                                                type:BootstrapDialog.TYPE_DANGER,
                                                message:object.attr("confirm-fail")
                                            })
                                            object.prop("checked", !flag)
                                        }
                                        //This code will refresh the current tab after this model is closed.
                                        $("#modal_message_alert").html("<span>&nbsp;</span>");
                                        $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                            $("#modal_message_alert").alert('close');
                                        });

                                    });
                            dialog.close();
                        }
                    },
                    {
                        label:'No',
                        action:function (dialog) {
                            object.prop("checked", !flag);
                            dialog.close();
                        }
                    }
                ]
            });

        });

//        $("table#products-table").delegate("a.show-confirm", "click", function(){
//            var object = $(this);
//            BootstrapDialog.show({
//                type:BootstrapDialog.TYPE_DANGER,
//                closeByBackdrop: false,
//                closeByKeyboard: false,
//                title:'Xác Nhận',
//                message:object.attr("lang"),
//                buttons:[
//                    {
//                        label:'Yes',
//                        action:function (dialog) {
//                            $.ajax({
//                                type: "GET",
//                                url: object.attr("hreflang"),
//                                data: $("#form").serialize(), // serializes the form's elements.
//                                success: function(data)
//                                {
//                                    object.closest('tr').remove();
//                                    $("#modal_message_alert").html(data);
//                                    $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
//                                        $("#modal_message_alert").alert('close');
//                                    });
//                                }
//                            });
//                            dialog.close();
//                        }
//                    },
//                    {
//                        label:'No',
//                        action:function (dialog) {
//                            dialog.close();
//                        }
//                    }
//                ]
//            });
//        });
    })
</script>
