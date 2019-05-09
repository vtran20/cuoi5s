<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="isBasketPage" required="true" rtexprvalue="true" type="java.lang.Boolean"%>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 30%;"><fmt:message key="basket.product.service"/></th>
                <th style="width: 20%;"><fmt:message key="basket.product.service.price"/></th>
                <th style="width: 20%;"><fmt:message key="basket.product.qty"/></th>
                <th style="width: 20%;"><fmt:message key="basket.product.price.total"/></th>
                <th style="width: 10%;"></th>
            </tr>
            </thead>
            <tbody>
<c:forEach items="${items}" varStatus="item">
    <spring:eval expression="new java.util.Date()" var="currentDate"/>
    <c:if test="${empty item.current.PARENT_PRODUCT_ID}">
        <tr>
            <td colspan="5">
                <table width="100%">
                    <tr>
                        <td class="product-in-table" style="width: 30%;">
                            <div class="product-it-in">
                                <h3>${item.current.NAME}</h3>
                                <c:choose>
                                    <c:when test="${!empty item.current.SITE_ID}">
                                        <spring:eval expression="serviceLocator.getSiteDao().findById(T(java.lang.Long).valueOf(item.current.SITE_ID))" var="thisSite"/>
                                        <c:choose>
                                            <c:when test="${!empty thisSite.domain}">
                                                <div>${thisSite.domain}</div>
                                            </c:when>
                                            <c:otherwise>
                                                <div>${thisSite.subDomain}</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key='message.product.is.invalid'/>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${isBasketPage}">
                                    <%--<span>--%>
                                    <%--<button type="button" class="btn-u" data-toggle="modal" data-target=".bs-example-modal-lg"><fmt:message key="product.add.modules"/></button>--%>
                                    <%--</span>--%>
                                    <spring:eval expression="serviceLocator.getProductDao().getNotAddedModules(site.id, item.current.SITE_ID)" var="notAddedProducts" />
                                    <c:if test="${!empty notAddedProducts}">
                                        <a class="btn btn-xs btn-info" title="<fmt:message key="product.add.modules"/>" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="product.add.modules"/>" data-page="/site/modal_modules.html?thisSiteId=${item.current.SITE_ID}" data-target="#modal-form">
                                            <i class="fa fa-plus"></i> <fmt:message key="product.add.modules"/>
                                        </a>
                                    </c:if>
                                </c:if>
                            </div>
                        </td>
                        <td style="width: 20%;">
                            <c:choose>
                                <c:when test="${isBasketPage}">
                                    <spring:eval expression="serviceLocator.getProductVariantDao().findProductVariantByColorSize(item.current.ITEM_ID, 'Y')" var="productVariants"/>
                                    <c:choose>
                                        <c:when test="${fn:length(productVariants) > 1}">
                                            <select name="productVariantId_${item.current.ITEM_ID}-${item.current.SITE_ID}" class="selectService required input-lg">
                                                <option value=''><fmt:message key="product.select.service"/></option>
                                                <c:forEach items="${productVariants}" var="pv">
                                                    <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(T(com.easysoft.ecommerce.util.WebUtil).generatePriceMin(pv.price/100.0, pv.pricePromo/100.0),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="priceItem"/>
                                                    <c:set value="" var="selected"/>
                                                    <c:if test="${pv.id == item.current.ITEM_VARIANT_ID}">
                                                        <c:set value="selected" var="selected"/>
                                                    </c:if>
                                                    <option value="${item.current.ITEM_ID}-${pv.id}-${item.current.SITE_ID}" ${selected}> ${pv.sku} - ${priceItem}</option>
                                                </c:forEach>
                                            </select>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="price" value="${item.current.FINAL_PRICE_ITEM}"/>
                                            <c:choose>
                                                <c:when test="${!empty price}">
                                                    <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPrice"/>
                                                    ${itemPrice}
                                                </c:when>
                                                <c:otherwise>
                                                    Free
                                                </c:otherwise>
                                            </c:choose>

                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="price" value="${item.current.FINAL_PRICE_ITEM}"/>
                                    <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPrice"/>
                                    ${itemPrice}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style="width: 20%;">
                            <c:choose>
                                <c:when test="${isBasketPage}">
                                    <button type='button' class="quantity-button" name='subtract' onclick='javascript: subtractQty("quantity_${item.current.ITEM_VARIANT_ID}_${item.current.SITE_ID}");' value='-'>-</button>
                                    <input type='text' class="quantity-field" name='quantity_${item.current.ITEM_VARIANT_ID}' value="${item.current.QUANTITY}" id='quantity_${item.current.ITEM_VARIANT_ID}_${item.current.SITE_ID}'/>
                                    <button type='button' class="quantity-button" name='add' onclick='javascript: addQty("quantity_${item.current.ITEM_VARIANT_ID}_${item.current.SITE_ID}");' value='+'>+</button>
                                </c:when>
                                <c:otherwise>
                                    <input type='text' class="quantity-field" name='quantity_${item.current.ITEM_VARIANT_ID}' value="${item.current.QUANTITY}" disabled/>
                                </c:otherwise>
                            </c:choose>
                            &nbsp;&nbsp;<fmt:message key="basket.year"/>
                        </td>
                        <td class="shop-red"  style="width: 20%;">
                            <c:set var="price" value="${item.current.FINAL_PRICE_ITEM}"/>
                            <c:if test="${! empty price}">
                                <c:set value="${item.current.QUANTITY}" var="quantity"/>
                                <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price/100.0*quantity,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPriceTotal"/>
                                ${itemPriceTotal}
                                <c:if test="${item.current.PRICE_ITEM_PROMO_DISCOUNT > 0}">
                                    <c:set value="${item.current.PRICE_ITEM_PROMO_DISCOUNT}" var="itemDiscountPrice"/>
                                    <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(itemDiscountPrice/100.0 * quantity,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPriceDiscountTotal"/>
                                    <br><span class="discount-row">-${itemPriceDiscountTotal}</span>
                                </c:if>
                            </c:if>
                        </td>
                        <td style="width: 10%;">
                            <c:if test="${isBasketPage}">
                                <a href="/site/checkout/deletecart.html?productId=${item.current.ITEM_ID}&thisSiteId=${item.current.SITE_ID}" class="close"><span>&times;</span><span class="sr-only">Close</span></a>
                            </c:if>
                        </td>
                    </tr>
                    <spring:eval expression="T(com.easysoft.ecommerce.controller.SessionUtil).getRelatedProducts (items, item.current.ITEM_ID, 1, item.current.SITE_ID)" var="relatedItems"/>
                    <c:if test="${fn:length(relatedItems) > 0}">
                        <c:forEach items="${relatedItems}" varStatus="item">
                            <tr>
                                <td colspan="5">
                                    <table width="100%">
                                        <tr>
                                            <td class="product-in-table" style="width: 70%;" colspan="3">
                                                <div class="product-it-in">
                                                    <h3>${item.current.NAME}</h3>
                                                </div>
                                            </td>
                                                <%--<td  style="width: 20%;">--%>
                                                <%--<c:choose>--%>
                                                <%--<c:when test="${isBasketPage}">--%>
                                                <%--<spring:eval expression="serviceLocator.getProductVariantDao().findProductVariantByColorSize(item.current.ITEM_ID, 'Y')" var="productVariants"/>--%>
                                                <%--<c:choose>--%>
                                                <%--<c:when test="${fn:length(productVariants) > 1}">--%>
                                                <%--<select name="productVariantId_${item.current.ITEM_ID}-${item.current.SITE_ID}" class="selectService required input-lg">--%>
                                                <%--<option value=''><fmt:message key="product.select.service"/></option>--%>
                                                <%--<c:forEach items="${productVariants}" var="pv">--%>
                                                <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(T(com.easysoft.ecommerce.util.WebUtil).generatePriceMin(pv.price, pv.pricePromo),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="priceItem"/>--%>
                                                <%--<c:set value="" var="selected"/>--%>
                                                <%--<c:if test="${pv.id == item.current.ITEM_VARIANT_ID}">--%>
                                                <%--<c:set value="selected" var="selected"/>--%>
                                                <%--</c:if>--%>
                                                <%--<option value="${item.current.ITEM_ID}-${pv.id}-${item.current.SITE_ID}" ${selected}> ${pv.sku} - ${priceItem}</option>--%>
                                                <%--</c:forEach>--%>
                                                <%--</select>--%>
                                                <%--</c:when>--%>
                                                <%--<c:otherwise>--%>
                                                <%--<c:set var="price" value="${item.current.FINAL_PRICE_ITEM}"/>--%>
                                                <%--<c:choose>--%>
                                                <%--<c:when test="${!empty price}">--%>
                                                <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPrice"/>--%>
                                                <%--${itemPrice}--%>
                                                <%--</c:when>--%>
                                                <%--<c:otherwise>--%>
                                                <%--Free--%>
                                                <%--</c:otherwise>--%>
                                                <%--</c:choose>--%>

                                                <%--</c:otherwise>--%>
                                                <%--</c:choose>--%>
                                                <%--</c:when>--%>
                                                <%--<c:otherwise>--%>
                                                <%--<c:set var="price" value="${item.current.FINAL_PRICE_ITEM}"/>--%>
                                                <%--<spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPrice"/>--%>
                                                <%--${itemPrice}--%>
                                                <%--</c:otherwise>--%>
                                                <%--</c:choose>--%>
                                                <%--</td>--%>
                                                <%--<td>--%>
                                                <%--<c:choose>--%>
                                                <%--<c:when test="${isBasketPage}">--%>
                                                <%--<button type='button' class="quantity-button" name='subtract' onclick='javascript: subtractQty("quantity_${item.current.ITEM_VARIANT_ID}");' value='-'>-</button>--%>
                                                <%--<input type='text' class="quantity-field" name='quantity_${item.current.ITEM_VARIANT_ID}' value="${item.current.QUANTITY}" id='quantity_${item.current.ITEM_VARIANT_ID}'/>--%>
                                                <%--<button type='button' class="quantity-button" name='add' onclick='javascript: addQty("quantity_${item.current.ITEM_VARIANT_ID}");' value='+'>+</button>--%>
                                                <%--</c:when>--%>
                                                <%--<c:otherwise>--%>
                                                <%--<input type='text' class="quantity-field" name='quantity_${item.current.ITEM_VARIANT_ID}' value="${item.current.QUANTITY}" disabled/>--%>
                                                <%--</c:otherwise>--%>
                                                <%--</c:choose>--%>
                                                <%--&nbsp;&nbsp;<fmt:message key="basket.year"/>--%>
                                                <%--</td>--%>
                                            <td class="shop-red"  style="width: 20%;">
                                                <c:set var="price" value="${item.current.FINAL_PRICE_ITEM}"/>
                                                <c:if test="${! empty price}">
                                                    <c:set value="${item.current.QUANTITY}" var="quantity"/>
                                                    <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price/100.0*quantity,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPriceTotal"/>
                                                    ${itemPriceTotal}
                                                    <c:if test="${item.current.PRICE_ITEM_PROMO_DISCOUNT > 0}">
                                                        <c:set value="${item.current.PRICE_ITEM_PROMO_DISCOUNT}" var="itemDiscountPrice"/>
                                                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(itemDiscountPrice/100.0 * quantity,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPriceDiscountTotal"/>
                                                        <br><span class="discount-row">-${itemPriceDiscountTotal}</span>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td style="width: 10%;">
                                                <c:if test="${isBasketPage}">
                                                    <a href="#" data-message="<fmt:message key='site.do.you.want.remove.this.module'/>" data-href="/site/checkout/removemodulefromcart.html?productId=${item.current.ITEM_ID}&thisSiteId=${item.current.SITE_ID}" class="close show-confirm-basket"><span>&times;</span><span class="sr-only">Close</span></a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>

                </table>
            </td>
        </tr>
</c:if>
</c:forEach>
            </tbody>
        </table>
    </div>
<script type="text/javascript">
    $("select.selectService").on('change', function() {
        var object = $(this);
        variantId=object.val();
        document.location.href="/site/checkout/updatecart.html?data="+variantId;
    });
    function subtractQty(inputId) {
        if(document.getElementById(inputId).value - 1 < 0)
            return;
        else
            document.getElementById(inputId).value--;
        window.location = "/site/checkout/updatecart.html?"+inputId+"="+document.getElementById(inputId).value;
    }
    function addQty(inputId) {
        document.getElementById(inputId).value++;
        window.location = "/site/checkout/updatecart.html?"+inputId+"="+document.getElementById(inputId).value;
    }

    jQuery('input.quantity-field').on('change', function() {
        var object = $(this);
        window.location = "/site/checkout/updatecart.html?"+object.attr("id")+"="+object.val();
    });

    $(".show-confirm-basket").click(function () {
        var object = $(this);
        BootstrapDialog.show({
            title: 'Xác nhận',
            message: object.attr("data-message"),
            buttons: [{
                label: 'Yes',
                action: function(dialog) {
                    dialog.close();
                    $.ajax({
                        type: "POST",
                        url: object.attr("data-href"),
                        success: function(data)
                        {
                            window.location.reload(true);
                        }
                    });
                    return false; // avoid to execute the actual submit of the form.

                }
            }, {
                label: 'No',
                action: function(dialog) {
                    dialog.close();
                }
            }]
        });

    });

</script>
