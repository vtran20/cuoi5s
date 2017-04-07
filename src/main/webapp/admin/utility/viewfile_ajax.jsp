<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<sec:authentication property="principal.site" var="site"/>

<spring:eval expression="serviceLocator.getConfigurationService().getWorkingDir()" var="basePath"/>
<c:set var="path" value="${basePath}/${command.path}"/>
<spring:eval expression="new com.easysoft.ecommerce.service.image.FileSystem(path, false)" var="fileSystem"/>
<c:if test="${!empty command.start && !empty command.end}">
    
</c:if>
<c:if test="${!empty command.start && empty command.end}">
    <spring:eval expression="fileSystem.getFilesStartWith(command.start)" var="fileNames"/>
</c:if>
<c:if test="${empty command.start && !empty command.end}">
    <spring:eval expression="fileSystem.getFilesEndWith(command.end)" var="fileNames"/>
</c:if>
<c:if test="${empty command.start && empty command.end}">
    <spring:eval expression="fileSystem.getFiles()" var="fileNames"/>
</c:if>
<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        oTable = $('#files-table').dataTable({
            "bJQueryUI": true,
            "sPaginationType": "full_numbers",
            "bSort": false
        });
    });
</script>

<c:if test="${! empty fileNames}">
    <%--${fn:length(fileNames)}--%>
    <%--<c:forEach items="${fileNames}" var="i" begin="0" step="2" varStatus ="status">--%>
    <%--<c:out value="${fileNames[status.index]}" />--%>
    <%--<c:out value="${fileNames[status.index + 1]}" />--%>
    <%--<c:out value="${fileNames[status.index + 2]}" />--%>
    <%--<c:out value="${fileNames[status.index + 3]}" />--%>
    <%--</c:forEach>--%>

    <table cellpadding="0" cellspacing="0" border="0" class="display" id="files-table">
	<thead>
		<tr>
            <th style="width:100px;"></th>
			<th style="width:100px;"></th>
			<th style="width:100px;"></th>
			<th style="width:100px;"></th>
			<th style="width:100px;"></th>
			<th style="width:100px;"></th>
		</tr>
	</thead>
	<tbody>
    <c:forEach items="${fileNames}" var="i" begin="0" step="6" varStatus ="status">
		<tr>
            <td>
            <c:if test="${!empty fileNames[status.index]}">
            <c:if test="${!fn:endsWith(fileNames[status.index], 'png') && !fn:endsWith(fileNames[status.index], 'jpg') && !fn:endsWith(fileNames[status.index], 'gif')}">
                <c:set value="${fn:split(fileNames[status.index], '.')}" var="name0"/>
                <c:set value="${name0[fn:length(name0)-1]}" var="name0"/>
			<img src="/admin/assets/images/ext/file.${name0}"><div class="file_name">${fileNames[status.index]}</div>
            </c:if>
            <c:if test="${fn:endsWith(fileNames[status.index], 'png') || fn:endsWith(fileNames[status.index], 'jpg') || fn:endsWith(fileNames[status.index], 'gif')}">
            <img src="/product.image?timestamp=&amp;path=/${command.path}/${fileNames[status.index]}&amp;resize.width=100&amp;resize.keepRatio=true"/><div class="file_name">${fileNames[status.index]}</div>
            </c:if>
            </c:if>
            </td>

            <td>
            <c:if test="${!empty fileNames[status.index + 1]}">
            <c:if test="${!fn:endsWith(fileNames[status.index + 1], 'png') && !fn:endsWith(fileNames[status.index + 1], 'jpg') && !fn:endsWith(fileNames[status.index + 1], 'gif')}">
                <c:set value="${fn:split(fileNames[status.index + 1], '.')}" var="name1"/>
                <c:set value="${name1[fn:length(name1)-1]}" var="name1"/>
			<img src="/admin/assets/images/ext/file.${name1}"><div class="file_name">${fileNames[status.index + 1]}</div>
            </c:if>
            <c:if test="${fn:endsWith(fileNames[status.index + 1], 'png') || fn:endsWith(fileNames[status.index + 1], 'jpg') || fn:endsWith(fileNames[status.index + 1], 'gif')}">
            <img src="/product.image?timestamp=&amp;path=/${command.path}/${fileNames[status.index + 1]}&amp;resize.width=100&amp;resize.keepRatio=true"/><div class="file_name">${fileNames[status.index + 1]}</div>
            </c:if>
            </c:if>
            </td>

            <td>
            <c:if test="${!empty fileNames[status.index + 2]}">
            <c:if test="${!fn:endsWith(fileNames[status.index + 2], 'png') && !fn:endsWith(fileNames[status.index + 2], 'jpg') && !fn:endsWith(fileNames[status.index + 2], 'gif')}">
                <c:set value="${fn:split(fileNames[status.index + 2], '.')}" var="name2"/>
                <c:set value="${name2[fn:length(name2)-1]}" var="name2"/>
			<img src="/admin/assets/images/ext/file.${name2}"><div class="file_name">${fileNames[status.index + 2]}</div>
            </c:if>
            <c:if test="${fn:endsWith(fileNames[status.index + 2], 'png') || fn:endsWith(fileNames[status.index + 2], 'jpg') || fn:endsWith(fileNames[status.index + 2], 'gif')}">
            <img src="/product.image?timestamp=&amp;path=/${command.path}/${fileNames[status.index + 2]}&amp;resize.width=100&amp;resize.keepRatio=true"/><div class="file_name">${fileNames[status.index + 2]}</div>
            </c:if>
            </c:if>
            </td>

            <td>
            <c:if test="${!empty fileNames[status.index + 3]}">
            <c:if test="${!fn:endsWith(fileNames[status.index + 3], 'png') && !fn:endsWith(fileNames[status.index + 3], 'jpg') && !fn:endsWith(fileNames[status.index + 3], 'gif')}">
                <c:set value="${fn:split(fileNames[status.index + 3], '.')}" var="name3"/>
                <c:set value="${name3[fn:length(name3)-1]}" var="name3"/>
			<img src="/admin/assets/images/ext/file.${name3}"><div class="file_name">${fileNames[status.index + 3]}</div>
            </c:if>
            <c:if test="${fn:endsWith(fileNames[status.index + 3], 'png') || fn:endsWith(fileNames[status.index + 3], 'jpg') || fn:endsWith(fileNames[status.index + 3], 'gif')}">
            <img src="/product.image?timestamp=&amp;path=/${command.path}/${fileNames[status.index + 3]}&amp;resize.width=100&amp;resize.keepRatio=true"/><div class="file_name">${fileNames[status.index + 3]}</div>
            </c:if>
            </c:if>
            </td>

            <td>
            <c:if test="${!empty fileNames[status.index + 4]}">
            <c:if test="${!fn:endsWith(fileNames[status.index + 4], 'png') && !fn:endsWith(fileNames[status.index + 4], 'jpg') && !fn:endsWith(fileNames[status.index + 4], 'gif')}">
                <c:set value="${fn:split(fileNames[status.index + 4], '.')}" var="name4"/>
                <c:set value="${name4[fn:length(name4)-1]}" var="name4"/>
			<img src="/admin/assets/images/ext/file.${name4}"><div class="file_name">${fileNames[status.index + 4]}</div>
            </c:if>
            <c:if test="${fn:endsWith(fileNames[status.index + 4], 'png') || fn:endsWith(fileNames[status.index + 4], 'jpg') || fn:endsWith(fileNames[status.index + 4], 'gif')}">
            <img src="/product.image?timestamp=&amp;path=/${command.path}/${fileNames[status.index + 4]}&amp;resize.width=100&amp;resize.keepRatio=true"/><div class="file_name">${fileNames[status.index + 4]}</div>
            </c:if>
            </c:if>
            </td>

            <td>
            <c:if test="${!empty fileNames[status.index + 5]}">
            <c:if test="${!fn:endsWith(fileNames[status.index + 5], 'png') && !fn:endsWith(fileNames[status.index + 5], 'jpg') && !fn:endsWith(fileNames[status.index + 5], 'gif')}">
                <c:set value="${fn:split(fileNames[status.index + 5], '.')}" var="name5"/>
                <c:set value="${name5[fn:length(name5)-1]}" var="name5"/>
			<img src="/admin/assets/images/ext/file.${name5}"><div class="file_name">${fileNames[status.index + 5]}</div>
            </c:if>
            <c:if test="${fn:endsWith(fileNames[status.index + 5], 'png') || fn:endsWith(fileNames[status.index + 5], 'jpg') || fn:endsWith(fileNames[status.index + 5], 'gif')}">
            <img src="/product.image?timestamp=&amp;path=/${command.path}/${fileNames[status.index + 5]}&amp;resize.width=100&amp;resize.keepRatio=true"/><div class="file_name">${fileNames[status.index + 5]}</div>
            </c:if>
            </c:if>
            </td>
		</tr>
    </c:forEach>
	</tbody>
</table>
</c:if>
<c:if test="${empty fileNames}">
    <div>Cannot found any files.</div>
</c:if>