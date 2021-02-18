function addPolygonList(polygons, level) {
	var tip = [];
	var objid = [];
	var polygonDefaultStyle = {
		line : {
			style : 'solid',
			color : [ 0, 100, 190, 1 ],
			width : 2
		},
		fill : {
			style : 'solid',
			color : [ 255, 0, 0, 0 ]
		}
	};
	for (var i = 0; i < polygons.length; i++) {
		tip.push({
			"color" : null,
			"name" : "",
			type : "scribing"
		});
		objid.push(polygons[i]);
	}
	if (objid.length > 0 && objid.length == tip.length) {
		gmap.addPolygonById(objid, tip, polygonDefaultStyle, reclickHandle,
				level);
	}
}
function reclickHandle(index, x, y, tip) {
}
var sComCode="";
/* ajax请求方式填充社区 */
function getBmAjaxList(code, level, obj) {
	var inputdata = {
		"comCode" : code
	};
	$.ajax({
		type : "POST",
		url : config.bmUrl,
		data : inputdata,
		dataType : "json",
		async : false,
		success : function(data) {
			setData(data.data.list, level, code, obj);
		},
		beforeSend : function() {
		},
		error : function() {
		}
	});
}
/* JS方式填充社区 */
function getBmList(code, level, obj) {
	sComCode = code;
	var comCode = code.endWith("000") ? code.substring(0, 9) : code;
	var bmList1 = [];
	var bmLen = bmList.length;
	for (var i = 0; i < bmLen; i++) {
		var xx = bmList[i];
		if (typeof bmList[i].SQDM != 'undefined'
				&& bmList[i].SQDM.startWith(comCode)) {
			bmList1.push(bmList[i]);
		}
	}
	if (bmList1.length > 0) {
		setData(bmList1, level, code, obj);
	}
}
var heatmapcode=false;
function heatMapInitNew(code){
	heatmapcode = true;
	if(code=="500104000000"){//所选为大渡口区
		heatmapcode=false;
		heatMapInit(heatmaplist);
	}else if(code.endWith("000")){//所选为街道
		code = code.substring(0,9);
	}
	var heatData=[];
	for(var i=0;i<heatmaplist.length;i++){
		sqdm = heatmaplist[i].attributes.sqdm;
		if(sqdm && sqdm.startWith(code)){
			heatData.push(heatmaplist[i]);
		}
	}
	gmap.clearHeatMapData(heatData);
}
// 全局定义划片数组
var polygons = [];
/* 填充社区与街道方法 */
function setData(data, level, code, obj) {
	polygons = [];// 清空划片数据数组
	gmap.clearPolygons();// 清空以划片数据
	if (level == 1) {// 如果是1级菜单点击事件
		setComDiv(data,code);
		addPolygonList(polygons, 3);
		heatMapInitNew(code);
	} else if (level == 2) {// 如果是2级社区点击事件
		if(obj){
			$(".all-style").removeClass();
			$(obj).addClass("all-style");
		}
		var dataLen = data.length;
		for (var i = 0; i < dataLen; i++) {
			if (code == data[i].SQDM) {
				polygons.push(data[i].MAPS_OBJECT_ID);
			}
		}
		addPolygonList(polygons, 4);
		heatMapInitNew(code);
	} else {// 如果是其他级别点击事件
		if (typeof code == 'undefined') code = "500104000000";
		if(code=="500104000000"){//所选为大渡口区
			var dataLen = data.length;
			for (var i = 0; i < dataLen; i++) {
				if (code == data[i].SQDM) {
					polygons.push(data[i].MAPS_OBJECT_ID);
				}
			}
			$("select").select2("val", code);
			getBmList(code,1);
			return;
		}
		if(code.endWith("000")){//所选为街道
			getBmList(code,1);
			$("select").select2("val", code);
			return;
		}
		//所选为社区
		var comCode = code.substring(0, 9);
		var streetCode = comCode+"000";
		var comData = [],dataLen = bmList.length;
		for (var i = 0; i < dataLen; i++) {
			if (bmList[i].SQDM.startWith(comCode)) {
				comData.push(bmList[i]);
			}
			if(streetCode == bmList[i].SQDM){
				//下拉框设定值
				$("select").select2("val", bmList[i].SQDM);
			}
		}
		setComDiv(comData,code,0);
		setData(comData,2,code);
	}
}
/*填充社区信息*/
function setComDiv(data,code,level){
	var str = "", cla = "", dataLen = data.length,isCla=false;
	for (var i = 0; i < dataLen; i++) {
		 if(code == data[i].SQDM){
			if (level!=0) {
				polygons.push(data[i].MAPS_OBJECT_ID);
				continue;
			}else{
				 cla ="class='all-style'";
				 isCla = true;
			}
		 }
		str += "<a href=\"javascript:void(0);\" " + cla
				+ " onclick=\"getBmList('" + data[i].SQDM + "',2,this)\">"
				+ data[i].ZNQMC + "</a>";
		cla = "";
		// polygons.push(data[i].MAPS_OBJECT_ID);
	}
	if (cla == "" && !isCla)
		cla = "class='all-style'";
	str = "<a href=\"javascript:void(0);\" " + cla
			+ " onclick=\"getBmList('" + code + "',2,this)\">全部</a>" + str;
	if (code == "") {
		str = "<a href=\"javascript:void(0);\" style=\"all-style\">全部</a>";
	}
	$(".deng-style").html(str);
	if (parseInt($('.deng-style').height()) > 31) {
		$('#comDiv').css('overflow','visible');
		$('.deng-style').css('border-color','#ddd');
		$(".more-a").removeClass("hide");
	} else {
		$('#comDiv').css('overflow','hidden');
		$('.deng-style').css('border-color','#fff');
		$(".more-a").addClass("hide");
	}
}
var bmList = [];// 社区街道集合数组
/* 查询JS动态加载数据 */
function searchSelect(val) {
	var str = "";
	var bmLen = bmList.length;
	for (var i = 0; i < bmLen; i++) {
		if (bmList[i].ZNQMC.indexOf(val) != -1) {
			str += "<li class=\"select2-results-dept-0 select2-result select2-result-selectable\" role=\"presentation\">"
					+ "<div class=\"select2-result-label\" id=\"select2-result-label-8\" role=\"option\" onclick=\"getBmList('"
					+ bmList[i].SQDM+ "',0);closeDiv();\"><span class=\"select2-match\"></span>"
					+ bmList[i].ZNQMC + "</div>" + "</li>";
		}
	}
	$(".select2-results").html(str);
}
/* 关闭查询div */
function closeSearchDiv(){
	setTimeout(function() {
		closeDiv();
	}, 200);
}
function closeSearchDiv1() {
//	setTimeout(function() {
//		closeDiv();
//	}, 200);
//	var div = document.getElementById("search-hide");
//	var divx1 = div.offsetLeft;
//    var divy1 = div.offsetTop;  
//    var divx2 = div.offsetLeft + div.offsetWidth;  
//    var divy2 = div.offsetTop + div.offsetHeight;
	var xx = mx;
	var yy = my;
//	var X = $('#search-hide').offset().top; 
//	var Y = $('#search-hide').offset().left;
//	var X1 = $('#search-hide').width()+X;
//	var Y1 = $('#search-hide').height()+Y;
//    if( mx < X || mx > X1 
//    		|| my < Y || my > Y1){  
//    	closeDiv();
//    }
	var d_left = document.getElementById('search-hide').offsetLeft;
    var d_top = document.getElementById('search-hide').offsetTop;
    var d_width = document.getElementById('search-hide').clientWidth;
    var d_height = document.getElementById('search-hide').clientHeight;
 //alert(wx + '_' + wy + '_' + d_left + '_' + d_width + '_' + d_top + '_' + d_height)
    if(xx < d_left || yy < d_top || xx > (d_left + d_width) || yy > (d_top + d_height))
    	closeDiv();
}
var mx=0;my=0;
function mouseMove(ev) 
{ 
	Ev= ev || window.event; 
	var mousePos = mouseCoords(ev); 
	mx=mousePos.x;
	my=mousePos.y;
} 
function mouseCoords(ev) 
{ 
	if(ev.pageX || ev.pageY){ 
		return {x:ev.pageX, y:ev.pageY}; 
	} 
	return{ 
		x:ev.clientX + document.body.scrollLeft - document.body.clientLeft, 
		y:ev.clientY + document.body.scrollTop - document.body.clientTop 
	}; 
} 
/* 隐藏div */
function closeDiv() {
	$('.search-hide').addClass('hide');
	$('.search-a').parent().css('border-color', '#fff');
}
/* 统计列表及按钮显示隐藏 */
function hideAndShow(flag) {
	if (flag == 1) {
		// $(obj).children('#mapTable').show();
		$(".mapDiv1").addClass("hide");
		$(".mapDiv2").removeClass("hide");
	} else {
		// $(obj).children('#mapTable').hide();
		$(".mapDiv1").removeClass("hide");
		$(".mapDiv2").addClass("hide");
	}
}
/* ajax请求方式填充配置列表*/
function getConfigAjaxList(code) {
	var inputdata = {
	};
	$.ajax({
		type : "POST",
		url : config.configUrl,
		data : inputdata,
		dataType : "json",
		async : false,
		success : function(data) {
			setConfigData(data.data.list,code);
		},
		beforeSend : function() {
		},
		error : function() {
		}
	});
}
function setConfigData(data,code){
	var str="";
	for(var i=0;i<data.length;i++){
//		if(code!=data[i].SEARCH_CODE)
//			str+="<div class=\"mapDiv3 hide\" codevalue='"+data[i].SEARCH_CODE+"' onclick=\"hideAndShow1(this);\">"
//			+"<span style=\"width: 100px;background: #fff; padding: 5px; border: #999 solid 1px; color: #777; cursor: pointer; border-radius: 3px;\">"
//			+data[i].SEARCH_NAME+"</span></div>";
		str += "<li class=\"select2-results-dept-0 select2-result select2-result-selectable\" style='width: 100%;' role=\"presentation\">"
			+ "<div class=\"select2-result-label\" id=\"select2-result-label-8\" role=\"option\" onclick=\"getConfigAjaxMapList('"
			+ data[i].SEARCH_CODE+ "','"+ data[i].SEARCH_NAME + "');\"><span class=\"select2-match\"></span>"
			+ data[i].SEARCH_NAME + "</div>" + "</li>";
	}
//	$("#heatMapConfigList").append(str);
	$("#mapTable1").html(str);
}
var mapDiv3 = 1;
function hideAndShow1(obj) {
	if(mapDiv3==0){
		$(".mapDiv4").removeClass("hide");
		mapDiv3=1;
	}
	else
	{
		mapDiv3=0;
		$(".mapDiv4").addClass("hide");
	}
//	if(mapDiv3==0){
//		$(".mapDiv3").removeClass("hide");
//		mapDiv3=1;
//	}
//	else{
//		mapDiv3=0;
//		$(".mapDiv3").addClass("hide");
//		$(obj).removeClass("hide");
//		getConfigAjaxMapList($(obj).attr("codevalue"));
//	}
}
/* ajax请求方式填充地图数据列表*/
function getConfigAjaxMapList(code,name) {
//	hideAndShow1();
	LayerShow("");
	$(".mapDiv3").find("span").html(name);
	var inputdata = {
			code : code,
			comCode:sComCode
	};
	var loadi=0;
	$.ajax({
		type : "POST",
		url : config.mapUrl,
		data : inputdata,
		dataType : "json",
		async : false,
		success : function(data) {
       	 	layer.close(loadi);
			setMapData(data,code);
		},
		beforeSend : function() {
			loadi = layer.load('加载中…');
		},
		error : function() {
       	 layer.close(loadi);
		}
	});
}
function setMapData(data,code){
	heatmaplist = data.list;
	heatmapQUlist = data.listQU;
	radius = data.bean.radius;
//	if($("#map")){
//    	var mapLevel = $("#map").attr("data-zoom");
//    	if(mapLevel<14){
//    		heatmapcode = false;
//    	}
//    }
	gmap.clearHeatMapData(data.listCOM?data.listCOM:heatmaplist);
	setTimeout(function(){LayerHide();},1000);
	
}

function loadDiv(text) {
     var div = "<div id='_layer_'> <div id='_MaskLayer_' style='filter: alpha(opacity=30); -moz-opacity: 0.3; opacity: 0.3;background-color: #000; width: 100%; height: 100%; z-index: 1000; position: absolute;left: 0; top: 0; overflow: hidden; display: none'></div><div id='_wait_' style='z-index: 1005; position: absolute; width:430px;height:218px; display: none'  ><center><h3>" + text + "<img src='../images/loading.gif' /></h3><button class='hide' onclick='LayerHide()'>关闭</button></center></div></div>"; 
   return div; 
}
function LayerShow(text) {
    var addDiv= loadDiv(text);  
  var element = $(addDiv).appendTo(document.body);     
  $(window).resize(Position);  
   var deHeight = $(document).height();    
 var deWidth = $(document).width();    
 Position();     
$("#_MaskLayer_").show();   
  $("#_wait_").show();
 }
function Position() {  
	 $("#_MaskLayer_").width($(document).width());   
	  var deHeight = $(window).height();     
	  var deWidth = $(window).width();     
	  $("#_wait_").css({ left: (deWidth - $("#_wait_").width()) / 2 + "px", top: (deHeight - $("#_wait_").height()) / 2 + "px" }); 
	}
function LayerHide() { 
    $("#_MaskLayer_").hide(); 
    $("#_wait_").hide(); 
    del(); 
}
function del() { 
	 var delDiv = document.getElementById("_layer_");     delDiv.parentNode.removeChild(delDiv); 
	 //删除
	 }
String.prototype.endWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substring(this.length - str.length) == str)
		return true;
	else
		return false;
	return true;
}
String.prototype.startWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substr(0, str.length) == str)
		return true;
	else
		return false;
	return true;
}
function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
