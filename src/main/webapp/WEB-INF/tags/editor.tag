<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%--This use for admin when users edit the site--%>
<sec:authorize url="/admin/page/index.html">
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<c:set value="${fn:replace(uri,'index.html','')}" var="uri"/>
<c:if test="${!(fn:indexOf(uri, '.html') >= 0 || fn:indexOf(uri, '/news/') >= 0)}">
<c:set value="${fn:replace(uri,'/','')}" var="uri"/>
<c:set value="${fn:replace(uri,'index.wpt','')}" var="uri"/>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<%--<link rel="stylesheet" href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" type="text/css"/>--%>
<link rel="stylesheet" href="/wro/${version}left_panel_css.css" type="text/css"/>

<script type="text/javascript">
    $(function() {
        /********************************************************************************************************************
         LEFT PANEL
         ********************************************************************************************************************/
        $("#editor-left-navigation").buildMbExtruder({
            position:"left",
            width:305,
            sensibility:800,
            extruderOpacity:.8,
            flapDim: 100,
            hidePanelsOnClose:true,
            onExtOpen:function(){},
            onExtContentLoad:function(){},
            onExtClose:function(){}
        });
        //Default open page design editor
        $("#editor-left-navigation").openMbExtruder();
        /********************************************************************************************************************
         CLOSES ALL S ON PAGE LOAD
         ********************************************************************************************************************/
        $( "#extruder-wrapper" ).accordion({
            heightStyle: "content"
        });

        /********************************************************************************************************************
         Implement Edit Webpage (Drag and Drop)
         ********************************************************************************************************************/

        $( "#page-content" ).sortable({
            placeholder: "entire-page-template-hover",
            revert: true,
            containment: "body",
            distance: 50,
            items: "section:not(.ui-state-disabled)",
            update: function( event, ui ) {
                //updateReOrder(event, ui)
            }
        });

        //Drag and drop for template page
        $( "li.page-template" ).draggable({
            containment: "#page-content",
            cursor: 'move',
            opacity: 0.5,
            helper: function( event ) {
                //set the height of #page-content is 1000px or more to make sure that the page template content will be covered by this height
                $("#page-content").attr("style","height: 1000px");
                return $( "#page-template-"+$(this).attr("id")).html();
            }
        });


        $( "#modal-on-click" ).draggable({
            handle: ".modal-header"
        });

        //Drag and drop for widgets
        $( "li.widget-template" ).draggable({
            containment: "#page-content",
            cursor: 'move',
            opacity: 0.5,
            helper: function( event ) {
                $("#page-content").attr("style","height: 20000px");
                return $( "#widget-template-"+$(this).attr("id")).html();
            }
        });
        //Drag and drop for header
        $( "li.header-template" ).draggable({
            containment: "header",
            cursor: 'move',
            opacity: 0.5,
            helper: function( event ) {
                $("header").attr("style","height: 200px");
                return $( "#header-template-"+$(this).attr("id")).html();
            }
        });
        //Drag and drop for footer
        $( "li.footer-template" ).draggable({
            containment: "footer",
            cursor: 'move',
            opacity: 0.5,
            helper: function( event ) {
                $("footer").attr("style","height: 400px");
                return $( "#footer-template-"+$(this).attr("id")).html();
            }
        });

        $('div ul.thumbs li.widget-template-thumb-img img').hover( function(){
            var source = $(this).attr("src");
            source = source.replace("_thumb","");
            var image = $('<img>').attr("src", source).attr("width", 600).attr("height", 600);
            $(this).popover({
                trigger: 'hover',
                template: '<div class="popover"><div class="arrow"></div><div class="popover-inner editor-popover"><h3 class="popover-title"></h3><div class="popover-content"><p></p></div></div></div>',
                html : true,
                content: image[0],
                title: ''
            });
        });

        //This is configuration for uploading image by drag and drop
        var pageConfig = {
            'imageupload': {
                path:'${site.siteCode}',
                imagesPathOverride:'${site.siteCode}',
                generateName:'on',
                skipOptimization:'on',
                url:'${imageServer}/images/upload.json',
                serverurl:'${imageServer}'
            },
            csrf:'<sec:authentication property="details.csrf"/>',
            uri:'${uri}'
        };
        $.extend(ieditor.config, pageConfig);
    });
</script>
<app:cache key="front-end-editor">

    <spring:eval expression="serviceLocator.widgetTemplateDao.getWidgetTemplateByType('page', 'Y')" var="pageTemplates"/>
    <spring:eval expression="serviceLocator.widgetTemplateDao.getWidgetTemplateByType('widget', 'Y')" var="widgetTemplates"/>
    <spring:eval expression="serviceLocator.widgetTemplateDao.getWidgetTemplateByType('footer', 'Y')" var="footerTemplates"/>
    <spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>

    <div class="wrapper" id="left-navigator">
    <div id="editor-left-navigation" class="a {title:'Design Site'}">
        <div class="editor-button" style="padding-bottom: 2px;">
                <%--<a class="btn btn-small" href="#preview"><i class="icon-search"></i> Preview</a>--%>
            <a class="btn btn-small" href="#save"><i class="icon-ok"></i> <fmt:message key="editor.save"/></a>
            <a class="btn btn-small" href="#publish"><i class="icon-globe"></i> <fmt:message key="editor.publish"/></a>
            <a class="btn btn-small" href="#undo" id="undo-button"><i class="icon-circle-arrow-left"></i> <fmt:message key="editor.undo"/></a>
            <a class="btn btn-small" href="/admin/j_spring_security_logout"><i class="icon-off"></i> <fmt:message key="editor.logout"/></a>
        </div>
        <div id="alert-message" class="alert hide"></div>
        <div id="extruder-wrapper">
            <h3><fmt:message key="editor.page.templates"/></h3>

            <div>
                <ul class="thumbs">
                    <c:forEach items="${pageTemplates}" var="template">
                        <li id="page-dragable-${template.id}" class="page-template widget-template-thumb-img"><img src="${template.imageUrl}" width="80" height="80"/></li>
                    </c:forEach>
                </ul>
            </div>
            <h3><fmt:message key="editor.page.widgets"/></h3>

            <div>
                <ul class="thumbs">
                    <c:forEach items="${widgetTemplates}" var="widgetTemplate">
                        <li id="widget-dragable-${widgetTemplate.id}" class="widget-template widget-template-thumb-img"><img src="${widgetTemplate.imageUrl}"  width="80" height="80"/></li>
                    </c:forEach>
                </ul>
            </div>
            <%--<h3>Header Templates</h3>--%>
            <%--<div>--%>
                <%--<ul class="thumbs">--%>
                    <%--<li id="header-dragable-1" class="header-template"><img src="http://placehold.it/80x80"/></li>--%>
                    <%--<li id="header-dragable-2" class="header-template"><img src="http://placehold.it/80x80"/></li>--%>
                <%--</ul>--%>
            <%--</div>--%>
            <h3><fmt:message key="editor.footer.templates"/></h3>
            <div>
                <ul class="thumbs">
                    <c:forEach items="${footerTemplates}" var="footerTemplate">
                    <li id="footer-dragable-${footerTemplate.id}" class="footer-template widget-template-thumb-img"><img src="${footerTemplate.imageUrl}"/></li>
                    </c:forEach>
                </ul>
            </div>
            <%--<h3>Color/Font Site</h3>--%>

            <%--<div>--%>
                <%--<ul class="thumbs">--%>
                    <%--<c:forEach items="${colors}" var="color">--%>
                        <%--<li id="site-color-template-${color.id}" class="site-color-template" data-src="${color.css1}"><img src="${color.imageUrl}"/></li>--%>
                    <%--</c:forEach>--%>
                <%--</ul>--%>
            <%--</div>--%>
            <h3><fmt:message key="editor.images.library"/></h3>

            <div>
                <ul class="thumbs" id="list-images">

                    <!-- put images here -->
                    <script type="text/javascript">
                        $(function () {
                            flag = true;
                            $.ajax({
                                type: 'GET',
                                url: '${imageServer}/folders/images.json',
                                data: { path: '${site.siteCode}' },
                                dataType: 'json',
                                success: function (data) {
                                    $.each(data, function(index, element) {
                                        //for some reason, success is called twice, so using 'flag' to prevent one.
                                        if (flag && element.images) {
                                            flag = false;
                                            $.each(element.images, function(index1, element1) {
                                                $("<li class='image-library ui-draggable' data-src='${imageServer}/get/"+element1.name+".image' title='"+element1.original+"'>").append($("<img/>").attr("src", "${imageServer}/get/w/80/h/80/"+element1.name+".image"))
                                                        .append($("</li>")).appendTo("#list-images").draggable({
                                                            containment: "div.entire-page-level",
                                                            cursor: 'move',
                                                            opacity: 0.5,
                                                            helper: function( event ) {
                                                                return $(this).html();
                                                            }
                                                        });
                                            });
                                        }
                                    });
                                }
                            });
                        });
                    </script>
                </ul>
            </div>
        </div>
    </div>
    <div id='modal-on-click' class='modal hide fade'><div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button><h5><fmt:message key="editor.edit.element"/></h5></div><div class='modal-body'></div><div class='modal-footer'>    <a href='#' data-dismiss='modal' class='btn'><fmt:message key="editor.close"/></a><a id='removeElement' class='btn btn-danger'><fmt:message key="editor.delete"/></a><a id='updateElement' class='btn btn-primary'><fmt:message key="editor.update"/></a></div></div>
</div>

<!--Pages templates-->
<c:forEach items="${pageTemplates}" var="template">
    <div id="page-template-page-dragable-${template.id}" class="hide">
            ${template.content}
    </div>
</c:forEach>
<!--Widget templates-->
<c:forEach items="${widgetTemplates}" var="widgetTemplate">
    <div id="widget-template-widget-dragable-${widgetTemplate.id}" class="hide">
            ${widgetTemplate.content}
    </div>
</c:forEach>
<%--Header templates--%>
<div id="header-template-header-dragable-1" class="hide">
    <div class="container">
        <div class="row">
            <div class="span4 logo">
                <h1 class="logo-title"><a href="/">Logo 1</a></h1>
                <p class="slogan">Slogan 1</p>
            </div>
            <div class="span8">
                <div class="row">
                    <div class="span8 customer_service">
                        <h4>
                            <small>For additional information call: <strong>+00 1234 5678</strong></small>
                        </h4>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<div id="header-template-header-dragable-2" class="hide">
    <div class="container">
        <div class="row">
            <div class="span4 logo">
                <a href="/">
                    <img src="http://placehold.it/250x150"/>
                </a>
                <p class="slogan">Slogan 2</p>
            </div>
            <div class="span8">
                <div class="row">
                    <div class="span8 customer_service">
                        <h4>
                            <small>For additional information call: <strong>+00 1234 5678</strong></small>
                        </h4>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<%--Footer templates--%>
<c:forEach items="${footerTemplates}" var="footerTemplate">
    <div id="footer-template-footer-dragable-${footerTemplate.id}" class="hide">
            ${footerTemplate.content}
    </div>
</c:forEach>
<jsp:include page="/themes/editor/modal.uts" flush="true"/>
<div id='browser-support' class='modal hide fade'>
    <div class='modal-header'>
        <button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>
        <h5><fmt:message key="editor.message"/></h5></div>
    <div class='modal-body'><p><fmt:message key="editor.browser.support.message"/></p><ul><li>Download <a href='http://www.google.com/chrome'>Chrome</a></li><li>Download <a href='http://www.mozilla.org/en-US'>Firefox</a></li></ul></div>
    <div class='modal-footer'></div>
</div>
<!--[if IE]>
<script type="text/javascript">
$("#browser-support").modal({
    backdrop:false
});
</script>
<![endif]-->
<script src="/wro/${version}left_panel_js.js" type="text/javascript"></script>
</app:cache>
</c:if>
</sec:authorize>

