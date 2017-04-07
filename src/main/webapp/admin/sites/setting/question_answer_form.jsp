<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="siteQuestionAnswerId" value="${param.id}"/>
<c:if test="${! empty siteQuestionAnswerId}">
    <spring:eval expression="serviceLocator.siteQuestionAnswerDao.findById(T(java.lang.Long).valueOf(siteQuestionAnswerId))" var="siteQuestionAnswer"/>
</c:if>

<c:set value="checked" var="activeChecked"/>
<c:if test="${! empty siteQuestionAnswer.id}">
    <c:set value="Update Question & Answer" var="formState"/>
    <c:set value="${siteQuestionAnswer.id}" var="siteQuestionAnswerId"/>
    <c:if test="${fn:startsWith(siteQuestionAnswer.active, 'N')}">
        <c:set value="" var="activeChecked"/>
    </c:if>
</c:if>
<c:if test="${empty siteQuestionAnswer.id}">
    <c:set value="Add new Question & Answer" var="formState"/>
</c:if>
<script>
    $(function () {

        $("#form").validate({
            rules:{
                question:{
                    required:true,
                    maxlength:500
                },
                answer:{
                    required:true,
                    maxlength:5000
                }
            },

            messages:{
                question:{
                    required:"Vui lòng nhập câu hỏi",
                    maxlength:"Vui lòng nhập tối đa 500 ký tự"
                },
                answer:{
                    required:"Vui lòng nhập câu trả lời",
                    maxlength:"Vui lòng nhập tối đa 5000 ký tự"
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

    });
</script>

<!-- Form Control States -->
<div class="box" id="box-1">
    <h4 class="box-header round-top">
    <c:if test="${! empty siteQuestionAnswer.id}">
        <fmt:message key="setting.question.update.question.answer"/>
    </c:if>
    <c:if test="${ empty siteQuestionAnswer.id}">
        <fmt:message key="setting.question.add.question.answer"/>
    </c:if>
    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
        <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
        <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                class="icon-cog"></i></a>
    </h4>

    <div class="box-container-toggle">
        <div class="box-content">
            <form name="form" id="form" class="form-horizontal" action="/admin/sites/setting/savesitequestionanswer.html" method="post">
                <input type="hidden" name="id" value="${siteQuestionAnswerId}"/>
                <h:csrf/>

                <fieldset>
                    <legend><fmt:message key="setting.question.question.answer"/></legend>

                    <div class="control-group">
                        <label class="control-label" for="question"><fmt:message key="setting.question.question"/>*</label>

                        <div class="controls">
                            <textarea class="input-xlarge required" id="question" name="question" rows="3" autofocus>${siteQuestionAnswer.question}</textarea>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="answer"><fmt:message key="setting.question.answer"/>*</label>

                        <div class="controls">
                            <textarea class="input-xxlarge required" id="answer" name="answer" rows="8">${siteQuestionAnswer.answer}</textarea>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="optionsCheckbox"><fmt:message key="setting.question.active"/></label>

                        <div class="controls">
                            <label class="checkbox">
                                <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/>
                                <fmt:message key="setting.question.active.explain"/>
                            </label>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary"><fmt:message key="common.save.changes"/></button>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</div>
<!--/span-->
