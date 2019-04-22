<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<!DOCTYPE html>
<html lang="en">
<spring:eval expression="systemContext.getGlobalConfig('image.server')" var="imageServer"/>
<%--Store information--%>
<spring:eval expression="serviceLocator.getNailStoreDao().findAll(site.id)" var="stores"/>
<c:if test="${fn:length(stores) >= 1}">
    <c:set var="store" value="${stores[0]}"/>
</c:if>
<%--About us information--%>
<spring:eval expression="serviceLocator.menuDao.getMenu(site, '', 'Y')" var="homeMenu"/>
<spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(homeMenu.id, 'Y')" var="menuRows"/>
<c:forEach var="row" items="${menuRows}">
    <c:if test="${fn:contains(row.title, 'About Us')}">
        <c:set var="aboutUsRow" value="${row}"/>
        <spring:eval expression="serviceLocator.siteMenuPartContentDao.getContentParts(aboutUsRow.id, 'Y')" var="partContents"/>
        <c:if test="${fn:length(partContents) > 0}">
            <c:set var="aboutUsContent" value="${partContents[0]}"/>
        </c:if>
    </c:if>
</c:forEach>
<spring:eval expression="serviceLocator.getNailServiceDao().getGroupServices(store.id)" var="groups"/>
<spring:eval expression="serviceLocator.albumImageDao.findAlbumImages(0, 0, 100, 'id', site.id, true)" var="galleryImages"/>
<%--Logo--%>
<spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'LOGO_IMAGE', site.id)" var="logoImg"/>
<spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'LOGO_CROP', site.id)" var="logoCrop"/>
<c:if test="${!empty logoImg.value}">
    <c:choose>
        <c:when test="${! empty logoCrop.value}">
            <c:set value="${imageServer}/get/${logoImg.value}.png?op=crop|${logoCrop.value}&op=scale|x60" var="logoUrl"/>
        </c:when>
        <c:otherwise>
            <c:set value="${imageServer}/get/${logoImg.value}.png?op=scale|x60" var="logoUrl"/>
        </c:otherwise>
    </c:choose>
</c:if>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>${!empty store.name? store.name : 'Home'}</title>
    <meta name="description" content="${store.name} located in ${store.city}, ${store.state} ${store.zipCode}, is one of the best salons in this area. We offers premier nails care and spa treatment services to satisfy your needs for enhancing natural beauty and refreshing your day">
    <meta name="keywords" content="Manicure & Pedicure, nails, spa">
    <!-- META FOR FACEBOOK -->
    <meta content="${store.name}" property="og:site_name"/>
    <meta property="og:url" itemprop="url" content="http://${site.domain}"/>
    <meta property="og:image" itemprop="thumbnailUrl" content="${logoUrl}"/>
    <meta content="" itemprop="headline" property="og:title"/>
    <meta content="" itemprop="description" property="og:description"/>
    <!-- END META FOR FACEBOOK -->
    <meta name="robots" content="index,follow"/>
    <meta name="geo.placename" content="${store.city} ${store.zipCode}, ${store.country}"/>
    <meta name="geo.position" content=""/>
    <meta name="ICBM" content=""/>
    <!-- Twitter Card -->
    <meta name="twitter:card" value="summary">
    <meta name="twitter:url" content="http://${site.domain}">
    <meta name="twitter:title" content="">
    <meta name="twitter:description" content="">
    <meta name="twitter:image" content=""/>
    <!-- End Twitter Card -->
    <%--GOOGLE SEARCH LOCAL BUSINESS--%>
    <%--https://schema.org/NailSalon--%>
    <c:choose>
        <c:when test="${fn:contains(store.hourMon, '-')}">
            <c:set var="hours" value="${fn:split(store.hourMon, '-')}"/>
            <c:set var="hourMonStart" value="${hours[0]}"/>
            <c:set var="hourMonEnd" value="${hours[1]}"/>
        </c:when>
        <c:otherwise>
            <c:set var="hourMonStart" value="00:00"/>
            <c:set var="hourMonEnd" value="00:00"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(store.hourTue, '-')}">
            <c:set var="hours" value="${fn:split(store.hourTue, '-')}"/>
            <c:set var="hourTueStart" value="${hours[0]}"/>
            <c:set var="hourTueEnd" value="${hours[1]}"/>
        </c:when>
        <c:otherwise>
            <c:set var="hourTueStart" value="00:00"/>
            <c:set var="hourTueEnd" value="00:00"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(store.hourWed, '-')}">
            <c:set var="hours" value="${fn:split(store.hourWed, '-')}"/>
            <c:set var="hourWedStart" value="${hours[0]}"/>
            <c:set var="hourWedEnd" value="${hours[1]}"/>
        </c:when>
        <c:otherwise>
            <c:set var="hourWedStart" value="00:00"/>
            <c:set var="hourWedEnd" value="00:00"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(store.hourThu, '-')}">
            <c:set var="hours" value="${fn:split(store.hourThu, '-')}"/>
            <c:set var="hourThuStart" value="${hours[0]}"/>
            <c:set var="hourThuEnd" value="${hours[1]}"/>
        </c:when>
        <c:otherwise>
            <c:set var="hourThuStart" value="00:00"/>
            <c:set var="hourThuEnd" value="00:00"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(store.hourFri, '-')}">
            <c:set var="hours" value="${fn:split(store.hourFri, '-')}"/>
            <c:set var="hourFriStart" value="${hours[0]}"/>
            <c:set var="hourFriEnd" value="${hours[1]}"/>
        </c:when>
        <c:otherwise>
            <c:set var="hourFriStart" value="00:00"/>
            <c:set var="hourFriEnd" value="00:00"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(store.hourSat, '-')}">
            <c:set var="hours" value="${fn:split(store.hourSat, '-')}"/>
            <c:set var="hourSatStart" value="${hours[0]}"/>
            <c:set var="hourSatEnd" value="${hours[1]}"/>
        </c:when>
        <c:otherwise>
            <c:set var="hourSatStart" value="00:00"/>
            <c:set var="hourSatEnd" value="00:00"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(store.hourSun, '-')}">
            <c:set var="hours" value="${fn:split(store.hourSun, '-')}"/>
            <c:set var="hourSunStart" value="${hours[0]}"/>
            <c:set var="hourSunEnd" value="${hours[1]}"/>
        </c:when>
        <c:otherwise>
            <c:set var="hourSunStart" value="00:00"/>
            <c:set var="hourSunEnd" value="00:00"/>
        </c:otherwise>
    </c:choose>
    <script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "NailSalon",
  "image": [
    "${aboutUsContent.imgUrl}?op=scale|1",
    "${aboutUsContent.imgUrl}?op=scale|4",
    "${aboutUsContent.imgUrl}?op=scale|16"
   ],
  "@id": "${site.subDomain}",
  "name": "${store.name}",
  "address": {
    "@type": "PostalAddress",
    "streetAddress": "${store.address_1}",
    "addressLocality": "${store.city}",
    "addressRegion": "${store.state}",
    "postalCode": "${store.zipCode}",
    "addressCountry": "${store.country}"
  },
  "geo": {
    "@type": "GeoCoordinates",
    "latitude": ${store.latitude},
    "longitude": ${store.longitude}
  },
  "url": "${site.domain}",
  "telephone": "+1${store.phone}",
  "openingHoursSpecification": [
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Monday",
      "opens": "${hourMonStart}",
      "closes": "${hourMonEnd}"
    },
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Tuesday",
      "opens": "${hourTueStart}",
      "closes": "${hourTueEnd}"
    },
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Wednesday",
      "opens": "${hourWedStart}",
      "closes": "${hourWedEnd}"
    },
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Thursday",
      "opens": "${hourThuStart}",
      "closes": "${hourThuEnd}"
    },
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Friday",
      "opens": "${hourFriStart}",
      "closes": "${hourFriEnd}"
    },
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Saturday",
      "opens": "${hourSatStart}",
      "closes": "${hourSatEnd}"
    },
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Sunday",
      "opens": "${hourSunStart}",
      "closes": "${hourSunEnd}"
    }
  ]
}
</script>
    <%--END GOOGLE SEARCH LOCAL BUSINESS--%>
    <link href="${site.domain}" rel="canonical">
    <!-- Google Fonts -->
    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Open+Sans:300,400,600,700|Leckerli+One">

    <!-- CSS Global Compulsory -->
    <link rel="stylesheet" href="/themes/m4x1p/vendor/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/icon-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/icon-hs/style.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/icon-line/css/simple-line-icons.css">
    <link rel="stylesheet" href="/themes/m4x1p/css/bootstrap-select.css" />

    <!-- CSS Implementing Plugins -->
    <link rel="stylesheet" href="/themes/m4x1p/vendor/hamburgers/hamburgers.min.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/slick-carousel/slick/slick.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/animate.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/custombox/custombox.min.css">

    <!-- CSS Template -->
    <link rel="stylesheet" href="/themes/m4x1p/css/styles.op-spa.css">

    <!-- CSS Customization -->
    <link rel="stylesheet" href="/themes/m4x1p/css/custom.css">
    <link rel="stylesheet" href="/themes/m4x1p/css/red.css">
</head>

<body>
<h1 style="display: none">${store.name}</h1>
<main>
<!-- Header -->
<header id="js-header" class="u-header u-header--sticky-top u-header--change-appearance g-z-index-9999"
        data-header-fix-moment="100">
    <div class="light-theme u-header__section g-transition-0_3 g-py-6 g-py-14--md"
         data-header-fix-moment-exclude="light-theme g-py-14--md"
         data-header-fix-moment-classes="dark-theme u-shadow-v27 g-bg-white g-py-11--md">
        <nav class="navbar navbar-expand-lg g-py-0">
            <div class="container g-pos-rel">
                <!-- Logo -->
                <a href="/#" class="js-go-to navbar-brand u-header__logo"
                   data-type="static">
                    <img class="u-header__logo-img u-header__logo-img--main d-block g-height-45" src="${logoUrl}" alt="${store.name} logo"
                         data-header-fix-moment-exclude="d-block"
                         data-header-fix-moment-classes="d-none">

                    <img class="u-header__logo-img u-header__logo-img--main d-none g-height-45" src="${logoUrl}" alt="${store.name} logo"
                         data-header-fix-moment-exclude="d-none"
                         data-header-fix-moment-classes="d-block"> </a>
                <!-- End Logo -->

                <!-- Navigation -->
                <div class="collapse navbar-collapse align-items-center flex-sm-row" id="navBar" data-mobile-scroll-hide="true">
                    <ul class="js-scroll-nav navbar-nav text-uppercase g-font-weight-700 g-font-size-11 g-pt-20 g-pt-0--lg ml-auto">
                        <li class="nav-item g-mr-12--lg g-mb-7 g-mb-0--lg active">
                            <a href="#home" class="nav-link p-0">Home
                                <span class="sr-only">(current)</span>
                            </a>
                        </li>
                        <li class="nav-item g-mx-12--lg g-mb-7 g-mb-0--lg">
                            <a href="#about" class="nav-link p-0">About</a>
                        </li>
                        <c:if test="${!empty groups}">
                        <li class="nav-item g-mx-12--lg g-mb-7 g-mb-0--lg">
                            <a href="#services" class="nav-link p-0">Services</a>
                        </li>
                        </c:if>
                        <li class="nav-item g-mx-12--lg g-mb-7 g-mb-0--lg">
                            <a href="#booking" class="nav-link p-0">Make Appointment</a>
                        </li>
                        <c:if test="${!empty galleryImages}">
                            <li class="nav-item g-mx-12--lg g-mb-7 g-mb-0--lg">
                                <a href="#gallery" class="nav-link p-0">Gallery</a>
                            </li>
                        </c:if>
                        <li class="nav-item g-ml-12--lg">
                            <a href="#contact" class="nav-link p-0">Contact</a>
                        </li>
                    </ul>
                </div>
                <!-- End Navigation -->

                <!-- Responsive Toggle Button -->
                <button class="navbar-toggler btn g-line-height-1 g-brd-none g-pa-0 g-pos-abs g-top-20 g-right-0" type="button"
                        aria-label="Toggle navigation"
                        aria-expanded="false"
                        aria-controls="navBar"
                        data-toggle="collapse"
                        data-target="#navBar">
                <span class="hamburger hamburger--slider">
                  <span class="hamburger-box">
                    <span class="hamburger-inner"></span>
                  </span>
                </span>
                </button>
                <!-- End Responsive Toggle Button -->
            </div>
        </nav>
    </div>
</header>
<!-- End Header -->

<%--Home--%>
<%--<section id="home" class="u-bg-overlay g-height-100vh g-min-height-600 g-bg-img-hero g-bg-black-opacity-0_3--after" style="background-image: url(https://images.unsplash.com/photo-1519014816548-bf5fe059798b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80);">--%>
    <%--<div class="u-bg-overlay__inner g-absolute-centered--y w-100">--%>
        <%--<div class="container text-center g-max-width-750">--%>
            <%--<h1 class="g-line-height-1_5 g-font-weight-700 g-font-size-50 g-color-white g-mb-15">Our reputation is built on how we treat YOU</h1>--%>
            <%--<h2 class="g-font-weight-600 g-font-size-22 g-color-white">Discount 20% for $50 services until 4/15/2019</h2>--%>
            <%--<a class="btn btn-md text-uppercase u-btn-primary g-font-weight-700 g-font-size-11 g-brd-none rounded-0 g-py-10 g-px-25" href="/booking.html">Make Appointment</a>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</section>--%>
<spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows('', site, 'Y')" var="rows"/>
<c:if test="${! empty rows}">
    <c:forEach var="row" items="${rows}">
        <spring:eval expression="serviceLocator.getContentService().merge(row)" var="htmlContent"/>
        ${htmlContent}
    </c:forEach>
</c:if>

<%--Home End--%>

<%--About Us--%>
<%--<section id="about" class="g-pb-80">--%>
    <%--<div class="container-fluid px-0">--%>
        <%--<div class="row no-gutters">--%>
            <%--<div class="col-md-6 g-bg-img-hero g-min-height-400" style="background-image: url(https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80);"></div>--%>
            <%--<div class="col-md-6 d-flex align-items-center text-center g-pa-50">--%>
                <%--<div class="w-100">--%>
                    <%--<div class="g-mb-25">--%>
                        <%--<h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">About Us</h4>--%>
                        <%--<h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">Perfect place for your relaxation</h2>--%>
                    <%--</div>--%>

                    <%--<p class="g-mb-35">Welcome to [Nails Salon] where your comfort and safety are out top priority. Indulge yourself with our luxurious treatment from a full line of nails, skin, an spa care designed to pamper, rejuvenate and restore you body & mind. As a guest, you are entitled to the finest products and services available. Our innovative pipe-less chair and instrument are sterilized after every use to ensure your safety. Rest assure that you will be in good hand at [Nails Salon].</p>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--<div class="row no-gutters">--%>
            <%--<div class="col-md-6">--%>
                <%--<img class="img-fluid" src="https://images.unsplash.com/photo-1506668635606-caa9ef5ce079?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">--%>
            <%--</div>--%>

            <%--<div class="col-md-6">--%>
                <%--<div class="js-carousel"--%>
                     <%--data-infinite="true"--%>
                     <%--data-arrows-classes="u-arrow-v1 g-absolute-centered--y g-width-45 g-height-55 g-font-size-12 g-theme-color-gray-dark-v1 g-bg-white g-mt-minus-10"--%>
                     <%--data-arrow-left-classes="fa fa-chevron-left g-left-0"--%>
                     <%--data-arrow-right-classes="fa fa-chevron-right g-right-0">--%>
                    <%--<div class="js-slide">--%>
                        <%--<img class="img-fluid" src="https://images.unsplash.com/photo-1519014816548-bf5fe059798b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80" alt="Image description">--%>
                    <%--</div>--%>
                    <%--<div class="js-slide">--%>
                        <%--<img class="img-fluid" src="https://images.unsplash.com/photo-1459164648438-af647b154e96?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">--%>
                    <%--</div>--%>

                    <%--<div class="js-slide">--%>
                        <%--<img class="img-fluid" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</section>--%>
<%--About Us End--%>

<%--Services--%>
<c:if test="${!empty groups}">
    <c:if test="${fn:length(groups) == 1}"><c:set value="col-lg-12 g-mb-30" var="serviceClass"/><c:set value="2000" var="width"/> </c:if>
    <c:if test="${fn:length(groups) == 2}"><c:set value="col-lg-6 g-mb-30" var="serviceClass"/><c:set value="1000" var="width"/></c:if>
    <c:if test="${fn:length(groups) == 3}"><c:set value="col-lg-4 g-mb-30" var="serviceClass"/><c:set value="700" var="width"/></c:if>
    <c:if test="${fn:length(groups) >= 4}"><c:set value="col-lg-3 col-md-4 col-sm-6 g-mb-30" var="serviceClass"/><c:set value="600" var="width"/></c:if>

    <%--Groups and Services--%>
    <spring:eval expression="serviceLocator.menuDao.getMenu(site, 'services.html', 'Y')" var="servicesMenu"/>
    <spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(servicesMenu.id, 'Y')" var="menuRows"/>
    <c:forEach var="row" items="${menuRows}">
        <c:if test="${fn:contains(row.title, 'Services')}">
            <c:set var="servicesRow" value="${row}"/>
            <spring:eval expression="serviceLocator.siteMenuPartContentDao.getContentParts(servicesRow.id, 'Y')" var="partContents"/>
            <c:if test="${fn:length(partContents) > 0}">
                <c:set var="servicesContent" value="${partContents[0]}"/>
            </c:if>
        </c:if>
    </c:forEach>

    <section id="services" class="g-py-70">
    <div class="container text-center g-width-590 g-mb-50">
        <div class="g-mb-25">
            <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">Our Services</h4>
            <h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">${!empty servicesContent && !empty servicesContent.title? servicesContent.title : store.name}</h2>
        </div>
        <p class="mb-0">${!empty servicesContent && !empty servicesContent.content? servicesContent.content : ''}</p>
    </div>
    <div class="container-fluid px-0">
        <div class="row no-gutters">
            <c:forEach items="${groups}" var="group" varStatus="itemStatus">
            <spring:eval expression="serviceLocator.getNailServiceDao().getServices(group.id, store.id)" var="services"/>
            <div class="${serviceClass}">
                <!-- Article -->
                <article class="text-center u-block-hover u-block-hover__additional--jump g-bg-white g-color-gray-dark-v3">
                    <!-- Article Image -->
                    <c:if test="${!empty group.crop}"><c:set var="cropData" value="op=crop|${group.crop}"/></c:if>
                    <img class="w-100" src="${group.imageUrl}?${cropData}&op=scale|${width}" alt="${group.name}">
                    <!-- End Article Image -->

                    <!-- Article Content -->
                    <div class="g-py-40 g-px-30">
                        <h4 class="text-uppercase g-font-weight-900 g-color-black g-font-size-20 g-mb-15 text-left">${group.name}</h4>
                        <ul class="list-unstyled text-uppercase g-mb-30 text-left">
                            <c:forEach var="service" items="${services}">
                                <spring:eval
                                        expression="T(com.easysoft.ecommerce.util.Money).valueOf((service.price/100),site.siteParamsMap.get('CURRENCY'), site.siteParamsMap.get('CURRENCY_FORMAT')).getMoneyValue()"
                                        var="servicePrice"/>
                                <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">${service.name}<span class="text-right" style="float: right">$${servicePrice}</span></li>
                            </c:forEach>
                        </ul>
                    </div>
                    <!-- End Article Content -->
                </article>
                <!-- End Article -->
            </div>
            </c:forEach>
        </div>
    </div>
</section>
</c:if>
<%--Services End--%>

<%--<decorator:body/>--%>

<!-- Booking -->
<spring:eval expression="serviceLocator.getNailStoreDao().findActiveByOrder('active', 'Y', null, site.id)" var="stores"/>
<c:if test="${!empty stores && fn:length(stores) == 1}">
    <c:set var="store" value="${stores[0]}"/>
    <spring:eval expression="serviceLocator.getNailEmployeeDao().findAllByStore(store.id)" var="employees"/>
</c:if>

<section id="booking" class="g-py-70">
    <div class="container text-center g-width-590 g-mb-50">
        <div class="g-mb-25">
            <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">Making Appointment</h4>
            <%--<h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">Get in touch</h2>--%>
        </div>
        <p class="mb-0">Please fill out the information and submit your appointment.</p>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-12 g-mb-40 g-mb-0--md">
                <form id="form">
                    <c:if test="${!empty stores && fn:length(stores) == 1}">
                        <c:set var="store" value="${stores[0]}"/>
                        <input type="hidden" name="storeId" id="storeId" value="${store.id}"/>
                    </c:if>
                    <c:if test="${!empty stores && fn:length(stores) >= 2}">
                        <%--<div class="col-md-4 col-xs-12">--%>
                        <%--<div class="form-group">--%>
                        <%--<label for="storeId">Location</label>--%>
                        <%--<select name="storeId" class="form-control" id="storeId">--%>
                        <%--<option value="">Select Location</option>--%>
                        <%--<c:forEach items="${stores}" var="store">--%>
                        <%--<option value="${store.id}">${store.name}: ${store.address_1} ${store.city}, ${store.state} ${store.zipCode}</option>--%>
                        <%--</c:forEach>--%>
                        <%--</select>--%>
                        <%--</div>--%>
                        <%--</div>--%>
                    </c:if>
                    <div class="form-group row g-mb-50">
                        <div class="col-md-4">
                            <label for="date">Select Date</label>
                            <input  type="date" placeholder="Select Date" name="date" id="date" class="form-control h-60 g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-pa-12">
                        </div>
                        <div class="col-md-4">
                            <label for="employeeId">Select Technician</label>
                            <select name="employeeId"  id="employeeId" class="form-control h-60 g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-pa-12">
                                <option value="0">Any Technicians</option>
                                <c:forEach items="${employees}" var="employee">
                                    <option value="${employee.id}">${employee.firstName} ${employee.lastName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="serviceId">Select Service</label>
                            <select name="serviceId" id="serviceId" multiple="multiple" class="form-control h-60 g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-py-5">
                                <c:forEach items="${groups}" var="group">
                                    <optgroup label="${group.name}">
                                        <!--List service-->
                                        <spring:eval expression="serviceLocator.getNailServiceDao().getServices(group.id, store.id)" var="services"/>
                                        <c:forEach items="${services}" var="service">
                                            <option value="${service.id}">${service.name}</option>
                                        </c:forEach>
                                    </optgroup>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn-md text-uppercase btn-block u-btn-outline-primary g-font-weight-700 g-font-size-11 g-brd-2 rounded-0 g-py-19 g-px-20" type="button" role="button" id="search-timeslot-button">Search Time Slots</button>
                </form>
            </div>
            <div class="col-md-12 g-my-30" id="display-timeslot-result"></div>
            <%--<h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0" id="display-timeslot-result"></h2>--%>
        </div>
    </div>
</section>

<div class="modal fade" id="responsive-booking" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel4">Make Appointment</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="container">
                    <div  id="modal_message_alert"></div>
                    <form class="margin-bottom-40" role="form" id="formModal">
                        <input type="hidden" name="selectedStoreId" id="selectedStoreId" value=""/>
                        <input type="hidden" name="selectedDate" id="selectedDate" value=""/>
                        <input type="hidden" name="selectedTime" id="selectedTime" value=""/>
                        <input type="hidden" name="selectedServiceId" id="selectedServiceId" value=""/>
                        <input type="hidden" name="selectedEmployeeId" id="selectedEmployeeId" value=""/>
                        <div class="form-group">
                            <label for="firstName"><fmt:message key="site.register.firstName"/></label>
                            <input type="text" class="form-control" name="firstName" id="firstName" value="" placeholder="<fmt:message key="site.register.firstName"/>"/>
                        </div>
                        <div class="form-group">
                            <label for="lastName"><fmt:message key="site.register.lastName"/></label>
                            <input type="text" class="form-control" name="lastName" id="lastName" value="" placeholder="<fmt:message key="site.register.lastName"/>"/>
                        </div>
                        <div class="form-group">
                            <label for="phone"><fmt:message key="site.register.phone"/></label>
                            <input type="text" class="form-control" name="phone" id="phone" value="" placeholder="(xxx) xxx-xxxx"/>
                        </div>
                        <div class="form-group">
                            <label for="email"><fmt:message key="site.register.email"/></label>
                            <input type="text" class="form-control" name="email" id="email" value="" placeholder="<fmt:message key="site.register.email"/>"/>
                        </div>
                        <div class="form-group">
                            <label for="message"><fmt:message key="booking.message.content"/></label>
                            <textarea class="form-control" id="message" name="message" rows="2" placeholder="<fmt:message key="booking.message.content"/>" maxlength="999"></textarea>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <%--<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>--%>
                <button type="button" id="submit-appointment" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<%--Booking End--%>

<%--Gallery--%>
<c:if test="${!empty galleryImages}">
    <spring:eval expression="serviceLocator.menuDao.getMenu(site, 'gallery.html', 'Y')" var="galleryMenu"/>
    <spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(galleryMenu.id, 'Y')" var="galleryMenuRows"/>
    <c:forEach var="row" items="${galleryMenuRows}">
        <c:if test="${fn:contains(row.title, 'Gallery')}">
            <c:set var="galleryRow" value="${row}"/>
            <spring:eval expression="serviceLocator.siteMenuPartContentDao.getContentParts(galleryRow.id, 'Y')" var="galleryPartContents"/>
            <c:if test="${fn:length(galleryPartContents) > 0}">
                <c:set var="galleryContent" value="${galleryPartContents[0]}"/>
            </c:if>
        </c:if>
    </c:forEach>
    <section id="gallery" class="g-theme-bg-gray-light-v1 g-py-80">
        <div class="container text-center g-width-590 g-mb-50">
            <div class="g-mb-25">
                <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">Gallery</h4>
                <c:if test="${!empty galleryContent && !empty galleryContent.title}">
                    <h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">${galleryContent.title}</h2>
                </c:if>
            </div>
            <c:if test="${!empty galleryContent && !empty galleryContent.content}">
                <p class="mb-0">${galleryContent.content}</p>
            </c:if>
        </div>
        <c:if test="${!empty galleryImages}">
            <div class="container-fluid px-0">
                <div class="row no-gutters">
                    <c:forEach var="image" items="${galleryImages}" end="1">
                        <c:if test="${!empty image.crop}">
                            <c:set var="cropImg" value="op=crop|${image.crop}"/>
                        </c:if>
                        <div class="col-md-6 g-bg-img-hero g-min-height-400" style="background-image: url(${imageServer}/get/${image.uri}.jpg?${cropImg}&op=scale|1200);"></div>
                        <%--<div class="col-md-6 g-bg-img-hero g-min-height-400" style="background-image: url(https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80);"></div>--%>
                    </c:forEach>
                </div>

                <div class="row no-gutters">
                    <div class="col-md-6">
                        <c:forEach var="image" items="${galleryImages}" begin="2" end="2">
                            <c:if test="${!empty image.crop}">
                                <c:set var="cropImg" value="op=crop|${image.crop}"/>
                            </c:if>
                            <img class="img-fluid" src="${imageServer}/get/${image.uri}.jpg?${cropImg}&op=scale|1200" alt="Gallery Images">
                        </c:forEach>
                    </div>

                    <div class="col-md-6">
                        <div class="js-carousel"
                             data-infinite="true"
                             data-arrows-classes="u-arrow-v1 g-absolute-centered--y g-width-45 g-height-55 g-font-size-12 g-theme-color-gray-dark-v1 g-bg-white g-mt-minus-10"
                             data-arrow-left-classes="fa fa-chevron-left g-left-0"
                             data-arrow-right-classes="fa fa-chevron-right g-right-0">
                            <c:forEach var="image" items="${galleryImages}" begin="3">
                                <c:if test="${!empty image.crop}">
                                    <c:set var="cropImg" value="op=crop|${image.crop}"/>
                                </c:if>
                                <div class="js-slide">
                                    <img class="img-fluid" src="${imageServer}/get/${image.uri}.jpg?${cropImg}&op=scale|1200" alt="Gallery Images">
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </section>
</c:if>

<!-- Section Content -->
<section id="contact" class="g-py-70">
    <div class="container text-center g-width-590 g-mb-50">
        <div class="g-mb-25">
            <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">Contact us</h4>
            <h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">Get in touch</h2>
        </div>
        <%--<p class="mb-0">Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo.</p>--%>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-6 g-mb-40 g-mb-0--md">
                <div  id="contact_message_alert"></div>
                <form method="post" id="contactForm">
                    <input type="hidden" name="storeId" value="${store.id}">
                    <div class="form-group g-mb-25">
                        <input class="form-control h-100 g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-pa-12" type="text" name="firstName" placeholder="Your name *">
                    </div>
                    <div class="form-group g-mb-25">
                        <input class="form-control h-100 g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-pa-12" type="text" name="sendersEmail" placeholder="Your email *">
                    </div>
                    <div class="form-group g-mb-25">
                        <textarea class="form-control g-resize-none g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-py-6 g-px-12" rows="4" name="message" placeholder="Message *"></textarea>
                    </div>
                    <button class="btn btn-md text-uppercase btn-block u-btn-outline-primary g-font-weight-700 g-font-size-11 g-brd-2 rounded-0 g-py-19 g-px-20" id="submit_contactus" type="button" role="button">Submit</button>
                </form>
            </div>

            <div class="col-md-3 text-center text-md-left g-mb-40--md">
                <div class="g-mb-25">
                <span class="u-icon-v1 u-icon-size--xs g-color-primary">
                  <i class="icon-directions"></i>
                </span>
                    <em class="d-block g-color-black-light-v2 text-uppercase g-font-size-12 g-font-style-normal g-mb-5">Address</em>
                    <strong class="d-block">${store.address_1}</strong>
                    <strong class="d-block">${store.city}, ${store.state} ${store.zipCode}</strong>
                </div>

                <div class="g-mb-25">
                <span class="u-icon-v1 u-icon-size--xs g-color-primary">
                  <i class="icon-call-in"></i>
                </span>
                    <em class="d-block g-color-black-light-v2 text-uppercase g-font-size-12 g-font-style-normal g-mb-5">Phone Number</em>
                    <strong class="d-block">(${fn:substring(store.phone, 0, 3)}) ${fn:substring(store.phone, 3, 6)}-${fn:substring(store.phone, 6, fn:length(store.phone))}</strong>
                </div>

                <div>
                <span class="u-icon-v1 u-icon-size--xs g-color-primary">
                  <i class="icon-envelope-letter"></i>
                </span>
                    <em class="d-block g-color-black-light-v2 text-uppercase g-font-size-12 g-font-style-normal g-mb-5">Email</em>
                    <strong class="d-block"><a href="#" class="g-color-main">${store.email}</a></strong>
                </div>
            </div>
            <div class="col-md-3 text-center text-md-left g-mb-40--md">
                <span class="u-icon-v1 u-icon-size--xs g-color-primary">
                  <i class="icon-clock"></i>
                </span>
                <div class="media-body text-left">
                    <span class="d-block g-font-weight-500 g-font-size-default text-uppercase mb-2">Business Hours</span>
                    <c:choose>
                        <c:when test="${fn:contains(store.hourMon, '-')}">
                            <c:set var="hours" value="${fn:split(store.hourMon, '-')}"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[0], 'hh:mm')" var="start"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[1], 'hh:mm')" var="end"/>
                            <strong class="d-block">Mon:<span style="float: right">${start}-${end}</span></strong>
                        </c:when>
                        <c:otherwise>
                            <strong class="d-block">Mon: <span style="float: right">Closed</span></strong>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${fn:contains(store.hourTue, '-')}">
                            <c:set var="hours" value="${fn:split(store.hourTue, '-')}"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[0], 'hh:mm')" var="start"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[1], 'hh:mm')" var="end"/>
                            <strong class="d-block">Tue:<span style="float: right">${start}-${end}</span></strong>
                        </c:when>
                        <c:otherwise>
                            <strong class="d-block">Tue: <span style="float: right">Closed</span></strong>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${fn:contains(store.hourWed, '-')}">
                            <c:set var="hours" value="${fn:split(store.hourWed, '-')}"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[0], 'hh:mm')" var="start"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[1], 'hh:mm')" var="end"/>
                            <strong class="d-block">Wed:<span style="float: right">${start}-${end}</span></strong>
                        </c:when>
                        <c:otherwise>
                            <strong class="d-block">Wed: <span style="float: right">Closed</span></strong>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${fn:contains(store.hourThu, '-')}">
                            <c:set var="hours" value="${fn:split(store.hourThu, '-')}"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[0], 'hh:mm')" var="start"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[1], 'hh:mm')" var="end"/>
                            <strong class="d-block">Thu:<span style="float: right">${start}-${end}</span></strong>
                        </c:when>
                        <c:otherwise>
                            <strong class="d-block">Thu: <span style="float: right">Closed</span></strong>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${fn:contains(store.hourFri, '-')}">
                            <c:set var="hours" value="${fn:split(store.hourFri, '-')}"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[0], 'hh:mm')" var="start"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[1], 'hh:mm')" var="end"/>
                            <strong class="d-block">Fri:<span style="float: right">${start}-${end}</span></strong>
                        </c:when>
                        <c:otherwise>
                            <strong class="d-block">Fri: <span style="float: right">Closed</span></strong>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${fn:contains(store.hourSat, '-')}">
                            <c:set var="hours" value="${fn:split(store.hourSat, '-')}"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[0], 'hh:mm')" var="start"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[1], 'hh:mm')" var="end"/>
                            <strong class="d-block">Sat:<span style="float: right">${start}-${end}</span></strong>
                        </c:when>
                        <c:otherwise>
                            <strong class="d-block">Sat: <span style="float: right">Closed</span></strong>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${fn:contains(store.hourSun, '-')}">
                            <c:set var="hours" value="${fn:split(store.hourSun, '-')}"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[0], 'hh:mm')" var="start"/>
                            <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).convertHours2AMPM(hours[1], 'hh:mm')" var="end"/>
                            <strong class="d-block">Sun:<span style="float: right">${start}-${end}</span></strong>
                        </c:when>
                        <c:otherwise>
                            <strong class="d-block">Sun: <span style="float: right">Closed</span></strong>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- End Contact -->

<!-- Footer -->
<footer>
    <c:set var="address" value="${store.name}, ${store.address_1},${store.city},${store.state} ${store.zipCode}"/>
    <spring:eval expression="T(java.net.URLEncoder).encode(address)" var="encodeAddress"/>
    <%--<iframe  width="100%" height="500" frameborder="0" style="border:0" allowfullscreen src="https://maps.google.com/maps?width=700&amp;height=440&amp;hl=en&amp;q=3208%20Lockridge%20St%2C%20Ann%20Arbor%20MI%2048108+(Title)&amp;ie=UTF8&amp;t=&amp;z=12&amp;iwloc=B&amp;output=embed" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>--%>
    <iframe  width="100%" height="500" frameborder="0" style="border:0" allowfullscreen src="https://maps.google.com/maps?width=700&amp;height=440&amp;hl=en&amp;q=${encodeAddress}&amp;ie=UTF8&amp;t=&amp;z=14&amp;iwloc=B&amp;output=embed" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>
    <!-- End Google Map -->

    <div class="container-fluid text-center g-color-gray-dark-v5 g-py-40">
        <a class="d-inline-block g-mb-25" href="/"> <img src="${logoUrl}" alt="${store.name} logo"> </a>
        <p class="g-mb-1"></p>

        <ul class="list-inline d-inline-block g-mb-30">
            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'TWITTER_FAN_PAGE', site.id)" var="twitter"/>
            <c:if test="${!empty twitter.value}">
                <li class="list-inline-item g-mr-10">
                    <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-white--hover g-bg-twitter g-color-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="${twitter.value}"><i class="fa fa-twitter"></i></a>
                </li>
            </c:if>
            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'PINTEREST_FAN_PAGE', site.id)" var="pinterest"/>
            <c:if test="${!empty pinterest.value}">
                <li class="list-inline-item g-mr-10">
                    <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-white--hover g-bg-pinterest g-color-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="${pinterest.value}"><i class="fa fa-pinterest"></i></a>
                </li>
            </c:if>
            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'FACEBOOK_FAN_PAGE', site.id)" var="facebook"/>
            <c:if test="${!empty facebook.value}">
                <li class="list-inline-item g-mr-10">
                    <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-white--hover g-bg-facebook g-color-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="${facebook.value}"><i class="fa fa-facebook"></i></a>
                </li>
            </c:if>
            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'INSTAGRAM_FAN_PAGE', site.id)" var="instagram"/>
            <c:if test="${!empty instagram.value}">
                <li class="list-inline-item g-mr-10">
                    <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-white--hover g-bg-instagram g-color-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="${instagram.value}"><i class="fa fa-instagram"></i></a>
                </li>
            </c:if>
            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'YOUTUBE_FAN_PAGE', site.id)" var="youtube"/>
            <c:if test="${!empty youtube.value}">
                <li class="list-inline-item g-mr-10">
                    <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-white--hover g-bg-youtube g-color-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="${youtube.value}"><i class="fa fa-youtube"></i></a>
                </li>
            </c:if>
            <%--<li class="list-inline-item g-mr-10">--%>
                <%--<a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-white--hover g-bg-google-plus g-color-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="#"><i class="fa fa-google-plus"></i></a>--%>
            <%--</li>--%>
            <spring:eval expression="serviceLocator.getSiteParamDao().findUniqueBy('key', 'YELP_FAN_PAGE', site.id)" var="yelp"/>
            <c:if test="${!empty yelp.value}">
                <li class="list-inline-item g-mr-10">
                    <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-white--hover g-bg-yelp g-color-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="${yelp.value}"><i class="fa fa-yelp"></i></a>
                </li>
            </c:if>
        </ul>

        <ul class="js-scroll-nav list-inline text-uppercase g-font-weight-700 g-font-size-11 g-mb-30">
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#home">Home</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#about">About</a>
            </li>
            <c:if test="${!empty groups}">
                <li class="list-inline-item g-px-12--md">
                    <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#services">Services</a>
                </li>
            </c:if>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#booking">Make Appointment</a>
            </li>
            <c:if test="${!empty galleryImages}">
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#gallery">Gallery</a>
            </li>
            </c:if>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#contact">Contact</a>
            </li>
        </ul>

        <div class="">
            <%--<small class="d-block g-font-size-default g-mr-10 g-mb-10 g-mb-0--md">2019 © All Rights Reserved.</small>--%>
            <ul class="u-list-inline">
                <li class="list-inline-item">
                    Design by <a class="" href="http://webphattai.com">WebPhatTai.com</a>
                </li>
            </ul>
        </div>
    </div>
</footer>
<!-- End Footer -->

<a class="js-go-to u-go-to-v1" href="#"
   data-type="fixed"
   data-position='{
           "bottom": 15,
           "right": 15
         }'
   data-offset-top="400"
   data-compensation="#js-header"
   data-show-effect="zoomIn"> <i class="hs-icon hs-icon-arrow-top"></i> </a>
</main>
<!-- JS Global Compulsory -->
<script src="/themes/m4x1p/vendor/jquery/jquery.min.js"></script>
<script src="/themes/m4x1p/vendor/jquery-migrate/jquery-migrate.min.js"></script>
<script src="/themes/m4x1p/vendor/popper.js/popper.min.js"></script>
<script src="/themes/m4x1p/vendor/bootstrap/bootstrap.min.js"></script>

<!-- JS Implementing Plugins -->
<script src="/themes/m4x1p/vendor/appear.js"></script>
<script src="/themes/m4x1p/vendor/slick-carousel/slick/slick.js"></script>
<%--<script src="/themes/m4x1p/vendor/gmaps/gmaps.min.js"></script>--%>
<script src="/themes/m3x/plugins/sky-forms/version-2.0.1/js/jquery.validate.min.js"></script>
<!-- JS Implementing Plugins -->
<script  src="/themes/m4x1p/vendor/custombox/custombox.min.js"></script>
<!-- JS Plugins Init. -->

<!-- JS Unify -->
<script src="/themes/m4x1p/js/hs.core.js"></script>
<script src="/themes/m4x1p/js/components/hs.header.js"></script>
<script src="/themes/m4x1p/js/helpers/hs.hamburgers.js"></script>
<script src="/themes/m4x1p/js/components/hs.scroll-nav.js"></script>
<script src="/themes/m4x1p/js/components/hs.rating.js"></script>
<script src="/themes/m4x1p/js/components/hs.carousel.js"></script>
<script src="/themes/m4x1p/js/components/gmap/hs.map.js"></script>
<script src="/themes/m4x1p/js/components/hs.go-to.js"></script>
<script src="/themes/m4x1p/js/components/hs.modal-window.js"></script>
<script src="/themes/m4x1p/js/bootstrap-select.min.js"></script>

<!-- JS Customization -->
<script src="/themes/m4x1p/js/custom.js"></script>

<!-- JS Plugins Init. -->
<script>
    $(document).on('ready', function () {
        // initialization of carousel
        $.HSCore.components.HSCarousel.init('.js-carousel');

        // initialization of header
        $.HSCore.components.HSHeader.init($('#js-header'));
        $.HSCore.helpers.HSHamburgers.init('.hamburger');

        // initialization of go to section
        $.HSCore.components.HSGoTo.init('.js-go-to');
    });

    $(window).on('load', function () {
        // initialization of HSScrollNav
        $.HSCore.components.HSScrollNav.init($('.js-scroll-nav'), {
            duration: 700
        });
    });
</script>

<script>
    function loadTimeslots(storeId, serviceId, employeeId, date) {
        var currentTime = new Date().getTime()
        var url = "/load-timeslots.json?storeId="+storeId+"&serviceId="+serviceId+"&employeeId="+employeeId+"&date="+date+"&currentDate="+currentTime;
        <%--url = url+"?start="+start+ "&catId=" + '${id}';--%>
        div = $("<div/>");
        div.append("<div class='u-heading-v3-1 g-mb-40'><h3 class='u-heading-v3__title g-brd-primary'>Select Time Slot</h3></div>")
        $.getJSON( url, function( data ) {
            div1 = $("<div class='brand-page margin-bottom-40'/>");
            morning = data['morning']
            afternoon = data['afternoon']
            evening = data['evening']
            //morning
            if (morning && morning.length > 0) {
                div11 = $("<div class='row'/>");
                div11.append("<div class='col-md-2'><h4>Morning</h4></div>")
                divTimeSlot = $("<div class='col-md-10'/>");
                for(var i in morning) {
                    divTimeSlot.append("<button class='btn btn-md u-btn-outline-primary g-mr-10 g-mb-15 open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+morning[i]+"'>"+morning[i]+"</button>")
                    div11.append(divTimeSlot)
                }
                div1.append(div11)
            }
            //afternoon
            if (afternoon && afternoon.length > 0) {
                div11 = $("<div class='row'/>");
                div11.append("<div class='col-md-2'><h4>Afternoon</h4></div>")
                divTimeSlot = $("<div class='col-md-10'/>");
                for(var i in afternoon) {
                    divTimeSlot.append("<button class='btn btn-md u-btn-outline-primary g-mr-10 g-mb-15 open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+afternoon[i]+"'>"+afternoon[i]+"</button>")
                    div11.append(divTimeSlot)
                }
                div1.append(div11)
            }
            //evening
            if (evening && evening.length > 0) {
                div11 = $("<div class='row'/>");
                div11.append("<div class='col-md-2'><h4>Evening</h4></div>")
                divTimeSlot = $("<div class='col-md-10'/>");
                for(var i in evening) {
                    divTimeSlot.append("<button class='btn btn-md u-btn-outline-primary g-mr-10 g-mb-15 open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+evening[i]+"'>"+evening[i]+"</button>")
                    div11.append(divTimeSlot)
                }
                div1.append(div11)
            }

            div.append(div1)
        });
        $('#display-timeslot-result').html(div);

    }

    $(function () {
        // Initial date field.
        var now = new Date();
        var day = ("0" + now.getDate()).slice(-2);
        var month = ("0" + (now.getMonth() + 1)).slice(-2);
        var today = now.getFullYear()+"-"+(month)+"-"+(day) ;
        $("input[name='date']").val(today);

        $("#form").validate({
            rules:{
                storeId:"required",
                date: "required",
                serviceId:"required"
            },
            messages:{
                storeId:"<fmt:message key="booking.select.location"/>",
                date:"<fmt:message key="booking.select.date"/>",
                serviceId:"<fmt:message key="booking.select.service"/>"
            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });
        $("#formModal").validate({
            rules:{
                firstName:"required",
                lastName: "required",
                phone:"required",
                email:{
                    //required:true,
                    email:true
                }
            },
            messages:{
                firstName:"<fmt:message key="booking.enter.firstName"/>",
                lastName:"<fmt:message key="booking.enter.lastName"/>",
                phone:"<fmt:message key="booking.enter.phone"/>",
                email:{
                    email:"<fmt:message key="booking.enter.email.invalid"/>"
                }
            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });

//        $('#form input[name="siteCode"]').blur(function () {
//            $("p.help-block b").text($(this).attr("value"));
//        });

        $('#search-timeslot-button').click(function() {
            if ($("#form").valid()) {
                var storeId = $("#storeId").val()
                var serviceId = $("#serviceId").val()
                var employeeId = $("#employeeId").val()
                var date = $("#date").val()
                loadTimeslots(storeId, serviceId, employeeId, date)
            }
        });

        $('#phone').usPhoneFormat({
            format: '(xxx) xxx-xxxx'
        });

//        $('#serviceId').multiselect();
        $('#serviceId').selectpicker();

        $("body").on("click", "button.open-booking-modal", function () {
            $('#modal_message_alert').html("");
            var storeId = $("#storeId").val()
            var serviceId = $("#serviceId").val()
            var employeeId = $("#employeeId").val()
            var date = $("#date").val()
            var object = $(this);
            $("input[name='selectedStoreId']").val(storeId)
            $("input[name='selectedServiceId']").val(serviceId)
            $("input[name='selectedEmployeeId']").val(employeeId)
            $("input[name='selectedDate']").val(date)
            $("input[name='selectedTime']").val(object.val())
        });

        $('#submit-appointment').click(function() {
            if ($("#formModal").valid()) {
                var storeId = $("#selectedStoreId").val()
                var serviceId = $("#selectedServiceId").val()
                var employeeId = $("#selectedEmployeeId").val()
                var date = $("#selectedDate").val()
                var time = $("#selectedTime").val()
                var formData = $("#formModal").serialize();
                $.ajax({
                    type: "POST",
                    url: "/submit_appointment.html",
                    data: formData,
                    success: function(result){
                        $('#modal_message_alert').html(result);
                    },
                    error: function (result) {
                        $('#modal_message_alert').html(result);
                    }
                });
            }
        });

        $("#contactForm").validate({
            rules:{
                firstName:"required",
                sendersEmail:{
                    required:true,
                    email:true
                },
                message:"required"
            },
            messages:{
                firstName:"<fmt:message key="site.contact.name"/>",
                sendersEmail:{
                    required:"<fmt:message key="site.register.emailisrequired"/>",
                    email:"<fmt:message key="site.register.emailisinvalid"/>"
                },
                message:"<fmt:message key="common.required"/>"
            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });

        $('#submit_contactus').click(function() {
            if ($("#contactForm").valid()) {
                var formData = $("#contactForm").serialize();
                $.ajax({
                    type: "POST",
                    url: "/ajax/contact-us.html",
                    data: formData,
                    success: function(result){
                        $('#contact_message_alert').html(result);
                    },
                    error: function (result) {
                        $('#contact_message_alert').html(result);
                    }
                });
            }
        });

    });

</script>

</body>
</html>