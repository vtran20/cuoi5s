<!DOCTYPE>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.create.new.website"/></title>
    <meta name="decorator" content="admin_new"/>
</head>

<body>
<div class="span10">
    <div>
        <ul class="breadcrumb">
            <li>
                <a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span>
            </li>
            <li class="active"><fmt:message key="site.footer.short.content"/></li>
        </ul>
    </div>

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

        });
    </script>

    <h:frontendmessage _messages="${messages}"/>

    <div class="row-fluid">
    <div class="span12">
    <!-- Portlet: Member List -->
    <div class="box" id="box-0">
    <h4 class="box-header round-top">
        <a class="box-btn" title="close"><i class="icon-remove"></i></a>
        <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
        <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                class="icon-cog"></i></a>
    </h4>
    <div class="box-container-toggle">

    <!-- Form Control States -->
    <div class="box-content">
        <spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>

        <form name="form" id="form" class="form-horizontal" action="/admin/sites/footer_content.html" method="post">
            <input name="id" type="hidden" value="${siteHeaderFooter.id}"/>

            <h:csrf/>
            <div class="row-fluid">
                <div class="span12">
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label" for="header"><fmt:message key="site.design.content.header"/></label>

                            <div class="controls">
                                <input name="footerHeader" class="input-xlarge" maxlength="30" id="header" type="text" value="${siteHeaderFooter.footerHeader}" autofocus/>
                            </div>
                        </div>
                    </fieldset>
                </div>

            </div>
            <div class="row-fluid">
                <div class="span12">
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label" for="footerContent"><fmt:message key="site.design.content.content"/></label>
                            <div class="controls">
                                <textarea name="footerContent" class="footerContent" maxlength="2000" rows="20" id="footerContent" style="width: 30%;">${siteHeaderFooter.footerContent}</textarea>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary"><fmt:message key="common.save.changes"/></button>
                    </div>
                </div>
            </div>
        </form>
    </div>
    </div>
    </div>
    </div>
</div>
    </div>
    <script type="text/javascript" src="/themes/tmce/tiny_mce.js"></script>

    <script type="text/javascript">
        tinyMCE.init({
            // General options
            mode : "specific_textareas",
            editor_selector : "footerContent",
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

</body>
</html>