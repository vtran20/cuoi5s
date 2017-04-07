//Diglog is used for confirm/alert popup
//$(function() {
//    $(".show-confirm").click(function () {
//        var object = $(this);
//        $.fn.dialog2.helpers.confirm(object.attr("lang"), {
//            confirm:function () {
//                window.location.href = object.attr("href")
//            },
//            decline:function () {
//            }
//        });
//
//        event.preventDefault();
//    });

    $(".show-confirm").click(function () {
        var object = $(this);
        BootstrapDialog.show({
            title:'',
            message:object.attr("lang"),
            buttons:[
                {
                    label:'Yes',
                    action:function (dialog) {
                        dialog.close();
                        window.location.href = object.attr("hreflang");
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

//});


//$(function() {
//    $( "#datatable tbody" ).sortable({
//        revert: true,
//        containment: "#datatable",
//        items: "tr:not(.ui-state-disabled)",
//        update: function( event, ui ) {
//            updateReOrder(event, ui)
//        }
//    });
//});
