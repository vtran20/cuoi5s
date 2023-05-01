<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="sessionObject" required="true" rtexprvalue="true" type="java.util.Map"%>
<%@ attribute name="isBasketPage" required="true" rtexprvalue="true" type="java.lang.Boolean"%>

<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
            <tr>
                <th><fmt:message key="basket.product.description"/></th>
                <th><fmt:message key="basket.product.price"/></th>
                <th><fmt:message key="basket.product.qty"/></th>
                <th><fmt:message key="basket.product.price.total"/></th>
            </tr>
            </thead>
            <tbody>
<c:forEach items="${items}" varStatus="item">
    <spring:eval expression="new java.util.Date()" var="currentDate"/>
    <tr>
        <td class="product-in-table" style="width: 50%;">
            <c:set var="imageUrl" value="/assets/images/no_image.png"/>
            <c:if test="${!empty item.current.IMAGE_URL}">
                <c:set var="imageUrl" value="${imageServer}/get/${item.current.IMAGE_URL}.jpg?op=scale_120x&op=crop|0,0,120,120"/>
            </c:if>
            <a href="/product/${item.current.ITEM_URI}-${item.current.ITEM_ID}.html"><img class="img-responsive" width="120" src="${imageUrl}" alt="${item.current.NAME}"></a>
            <div class="product-it-in">
                <h3><a href="/product/${item.current.ITEM_URI}-${item.current.ITEM_ID}.html">${item.current.NAME}</a></h3>
                <c:if test="${!empty item.current.MODEL_NUMBER}">
                    <span><fmt:message key="product.style"/>: ${item.current.MODEL_NUMBER}</span><br>
                </c:if>
                <c:if test="${!empty item.current.COLOR}">
                    <span><fmt:message key="basket.product.color"/>: ${item.current.COLOR}</span><br>
                </c:if>
                <c:if test="${!empty item.current.SIZE}">
                    <span><fmt:message key="basket.product.size"/>: ${item.current.SIZE}</span><br>
                </c:if>
            </div>
        </td>
        <td>
            <c:set var="price" value="${item.current.FINAL_PRICE_ITEM}"/>
            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPrice"/>
                ${itemPrice}
        </td>
        <td>
            <c:choose>
                <c:when test="${isBasketPage}">
                    <button type='button' class="quantity-button" name='subtract' onclick='javascript: subtractQty("quantity_${item.current.ITEM_VARIANT_ID}");' value='-'>-</button>
                    <input type='text' class="quantity-field" name='quantity_${item.current.ITEM_VARIANT_ID}' value="${item.current.QUANTITY}" id='quantity_${item.current.ITEM_VARIANT_ID}'/>
                    <button type='button' class="quantity-button" name='add' onclick='javascript: addQty("quantity_${item.current.ITEM_VARIANT_ID}");' value='+'>+</button>
                </c:when>
                <c:otherwise>
                    <input type='text' class="quantity-field" name='quantity_${item.current.ITEM_VARIANT_ID}' value="${item.current.QUANTITY}" disabled/>
                </c:otherwise>
            </c:choose>
        </td>
        <td class="shop-red">
            <c:set value="${item.current.QUANTITY}" var="quantity"/>
            <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(price*quantity,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPriceTotal"/>
                ${itemPriceTotal}
            <c:if test="${item.current.PRICE_ITEM_PROMO_DISCOUNT > 0}">
                <c:set value="${item.current.PRICE_ITEM_PROMO_DISCOUNT}" var="itemDiscountPrice"/>
                <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(itemDiscountPrice * quantity,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="itemPriceDiscountTotal"/>
                <br><span class="discount-row">-${itemPriceDiscountTotal}</span>
            </c:if>
        </td>
        <c:if test="${isBasketPage}">
            <td>
                <a href="/checkout/updatecart.html?quantity_${item.current.ITEM_VARIANT_ID}=0"class="close"><span>&times;</span><span class="sr-only">Close</span></a>
            </td>
        </c:if>
    </tr>
</c:forEach>
            </tbody>
        </table>
    </div>
<script type="text/javascript">
    function subtractQty(inputId) {
        if(document.getElementById(inputId).value - 1 < 0)
            return;
        else
            document.getElementById(inputId).value--;
        window.location = "/checkout/updatecart.html?"+inputId+"="+document.getElementById(inputId).value;
    }
    function addQty(inputId) {
        document.getElementById(inputId).value++;
        window.location = "/checkout/updatecart.html?"+inputId+"="+document.getElementById(inputId).value;
    }
</script>