<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<html>
<c:set value="${fn:substring(uri, 1, fn:length(uri))}" var="menuUri"/>
<spring:eval expression="serviceLocator.getMenuDao().findUniqueBy('uri', menuUri, site.id)" var="menu"/>
<head>
    <title><c:out value="${site.name} | ${menu.name}"/></title>
    <meta name="description" content="${menu.name}"/>
    <meta name="keywords" content="${menu.name}"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<script>
    $(function () {
        $("#form").validate({
            rules:{
                firstName:"required",
                sendersEmail:{
                    required:true,
                    email:true
                },
                message:"required"
            },
            messages:{
                firstName:"<fmt:message key="site.contact.name"/>",
                sendersEmail:{
                    required:"<fmt:message key="site.register.emailisrequired"/>",
                    email:"<fmt:message key="site.register.emailisinvalid"/>"
                },
                message:"<fmt:message key="common.required"/>"
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

        $('#form input[name="siteCode"]').blur(function () {
            $("p.help-block b").text($(this).attr("value"));
        });

    });
</script>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left">${menu.name}</h1>
    </div>
</div>

<!-- Google Map -->
<div id="map" class="map">
</div><!---/map-->
<!-- End Google Map -->

<div class="container content">
    <div class="row margin-bottom-30">
        <div class="col-md-6 mb-margin-bottom-30">
            <div class="headline"><h2><fmt:message key="site.contact.us.message"/></h2></div>
            <h:frontendmessage _messages="${messages}"/>
            <div class="">
                <form method="post" id="form" action="/contact-us.html" role="form" class="form-horizontal" novalidate="novalidate">
                    <div class="form-group">
                        <label class="col-lg-3 control-label" for="firstName"><fmt:message key="site.contact.us.name"/>*</label>
                        <div class="col-lg-9">
                            <input type="text" value="" placeholder="<fmt:message key='site.contact.us.name'/>" maxlength="40" name="firstName" id="firstName" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label" for="sendersEmail"><fmt:message key="site.contact.us.email"/>*</label>
                        <div class="col-lg-9">
                            <input type="text" value="" placeholder="<fmt:message key='site.contact.us.email'/>" maxlength="100" name="sendersEmail" id="sendersEmail" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label" for="phoneNumber"><fmt:message key="site.contact.us.phone"/></label>
                        <div class="col-lg-9">
                            <input type="text" value="" placeholder="<fmt:message key='site.contact.us.phone'/>" maxlength="100" name="phoneNumber" id="phoneNumber" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label" for="message"><fmt:message key="site.contact.us.content"/>*</label>
                        <div class="col-lg-9">
                            <textarea class="form-control" id="message" name="message" rows="4" placeholder="<fmt:message key="site.contact.us.content"/>" maxlength="999"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label"></label>
                        <div class="col-lg-9">
                            <spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SITE_KEY')" var="siteKey"/>
                            <spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SECRET_KEY')" var="siteSecret"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).createSToken(siteSecret)" var="encryptedString"/>
                            <div class="g-recaptcha" data-sitekey="${siteKey}" data-stoken="${encryptedString}"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-offset-3 col-lg-9">
                            <button class="btn-u btn-u-red" type="submit"><fmt:message key="site.contact.us.send"/></button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
<%--Start cache here to make sure captcha key always generate new one--%>
<app:cache key="${uri}">
        <spring:eval expression="serviceLocator.siteSupportDao.findAll(site.id)" var="siteSupports"/>
        <spring:eval expression="serviceLocator.siteContactDao.findAll(site.id)" var="siteContacts"/>

        <div class="col-md-3">
            <!-- Business Hours -->
            <c:if test="${!empty siteSupports}">
                <div class="headline"><h2><fmt:message key="template.admin.customer.service"/></h2></div>
                <c:forEach items="${siteSupports}" var="siteSupport">
                    <c:if test="${!empty siteSupport.name}">
                        <h5>${siteSupport.name}</h5>
                    </c:if>
                    <ul class="list-unstyled who margin-bottom-30">
                        <c:if test="${!empty siteSupport.phone}">
                            <li><a href="#"><i class="fa fa-phone"></i>${siteSupport.phone}</a></li>
                        </c:if>
                        <c:if test="${!empty siteSupport.chatId}">
                            <c:if test="${siteSupport.chatType == 'yahoo'}">
                                <li><fmt:message key="site.contact.us.yahoo.chat"/>: <a href="ymsgr:sendim?${siteSupport.chatId}"><img src="http://opi.yahoo.com/online?u=${siteSupport.chatId}&amp;m=g&amp;t=2" border="0"></a></li>
                            </c:if>
                            <c:if test="${siteSupport.chatType == 'skype'}">
                                <fmt:message key="site.contact.us.skype.chat"/>: <a href="skype:${siteSupport.chatId}?call"><img src="http://www.skypeassets.com/content/dam/scom/images/downloads/desktop/apps-click-to-call.png" alt="Skype Me™!"></a> <br>
                            </c:if>
                        </c:if>
                        <c:if test="${!empty siteSupport.timeAvailable}">
                            <fmt:message key="site.contact.us.hours"/>: ${siteSupport.timeAvailable}<br>
                        </c:if>
                    </ul>
                </c:forEach>
            </c:if>

        </div><!--/col-md-3-->
        <div class="col-md-3">

            <!-- Contacts -->
            <c:if test="${!empty siteContacts}">
            <div class="headline"><h2><fmt:message key="site.contact.us"/></h2></div>
                <c:forEach items="${siteContacts}" var="siteContact">
                <c:if test="${!empty siteContact.name}">
                    <h5>${siteContact.name}</h5>
                </c:if>
                <ul class="list-unstyled who margin-bottom-30">
                    <li><a href="#"><i class="fa fa-home"></i>${siteContact.address_1}, ${siteContact.city}, ${siteContact.state}&nbsp;${siteContact.zipCode}</a></li>
                    <c:if test="${!empty siteContact.email}">
                        <li><a href="#"><i class="fa fa-envelope"></i><a href="mailto:${siteContact.email}" class="">${siteContact.email}</a></a></li>
                    </c:if>
                    <c:if test="${!empty siteContact.phone}">
                        <li><a href="#"><i class="fa fa-phone"></i>${siteContact.phone}</a></li>
                    </c:if>
                    <c:if test="${!empty siteContact.fax}">
                        <li><a href="#"><i class="fa fa-fax"></i>${siteContact.fax} (fax)</a></li>
                    </c:if>
                </ul>
                    </c:forEach>
            </c:if>
        </div><!--/col-md-3-->
    </div><!--/row-->

</div><!--/container-->
<!--=== End Content Part ===-->

<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
                                    <%--http://maps.google.com/maps/api/js?sensor=false&.js--%>
<script type="text/javascript" src="/themes/m3x/plugins/gmap/gmap.js"></script>
<script type="text/javascript">
//REFERENCE:    http://stackoverflow.com/questions/19640055/multiple-markers-google-map-api-v3-from-array-of-addresses-and-avoid-over-query
$(document).ready(function(){
    var map;
    var hasMap = 'N';
    <c:forEach items="${siteContacts}" var="siteContact" varStatus="siteContactStatus">
        hasMap = 'Y';
        $.getJSON('http://maps.googleapis.com/maps/api/geocode/json?address=${siteContact.address_1}, ${siteContact.city}, ${siteContact.state} ${siteContact.zipCode}&sensor=false', null, function (data) {
            var p = data.results[0].geometry.location
            <c:if test="${siteContactStatus.index == 0}">
                map = new GMaps({
                    div: '#map',
                    lat:  p.lat,
                    lng:  p.lng
                });
            </c:if>
            map.addMarker({
                lat:  p.lat,
                lng:  p.lng,
                title: '${siteContact.name}'
            });
        });
    </c:forEach>

    if (hasMap == 'N') {
        $("#map").removeClass();
    }
});

</script>
</body>
</html>
</app:cache>