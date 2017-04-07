<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<div class="page-header">
    <h4><fmt:message key="images.view.images"/></h4>
</div>
<div class="row">
    <div class="col-xs-12">
        <div  id="message_alert"></div>
        <!-- Portlet: Gallery -->
        <div class="box">
            <div class="box-container-toggle">
                <div class="box-content">
                    <div id="image_library" data-toggle="modal-gallery" data-target="#modal-gallery">
                        <ul class="ace-thumbnails clearfix">
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
<script type="text/x-mustache" id="album_image_mustache">
    <li>
        <a href="#/admin/images/view" data-rel="colorbox">
            <img width="150" alt="150x150" src="${imageServer}/get/w/150/{{name}}.jpg" />
            <div class="text">
                <div class="inner"></div>
            </div>
        </a>
        <div class="tools tools-bottom">
            <a class="delete-album-image" lang='<fmt:message key="common.confirm.delete.image"/>' hreflang="/admin/images/deleteimage.html?csrf=<sec:authentication property="details.csrf"/>&uri={{name}}" removeUrl="${imageServer}/images/remove.json?path=${site.siteCode}&name={{name}}">
                <i class="ace-icon fa fa-times red"></i>
            </a>
        </div>
    </li>
</script>
<script type="text/javascript">
    $(function () {
        $.ajax({
            type: 'GET',
            url: '${imageServer}/folders/images.json',
            data: { path: '${site.siteCode}' },
            dataType: 'json',
            success: function (data) {
                $.each(data, function(index, element) {
                    if (element.images) {
                        $.each(element.images, function(index, result) {
                            //Render html from data return and append to the current page
                            var temp = $("#album_image_mustache").html();
                            var newImage = Mustache.render(temp, result);
                            $(".ace-thumbnails").append(newImage);

                            <%--$("<a href='#${imageServer}/get/"+element.name+".image' title='"+element.original+"'>")--%>
                                    <%--.append($("<img/>").attr("src", "${imageServer}/get/w/150/"+element.name+".image").attr("rel","gallery"))--%>
                                    <%--.append($("</a>")).appendTo("#gallery");--%>
                        });
                    }
                });
            }
        });
    });
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
                                $.ajax({
                                    url: object.attr("removeUrl")+'&key='+data,
                                    xhrFields: {
                                        withCredentials: false
                                    }
                                }).done(function( data ) {
                                    //Remove image
                                    object.closest('li').remove();
                                });
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