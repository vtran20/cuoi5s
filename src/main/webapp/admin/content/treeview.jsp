<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>Image Management</title>
    <meta name="decorator" content="admin"/>
</head>

<body>

<sec:authentication property="principal.site" var="site"/>
<script type="text/javascript">
    $(function() {
        $("#tabs").tabs({
            ajaxOptions: {
                error: function(xhr, status, index, anchor) {
                    $(anchor.hash).html("Couldn't load this tab. We'll try to fix this as soon as possible.");
                }
            },
            cookie: {
                // store cookie for a day, without, it would be a session cookie
                expires: 1
            }
        });

        $("input:submit").button();

        $('.jstree').jScrollPane({showArrows:true, scrollbarWidth: 17, scrollbarMargin:50});


    });

    function listFiles(path, startWith, endWith) {
        $.get("/admin/content/viewfile.html", // your page to get
            { path: path, start: startWith, end: endWith}, // some params to pass it
                function(data) {
                    $("#image-thumbnail").html(data);
                });
        return true;
    }
</script>

<div id="tabs">
<ul>
    <li><a href="#tabs-1">General</a></li>
</ul>
<div id="tabs-1">
<div id="dialog-form">

<table style="width: 100%">
<tbody>
<tr>
<td>
    <spring:eval expression="serviceLocator.getCatalogDao().getAllCatalogsBySite(systemContext.site)" var="catalogs"/>

    <div style="margin-right: 5px; height: 600px;" class="common-sm-tab-box">
        <h5>Tree View</h5>

        <div class="jstree" id="treeview">
        <c:forEach items="${catalogs}" var="catalog">
            <ul>
                <li id='${catalog.uri}'>
                    <a href='#'>${catalog.name}</a>
                    <ul>
                        <li id="product-${catalog.uri}">
                            <a href='#'>Product</a>
                            <ul>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', '0,1,2,3,4,5,6,7,8,9','')">0-9</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'a','')">a</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'b','')">b</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'c','')">c</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'd','')">d</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'e','')">e</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'f','')">f</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'g','')">g</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'h','')">h</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'i','')">i</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'j','')">j</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'k','')">k</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'l','')">l</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'm','')">m</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'n','')">n</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'o','')">o</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'p','')">p</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'q','')">q</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'r','')">r</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 's','')">s</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 't','')">t</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'u','')">u</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'v','')">v</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'x','')">x</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'y','')">y</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/product', 'z','')">z</a></li>
                            </ul>
                        </li>
                        <li id="category">
                            <a href='#' onclick="listFiles('${catalog.uri}/category', '','')">Category</a>
                        </li>
                        <li id="assets">
                            <a href='#'>Assets</a>
                            <ul>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/assets/images','', '')">images</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/assets/css','', '')">css</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/assets/js','', '')">js</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/assets/pdfs','', '')">pdfs</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/assets/flash','', '')">flash/video</a></li>
                                <li><a href='#' onclick="listFiles('${catalog.uri}/assets/xml','', '')">xml</a></li>
                            </ul>
                        </li>
                    </ul>
                </li>
            </ul>
        </c:forEach>
        </div>
        <script>
            $(function () {
                $("#treeview").jstree({
//                    "core" : { "initially_open" : [ "catalog1","product" ] },
                    "plugins" : [ "themes", "html_data" ]
                });
            });
        </script>

    </div>

</td>
<td style="width: 700px;">
<div style="margin-right: 5px; height: 620px;" id="image-thumbnail" >

</div>

</td>
</tr>
</tbody>
</table>    
</div>
</div>

</div>


</body>
</html>
