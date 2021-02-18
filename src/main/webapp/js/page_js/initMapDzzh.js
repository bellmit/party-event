function map_listAddPoints(list_data_all,clickHandler)//地图列表点绘制方法
{
	
	setTimeout(function(){
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    		if(typeof list_data_all[i].LNG !="undefined"&&typeof list_data_all[i].LAT !="undefined" &&list_data_all[i].LNG!=""&&list_data_all[i].LAT!=""){
    			var strContent = '';
    			strContent += "灾害地址:"+list_data_all[i].STREET+"-"+list_data_all[i].COMMUNITY+"</br>";
    			strContent += "主要诱因:"+list_data_all[i].REASON+"</br>";
        		strContent += "灾害类型:"+list_data_all[i].TYPE+"</br>";
        		strContent += "防灾责任单位:"+list_data_all[i].HANDLE_UNIT+"</br>";
        		strContent += "单位联系电话:"+list_data_all[i].HANDLE_UNIT_PHONE+"</br>";
        		strContent += "群测群防员:"+list_data_all[i].QCQF_PERSON+"</br>";
        		strContent += "人员联系电话:"+list_data_all[i].QCQF_PERSON_PHONE+"</br>";
        		strContent += "发生时间:"+list_data_all[i].TIME;
        		var pageName="red";
        		var hoverPageNmae="blue";
        		var tip={"index":i,"title":list_data_all[i].NAME,content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].LNG,list_data_all[i].LAT,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    	}
		var strMapTable = "";
		for (var i = 0; i < list_data_all.length; i++) {
			strMapTable += "<li class='mapTableLi' style='width:347px'><span onclick='showTip("+i+")' style='cursor:pointer;'>" + list_data_all[i].NAME
					+"("+list_data_all[i].TYPE+")"+ "</span><span style=\"right: 10px; position:absolute; top:7px;\">"
					+(list_data_all[i].DANGE_FORTUNE==null?"0":list_data_all[i].DANGE_FORTUNE==""?"0":list_data_all[i].DANGE_FORTUNE)+ "</span></li>";
		}
		$("#mapTable").html(strMapTable);
		$("#allcount").html(list_data_all.length);
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


function doubleTableRender(clickHandler){
	 var inputdata = {"COMMUNITY":$("#COMMUNITY").val(),"TYPE":$("#TYPE").val(),"NAME":$("#NAME").val(),"STREET":$("#STREET").val(),"QX":$("#QX").val()};
	 $.ajax({
	        type:"POST",
	        url:contextPath+'/dzzh/dzzh_list_all.do',
	        data:inputdata,
	        dataType:"json",
	        success:function(data){
	        	list_data_all = data.data.list;
	        	map_listAddPoints(list_data_all,clickHandler);
	        },
	        beforeSend:function(){
	        },
	        error:function(){
	        	map_listAddPoints(list_data_all,clickHandler);
	        }
		 });
	 var height = $(window).height()-$("#listMapLi").offset().top;
	 if($("#mapList").find("ul").height()>(height-$("#mapPager").height())){
		 $("#mapList").height((height-$("#mapPager").height())+"px");
	 }
	var height = $(window).height()-$("#listMapLi").offset().top;
	$("#listMapLi").height(height+"px");
	$("select").select2();
}
function clickHandler(index,x,y,tip){
}

function showTip(index){
	if(typeof list_data_all[index].LNG !="undefined"&&typeof list_data_all[index].LAT !="undefined" &&list_data_all[index].LNG!=""&&list_data_all[index].LAT!=""){
		 gmap.closeInfoWindow();
		 for(var i = 0 ; i < $("#mapTable li").length;i++){
			 gmap.changePointToImg(i); 
		 }
		 //($("#mapTable li").eq(index)).css("background-color","#FF0000");
		 gmap.changePointToHoverImg(pointArray[index].id);
		 xy = gmap.tdtWebMercatorToGeographic(list_data_all[index].LNG,list_data_all[index].LAT);
		 gmap.centerAtPoint(xy.x,xy.y,13);
		 setTimeout(function(){
	    		var height = 10*gmap.getMapLevel()+1;
	    		var top = 165-height;
	    		$("#sliderHeight").height(height);
	        	$("#sliderTop").css("top",top+"px");
	    	},1500);
		 gmap.setPointToTop(pointArray[index].id);
	 }
}

function newWindow(yoururl) {
	var height = $(window.parent.document).height();
	var width = $(window.parent.document).width();
	var top = (height-500)/2;
	var left = (width-700)/2;
	window.open(yoururl, '_blank','height=500,width=700,top='+top+',left='+left);
	}

function searchHospital(){
	doubleTableRender(null);
}

$(function(){
	
	doubleTableRender(null);
	$("#search-reset").click(function(){
    	$("#s2id_STREET").find("span").eq(0).html("请选择");
    	$("#s2id_TYPE").find("span").eq(0).html("请选择");
    	$("#NAME").val("");
    	$("#TYPE").val("");
    	$("#STREET").val("");
    });
    
});