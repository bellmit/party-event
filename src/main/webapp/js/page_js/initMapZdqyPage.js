function map_listAddPoints(list_data,list_data_all,clickHandler)//地图列表点绘制方法
{
	
	setTimeout(function(){
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    		if(typeof list_data_all[i].LNG_D !="undefined"&&typeof list_data_all[i].LAT_D !="undefined" &&list_data_all[i].LNG_D!=""&&list_data_all[i].LAT_D!=""){
    			var strContent = '';
        		strContent += "区域名称:"+list_data_all[i].QYMC;
        		strContent += "<br><a href='javascript:void(0)' onclick='newWindow(\""+list_data_all[i].VIDEOURL+"\")' style='margin-right:10px'>视频</a>";
        		var title = "";
        		var pageName="red";
        		var hoverPageNmae="blue";
        		var tip={"index":i,fireId:list_data_all[i].ID,"title":title,content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].LNG_D,list_data_all[i].LAT_D,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    	}
		for(var i=0;i<list_data.length;i++)//根据列表数据画点
    	{
    		if(typeof list_data[i].LNG_D !="undefined"&&typeof list_data[i].LAT_D !="undefined" &&list_data[i].LNG_D!=""&&list_data[i].LAT_D!=""){
    			var strContent = '';
        		strContent += "区域名称:"+list_data[i].QYMC;
        		strContent += "<br><a href='javascript:void(0)' onclick='newWindow(\""+list_data[i].VIDEOURL+"\")' style='margin-right:10px'>视频</a>";
        		var title = "";
        		var pageName="p"+(i+1);
        		var hoverPageNmae="ph"+(i+1);
        		var tip={"index":i,fireId:list_data[i].ID,"title":title,content:strContent};
        		var pointId=gmap.addPoint(list_data[i].LNG_D,list_data[i].LAT_D,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    	}
		//gmap.centerAtPoint('106.4800585661','29.4775758593',10);
    	gmap.setExtentByshowPointGraphicsLayer();
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    },1000);
	
}


//普通列表和地图列表的绑定生成
function doubleTableRender(clickHandler){
	var requestData = {linesPerPage:10,"QYMC":$("#QYMC").val()};
	var inputData = {"QYMC":$("#QYMC").val()};
	var imgurl=contextPath +"/js/gmap/images";
	var opts_map = {
	        basePath:contextPath +"/js/gmap/ulynlist",
	        url:contextPath+'/zdqy/zdqy_list.do',
	        tableTpl:"maplist",
	        pageBarTpl:"maplist",
	        tableColumn: {
	            loadFilter:function(data){
	            	for(var i=0;i<data.length;i++){
	            		var item = data[i];
	            		item['XH'] = i+1;
	            	}
	            	list_data=data;
	            },
	            columns:[
	                     {field: "XH", className: imgurl,label:imgurl, width: 50},
	                     
	                     {field: '', label: '地址', className: "address", width: 50, bodyContent: "[QYMC]"}
	                 ] ,
	            rownumbers: false 
	        },
	        afterTableRender:function(){
	        	 $("#mapList li").click(function(){
	        		 var index = $(this).index();
	        		 if(typeof list_data[index].LNG_D !="undefined"&&typeof list_data[index].LAT_D !="undefined" &&list_data[index].LNG_D!=""&&list_data[index].LAT_D!=""){
	        			 gmap.closeInfoWindow();
	        			 for(var i = 0 ; i < $("#mapList li").length;i++){
	        				 gmap.changePointToImg(i+list_data_all.length); 
	        			 }
	        			 gmap.changePointToHoverImg(pointArray[index].id+list_data_all.length);
	        			 gmap.centerAtPoint(list_data[index].LNG_D,list_data[index].LAT_D,13);
	        			 setTimeout(function(){
	        		    		var height = 10*gmap.getMapLevel()+1;
	        		    		var top = 165-height;
	        		    		$("#sliderHeight").height(height);
	        		        	$("#sliderTop").css("top",top+"px");
	        		    	},1500);
	        			 gmap.setPointToTop(pointArray[index].id+list_data_all.length);
	        		 }
	    			 
	    		});
	        	 
	        	 $.ajax({
	     	        type:"POST",
	     	        url:contextPath+'/zdqy/zdqy_list_all.do',
	     	        data:inputData,
	     	        dataType:"json",
	     	        success:function(data){
	     	        	list_data_all = data.data.list;
	     	        	map_listAddPoints(list_data,list_data_all,clickHandler);
	     	        },
	     	        beforeSend:function(){
	     	        },
	     	        error:function(){
	     	        	map_listAddPoints(list_data,list_data_all,clickHandler);
	     	        }
	     		 });
	        	 var height = $(window).height()-$("#listMapLi").offset().top;
	        	 if($("#mapList").find("ul").height()>(height-$("#mapPager").height())){
	        		 $("#mapList").height((height-$("#mapPager").height())+"px");
	        	 }
	     		
	        },
	        requestData:requestData,
	        pageBarId: "mapPager"
	    };
	$("#mapList").ulynlist(opts_map);
	 var height = $(window).height()-$("#listMapLi").offset().top;
	 if($("#mapList").find("ul").height()>(height-$("#mapPager").height())){
		 $("#mapList").height((height-$("#mapPager").height())+"px");
	 }
	var height = $(window).height()-$("#listMapLi").offset().top;
	$("#listMapLi").height(height+"px");
}
function clickHandler(index,x,y,tip){
}

function newWindow(yoururl) {
	var height = $(window.parent.document).height();
	var width = $(window.parent.document).width();
	var top = (height-500)/2;
	var left = (width-700)/2;
	window.open(yoururl, '_blank','height=500,width=700,top='+top+',left='+left);
	}
function searchHospital(obj){
	doubleTableRender(null);
	
}

$(function(){
	
	doubleTableRender(null);
	$("#search-reset").click(function(){
		$("#QYMC").val('');
    });
    
});