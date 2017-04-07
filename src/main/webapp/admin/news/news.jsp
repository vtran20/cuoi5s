<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="serviceLocator.newsDao.findAllOrder('createdDate', site.id)" var="newsList"/>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>
<div class="page-header">
    <a class="btn btn-xs btn-info" data-title="<fmt:message key="news.add.news"/>" data-url="/admin/news/create" href="#/admin/news/create">
        <i class="fa fa-plus"></i> <fmt:message key="news.add.news"/>
    </a>
</div><!-- /.page-header -->
<h:frontendmessage _messages="${messages}"/>
<div  id="message_alert"></div>
<%--Table--%>
<div class="row">
    <div class="col-xs-12">
        <!-- Portlet: Member List -->
        <div class="box" id="box-0">
            <h4 class="box-header round-top"><fmt:message key="news.news.list"/>
                <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                        class="icon-cog"></i></a>
            </h4>

            <div class="box-container-toggle">
                <div class="box-content">
                    <c:if test="${!empty newsList}">

                        <table cellpadding="0" cellspacing="0" border="0"
                               class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                            <thead>
                            <tr>
                                <th><fmt:message key="news.news.title"/></th>
                                <th><fmt:message key="news.status"/></th>
                                <th><fmt:message key="news.actions"/></th>
                            </tr>
                            </thead>
                            <tbody>

                            <c:forEach items="${newsList}" varStatus="news" var="currentNews">

                                <tr id="${news.current.id}-s${news.current.sequence}">
                                    <td><c:out value="${news.current.title}"/></td>
                                    <td class="center">
                                        <c:set value="${news.current.active}" var="active"/>
                                        <c:if test="${fn:startsWith(active, 'Y')}">
                                            <span class="label label-success"><fmt:message key="news.active.status"/></span>
                                        </c:if>
                                        <c:if test="${!fn:startsWith(active, 'Y')}">
                                            <span class="label label-inverse"><fmt:message key="news.inactive"/></span>
                                        </c:if>

                                    </td>
                                    <c:set var="siteUrl" value="/news/${news.current.uri}"/>
                                    <td class="center">
                                        <a class="btn btn-xs btn-success" target="_blank" href="${siteUrl}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.view"/>"><i class="normal-icon ace-icon fa fa-eye bigger-120"></i></a>
                                        <a class="btn btn-xs btn-info" data-url="/admin/news/create" href="#/admin/news/create?id=${news.current.id}" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="common.edit"/>"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
                                        <a class="btn btn-xs btn-danger global_show-confirm" lang='<fmt:message key="common.doyouwanttodeletethiscontent"/>' hreflang="/admin/news/deletenews.html?id=${news.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="common.delete"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>
                                        <a class="btn btn-xs btn-grey global_show-confirm" lang="<fmt:message key="news.do.you.want.copy.this.news"/>" hreflang="/admin/news/copynews.html?id=${news.current.id}&csrf=<sec:authentication property="details.csrf"/>" data-rel="popover" data-trigger="hover" data-placement="top" data-content="<fmt:message key="news.copy.a.news"/>" data-header="<fmt:message key="common.confirm.title"/>"><i class="ace-icon fa fa-copy bigger-120"></i></a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${empty newsList}">
                        <fmt:message key="news.is.empty"/>
                    </c:if>

                </div>
            </div>
        </div>
        <!--/span-->
    </div>
</div>
<!--/span-->
<script>
    $('[data-rel=popover]').popover({container:'body'});

</script>
<%--</body>--%>
<%--</html>--%>