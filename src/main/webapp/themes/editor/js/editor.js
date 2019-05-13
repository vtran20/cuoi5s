(function($){
    //This variable will be true if we change anything in body and footer.
    var changedContent = false;

    //This section implement select html element and update/remove it from the web page.
    ieditor = {};
    ieditor.config = {
        'imageupload': {
            path:'abc',
            imagesPathOverride:'abc',
            generateName:'on',
            skipOptimization:'on',
            url:'http://images.webphattai.com/images/upload.json',
            serverurl:'http://images.webphattai.com'
        },
        csrf:'',
        uri:''
    };
    var EditorElement = Backbone.Model.extend({
        defaults:{
            text:'',
            href:'#',
            alt: '',
            src:'',
            width:'',
            height:'',
            element:null, //element selected
            parentElement:null, //parent element selected
            editElement:null,
            dropoverImg:null,
            originalContent:null
        }

    });

//    var ItemView;
    ItemView = Backbone.View.extend({
        el:$('body'),
        events:{
            'click a[href=#save]':'saveContent',
            'click a[href=#publish]':'publishContent',
            'click body *':'clickOutRange',
//            'click #page-content *, #page-content section, #cms-header *, #cms-footer *':'showElement',
            'click #page-content section, #cms-footer *':'showElement',
            'click a#updateElement':'updateElement',
            'click a#removeElement':'removeElement',
            'click li.site-color-template':'selectSiteColor',
            'click button[data-dismiss="modal"], a[data-dismiss="modal"]': 'closeModal',
            'dragover div.entire-page-level':'dragOverImg',
            'drop div.entire-page-level':'dropImg',
            'dblclick div.entire-page-level *'  : 'editContent',
            'keyup':'keyProcessing'

        },
        initialize:function () {
            _.bindAll(this, 'render', 'showElement', 'removeElement', 'selectSiteColor', 'updateElement', 'closeModal','editContent','keyProcessing','dragOverImg','dropImg','clickOutRange','saveContent','publishContent','validateImage'); // every function that uses 'this' as the current object should be in here
            this.render();
        },
        render:function () {
//            $("#left-navigator").append("<div id='modal-on-click' class='modal hide fade'><div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button><h5>Edit Element</h5></div><div class='modal-body'></div><div class='modal-footer'>    <a href='#' data-dismiss='modal' class='btn'>Close</a><a id='removeElement' class='btn btn-warning'>Remove html seletor</a><a id='updateElement' class='btn btn-primary'>Save changes</a></div></div>");
        },
        removeElement:function (event) {
            if (this.model.get('element')) {
                this.model.get('element').remove();
                $(this).changedContent();
            }
            if (this.model.get('parentElement')) {
                this.model.get('parentElement').remove();
                $(this).changedContent();
            }
            $('#modal-on-click').modal('hide');
        },
        selectSiteColor:function (event) {
            event.stopPropagation();
            event.preventDefault();
            var currElement = $(event.target);
            $("link#site-color-template").attr("href", currElement.parent().attr ("data-src"));
            //$("link#site-color-template").attr("href","");
        },
        updateElement:function (event) {
            if (this.model.get('parentElement') != null && "carousel-area" == this.model.get('parentElement').attr("id")) {
                var listItems = this.model.get('parentElement').find("div.container div.carousel-inner div.item");
                for (i=0; i<listItems.length; i++) {
                    var carouselItem = listItems.eq(i);
                    if (i == 0) {
                        carouselItem.find("div.carousel-caption h4").text($('#modal_dyn_title1').val());
                        carouselItem.find("div.carousel-caption p").text($('#modal_dyn_description1').val());
                        carouselItem.find("img").attr("src", $('#modal_dyn_image1').val());
                    }
                    if (i == 1) {
                        carouselItem.find("div.carousel-caption h4").text($('#modal_dyn_title2').val());
                        carouselItem.find("div.carousel-caption p").text($('#modal_dyn_description2').val());
                        carouselItem.find("img").attr("src", $('#modal_dyn_image2').val());
                    }
                    if (i == 2) {
                        carouselItem.find("div.carousel-caption h4").text($('#modal_dyn_title3').val());
                        carouselItem.find("div.carousel-caption p").text($('#modal_dyn_description3').val());
                        carouselItem.find("img").attr("src", $('#modal_dyn_image3').val());
                    }
                }
                $(this).changedContent();
            } else if (this.model.get('element').prop("tagName") && "a" == this.model.get('element').prop("tagName").toLowerCase()) {
                this.model.set({text:$('#modal_dyn_text').val(), href:$('#modal_dyn_href').val()});
                this.model.get('element').html($('#modal_dyn_text').val());
                this.model.get('element').attr('href', $('#modal_dyn_href').val());
                $(this).changedContent();
            } else if (this.model.get('element').prop("tagName") && "img" == this.model.get('element').prop("tagName").toLowerCase()) {
                this.model.set({src:$('#modal_dyn_src').val(), alt:$('#modal_dyn_alt').val(), width:$('#modal_dyn_width').val(), height:$('#modal_dyn_height').val()});
                this.model.get('element').attr({src:$('#modal_dyn_src').val(), alt:$('#modal_dyn_alt').val(), width:$('#modal_dyn_width').val(), height:$('#modal_dyn_height').val()});
                $(this).changedContent();
            }
            $('#modal-on-click').modal('hide');
        },
        //Remove selected if click out of range.
        clickOutRange:function (event) {
            if (this.model && this.model.get('element')) {
                this.model.get('element').removeClass ('editor-element-selected');
            }
        },
        showElement:function (event) {
            //in case click out of editor, we remove element selected.
            this.clickOutRange (event);

            event.stopPropagation();
            event.preventDefault();
            var currElement = $(event.target);
            //remove the border of the previous element and refresh current element
            if (this.model && this.model.get('element')) {
                this.model.get('element').removeClass ('editor-element-selected');
                this.model.set({text:currElement.html(), href:currElement.attr('href'), alt:currElement.attr('alt'),src:currElement.attr('src'),width:currElement.attr('width'),height:currElement.attr('height'), element:currElement});
            } else {
                this.model = new EditorElement({text:currElement.html(), href:currElement.attr('href'), alt:currElement.attr('alt'),src:currElement.attr('src'),width:currElement.attr('width'),height:currElement.attr('height'), element:currElement});
            }
            //Click on contenteditable
            if (currElement.attr("contenteditable") == "true" || currElement.parent().attr("contenteditable") == "true") {
                etch.editableInit(event);
            } else {
                $("#modal-on-click").modal({
                    backdrop:false
                });
                var carousel = this.getCarousel(event);
                if (carousel != null) {
                    //set parent element
                    this.model.set({parentElement: carousel});

                    var listItems = carousel.find("div.container div.carousel-inner div.item");
                    for (i=0; i<listItems.length; i++) {
                        var carouselItem = listItems.eq(i);
                        if (i == 0) {
                            title1 = carouselItem.find("div.carousel-caption h4").text();
                            description1 = carouselItem.find("div.carousel-caption p").text();
                            image1 = carouselItem.find("img").attr("src");
                        }
                        if (i == 1) {
                            title2 = carouselItem.find("div.carousel-caption h4").text();
                            description2 = carouselItem.find("div.carousel-caption p").text();
                            image2 = carouselItem.find("img").attr("src");
                        }
                        if (i == 2) {
                            title3 = carouselItem.find("div.carousel-caption h4").text();
                            description3 = carouselItem.find("div.carousel-caption p").text();
                            image3 = carouselItem.find("img").attr("src");
                        }
                    }
                    var variables = { title1:title1, description1:description1, image1:image1,
                        title2:title2, description2:description2, image2:image2,
                        title3:title3, description3:description3, image3:image3};
                    var template = _.template($("#carousel_template").html(), variables);
                    $(".modal-body").html(template);
                } else if (currElement.prop("tagName") && "a" == currElement.prop("tagName").toLowerCase()) {

                    var variables = { text:this.model.get('text'), href:this.model.get('href')};
                    // Compile the template using underscore
                    var template = _.template($("#a_template").html(), variables);
                    $(".modal-body").html(template);
                } else if (currElement.prop("tagName") && "img" == currElement.prop("tagName").toLowerCase()) {

                    var variables = { alt:this.model.get('alt'), src:this.model.get('src'), width:this.model.get('width'), height:this.model.get('height')};
                    // Compile the template using underscore
                    var template = _.template($("#img_template").html(), variables);
                    $(".modal-body").html(template);

                } else {
                    //currElement.attr ('style', 'border:1px dashed #AAA');
                    currElement.addClass ('editor-element-selected');
                    //this.model = new EditorElement({element:currElement});
                    //Don't need popup modal
                    $(".modal-body").html('');
                    $('#modal-on-click').modal('hide');
                }
                //Remove editable when click outside edit area
                if (this.model && this.model.get('editElement')) {
                    this.model.get('editElement').removeAttr ('contenteditable');
                    this.model.get('editElement').removeClass ('editor-element-edit');
                }
                $("#page-content").sortable('enable');
            }
        },
        //drag file from local and over an image
        dragOverImg:function (event) {
            event.stopPropagation();
            event.preventDefault();
            var currElement = $(event.target), prevDropOverImg;
            if ("img" == currElement.prop("tagName").toLowerCase()) {
                if (this.model) {
                    //remove selected class on the previous dropOver
                    prevDropOverImg = this.model.get('dropoverImg');
                    if (prevDropOverImg) {
                        prevDropOverImg.removeClass('image-drag-over');
                    }
                    this.model.set({dropoverImg: currElement});
                } else {
                    this.model = new EditorElement({dropoverImg: currElement});
                }
                this.model.get('dropoverImg').addClass("image-drag-over");
            } else {
                //remove selected class on the previous dropOver
                this.removeDropOverClass();
            }
        },
        removeDropOverClass:function () {
            //remove selected class on the previous dropOver
            if (this.model) {
                prevDropOverImg = this.model.get('dropoverImg');
                if (prevDropOverImg) {
                    prevDropOverImg.removeClass('image-drag-over');
                }
            }
        },
        validateImage:function (fileType) {
            var arr = ['image/jpg','image/png','image/bmp','image/jpeg','image/gif'];
            return jQuery.inArray(fileType, arr);
        },
        dropImg:function (event) {
//            event.originalEvent.stopPropagation();
//            event.originalEvent.preventDefault();
            event.stopPropagation();
            event.preventDefault();
            dataTransfer = event.dataTransfer = event.originalEvent.dataTransfer;
            var currElement = $(event.target);
            if (dataTransfer) {
                if ("img" == currElement.prop("tagName").toLowerCase()) {
                    var file = $.makeArray(dataTransfer && dataTransfer.files);
                    if(file && typeof file[0] != undefined && this.validateImage(file[0].type) >= 0) {
                        if(confirm('Do you want to upload and replace the current image?')) {
                            //show spinner
                            var parent = currElement.parent();
                            parent.append("<img src='/themes/editor/img/spinner.gif' id='spinner-gif'>");
                            var data = new FormData();
                            data.append('file',file[0]);
                            data.append('path',ieditor.config.imageupload.path);
                            data.append('imagesPathOverride',ieditor.config.imageupload.imagesPathOverride);
                            data.append('generateName',ieditor.config.imageupload.generateName);
                            data.append('skipOptimization',ieditor.config.imageupload.skipOptimization);
                            //                $(".dfiles[rel='"+ids+"']").find(".progress").show();
                            $.ajax({
                                type:"POST",
                                xhrFields: {withCredentials: false},
                                url:ieditor.config.imageupload.url,
                                data:data,
                                dataType: 'json',
//                            cache: false,
                                contentType: false,
                                processData: false,
                                success:function(res){
                                    //remove spinner
                                    if (parent.find("#spinner-gif")) {
                                        parent.find("#spinner-gif").remove();
                                    }
                                    $.each(res, function(index, element) {
                                        if (element.name) {
                                            var imgSize = "";
                                            oldSrc = currElement.attr("src");
                                            if (oldSrc.indexOf("placehold.it") > 0) {
                                                imgSize = oldSrc.substring(oldSrc.lastIndexOf("/")+1, oldSrc.length);
                                                if (imgSize && imgSize.indexOf("x") > 0) {
                                                    //get w and h and put into img
                                                    w = imgSize.substring(0, imgSize.indexOf("x"));
                                                    h = imgSize.substring(imgSize.indexOf("x") + 1, imgSize.length);
                                                }
                                            } else {
                                                //get w and h and put into img
                                                w = currElement.attr("width");
                                                h = currElement.attr("height");
                                                imgSize = w+"x"+h;
                                            }
                                            var src = ieditor.config.imageupload.serverurl+"/get/"+element.name+".image?op=scale|"+imgSize;
                                            var alt = element.original;
                                            currElement.attr({"src": src, "alt": alt, "width":w, "height":h});
                                        }

                                    });
                                }
                            });
                            $(this).changedContent();
                        }
                    }
                }
            }
            this.removeDropOverClass();
        },
        keyProcessing:function (event) {
            event.stopPropagation();
            event.preventDefault();
            //Delete key
            if (event.keyCode == 46) {
                if (this.model.get('editElement') != null && this.model.get('editElement').attr ('contenteditable') == "true") {
                    //Do nothing
                } else {
                    if ($('#modal-on-click').is(":visible")) {
                        //do nothing
                    } else {
                        this.removeElement(event);
                        $(this).changedContent();
                    }
                }
                //Esc key
            } else if (event.keyCode == 27) {
                this.cleanContent();
            }
        },
        editContent:function (event) {
            event.stopPropagation();
            event.preventDefault();
            var id = event.target.id;
            var currElement = $(event.target);
            var children = currElement.children();
            //if the uesrs click on body page and this element has no children.
            if (/*id != 'page-content' && */!hasChildren(currElement)) {
                //disable to allow contentEditable
                $("#page-content").sortable('disable');
                if (this.model) {
                    this.model.set({editElement: currElement});
                } else {
                    this.model = new EditorElement({editElement: currElement});
                }
                //Remove selected element
                if (this.model.get('element') && this.model.get('element').hasClass('editor-element-selected')) {
                    this.model.get('element').removeClass ('editor-element-selected');
                }
                //Change the selected to content editable
                this.model.get ('editElement').attr('contentEditable','true');
                this.model.get ('editElement').addClass('editor-element-edit');
                //currElement.attr('contentEditable','true');
                //currElement.addClass('editor-element-edit');
                etch.editableInit(event);
                // alert (currElement.addEventListener);
                // alert (currElement.attachEvent);
                // if (typeof currElement.addEventListener != "undefined") {
                // currElement.addEventListener("keypress", enterKeyPressHandler (event), false);
                // } else if (typeof currElement.attachEvent != "undefined") {
                // currElement.attachEvent("onkeypress", enterKeyPressHandler (event));
                // }
            }

        },
        closeModal:function (event) {
            this.cleanContent();
        },
        saveContent:function () {
//            event.preventDefault();
            //remove element selected/edit content if any
            this.cleanContent();

            var content = $("#page-content").html();
//            var header = $("#cms-header").html();
            var footer = $("#cms-footer").html();
            //TODO: Save header and footer
            $.ajax({
                url:'/admin/page/save.html',
                async:false,
                type:"POST",
                data:{
                    preContent:content,
//                    preHeader:header,
                    preFooter:footer,
                    menuUri: ieditor.config.uri,
                    csrf:ieditor.config.csrf
                },
                dataType:"html",
                success:function (data) {
                    //redirect to login page if data return is login page.
                    if (data.substr(0, 1) == '<') {
                        window.location.replace("/admin/login.html");
                    } else {
                        jQuery("#alert-message").addClass('alert-success').html(data).show().delay(10000).fadeOut();
                    }
                }
            });
            //saved the edit content
            changedContent = false;
        },
        publishContent:function () {
//            event.preventDefault();
            //remove element selected/edit content if any
            this.cleanContent();

            var content = $("#page-content").html();
//            var header = $("#cms-header").html();
            var footer = $("#cms-footer").html();
            //TODO: publish header and footer
            $.ajax({
                url:'/admin/page/save.html',
                async:false,
                type:"POST",
                data:{
                    preContent:content,
//                    preHeader:header,
                    preFooter:footer,
                    menuUri: ieditor.config.uri,
                    action: 'publish',
                    csrf:ieditor.config.csrf
                },
                dataType:"html",
                success:function (data) {
                    //redirect to login page if data return is login page.
                    if (data.substr(0, 1) == '<') {
                        window.location.replace("/admin/login.html");
                    } else {
                        jQuery("#alert-message").addClass('alert-success').html(data).show().delay(10000).fadeOut();
                    }
                }
            });
            //saved the edit content
            changedContent = false;
        },
        cleanContent:function () {
            if (this.model && this.model.get('element') && this.model.get('element').hasClass('editor-element-selected')) {
                this.model.get('element').removeClass ('editor-element-selected');
            }
            if (this.model && this.model.get('editElement') && this.model.get('editElement').hasClass ('editor-element-edit')) {
                this.model.get('editElement').removeAttr ('contenteditable');
                this.model.get('editElement').removeClass ('editor-element-edit');
            }

        },
        getCarousel:function (event) {
            var currElement = $(event.target);
            var parent = currElement.closest("section#carousel-area");
            if ("carousel-area" == parent.attr("id")) {
                return parent;
            } else {
                return null;
            }
        }


    });
    
    var ItemView = new ItemView();

    //drop image from editor
    var dropImageOption = {
        accept: "li.image-library",
        hoverClass: "image-drag-over",
        drop: function( event, ui ) {
            var $draggable = $(ui.draggable);
            var currElement = $(event.target);
            var imgSize = "";
            oldSrc = currElement.attr("src");
            if (oldSrc.indexOf("placehold.it") > 0) {
                imgSize = oldSrc.substring(oldSrc.lastIndexOf("/")+1, oldSrc.length);
                if (imgSize && imgSize.indexOf("x") > 0) {
                    //get w and h and put into img
                    w = imgSize.substring(0, imgSize.indexOf("x"));
                    h = imgSize.substring(imgSize.indexOf("x") + 1, imgSize.length);
                }
            } else {
                //get w and h and put into img
                w = currElement.attr("width");
                h = currElement.attr("height");
                imgSize = w+"x"+h;
            }
            var src = $draggable.attr("data-src")+"?op=scale|"+imgSize;
            var alt = $draggable.attr("title");
            currElement.attr({"src": src, "alt": alt, "width":w, "height":h});
            $(this).changedContent();
        }
    };

    $( "#page-content img" ).droppable(dropImageOption);

    /*
     * Target of drop element is page-content.
     1. If draggable is page-template, we will replace current page.
     2. If draggable is widget-template, we will append at the end of page.
     */
    $( "#page-content" ).droppable({
        accept: function(d) {
            if(d.hasClass("page-template")||(d.hasClass("widget-template"))){
                return true;
            }
        },
        hoverClass: "entire-page-template-hover",
        drop: function( event, ui ) {

            var $draggable = $(ui.draggable);
//            var pageTemplate = $draggable.find('.page-template');
//            var widgetTemplate = $draggable.find('.widget-template');

            if ($draggable.hasClass('page-template')) {
                //Add page template into content
                if(confirm('Do you want to replace a new template page. The old content will be removed, ok?')) {
                    $("#page-content").html($("#page-template-"+ui.draggable.attr('id')).html());
                }
            } else if ($draggable.hasClass('widget-template')) {
                $("#page-content").append($("#widget-template-"+ui.draggable.attr('id')).html());
            }
            $("#page-content").removeAttr("style");

            $(this).changedContent();

            //Rebind image of new content.
            $( "#page-content img" ).droppable(dropImageOption);
        }
    });

    var hasChildren = function (c) {
        var children = c.children();
        for (var i = 0; i < children.length; i++) {
            if (!(
                    "br" == children[i].tagName.toLowerCase() ||
                            "i" == children[i].tagName.toLowerCase() ||
                            "u" == children[i].tagName.toLowerCase() ||
                            "a" == children[i].tagName.toLowerCase() ||
                            "b" == children[i].tagName.toLowerCase())
                    ) {
                return true;
            }
        }
        return false;
    };

    /*
     * Target of drop element is header. Accepting header content only
     */
    $( "header" ).droppable({
        accept: ".header-template",
        hoverClass: "entire-page-template-hover",
        drop: function( event, ui ) {

            var $draggable = $(ui.draggable);
            if ($draggable.hasClass('header-template')) {
                //Add page template into content
                if(confirm('Do you want to replace a new header template. The old content will be removed, ok?')) {
                    $("header").html($("#header-template-"+ui.draggable.attr('id')).html());
                }
            }
            $("header").removeAttr("style");

            $(this).changedContent();

            //Rebind image of new content.
            $( "header img" ).droppable(dropImageOption);
        }
    });
    /*
     * Target of drop element is header. Accepting header content only
     */
    $( "footer" ).droppable({
        accept: ".footer-template",
        hoverClass: "entire-page-template-hover",
        drop: function( event, ui ) {

            var $draggable = $(ui.draggable);
            if ($draggable.hasClass('footer-template')) {
                //Add page template into content
                if(confirm('Do you want to replace a new footer template. The old content will be removed, ok?')) {
                    $("footer").html($("#footer-template-"+ui.draggable.attr('id')).html());
                }
            }
            $("footer").removeAttr("style");

            $(this).changedContent();

            //Rebind image of new content.
            $( "footer img" ).droppable(dropImageOption);
        }
    });

//    Ctrl+z
    $(document).keydown(function(event) {
        var currKey=0,e=e||event;
        currKey=e.keyCode||e.which||e.charCode;  //do this handle FF and IE
        if ( String.fromCharCode(currKey).toLowerCase() == 'z') {
            event.preventDefault();
            $(this).undoContent();
        }
    });

    $.fn.undoContent = function() {
        if(pageContent && confirm('Do you want to rollback to the original page?')) {
            $("#page-content").html(pageContent.html());
            $( "#page-content img" ).droppable(dropImageOption);
            $("#cms-footer").html(footerContent.html());
            $( "footer img" ).droppable(dropImageOption);
        }
    };

    $.fn.changedContent = function () {
        changedContent = true;
    };

    /*Detect kepress in entire page*/
    $("#page-content").keypress(function(e) {
        e.stopPropagation();
        var code = (e.keyCode ? e.keyCode : e.which);
        if(code == 13) { //Enter keycode
            enterKeyPressHandler(e);
        }
        $(this).changedContent();
    });

    var enterKeyPressHandler = function(evt) {
        evt.stopPropagation();
        var sel, range, br, addedBr = false;
        evt = evt || window.event;
        var charCode = evt.which || evt.keyCode;
        if (charCode == 13) {
            if (typeof window.getSelection != "undefined") {
                sel = window.getSelection();
                if (sel.getRangeAt && sel.rangeCount) {
                    range = sel.getRangeAt(0);
                    range.deleteContents();
                    br = document.createElement("br");
                    range.insertNode(br);
                    range.setEndAfter(br);
                    range.setStartAfter(br);
                    sel.removeAllRanges();
                    sel.addRange(range);
                    addedBr = true;
                }
            } else if (typeof document.selection != "undefined") {
                sel = document.selection;
                if (sel.createRange) {
                    range = sel.createRange();
                    range.pasteHTML("<br>");
                    range.select();
                    addedBr = true;
                }
            }

            // If successful, prevent the browser's default handling of the keypress
            if (addedBr) {
                if (typeof evt.preventDefault != "undefined") {
                    evt.preventDefault();
                } else {
                    evt.returnValue = false;
                }
            }
        }
    };


    /*Setting etch.config override the current one.*/
    _.extend(etch.config.buttonClasses, {
        'default': ['bold', 'italic', 'underline', 'unordered-list', 'ordered-list', 'link', 'clear-formatting']
    });

    var articleView = Backbone.View.extend({
        events: {
            'mousedown .editable': 'editableClick'
        },

        editableClick: etch.editableInit
    });

    //Using this data for rollback
    var pageContent;
    var footerContent;
    $(document).ready(function(){
        pageContent = $("#page-content").clone();
        footerContent = $("#cms-footer").clone();
    });

    $(window).bind('beforeunload', function(){
        if (changedContent == true) {
            return 'Your content page was changed!';
        }
    });

})(jQuery);

$(function () {
    $("#undo-button").click(function () {
        $(this).undoContent();
    });

    //Auto save every 5 minutes
    window.setInterval(function() {
        $("a[href=#save]").click();
    }, 1000*60*10);


});
