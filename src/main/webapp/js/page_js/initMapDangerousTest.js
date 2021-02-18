
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
        			strContent += "<br/><div style='float:left'>设备编号："+(list_data_all[i].DEVICE_CODE==null?"":list_data_all[i].DEVICE_CODE)+"</div>";
        			strContent += "<div style='float:right'>";
        			if(list_data_all[i].WXY_TYPE=='1'||list_data_all[i].WXY_TYPE=='2'){
        				strContent += "<a href='javascript:void(0)' onclick='newWindowdetail(\""+contextPath+"/dangerous/detailTest.do?id="+list_data_all[i].ID+"\")' style='margin-right:10px'>详情</a>";
        			}
        			if(list_data_all[i].WXY_TYPE!='1'&&list_data_all[i].WXY_TYPE!='2'){
    					strContent += "<a href='javascript:void(0)' onclick='newWindow(\""+list_data_all[i].VIDEOURL+"\")' style='margin-right:10px'>视频</a>";
    				}
        			if(list_data_all[i].WXY_TYPE=='1'||list_data_all[i].WXY_TYPE=='2'){
    					strContent +="<a href='javascript:void(0)' onclick='getWEB()' style='margin-right:10px'>刷新</a>";	
    				}
        			strContent+="</div>";
        		}else{
        			strContent += "<br/>";
        			strContent += "<div style='float:right'>";
    				if(list_data_all[i].WXY_TYPE=='1'||list_data_all[i].WXY_TYPE=='2'){
    					strContent +="<a href='javascript:void(0)' onclick='newWindowdetail(\""+contextPath+"/dangerous/detailTest.do?id="+list_data_all[i].ID+"\")' style='margin-left:10px'>详情</a>";	
    				}
    				if(list_data_all[i].WXY_TYPE!='1'&&list_data_all[i].WXY_TYPE!='2'){
    					strContent += "<a href='javascript:void(0)' onclick='newWindow(\""+list_data_all[i].VIDEOURL+"\")' style='margin-right:10px'>视频</a>";
    				}
        			if(list_data_all[i].WXY_TYPE=='1'||list_data_all[i].WXY_TYPE=='2'){
    					strContent +="<a href='javascript:void(0)' onclick='getWEB()' style='margin-right:10px'>刷新</a>";	
    				}
        			strContent+="</div>";
        		}
        		if(list_data_all[i].PHOTOURL!=null && list_data_all[i].PHOTOURL!=''){
        			strContent += "<br/><img width='250px' height='150px' src='"+contextPath+list_data_all[i].PHOTOURL+"'>";
        		}
        		var pageName="";
        		if(list_data_all[i].METHANE_CONC==null||list_data_all[i].METHANE_CONC<70){
        			if(list_data_all[i].WXY_TYPE=="1"||list_data_all[i].WXY_TYPE=="2"){
	        			pageName="greengif";
	        			greencount++;
        			}
        		}else if(list_data_all[i].METHANE_CONC<90&&list_data_all[i].METHANE_CONC>70){
        			pageName="yellowgif";
        			yellowcount++;
        		}else if(list_data_all[i].METHANE_CONC>90){
        			pageName="redgif";
        			redcount++;
        		}
        		var hoverPageNmae=pageName;
        		if(list_data_all[i].WXY_TYPE=="3"||list_data_all[i].WXY_TYPE=="4"){
        			pageName="red";
        			hoverPageNmae="blue";
        		}
        		if(list_data_all[i].WXY_TYPE=="1"||list_data_all[i].WXY_TYPE=="2"){
        			isHave = true;
        		}
        		var tip={"index":i,fireId:list_data_all[i].ID,"title":title,content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].LNG_D,list_data_all[i].LAT_D,tip,pageName,hoverPageNmae,clickHandler);
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
    	
    	$("#legend").remove();
    	if(isHave){
	    	var div = "<div id='legend' class=' BMap_noprint anchorBR zdgc-con' style='position: absolute; z-index: 10; bottom: 0px; left: auto;right:12px; top: auto; color:black;'>"+
		    "<div class='legend-text' style='float:left'>图例：</div>"+
		    "<a href='javascript:void(0);' title='报警' class='legend-item legend-mapro-1' style='width:70px;text-align:center;line-height: 18px;' onclick='getPronum(0,this)'>"+
		     " <img src=\""+contextPath+"/js/gmap/images/redgif.gif"+"\" width='20px' height='20px'>("+redcount+")</a>"+
		    "<a href='javascript:void(0);' title='预警' class='legend-item legend-mapro-2' style='width:70px;text-align:center;line-height: 18px;' onclick='getPronum(1,this)'>"+
		    " <img src=\""+contextPath+"/js/gmap/images/yellowgif.gif"+"\" width='20px' height='20px'>("+yellowcount+")</a>"+
		    "<a href='javascript:void(0);' title='正常' class='legend-item legend-mapro-3' style='width:70px;text-align:center;line-height: 18px;' onclick='getPronum(2,this)'>"+
		    " <img src=\""+contextPath+"/js/gmap/images/greengif.gif"+"\" width='20px' height='20px'>("+greencount+")</a></div>";
		     $("#map_root").append(div);
    	}
    	
//    	gmap.centerAtPoint(data[0].LNG_D,data[0].LAT_D,11);
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
	 var inputdata = {"type":typeReal,"process":progress};
	 $.ajax({
	        type:"POST",
	        url:contextPath+'/dangerous/dangerous_list_all.do',
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

$(function(){
    //侧边栏渲染
//    renderSidebar();
	
	doubleTableRender(null);
	
    
});