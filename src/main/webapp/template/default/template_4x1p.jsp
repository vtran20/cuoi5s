<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<!DOCTYPE html>
<html lang="en">
<%--Store information--%>
<spring:eval expression="serviceLocator.getNailStoreDao().findAll(site.id)" var="stores"/>
<c:if test="${fn:length(stores) >= 1}">
    <c:set var="store" value="${stores[0]}"/>
</c:if>
<%--About us information--%>
<spring:eval expression="serviceLocator.menuDao.getRootMenus(site, 'N', 'N')" var="menus"/>
<c:forEach items="${menus}" var="menu">
    <c:if test="${fn:contains(menu.name, 'Home')}">
        <c:set var="homeMenu" value="${menu}"/>
        <spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getMenuRows(menu.id, 'Y')" var="menuRows"/>
        <c:forEach var="row" items="${menuRows}">
            <c:if test="${fn:contains(row.title, 'About Us')}">
                <c:set var="aboutUsRow" value="${row}"/>
                <spring:eval expression="serviceLocator.siteMenuPartContentDao.getContentParts(aboutUsRow.id, 'Y')" var="partContents"/>
                <c:if test="${fn:length(partContents) > 0}">
                    <c:set var="aboutUsContent" value="${partContents[0]}"/>
                </c:if>
            </c:if>
        </c:forEach>
    </c:if>
</c:forEach>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>${!empty store.name? store.name : 'Home'}</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <!-- META FOR FACEBOOK -->
    <meta content="${store.name}" property="og:site_name"/>
    <meta property="og:url" itemprop="url" content="http://${site.domain}"/>
    <meta property="og:image" itemprop="thumbnailUrl" content="logo"/>
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
    <%--GOOGLE SEARCH META GOOGLE SEARCH STRUCTURED DATA FOR ARTICLE && GOOGLE BREADCRUMB STRUCTURED DATA--%>

    <%--END GOOGLE SEARCH META GOOGLE SEARCH STRUCTURED DATA FOR ARTICLE && GOOGLE BREADCRUMB STRUCTURED DATA--%>

    <!-- Google Fonts -->
    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Open+Sans:300,400,600,700|Leckerli+One">

    <!-- CSS Global Compulsory -->
    <link rel="stylesheet" href="/themes/m4x1p/vendor/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/icon-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/icon-hs/style.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/icon-line/css/simple-line-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.css" />

    <!-- CSS Implementing Plugins -->
    <link rel="stylesheet" href="/themes/m4x1p/vendor/hamburgers/hamburgers.min.css">
    <link rel="stylesheet" href="/themes/m4x1p/vendor/slick-carousel/slick/slick.css">

    <!-- CSS Template -->
    <link rel="stylesheet" href="/themes/m4x1p/css/styles.op-spa.css">

    <!-- CSS Customization -->
    <link rel="stylesheet" href="/themes/m4x1p/css/custom.css">

</head>

<body>
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
                    <img class="u-header__logo-img u-header__logo-img--main d-block g-width-60" src="/themes/m4x1p/img/logo-light.png" alt="Image description"
                         data-header-fix-moment-exclude="d-block"
                         data-header-fix-moment-classes="d-none">

                    <img class="u-header__logo-img u-header__logo-img--main d-none g-width-60" src="/themes/m4x1p/img/logo-dark.png" alt="Image description"
                         data-header-fix-moment-exclude="d-none"
                         data-header-fix-moment-classes="d-block"> </a>
                <!-- End Logo -->

                <!-- Navigation -->
                <div class="collapse navbar-collapse align-items-center flex-sm-row" id="navBar" data-mobile-scroll-hide="true">
                    <ul id="js-scroll-nav" class="navbar-nav text-uppercase g-font-weight-700 g-font-size-11 g-pt-20 g-pt-0--lg ml-auto">
                        <li class="nav-item g-mr-12--lg g-mb-7 g-mb-0--lg active">
                            <a href="#home" class="nav-link p-0">Home
                                <span class="sr-only">(current)</span>
                            </a>
                        </li>
                        <li class="nav-item g-mx-12--lg g-mb-7 g-mb-0--lg">
                            <a href="#about" class="nav-link p-0">About</a>
                        </li>
                        <li class="nav-item g-mx-12--lg g-mb-7 g-mb-0--lg">
                            <a href="#services" class="nav-link p-0">Services</a>
                        </li>
                        <li class="nav-item g-mx-12--lg g-mb-7 g-mb-0--lg">
                            <a href="#booking" class="nav-link p-0">Make Appointment</a>
                        </li>
                        <li class="nav-item g-mx-12--lg g-mb-7 g-mb-0--lg">
                            <a href="#gallery" class="nav-link p-0">Gallery</a>
                        </li>
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
<section id="home" class="u-bg-overlay g-height-100vh g-min-height-600 g-bg-img-hero g-bg-black-opacity-0_3--after" style="background-image: url(https://images.unsplash.com/photo-1519014816548-bf5fe059798b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80);">
    <div class="u-bg-overlay__inner g-absolute-centered--y w-100">
        <div class="container text-center g-max-width-750">
            <h1 class="g-line-height-1_5 g-font-weight-700 g-font-size-50 g-color-white g-mb-15">Our reputation is built on how we treat YOU</h1>
            <h2 class="g-font-weight-600 g-font-size-22 g-color-white">Discount 20% for $50 services until 4/15/2019</h2>
            <a class="btn btn-md text-uppercase u-btn-primary g-font-weight-700 g-font-size-11 g-brd-none rounded-0 g-py-10 g-px-25" href="/booking.html">Make Appointment</a>
        </div>
    </div>
</section>
<%--Home End--%>

<%--About Us--%>
<section id="about" class="g-pb-80">
    <div class="container-fluid px-0">
        <div class="row no-gutters">
            <div class="col-md-6 g-bg-img-hero g-min-height-400" style="background-image: url(https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80);"></div>
            <div class="col-md-6 d-flex align-items-center text-center g-pa-50">
                <div class="w-100">
                    <div class="g-mb-25">
                        <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">About Us</h4>
                        <h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">Perfect place for your relaxation</h2>
                    </div>

                    <p class="g-mb-35">Welcome to [Nails Salon] where your comfort and safety are out top priority. Indulge yourself with our luxurious treatment from a full line of nails, skin, an spa care designed to pamper, rejuvenate and restore you body & mind. As a guest, you are entitled to the finest products and services available. Our innovative pipe-less chair and instrument are sterilized after every use to ensure your safety. Rest assure that you will be in good hand at [Nails Salon].</p>
                </div>
            </div>
        </div>

        <div class="row no-gutters">
            <div class="col-md-6">
                <img class="img-fluid" src="https://images.unsplash.com/photo-1506668635606-caa9ef5ce079?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">
            </div>

            <div class="col-md-6">
                <div class="js-carousel"
                     data-infinite="true"
                     data-arrows-classes="u-arrow-v1 g-absolute-centered--y g-width-45 g-height-55 g-font-size-12 g-theme-color-gray-dark-v1 g-bg-white g-mt-minus-10"
                     data-arrow-left-classes="fa fa-chevron-left g-left-0"
                     data-arrow-right-classes="fa fa-chevron-right g-right-0">
                    <div class="js-slide">
                        <img class="img-fluid" src="https://images.unsplash.com/photo-1519014816548-bf5fe059798b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80" alt="Image description">
                    </div>
                    <div class="js-slide">
                        <img class="img-fluid" src="https://images.unsplash.com/photo-1459164648438-af647b154e96?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">
                    </div>

                    <div class="js-slide">
                        <img class="img-fluid" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<%--About Us End--%>

<%--Services--%>
<section id="services">
    <div class="container text-center g-width-590 g-mb-50">
        <div class="g-mb-25">
            <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">Our Services</h4>
            <h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">QT Nails</h2>
        </div>
        <p class="mb-0">Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo.</p>
    </div>
    <div class="container-fluid px-0">
        <div class="row no-gutters">
            <div class="col-lg-3 col-sm-6 g-mb-30">
                <!-- Article -->
                <article class="text-center u-block-hover u-block-hover__additional--jump g-bg-white g-color-gray-dark-v3">
                    <!-- Article Image -->
                    <img class="w-100" src="https://images.unsplash.com/photo-1511803574983-43bf1f387ece?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image Description">
                    <!-- End Article Image -->

                    <!-- Article Content -->
                    <div class="g-py-40 g-px-30">
                        <h4 class="text-uppercase g-font-weight-900 g-color-black g-font-size-20 g-mb-15 text-left">Manicure & Pedicure</h4>
                        <ul class="list-unstyled text-uppercase g-mb-30 text-left">
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Curabitur sit amet<span class="text-right" style="float: right">$39</span></li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Etiam ac massa sit</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Fusce accumsan faucibus</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Duis tristique bibendum</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Duis vehicula</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Donec fringilla</li>
                        </ul>
                    </div>
                    <!-- End Article Content -->
                </article>
                <!-- End Article -->
            </div>
            <div class="col-lg-3 col-sm-6 g-mb-30">
                <!-- Article -->
                <article class="text-center u-block-hover u-block-hover__additional--jump g-bg-white g-color-gray-dark-v3">
                    <!-- Article Image -->
                    <img class="w-100" src="https://images.unsplash.com/photo-1464863979621-258859e62245?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1533&q=80" alt="Image Description">
                    <!-- End Article Image -->

                    <!-- Article Content -->
                    <div class="g-py-40 g-px-30">
                        <h4 class="text-uppercase g-font-weight-900 g-color-black g-font-size-20 g-mb-15">Waxing</h4>
                        <ul class="list-unstyled text-uppercase g-mb-30">
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Curabitur sit amet</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Etiam ac massa sit</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Fusce accumsan faucibus</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Duis tristique bibendum</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Duis vehicula</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Donec fringilla</li>
                        </ul>
                    </div>
                    <!-- End Article Content -->
                </article>
                <!-- End Article -->
            </div>

            <div class="col-lg-3 col-sm-6 g-mb-30">
                <!-- Article -->
                <article class="text-center u-block-hover u-block-hover__additional--jump g-bg-white g-color-gray-dark-v3">
                    <!-- Article Image -->
                    <img class="w-100" src="https://images.unsplash.com/photo-1467632499275-7a693a761056?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1534&q=80" alt="Image Description">
                    <!-- End Article Image -->

                    <!-- Article Content -->
                    <div class="g-py-40 g-px-30">
                        <h4 class="text-uppercase g-font-weight-900 g-color-black g-font-size-20 g-mb-15">Eyelash Extensions</h4>
                        <ul class="list-unstyled text-uppercase g-mb-30">
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Curabitur sit amet</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Etiam ac massa sit</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Fusce accumsan faucibus</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Duis tristique bibendum</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Duis vehicula</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Donec fringilla</li>
                        </ul>
                    </div>
                    <!-- End Article Content -->
                </article>
                <!-- End Article -->
            </div>

            <div class="col-lg-3 col-sm-6 g-mb-30">
                <!-- Article -->
                <article class="text-center u-block-hover u-block-hover__additional--jump g-bg-white g-color-gray-dark-v3">
                    <!-- Article Image -->
                    <img class="w-100" src="https://images.unsplash.com/photo-1492618269284-653dce58fd6d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1510&q=80" alt="Image Description">
                    <!-- End Article Image -->

                    <!-- Article Content -->
                    <div class="g-py-40 g-px-30">
                        <h4 class="text-uppercase g-font-weight-900 g-color-black g-font-size-20 g-mb-15">Other Services</h4>
                        <ul class="list-unstyled text-uppercase g-mb-30">
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Curabitur sit amet</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Etiam ac massa sit</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Fusce accumsan faucibus</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Duis tristique bibendum</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Duis vehicula</li>
                            <li class="g-mb-9 g-brd-bottom g-brd-gray-light-v4 g-py-15">Donec fringilla</li>
                        </ul>
                    </div>
                    <!-- End Article Content -->
                </article>
                <!-- End Article -->
            </div>
        </div>
    </div>

</section>
<%--Services End--%>

<%--<decorator:body/>--%>

<!-- Booking -->
<spring:eval expression="serviceLocator.getNailStoreDao().findActiveByOrder('active', 'Y', null, site.id)" var="stores"/>
<c:if test="${!empty stores && fn:length(stores) == 1}">
    <c:set var="store" value="${stores[0]}"/>
    <spring:eval expression="serviceLocator.getNailServiceDao().getGroupServices(store.id)" var="groupServices"/>
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
                                <option value="">Select Service</option>
                                <option value="">Select Service1</option>
                                <optgroup label="group1">
                                    <option value="">Select Service1</option>
                                    <option value="">Select Service1</option>
                                </optgroup>
                                <optgroup label="group2">
                                    <option value="">Select Service1</option>
                                    <option value="">Select Service1</option>
                                </optgroup>
                                <c:forEach items="${groupServices}" var="group">
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
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel4">Make Appointment</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div  id="modal_message_alert"></div>
                    <form class="margin-bottom-40" role="form" id="formModal">
                        <input type="hidden" name="selectedStoreId" id="selectedStoreId" value=""/>
                        <input type="hidden" name="selectedDate" id="selectedDate" value=""/>
                        <input type="hidden" name="selectedTime" id="selectedTime" value=""/>
                        <input type="hidden" name="selectedServiceId" id="selectedServiceId" value=""/>
                        <input type="hidden" name="selectedEmployeeId" id="selectedEmployeeId" value=""/>
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="form-group">
                                <label for="firstName"><fmt:message key="site.register.firstName"/></label>
                                <input type="text" class="form-control" name="firstName" id="firstName" value="" placeholder="<fmt:message key="site.register.firstName"/>"/>
                            </div>
                        </div>
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="form-group">
                                <label for="lastName"><fmt:message key="site.register.lastName"/></label>
                                <input type="text" class="form-control" name="lastName" id="lastName" value="" placeholder="<fmt:message key="site.register.lastName"/>"/>
                            </div>
                        </div>
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="form-group">
                                <label for="phone"><fmt:message key="site.register.phone"/></label>
                                <input type="text" class="form-control" name="phone" id="phone" value="" placeholder="(xxx) xxx-xxxx"/>
                            </div>
                        </div>
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="form-group">
                                <label for="email"><fmt:message key="site.register.email"/></label>
                                <input type="text" class="form-control" name="email" id="email" value="" placeholder="<fmt:message key="site.register.email"/>"/>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <div class="form-group">
                                <label for="message"><fmt:message key="booking.message.content"/></label>
                                <textarea class="form-control" id="message" name="message" rows="2" placeholder="<fmt:message key="booking.message.content"/>" maxlength="999"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-u btn-u-default" data-dismiss="modal">Close</button>
                <button type="button" id="submit-appointment" class="btn-u btn-u-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<%--Booking End--%>

<section id="gallery" class="g-theme-bg-gray-light-v1 g-py-80">
    <div class="container text-center g-width-590 g-mb-50">
        <div class="g-mb-25">
            <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">Gallery</h4>
            <h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">It's your time to relax</h2>
        </div>

        <p class="mb-0">Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo.</p>
    </div>
    <div class="container-fluid px-0">
        <div class="row no-gutters">
            <div class="col-md-6 g-bg-img-hero g-min-height-400" style="background-image: url(https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80);"></div>
            <div class="col-md-6 g-bg-img-hero g-min-height-400" style="background-image: url(https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80);"></div>
        </div>

        <div class="row no-gutters">
            <div class="col-md-6">
                <img class="img-fluid" src="https://images.unsplash.com/photo-1506668635606-caa9ef5ce079?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">
            </div>

            <div class="col-md-6">
                <div class="js-carousel"
                     data-infinite="true"
                     data-arrows-classes="u-arrow-v1 g-absolute-centered--y g-width-45 g-height-55 g-font-size-12 g-theme-color-gray-dark-v1 g-bg-white g-mt-minus-10"
                     data-arrow-left-classes="fa fa-chevron-left g-left-0"
                     data-arrow-right-classes="fa fa-chevron-right g-right-0">
                    <div class="js-slide">
                        <img class="img-fluid" src="https://images.unsplash.com/photo-1519014816548-bf5fe059798b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80" alt="Image description">
                    </div>

                    <div class="js-slide">
                        <img class="img-fluid" src="https://images.unsplash.com/photo-1459164648438-af647b154e96?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">
                    </div>

                    <div class="js-slide">
                        <img class="img-fluid" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="Image description">
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Section Content -->
<section id="contact" class="g-py-70">
    <div class="container text-center g-width-590 g-mb-50">
        <div class="g-mb-25">
            <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">Contact us</h4>
            <h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">Get in touch</h2>
        </div>

        <p class="mb-0">Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo.</p>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-8 g-mb-40 g-mb-0--md">
                <form>
                    <div class="form-group g-mb-25">
                        <input class="form-control h-100 g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-pa-12" type="text" placeholder="Your name">
                    </div>
                    <div class="form-group g-mb-25">
                        <input class="form-control h-100 g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-pa-12" type="tel" placeholder="Your email *">
                    </div>
                    <div class="form-group g-mb-25">
                        <textarea class="form-control g-resize-none g-color-gray-dark-v5 g-placeholder-inherit g-theme-bg-gray-light-v1 g-brd-none rounded-0 g-py-6 g-px-12" rows="5" placeholder="Message"></textarea>
                    </div>
                    <button class="btn btn-md text-uppercase btn-block u-btn-outline-primary g-font-weight-700 g-font-size-11 g-brd-2 rounded-0 g-py-19 g-px-20" type="submit" role="button">Submit</button>
                </form>
            </div>

            <div class="col-md-2 text-center text-md-left g-mb-40--md">
                <div class="g-mb-25">
                <span class="u-icon-v1 u-icon-size--xs g-color-primary">
                  <i class="icon-directions"></i>
                </span>
                    <em class="d-block g-color-black-light-v2 text-uppercase g-font-size-12 g-font-style-normal g-mb-5">Address</em>
                    <strong class="d-block">9660 Audelia Rd, Ste 205</strong>
                    <strong class="d-block">Dallas, TX 75238</strong>
                </div>

                <div class="g-mb-25">
                <span class="u-icon-v1 u-icon-size--xs g-color-primary">
                  <i class="icon-call-in"></i>
                </span>
                    <em class="d-block g-color-black-light-v2 text-uppercase g-font-size-12 g-font-style-normal g-mb-5">Phone Number</em>
                    <strong class="d-block">214-503-1616</strong>
                </div>

                <div>
                <span class="u-icon-v1 u-icon-size--xs g-color-primary">
                  <i class="icon-envelope-letter"></i>
                </span>
                    <em class="d-block g-color-black-light-v2 text-uppercase g-font-size-12 g-font-style-normal g-mb-5">Email</em>
                    <strong class="d-block"><a href="#" class="g-color-main">info@webphattai.com</a></strong>
                </div>
            </div>
            <div class="col-md-2 text-center text-md-left g-mb-40--md">
                <span class="u-icon-v1 u-icon-size--xs g-color-primary">
                  <i class="icon-clock"></i>
                </span>
                <div class="media-body text-left">
                    <span class="d-block g-font-weight-500 g-font-size-default text-uppercase mb-2">Business Hours</span>
                    <strong class="d-block">Mon: <span style="float: right">09am-06pm</span></strong>
                    <strong class="d-block">Tue: <span style="float: right">09am-06pm</span></strong>
                    <strong class="d-block">Wed: <span style="float: right">09am-06pm</span></strong>
                    <strong class="d-block">Thu: <span style="float: right">09am-06pm</span></strong>
                    <strong class="d-block">Fri: <span style="float: right">09am-06pm</span></strong>
                    <strong class="d-block">Sat: <span style="float: right">09am-06pm</span></strong>
                    <strong class="d-block">Sun: <span style="float: right">Closed</span></strong>
                </div>
            </div>
        </div>
</section>
<!-- End Contact -->

<!-- Footer -->
<footer>
    <!-- Google Map -->
    <%--<div id="gMap" class="js-g-map g-min-height-450 h-100"--%>
         <%--data-type="custom"--%>
         <%--data-lat="40.674"--%>
         <%--data-lng="-73.946"--%>
         <%--data-zoom="12"--%>
         <%--data-title="Travel"--%>
         <%--data-styles='[--%>
               <%--["", "", [{"gamma":1},{"weight":1},{"visibility":"simplified"},{"hue":"#8dab68"}]],--%>
               <%--["", "labels", [{"visibility":"on"}]],--%>
               <%--["water", "", [{"color":"#aee2e0"}]]--%>
             <%--]'--%>
         <%--data-pin="true"--%>
         <%--data-pin-icon="/themes/m4x1p/img/pin.png">--%>

        <%--<%--<iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2953.81567460013!2d-83.74793828522401!3d42.23975075068461!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x883cafea6feda06f%3A0xa0c7c8cfa6ff4061!2sBriarwood+Foxy+Nails!5e0!3m2!1svi!2s!4v1527070957773" width="100%" height="450" frameborder="0" style="border:0" allowfullscreen></iframe>--%>
         <%--</div>--%>
    <iframe  width="100%" height="500" frameborder="0" style="border:0" allowfullscreen src="https://maps.google.com/maps?width=700&amp;height=440&amp;hl=en&amp;q=3208%20Lockridge%20St%2C%20Ann%20Arbor%20MI%2048108+(Title)&amp;ie=UTF8&amp;t=&amp;z=12&amp;iwloc=B&amp;output=embed" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>
    <!-- End Google Map -->

    <div class="container-fluid text-center g-color-gray-dark-v5 g-py-40">
        <a class="d-inline-block g-mb-25" href="/"> <img src="/themes/m4x1p/img/logo-dark.png" alt="Logo"> </a>

        <p class="g-mb-30">In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo.</p>

        <ul class="list-inline d-inline-block g-mb-30">
            <li class="list-inline-item g-mr-10">
                <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-gray-dark-v2 g-color-white--hover g-bg-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="#"><i class="fa fa-twitter"></i></a>
            </li>
            <li class="list-inline-item g-mr-10">
                <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-gray-dark-v2 g-color-white--hover g-bg-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="#"><i class="fa fa-pinterest"></i></a>
            </li>
            <li class="list-inline-item g-mr-10">
                <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-gray-dark-v2 g-color-white--hover g-bg-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="#"><i class="fa fa-facebook"></i></a>
            </li>
            <li class="list-inline-item">
                <a class="u-icon-v3 g-width-35 g-height-35 g-font-size-15 g-color-gray-dark-v2 g-color-white--hover g-bg-white g-bg-primary--hover g-transition-0_2 g-transition--ease-in" href="#"><i class="fa fa-linkedin"></i></a>
            </li>
        </ul>

        <ul class="list-inline text-uppercase g-font-weight-700 g-font-size-11 mb-0">
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">Home</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">About</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">Best offers</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">Our procedures</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">Advices</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">Subscribe</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">Gallery</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">Products</a>
            </li>
            <li class="list-inline-item g-px-12--md">
                <a class="g-color-gray-dark-v5 g-color-primary--hover g-text-underline--none--hover" href="#">Contact</a>
            </li>
        </ul>
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

<!-- JS Unify -->
<script src="/themes/m4x1p/js/hs.core.js"></script>
<script src="/themes/m4x1p/js/components/hs.header.js"></script>
<script src="/themes/m4x1p/js/helpers/hs.hamburgers.js"></script>
<script src="/themes/m4x1p/js/components/hs.scroll-nav.js"></script>
<script src="/themes/m4x1p/js/components/hs.rating.js"></script>
<script src="/themes/m4x1p/js/components/hs.carousel.js"></script>
<script src="/themes/m4x1p/js/components/gmap/hs.map.js"></script>
<script src="/themes/m4x1p/js/components/hs.go-to.js"></script>
<%--<script src="/themes/m3x/plugins/multiselect/js/bootstrap-multiselect.js"></script>--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>

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
        $.HSCore.components.HSScrollNav.init($('#js-scroll-nav'), {
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
                div11 = $("<div class='row col-md-12'/>");
                div11.append("<h4>Morning</h4>")
                for(var i in morning) {
                    divTimeSlot = $("<div class='col-xs-6 col-sm-3 col-md-2'/>");
                    divTimeSlot.append("<button class='btn btn-block btn-amazon open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+morning[i]+"'>"+morning[i]+"</button>")
                    div11.append(divTimeSlot)
                }
                div1.append(div11)
            }
            //afternoon
            if (afternoon && afternoon.length > 0) {
                div11 = $("<div class='row col-md-12'/>");
                div11.append("<h4>Afternoon</h4>")
                for(var i in afternoon) {
                    divTimeSlot = $("<div class='col-xs-6 col-sm-3 col-md-2'/>");
                    divTimeSlot.append("<button class='btn btn-block btn-amazon open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+afternoon[i]+"'>"+afternoon[i]+"</button>")
                    div11.append(divTimeSlot)
                }
                div1.append(div11)
            }
            //evening
            if (evening && evening.length > 0) {
                div11 = $("<div class='row col-md-12'/>");
                div11.append("<h4>Evening</h4>")
                for(var i in evening) {
                    divTimeSlot = $("<div class='col-xs-6 col-sm-3 col-md-2'/>");
                    divTimeSlot.append("<button class='btn btn-block btn-amazon open-booking-modal' data-toggle='modal' data-target='#responsive-booking' value='"+evening[i]+"'>"+evening[i]+"</button>")
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
                console.log(storeId)
                console.log(serviceId)
                console.log(employeeId)
                console.log(date)
                console.log(time)
                console.log($("#firstName").val())
                console.log($("#lastName").val())
                console.log($("#phone").val())
                console.log($("#email").val())
                console.log($("#message").val())
                var formData = $("#formModal").serialize();
                console.log(formData)
                $.ajax({
                    type: "POST",
                    url: "/submit_appointment.html",
                    data: formData,
                    success: function(result){
                        $('#modal_message_alert').html(result);
                    },
                    error: function (result) {
                        $('#modal_message_alert').html(result);
                        console.log (result)
                    }
                });
            }
        });


    });

</script>

</body>
</html>