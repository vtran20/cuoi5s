<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>String Param</title>
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
		<li><a href="#tabs-1">String Param</a></li>
	</ul>
	<div id="tabs-1">

<sec:authentication property="principal.site" var="site"/>
<spring:eval expression="serviceLocator.stringParamDao.findAll()" var="items" />

<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        oTable = $('#stringparams').dataTable({
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"
        });

        $( "button, a.button" ).button();

    });

    function removeStringParam (stringParamId) {
        var answer = confirm("Do you want to delete this String Param?");
        if (answer){
            $.get("/admin/utility/"+stringParamId+"/stringparamdelete.html",
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

<div class="add_stringparam button">
    <a class="button" href="/admin/utility/form.html">+ Create String Param</a>
</div>
<c:if test="${!empty items}">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="stringparams">
	<thead>
		<tr>
			<th style="text-align:left">String Param Name</th>
			<th style="text-align:left;width:200px">String Param Key</th>
			<th style="width:30px">Edit</th>
			<th style="width:30px">Delete</th>
		</tr>
	</thead>
	<tbody>
    <c:forEach items="${items}" varStatus="stringparam">
		<tr>
			<td><c:out value="${stringparam.current.name}"/></td>
			<td><c:out value="${stringparam.current.key}"/></td>
			<td class="center"><a href="/admin/utility/${stringparam.current.id}/editstringparam.html"><img src="/admin/assets/images/common/document-attribute-e.png" alt="Edit Spring Param"/></a></td>
			<td class="center removeStringParam"><a href="#" onclick="removeStringParam(${stringparam.current.id})"><img src="/admin/assets/images/common/cross-button.png" alt="Delete String Param"/></a></td>
		</tr>
    </c:forEach>
	</tbody>
</table>
</c:if>
<c:if test="${empty items}">
    No String Param.
</c:if>

    </div>
</div>

</body>
</html>
