<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>
<spring:eval expression="serviceLocator.siteDao.getSitesByUser(T(java.lang.Long).valueOf(sessionObject.USER_ID))" var="sites" />
<c:if test="${fn:length(sites) > 1}">
    <form action="/site/modules.html" method="get">
    <div class="row  margin-bottom-30">
        <div class="col-md-12">
            <select name="thisSiteId" class="site-select required input-lg">
                <option value=''><fmt:message key="site.select.site"/></option>
                <c:forEach items="${sites}" var="pv">
                    <option value="${pv.id}"> ${pv.subDomain}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="row  margin-bottom-30">
        <div class="col-md-12">
            <button type="submit" class="btn-u add-module"><fmt:message key="site.select.site"/></button>
        </div>
    </div>
    </form>
</c:if>
