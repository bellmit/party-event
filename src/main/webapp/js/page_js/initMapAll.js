var isCenter = 1;
var serach_circle;
//圆圈样式
var style = [];
style.line = [];
style.fill = [];
style.line.style = "solid";
style.line.color = [255,255,255,.8];
style.line.width = 1;
style.fill.style = "solid";
style.fill.color = [255,0,0,0.2];
function map_listAddPoints(list_data_all,clickHandler)//地图列表点绘制方法
{
	
	$("#allcount").html(list_data_all.length);
	var isHave = false;
	setTimeout(function(){
		var redcount = 0;
		var yellowcount = 0;
		var greencount = 0;
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
			var info = list_data_all[i];
    		if(typeof info.LNG_D !="undefined"&&typeof info.LAT_D !="undefined" &&info.LNG_D!=""&&info.LAT_D!=""){
    			if(info.infoType == "meet"){
	    			var strContent = '' , title=info.TITLE;
	    			strContent += "活动单位:"+info.NAME;
	    			strContent += "<br/>";
	        		strContent += "活动地址:"+info.ADDRESS;
	    			strContent += "<br/>";
	    			strContent += "<div style='float:left'>开始时间："+formatpc(info.START_TIME)+"</div>";
	    			strContent += "<br/>";
	    			strContent += "<div style='float:left'>结束时间："+formatpc(info.END_TIME)+"</div>";
	        		var pageName="mark",hoverPageNmae="mark1";
	        		var tip={"index":i,fireId:info.EXT_ID,"title":title,content:strContent,"name":title,"count":strContent};
	        		var pointId=gmap.addPoint(info.LNG_D,info.LAT_D,tip,pageName,hoverPageNmae,clickHandler);
	        		var pointInfo={"id":pointId,"index":i};
	        		pointArray.push(pointInfo);
    			}else if(info.infoType == "qd"){
	    			var strContent = '' , title=info.TITLE;
	    			strContent += "教师:"+info.NAME;
	    			strContent += "<br/>";
	        		strContent += "签到地址:"+info.ADDRESS;
	    			strContent += "<br/>";
	    			strContent += "<div style='float:left'>签到时间："+formatpc(info.CREATETIME)+"</div>";
	        		var pageName="blue",hoverPageNmae="blue1";
	        		var tip={"index":i,fireId:info.EXT_ID,"title":title,content:strContent,"name":title,"count":strContent};
	        		var pointId=gmap.addPoint(info.LNG_D,info.LAT_D,tip,pageName,hoverPageNmae,clickHandler);
	        		var pointInfo={"id":pointId,"index":i};
	        		pointArray.push(pointInfo);
    			}
    		}
    	}
		
    	gmap.setExtentByshowPointGraphicsLayer();
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    	
    	$("#legend").remove();
    },1000);
}

function getPronum(progress,e){
	doubleTableRender(null,progress);
}

//普通列表和地图列表的绑定生成
function doubleTableRender(clickHandler,progress){
	if(progress == '0'){
		redcount = 0;
	}
	if(progress == '1'){
		yellowcount = 0;
	}
	if(progress == '2'){
		greencount = 0;
	}
	if(progress == null){
		redcount = 0;
		yellowcount = 0;
		greencount = 0;
	}
	var typeReal = "";
	 $(".checktype").each(function(){
			if($(this).hasClass("all-style")){
				if($(this).attr("data-id") != "0"){
					typeReal = $(this).attr("data-id");					
				}
			}
	    });
//	 var inputdata = {"type":typeReal,"process":progress};
//	 $.ajax({
//	        type:"POST",
//	        url:contextPath+'/dangerous/dangerous_list_all.do',
//	        data:inputdata,
//	        dataType:"json",
//	        success:function(data){
//	        	list_data_all = data.data.list;
	        	map_listAddPoints(v_list_data_all,clickHandler);
//	        },
//	        beforeSend:function(){
//	        },
//	        error:function(){
//	        	map_listAddPoints(list_data_all,clickHandler);
//	        }
//		 });
	 var height = $(window).height()-$("#listMapLi").offset().top;
	 if($("#mapList").find("ul").height()>(height-$("#mapPager").height())){
		 $("#mapList").height((height-$("#mapPager").height())+"px");
	 }
	var height = $(window).height()-$("#listMapLi").offset().top;
	$("#listMapLi").height(height+"px");
}
function clickHandler(index,x,y,tip){
	//window.location.href=contextPath+'/fire/detail.do?fireId='+tip.fireId;
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
	doubleTableRender(null);
	
}

function newWindow(yoururl) {
	//var open_link = window.open('', '_newtab');
	//open_link.location =yoururl;

	//window.open(yoururl, '_blank', 'toolbar=0,location=0,menubar=0');
	//window.open (yoururl,'newwindow','height=500,width=800,top=125,left=350,toolbar=no,menubar=no,scrollbars=no, //resizable=no,location=no, status=no');
	var height = $(window.parent.document).height();
	var width = $(window.parent.document).width();
	var top = (height-500)/2;
	var left = (width-700)/2;
	window.open(yoururl, '_blank','height=500,width=700,top='+top+',left='+left);
	}

function newWindowdetail(yoururl) {
	//var open_link = window.open('', '_newtab');
	//open_link.location =yoururl;

	//window.open(yoururl, '_blank', 'toolbar=0,location=0,menubar=0');
	//window.open (yoururl,'newwindow','height=500,width=800,top=125,left=350,toolbar=no,menubar=no,scrollbars=no, //resizable=no,location=no, status=no');
	var height = $(window.parent.document).height();
	var width = $(window.parent.document).width();
	var top = (height-430)/2;
	var left = (width-930)/2;
	window.open(yoururl, '_blank','height=430,width=930,top='+top+',left='+left);
	}

function getWEB(){
		isUpdate = false;
		$.ajax({
	        type:"POST",
	        url:contextPath+"/dangerous/getWEB.do",
	        data:"",
	        dataType:"json",
	        success:function(data){
	        	alert("数据刷新成功");
	        	doubleTableRender(null);
	        },
	        beforeSend:function(){
	        },
	        error:function(){
	        	doubleTableRender(null);
	        }
		 });
	
	
}
var year = new Date().getFullYear();
function formatpc(str){
	var strTime = '';
	strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12);
//	return strTime.replace(year+"-","");
	return strTime;
}
//以学校为圆心画圆
function drawThreeCircle(data){
	//判断是否有数据
	if(data&&data.length>0){
		//清理圆
		initCircle();
		//获取学校的经纬度
		var x = data[0].LNG_D;
		var y = data[0].LAT_D;
		setTimeout(function(){
			//设置半径
			var radiusDefault;
			//画半径200的圆
			radiusDefault = 200;
			drawCircle(x,y,radiusDefault,"","",data);
			//画半径100的圆
			radiusDefault = 100;//半径
			//样式
			style.line = [];
			style.fill = [];
			style.line.style = "solid";
			style.line.color = [255,255,255,.8];
			style.line.width = 1;
			style.fill.style = "solid";
			style.fill.color = [255,255,0,0.2];
			drawCircle(x,y,radiusDefault,"","",data);
			//画半径50的圆
			radiusDefault = 50;//半径
			style.line = [];
			style.fill = [];
			style.line.style = "solid";
			style.line.color = [255,255,255,.8];
			style.line.width = 1;
			style.fill.style = "solid";
			style.fill.color = [0,255,0,0.2];
			drawCircle(x,y,radiusDefault,"","",data);
	    	setTimeout(function(){
	    		var height = 10*gmap.getMapLevel()+1;
	    		var top = 165-height;
	    		$("#sliderHeight").height(height);
	        	$("#sliderTop").css("top",top+"px");
	    	},1500);
	    },1000);
	}
}
//画圆
function drawCircle(x,y,radius,tip,clickHandler,data){
	serach_circle = gmap.addCircleForQuery(x,y,radius,tip,style,clickHandler);
	if(isCenter == 1){
		gmap.centerAtPoint(x,y,15);
		isCenter = 2;
	}
}
//清除圆
function initCircle(){
	gmap.clearQueryCircle();
	gmap.clearPolygons();
}
$(function(){
	setTimeout(doubleTableRender(null),1000);
});