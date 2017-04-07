<%@page import="java.util.Calendar"%>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.TimeZone" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
<!--
<%
Calendar cal = GregorianCalendar.getInstance();
cal.setTimeZone(TimeZone.getTimeZone("GMT"));
cal.clear();
cal.set(2000, 0, 1);
int count = 0;
long initial = cal.getTime().getTime();
cal.add(Calendar.DATE, 1);
long gap = cal.getTime().getTime() - initial;
cal.clear();
cal.set(2000, 0, 1);
while (cal.get(Calendar.YEAR) < 2011) {
    count++;
    cal.add(Calendar.DATE, 1);
}

out.print("var initial=" + initial + ";");
out.print("var count=" + count + ";");
out.print("var gap=" + gap + ";");
%>
window.onload = function ()
{
    var offsets = "";
    for (var index = 0; index < count; index ++) {
        var date = new Date(initial);
        if (offsets != "") offsets += ",";
        offsets += new String(date.getTimezoneOffset());
        initial += gap;
    }

    if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    var url = "/guesstz";
    var params = "offsets=" + offsets;
    xmlhttp.open("POST", url, false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.setRequestHeader("Content-length", params.length);
    xmlhttp.setRequestHeader("Connection", "close");
    xmlhttp.send(params);
    timeZone = xmlhttp.responseText;

    document.forms[0].timeZone.value = timeZone;
    document.forms[0].submit();

}
-->
</script>
</head>
<body>
<form id="guesstz" name="guesstz" method="get" action="${param.url}">
<input name="timeZone" type="hidden" value="" />
</form>
</body>
</html>
