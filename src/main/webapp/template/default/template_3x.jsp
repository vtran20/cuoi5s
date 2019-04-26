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
    <link rel="stylesheet" id="templateColorCode" href="/wro/${version}${template.templateCssCode}_${template.templateColorCode}.css" type="text/css"/>
    <link rel="stylesheet" id="skinColor" href="/wro/${version}${template.templateCssCode}_${template.skinColor}-skin.css" type="text/css"/>
    <script src="/wro/${version}${template.templateCssCode}_js.js" type="text/javascript"></script>
    <c:choose>
    <c:when test="${language == 'vi-VN'}"><script src='//www.google.com/recaptcha/api.js?hl=vi'></script></c:when>
    <c:otherwise><script src='//www.google.com/recaptcha/api.js'></script></c:otherwise>
    </c:choose>
<h:head/>
</head>
<c:choose>
    <c:when test="${template.fullWide != 'Y'}">
        <c:set var="boxLayout" value="boxed-layout container"/>
        <c:set var="box" value="active-switcher-btn"/>
    </c:when>
    <c:otherwise>
        <c:set var="fullWide" value="active-switcher-btn"/>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${template.skinColor == 'dark'}">
        <c:set var="skinColor" value="dark"/>
        <c:set var="skinDark" value="active-switcher-btn"/>
    </c:when>
    <c:otherwise>
        <c:set var="skinLight" value="active-switcher-btn"/>
    </c:otherwise>
</c:choose>
<%--<c:if test="${template.headerFix == 'Y'}"> header-fixed </c:if>--%>
<body class="${skinColor} ${boxLayout}">
<%--Switch style--%>
<spring:eval expression="systemContext.getGlobalConfig('demo.sites')" var="demoSites"/>
<c:if test="${fn:contains(demoSites, site.subDomain)}">
    <!--=== Style Switcher ===-->
    <script>
        jQuery(function ($) {
            var panel = jQuery('.style-switcher');
            var templateColorCode = "${template.templateColorCode}";
            var skinColor = "${template.skinColor}";
            var fullWide = "${template.fullWide}";

            jQuery('.style-switcher-btn').click(function () {
                jQuery('.style-switcher').show();
            });

            jQuery('.theme-close').click(function () {
                jQuery('.style-switcher').hide();
            });

            jQuery('li', panel).click(function () {
                var color = jQuery(this).attr("data-style");
                var data_header = jQuery(this).attr("data-header");
                setColor(color, data_header);
                jQuery('.list-unstyled li', panel).removeClass("theme-active");
                jQuery(this).addClass("theme-active");
            });

            var setColor = function (color, data_header) {
                var newUrl = $("#templateColorCode").attr("href").replace(templateColorCode,color);
                jQuery('#templateColorCode').attr("href", newUrl);
                templateColorCode = color; //keep the current one
            }

            //Skin Layout
            jQuery('.skins-btn').click(function(){
                jQuery(this).addClass("active-switcher-btn");
                jQuery(".handle-skins-btn").removeClass("active-switcher-btn");
                jQuery("body").removeClass("light");
                jQuery("body").addClass("dark");
                var newUrl = $("#skinColor").attr("href").replace(skinColor,"dark");
                jQuery('#skinColor').attr("href", newUrl);
                skinColor = "dark"; //keep the current one

            });
            jQuery('.handle-skins-btn').click(function(){
                jQuery(this).addClass("active-switcher-btn");
                jQuery(".skins-btn").removeClass("active-switcher-btn");
                jQuery("body").removeClass("dark");
                jQuery("body").addClass("light");
                var newUrl = $("#skinColor").attr("href").replace(skinColor,"light");
                jQuery('#skinColor').attr("href", newUrl);
                skinColor = "light"; //keep the current one
            });


            //Boxed Layout
            jQuery('.boxed-layout-btn').click(function(){
                jQuery(this).addClass("active-switcher-btn");
                jQuery(".wide-layout-btn").removeClass("active-switcher-btn");
                jQuery("body").addClass("boxed-layout container");
                fullWide = "N";
            });

            jQuery('.wide-layout-btn').click(function(){
                jQuery(this).addClass("active-switcher-btn");
                jQuery(".boxed-layout-btn").removeClass("active-switcher-btn");
                jQuery("body").removeClass("boxed-layout container");
                fullWide = "Y";
            });
            //Select Template
            jQuery('#select-site-template').click(function(){
                selectTemplate = $(this).attr("data-src")+"&templateColorCode="+templateColorCode+"&skinColor="+skinColor+"&fullWide="+fullWide;
                window.location.href=selectTemplate;
            });


        }); // jQuery End

    </script>

    <link rel="stylesheet" href="/themes/m3x/plugins/style-switcher/style-switcher.css">
    <i class="style-switcher-btn fa fa-cogs hidden-xs"></i>
    <div class="style-switcher animated fadeInRight">
        <div class="style-swticher-header">
            <div class="style-switcher-heading">Tùy Chỉnh Định Dạng</div>
            <div class="theme-close"><i class="icon-close"></i></div>
        </div>

        <div class="style-swticher-body">
            <!-- Theme Colors -->
            <div class="style-switcher-heading">Màu Chủ Đạo</div>
            <ul class="list-unstyled">
                <li class="theme-green <c:if test="${template.templateColorCode == 'green'}">theme-active</c:if>" data-style="green" data-header="light"></li>
                <li class="theme-blue <c:if test="${template.templateColorCode == 'blue'}">theme-active</c:if>" data-style="blue" data-header="light"></li>
                <li class="theme-orange <c:if test="${template.templateColorCode == 'orange'}">theme-active</c:if>" data-style="orange" data-header="light"></li>
                <li class="theme-red <c:if test="${template.templateColorCode == 'red'}">theme-active</c:if>" data-style="red" data-header="light"></li>
                <li class="theme-light <c:if test="${template.templateColorCode == 'light'}">theme-active</c:if>" data-style="light" data-header="light"></li>
                <li class="theme-purple <c:if test="${template.templateColorCode == 'purple'}">theme-active</c:if>" data-style="purple" data-header="light"></li>
                <li class="theme-aqua <c:if test="${template.templateColorCode == 'aqua'}">theme-active</c:if>" data-style="aqua" data-header="light"></li>
                <li class="theme-brown <c:if test="${template.templateColorCode == 'brown'}">theme-active</c:if>" data-style="brown" data-header="light"></li>
                <li class="theme-dark-blue <c:if test="${template.templateColorCode == 'dark-blue'}">theme-active</c:if>" data-style="dark-blue" data-header="light"></li>
                <li class="theme-light-green <c:if test="${template.templateColorCode == 'light-green'}">theme-active</c:if>" data-style="light-green" data-header="light"></li>
                <li class="theme-dark-red <c:if test="${template.templateColorCode == 'dark-red'}">theme-active</c:if>" data-style="dark-red" data-header="light"></li>
                <li class="theme-teal <c:if test="${template.templateColorCode == 'teal'}">theme-active</c:if>" data-style="teal" data-header="light"></li>
            </ul>

            <!-- Theme Skins -->
            <div class="style-switcher-heading">Màu Nền</div>
            <div class="row no-col-space margin-bottom-20 skins-section">
                <div class="col-xs-6">
                    <button data-skins="light" class="btn-u btn-u-xs btn-u-dark btn-block ${skinLight} handle-skins-btn">Sáng</button>
                </div>
                <div class="col-xs-6">
                    <button data-skins="dark" class="btn-u btn-u-xs btn-u-dark btn-block ${skinDark} skins-btn">Tối</button>
                </div>
            </div>

            <hr>

            <!-- Layout Styles -->
            <div class="style-switcher-heading">Rộng Toàn Màn Hình</div>
            <div class="row no-col-space margin-bottom-20">
                <div class="col-xs-6">
                    <a href="javascript:void(0);" class="btn-u btn-u-xs btn-u-dark btn-block ${fullWide} wide-layout-btn">Có</a>
                </div>
                <div class="col-xs-6">
                    <a href="javascript:void(0);" class="btn-u btn-u-xs btn-u-dark btn-block ${box} boxed-layout-btn">Không</a>
                </div>
            </div>

            <hr>

            <!-- Theme Type -->
            <%--<div class="style-switcher-heading">Theme Types and Versions</div>--%>

            <spring:eval expression="serviceLocator.siteDao.getTemplateFromSite(site)" var="selectedTemplate"/>
            <spring:eval expression="systemContext.getGlobalConfig('main.site.url')" var="mainSiteUrl"/>
            <c:if test="${!empty template}">
                <div class="row no-col-space margin-bottom-10">
                    <div class="col-xs-12">
                        <a id="select-site-template" href="#" data-src="${mainSiteUrl}/site/select-template.html?templateId=${selectedTemplate.id}" class="btn-u btn-u-red btn-block">Chọn Mẫu Này</a>
                    </div>
                </div>
            </c:if>

            <%--<div class="row no-col-space">--%>
                <%--<div class="col-xs-6">--%>
                    <%--<a href="Blog/index.html" class="btn-u btn-u-xs btn-u-dark btn-block">Blog <small class="dp-block">Template</small></a>--%>
                <%--</div>--%>
                <%--<div class="col-xs-6">--%>
                    <%--<a href="RTL/index.html" class="btn-u btn-u-xs btn-u-dark btn-block">RTL <small class="dp-block">Version</small></a>--%>
                <%--</div>--%>
            <%--</div>--%>
        </div>
    </div><!--/style-switcher-->

    <!--=== End Style Switcher ===-->
</c:if>
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<div class="wrapper">
<!--=== Header ===-->
<div class="header">
    <div class="container">
        <app:cache key="header_cuoi5s">
        <!-- Logo -->
        <a href="/" class="logo">
            <spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>
            <c:choose>
                <c:when test="${fn:startsWith(siteHeaderFooter.useLogoImg, 'Y')}">
                    <c:if test="${! empty siteHeaderFooter.crop}"><c:set value="op=crop|${siteHeaderFooter.crop}" var="opCrop"/></c:if>
                    <img id="logo-header" src="${imageServer}/get/${siteHeaderFooter.logoImg}.png?${opCrop}&op=scale|x70" alt="${site.name}">
                </c:when>
                <c:otherwise>
                    ${siteHeaderFooter.logoText}
                </c:otherwise>
            </c:choose>
        </a>
        <!-- End Logo -->
        </app:cache>
            <!-- Topbar -->
        <div class="topbar">
            <!-- Topbar Navigation -->
            <ul class="loginbar pull-right">
                <c:if test="${sessionObject != null}">
                    <spring:eval expression="sessionObject.getItemsInCart()" var="totalItemsInCart"/>
                    <li class=""><a href="/checkout/basket.html"><i class="fa fa-shopping-cart fa-2x"></i> (${totalItemsInCart})</a></li>
                    <%--<li class="topbar-devider"></li>--%>
                </c:if>
                <%--<li><a href="/admin/login.html"><fmt:message key="site.login"/></a></li>--%>
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
                                    <c:when test="${!empty siteHeaderFooter.footerNewsLinkDisplay}">
                                        <c:out value="${siteHeaderFooter.footerNewsLinkDisplay}"/>
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
                <div class="col-md-6">
                    <p>
                        <jsp:useBean id="now" class="java.util.Date" />
                        <fmt:formatDate var="year" value="${now}" pattern="yyyy" />
                        ${year} &copy; ${site.domain}. ALL Rights Reserved.
                        <%--<a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a>--%>
                    </p>
                </div>

                <!-- Social Links -->
                <div class="col-md-3">
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
                <div class="col-md-3">
                    <p>
                        <c:choose>
                            <c:when test="${site.parentSite.siteType == 3}">
                                <c:set var="ownerDomain" value="${site.parentSite.domain}"/>
                                <c:set var="ownerName" value="${site.parentSite.domain}"/>
                            </c:when>
                            <c:when test="${site.siteType == 3}">
                                <c:set var="ownerDomain" value="${site.domain}"/>
                                <c:set var="ownerName" value="${site.domain}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="ownerDomain" value="${site.parentSite.domain}"/>
                                <c:set var="ownerName" value="WebPhatTai."/>
                            </c:otherwise>
                        </c:choose>
                        Designed by <a href="http://${ownerDomain}">${ownerName}</a> | <a href="/admin/login.html"><fmt:message key='site.login.admin'/></a>
                    </p>
                </div>
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