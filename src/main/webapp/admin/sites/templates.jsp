<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="site.template.title"/></title>
</head>

<body>
<c:if test="${! empty site}">
    <spring:eval expression="serviceLocator.templateDao.findBy('active', 'Y')" var="templates"/>
</c:if>
<script>
    $(document).ready(function () {
        $(".show-confirm").click(function () {
            var object = $(this);
            $.fn.dialog2.helpers.confirm(object.attr("lang"), {
                confirm:function () {
                    window.location.href = object.attr("href")
                },
                decline:function () {
                }
            });

            event.preventDefault();
        });
    });

</script>

<ul class="thumbnails">
    <c:forEach items="${templates}" var="template">
        <li class="span3">
            <div class="thumbnail">
                <img src="http://placehold.it/300x200" alt="">

                <div class="caption">
                    <h3>${template.templateModel}</h3>

                    <p>${template.name}</p>

                    <spring:eval expression="serviceLocator.siteDao.findById(T(java.lang.Long).valueOf(template.siteSample.id))" var="siteSample"/>
                    <p><a href="/admin/sites/select-template.html?templateId=${template.id}"
                          class="btn btn-primary show-confirm" lang="<fmt:message key='site.do.you.want.select.this.template'/>"><fmt:message key="menu.template.select"/></a> <a
                            href="http://${siteSample.subDomain}" class="btn" target="_blank"><fmt:message key="site.view.demo"/></a></p>
                </div>
            </div>
        </li>
    </c:forEach>
</ul>

</body>
</html>
