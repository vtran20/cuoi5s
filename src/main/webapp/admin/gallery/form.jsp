<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="albumId" value="${param.id}"/>
<c:if test="${! empty albumId}">
    <spring:eval expression="serviceLocator.albumDao.findById(T(java.lang.Long).valueOf(albumId))" var="album"/>
</c:if>

<c:set value="checked" var="activeChecked"/>
<c:if test="${! empty album.id}">
    <c:set value="Update album <b>${album.name}</b>" var="formState"/>
    <c:set value="${album.id}" var="albumId"/>
    <c:set value="${album.uri}" var="uri"/>
    <c:if test="${fn:startsWith(album.active, 'N')}">
        <c:set value="" var="activeChecked"/>
    </c:if>

</c:if>
<c:if test="${empty album.id}">
    <c:set value="Add new album" var="formState"/>
    <c:set value="checked" var="activeChecked"/>
</c:if>
<script type="text/javascript">
    $(function () {

        $("#form").validate({
//            rules: {
//                name: "required"
//                uri: "required"
//            },

            messages:{
                name:"<fmt:message key="gallery.album.name.required"/>"
            },
            highlight:function (label) {
                $(label).closest('.control-group').removeClass('success');
                $(label).closest('.control-group').addClass('error');
            },
            success:function (label) {
                $(label).closest('.control-group').removeClass('error');
                $(label).closest('.control-group').addClass('success');
            }
        });

        <%--$('#form input[name="name"]').blur(function () {--%>
            <%--var albumName = document.form.name.value.trim();--%>
            <%--var oldId = '${album.id}';--%>
            <%--var domain = '${site.domain}';--%>
            <%--if (domain == '') {--%>
                <%--domain = '${site.subDomain}';--%>
            <%--}--%>
            <%--//if is home album then don't need build uri--%>
            <%--if (albumName != '') {--%>
                <%--$.ajax({--%>
                    <%--url:'/admin/gallery/geturiforalbum.html',--%>
                    <%--async:false,--%>
                    <%--type:"GET",--%>
                    <%--data:'albumName=' + albumName + '&albumId=' + oldId,--%>
                    <%--success:function (data) {--%>
                        <%--//redirect to login album if data return is login album.--%>
                        <%--if (data.substr(0, 1) == '<') {--%>
                            <%--window.location.replace("/admin/login.html");--%>
                        <%--} else {--%>
                            <%--var url = 'http://' + domain + '/' + data;--%>
                            <%--$("#uri-for-album").text(url);--%>
                            <%--document.form.uri.value = data;--%>
                        <%--}--%>
                    <%--}--%>
                <%--});--%>
            <%--}--%>
        <%--});--%>

        $("#form").submit(function() {
            var form = $( "#form" );
            if (form.valid()) {
                var url = "/admin/gallery/insert.html"; // the script where you handle the form input.
                $.ajax({
                    type: "POST",
                    url: url,
                    data: $("#form").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        $('#modal_message_alert').html(data);
                        $("#modal_message_alert").fadeTo(2000, 500).slideUp(500, function(){
                            $("#modal_message_alert").alert('close');
                        });
                    }
                });
                return false; // avoid to execute the actual submit of the form.
            }
        });

    });
</script>

<div class="row">
<div class="col-xs-12">
    <div  id="modal_message_alert"></div>
    <!-- Form Control States -->
    <div class="box" id="box-1">
        <%--<h4 class="box-header round-top"><fmt:message key="gallery.album.form"/>--%>
            <%--<a class="box-btn" title="close"><i class="icon-remove"></i></a>--%>
            <%--<a class="box-btn" title="toggle"><i class="icon-minus"></i></a>--%>
            <%--<a class="box-btn" title="config" data-toggle="modal" href="#box-config-modal"><i--%>
                    <%--class="icon-cog"></i></a>--%>
        <%--</h4>--%>

        <div class="box-container-toggle">
            <div class="box-content">
                <form name="form" id="form" class="form-horizontal" action="#" method="post">
                    <input name="id" type="hidden" value="${albumId}"/>
                    <input name="uri" type="hidden" value="${uri}"/>
                    <h:csrf/>

                    <fieldset>
                        <c:if test="${! empty album.id}">
                        <legend><fmt:message key="gallery.album.update"/> <b>${album.name}</b></legend>
                        </c:if>
                        <c:if test="${empty album.id}">
                        <legend><fmt:message key="gallery.create.new"/></legend>
                        </c:if>

                        <%--<div class="control-group">--%>
                            <%--<label class="control-label"><fmt:message key="menu.url"/></label>--%>

                            <%--<div class="controls">--%>
                                <%--<c:if test="${!empty album.uri}">--%>
                                    <%--<c:set value="${album.uri}" var="fullUri"/>--%>
                                <%--</c:if>--%>
                                <%--<c:set var="domain" value="${site.domain}"/>--%>
                                <%--<c:if test="${empty domain}">--%>
                                    <%--<c:set var="domain" value="${site.subDomain}"/>--%>
                                <%--</c:if>--%>
                                <%--<span class="help-inline" id="uri-for-album">http://${domain}/${fullUri}</span>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <div class="control-group">
                            <label class="control-label" for="focusedInput"><fmt:message key="gallery.album.name"/></label>

                            <div class="controls">
                                <input name="name" class="required input-xlarge" id="focusedInput" type="text" value="${album.name}" maxlength="255" autofocus/>
                            </div>
                        </div>

                        <div class="control-group">
                            <%--<label class="control-label" for="optionsCheckbox"><fmt:message key="gallery.album.active"/></label>--%>
                            <br>
                            <div class="controls">
                                <%--<label class="checkbox">--%>
                                    <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/>
                                    <fmt:message key="gallery.active.explain"/>
                                <%--</label>--%>
                            </div>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary"><fmt:message key="common.save.changes"/></button>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
</div>
</div>