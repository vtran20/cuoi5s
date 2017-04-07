<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="rowId" value="${param.rowId}"/>
<spring:eval expression="serviceLocator.newsDao.findByOrder(null, null, 'sequence', site.id)" var="newses"/>
<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div id="modal_message_alert"></div>
            <div class="col-xs-12">
                <c:if test="${!empty newses}">
                    <table id="simple-table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th><fmt:message key="news.news.list"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${newses}" var="currentNews">
                            <tr>
                                <td>
                                    <c:out value="${currentNews.title}"/><br>
                                </td>
                                <td class="center">
                                    <input id="news_${currentNews.id}" class="news-toggle-checkbox" type="checkbox" name="news" value="${currentNews.id}" data-src="/admin/design/select_news.html?rowId=${rowId}&newsId=${currentNews.id}&csrf=<sec:authentication property='details.csrf'/>" data-header="<fmt:message key='common.confirm.title'/>" lang='<fmt:message key="common.doyouwantaddthisnews"/>' confirm-success="<fmt:message key='common.data.saved.success'/>" confirm-fail="<fmt:message key='common.data.saved.fail'/>">
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty newses}">
                    <fmt:message key="common.is.empty"/>
                </c:if>
            </div><!-- /.span -->
        </div><!-- /.row -->
    </div>
</div>
<script type="text/javascript">
    //Show confirm and process check/uncheck checkbox
    $(".news-toggle-checkbox").on("click", function() {
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
                                    $('#modal_message_alert').html(data);
                                    $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                        $("#modal_message_alert").alert('close');
                                        //don't want reload when the modal closed
                                        //$('#modal_message_alert').html("");
                                        //release modal
                                        $('#modal-form').removeData('bs.modal');
                                    });
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