<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<c:set var="menuId" value="${param.id}"/>
<c:set var="external" value="${param.external}"/>
<c:if test="${! empty menuId}">
    <spring:eval expression="serviceLocator.menuDao.findById(T(java.lang.Long).valueOf(menuId))" var="menu"/>
</c:if>
<c:if test="${! empty menu.id}">
    <c:set value="/admin/menu/update.html" var="formUrl"/>
    <c:set value="${menu.id}" var="menuId"/>
    <c:set value="${menu.uri}" var="uri"/>
    <c:if test="${menu.menuTemplate == 'E'}">
        <c:set var="external" value="E"/>
    </c:if>
    <c:choose>
        <c:when test="${fn:startsWith(menu.active, 'N')}">
            <c:set value="" var="activeChecked"/>
        </c:when>
        <c:otherwise>
            <c:set value="checked" var="activeChecked"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:startsWith(menu.headerMenu, 'N')}">
            <c:set value="" var="headerMenuChecked"/>
        </c:when>
        <c:otherwise>
            <c:set value="checked" var="headerMenuChecked"/>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${empty menu.id}">
    <c:set value="/admin/menu/insert.html" var="formUrl"/>
    <c:set value="checked" var="activeChecked"/>
    <c:set value="checked" var="headerMenuChecked"/>
</c:if>
<script>
    $(function () {

        $("#form").validate({
            messages:{
                <%--name:"<fmt:message key="menu.name.required"/>"--%>
            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('success');
                $(label).closest('.form-group').addClass('error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('error');
                $(label).closest('.form-group').addClass('success');
            }
        });

        $("#form").submit(function() {
            var form = $( "#form" );
            if (form.valid()) {
                var url = "/admin/menu/insert.html"; // the script where you handle the form input.
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
    $('[data-rel=popover]').popover({container:'body'});

</script>

<div class="page-header">
    <c:if test="${! empty menu.id}">
        <h4><fmt:message key="menu.update.menu"/> <b>${menu.name}</b></h4>
    </c:if>
    <c:if test="${empty menu.id}">
        <h4><fmt:message key="menu.add.new.menu"/></h4>
    </c:if>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div  id="modal_message_alert"></div>
        <form name="form" id="form" action="#" method="post">
            <input name="id" type="hidden" value="${menuId}"/>
            <input name="menuTemplate" type="hidden" value="${external}"/>
            <h:csrf/>
            <div class="form-group">
                <label class="control-label" for="name"><fmt:message key="menu.name"/></label>
                <div class="controls">
                    <input name="name" class="required input-xlarge" id="name" type="text" value="${menu.name}" maxlength="255"/>
                </div>
            </div>
            <c:choose>
                <c:when test="${external == 'E'}">
                    <div class="form-group">
                        <label class="control-label" for="uri"><fmt:message key="menu.url"/></label>
                        <div class="controls">
                            <input name="uri" class="required input-xlarge" id="uri" type="text" value="${menu.uri}" maxlength="255"/>
                            <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="menu.url.external"/>" title="">?</span>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <input name="uri" type="hidden" value="${uri}"/>
                </c:otherwise>
            </c:choose>
            <div class="form-group">
                <label class="control-label" for="selectError"><fmt:message key="menu.parent"/></label>
                <div class="controls">
                    <c:if test="${! empty site}">
                        <spring:eval expression="serviceLocator.menuDao.getRootMenus(site, 'N', 'N')" var="menus"/>
                        <c:if test="${! empty menu}">
                            <spring:eval expression="serviceLocator.menuDao.getParentMenu(site, menu.id, 'N')" var="parentMenu"/>
                        </c:if>
                    </c:if>
                    <c:if test="${! empty menus}">
                        <select id="selectError" name="parentId">
                            <option value="0"></option>
                            <c:forEach items="${menus}" var="m">
                                <c:if test="${empty menu || m.id != menu.id}">
                                <c:choose>
                                    <c:when test="${! empty parentMenu && parentMenu.id == m.id}">
                                        <option value="${m.id}" selected>${m.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${m.id}">${m.name}</option>
                                    </c:otherwise>
                                </c:choose>
                                </c:if>
                                <%--SubMenu level--%>
                                <spring:eval expression="serviceLocator.menuDao.getSubMenus(site, m, 'N', 'N')" var="subMenus"/>
                                <c:forEach items="${subMenus}" var="submenu">
                                    <c:if test="${empty menu || submenu.id != menu.id}">
                                    <c:choose>
                                        <c:when test="${! empty parentMenu && parentMenu.id == submenu.id}">
                                            <option value="${submenu.id}" selected>----${submenu.name}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${submenu.id}">----${submenu.name}</option>
                                        </c:otherwise>
                                    </c:choose>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </select>
                    </c:if>
                    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="menu.support.level"/>" title="">?</span>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label" for="optionsCheckbox"></label>
                <div class="controls">
                    <%--<label class="checkbox">--%>
                        <input type="checkbox" id="optionsCheckbox" name="active" ${activeChecked}/><fmt:message key="menu.active.status"/>
                        <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="menu.checkbox.active.explain"/>" title="">?</span>
                    <%--</label>--%>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="optionsCheckbox"></label>

                <div class="controls">
                    <%--<label class="checkbox">--%>
                        <input type="checkbox" id="optionsCheckbox" name="headerMenu" ${headerMenuChecked}/><fmt:message key="menu.header.menu.status"/>
                        <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="<fmt:message key="menu.header.menu.active.explain"/>" title="">?</span>
                    <%--</label>--%>
                </div>
            </div>

            <div class="modal-footer">
                <button class="btn btn-sm btn-primary" type="submit">
                    <i class="ace-icon fa fa-check"></i>
                    <fmt:message key="common.save.changes"/>
                </button>
            </div>
        </form>
    </div>
</div>
