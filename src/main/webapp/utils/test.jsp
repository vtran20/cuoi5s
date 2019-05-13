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
    <%--<meta name="decorator" content="${template.templateCode}"/>--%>

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
    StringReader reader = new StringReader("#set( $rowTitle = \"About Us\" )\n" +
            "#if ($row.title && $row.showTitle == 'Y') #set( $rowTitle = $row.title ) #end\n" +
            "#foreach( $content in $contents )\n" +
            "#set( $crop = \"\" )\n" +
            "#if ($content.crop && $content.crop != '')\n" +
            "#set( $crop = \"?op=crop|$content.crop\" )\n" +
            "#end\n" +
            "#set( $imageUrl = \"http://images.webphattai.com/get/49318030-7047-409c-92c9-7aa62946522d.jpg\")\n" +
            "#if ($content.imgUrl && $content.imgUrl != '')\n" +
            "#set( $imageUrl = \"$content.imgUrl?op=crop|$content.crop&op=scale|1200\" )\n" +
            "#end\n" +
            "#set( $contentTitle = \"\" )\n" +
            "#if ($content.title) #set( $contentTitle = $content.title ) #end\n" +
            "#set( $contentContent = \"\" )\n" +
            "#if ($content.content) #set( $contentContent = $content.content) #end\n" +
            "\n" +
            "<section id=\"about\" class=\"g-pb-80\">\n" +
            "    <div class=\"container-fluid px-0\">\n" +
            "        <div class=\"row no-gutters\">\n" +
            "            <div class=\"col-md-6 g-bg-img-hero g-min-height-400\" style=\"background-image: url($imageUrl);\"></div>\n" +
            "            <div class=\"col-md-6 d-flex align-items-center text-center g-pa-50\">\n" +
            "                <div class=\"w-100\">\n" +
            "                    <div class=\"g-mb-25\">\n" +
            "                        <h4 class=\"g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25\">$rowTitle</h4>\n" +
            "                        <h2 class=\"text-uppercase g-font-weight-600 g-font-size-22 mb-0\">$contentTitle</h2>\n" +
            "                    </div>\n" +
            "\n" +
            "                    <p class=\"g-mb-35\">$contentContent</p>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"row no-gutters\">\n" +
            "            <div class=\"col-md-6\">\n" +
            "                <img class=\"img-fluid\" src=\"http://images.webphattai.com/get/25b24315-8936-40f2-9e87-34e143ccd62d.jpg?op=scale|1200\" alt=\"Image description\">\n" +
            "            </div>\n" +
            "            <div class=\"col-md-6\">\n" +
            "                <div class=\"js-carousel\"\n" +
            "                     data-infinite=\"true\"\n" +
            "                     data-arrows-classes=\"u-arrow-v1 g-absolute-centered--y g-width-45 g-height-55 g-font-size-12 g-theme-color-gray-dark-v1 g-bg-white g-mt-minus-10\"\n" +
            "                     data-arrow-left-classes=\"fa fa-chevron-left g-left-0\"\n" +
            "                     data-arrow-right-classes=\"fa fa-chevron-right g-right-0\">\n" +
            "                    <div class=\"js-slide\">\n" +
            "                        <img class=\"img-fluid\" src=\"http://images.webphattai.com/get/082cc9a4-a605-489c-a192-d796336b531d.jpg?op=scale|1200\" alt=\"Image description\">\n" +
            "                    </div>\n" +
            "                    <div class=\"js-slide\">\n" +
            "                        <img class=\"img-fluid\" src=\"http://images.webphattai.com/get/939f8d93-73b0-4063-9b01-931bdad6697c.jpg?op=scale|1200\" alt=\"Image description\">\n" +
            "                    </div>\n" +
            "                    <div class=\"js-slide\">\n" +
            "                        <img class=\"img-fluid\" src=\"http://images.webphattai.com/get/9c2e026d-43e2-493a-a0d8-005a9b2f43db.jpg?op=scale|1200\" alt=\"Image description\">\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</section>\n" +
            "#end");
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
    List<SiteMenuPartContent> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getContentParts(169l);
//    List<Product> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getProductContentParts(19l);
//    List<News> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getNewsContentParts(107l);
    Row row = ServiceLocatorHolder.getServiceLocator().getRowDao().findById(169l);
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
            <img src="http://images.webphattai.com/get/ca704f97-106a-4d8a-9967-e2947226b01a.jpg?op=scale|1600x&op=crop|0,300,1600,500" alt="This is caption 1 <a href='#link'>Even with links!</a>">
        </a>
    </li>
    <li>
        <a href="#2">
            <img src="http://images.webphattai.com/get/0344104d-9242-411c-878f-671366386c7a.jpg?op=scale|1600x&op=crop|0,600,1600,500"  alt="This is caption 2">
        </a>
    </li>
    <li>
        <a href="#3">
            <img src="http://images.webphattai.com/get/ca704f97-106a-4d8a-9967-e2947226b01a.jpg?op=scale|1600x&op=crop|0,300,1600,500" alt="And this is some very long caption for slide 3. Yes, really long.">
        </a>
    </li>
    <li>
        <a href="#4">
            <img src="http://images.webphattai.com/get/5f51f9ed-21ab-4964-815d-6a65f701ffbb.jpg?op=scale|1600x&op=crop|0,400,1600,500" alt="And this is some very long caption for slide 4.">
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
                <a href=""><img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt=""></a>
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <a href=""><img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt=""></a>
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/056c95bf-c0c5-47e9-8699-effd476f6fc0.jpg?op=scale|220&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <a href=""><img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt=""></a>
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|220&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/02b63d74-16c0-435d-a3ad-b66e7494f541.jpg?op=scale|220&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
            </div>
            <div class="item">
                <img src="http://images.webphattai.com/get/30fbfc12-91f3-4323-960c-13161ce57b55.jpg?op=scale|200&op=crop|0,0,200,150" alt="">
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

#foreach( $content in $contents )
#set( $crop = "" )
#if ($content.crop && $content.crop != '')
#set( $crop = "?op=crop|$content.crop" )
#end
<section id="home" class="u-bg-overlay g-height-100vh g-min-height-600 g-bg-img-hero g-bg-black-opacity-0_3--after" style="background-image: url($content.imgUrl$crop);">
    <div class="u-bg-overlay__inner g-absolute-centered--y w-100">
        <div class="container text-center g-max-width-750">
            #if ($content.title && $content.title != '')
            <h1 class="g-line-height-1_5 g-font-weight-700 g-font-size-50 g-color-white g-mb-15">$content.title</h1>
            #end
            <a class="btn btn-md text-uppercase u-btn-primary g-font-weight-700 g-font-size-11 g-brd-none rounded-0 g-py-10 g-px-25" href="#booking">Make Appointment</a>
        </div>
    </div>
</section>
#end

#set( $rowTitle = "About Us" )
#if ($row.title && $row.showTitle == 'Y') #set( $rowTitle = $row.title ) #end
#foreach( $content in $contents )
#set( $crop = "" )
#if ($content.crop && $content.crop != '')
#set( $crop = "?op=crop|$content.crop" )
#end
#set( $imageUrl = "http://images.webphattai.com/get/49318030-7047-409c-92c9-7aa62946522d.jpg")
#if ($content.imgUrl && $content.imgUrl != '')
#set( $imageUrl = "$content.imgUrl?op=crop|$content.crop&op=scale|1200" )
#end
#set( $contentTitle = "" )
#if ($content.title) #set( $contentTitle = $content.title ) #end
#set( $contentContent = "" )
#if ($content.content) #set( $contentContent = $content.content) #end

<section id="about" class="g-pb-80">
    <div class="container-fluid px-0">
        <div class="row no-gutters">
            <div class="col-md-6 g-bg-img-hero g-min-height-400" style="background-image: url($imageUrl);"></div>
            <div class="col-md-6 d-flex align-items-center text-center g-pa-50">
                <div class="w-100">
                    <div class="g-mb-25">
                        <h4 class="g-font-weight-700 g-font-size-20 g-theme-h-v1 g-color-primary g-mb-25">$rowTitle</h4>
                        <h2 class="text-uppercase g-font-weight-600 g-font-size-22 mb-0">$contentTitle</h2>
                    </div>

                    <p class="g-mb-35">$contentContent</p>
                </div>
            </div>
        </div>
        <div class="row no-gutters">
            <div class="col-md-6">
                <img class="img-fluid" src="http://images.webphattai.com/get/25b24315-8936-40f2-9e87-34e143ccd62d.jpg?op=scale|1200" alt="Image description">
            </div>
            <div class="col-md-6">
                <div class="js-carousel"
                     data-infinite="true"
                     data-arrows-classes="u-arrow-v1 g-absolute-centered--y g-width-45 g-height-55 g-font-size-12 g-theme-color-gray-dark-v1 g-bg-white g-mt-minus-10"
                     data-arrow-left-classes="fa fa-chevron-left g-left-0"
                     data-arrow-right-classes="fa fa-chevron-right g-right-0">
                    <div class="js-slide">
                        <img class="img-fluid" src="http://images.webphattai.com/get/082cc9a4-a605-489c-a192-d796336b531d.jpg?op=scale|1200" alt="Image description">
                    </div>
                    <div class="js-slide">
                        <img class="img-fluid" src="http://images.webphattai.com/get/939f8d93-73b0-4063-9b01-931bdad6697c.jpg?op=scale|1200" alt="Image description">
                    </div>
                    <div class="js-slide">
                        <img class="img-fluid" src="http://images.webphattai.com/get/9c2e026d-43e2-493a-a0d8-005a9b2f43db.jpg?op=scale|1200" alt="Image description">
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
#end

</body>
</html>
