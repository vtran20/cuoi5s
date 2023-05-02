<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.data.gallery"/></title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>
<body>
<style>
    .ace-thumbnails {
        list-style: none;
        margin: 0;
        padding: 0
    }

    .ace-thumbnails > li {
        float: left;
        display: block;
        position: relative;
        overflow: hidden;
        margin: 2px;
        border: 2px solid #333
    }

    .ace-thumbnails > li > :first-child {
        display: block;
        position: relative
    }

    .ace-thumbnails > li > :first-child:focus {
        outline: none
    }

    .ace-thumbnails > li .tags {
        display: inline-block;
        position: absolute;
        bottom: 0;
        right: 0;
        overflow: visible;
        direction: rtl;
        padding: 0;
        margin: 0;
        height: auto;
        width: auto;
        background-color: transparent;
        border-width: 0;
        vertical-align: inherit
    }

    .ace-thumbnails > li .tags > .label-holder {
        opacity: .92;
        filter: alpha(opacity=92);
        display: table;
        margin: 1px 0 0 0;
        direction: ltr;
        text-align: left
    }

    .ace-thumbnails > li .tags > .label-holder:hover {
        opacity: 1;
        filter: alpha(opacity=100)
    }

    .ace-thumbnails > li > .tools {
        position: absolute;
        top: 0;
        bottom: 0;
        left: -30px;
        width: 24px;
        background-color: rgba(0, 0, 0, 0.55);
        text-align: center;
        vertical-align: middle;
        -webkit-transition: all 0.2s ease;
        transition: all 0.2s ease
    }

    .ace-thumbnails > li > .tools.tools-right {
        left: auto;
        right: -30px
    }

    .ace-thumbnails > li > .tools.tools-bottom {
        width: auto;
        height: 28px;
        left: 0;
        right: 0;
        top: auto;
        bottom: -30px
    }

    .ace-thumbnails > li > .tools.tools-top {
        width: auto;
        height: 28px;
        left: 0;
        right: 0;
        top: -30px;
        bottom: auto
    }

    .ace-thumbnails > li:hover > .tools {
        left: 0;
        right: 0
    }

    .ace-thumbnails > li:hover > .tools.tools-bottom {
        top: auto;
        bottom: 0
    }

    .ace-thumbnails > li:hover > .tools.tools-top {
        bottom: auto;
        top: 0
    }

    .ace-thumbnails > li:hover > .tools.tools-right {
        left: auto;
        right: 0
    }

    .ace-thumbnails > li > .in.tools {
        left: 0;
        right: 0
    }

    .ace-thumbnails > li > .in.tools.tools-bottom {
        top: auto;
        bottom: 0
    }

    .ace-thumbnails > li > .in.tools.tools-top {
        bottom: auto;
        top: 0
    }

    .ace-thumbnails > li > .in.tools.tools-right {
        left: auto;
        right: 0
    }

    .ace-thumbnails > li > .tools > a, .ace-thumbnails > li > :first-child .inner a {
        display: inline-block;
        color: #FFF;
        font-size: 18px;
        font-weight: normal;
        padding: 0 4px
    }

    .ace-thumbnails > li > .tools > a:hover, .ace-thumbnails > li > :first-child .inner a:hover {
        text-decoration: none;
        color: #C9E2EA
    }

    .ace-thumbnails > li .tools.tools-bottom > a, .ace-thumbnails > li .tools.tools-top > a {
        display: inline-block
    }

    .ace-thumbnails > li > :first-child > .text {
        position: absolute;
        right: 0;
        left: 0;
        bottom: 0;
        top: 0;
        text-align: center;
        color: #FFF;
        background-color: rgba(0, 0, 0, 0.55);
        opacity: 0;
        filter: alpha(opacity=0);
        -webkit-transition: all 0.2s ease;
        transition: all 0.2s ease
    }

    .ace-thumbnails > li > :first-child > .text:before {
        content: '';
        display: inline-block;
        height: 100%;
        vertical-align: middle;
        margin-right: 0
    }

    .ace-thumbnails > li > :first-child > .text > .inner {
        padding: 4px 0;
        margin: 0;
        display: inline-block;
        vertical-align: middle;
        max-width: 90%
    }

    .ace-thumbnails > li:hover > :first-child > .text {
        opacity: 1;
        filter: alpha(opacity=100)
    }

    @media only screen and (max-width: 480px) {
        .ace-thumbnails {
            text-align: center
        }

        .ace-thumbnails > li {
            float: none;
            display: inline-block
        }
    }

</style>
<h:authenticate request="${pageContext.request}" response="${pageContext.response}"/>
<c:set value="1200x781" var="dataRatio"/>
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="site.data.gallery"/></h1>
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
        <spring:eval expression="sessionObject.getString('UPDATE_CURRENT_SITE_ID')" var="siteId"/>
        <fmt:parseNumber var="siteId" type="number" value="${siteId}" integerOnly="true"/>
        <spring:eval expression="serviceLocator.getSiteDao().findById(siteId)" var="thisSite"/>

        <!-- Begin Content -->
        <div class="col-md-9">
            <spring:eval expression="serviceLocator.menuDao.getMenu(thisSite, 'gallery.html', 'Y')" var="galleryMenu"/>
            <c:if test="${!empty galleryMenu}">
                <spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(galleryMenu.id, 'Y')" var="menuRows"/>
                <c:forEach var="row" items="${menuRows}">
                    <c:if test="${fn:contains(row.title, 'Gallery')}">
                        <c:set var="galleryRow" value="${row}"/>
                        <spring:eval expression="serviceLocator.siteMenuPartContentDao.getContentParts(galleryRow.id, 'Y')" var="partContents"/>
                        <c:if test="${fn:length(partContents) > 0}">
                            <c:set var="galleryContent" value="${partContents[0]}"/>
                        </c:if>
                    </c:if>
                </c:forEach>
            </c:if>
            <div class="row">
                <!-- Begin Sidebar Menu -->
                <div class="col-md-12">
                    <div class="panel panel-red margin-bottom-40">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.data.gallery"/></h3>
                        </div>
                        <div class="panel-body">
                            <form class="form-horizontal" role="form" action="/site/data/update_gallery.html" id="form" method="post">
                                <input name="menuId" type="hidden" value="${galleryMenu.id}">
                                <input name="rowId" type="hidden" value="${galleryRow.id}">
                                <input name="id" type="hidden" value="${galleryContent.id}">
                                <h:frontendmessage _messages="${messages}"/>
                                <div class="form-group">
                                    <label for="title" class="col-lg-3 control-label"><fmt:message key="site.data.headline"/></label>
                                    <div class="col-lg-9">
                                        <input id="title" type="text" placeholder="<fmt:message key="site.data.headline"/>" name="title" class="form-control" value="${galleryContent.title}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="content" class="col-lg-3 control-label"><fmt:message key="site.data.gallery.description"/></label>
                                    <div class="col-lg-9">
                                        <textarea name="content" id="content" rows="2" class="form-control" placeholder="<fmt:message key="site.data.gallery.description"/>">${galleryContent.content}</textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-lg-offset-3 col-lg-9">
                                        <button type="submit" class="btn-u btn-u-red"><fmt:message key="common.save.changes"/></button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row margin-bottom-20">
                <div class="col-md-12">
                    <a class="btn btn-success" id="upload-image-button">
                        <i class="fa fa-cloud-upload"></i> <fmt:message key="common.upload.image"/>
                    </a>
                    <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
                    <form id="fileupload" action="${imageServer}/images/uploads.json" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="path" value="${thisSite.siteCode}">
                        <input type="hidden" name="imagesPathOverride" value="${thisSite.siteCode}">
                        <input type="hidden" name="generateName" value="on">
                        <input type="hidden" name="skipOptimization" value="on">
                        <input type="file" name="files[]" multiple="" id="upload-image-file" style="display: none">
                    </form>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div  id="message_alert"></div>
                    <ul class="ace-thumbnails">
                        <spring:eval expression="serviceLocator.albumImageDao.findAlbumImages(0, 0, 100, 'id', thisSite.id, true)" var="images"/>
                        <c:forEach items="${images}" var="image">
                            <li>
                                <a href="#" data-rel="colorbox">
                                    <c:choose>
                                        <c:when test="${!empty image.crop}"><img id="image${image.id}" src="${imageServer}/get/${image.uri}.jpg?op=crop_${image.crop}&op=scale_x100" alt=""/></c:when>
                                        <c:otherwise><img id="image${image.id}" src="${imageServer}/get/${image.uri}.jpg?op=scale_x100" alt=""/></c:otherwise>
                                    </c:choose>
                                    <div class="text">
                                        <div class="inner"></div>
                                    </div>
                                </a>
                                <div class="tools tools-bottom">
                                    <c:set var="key" value="${image.uri}"/>
                                    <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).encrypt(key)" var="keyEncrypted"/>
                                    <c:url value="${imageServer}/images/remove.json" var="removeUrl">
                                        <c:param name="key" value="${keyEncrypted}"/>
                                        <c:param name="path" value="${thisSite.siteCode}"/>
                                        <c:param name="name" value="${image.uri}"/>
                                    </c:url>
                                    <a href="#image-modal-form" data-id="${image.id}" data-img="${imageServer}/get/${image.uri}.jpg" data-crop="${empty image.crop? '' : image.crop}" data-ratio="${dataRatio}" role="button" data-toggle="modal" data-target="#image-modal-form"  data-backdrop="static" data-keyboard="false">
                                        <i class="ace-icon fa fa-pencil"></i>
                                    </a>
                                    <a class="delete-album-image" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>'
                                       hreflang="/site/data/deleteimage.html?id=${image.id}&siteId=${thisSite.id}"
                                       removeUrl="${removeUrl}"> <i class="ace-icon fa fa-times red"></i>
                                    </a>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

        </div>

        <!-- End Content -->
    </div>
</div>

<script>
    $(function () {
        var dataRatio = '${dataRatio}'
        $('#upload-image-button').on("click", function () {  /* cutomized button clicked */
            $('#upload-image-file').click();
            /* Now file upload button auto clicked & file browser dialog opens. */

        });
        //Upload multiple files
        $("#upload-image-file").change(function () {
            uploadImages($("#upload-image-button"), this.files);
        });

        function uploadImages(currElement, files) {
            //show spinner
            var parent = currElement.parent();
            var i = 0, len = files.length;
            parent.append("<img src='/themes/editor/img/spinner.gif' id='spinner-gif'>");
            for (; i < len; i++) {
                var res = new FormData();
                res.append('path', '${thisSite.siteCode}');
                res.append('imagesPathOverride', '${thisSite.siteCode}');
                res.append('generateName', 'on');
                res.append('skipOptimization', 'on');
                res.append('files[]', files[i]);

                $.ajax({
                    type: "POST",
                    xhrFields: {withCredentials: false},
                    url: '${imageServer}/images/uploads.json',
                    data: res,
                    dataType: 'json',
                    contentType: false,
                    processData: false,
                    success: function (res) {
                        //remove spinner
                        if (parent.find("#spinner-gif")) {
                            parent.find("#spinner-gif").remove();
                        }
                        for (var i = 0; i < res.length; i++) {
                            //$.each(res, function(index, element) {
                            if (res[i].error) {
                            } else {
                                //Insert into AlbumImage
                                $.ajax({
                                    type: 'GET',
                                    contentType: "application/json; charset=utf-8",
                                    dataType: "json",
                                    url: '/site/data/insert_gallery_image.html',
                                    data: { uri: res[i].uri, imageName: res[i].name, siteId:${thisSite.id}, albumId: null },
                                    success: function (result) {
                                        //Render html from data return and append to the current page
                                        var temp = $("#album_image_mustache").html();
                                        var newImage = Mustache.render(temp, result);
                                        $(".ace-thumbnails").append(newImage);
                                    },
                                    error: function (result) {
                                    }
                                });

                            }
                        }
                    },
                    error: function (result) {
                        //remove spinner
                        if (parent.find("#spinner-gif")) {
                            parent.find("#spinner-gif").remove();
                        }
                    }
                });
            }
        }

        //Delegated events have the advantage that they can process events from descendant elements that are added to the document at a later time.
        //$(".delete-album-image").on('click', function() {
        $("ul.ace-thumbnails").delegate("a.delete-album-image", "click", function(){

            var object = $(this);
            BootstrapDialog.show({
                type:BootstrapDialog.TYPE_DANGER,
                closeByBackdrop: false,
                closeByKeyboard: false,
                title:'<fmt:message key="common.confirm.title"/>',
                message:object.attr("lang"),
                buttons:[
                    {
                        label:'Yes',
                        action:function (dialog) {
                            $.ajax({
                                type: "GET",
                                url: object.attr("hreflang"),
                                data: $("#form").serialize(), // serializes the form's elements.
                                success: function(data)
                                {
                                    if (data == "ok") {
                                        $.ajax({
                                            url: object.attr("removeUrl"),
                                            xhrFields: {
                                                withCredentials: false
                                            }
                                        }).done(function( data ) {
                                            //Remove image
                                            object.closest('li').remove();
                                        });
                                    }
                                }
                            });
                            dialog.close();
                        }
                    },
                    {
                        label:'No',
                        action:function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });
        });

//TODO: use this code for testing bad quality images
//        $("#upload-image-file").change(function () {
//            fileList = [];
//            var _URL = window.URL || window.webkitURL;
//            //var file, img;
//            var hasInvalidImage = false
//            for (var i = 0; i < this.files.length; i++) {
//                file = this.files[i]
//                img = new Image();
//                img.src = _URL.createObjectURL(file);
//                img.onload = function() {
//                    wh = dataRatio.split("x")
//                    if (this.width < wh[0] && this.height < wh[1]) {
//                        console.log(wh)
//                        console.log(file.name)
//                        console.log(this.width + " " + this.height);
//                        hasInvalidImage = true
//                    } else {
//                        fileList.push(file);
//                    }
//                };
//                img.onerror = function() {
//                    console.log( "not a valid file: " + file.type);
//                };
//                console.log(fileList)
//            }
//            console.log("fileList")
//            console.log(fileList)
////            uploadImages($("#upload-image-button"), this.files);
//        });

//            $("#message_alert").fadeTo(10000, 500).slideUp(500, function(){
//                $("#message_alert").alert('close');
//                //don't want reload when the modal closed
//                //$('#modal_message_alert').html("");
//                //release modal
//                $('#modal-form').removeData('bs.modal');
//            });

    });

    function callbackFromImageModal (image, button) {
//        $("#crop").val(Math.round(image.x)+','+Math.round(image.y)+','+Math.round(image.width)+','+Math.round(image.height));
//        $("input[name='imgUrl']").val(imageUrl);
//        $("#content-image").attr("src",imageUrl);
        $(button).data("crop", Math.round(image.x)+','+Math.round(image.y)+','+Math.round(image.width)+','+Math.round(image.height))

        //Update crop data for the image
        $.ajax({
            type: "GET",
            url: '/site/data/update_image.html?id='+$(button).data("id")+'&crop='+$(button).data("crop"),
            //data: $("#form").serialize(), // serializes the form's elements.
            success: function(data)
            {
                if (data == "ok") {
                    //TODO: set image follow the new crop
                    var newImageUrl = $(button).data("img")+'?op=crop_'+$(button).data("crop")+'&op=scale_x100'
                    //rebuild a new url with crop
                    $("#image"+$(button).data("id")).attr('src', newImageUrl)
                }
            }
        });

    }

    //Sidebar Navigation Toggle
    jQuery('.list-toggle').on('click', function () {
        jQuery(this).toggleClass('active');
    });

</script>

<script type="text/x-mustache" id="album_image_mustache">
    <li>
        <a href="#" data-rel="colorbox">
            <img id="image{{id}}" src="${imageServer}/get/{{uri}}.jpg?op=scale_x100" alt=""/>
            <div class="text">
                <div class="inner"></div>
            </div>
        </a>

        <div class="tools tools-bottom">
            <a href="#image-modal-form" data-id="{{id}}" data-img="${imageServer}/get/{{uri}}.jpg" data-crop="" data-ratio="${dataRatio}" role="button" data-toggle="modal" data-target="#image-modal-form"   data-backdrop="static" data-keyboard="false">
                <i class="ace-icon fa fa-pencil"></i>
            </a>
            <a class="delete-album-image" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>'
                hreflang="/site/data/deleteimage.html?id={{id}}"
                removeUrl="{{delete_url}}"> <i class="ace-icon fa fa-times red"></i>
            </a>
        </div>
    </li>
</script>
<h:form_modal/>
<h:image_modal_front thisSite="${thisSite}"/>
</body>
</html>
