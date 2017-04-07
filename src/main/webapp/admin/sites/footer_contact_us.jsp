<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>
<form name="form_footer_contact" id="form_footer_contact" class="form-horizontal" action="#" method="post">
    <h:csrf/>
    <fieldset>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right" for="footerContactDisplay"><fmt:message key="common.active.status"/></label>
            <div class="col-sm-10 controls">
                <c:choose>
                    <c:when test="${siteHeaderFooter.footerContactDisplay == 'Y'}">
                        <c:set var="footerContactDisplay" value="checked"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="footerContactDisplay" value=""/>
                    </c:otherwise>
                </c:choose>
                <label class="control-label">
                    <input name="footerContactDisplay" id="footerContactDisplay" type="checkbox" ${footerContactDisplay}/>
                </label>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right" for="footerContactHeader"><fmt:message key="common.title"/></label>
            <div class="col-sm-10 forms">
                <input name="footerContactHeader" class="input-xlarge" maxlength="50" id="footerContactHeader" type="text" value="${siteHeaderFooter.footerContactHeader}" autofocus/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <%--<spring:eval expression="serviceLocator.menuDao.findByOrder('usefulLink', 'Y', 'sequence', site.id)" var="menus"/>--%>
                <spring:eval expression="serviceLocator.siteContactDao.findByOrder('showFooter', 'Y', 'sequence', site.id)" var="siteContacts"/>
                <c:if test="${!empty siteContacts}">
                    <table id="simple-table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th><fmt:message key="setting.contactus.contact.list"/></th>
                            <th></th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${siteContacts}" varStatus="contact" var="currentContact">
                            <tr id="${contact.current.id}-s${contact.current.sequence}">
                                <td>
                                    <c:out value="${currentContact.name}"/><br>
                                    <c:out value="${currentContact.address_1}"/>, <c:out value="${currentContact.city}"/>, <c:out
                                        value="${currentContact.state}"/>&nbsp;<c:out value="${currentContact.zipCode}"/>. Email: <c:out
                                        value="${currentContact.email}"/>, P: <c:out value="${currentContact.phone}"/>, F: <c:out
                                        value="${currentContact.fax}"/>
                                </td>
                                <td>
                                    <div class="hidden-sm hidden-xs btn-group">
                                        <a class="btn btn-xs btn-danger show-confirm" data-rel="tooltip" title="<fmt:message key="common.delete"/>"  lang='<fmt:message key="footer.do.you.want.delete.contact.in.footer"/>' hreflang="/admin/sites/removeContactInFooter.html?id=${contact.current.id}&csrf=<sec:authentication property="details.csrf"/>">
                                        <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </div>
            <div class="col-sm-1"></div>
        </div>

        <div class="modal-footer">
            <button class="btn btn-sm btn-primary pull-left" type="submit">
                <i class="ace-icon fa fa-check"></i>
                <fmt:message key="common.save.changes"/>
            </button>
            <a class="btn btn-sm btn-primary pull-left" href="#footer_contact_us_modal" role="button" data-toggle="modal" data-title='<fmt:message key="site.footer.add.contact.infor"/>' data-page="/admin/sites/footer_contact_popup.html" data-target="#footer_contact_us_modal">
            <i class="ace-icon fa fa-home"></i>
                <fmt:message key="site.footer.add.contact.infor"/>
            </a>
        </div>
    </fieldset>
</form>

<script type="text/javascript">
    $('[data-rel=popover]').popover({container:'body'});

    $(function () {
        $("#form_footer_contact").submit(function() {
            var url = "/admin/sites/footer_contact_update.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form_footer_contact").serialize(), // serializes the form's elements.
                success: function(data)
                {
                    $('#message_alert').html(data);
                    $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                        $("#message_alert").alert('close');
                    });
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });
    });
    $(".show-confirm").click(function () {
        var object = $(this);
        BootstrapDialog.show({
            type:BootstrapDialog.TYPE_DANGER,
            closeByBackdrop: false,
            closeByKeyboard: false,
            title:'Xác Nhận',
            message:object.attr("lang"),
            buttons:[
                {
                    label:'Yes',
                    action:function (dialog) {
                        $.ajax({
                            type: "GET",
                            url: object.attr("hreflang"),
                            data: $("#form").serialize(), // serializes the form's elements.
                            success: function(data)
                            {
                                //Reload the current tab
                                var $link = $('li.active a[data-toggle="tab"]');
                                $link.parent().removeClass('active');
                                var tabLink = $link.attr('href');
                                $('#form-tab a[href="' + tabLink + '"]').tab('show');
                            }
                        });
                        dialog.close();
                    }
                },
                {
                    label:'No',
                    action:function (dialog) {
                        dialog.close();
                    }
                }
            ]
        });
    });

</script>
<h:form_modal modalForm="footer_contact_us_modal"/>