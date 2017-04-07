<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="pageId" value="${param.id}"/>
<c:if test="${! empty pageId}">
    <spring:eval expression="serviceLocator.pageDao.findById(T(java.lang.Long).valueOf(pageId))" var="page"/>
</c:if>

<c:set value="checked" var="activeChecked"/>
<c:if test="${! empty page.id}">
    <c:set value="Update page <b>${page.title}</b>" var="formState"/>
    <c:set value="/admin/page/update.html" var="formUrl"/>
    <c:set value="${page.id}" var="pageId"/>
    <c:set value="${page.uri}" var="uri"/>
    <c:if test="${fn:startsWith(page.active, 'N')}">
        <c:set value="" var="activeChecked"/>
    </c:if>

</c:if>
<c:if test="${empty page.id}">
    <c:set value="Add new Page" var="formState"/>
    <c:set value="/admin/page/insert.html" var="formUrl"/>
</c:if>
<script>
    $(function () {

        $("#form").validate({
//            rules: {
//                name: "required"
//                uri: "required"
//            },

            messages:{
                title:"<fmt:message key="page.title.required"/>"
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

        $('#form input[name="title"]').blur(function () {
            var pageName = document.form.title.value.trim();
            var oldId = '${page.id}';
            var domain = '${site.domain}';
            if (domain == '') {
                domain = '${site.subDomain}';
            }
            //if is home page then don't need build uri
            if (pageName != '') {
                $.ajax({
                    url:'/admin/page/geturiforpage.html',
                    async:false,
                    type:"GET",
                    data:'pageName=' + pageName + '&pageId=' + oldId,
                    success:function (data) {
                        //redirect to login page if data return is login page.
                        if (data.substr(0, 1) == '<') {
                            window.location.replace("/admin/login.html");
                        } else {
                            var url = 'http://' + domain + '/content/' + data;
                            $("#uri-for-page").text(url);
                            document.form.uri.value = data;
                        }
                    }
                });
            }
        });

    });
</script>


<div class="span12 column ui-sortable" id="col0">

    <!-- Form Control States -->
    <div class="box" id="box-1">
        <h4 class="box-header round-top"><fmt:message key="page.form.page"/>
            <a class="box-btn" title="close"><i class="icon-remove"></i></a>
            <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
            <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                    class="icon-cog"></i></a>
        </h4>

        <div class="box-container-toggle">
            <div class="box-content">
                <form name="form" id="form" class="form-horizontal" action="${formUrl}" method="post">
                    <input name="id" type="hidden" value="${pageId}"/>
                    <input name="uri" type="hidden" value="${uri}"/>
                    <h:csrf/>

                    <fieldset>
                        <c:if test="${! empty page.id}">
                        <legend><fmt:message key="page.update.page"/> <b>${page.title}</b></legend>
                        </c:if>
                        <c:if test="${empty page.id}">
                        <legend><fmt:message key="page.create.new.page"/></legend>
                        </c:if>

                        <div class="control-group">
                            <label class="control-label"><fmt:message key="menu.url"/></label>

                            <div class="controls">
                                <c:if test="${!empty page.uri}">
                                    <c:set value="${page.uri}" var="fullUri"/>
                                </c:if>
                                <c:set var="domain" value="${site.domain}"/>
                                <c:if test="${empty domain}">
                                    <c:set var="domain" value="${site.subDomain}"/>
                                </c:if>
                                <span class="help-inline" id="uri-for-page">http://${domain}/content/${fullUri}</span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="focusedInput"><fmt:message key="page.title"/></label>

                            <div class="controls">
                                <input name="title" class="required input-xlarge" id="focusedInput" type="text" value="${page.title}" maxlength="255" autofocus/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="optionsCheckbox"><fmt:message key="page.active"/></label>

                            <div class="controls">
                                <label class="checkbox">
                                    <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/>
                                    <fmt:message key="page.active.explain"/>
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

</div>