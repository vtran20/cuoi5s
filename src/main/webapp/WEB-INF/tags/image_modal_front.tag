<%--http://www.jqueryscript.net/other/Easy-jQuery-Image-Cropping-Plugin-with-Live-Previews-Image-Cropper.html--%>
<%--http://jsfiddle.net/dekkard/9r6czo65/12/--%>
<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>
<%@ attribute name="uploadable" required="false" rtexprvalue="true" type="java.lang.String"%>
<style type="text/css">
    /*.modal-body {*/
        /*min-height: 300px;*/
    /*}*/
    .img-container {
        min-height: 375px;
        max-height: 375px
    }
    #warning-image {
        margin-bottom: 10px;
        color: red;
    }
</style>
<div id="image-modal-form" class="modal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="blue bigger">Crop and Resize images</h4>
            </div>

            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12 col-sm-8">
                        <div class="img-container">
                            <img id="image" src="">
                        </div>
                        <div id="warning-image"></div>
                        <input type="file" name="image-cropper-upload" class="btn btn-xs btn-success hidden-md hidden-lg">
                        <c:if test="${!empty uploadable}">
                            <button class="btn btn-info" id="image-cropper-upload" name="loadImage" type="button" data-method="getData">Upload Image</button>
                        </c:if>
                        <%--<button class="btn btn-xs btn-info" name="loadImageFromUrl" type="button">Upload From Url</button>--%>
                    </div>
                    <div class="col-xs-12 col-sm-4">
                        <div id="x"></div>
                        <div id="y"></div>
                        <div id="width"></div>
                        <div id="height"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" name="approveImage"><i class="ace-icon fa fa-check"></i> OK</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
    $( document ).ready(function() {
        var console = window.console || { log: function () {} };
        var $image = $('#image');
        var crop = "";
        var imageRatio = "";
        var id = "";

        var options = {
            viewMode: 1,
            aspectRatio: imageRatio,
            minContainerWidth: 571,
            minContainerHeight: 375,
            crop: function(e) {
                // Output the result data for cropping image.
//                $("#x").html(e.x);
//                $("#y").html(e.y);
//                $("#width").html(e.width);
//                $("#height").html(e.height);
            }
        };
        var button;
        $('#image-modal-form').on('show.bs.modal', function(event){
            button = $(event.relatedTarget);
            url = button.data('img');
            console.log("url="+url);
            if (typeof url !== 'undefined' ) {
                if (url.indexOf('image.mangchiase.com') >= 0 || url.indexOf('images.webphattai.com') >= 0) {
                    $image.cropper('destroy').cropper(options).cropper('replace', url);
                } else {
                    $image.cropper('destroy').cropper(options).cropper('replace', url).cropper('clear');
                }
            }
        });
        $('#image-modal-form').on('shown.bs.modal', function(event){
            button = $(event.relatedTarget);
            crop = button.data('crop');
            var ratio = button.data('ratio');
            id = button.data('id');
            if (typeof ratio !== 'undefined' ) {
                imageRatio = ratio;
            }
        });


        $image.on('built.cropper', function (e) {
            //Change aspectRatio
            if( typeof imageRatio !== 'undefined' && imageRatio !== "") {
                aspectRatio = eval(imageRatio.replace('x','/'));
                $image.cropper('setAspectRatio', aspectRatio);
            }

            console.log('crop:'+crop);
            if (typeof crop !== 'undefined') {
                var res = crop.split(",");
                cropData = {"x":eval(res[0]),"y":eval(res[1]),"width":eval(res[2]),"height":eval(res[3])};
                $image.cropper('setData', cropData);
                console.log('cropToString:'+JSON.stringify(cropData));
            }

            if( typeof imageRatio !== 'undefined' && imageRatio != "") {
                imageData = $image.cropper('getImageData');
                cropWidth = eval (imageRatio.split('x')[0]);
                cropHeight = eval (imageRatio.split('x')[1]);
                canvas = $image.cropper('getCanvasData');
                minCropBoxWidth=Math.floor(eval((canvas.width*cropWidth)/imageData.naturalWidth));
                minCropBoxHeight=Math.floor(eval((canvas.height*cropHeight)/imageData.naturalHeight));

//                cropper = $image.data('cropper'); // Get the instance
//                cropBox = cropper.cropBox; // Get the crop box object
//                if (cropBox !== null) {
//                    cropBox.minWidth = minCropBoxWidth; // Set a new minimum width
//                    cropBox.minHeight = minCropBoxHeight; // Set a new minimum height
//                }

                //Warning if image is smaller than what we expect
                if (imageData.naturalWidth != 1 && (imageData.naturalWidth < cropWidth || imageData.naturalHeight < cropHeight)) {
                    $("#warning-image").html("<fmt:message key='common.image.smaller.than.required'/> "+imageRatio);
                } else {
                    $("#warning-image").html("")
                }

                console.log('imageData:'+JSON.stringify(imageData));
                console.log("cropWidth:"+cropWidth);
                console.log("cropHeight:"+cropHeight);
                console.log("minCropBoxWidth:"+minCropBoxWidth);
                console.log("minCropBoxHeight:"+minCropBoxHeight);
                console.log("imageData.naturalWidth:"+imageData.naturalWidth);
//                console.log("cropBox:"+JSON.stringify(cropBox));
//                console.log("getCanvasData:"+JSON.stringify($image.cropper('getCanvasData')));
//                $image.cropper(options);
            }

        });

        $('#image-modal-form').on('hide.bs.modal', '.modal', function () {
            $(this).removeData('bs.modal');
            $image.cropper('destroy');
        });



        $("button[name=loadImageFromUrl]").on("click", function () {
            $image.cropper('setData', {"x":0,"y":129,"width":1024,"height":540});
//            var url = prompt("Please enter image URL", "");
//            if (url != null) {
//                loadImage($image, url, 8/4)
//            }
        });
//        $("button[name=loadImage]").on("click", function () {
//            var url = prompt("Please enter video name", "");
//            if (url != null) {
//                $image.cropper('reset').cropper('replace', url);
//            }
//        });
        $("button[name=approveImage]").on("click", function () {
            console.log ($image.attr("src"))
            if ($image.attr("src")) {
                var image = $image.cropper('getData');
                console.log(Math.round(image.x));
                console.log(Math.round(image.y));
                console.log(Math.round(image.width));
                console.log(Math.round(image.height));
                // Make sure the callback is a function​
                if (typeof callbackFromImageModal === "function") {
                    // Call it, since we have confirmed it is callable​
                    callbackFromImageModal(image, button, $image.attr('src'));
                }
                $('#image-modal-form').modal('hide');
            } else {
                $('#image-modal-form').modal('hide');
            }
        });

        /************Start Upload Image*************/
            //Upload thumbnail image for news
        $('#image-cropper-upload').on("click", function() {  /* cutomized button clicked */
            console.log('enter upload')
            $('input[name=image-cropper-upload]').click();  /* Now file upload button auto clicked & file browser dialog opens. */

        });

        $("input[name=image-cropper-upload]").change(function() {
            uploadImageToCropper($image, this.files[0]);
        });

        function uploadImageToCropper (currElement, uploadFile) {
            //show spinner
            var parent = currElement.parent();
            parent.append("<img src='/themes/editor/img/spinner.gif' id='spinner-gif'>");
            var _URL = window.URL || window.webkitURL;
            var file, img;
            if ((file = uploadFile)) {
                img = new Image();
                img.src = _URL.createObjectURL(file);
                img.onload = function() {
                    wh = imageRatio.split("x")
                    if (this.width < wh[0] && this.height < wh[1]) {
                        console.log(wh)
                        console.log(file.name)
                        console.log(this.width + " " + this.height);
                        $("#warning-image").html("<div class='alert alert-danger'><fmt:message key='common.image.smaller.size.ignore'/><button type='button' class='close' data-dismiss='alert'>x</button></div>")
                        //remove spinner if any
                        if (parent.find("#spinner-gif")) {
                            parent.find("#spinner-gif").remove();
                        }
                    } else {
                        var data = new FormData();
                        data.append('file', file);
                        data.append('path','${site.siteCode}');
                        data.append('imagesPathOverride','${site.siteCode}');
                        data.append('generateName','on');
                        data.append('skipOptimization','on');
                        var uploadImageUrl;
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
                                        console.log("${imageServer}"+"/get/"+element.name+".jpg");
                                        uploadImageUrl = "${imageServer}"+"/get/"+element.name+".jpg";
                                        $image.cropper('destroy').cropper(options).cropper('replace', uploadImageUrl);
                                    }
                                });
                            }
                        });
                    }
                };
                img.onerror = function() {
                    //remove spinner
                    if (parent.find("#spinner-gif")) {
                        parent.find("#spinner-gif").remove();
                    }
                    console.log( "not a valid file: " + file.type);
                };
            }
        }
        /************End Upload Image*************/
    });
</script>
