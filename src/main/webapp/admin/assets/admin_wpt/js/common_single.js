//Show confirm and process removing data
$(".global_show-confirm").on("click", function() {
    var object = $(this);
    BootstrapDialog.show({
        type:BootstrapDialog.TYPE_DANGER,
        closeByBackdrop: false,
        closeByKeyboard: false,
        title:object.attr("data-header"),
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
                            if ($link.attr('data-tab-url')) {
                                $('#form-tab a[href="' + tabLink + '"]').tab('show');
                                $('#message_alert').html(data);
                                $("#message_alert").fadeTo(2000, 500).slideUp(500, function(){
                                    $("#message_alert").alert('close');
                                });

                            } else {
                                BootstrapDialog.show({
                                    title:object.attr("data-header"),
                                    message: data,
                                    buttons: [{
                                        label: 'OK',
                                        action: function(dialog) {
                                            window.location.reload(true);
                                            dialog.close();
                                        }
                                    }]
                                });
                            }
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

//Show confirm and process check/uncheck checkbox
$(".global-toggle-checkbox").on("click", function() {
    var object = $(this);
    var flag = object.prop( "checked" );
    BootstrapDialog.show({
        type:BootstrapDialog.TYPE_DANGER,
        closeByBackdrop: false,
        closeByKeyboard: false,
        title:object.attr("data-header"),
        message:object.attr("lang"),
        buttons:[
            {   label:'Yes',
                action:function (dialog) {
                    $.get(object.attr("data-src"), { flag: flag}, // some params to pass it
                        function(data) {
                            if ("ok" == data) {
                                BootstrapDialog.show({
                                    type:BootstrapDialog.TYPE_INFO,
                                    message:object.attr("confirm-success")
                                })
                            } else {
                                BootstrapDialog.show({
                                    type:BootstrapDialog.TYPE_DANGER,
                                    message:object.attr("confirm-fail")
                                })
                                object.prop("checked", !flag)
                            }
                        });
                    dialog.close();
                }
            },
            {
                label:'No',
                action:function (dialog) {
                    object.prop("checked", !flag);
                    dialog.close();
                }
            }
        ]
    });

});
