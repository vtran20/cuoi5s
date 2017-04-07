<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="https://blueimp.github.io/jQuery-File-Upload/js/vendor/jquery.ui.widget.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="https://blueimp.github.io/jQuery-File-Upload/js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="https://blueimp.github.io/jQuery-File-Upload/js/jquery.fileupload.js"></script>
<div class="row">
    <div class="col-xs-12">
        <div id="message_alert"></div>
        <input type="hidden" id="images-selected" name="images-selected" value=""/>
        <input type="hidden" id="parent-id" name="parent-id" value=""/>
        <div class="tabbable" id="tabs">
            <ul class="nav nav-tabs" id="form-tab">
                <li class="active"><a href="#image_external" data-toggle="tab">Image Url</a></li>
                <li><a href="#upload" data-toggle="tab">Upload</a></li>
                <li><a href="#image_library" data-toggle="tab">Image Library</a></li>
            </ul>

            <div class="tab-content">
                <div class="tab-pane fade in active" id="image_external">
                    <div class="box-container-toggle">
                        <form class="form-horizontal">
                            <div class="control-group">
                                <label class="control-label" for="url">Image Url</label>
                                <div class="controls">
                                    <input name="url" class="input-xxlarge" id="url" type="text" maxlength="255" value=""/>
                                </div>
                            </div>
                            <%--<div class="control-group">--%>
                            <%--<label class="control-label" for="width">Width</label>--%>
                            <%--<div class="controls">--%>
                            <%--<input type="text" class="input-small" id="width" maxlength="4">--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="control-group">--%>
                            <%--<label class="control-label" for="height">Height</label>--%>
                            <%--<div class="controls">--%>
                            <%--<input type="text" class="input-small" id="height" maxlength="4">--%>
                            <%--</div>--%>
                            <%--</div>--%>
                        </form>
                    </div>
                </div>
                <div class="tab-pane fade" id="upload">
                    <div class="box-container-toggle">
                        <div class="box-content">
                            <blockquote>
                                <p>File Upload widget with multiple file selection, drag&amp;drop support, progress bars and
                                    preview
                                    images for jQuery.<br></p>
                            </blockquote>
                            <!-- The file upload form used as target for the file upload widget -->

                            <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
                            <%--<form id="fileupload" action="${imageServer}/images/uploads.json" method="POST" enctype="multipart/form-data">--%>

                                <input type="hidden" name="path" value="${site.siteCode}">
                                <input type="hidden" name="imagesPathOverride" value="${site.siteCode}">
                                <input type="hidden" name="generateName" value="on">
                                <input type="hidden" name="skipOptimization" value="on">

                                <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
                                <div class="box-content fileupload-buttonbar">
                                    <div class="span7">
                                        <!-- The fileinput-button span is used to style the file input field as button -->
                    <span class="btn btn-success fileinput-button">
                        <i class="icon-plus icon-white"></i>
                        <span>Add files...</span>
                        <input id=fileupload type="file" name="files[]" multiple="">
                    </span>
                                        <button type="submit" class="btn btn-primary start">
                                            <i class="icon-upload icon-white"></i>
                                            <span>Start upload</span>
                                        </button>
                                        <button type="reset" class="btn btn-warning cancel">
                                            <i class="icon-ban-circle icon-white"></i>
                                            <span>Cancel upload</span>
                                        </button>
                                        <button type="button" class="btn btn-danger delete">
                                            <i class="icon-trash icon-white"></i>
                                            <span><fmt:message key="common.delete"/></span>
                                        </button>
                                        <input type="checkbox" class="toggle">
                                    </div>
                                    <!-- The global progress information -->
                                    <div class="span5 fileupload-progress fade">
                                        <!-- The global progress bar -->
                                        <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                                            <div class="bar" style="width:0%;"></div>
                                        </div>
                                        <!-- The extended global progress information -->
                                        <div class="progress-extended">&nbsp;</div>
                                    </div>
                                </div>
                                <!-- The loading indicator is shown during file processing -->
                                <div class="fileupload-loading"></div>
                                <br>
                                <!-- The table listing the files available for upload/download -->
                                <table role="presentation" class="table table-striped">
                                    <tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
                                </table>
                            <%--</form>--%>
                            <br>

                            <div class="well">
                                <ul>
                                    <li>After upload, please select an image that you want at <strong>Image Library</strong> tab.</li>
                                    <li>You can <strong>drag &amp; drop</strong> files from your desktop on this webpage with Google Chrome, Mozilla Firefox and Apple Safari.</li>
                                    <li>The maximum file size for uploading is <strong>5 MB</strong></li>
                                    <li>Only image files (<strong>JPG, GIF, PNG</strong>) are allowed</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                        <%--<p>Loading</p>--%>

                </div>
                <div class="tab-pane fade" id="image_library">
                    <div class="box-container-toggle">
                        <div class="box-content">
                            <button class="btn btn-primary start" id="reload-images">
                                <i class="icon-refresh icon-white"></i>
                                <span>Reload Images</span>
                            </button>
                            <span>Click Reload Images if you cannot find your uploaded images.</span>
                            <hr>
                            <ul class="thumbs" id="list-images">
                                <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>

                                <!-- put images here -->
                                <script type="text/javascript">
                                    $.fn.loadImageLibrary = function() {
                                        $.ajax({
                                            type:'GET',
                                            url:'${imageServer}/folders/images.json',
                                            data:{ path:'${site.siteCode}' },
                                            dataType:'json',
                                            success:function (data) {
                                                $.each(data, function (index, element) {
                                                    if (element.images) {
                                                        $.each(element.images, function (index1, element1) {
                                                            $("<li class='image-library' lang='${imageServer}/get/" + element1.name + ".image'>")
                                                                    .append($("<img/>").attr("src", "${imageServer}/get/w/80/h/80/" + element1.name + ".image"))
                                                                    .append($("</li>")).appendTo("#list-images");
                                                        });
                                                    }
                                                });
                                            }
                                        });

                                    };
                                    $("#reload-images").click(function() {
                                        $("ul#list-images").html("");
                                        $(this).loadImageLibrary();
                                    });

                                    $("ul#list-images li.image-library").on('click',function() {
                                        $('ul#list-images li.selected').removeClass('selected');
                                        $(this).addClass("selected");
                                        $("input#images-selected").val($(this).attr("lang"));
                                    });

                                    $(this).loadImageLibrary();

                                </script>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal-footer">
    <a href="#" id="insert-image-selected" class="btn btn-primary" onclick="callbackParent();">Insert Image</a>
</div>

<script>

    /*global window, $ */
    $(function () {
        'use strict';
        $('#fileupload').fileupload({
            url: "${imageServer}/images/uploads.json",
            dataType: 'json',
            done: function (e, data) {
//                $.each(data.result.files, function (index, file) {
//                    $('<p/>').text(file.name).appendTo('#files');
//                });
                  alert ("done");
            },
//            progressall: function (e, data) {
//                var progress = parseInt(data.loaded / data.total * 100, 10);
//                $('#progress .progress-bar').css(
//                        'width',
//                        progress + '%'
//                );
//            }
        }).prop('disabled', !$.support.fileInput)
                .parent().addClass($.support.fileInput ? undefined : 'disabled');
    });

    function callbackParent() {
//        $("#modal-form").modal('hide');
        //Reload the current tab
//        var $link = $('li.active a[data-toggle="tab"]');
//        $link.parent().removeClass('active');
//        var tabLink = $link.attr('href');
//        $('#form-tab a[href="' + tabLink + '"]').tab('show');
    }
</script>

<!-- MultiFile Upload -->
<!-- Error messages for the upload/download templates -->
<script>
    var fileUploadErrors = {
        maxFileSize:'File is too big',
        minFileSize:'File is too small',
        acceptFileTypes:'Filetype not allowed',
        maxNumberOfFiles:'Max number of files exceeded',
        uploadedBytes:'Uploaded bytes exceed file size',
        emptyResult:'Empty file upload result'
    };
</script>
<!-- The template to display files available for upload -->
<script id="template-upload" type="text/html">
    {% for (var i=0, files=o.files, l=files.length, file=files[0]; i < l; file=files[++i]) { %}
    <tr class="template-upload fade">
        <td class="preview"><span class="fade"></span></td>
        <td class="name">{%=file.name%}</td>
        <td class="size">{%=o.formatFileSize(file.size)%}</td>
        {% if (file.error) { %}
        <td class="error" colspan="2"><span class="label label-important">Error</span> {%=fileUploadErrors[file.error]
            || file.error%}
        </td>
        {% } else if (o.files.valid && !i) { %}
        <td>
            <div class="progress progress-success progress-striped active">
                <div class="bar" style="width:0%;"></div>
            </div>
        </td>
        <td class="start">{% if (!o.options.autoUpload) { %}
            <button class="btn btn-primary">
                <i class="icon-upload icon-white"></i> Start
            </button>
            {% } %}
        </td>
        {% } else { %}
        <td colspan="2"></td>
        {% } %}
        <td class="cancel">{% if (!i) { %}
            <button class="btn btn-warning">
                <i class="icon-ban-circle icon-white"></i> Cancel
            </button>
            {% } %}
        </td>
    </tr>
    {% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/html">
    {% for (var i=0, files=o.files, l=files.length, file=files[0]; i < l; file=files[++i]) { %}
    <tr class="template-download fade">
        {% if (file.error) { %}
        <td></td>
        <td class="name">{%=file.name%}</td>
        <td class="size">{%=o.formatFileSize(file.size)%}</td>
        <td class="error" colspan="2"><span class="label label-important">Error</span> {%=fileUploadErrors[file.error]
            || file.error%}
        </td>
        {% } else { %}
        <td class="preview">{% if (file.thumbnail_url) { %}
            <a href="#" title="{%=file.name%}" rel="gallery"><img src="{%=file.thumbnail_url%}"></a>
            {% } %}
        </td>
        <td class="name">
            <a href="#" title="{%=file.name%}" rel="{%=file.thumbnail_url&&'gallery'%}">{%=file.name%}</a>
        </td>
        <td class="size">{%=o.formatFileSize(file.size)%}</td>
        <td colspan="2"></td>
        {% } %}
        <td class="delete">
            <button class="btn btn-danger" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}">
                <i class="icon-trash icon-white"></i> Delete
            </button>
            <input type="checkbox" name="delete" value="1">
        </td>
    </tr>
    {% } %}
</script>
