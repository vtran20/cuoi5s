<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%--This tag will load the dynamic content by user that. It is used in the pages that are cached at page level--%>
<%@ attribute name="id" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="url" required="true" rtexprvalue="true" type="java.lang.String" %>

<script>
    $("#${id}").load("${url}");
</script>
