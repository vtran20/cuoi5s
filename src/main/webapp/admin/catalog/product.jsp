<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="productId" value="${param.id}"/>
<c:if test="${! empty productId}">
    <spring:eval expression="serviceLocator.productDao.findById(T(java.lang.Long).valueOf(productId))" var="product"/>
</c:if>

<c:set value="checked" var="activeChecked"/>
<c:choose>
    <c:when test="${! empty product.id}">
        <fmt:message key="template.admin.edit.product" var="formState"/>
        <c:set value="${product.id}" var="productId"/>
        <c:set value="${product.uri}" var="uri"/>
        <c:set value=": ${product.name}" var="name"/>
        <c:if test="${fn:startsWith(product.active, 'N')}">
            <c:set value="" var="activeChecked"/>
        </c:if>

    </c:when>
    <c:otherwise>
        <c:set value="" var="name"/>
        <c:set value="/admin/catalog/product_insert.html" var="formUrl"/>
        <c:set value="checked" var="activeChecked"/>
    </c:otherwise>
</c:choose>

<title><fmt:message key="template.admin.create.product"/></title>
<div class="page-header">
    <h4><fmt:message key="template.admin.create.product"/></h4>
    <a class="btn btn-xs btn-info" data-title="<fmt:message key="product.add"/>" href="#modal-form" role="button" data-toggle="modal" data-page="/admin/catalog/product_form.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="product.add"/>
    </a>
    <p>
        Để hiển thị sản phẩm, bạn cần nhập và lưu tối thiểu những nội dung sau:<br>
        1. Tab "Thông Tin Sản Phẩm": Hiển thị trên web, Tên sản phẩm, Mã sản phẩm<br>
        2. Tab "Giá/Kích Thước/Màu Sắc": Giá, Số Lượng
    </p>
</div><!-- /.page-header -->
<div class="row">
    <div class="col-sm-12">
        <div  id="message_alert"></div>
        <div class="tabbable">
            <ul class="nav nav-tabs" id="form-tab">
                <li class="active">
                    <a data-toggle="tab" href="#home">
                        <i class="ace-icon fa fa-cube"></i>
                        <fmt:message key="product.information"/>
                    </a>
                </li>
                <li>
                    <a data-toggle=tab href="#colors_sizes" data-tab-url="/admin/catalog/product_colors_sizes.html?id=${product.id}" data-tab-always-refresh="true">
                        <i class="ace-icon fa  fa-pied-piper"></i>
                        <fmt:message key="product.price.color.size"/>
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" href="#images_document" data-tab-url="/admin/catalog/product_images_documents.html?id=${product.id}" data-tab-always-refresh="true">
                        <i class="ace-icon fa fa-file-image-o"></i>
                        <fmt:message key="product.images.documents"/>
                    </a>
                </li>
                <%--<li>--%>
                    <%--<a data-toggle="tab" href="#reated_products" data-tab-url="/admin/catalog/product_related_products.html?id=${product.id}" data-tab-always-refresh="true">--%>
                        <%--<i class="ace-icon fa fa-chevron-circle-down"></i>--%>
                        <%--<fmt:message key="product.related.products"/>--%>
                    <%--</a>--%>
                <%--</li>--%>
                <li>
                    <a data-toggle="tab" href="#reated_categories" data-tab-url="/admin/catalog/product_related_categories.html?id=${product.id}" data-tab-always-refresh="true">
                        <i class="ace-icon fa fa-folder"></i>
                        <fmt:message key="product.related.categories"/>
                    </a>
                </li>
                <%--<li>--%>
                <%--<a data-toggle="tab" href="#google_analytics">--%>
                <%--<i class="ace-icon fa fa-google-plus-square"></i>--%>
                <%--<fmt:message key="product.other.information"/>--%>
                <%--</a>--%>
                <%--</li>--%>
            </ul>
            <div class="tab-content">
                <div id="home" class="tab-pane fade in active">
                    <div class="page-header">
                        <a class="btn btn-xs btn-info" id="product_information">
                            <i class="ace-icon fa fa-check"></i> <fmt:message key="common.save.changes"/>
                        </a>
                    </div><!-- /.page-header -->
                    <form name="product_information" class="form-horizontal" action="#" method="post">
                        <h:csrf/>
                        <input name="id" type="hidden" value="${productId}"/>
                        <input name="uri" type="hidden" value="${uri}"/>
                        <input name="type" type="hidden" value=""/>
                        <fieldset>
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="active"><fmt:message key="common.active.status"/></label>
                                        <div class="col-sm-8 forms">
                                            <input type="checkbox" id="active" name="active" ${activeChecked}/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="name"><fmt:message key="product.name"/>*</label>
                                        <div class="col-sm-8 forms">
                                            <input name="name" class="required input-xlarge" id="name" type="text" maxlength="255" value="${product.name}" autofocus/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="model"><fmt:message key="product.model"/>*</label>
                                        <div class="col-sm-8 forms">
                                            <input name="model" class="" id="model" type="text" maxlength="15" value="${product.model}"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="description"><fmt:message key="product.short.description"/></label>
                                        <div class="col-sm-8 forms">
                                            <textarea name="description" rows="5" class="input-xlarge" id="description" >${product.description}</textarea>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="newProduct"><fmt:message key="product.new.product.status"/></label>
                                        <div class="col-sm-8 forms">
                                            <input type="checkbox" id="newProduct" name="newProduct" ${product.newProduct == 'Y'?'checked':''}/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="attribute1"><fmt:message key="product.attribute.1"/></label>
                                        <div class="col-sm-8 forms">
                                            <input name="attribute1" class="input-xlarge" id="attribute1" type="text" maxlength="255" value="<c:out value="${product.attribute1}"/>" autofocus/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="attribute2"><fmt:message key="product.attribute.2"/></label>
                                        <div class="col-sm-8 forms">
                                            <input name="attribute2" class="input-xlarge" id="attribute2" type="text" maxlength="255" value="<c:out value="${product.attribute2}"/>" autofocus/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="attribute3"><fmt:message key="product.attribute.3"/></label>
                                        <div class="col-sm-8 forms">
                                            <input name="attribute3" class="input-xlarge" id="attribute3" type="text" maxlength="255" value="<c:out value="${product.attribute3}"/>" autofocus/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="attribute4"><fmt:message key="product.attribute.4"/></label>
                                        <div class="col-sm-8 forms">
                                            <input name="attribute4" class="input-xlarge" id="attribute4" type="text" maxlength="255" value="<c:out value="${product.attribute4}"/>" autofocus/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 control-label no-padding-right" for="attribute5"><fmt:message key="product.attribute.5"/></label>
                                        <div class="col-sm-8 forms">
                                            <input name="attribute5" class="input-xlarge" id="attribute5" type="text" maxlength="255" value="<c:out value="${product.attribute5}"/>" autofocus/>
                                        </div>
                                    </div>
                                </div>
                            </div>


                        </fieldset>
                    </form>
                </div>
                <div id="colors_sizes" class="tab-pane fade">
                    <p>Loading...</p>
                </div>
                <div id="images_document" class="tab-pane fade">
                    <p>Loading...</p>
                </div>
                <%--<div id="reated_products" class="tab-pane fade">--%>
                    <%--<p>Loading...</p>--%>
                <%--</div>--%>
                <div id="reated_categories" class="tab-pane fade">
                    <p>Loading...</p>
                </div>
                <%--<div id="google_analytics" class="tab-pane fade">--%>
                <%--</div>--%>
            </div>
        </div>
    </div>
</div>
<script>
    var remoteTabsPluginLoaded = new RemoteTabs();
    $(function () {

        $("#product_information").validate({
            rules:{
                name:{
                    required:true
                },
                price:{
                    required:true
                },
                inventory:{
                    required:true
                }
            },

            messages:{
                name:{
                    required:"<fmt:message key="product.name.is.required"/>"
                },
                price:{
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

        $("#product_information").on("click", function() {
            var form = $( "form[name=product_information]" );
            if (form.valid()) {
                $.ajax({
                    type: "POST",
                    url: '/admin/catalog/product_update.html',
                    data: form.serialize(), // serializes the form's elements.
                    success: function(data)
                    {
//                        document.location.href="#/admin/catalog/product?id="+data.id;
                        $('#message_alert').html(data.messages);
                        $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#message_alert").alert('close');
                        });
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            }
        });

        $("#publish").click(function() {
            $("#form input[name='type']").val('publish');
            $('#form').submit();
        });

    });
</script>
<h:form_modal/>
<h:image_modal/>
