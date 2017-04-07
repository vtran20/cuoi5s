(function($) {
	var site = window.site = function() {
		return {
			data : {
				ratingImgSrc: "../assets/images/but/cat/but-star-empty.png"
			},
			func : {
				refinementsToggle : function(){
					var SCROLL_LIMIT = 30;
					var isLeftNav = ($(".glo-left-nav-container"));
					if(isLeftNav != null){
						$(".refinements .type a.refine-cat").toggle(function(elem){
							var currImage = $(this).css("background-image");
							newImage = currImage.split("plus")[0] + "minus" + currImage.split("plus")[1];
							$(this).css("background-image", newImage);
							if($(this).parent().parent().children(".options").children("ul").children("li").length > SCROLL_LIMIT) {
								$(this).parent().parent().children(".options").addClass("scroll-pane");
								$(this).parent().parent().children(".options").jScrollPane({showArrows:true,scrollbarMargin:0,scrollbarWidth:16,arrowSize:11,paneHeight:170});
								$(this).parent().parent().children(".jScrollPaneContainer").before('<div class="jScrollPaneDivider"></div>');
								$(this).parent().parent().children(".jScrollPaneContainer").after('<div class="jScrollPaneDivider"></div>');
								$(this).parent().parent().children(".jScrollPaneContainer").children(".options").show("slow");
							} else {
								$(this).parent().parent().children(".options").show("slow");
							}
							elem.preventDefault();
						},
						function(elem){
							var currImage = $(this).css("background-image");
							newImage = currImage.split("minus")[0] + "plus" + currImage.split("minus")[1];
							$(this).css("background-image", newImage);
							if($(this).parent().parent().children(".jScrollPaneContainer").children(".options").children("ul").children("li").length > SCROLL_LIMIT) {
								$(this).parent().parent().children(".jScrollPaneContainer").children(".options").jScrollPaneRemove();
								$(this).parent().parent().children(".jScrollPaneDivider").remove();
							}
							$(this).parent().parent().children(".options").hide("slow");
							elem.preventDefault();
						});
						
						//Display the first refinement, when user first comes to page
//						var optionsSelected = $(".options .selected").length;
//						if(optionsSelected == 0){
//						    $(".refinements .type a.refine-cat").eq(0).trigger('click');
//                            alert ($(".refinements .type a.refine-cat").eq(0).html());
//						}
//
//						if($(".options").length > 1){
//							$(".refinements .type a.refine-cat").eq($(".options").length - 1).trigger('click');
//						}
//
//                        //Display selected refinements
//                        $(".options .selected").parent().parent().children(".type").children(".refine-cat").trigger('click');

					}
				},
				ratingHover : function(){

               if(lib.utils.isIE6()){
                $('.bvImage').attr('src','/assets/images/but/cat/but-star-empty.gif');
               }
					var bvTimer;
					$(".bvImage").each(function(i){
						$(this).hover(
							function(){
								clearTimeout(bvTimer);
								var currSrc = $(this).attr('src');
								var onSrc = "";
								var offSrc = "";
								
								//if moving to the off state image (to the right)
								if(currSrc.indexOf('empty') != -1){
									onSrc = currSrc.split('empty')[0] + 'filled' + currSrc.split('empty')[1];
									offSrc = $(this).attr('src');
								}
								//if moving to the on state image (to the left)
								else {
									onSrc = $(this).attr('src');
									offSrc = currSrc.split('filled')[0] + 'empty' + currSrc.split('filled')[1];
								}
								//for ie6
								if(lib.utils.isIE6()){
									onSrc = site.data.ratingImgSrc.split('empty')[0] + 'filled' + site.data.ratingImgSrc.split('empty')[1];
									offSrc = site.data.ratingImgSrc;
								}
								
								$(".bvImage:lt("+i+")").add(this).attr('src', onSrc);
								$(".bvImage:gt("+i+")").attr('src', offSrc);
								
							},
							function(){
								bvTimer = setTimeout(function() {
									var currSrc = $(".bvImage").eq(i).attr('src');
									
									if(currSrc.indexOf('filled') != -1){
										var offSrc = currSrc.split('filled')[0] + 'empty' + currSrc.split('filled')[1];
										$(".bvImage").attr('src', offSrc);
									}
									
									//for ie6
									if(lib.utils.isIE6()){
										var offSrc = site.data.ratingImgSrc;
										$(".bvImage").attr('src', offSrc);
									}
	
								}, 250);
							}
						);
					});
				}
			}
		};
	}($);
})($);

//on body load
$(function() {

	//Leftnav refinements
	site.func.refinementsToggle();
	
	//rating for leftnav
//	site.func.ratingHover(); //Rating. Not support now
	
	// For IE6 png fix with hover states
	site.data.ratingImgSrc = $(".bvImage").attr('src');
	//$('.bvImage').addClass('widget-ie6png');

});