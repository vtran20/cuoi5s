<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>CMS Area</title>
    <meta name="decorator" content="admin"/>
</head>

<body>
<script type="text/javascript">
$(function() {
    $("#tabs").tabs({
        ajaxOptions: {
            error: function(xhr, status, index, anchor) {
                $(anchor.hash).html("Couldn't load this tab. We'll try to fix this as soon as possible. If this wouldn't be a demo.");
            }
        }
    });

});

</script>
<div id="tabs">
	<ul>
		<li><a href="#cms_area">CMS Area</a></li>
	</ul>
	<div id="cms_area">

<sec:authentication property="principal.site" var="site"/>
<spring:eval expression="serviceLocator.cmsAreaDao.getAllCmsAreaBySite(systemContext.site)" var="items" />

<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        oTable = $('#cmsareas').dataTable({
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"
        });

        $( "button, a.button" ).button();

    });

    function removeCMSArea (cmsAreaId) {
        var answer = confirm("Do you want to delete this CMS Area?");
        if (answer){
            $.get("/admin/content/"+cmsAreaId+"/cmsareadelete.html",
                    function(data) {
                        if ("ok" == data) {
                            alert ("Delete successfully!")
                        } else {
                            alert ("Delete fail!")
                        }
                        window.location.reload();
            });
        }
    }

</script>

<div class="add_cms_area button">
    <a class="button" href="/admin/content/form.html">+ Create CMS Area</a>
</div>
<c:if test="${!empty items}">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="cmsareas">
	<thead>
		<tr>
			<th style="text-align:left">CMS Area Name</th>
			<th style="width:100px">Is dynamic Content</th>
			<th style="text-align:left;width:200px">Path</th>
			<th style="width:30px">Edit</th>
			<th style="width:30px">Delete</th>
		</tr>
	</thead>
	<tbody>
    <c:forEach items="${items}" varStatus="cmsarea">
		<tr>
			<td><c:out value="${cmsarea.current.cmsName}"/></td>
			<td>${cmsarea.current.dynamic == true?'Y':'N'}</td>
			<td><c:out value="${cmsarea.current.path}"/></td>
			<td class="center"><a href="/admin/content/${cmsarea.current.id}/editcmsarea.html"><img src="/admin/assets/images/common/document-attribute-e.png" alt="Edit CMS Area"/></a></td>
			<td class="center removeCMSArea"><a href="#" onclick="removeCMSArea(${cmsarea.current.id})"><img src="/admin/assets/images/common/cross-button.png" alt="Delete CMS Area"/></a></td>
		</tr>
    </c:forEach>
	</tbody>
</table>
</c:if>
<c:if test="${empty items}">
    No CMS Area.
</c:if>

    </div>
</div>

</body>
</html>
