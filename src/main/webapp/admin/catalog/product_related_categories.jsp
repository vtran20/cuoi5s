<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<script src="/admin/assets/admin_wpt/js/common_single.js" type="text/javascript"></script>
<c:set var="productId" value="${param.id}"/>
<%--<div class="page-header">--%>
    <%--<a class="btn btn-xs btn-info" id="product_information">--%>
        <%--<i class="ace-icon fa fa-check"></i> <fmt:message key="common.save.changes"/>--%>
    <%--</a>--%>
<%--</div><!-- /.page-header -->--%>

<div class="row">
    <div class="col-xs-12">
        <div class="box" id="box-1">
            <spring:eval expression="serviceLocator.categoryDao.getSubCategories(T(java.lang.Long).valueOf(productId), 'N')" var="relatedCategories"/>
            <spring:eval expression="serviceLocator.categoryDao.getRootCategories(site, false)" var="rootCategories"/>
            <div class="box-container-toggle">
                <div class="box-content">
                    <c:if test="${!empty rootCategories}">

                        <table cellpadding="0" cellspacing="0" border="0"
                               class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                            <thead>
                            <tr>
                                <th><fmt:message key="news.category.name"/></th>
                            </tr>
                            </thead>
                            <tbody>

                            <c:forEach items="${rootCategories}" var="parentCategory">
                                <c:set value="" var="checkboxSelected"/>
                                <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).containCategory(relatedCategories, parentCategory)" var="existing"/>
                                <c:if test="${existing == 'true'}">
                                    <c:set value="checked" var="checkboxSelected"/>
                                </c:if>
                                <tr id="${parentCategory.id}-s${parentCategory.sequence}">
                                    <td><input class="global-toggle-checkbox" type="checkbox" name="newsCategoryId" value="${parentCategory.id}" ${checkboxSelected} lang='<fmt:message key="category.do.you.want.to.assign.product.to.category"/>' data-src="/admin/catalog/assign_product_to_category.html?prodId=${productId}&catId=${parentCategory.id}&csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"/> <c:out value="${parentCategory.name}"/></td>
                                </tr>
                                <%--Get Categories--%>
                                <spring:eval expression="serviceLocator.categoryDao.findByOrder('parentCategory.id', parentCategory.id, 'sequence', site.id)" var="categories"/>
                                <c:if test="${! empty categories}">
                                    <c:forEach items="${categories}" var="category">
                                        <c:set value="" var="checkboxSelected"/>
                                        <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).containCategory(relatedCategories, category)" var="existing"/>
                                        <c:if test="${existing == 'true'}">
                                            <c:set value="checked" var="checkboxSelected"/>
                                        </c:if>
                                        <tr id="${parentCategory.id}-${category.id}-s${category.sequence}">
                                            <td>-------- <input class="global-toggle-checkbox" type="checkbox" name="newsCategoryId" value="${category.id}" ${checkboxSelected} lang='<fmt:message key="category.do.you.want.to.assign.product.to.category"/>' data-src="/admin/catalog/assign_product_to_category.html?prodId=${productId}&catId=${category.id}&csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"/> <c:out value="${category.name}"/></td>
                                        </tr>

                                        <%--Get subCategories--%>
                                        <spring:eval expression="serviceLocator.categoryDao.findByOrder('parentCategory.id', category.id, 'sequence', site.id)" var="subCategories"/>
                                        <c:if test="${! empty subCategories}">
                                            <c:forEach items="${subCategories}" var="subCategory">
                                                <c:set value="" var="checkboxSelected"/>
                                                <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).containCategory(relatedCategories, subCategory)" var="existing"/>
                                                <c:if test="${existing == 'true'}">
                                                    <c:set value="checked" var="checkboxSelected"/>
                                                </c:if>
                                                <tr id="${parentCategory.id}-${category.id}-${subCategory.id}-s${subCategory.sequence}">
                                                    <td>---------------- <input class="global-toggle-checkbox" type="checkbox" name="newsCategoryId" value="${subCategory.id}" ${checkboxSelected} lang='<fmt:message key="category.do.you.want.to.assign.product.to.category"/>' data-src="/admin/catalog/assign_product_to_category.html?prodId=${productId}&catId=${subCategory.id}&csrf=<sec:authentication property="details.csrf"/>" data-header="<fmt:message key="common.confirm.title"/>" confirm-success="<fmt:message key="common.data.saved.success"/>" confirm-fail="<fmt:message key="common.data.saved.fail"/>"/> <c:out value="${subCategory.name}"/></td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>

                                    </c:forEach>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${empty rootCategories}">
                        <fmt:message key="news.category.is.empty"/>
                    </c:if>

                </div>
            </div>
        </div>
    </div>
</div>