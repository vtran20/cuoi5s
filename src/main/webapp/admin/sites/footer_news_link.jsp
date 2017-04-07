<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>
<spring:eval expression="serviceLocator.siteHeaderFooterDao.findUniqueBy('site.id', site.id)" var="siteHeaderFooter"/>
<form name="form_footer_news_link" id="form_footer_news_link" class="form-horizontal" action="#" method="post">
    <h:csrf/>
    <fieldset>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right" for="footerNewsLinkDisplay"><fmt:message key="common.active.status"/></label>
            <div class="col-sm-10 controls">
                <c:choose>
                    <c:when test="${siteHeaderFooter.footerNewsLinkDisplay == 'Y'}">
                        <c:set var="footerNewsLinkDisplay" value="checked"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="footerNewsLinkDisplay" value=""/>
                    </c:otherwise>
                </c:choose>
                <label class="control-label">
                    <input name="footerNewsLinkDisplay" id="footerNewsLinkDisplay" type="checkbox" ${footerNewsLinkDisplay}/>
                </label>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right" for="footerNewsLinkHeader"><fmt:message key="common.title"/></label>
            <div class="col-sm-10 forms">
                <input name="footerNewsLinkHeader" class="input-xlarge" maxlength="30" id="footerNewsLinkHeader" type="text" value="${siteHeaderFooter.footerNewsLinkHeader}" autofocus/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <spring:eval expression="serviceLocator.newsDao.findByOrder('footerLink', 'Y', 'sequence', site.id)" var="newses"/>
                <c:if test="${!empty newses}">
                    <table cellpadding="0" cellspacing="0" border="0"
                           class="table table-striped table-bordered bootstrap-datatable" id="datatable">
                        <thead>
                        <tr>
                            <th><fmt:message key="menu.menu.name"/></th>
                            <th><fmt:message key="menu.status"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach items="${newses}" varStatus="menu" var="currentMenu">
                            <c:set var="disableClass" value=""/>
                            <c:if test="${menu.current.homePage == 'Y'}">
                                <c:set var="disableClass" value="ui-state-disabled"/>
                            </c:if>

                            <tr class="ui-state-disabled">
                                <td><c:out value="${menu.current.name}"/></td>
                                <td class="center">
                                    <c:set value="${menu.current.active}" var="active"/>
                                    <c:if test="${fn:startsWith(active, 'Y')}">
                                        <span class="label label-success"><fmt:message key="menu.active"/></span>
                                    </c:if>
                                    <c:if test="${!fn:startsWith(active, 'Y')}">
                                        <span class="label label-inverse"><fmt:message key="menu.inactive"/></span>
                                    </c:if>

                                </td>
                                <td class="center">
                                        <a class="btn btn-xs btn-danger show-confirm" title='<fmt:message key="common.delete"/>' lang='<fmt:message key="common.doyouwanttodeletethisnewslink"/>' hreflang='/admin/sites/removenewslink.html?id=${menu.current.id}&csrf=<sec:authentication property="details.csrf"/>'>
                                            <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                        </a>
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
            <a class="btn btn-sm btn-primary pull-left" href="#footer_news_link_modal" role="button" data-toggle="modal" data-title="<fmt:message key="site.footer.add.news.link"/>" data-page="/admin/sites/news_link.html" data-target="#footer_news_link_modal">
                <i class="ace-icon fa fa-link"></i> <fmt:message key="site.footer.add.news.link"/>
            </a>
        </div>
    </fieldset>
</form>

<script type="text/javascript">
    $('[data-rel=popover]').popover({container:'body'});

    $(function () {

        $("#form_footer_news_link").submit(function() {
            var url = "/admin/sites/footer_newslink.html"; // the script where you handle the form input.
            $.ajax({
                type: "POST",
                url: url,
                data: $("#form_footer_news_link").serialize(), // serializes the form's elements.
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
<h:form_modal modalForm="footer_news_link_modal"/>