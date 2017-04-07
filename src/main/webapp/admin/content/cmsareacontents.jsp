<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

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
	<div id="cms_area">

<sec:authentication property="principal.site" var="site"/>
<spring:eval expression="serviceLocator.cmsAreaContentDao.findBy('cmsArea.id', cmsAreaId)" var="items" />

<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        oTable = $('#cmsareacontents').dataTable({
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"
        });

        $( "button, a.button" ).button();

    });

    function removeCMSAreaContent (cmsAreaContentId) {
        var answer = confirm("Do you want to delete this CMS Area Content?");
        if (answer){
            $.get("/admin/content/"+cmsAreaContentId+"/cmsareacontentdelete.html",
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
    <a class="button" href="/admin/content/form.html">+ Create CMS Area Content</a>
</div>
<c:if test="${!empty items}">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="cmsareacontents">
	<thead>
		<tr>
			<th style="text-align:left">Active</th>
			<th style="width:150px">Start Date</th>
			<th style="text-align:left;width:150px">End Date</th>
			<th style="width:30px">Edit</th>
			<th style="width:30px">Delete</th>
		</tr>
	</thead>
	<tbody>
    <c:forEach items="${items}" varStatus="cmsareacontent">
		<tr>
            <td class="center"><input id="cmsareacontent_${cmsareacontent.current.id}" class="cmsareacontentActive" type="checkbox" name="active" value="${cmsareacontent.current.id}" ${cmsareacontent.current.active == true?'checked':''} onClick="activeCmsAreaContent(this, ${cmsareacontent.current.id})"></td>
			<td><c:out value="${cmsareacontent.current.startDate}"/></td>
			<td><c:out value="${cmsareacontent.current.endDate}"/></td>
			<td class="center"><a href="/admin/content/${cmsareacontent.current.id}/editcmsareacontent.html"><img src="/admin/assets/images/common/document-attribute-e.png" alt="Edit CMS Area Content"/></a></td>
			<td class="center removeCMSAreaContent"><a href="#" onclick="removeCMSAreaContent(${cmsareacontent.current.id})"><img src="/admin/assets/images/common/cross-button.png" alt="Delete CMS Area Content"/></a></td>
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
