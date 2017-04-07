<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="rowId" value="${param.rowId}"/>
<c:if test="${! empty rowId}">
    <spring:eval expression="serviceLocator.siteMenuPartContentDao.getWidgetTemplate(T(java.lang.Long).valueOf(rowId))" var="currWidgetTemplate"/>
</c:if>
<c:set var="partContentId" value="${param.id}"/>
<c:if test="${! empty partContentId}">
    <spring:eval expression="serviceLocator.siteMenuPartContentDao.findById(T(java.lang.Long).valueOf(partContentId))" var="partContent"/>
</c:if>
<script type="text/javascript">
    $(function () {

        $("#form").validate({
//            rules: {
//                name: "required"
//                uri: "required"
//            },

            messages:{
                title:"<fmt:message key="page.title.required"/>"
            },
            highlight:function (label) {
                $(label).closest('.control-group').removeClass('success');
                $(label).closest('.control-group').addClass('error');
            },
            success:function (label) {
                $(label).closest('.control-group').removeClass('error');
                $(label).closest('.control-group').addClass('success');
            }
        });

        //Save part content
        $("#form").submit(function() {
            var url = "/admin/design/update_content.html"; // the script where you handle the form input.
            var form = $( "#form" );
            if (form.valid()) {
                tinyContent = $('#tiny_content');
                if (tinyContent.length) {
                <c:choose>
                <c:when test="${currWidgetTemplate.name != 'BLANK'}">
                    tinyContent.val(tinyMCE.get('content_temp').getContent());
                </c:when>
                <c:otherwise>
                    tinyContent.val($('#content_temp').val());
                </c:otherwise>
                </c:choose>
                }
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#modal_message_alert').html(data);
                        $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#modal_message_alert").alert('close');
                            //don't want reload when the modal closed
                            //$('#modal_message_alert').html("");
                            //release modal
                            $('#modal-form').removeData('bs.modal');
                        });
                    }
                });
//            $('#modal-form').modal('hide'); //Close after submit
                return false; // avoid to execute the actual submit of the form.
            }
        });

        //add new part content
        $("#add_part_content").on("click", function(){
            $("[name=id]").val("");
            if ($("[name=header]").length) {
                $("[name=header]").val("");
            }
            if ($("[name=title]").length) {
                $("[name=title]").val("");
            }
            if ($("[name=link]").length) {
                $("[name=link]").val("");
            }
            if ($("[name=imgUrl]").length) {
                $("[name=imgUrl]").val("");
                $("#content-image").attr("src","http://placehold.it/200x100&text=DROP+IMAGE+HERE");
            }
            if ($("[name=videoUrl]").length) {
                $("[name=videoUrl]").val("");
            }
            tinyContent = $('#tiny_content');
            if (tinyContent.length) {
                tinyContent.html("");
                $("#content_temp").html("");

            }
        })

    });
</script>
<!-- Form Control States -->
<div class="row">
<div class="col-xs-12">
    <c:set value="checked" var="activeChecked"/>
    <c:if test="${!empty partContent}">
        <c:if test="${fn:startsWith(partContent.active, 'N')}">
            <c:set value="" var="activeChecked"/>
        </c:if>
    </c:if>

    <div  id="modal_message_alert"></div>
    <form name="form" id="form" class="form-horizontal" action="#" method="post">
        <input type="hidden" name="rowId" value="${rowId}">
        <input type="hidden" name="id" value="${partContent.id}">
    <div class="row">
        <div class="col-xs-12">
            <%--<div class="form-actions">--%>
                <button type="submit" class="btn btn-xs btn-success"><i class="fa fa-save"></i> <fmt:message key="common.save.changes"/></button>
                <a class="btn btn-xs btn-info" id="add_part_content">
                    <i class="fa fa-plus"></i> <fmt:message key="part.content.add"/>
                </a>
            <%--</div>--%>
        </div>
    </div>
        <h:csrf/>
        <div class="row">
            <div class="col-xs-6">
                <fieldset>
                    <div class="control-group">
                        <%--<label class="control-label" for="optionsCheckbox"><fmt:message key="page.active"/></label>--%>
                        <div class="controls">
                            <%--<label class="checkbox">--%>
                            <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/>
                            <fmt:message key="content.active.explain"/>
                            <%--</label>--%>
                        </div>
                    </div>
                    <c:if test="${empty currWidgetTemplate.supportField || fn:contains(currWidgetTemplate.supportField, 'header')}">
                        <div class="control-group">
                            <label class="control-label" for="header"><fmt:message key="site.design.content.header"/></label>

                            <div class="controls">
                                <input name="header" class="input-xlarge" id="header" type="text" maxlength="250" value="${partContent.header}" autofocus/>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${empty currWidgetTemplate.supportField || fn:contains(currWidgetTemplate.supportField, 'title')}">
                        <div class="control-group">
                            <label class="control-label" for="title"><fmt:message key="site.design.content.title"/></label>

                            <div class="controls">
                                <input name="title" class="input-xlarge" id="title" type="text" maxlength="250" value="${partContent.title}" autofocus/>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${empty currWidgetTemplate.supportField || fn:contains(currWidgetTemplate.supportField, 'videoUrl')}">
                        <div class="control-group">
                            <label class="control-label" for="videoUrl"><fmt:message key="site.design.content.videoUrl"/></label>

                            <div class="controls">
                                <input name="videoUrl" class="input-xlarge" id="videoUrl" type="text" maxlength="250" value="${partContent.videoUrl}" autofocus/>
                                <p><fmt:message key="site.design.videoUrl.example"/>: https://www.youtube.com/watch?v=-5PnhYSuidM</p>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${empty currWidgetTemplate.supportField || fn:contains(currWidgetTemplate.supportField, 'link')}">
                        <div class="control-group">
                            <label class="control-label" for="link"><fmt:message key="site.design.content.link"/></label>

                            <div class="controls">
                                <input name="link" class="input-xlarge" id="link" type="text" maxlength="250" value="${partContent.link}" autofocus/>
                            </div>
                        </div>
                    </c:if>
                </fieldset>
            </div>
            <div class="col-xs-6">
                <fieldset>
                    <c:if test="${empty currWidgetTemplate.supportField || fn:contains(currWidgetTemplate.supportField, 'imgUrl')}">
                        <div class="control-group">
                            <label class="control-label" for="optionsCheckbox"><fmt:message key="content.image"/></label>
                            <div class="controls">
                                <c:if test="${empty partContent.imgUrl}">
                                    <img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="content-image" src="http://placehold.it/200x100&text=UPLOAD+IMAGE" alt="" height="100" />
                                    <input type="hidden" name="imgUrl" id="imgUrl" value=""/>
                                    <input type="hidden" name="crop" id="crop" value=""/>
                                </c:if>
                                <c:if test="${!empty partContent.imgUrl}">
                                    <img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="content-image" src="${partContent.imgUrl}?op=scale|200x100" alt="" height="100" />
                                    <input type="hidden" name="imgUrl" id="imgUrl" value="${partContent.imgUrl}"/>
                                    <input type="hidden" name="crop" id="crop" value="${partContent.crop}"/>
                                </c:if>
                                <c:if test="${! empty currWidgetTemplate.imageSize}">
                                    <input type="hidden" name="imageRatio" id="imageRatio" value="${currWidgetTemplate.imageSize}"/>
                                    <p><fmt:message key="site.design.image.size"/>: ${currWidgetTemplate.imageSize} <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="site.design.image.size.explain"/>" title="">?</span></p>
                                </c:if>
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls" style="margin-top: 5px">
                                <a class="btn btn-xs btn-danger show-confirm" id="delete-content-image">
                                    <i class="fa fa-trash icon-white"></i>
                                    <fmt:message key="site.design.delete.image"/>
                                </a>
                                <a class="btn btn-xs btn-info hidden-sm hidden-xs" href="#image-modal-form" data-img="#imgUrl" data-crop="#crop" data-ratio="#imageRatio" role="button" data-toggle="modal" data-target="#image-modal-form">
                                    <i class="fa fa-cloud-upload"></i> <fmt:message key="images.upload.images"/>
                                </a>
                                <label class="caption">
                                    <input id=fileupload type="file" name="content-image" class="btn btn-xs btn-success hidden-md hidden-lg">
                                </label>
                            </div>
                        </div>
                    </c:if>
                </fieldset>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <c:if test="${empty currWidgetTemplate.supportField || fn:contains(currWidgetTemplate.supportField, 'content')}">
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label" for="content_temp"><fmt:message key="site.design.content.content"/></label>
                            <div class="controls">
                                <textarea name="content_temp" class="input-xlarge content_temp" rows="15" id="content_temp" style="width: 100%;">${partContent.content}</textarea>
                                    <%--That's because it's not a textarea any longer. It's replaced with an iframe (and whatnot), and the serialize function only gets data from form fields.--%>
                                <input type="hidden" id="tiny_content" name="content" />
                            </div>
                        </div>
                    </fieldset>
                </c:if>
            </div>
        </div>
    </form>
</div>
</div>

<c:if test="${currWidgetTemplate.name != 'BLANK'}">
    <script type="text/javascript" src="/themes/tmce/tiny_mce.js"></script>
    <script type="text/javascript">
        tinyMCE.init({
            // General options
            mode : "specific_textareas",
            editor_selector : "content_temp",
//        theme : "advanced",
//        skin : "bootstrap",
            content_css : "/themes/tmce/content.css",
//        plugins : "media",//"autolink,lists,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,wordcount,advlist,autosave,visualblocks",

            // Theme options
            theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
            theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
//        theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
//        theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak,restoredraft,visualblocks",
//        theme_advanced_toolbar_location : "top",
//        theme_advanced_toolbar_align : "left",
//        theme_advanced_statusbar_location : "bottom",
//        theme_advanced_resizing : true,


//        Drop lists for link/image/media/template dialogs
            template_external_list_url : "lists/template_list.js",
            external_link_list_url : "lists/link_list.js",
            external_image_list_url : "lists/image_list.js",
            media_external_list_url : "lists/media_list.js",

            // Style formats
            style_formats : [
                {title : 'Bold text', inline : 'b'},
                {title : 'Red text', inline : 'span', styles : {color : '#ff0000'}},
                {title : 'Red header', block : 'h1', styles : {color : '#ff0000'}},
                {title : 'Example 1', inline : 'span', classes : 'example1'},
                {title : 'Example 2', inline : 'span', classes : 'example2'},
                {title : 'Table styles'},
                {title : 'Table row 1', selector : 'tr', classes : 'tablerow1'}
            ]
        });
    </script>
</c:if>


<script type="text/javascript">
    <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
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
                            $("input[name='imgUrl']").val(contentImage);
                        }

                    });
                }
            });
        }
    }
    $(function () {

        $("#delete-content-image").click(function () {
            $("#form input[name='imgUrl']").val("");
            $("#form input[name='crop']").val("");
            $("#content-image").attr("src", "http://placehold.it/200x100&text=DROP+IMAGE+HERE");
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
        $("input[name='imgUrl']").val(imageUrl);
        $("#content-image").attr("src",imageUrl);

    }
</script>
