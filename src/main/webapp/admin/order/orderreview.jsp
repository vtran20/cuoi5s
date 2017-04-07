<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<%--<spring:eval expression="order.getUserId()" var="userId"/>--%>
<c:set var="id" value="${param.id}"/>
<c:if test="${! empty id}">
    <spring:eval expression="serviceLocator.orderSessionDao.getOrder(T(java.lang.Long).valueOf(id), 0, site)" var="order"/>
    <spring:eval expression="serviceLocator.orderSessionDao.getOrderSession(T(java.lang.Long).valueOf(id), 0l, site)" var="orderSession"/>
</c:if>
<spring:eval expression="orderSession.getOrder().getItems()" var="items"/>

<c:set var="billingAddress" value="${orderSession.ADDRESSES.BILLING_ADDRESS}"/>
<c:set var="shippingAddress" value="${orderSession.ADDRESSES.SHIPPING_ADDRESS}"/>

<div class="row">
    <div class="col-sm-12">
        <div class="page-header">
            <h4><fmt:message key="billing.shipping.header"/></h4>
        </div>
        <div  id="modal_message_alert"></div>
        <div class="row">
            <div class="col-sm-12">
            <div id="che-ord-rev-checkout-inner">
                    <div id="che-ord-rev-top">
                        <c:set value='${order.status}' var="status"/>
                        <spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValue('ORDER_STATUS', serviceLocator.locale, status)" var="orderStatus"/>
                        <div class="che-ord-rev-text"><fmt:message key="receipt.order.number"/>: <b>${order.id}</b></div>
                        <br>
                        <spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValues('ORDER_STATUS', serviceLocator.locale)" var="stringParamValues"/>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="che-ord-rev-text"><fmt:message key="order.order.status"/></div>
                                <c:choose>
                                    <c:when test="${order.orderType == 1}">
                                        <select name="orderStatus" class="required" onchange="changeOrderStatus(this, ${order.id})">
                                            <c:forEach items="${stringParamValues}" var="stringParam">
                                                "${stringParam.key}=='PAID'"
                                                <c:if test="${stringParam.key == 'PAID' || stringParam.key == 'NEW_ORDER' || stringParam.key == 'CANCELLED'}">
                                                    <c:if test="${stringParam.key == order.status}">
                                                        <option value="${stringParam.key}" selected>${stringParam.value}</option>
                                                    </c:if>
                                                    <c:if test="${stringParam.key != order.status}">
                                                        <option value="${stringParam.key}">${stringParam.value}</option>
                                                    </c:if>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <select name="orderStatus" class="required" onchange="changeOrderStatus(this, ${order.id})">
                                            <c:forEach items="${stringParamValues}" var="stringParam">
                                                <c:if test="${stringParam.key == order.status}">
                                                    <option value="${stringParam.key}" selected>${stringParam.value}</option>
                                                </c:if>
                                                <c:if test="${stringParam.key != order.status}">
                                                    <option value="${stringParam.key}">${stringParam.value}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <br>
                        <br>
                        <c:choose>
                            <c:when test="${order.orderType == '1'}">
                                <h:basketserviceitems items="${items}" isBasketPage="false"/>
                            </c:when>
                            <c:otherwise>
                                <h:basketitems items="${items}" sessionObject="${orderSession}" isBasketPage="false"/>
                            </c:otherwise>
                        </c:choose>
                        <div class="clr"><!--  --></div>
                            <c:set var="promoCode" value="${orderSession.ORDER.PROMO_CODE}"/>
                            <c:if test="${!empty promoCode}">
                                <div id="che-bas-promo-update-wrapper">
                                        <div class="promo-code-message">
                                            <fmt:message key="message.promocode.using"/>&nbsp;&nbsp;<b>${promoCode}</b>
                                        </div>
                                </div>
                            </c:if>
                    </div>
                    <div class="clr"><!--  --></div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-4">
                <div id="che-ord-rev-billto">
                    <div class="che-ord-rev-order-info-header signinold"><fmt:message key="receipt.billing.information"/></div>
                    <ul>
                        <li>${billingAddress.LAST_NAME}&nbsp;${billingAddress.FIRST_NAME}</li>
                        <li>${billingAddress.ADDRESS_LINE}</li>
                        <%--<li>${billingAddress.DISTRICT}</li>--%>
                        <c:set value='${billingAddress.CITY}' var="city"/>
                        <c:if test="${!empty city}">
                            <spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValue('CITY', serviceLocator.locale, city)" var="billingCity"/>
                            <li>${billingCity.value}</li>
                        </c:if>
                        <li>${billingAddress.PHONE}</li>
                        <li>${billingAddress.EMAIL}</li>
                    </ul>
                </div>
            </div>
            <div class="col-sm-4">
                <div id="che-ord-rev-paymentinfo">
                    <div class="che-ord-rev-order-info-header signinold"><fmt:message key="receipt.shipping.address"/></div>
                    <ul>
                        <li>${shippingAddress.LAST_NAME}&nbsp;${shippingAddress.FIRST_NAME}</li>
                        <li>${shippingAddress.ADDRESS_LINE}</li>
                        <%--<li>${shippingAddress.DISTRICT}</li>--%>
                        <c:set value='${shippingAddress.CITY}' var="city"/>
                        <c:if test="${!empty city}">
                            <spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValue('CITY', serviceLocator.locale, city)" var="shippingCity"/>
                            <li>${shippingCity.value}</li>
                        </c:if>
                        <li>${shippingAddress.PHONE}</li>
                    </ul>
                    <!--  end bottom payment -->
                </div>
            </div>
            <div class="col-sm-4">
                <div id="che-ord-rev-ordertotal">
                    <div class="che-ord-rev-order-info-header"><fmt:message key="receipt.total.entire.order"/></div>
                    <dl id="review-order-summary">
                        <dt><fmt:message key="receipt.subtotal"/></dt>
                        <spring:eval expression="orderSession.getOrder().getSubPriceTotal()"
                                     var="subPrice"/>
                        <spring:eval
                                expression="T(com.easysoft.ecommerce.util.Money).valueOf(subPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"
                                var="subTotalPrice"/>
                        <dd><span class="js-merchandise">${subTotalPrice}</span></dd>
                        <dd class="clr"><!--  --></dd>
                        <%--Order discount--%>
                        <spring:eval expression="orderSession.getOrder().getSubPriceDiscountTotal()" var="subTotalDiscount"/>
                        <c:if test="${!empty subTotalDiscount && subTotalDiscount > 0}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subTotalDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalDiscount"/>
                            <dt class="discount-row"><fmt:message key="subordertotal-discount"/></dt>
                            <dd class="discount-row">-${subTotalDiscount}</dd>
                            <dd class="clr"><!--  --></dd>
                        </c:if>

                        <%--<dt id="giftBox1"><fmt:message key="receipt.gift.fee"/></dt>--%>
                        <%--<dd id="giftBox2"><span class="js-giftBoxFee"></span></dd>--%>
                        <dd id="giftBox3" class="clr"><!--  --></dd>
                        <dt><fmt:message key="receipt.shipping.fee"/></dt>

                        <%--<spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getShippingFee(orderSession)" var="shippingPrice"/>--%>
                        <spring:eval expression="orderSession.getShippingFee()" var="shippingPrice"/>
                        <spring:eval expression="orderSession.getOrder().getShippingDiscountPrice()" var="shippingPriceDiscount"/>
                        <%--Shipping price > 0, Check discount for shipping fee--%>
                        <c:if test="${!empty shippingPrice && shippingPrice > 0}">
                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>
                        <dd><span class="js-orderShippingAmount">${shipping_Price}</span><br></dd>
                        <c:if test="${shippingPriceDiscount > 0}">
                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPriceDiscount,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price_Discount"/>
                        <dl class="discount-row">
                        <dt> <fmt:message key="receipt.discount.shipping.fee"/></dt>
                        <dd>-${shipping_Price_Discount}</dd>
                        </dl>
                        </c:if>
                        </c:if>
                        <%--Shipping price is 0, don't care discount for shipping fee--%>
                        <c:if test="${empty shippingPrice || shippingPrice <= 0}">
                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>
                        <dd><span class="js-orderShippingAmount">${shipping_Price}</span></dd>
                        </c:if>
                        <spring:eval
                                expression="T(com.easysoft.ecommerce.util.Money).valueOf(orderSession.getOrder().getTaxPrice(),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"
                                var="tax_Price"/>
                        <dd class="clr"></dd>
                        <dt id="salesTax1"><fmt:message key="receipt.tax"/></dt>
                        <dd id="salesTax2"><span class="js-salesTax">${tax_Price}</span></dd>
                        <dd id="salesTax3" class="clr"><!--  --></dd>
                    </dl>
                    <spring:eval expression="orderSession.getOrder().getTotalPrice()" var="price"/>
                    <spring:eval
                            expression="T(com.easysoft.ecommerce.util.Money).valueOf(price, site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"
                            var="totalPrice"/>
                    <div id="che-sin-grand-total-wrapper" style="color: #000000;background-color: #ffffff;">
                        <div><fmt:message key="receipt.total.entire.order"/>:</div>
                        <span class="js-orderGrandTotalAmount che-sin-grand-total">${totalPrice}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function changeOrderStatus(thisSelect, orderId) {

        var object = $(thisSelect);
        BootstrapDialog.show({
            type:BootstrapDialog.TYPE_DANGER,
            closeByBackdrop: false,
            closeByKeyboard: false,
            title:'<fmt:message key="common.confirm.title"/>',
            message:'<fmt:message key="common.do.you.want.to.change.this.content"/>',
            buttons:[
                {
                    label:'Yes',
                    action:function (dialog) {
                        $.ajax({
                            type: "GET",
                            url: '/admin/order/changeorderstatus.html',
                            data: { orderId: orderId, orderStatus:object.val()}, // serializes the form's elements.
                            success: function(data)
                            {
                                $('#modal_message_alert').html(data);
                                $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                    $("#modal_message_alert").alert('close');
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
    }
</script>