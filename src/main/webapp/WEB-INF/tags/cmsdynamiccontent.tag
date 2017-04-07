<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ include file="common.tagf" %>

<%@ attribute name="name"  required="true" rtexprvalue="true" type="java.lang.String"%>
<spring:eval expression="serviceLocator.cmsAreaContentDao.getCmsAreaDynamicContent(systemContext.site, name, T(com.easysoft.ecommerce.web.filter.BrowserURIHolder).getURI())" var="cmsContent" />
${cmsContent.content}



