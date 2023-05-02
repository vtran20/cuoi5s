<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><decorator:title/></title>
    <decorator:head/>

    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_css.css" type="text/css"/>
    <%--header--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_header-default.css" type="text/css"/>
    <%--Footer--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_footer-v1.css" type="text/css"/>
    <%--(red, blue, orange, light, purple, aqua, brown, dark-blue, light-green, green)--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_${template.templateColorCode}.css" type="text/css"/>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_${template.skinColor}-skin.css" type="text/css"/>
    <script src="/wro/${version}${template.templateCssCode}_js.js" type="text/javascript"></script>
    <c:choose>
        <c:when test="${language == 'vi-VN'}"><script src='//www.google.com/recaptcha/api.js?hl=vi'></script></c:when>
        <c:otherwise><script src='//www.google.com/recaptcha/api.js'></script></c:otherwise>
    </c:choose>
    <h:head/>
    <%--<!--Start of Zopim Live Chat Script-->--%>
    <%--<script type="text/javascript">--%>
        <%--window.$zopim||(function(d,s){var z=$zopim=function(c){z._.push(c)},$=z.s=--%>
                <%--d.createElement(s),e=d.getElementsByTagName(s)[0];z.set=function(o){z.set.--%>
                <%--_.push(o)};z._=[];z.set._=[];$.async=!0;$.setAttribute("charset","utf-8");--%>
            <%--$.src="//v2.zopim.com/?3XjewTLZzY7rSv4h2qQQewOoFP3PsVH4";z.t=+new Date;$.--%>
                    <%--type="text/javascript";e.parentNode.insertBefore($,e)})(document,"script");--%>
    <%--</script>--%>
    <%--<!--End of Zopim Live Chat Script-->--%>
</head>
<c:if test="${template.fullWide != 'Y'}"><c:set var="boxLayout" value="boxed-layout container"/></c:if>
<c:if test="${template.skinColor == 'dark'}"> <c:set var="skinColor" value="dark"/> </c:if>
<%--<c:if test="${template.headerFix == 'Y'}"> header-fixed </c:if>--%>
<body class="${skinColor} ${boxLayout}">
<div class="wrapper">
<!--=== Header ===-->
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<div class="header">
    <div class="container">
        <app:cache key="header_cuoi5s">
        <!-- Logo -->
        <a href="/" class="logo">
            <spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>
            <c:choose>
                <c:when test="${fn:startsWith(siteHeaderFooter.useLogoImg, 'Y')}">
                    <c:if test="${! empty siteHeaderFooter.crop}"><c:set value="op=crop_${siteHeaderFooter.crop}" var="opCrop"/></c:if>
                    <img class="hidden-xs" id="logo-header" src="${imageServer}/get/${siteHeaderFooter.logoImg}.png?${opCrop}&op=scale_x70" alt="${site.name}">
                    <img class="hidden-sm hidden-md hidden-lg" id="logo-header" src="${imageServer}/get/${siteHeaderFooter.logoImg}.png?${opCrop}&op=scale_x50" alt="${site.name}">
                </c:when>
                <c:otherwise>
                    ${siteHeaderFooter.logoText}
                </c:otherwise>
            </c:choose>
        </a>
        <!-- End Logo -->
        </app:cache>
        <%--hidden-xs hidden-sm hidden-md hidden-lg--%>
        <!-- Topbar -->
        <div class="topbar">
            <!-- Topbar Navigation -->
            <ul class="loginbar pull-right">
                <spring:eval expression="T(com.easysoft.ecommerce.controller.SessionUtil).isLoggedIn(pageContext.request, pageContext.response)" var="isLoggedIn"/>
                <c:choose>
                    <c:when test="${isLoggedIn == 'true'}">
                        <c:if test="${sessionObject != null}">
                            <spring:eval expression="sessionObject.getItemsInCart()" var="totalItemsInCart"/>
                            <li class=""><a href="/site/checkout/basket.html"><i class="fa fa-shopping-cart fa-2x"></i> (${totalItemsInCart})</a></li>
                            <li class="topbar-devider"></li>
                        </c:if>
                        <li class="hidden-xs"><a href="/site/main.html"><fmt:message key="site.my.account"/></a></li>
                        <li class="hidden-sm hidden-md hidden-lg"><a href="/site/main.html"><i class="fa fa-user fa-2x"></i></a></li>
                        <li class="topbar-devider"></li>
                        <li class="hidden-xs"><a href="/site/dang-xuat.html"><fmt:message key="site.logout"/></a></li>
                        <li class="hidden-sm hidden-md hidden-lg"><a href="/site/dang-xuat.html"><i class="fa fa-sign-out fa-2x"></i></a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="hidden-xs"><a href="/site/dang-ky.html"><fmt:message key="site.try.it"/></a></li>
                        <li class="hidden-xs topbar-devider"></li>
                        <li class="hidden-xs"><a href="/site/login.html"><fmt:message key="site.login"/></a></li>
                        <li class="hidden-sm hidden-md hidden-lg"><a href="/site/login.html"><i class="fa fa-sign-in fa-2x"></i></a></li>
                    </c:otherwise>
                </c:choose>
                <%--<li><a href="/admin/login.html">Login</a></li>--%>
                <%--<li><a href="#"><i class="fa fa-twitter"></i></a></li>--%>
                <%--<li><a href="#"><i class="fa fa-google-plus"></i></a></li>--%>
                <%--<li><div class="fb-like" data-href="https://facebook.com/cuoi5s" data-layout="button" data-action="like" data-show-faces="true" data-share="false"></div></li>--%>
            </ul>
            <!-- End Topbar Navigation -->
        </div>
        <!-- Brand and toggle get grouped for better mobile display -->
        <%--<div class="navbar-toggle">--%>
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="fa fa-bars"></span>
        </button>
        <%--</div>--%>

    </div>
    <!-- End Topbar -->

    <!-- Navbar -->
    <div class="collapse navbar-collapse mega-menu navbar-responsive-collapse" role="navigation">
        <div class="container">
            <!-- Collect the nav links, forms, and other content for toggling -->
            <h:menu_cuoi5s/>

        </div>
    </div>
    <!-- End Navbar -->
</div>
<!--=== End Header ===-->

<!--=== Content Part ===-->
<decorator:body/>
<!-- End Content Part -->

<app:cache key="footer_cuoi5s">
<%--Start Footer--%>
<c:set var="column" value="0"/>
<spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>
<c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerHeaderDisplay == 'Y'}">
    <c:set var="column" value="${column+1}"/>
</c:if>
<c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerSupportDisplay == 'Y'}">
    <spring:eval expression="serviceLocator.siteSupportDao.findByOrder('showFooter', 'Y', 'sequence', site.id)" var="siteSupports"/>
    <c:set var="column" value="${column+1}"/>
</c:if>
<c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerUsefulLinkDisplay == 'Y'}">
    <spring:eval expression="serviceLocator.menuDao.findActiveByOrder('usefulLink', 'Y', 'sequence', site.id)" var="menus"/>
    <c:set var="column" value="${column+1}"/>
</c:if>
<c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerNewsLinkDisplay == 'Y'}">
    <spring:eval expression="serviceLocator.newsDao.findActiveByOrder('footerLink', 'Y', 'sequence', site.id)" var="newses"/>
    <c:set var="column" value="${column+1}"/>
</c:if>
<c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerContactDisplay == 'Y'}">
    <spring:eval expression="serviceLocator.siteContactDao.findByOrder('showFooter', 'Y', 'sequence', site.id)" var="siteContacts"/>
    <c:set var="column" value="${column+1}"/>
</c:if>
<c:choose>
    <c:when test="${column > 0}">
        <c:set var="number" value="${12%column}"/>
        <fmt:formatNumber var="number" value="${12/column}" maxFractionDigits="0" />
    </c:when>
    <c:otherwise>
        <fmt:formatNumber var="number" value="${12}" maxFractionDigits="0" />
    </c:otherwise>
</c:choose>


<div class="footer-v1">
    <div class="footer">
        <div class="container">
            <div class="row">
                <c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerHeaderDisplay == 'Y'}">
                    <!-- About -->
                    <div class="col-md-${number} md-margin-bottom-40">
                        <div class="posts">
                            <div class="headline"><h2>${siteHeaderFooter.footerHeader}</h2></div>
                                ${siteHeaderFooter.footerContent}
                        </div>
                    </div>
                </c:if>
                <!-- End About -->

                <!-- Support -->
                <c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerSupportDisplay == 'Y'}">
                    <div class="col-md-${number} map-img md-margin-bottom-40">
                        <div class="headline">
                            <h2>
                                <c:choose>
                                    <c:when test="${!empty siteHeaderFooter.footerSupportHeader}">
                                        <c:out value="${siteHeaderFooter.footerSupportHeader}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="template.admin.customer.service"/>
                                    </c:otherwise>
                                </c:choose>
                            </h2>
                        </div>
                        <c:forEach items="${siteSupports}" var="siteSupport">
                            <address class="md-margin-bottom-40">
                                <c:if test="${!empty siteSupport.name}">
                                    ${siteSupport.name} <br>
                                </c:if>
                                <c:if test="${!empty siteSupport.phone}">
                                    Phone: ${siteSupport.phone} <br>
                                </c:if>
                                <c:if test="${!empty siteSupport.chatId}">
                                    <c:if test="${siteSupport.chatType == 'yahoo'}">
                                        Yahoo Chat: <a href="ymsgr:sendim?${siteSupport.chatId}"><img src="http://opi.yahoo.com/online?u=${siteSupport.chatId}&amp;m=g&amp;t=2" border="0"></a><br>
                                    </c:if>
                                    <c:if test="${siteSupport.chatType == 'skype'}">
                                        Skype Chat: <a href="skype:${siteSupport.chatId}?call"><img src="http://www.skypeassets.com/content/dam/scom/images/downloads/desktop/apps-click-to-call.png" alt="Skype Me™!"></a> <br>
                                    </c:if>
                                </c:if>
                                <c:if test="${!empty siteSupport.timeAvailable}">
                                    Hours: ${siteSupport.timeAvailable}<br>
                                </c:if>
                            </address>
                        </c:forEach>
                    </div>
                </c:if>
                <!-- End Support -->

                <!-- Link List -->
                <c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerUsefulLinkDisplay == 'Y'}">
                    <div class="col-md-${number} md-margin-bottom-40">
                        <div class="headline">
                            <h2>
                                <c:choose>
                                    <c:when test="${!empty siteHeaderFooter.footerUsefulLinkHeader}">
                                        <c:out value="${siteHeaderFooter.footerUsefulLinkHeader}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="footer.site.useful.link"/>
                                    </c:otherwise>
                                </c:choose>
                            </h2>
                        </div>
                        <ul class="list-unstyled link-list">
                            <c:forEach items="${menus}" var="menu">
                                <c:set var="siteUrl" value="/content/${menu.uri}"/>
                                <%--Convert url by using correct language. Ex: contact-us.html -> lien-he.html --%>
                                <c:if test="${menu.homePage == 'Y'}">
                                    <c:set var="siteUrl" value="/"/>
                                </c:if>
                                <c:if test="${menu.menuTemplate == 'Y'}">
                                    <c:set var="siteUrl" value="/${menu.uri}"/>
                                </c:if>
                                <c:if test="${menu.menuTemplate == 'E'}">
                                    <c:set var="siteUrl" value="${menu.uri}"/>
                                </c:if>
                                <c:url value="${siteUrl}" var="siteUrl"/>
                                <li><a href="${siteUrl}">${menu.name}</a><i class="fa fa-angle-right"></i></li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
                <!-- End Link List -->
                <!-- News Link List -->
                <c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerNewsLinkDisplay == 'Y'}">
                    <div class="col-md-${number} md-margin-bottom-40">
                        <div class="headline">
                            <h2>
                                <c:choose>
                                    <c:when test="${!empty siteHeaderFooter.footerNewsLinkHeader}">
                                        <c:out value="${siteHeaderFooter.footerNewsLinkHeader}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="footer.site.news.link"/>
                                    </c:otherwise>
                                </c:choose>
                            </h2>
                        </div>
                        <ul class="list-unstyled link-list">
                            <c:forEach items="${newses}" var="news">
                                <c:url value="/news/${news.uri}" var="newsurl"/>
                                <li><a href="${newsurl}">${news.title}</a><i class="fa fa-angle-right"></i></li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
                <!-- End News Link List -->

                <!-- Contact Us -->
                <c:if test="${!empty siteHeaderFooter && siteHeaderFooter.footerContactDisplay == 'Y'}">
                    <div class="col-md-${number} map-img md-margin-bottom-40">
                        <div class="headline"><h2>
                            <c:choose>
                                <c:when test="${!empty siteHeaderFooter.footerContactHeader}">
                                    <c:out value="${siteHeaderFooter.footerContactHeader}"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="site.contact.us"/>
                                </c:otherwise>
                            </c:choose>
                        </h2></div>
                        <c:forEach items="${siteContacts}" var="siteContact">
                            <address class="md-margin-bottom-40">
                                <c:if test="${!empty siteContact.name}">
                                    ${siteContact.name} <br>
                                </c:if>
                                <c:if test="${!empty siteContact.address_1}">
                                    ${siteContact.address_1}, ${siteContact.city}, ${siteContact.state}&nbsp;${siteContact.zipCode}<br>
                                </c:if>
                                <c:if test="${!empty siteContact.phone}">
                                    Phone: ${siteContact.phone} <br>
                                </c:if>
                                <c:if test="${!empty siteContact.fax}">
                                    Fax: ${siteContact.fax} <br>
                                </c:if>
                                <c:if test="${!empty siteContact.email}">
                                    Email: <a href="mailto:${siteContact.email}" class="">${siteContact.email}</a> <br>
                                </c:if>
                            </address>
                        </c:forEach>
                    </div>
                    <!-- End Address -->
                </c:if>
            </div>
        </div>
    </div><!--/footer-->

    <div class="copyright">
        <div class="container">
            <div class="row">
                <div class="col-md-7">
                    <p>
                        <jsp:useBean id="now" class="java.util.Date" />
                        <fmt:formatDate var="year" value="${now}" pattern="yyyy" />
                        ${year} &copy; ${site.domain}. ALL Rights Reserved.
                        <a href="/noi-dung/chinh-sach-bao-mat.html">Chính Sách Bảo Mật</a> | <a href="/noi-dung/dieu-khoan-su-dung.html">Điều Khoản Sử Dụng</a>
                    </p>
                    <div><span style="font-size: 16px;color:#eee;">Website và Dữ liệu được hosting tại</span> <img src="/assets/images/amazon_cloud.png"/></div>
                </div>

                <!-- Social Links -->
                <div class="col-md-2">
                    <ul class="footer-socials list-inline">
                        <spring:eval expression="site.siteParamsMap.get('FACEBOOK_FAN_PAGE')" var="facebookFanPage"/>
                        <c:if test="${!empty facebookFanPage}">
                            <li><a href="${facebookFanPage}" class="tooltips" data-toggle="tooltip" data-placement="top" title="" data-original-title="Facebook"><i class="fa fa-facebook"></i></a></li>
                        </c:if>
                        <spring:eval expression="site.siteParamsMap.get('GOOGLE_FAN_PAGE')" var="googleFanPage"/>
                        <c:if test="${!empty googleFanPage}">
                            <li><a href="${googleFanPage}" class="tooltips" data-toggle="tooltip" data-placement="top" title="" data-original-title="Google Plus"><i class="fa fa-google-plus"></i></a></li>
                        </c:if>
                        <spring:eval expression="site.siteParamsMap.get('YOUTUBE_FAN_PAGE')" var="youtubeChannel"/>
                        <c:if test="${!empty youtubeChannel}">
                            <li><a href="${youtubeChannel}" class="tooltips" data-toggle="tooltip" data-placement="top" title="" data-original-title="Youtube Channel"><i class="fa fa-youtube"></i></a></li>
                        </c:if>
                        <spring:eval expression="site.siteParamsMap.get('TWITTER_FAN_PAGE')" var="twitterFanPage"/>
                        <c:if test="${!empty twitterFanPage}">
                            <li><a href="${twitterFanPage}" class="tooltips" data-toggle="tooltip" data-placement="top" title="" data-original-title="Twiter"><i class="fa fa-twitter"></i></a></li>
                        </c:if>
                    </ul>
                </div>
                <!-- End Social Links -->
                <c:choose>
                    <c:when test="${site.parentSite.siteType == 3}">
                        <c:set var="ownerDomain" value="${site.parentSite.domain}"/>
                        <c:set var="ownerName" value="${site.parentSite.domain}"/>
                        <div class="col-md-3">
                            <p>
                                Designed by <a href="http://${ownerDomain}">${ownerName}</a> | <a href="/admin/login.html"><fmt:message key='site.login.admin'/></a>
                            </p>
                        </div>
                    </c:when>
                    <c:when test="${site.siteType == 3}">
                        <c:set var="ownerDomain" value="${site.domain}"/>
                        <c:set var="ownerName" value="${site.domain}"/>
                        <div class="col-md-3">
                            <p>
                                Designed by <a href="http://${ownerDomain}">${ownerName}</a> | <a href="/admin/login.html"><fmt:message key='site.login.admin'/></a>
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:set var="ownerDomain" value="${site.parentSite.domain}"/>
                        <c:set var="ownerName" value="WebPhatTai."/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div><!--/copyright-->
</div>
<%--End Footer--%>
</app:cache>
</div><!--/wrapper-->

<script type="text/javascript">
    jQuery('.search').click(function () {
        if(jQuery('.search-btn').hasClass('fa-search')){
            jQuery('.search-open').fadeIn(500);
            jQuery('.search-btn').removeClass('fa-search');
            jQuery('.search-btn').addClass('fa-times');
        } else {
            jQuery('.search-open').fadeOut(500);
            jQuery('.search-btn').addClass('fa-search');
            jQuery('.search-btn').removeClass('fa-times');
        }
    });
</script>
<!--[if lt IE 9]>
<script src="/themes/m3x/plugins/respond.js"></script>
<script src="/themes/m3x/plugins/html5shiv.js"></script>
<script src="/themes/m3x/js/plugins/placeholder-IE-fixes.js"></script>
<![endif]-->


<%--<h:editor/>--%>

</body>
</html>