<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>


<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<app:cache key="${uri}|${pageContext.request.queryString}">
<c:set value="${fn:replace(uri,'/product/','')}" var="ids"/>
<c:set value="${fn:replace(ids,'.html','')}" var="ids"/>
<c:set value="${fn:split(ids,'/')}" var="ids"/>
<c:set value="${ids[fn:length(ids)-1]}" var="produri"/>
<c:set value="${fn:split(produri,'-')}" var="list"/>
<c:set value="${list[fn:length(list)-1]}" var="prodId"/>
<spring:eval expression="serviceLocator.getProductDao().findById(T(java.lang.Long).valueOf(prodId))" var="product"/>
<c:if test="${fn:length(ids) >= 2}">
    <c:set value="${ids[fn:length(ids)-2]}" var="subCatId"/>
    <c:set value="${fn:split(subCatId,'-')}" var="subCatUriList"/>
    <c:set value="${subCatUriList[fn:length(subCatUriList)-1]}" var="subCatId"/>
    <spring:eval expression="serviceLocator.getCategoryDao().findById(T(java.lang.Long).valueOf(subCatId))" var="subCat"/>
</c:if>

<c:if test="${fn:length(ids) >= 3}">
    <c:set value="${ids[fn:length(ids)-3]}" var="catId"/>
    <c:set value="${fn:split(catId,'-')}" var="catUriList"/>
    <c:set value="${catUriList[fn:length(catUriList)-1]}" var="catId"/>
    <spring:eval expression="serviceLocator.getCategoryDao().findById(T(java.lang.Long).valueOf(catId))" var="cat"/>
</c:if>
<c:if test="${fn:length(ids) >= 4}">
    <c:set value="${ids[fn:length(ids)-4]}" var="parCatId"/>
    <c:set value="${fn:split(parCatId,'-')}" var="parCatUriList"/>
    <c:set value="${parCatUriList[fn:length(parCatUriList)-1]}" var="parCatId"/>
    <spring:eval expression="serviceLocator.getCategoryDao().findById(T(java.lang.Long).valueOf(parCatId))" var="parCat"/>
</c:if>
<c:if test="${product != null}">
    <spring:eval expression="serviceLocator.getProductVariantDao().findProductVariantByColorSize(product.id, 'Y')" var="productVariants"/>
    <c:if test="${!empty productVariants}">
        <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getInventoryProductVariants(productVariants)" var="productVariants"/>
    </c:if>
</c:if>

<html>
<head>
    <title>${product.name} - ${product.model}</title>
    <meta name="title" content="${product.name} - ${product.model}"/>
    <meta name="description" content="${product.description}"/>
    <meta name="keywords" content="${product.description}"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<script type="text/javascript">
    $(function() {
        $("#widget-product-swatches a").click(function() {
            if ($('select#parent option[value='+this.id+']').length > 0) {
                $('select#parent option[value='+this.id+']').attr('selected','selected');
                $('#parent').change();
            }

            if ($('select#singleColor option[id='+this.id+']').length > 0) {
                $('select#singleColor option[id='+this.id+']').attr('selected','selected');
                $('#singleColor').change();

            }
        });
        
    });

</script>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<spring:eval expression="serviceLocator.productFileDao.findProductFiles(product.id, 'PRODUCT_FILE_IMAGE')" var="productFiles" />

<%--Sub Category    --%>
<c:set var="path" value="/category"/>
<c:if test="${!empty parCat}">
    <c:set value="${path}/${parCat.uri}-${parCat.id}" var="path"/>
</c:if>
<c:if test="${!empty cat}">
    <c:set value="${path}/${cat.uri}-${cat.id}" var="path"/>
</c:if>
<c:choose>
    <c:when test="${!empty subCat}">
        <c:set value="${path}/${subCat.uri}-${subCat.id}" var="path"/>
    </c:when>
    <c:otherwise>
        <spring:eval expression="serviceLocator.getCategoryDao().getSubCategory(product.id)" var="subCat"/>
        <c:set value="${path}/${subCat.uri}-${subCat.id}" var="path"/>
    </c:otherwise>
</c:choose>

<!--=== Shop Product ===-->
<div class="shop-product">
    <!-- Breadcrumbs v5 -->
    <div class="container">
        <ul class="breadcrumb-v5">
            <li><a href="/"><i class="fa fa-home"></i></a></li>
            <li><a href="${path}.html">${subCat.name}</a></li>
            <li class="active">${product.name}</li>
        </ul>
    </div>
    <!-- End Breadcrumbs v5 -->

    <div class="container">
        <div class="row">
            <div class="col-md-6 md-margin-bottom-50">
                <div id="myCarousel-1" class="carousel slide carousel-v1">
                    <div class="carousel-inner">
                        <c:choose>
                            <c:when test="${fn:length(productFiles) > 0}">
                                <c:forEach var="image" items="${productFiles}" varStatus="imageIndex">
                                    <c:set var="active" value=""/>
                                    <c:if test="${imageIndex.index == 0}">
                                        <c:set var="active" value="active"/>
                                    </c:if>
                                    <div class="item ${active}">
                                        <c:choose>
                                            <c:when test="${!empty image.crop}"><img src="${imageServer}/get/${image.uri}.jpg?op=crop_${image.crop}&op=scale_600" alt=""></c:when>
                                            <c:otherwise><img src="${imageServer}/get/${image.uri}.jpg?op=scale_600" alt=""></c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="item active">
                                    <img src="/assets/images/no_image.png" alt="">
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <c:if test="${fn:length(productFiles) > 1}">
                        <div class="carousel-arrow">
                            <a class="left carousel-control" href="#myCarousel-1" data-slide="prev">
                                <i class="fa fa-angle-left"></i>
                            </a>
                            <a class="right carousel-control" href="#myCarousel-1" data-slide="next">
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="col-md-6">
                <div class="shop-product-heading">
                    <h2>${product.name}</h2>
                    <ul class="list-inline shop-product-social">
                        <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                        <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                        <li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                        <li><a href="#"><i class="fa fa-pinterest"></i></a></li>
                    </ul>
                </div><!--/end shop product social-->

                <p>${product.description}</p><br>
                <ul class="list-unstyled specifies-list">
                    <c:if test="${!empty product.attribute1}"><li><i class="fa fa-caret-right"></i>${product.attribute1}</li></c:if>
                    <c:if test="${!empty product.attribute2}"><li><i class="fa fa-caret-right"></i>${product.attribute2}</li></c:if>
                    <c:if test="${!empty product.attribute3}"><li><i class="fa fa-caret-right"></i>${product.attribute3}</li></c:if>
                    <c:if test="${!empty product.attribute4}"><li><i class="fa fa-caret-right"></i>${product.attribute4}</li></c:if>
                    <c:if test="${!empty product.attribute5}"><li><i class="fa fa-caret-right"></i>${product.attribute5}</li></c:if>
                </ul>
                <ul class="list-inline shop-product-prices margin-bottom-30">
                    <spring:eval expression="T(com.easysoft.ecommerce.util.MoneyRange).valueOf(product.displayPrice,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="money" />
                    <spring:eval expression="T(com.easysoft.ecommerce.util.MoneyRange).valueOf(product.displayPricePromo,site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="promo" />
                    <c:if test="${!empty promo}">
                        <c:if test="${promo == money}">
                            <li class="shop-red">${money}</li>
                        </c:if>
                        <c:if test="${promo != money}">
                            <li class="shop-red">${promo}</li>
                            <li class="line-through">${money}</li>
                        </c:if>
                    </c:if>
                    <c:if test="${empty promo}">
                    <li class="shop-red">${money}</li>
                    </c:if>
                    <%--<li><small class="shop-bg-red time-day-left">4 days left</small></li>--%>
                </ul><!--/end shop product prices-->
                    <h:prodvariant product="${product}" productVariants="${productVariants}"/>
            </div>
        </div><!--/end row-->
    </div>
</div>
<!--=== End Shop Product ===-->
</body>
</html>
</app:cache>