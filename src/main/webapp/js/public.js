// JavaScript Document
var tabs = function(ele,menu){
   var objs = $(ele),menu = $(menu);
	   objs.click(function(){
		   var index = objs.index($(this));
           if($(this).hasClass('cur')){
               $(this).removeClass('cur');
               menu.stop(true,true).slideUp();
               return;
           }
		   objs.each(function(i) {
			  objs.eq(i).removeClass('cur');
			  menu.eq(i).stop(true,true).slideUp();
			});
			$(this).addClass('cur');
			  menu.eq(index).stop(true,true).slideDown();
	   });
};
/*$(function(){	
	tabs($('.app-cell'),$('.app-info .app-hide'));
});*/

function getAjaxData(url,title,pageNo,pageSize,ifOpen){
	var inputdata={title:title,currentPage:pageNo,linesPerPage:pageSize,ifOpen:ifOpen};
   	 $.ajax({
            type:"POST",
            url: url,
            data:inputdata,
            dataType:"json",
            async : false,
            success:function(data){
            	setData(data);
            },
            beforeSend:function(){
            },
            error : function(text)
            {
            }
   	 });
}
