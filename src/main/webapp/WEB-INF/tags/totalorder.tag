<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="sessionObject" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.model.session.SessionObject"%>
<%--<%@ attribute name="isBasketPage" required="true" rtexprvalue="true" type="java.lang.Boolean"%>--%>

<ul class="list-inline total-result">
    <li>
        <h4><fmt:message key="basket.subtotal"/>:</h4>
        <spring:eval expression="sessionObject.getOrder().getSubPriceTotal()" var="subPrice"/>
        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subPrice/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalPrice"/>
        <div class="total-result-in">
            <span>${subTotalPrice}</span>
        </div>
        <spring:eval expression="sessionObject.getOrder().getOriginalSubTotal()" var="originalSubTotal"/>
        <c:if test="${subPrice != originalSubTotal}">
            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(originalSubTotal/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="originalSubTotalPrice"/>
            <div class="total-result-in line-through">
                <span>${originalSubTotalPrice}</span>
            </div>
        </c:if>
    </li>
    <spring:eval expression="sessionObject.getOrder().getSubPriceDiscountTotal()" var="subTotalDiscount"/>
    <c:if test="${!empty subTotalDiscount && subTotalDiscount > 0}">
        <li>
            <h4><fmt:message key="subordertotal-discount"/>:</h4>
            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(subTotalDiscount/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="subTotalDiscount"/>
            <div class="total-result-in">
                <span class="text-right">-${subTotalDiscount}</span>
            </div>
        </li>
    </c:if>
    <li>
        <spring:eval expression="sessionObject.getShippingFee()" var="shippingPrice"/>
        <spring:eval expression="sessionObject.getOrder().getShippingDiscountPrice()" var="shippingPriceDiscount"/>
        <%--Shipping price > 0, Check discount for shipping fee--%>
        <c:if test="${!empty shippingPrice && shippingPrice > 0}">
            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPrice/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price"/>
            <h4><fmt:message key="shipping.fee"/>:</h4>
            <div class="total-result-in">
                <span class="text-right">${shipping_Price}</span>
            </div>
            <c:if test="${shippingPriceDiscount > 0}">
                <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(shippingPriceDiscount/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="shipping_Price_Discount"/>
                <h4><fmt:message key="basket.discount.shipping.fee"/>:</h4>
                <div class="total-result-in">
                    <span class="text-right">-${shipping_Price_Discount}</span><br>
                </div>
            </c:if>
        </c:if>
    </li>
    <spring:eval expression="sessionObject.getOrder().getTaxPrice()" var="taxPrice"/>
    <%--<c:if test="${taxPrice > 0}">--%>
    <li>
        <h4><fmt:message key="basket.tax"/>:</h4>
        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(taxPrice/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="tax_Price"/>
        <div class="total-result-in">
            <span>${tax_Price}</span>
        </div>
    </li>
    <%--</c:if>--%>
    <li class="divider"></li>
    <li class="total-price">
        <h4><fmt:message key="basket.ordertotal"/>:</h4>
        <spring:eval expression="sessionObject.getOrder().getTotalPrice()" var="price"/>
        <c:if test="${!empty price}">
            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price/100.0,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="totalPrice"/>
            <div class="total-result-in">
                <span>${totalPrice}</span>
            </div>
        </c:if>
    </li>
</ul>
