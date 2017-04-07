<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%--This use for admin when users edit the site--%>
<sec:authorize url="/admin/page/index.html">
    <style type="text/css">
        #myModal .modal-body {
            max-height: 600px;
        }

        #myModal {
            width: 900px; /* SET THE WIDTH OF THE MODAL */
            margin: -380px 0 0 -450px; /* CHANGE MARGINS TO ACCOMODATE THE NEW WIDTH (original = margin: -250px 0 0 -280px;) */
        }
    </style>
    <div class="modal hide fade" id="myModal">
        <div class="modal-header ui-droppable">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>Images</h3>
        </div>
        <div class="modal-body">
            <input type="hidden" id="images-selected" name="images-selected" value=""/>
            <input type="hidden" id="parent-id" name="parent-id" value=""/>
            <ul class="nav nav-tabs">
                <li class="active"><a href="#image_external" data-toggle="tab">Image Url</a></li>
                <li><a href="#upload" data-toggle="tab">Upload</a></li>
                <li><a href="#image_library" data-toggle="tab">Image Library</a></li>
            </ul>

            <div class="tab-content">
                <div class="tab-pane active" id="image_external">
                    <div class="box-container-toggle">
                        <div class="box-content">

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
                </div>
                <div class="tab-pane" id="upload">
                    <div class="box-container-toggle">
                        <div class="box-content">
                            <blockquote>
                                <p>File Upload widget with multiple file selection, drag&amp;drop support, progress bars and
                                    preview
                                    images for jQuery.<br></p>
                            </blockquote>
                            <!-- The file upload form used as target for the file upload widget -->

                            <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
                            <form id="fileupload" action="${imageServer}/images/uploads.json" method="POST" enctype="multipart/form-data">

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
                    <input type="file" name="files[]" multiple="">
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
                                <ul>
                                    <li>After upload, please select an image that you want at <strong>Image Library</strong> tab.</li>
                                    <li>You can <strong>drag &amp; drop</strong> files from your desktop on this webpage with Google Chrome, Mozilla Firefox and Apple Safari.</li>
                                    <li>The maximum file size for uploading is <strong>5 MB</strong></li>
                                    <li>Only image files (<strong>JPG, GIF, PNG</strong>) are allowed</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tab-pane" id="image_library">
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
        <div class="modal-footer">
            <a href="#" id="insert-image-selected" class="btn btn-primary">Insert Image</a>
        </div>
    </div>

</sec:authorize>

