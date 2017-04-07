<%@ page import="com.easysoft.ecommerce.service.ServiceLocatorHolder" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easysoft.ecommerce.model.*" %>
<%@ page import="org.apache.velocity.runtime.RuntimeServices" %>
<%@ page import="org.apache.velocity.runtime.RuntimeSingleton" %>
<%@ page import="java.io.StringReader" %>
<%@ page import="org.apache.velocity.runtime.parser.node.SimpleNode" %>
<%@ page import="org.apache.velocity.runtime.parser.ParseException" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.velocity.VelocityContext" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.easysoft.ecommerce.util.QueryString" %>

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
    <meta name="decorator" content="${template.templateCode}"/>

</head>

<body>
<br><br><br><br><br>
<%
    Calendar startDate = Calendar.getInstance();


    Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//    AlbumImage a = ServiceLocatorHolder.getServiceLocator().getAlbumImageDao().findById(100l, 29l);


//    Long productId = 696l;
//    //Delete Product Variant
//    List<ProductVariant> variants = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findAll(productId, "N");
//    for (ProductVariant variant : variants) {
//        ServiceLocatorHolder.getServiceLocator().getProductVariantDao().remove(variant);
//    }
//    //Delete Product File
//    List<ProductFile> productFiles = ServiceLocatorHolder.getServiceLocator().getProductFileDao().findBy("product.id", productId);
//    for (ProductFile productFile : productFiles) {
//        ServiceLocatorHolder.getServiceLocator().getProductFileDao().remove(productFile);
//    }
//    //Delete Product category
//    ServiceLocatorHolder.getServiceLocator().getCategoryDao().removeProductCategory(productId, site);
//
//    Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().findById(productId, site.getId());
//    if (product != null) {
//        ServiceLocatorHolder.getServiceLocator().getProductDao().remove(product);
//    }

    //    /* Derive the key, given password and salt. */
//    byte [] salt = new byte [8];
//    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//    KeySpec spec = new PBEKeySpec("abc".toCharArray(), salt, 65536, 256);
//    SecretKey tmp = factory.generateSecret(spec);
//    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
///* Encrypt the message. */
//    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//    cipher.init(Cipher.ENCRYPT_MODE, secret);
//    AlgorithmParameters params = cipher.getParameters();
//    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
//    byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));
//    out.print(new String(ciphertext, "UTF-8") +"<br>");

//    try {
//        String text = "Hello World";
//        String key = "9283wu827HS*&977"; // 128 bit key
//        String encoding = "UTF-8"; // 128 bit key
////        String encoding = null;
//        // Create key and cipher
//        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//
//        // encrypt the text
//        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
//        byte[] encrypted = cipher.doFinal(text.getBytes());
//        String output = new BASE64Encoder().encode(encrypted);
//
//        out.print(output + "<br>");
////        out.print(URLEncoder.encode(new BASE64Encoder().encode(encrypted), encoding) + "<br>");
////        out.print(URLDecoder.decode(new BASE64Encoder().encode(encrypted), encoding) + "<br>");
//
//        String fileName = request.getParameter("name");
////        out.print(fileName);
//        // decrypt the text
//        cipher.init(Cipher.DECRYPT_MODE, aesKey);
//        byte[] decordedValue = new BASE64Decoder().decodeBuffer(fileName);
//        String decrypted = new String(cipher.doFinal(decordedValue));
//        out.print(decrypted);
//    }catch(Exception e) {
//        e.printStackTrace();
//    }
//
//
    RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
    StringReader reader = new StringReader("" +
            "<div class=\"container content\">\n" +
            "    #if ($row.title && $row.showTitle == 'Y') <div class=\"headline\"><h2>${row.title}</h2></div> #end\n" +
            "    <div class=\"illustration-v2\">\n" +
            "        <ul class=\"list-inline owl-slider\">\n" +
            "            #foreach( $content in $contents )\n" +
            "            #set($productImage = false)\n" +
            "            <li class=\"item\">\n" +
            "                <div class=\"product-img\">\n" +
            "                    #set($productImage = $priceMap[$content.id].productImage)\n" +
            "                    #set($imageUrl = \"/assets/images/no_image.png\")\n" +
            "                    #if ($productImage)\n" +
            "                    #if ($productImage.crop && $productImage.crop != '')\n" +
            "                    #set($imageUrl = \"${imageServer}/get/${productImage.uri}.jpg?op=crop|$productImage.crop&op=scale|$widgetTemplate.imageSize\")\n" +
            "                    #else\n" +
            "                    #set($imageUrl = \"${imageServer}/get/${productImage.uri}.jpg?op=scale|262x328&op=crop|0,0,262,328\")\n" +
            "                    #end\n" +
            "                    #end\n" +
            "                    <a href=\"/product/${content.uri}-${content.id}.html\"><img class=\"full-width img-responsive\" src=\"${imageUrl}\" alt=\"\"></a>\n" +
            "                    <a class=\"product-review\" href=\"/product/${content.uri}-${content.id}.html\">Quick review</a>\n" +
            "                    #if ($content.newProduct == 'Y')<div class=\"shop-rgba-dark-green rgba-banner\">Sản Phẩm Mới</div>#end\n" +
            "                </div>\n" +
            "                <div class=\"product-description product-description-brd\">\n" +
            "                    <div class=\"overflow-h margin-bottom-5\">\n" +
            "                        <div class=\"pull-left\">\n" +
            "                            <h4 class=\"title-price\"><a href=\"/product/${content.uri}-${content.id}.html\">${content.name}</a></h4>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div class=\"overflow-h margin-bottom-5\">\n" +
            "                        <div class=\"product-price\">\n" +
            "                            #set($money = $priceMap[$content.id].money)\n" +
            "                            #set($promo = $priceMap[$content.id].promo)\n" +
            "                            #if ($promo && $promo != '')\n" +
            "                            #if ($promo == $money)\n" +
            "                            <span class=\"title-price\">${money}</span>\n" +
            "                            #end\n" +
            "                            #if ($promo != $money)\n" +
            "                            <span class=\"title-price\">${promo}</span>\n" +
            "                            <span class=\"title-price line-through\">${money}</span>\n" +
            "                            #end\n" +
            "                            #else\n" +
            "                            <span class=\"title-price\">${money}</span>\n" +
            "                            #end\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </li>\n" +
            "            #end\n" +
            "        </ul>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<script type=\"text/javascript\">\n" +
            "    jQuery(document).ready(function() {\n" +
            "        OwlCarousel.initOwlCarousel();\n" +
            "    });\n" +
            "</script>\n" +
            "" +
            "" +
            "");
    SimpleNode node = null;
    try {
        node = runtimeServices.parse(reader, "test");
    } catch (ParseException e) {
        e.printStackTrace();
    }

    org.apache.velocity.Template template = new org.apache.velocity.Template();
    template.setRuntimeServices(runtimeServices);
    template.setData(node);
    template.initDocument();
    template.setEncoding("UTF-8");

    StringWriter writer = new StringWriter();
//    List<SiteMenuPartContent> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getContentParts(20l);
    List<Product> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getProductContentParts(19l);
//    List<News> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getNewsContentParts(107l);
    Row row = ServiceLocatorHolder.getServiceLocator().getRowDao().findById(19l);
    WidgetTemplate widgetTemplate = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getWidgetTemplate(row.getId());

    Map priceMap = new HashMap();
    if (contentParts != null) {
        for (Object product: contentParts) {
            if (product instanceof Product) {
                Product prod = (Product) product;
                String money = com.easysoft.ecommerce.util.MoneyRange.valueOf(prod.getDisplayPrice(), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString();
                String promo = com.easysoft.ecommerce.util.MoneyRange.valueOf(prod.getDisplayPricePromo(), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString();
                ProductFile productImage = ServiceLocatorHolder.getServiceLocator().getProductFileDao().getDefaultImage(((Product) product).getId(), "PRODUCT_FILE_IMAGE");
                Map temp = new HashMap();
                temp.put("money", money);
                temp.put("promo", promo);
                temp.put("productImage", productImage);
                priceMap.put(prod.getId(), temp);
            }
        }
    }

    Map context = new HashMap();
    context.put("contents", contentParts);
    context.put("row", row);
    context.put("widgetTemplate", widgetTemplate);
    context.put("priceMap", priceMap);
    String imageServer = ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("image.server");
    context.put("imageServer",imageServer);
    VelocityContext velocityContext = new VelocityContext(context);
    template.merge(velocityContext, writer);
    out.print(writer.toString());

//    List<SiteMenuPartContent> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getContentParts(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite(), "CONTENT", "", "Y");
//    for (SiteMenuPartContent content: contentParts) {
//        String text = ServiceLocatorHolder.getServiceLocator().getContentService().merge(content);
//        out.print(text);
//    }

%>



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
    <div class="row">
        #foreach( $content in $contents )
        #set( $link = "/news/$content.uri" )
        <div class="col-sm-3">
            <div class="thumbnails thumbnail-style thumbnail-kenburn">
                #if ($content.thumbImg && $content.thumbImg != '')
                <div class="thumbnail-img">
                    <div class="overflow-hidden">
                        <img class="img-responsive" src="${content.thumbImg}?op=scale|600&op=crop|0,0,600,500" alt="">
                    </div>
                    <a class="btn-more hover-effect" href="$link">chi tiết +</a>
                </div>
                #end
                <div class="caption">
                    #if ($content.title && $content.title != '')<h3><a class="hover-effect" href="$link">${content.title}</a></h3>#end
                    <p>${content.shortDescription}</p>
                </div>
            </div>
        </div>
        #if ($foreach.count%4 == 0) <div class="global-clear"></div> #end
        #end
    </div>
</div>



<link rel="stylesheet" href="/themes/m3x/plugins/slippry-1.3.1/css/slippry.css" type="text/css"/>
<script src="/themes/m3x/plugins/slippry-1.3.1/js/slippry.min.js" type="text/javascript"></script>
<style>
    div.sy-box {
        margin-bottom: 30px;
    }
</style>
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
        adaptiveHeight:false,
        pager:false
    });
</script>






<c:set var="url" value="/product"/>
<%--<c:set var="url" value="${url}/${product.uri}-${product.id}.html"/>--%>
<c:url var="productUrl" value="${url}"/>
<style>
    .carousel-inner > .item > img {
        width: 100%;
    }
</style>
<div class="container content">
    <div class="row">
        <div id="myCarousel-1" class="carousel slide carousel-v1">
            <div class="carousel-inner">
                <div class="item">
                    <img src="http://htmlstream.com/preview/unify-v1.8/assets/img/main/img3.jpg" alt="">
                    <div class="carousel-caption">
                        <p>Facilisis odio, dapibus ac justo acilisis gestinas.</p>
                    </div>
                </div>
                <div class="item active">
                    <img src="http://htmlstream.com/preview/unify-v1.8/assets/img/main/img12.jpg" alt="">
                    <div class="carousel-caption">
                        <p>Cras justo odio, dapibus ac facilisis into egestas.</p>
                    </div>
                </div>
                <div class="item">
                    <img src="http://htmlstream.com/preview/unify-v1.8/assets/img/main/img6.jpg" alt="">
                    <div class="carousel-caption">
                        <p>Justo cras odio apibus ac afilisis lingestas de.</p>
                    </div>
                </div>
            </div>

            <div class="carousel-arrow">
                <a class="left carousel-control" href="#myCarousel-1" data-slide="prev">
                    <i class="fa fa-angle-left"></i>
                </a>
                <a class="right carousel-control" href="#myCarousel-1" data-slide="next">
                    <i class="fa fa-angle-right"></i>
                </a>
            </div>
        </div>
    </div>
</div>

</div><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>










<div class="container content">
    #if ($row.title && $row.showTitle == 'Y') <div class="headline"><h2>${row.title}</h2></div> #end
    #foreach( $content in $contents )
    <div class="row blog-page magin-bottom-30">
        #if ($content.imgUrl && $content.imgUrl != '')
        <div class="col-md-4">
            #if ($content.title && $content.title != '')
            #if ($content.link && $content.link != '')
            <div><h4><a href="$content.link">${content.title}</a></h4></div>
            #else
            <div><h4>${content.title}</h4></div>
            #end
            #end
            #if ($content.content && $content.content != '')${content.content}#end
        </div>
        <div class="col-md-8">
            #if ($content.imgUrl && $content.imgUrl != '')
            #if ($content.link && $content.link != '')
            <a href="$content.link"><img src="${content.imgUrl}" class="img-responsive margin-bottom-10" alt=""></a>
            #else
            <img src="${content.imgUrl}" class="img-responsive margin-bottom-10" alt="">
            #end
            #end
        </div>
        #else
        <div class="col-md-12">
            #if ($content.title && $content.title != '')
            #if ($content.link && $content.link != '')
            <div><h4><a href="$content.link">${content.title}</a></h4></div>
            #else
            <div><h4>${content.title}</h4></div>
            #end
            #end
            #if ($content.content)${content.content}#end
        </div>
        #end
    </div>
    #end
</div>

<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>

<div class="container content">

    <div class="illustration-v2 margin-bottom-60">
        <div class="headline"><h2>Our Clients</h2></div>
        <div class="owl-slider">
            <div class="item">
                <a href=""><img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt=""></a>
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <a href=""><img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt=""></a>
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/056c95bf-c0c5-47e9-8699-effd476f6fc0.jpg?op=scale|220&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <a href=""><img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt=""></a>
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/02b63d74-16c0-435d-a3ad-b66e7494f541.jpg?op=scale|220&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://image.mangchiase.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    jQuery(document).ready(function() {
        OwlCarousel.initOwlCarousel();
    });
</script>
<fmt:message key='site.template.does.not.existed'/>
<fmt:message key='message.product.is.invalid'/>
<fmt:message key='addedexistedadminuser.successfully.please.check.email'/>
<fmt:message key='addedadminuser.successfully.please.check.email'/>


        #if ($content.crop && $content.crop != '')
        background: rgba(0, 0, 0, 0) url("${content.imgUrl}?op=crop|$content.crop&op=scale|$widgetTemplate.imageSize") no-repeat scroll center center / cover ;
        #else
        background: rgba(0, 0, 0, 0) url("${content.imgUrl}") no-repeat scroll center center / cover ;
        #end



#if ($content.crop && $content.crop != '')
<div class="da-img"><img src="${content.imgUrl}?op=crop|$content.crop&op=scale|$widgetTemplate.imageSize" alt="" /></div>
#else
<div class="da-img"><img src="${content.imgUrl}?op=scale|600&op=crop|0,0,550,290" alt="" /></div>
#end

</body>
</html>
