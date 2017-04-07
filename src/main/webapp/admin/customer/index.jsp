<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>
<script>
$('[data-rel=popover]').popover({container:'body'});
</script>

<spring:eval expression="serviceLocator.contactUsDao.findAll(site.id)" var="contactMessages" />

<%--<div class="page-header">--%>
    <%--&lt;%&ndash;<h4>Cập Nhật Thông Tin Liên Hệ</h4>&ndash;%&gt;--%>
    <%--<a class="btn btn-xs btn-info" title="<fmt:message key="common.add.new"/>" href="#modal-form" role="button" data-toggle="modal" data-page="/admin/news/form.html" data-target="#modal-form">--%>
        <%--<i class="fa fa-plus"></i> <fmt:message key="news.add.news.category"/>--%>
    <%--</a>--%>
<%--</div><!-- /.page-header -->--%>

<!-- Bread Crumb Navigation -->
<div class="row">
<div class="col-xs-12">
    <h:frontendmessage _messages="${messages}"/>
    <%--Table--%>
    <div class="row">
        <div class="col-xs-12">
            <!-- Portlet: Member List -->
            <div class="box" id="box-0">
                <h4 class="box-header round-top"><fmt:message key="customer.message.list"/>
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>

                <div class="box-container-toggle">
                    <div class="box-content">
                        <c:if test="${!empty contactMessages}">

                            <table cellpadding="0" cellspacing="0" border="0"
                                   class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                <thead>
                                <tr>
                                    <th><fmt:message key="customer.message.name"/></th>
                                    <th><fmt:message key="common.actions"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${contactMessages}" var="message">

                                    <tr>
                                        <td style="width: 90%">
                                            <c:out value="${message.firstName}"/><br>
                                            <c:out value="${message.sendersEmail}"/><br>
                                            ${message.message}
                                        </td>
                                        <td class="center">
                                            <a class="btn btn-xs btn-danger global_show-confirm" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>' hreflang="/admin/customer/delete_message.html?id=${message.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.category.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>

                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty contactMessages}">
                            <fmt:message key="common.is.empty"/>
                        </c:if>

                    </div>
                </div>
            </div>
            <!--/span-->
        </div>
    </div>

    <!--/span-->
</div>
</div>
<%--<h:form_modal/>--%>
