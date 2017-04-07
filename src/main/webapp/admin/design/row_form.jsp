<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="menuId" value="${param.menuId}"/>
<c:set var="rowId" value="${param.id}"/>
<c:set var="widgetType" value="${param.widgetType}"/>
<c:if test="${! empty rowId}">
    <spring:eval expression="serviceLocator.rowDao.findById(T(java.lang.Long).valueOf(rowId))" var="row"/>
</c:if>


<c:if test="${! empty row.id}">
    <c:set value="Update Row <b>${row.title}</b>" var="formState"/>
    <c:set value="${row.id}" var="rowId"/>
    <c:choose>
        <c:when test="${fn:startsWith(row.active, 'N')}">
            <c:set value="" var="activeChecked"/>
        </c:when>
        <c:otherwise>
            <c:set value="checked" var="activeChecked"/>
        </c:otherwise>
    </c:choose>
    <spring:eval expression="serviceLocator.siteMenuPartContentDao.getWidgetTemplate(T(java.lang.Long).valueOf(rowId))" var="currWidgetTemplate"/>
</c:if>
<c:if test="${empty row.id}">
    <c:set value="Add Row" var="formState"/>
    <c:set value="checked" var="activeChecked"/>
</c:if>
<script type="text/javascript">
    $(function () {
        $("#form").validate({
            messages:{
                title:"<fmt:message key="common.required"/>",
                widgetTemplateId:"<fmt:message key="common.required"/>"
            },
            ignore:"", //To allow validation of hidden elements, override the ignore and set it to empty string:
            rules: {
                widgetTemplateId: {
                    number: true,
                    required: true
                }
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

        $("#form").submit(function() {
            var url = "/admin/design/update_row.html"; // the script where you handle the form input.
            var form = $( "#form" );
            if (form.valid()) {
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#modal_message_alert').html(data);
                        $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#modal_message_alert").alert('close');
                        });
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            }
        });

    });

</script>

<!-- Form Control States -->
<div class="row">
<div class="col-xs-12">
    <form name="form" id="form" class="form-horizontal" action="#" method="post">
        <style>
        .big-icon {
            font-size: 28px;
            /*border: 1px solid #ddd;*/
            float: left;
            /*height: 90px;*/
            /*margin: 0 -1px -1px 0;*/
            padding: 10px 15px;
            text-align: center;
            /*width: 25%;*/
            word-wrap: break-word;
        }

            /*- FILTER OPTIONS -*/
        ul#filterOptions {
            /*width: 802px;*/
            height: 52px;
            margin: 10px 0;
            overflow: hidden;
            list-style: none;
        }
        ul#filterOptions li { height: 52px; margin-right: 2px; float: left; }
        ul#filterOptions li a {
            height: 50px;
            /*padding: 0 20px;*/
            border: 1px solid #999;
            background: #cfcfcf;
            color: #fff;
            font-weight: bold;
            line-height: 50px;
            text-decoration: none;
            display: block;
        }
        ul#filterOptions li a span {
            height: 48px;
            padding: 0 8px;
            /*border: 1px solid #999;*/
            background: #cfcfcf;
            color: #fff;
            /*font-weight: bold;*/
            font-size: 22px;
            line-height: 50px;
            text-decoration: none;
            display: block;
        }
        ul#filterOptions li a:hover { background: #c9c9c9; }
        ul#filterOptions li.active a, ul#filterOptions li.active a span { background: #999; }
            /*- -*/
            /*- OUR DATA HOLDER -*/
        #ourHolder { overflow: hidden; }
        #ourHolder div.item {
            width: 84px;
            height: 84px;
            float: left;
            border: 2px solid #fff;
            margin: 2px 2px 2px 2px;
            text-align: center;
        }
        #ourHolder div.item img {
            border: 1px solid #999;
        }
        #ourHolder div.item:hover {
            border: 2px solid red;
        }
        #ourHolder div.item.template-active {
            border: solid 2px red;
        }
        #ourHolder div.item h3 { margin-top: 10px; font-size: 16px; line-height: 20px; }

        #background-image div.background {
            width: 84px;
            height: 44px;
            float: left;
            border: 2px solid #fff;
            margin: 2px 2px 2px 2px;
            text-align: center;
        }
        #background-image div.background img {
            border: 1px solid #999;
        }
        #background-image div.background:hover {
            border: 2px solid red;
        }
        #background-image div.background.background-active {
            border: solid 2px red;
        }
        #background-image div.background h3 { margin-top: 10px; font-size: 16px; line-height: 20px; }

    </style>
    <div class="row">
        <div class="col-xs-12">
            <button type="submit" class="btn btn-primary"><fmt:message key="common.save.changes"/></button>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div  id="modal_message_alert"></div>

            <ul id="filterOptions">
                <li class="active"><a href="#" class="all"><span>ALL</span></a></li>
                <li><a href="#" class="text"><i class="big-icon fa fa-file-text-o"></i> </a></li>
                <li><a href="#" class="slide"><i class="big-icon fa fa-arrows-h"></i> </a></li>
                <li><a href="#" class="image"><i class="big-icon fa fa-picture-o"></i> </a></li>
                <li><a href="#" class="video"><i class="big-icon fa fa-file-video-o"></i> </a></li>
            </ul>
            <%--Find all widgets are used for body--%>
            <spring:eval expression="serviceLocator.widgetTemplateDao.getWidgetTemplateByType(widgetType, 'Y')" var="widgetTemplates"/>
            <fieldset>
                <div class="control-group">
                    <label class="control-label" for="widgetTemplateId"><fmt:message key="content.select.content.template"/></label>
                    <div class="controls">
                        <div id="ourHolder">
                            <c:if test="${! empty widgetTemplates}">
                                <c:forEach items="${widgetTemplates}" var="widgetTemplate">
                                    <c:choose>
                                        <c:when test="${widgetTemplate.name == 'BLANK'}">
                                            <%--If system admin, we show BLANK widget--%>
                                            <sec:authorize ifAnyGranted="ROLE_SYSTEM_ADMIN">
                                                <div class="item <c:if test="${widgetTemplate.id == currWidgetTemplate.id}">template-active</c:if> ${widgetTemplate.widgetType}" lang="${widgetTemplate.id}">
                                                    <img src="${widgetTemplate.imageUrl}" title="${widgetTemplate.name}" class="select-widget-template" width="80" height="80"/>
                                                </div>
                                            </sec:authorize>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="item <c:if test="${widgetTemplate.id == currWidgetTemplate.id}">template-active</c:if> ${widgetTemplate.widgetType}" lang="${widgetTemplate.id}" supportField="${widgetTemplate.supportField}">
                                                <img src="${widgetTemplate.imageUrl}" title="${widgetTemplate.name}" class="select-widget-template" width="80" height="80"/>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:if>
                            <input name="widgetTemplateId" id="widgetTemplateId" class="required" type="hidden" value="${currWidgetTemplate.id}"/>
                        </div>
                    </div>
                </div>
            </fieldset>

            <fieldset id="background-area" style="<c:if test="${!fn:contains(currWidgetTemplate.supportField, 'background')}">display: none</c:if>">
                <spring:eval expression="serviceLocator.getStringParamValueDao().getStringParamValues('SLIDE_BACKGROUND', serviceLocator.locale)" var="backgrounds"/>
                <div class="control-group">
                    <label class="control-label" for="background"><fmt:message key="content.select.content.bacground"/></label>
                    <div class="controls">
                        <div id="background-image">
                            <c:if test="${! empty backgrounds}">
                                <c:forEach items="${backgrounds}" var="background">
                                    <div class="background <c:if test="${background.value == row.background}">background-active</c:if>" lang="${background.value}">
                                        <img src="${background.key}" class="select-background" width="80" height="40"/>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <input name="background" id="background" class="" type="hidden" value="${row.background}"/>
                        </div>
                    </div>
                </div>
            </fieldset>

        </div>
    </div>
        <script type="text/javascript">
            $(document).ready(function() {

                //Select Widget Template
                $("div.item").on('click',function() {
                    var object = $(this);
                    $("input#widgetTemplateId").val($(this).attr("lang"));
                    $("div.item").removeClass("template-active");
                    object.addClass("template-active");

                    //Check to whether show background image or not.
                    if ($(this).attr("supportField").contains("background")) {
                        $("#background-area").show();
                    } else {
                        $("#background-area").hide();
                    }
                });
                //Select Background
                $("div.background").on('click',function() {
                    var object = $(this);
                    $("input#background").val($(this).attr("lang"));
                    $("div.background").removeClass("background-active");
                    object.addClass("background-active");
                });

                //Filter widget template
                $('#filterOptions li a').click(function() {
                    // fetch the class of the clicked item
                    var ourClass = $(this).attr('class');

                    // reset the active class on all the buttons
                    $('#filterOptions li').removeClass('active');
                    // update the active state on our clicked button
                    $(this).parent().addClass('active');

                    if(ourClass.contains('all')) {
                        // show all our items
                        $('#ourHolder').children('div.item').show();
                    }
                    else {
                        // hide all elements that don't share ourClass
                        $('#ourHolder').children('div:not(.' + ourClass + ')').hide();
                        // show all elements that do share ourClass
                        $('#ourHolder').children('div.' + ourClass).show();
                    }
                    return false;
                });
            });
        </script>

        <input name="menuId" type="hidden" value="${menuId}"/>
        <input name="id" type="hidden" value="${rowId}"/>
        <h:csrf/>
        <div class="row">
            <div class="col-xs-6">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="title"><fmt:message key="site.design.row.title"/></label>

                        <div class="controls">
                            <input name="title" class="input-xlarge required" id="title" type="text" maxlength="250" value="${row.title}" autofocus/>
                        </div>
                    </div>

                    <div class="control-group">
                        <div class="controls">
                                <input type="checkbox" id="showTitle" name="showTitle" ${row.showTitle == 'Y'?'checked':''}/>
                                <fmt:message key="admin.design.web.row.show.header"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="controls">
                                <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/>
                                <fmt:message key="content.active.explain"/>
                        </div>
                    </div>
                </fieldset>
            </div>
            <div class="col-xs-6">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <button type="submit" class="btn btn-primary"><fmt:message key="common.save.changes"/></button>
            </div>
        </div>

    </form>
</div>
</div>
