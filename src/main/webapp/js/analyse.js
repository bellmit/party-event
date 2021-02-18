
$(window).resize(function(){
    $('#maplistdiv,.analye-con').css('height',parseInt($(window).height()-$('.dwTop').height()-40));
    $('#mapList').css('height',parseInt($(window).height()-$('.dwTop').height()-90));
});
$(function () {
    /* 默认状态 */
    $('#maplistdiv,.analye-con').css('height',parseInt($(window).height()-90));
    $('#mapList').css('height',parseInt($(window).height()-140));

    $('.tj-con').css('width',parseInt($('.zzCondition').width()));

    if($('.analye-con').hasClass('on')){
        $('.BMap_stdMpSlider').css('width',parseInt($('.BMap_stdMpZoom').width()-161));
    }

    $('.up-down-btn').click(function () {
        if($(this).text() == '收起'){
            $('.tj-con').stop().slideUp();
            $(this).text('展开');
        }else{
            $('.tj-con').stop().slideDown();
            $(this).text('收起');
        }
    });

    $('.PartContent span').click(function(){
        var index = $('.PartContent').index($(this).parent());
        //alert(index);
        $(this).addClass('hasSelect');
        $(this).siblings().removeClass('hasSelect');
        $('.cx-cell').eq(index).show().find('b').text($(this).text());
    });

    $('.cx-cell .del-a').click(function(){
        var index = $('.cx-cell').index($(this).parent());
        $(this).parent().hide();
        $('.PartContent').eq(index).find('.selectAll').addClass('hasSelect');
        $('.PartContent').eq(index).find('.selectAll').siblings().removeClass('hasSelect');
    });

    $('#analyseId .td1').click(function(){
        var index = $('#analyseId .td1').index($(this));
        $(this).parent().find('td').addClass('td-on');
        $(this).parent().siblings().find('td').removeClass('td-on');
        $('.analyse-table-info').eq(index).show();
        $('.analyse-table-info').eq(index).siblings().hide();
    });
    $('.send-a').click(function(){
        $('.yj-info').show();
    });
});
