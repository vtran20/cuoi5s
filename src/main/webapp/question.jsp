<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<html>
<head>
    <title><fmt:message key="question.answer.title"/></title>
    <meta name="description" content="Hỏi và Đáp"/>
    <meta name="keywords" content="Hỏi và Đáp"/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>

<!--=== Breadcrumbs ===-->
<div class="breadcrumbs">
    <div class="container">
        <h1 class="pull-left"><fmt:message key="question.answer.header"/></h1>
    </div>
</div><!--/breadcrumbs-->
<!--=== End Breadcrumbs ===-->
<spring:eval expression="serviceLocator.siteQuestionAnswerDao.findByOrder('active', 'Y', 'sequence', site.id)" var="siteQuestions"/>
<div class="container content">
<div class="row">
<div class="col-md-9">
<!-- General Questions -->
<div class="headline"><h2><fmt:message key="question.answer.title"/></h2></div>
<c:if test="${! empty siteQuestions}">
<div class="panel-group acc-v1 margin-bottom-40" id="accordion">
<c:forEach items="${siteQuestions}" varStatus="siteQuestion">
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapse${siteQuestion.index + 1}">
                    ${siteQuestion.index + 1}. ${siteQuestion.current.question}
            </a>
        </h4>
    </div>
    <div id="collapse${siteQuestion.index + 1}" class="panel-collapse collapse" style="height: 0px;">
        <div class="panel-body">
                ${siteQuestion.current.answer}
        </div>
    </div>
</div>
</c:forEach>
</div>
</c:if>
<!-- End General Questions -->

</div><!--/col-md-9-->
</div>
</div>

</body>
</html>