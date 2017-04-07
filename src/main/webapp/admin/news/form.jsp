<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="id" value="${param.id}"/>
<c:if test="${! empty id}">
    <spring:eval expression="serviceLocator.newsCategoryDao.findById(T(java.lang.Long).valueOf(id))" var="newsCategory"/>
</c:if>

<c:set value="checked" var="activeChecked"/>
<c:if test="${! empty newsCategory.id}">
    <c:set value="${newsCategory.id}" var="newsCategoryId"/>
    <c:set value="${newsCategory.uri}" var="uri"/>
    <c:if test="${fn:startsWith(newsCategory.active, 'N')}">
        <c:set value="" var="activeChecked"/>
    </c:if>
    <c:set value="autofocus" var="autofocus"/>

</c:if>
<c:if test="${empty newsCategory.id}">
    <c:set value="" var="autofocus"/>
</c:if>
<script type="text/javascript">
    $(function () {

        $("#form").validate({
//            rules: {
//                name: "required"
//                uri: "required"
//            },

            messages:{
                name:"<fmt:message key="newscategory.name.required"/>"
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

        <%--$('#form input[name="name"]').blur(function () {--%>
            <%--var newsCategoryName = document.form.name.value.trim();--%>
            <%--var oldId = '${newsCategory.id}';--%>
            <%--var domain = '${site.domain}';--%>
            <%--if (domain == '') {--%>
                <%--domain = '${site.subDomain}';--%>
            <%--}--%>
            <%--//if is home newsCategory then don't need build uri--%>
            <%--if (newsCategoryName != '') {--%>
                <%--$.ajax({--%>
                    <%--url:'/admin/news/geturifornewscategory.html',--%>
                    <%--async:false,--%>
                    <%--type:"GET",--%>
                    <%--data:'newsCategoryName=' + newsCategoryName + '&newsCategoryId=' + oldId,--%>
                    <%--success:function (data) {--%>
                        <%--//redirect to login page if data return is login page.--%>
                        <%--if (data.substr(0, 1) == '<') {--%>
                            <%--window.location.replace("/admin/login.html");--%>
                        <%--} else {--%>
                            <%--var url = 'http://' + domain + '/news/' + data;--%>
                            <%--$("#uri-for-page").text(url);--%>
                            <%--document.form.uri.value = data;--%>
                        <%--}--%>
                    <%--}--%>
                <%--});--%>
            <%--}--%>
        <%--});--%>

        $("#form").submit(function() {
            var form = $( "#form" );
            if (form.valid()) {
                var url = "/admin/news/update.html"; // the script where you handle the form input.
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


<div class="span12 column ui-sortable" id="col0">

    <!-- Form Control States -->
    <div class="box" id="box-1">
        <%--<h4 class="box-header round-top"><fmt:message key="news.form.news.category"/>--%>
            <%--<a class="box-btn" title="close"><i class="icon-remove"></i></a>--%>
            <%--<a class="box-btn" title="toggle"><i class="icon-minus"></i></a>--%>
            <%--<a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i--%>
                    <%--class="icon-cog"></i></a>--%>
        <%--</h4>--%>
            <div  id="modal_message_alert"></div>
            <div class="box-container-toggle">
            <div class="box-content">
                <form name="form" id="form" class="form-horizontal" action="#" method="post">
                    <input name="id" type="hidden" value="${param.id}"/>
                    <input name="uri" type="hidden" value="${uri}"/>
                    <h:csrf/>

                    <fieldset>
                        <c:if test="${! empty newsCategory.id}">
                        <legend><fmt:message key="news.update.news.category"/> <b>${newsCategory.name}</b></legend>
                        </c:if>
                        <c:if test="${empty newsCategory.id}">
                        <legend><fmt:message key="news.add.news.category"/></legend>
                        </c:if>
                        <div class="control-group">
                            <label class="control-label" for="focusedInput"><fmt:message key="news.category.name"/></label>
                            <div class="controls">
                                <input name="name" class="required input-xlarge" id="focusedInput" type="text"
                                       value="${newsCategory.name}" ${autofocus}/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="selectError"><fmt:message key="menu.parent"/></label>
                            <div class="controls">
                                <c:if test="${! empty site}">
                                    <spring:eval expression="serviceLocator.newsCategoryDao.getRootNewsCategories(site, 'N')" var="newsCategories"/>
                                    <c:if test="${! empty newsCategory}">
                                    <spring:eval expression="serviceLocator.newsCategoryDao.getParentNewsCategory(site, newsCategory.id, 'N')" var="parentNewsCategory"/>
                                    </c:if>
                                </c:if>
                                <%--<c:if test="${! empty newsCategories}">--%>
                                <select id="selectError" name="parentId">
                                    <option value="0"></option>
                                    <c:forEach items="${newsCategories}" var="m">
                                        <c:if test="${empty newsCategory || m.id != newsCategory.id}">
                                            <c:choose>
                                                <c:when test="${! empty parentNewsCategory && parentNewsCategory.id == m.id}">
                                                    <option value="${m.id}" selected>${m.name}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${m.id}">${m.name}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            <%--</c:if>--%>
                                <span class="help-inline"><fmt:message key="news.category.level"/></span>
                            </div>
                        </div>

                        <div class="control-group">
                            <%--<label class="control-label" for="optionsCheckbox"><fmt:message key="news.category.active"/></label>--%>
                            <br>
                            <div class="controls">
                                <%--<label class="checkbox">--%>
                                    <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/>
                                    <fmt:message key="news.category.active.explain"/>
                                <%--</label>--%>
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

</div>