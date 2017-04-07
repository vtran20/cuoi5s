<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="products" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="parCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="cat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="subCat" required="false" rtexprvalue="true" type="com.easysoft.ecommerce.model.Category"%>
<%@ attribute name="step" required="false" rtexprvalue="true" type="java.lang.Integer"%>
<c:if test="${!(!empty step && step > 0)}">
    <c:set value="3" var="step"/>
</c:if>
<c:forEach begin="0" end="${fn:length(products)}" step="${step}" varStatus="index">
<div class="row illustration-v2 margin-bottom-30">
    <c:forEach items="${products}" begin="${index.index}" end="${index.index + step - 1}" var="product">

        <h:productthumbnail product="${product}" parCat="${parCat}" cat="${cat}" subCat="${subCat}"/>

    </c:forEach>

</div>
</c:forEach>
