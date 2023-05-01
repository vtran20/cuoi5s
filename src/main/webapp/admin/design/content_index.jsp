<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="rowId" value="${param.id}"/>
<c:if test="${! empty rowId}">
    <spring:eval expression="serviceLocator.rowDao.findById(T(java.lang.Long).valueOf(rowId))" var="row"/>
    <spring:eval expression="serviceLocator.siteMenuPartContentDao.getWidgetTemplate(T(java.lang.Long).valueOf(rowId))" var="currWidgetTemplate"/>
    <spring:eval expression="serviceLocator.siteMenuPartContentDao.getContentParts(T(java.lang.Long).valueOf(rowId), 'N')" var="partContents"/>
</c:if>

<script type="text/javascript">
    $(function () {
        <%--Sort table--%>
        // Sortable rows
        $('#simple-table-content').sortable({
            containerSelector: 'table',
            itemPath: '> tbody',
            itemSelector: 'tr',
            delay: 200,
            placeholder: '<tr class="placeholder"/>',
            onDrop: function  ($item, container, _super) {
                var result = "";
                var container1 = $("table#simple-table-content tbody tr");
                for (i = 0; i < container1.size(); i++) {
                    if (result=='') {
                        result = $(container1[i]).attr("id")
                    } else {
                        result += ","+$(container1[i]).attr("id")
                    }

                }
                _super($item, container);
                <%--Submit to server and reload page--%>
                $.ajax({
                    type: "GET",
                    url: '/admin/design/reorderPartContent.html',
                    data: {currentItem: $item.attr("id"), orderList:result},
                    success: function(data)
                    {
                        $('#message_alert').html(data);
                        $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#message_alert").alert('close');
                        });
                    }
                });
            }
        });
    });

    <%--This is special case which can call from common_single.js--%>
    $(".show-confirm").on("click", function() {
        var object = $(this);
        BootstrapDialog.show({
            type:BootstrapDialog.TYPE_DANGER,
            closeByBackdrop: false,
            closeByKeyboard: false,
            title:object.attr("data-header"),
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
                                //Reload the current tab
                                var $link = $('li.active a[data-toggle="tab"]');
                                $link.parent().removeClass('active');
                                var tabLink = $link.attr('href');
                                if ($link.attr('data-tab-url')) {
                                    $('#form-tab a[href="' + tabLink + '"]').tab('show');
                                    $('#message_alert').html(data);
                                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                        $("#message_alert").alert('close');
                                    });

                                } else {
                                    BootstrapDialog.show({
                                        title:object.attr("data-header"),
                                        message: data,
                                        buttons: [{
                                            label: 'OK',
                                            action: function(dialog) {
                                                window.location.reload(true);
                                                dialog.close();
                                            }
                                        }]
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

    //Show confirm and process check/uncheck checkbox
    $(".toggle-checkbox").on("click", function() {
        var object = $(this);
        var flag = object.prop( "checked" );
        BootstrapDialog.show({
            type:BootstrapDialog.TYPE_DANGER,
            closeByBackdrop: false,
            closeByKeyboard: false,
            title:object.attr("data-header"),
            message:object.attr("lang"),
            buttons:[
                {   label:'Yes',
                    action:function (dialog) {
                        $.get(object.attr("data-src"), { flag: flag}, // some params to pass it
                                function(data) {
                                    if ("ok" == data) {
                                        BootstrapDialog.show({
                                            type:BootstrapDialog.TYPE_INFO,
                                            message:object.attr("confirm-success")
                                        })
                                    } else {
                                        BootstrapDialog.show({
                                            type:BootstrapDialog.TYPE_DANGER,
                                            message:object.attr("confirm-fail")
                                        })
                                        object.prop("checked", !flag)
                                    }
                                });
                        dialog.close();
                    }
                },
                {
                    label:'No',
                    action:function (dialog) {
                        object.prop("checked", !flag);
                        dialog.close();
                    }
                }
            ]
        });

    });

</script>
<div class="page-header">
    <a class="btn btn-xs btn-info" href="#modal-form" role="button" data-toggle="modal" data-title="<fmt:message key="common.add.new"/> ${row.title}" data-page="/admin/design/content_form.html?rowId=${rowId}" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="common.add.new"/> ${row.title}
    </a>
</div><!-- /.page-header -->
<!-- Form Control States -->
<c:if test="${! empty partContents}">
<div class="row">
    <div class="col-xs-12">
        <table id="simple-table-content" class="table table-striped table-bordered table-hover sorted_table">
            <thead>
            <tr>
                <th><fmt:message key="admin.design.web.content.list"/></th>
                <th><fmt:message key="common.image"/></th>
                <th><fmt:message key="common.status"/></th>
                <th></th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${partContents}" varStatus="partContent">
                <tr id="${partContent.current.id}">
                    <td>
                        <c:if test="${! empty partContent.current.header}">
                            <h5><c:out value="${partContent.current.header}"/></h5>
                        </c:if>
                        <c:if test="${! empty partContent.current.title}">
                            <h6><c:out value="${partContent.current.title}"/></h6>
                        </c:if>
                        <c:if test="${! empty partContent.current.content}">
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).getTextFromHtml(partContent.current.content)" var="textContent"/>
                            <c:set value="${fn:length(textContent)}" var="contentLength"/>
                            <c:if test="${contentLength > 100}">
                                <c:set value="${fn:substring(textContent, 0, 100)} ..." var="textContent"/>
                            </c:if>
                            ${textContent}
                        </c:if>
                    </td>
                    <c:choose>
                        <c:when test="${!empty partContent.current.imgUrl}">
                            <td><img src="${partContent.current.imgUrl}?op=scale_200x100"/></td>
                        </c:when>
                        <c:otherwise>
                            <td></td>
                        </c:otherwise>
                    </c:choose>
                    <td>
                        <%--<input id="active_${partContent.current.id}" class="" type="checkbox" name="active" value="${partContent.current.id}" ${partContent.current.active == 'Y'?'checked':''} onClick="activeContent(this, ${partContent.current.id})">--%>
                        <input class="toggle-checkbox" type="checkbox" name="active" value="${partContent.current.id}" ${partContent.current.active == 'Y'?'checked':''} lang='<fmt:message key="common.do.you.want.to.change.this.content"/>' data-src="/admin/design/${partContent.current.id}/activate_partcontent.html?csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"/></td>

                    </td>
                    <td>
                        <div class="hidden-sm hidden-xs btn-group">
                            <a class="btn btn-xs btn-info" title="<fmt:message key="common.edit"/>" role="button"
                               data-toggle="modal" data-title="<fmt:message key="admin.design.web.content"/>"
                               data-page="/admin/design/content_form.html?id=${partContent.current.id}&rowId=${rowId}"
                               data-target="#modal-form">
                                <i class="ace-icon fa fa-pencil bigger-120"></i>
                            </a>

                            <a class="btn btn-xs btn-danger show-confirm" title="<fmt:message key="common.delete"/>"
                               lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>'
                               hreflang="/admin/design/delete_content.html?id=${partContent.current.id}&rowId=${rowId}&csrf=<sec:authentication property="details.csrf"/>">
                                <i class="ace-icon fa fa-trash-o bigger-120"></i>
                            </a>
                        </div>
                        <div class="hidden-md hidden-lg">
                            <div class="inline pos-rel">
                                <button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown"
                                        data-position="auto">
                                    <i class="ace-icon fa fa-cog icon-only bigger-110"></i>
                                </button>
                            </div>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div><!-- /.span -->
</div><!-- /.row -->
</c:if>