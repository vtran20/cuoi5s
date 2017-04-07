<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<html>
<head>
<link rel="stylesheet" type="text/css" media="all" href="/wro/${version}admin_css.css" />
<script src="/wro/${version}admin_js.js" type="text/javascript"></script>
<title>System Administrator: <decorator:title/></title>

</head>
<script>
    function closePopup() {
        window.opener.window.location.reload(true);
        window.close ();
    }
</script>
<body>
<div style="float:right;padding:5px 10px 5px 0;font-weight:bold;font-size:13px;vertical-align:middle;"><a href="#" onclick="closePopup()"><img src="/admin/assets/images/common/cross-button.png"/> Close</a></div>
<!-- Start Content -->
<div><decorator:body/></div>
<!-- End Content -->

</body>
</html>
