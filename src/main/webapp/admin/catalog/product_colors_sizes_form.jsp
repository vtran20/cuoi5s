<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<c:set var="productId" value="${param.id}"/>
<c:if test="${! empty productId}">
    <spring:eval expression="serviceLocator.productDao.findBy('product.id', T(java.lang.Long).valueOf(productId), site)" var="productVariants"/>
</c:if>

<div class="row">
    <div class="col-xs-12">
        <div id="message_alert"></div>
        <div class="page-header">
    <a class="btn btn-xs btn-info" id="product_information">
        <i class="ace-icon fa fa-check"></i> <fmt:message key="common.save.changes"/>
    </a>
</div><!-- /.page-header -->
    </div>
</div>
