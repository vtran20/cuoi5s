<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>
<form name="form_footer_support" id="form_footer_support" class="form-horizontal" action="#" method="post">
    <h:csrf/>
    <fieldset>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right" for="footerSupportDisplay"><fmt:message key="common.active.status"/></label>
            <div class="col-sm-10 controls">
                <c:choose>
                    <c:when test="${siteHeaderFooter.footerSupportDisplay == 'Y'}">
                        <c:set var="footerSupportDisplay" value="checked"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="footerSupportDisplay" value=""/>
                    </c:otherwise>
                </c:choose>
                <label class="control-label">
                    <input name="footerSupportDisplay" id="footerSupportDisplay" type="checkbox" ${footerSupportDisplay}/>
                </label>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right" for="footerSupportHeader"><fmt:message key="common.title"/></label>
            <div class="col-sm-10 forms">
                <input name="footerSupportHeader" class="input-xlarge" maxlength="50" id="footerSupportHeader" type="text" value="${siteHeaderFooter.footerSupportHeader}" autofocus/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <%--<spring:eval expression="serviceLocator.menuDao.findByOrder('usefulLink', 'Y', 'sequence', site.id)" var="menus"/>--%>
                <spring:eval expression="serviceLocator.siteSupportDao.findByOrder('showFooter', 'Y', 'sequence', site.id)" var="siteSupports"/>
                <c:if test="${!empty siteSupports}">
                    <table id="simple-table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th><fmt:message key="setting.support.support.list"/></th>
                            <th></th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${siteSupports}" varStatus="support" var="currentSupport">
                            <tr id="${support.current.id}-s${support.current.sequence}">
                                <td>
                                    <c:out value="${currentSupport.name}"/><br>
                                    <fmt:message key="setting.support.chat.id"/>: <c:out value="${currentSupport.chatId}"/>, <fmt:message key="setting.support.chat.type"/>: <c:out value="${currentSupport.chatType}"/>,
                                    <fmt:message key="setting.support.phonenumber"/>: <c:out value="${currentSupport.phone}"/>, <fmt:message key="setting.support.support.time"/>: <c:out value="${currentSupport.timeAvailable}"/>
                                </td>
                                <td>
                                    <div class="hidden-sm hidden-xs btn-group">
                                        <a class="btn btn-xs btn-danger show-confirm" data-rel="tooltip" title="<fmt:message key="common.delete"/>"  lang='<fmt:message key="setting.do.you.want.delete.support"/>' hreflang="/admin/sites/removeSupportInFooter.html?id=${support.current.id}&csrf=<sec:authentication property="details.csrf"/>">
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
            <a class="btn btn-sm btn-primary pull-left" href="#footer_support_modal" role="button" data-toggle="modal" data-title="<fmt:message key="site.footer.add.support.infor"/>" data-page="/admin/sites/footer_support_popup.html" data-target="#footer_support_modal">
                <i class="ace-icon fa fa-phone"></i>
                <fmt:message key="site.footer.add.support.infor"/>
            </a>
        </div>
    </fieldset>
</form>

<script type="text/javascript">
    $('[data-rel=popover]').popover({container:'body'});

    $(function () {
        $("#form_footer_support").submit(function() {
            var url = "/admin/sites/footer_support_update.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form_footer_support").serialize(), // serializes the form's elements.
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
<h:form_modal modalForm="footer_support_modal"/>