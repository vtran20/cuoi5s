<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="product" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.model.Product"%>
<%@ attribute name="productVariants" required="true" rtexprvalue="true" type="java.util.List"%>

<c:if test="${!empty productVariants}">

<script type="text/javascript">
    $(function() {
        $("#addToCart").validate({
            rules: {
                quantity: {
                    required: true,
                    min: 1
                }
            },
            messages: {
                quantity: {
                    required: "<fmt:message key="product.please.enter.number.item"/>",
                    min: "<fmt:message key="product.minimum.1"/>"
                },
                parent:  "<fmt:message key="product.selectbox.required"/>",
                productVariantId:  "<fmt:message key="product.selectbox.required"/>"

            }
        });
    });
    function subtractQty(){
        if(document.getElementById("quantity").value - 1 < 0)
            return;
        else
            document.getElementById("quantity").value--;
    }
</script>
<spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getVariantGroup(product, productVariants)" var="variantGroup"/>
<form:form id="addToCart" action="/checkout/addtocart.html" commandName="command" method="POST">
    <input type="hidden" name="productId" value="${product.id}"/> 
    <%--If have only one product variant--%>
    <c:if test="${fn:length(productVariants) == 1}">
        <div class="productId" id="widget-product-swatches">
            <spring:eval expression="productVariants.get(0)" var="pv"/>
            <input type="hidden" name="productVariantId" value="${pv.id}"/> 
        </div>
    </c:if>

    <%--If have more than one product variant--%>
    <c:if test="${fn:length(productVariants) > 1}">
        <c:if test="${variantGroup == 'C' || variantGroup == 'B'}">
            <script type="text/javascript">
            $(function() {

            function makeSublist(parent,child,isSubselectOptional,childVal)
            {
                $("body").append("<select style='display:none' id='"+parent+child+"'></select>");
                $('#'+parent+child).html($("#"+child+" option"));

                    var parentValue = $('#'+parent).attr('value');
                    $('#'+child).html($("#"+parent+child+" .sub_"+parentValue).clone());

                childVal = (typeof childVal == "undefined")? "" : childVal ;
                $("#"+child+' option[value="'+ childVal +'"]').attr('selected','selected');
                $('#'+child).prepend("<option value=''><fmt:message key="product.select.size"/></option>");
                //disable select child
                if (parentValue == 0) {
                    $("#"+child).attr('disabled', 'disabled');
                }
                $('#'+parent).change(
                    function()
                    {
                        $("#"+child).removeAttr('disabled');
                        var parentValue = $('#'+parent).attr('value');
                        $('#'+child).html($("#"+parent+child+" .sub_"+parentValue).clone());
                        if(isSubselectOptional) $('#'+child).prepend("<option value=''><fmt:message key="product.select.size"/></option>");
                        $('#'+child).trigger("change");
                        $('#'+child).focus();

                        if (parentValue == '') {
                            $("#"+child).attr('disabled', 'disabled');
                        } else {
                            $("#"+child).removeAttr('disabled');
                        }
                        $("#"+child+' option[value=]').attr('selected','selected');

                        //set color bar and image
                        $("#widget-product-swatches a").each(function() {
                            if ($(this).attr('id') == parentValue) {
                                $(this).removeClass("colorNotSelected").addClass("colorSelected");
                                <%--$('#izView img').attr('src', '${baseImageUrl}/product.image?timestamp=&path='+this.rel+'&resize.width=351&resize.keepRatio=true');--%>
                                <%--$('#photo1 img').attr('src', '${baseImageUrl}/product.image?timestamp=&path='+this.rel+'&resize.width=550&resize.keepRatio=true');--%>
                            } else {
                                $(this).removeClass("colorSelected").addClass("colorNotSelected");
                            }
                        });


                    }
                );
            }

                $('#singleColor').change(
                    function()
                    {
                        var parentValue = $('#singleColor option:selected').attr('id');
                        //set color bar and image
                        $("#widget-product-swatches a").each(function() {
                            if ($(this).attr('id') == parentValue) {
                                $(this).removeClass("colorNotSelected").addClass("colorSelected");
                                <%--$('#izView img').attr('src', '${baseImageUrl}/product.image?timestamp=&path='+this.rel+'&resize.width=351&resize.keepRatio=true');--%>
                                <%--$('#photo1 img').attr('src', '${baseImageUrl}/product.image?timestamp=&path='+this.rel+'&resize.width=550&resize.keepRatio=true');--%>
                            } else {
                                $(this).removeClass("colorSelected").addClass("colorNotSelected");
                            }
                        });


                    }
                );

                $(document).ready(function()
                {
                    //makeSublist('child','grandsun', true, '');
                    makeSublist('parent','child', true, '0');
                });
            });
                
            </script>
        <%--<div class="productId" id="widget-product-swatches">--%>
            <%--<c:forEach items="${productVariants}" var="pv" varStatus="pos">--%>
                <%--<c:set var="colorIcon" value="${baseImageUrl}/product.image?timestamp=&path=${pv.imageUrl}&resize.width=40&resize.keepRatio=true"/>--%>
                <%--<c:if test="${pos.index > 0 && pos.index % 6 == 0}">--%>
                    <%--<div class="clr"><!--  --></div>--%>
                <%--</c:if>--%>
            <%--<a href="#" id="${pv.colorCode}" class="colorNotSelected" &lt;%&ndash;style="background:${pv.colorCodeRBG};"&ndash;%&gt; rel="${pv.imageUrl}">--%>
                <%--<img alt="${pv.colorName}" src="${colorIcon}">--%>
            <%--</a>--%>
            <%--</c:forEach>--%>
        <%--</div>--%>
            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getColorProductVariant(productVariants)" var="pvs"/>
            <c:if test="${variantGroup == 'B'}">
                <h3 class="shop-product-title"><fmt:message key="product.color"/></h3>
                <div class="variant-color margin-bottom-30">
                    <select name="parent" id="parent" class="required input-lg">
                        <option value=''><fmt:message key="product.select.color"/></option>
                        <c:forEach items="${pvs}" var="map">
                            <option value="${map.key}" id="color_${map.key}">${map.value.colorName}</option>
                        </c:forEach>
                    </select>
                </div>

                <h3 class="shop-product-title"><fmt:message key="product.size"/></h3>
                <div class="variant-color margin-bottom-30">
                    <select id="child" name="productVariantId" class="required input-lg">
                        <c:forEach items="${productVariants}" var="pv">
                            <spring:eval
                                    expression="T(com.easysoft.ecommerce.util.Money).valueOf(T(com.easysoft.ecommerce.util.WebUtil).generatePriceMin(pv.price, pv.pricePromo),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()"
                                    var="priceItem"/>
                            <option class="sub_${pv.colorCode}" value="${pv.id}">${pv.sizeName} - ${priceItem}</option>
                        </c:forEach>
                    </select>
                </div>

            </c:if>
            <c:if test="${variantGroup == 'C'}">
                <h3 class="shop-product-title"><fmt:message key="product.color"/></h3>
                <div class="variant-color margin-bottom-30">
                    <select id="singleColor" name="productVariantId" class="required input-lg">
                        <option value=''><fmt:message key="product.select.color"/></option>
                        <c:forEach items="${productVariants}" var="pv">
                        <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(T(com.easysoft.ecommerce.util.WebUtil).generatePriceMin(pv.price, pv.pricePromo),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="priceItem"/>
                        <option value="${pv.id}" id="${pv.colorCode}">${pv.colorName} - ${priceItem}</option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>
        </c:if>
        <c:if test="${variantGroup == 'S'}">
            <h3 class="shop-product-title"><fmt:message key="product.size"/></h3>
            <div class="variant-color margin-bottom-30">
            <select id="child" name="productVariantId" class="required input-lg">
                <option value=''><fmt:message key="product.select.size"/></option>
                ${fn:length(productVariants)}${productVariants}
                <c:forEach items="${productVariants}" var="pv">
                <spring:eval expression="T(com.easysoft.ecommerce.util.Money).valueOf(T(com.easysoft.ecommerce.util.WebUtil).generatePriceMin(pv.price, pv.pricePromo),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).toString()" var="priceItem"/>
                <option value="${pv.id}">${pv.sizeName} - ${priceItem}</option>
                </c:forEach>
            </select>
            </div>
        </c:if>
    </c:if>

    <h3 class="shop-product-title"><fmt:message key="product.qty"/></h3>
    <div class="margin-bottom-40">
        <button type='button' class="quantity-button" name='subtract' onclick='javascript:subtractQty();' value='-'>-</button>
        <input type='text' class="quantity-field" name='quantity' value="1" id='quantity'/>
        <button type='button' class="quantity-button" name='add' onclick='javascript: document.getElementById("quantity").value++;' value='+'>+</button>
        <button type="submit" class="btn-u btn-u-sea-shop btn-u-lg"><fmt:message key="product.add.to.cart"/></button>
    </div><!--/end product quantity-->
</form:form>
</c:if>
<c:if test="${empty productVariants}">
    <div id="widget-product-swatches">
         <fmt:message key="product.out.of.stock"/>
    </div>

</c:if>