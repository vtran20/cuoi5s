<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="products" required="true" rtexprvalue="true" type="java.util.ArrayList"%>
<%@ attribute name="id" required="true" rtexprvalue="true" type="java.lang.String"%>

<!-- "previous page" action -->
<a class="prev browse left"></a>

<!-- root element for scrollable -->
<div class="scrollable" id="${id}">

    <!-- root element for the items -->
    <div class="items">
        <c:set var="itemsperrow" value="5"/>
        <!-- 1-itemsperrow -->
        <c:set var="count" value="${fn:length(products)}"/>
        <c:forEach items="${products}" varStatus="status" begin="0" end="${fn:length(products)}" step="${itemsperrow}">
            <div>
                <ul style="list-style-type: none; z-index: 1;">
                    <c:if test="${status.index + itemsperrow < count}">
                        <c:set var="end" value="${status.index + itemsperrow}"/>
                    </c:if>
                    <c:if test="${status.index + itemsperrow >= count}">
                        <c:set var="end" value="${count}"/>
                    </c:if>
                    <c:forEach var="product" items="${products}" varStatus="status" begin="${status.index}" end="${end}">

                        <li class="item-recent-view" style="float: left; width: 184px;">
                            <h:productthumbnail product="${product}"/>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:forEach>

    </div>

</div>

<!-- "next page" action -->
<a class="next browse right"></a>

<script>
    // execute your scripts when the DOM is ready. this is mostly a good habit
    $(function() {

        // initialize scrollable
        $("#${id}").scrollable();

    });
</script>


