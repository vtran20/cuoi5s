<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>
<%@ attribute name="modalForm" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="modalSize" required="false" rtexprvalue="true" type="java.lang.String"%>
<c:if test="${empty modalForm}">
    <c:set value="modal-form" var="modalForm"/>
</c:if>
<script type="text/javascript">
    $( document ).ready(function() {
        //TODO: @mediafreakch you can call $('#myModal').removeData('bs.modal') at anytime which is the same a refresh method.
        $('#${modalForm}').on('shown.bs.modal', function(event){
            var button = $(event.relatedTarget);
            var title = button.data('title');
            var url = button.data('page');
            var modal = $(this);
            modal.find('.modal-header h4').text(title);
            $.get(url, function(data){
                $('#${modalForm}').find('.modal-body').html(data);
            })
        });

        $('#${modalForm}').on('hidden.bs.modal', function () {
            $(this).removeData('bs.modal');
            if ($("#message_alert").text() != '' || $("#modal_message_alert").text() != '') {
                //Reload the current tab
                var $link = $('li.active a[data-toggle="tab"]');
                $link.parent().removeClass('active');
                var tabLink = $link.attr('href');
                if ($link.attr('data-tab-url')) {
                    $('#form-tab a[href="' + tabLink + '"]').tab('show');
                } else {
                    window.location.reload(true);
                }
            }
        })
    });
</script>
<c:set var="classSize" value="modal-lg"/>
<c:if test="${!empty modalSize}">
    <c:set var="classSize" value="${modalSize}"/>
</c:if>
<div id="${modalForm}" class="modal" tabindex="-1">
    <div class="modal-dialog ${classSize}">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="blue bigger"></h4>
            </div>

            <div class="modal-body">
            </div>

        </div>
    </div>
</div>