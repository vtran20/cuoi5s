<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="/common.jspf" %>


<%--Don't need to use tag cache, we cached page level--%>
<%--<app:cache key="rewritten_uri:${uri}|page:section.jsp">--%>
<spring:eval expression="T(com.easysoft.ecommerce.web.filter.RewrittenURIHolder).getURI()" var="uri"/>
<c:set value="${fn:replace(uri,'/catalog/','')}" var="lastid"/>
<c:set value="${fn:split(lastid,'/')}" var="lastids"/>
<c:set value="${lastids[0]}" var="cataloguri"/>
<%--<c:if test="${! empty cataloguri}">--%>
<%--<c:set value="${fn:split(cataloguri,'-')}" var="list"/>--%>
<%--<c:set value="${list[fn:length(list)-1]}" var="catalogId"/>--%>
<%--<spring:eval expression="serviceLocator.getCatalogDao().findById(T(java.lang.Long).valueOf(catalogId))" var="catalog"/>--%>
<%--</c:if>--%>
<spring:eval expression="new com.easysoft.ecommerce.util.QueryString(pageContext.request.queryString, false)" var="params"/>

<html>
<head>
    <title></title>
    <%--<meta name="description" content="${catalog.description}"/>--%>
    <%--<meta name="keywords" content="${catalog.description}"/>--%>
    <%--<meta name="decorator" content="no_leftnav" />--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_css.css" type="text/css"/>
    <link rel="stylesheet" href="/themes/m3x/css/style.css" type="text/css"/>
    <link rel="stylesheet" href="/themes/m3x/css/shop.style.css" type="text/css"/>
    <%--(red, blue, orange, light, purple, aqua, brown, dark-blue, light-green, green)--%>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_${template.templateColorCode}.css" type="text/css"/>
    <link rel="stylesheet" href="/wro/${version}${template.templateCssCode}_${template.skinColor}-skin.css" type="text/css"/>
    <script src="/wro/${version}${template.templateCssCode}_js.js" type="text/javascript"></script>

    <%--<script type="text/javascript" src="//cdn.jsdelivr.net/jquery/2.1.3/jquery.min.js"></script>--%>
    <%--<script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/2.9.0/moment.min.js"></script>--%>
    <!-- Include Date Range Picker -->
    <%--<script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/1/daterangepicker.js"></script>--%>
    <%--<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/1/daterangepicker-bs3.css" />--%>
    <spring:eval expression="serviceLocator.getSiteTemplateDao().findUniqueBy('site.id', site.id)" var="template"/>
    <meta name="decorator" content=""/>

</head>

<body>

<div class="interactive-slider-v2">
    <div class="container">
        <h1>Sản Phẩm Mới</h1>
        <p>Bộ sưu tập sẽ ra mắt vào ngày 25/7/2016</p>
        <a class="btn-u btn-bordered btn-brd-hover" href=""> Chi Tiết</a>
    </div>
</div>

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<div class="container content">
    #if ($row.title && $row.showTitle == 'Y') <div class="headline"><h2>${row.title}</h2></div> #end
    <div class="illustration-v2">
        <ul class="list-inline owl-slider">
            #foreach( $content in $contents )
            #set($productImage = false)
            <li class="item">
                <div class="product-img">
                    #set($productImage = $priceMap[$content.id].productImage)
                    #set($imageUrl = "/assets/images/no_image.png")
                    #if ($productImage)
                    #set($imageUrl = "${imageServer}/get/${productImage.uri}.jpg?op=scale|x328&op=crop|0,0,262,328")
                    #end
                    <a href="/product/${content.uri}-${content.id}.html"><img class="full-width img-responsive" src="${imageUrl}" alt=""></a>
                    <a class="product-review" href="/product/${content.uri}-${content.id}.html">Quick review</a>
                </div>
                <div class="product-description product-description-brd">
                    <div class="overflow-h margin-bottom-5">
                        <div class="pull-left">
                            <h4 class="title-price"><a href="/product/${content.uri}-${content.id}.html">${content.name}</a></h4>
                        </div>
                    </div>
                    <div class="overflow-h margin-bottom-5">
                        <div class="product-price">
                            #set($money = $priceMap[$content.id].money)
                            #set($promo = $priceMap[$content.id].promo)
                            #if ($promo && $promo != '')
                            #if ($promo == $money)
                            <span class="title-price">${money}</span>
                            #end
                            #if ($promo != $money)
                            <span class="title-price">${promo}</span>
                            <span class="title-price line-through">${money}</span>
                            #end
                            #else
                            <span class="title-price">${money}</span>
                            #end
                        </div>
                    </div>
                </div>
            </li>
            #end
        </ul>
    </div>
</div>
<script type="text/javascript">
    jQuery(document).ready(function() {
        OwlCarousel.initOwlCarousel();
    });
</script>





<link rel="stylesheet" href="/themes/m3x/plugins/slippry-1.3.1/css/slippry.css" type="text/css"/>
<script src="/themes/m3x/plugins/slippry-1.3.1/js/slippry.min.js" type="text/javascript"></script>
<ul id="out-of-the-box-demo">
    <li>
        <a href="#1">
            <img src="http://image.mangchiase.com/get/ca704f97-106a-4d8a-9967-e2947226b01a.jpg?op=scale|1600x&op=crop|0,300,1600,500" alt="This is caption 1 <a href='#link'>Even with links!</a>">
        </a>
    </li>
    <li>
        <a href="#2">
            <img src="http://image.mangchiase.com/get/0344104d-9242-411c-878f-671366386c7a.jpg?op=scale|1600x&op=crop|0,600,1600,500"  alt="This is caption 2">
        </a>
    </li>
    <li>
        <a href="#3">
            <img src="http://image.mangchiase.com/get/ca704f97-106a-4d8a-9967-e2947226b01a.jpg?op=scale|1600x&op=crop|0,300,1600,500" alt="And this is some very long caption for slide 3. Yes, really long.">
        </a>
    </li>
    <li>
        <a href="#4">
            <img src="http://image.mangchiase.com/get/5f51f9ed-21ab-4964-815d-6a65f701ffbb.jpg?op=scale|1600x&op=crop|0,400,1600,500" alt="And this is some very long caption for slide 4.">
        </a>
    </li>
</ul>
<script>
    jQuery('#out-of-the-box-demo').slippry({
        adaptiveHeight:false
    });
</script>

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>






































<div class="container content">
    #if ($row.title && $row.showTitle == 'Y') <div class="headline"><h2>${row.title}</h2></div> #end
    <div class="illustration-v2 margin-bottom-60">
        <ul class="list-inline owl-slider">
            #foreach( $content in $contents )
            #set($productImage = false)
            <li class="item">
                <div class="product-img">
                    #set($productImage = $priceMap[$content.id].productImage)
                    #set($imageUrl = "/assets/images/no_image.png")
                    #if ($productImage)
                    #set($imageUrl = "${imageServer}/get/${productImage.uri}.jpg?op=scale|x328&op=crop|0,0,262,328")
                    #end
                    <a href="/product/${content.uri}-${content.id}.html"><img class="full-width img-responsive" src="${imageUrl}" alt=""></a>
                    <a class="product-review" href="/product/${content.uri}-${content.id}.html">Quick review</a>
                    <a class="add-to-cart" href="#"><i class="fa fa-shopping-cart"></i>Add to cart</a>
                </div>
                <div class="product-description product-description-brd">
                    <div class="overflow-h margin-bottom-5">
                        <div class="pull-left">
                            <h4 class="title-price"><a href="/product/${content.uri}-${content.id}.html">${content.name}</a></h4>
                        </div>
                    </div>
                    <div class="overflow-h margin-bottom-5">
                        <div class="product-price">
                            #set($money = $priceMap[$content.id].money)
                            #set($promo = $priceMap[$content.id].promo)
                            #if ($promo && $promo != '')
                            #if ($promo == $money)
                            <span class="title-price">${money}</span>
                            #end
                            #if ($promo != $money)
                            <span class="title-price">${promo}</span>
                            <span class="title-price line-through">${money}</span>
                            #end
                            #else
                            <span class="title-price">${money}</span>
                            #end
                        </div>
                    </div>
                </div>
            </li>
            #end
        </ul>
    </div>
</div>
<script type="text/javascript">
    jQuery(document).ready(function() {
        OwlCarousel.initOwlCarousel();
    });
</script>




<div class="container content">
<div class="row no-space-pricing pricing-mega-v1">
<div class="col-md-3 col-sm-6 hidden-sm hidden-xs">
    <div class="pricing hidden-area">
        <div class="pricing-head ">
            <h4 class="price">Phí Hàng Tháng</h4>
        </div>
        <ul class="pricing-content list-unstyled">
            <li class="bg-color">
                <i class="fa fa-cloud"></i>Dung Lượng Lưu Trữ
            </li>
            <li>
                <i class="fa fa-users"></i>Đơn Vị Dữ Liệu
            </li>
            <li class="bg-color">
                <i class="fa fa-external-link"></i>Băng Thông Không Giới Hạn
            </li>
            <li>
                <i class="fa fa-search"></i>Hỗ Trợ SEO
            </li>
            <li class="bg-color">
                <i class="fa fa-globe"></i> Sử Dụng Tên Miền Riêng
            </li>
            <li>
                <i class="fa fa-tablet"></i>Hiển Thị Tất Cả Thiết Bị
            </li>
            <li class="bg-color">
                <i class="fa fa-ban"></i>Bảo Mật
            </li>
            <li>
                <i class="fa fa-cloud-upload"></i>Lưu Trữ Tại Amazon Cloud
            </li>
            <li class="bg-color">
                <i class="fa fa-database"></i>Sao Lưu Dữ Liệu
            </li>
            <li>
                <i class="fa fa-shopping-cart"></i>Module Ecommerce
            </li>
            <li class="bg-color">
                <i class="fa fa-newspaper-o"></i>Module Tin Tức
            </li>
            <li>
                <i class="fa fa-picture-o"></i>Module Album/Gallery
            </li>
        </ul>
    </div>
</div>
<div class="col-md-3 col-sm-6 block">
    <div class="pricing">
        <div class="pricing-head">
            <h3>
                Basic
            </h3>
            <h4 class="price">
                99<i>.000</i><i>Đ</i>
                <span class="hidden-md hidden-lg">Phí Hàng Tháng</span>
            </h4>
        </div>
        <ul class="pricing-content list-unstyled ">
            <li class="bg-color">
                200 MB
                <span class="hidden-md hidden-lg">Dung Lượng Lưu Trữ</span>
            </li>
            <li>
                300 đơn vị<span class="hidden-md hidden-lg">Đơn Vị Dữ Liệu</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Băng Thông Không Giới Hạn</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Hỗ Trợ SEO</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Sử Dụng Tên Miền Riêng</span>
            </li>
            <li>
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Hiển Thị Tất Cả Thiết Bị</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Bảo Mật</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Lưu Trữ Tại Amazon Cloud</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Sao Lưu Dữ Liệu</span>
            </li>
            <li>
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Module Ecommerce</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Module Tin Tức</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Module Album/Gallery</span>
            </li>
        </ul>
        <div class="btn-group btn-group-justified">
            <a href="/site/dang-ky.html" type="button" class="btn-u btn-block">
                <i class="fa fa-shopping-cart"></i> Dùng Thử
            </a>

        </div>
    </div>
</div>
<div class="col-md-3 col-sm-6 block">
    <div class="pricing">
        <div class="pricing-head">
            <h3>
                Professional <span>(POPULAR)</span>
            </h3>
            <h4 class="price">
                199<i>.000</i><i>Đ</i>
                <span class="hidden-md hidden-lg">Phí Hàng Tháng</span>
            </h4>
        </div>
        <ul class="pricing-content list-unstyled ">
            <li class="bg-color">
                1 GB
                <span class="hidden-md hidden-lg">Dung Lượng Lưu Trữ</span>
            </li>
            <li>
                1000 đơn vị<span class="hidden-md hidden-lg">Đơn Vị Dữ Liệu</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Băng Thông Không Giới Hạn</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Hỗ Trợ SEO</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Sử Dụng Tên Miền Riêng</span>
            </li>
            <li>
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Hiển Thị Tất Cả Thiết Bị</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Bảo Mật</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Lưu Trữ Tại Amazon Cloud</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Sao Lưu Dữ Liệu</span>
            </li>
            <li>
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Module Ecommerce</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Module Tin Tức</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Module Album/Gallery</span>
            </li>
        </ul>
        <div class="btn-group btn-group-justified">
            <a href="/site/dang-ky.html" type="button" class="btn-u btn-block">
                <i class="fa fa-shopping-cart"></i> Dùng Thử
            </a>

        </div>
    </div>
</div>
<div class="col-md-3 col-sm-6 block">
    <div class="pricing">
        <div class="pricing-head">
            <h3>
                Unlimited
            </h3>
            <h4 class="price">
                399<i>.000</i><i>Đ</i>
                <span class="hidden-md hidden-lg">Phí Hàng Tháng</span>
            </h4>
        </div>
        <ul class="pricing-content list-unstyled ">
            <li class="bg-color">
                Không Giới Hạn
                <span class="hidden-md hidden-lg">Dung Lượng Lưu Trữ</span>
            </li>
            <li>
                Không Giới Hạn<span class="hidden-md hidden-lg">Đơn Vị Dữ Liệu</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Băng Thông Không Giới Hạn</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Hỗ Trợ SEO</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Sử Dụng Tên Miền Riêng</span>
            </li>
            <li>
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Hiển Thị Tất Cả Thiết Bị</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Bảo Mật</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Lưu Trữ Tại Amazon Cloud</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Sao Lưu Dữ Liệu</span>
            </li>
            <li>
                <i class="fa fa-check"></i><span class="hidden-md hidden-lg">Module Ecommerce</span>
            </li>
            <li class="bg-color">
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Module Tin Tức</span>
            </li>
            <li>
                <i class="fa fa-check"></i>
                <span class="hidden-md hidden-lg">Module Album/Gallery</span>
            </li>
        </ul>
        <div class="btn-group btn-group-justified">
            <a href="/site/dang-ky.html" type="button" class="btn-u btn-block">
                <i class="fa fa-shopping-cart"></i> Dùng Thử
            </a>

        </div>
    </div>
</div>


</div>
</div>

<div class="container content">
    <div class="headline"><h2>GALLERY</h2></div>     <div class="row">
    <div class="col-sm-3">
        <div class="thumbnails thumbnail-style thumbnail-kenburn">
            <div class="thumbnail-img">
                <div class="overflow-hidden">
                    <iframe src="//vimeo.com/channels/staffpicks/134616827" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
                </div>
                <a href="http://m12.webphattai.com/page/album.html?albumId=36" class="btn-more hover-effect">chi tiết +</a>                </div>
            <div class="caption">
                <h3><a href="http://m12.webphattai.com/page/album.html?albumId=36" class="hover-effect">Nail</a></h3>                                    </div>
        </div>
    </div>
    <div class="col-sm-3">
        <div class="thumbnails thumbnail-style thumbnail-kenburn">
            <div class="thumbnail-img">
                <div class="overflow-hidden">
                    <iframe src="//www.youtube.com/embed/1xhAEKX8w0k" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
                </div>
                <a href="http://m12.webphattai.com/page/album.html?albumId=41" class="btn-more hover-effect">chi tiết +</a>                </div>
            <div class="caption">
                <h3><a href="http://m12.webphattai.com/page/album.html?albumId=41" class="hover-effect">Full set</a></h3>                                    </div>
        </div>
    </div>
    <div class="col-sm-3">
        <div class="thumbnails thumbnail-style thumbnail-kenburn">
            <div class="thumbnail-img">
                <div class="overflow-hidden">
                    <img alt="" src="http://image.mangchiase.com/get/077f19d9-bbb5-46c4-9a30-ec48203022c2.jpg?op=scale|300&amp;op=crop|0,0,300,200" class="img-responsive">
                </div>
                <a href="http://m12.webphattai.com/page/album.html?albumId=42" class="btn-more hover-effect">chi tiết +</a>                </div>
            <div class="caption">
                <h3><a href="http://m12.webphattai.com/page/album.html?albumId=42" class="hover-effect">Pink and white</a></h3>                                    </div>
        </div>
    </div>
    <div class="col-sm-3">
        <div class="thumbnails thumbnail-style thumbnail-kenburn">
            <div class="thumbnail-img">
                <div class="overflow-hidden">
                    <img alt="" src="http://image.mangchiase.com/get/239a896c-27cb-466c-9d6c-51b5bc80b75e.jpg?op=scale|300&amp;op=crop|0,0,300,200" class="img-responsive">
                </div>
                <a href="http://m12.webphattai.com/page/album.html?albumId=39" class="btn-more hover-effect">chi tiết +</a>                </div>
            <div class="caption">
                <h3><a href="http://m12.webphattai.com/page/album.html?albumId=39" class="hover-effect">Foot</a></h3>                                    </div>
        </div>
    </div>
    <div class="global-clear"></div>
</div>
</div>
</body>
</html>
