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
</head>

<body>

<%
    Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//    AlbumImage a = ServiceLocatorHolder.getServiceLocator().getAlbumImageDao().findById(100l, 29l);
//    SiteMenuPartContent a = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().findById(36l, 29l);


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
            "#foreach( $content in $contents )\n" +
            "<div class=\"container content\">\n" +
            "            #if ($row.title) <div class=\"headline\"><h2>${row.title}</h2></div> #end\n" +
            "    <div class=\"row blog-page magin-bottom-30\">\n" +
            "        #if ($content.imgUrl)\n" +
            "        <div class=\"col-md-5\">\n" +
            "            #if ($content.title)<div><h4>${content.title}</h4></div>#end\n" +
            "            #if ($content.content)${content.content}#end\n" +
            "        </div>\n" +
            "        <div class=\"col-md-7\">\n" +
            "            #if ($content.imgUrl)<img src=\"${content.imgUrl}\" class=\"img-responsive margin-bottom-10\" alt=\"\">#end\n" +
            "        </div>\n" +
            "        #end\n" +
            "        #if ($content.imgUrl == \"\")\n" +
            "        <div class=\"col-md-12\">\n" +
            "            #if ($content.header)<div class=\"headline\"><h2>${content.header}</h2></div>#end\n" +
            "            #if ($content.title)<div><h4>${content.title}</h4></div>#end\n" +
            "            #if ($content.content)${content.content}#end\n" +
            "        </div>\n" +
            "        #end\n" +
            "    </div>\n" +
            "</div>\n" +
            "#end" +
            "" +
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
    List<SiteMenuPartContent> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getContentParts(5l);
    Row row = ServiceLocatorHolder.getServiceLocator().getRowDao().findById(5l);
    Map context = new HashMap();
    context.put("contents", contentParts);
    context.put("row", row);
    VelocityContext velocityContext = new VelocityContext(context);
    template.merge(velocityContext, writer);
    out.print(writer.toString());

//    List<SiteMenuPartContent> contentParts = ServiceLocatorHolder.getServiceLocator().getSiteMenuPartContentDao().getContentParts(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite(), "CONTENT", "", "Y");
//    for (SiteMenuPartContent content: contentParts) {
//        String text = ServiceLocatorHolder.getServiceLocator().getContentService().merge(content);
//        out.print(text);
//    }

%>
==========

#foreach( $content in $contents )
<div class="container content">
    <div class="row blog-page magin-bottom-30">
        #if ($content.imgUrl)
        <div class="col-md-5">
            #if ($content.header)<div class="headline"><h2>${content.header}</h2></div>#end
            #if ($content.title)<div><h4>${content.title}</h4></div>#end
            #if ($content.content)${content.content}#end
        </div>
        <div class="col-md-7">
            #if ($content.imgUrl)<img src="${content.imgUrl}" class="img-responsive margin-bottom-10" alt="">#end
        </div>
        #end
        #if ($content.imgUrl == "")
        <div class="col-md-12">
            #if ($content.header)<div class="headline"><h2>${content.header}</h2></div>#end
            #if ($content.title)<div><h4>${content.title}</h4></div>#end
            #if ($content.content)${content.content}#end
        </div>
        #end
    </div>
</div>
#end



<%--<spring:eval expression="serviceLocator.getSiteMenuPartContentDao().getContentParts(uri, 'Y')" var="contentParts"/>--%>

------------

<div class="leftnav">
    <ul id="left-menu" class="left-menu">
        <li>
            <a class="m1" href="/content/cau-hoi-thuong-gap.html">Câu hỏi thường gặp</a>
        </li>
        <li>
            <a class="m1" href="/content/huong-dan-mua-hang.html">Hướng dẫn mua hàng</a>
        </li>
        <li>
            <a class="m2" href="/content/phuong-thuc-thanh-toan.html">Phương thức thanh toán</a>
            <ul id="left-menu-2">
                <li><a href="/content/phuong-thuc-thanh-toan.html#ngan-luong">Ngân lượng</a>
                </li>
                <li><a href="/content/phuong-thuc-thanh-toan.html#bao-kim">Bảo kim</a>
                </li>
                <li><a href="/content/phuong-thuc-thanh-toan.html#chuyen-khoan">Chuyển khoản</a>
                </li>
            </ul>
        </li>
        <li>
            <a class="m3" href="/content/phuong-thuc-van-chuyen.html">Phương thức vận chuyển</a>
        </li>
        <li>
            <a class="m4" href="/content/doi-tra-hang.html">Đổi/trả hàng</a>
        </li>
    </ul>
</div>

<div class='page-body body-with-border page-body-float-left'>

    <div id="thumbnail-height">

        <div class="catalog-category-breadcrumb">

            <c:set value="/" var="homeurl"/>
            <a href="${homeurl}"><fmt:message key="breadcrumb.home"/></a>&nbsp;&gt;&nbsp;
            This is testing

        </div>
        <div class="catalog-thumbnail">
            <!--Start CMS content-->


            <style>
                .cms-content {
                    margin: 10px 10px 10px 10px;
                }
                .cms-content h1, .cms-content h2, .cms-content h3, .cms-content h4, .cms-content h5, .cms-content h6 {
                    color:#DF0403;
                }
                .cms-content h2 {
                    padding: 10px 0 10px 0;
                }
                .cms-content h3 {
                    padding: 10px 0 10px 0;
                }
                .cms-content p {
                    padding: 0 0 15px 0;
                    line-height: 20px;
                }
                .cms-content ul {
                    display: block;
                    list-style-type: square;
                    padding: 0 0 10px 40px;
                    line-height: 20px;
                }

            </style>

            <div class="cms-content">
                <h2>Chính sách bảo mật</h2>
                <p>
                    Cùng shopping (cungshopping.com) đưa ra một số nguyên tắc bảo mật thông tin riêng tư. Chính sách có thể được sửa đổi, bổ sung, điều chỉnh tuỳ từng thời điểm, tuỳ thuộc vào sự phát triển của dịch vụ trên Mạng và sự thay đổi của hệ thống pháp luật liên quan. Vì vậy vui lòng kiểm tra/tham khảo Chính sách này thường xuyên. Việc thay đổi, bổ sung, điều chỉnh sẽ có hiệu lực ngay khi được đăng tải trên Website.
                </p>
                <p>
                    Khi khách hàng đăng ký sử dụng dịch vụ, khách hàng/người sử dụng được coi là đã chấp thuận Chính sách.
                </p>
                <h3>1. Về việc thu thập thông tin cá nhân</h3>
                <p>
                    Việc thu thập thông tin sẽ được thực hiện bằng cách thu thập, lưu giữ lại thông tin của Bạn khi Bạn đăng ký sử dụng các dịch vụ Cùng Shopping.
                </p>
                <p>
                    Khi Bạn đăng ký sử dụng Mạng, Bạn sẽ được yêu cầu cung cấp các thông tin cá nhân của Bạn bao gồm họ tên, địa chỉ email. Ngoài ra bạn còn được yêu cầu cung cấp thêm một số thông tin như địa chỉ, số điện thoại trong trường hợp Bạn đặt hàng trên hệ thống của chúng tôi. Thông tin này sẽ được lưu trữ lại trong hệ thống nhằm phục vụ Bạn tốt hơn.
                </p>
                <h3>2. Chia sẻ thông tin</h3>
                <p>
                    Bảo mật thông tin của Bạn là ưu tiên số một, có ý nghĩa thực sự quan trọng trong việc cung cấp dịch vụ của Mạng. Chỉ những thông tin sau đây có thể được chia sẻ với những điều kiện nhất định.
                </p>
                <p>
                    Những thông tin cá nhân của Bạn có thể sẽ được chia sẻ cho bên thứ ba trong những trường hợp sau:
                </p>
                <ul>
                    <li>Khi có sự đồng ý chia sẻ thông tin đó của Bạn.</li>
                    <li>Khi những thông tin đó được chia sẻ để cung cấp dịch vụ và/hoặc sản phẩm theo yêu cầu của Bạn.</li>
                    <li>Khi những thông tin đó cần có để thực hiện kết nối với các công ty, tổ chức đối tác cung cấp sản phẩm và/hoặc dịch vụ cho Bạn.</li>
                    <li>Khi có sự yêu cầu của cơ quan pháp luật có thẩm quyền.</li>
                    <li>Khi những thông tin đó cần cung cấp để thực hiện việc bảo vệ quyền lợi, tài sản hoặc an toàn của Mạng, của khách hàng, cá nhân/tổ chức khác có liên quan trong việc trao đổi thông tin để phòng chống gian lận và giảm rủi ro.</li>
                    <li>Khi những thông tin đó cần cung cấp để cần đối chiếu hoặc làm rõ thông tin với bên thứ ba để bảo đảm sự chính xác thông tin.</li>
                </ul>
                <h3>3. An toàn thông tin</h3>
                <p>
                    Để đảm bảo được bảo mật thông tin, thì an toàn thông tin là điều kiện tiên quyết. Chúng tôi không lưu trữ bất kỳ thông tin liên quan đến thẻ tín dụng/thẻ ghi nợ mà khách hàng sử dụng trong quá trình giao dich. Mật khẩu được mã hóa trước khi lưu vào hệ thống của chúng tôi.
                </p>
                <p>
                    Bên cạnh đó Bạn cũng góp phần quan trọng trong việc bảo vệ thông tin cá nhân. Bạn sẽ hoàn toàn chịu trách nhiệm trong việc lưu giữ an toàn Tên sử dụng và Mật khẩu được cung cấp để truy cập Mạng. Bạn phải luôn cẩn trọng, ý thức đầy đủ và có trách nhiệm trong việc sử dụng và tiết lộ những thông tin này. Bạn cũng phải mọi chịu trách nhiệm khi có bất kỳ hậu quả gì xảy ra do việc tiết lộ/để lộ thông tin này.
                </p>
                <h3>4. Thu thập và thay đổi thông tin cá nhân</h3>
                <p>
                    Nếu thông tin cá nhân của Bạn có sự thay đổi hoặc nếu Bạn không muốn sử dụng dịch vụ của Cùng Shopping, Bạn có thể sửa, cập nhật, xóa thông tin bằng cách truy cập vào Website hoặc liên hệ với Bộ phận Chăm sóc Khách hàng.
                </p>
            </div>

            <div class="clr"></div>
        </div>
    </div>
</div>
</body>
</html>
<%--</app:cache>--%>



<form id="fileupload" action="${imageServer}/images/uploads.json" method="POST" enctype="multipart/form-data">

    <input type="hidden" name="path" value="${site.siteCode}">
    <input type="hidden" name="imagesPathOverride" value="${site.siteCode}">
    <input type="hidden" name="generateName" value="on">
    <input type="hidden" name="skipOptimization" value="on">

    <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
    <div class="box-content fileupload-buttonbar">
        <div class="span7">
            <!-- The fileinput-button span is used to style the file input field as button -->
                    <span class="btn btn-success fileinput-button">
                        <i class="icon-plus icon-white"></i>
                        <span>Add files...</span>
                        <input type="file" name="files[]" multiple="">
                    </span>
            <button type="submit" class="btn btn-primary start">
                <i class="icon-upload icon-white"></i>
                <span>Start upload</span>
            </button>
        </div>
        <!-- The global progress information -->
    </div>
    <!-- The loading indicator is shown during file processing -->
    <div class="fileupload-loading"></div>
    <br>
    <!-- The table listing the files available for upload/download -->
    <table role="presentation" class="table table-striped">
        <tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
    </table>
</form>

<fmt:message key="order.change.status.invalid"/>

<%--<jsp:useBean id="content" class="java.util.HashMap" scope="request"/>--%>
<%--<c:set target="${content}" property="imgUrl" value="http://google.com"/>--%>
<%----%>
<%--<spring:eval expression="serviceLocator.getContentService().merge(content, 127)" var="text"/>--%>
<%--${text}--%>
------------

<%--<input type="text" id="daterange" name="daterange" value="01/01/2015 - 01/31/2015">--%>
<%--<script type="text/javascript">--%>
<%--$(function() {--%>
<%--$('input[name="daterange"]').daterangepicker();--%>
<%--});--%>
<%--</script>--%>