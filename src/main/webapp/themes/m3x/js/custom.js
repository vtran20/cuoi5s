function loadMoreNewVideosOnPage(urlJson) {
    $.getJSON( urlJson, function( data ) {
        for(var i in data) {
            url = "/video/"+data[i].uri+"/"+data[i].id+".html?videoId="+data[i].videoId;
            div = $("<div class='col-sm-4'/>");
            div1 = $("<div class='thumbnail-style'/>");
            div1.append("<a href='"+url+"'><img alt='"+data[i].name+"' src='"+data[i].mediumImgUrl+"' class='img-responsive'></a>");
            if (data[i].duration != null) {
                div1.append("<div class='duration'>"+data[i].duration+"</div>");
            }
            var d = new Date(data[i].updatedDate);
            ul = $("<ul class='list-unstyled list-inline blog-info'>");
            ul.append("<li><i class='fa fa-calendar'></i> " + d.getDate() + "/" + (d.getMonth()+1) + "/" + d.getFullYear() +"</li>");
//                ul.append("<li><i class='fa fa-eye'></i> "+data[i].viewCount+" views</li>");
            div1.append(ul);
            div1.append("<p title='"+data[i].name+"'>"+data[i].name+"</p>");
            div.append(div1);
            $('#load-more-videos').append(div);
        }
    });

}

//Search video by channel
$("#search-button").click(function() {
    var url = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyCjdekNBTgmJ3x6_hepaapf2BgQHE195Z4&part=snippet&maxResults=50&eventType=live&type=video&q=";
    var query = $('#query').val();
    if (query != null && query != "") {
        url = url+ query
        $.getJSON( url, function( data ) {
            $('#search-result div').replaceWith("");
            if (data.items.length > 0) {
                div1 = $("<div class='col-md-12'/>");
                div21 = $("<div class='headline headline-md'/>");
                div21.append("<h3>Video đang trực tiếp</h3>");
                div1.append(div21);
                div22 = $("<div class='row'/>");

                for(var i in data.items) {
                    div3 = $("<div class='col-sm-3'>");
                    div4 = $("<div class='thumbnail-style'>");
                    div4.append("<a href='/live-video/"+data.items[i].id.videoId+".html'><img alt='" + data.items[i].snippet.title + "' src='"+data.items[i].snippet.thumbnails.medium.url+"' class='img-responsive' onerror='this.src=&quot;http://placehold.it/320x180&quot;; this.width=&quot;320&quot;; this.height=&quot;180&quot;;' style='visibility: visible;'></a>");
                    div4.append("<ul class='list-unstyled list-inline blog-info'/>");
                    div4.append("<p title='" + data.items[i].snippet.title + "'>" + data.items[i].snippet.title + "</p>");
                    div3.append(div4);
                    div22.append(div3);
                }
                div1.append(div22);
                $('#search-result').append(div1);
            } else {
                $('#search-result').append("<div class='col-md-6 col-md-offset-3'><div class='margin-bottom-40'></div><h3>Không tìm thấy kết quả tìm kiếm với từ khóa '<b>"+query+"</b>'</h3></div>");
            }
        });
    }

    //_gaq.push(['_trackEvent', 'SearchLive', query, '']);
});

$('#query').keypress(function(e){
    if (e.keyCode == 13 || e.which == 13) {
        $('#search-button').click();
    }
});

// ==================================================
//
// jquery-input-mask-phone-number v1.0
//
// Licensed (The MIT License)
//
// Copyright © Raja Rama Mohan Thavalam <rajaram.tavalam@gmail.com>
//
// ==================================================
;
(function ($) {
    $.fn.usPhoneFormat = function (options) {
        var params = $.extend({
            format: 'xxx-xxx-xxxx',
            international: false,

        }, options);

        if (params.format === 'xxx-xxx-xxxx') {
            $(this).bind('paste', function (e) {
                e.preventDefault();
                var inputValue = e.originalEvent.clipboardData.getData('Text');
                if (!$.isNumeric(inputValue)) {
                    return false;
                } else {
                    inputValue = String(inputValue.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3"));
                    $(this).val(inputValue);
                    $(this).val('');
                    inputValue = inputValue.substring(0, 12);
                    $(this).val(inputValue);
                }
            });
            $(this).on('keypress', function (e) {
                if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                    return false;
                }
                var curchr = this.value.length;
                var curval = $(this).val();
                if (curchr == 3 && e.which != 8 && e.which != 0) {
                    $(this).val(curval + "-");
                } else if (curchr == 7 && e.which != 8 && e.which != 0) {
                    $(this).val(curval + "-");
                }
                $(this).attr('maxlength', '12');
            });

        } else if (params.format === '(xxx) xxx-xxxx') {
            $(this).on('keypress', function (e) {
                if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                    return false;
                }
                var curchr = this.value.length;
                var curval = $(this).val();
                if (curchr == 3 && e.which != 8 && e.which != 0) {
                    $(this).val('(' + curval + ')' + " ");
                } else if (curchr == 9 && e.which != 8 && e.which != 0) {
                    $(this).val(curval + "-");
                }
                $(this).attr('maxlength', '14');
            });
            $(this).bind('paste', function (e) {
                e.preventDefault();
                var inputValue = e.originalEvent.clipboardData.getData('Text');
                if (!$.isNumeric(inputValue)) {
                    return false;
                } else {
                    inputValue = String(inputValue.replace(/(\d{3})(\d{3})(\d{4})/, "($1) $2-$3"));
                    $(this).val(inputValue);
                    $(this).val('');
                    inputValue = inputValue.substring(0, 14);
                    $(this).val(inputValue);
                }
            });

        }
    }
}(jQuery));