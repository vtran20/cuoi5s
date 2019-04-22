<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="billing.shipping.info"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<h:authenticate request="${pageContext.request}" response="${pageContext.response}"/>
<spring:eval expression="sessionObject.getString('UPDATE_CURRENT_SITE_ID')" var="siteId"/>
<fmt:parseNumber var = "siteId" type = "number" value = "${siteId}" integerOnly = "true" />
<spring:eval expression="serviceLocator.getSiteDao().findById(siteId)" var="thisSite"/>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>

<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.data.general.info"/></h1>
        <ul class="pull-right breadcrumb">
            <li><a href="/">Home</a></li>
            <li><a href="/site/mysites.html">My Sites</a></li>
            <li class="active"><fmt:message key="site.data.general.info"/></li>
        </ul>
    </div>
</div>
<div class="container content">
<div class="row">
    <jsp:include page="leftnav.jsp"/>
    <c:if test="${empty store}">
        <spring:eval expression="serviceLocator.getNailStoreDao().findAll(sessionObject.UPDATE_CURRENT_SITE_ID)" var="stores"/>
        <c:if test="${fn:length(stores) >= 1}">
            <c:set var="store" value="${stores[0]}"/>
        </c:if>
    </c:if>
<!-- Begin Content -->
<div class="col-md-9">
    <!-- Begin Sidebar Menu -->
    <div class="panel panel-red margin-bottom-40">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.data.general.information"/></h3>
        </div>
        <div class="panel-body">
            <form class="form-horizontal" role="form" action="/site/data/create-update-store.html" id="form" method="post">
                <%--<input type="hidden" name="siteId" value="${sessionObject.UPDATE_CURRENT_SITE_ID}">--%>
                <input name="id" type="hidden" value="${store.id}">
                <input name="country" type="hidden" value="US">
                <h:frontendmessage _messages="${messages}"/>
                <div class="col-md-6">
                    <div class="headline"><h4><fmt:message key="site.data.salon.infor"/></h4></div>
                    <div class="form-group">
                        <label for="name" class="col-lg-3 control-label"><fmt:message key="site.data.salon.name"/></label>
                        <div class="col-lg-9">
                            <input id="name" type="text" placeholder="<fmt:message key="site.data.salon.name"/>" name="name" class="form-control required" value="${store.name}" maxlength="60">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="address_1" class="col-lg-3 control-label"><fmt:message key="site.data.address.address"/></label>
                        <div class="col-lg-9">
                            <input id="address_1" type="text" placeholder="<fmt:message key="site.data.address.address"/>" name="address_1" class="form-control required" value="${store.address_1}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="city" class="col-lg-3 control-label"><fmt:message key="site.data.address.city"/></label>
                        <div class="col-lg-9">
                            <input id="city" type="text" placeholder="<fmt:message key="site.data.address.city"/>" name="city" class="form-control required" value="${store.city}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label" for="state"><fmt:message key="site.data.address.state"/></label>
                        <div class="col-lg-9">
                            <h:stringparamselector name="state" stringParam="USA_STATE" defaultValue="${store.state}" includeTitle="Select State" styleClass="form-control required"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="zipCode" class="col-lg-3 control-label"><fmt:message key="site.data.address.zipcode"/></label>
                        <div class="col-lg-9">
                            <input id="zipCode" type="text" placeholder="<fmt:message key="site.data.address.zipcode"/>" name="zipCode" class="form-control required" value="${store.zipCode}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="latitude" class="col-lg-3 control-label"><fmt:message key="site.data.address.latitude"/></label>
                        <div class="col-lg-9">
                            <input id="latitude" type="text" placeholder="<fmt:message key="site.data.address.latitude"/>" name="latitude" class="form-control required" value="${store.latitude}">
                            <small class="form-text text-muted"><fmt:message key="site.data.lat.long"/></small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="longitude" class="col-lg-3 control-label"><fmt:message key="site.data.address.longitude"/></label>
                        <div class="col-lg-9">
                            <input id="longitude" type="text" placeholder="<fmt:message key="site.data.address.longitude"/>" name="longitude" class="form-control required" value="${store.longitude}">
                            <small class="form-text text-muted"><fmt:message key="site.data.lat.long"/></small>
                        </div>
                    </div>
                    <%--<div class="form-group">--%>
                        <%--<label for="country" class="col-lg-3 control-label"><fmt:message key="site.data.address.country"/></label>--%>
                        <%--<div class="col-lg-9">--%>
                            <%--&lt;%&ndash;<input id="country" type="text" placeholder="<fmt:message key="site.data.address.country"/>" name="country" class="form-control required" value="${store.country}">&ndash;%&gt;--%>
                            <%--<select id="country" class="form-control required" name="country">--%>
                                <%--<option value="US" ${store.country == 'US'? 'selected' : ''}>USA</option>--%>
                                <%--<option value="CA" ${store.country == 'CA'? 'selected' : ''}>Canada</option>--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="headline"><h4>Contact Us</h4></div>
                    <div class="form-group">
                        <label for="phone" class="col-lg-3 control-label"><fmt:message key="site.data.address.phone"/></label>
                        <div class="col-lg-9">
                            <c:if test="${!empty store.phone}">
                                <c:set var="phoneFormat" value="(${fn:substring(store.phone, 0, 3)}) ${fn:substring(store.phone, 3, 6)}-${fn:substring(store.phone, 6, fn:length(store.phone))}"/>
                            </c:if>
                            <input type="text" class="form-control required" id="phone" name="phone" maxlength="20" placeholder="(xxx) xxx-xxxx" value="${phoneFormat}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-lg-3 control-label"><fmt:message key="site.data.address.email"/></label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control required" id="email" name="email" maxlength="20" placeholder="<fmt:message key="site.data.address.email"/>" value="${store.email}">
                        </div>
                    </div>
                    <%--<div class="headline"><h4>Follow Us</h4></div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="facebook" class="col-lg-3 control-label"><fmt:message key="site.data.followus.facebook"/></label>--%>
                    <%--<div class="col-lg-9">--%>
                    <%--<input id="facebook" type="text" placeholder="<fmt:message key="site.data.followus.facebook"/>" name="facebook" class="form-control required" value="${user.address_1}">--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="yelp" class="col-lg-3 control-label"><fmt:message key="site.data.followus.yelp"/></label>--%>
                    <%--<div class="col-lg-9">--%>
                    <%--<input id="yelp" type="text" placeholder="<fmt:message key="site.data.followus.yelp"/>" name="yelp" class="form-control required" value="${user.address_1}">--%>
                    <%--</div>--%>
                    <%--</div>--%>

                </div>
                <div class="col-md-6">
                    <div class="headline"><h4><fmt:message key="images.upload.logo"/></h4></div>
                    <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'LOGO_IMAGE', thisSite.id)" var="logoImg"/>
                    <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'LOGO_CROP', thisSite.id)" var="logoCrop"/>
                    <div class="col-md-6">
                        <a class="btn btn-success hidden-sm hidden-xs margin-bottom-20" href="#image-modal-form" data-id="${thisSite.id}" data-img="${logoImg.value}" data-crop="${logoCrop.value}" role="button" data-toggle="modal" data-target="#image-modal-form" data-backdrop="static" data-keyboard="false">
                            <i class="fa fa-cloud-upload"></i> <fmt:message key="images.upload.logo"/>
                        </a>
                        <label class="caption margin-bottom-20">
                            <input id=fileupload type="file" name="content-image" class="btn btn-xs btn-success hidden-md hidden-lg">
                        </label>
                    </div>
                    <div id="logo-image" class="col-md-6">
                        <c:if test="${!empty logoImg.value}">
                            <c:if test="${! empty logoCrop.value}"><c:set value="op=crop|${logoCrop.value}" var="opCrop"/></c:if>
                            <img src="${imageServer}/get/${logoImg.value}.png?${opCrop}&op=scale|x60"/>
                        </c:if>
                    </div>

                    <div class="headline"><h4>Opening Hours</h4></div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">
                            <fmt:message key="site.data.hours.monday"/>
                        </label>
                        <div>
                            <div class="col-md-4">
                                <h:hoursselector name="mondayOpenHour" defaultValue="${store.hourMon}" includeTitle="From" styleClass="form-control"/>
                            </div>
                            <div class="col-md-1">
                                -
                            </div>
                            <div class="col-md-4">
                                <h:hoursselector name="mondayCloseHour" defaultValue="${store.hourMon}" includeTitle="To" styleClass="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">
                            <fmt:message key="site.data.hours.tuesday"/>
                        </label>
                        <div>
                            <div class="col-md-4">
                                <h:hoursselector name="tuesdayOpenHour" defaultValue="${store.hourTue}" includeTitle="From" styleClass="form-control"/>
                            </div>
                            <div class="col-md-1">
                                -
                            </div>
                            <div class="col-md-4">
                                <h:hoursselector name="tuesdayCloseHour" defaultValue="${store.hourTue}" includeTitle="To" styleClass="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">
                            <fmt:message key="site.data.hours.wednesday"/>
                        </label>
                        <div>
                            <div class="col-md-4">
                                <h:hoursselector name="wednesdayOpenHour" defaultValue="${store.hourWed}" includeTitle="From" styleClass="form-control"/>
                            </div>
                            <div class="col-md-1">
                                -
                            </div>
                            <div class="col-md-4">
                                <h:hoursselector name="wednesdayCloseHour" defaultValue="${store.hourWed}" includeTitle="To" styleClass="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">
                            <fmt:message key="site.data.hours.thursday"/>
                        </label>
                        <div>
                            <div class="col-md-4">
                                <h:hoursselector name="thursdayOpenHour" defaultValue="${store.hourThu}" includeTitle="From" styleClass="form-control"/>
                            </div>
                            <div class="col-md-1">
                                -
                            </div>
                            <div class="col-md-4">
                                <h:hoursselector name="thursdayCloseHour" defaultValue="${store.hourThu}" includeTitle="To" styleClass="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">
                            <fmt:message key="site.data.hours.friday"/>
                        </label>
                        <div>
                            <div class="col-md-4">
                                <h:hoursselector name="fridayOpenHour" defaultValue="${store.hourFri}" includeTitle="From" styleClass="form-control"/>
                            </div>
                            <div class="col-md-1">
                                -
                            </div>
                            <div class="col-md-4">
                                <h:hoursselector name="fridayCloseHour" defaultValue="${store.hourFri}" includeTitle="To" styleClass="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">
                            <fmt:message key="site.data.hours.saturday"/>
                        </label>
                        <div>
                            <div class="col-md-4">
                                <h:hoursselector name="saturdayOpenHour" defaultValue="${store.hourSat}" includeTitle="From" styleClass="form-control"/>
                            </div>
                            <div class="col-md-1">
                                -
                            </div>
                            <div class="col-md-4">
                                <h:hoursselector name="saturdayCloseHour" defaultValue="${store.hourSat}" includeTitle="To" styleClass="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">
                            <fmt:message key="site.data.hours.sunday"/>
                        </label>
                        <div>
                            <div class="col-md-4">
                                <h:hoursselector name="sundayOpenHour" defaultValue="${store.hourSun}" includeTitle="From" styleClass="form-control"/>
                            </div>
                            <div class="col-md-1">
                                -
                            </div>
                            <div class="col-md-4">
                                <h:hoursselector name="sundayCloseHour" defaultValue="${store.hourSun}" includeTitle="To" styleClass="form-control"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-12">
                        <button type="submit" class="btn-u btn-u-red"><fmt:message key="common.save.changes"/></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- End Content -->
</div>
</div>

<script type="text/javascript">
    //Sidebar Navigation Toggle
    jQuery('.list-toggle').on('click', function() {
        jQuery(this).toggleClass('active');
    });
    $(function () {
        $('#phone').usPhoneFormat({
            format: '(xxx) xxx-xxxx'
        });

        $("#form").validate({
            rules:{
                name:"required",
                address_1:"required",
                city:"required",
                state:"required",
                zipCode:"required"

            },

            messages:{
                <%--firstName:"<fmt:message key="site.register.firstnameisrequied"/>",--%>
                <%--lastName:"<fmt:message key="site.register.lastnameisrequired"/>",--%>
                <%--address_1:"<fmt:message key="site.register.address"/>",--%>
                <%--city:"<fmt:message key="site.register.city"/>",--%>
                <%--phone:"<fmt:message key="site.register.phoneisrequired"/>"--%>

            },
            errorPlacement: function(error, element) {
                if (element.attr("name") == "partner") {
                    error.insertAfter("#errormessagehere");
                } else {
                    error.insertAfter(element);
                }
            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });
    })
    function callbackFromImageModal (image, button, imageUrl, imageUri) {
        console.log(imageUrl)
        $(button).data("crop", Math.round(image.x)+','+Math.round(image.y)+','+Math.round(image.width)+','+Math.round(image.height))
        //Update crop data for the image if any or insert if it is the new one
        //Update/Insert crop image into database
        $.ajax({
            type: "GET",
            url: '/site/data/update_logo_image.html?id=${thisSite.id}&crop='+$(button).data("crop")+'&imgUrl='+imageUri,
            success: function(data)
            {
                if (data == "ok") {
                    var newImageUrl = $(button).data("img")+'?op=scale|220'
                    //rebuild a new url with crop
                    $("#logo-image").html('<img src="'+newImageUrl+'"/>')
                }
            }
        });

    }
</script>
<h:image_modal_front uploadable="true" thisSite="${thisSite}"/>
</body>
</html>
