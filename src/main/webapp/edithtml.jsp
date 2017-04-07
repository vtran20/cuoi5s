<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
</head>

<body>
<script>
    window.onload = function () {
        $('#textarea_html').val(window.opener.document.getElementById("content-editor").innerHTML);
    }

    function sendDataToParent() {
        window.opener.document.getElementById("content-editor").innerHTML = $('#textarea_html').val();
        window.close();
    }
</script>
<a href="javascript:sendDataToParent()">Save</a>

<div style="clear: both;"/>
<div>
    <textarea cols="100" rows="100" id="textarea_html"></textarea>
</div>
</body>
</html>