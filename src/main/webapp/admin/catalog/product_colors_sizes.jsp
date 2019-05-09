<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>
<c:set var="productId" value="${param.id}"/>
<c:if test="${! empty productId}">
    <spring:eval expression="serviceLocator.productVariantDao.findBy('product.id', T(java.lang.Long).valueOf(productId))" var="productVariants"/>
</c:if>

<script>
    $("#product_variant").validate({
        rules:{
            productPrice:{
                required:true
            },
            inventory:{
                required:true
            }
        },

        messages:{
            productPrice:{
                required:"<fmt:message key="product.price.is.required"/>"
            },
            inventory:{
                required:"<fmt:message key="product.inventory.is.required"/>"
            }
        },
        highlight:function (label) {
            $(label).closest('.form-group').removeClass('success');
            $(label).closest('.form-group').addClass('error');
        },
        success:function (label) {
            $(label).closest('.form-group').removeClass('error');
            $(label).closest('.form-group').addClass('success');
        }
    });
    $("#product_variant").on("click", function() {
        var form = $( "form[name=product_variant]" );
        if (form.valid()) {
            $.ajax({
                type: "POST",
                url: '/admin/catalog/productvariant_update.html',
                data: form.serialize(), // serializes the form's elements.
                success: function(data)
                {
//                        document.location.href="#/admin/catalog/product?id="+data.id;
                    //Reload the current tab
                    var $link = $('li.active a[data-toggle="tab"]');
                    $link.parent().removeClass('active');
                    var tabLink = $link.attr('href');
                    $('#form-tab a[href="' + tabLink + '"]').tab('show');
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
            return false; // avoid to execute the actual submit of the form.
        }
    });

    $(".product_variant_edit").on("click", function(){
        $.ajax({
            type: 'GET',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            url: '/admin/catalog/getproductvariant.html',
            data: { id:$(this).attr("id") },
            success: function (result) {
                //Render html from data return and append to the current page
                var temp = $("#product_variant_mustache").html();
                //Replace data from result to template
                var productVariantForm = Mustache.render(temp, result);
                //add selected into the option that match color
                var tempHtml = $(productVariantForm).find("option[value='"+result.colorCode+"']").attr("selected", true).end();
                //add selected into the option that match size
                var tempHtml1 = $(tempHtml).find("option[value='"+result.sizeCode+"']").attr("selected", true).end();
                $("#product_variant_form").html(tempHtml1);
            },
            error: function (result) {
            }
        });
    });
    $("#add_variant").on("click", function(){
        result = {
            "id":"",
            "productPrice":"",
            "sku":"",
            "colorCode":"",
            "sizeCode":"",
            "inventory":""
        };
        //Render html from data return and append to the current page
        var temp = $("#product_variant_mustache").html();
        //Replace data from result to template
        var productVariantForm = Mustache.render(temp, result);
        $("#product_variant_form").html(productVariantForm);
    })

</script>

<form name="product_variant" class="form-horizontal" action="#" method="post">
    <h:csrf/>
    <input name="productId" type="hidden" value="${productId}"/>
    <div class="page-header">
    <a class="btn btn-xs btn-info" id="product_variant">
        <i class="ace-icon fa fa-check"></i> <fmt:message key="common.save.changes"/>
    </a>
    <a class="btn btn-xs btn-info" id="add_variant">
        <i class="fa fa-plus"></i> <fmt:message key="product.variant.add"/>
    </a>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div  id="message_alert"></div>

        <div class="row">
    <div class="col-xs-12" id="product_variant_form">
    <c:if test="${fn:length(productVariants) > 0}">
        <c:set var="productVariant" value="${productVariants[0]}"/>
    </c:if>
        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(productVariant.price/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).getMoneyValue()" var="productPrice"/>
        <input name="id" type="hidden" value="${productVariant.id}"/>
        <fieldset>
            <div class="row">
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding-right" for="productPrice"><fmt:message key="product.price"/>*</label>
                        <div class="col-sm-8 forms">
                            <spring:eval expression="site.siteParamsMap.get('CURRENCY')" var="currency"/>
                            <c:if test="${productVariant.price <= 0}">
                                <c:set var="productPrice" value=""/>
                            </c:if>
                            <input name="productPrice" class="required" id="productPrice" type="text" maxlength="15" value="${productPrice}"/> ${currency}
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding-right" for="inventory"><fmt:message key="product.inventory"/>*</label>
                        <div class="col-sm-8 forms">
                            <c:set var="productInventory" value="${productVariant.inventory}"/>
                            <c:if test="${productVariant.inventory <= 0}">
                                <c:set var="productInventory" value=""/>
                            </c:if>
                            <input name="inventory" class="required number" id="inventory" type="text" maxlength="15" value="${productInventory}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding-right" for="sku"><fmt:message key="product.sku"/></label>
                        <div class="col-sm-8 forms">
                            <input name="sku" class="" id="sku" type="text" maxlength="15" value="${productVariant.sku}"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding-right" for="colorCode"><fmt:message key="product.color"/></label>
                        <div class="col-sm-8 forms">
                            <td><h:stringparamselector name="colorCode" stringParam="PRODUCT_COLOR" defaultValue="${productVariant.colorCode}" includeTitle="Chọn Màu Sắc"/></td>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding-right" for="colorCode"><fmt:message key="product.size"/></label>
                        <div class="col-sm-8 forms">
                            <td><h:stringparamselector name="sizeCode" stringParam="PRODUCT_SIZE" defaultValue="${productVariant.sizeCode}" includeTitle="Chọn Kích Thước"/></td>
                        </div>
                    </div>
                </div>
            </div>


        </fieldset>
    </div>
    </div>
        <div class="row">
            <div class="col-xs-12">
            <c:if test="${fn:length(productVariants) > 1}">
    <table id="simple-table" class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th><fmt:message key="product.color"/></th>
        <th><fmt:message key="product.size"/></th>
        <th><fmt:message key="product.price"/></th>
        <th><fmt:message key="product.inventory"/></th>
        <th><fmt:message key="product.sku"/></th>
        <th></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach items="${productVariants}" varStatus="productVariant">
    <tr>
        <td>
            <c:out value="${productVariant.current.colorName}"/><br>
        </td>
        <td>
            <c:out value="${productVariant.current.sizeName}"/><br>
        </td>
        <td>
            <c:out value="${productVariant.current.price}"/><br>
        </td>
        <td>
            <c:out value="${productVariant.current.inventory}"/><br>
        </td>
        <td>
            <c:out value="${productVariant.current.sku}"/><br>
        </td>
        <td>
                           <div class="hidden-sm hidden-xs btn-group">
                               <a class="btn btn-xs btn-info product_variant_edit" title="<fmt:message key="common.edit"/>" id="${productVariant.current.id}">
                                   <i class="ace-icon fa fa-pencil bigger-120"></i>
                               </a>

                               <a class="btn btn-xs btn-danger global_show-confirm" title="<fmt:message key="common.delete"/>" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>' hreflang="/admin/catalog/deleteproductvariant.html?id=${productVariant.current.id}&productId=${productId}&csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>">
                                   <i class="ace-icon fa fa-trash-o bigger-120"></i>
                               </a>
                           </div>
                       </td>
                   </tr>
                   </c:forEach>
                   </tbody>
                   </table>
</c:if>
<c:if test="${empty productVariants}">
    <fmt:message key="common.is.empty"/>
</c:if>
    </div><!-- /.span -->
</div><!-- /.row -->
</div>
</div>
</form>
<script type="text/x-mustache" id="product_variant_mustache">
    <input name="id" type="hidden" value="{{id}}"/>
    <fieldset>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right" for="productPrice"><fmt:message key="product.price"/>*</label>
                    <div class="col-sm-8 forms">
                        <spring:eval expression="site.siteParamsMap.get('CURRENCY')" var="currency"/>
                        <input name="productPrice" class="required" id="productPrice" type="text" maxlength="15" value="{{price/100.0}}"/> ${currency}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right" for="inventory"><fmt:message key="product.inventory"/>*</label>
                    <div class="col-sm-8 forms">
                        <input name="inventory" class="required" id="inventory" type="text" maxlength="15" value="{{inventory}}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right" for="sku"><fmt:message key="product.sku"/></label>
                    <div class="col-sm-8 forms">
                        <input name="sku" class="" id="sku" type="text" maxlength="15" value="{{sku}}"/>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right" for="colorCode"><fmt:message key="product.color"/></label>
                    <div class="col-sm-8 forms">
                        <td><h:stringparamselector name="colorCode" stringParam="PRODUCT_COLOR" defaultValue="" includeTitle="Chọn Màu Sắc"/></td>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right" for="colorCode"><fmt:message key="product.size"/></label>
                    <div class="col-sm-8 forms">
                        <td><h:stringparamselector name="sizeCode" stringParam="PRODUCT_SIZE" defaultValue="" includeTitle="Chọn Kích Thước"/></td>
                    </div>
                </div>
            </div>
        </div>


    </fieldset>
</script>

<%--<h:form_modal/>--%>
