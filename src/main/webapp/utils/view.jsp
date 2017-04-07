<!DOCTYPE html>
<%@ page import="com.easysoft.ecommerce.controller.SessionUtil" %>
<%@ page import="com.easysoft.ecommerce.model.session.SessionObject" %>
<%@ page import="com.thoughtworks.xstream.XStream" %>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>

<html>
<head>
<title>View Session Object</title>
</head>

<body>


<pre>

<%
    Integer icount = (Integer)session.getAttribute("count");
    out.println("total live sessions "+icount);

    XStream xstream = new XStream();
    xstream.alias("Session", SessionObject.class);
    xstream.alias("Addresses", com.easysoft.ecommerce.model.session.AddressesMap.class);
    xstream.alias("Address", com.easysoft.ecommerce.model.session.AddressMap.class);
    xstream.alias("CreditCard", com.easysoft.ecommerce.model.session.CreditCardMap.class);
    xstream.alias("Item", com.easysoft.ecommerce.model.session.ItemMap.class);
    xstream.alias("Order", com.easysoft.ecommerce.model.session.OrderMap.class);

    String sessionId = request.getParameter("id");
    SessionObject so = null;
    if (!org.apache.commons.lang.StringUtils.isEmpty(sessionId)) {
        com.easysoft.ecommerce.dao.UserSessionDao usd = com.easysoft.ecommerce.service.ServiceLocatorHolder.getServiceLocator().getSystemContext().getServiceLocator().getUserSessionDao();
        so = usd.getUserSession(sessionId);
    } else {
        so = SessionUtil.load(request, response);
    }
    java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
    java.io.Writer writer = new java.io.OutputStreamWriter(outputStream, "UTF-8");

    xstream.toXML(so, writer);
    String data = outputStream.toString("UTF-8");

//    String data = xstream.toXML(so);

    out.write(so.toString());
    out.write("<br>");
    out.write(org.apache.commons.lang.StringEscapeUtils.escapeXml(data));

%>
</pre>




</body>
</html>
