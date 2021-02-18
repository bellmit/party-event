<!--
 版权所有：厦门畅享信息技术有限公司
 Copyright 2014 Xiamen Sunsharing Information CO., LTD.
 All right reserved. 
====================================================
 文件名称: text.html
 修订记录：
 No    日期				作者(操作:具体内容)
 1.    14-10-29			Administrator(创建:创建文件)
====================================================
 文件描述：(说明本文件需要实现的内容)
 
-->
<%@page import="com.sunsharing.ihome.air.sys.ConfigParam"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>应急信息</title>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/gmap/style/slider/map.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-3.5.1/select2.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/sslib/qlTree/qlTree.css"/>"/>

    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/base.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/nineModules.css"/>"/>
<style>

	body #s2id_type .select2-choice{
		height: 30px;
		line-height: 30px;
		width:318px;
	}
	#divMouse{position:absolute;width:105px;height:30px;background-color:#979fad;display:none;z-index: 101;padding-left:5px} 


</style>
	<script type="text/javascript" src="<c:url value="/js/jquery.js"/>" ></script>
	<script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/select2-3.5.1/select2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/artTemplate/template-simple.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/gmap/ulynlist/ulynlist.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/gmap/ulynlist/ulynlist.table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/gmap/ulynlist/ulynlist.pagebar.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/qlTree/qlTree.js"/>"></script>
	<jsp:include page="../include/headconfig.jsp"></jsp:include>
	<script type="text/javascript">
	var ulynlistPath = {
		basePath : '<c:url value="/js/gmap/ulynlist"/>',
		url : '<c:url value="/js/gmap/ulynlist"/>'
	};
	var pointArray=new Array();
	var gmap;
	var list_data_all=new Array();
	</script>

    
	
	
	<script src="<c:url value="/lib/My97DatePicker/WdatePicker.js"/>" charset="gb2312"></script>
<!-- 	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=6e1d54a50dbf8ee27f7ea667e48809e3"></script> -->
	
	<script type="text/javascript">
	var imgs = 	[
		      		{
		      			img:'dingwei',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/dingwei.png'/>",
							"width":17,
							"height":19,
							"xOffset":0,
							"yOffset":0
		      			}
		      		},
		      		{
		      			img:'biaozhu',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ico_biaozhu.png'/>",
		      				"width":34,
		      				"height":34,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		}
		      	];
	function initMap(){
		gmap = new sunsharing.gmap({proxy:config.proxyUrl,
			isDefaultCenter:config.isDefaultCenter,
			defaultCenter:{x:config.x,y:config.y},
			defaultLevel:13,
			isUseJmLocalLayer:config.isUseJmLocalLayer,
			isUseJtLocalLayer:config.isUseJtLocalLayer,
			jmAreaLayerUrl:config.jmAreaLayerUrl,//现场精图卫星图地址
			jtNormalLayerUrl:config.jtAreaLayerUrl,//现场精图矢量图地址
			queryCommunityAreaURL:config.url+"/ArcGIS/rest/services/xzqh/FeatureServer/4",
			featureLayerUrl:config.url+"/ArcGIS/rest/services/XA_AREA/FeatureServer/0/",
			pointQueryUrl:config.url+"/ArcGIS/rest/services/CQ-DIAN/FeatureServer/0",
			isShowTip:false,
			initComplete:function(){
				gmap.registerImages(imgs);
			    loadModule(gmap,config.proxyUrl);
			},
			polygonDefaultStyle:{
					line:{
						style:'solid',	
						color:[0,100,190,1],
						width:1
					},
					fill:{
						style:'solid',
						color:[205,175,0,.8]
					}
				},
				isUseTDT: config.isUseTDT,
				wkid : config.coordinateSystem
		});
	}
	</script>
	<link rel='stylesheet' type='text/css' href='<c:url value="/js/gmap/style/gmap.css"/>'/>
	<script src="<c:url value="/js/gmap/layer/CustomTiledMapLayer.js"/>"></script>
	<script src="<c:url value="/js/gmap/layer/GoogleLabelLayer.js"/>"></script>
	<script src="<c:url value="/js/gmap/gmap_ql.js"/>"></script>
	<script src="<c:url value="/js/gmap/gmap_module_ql.js"/>"></script>
</head>
<style>
    
.jgxz{
	 font-weight: bold;;
	 color: #357ebd;
}
    
    </style>
<body onload="initMap();">
<div class="content-box">
    <div class="cb-content" style="margin-left: 0px">
        <div class="content">
            <div class="mainContent" style="top:0px">
                <div class="mcBox">
                    <div class="dwBottom" id="dwBottom">
		                <div id="listMapLi">
			                <div id="map" style="position:relative; width: 100%;height: 100%;"></div>
		                </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="divMouse">
单击添加位置信息
</div>
</body>
<script>
function setMouse(flag,tsure){
	sure = tsure?tsure:sure;
	if(sure=='0')flag = false;
    $("#divMouse").css("display",flag?"block":"none");
    $(document).mousemove(function (e) {
        var xPos = parseInt(e.pageX+12) + "px";
        var yPos = e.pageY + "px";
        $("#divMouse").css("left", xPos);
        $("#divMouse").css("top", yPos);
    });
}
function saveNewPoint(x,y,v,url){
// 		id = $("#frm").serialize();
	parent.setInputValue($("#address").val(),x,y);
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(index);	
}
function getTable(x,y){
	var content = "<form id='frm'>";
	$.ajax({
		url:config.amap_web_url,
		method:'get',
		data:{"key":config.amap_web_url_key,"location":x+","+y},
		dataType:'json',
		async:false,
		success:function(data){
			//{"status":"1","info":"OK","infocode":"10000","regeocode":{"formatted_address":"重庆市大渡口区新山村街道文体路170号重庆市大渡口区人民政府","addressComponent":{"country":"中国","province":"重庆市","city":[],"citycode":"023","district":"大渡口区","adcode":"500104","township":"新山村街道","towncode":"500104001000","neighborhood":{"name":[],"type":[]},"building":{"name":[],"type":[]},"streetNumber":{"street":"文体路","number":"170号","location":"106.482608,29.48416","direction":"东北","distance":"23.6036"},"businessAreas":[[]]}}}
			if(data && data.regeocode.formatted_address){
				content+="地理位置:"+data.regeocode.formatted_address;
				content+="<input type='hidden' value='"+data.regeocode.formatted_address+"' id='address' name='address'/>";
				content+="<br><button onclick=\"return saveNewPoint('"+x+"','"+y+"','frm','');\">确认坐标点</button>";
			}else
			{
				alert("获取地理位置失败,请稍后重试");
			}
		},error: function(XMLHttpRequest, textStatus, errorThrown) {
		    console.log(XMLHttpRequest.status);    
		    console.log(XMLHttpRequest.readyState);    
		    console.log(textStatus);
		}
	});
		return content+"</form>";
}
var geocoder;
$(document).ready(function() {
	var height = $(window).height()-$("#listMapLi").offset().top;
    $("#listMapLi").height(height+"px");
	setTimeout(function() {
		gmap.hideMapTool();
		gmap.startDrawPoint(true,"dingwei",
				function(x,y){
					var grahics = gmap.getEditGraphicsLayerGraphics();
					if(grahics){
						for(var g1=0;g1<grahics.length;g1++){
							if(grahics[g1].geometry.x!=x&&grahics[g1].geometry.y!=y){
								gmap.clearEditGraphicsLayerByGraphics(grahics[g1]);
							}
						}
					}
// 					setMouse(false,'0');
// 					gmap.endDrawAction(false,"dingwei");
					openWindow(x,y);
					$(".esriPopupWrapper").attr("onmouseover","setMouse(false,'0')");
					$(".esriPopupWrapper").attr("onmouseout","setMouse(true,1)");
				});
		}, 1500);
	setMouse(true,1);
// 	 AMap.service('AMap.Geocoder',function(){//回调函数
//         //实例化Geocoder
//         geocoder = new AMap.Geocoder({
//         });
//         //TODO: 使用geocoder 对象完成相关功能
//     }) 
// 	findAddress("106.482025","29.484896",v);
});
function openWindow(x,y){
	xy = {x:x,y:y};
	if(y>30)
		xy = gmap.webMercatorToGeographic(x,y);
	gmap.openInfoWindowByLocation(x,y,"添加地理位置",getTable(xy.x,xy.y));
}
function findAddress(x,y,v){
	 //逆地理编码
    var lnglatXY=[x, y];//地图上所标点的坐标
    geocoder.getAddress(lnglatXY, function(status, result) {
        if (status === 'complete' && result.info === 'OK') {
           //获得了有效的地址信息:
           //即，result.regeocode.formattedAddress
           v = result.regeocode.formattedAddress;
        }else{
           //获取地址失败
            alert("获取失败");
        }
    });
}
</script>
</html>
