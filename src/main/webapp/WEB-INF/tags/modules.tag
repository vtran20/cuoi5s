<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>
<%@ attribute name="isLoggedIn" required="true" rtexprvalue="true" type="java.lang.Boolean"%>
<%@ attribute name="clientSiteId" required="false" rtexprvalue="true" type="java.lang.Long"%>
<%@ attribute name="popup" required="false" rtexprvalue="true" type="java.lang.String"%>
<c:choose>
    <c:when test="${clientSiteId > 0 && isLoggedIn}">
        <spring:eval expression="serviceLocator.getProductDao().getAddedModules(site.id, clientSiteId)" var="addedProducts" />
        <spring:eval expression="serviceLocator.getProductDao().getNotAddedModules(site.id, clientSiteId)" var="notAddedProducts" />
        <div class="">
            <h:frontendmessage _messages="${messages}"/>
            <div  id="modal_message_alert"></div>
            <c:if test="${!empty addedProducts}">
                <div class="headline"><h2><fmt:message key='message.module.is.existed.in.site'/></h2></div>
            <div class="row  margin-bottom-30">
                <c:forEach var="product" items="${addedProducts}">
                    <div class="col-sm-2 col-xs-6 sm-margin-bottom-30">
                        <span>${product.imageUrl}</span>
                        <p class="padding-top-5">${product.name}</p>
                        <ul class="list-inline shop-product-prices margin-bottom-10">
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
                        </ul>
                        <c:choose>
                            <c:when test="${popup == 'Y'}">
                                <button type="button" class="btn-u show-confirm-modal" data-message="<fmt:message key='site.do.you.want.remove.this.module'/>" data-href="/site/checkout/removemodulefromcart.html?productId=${product.id}&thisSiteId=${clientSiteId}"><fmt:message key="product.remove.modules"/></button>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btn-u show-confirm" data-message="<fmt:message key='site.do.you.want.remove.this.module'/>" data-href="/site/checkout/removemodulefromsite.html?productId=${product.id}&thisSiteId=${clientSiteId}"><fmt:message key="product.remove.modules"/></button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>
            <hr>
            </c:if>
            <c:if test="${!empty notAddedProducts}">
                <div class="headline"><h2><fmt:message key='message.module.isnot.existed.in.site'/></h2></div>
            <div class="row  margin-bottom-30">
                <c:forEach var="product" items="${notAddedProducts}">
                    <div class="col-sm-2 col-xs-6 sm-margin-bottom-30">
                        <span>${product.imageUrl}</span>
                        <p class="padding-top-5">${product.name}</p>
                        <ul class="list-inline shop-product-prices margin-bottom-10">
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
                        <c:choose>
                            <c:when test="${popup == 'Y'}">
                                <button type="button" class="btn-u show-confirm-modal" data-message="<fmt:message key='site.do.you.want.add.this.module'/>" data-href="/site/checkout/addmoduletocart.html?productId=${product.id}&thisSiteId=${clientSiteId}"><fmt:message key="product.add.modules"/></button>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btn-u show-confirm" data-message="<fmt:message key='site.do.you.want.add.this.module'/>" data-href="/site/checkout/addmoduletosite.html?productId=${product.id}&thisSiteId=${clientSiteId}"><fmt:message key="product.add.modules"/></button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>
            </c:if>
        </div>
    </c:when>
    <c:otherwise>
        <spring:eval expression="serviceLocator.getProductDao().getModules(site.id)" var="products" />
        <div class="container">
            <div class="headline"><h2><fmt:message key='message.modules.available'/></h2></div>
            <div class="row  margin-bottom-30">
                <c:forEach var="product" items="${products}">
                    <div class="col-sm-2 col-xs-6 sm-margin-bottom-30">
                        <span>${product.imageUrl}</span>
                        <p class="padding-top-5">${product.name}</p>
                        <ul class="list-inline shop-product-prices margin-bottom-10">
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
                        <c:choose>
                            <c:when test="${isLoggedIn}">
                                <button type="button" class="btn-u site-select" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="site.select.site"/>" data-page="/site/select_site.html?productId=${product.id}" data-target="#modal-form"><fmt:message key="product.add.modules"/></button>
                            </c:when>
                            <c:otherwise>
                                <a class="btn-u" href="/site/login.html"><fmt:message key="product.add.modules"/></a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<h:front_modal modalSize="modal-sm"/>
<script type="text/javascript">
    $(document).ready(function () {
        $("body").on("click", ".show-confirm", function () {
            var object = $(this);
            BootstrapDialog.show({
                title: '<fmt:message key="common.confirm.title"/>',
                message: object.attr("data-message"),
                buttons: [{
                    label: 'Yes',
                    action: function(dialog) {
                        dialog.close();
                        window.location.href = object.attr("data-href");
                    }
                }, {
                    label: 'No',
                    action: function(dialog) {
                        dialog.close();
                    }
                }]
            });

        });
        $("body").on("click", ".show-confirm-modal", function () {
            var object = $(this);
            BootstrapDialog.show({
                title: '<fmt:message key="common.confirm.title"/>',
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
                                $('#modal_message_alert').html(data);
                                $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                    $("#modal_message_alert").alert('close');
                                });
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

    });

//    $("button.add-module").on("click", function () {
//        var object = $(this);
//        addUrl=object.attr("data-href");
//        document.location.href=addUrl;
//    });
//    $("button.site-select").on("click", function () {
//        var object = $(this);
//        addUrl=object.attr("data-page");
//        document.location.href=addUrl;
//    });
    $("select.show-confirm").on('change', function() {
        var object = $(this);
        siteId=object.val();
        document.location.href="/site/modules.html?thisSiteId="+siteId;
    });

</script>
