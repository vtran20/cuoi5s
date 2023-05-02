<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<title><fmt:message key="site.header.header.information"/></title>
<div class="page-header">
    <h4><fmt:message key="site.header.header.information"/></h4>
</div><!-- /.page-header -->

<div class="row">
<div class="col-xs-12">
<div  id="message_alert"></div>
    <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
    <spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>
<form name="form" id="form" class="form-horizontal" action="#" method="post">
    <input name="id" type="hidden" value="${siteHeaderFooter.id}"/>
    <c:if test="${fn:startsWith(siteHeaderFooter.useLogoImg, 'Y')}">
        <c:set value="checked" var="useImageLogo"/>
    </c:if>
    <c:if test="${!fn:startsWith(siteHeaderFooter.useLogoImg, 'Y')}">
        <c:set value="checked" var="useImageText"/>
    </c:if>

    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right"><fmt:message key="site.header.logo.image"/></label>
        <div class="controls col-sm-9" id="logo-website">
            <input type="radio" name="useLogoImg" value="Y" ${useImageLogo}/>
            <c:if test="${empty siteHeaderFooter.logoImg}">
                <img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="logo-image" src="http://placehold.it/200x50&text=DROP+LOGO+HERE" alt="" height="70" />
                <input type="hidden" name="logoImg" value=""/>
            </c:if>
            <c:if test="${!empty siteHeaderFooter.logoImg}">
                <img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="logo-image" src="${siteHeaderFooter.logoImg}?op=scale_x70" alt="" />
                <input type="hidden" name="logoImg" value="${siteHeaderFooter.logoImg}"/>
            </c:if>
            <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="Logo giới hạn chiều cao là 70px chiều rộng tùy ý. Hệ thống sẽ tự động thay đổi kích thước để phù hợp với yêu cầu này" title="">?</span>
            <div class="controls">
                <a class="btn btn-danger show-confirm" id="delete-logo-image" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>' hreflang="/admin/sites/deletelogoimage.html?csrf=<sec:authentication property="details.csrf"/>" removeUrl="${imageServer}/images/remove.json?path=${site.siteCode}">
                    <i class="fa fa-trash-o icon-white"></i>
                    <fmt:message key="common.delete.image"/>
                </a>
                <a class="btn btn-info" id="upload-logo-image">
                    <i class="fa fa-file-image-o"></i> <fmt:message key="common.upload.image"/>
                </a>
                <input id="file-logo-image" type="file" name="logo_image" class="btn btn-success" style="display: none">
            </div>
        </div>

    </div>

    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="logoText_temp"><fmt:message key="site.header.logo.text"/></label>
        <div class="controls col-sm-9">
            <input type="radio" name="useLogoImg" value="N" ${useImageText}/>
            <textarea id="logoText_temp" class="logoText_temp" name="logoText_temp" rows="2" style="width: 50%;">${siteHeaderFooter.logoText}</textarea>
            <input type="hidden" id="logoText" name="logoText" />
        </div>
    </div>
    <div class="form-actions">
        <button type="submit" class="btn btn-primary"><fmt:message key="common.save.changes"/></button>
    </div>
</form>
</div>
</div>

<script type="text/javascript" src="/themes/tmce/tiny_mce.js"></script>
<script type="text/javascript">
    tinyMCE.init({
        // General options
        mode : "specific_textareas",
        editor_selector : "logoText_temp",
        theme : "advanced",
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
//        ,

        // Replace values for the template plugin
//        template_replace_values : {
//            username : "Some User",
//            staffid : "991234"
//        }
//        ,
//        file_browser_callback : 'myFileBrowser'
    });
</script>
<script>
    //Upload thumbnail image for news
    $('#upload-logo-image').on("click", function() {  /* cutomized button clicked */
        $('#file-logo-image').click();  /* Now file upload button auto clicked & file browser dialog opens. */

    });

    $("#file-logo-image").change(function() {
        var currElement = $("#logo-image");
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
                            var src = "${imageServer}"+"/get/"+element.name+".jpg?op=scale_"+imgSize;
                            currElement.attr({"src": src, "width":w, "height":h});
                            //Set image into hidden field to submit to server
                            var logoImgSave = "${imageServer}"+"/get/"+element.name+".jpg";
                            $("#form input[name='logoImg']").val(logoImgSave);
                        }

                    });
                }
            });
        }
    }

    $("#delete-logo-image").click(function () {
        var object = $(this);
        $.ajax({
            type: "get",
            url: object.attr("hreflang")+'&logoImg='+$("#form input[name='logoImg']").val()+'&id='+$("#form input[name='id']").val(),
            //Add contentType and dataType to avoid HTTP Error 406 Not acceptable
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data)
            {
                $.ajax({
                    url: object.attr("removeUrl")+'&name='+data.image_uri+'&key='+data.key,
                    xhrFields: {
                        withCredentials: false
                    }
                }).done(function( result ) {
                            //Remove image
                            $("#form input[name='logoImg']").val("");
                            $("#logo-image").attr("src", "http://placehold.it/300x200&text=DROP+IMAGE+HERE");
                        });
            }
        });
    });

    $(function () {
        $("#form").submit(function() {
            var url = "/admin/sites/update_header_footer.html"; // the script where you handle the form input.
            $('#logoText').val(tinyMCE.get('logoText_temp').getContent());
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });

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

        //Upload thumbnail image
        $("#site-logo-img").click(function() {
            var dataId = $(this).data('id');
            $(".modal-body #parent-id").val( dataId );
            $('#myModal').on('show.bs.modal', function(event){
//                backdrop:false
            });
        });
        $('#modal-form').on('hide.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var imgLink;
            if ($(".modal-header h4").text() == 'Logo Image') {
                //Select Image from Internal
                if ($(".modal-body div.tab-pane.active").attr("id") == 'image_library') {
                    imgLink = $(".modal-body #images-selected").val();
                    if (imgLink != "") {
                        $("#logo-image").attr("src", imgLink+"?op=scale_x50");
                        $("#form input[name='logoImg']").val(imgLink);
                    }
                    //Select Image from External
                } else if ($(".modal-body div.tab-pane.active").attr("id") == 'image_external') {
                    imgLink = $(".modal-body #url").val();
                    if (imgLink != "") {
                        $("#logo-image").attr("src", imgLink);
                        $("#form input[name='logoImg']").val(imgLink);
                    }
                }
            }
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
            $("#logo-image").removeClass("selected");
        }
    });

    $('[data-rel=popover]').popover({container:'body'});

</script>
