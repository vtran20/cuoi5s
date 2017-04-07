<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%--<%@ attribute name="product" required="true" rtexprvalue="true" type="com.easysoft.ecommerce.model.Product"%>--%>
<%--<%@ attribute name="productVariants" required="true" rtexprvalue="true" type="java.util.List"%>--%>

<script type="text/javascript"
        src="http://www.gmarwaha.com/jquery/jcarousellite/js/jcarousellite_1.0.1.pack.js"></script>
<script type="text/javascript" src="http://gsgd.co.uk/js/jquery.easing.1.1.js"></script>

<script>
    $(function() {
        $(".main .jCarouselLite").jCarouselLite({
            btnNext: ".main .next",
            btnPrev: ".main .prev",
            speed: 1000,
            visible: 5,
            scroll: 5,
            easing: "easeinout"
        });

    });
</script>
<style>
    .carousel {
        padding: 10px 0 0 0;
        margin: 0 0 20px 10px;
        position: relative;
    }

    .main {
        margin-left: 4px;
    }

    .carousel a.prev, .carousel a.next {
        display: block;
        float: left;
        width: 30px; /*        height: 143px;  */
        height: 284px;
        text-decoration: none;
        background: url("http://cungshopping.com/themes/default/images/scrollPrevHotBuys.gif") no-repeat scroll -11px 0px transparent !important;

    }

    .carousel a.next {
        background: url("http://cungshopping.com/themes/default/images/scrollNext.gif") no-repeat scroll 1px 0px transparent !important;
    }

        /*
        .carousel a.next:hover {
                        background-image: url("http://www.gmarwaha.com/image/imageNavRightHover.gif");
                    }
        .carousel a.prev:hover {
                        background-image: url("http://www.gmarwaha.com/image/imageNavLeftHover.gif");
                    }
        */
    .carousel a:hover, .carousel a:active {
        border: none;
        outline: none;
    }

    .carousel .jCarouselLite {
        border: 1px solid #B6B7AF;
        float: left; /*background-color: #dfdfdf;*/
    /* Needed for rendering without flicker */
        position: relative;
        visibility: hidden;
        left: -5000px;
    }

    .carousel ul {
        margin: 0;
    }

    .jCarouselLite ul li {
        height: 284px;
    }

    .carousel li img, .carousel li div {
        background-color: #fff;
        /*width: 156px;
       height: 118px;*/
        margin: 10px;
        padding: 0px 4px;
    }

</style>
<div class="carousel main">
    <a class="prev" href="#">&nbsp;</a>

    <div class="jCarouselLite">
        <ul>
            <li>
                <img alt="Ao Anakids 4" width="156" height="192"
                     src="http://cungshopping.com/product.image?timestamp=&path=/thoi-trang/product/vay_p9971.jpg&resize.width=156&cropresize.height=192">

                <div>sfsdfd</div>
            </li>
            <li>
                <img alt="Ao Anakids 4" width="156" height="192"
                     src="http://cungshopping.com/product.image?timestamp=&path=/thoi-trang/product/prod_1.jpg&resize.width=156&cropresize.height=192">

                <div>sdfdfd</div>
            </li>
            <li>
                <img alt="Ao Anakids 4" width="156" height="192"
                     src="http://cungshopping.com/product.image?timestamp=&path=/thoi-trang/product/prod_1.jpg&resize.width=156&cropresize.height=192">

                <div>sdfdfd</div>
            </li>
            <li>
                <img alt="Ao Anakids 4" width="156" height="192"
                     src="http://cungshopping.com/product.image?timestamp=&path=/thoi-trang/product/prod_1.jpg&resize.width=156&cropresize.height=192">

                <div>sdfdfd</div>
            </li>
            <li>
                <img alt="Ao Anakids 4" width="156" height="192"
                     src="http://cungshopping.com/product.image?timestamp=&path=/thoi-trang/product/prod_1.jpg&resize.width=156&cropresize.height=192">

                <div>sdfdfd</div>
            </li>
            <li>
                <img alt="Ao Anakids 4" width="156" height="192"
                     src="http://cungshopping.com/product.image?timestamp=&path=/thoi-trang/product/prod_1.jpg&resize.width=156&cropresize.height=192">

                <div>sdfdfd</div>
            </li>
            <li>
                <img alt="Ao Anakids 4" width="156" height="192"
                     src="http://cungshopping.com/product.image?timestamp=&path=/thoi-trang/product/prod_1.jpg&resize.width=156&cropresize.height=192">

                <div>sdfdfd</div>
            </li>
            <li>
                <img alt="Ao Anakids 4" width="156" height="192"
                     src="http://cungshopping.com/product.image?timestamp=&path=/thoi-trang/product/prod_1.jpg&resize.width=156&cropresize.height=192">

                <div>sdfdfd</div>
            </li>
        </ul>
    </div>
    <a class="next" href="#">&nbsp;</a>

    <div class="clear"></div>
</div>
