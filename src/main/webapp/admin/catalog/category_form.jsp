<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="id" value="${param.id}"/>
<c:if test="${! empty id}">
    <spring:eval expression="serviceLocator.categoryDao.findById(T(java.lang.Long).valueOf(id))" var="category"/>
</c:if>
<%--Default is checked--%>
<c:set value="checked" var="activeChecked"/>
<c:if test="${! empty category.id}">
    <c:if test="${fn:startsWith(category.active, 'N')}">
        <c:set value="" var="activeChecked"/>
    </c:if>
</c:if>
<script>
    $(function () {

        $("#form").validate({
//            rules: {
//                name: "required"
//                uri: "required"
//            },

            messages:{
                name:"<fmt:message key="category.name.required"/>"
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
            var form = $( "#form" );
            if (form.valid()) {
                var url = "/admin/catalog/category_update.html"; // the script where you handle the form input.
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
    $('[data-rel=popover]').popover({container:'body'});
</script>

<div class="page-header">
    <c:if test="${! empty category.id}">
        <h4><fmt:message key="category.update"/></h4>
    </c:if>
    <c:if test="${empty category.id}">
        <h4><fmt:message key="category.add"/></h4>
    </c:if>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div  id="modal_message_alert"></div>
        <form name="form" id="form" action="#" method="post">
            <input name="id" type="hidden" value="${id}"/>
            <input name="uri" id="uri" type="hidden" value="${uri}"/>
            <h:csrf/>
            <div class="form-group">
                <label class="control-label" for="name"><fmt:message key="category.name"/></label>

                <div class="controls">
                    <input name="name" class="required input-xlarge" id="name" type="text" value="${category.name}" maxlength="49"/>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label" for="selectError"><fmt:message key="category.parent"/></label>
                <div class="controls">
                    <c:if test="${! empty site}">
                        <spring:eval expression="serviceLocator.categoryDao.getRootCategories(site, false)" var="rootCategories"/>
                        <c:if test="${! empty category}">
                            <spring:eval expression="serviceLocator.categoryDao.getParentCategory(category.id)" var="parentCategory"/>
                        </c:if>
                    </c:if>
                    <c:if test="${! empty rootCategories}">
                        <select id="selectError" name="parentId">
                            <option value="0"></option>
                            <c:forEach items="${rootCategories}" var="m">
                                <c:if test="${empty category || m.id != category.id}">
                                    <c:choose>
                                        <c:when test="${! empty parentCategory && parentCategory.id == m.id}">
                                            <option value="${m.id}" selected>${m.name}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${m.id}">${m.name}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <%--Category level--%>
                                <spring:eval expression="serviceLocator.categoryDao.findByOrder('parentCategory.id', m.id, 'sequence', site.id)" var="categories"/>
                                <c:forEach items="${categories}" var="subCategory">
                                    <c:if test="${empty category || subCategory.id != category.id}">
                                        <c:choose>
                                            <c:when test="${! empty parentCategory && parentCategory.id == subCategory.id}">
                                                <option value="${subCategory.id}" selected>----${subCategory.name}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${subCategory.id}">----${subCategory.name}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </select>
                    </c:if>
                    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="category.support.level"/>" title="">?</span>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label" for="optionsCheckbox"></label>
                <div class="controls">
                    <%--<label class="checkbox">--%>
                    <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/><fmt:message key="common.active.status"/>
                    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="common.active.status"/>" title="">?</span>
                    <%--</label>--%>
                </div>
            </div>

            <div class="modal-footer">
                <button class="btn btn-sm btn-primary" type="submit">
                    <i class="ace-icon fa fa-check"></i>
                    <fmt:message key="common.save.changes"/>
                </button>
            </div>
        </form>
    </div>
</div>
