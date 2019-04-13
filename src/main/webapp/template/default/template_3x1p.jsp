<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><decorator:title/></title>
    <decorator:head/>

    <!-- CSS Global Compulsory -->
    <link rel="stylesheet" href="/themes/m3x/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/themes/m3x1p/css/one.style.css">
    <link rel="stylesheet" href="/themes/m3x1p/css/app.css">
    <link rel="stylesheet" href="/themes/m3x1p/css/blocks.css">
    <link rel="stylesheet" href="/themes/m3x1p/css/plugins.css">

    <!-- CSS Footer -->
    <link rel="stylesheet" href="/themes/m3x/css/footers/footer-v7.css">

    <!-- CSS Implementing Plugins -->
    <link rel="stylesheet" href="/themes/m3x/plugins/animate.css">
    <link rel="stylesheet" href="/themes/m3x/plugins/line-icons/line-icons.css">
    <link rel="stylesheet" href="/themes/m3x/plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="/themes/m3x1p/plugins/pace/pace-flash.css">
    <link rel="stylesheet" href="/themes/m3x1p/plugins/revolution-slider/rs-plugin/css/settings.css" type="text/css" media="screen">
    <!--[if lt IE 9]>
    <link rel="stylesheet" href="/themes/m3x1p/plugins/revolution-slider/rs-plugin/css/settings-ie8.css" type="text/css" media="screen"><![endif]-->

    <!-- Style Switcher -->
    <link rel="stylesheet" href="/themes/m3x/css/plugins/style-switcher.css">

    <!-- CSS Theme -->
    <link rel="stylesheet" href="/themes/m3x/css/theme-colors/default.css" id="style_color">

    <!-- CSS Customization -->
    <link rel="stylesheet" href="/themes/m3x1p/css/custom.css">

    <%--TODO: Demo only--%>
    <link rel="stylesheet" href="https://mobirise.com/bootstrap-gallery/assets/mobirise-gallery/style.css">

</head>

<body id="body" data-spy="scroll" data-target=".one-page-header" class="demo-lightbox-gallery">
<!--=== Header ===-->
<nav class="one-page-header one-page-header-style-2 navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="menu-container page-scroll">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

            <a class="navbar-brand" href="#intro">
                <span>U</span>nify
                <!-- <img src="/themes/m3x1p/img/logo1.png" alt="Logo"> -->
            </a>
        </div>
        <div class="top-contact-block">
            <i class="fa fa-phone fa-1"></i> <a href="tel:0 800 2000 123">0 800 2000 123</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <div class="menu-container">
                <ul class="nav navbar-nav">
                    <li class="page-scroll home">
                        <a href="#body">Home</a>
                    </li>
                    <li class="page-scroll">
                        <a href="#about">About Us</a>
                    </li>
                    <li class="page-scroll">
                        <a href="#services">Services</a>
                    </li>
                    <li class="page-scroll">
                        <a href="#galleries">Gallery</a>
                    </li>
                    <li class="page-scroll">
                        <a href="#contact">Contact</a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
</nav>
<!--=== End Header ===-->

<!-- Intro Section -->
<section id="intro" class="intro-section">
    <div class="fullscreenbanner-container">
        <div class="fullscreenbanner">
            <ul>
                <!-- SLIDE 1 -->
                <li data-transition="curtain-1" data-slotamount="5" data-masterspeed="700" data-title="Slide 1">
                    <!-- MAIN IMAGE -->
                    <img src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="slidebg1" data-bgfit="cover" data-bgposition="center center" data-bgrepeat="no-repeat">

                    <!-- LAYERS -->
                    <div class="tp-caption rs-caption-1 sft start"
                         data-x="center"
                         data-hoffset="0"
                         data-y="100"
                         data-speed="800"
                         data-start="2000"
                         data-easing="Back.easeInOut"
                         data-endspeed="300">
                        Our reputation is built on how we treat <b>YOU</b>
                    </div>

                    <!-- LAYER -->
                    <div class="tp-caption rs-caption-2 sft"
                         data-x="center"
                         data-hoffset="0"
                         data-y="200"
                         data-speed="1000"
                         data-start="3000"
                         data-easing="Power4.easeOut"
                         data-endspeed="300"
                         data-endeasing="Power1.easeIn"
                         data-captionhidden="off"
                         style="z-index: 6">
                        Creative freedom matters user experience.<br>
                        We minimize the gap between technology and its audience.
                    </div>

                    <!-- LAYER -->
                    <div class="tp-caption rs-caption-3 sft"
                         data-x="center"
                         data-hoffset="0"
                         data-y="360"
                         data-speed="800"
                         data-start="3500"
                         data-easing="Power4.easeOut"
                         data-endspeed="300"
                         data-endeasing="Power1.easeIn"
                         data-captionhidden="off"
                         style="z-index: 6">
                        <span class="page-scroll"><a href="#about" class="btn-u btn-brd btn-brd-hover btn-u-light">Learn More</a></span>
                        <span class="page-scroll"><a href="#portfolio" class="btn-u btn-brd btn-brd-hover btn-u-light">Our Work</a></span>
                    </div>
                </li>

                <!-- SLIDE 2 -->
                <li data-transition="slideup" data-slotamount="5" data-masterspeed="1000" data-title="Slide 2">
                    <!-- MAIN IMAGE -->
                    <img src="https://images.unsplash.com/photo-1519014816548-bf5fe059798b?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="slidebg1" data-bgfit="cover" data-bgposition="center center" data-bgrepeat="no-repeat">

                    <!-- LAYERS -->
                    <div class="tp-caption rs-caption-1 sft start"
                         data-x="center"
                         data-hoffset="0"
                         data-y="100"
                         data-speed="800"
                         data-start="1500"
                         data-easing="Back.easeInOut"
                         data-endspeed="300">
                        DEDICATED ADVANCED TEAM
                    </div>

                    <!-- LAYER -->
                    <div class="tp-caption rs-caption-2 sft"
                         data-x="center"
                         data-hoffset="0"
                         data-y="200"
                         data-speed="1000"
                         data-start="2500"
                         data-easing="Power4.easeOut"
                         data-endspeed="300"
                         data-endeasing="Power1.easeIn"
                         data-captionhidden="off"
                         style="z-index: 6">
                        We are creative technology company providing<br>
                        key digital services on web and mobile.
                    </div>

                    <!-- LAYER -->
                    <div class="tp-caption rs-caption-3 sft"
                         data-x="center"
                         data-hoffset="0"
                         data-y="360"
                         data-speed="800"
                         data-start="3500"
                         data-easing="Power4.easeOut"
                         data-endspeed="300"
                         data-endeasing="Power1.easeIn"
                         data-captionhidden="off"
                         style="z-index: 6">
                        <span class="page-scroll"><a href="#about" class="btn-u btn-brd btn-brd-hover btn-u-light">Learn More</a></span>
                        <span class="page-scroll"><a href="#portfolio" class="btn-u btn-brd btn-brd-hover btn-u-light">Our Work</a></span>
                    </div>
                </li>

                <!-- SLIDE 3 -->
                <li data-transition="slideup" data-slotamount="5" data-masterspeed="700" data-title="Slide 3">
                    <!-- MAIN IMAGE -->
                    <img src="https://images.unsplash.com/photo-1521768669-94a0b53f5905?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="slidebg1" data-bgfit="cover" data-bgposition="center center" data-bgrepeat="no-repeat">

                    <!-- LAYERS -->
                    <div class="tp-caption rs-caption-1 sft start"
                         data-x="center"
                         data-hoffset="0"
                         data-y="110"
                         data-speed="800"
                         data-start="1500"
                         data-easing="Back.easeInOut"
                         data-endspeed="300">
                        WE DO THINGS DIFFERENTLY
                    </div>

                    <!-- LAYER -->
                    <div class="tp-caption rs-caption-2 sfb"
                         data-x="center"
                         data-hoffset="0"
                         data-y="210"
                         data-speed="800"
                         data-start="2500"
                         data-easing="Power4.easeOut"
                         data-endspeed="300"
                         data-endeasing="Power1.easeIn"
                         data-captionhidden="off"
                         style="z-index: 6">
                        Creative freedom matters user experience.<br>
                        We minimize the gap between technology and its audience.
                    </div>

                    <!-- LAYER -->
                    <div class="tp-caption rs-caption-3 sfb"
                         data-x="center"
                         data-hoffset="0"
                         data-y="370"
                         data-speed="800"
                         data-start="3500"
                         data-easing="Power4.easeOut"
                         data-endspeed="300"
                         data-endeasing="Power1.easeIn"
                         data-captionhidden="off"
                         style="z-index: 6">
                        <span class="page-scroll"><a href="#about" class="btn-u btn-brd btn-brd-hover btn-u-light">Learn More</a></span>
                        <span class="page-scroll"><a href="#portfolio" class="btn-u btn-brd btn-brd-hover btn-u-light">Our Work</a></span>
                    </div>
                </li>
            </ul>
            <div class="tp-bannertimer tp-bottom"></div>
            <div class="tp-dottedoverlay twoxtwo"></div>
        </div>
    </div>
</section>
<!-- End Intro Section -->

<!--  About Section -->
<section id="about" class="about-section section-first">

    <div class="container content-lg">
        <div class="title-v1">
            <h2>About Us</h2>
        </div>

        <div class="row">
            <div class="col-md-6 content-boxes-v3 margin-bottom-40">
                <div class="clearfix margin-bottom-30">
                    <%--<i class="icon-custom icon-md rounded-x icon-bg-u icon-line icon-trophy"></i>--%>
                    <div class="content-boxes-in-v3 ">
                        <%--<h2 class="heading-sm">Innovation Leader</h2>--%>
                        <p>
                            Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's
                            Lorem Ipsum has been the industry's Lorem Ipsum has been the industry's Lorem Ipsum has been the industry's Lorem Ipsum has been the industry's
                            Lorem Ipsum has been the industry's Lorem Ipsum has been the industry'sLorem Ipsum has been the industry'sLorem Ipsum has been the industry's
                            Lorem Ipsum has been the industry'snLorem Ipsum has been the industry'snLorem Ipsum has been the industry's Lorem Ipsum has been the industry's
                            Lorem Ipsum has been the industry's Lorem Ipsum has been the industry's Lorem Ipsum has been the industry's Lorem Ipsum has been the industry's
                            Lorem Ipsum has been the industry's
                        </p>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <img class="img-responsive" src="/themes/m3x1p/img/mockup/mockup1.png" alt="">
            </div>
        </div>
    </div>

</section>
<!--  About Section -->
<%--Services Section--%>
<section id="services">
    <div class="parallax-quote parallaxBg">
        <div class="container">
            <div class="parallax-quote-in">
                <p>We want to bring a <span class="color-green">Confidence</span> to you.</p>
            </div>
        </div>
    </div>
    <div class="container padding-top-100 padding-bottom-70 margin-bottom-40">
        <div class="title-v1 margin-top-20">
            <h2>Our Services</h2>
            <%--<p>We do <strong>things</strong> differently company providing key digital services. <br>--%>
            <%--Focused on helping our clients to build a <strong>successful</strong> business on web and mobile.</p>--%>
        </div>

        <div class="row featured-blog">
            <div class="col-sm-4 sm-margin-bottom-50">
                <div class="featured-img">
                    <img class="img-responsive margin-bottom-20" src="https://images.unsplash.com/photo-1519014816548-bf5fe059798b?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="">
                    <a href="#"><i class="rounded-x fa fa-link"></i></a>
                </div>
                <h2><a class="color-dark" href="#">Manicure & Pedicure</a></h2>
                <%--<p>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum.</p>--%>
                <ul class="list-unstyled lists-v1">
                    <li><i class="fa fa-angle-right"></i>Curabitur porttitor sapien</li>
                    <li><i class="fa fa-angle-right"></i>Donec vitae quam neque</li>
                    <li><i class="fa fa-angle-right"></i>Cum sociis natoque</li>
                    <li><i class="fa fa-angle-right"></i>Aliquam et orci orci</li>
                </ul>
            </div>
            <div class="col-sm-4 sm-margin-bottom-50">
                <div class="featured-img">
                    <img class="img-responsive margin-bottom-20" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="">
                    <a href="#"><i class="rounded-x fa fa-link"></i></a>
                </div>
                <h2><a class="color-dark" href="#">Developer Friendly Code</a></h2>
                <ul class="list-unstyled lists-v1">
                    <li><i class="fa fa-angle-right"></i>Curabitur porttitor sapien</li>
                    <li><i class="fa fa-angle-right"></i>Donec vitae quam neque</li>
                    <li><i class="fa fa-angle-right"></i>Cum sociis natoque</li>
                    <li><i class="fa fa-angle-right"></i>Aliquam et orci orci</li>
                </ul>
            </div>
            <div class="col-sm-4">
                <div class="featured-img">
                    <img class="img-responsive margin-bottom-20" src="https://images.unsplash.com/photo-1521768669-94a0b53f5905?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt="">
                    <a href="#"><i class="rounded-x fa fa-link"></i></a>
                </div>
                <h2><a class="color-dark" href="#">Bootstrap 3 Template</a></h2>
                <ul class="list-unstyled  lists-v1">
                    <li><i class="fa fa-angle-right"></i>Curabitur porttitor sapien</li>
                    <li><i class="fa fa-angle-right"></i>Donec vitae quam neque</li>
                    <li><i class="fa fa-angle-right"></i>Cum sociis natoque</li>
                    <li><i class="fa fa-angle-right"></i>Aliquam et orci orci</li>
                </ul>
            </div>
        </div>


    </div>
</section>
<%--End Services Section--%>

<%--Gallery Section--%>
<section id="galleries">
    <div class="container content">
        <div class="text-center margin-bottom-50">
            <h2 class="title-v2 title-center">Gallery Collections</h2>

            <p class="space-lg-hor">If you are going to use a <span class="color-green">passage of Lorem Ipsum</span>, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making <span class="color-green">this the first</span> true generator on the Internet.</p>
        </div>

        <div class="row text-center">
            <div class="row  margin-bottom-30">
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1531299244174-d247dd4e5a66?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1570&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 1">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1531299244174-d247dd4e5a66?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1570&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1531299244174-d247dd4e5a66?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1570&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 2">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1531299244174-d247dd4e5a66?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1570&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 3">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 4">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 5">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 6">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 7">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 8">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 9">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 10">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 11">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
                <div class="col-sm-3 sm-margin-bottom-30">
                    <a href="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" rel="gallery2" class="fancybox img-hover-v1" title="Image 12">
                        <span><img class="img-responsive" src="https://images.unsplash.com/photo-1514543051966-e939828f2c9f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1500&q=80" alt=""></span>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <!-- Gallery -->
</section>

<%--End Gallery Section--%>

<!-- Contact Section -->
<section id="contact" class="contacts-section">
    <div class="container content-lg">
        <div class="title-v1">
            <h2>Contact Us</h2>

            <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry. <br>
                It has been the industry's standard dummy text.</p>
        </div>

        <div class="row contacts-in">
            <div class="col-md-6 md-margin-bottom-40">
                <ul class="list-unstyled">
                    <li><i class="fa fa-home"></i> 5B Streat, City 50987 New Town US</li>
                    <li><i class="fa fa-phone"></i> 1(800) 220 084</li>
                    <li><i class="fa fa-envelope"></i> <a href="info@example.com">info@example.com</a></li>
                    <li><i class="fa fa-globe"></i> <a href="http://htmlstream.com">www.htmlstream.com</a></li>
                </ul>
            </div>

            <div class="col-md-6">
                <form action="/themes/m3x1p/php/sky-forms-pro/demo-contacts-process.php" method="post" id="sky-form3" class="sky-form contact-style">
                    <fieldset>
                        <label>Name</label>

                        <div class="row">
                            <div class="col-md-7 margin-bottom-20 col-md-offset-0">
                                <div>
                                    <input type="text" name="name" id="name" class="form-control">
                                </div>
                            </div>
                        </div>

                        <label>Email <span class="color-red">*</span></label>

                        <div class="row">
                            <div class="col-md-7 margin-bottom-20 col-md-offset-0">
                                <div>
                                    <input type="text" name="email" id="email" class="form-control">
                                </div>
                            </div>
                        </div>

                        <label>Message</label>

                        <div class="row">
                            <div class="col-md-11 margin-bottom-20 col-md-offset-0">
                                <div>
                                    <textarea rows="8" name="message" id="message" class="form-control"></textarea>
                                </div>
                            </div>
                        </div>

                        <p>
                            <button type="submit" class="btn-u btn-brd btn-brd-hover btn-u-dark">Send Message</button>
                        </p>
                    </fieldset>

                    <div class="message">
                        <i class="rounded-x fa fa-check"></i>

                        <p>Your message was successfully sent!</p>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="copyright-section">
        <p>2015 &copy; All Rights Reserved. Unify Theme by <a target="_blank" href="https://twitter.com/htmlstream">Htmlstream</a></p>
        <ul class="social-icons">
            <li><a href="#" data-original-title="Facebook" class="social_facebook rounded-x"></a></li>
            <li><a href="#" data-original-title="Twitter" class="social_twitter rounded-x"></a></li>
            <li><a href="#" data-original-title="Goole Plus" class="social_googleplus rounded-x"></a></li>
            <li><a href="#" data-original-title="Pinterest" class="social_pintrest rounded-x"></a></li>
            <li><a href="#" data-original-title="Linkedin" class="social_linkedin rounded-x"></a></li>
        </ul>
        <span class="page-scroll"><a href="#intro"><i class="fa fa-angle-double-up back-to-top"></i></a></span>
    </div>
</section>
<!-- End Contact Section -->

<!-- JS Global Compulsory -->
<script src="/themes/m3x/plugins/jquery/jquery.min.js"></script>
<script src="/themes/m3x/plugins/jquery/jquery-migrate.min.js"></script>
<script src="/themes/m3x/plugins/bootstrap/js/bootstrap.min.js"></script>
<!-- JS Implementing Plugins -->
<script src="/themes/m3x1p/plugins/smoothScroll.js"></script>
<script src="/themes/m3x1p/plugins/jquery.easing.min.js"></script>
<script src="/themes/m3x1p/plugins/pace/pace.min.js"></script>
<script src="/themes/m3x1p/plugins/revolution-slider/rs-plugin/js/jquery.themepunch.tools.min.js"></script>
<script src="/themes/m3x1p/plugins/revolution-slider/rs-plugin/js/jquery.themepunch.revolution.min.js"></script>

<!-- JS Page Level-->
<script src="/themes/m3x1p/js/one.app.js"></script>
<script src="/themes/m3x1p/js/plugins/pace-loader.js"></script>
<script src="/themes/m3x1p/js/plugins/revolution-slider.js"></script>
<%--TODO: Demo only--%>
<script src="https://mobirise.com/bootstrap-gallery/assets/mobirise-gallery/script.js"></script>
<script>
    jQuery(document).ready(function () {
        App.init();
        RevolutionSlider.initRSfullScreen();
    });
</script>
<!--[if lt IE 9]>
<script src="/themes/m3x1p/plugins/respond.js"></script>
<script src="/themes/m3x1p/plugins/html5shiv.js"></script>
<script src="/themes/m3x1p/js/plugins/placeholder-IE-fixes.js"></script>
<script src="/themes/m3x1p/plugins/sky-forms-pro/skyforms/js/sky-forms-ie8.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script src="/themes/m3x1p/plugins/sky-forms-pro/skyforms/js/jquery.placeholder.min.js"></script>
<![endif]-->
</body>
</html>