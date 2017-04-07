<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<c:set var="newsId" value="${param.id}"/>
<c:if test="${! empty newsId}">
    <spring:eval expression="serviceLocator.newsDao.findById(T(java.lang.Long).valueOf(newsId))" var="news"/>
</c:if>

<c:set value="checked" var="activeChecked"/>
<c:if test="${! empty news.id}">
    <c:set value="Update News <b>${news.title}</b>" var="formState"/>
    <c:set value="/admin/news/updatenews.html" var="formUrl"/>
    <c:set value="${news.id}" var="newsId"/>
    <c:set value="${news.uri}" var="uri"/>
    <c:if test="${fn:startsWith(news.active, 'N')}">
        <c:set value="" var="activeChecked"/>
    </c:if>

</c:if>
<c:if test="${empty news.id}">
    <c:set value="Add News" var="formState"/>
    <c:set value="/admin/news/insertnews.html" var="formUrl"/>
</c:if>
<script>
    $(function () {

        $("#form").validate({
            rules:{
                title:{
                    required:true,
                    maxlength:255
                },
                preShortDescription:{
                    required:true,
                    maxlength:500
                }
            },

            messages:{
                title:{
                    required:"<fmt:message key="news.title.is.required"/>",
                    maxlength:"<fmt:message key="news.title.maximum"/>"
                },
                preShortDescription:{
                    required:"<fmt:message key="news.shortcontent.is.required"/>",
                    maxlength:"<fmt:message key="news.shortcontent.maximum"/>"
                }
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
        $("#publish").click(function() {
            $("#form input[name='type']").val('publish');
            var url = "/admin/news/updatenews.html"; // the script where you handle the form input.
            var form = $( "#form" );
            if (form.valid()) {

                $('#tiny_content').val(tinyMCE.get('preContent_temp').getContent());
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#message_alert').html(data.messages);
                        $("#form input[name='id']").val(data.id);
                        $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#message_alert").alert('close');
                        });
                    }
                });
//            $('#modal-form').modal('hide'); //Close after submit
                return false; // avoid to execute the actual submit of the form.
            }
        });

        $("#save").click(function() {
            $("#form input[name='type']").val('save');
            var url = "/admin/news/updatenews.html"; // the script where you handle the form input.
            var form = $( "#form" );
            if (form.valid()) {

                $('#tiny_content').val(tinyMCE.get('preContent_temp').getContent());
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#message_alert').html(data.messages);
                        $("#form input[name='id']").val(data.id);
                        $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#message_alert").alert('close');
                        });
                    }
                });
//            $('#modal-form').modal('hide'); //Close after submit
                return false; // avoid to execute the actual submit of the form.
            }
        });

    });
</script>
<!-- Bread Crumb Navigation -->
<div class="row">
<div class="col-xs-12">
    <form name="form" id="form" class="form-horizontal" action="#" method="post">
        <div  id="message_alert"></div>
        <div class="rows">
            <div class="form-actions">
                <button type="button" id="save" class="btn btn-primary"><i class="fa fa-save icon-white"></i> <fmt:message key="news.save.draft"/></button>
                <button type="button" id="publish" class="btn btn-success"><i class="fa fa-globe icon-white"></i> <fmt:message key="news.publish"/></button>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="widget-box">
                    <div class="widget-header">
                        <h5 class="widget-title">${formState}</h5>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main">
                            <div class="box-content">
                                <input name="id" type="hidden" value="${newsId}"/>
                                <input name="uri" type="hidden" value="${uri}"/>
                                <input name="type" type="hidden" value=""/>
                                <h:csrf/>
                                <div class="row">
                                    <div class="col-sm-9">
                                        <div class="row">
                                            <div class="col-sm-8">
                                                <div class="control-group">
                                                    <label class="control-label" for="title"><fmt:message key="news.title"/>*</label>
                                                    <div class="controls">
                                                        <input name="title" class="required input-xxlarge" id="title" type="text" maxlength="255" value="${news.title}" autofocus/>
                                                    </div>
                                                </div>
                                                <div class="control-group">
                                                    <label class="control-label" for="preShortDescription"><fmt:message key="news.short.description"/>*</label>

                                                    <div class="controls">
                                                        <textarea name="preShortDescription" rows="5" class="required input-xxlarge" id="preShortDescription" >${news.preShortDescription}</textarea>
                                                    </div>
                                                </div>
                                                <div class="control-group">
                                                    <%--<label class="control-label" for="optionsCheckbox"><fmt:message key="news.active"/></label>--%>
                                                    <div class="controls">
                                                        <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/>
                                                        <fmt:message key="news.active.explain"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4">
                                                <div class="control-group">
                                                    <label class="control-label" for="title"><fmt:message key="news.image"/>*</label>
                                                    <div class="controls">
                                                        <div class="thumbnail">
                                                            <c:if test="${empty news.thumbImg}">
                                                                <img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="thumbnail-image" src="http://placehold.it/300x200&text=DROP+IMAGE+HERE" alt="" width="300" height="200" />
                                                                <input type="hidden" name="thumbImg" value=""/>
                                                            </c:if>
                                                            <c:if test="${!empty news.thumbImg}">
                                                                <img ondrop="$(this).dropImage(event)" ondragover="$(this).dragOverImage(event)" ondragleave="$(this).dragLeaveImage(event)" id="thumbnail-image" src="${news.thumbImg}?op=scale|300x200" alt="" width="300" height="200" />
                                                                <input type="hidden" name="thumbImg" value="${news.thumbImg}"/>
                                                            </c:if>
                                                            <%--<div class="caption" style="text-align:center">--%>
                                                                <%--<a class="btn btn-success" href="#" id="news-thumbnail-img" data-id="news-thumbnail-img" data-toggle="modal" role="button"> <i class="icon-plus icon-white"></i> Upload Image</a>--%>
                                                            <%--</div>--%>
                                                        </div>
                                                    </div>
                                                    <div class="control-group">
                                                        <div class="controls">
                                                            <a class="btn btn-danger show-confirm" id="delete-thumbnail-image" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>' hreflang="/admin/news/deletethumbnailimage.html?csrf=<sec:authentication property="details.csrf"/>" removeUrl="${imageServer}/images/remove.json?path=${site.siteCode}">
                                                                <i class="fa fa-trash-o icon-white"></i>
                                                                <fmt:message key="common.delete.image"/>
                                                            </a>
                                                            <label class="caption">
                                                                <%--<a class="btn btn-success" href="#" id="content-img" data-id="content-img" data-toggle="modal" role="button"> <i class="icon-plus icon-white"></i> Upload Image</a>--%>
                                                                    <a class="btn btn-info" id="upload-thumbnail-image">
                                                                        <i class="fa fa-file-image-o"></i> <fmt:message key="common.upload.image"/>
                                                                    </a>
                                                            </label>
                                                            <input id=file-thumbnail-image type="file" name="thumbnail-image" class="btn btn-success" style="display: none">
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <label for="preContent_temp"><fmt:message key="news.content"/> </label>
                                                <div class="caption">
                                                    <a class="btn btn-info" id="upload-news-detail-image">
                                                        <i class="fa fa-file-image-o"></i> <fmt:message key="common.upload.image"/>
                                                    </a>
                                                <%--<a class="btn btn-success" href="#" id="news_detail-image" data-id="news-detail-image" data-toggle="modal" role="button"> <i class="icon-plus icon-white"></i> Insert Image</a>--%>
                                                    <input id=file-news-detail-image type="file" name="news-detail-image" class="btn btn-success" style="display: none">
                                                </div>
                                                <%--http://stackoverflow.com/questions/3918773/textarea-tinymce-and-insert-an-image-in-textarea--%>
                                                <%--http://stackoverflow.com/questions/2572996/html-editor-alternative-besides-tinymce--%>
                                                <div>
                                                    <textarea id="preContent_temp" class="preContent_temp" name="preContent_temp" rows="30" style="width: 100%;">${news.preContent}</textarea>
                                                    <input type="hidden" id="tiny_content" name="preContent" />
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="col-sm-3">
                                        <!-- Portlet: Member List -->
                                        <div class="box" id="box-1">
                                            <spring:eval expression="serviceLocator.newsCategoryDao.getRootNewsCategories(site, 'Y')" var="newsCategories"/>
                                            <div class="box-container-toggle">
                                                <div class="box-content">
                                                    <c:if test="${!empty newsCategories}">

                                                        <table cellpadding="0" cellspacing="0" border="0"
                                                               class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                                            <thead>
                                                            <tr>
                                                                <th><fmt:message key="news.category.name"/></th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>

                                                            <c:forEach items="${newsCategories}" var="currentNewsCategory">
                                                                <c:set value="" var="checkboxSelected"/>
                                                                <c:if test="${!empty newsId}">
                                                                    <spring:eval expression="serviceLocator.newsCategoryDao.findNewsCategory(newsId, currentNewsCategory.id, site.id)" var="newsCat"/>
                                                                    <c:if test="${(!empty newsCat) && (newsCat.id == currentNewsCategory.id)}">
                                                                        <c:set value="checked" var="checkboxSelected"/>
                                                                    </c:if>
                                                                </c:if>
                                                                <tr id="${currentNewsCategory.id}-s${currentNewsCategory.sequence}">
                                                                    <td><input type="checkbox" name="newsCategoryId" value="${currentNewsCategory.id}" ${checkboxSelected}/> <c:out value="${currentNewsCategory.name}"/></td>
                                                                </tr>
                                                                <%--Get SubNewsCategory--%>
                                                                <spring:eval expression="serviceLocator.newsCategoryDao.getSubNewsCategories(site, currentNewsCategory, 'Y')" var="subNewsCategories"/>
                                                                <c:if test="${! empty subNewsCategories}">
                                                                    <c:forEach items="${subNewsCategories}" var="subNewsCategory">
                                                                        <c:set value="" var="checkboxSelected"/>
                                                                        <c:if test="${!empty newsId}">
                                                                            <spring:eval expression="serviceLocator.newsCategoryDao.findNewsCategory(newsId, subNewsCategory.id, site.id)" var="newsCat"/>
                                                                            <c:if test="${(!empty newsCat) && (newsCat.id == subNewsCategory.id)}">
                                                                                <c:set value="checked" var="checkboxSelected"/>
                                                                            </c:if>
                                                                        </c:if>
                                                                        <tr id="${currentNewsCategory.id}-${subNewsCategory.id}-s${subNewsCategory.sequence}">
                                                                            <td>---- <input type="checkbox" name="newsCategoryId" value="${subNewsCategory.id}" ${checkboxSelected}/> <c:out value="${subNewsCategory.name}"/></td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </c:if>
                                                    <c:if test="${empty newsCategories}">
                                                        <fmt:message key="news.category.is.empty"/>
                                                    </c:if>

                                                </div>
                                            </div>
                                        </div>
                                        <!--/span-->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </div>
    </form>
</div>
</div>
<script type="text/javascript" src="/themes/tmce/tiny_mce.js"></script>

<script type="text/javascript">
    tinyMCE.init({
        // General options
        mode : "specific_textareas",
        editor_selector : "preContent_temp",
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

<script type="text/javascript">
    //Upload thumbnail image for news
    $('#upload-thumbnail-image').on("click", function() {  /* cutomized button clicked */
        $('#file-thumbnail-image').click();  /* Now file upload button auto clicked & file browser dialog opens. */

    });

    $("#file-thumbnail-image").change(function() {
        var currElement = $("#thumbnail-image");
        uploadImage(currElement, this.files[0]);
    });

    //Upload and insert image to news detail
    $('#upload-news-detail-image').on("click", function() {  /* cutomized button clicked */
        $('#file-news-detail-image').click();  /* Now file upload button auto clicked & file browser dialog opens. */

    });

    $("#file-news-detail-image").change(function() {
        var currElement = $("#news-detail-image");
        var imgLink = insertImageToNews(currElement, this.files[0]);
    });

    $("#delete-thumbnail-image").click(function () {
        var object = $(this);
        $.ajax({
            type: "get",
            url: object.attr("hreflang")+'&thumbImg='+$("#form input[name='thumbImg']").val()+'&id='+$("#form input[name='id']").val(),
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
                            $("#form input[name='thumbImg']").val("");
                            $("#thumbnail-image").attr("src", "http://placehold.it/300x200&text=DROP+IMAGE+HERE");
                        });
            }
        });
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
                            //oldSrc=http://placehold.it/300x200&text=DROP+IMAGE+HERE
                            if (oldSrc.indexOf("placehold.it") > 0) {
                                if (oldSrc.lastIndexOf("&") > 0) {
                                    oldSrc = oldSrc.substring(0, oldSrc.lastIndexOf("&"));
                                    //oldSrc=http://placehold.it/300x200
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
                            $("input[name='thumbImg']").val(contentImage);
                        }
                    });
                }
            });
        }
    }
    function insertImageToNews (currElement, file) {
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
                            imgLink = '${imageServer}/get/'+element.name+'.jpg'+'?op=scale|770x';
                            tinyMCE.execCommand('mceInsertContent',false,"<img src='"+imgLink+"' width='770'/>");
                            //return "${imageServer}"+"/get/"+element.name+".jpg";
                        }
                    });
                }
            });
        }
    }
    $(function () {
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
            $("#thumbnail-image").removeClass("selected");
        }
    });

</script>