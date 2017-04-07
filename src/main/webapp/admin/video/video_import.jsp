<%@ page import="com.easysoft.ecommerce.util.YoutubeService" %>
<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title><fmt:message key="template.admin.create.video"/></title>
    <meta name="decorator" content="admin_new"/>
</head>

<body>

<c:set var="productId" value="${param.id}"/>
<c:if test="${! empty productId}">
    <spring:eval expression="serviceLocator.productDao.findById(T(java.lang.Long).valueOf(productId))" var="product"/>
</c:if>

<c:set value="checked" var="activeChecked"/>
<c:if test="${! empty product.id}">
    <fmt:message key="template.admin.edit.product" var="formState"/>
    <c:set value="/admin/catalog/product_update.html" var="formUrl"/>
    <c:set value="${product.id}" var="productId"/>
    <c:set value="${product.uri}" var="uri"/>
    <c:set value=": ${product.name}" var="name"/>
    <c:if test="${fn:startsWith(product.active, 'N')}">
        <c:set value="" var="activeChecked"/>
    </c:if>

</c:if>
<c:if test="${empty product.id}">
    <fmt:message key="template.admin.create.product" var="formState"/>
    <c:set value="" var="name"/>
    <c:set value="/admin/catalog/product_insert.html" var="formUrl"/>
</c:if>

<!-- Bread Crumb Navigation -->
<div class="span10">
    <div>
        <ul class="breadcrumb">
            <li><a href="/admin/index.html"><fmt:message key="common.home"/></a> <span class="divider">/</span></li>
            <li class="active">Tìm kiếm video</li>
        </ul>
    </div>
    <div id="fb-root"></div>
    <h:frontendmessage _messages="${messages}"/>
    <div id="alert-message" class="alert hide"></div>

    <form name="form" id="form" class="form-horizontal" action="${formUrl}" method="post">
    <h:csrf/>
    <fieldset>

        <div class="control-group">
            <div class="controls">
                <input name="searchField" class="required input-xlarge" id="searchField" value="" type="text" autofocus="true"/>
                <button type="button" id="search" class="btn btn-success"><i class="icon-search icon-white"></i> <fmt:message key="search.button"/></button>
                <span class="help-inline">Nhập video Id và cách nhau bởi dấu phẩy.</span>
                <%--<%--%>
                    <%--String fbURL = "http://www.facebook.com/dialog/oauth?client_id=6753221261&redirect_uri=" + URLEncoder.encode("http://cuoi5s.com/admin/video/login_facebook.html", "UTF-8") + "&scope=email,publish_stream,create_event,manage_pages";--%>
                <%--%>--%>
            </div>
        </div>
        <div class="control-group">
        <div class="controls">
                <input name="searchQuery" class="required input-xxlarge" id="searchQuery" value="" type="text"/>
                <button type="button" id="searchQueryButton" class="btn btn-success"><i class="icon-search icon-white"></i> <fmt:message key="search.channel.button"/></button>
                <span class="help-inline">Query likes: channelId=UCl4QpyLOdX1WAoEMvfPl7Jg, playlistId=PLreE8l6UGfoPA-CcyIna4NfegdiTGTHxH</span>
                <span class="help-inline">Live videos: eventType=live&type=video&q=manchester</span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <%--<label class="checkbox">--%>
                    <span class="help-inline"><input type="radio" name="reference" value="search" checked/></span>
                    <span class="help-inline">Search</span>
                    <span class="help-inline"><input type="radio" name="reference" value="playlistItems"/></span>
                    <span class="help-inline">Playlist</span>
                <%--</label>--%>
            </div>
        </div>
    </fieldset>

    <%--Table--%>
    <div class="row-fluid">
        <div class="span9">
            <div class="row-fluid">
                <div class="span12">
                    <!-- Portlet: Member List -->
                    <div class="box" id="box-0">
                        <h4 class="box-header round-top"><fmt:message key="category.categories"/>
                            <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                            <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                            <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                                    class="icon-cog"></i></a>
                        </h4>
                        <spring:eval expression="serviceLocator.categoryDao.getRootCategories(site, false)" var="rootCategories"/>

                        <div class="box-container-toggle">
                            <div class="box-content">
                                <%--<button type="submit" class="btn btn-primary"><i class="icon-ok icon-white"></i> <fmt:message key="video.import"/></button>--%>
                                <c:if test="${!empty rootCategories}">

                                    <table cellpadding="0" cellspacing="0" border="0"
                                           class="table table-striped table-bordered bootstrap-datatable" id="video_search_results">
                                        <thead>
                                        <tr>
                                            <th><fmt:message key="category.name"/></th>
                                            <th><fmt:message key="common.status"/></th>
                                            <th><fmt:message key="common.actions"/> &nbsp;<button type='button' class='btn btn-success' onclick="importVideos(this)">Import All</button>
                                                &nbsp;<button type='button' class='btn btn-success' onclick="importLiveVideos(this)">Import Live Videos</button></th>
                                        </tr>
                                        </thead>
                                        <tbody>&nbsp;</tbody>
                                    </table>
                                </c:if>
                                <c:if test="${empty rootCategories}">
                                    <fmt:message key="category.is.empty"/>
                                </c:if>

                            </div>
                        </div>
                    </div>
                    <!--/span-->
                </div>
            </div>        </div>
        <div class="span3">
            <!-- Portlet: Member List -->
            <div class="box" id="box-1">
                <h4 class="box-header round-top"><fmt:message key="category.categories"/>
                    <a class="box-btn" title="close"><i class="icon-remove"></i></a>
                    <a class="box-btn" title="toggle"><i class="icon-minus"></i></a>
                    <a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i
                            class="icon-cog"></i></a>
                </h4>
                <spring:eval expression="serviceLocator.categoryDao.getRootCategories(site, false)" var="rootCategories"/>

                <div class="box-container-toggle">
                    <div class="box-content">
                        <c:if test="${!empty rootCategories}">

                            <table cellpadding="0" cellspacing="0" border="0"
                                   class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                                <thead>
                                <tr>
                                    <th><fmt:message key="category.name"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <%--Get all categories which the product belong--%>
                                <c:if test="${!empty productId}">
                                    <spring:eval expression="serviceLocator.categoryDao.getSubCategories(productId)" var="productCats"/>
                                </c:if>
                                <c:forEach items="${rootCategories}" var="currentCategory">
                                    <%--Handheld the check box of category if the product belong--%>
                                    <c:set value="" var="checkboxSelected"/>
                                    <c:if test="${(!empty productCats)}">
                                        <c:forEach items="${productCats}" var="productCat">
                                            <c:if test="${(productCat.id == currentCategory.id)}">
                                                <c:set value="checked" var="checkboxSelected"/>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                    <tr id="${currentCategory.id}-s${currentCategory.sequence}">
                                        <td><input type="checkbox" name="categoryId" value="${currentCategory.id}" ${checkboxSelected}/> <c:out value="${currentCategory.name}"/></td>
                                    </tr>
                                    <%--Get Category Level 2--%>
                                    <spring:eval expression="serviceLocator.categoryDao.findBy('parentCategory.id', currentCategory.id)" var="level2Categories"/>
                                    <c:if test="${! empty level2Categories}">
                                        <c:forEach items="${level2Categories}" var="level2Category">
                                            <%--Handheld the check box of category if the product belong--%>
                                            <c:set value="" var="checkboxSelected"/>
                                            <c:if test="${(!empty productCats)}">
                                                <c:forEach items="${productCats}" var="productCat">
                                                    <c:if test="${(productCat.id == level2Category.id)}">
                                                        <c:set value="checked" var="checkboxSelected"/>
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                            <tr id="${currentCategory.id}-${level2Category.id}-s${level2Category.sequence}">
                                                <td>---- <input type="checkbox" name="categoryId" value="${level2Category.id}" ${checkboxSelected}/> <c:out value="${level2Category.name}"/></td>
                                            </tr>


                                            <%--Get Category Level 3--%>
                                            <spring:eval expression="serviceLocator.categoryDao.findBy('parentCategory.id', level2Category.id)" var="level3Categories"/>
                                            <c:if test="${! empty level3Categories}">
                                                <c:forEach items="${level3Categories}" var="level3Category">
                                                    <%--Handheld the check box of category if the product belong--%>
                                                    <c:set value="" var="checkboxSelected"/>
                                                    <c:if test="${(!empty productCats)}">
                                                        <c:forEach items="${productCats}" var="productCat">
                                                            <c:if test="${(productCat.id == level3Category.id)}">
                                                                <c:set value="checked" var="checkboxSelected"/>
                                                            </c:if>
                                                        </c:forEach>
                                                    </c:if>
                                                    <tr id="${currentCategory.id}-${level2Category.id}-s${level2Category.sequence}-${level3Category.id}-s${level3Category.sequence}">
                                                        <td>-------- <input type="checkbox" name="categoryId" value="${level3Category.id}" ${checkboxSelected}/> <c:out value="${level3Category.name}"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>


                                        </c:forEach>
                                    </c:if>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty rootCategories}">
                            <fmt:message key="category.is.empty"/>
                        </c:if>

                    </div>
                </div>
            </div>
            <!--/span-->
        </div>
    </div>
    </form>

</div>

<%
    String APP_ID="6753221261";
    String App_secret="d83ecc7141ed682adc437feddbeb2d6b";

%>
<script>
    $(function () {

        $("#search").click(function() {
            var url = "https://www.googleapis.com/youtube/v3/videos?key=<%=YoutubeService.apiKey%>&part=snippet";
            url = url+"&id="+$('#searchField').val();

            $.getJSON( url, function( data ) {
                $('table#video_search_results tbody').replaceWith("");
                for(var i in data.items) {
                    tr = $('<tr/>');
                    tr.append("<td width='30%'><span>" + data.items[i].snippet.title + "</span></td>");
                    tr.append("<td><iframe id='ytplayer' type='text/html' width='200' src='http://www.youtube.com/embed/"+data.items[i].id+"' frameborder='0'></iframe></td>");
                    tr.append("<td class='center' id='"+data.items[i].id+"'><button type='button' class='btn btn-success' onclick='importVideo(\"" + data.items[i].id+"\", this)'>Import</button></td>");
                    $('table#video_search_results ').append(tr);
                }
            });

        });
        //Search video by channel
        $("#searchQueryButton").click(function() {
            //https://www.googleapis.com/youtube/v3/search?key=AIzaSyCjdekNBTgmJ3x6_hepaapf2BgQHE195Z4&part=snippet&channelId=UCl4QpyLOdX1WAoEMvfPl7Jg&maxResults=50
            action = $("input[name='reference']:checked").val();

            var url = "https://www.googleapis.com/youtube/v3/"+action+"?key=<%=YoutubeService.apiKey%>&part=snippet&maxResults=50&";
            url = url+$('#searchQuery').val();

            $.getJSON( url, function( data ) {
                $('table#video_search_results tbody').replaceWith("");
                for(var i in data.items) {
                    videoId = "";
                    if (action == 'playlistItems') {
                        videoId = data.items[i].snippet.resourceId.videoId;
                    } else {
                        videoId = data.items[i].id.videoId;
                    }
                    tr = $('<tr/>');
                    tr.append("<td width='30%'><span>" + data.items[i].snippet.title + "</span></td>");
                    tr.append("<td><iframe id='ytplayer' type='text/html' width='200' src='http://www.youtube.com/embed/"+videoId+"' frameborder='0'></iframe></td>");
                    tr.append("<td class='center' id='"+videoId+"'><input type='checkbox' name='videoId' value='"+videoId+"'/>&nbsp;&nbsp;<button type='button' class='btn btn-success' onclick='importVideo(\"" + videoId+"\", this)'>Import + Facebook</button></td>");
                    $('table#video_search_results ').append(tr);
                }
            });

        });

    });

    function importVideo (videoId, btn) {
        //action = $("input[name='categoryId']:checked").val();
        category = "";
        $("input[name='categoryId']:checked").each(function() {
            if (category == "") {
                category = $(this).val();
            } else {
                category = category +","+$(this).val();
            }
        });

        if (category == "") {
            alert ("Please select categories");
            return;
        }
        //Logged in
        var name = prompt("Please enter video name", "");

        var url = "/admin/video/import/"+videoId+".json?name="+name+"&categoryIds="+category;
        $.getJSON( url, function( data ) {
            postToPage(data, btn, name)
        });
    }

    function importVideos (btn) {
        //action = $("input[name='categoryId']:checked").val();
        category = "";
        $("input[name='categoryId']:checked").each(function() {
            if (category == "") {
                category = $(this).val();
            } else {
                category = category +","+$(this).val();
            }
        });
        if (category == "") {
            alert ("Please select categories");
            return;
        }
        ids = "";
        $("input[name='videoId']:checked").each(function() {
            if (ids == "") {
                ids = $(this).val();
            } else {
                ids = ids +","+$(this).val();
            }
        });
        if (ids == "") {
            alert ("Please select videos");
            return;
        }
        var url = "/admin/video/import.json?categoryIds="+category+"&ids="+ids;
        $.getJSON( url, function( data ) {
            $(btn).parent().append(data.status);
        });
    }
    function importLiveVideos (btn) {
        //action = $("input[name='categoryId']:checked").val();
//        category = "";
//        $("input[name='categoryId']:checked").each(function() {
//            if (category == "") {
//                category = $(this).val();
//            } else {
//                category = category +","+$(this).val();
//            }
//        });
//        if (category == "") {
//            alert ("Please select categories");
//            return;
//        }
        var name = prompt("Please enter video name", "");
        ids = "";
        $("input[name='videoId']:checked").each(function() {
            if (ids == "") {
                ids = $(this).val();
            } else {
                ids = ids +","+$(this).val();
            }
        });
        if (ids == "") {
            alert ("Please select videos");
            return;
        }
        var url = "/admin/video/importLive.json?name="+name+"&ids="+ids;
        $.getJSON( url, function( data ) {
            $(btn).parent().append(data.status);
        });
    }
    // This is called with the results from from FB.getLoginStatus().
    // This function is called when someone finishes with the Login
    // Button.  See the onlogin handler attached to it in the sample
    // code below.
    function checkLoginState() {
        FB.getLoginStatus(function(response) {
            if (response.status === 'connected') {
                // Logged into your app and Facebook.
                return 0;
            } else if (response.status === 'not_authorized') {
                // The person is logged into Facebook, but not your app.
//                    document.getElementById('status').innerHTML = 'Please log ' +
//                            'into this app.';
                FB.login(function(response) {
                    // handle the response
                }, {scope: 'public_profile,email,create_event,manage_pages'});
                return 1;
            } else {
                // The person is not logged into Facebook, so we're not sure if
                // they are logged into this app or not.
                FB.login(function(response) {
                    // handle the response
                }, {scope: 'public_profile,email,create_event,manage_pages'});
                return 2;
            }
        });
    }

    window.fbAsyncInit = function() {
        FB.init({
            appId      : '<%=APP_ID%>',
            status     : true,
            cookie     : true,
            xfbml      : true,
            oauth      : true,
            version    : 'v2.0'
        });

        checkLoginState();
    };

    (function(d, s, id){
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {return;}
        js = d.createElement(s); js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    function postToPage(data, btn, title) {
        var page_id = '621494081266644';

    var url = 'http://${site.domain}/video/'+data.video.uri+'/'+data.video.id+'.html?videoId='+data.video.videoId;
//        var url = 'www.youtube.com/watch?v='+data.video.videoId;
//                console.log (url);
        FB.api('/' + page_id, {fields: 'access_token'}, function(resp) {
            if(resp.access_token) {
                FB.api('/' + page_id + '/feed',
                        'post',
                        { message: title, link: url,access_token: resp.access_token }
                        ,function(response) {
                            if (response && !response.error) {
                                $(btn).parent().append(data.status);
                            } else {
                                $(btn).parent().append("fail");
                            }
                        });
            }
        });
    }
</script>

</body>
</html>