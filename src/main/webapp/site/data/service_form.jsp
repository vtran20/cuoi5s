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
<style>
    /*.form-group div.row {*/
        /*width: 84px;*/
        /*height: 44px;*/
        /*float: left;*/
        /*border: 2px solid #fff;*/
        /*margin: 2px 2px 2px 2px;*/
        /*text-align: center;*/
    /*}*/
    .form-group div.row img {
        border: 2px solid #fff;
    }
    .form-group div.row img:hover {
        border: 2px solid red;
    }
    .form-group div.row img.select-active {
        border: solid 2px red;
    }
</style>
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
                    <div class="form-group">
                        <label for="imageUrl"><fmt:message key="site.data.group.image"/></label>
                        <input type="hidden" class="form-control required" name="imageUrl" id="imageUrl" value="${service.imageUrl}"/>
                        <input type="hidden" class="" name="crop" id="crop" value="${service.crop}"/>
                        <div class="row">
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/3aa7796e-6141-4f97-9d59-85ad6ed736f2.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/3aa7796e-6141-4f97-9d59-85ad6ed736f2.jpg?op=crop|0,17,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/3aa7796e-6141-4f97-9d59-85ad6ed736f2.jpg" data-crop="0,17,2200,1432"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/e9534284-9bf9-48f2-ad87-76add14fb181.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/e9534284-9bf9-48f2-ad87-76add14fb181.jpg?op=crop|15,0,1954,1272&op=scale|400x" data-img="http://image.mangchiase.com/get/e9534284-9bf9-48f2-ad87-76add14fb181.jpg" data-crop="15,0,1954,1272"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/7eadc87a-c819-428a-804d-f38ae742409a.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/7eadc87a-c819-428a-804d-f38ae742409a.jpg?op=crop|0,20,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/7eadc87a-c819-428a-804d-f38ae742409a.jpg" data-crop="0,20,2200,1432"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/9c2e026d-43e2-493a-a0d8-005a9b2f43db.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/9c2e026d-43e2-493a-a0d8-005a9b2f43db.jpg?op=crop|2,37,2198,1430&op=scale|400x" data-img="http://image.mangchiase.com/get/9c2e026d-43e2-493a-a0d8-005a9b2f43db.jpg" data-crop="2,37,2198,1430"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/25b24315-8936-40f2-9e87-34e143ccd62d.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/25b24315-8936-40f2-9e87-34e143ccd62d.jpg?op=crop|0,19,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/25b24315-8936-40f2-9e87-34e143ccd62d.jpg" data-crop="0,19,2200,1432"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/c1a9485b-5b50-487d-aee3-c0baf7b81f0e.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/c1a9485b-5b50-487d-aee3-c0baf7b81f0e.jpg?op=crop|0,1183,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/c1a9485b-5b50-487d-aee3-c0baf7b81f0e.jpg" data-crop="0,1183,2200,1432"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/741dedc3-d1f3-468b-b5ff-32d423c99491.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/741dedc3-d1f3-468b-b5ff-32d423c99491.jpg?op=crop|0,223,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/741dedc3-d1f3-468b-b5ff-32d423c99491.jpg" data-crop="0,223,2200,1432"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/836c0f8f-658f-48db-8361-bb2e6c53081e.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/836c0f8f-658f-48db-8361-bb2e6c53081e.jpg?op=crop|0,24,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/836c0f8f-658f-48db-8361-bb2e6c53081e.jpg" data-crop="0,24,2200,1432"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/f7ece3f3-5725-449a-b805-d4b671a94510.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/f7ece3f3-5725-449a-b805-d4b671a94510.jpg?op=crop|0,18,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/f7ece3f3-5725-449a-b805-d4b671a94510.jpg" data-crop="0,18,2200,1432"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/58477533-79dd-4825-acb9-61f5960c1b5d.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/58477533-79dd-4825-acb9-61f5960c1b5d.jpg?op=crop|0,27,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/58477533-79dd-4825-acb9-61f5960c1b5d.jpg" data-crop="0,27,2200,1432"/></div>
                            <div class="col-lg-4 col-md-4 col-sm-6"><img class="img-responsive margin-bottom-5 ${service.imageUrl =='http://image.mangchiase.com/get/e84bc993-08b8-4ba0-b4d5-19dda918d1f7.jpg'? 'select-active' : ''}" src="http://image.mangchiase.com/get/e84bc993-08b8-4ba0-b4d5-19dda918d1f7.jpg?op=crop|0,19,2200,1432&op=scale|400x" data-img="http://image.mangchiase.com/get/e84bc993-08b8-4ba0-b4d5-19dda918d1f7.jpg" data-crop="0,19,2200,1432"/></div>
                        </div>
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
            messages:{
                name:"<fmt:message key="common.required"/>",
                imageUrl:"<fmt:message key="common.select.image.required"/>"
            },
            ignore:"", //To allow validation of hidden elements, override the ignore and set it to empty string:
            rules: {
                minutes: 'number'
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
        //Select Widget Template
        $(".form-group div.row img").on('click',function() {
            var object = $(this);
            $("input#imageUrl").val($(this).data("img"));
            $("input#crop").val($(this).data("crop"));
            $(".form-group div.row img").removeClass("select-active");
            object.addClass("select-active");
        });

    })
</script>