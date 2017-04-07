<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>Template and Color Site</title>
</head>

<body>
<script>
    $(function () {

        $("#template_update").validate({
            messages:{
                <%--name:"<fmt:message key="menu.name.required"/>"--%>
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

<form name="template_update" id="template_update" class="form-horizontal" action="/admin/sites/template_update.html" method="post">
<spring:eval expression="serviceLocator.siteTemplateDao.findUniqueBy('site.id', site.id)" var="siteTemplate"/>
    <input name="id" type="hidden" value="${site.id}"/>

    <fieldset>

        <%--<div class="control-group">--%>
            <%--<label class="control-label" for="templateCode"><fmt:message key="site.form.site.template"/></label>--%>
            <%--<div class="controls">--%>
                <%--<input name="templateCode" type="hidden" value="${siteTemplate.templateCode}" id="templateCode"/>--%>
                <%--<div>Template Code: ${siteTemplate.templateCode}</div>--%>
                <%--<img src="http://placehold.it/150x100" alt="">--%>
                <%--<a class="btn btn-success" href="/admin/sites/templates.html" data-toggle="modal" data-target="#templateModel">--%>
                    <%--<i class="icon-zoom-in icon-white"></i> Select Template--%>
                <%--</a>--%>
            <%--</div>--%>
        <%--</div>--%>
        <div class="control-group">
            <label class="control-label" for="templateColorCode"><fmt:message key="site.form.site.color"/></label>
            <div class="controls">
                <style>
                    .list-unstyled {
                        padding-left: 0;
                        list-style: none;
                    }
                    .style-switcher li {
                        width: 26px;
                        height: 26px;
                        cursor: pointer;
                        background: #c00;
                        margin: 0 5px 5px 0;
                        display: inline-block;
                        border-radius: 10% !important;
                        border: solid 2px #FFF;
                    }
                    ul {
                        padding: 0;
                        margin: 0 0 9px 0px;
                    }
                    .style-switcher li.theme-green {
                        background: #72c02c;
                    }
                    .style-switcher li.theme-blue {
                        background: #3498db;
                    }
                    .style-switcher li.theme-orange {
                        background: #e67e22;
                    }
                    .style-switcher li.theme-red {
                        background: #e74c3c;
                    }
                    .style-switcher li.theme-light {
                        background: #ecf0f1;
                    }
                    .style-switcher li.theme-purple {
                        background: #9b6bcc;
                    }
                    .style-switcher li.theme-aqua {
                        background: #27d7e7;
                    }
                    .style-switcher li.theme-brown {
                        background: #9c8061;
                    }
                    .style-switcher li.theme-dark-blue {
                        background: #4765a0;
                    }
                    .style-switcher li.theme-light-green {
                        background: #79d5b3;
                    }
                    .style-switcher li.theme-dark-red {
                        background: #a10f2b;
                    }
                    .style-switcher li.theme-teal {
                        background: #18ba9b;
                    }
                    .style-switcher li:hover, .style-switcher li.theme-active {
                        border: solid 2px red;
                    }
                </style>
                <input name="templateColorCode" type="hidden" value="${siteTemplate.templateColorCode}" id="templateColorCode"/>
                <div class="style-switcher animated fadeInRight" style="display: block;">
                    <ul class="list-unstyled" id="color-group">
                        <li class="theme-green <c:if test="${siteTemplate.templateColorCode == 'green'}">theme-active</c:if>" data-style="green" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-blue <c:if test="${siteTemplate.templateColorCode == 'blue'}">theme-active</c:if>" data-style="blue" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-orange <c:if test="${siteTemplate.templateColorCode == 'orange'}">theme-active</c:if>" data-style="orange" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-red <c:if test="${siteTemplate.templateColorCode == 'red'}">theme-active</c:if>" data-style="red" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-light <c:if test="${siteTemplate.templateColorCode == 'light'}">theme-active</c:if>" data-style="light" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-purple <c:if test="${siteTemplate.templateColorCode == 'purple'}">theme-active</c:if>" data-style="purple" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-aqua <c:if test="${siteTemplate.templateColorCode == 'aqua'}">theme-active</c:if>" data-style="aqua" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-brown <c:if test="${siteTemplate.templateColorCode == 'brown'}">theme-active</c:if>" data-style="brown" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-dark-blue <c:if test="${siteTemplate.templateColorCode == 'dark-blue'}">theme-active</c:if>" data-style="dark-blue" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-light-green <c:if test="${siteTemplate.templateColorCode == 'light-green'}">theme-active</c:if>" data-style="light-green" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-dark-red <c:if test="${siteTemplate.templateColorCode == 'dark-red'}">theme-active</c:if>" data-style="dark-red" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                        <li class="theme-teal <c:if test="${siteTemplate.templateColorCode == 'teal'}">theme-active</c:if>" data-style="teal" data-header="light" lang="<fmt:message key="site.form.do.you.want.change.color"/>"></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="fullWide"><fmt:message key="site.form.site.layout"/></label>
            <div class="controls">
                <select id="fullWide" name="fullWide" lang="<fmt:message key="site.form.do.you.want.change.layout"/>">
                    <option value="Y" <c:if test="${siteTemplate.fullWide == 'Y'}">selected</c:if>>Full Wide</option>
                    <option value="N" <c:if test="${siteTemplate.fullWide != 'Y'}">selected</c:if>>Boxed</option>
                </select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="headerFix"><fmt:message key="site.form.site.header.fixed"/></label>
            <div class="controls">
                <select id="headerFix" name="headerFix" lang="<fmt:message key="site.form.do.you.want.change.header.fix"/>">
                    <option value="Y" <c:if test="${siteTemplate.headerFix == 'Y'}">selected</c:if>>Fix Header</option>
                    <option value="N" <c:if test="${siteTemplate.headerFix != 'Y'}">selected</c:if>>No Fix Header</option>
                </select>
            </div>
        </div>

    </fieldset>
    <script type="text/javascript">
        $(document).ready(function() {
            $('#templateModel .btn-info').click(function(e) {
                e.preventDefault();
                $('#myModal').modal('hide');
//                $('template_update').submit();
            });
        });

        $("ul#color-group li").live('click',function() {
            var object = $(this);
            var originalColor = '${siteTemplate.templateColorCode}';
            $("input#templateColorCode").val($(this).attr("data-style"));
            $.fn.dialog2.helpers.confirm(object.attr("lang"), {
                confirm:function () {
//                    $('ul#color-group li').removeClass('theme-active');
//                    $(this).addClass("theme-active");
                    $('form#template_update').submit();
                },
                decline:function () {
                    $("input#templateColorCode").val(originalColor);
                }
            });
            event.preventDefault();
        });
        $("#fullWide").on('change', function() {
            var object = $(this);
            $.fn.dialog2.helpers.confirm(object.attr("lang"), {
                confirm:function () {
                    $('form#template_update').submit();
                },
                decline:function () {
                }
            });
            event.preventDefault();
        });
        $("#headerFix").on('change', function() {
            var object = $(this);
            $.fn.dialog2.helpers.confirm(object.attr("lang"), {
                confirm:function () {
                    $('form#template_update').submit();
                },
                decline:function () {
                }
            });
            event.preventDefault();
        });

    </script>

</form>

</body>
</html>
