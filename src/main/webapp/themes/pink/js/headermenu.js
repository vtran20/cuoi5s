$(document).ready(function() {
    buildTopNav();
});
var buildTopNav = function() {
    var animationSpeed = 150;
    $('#topnav').removeClass('noscript').find('.menu a').each(function() {
        var $this = $(this),$trail = $this.closest('.tab').find('h3'),trail = [];
        $trail.each(function() {
            trail.push($(this).text() + ' > ');
        });
        $this.attr('name', 'Top Nav / ' + trail.join('') + $this.text());
    }).end().find('ul > li.tab').bind({'mouseenter':function(e) {
        if (!$(e.target).closest('li').hasClass('tab')) {
            return false;
        }
        var li = $(this).find('> ul > li');
        if (li.length == 1) {
            li.click();
        } else {
            if (e.pageX < $(this).offset().left + li.first().outerWidth(true)) {
                li.first().mouseenter();
            } else {
                li.last().mouseenter();
            }
        }
    },'mouseleave':function(e) {
        hideTopNav($(this).find('.expanded'), animationSpeed);
    }}).find('> ul > li:not(.leaf)').bind({'mouseenter click':function(e) {
        e.stopPropagation();
        if (window.searchBar != null) {
            window.searchBar.hideSuggestions();
        }
        clearActiveTimeout();
        var whichTabOver = ($(this).hasClass("more")) ? $(this).prev() : $(this);
        var expanded = $('#topnav .expanded');
        if (expanded.length > 0 && expanded[0] != whichTabOver[0]) {
            hideTopNav(expanded, animationSpeed);
        }
        var leftOffset = whichTabOver.parentsUntil("li.tab").parent().hasClass("active") ? 3 : 16;
        var menu = whichTabOver.find("div.menu");
        menu.css("left", whichTabOver.position().left - leftOffset + "px").removeClass("cols-1 cols-2 cols-3 cols-4 cols-5").addClass("cols-" + menu.find("ul.column").length);
        whichTabOver.addClass("expanded");
        if ($("li.more>span").css("opacity") < 1 && whichTabOver.next().hasClass("more")) {
            $("li.more>span").hide();
        } else {
            whichTabOver.next("li.more").children("span").fadeOut(animationSpeed);
        }
        var animatedMenubar = $("div:animated");
        if (animatedMenubar.length > 0) {
            $("div.menubar").stop(false, true);
            whichTabOver.children("div.menubar").show();
        } else {
            delayTopNav = setTimeout(function() {
                whichTabOver.children("div.menubar").slideDown(animationSpeed);
            }, 300);
        }
    },'mouseleave':function(e) {
        var $rel = $(e.relatedTarget).closest('li');
        if ($rel.closest('.tab')[0] == $(this).closest('.tab')[0] && !$rel.is('.tab ul li')) {
            return false;
        }
        hideTopNav($(this), animationSpeed);
    }})
};
var hideTopNav = function(openTab, speed) {
    clearActiveTimeout();
    if (openTab.hasClass("more")) {
        openTab = openTab.prev();
    }
    openTab.removeClass("expanded").find("div.menubar").slideUp(speed).end().next("li.more").children("span").fadeIn(speed);
};
var clearActiveTimeout = function() {
    if (typeof(delayTopNav) == "number" && delayTopNav > 0) {
        clearTimeout(delayTopNav);
        delayTopNav = 0;
    }
};
