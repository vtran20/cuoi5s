<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>
<%@ attribute name="column" required="true" rtexprvalue="true" type="java.lang.Integer"%>
<c:choose>
    <c:when test="${column > 0}">
        <fmt:formatNumber var="number" value="${12/column}" maxFractionDigits="0" />
    </c:when>
    <c:otherwise>
        <fmt:formatNumber var="number" value="${12}" maxFractionDigits="0" />
    </c:otherwise>
</c:choose>
<app:cache key="sitetemplates${number}">
<spring:eval expression="serviceLocator.templateDao.findActiveByOrder('site.id', site.id, 'sequence')" var="templates"/>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<c:if test="${templates != null}">
    <div class="headline"><h2><fmt:message key="template.website.list"/></h2></div>
    <div class="row">
        <c:forEach items="${templates}" var="template">
            <spring:eval expression="serviceLocator.siteDao.findById(T(java.lang.Long).valueOf(template.siteSample.id))" var="siteSample"/>
            <div class="col-md-${number} col-sm-6">
                <div class="thumbnails thumbnail-style thumbnail-kenburn">
                    <div class="thumbnail-img">
                        <div class="overflow-hidden">
                            <c:choose>
                                <c:when test="${!empty template.crop}"><c:set var="imageQS" value="op=crop|${template.crop}&op=scale|400x"/></c:when>
                                <c:otherwise><c:set var="imageQS" value="op=scale|400x&op=crop|0,0,400,500"/></c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${fn:contains(template.imageUrl, imageServer)}"><img alt="" src="${template.imageUrl}?${imageQS}" class="img-responsive"></c:when>
                                <c:otherwise><img alt="" src="${imageServer}/get/${template.imageUrl}.jpg?${imageQS}" class="img-responsive"></c:otherwise>
                            </c:choose>
                        </div>
                        <%--<a href="#" class="btn-more hover-effect">read more +</a>--%>
                    </div>
                    <div class="caption">
                        <h3><a href="#" class="hover-effect">${template.templateModel}</a></h3>
                        <p>${template.name}</p>
                        <%--<p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>--%>
                        <p><a href="http://${siteSample.subDomain}/demo.html" target="_blank" class="btn-u btn-u-green btn-u-small"><fmt:message key="template.view.demo"/></a>&nbsp;<a href="#" hreflang="/site/select-template.html?templateId=${template.id}" class="btn-u btn-u-small show-confirm">Chọn&nbsp;mẫu</a></p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>
<script type="text/javascript">
    $(document).ready(function () {
        $(".show-confirm").click(function () {
            var object = $(this);
            BootstrapDialog.show({
                title: 'Xác nhận',
                message: '<fmt:message key='site.do.you.want.select.this.template'/>',
                buttons: [{
                    label: 'Yes',
                    action: function(dialog) {
                        dialog.close();
                        window.location.href = object.attr("hreflang");
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
</script>
</app:cache>