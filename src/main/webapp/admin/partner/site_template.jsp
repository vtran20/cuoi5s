<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<div class="page-header">
<a class="btn btn-xs btn-info" data-title="<fmt:message key="site.template.add"/>" href="#modal-form" role="button" data-toggle="modal" data-page="/admin/partner/site_template_form.html" data-target="#modal-form">
<i class="fa fa-plus"></i> <fmt:message key="site.template.add"/>
</a>
</div><!-- /.page-header -->
<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-sm-12">
                <input type="text" placeholder="Nhập tên miền phụ ..." class="col-xs-10 col-sm-5" id="search-data" autocomplete="off" maxlength="50"> <a href="#/admin/partner/site_template" id="search-site-template" class="btn btn-sm btn-success"><i class='ace-icon fa fa-search'></i></a>
            </div>
        </div>
        <hr>
        <div  id="message_alert"></div>
        <div class="row">
            <div class="col-xs-12">
                <div>
                    <%--Table--%>
                    <table id="site-template-table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Domain</th>
                            <th>Expire Date</th>
                            <th>Action</th>
                        </tr>
                        </thead>

                        <tfoot>
                        <tr>
                            <th>Name</th>
                            <th>Domain</th>
                            <th>Expire date</th>
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
<h:image_modal/>
<script type="text/javascript">
    $('#search-data').keypress(function(e){
        if (e.keyCode == 13 || e.which == 13) {
            $('#search-site-template').click();
        }
    });

    $("#search-site-template").on("click", function() {
        table = $('#site-template-table').dataTable( {
            "destroy": true,
            "processing": true,
            "serverSide": false,
            "ajax": {
                "url": "/admin/partner/search_site_template.html",
                "type": "GET",
                "data" : function ( d ) {
                    d.keyword = $("#search-data").val();
                }
            },
            "columns": [
                { "data": "name" },
                { "data": "subDomain"},
                { "data": "templateModel" },
                {
                    "class":          "details-control",
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": "",
                    render: function ( data, type, row ) {
                        // Combine the first and last names into a single table field
                        return "<a class='btn btn-xs btn-info' data-page='/admin/partner/site_template_form.html?id="+data.id+"' data-title='<fmt:message key='common.edit'/>' data-target='#modal-form' data-toggle='modal'><i class='ace-icon fa fa-pencil bigger-120'></i></a>" + " " +
                                "<a class='btn btn-xs btn-danger show-confirm' data-url='/admin/partner/site_template' title='<fmt:message key='common.delete'/>' lang='<fmt:message key='product.do.you.want.delete.this.template'/>' hreflang='/admin/partner/deletesitetemplate.html?id="+data.id+"&csrf=<sec:authentication property='details.csrf'/>'><i class='ace-icon fa fa-trash-o bigger-120'></i></a>";
//                        return "<a class='btn btn-xs btn-info' href='#/admin/partner/site_template_form.html?id="+data.id+"' data-url='/admin/partner/site_'><i class='ace-icon fa fa-user-secret bigger-120'></i> Admin</a>";
//                        return "<a class='blue' data-page='/admin/order/orderreview.html?id="+data.id+"' data-target='#modal-form' data-toggle='modal'><i class='ace-icon fa fa-user-secret bigger-120'></i></a>";
                    }
                }
            ],
            //add product id to tr.id
            "rowCallback": function( row, data ) {
                $(row).attr('site_template_id',data.id);
            }
        } );

        $("table#site-template-table").delegate("a.show-confirm", "click", function(){
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
</script>
