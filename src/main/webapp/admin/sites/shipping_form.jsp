<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<div class="row">
    <div class="col-xs-12">
        <div  id="message_alert"></div>
        <div class="col-sm-6">
            <h4 class="header">Phương Thức Vận Chuyển</h4>
            <spring:eval expression="serviceLocator.shippingSiteDao.findUniqueBy(null, null, site.id)" var="shippingSite"/>
            <p>Chú ý: Nếu miễn phí vận chuyển. Bạn Nên Chọn "Phí Vận Chuyển Cố Định" và nhập giá vận chuyển bằng 0 VND</p>
            <form name="form" id="form" class="form-horizontal" action="#" method="post">
                <input name="id" type="hidden" value="${shippingSite.id}"/>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="name">Tên Hiển Thị</label>
                    <div class="col-sm-9">
                        <input name="name" class="input-xlarge required" id="name" type="text" value="${shippingSite.name}" maxlength="255"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="description"><fmt:message key="site.form.description"/></label>
                    <div class="col-sm-9">
                        <textarea name="description" class="input-xlarge" id="description" rows="3">${shippingSite.description}</textarea>
                        <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="Ví dụ: Thời gian vận chuyển, ..." title="">?</span>
                    </div>
                </div>
                <c:if test="${shippingSite.usePriceBySite == 'Y'}">
                    <c:set var="usePriceBySiteChecked" value="checked"/>
                </c:if>
                <c:if test="${shippingSite.usePriceByProduct == 'Y'}">
                    <c:set var="usePriceByProductChecked" value="checked"/>
                </c:if>
                <c:if test="${usePriceBySiteChecked != 'checked' && usePriceByProductChecked != 'checked'}">
                    <c:set var="usePriceBySiteChecked" value="checked"/>
                </c:if>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right">
                    </label>
                    <div class="col-sm-9">
                        <div class="widget-box">
                            <div class="widget-header">
                                <h5 class="widget-title">
                                    <input name="typeShippingFee" id="usePriceBySite" type="radio" class="ace" value="1" ${usePriceBySiteChecked}/><label class="lbl" for="usePriceBySite"> Phí Vận Chuyển Cố Định</label>
                                    <input name="usePriceBySite" type="hidden" value="${shippingSite.usePriceBySite}"/>
                                </h5>
                            </div>
                            <div class="widget-body" id="usePriceBySiteBody">
                                <div class="widget-main">
                                    <div class="control-group">
                                        <div class="form-group">
                                            <label class="col-sm-5 control-label no-padding-right" for="priceBySite">Giá Tiền</label>
                                            <div class="col-sm-6">
                                                <input name="priceBySite" class="input-xlarge" id="priceBySite" type="text" value="${shippingSite.priceBySite != null?shippingSite.priceBySite:0}" maxlength="20"/>
                                            </div>
                                            <div class="col-sm-1">
                                                VND
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right">
                    </label>
                    <div class="col-sm-9">
                        <div class="widget-box">
                            <div class="widget-header">
                                <h5 class="widget-title">
                                    <input name="typeShippingFee" id="usePriceByProduct" type="radio" class="ace" value="2" ${usePriceByProductChecked}/><label class="lbl" for="usePriceByProduct"> Phí Vận Chuyển Theo Sản Phẩm</label>
                                    <input name="usePriceByProduct" type="hidden" value="${shippingSite.usePriceByProduct}"/>
                                </h5>
                            </div>
                            <div class="widget-body" id="usePriceByProductBody">
                                <div class="widget-main">
                                    <div class="control-group">
                                        <div class="form-group">
                                            <label class="col-sm-5 control-label no-padding-right" for="priceByProduct">Giá Tiền Sản Phẩm Đầu Tiên</label>
                                            <div class="col-sm-6">
                                                <input name="priceByProduct" class="input-xlarge" id="priceByProduct" type="text" value="${shippingSite.priceByProduct != null? shippingSite.priceByProduct:0}" maxlength="20"/>
                                            </div>
                                            <div class="col-sm-1">
                                                VND
                                            </div>
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <div class="form-group">
                                            <label class="col-sm-5 control-label no-padding-right" for="percentOfFirstProduct">Phần Trăm Theo Sản Phẩm Đầu Tiên (0 đến 100%)</label>
                                            <div class="col-sm-6">
                                                <input name="percentOfFirstProduct" class="input-xlarge" id="percentOfFirstProduct" type="text" value="${shippingSite.percentOfFirstProduct != null? shippingSite.percentOfFirstProduct:0}" maxlength="20"/>
                                            </div>
                                            <div class="col-sm-1">
                                                %<span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="Ví dụ: Nếu giá trị phần trăm là 50, giá vận chuyển của sản phẩm đầu tiên là 20000, vậy giá vận chuyển của sản phẩm thứ 2 sẽ là 10000 và tổng phí vận chuyển là 30000 VND" title="">?</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-sm btn-primary pull-left" type="submit">
                        <i class="ace-icon fa fa-check"></i>
                        <fmt:message key="common.save.changes"/>
                    </button>
                </div>
            </form>
        </div>
        <div class="col-sm-6">
            <h4 class="header">Giảm Giá Phí Vận Chuyển</h4>
                <div class="form-group">
                    <div class="col-sm-12">
                        <p>Click vào check box để chọn các chương trình giảm giá dưới đây.</p>
                        <spring:eval expression="serviceLocator.promotionDao.findUniqueBy('promoCode', 'FREESHIPPINGINNER', site.id)" var="freeShippingInner"/>
                        <form name="formdiscount1" id="formdiscount1" class="form-horizontal" action="#" method="post">
                            <input name="id" type="hidden" value="${freeShippingInner.id}"/>
                            <input name="promoCode" type="hidden" value="FREESHIPPINGINNER"/>
                            <input name="promoParam1" type="hidden" value="FREE"/>
                            <c:choose>
                                <c:when test="${! empty freeShippingInner && freeShippingInner.active == 'Y'}">
                                    <c:set var="discount1" value="checked"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="discount1" value=""/>
                                </c:otherwise>
                            </c:choose>
                            <div class="widget-box">
                                <div class="widget-header">
                                    <h5 class="widget-title">
                                        <input name="active" id="discountShippingFee1" type="checkbox" class="ace" ${discount1}/><label class="lbl" for="discountShippingFee1"> Miễn Phí Phí Vận Chuyển Nội Thành</label>
                                    </h5>
                                </div>
                                <div class="widget-body">
                                    <div class="widget-main">
                                        <div class="control-group">
                                            <div class="form-group">
                                                <label class="col-sm-5 control-label no-padding-right" for="name1">Mô Tả Giảm Giá</label>
                                                <div class="col-sm-7">
                                                    <input name="name" class="input-xxlarge required" id="name1" type="text" value="${freeShippingInner.name}" maxlength="255"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-5 control-label no-padding-right" for="priceBySite">Chọn Thành Phố</label>
                                                <div class="col-sm-7">
                                                    <h:stringparamselector name="city" styleClass="required" stringParam="CITY" defaultValue="${freeShippingInner.promoParam2}" isMultiSelect="Y"/>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button class="btn btn-sm btn-primary pull-left" type="submit">
                                                    <i class="ace-icon fa fa-check"></i>
                                                    <fmt:message key="common.save.changes"/>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-12">
                        <spring:eval expression="serviceLocator.promotionDao.findUniqueBy('promoCode', 'FREESHIPPINGGREATER', site.id)" var="freeShippingGreater"/>
                        <form name="formdiscount2" id="formdiscount2" class="form-horizontal" action="#" method="post">
                            <input name="id" type="hidden" value="${freeShippingGreater.id}"/>
                            <input name="promoCode" type="hidden" value="FREESHIPPINGGREATER"/>
                            <input name="promoParam1" type="hidden" value="FREE"/>

                            <c:choose>
                                <c:when test="${! empty freeShippingGreater && freeShippingGreater.active == 'Y'}">
                                    <c:set var="discount2" value="checked"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="discount2" value=""/>
                                </c:otherwise>
                            </c:choose>
                            <div class="widget-box">
                                <div class="widget-header">
                                    <h5 class="widget-title">
                                        <input name="active" id="discountShippingFee2" type="checkbox" class="ace" ${discount2}/><label class="lbl" for="discountShippingFee2"> Miễn Phí Nếu Tổng Tiền Trong Đơn Đặt Hàng Lớn Hơn Hoặc Bằng</label>
                                    </h5>
                                </div>
                                <div class="widget-body">
                                    <div class="widget-main">
                                        <div class="control-group">
                                            <div class="form-group">
                                                <label class="col-sm-5 control-label no-padding-right" for="name2">Mô Tả Giảm Giá</label>
                                                <div class="col-sm-7">
                                                    <input name="name" class="input-xxlarge required" id="name2" type="text" value="${freeShippingGreater.name}" maxlength="255"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-5 control-label no-padding-right" for="promoParam2">Giá Tiền Tổng Đơn Đặt Hàng</label>
                                                <div class="col-sm-7">
                                                    <input name="promoParam2" class="input-xxlarge required" id="promoParam2" type="text" value="${freeShippingGreater.promoParam2}" maxlength="20"/>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button class="btn btn-sm btn-primary pull-left" type="submit">
                                                    <i class="ace-icon fa fa-check"></i>
                                                    <fmt:message key="common.save.changes"/>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
        </div>
    </div>
</div>

<script>
    $('[data-rel=popover]').popover({container:'body'});

    // this is the id of the form
    $("#form").submit(function() {
        var form = $( "#form" );
        if (form.valid()) {
            var url = "/admin/sites/update_shipping_setting.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
        }
        return false; // avoid to execute the actual submit of the form.
    });
    $("#formdiscount1").submit(function() {
        var form = $( "#formdiscount1" );
        if (form.valid()) {
            var url = "/admin/sites/update_discount_shipping.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
//                contentType: "text/html;charset=utf-8",
                data: $("#formdiscount1").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
        }
        return false; // avoid to execute the actual submit of the form.
    });
    $("#formdiscount2").submit(function() {
        var form = $( "#formdiscount2" );
        if (form.valid()) {
            var url = "/admin/sites/update_discount_shipping.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                contentType: "text/html;charset=utf-8",
                data: $("#formdiscount2").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
        }
        return false; // avoid to execute the actual submit of the form.
    });
    //Shipping Fee
    function selectPriceBySite() {
        $("#usePriceBySiteBody").show();
        $("#usePriceByProductBody").hide();
        $("input[name=priceBySite]").addClass('required');
        $("input[name=priceByProduct]").removeClass('required');
        $("input[name=percentOfFirstProduct]").removeClass('required');
        $("input[name=usePriceBySite]").val('Y');
        $("input[name=usePriceByProduct]").val('N');
    }
    function selectPriceByProduct() {
        $("#usePriceBySiteBody").hide();
        $("#usePriceByProductBody").show();
        $("input[name=priceBySite]").removeClass('required');
        $("input[name=priceByProduct]").addClass('required');
        $("input[name=percentOfFirstProduct]").addClass('required');
        $("input[name=usePriceBySite]").val('N');
        $("input[name=usePriceByProduct]").val('Y');
    }

    $("input[id=usePriceBySite]").click(function() {
        selectPriceBySite();
    });
    $("input[id=usePriceByProduct]").click(function() {
        selectPriceByProduct();
    });

    $("input[id=usePriceBySite]").each(function() {
        if ($(this).is(":checked")) {
            selectPriceBySite();
        }
    });
    $("input[id=usePriceByProduct]").each(function() {
        if ($(this).is(":checked")) {
            selectPriceByProduct();
        }
    });

    $("#form").validate({
//                messages: {
//                    firstNameForBilling:  "Vui lòng nhập Tên",
//                    lastNameForBilling:  "Vui lòng nhập Họ",
//                    address1ForBilling:  "Vui lòng nhập Địa chỉ",
//                    cityForBilling:  "Vui lòng nhập Thành phố/Tỉnh",
//                    phoneForBilling:  "Vui lòng nhập Số Điện thoại",
//                    emailAddressForBilling:  {
//                        required: "Vui lòng nhập Email",
//                        email: "Email không chính xác"
//                    },
//                    firstNameForShipping:  "Vui lòng nhập Tên",
//                    lastNameForShipping:  "Vui lòng nhập Họ",
//                    address1ForShipping:  "Vui lòng nhập Địa chỉ",
//                    cityForShipping:  "Vui lòng nhập Thành phố/Tỉnh",
//                    phoneForShipping:  "Vui lòng nhập Số Điện thoại"
//                },
                highlight:function (label) {
                    $(label).closest('.form-group').removeClass('success');
                    $(label).closest('.form-group').addClass('error');
                },
                success:function (label) {
                    $(label).closest('.form-group').removeClass('error');
                    $(label).closest('.form-group').addClass('success');
                }

            });

</script>