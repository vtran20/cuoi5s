<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="setting.question.question.and.answer"/></title>
    <meta name="decorator" content="admin_new"/>
</head>

<body>

<script>
    function updateReOrder (event, ui) {
        var item = $(ui.item).attr('id');
        var list = $('#datatable tbody').sortable('toArray').toString();
        window.location.href = '/admin/sites/setting/reorderquestion.html?currentItem='+item+'&orderList='+list;
    }

    $(document).ready(function () {
        $(".edit-question-answer,.new-question-answer").click(function () {
            $("#sitesetting-question-answer").load($(this).attr("hreflang"));
        });

    });
</script>

<c:if test="${not empty site}">

    <spring:eval expression="serviceLocator.siteQuestionAnswerDao.findByOrder('active', 'Y', 'sequence', site.id)" var="siteQuestionAnswers"/>
</c:if>

<!-- Bread Crumb Navigation -->
<div class="span10">
    <div>
        <ul class="breadcrumb">
            <li>
                <a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span>
            </li>
            <li class="active"><fmt:message key="setting.question.question.answer"/></li>
        </ul>
    </div>

    <div class="row-fluid">
        <div class="span2 action-btn round-all">
            <a class="new-question-answer" hreflang="/admin/sites/setting/question_answer_form.html" href="#sitesetting-question-answer">
                <div><i class="icon-plus-sign"></i></div>
                <div><strong><fmt:message key="question.answer.add.new.question.answer"/></strong></div>
            </a>
        </div>
    </div>
    <h:frontendmessage _messages="${messages}"/>

    <%--Table--%>
    <div class="row-fluid">
        <div class="span12">
            <div class="box" id="box-0">
                <h4 class="box-header round-top"><fmt:message key="setting.question.answer.list"/>
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>

                <div class="box-container-toggle">
                    <div class="box-content">
                        <c:if test="${!empty siteQuestionAnswers}">

                            <table cellpadding="0" cellspacing="0" border="0"
                                   class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                <thead>
                                <tr>
                                    <th><fmt:message key="setting.question.question"/></th>
                                    <th><fmt:message key="setting.question.answer"/></th>
                                    <th><fmt:message key="setting.question.actions"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${siteQuestionAnswers}" varStatus="questionAnswer" var="currentQuestionAnswer">
                                    <tr id="${questionAnswer.current.id}-s${questionAnswer.current.sequence}">
                                        <td><c:out value="${currentQuestionAnswer.question}"/></td>
                                        <td><c:out value="${currentQuestionAnswer.answer}"/></td>
                                        <td class="center">
                                            <c:set value="${questionAnswer.current.active}" var="active"/>
                                            <c:if test="${fn:startsWith(active, 'Y')}">
                                                <span class="label label-success"><fmt:message key="setting.question.active.status"/></span>
                                            </c:if>
                                            <c:if test="${!fn:startsWith(active, 'Y')}">
                                                <span class="label label-inverse"><fmt:message key="setting.question.inactive.status"/></span>
                                            </c:if>

                                        </td>
                                        <td class="center">
                                            <a class="btn btn-info edit-question-answer"
                                               hreflang="/admin/sites/setting/question_answer_form.html?id=${questionAnswer.current.id}" href="#sitesetting-question-answer">
                                                <i class="icon-edit icon-white"></i>
                                                <fmt:message key="common.edit"/>
                                            </a>
                                            <a class="btn btn-danger show-confirm" readonly="readonly"
                                               lang='<fmt:message key="setting.question.do.you.want.delete.question"/>'
                                               href="/admin/sites/setting/deletesitequestionanswer.html?id=${questionAnswer.current.id}&csrf=<sec:authentication property="details.csrf"/>">
                                                <i class="icon-trash icon-white"></i>
                                                <fmt:message key="common.delete"/>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty siteQuestionAnswers}">
                            <fmt:message key="setting.question.answer.is.empty"/>
                        </c:if>

                    </div>
                </div>
            </div>
            <!--/span-->
        </div>
    </div>

    <%--Form--%>
    <div class="row-fluid" id="sitesetting-question-answer">
    </div>
    <h:reloadform id="sitesetting-question-answer" url="/admin/sites/setting/question_answer_form.html"/>


    <!--/span-->
</div>

</body>
</html>