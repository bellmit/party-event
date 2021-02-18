
function map_listAddPointsByobjIds(data,gmap,clickHandler)
{
	setTimeout(function(){
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		//根据data组装好ids和tips，过程省略
		var ids=[];
		var tips=[];
		var imgxx={"img":"xx","hoverImg":"vv"};
		var imgxxs=[];
		imgxxs.push(imgxx);
		gmap.addPointById(ids,tips,imgxxs,clickHandler);
	});
}


function map_listAddPoints(data,clickHandler)//地图列表点绘制方法
{
	setTimeout(function(){
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    		if(typeof list_data_all[i].MAPS_X !="undefined"&&typeof list_data_all[i].MAPS_Y !="undefined" &&list_data_all[i].MAPS_X!=""&&list_data_all[i].MAPS_Y!=""){
    			var strContent = '';
    			strContent += '部件名称:'+list_data_all[i].NAME+"<br>";
    			strContent += '部件类别:'+list_data_all[i].TYPE+"<br>";
    			strContent += '部件编码:'+list_data_all[i].CODE+"<br>";
    			strContent += '地址描述:'+list_data_all[i].ADDRESS+"<br>";
        		var pageName="red";
        		if(list_data_all[i].STATUS=="1"){
        			pageName="gray";
        		}
        		var hoverPageNmae="blue";
        		var tip={"index":i,fireId:list_data_all[i].ID,"title":"部件信息",content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].MAPS_X,list_data_all[i].MAPS_Y,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    		
    	}
    	for(var i=0;i<data.length;i++)//根据列表数据画点
    	{
    		if(typeof data[i].MAPS_X !="undefined"&&typeof data[i].MAPS_Y !="undefined" &&data[i].MAPS_X!=""&&data[i].MAPS_Y!=""){
    			var strContent = '';
    			strContent += '部件名称:'+data[i].NAME+"<br>";
    			strContent += '部件类别:'+data[i].TYPE+"<br>";
    			strContent += '部件编码:'+data[i].CODE+"<br>";
    			strContent += '地址描述:'+data[i].ADDRESS+"<br>";
    			var pageCount=i+1;
    			
        		var pageName="p"+pageCount;
        		if(data[i].STATUS=="1"){
        			pageName="p"+pageCount+"g";
        		}
        		var hoverPageNmae="ph"+pageCount;
        		var tip={"index":i,fireId:data[i].ID,"title":"部件信息",content:strContent};
        		var pointId=gmap.addPoint(data[i].MAPS_X,data[i].MAPS_Y,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    		
    	}
    	$("#allcount").html(list_data_all.length);
    	gmap.setExtentByshowPointGraphicsLayer();
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    	//gmap.centerAtPoint(data[0].MAPS_X,data[0].MAPS_Y,11);
    },1000);
}

//普通列表和地图列表的绑定生成
function doubleTableRender(contextPath,listDiv,pageDiv,mapListDiv,mapPageDiv,requestData,clickHandler){
	var imgurl=contextPath +"/js/gmap/images";
	var opts_map = {
	        basePath:contextPath +"/js/gmap/ulynlist",
	        url:contextPath+'/bjxx/bjxx_list.do',
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
	                     
	                     {field: '', label: '地址', className: "address", width: 50, bodyContent: "[ADDRESS]"}
	                 ] ,
	            rownumbers: false 
	        },
	        afterTableRender:function(){
	        	 $("#mapList li").click(function(){
	        		 var index = $(this).index();
	        		 if(typeof list_data[index].MAPS_X !="undefined"&&typeof list_data[index].MAPS_Y !="undefined" &&list_data[index].MAPS_X!=""&&list_data[index].MAPS_Y!=""){
	        			 gmap.closeInfoWindow();
	        			 for(var i = 0 ; i < $("#mapList li").length;i++){
	        				 gmap.changePointToImg(i+list_data_all.length); 
	        			 }
	        			 gmap.changePointToHoverImg(pointArray[index].id+list_data_all.length);
	        			 gmap.centerAtPoint(list_data[index].MAPS_X,list_data[index].MAPS_Y,13);
	        			 setTimeout(function(){
	        		    		var height = 10*gmap.getMapLevel()+1;
	        		    		var top = 165-height;
	        		    		$("#sliderHeight").height(height);
	        		        	$("#sliderTop").css("top",top+"px");
	        		    	},1500);
	        			 gmap.setPointToTop(pointArray[index].id+list_data_all.length);
	        		 }
	    			 
	    		});
	        	 
	        	 var inputdata = {"type":$("#type").val(),"name":$("#name").val(),"address":$("#address").val(),"isMap":"isMap"};
	        	 $.ajax({
	        	        type:"POST",
	        	        url:contextPath+'/bjxx/bjxx_list_all.do',
	        	        data:inputdata,
	        	        dataType:"json",
	        	        async:false,
	        	        success:function(data){
	        	        	list_data_all = data.data.list;
	        	        	map_listAddPoints(list_data,clickHandler);
	        	        },
	        	        beforeSend:function(){
	        	        },
	        	        error:function(){
	        	        	map_listAddPoints(list_data,clickHandler);
	        	        }
	        		 });
	        	 var height = $(window).height()-$("#listMapLi").offset().top;
	        	 if($("#mapList").find("ul").height()>(height-$("#mapPager").height())){
	        		 $("#mapList").height((height-$("#mapPager").height())+"px");
	        	 }
	        	 
	     		
	        },
	        requestData:requestData,
	        pageBarId: mapPageDiv
	    };
	
	    $("#"+mapListDiv).ulynlist(opts_map);
	    var height = $(window).height()-$("#listMapLi").offset().top;
	    $("#listMapLi").height(height+"px");
	    $("#maplistdiv").height(height+"px");
}
function clickHandler(index,x,y,tip){
	//window.location.href=contextPath+'/fire/detail.do?fireId='+tip.fireId;
}

function queryDataAssembly() {
    var requestData = {"type":$("#type").val(),"name":$("#name").val(),"address":$("#address").val(),"isMap":"isMap",linesPerPage:10};
    $.ajax({
        type:"POST",
        url:contextPath+'/bjxx/bjxx_list.do',
        data:{"type":$("#type").val(),"name":$("#name").val(),"address":$("#address").val(),linesPerPage:10,currentPage:1},
        dataType:"json",
        async:false,
        success:function(data){
        	$(".jgxz").eq(0).html(data.data.totalNum);
        },
        beforeSend:function(){
        },
        error:function(){
        }
	 });
    var listDiv = 'dwTab';
    var pageDiv = 'dwPager';
    var mapListDiv = 'mapList';
    var mapPageDiv = 'mapPager';
    doubleTableRender(contextPath, listDiv, pageDiv, mapListDiv, mapPageDiv, requestData, clickHandler);
}

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}
$(function(){
    //侧边栏渲染
//    renderSidebar();
	
    //表格渲染
	queryDataAssembly();
	
    $("#search-btn").click(function(){
    	queryDataAssembly();
    });
    
    $("#search-reset").click(function(){
    	$(".all-style").removeClass("all-style");
    	$("#全部").addClass("all-style");
    	$("#name").val("");
    	$("#type").val("");
    	$("#address").val("");
    });
    
});