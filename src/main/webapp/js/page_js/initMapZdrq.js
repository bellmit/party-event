function map_listAddPoints(list_data_all,clickHandler,TYPE)//地图列表点绘制方法
{
	setTimeout(function(){
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    		if(typeof list_data_all[i].X !="undefined"&&typeof list_data_all[i].Y !="undefined" &&list_data_all[i].X!=""&&list_data_all[i].Y!=""){
    			var obj = gmap.webMercatorToGeographic(list_data_all[i].X,list_data_all[i].Y)
    			var strContent = '';
    			strContent += ""+list_data_all[i].JDMC+"-"+list_data_all[i].SQMC+"-"+list_data_all[i].DZQC+"</br>";
    			strContent += "人群数量："+list_data_all[i].JMCOUNT+"<div><a href='javascript:void(0)' onclick='newWindow(\""+contextPath+"/zdrq/list.do?TYPE="+TYPE+"&LDBH="+list_data_all[i].LDBH+"\")' style='float:right'>详情</a></div>";
        		var pageName="red";
        		var hoverPageNmae="blue";
        		var tip={"index":i,"title":"",content:strContent};
        		var pointId=gmap.addPoint(obj.x,obj.y,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    	}
		//gmap.centerAtPoint('106.4800585661','29.4775758593',10);
		if(list_data_all.length > 0){
			gmap.setExtentByshowPointGraphicsLayer();			
		}
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    },1000);
	
}


function doubleTableRender(clickHandler){
	 var inputdata = {"COMMUNITY":$("#COMMUNITY").val(),"TYPE":$("#TYPE").val(),"NAME":$("#NAME").val(),"STREET":$("#STREET").val(),"QX":$("#QX").val(),linesPerPage:10,currentPage:1};
	 $.ajax({
	        type:"POST",
	        url:contextPath+'/zdrq/zdrq_list.do?LDBH=',
	        data:inputdata,
	        dataType:"json",
	        success:function(data){
	        	$("#allcount").html(data.data.totalNum);
	        },
	        beforeSend:function(){
	        },
	        error:function(){
	        	
	        }
		 });
	 $.ajax({
	        type:"POST",
	        url:contextPath+'/zdrq/zdrq_list_all.do',
	        data:inputdata,
	        dataType:"json",
	        success:function(data){
	        	list_data_all = data.data.list;
	        	map_listAddPoints(list_data_all,clickHandler,inputdata.TYPE);
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
	window.location.href =window.location.href; 
}

function newWindow(yoururl) {
	var height = $(window.parent.document).height();
	var width = $(window.parent.document).width();
	var top = (height-500)/2;
	var left = (width-850)/2;
	window.open(yoururl, '_blank','height=500,width=850,top='+top+',left='+left);
	}

function searchHospital(){
	doubleTableRender(clickHandler);
}

$(function(){
	
	doubleTableRender(clickHandler);
	$("#search-reset").click(function(){
		$("#s2id_TYPE").find("span").eq(0).html("请选择");
    	$("#s2id_STREET").find("span").eq(0).html("请选择");
    	$("#TYPE").val("");
    	$("#STREET").val("");
    });
    
});