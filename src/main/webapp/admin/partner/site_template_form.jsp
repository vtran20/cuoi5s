<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="templateId" value="${param.id}"/>
<c:if test="${! empty templateId}">
    <spring:eval expression="serviceLocator.templateDao.findById(T(java.lang.Long).valueOf(templateId))" var="thisTemplate"/>
    <spring:eval expression="serviceLocator.siteDao.findById(thisTemplate.siteSample.id)" var="thisSite"/>
</c:if>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>

<script>
    $(function () {

        $("#form").validate({
            messages:{
                <%--name:"<fmt:message key="menu.name.required"/>"--%>
            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('success');
                $(label).closest('.form-group').addClass('error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('error');
                $(label).closest('.form-group').addClass('success');
            }
        });

        $("#form").submit(function() {
            var url = "/admin/partner/savesitetemplate.html"; // the script where you handle the form input.
            var form = $( "#form" );
            if (form.valid()) {
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        if ($.isNumeric(data.id)) {
                            $('#templateId').attr("value", data.id);
                        }
                        $('#modal_message_alert').html(data.messages);
                        $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#modal_message_alert").alert('close');
                        });
                    }
                });
//            $('#modal-form').modal('hide'); //Close after submit
                return false; // avoid to execute the actual submit of the form.
            }
        });

    });
</script>

<form name="form" id="form" action="#" method="post">
    <div class="row">
    <div class="col-xs-6">
        <div id="modal_message_alert"></div>
            <input type="hidden" id="templateId" name="id" value="${thisTemplate.id}"/>
            <h:csrf/>
            <div class="form-group">
                <label class="control-label" for="subDomain"><fmt:message key="partner.subdomain.name"/>*</label>
                <div class="">
                    <input name="subDomain" class="required input-xlarge" id="subDomain" type="text" value="${thisSite.subDomain}" maxlength="255" autofocus/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="name"><fmt:message key="partner.template.name"/>*</label>
                <div class="">
                    <input name="name" class="required input-xlarge" id="name" type="text" value="${thisTemplate.name}" maxlength="255" autofocus/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="templateModel"><fmt:message key="partner.template.model"/>*</label>
                <div class="controls">
                    <input name="templateModel" class="required input-xlarge" id="templateModel" type="text" value="${thisTemplate.templateModel}" maxlength="25"/>
                </div>
            </div>
    </div>
    <div class="col-xs-6">
        <fieldset>
            <div class="control-group">
                <label class="control-label"><fmt:message key="content.image"/></label>
                <div class="controls">
                    <c:if test="${empty thisTemplate.imageUrl}">
                        <img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="content-image" src="http://placehold.it/200x100&text=UPLOAD+IMAGE" alt="" height="100" />
                        <input type="hidden" name="imageUrl" id="imageUrl" value=""/>
                        <input type="hidden" name="crop" id="crop" value=""/>
                    </c:if>
                    <c:if test="${!empty thisTemplate.imageUrl}">
                        <c:choose>
                            <c:when test="${fn:contains(thisTemplate.imageUrl, imageServer)}"><img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="content-image" src="${thisTemplate.imageUrl}?op=scale|200x100" alt="" height="100" /></c:when>
                            <c:otherwise><img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="content-image" src="${imageServer}/get/${thisTemplate.imageUrl}.jpg?op=scale|200x100" alt="" height="100" /></c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${fn:contains(thisTemplate.imageUrl, imageServer)}"><input type="hidden" name="imageUrl" id="imageUrl" value="${thisTemplate.imageUrl}"/></c:when>
                            <c:otherwise><input type="hidden" name="imageUrl" id="imageUrl" value="${imageServer}/get/${thisTemplate.imageUrl}.jpg"/></c:otherwise>
                        </c:choose>
                        <input type="hidden" name="crop" id="crop" value="${thisTemplate.crop}"/>
                    </c:if>
                    <input type="hidden" name="imageRatio" id="imageRatio" value="400x500"/>
                </div>
            </div>
            <div class="control-group">
                <div class="controls" style="margin-top: 5px">
                    <a class="btn btn-xs btn-danger show-confirm" id="delete-content-image">
                        <i class="fa fa-trash icon-white"></i>
                        <fmt:message key="site.design.delete.image"/>
                    </a>
                    <a class="btn btn-xs btn-info hidden-sm hidden-xs" href="#image-modal-form" data-img="#imageUrl" data-crop="#crop" data-ratio="#imageRatio" role="button" data-toggle="modal" data-target="#image-modal-form">
                        <i class="fa fa-cloud-upload"></i> <fmt:message key="images.upload.images"/>
                    </a>
                    <label class="caption">
                        <input id=fileupload type="file" name="content-image" class="btn btn-xs btn-success hidden-md hidden-lg">
                    </label>
                </div>
            </div>
        </fieldset>
    </div>
    </div>
<div class="row">
<div class="modal-footer">
            <button class="btn btn-sm btn-primary" type="submit">
                <i class="ace-icon fa fa-check"></i>
                <fmt:message key="common.save.changes"/>
            </button>
        </div>
</div>

</form>
<script type="text/javascript">
    $("#fileupload").change(function() {
        var currElement = $("#content-image");
        uploadImage(currElement, this.files[0])
    });

    function uploadImage (currElement, file) {
        if(confirm('<fmt:message key="news.do.you.want.to.upload.and.replace.the.current.image"/>')) {
            //show spinner
            var parent = currElement.parent();
            parent.append("<img src='/themes/editor/img/spinner.gif' id='spinner-gif'>");
            var data = new FormData();
            data.append('file', file);
            data.append('path','${site.siteCode}');
            data.append('imagesPathOverride','${site.siteCode}');
            data.append('generateName','on');
            data.append('skipOptimization','on');
            $.ajax({
                type:"POST",
                xhrFields: {withCredentials: false},
                url:'${imageServer}/images/upload.json',
                data:data,
                dataType: 'json',
                contentType: false,
                processData: false,
                success:function(res){
                    //remove spinner
                    if (parent.find("#spinner-gif")) {
                        parent.find("#spinner-gif").remove();
                    }
                    $.each(res, function(index, element) {
                        if (element.name) {
                            var imgSize = "";
                            oldSrc = currElement.attr("src");
                            //oldSrc=http://placehold.it/174x120&text=DROP+IMAGE+HERE
                            if (oldSrc.indexOf("placehold.it") > 0) {
                                if (oldSrc.lastIndexOf("&") > 0) {
                                    oldSrc = oldSrc.substring(0, oldSrc.lastIndexOf("&"));
                                    //oldSrc=http://placehold.it/174x120
                                }
                                imgSize = oldSrc.substring(oldSrc.lastIndexOf("/")+1, oldSrc.length);
                                if (imgSize && imgSize.indexOf("x") > 0) {
                                    //get w and h and put into img
                                    w = imgSize.substring(0, imgSize.indexOf("x"));
                                    h = imgSize.substring(imgSize.indexOf("x") + 1, imgSize.length);
                                }
                            } else {
                                //get w and h and put into img
                                w = currElement.attr("width");
                                h = currElement.attr("height");
                                //Don't use width
                                imgSize = "x"+h;
                            }
                            var src = "${imageServer}"+"/get/"+element.name+".jpg?op=scale|"+imgSize;
                            currElement.attr({"src": src, "width":w, "height":h});
                            //Set image into hidden field to submit to server
                            var contentImage = "${imageServer}"+"/get/"+element.name+".jpg";
//                            $("#form input[name='logoImg']").val(logoImgSave);
                            $("input[name='imageUrl']").val(contentImage);
                        }

                    });
                }
            });
        }
    }
    $(function () {

        $("#delete-content-image").click(function () {
            $("#form input[name='imageUrl']").val("");
            $("#form input[name='crop']").val("");
            $("#content-image").attr("src", "http://placehold.it/200x100&text=UPLOAD+IMAGE");
        });

        //Upload image by drag & drop from desktop
        $.fn.dragOverImage = function (event) {
            event.stopPropagation();
            event.preventDefault();
            var currElement = $(event.target);
            currElement.addClass("selected");
        };

        $.fn.dragLeaveImage = function (event) {
            event.stopPropagation();
            event.preventDefault();
            var currElement = $(event.target);
            currElement.removeClass("selected");
        };

        $.fn.validateImage = function (fileType) {
            var arr = ['image/jpg','image/png','image/bmp','image/jpeg','image/gif'];
            return jQuery.inArray(fileType, arr);
        };

        $.fn.dropImage = function (event) {
            event.stopPropagation();
            event.preventDefault();
            var dataTransfer = event.dataTransfer;// = event.originalEvent.dataTransfer;
            var currElement = $(event.target);
            if (dataTransfer) {
                if ("img" == currElement.prop("tagName").toLowerCase()) {
                    var file = $.makeArray(dataTransfer && dataTransfer.files);
                    if(file && typeof file[0] != undefined && this.validateImage(file[0].type) >= 0) {
                        uploadImage(currElement, file[0]);
                    }
                }
            }
            $("#content-image").removeClass("selected");
        }
    });

    function callbackFromImageModal (image, imageUrl) {
        $("#crop").val(Math.round(image.x)+','+Math.round(image.y)+','+Math.round(image.width)+','+Math.round(image.height));
        $("input[name='imageUrl']").val(imageUrl);
        $("#content-image").attr("src",imageUrl);

    }
</script>
