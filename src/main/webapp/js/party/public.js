/**
 * Created by Administrator on 2017/3/23.
 */

function tab() {
    $('.sq-tab a').on('click',function(){
        var index = $('.sq-tab a').index($(this));
        $(this).addClass('cur').siblings('a').removeClass('cur');
        $('.sq-tab-info').eq(index).removeClass('none').siblings('.sq-tab-info').addClass('none');
    });
};

function searchMenu() {
    var $searchBar = $('#searchBar'),
        // $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel');

    function hideSearchResult(){
        // $searchResult.hide();
        $searchInput.val('');
    }
    function cancelSearch(){
        hideSearchResult();
        $searchBar.removeClass('weui-search-bar_focusing');
        $searchText.show();
    }

    $searchText.on('click', function(){
        $searchBar.addClass('weui-search-bar_focusing');
        $searchInput.focus();
    });
    $searchInput
        .on('blur', function () {
            if(!this.value.length) cancelSearch();
        })
        .on('input', function(){
            /*if(this.value.length) {
                $searchResult.show();
            } else {
                $searchResult.hide();
            }*/
        })
    ;
    $searchClear.on('click', function(){
        hideSearchResult();
        $searchInput.focus();
    });
    $searchCancel.on('click', function(){
        cancelSearch();
        $searchInput.blur();
    });
}
function colorChange() {  //改变初始颜色
    if($('.color-change').val() == '请选择'){
        $('.color-change').addClass('c999');
        $(this).parent().next().addClass('none');
    }
}
function uploadimg() {  //上传图片
    var tmpl = '<li class="weui-uploader__file" style="background-image:url(#url#)"></li>',
        $gallery = $("#gallery"),
        $galleryImg = $("#galleryImg"),
        $uploaderInput = $(".weui-uploader__input"),
        $uploaderFiles = $('.tab-info').find(".weui-uploader__files"),
        that_ = "";
    $uploaderFiles.each(function(j){
        $uploaderInput.eq(j).on("change", function(e){
            var src, url = window.URL || window.webkitURL || window.mozURL, files = e.target.files;
            for (var i = 0, len = files.length; i < len; ++i) {
                var file = files[i];
                if (url) {
                    src = url.createObjectURL(file);
                } else {
                    src = e.target.result;
                }
                $uploaderFiles.eq(j).append($(tmpl.replace('#url#', src)));
            }
        });
        $uploaderFiles.eq(j).on("click", "li", function(){
            $galleryImg.attr("style", this.getAttribute("style"));
            $gallery.fadeIn(100);
            that_ = $(this);

        });
        $uploaderFiles.eq(j).on("click",".delete-btn",function(){

        });
    });
    $gallery.on("click","#galleryImg", function(){
        $gallery.fadeOut(100);
    });
    $gallery.on("click",".weui-gallery__del", function(){
        if(confirm('确定删除该内容？')){
            that_.remove();
            $gallery.fadeOut(100);
        }else{
            return;
        }
    });

}
function helpTips() {
    var $helpCon = $('#helpCon'),
        $help_dialog = $('.help_dialog'),
        value = $('.apply-con').find('.color-change option:selected').val(),
        index= $('.apply-con').find('.color-change').get(0).selectedIndex,
        tmp = [ ];
        tmp.push("<p>1、夫妻双方身份证、户口本复印件<br>2、夫妻双方结婚证（再婚的需提供离婚证及离婚协议书或离婚判决书、调解书）。</p>");
        tmp.push("<p>1、本人身份证复印件1份；<br>2、一寸免冠照一张（彩色）。</p>");
        tmp.push("<p>1、户主身份证、户口簿原件（交验）及复印件1份；<br>2、享受低保等政策的困难补助群众需提供的相关证明；<br>3、新参保人员提供近期1寸免冠照片（红底或蓝底）1张。</p>");

    $helpCon.on('click', '.weui-dialog__btn', function(){
        $(this).parents('.help_dialog').fadeOut(200);
    });
    $('.apply-con').on('click','.weui-vcode-btn', function(){
        $help_dialog.find('.weui-dialog__title').text(value);
        $help_dialog.find('.weui-dialog__bd').html(tmp[index-1]);
        $help_dialog.fadeIn(200);
    });
}

$(function(){
    searchMenu(); /* 问题列表查询关键字 */
    tab();  //社区tab切换
});

/* 切换横竖屏 */
function orient() {

}
//用户变化屏幕方向时调用
$(window).bind( "onorientationchange" in window ? "orientationchange" : "resize", function(e){
    orient();
});
