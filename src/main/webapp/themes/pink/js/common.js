$(function() {

    //sort function
    $("#searchResultsSort").change(function() {
        if ($(this).val() != "select") {
            window.location = $(this).val();
        }
    });


    // events for header submmit search box.
    $("#searchForm").submit(function() {
        searchVal = $("#keyword-id");
        if ((searchVal == "Enter keyword or item #") || (searchVal == "")) {
            alert("Please enter a search term and try your search again");
            return false;
        }
    });

    // events for header search box.
    $("#keyword-id").each(function() {
        var default_value = this.value;
        $(this).focus(function() {
            if (this.value == default_value) {
                this.value = '';
            }
        });
        $(this).blur(function() {
            if (this.value == '') {
                this.value = default_value;
            }
        });
    });

    $("img[id=search_button]").click(function() {
        $('form[name=searchForm]').submit();
    });

    $("#keyword-id").autocomplete('/autocomplete.html', {
        width: 277,
        autoFill: false,
        selectFirst: false,
//        mustMatch: true,
        matchContains: "word"
    });

    // Dialog popup
    // increase the default animation speed to exaggerate the effect
    $.fx.speeds._default = 100;
    $("#dialog").dialog({
        autoOpen: false,
        show: "blind"
    });

    $("#dialog-promo-code").dialog({
        autoOpen: false,
        show: "blind"
    });


    $("a.che-add-help-link").click(function() {
        $(this.name).dialog("open");
        return false;
    });

    jQuery.fn.preventDoubleSubmit = function() {
        jQuery(this).submit(function() {
            if (this.beenSubmitted)
                return false;
            else
                this.beenSubmitted = true;
        });
    };

    $('.forgotpassword').popupWindow({
        centerScreen:1
    });

    //hover on thumbnail image on category
//    $(".cat-thu-product-all").hover(function () {
//        $(this).addClass("hover-dark");
//    }, function () {
//        $(this).removeClass("hover-dark");
//    });


//    More images
    $(".image-items img").click(function() {
        // see if same thumb is being clicked
        if ($(this).hasClass("active")) { return; }

        // calclulate large image's URL based on the thumbnail URL (flickr specific)
        var url = $(this).attr("src").replace("50", "351");

        // get handle to element that wraps the image and make it semi-transparent
        var wrap = $("#izView").fadeTo("medium", 0.5);

        // the large image from www.flickr.com
        var img = new Image();


        // call this function after it's loaded
        img.onload = function() {

            // make wrapper fully visible
            wrap.fadeTo("fast", 1);

            // change the image
            wrap.find("img").attr("src", url);

        };

        // begin loading the image from www.flickr.com
        img.src = url;

        // activate item
        $(".image-items img").removeClass("active");
        $(this).addClass("active");
        //use for view large image
        $('#photo1 img').attr('src', $(this).attr("src").replace("50", "550"));

    // when page loads simulate a "click" on the first image
    }).filter(":first").click();

    /* Start Javascript for diggdigg-floating-bar*/
    var dd_top = 0;
    var dd_left = 0;

    jQuery(document).ready(function(){

        var $floating_bar = jQuery('#dd_ajax_float');
        var $dd_start = jQuery('#dd_start');
        var $dd_outer = jQuery('.dd_outer');

        dd_top = parseInt($dd_start.offset().top);
        dd_left = -(dd_offset_from_content + 40);

        dd_adjust_inner_width();
        dd_position_floating_bar(dd_top, dd_left);

        $floating_bar.fadeIn('slow');

        if($floating_bar.length > 0){

            var pullX = $floating_bar.css('margin-left');

            jQuery(window).scroll(function () {

                var scroll_from_top = jQuery(window).scrollTop() + 30;
                var is_fixed = $dd_outer.css('position') == 'fixed';

                if($floating_bar.length > 0)
                {
                    if ( scroll_from_top > dd_top && !is_fixed )
                    {
                        dd_position_floating_bar(30, dd_left);
                        $dd_outer.css('position', 'fixed');
                    }
                    else if ( scroll_from_top < dd_top && is_fixed )
                    {
                        dd_position_floating_bar(dd_top, dd_left);
                        $dd_outer.css('position', 'absolute');
                    }

                }

            });
        }
    });

    jQuery(window).load(function(){

        var $dd_start = jQuery('#dd_start');
        var $floating_bar = jQuery('#dd_ajax_float');

        dd_top = parseInt($dd_start.offset().top);

        // reposition the floating bar
        dd_position_floating_bar(dd_top, dd_left);
        dd_adjust_inner_width();
    });

    jQuery(window).resize(function() {
        dd_adjust_inner_width();
    });

    var dd_is_hidden = false;
    var dd_resize_timer;
    function dd_adjust_inner_width() {

        var $dd_inner = jQuery('.dd_inner');
        var $dd_floating_bar = jQuery('#dd_ajax_float')
        var width = parseInt(jQuery(window).width() - (jQuery('#dd_start').offset().left * 2));
        $dd_inner.width(width);
        var dd_should_be_hidden = false; //(((jQuery(window).width() - width)/2) < -dd_left); //Comment by Vu, wnat to show all time.
        var dd_is_hidden = $dd_floating_bar.is(':hidden');

        if(dd_should_be_hidden && !dd_is_hidden)
        {
            clearTimeout(dd_resize_timer);
            dd_resize_timer = setTimeout(function(){ jQuery('#dd_ajax_float').fadeOut(); }, -dd_left);
        }
        else if(!dd_should_be_hidden && dd_is_hidden)
        {
            clearTimeout(dd_resize_timer);
            dd_resize_timer = setTimeout(function(){ jQuery('#dd_ajax_float').fadeIn(); }, -dd_left);
        }
    }

    function dd_position_floating_bar(top, left, position) {

        var $floating_bar = jQuery('#dd_ajax_float');
        if(top == undefined) top = 0;
        if(left == undefined) left = 0;
        if(position == undefined) position = 'absolute';

        $floating_bar.css({
            position: position,
            top: top + 'px',
            left: left + 'px'
        });
    }
    /* End Javascript for diggdigg-floating-bar*/

});
