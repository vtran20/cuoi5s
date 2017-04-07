<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="video" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.model.Video"%>

<div class="col-sm-4">
    <div class="thumbnail-style">
        <c:url var="url" value="/video/${video.uri}/${video.id}.html?videoId=${video.videoId}"/>
        <a href="${url}"><img alt="${video.name}" src="${video.mediumImgUrl}" class="img-responsive"></a>
        <c:if test="${!empty video.duration}">
        <div class="duration">${video.duration}</div>
        </c:if>
        <ul class="list-unstyled list-inline blog-info">
            <li><i class="fa fa-calendar"></i> <fmt:formatDate pattern="dd/MM/yyyy" value="${video.updatedDate}"/></li>
            <%--<li><i class="fa fa-eye"></i> ${video.viewCount} views</li>--%>
            <%--<li><i class="fa fa-pencil"></i> Diana Anderson</li>--%>
            <%--<li><i class="fa fa-comments"></i> <a href="#">24 Comments</a></li>--%>
        </ul>
        <p title="${video.name}">${video.name}</p>
    </div>
</div>
