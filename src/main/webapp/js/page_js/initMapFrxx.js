
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
		var allxycount = 0 ;
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    		if(list_data_all[i].MAPS_X!=null&&list_data_all[i].MAP_Y!=null&&typeof list_data_all[i].MAPS_X !="undefined"&&typeof list_data_all[i].MAP_Y !="undefined" &&list_data_all[i].MAPS_X!=""&&list_data_all[i].MAP_Y!=""){
    			var strContent = '';
    			strContent += "<a href='"+contextPath+"/frxx/detail.do?id="+list_data_all[i].ID+"'>企业名称:"+list_data_all[i].QYMC+"</a><br>";
    			strContent += '行业:'+list_data_all[i].HY+"<br>";
    			strContent += '法人代表:'+list_data_all[i].FRDB+"<br>";
    			strContent += '联系方式:'+list_data_all[i].LXFS+"<br>";
    			strContent += '企业创立时间:'+(list_data_all[i].QYCLSJ==null?'':list_data_all[i].QYCLSJ.substring(0,10))+"<br>";
        		var pageName="red";
        		if(list_data_all[i].STATUS=="1"){
        			pageName="gray";
        		}
        		var hoverPageNmae="blue";
        		var tip={"index":i,fireId:list_data_all[i].ID,"title":"企业信息",content:strContent};
        		xy = gmap.tdtWebMercatorToGeographic(list_data_all[i].MAPS_X,list_data_all[i].MAP_Y);
        		var pointId=gmap.addPoint(xy.x,xy.y,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
        		allxycount++;
    		}
    		
    	}
		$("#allxycount").html(allxycount);
    	for(var i=0;i<data.length;i++)//根据列表数据画点
    	{
    		if(list_data_all[i].MAPS_X!=null&&list_data_all[i].MAP_Y!=null&&typeof data[i].MAPS_X !="undefined"&&typeof data[i].MAP_Y !="undefined" &&data[i].MAPS_X!=""&&data[i].MAP_Y!=""){
    			var strContent = '';
    			strContent += "<a href='"+contextPath+"/frxx/detail.do?id="+data[i].ID+"'>企业名称:"+data[i].QYMC+"</a><br>";
    			strContent += '行业:'+data[i].HY+"<br>";
    			strContent += '法人代表:'+data[i].FRDB+"<br>";
    			strContent += '联系方式:'+data[i].LXFS+"<br>";
    			strContent += '企业创立时间:'+(data[i].QYCLSJ==null?'':data[i].QYCLSJ.substring(0,10))+"<br>";
    			var pageCount=i+1;
    			
        		var pageName="p"+pageCount;
        		if(data[i].STATUS=="1"){
        			pageName="p"+pageCount+"g";
        		}
        		var hoverPageNmae="ph"+pageCount;
        		var tip={"index":i,fireId:data[i].ID,"title":"企业信息",content:strContent};
        		xy = gmap.tdtWebMercatorToGeographic(list_data_all[i].MAPS_X,list_data_all[i].MAP_Y);
        		var pointId=gmap.addPoint(xy.x,xy.y,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    		
    	}
    	
    	gmap.setExtentByshowPointGraphicsLayer();
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    	//gmap.centerAtPoint(data[0].MAPS_X,data[0].MAP_Y,11);
    },1000);
}

//普通列表和地图列表的绑定生成
function doubleTableRender(contextPath,listDiv,pageDiv,mapListDiv,mapPageDiv,requestData,clickHandler){
	var imgurl=contextPath +"/js/gmap/images";
	var opts_map = {
	        basePath:contextPath +"/js/gmap/ulynlist",
	        url:contextPath+'/frxx/frxx_list.do',
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
	                     
	                     {field: '', label: '地址', className: "address", width: 50, bodyContent: "[QYMC]"},
	                     {
	                         field: '', label: '姓名', className: "sortable", width: 50,
	                         bodyContent: "<a href='javascript:Void(0)'>[FRDB]</a><span>[LXFS]</span>"
	                     }
	                     
	                 ] ,
	            rownumbers: false 
	        },
	        afterTableRender:function(){
	        	 $("#mapList li").click(function(){
	        		 var index = $(this).index();
	        		 if(typeof list_data[index].MAPS_X !="undefined"&&typeof list_data[index].MAP_Y !="undefined" &&list_data[index].MAPS_X!=""&&list_data[index].MAP_Y!=""){
	        			 gmap.closeInfoWindow();
	        			 for(var i = 0 ; i < $("#mapList li").length;i++){
	        				 gmap.changePointToImg(i+allxycount); 
	        			 }
	        			 gmap.changePointToHoverImg(pointArray[index].id+allxycount);
	        			 xy = gmap.tdtWebMercatorToGeographic(list_data[index].MAPS_X,list_data[index].MAP_Y);
	        			 gmap.centerAtPoint(xy.x,xy.y,13);
//	        			 setTimeout(function(){
//	        		    		var height = 10*gmap.getMapLevel()+1;
//	        		    		var top = 165-height;
//	        		    		$("#sliderHeight").height(height);
//	        		        	$("#sliderTop").css("top",top+"px");
//	        		    	},1500);
//	        			 gmap.setPointToTop(pointArray[index].id+allxycount.length);
	        		 }
	    			 
	    		});
	        	 
	        	 var inputdata = {"isMap":"iaMap","frdb":$("#frdb").val(),"hy":$("#hy").val(),"qymc":$("#qymc").val(),"gslx":$("#gslx").val()};
	        	 $.ajax({
	        	        type:"POST",
	        	        url:contextPath+'/frxx/frxx_list_all.do',
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
	    $("select").select2();
	    var height = $(window).height()-$("#listMapLi").offset().top;
	    $("#listMapLi").height(height+"px");
	    $("#maplistdiv").height(height+"px");
}
function clickHandler(index,x,y,tip){
	//window.location.href=contextPath+'/fire/detail.do?fireId='+tip.fireId;
}

function queryDataAssembly() {
    var requestData = {"isMap":"isMap","frdb":$("#frdb").val(),"hy":$("#hy").val(),"qymc":$("#qymc").val(),"gslx":$("#gslx").val(),linesPerPage:10};
    $.ajax({
        type:"POST",
        url:contextPath+'/frxx/frxx_list.do',
        data:{"frdb":$("#frdb").val(),"hy":$("#hy").val(),"qymc":$("#qymc").val(),"gslx":$("#gslx").val(),linesPerPage:10,currentPage:1},
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
    	$("#frdb").val("");
    	$("#hy").val("");
    	$("#qymc").val("");
    	$("#gslx").val("");
    });
    
});