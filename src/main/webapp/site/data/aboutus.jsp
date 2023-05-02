<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.data.about.us"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<h:authenticate request="${pageContext.request}" response="${pageContext.response}"/>
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
<!-- Begin Sidebar Menu -->
    <jsp:include page="leftnav.jsp"/>
<!-- End Sidebar Menu -->
    <%--String sSite = so.getString("UPDATE_CURRENT_SITE_ID");--%>
    <%--if (StringUtils.isNumeric(sSite)) {--%>
    <%--Long siteId = Long.valueOf(sSite);--%>
    <%--Site thisSite = serviceLocator.getSiteDao().findById(siteId);--%>
    <spring:eval expression="sessionObject.getString('UPDATE_CURRENT_SITE_ID')" var="siteId"/>
    <fmt:parseNumber var = "siteId" type = "number" value = "${siteId}" integerOnly = "true" />
    <spring:eval expression="serviceLocator.getSiteDao().findById(siteId)" var="thisSite"/>
    <spring:eval expression="serviceLocator.menuDao.getMenu(thisSite, '', 'Y')" var="homeMenu"/>
    <spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(homeMenu.id, 'Y')" var="menuRows"/>
    <c:forEach var="row" items="${menuRows}">
        <c:if test="${fn:contains(row.title, 'About Us')}">
            <c:set var="aboutUsRow" value="${row}"/>
            <spring:eval expression="serviceLocator.siteMenuPartContentDao.getContentParts(aboutUsRow.id, 'Y')" var="partContents"/>
            <c:if test="${fn:length(partContents) > 0}">
                <c:set var="aboutUsContent" value="${partContents[0]}"/>
            </c:if>
        </c:if>
    </c:forEach>
<!-- Begin Content -->
<div class="col-md-9">
    <div class="row">
        <!-- Begin Sidebar Menu -->
        <div class="col-md-8">
            <div class="panel panel-red margin-bottom-40">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.data.about.us"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal" role="form" action="/site/data/update_aboutus.html" id="form" method="post">
                        <input name="menuId" type="hidden" value="${homeMenu.id}">
                        <input name="rowId" type="hidden" value="${aboutUsRow.id}">
                        <input name="id" type="hidden" value="${aboutUsContent.id}">
                        <h:frontendmessage _messages="${messages}"/>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <label for="title" class="control-label"><fmt:message key="site.data.headline"/></label>
                                <input id="title" type="text" placeholder="<fmt:message key="site.data.headline"/>" name="title" class="form-control" value="${aboutUsContent.title}">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <label for="content" class="control-label"><fmt:message key="site.data.about.us"/></label>
                                <textarea name="content" id="content" rows="15" class="form-control required" placeholder="<fmt:message key="site.data.about.us"/>">${aboutUsContent.content}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <button type="submit" class="btn-u btn-u-red"><fmt:message key="common.save.changes"/></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-red margin-bottom-40">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.data.aboutus.images"/></h3>
                </div>
                <div class="panel-body">
                    <%--<a href="#image-modal-form" class="btn btn-info" id="image-cropper-upload" name="loadImage" type="button" href="#image-modal-form" data-id="${image.id}" data-img="${imageServer}/get/${image.uri}.jpg" data-crop="${empty image.crop? '' : image.crop}" data-ratio="1200x781" role="button" data-toggle="modal" data-target="#image-modal-form">Upload Image</a>--%>
                    <%--<img/>--%>

                    <a class="btn btn-success hidden-sm hidden-xs margin-bottom-20" href="#image-modal-form" data-id="${aboutUsContent.id}" data-img="${aboutUsContent.imgUrl}" data-crop="${aboutUsContent.crop}" data-ratio="1200x781" role="button" data-toggle="modal" data-target="#image-modal-form" data-backdrop="static" data-keyboard="false">
                        <i class="fa fa-cloud-upload"></i> <fmt:message key="images.upload.images"/>
                    </a>
                    <label class="caption margin-bottom-20">
                        <input id=fileupload type="file" name="content-image" class="btn btn-xs btn-success hidden-md hidden-lg">
                    </label>
                    <div id="aboutus-image">
                        <c:if test="${!empty aboutUsContent.imgUrl}">
                            <img src="${aboutUsContent.imgUrl}?op=scale_220"/>
                        </c:if>
                    </div>
                </div>
            </div>
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

        $('#content').summernote();

        $("#form").validate({
            rules:{
                content:"required"
            },
            messages:{
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

    });
    function callbackFromImageModal (image, button, imageUrl) {
        console.log(imageUrl)
        $(button).data("crop", Math.round(image.x)+','+Math.round(image.y)+','+Math.round(image.width)+','+Math.round(image.height))
        //Update crop data for the image if any or insert if it is the new one
        //Update/Insert crop image into database
        $.ajax({
            type: "GET",
            url: '/site/data/update_aboutus_image.html?id=${aboutUsContent.id}&crop='+$(button).data("crop")+'&imgUrl='+imageUrl,
            success: function(data)
            {
                if (data == "ok") {
                    var newImageUrl = $(button).data("img")+'?op=scale_220'
                    //rebuild a new url with crop
                    $("#aboutus-image").html('<img src="'+newImageUrl+'"/>')
                }
            }
        });

    }

</script>
<h:image_modal_front uploadable="true" thisSite="${thisSite}"/>
</body>
</html>
