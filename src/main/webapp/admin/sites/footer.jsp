<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<title><fmt:message key="site.footer.design"/></title>
<div class="page-header">
    <h4><fmt:message key="site.footer.design"/></h4>
    <p>Footer được hiển thị tối đa 4 cột. Mỗi cột tương ứng với 1 tab bên dưới. Bạn cần chọn hiển thị nếu muốn nội dung được hiển thị ở footer.</p>
</div><!-- /.page-header -->

<div class="row">
<div class="col-xs-12">

<div class="row">
<div class="col-sm-12">
<!-- #section:elements.tab -->
<div  id="message_alert"></div>
<div class="tabbable">
<ul class="nav nav-tabs" id="form-tab">
    <li class="active">
        <a data-toggle="tab" href="#home">
            <i class="ace-icon fa fa-cube"></i>
            Giới Thiệu Chung
        </a>
    </li>

    <li>
        <a data-toggle="tab" href="#usefulLink" data-tab-url="/admin/sites/footer_useful_link.html" data-tab-always-refresh="true">
            <i class="ace-icon fa fa-link"></i>
            Link Hữu Ích
        </a>
    </li>
    <li>
        <a data-toggle="tab" href="#news" data-tab-url="/admin/sites/footer_news_link.html" data-tab-always-refresh="true">
            <i class="ace-icon fa fa-link"></i>
            Tin Tức
        </a>
    </li>
    <li>
        <a data-toggle=tab href="#client_support" data-tab-url="/admin/sites/footer_support.html" data-tab-always-refresh="true">
            <i class="ace-icon fa fa-phone"></i>
            Hỗ Trợ Khách Hàng
        </a>
    </li>
    <li>
        <a data-toggle="tab" href="#contact_us" data-tab-url="/admin/sites/footer_contact_us.html" data-tab-always-refresh="true">
            <i class="ace-icon fa fa-home"></i>
            Thông Tin Liên Hệ
        </a>
    </li>
</ul>
    <spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>
<div class="tab-content">
<div id="home" class="tab-pane fade in active">
    <form name="form_footer_content" id="form_footer_content" class="form-horizontal" action="#" method="post">
        <h:csrf/>
        <fieldset>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" for="footerHeaderDisplay"><fmt:message key="common.active.status"/></label>
                <div class="col-sm-10 controls">
                    <c:choose>
                        <c:when test="${siteHeaderFooter.footerHeaderDisplay == 'Y'}">
                            <c:set var="footerHeaderDisplay" value="checked"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="footerHeaderDisplay" value=""/>
                        </c:otherwise>
                    </c:choose>
                    <label class="control-label">
                    <input name="footerHeaderDisplay" id="footerHeaderDisplay" type="checkbox" ${footerHeaderDisplay}/>
                    </label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" for="footerHeader"><fmt:message key="common.title"/></label>
                <div class="col-sm-10 forms">
                    <input name="footerHeader" class="input-xlarge" maxlength="50" id="footerHeader" type="text" value="${siteHeaderFooter.footerHeader}" autofocus/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" for="footerContent_temp"><fmt:message key="common.content"/></label>
                <div class="col-sm-10 forms">
                    <textarea name="footerContent_temp" class="footerContent_temp input-xxlarge" maxlength="50" id="footerContent_temp">${siteHeaderFooter.footerContent}</textarea>
                    <input type="hidden" id="footerContent" name="footerContent" />
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm btn-primary pull-left" type="submit">
                    <i class="ace-icon fa fa-check"></i>
                    <fmt:message key="common.save.changes"/>
                </button>
            </div>
        </fieldset>
    </form>
</div>

<div id="usefulLink" class="tab-pane fade">
<p>Loading</p>
</div>
<div id="news" class="tab-pane fade">
<p>Loading</p>
</div>

<div id="client_support" class="tab-pane fade">
    <p>Loading</p>
</div>

<div id="contact_us" class="tab-pane fade">
    <p>Loading</p>
</div>
</div>
</div>
</div>
</div>
</div>
</div>

<script>
    $('[data-rel=popover]').popover({container:'body'});

    $(function () {

        $("#form_footer_content").submit(function() {
            var url = "/admin/sites/footer_content.html"; // the script where you handle the form input.
            $('#footerContent').val(tinyMCE.get('footerContent_temp').getContent());
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form_footer_content").serialize(), // serializes the form's elements.
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
    });
    var remoteTabsPluginLoaded = new RemoteTabs();

</script>

<script type="text/javascript" src="/themes/tmce/tiny_mce.js"></script>
<script type="text/javascript">
    tinyMCE.init({
        // General options
        mode : "specific_textareas",
        editor_selector : "footerContent_temp",
        theme : "advanced",
//        skin : "bootstrap",
        content_css : "/themes/tmce/content.css",

        // Theme options
        theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
        theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",

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

    });
</script>
