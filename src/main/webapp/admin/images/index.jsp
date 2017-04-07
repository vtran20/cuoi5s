<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="images.upload.images"/></title>
    <meta name="decorator" content="admin_new"/>
</head>

<body>
<div class="span10">
    <div>
        <ul class="breadcrumb">
            <li>
                <a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span>
            </li>
            <li class="active"><fmt:message key="images.upload.images"/></li>
        </ul>
    </div>

    <!-- Portlet: Upload Images -->
    <div class="box">
        <h4 class="box-header round-top"><fmt:message key="images.upload.images"/>
            <a class="box-btn" title="close"><i class="icon-remove"></i></a>
            <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
            <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i class="icon-cog"></i></a>
        </h4>

        <div class="box-container-toggle">
            <div class="box-content">
                <blockquote>
                    <p><fmt:message key="images.upload.image.info"/><br></p>
                </blockquote>
                <!-- The file upload form used as target for the file upload widget -->

                <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
                <form id="fileupload" action="${imageServer}/images/uploads.json" method="POST"
                      enctype="multipart/form-data">

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
                    <span><fmt:message key="images.add.files"/></span>
                    <input type="file" name="files[]" multiple="">
                </span>
                            <button type="submit" class="btn btn-primary start">
                                <i class="icon-upload icon-white"></i>
                                <span><fmt:message key="images.start.upload"/></span>
                            </button>
                            <button type="reset" class="btn btn-warning cancel">
                                <i class="icon-ban-circle icon-white"></i>
                                <span><fmt:message key="images.cancel.upload"/></span>
                            </button>
                            <button type="button" class="btn btn-danger delete">
                                <i class="icon-trash icon-white"></i>
                                <span><fmt:message key="images.delete"/></span>
                            </button>
                            <input type="checkbox" class="toggle">
                        </div>
                        <!-- The global progress information -->
                        <div class="span5 fileupload-progress fade">
                            <!-- The global progress bar -->
                            <div class="progress progress-success progress-striped active" role="progressbar"
                                 aria-valuemin="0" aria-valuemax="100">
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
                </form>
                <br>

                <div class="well">
                    <h3><fmt:message key="images.notes.title"/></h3>
                    <ul>
                        <fmt:message key="images.notes.detail"/>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

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

</body>
</html>