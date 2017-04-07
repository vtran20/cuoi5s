<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="albumImageId" value="${param.id}"/>
<c:if test="${! empty albumImageId}">
    <spring:eval expression="serviceLocator.albumImageDao.findById(T(java.lang.Long).valueOf(albumImageId))" var="albumImage"/>
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

        $("#form").submit(function() {
            var form = $( "#form" );
            if (form.valid()) {
                var url = "/admin/gallery/update_image.html"; // the script where you handle the form input.
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
                return false;
            }
        });

    });
</script>

<div class="row">
<div class="col-xs-12">
    <div  id="modal_message_alert"></div>
    <!-- Form Control States -->
    <div class="box" id="box-1">
        <div class="box-container-toggle">
            <div class="box-content">
                <form name="form" id="form" class="form-horizontal" action="#" method="post">
                    <input name="id" type="hidden" value="${albumImageId}"/>
                    <h:csrf/>
                     <fieldset>
                        <%--<div class="control-group">--%>
                            <%--<label class="control-label" for="description"><fmt:message key="gallery.album.name"/></label>--%>
                            <%--<div class="controls">--%>
                                <%--<input name="fileName" class="required input-xlarge" id="fileName" type="text" value="${albumImage.fileName}"/>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <div class="control-group">
                            <label class="control-label" for="description"><fmt:message key="gallery.album.image.description"/></label>
                            <div class="controls">
                                <textarea name="description" class="input-xlarge" id="description">${albumImage.description}</textarea>
                            </div>
                        </div>

                        <%--<div class="control-group">--%>
                            <%--&lt;%&ndash;<label class="control-label" for="optionsCheckbox"><fmt:message key="gallery.album.active"/></label>&ndash;%&gt;--%>
                            <%--<br>--%>
                            <%--<div class="controls">--%>
                                <%--&lt;%&ndash;<label class="checkbox">&ndash;%&gt;--%>
                                    <%--<input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/>--%>
                                    <%--<fmt:message key="gallery.active.explain"/>--%>
                                <%--&lt;%&ndash;</label>&ndash;%&gt;--%>
                            <%--</div>--%>
                        <%--</div>--%>
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