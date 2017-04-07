<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<!--[if lt IE 7]>
<style type="text/css">
.widget-ie6png, .ie6png {
behavior: url(/iepngfix.htc);
}
</style>
<![endif]-->

<%--Add ico --%>
<spring:eval expression="site.siteParamsMap.get('LOGO_ICO')" var="ico"/>
<c:if test="${! empty ico}">
    <link rel="SHORTCUT ICON" href="${ico}" title="${site.domain}">
</c:if>

<%--Implement bing webmaster--%>
<spring:eval expression="site.siteParamsMap.get('BING_VERIFY_CODE')" var="bingVerifyCode"/>
<c:if test="${! empty bingVerifyCode}">
    <meta name="msvalidate.01" content="${bingVerifyCode}"/>
</c:if>

<%--Implement yahoo webmaster--%>
<spring:eval expression="site.siteParamsMap.get('YAHOO_VERIFY_CODE')" var="yahooVerifyCode"/>
<c:if test="${! empty yahooVerifyCode}">
    <META name="y_key" content="${yahooVerifyCode}"/>
</c:if>

<%--Implement google analysis--%>
<spring:eval expression="site.siteParamsMap.get('GOOGLE_ANALYSIS_ACCOUNT')" var="googleAnalysisAccount"/>
<c:if test="${!empty googleAnalysisAccount}">
    <script type="text/javascript">
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

        ga('create', '${googleAnalysisAccount}', '${site.domain}');
        ga('send', 'pageview');
    </script>
</c:if>