
function map_listAddPoints(list_data_all,clickHandler)//地图列表点绘制方法
{
	
	setTimeout(function(){
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    			var strContent = '';
        		strContent += "地址:"+list_data_all[i].ADDRESS;
    			strContent += "<br/><div>老人数量："+list_data_all[i].COUNT;
    			strContent += "<a href='javascript:void(0)' onclick='newWindowdetail(\""+contextPath+"/fire/detailOldman.do\")' style='float:right'>详情</a></div>";
        		var pageName="red";
        		var hoverPageNmae="blue";
        		var tip={"index":i,fireId:list_data_all[i].ID,"title":"",content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].LNG_D,list_data_all[i].LAT_D,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
		
    	gmap.setExtentByshowPointGraphicsLayer();
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    	setTimeout(function(){
    		for(var i=0;i<list_data_all.length;i++){
    			if(list_data_all[i].WXY_TYPE=="1"||list_data_all[i].WXY_TYPE=="2"){
    				if(list_data_all[i].METHANE_CONC<90&&list_data_all[i].METHANE_CONC>70){
    					gmap.setPointToTop(i);
    				}
    			}
    		}
    		for(var i=0;i<list_data_all.length;i++){
    			if(list_data_all[i].WXY_TYPE=="1"||list_data_all[i].WXY_TYPE=="2"){
    				if(list_data_all[i].METHANE_CONC>90){
    					gmap.setPointToTop(i);
    				}
    			}
    		}
    	},1500);
    	
//    	gmap.centerAtPoint(data[0].LNG_D,data[0].LAT_D,11);
    },1000);
}

//普通列表和地图列表的绑定生成
function doubleTableRender(clickHandler,progress){
	
	 $.ajax({
	        type:"POST",
	        url:contextPath+'/fire/fire_oldman_list_all.do',
	        data:"",
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
var isPointCircle = false;
function newWindowdetail(yoururl) {
	//var open_link = window.open('', '_newtab');
	//open_link.location =yoururl;

	//window.open(yoururl, '_blank', 'toolbar=0,location=0,menubar=0');
	//window.open (yoururl,'newwindow','height=500,width=800,top=125,left=350,toolbar=no,menubar=no,scrollbars=no, //resizable=no,location=no, status=no');
	var height = $(window).height();
	var width = $(window).width();
	var top = (height-415)/2;
	var left = (width-900)/2;
	window.open(yoururl, '_blank','height=430,width=930,top='+top+',left='+left);
	}

$(function(){
    //侧边栏渲染
//    renderSidebar();
	
	doubleTableRender(null);
	
	$("#draggable").click(function(){
        //alert("click");//点击（松开后触发）
        }).mousedown(function(e){
        _move=true;
        _x=e.pageX-parseInt($("#draggable").css("left"));
    });
    $("#draggable").click(function(){
        }).mouseup(function(e){
        	if(serach_circle!=null)
        	{  
        		pageNo=1;
        		var dis = $("#distance").val();
        		data = parseFloat(dis);
        		checkRadius(data*1000);
//             	areaSearch(serach_circle.Cc.center,serach_circle.Cc.radius);
        		areaSearch(serach_circle.getCenter(),data*1000);
        	}
        	
 //         alert("stop");
 //       _move=true;
 //       _x=e.pageX-parseInt($("#draggable").css("left"));			
    });
    
});
var serach_circle;
function drawCircle(x,y,radius,tip,clickHandler){
//	this.addCircle = function(x,y,radius,tip,style,clickHandler);
	var style = [];
	style.line = [];
	style.fill = [];
	style.line.style = "solid";
	style.line.color = [255,255,255,.8];
	style.line.width = 1;
	style.fill.style = "solid";
	style.fill.color = [0,100,190,0.2];
	serach_circle = gmap.addCircleForQuery(x,y,radius,tip,style,clickHandler);
}


