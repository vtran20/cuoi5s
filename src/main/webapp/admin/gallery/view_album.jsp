<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="albumId" value="${param.id}"/>
<c:if test="${! empty albumId}">
    <spring:eval expression="serviceLocator.albumDao.findById(T(java.lang.Long).valueOf(albumId))" var="album"/>
</c:if>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<div class="page-header">
    <a class="btn btn-xs btn-success" id="upload-image-button">
        <i class="fa fa-cloud-upload"></i> <fmt:message key="common.upload.image"/>
    </a>
    <form id="fileupload" action="${imageServer}/images/uploads.json" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="path" value="${site.siteCode}">
        <input type="hidden" name="imagesPathOverride" value="${site.siteCode}">
        <input type="hidden" name="generateName" value="on">
        <input type="hidden" name="skipOptimization" value="on">
        <input type="file" name="files[]" multiple="" id="upload-image-file" style="display: none">
    </form>
</div><!-- /.page-header -->
<script>
    $(function(){
        $('#upload-image-button').on("click", function() {  /* cutomized button clicked */
            $('#upload-image-file').click();  /* Now file upload button auto clicked & file browser dialog opens. */

        });
        //Upload multiple files
        $("#upload-image-file").change(function() {
            //var currElement = $("#thumbnail-image");
            uploadImages($("#upload-image-button"), this.files);

        });
        function uploadImages (currElement, files) {
            if(confirm('<fmt:message key="news.do.you.want.to.upload.the.current.image"/>')) {
                //show spinner
                var parent = currElement.parent();
                parent.append("<img src='/themes/editor/img/spinner.gif' id='spinner-gif'>");
                var i = 0, len = files.length;
                for (; i < len; i++) {
                    var res = new FormData();
                    res.append('path','${site.siteCode}');
                    res.append('imagesPathOverride','${site.siteCode}');
                    res.append('generateName','on');
                    res.append('skipOptimization','on');
                    res.append('files[]', files[i]);
                    $.ajax({
                        type:"POST",
                        xhrFields: {withCredentials: false},
                        url:'${imageServer}/images/uploads.json',
                        data:res,
                        dataType: 'json',
                        contentType: false,
                        processData: false,
                        success:function(res){
                            //remove spinner
                            if (parent.find("#spinner-gif")) {
                                parent.find("#spinner-gif").remove();
                            }
                            for (var i=0 ; i < res.length; i++) {
                            //$.each(res, function(index, element) {
                                if (res[i].error) {
                                } else {
                                    <%--url = "/admin/gallery/insert_image.html?uri="+res[i].uri+"&imageName="+res[i].name+"&csrf=<sec:authentication property="details.csrf"/>&albumId="+${albumId};--%>
                                    <%--$.getJSON( url, function( result ) {--%>
                                        <%--//Render html from data return and append to the current page--%>
                                        <%--var temp = $("#album_image_mustache").html();--%>
                                        <%--var newImage = Mustache.render(temp, result);--%>
                                        <%--$(".ace-thumbnails").append(newImage);--%>
                                    <%--});--%>

                                    //Insert into AlbumImage

                                    $.ajax({
                                        type: 'GET',
                                        contentType: "application/json; charset=utf-8",
                                        dataType: "json",
                                        url: '/admin/gallery/insert_image.html',
                                        data: { uri: res[i].uri, imageName:res[i].name, csrf:<sec:authentication property="details.csrf"/>, albumId:${albumId} },
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
                        }
                    });
                }
            }
        }
    });
</script>

<script type="text/x-mustache" id="album_image_mustache">
    <li>
        <a href="${imageServer}/get/w/150/{{uri}}.jpg" data-rel="colorbox">
            <img width="150" alt="150x150" src="${imageServer}/get/w/150/{{uri}}.jpg" />
            <div class="text">
                <div class="inner"></div>
            </div>
        </a>

        <div class="tools tools-bottom">
            <a href="#" data-toggle="modal" data-page="/admin/gallery/image_form.html?id={{id}}" data-target="#modal-form">
                <i class="ace-icon fa fa-pencil"></i>
            </a>
            <a class="delete-album-image" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>' hreflang="/admin/gallery/deleteimage.html?id={{id}}&csrf=<sec:authentication property="details.csrf"/>" removeUrl="{{delete_url}}">
                <i class="ace-icon fa fa-times red"></i>
            </a>
        </div>
    </li>
</script>
<div class="row">
    <div class="col-xs-12">
        <div  id="message_alert"></div>
        <!-- Portlet: Gallery -->
            <div class="box">
                <h4 class="box-header round-top">${album.name}
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>
                <div class="box-container-toggle">
                    <div class="box-content">
                        <div id="gallery" data-toggle="modal-gallery" data-target="#modal-gallery">
                            <spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>
                            <spring:eval expression="params.getParameter('id')" var="albumId"/>
                            <spring:eval expression="serviceLocator.albumImageDao.findAlbumImages(albumId, 0, 100, 'id', site.id, true)" var="albumImages"/>
                            <ul class="ace-thumbnails clearfix">
                            <c:forEach items="${albumImages}" var="image">
                                <%--<img src="${imageServer}/get/w/150/${image.uri}.jpg"/>--%>
                                <li>
                                    <a href="${imageServer}/get/w/150/${image.uri}.jpg" data-rel="colorbox">
                                        <img width="150" alt="150x150" src="${imageServer}/get/w/150/${image.uri}.jpg" />
                                        <div class="text">
                                            <div class="inner">${image.description}</div>
                                        </div>
                                    </a>

                                    <div class="tools tools-bottom">
                                        <a href="#" data-toggle="modal" data-title='<fmt:message key="album.image.update"/>' data-page="/admin/gallery/image_form.html?id=${image.id}" data-target="#modal-form">
                                            <i class="ace-icon fa fa-pencil"></i>
                                        </a>
                                        <c:set var="key" value="${image.uri}"/>
                                        <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).encrypt(key)" var="keyEncrypted"/>
                                        <c:url value="${imageServer}/images/remove.json" var="removeUrl">
                                            <c:param name="key" value="${keyEncrypted}"/>
                                            <c:param name="path" value="${site.siteCode}"/>
                                        </c:url>
                                        <a class="delete-album-image" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>' hreflang="/admin/gallery/deleteimage.html?id=${image.id}&csrf=<sec:authentication property="details.csrf"/>" removeUrl="${removeUrl}&name=${image.uri}">
                                            <i class="ace-icon fa fa-times red"></i>
                                        </a>
                                    </div>
                                </li>
                            </c:forEach>
                            </ul>
                        </div>
                        <hr>
                        <br>

                    </div>
                </div>
                <!--/span-->
            </div>
            <!-- Portlet Set 3 -->
    </div>
    <!--/row-->
</div>

<script type="text/javascript">
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

    var scripts = [null,"/admin/assets/admin_wpt/js/jquery.colorbox.js", null]
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
    //inline scripts related to this page
    jQuery(function($) {
        var $overflow = '';
        var colorbox_params = {
            rel: 'colorbox',
            reposition:true,
            scalePhotos:true,
            scrolling:false,
            previous:'<i class="ace-icon fa fa-arrow-left"></i>',
            next:'<i class="ace-icon fa fa-arrow-right"></i>',
            close:'&times;',
            current:'{current} of {total}',
            maxWidth:'100%',
            maxHeight:'100%',
            onOpen:function(){
                $overflow = document.body.style.overflow;
                document.body.style.overflow = 'hidden';
            },
            onClosed:function(){
                document.body.style.overflow = $overflow;
            },
            onComplete:function(){
                $.colorbox.resize();
            }
        };

        $('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
        $("#cboxLoadingGraphic").html("<i class='ace-icon fa fa-spinner orange fa-spin'></i>");//let's add a custom loading icon


        $(document).one('ajaxloadstart.page', function(e) {
            $('#colorbox, #cboxOverlay').remove();
        });
    })
});

</script>
<%--</body>--%>
<%--</html>--%>
<h:form_modal/>