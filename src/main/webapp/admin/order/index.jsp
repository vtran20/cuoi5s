<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>
<%--<script src="/admin/assets/admin_wpt/js/date-time/moment.min.js" type="text/javascript"></script>--%>
<%--<script src="/admin/assets/admin_wpt/js/date-time/daterangepicker.min.js" type="text/javascript"></script>--%>

<spring:eval expression="site.siteParamsMap.get('DATE_FORMAT')" var="dateFormat"/>
<div class="page-header">
    <h4><fmt:message key="order.order.list"/></h4>
</div><!-- /.page-header -->
<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-sm-2">
                <div class="checkbox">
                    <label>
                        <input name="orderStatus" type="checkbox" class="ace" value="NEW_ORDER" checked/><span class="lbl">&nbsp;<fmt:message key="order.order.status.neworder"/></span>
                    </label>
                </div>
            </div>
            <div class="col-sm-2">
                <div class="checkbox">
                    <label>
                        <input name="orderStatus" type="checkbox" class="ace" value="PAID" checked/><span class="lbl">&nbsp;<fmt:message key="order.order.status.paidorder"/></span>
                    </label>
                </div>
            </div>
            <div class="col-sm-2">
                <div class="checkbox">
                    <label>
                        <input name="orderStatus" type="checkbox" class="ace" value="SHIPPING" checked/><span class="lbl">&nbsp;<fmt:message key="order.order.status.shippingorder"/></span>
                    </label>
                </div>
            </div>
            <div class="col-sm-2">
                <div class="checkbox">
                    <label>
                        <input name="orderStatus" type="checkbox" class="ace" value="SHIPPED"><span class="lbl">&nbsp;<fmt:message key="order.order.status.shippedorder"/></span>
                    </label>
                </div>
            </div>
            <div class="col-sm-2">
                <div class="checkbox">
                    <label>
                        <input name="orderStatus" type="checkbox" class="ace" value="CANCELLED"><span class="lbl">&nbsp;<fmt:message key="order.order.status.cancelledorder"/></span>
                    </label>
                </div>
            </div>
            <div class="col-sm-2">
                <div class="checkbox">
                    <label>
                        <input name="orderStatus" type="checkbox" class="ace" value="RETURNED"><span class="lbl">&nbsp;<fmt:message key="order.order.status.returnedorder"/></span>
                    </label>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-5">
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right" for="city"><fmt:message key="order.order.shipping.to"/></label>
                    <div class="col-sm-8">
                        <h:stringparamselector name="city" stringParam="CITY" defaultValue="${orderFilter.city}" includeTitle="Chọn Thành Phố"/>
                    </div>
                </div>
            </div>
            <div class="col-sm-5">
                <div class="form-group">
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding-right" for="city"><fmt:message key="order.order.date"/></label>
                        <div class="col-sm-8">
                            <div id="reportrange" class="pull-left" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc">
                                <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                                <span></span> <b class="caret"></b>
                                <input type="hidden" name="startDate" id="startDate"/>
                                <input type="hidden" name="endDate" id="endDate"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-2">
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <a href="#/admin/order/index" id="search-order" class="btn btn-sm btn-success"><i class='ace-icon fa fa-search'> <fmt:message key="order.order.search"/></i></a>
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
                            <th><fmt:message key="order.order.id"/></th>
                            <th><fmt:message key="order.order.billing.address"/></th>
                            <th><fmt:message key="order.order.shipping.address"/></th>
                            <th><fmt:message key="order.order.status"/></th>
                            <th><fmt:message key="order.order.date"/></th>
                            <th><fmt:message key="order.order.total.price"/></th>
                            <th><fmt:message key="order.order.payment.provider"/></th>
                        </tr>
                        </thead>

                        <tfoot>
                        <tr>
                            <th><fmt:message key="order.order.id"/></th>
                            <th><fmt:message key="order.order.billing.address"/></th>
                            <th><fmt:message key="order.order.shipping.address"/></th>
                            <th><fmt:message key="order.order.status"/></th>
                            <th><fmt:message key="order.order.date"/></th>
                            <th><fmt:message key="order.order.total.price"/></th>
                            <th><fmt:message key="order.order.payment.provider"/></th>
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

    var scripts = [null,"/admin/assets/admin_wpt/js/jquery.colorbox.js", null]
    $('.page-content-area').ace_ajax('loadScripts', scripts, function() {
        //inline scripts related to this page
        jQuery(function($) {
            var $overflow = '';
            var colorbox_params = {
                rel: 'colorbox',
                reposition:true,
                scalePhotos:true,
                scrolling:false,
                previous:'<i class="ace-icon fa fa-arrow-left"></i>',
                next:'<i class="ace-icon fa fa-arrow-right"></i>',
                close:'&times;',
                current:'{current} of {total}',
                maxWidth:'100%',
                maxHeight:'100%',
                onOpen:function(){
                    $overflow = document.body.style.overflow;
                    document.body.style.overflow = 'hidden';
                },
                onClosed:function(){
                    document.body.style.overflow = $overflow;
                },
                onComplete:function(){
                    $.colorbox.resize();
                }
            };

            $('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
            $("#cboxLoadingGraphic").html("<i class='ace-icon fa fa-spinner orange fa-spin'></i>");//let's add a custom loading icon


            $(document).one('ajaxloadstart.page', function(e) {
                $('#colorbox, #cboxOverlay').remove();
            });
        })
    });


    //Order Status
    var orderStatus = new Object(); // or var map = {};
    <spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValues('ORDER_STATUS', serviceLocator.locale)" var="stringParamValues"/>
    <c:forEach var="orderStatus" items="${stringParamValues}">
    orderStatus['${orderStatus.key}'] = '${orderStatus.value}';
    </c:forEach>

    //Payment Provider
    var paymentProvider = new Object(); // or var map = {};
    <spring:eval expression="serviceLocator.getPaymentProviderDao().findAll()" var="providers"/>
    <c:forEach var="provider" items="${providers}">
    paymentProvider['${provider.id}'] = '${provider.name}';
    </c:forEach>

    $('#search-data').keypress(function(e){
        if (e.keyCode == 13 || e.which == 13) {
            $('#search-order').click();
        }
    });

    var status = [];
    $("#search-order").on("click", function() {
//        $("input[name=orderStatus]:checked").each(function() {
//            status[i] = $(this).val();
//        });
        table = $('#products-table').dataTable( {
            "destroy": true,
            "processing": true,
            "serverSide": false,
            "ajax": {
                "url": "/admin/order/findorders.html?"+$('input[name=orderStatus]:checked').serialize(),
                "type": "GET",
                "data" : function ( d ) {
                    d.city = $("select option:selected").val();
                    d.startDate = $("#startDate").val();
                    d.endDate = $("#endDate").val();
                }
            },
            "columns": [
                {
                    "class":          "details-control",
                    "orderable":      true,
                    "data":           null,
                    "defaultContent": "",
                    render: function ( data, type, row ) {
                        // Combine the first and last names into a single table field
                        return "<a class='blue' data-page='/admin/order/orderreview.html?id="+data.id+"' data-target='#modal-form' data-toggle='modal'>"+data.id+"</a>";
                    }
                },
                {
                    "class":          "details-control",
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": "",
                    render: function ( data, type, row ) {
                        // Combine the first and last names into a single table field
                        return data.lastName+"&nbsp;"+data.firstName+"<br>"+data.address_1+",&nbsp;"+data.city+"<br>"+data.email+",&nbsp;P: "+data.phone;
                    }
                },
                {
                    "class":          "details-control",
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": "",
                    render: function ( data, type, row ) {
                        // Combine the first and last names into a single table field
                        return data.lastNameShipping+"&nbsp;"+data.firstNameShipping+"<br>"+data.address_1Shipping+",&nbsp;"+data.cityShipping+", &nbsp;P: "+data.phoneShipping;
                    }
                },
                {
                    "class":          "details-control",
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": "",
                    render: function ( data, type, row ) {
                        // Combine the first and last names into a single table field
                        return orderStatus[data.status];
                    }
                },
                <%--TODO: Use date format from site--%>
                {
                    "class":          "details-control",
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": "",
                    render: function ( data, type, row ) {
                        var date = new Date(data.createdDate);
                        var month = date.getMonth() + 1;
                        return date.getDate() + "/" + month + "/" + date.getFullYear();
                    }
                },
                { "data": "totalPrice"},
                {
                    "class":          "details-control",
                        "orderable":      false,
                        "data":           null,
                        "defaultContent": "",
                        render: function ( data, type, row ) {
                            // Combine the first and last names into a single table field
//                            alert (data.provider.paymentProvider);
//                            alert (data.paymentProvider);
                            if (data.provider != null) {
                                if (data.provider.paymentProvider != null) {
                                    return data.provider.paymentProvider.name;
                                } else {
                                    return "";
                                }
                            } else {
                                return "";
                            }

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

    $(function() {
        //$('input[name="daterange"]').daterangepicker();
        $('#reportrange span').html(moment().subtract(6, 'days').format('${fn:toUpperCase(dateFormat)}') + ' - ' + moment().format('${fn:toUpperCase(dateFormat)}'));
        //initial start and end date
        $('#startDate').val(moment().subtract(6, 'days').format('${fn:toUpperCase(dateFormat)}'));
        $('#endDate').val(moment().format('${fn:toUpperCase(dateFormat)}'));
        $('#reportrange').daterangepicker({
            format: '${fn:toUpperCase(dateFormat)}',
            startDate: moment().subtract(6, 'days'),
            endDate: moment(),
//            minDate: '01/01/2012',
//            maxDate: '12/31/2055',
            dateLimit: { days: 180 },
            showDropdowns: true,
            showWeekNumbers: true,
            timePicker: false,
            timePickerIncrement: 1,
            timePicker12Hour: true,
            ranges: {
                'Today': [moment(), moment()],
                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            },
            opens: 'left',
            drops: 'down',
            buttonClasses: ['btn', 'btn-sm'],
            applyClass: 'btn-primary',
            cancelClass: 'btn-default',
            separator: ' to ',
            locale: {
                applyLabel: 'Submit',
                cancelLabel: 'Cancel',
                fromLabel: 'From',
                toLabel: 'To',
                customRangeLabel: 'Custom',
                daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
                monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
                firstDay: 1
            }
        }, function(start, end, label) {
            $('#startDate').val(start.format('${fn:toUpperCase(dateFormat)}'));
            $('#endDate').val(end.format('${fn:toUpperCase(dateFormat)}'));
            $('#reportrange span').html(start.format('${fn:toUpperCase(dateFormat)}') + ' - ' + end.format('${fn:toUpperCase(dateFormat)}'));
        });
    });
</script>

