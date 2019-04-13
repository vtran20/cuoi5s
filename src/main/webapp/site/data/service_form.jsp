<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="storeId" value="${param.storeId}"/>
<c:set var="groupId" value="${param.groupId}"/>
<c:set var="isService" value="${param.isService}"/>
<c:if test="${!empty param.id}">
    <c:set var="id" value="${param.id}"/>
    <spring:eval expression="serviceLocator.getNailServiceDao().findById(T(java.lang.Long).valueOf(id))" var="service"/>
    <c:if test="${!empty groupId || isService == 'true'}">
        <spring:eval expression="sessionObject.getString('UPDATE_CURRENT_SITE_ID')" var="siteId"/>
        <fmt:parseNumber var = "siteId" type = "number" value = "${siteId}" integerOnly = "true" />
        <spring:eval expression="serviceLocator.getSiteDao().findById(siteId)" var="thisSite"/>
        <spring:eval
                expression="T(com.easysoft.ecommerce.util.Money).valueOf((service.price/100),thisSite.siteParamsMap.get('CURRENCY'), thisSite.siteParamsMap.get('CURRENCY_FORMAT')).getMoneyValue()"
                var="servicePrice"/>
    </c:if>
</c:if>
<spring:eval expression="serviceLocator.getNailServiceDao().getGroupServices(T(java.lang.Long).valueOf(storeId))" var="groups"/>

<div class="row">
    <div id="modal_message_alert"></div>
    <form class="margin-bottom-0" role="form" id="formGroup" action="#">
        <input type="hidden" name="storeId" value="${storeId}">
        <input type="hidden" name="id" value="${id}">
        <c:choose>
            <%--This is for service--%>
            <c:when test="${!empty groupId || isService == 'true'}">
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="serviceName"><fmt:message key="site.data.service.name"/></label>
                        <input type="text" class="form-control required" name="name" id="serviceName" value="${service.name}" placeholder="<fmt:message key="site.data.service.name"/>"/>
                    </div>
                </div>
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="groupId"><fmt:message key="site.data.group.name"/></label>
                        <select name="groupId" id="groupId" class="form-control required">
                            <option value="">--Select Group--</option>
                            <c:forEach var="group" items="${groups}">
                                <option value="${group.id}" ${groupId == group.id? 'selected' : ''}>${group.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="price"><fmt:message key="site.data.service.price"/></label>
                        <input type="text" class="form-control required number" name="price" id="price" value="${servicePrice}" placeholder="<fmt:message key="site.data.service.price"/>"/>
                    </div>
                </div>
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="minutes"><fmt:message key="site.data.service.minutes"/></label>
                        <input type="text" class="form-control required number" name="minutes" id="minutes" value="${service.minutes}" placeholder="<fmt:message key="site.data.service.minutes"/>"/>
                    </div>
                </div>
            </c:when>
            <%--This is for group--%>
            <c:otherwise>
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="name"><fmt:message key="site.data.group.name"/></label>
                        <input type="text" class="form-control required" name="name" id="name" value="${service.name}" placeholder="<fmt:message key="site.data.group.name"/>"/>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="col-md-12">
            <button type="button" class="btn-u btn-u-default" data-dismiss="modal">Close</button>
            <button type="button" id="submit-group" class="btn-u btn-u-primary"><fmt:message key="common.save.changes"/></button>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        $("#formGroup").validate({
            rules:{
                minutes: 'number'
            },
            messages:{
            },
            //ignore:"", //To allow validation of hidden elements, override the ignore and set it to empty string:
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });
        $('body').on('click', '#submit-group', function() {
            var url = "/site/data/update_edit_group_service.html"; // the script where you handle the form input.
            var form = $( "#formGroup" );
            if (form.valid()) {

                var formData = $('#formGroup').serializeArray();
                // Find and replace `price`. Convert from float to cents
                for (index = 0; index < formData.length; ++index) {
                    if (formData[index].name == "price") {
                        var cents = formData[index].value * 100;
                        cents = parseInt(cents.toFixed(0))
                        formData[index].value = cents;
                        break;
                    }
                }
                console.log(formData)
                formData = $.param(formData);

                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData, // serializes the form's elements.
                    success: function(data)
                    {
                        $('#modal_message_alert').html(data);
                        $("#modal_message_alert").fadeTo(5000, 500).slideUp(500, function(){
                            $("#modal_message_alert").alert('close');
                        });
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            }
        })
    })
</script>