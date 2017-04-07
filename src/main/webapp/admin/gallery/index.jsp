<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>
<c:if test="${not empty site}">
    <spring:eval expression="serviceLocator.albumDao.findAll(site.id)" var="albums"/>
</c:if>
<div class="page-header">
    <a class="btn btn-xs btn-info" title="<fmt:message key="gallery.add.new.album"/>" role="button" data-toggle="modal" data-page="/admin/gallery/form.html" data-target="#modal-form">
        <i class="fa fa-plus"></i> <fmt:message key="gallery.add.new.album"/>
    </a>
</div>
<div class="row">
    <div class="col-xs-12">
        <h:frontendmessage _messages="${messages}"/>
        <%--Table--%>
        <div class="row-fluid">
            <div class="span12">
                <!-- Portlet: Member List -->
                <div class="box" id="box-0">
                    <div class="box-container-toggle">
                        <div class="box-content">
                            <c:if test="${!empty albums}">

                                <table cellpadding="0" cellspacing="0" border="0"
                                       class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                    <thead>
                                    <tr>
                                        <th><fmt:message key="gallery.album.name"/></th>
                                        <th><fmt:message key="gallery.album.status"/></th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    <c:forEach items="${albums}" varStatus="album" var="currentAlbum">
                                        <tr id="${album.current.id}-s${album.current.sequence}">
                                            <td><a data-url="/admin/gallery/view_album" href="#/admin/gallery/view_album?id=${album.current.id}"><c:out value="${album.current.name}"/></a></td>

                                            <td class="center">
                                                <c:set value="${album.current.active}" var="active"/>
                                                <c:if test="${fn:startsWith(active, 'Y')}">
                                                    <span class="label label-success"><fmt:message key="gallery.album.active.status"/></span>
                                                </c:if>
                                                <c:if test="${!fn:startsWith(active, 'Y')}">
                                                    <span class="label label-inverse"><fmt:message key="gallery.album.inactive.status"/></span>
                                                </c:if>

                                            </td>
                                            <td class="center">
                                                <a class="btn btn-xs btn-info" data-toggle="modal" data-page="/admin/gallery/form.html?id=${album.current.id}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="gallery.edit"/>" data-target="#modal-form"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
                                                <a class="btn btn-xs btn-danger global_show-confirm" lang='<fmt:message key="gallery.do.you.want.delete.album"/>' hreflang="/admin/gallery/delete.html?id=${album.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="album.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>
                            <c:if test="${empty albums}">
                                <fmt:message key="gallery.album.is.empty"/>
                            </c:if>

                        </div>
                    </div>
                </div>
                <!--/span-->
            </div>
        </div>
    </div>
</div>
<h:form_modal/>
<script type="text/javascript">
    $('[data-rel=popover]').popover({container:'body'});
</script>
