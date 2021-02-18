
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
    		if(typeof list_data_all[i].LNG_D !="undefined"&&typeof list_data_all[i].LAT_D !="undefined" &&list_data_all[i].LNG_D!=""&&list_data_all[i].LAT_D!=""){
    			var strContent = '';
        		strContent += "站点名称:"+list_data_all[i].HAZARD_ADDRESS;
        		var title = "";
        		if(list_data_all[i].WXY_TYPE=="1"){
        			title = "沼气池";
        			strContent += "<br/>";
        			strContent += "<div><div style='width:49%;float:left'>沼气浓度："+(list_data_all[i].METHANE_CONC==null?'':list_data_all[i].METHANE_CONC);
            		strContent += "</div><div style='width:49%;float:right'>温度："+(list_data_all[i].TEMPERATURE==null?'':list_data_all[i].TEMPERATURE);
            		strContent += "</div></div><br/>";
            		strContent += "<div><div style='width:49%;float:left'>供电状态："+(list_data_all[i].ELEC_STATUS==null?'':list_data_all[i].ELEC_STATUS);
            		strContent += "</div><div style='width:49%;float:right'>水位状态："+(list_data_all[i].WATER_STATUS==null?'':list_data_all[i].WATER_STATUS)+"</div></div>";
        		}
        		if(list_data_all[i].WXY_TYPE=="2"){
        			title = "下水道";
        			strContent += "<br/>";
        			strContent += "<div><div style='width:49%;float:left'>沼气浓度："+(list_data_all[i].METHANE_CONC==null?'':list_data_all[i].METHANE_CONC);
            		strContent += "</div><div style='width:49%;float:right'>温度："+(list_data_all[i].TEMPERATURE==null?'':list_data_all[i].TEMPERATURE);
            		strContent += "</div></div><br/>";
            		strContent += "<div><div style='width:49%;float:left'>供电状态："+(list_data_all[i].ELEC_STATUS==null?'':list_data_all[i].ELEC_STATUS);
            		strContent += "</div><div style='width:49%;float:right'>水位状态："+(list_data_all[i].WATER_STATUS==null?'':list_data_all[i].WATER_STATUS)+"</div></div>";
        		}
				if(list_data_all[i].WXY_TYPE=="3"){
					title = "重点单位";
					strContent += "<br/>";
					strContent += "<div style='float:left'>重大危险源级别："+(list_data_all[i].DANGERLEVEL==null?'':list_data_all[i].DANGERLEVEL)+"</div>";
				}
				if(list_data_all[i].WXY_TYPE=="4"){
					title = "应急避难场所";
					strContent += "<br/>";
				}
        		if(list_data_all[i].GATHER_TIME!=null&&list_data_all[i].GATHER_TIME!=''){
        			strContent += "<br/>采集时间："+formatpc(list_data_all[i].GATHER_TIME);
        		}
        		if(list_data_all[i].DEVICE_CODE!=null&&list_data_all[i].DEVICE_CODE!=''){
        			strContent += "<br/><div style='float:left'>设备编号："+(list_data_all[i].DEVICE_CODE==null?"":list_data_all[i].DEVICE_CODE)+"</div><div style='float:right'><a href='javascript:void(0)' onclick='newWindow(\""+list_data_all[i].VIDEOURL+"\")' >查看视频</a></div>";
        		}else{
        			strContent += "<br/><a href='javascript:void(0)' onclick='newWindow(\""+list_data_all[i].VIDEOURL+"\")' style='float:right'>查看视频</a>";
        		}
        		if(list_data_all[i].PHOTOURL!=null && list_data_all[i].PHOTOURL!=''){
        			strContent += "<br/><img width='250px' height='150px' src='"+contextPath+list_data_all[i].PHOTOURL+"'>";
        		}
        		var pageName="red";
        		
        		var hoverPageNmae="blue";
        		var tip={"index":i,fireId:list_data_all[i].ID,"title":title,content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].LNG_D,list_data_all[i].LAT_D,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    		
    	}
    	for(var i=0;i<data.length;i++)//根据列表数据画点
    	{
    		if(typeof data[i].LNG_D !="undefined"&&typeof data[i].LAT_D !="undefined" &&data[i].LNG_D!=""&&data[i].LAT_D!=""){
    			var strContent = '';
        		strContent += "站点名称:"+data[i].HAZARD_ADDRESS;
        		var title = "";
        		if(data[i].WXY_TYPE=="1"){
        			title = "沼气池";
        			strContent += "<br/>";
        			strContent += "<div><div style='width:49%;float:left'>沼气浓度："+(data[i].METHANE_CONC==null?'':data[i].METHANE_CONC);
            		strContent += "</div><div style='width:49%;float:right'>沼气温度："+(data[i].TEMPERATURE==null?'':data[i].TEMPERATURE);
            		strContent += "</div></div><br/>";
            		strContent += "<div><div style='width:49%;float:left'>供电状态："+(data[i].ELEC_STATUS==null?'':data[i].ELEC_STATUS);
            		strContent += "</div><div style='width:49%;float:right'>水位状态："+(data[i].WATER_STATUS==null?'':data[i].WATER_STATUS)+"</div></div>";
        		}
        		if(data[i].WXY_TYPE=="2"){
        			title = "下水道";
        			strContent += "<br/>";
        		}
				if(data[i].WXY_TYPE=="3"){
					title = "重点单位";
					strContent += "<br/>";
					strContent += "<div style='float:left'>重大危险源级别："+(list_data_all[i].DANGERLEVEL==null?'':list_data_all[i].DANGERLEVEL)+"</div>";
				}
				if(data[i].WXY_TYPE=="4"){
					title = "应急避难场所";
					strContent += "<br/>";
				}
        		
        		if(data[i].GATHER_TIME!=null&&data[i].GATHER_TIME!=''){
        			strContent += "<br/>采集时间："+formatpc(data[i].GATHER_TIME);
        		}
        		if(data[i].DEVICE_CODE!=null&&data[i].DEVICE_CODE!=''){
        			strContent += "<br/><div style='float:left'>设备编号："+(data[i].DEVICE_CODE==null?"":data[i].DEVICE_CODE)+"</div><div style='float:right'><a href='javascript:void(0)' onclick='newWindow(\""+list_data_all[i].VIDEOURL+"\")'>查看视频</a></div>";
        		}else{
        			strContent += "<br/><a href='javascript:void(0)' onclick='newWindow(\""+list_data_all[i].VIDEOURL+"\")' style='float:right'>查看视频</a>";
        		}
        		if(data[i].PHOTOURL!=null && data[i].PHOTOURL!=''){
        			strContent += "<br/><img width='250px' height='150px' src='"+contextPath+data[i].PHOTOURL+"'>";
        		}
				var pageCount=i+1;
    			
        		var pageName="p"+pageCount;
        		var hoverPageNmae="ph"+pageCount;
        		var tip={"index":i,fireId:data[i].ID,"title":title,content:strContent};
        		var pointId=gmap.addPoint(data[i].LNG_D,data[i].LAT_D,tip,pageName,hoverPageNmae,clickHandler);
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
//    	gmap.centerAtPoint(data[0].LNG_D,data[0].LAT_D,11);
    },1000);
}

//普通列表和地图列表的绑定生成
function doubleTableRender(contextPath,listDiv,pageDiv,mapListDiv,mapPageDiv,requestData,clickHandler){
	var imgurl=contextPath +"/js/gmap/images";
	var opts_map = {
	        basePath:contextPath +"/js/gmap/ulynlist",
	        url:contextPath+'/dangerous/dangerous_list.do',
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
	                     
	                     {field: '', label: '地址', className: "address", width: 50, bodyContent: "[HAZARD_ADDRESS]"},
	                     {
	                         field: '', label: '姓名', className: "sortable", width: 50,
	                         bodyContent: "<a href='javascript:Void(0)'>[REPORT_PERSON]</a><span>[PHONE]</span>"
	                     }
	                     
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
	        	 var typeReal = "";
	        	 $(".checktype").each(function(){
	        			if($(this).hasClass("all-style")){
	        				if($(this).attr("data-id") != "0"){
	        					typeReal = $(this).attr("data-id");					
	        				}
	        			}
	        	    });
	        	 var inputdata = {"type":typeReal};
	        	 $.ajax({
	        	        type:"POST",
	        	        url:contextPath+'/dangerous/dangerous_list_all.do',
	        	        data:inputdata,
	        	        dataType:"json",
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
	var typeReal = "";
	$(".checktype").each(function(){
		if($(this).hasClass("all-style")){
			typeReal = $(this).attr("data-id");					
		}
    });
    var requestData = {"type":typeReal,linesPerPage:10};

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

function changetype(obj){
	$(".checktype").each(function(){
		$(this).removeClass("all-style");
    });
	$(obj).addClass("all-style");
	queryDataAssembly();
	
}

function newWindow(yoururl) {
	//var open_link = window.open('', '_newtab');
	//open_link.location =yoururl;

	//window.open(yoururl, '_blank', 'toolbar=0,location=0,menubar=0');
	//window.open (yoururl,'newwindow','height=500,width=800,top=125,left=350,toolbar=no,menubar=no,scrollbars=no, //resizable=no,location=no, status=no');
	var height = $(window).height();
	var width = $(window).width();
	var top = (height-700)/2;
	var left = (width-500)/2;
	window.open(yoururl, '_blank','height=500,width=700,top='+top+',left='+left);
	}

$(function(){
    //侧边栏渲染
//    renderSidebar();
	
    //表格渲染
	queryDataAssembly();
	
    $(".search-btn").click(function(){
    	queryDataAssembly();
    });
    
});