<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<%--<app:cache key="${uri}">--%>
<html>
<c:set value="${fn:substring(uri, 1, fn:length(uri))}" var="uri"/>
<%--<c:set value="${fn:split(temp,'/')}" var="ids"/>--%>
<%--<c:set value="${ids[fn:length(ids)-1]}" var="id"/>--%>
<spring:eval expression="serviceLocator.getNailStoreDao().findActiveByOrder('active', 'Y', null, site.id)" var="stores"/>
<c:if test="${!empty stores && fn:length(stores) == 1}">
    <c:set var="store" value="${stores[0]}"/>
    <spring:eval expression="serviceLocator.getNailServiceDao().getGroupServices(store.id)" var="groupServices"/>
    <spring:eval expression="serviceLocator.getNailEmployeeDao().findAllByStore(store.id)" var="employees"/>
</c:if>

<head>
    <title><c:out value="${site.name} | ${menu.name}"/></title>
    <meta name="description" content="<c:out value="${site.name} | ${menu.name}"/>"/>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<style>
    #serviceId {
        position: inherit!important;
    }
</style>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left">Booking</h1>
        <ul class="pull-right breadcrumb">
            <li><a href="index.html">Home</a></li>
            <li class="active">Booking</li>
        </ul>
    </div>
</div>

<div class="container content">
    <div class="row booking-page">
        <form class="margin-bottom-40" role="form" id="form">
            <c:if test="${!empty stores && fn:length(stores) == 1}">
                <c:set var="store" value="${stores[0]}"/>
                <input type="hidden" name="storeId" id="storeId" value="${store.id}"/>
            </c:if>
            <c:if test="${!empty stores && fn:length(stores) >= 2}">
                <div class="col-md-4 col-xs-12">
                    <div class="form-group">
                        <label for="storeId">Location</label>
                        <select name="storeId" class="form-control" id="storeId">
                            <option value="">Select Location</option>
                            <c:forEach items="${stores}" var="store">
                                <option value="${store.id}">${store.name}: ${store.address_1} ${store.city}, ${store.state} ${store.zipCode}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </c:if>
            <div class="col-md-4 col-xs-12">
                <div class="form-group">
                    <label for="date">Select Date</label>
                    <input type="date" name="date" class="form-control" id="date" placeholder="Select Date">
                </div>
            </div>
            <div class="col-md-4 col-xs-12">
                <div class="form-group">
                    <label for="employeeId">Select Technician</label>
                    <select name="employeeId" class="form-control" id="employeeId">
                        <option value="0">Any Technicians</option>
                        <c:forEach items="${employees}" var="employee">
                            <option value="${employee.id}">${employee.firstName} ${employee.lastName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-md-4 col-xs-12">
                <div class="form-group">
                    <label for="serviceId">Select Service</label>
                    <select name="serviceId" class="form-control" id="serviceId" multiple="multiple">
                        <%--<option value="">Select Service</option>--%>
                        <c:forEach items="${groupServices}" var="group">
                            <optgroup label="${group.name}">
                                <!--List service-->
                                <spring:eval expression="serviceLocator.getNailServiceDao().getServices(group.id, store.id)" var="services"/>
                                <c:forEach items="${services}" var="service">
                                    <option value="${service.id}">${service.name}</option>
                                </c:forEach>
                            </optgroup>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <button type="button" class="btn-u" id="search-timeslot-button">Search</button>
            </div>
        </form>
    </div><!--/booking-page-->
    <div class="row col-md-12 margin-top-20" id="display-timeslot-result">
    </div>

</div>

<div class="modal fade" id="responsive-booking" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel4">Make Appointment</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div  id="modal_message_alert"></div>
                    <form class="margin-bottom-40" role="form" id="formModal">
                        <input type="hidden" name="selectedStoreId" id="selectedStoreId" value=""/>
                        <input type="hidden" name="selectedDate" id="selectedDate" value=""/>
                        <input type="hidden" name="selectedTime" id="selectedTime" value=""/>
                        <input type="hidden" name="selectedServiceId" id="selectedServiceId" value=""/>
                        <input type="hidden" name="selectedEmployeeId" id="selectedEmployeeId" value=""/>
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="form-group">
                                <label for="firstName"><fmt:message key="site.register.firstName"/></label>
                                <input type="text" class="form-control" name="firstName" id="firstName" value="" placeholder="<fmt:message key="site.register.firstName"/>"/>
                            </div>
                        </div>
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="form-group">
                                <label for="lastName"><fmt:message key="site.register.lastName"/></label>
                                <input type="text" class="form-control" name="lastName" id="lastName" value="" placeholder="<fmt:message key="site.register.lastName"/>"/>
                            </div>
                        </div>
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="form-group">
                                <label for="phone"><fmt:message key="site.register.phone"/></label>
                                <input type="text" class="form-control" name="phone" id="phone" value="" placeholder="(xxx) xxx-xxxx"/>
                            </div>
                        </div>
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="form-group">
                                <label for="email"><fmt:message key="site.register.email"/></label>
                                <input type="text" class="form-control" name="email" id="email" value="" placeholder="<fmt:message key="site.register.email"/>"/>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <div class="form-group">
                                <label for="message"><fmt:message key="booking.message.content"/></label>
                                <textarea class="form-control" id="message" name="message" rows="2" placeholder="<fmt:message key="booking.message.content"/>" maxlength="999"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-u btn-u-default" data-dismiss="modal">Close</button>
                <button type="button" id="submit-appointment" class="btn-u btn-u-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<script>
    function loadTimeslots(storeId, serviceId, employeeId, date) {
        var currentTime = new Date().getTime()
        var url = "/load-timeslots.json?storeId="+storeId+"&serviceId="+serviceId+"&employeeId="+employeeId+"&date="+date+"&currentDate="+currentTime;
        <%--url = url+"?start="+start+ "&catId=" + '${id}';--%>
        div = $("<div/>");
        div.append("<div class='headline'><h2 class='heading-sm'>Select Time Slot</h2></div>")
        $.getJSON( url, function( data ) {
            div1 = $("<div class='brand-page margin-bottom-40'/>");
            morning = data['morning']
            afternoon = data['afternoon']
            evening = data['evening']
            //morning
            if (morning && morning.length > 0) {
                div11 = $("<div class='row col-md-12'/>");
                div11.append("<h4>Morning</h4>")
                for(var i in morning) {
                    divTimeSlot = $("<div class='col-xs-6 col-sm-3 col-md-2'/>");
                    divTimeSlot.append("<button class='btn btn-block btn-amazon open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+morning[i]+"'>"+morning[i]+"</button>")
                    div11.append(divTimeSlot)
                }
                div1.append(div11)
            }
            //afternoon
            if (afternoon && afternoon.length > 0) {
                div11 = $("<div class='row col-md-12'/>");
                div11.append("<h4>Afternoon</h4>")
                for(var i in afternoon) {
                    divTimeSlot = $("<div class='col-xs-6 col-sm-3 col-md-2'/>");
                    divTimeSlot.append("<button class='btn btn-block btn-amazon open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+afternoon[i]+"'>"+afternoon[i]+"</button>")
                    div11.append(divTimeSlot)
                }
                div1.append(div11)
            }
            //evening
            if (evening && evening.length > 0) {
                div11 = $("<div class='row col-md-12'/>");
                div11.append("<h4>Evening</h4>")
                for(var i in evening) {
                    divTimeSlot = $("<div class='col-xs-6 col-sm-3 col-md-2'/>");
                    divTimeSlot.append("<button class='btn btn-block btn-amazon open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+evening[i]+"'>"+evening[i]+"</button>")
                    div11.append(divTimeSlot)
                }
                div1.append(div11)
            }

            div.append(div1)
        });
        $('#display-timeslot-result').html(div);

    }

    $(function () {
        // Initial date field.
        var now = new Date();
        var day = ("0" + now.getDate()).slice(-2);
        var month = ("0" + (now.getMonth() + 1)).slice(-2);
        var today = now.getFullYear()+"-"+(month)+"-"+(day) ;
        $("input[name='date']").val(today);

        $("#form").validate({
            rules:{
                storeId:"required",
                date: "required",
                serviceId:"required"
            },
            messages:{
                storeId:"<fmt:message key="booking.select.location"/>",
                date:"<fmt:message key="booking.select.date"/>",
                serviceId:"<fmt:message key="booking.select.service"/>"
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
        $("#formModal").validate({
            rules:{
                firstName:"required",
                lastName: "required",
                phone:"required",
                email:{
                    //required:true,
                    email:true
                }
            },
            messages:{
                firstName:"<fmt:message key="booking.enter.firstName"/>",
                lastName:"<fmt:message key="booking.enter.lastName"/>",
                phone:"<fmt:message key="booking.enter.phone"/>",
                email:{
                    email:"<fmt:message key="booking.enter.email.invalid"/>"
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

//        $('#form input[name="siteCode"]').blur(function () {
//            $("p.help-block b").text($(this).attr("value"));
//        });

        $('#search-timeslot-button').click(function() {
            if ($("#form").valid()) {
                var storeId = $("#storeId").val()
                var serviceId = $("#serviceId").val()
                var employeeId = $("#employeeId").val()
                var date = $("#date").val()
                loadTimeslots(storeId, serviceId, employeeId, date)
            }
        });

        $('#phone').usPhoneFormat({
            format: '(xxx) xxx-xxxx'
        });

        $('#serviceId').multiselect();

        $("body").on("click", "button.open-booking-modal", function () {
            $('#modal_message_alert').html("");
            var storeId = $("#storeId").val()
            var serviceId = $("#serviceId").val()
            var employeeId = $("#employeeId").val()
            var date = $("#date").val()
            var object = $(this);
            $("input[name='selectedStoreId']").val(storeId)
            $("input[name='selectedServiceId']").val(serviceId)
            $("input[name='selectedEmployeeId']").val(employeeId)
            $("input[name='selectedDate']").val(date)
            $("input[name='selectedTime']").val(object.val())
        });

        $('#submit-appointment').click(function() {
            if ($("#formModal").valid()) {
                var storeId = $("#selectedStoreId").val()
                var serviceId = $("#selectedServiceId").val()
                var employeeId = $("#selectedEmployeeId").val()
                var date = $("#selectedDate").val()
                var time = $("#selectedTime").val()
                console.log(storeId)
                console.log(serviceId)
                console.log(employeeId)
                console.log(date)
                console.log(time)
                console.log($("#firstName").val())
                console.log($("#lastName").val())
                console.log($("#phone").val())
                console.log($("#email").val())
                console.log($("#message").val())
                var formData = $("#formModal").serialize();
                console.log(formData)
                $.ajax({
                    type: "POST",
                    url: "/submit_appointment.html",
                    data: formData,
                    success: function(result){
                        $('#modal_message_alert').html(result);
                    },
                    error: function (result) {
                        $('#modal_message_alert').html(result);
                        console.log (result)
                    }
                });
            }
        });


    });

</script>

</body>
</html>
<%--</app:cache>--%>