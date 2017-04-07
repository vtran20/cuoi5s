<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>Cms Area</title>
    <meta name="decorator" content="admin"/>
</head>

<body>

<sec:authentication property="principal.site" var="site"/>

<script type="text/javascript">
$(function() {
    $("#tabs").tabs({
        ajaxOptions: {
            error: function(xhr, status, index, anchor) {
                $(anchor.hash).html("Couldn't load this tab. We'll try to fix this as soon as possible.");
            }
        },
        cookie: {
            // store cookie for a day, without, it would be a session cookie
            expires: 1
        }
    });

    $( "input:submit").button();

    // validate signup form on keyup and submit
    $("#form").validate({
        rules: {
            name: "required",
            uri: "required"
        }
    });

});
</script>

<c:if test="${! empty cmsArea.id}">
    <c:set value="/admin/content/${cmsArea.id}/updatecmsarea.html" var="formUrl"/>
    <c:set value="post" var="method"/>
    <c:set value="${cmsArea.id}" var="cmsAreaId"/>
    <div class="itemTitle">Cms Area: ${cmsArea.cmsName}</div>
</c:if>
<c:if test="${empty cmsArea.id}">
    <c:set value="/admin/content/insertcmsarea.html" var="formUrl"/>
    <c:set value="put" var="method"/>
    <c:set value="0" var="cmsAreaId"/>
</c:if>

<div id="tabs">
	<ul>
		<li><a href="#tabs-1">Cms Area</a></li>
		<li><a href="/admin/content/${cmsAreaId}/cmsareacontents.html">Cms Area Contents</a></li>
	</ul>
	<div id="tabs-1">
        <div id="dialog-form">

            <form:form id="form" action="${formUrl}" commandName="cmsArea" method="${method}">
                <table style="width: 100%">
                    <tbody>
                    <tr>
                        <td colspan="2" align="right">
                            <input type="submit" value="Save Content"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right" class="common-sm-tab-messages">
                            <h:frontendmessage _messages="${messages}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div style="margin-right: 5px; height: 120px;" class="common-sm-tab-box">
                                <h5>Content Information</h5>
                                <table style="width: 430px;" class="common-sm-form-table">
                                    <tbody>
                                    <tr>
                                        <td width="102px"><label for="cmsName">*Cms Area Name:</label></td>
                                        <td>
                                            <form:input path="cmsName" maxlength="50" size="25" id="cmsName"/>&nbsp;&nbsp;
                                        </td>
                                        <td>
                                            <form:checkbox path="dynamic" id="dynamic" /><label for="dynamic">Dynamic</label>
                                        </td>


                                    </tr>
                                    <tr>
                                        <td><label for="path">Path:</label></td>
                                        <td>
                                            <form:input path="path" maxlength="50" size="25" id="path"/>
                                        </td>
                                        <td>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label for="path">Title:</label></td>
                                        <td>
                                            <form:input path="title" maxlength="50" size="25" id="title"/>
                                        </td>
                                        <td>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                        </td>
                        <td style="width: 250px;">
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2">
                            <div style="height: 360px; margin-top: 10px; margin-bottom: 30px;" class="common-sm-tab-box">
                                <h5>Meta Descriptions</h5>

                                <div><label for="metaDescription">Meta Description:</label></div>
                                <div>
                                    <form:textarea path="metaDescription" cssStyle="width:99%" id="metaDescription"/>
                                </div>

                                <div class="gecko" style="height: 1px;"></div>

                                <div><label for="metaKeyword">Meta Keyword:</label></div>
                                <div>
                                    <form:textarea path="metaKeyword" cssStyle="width:99%" id="metaKeyword"/>
                                </div>

                                <div class="gecko" style="height: 1px;"></div>

                            </div>
                        </td>
                    </tr>

                    </tbody>
                </table>
            </form:form>
        </div>

	</div>
</div>

</body>
</html>
