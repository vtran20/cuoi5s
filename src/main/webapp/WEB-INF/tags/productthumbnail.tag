<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="product" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.model.Product"%>
<%@ attribute name="parCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="cat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="subCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>

<c:set var="url" value="/product"/>
<c:if test="${!empty parCat}"><c:set var="url" value="${url}/${parCat.uri}-${parCat.id}" /></c:if>
<c:if test="${!empty cat}"><c:set var="url" value="${url}/${cat.uri}-${cat.id}" /></c:if>
<c:if test="${!empty subCat}"><c:set var="url" value="${url}/${subCat.uri}-${subCat.id}" /></c:if>
<c:set var="url" value="${url}/${product.uri}-${product.id}.html"/>
<c:url var="url" value="${url}"/>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<spring:eval expression="serviceLocator.productFileDao.getDefaultImage(product.id, 'PRODUCT_FILE_IMAGE')" var="image" />

<div class="col-sm-4">
    <div class="product-img product-img-brd">
        <c:set var="imageUrl" value="/assets/images/no_image.png"/>
        <c:if test="${!empty image}">
            <c:choose>
                <c:when test="${!empty image.crop}"><c:set var="imageUrl" value="${imageServer}/get/${image.uri}.jpg?op=crop_${image.crop}&op=scale_x328"/></c:when>
                <c:otherwise><c:set var="imageUrl" value="${imageServer}/get/${image.uri}.jpg?op=scale_x328&op=crop_0,0,262,328"/></c:otherwise>
            </c:choose>
        </c:if>
        <a href="${url}"><img class="full-width img-responsive" src="${imageUrl}" alt="<c:out value="${product.name}" escapeXml="true"/>"></a>
        <a class="product-review" href="${url}">Quick review</a>
        <%--<a class="add-to-cart" href="#"><i class="fa fa-shopping-cart"></i>Add to cart</a>--%>
        <c:if test="${product.newProduct == 'Y'}">
            <div class="shop-rgba-dark-green rgba-banner">Sản Phẩm Mới</div>
        </c:if>
    </div>
    <div class="product-description product-description-brd margin-bottom-30" id="p-${product.id}">
        <div class="overflow-h margin-bottom-5">
            <div class="">
                <h4 class="title-price"><a href="${url}"><c:out value="${product.name}" escapeXml="true"/></a></h4>
                <%--<span class="gender text-uppercase">Men</span>--%>
                <%--<span class="gender">Suits - Blazers</span>--%>
            </div>
            <div class="product-price">
                <spring:eval expression="T(com.easysoft.ecommerce.util.MoneyRange).valueOf(product.displayPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="money" />
                <spring:eval expression="T(com.easysoft.ecommerce.util.MoneyRange).valueOf(product.displayPricePromo,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="promo" />
                <c:if test="${!empty promo}">
                    <c:if test="${promo == money}">
                        <span class="title-price">${money}</span>
                    </c:if>
                    <c:if test="${promo != money}">
                        <span class="title-price">${promo}</span>
                        <span class="title-price line-through">${money}</span>
                    </c:if>
                </c:if>
                <c:if test="${empty promo}">
                    <span class="title-price">${money}</span>
                </c:if>
            </div>
        </div>
    </div>
</div>
