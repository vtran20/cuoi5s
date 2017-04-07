<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="recentlyItems" value="${sessionObject.RECENTLY_VIEW}"/>
<c:if test="${!empty recentlyItems}">
    <spring:eval expression="serviceLocator.getProductDao().getProductsByIds(recentlyItems)" var="products"/>
    <c:if test="${! empty products}">
        <div class="recent-view" style="padding:5px 10px 10px 10px">
            <h3 style=";background-color:#DF0403;padding:7px 0 6px 5px;margin-bottom:3px;vertical-align:middle;color: #ffffff;border-radius: 6px;
border-radius: 6px;">
                SẢN PHẨM BẠN ĐÃ XEM GẦN ĐÂY</h3>
        </div>

        <h:scrollable products="${products}" id="recently-view"/>

    </c:if>
</c:if>
