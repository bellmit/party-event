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
<%@page import="com.sunsharing.party.ConfigParam"%>
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
<link rel="stylesheet" type="text/css"
	href="<c:url value="/lib/select2-4.0.2/dist/css/select2.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/sslib/qlTree/qlTree.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/base.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/nineModules.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/listpage.css"/>" />
<style>

	body #s2id_type .select2-choice{
		height: 30px;
		line-height: 30px;
		width:318px;
	}


</style>
    <script type="text/javascript" src="<c:url value="/lib/jquery-1.9.1/jquery-1.9.1.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/lib/select2-4.0.2/dist/js/select2.full.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/artTemplate/template-simple.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/gmap/ulynlist/ulynlist.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/gmap/ulynlist/ulynlist.table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/gmap/ulynlist/ulynlist.pagebar.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/qlTree/qlTree.js"/>"></script>
    <script src="<c:url value="/lib/layer/layer.min.js"/>"></script>
	<jsp:include page="../include/headconfig.jsp"></jsp:include>
	<script type="text/javascript">
	var contextPath = "<%=(request.getContextPath())%>";
	
	config.exportUrl = '<c:url value="/emergency/export.do"/>';

	var ulynlistPath = {
		basePath : '<c:url value="/js/gmap/ulynlist"/>',
		url : '<c:url value="/js/gmap/ulynlist"/>',
	    typelist:[]
	}
	var pointArray=new Array();
	var gmap;
	var list_data_all=new Array();
	<c:forEach items="${typelist }" var="type_item">
	      ulynlistPath.typelist.push({id:"${type_item.TYPE_ID}",name:"${type_item.TYPE_NAME}"});
	</c:forEach>
	</script>

    
	
	
<script src="<c:url value="/lib/My97DatePicker/WdatePicker.js"/>" charset="gb2312"></script>
	
	<script type="text/javascript">
	var imgs = 	[
		      		{
		      			img:'mark',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/mark.png'/>",
		      				"width":20,
		      				"height":34,
		      				"xOffset":0,
		      				"yOffset":17
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
		      		},
		      		{
		      			img:'zhu',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ico_zhu.png'/>",
		      				"width":34,
		      				"height":34,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'red',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/red.png'/>",
		      				"width":12,
		      				"height":16,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'blue',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/blue.png'/>",
		      				"width":12,
		      				"height":16,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'gray',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/gray.png'/>",
		      				"width":12,
		      				"height":16,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p1',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p1.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p2',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p2.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p3',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p3.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p4',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p4.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p5',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p5.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p6',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p6.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p7',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p7.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p8',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p8.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p9',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p9.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p10',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p10.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p1g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p1g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p2g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p2g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p3g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p3g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p4g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p4g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p5g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p5g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p6g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p6g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p7g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p7g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p8g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p8g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p9g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p9g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'p10g',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/p10g.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph1',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph1.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph2',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph2.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph3',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph3.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph4',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph4.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph5',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph5.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph6',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph6.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph7',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph7.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph8',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph8.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph9',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph9.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
		      		{
		      			img:'ph10',
		      			style:{
		      				"path":"<c:url value='/js/gmap/images/ph10.png'/>",
		      				"width":21,
		      				"height":28,
		      				"xOffset":0,
		      				"yOffset":17
		      			}
		      		},
	                {
	                    img:"zb",
	                    style:{
	                        "path":"<c:url value='/js/gmap/images/ico_zb.png'/>",
	                        "width":9,
	                        "height":9,
	                        "xOffset":0,
	                        "yOffset":4
	                    }
	                }
		      	];
	function initMap(){
		gmap = new sunsharing.gmap({proxy:config.proxyUrl,
			isDefaultCenter:config.isDefaultCenter,
			defaultCenter:{x:config.x,y:config.y},
			defaultLevel:13,
			jmAreaLayerUrl:config.jmAreaLayerUrl,//现场精图卫星图地址
			jtNormalLayerUrl:config.jtAreaLayerUrl,//现场精图矢量图地址
			queryCommunityAreaURL:config.url+"/ArcGIS/rest/services/xzqh/FeatureServer/4",
			isUseJmLocalLayer:config.isUseJmLocalLayer,
			isUseJtLocalLayer:config.isUseJtLocalLayer,
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
    
    <script type="text/javascript">
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
<%-- <jsp:include page="../include/listMenu.jsp"></jsp:include> --%>
<body onload="initMap();">
<div class="content-box">
	    <!-- <div class="cb-sidebar">
        <div id="qlTree"></div>
    	</div> -->
		<div class="cb-content" style="margin-left: 0px">
			<div class="content" style="margin-left: 0px">
				<div class="mainContent" style="top: 0px">
					<div class="mcBox">
						<jsp:include page="../include/emergencySearch1.jsp"></jsp:include>
                    <div class="dwBottom" id="dwBottom">
                        <div class="TableToolDiv clearfix">
                        	<div class="btnDiv" id = "jgxz">
                            	该条件下应急事件有坐标的数据为<span id="allcount" class="jgxz"></span>条
                            </div>
                           <div class="btnFunction">
                            	<button class="btnMap btnMapActive" type="btnMap"></button>
                            	<a href='javascript:void(0)' onclick='golist()'>
                                	<button class="btnTable" type="btnTable"></button>
                                </a>
                            </div>
                        </div>
		                <div id="listMapLi">
			                <div style="width: 25%;height: 100%;float: left;" id="maplistdiv">
							  	
							  	<div class="mapMs clearfix">
		                            <div id="mapList" class="mapList" style="overflow-y: scroll;"></div>
		                            <div id="mapPager" class="mapHere clearfix" style="margin-right: 20px"></div>
		                        </div>
							  	
							</div>
			                <div id="map" style="position:relative; width: 75%;height: 100%;"></div>
		                </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>                              
<script src="<c:url value="/js/page_js/initMapEmergency.js"/>"></script>
<script>
$(document).ready(function() {
	$(".select2").select2();
});
function golist(){
// 	var data='';
// 	data += "status="+$("#status").val();
// 	data += "&street="+$("#street").val();
// 	data += "&type="+$("#type").val();
// 	data += "&startTime="+$("#startTime").val();
// 	data += "&endTime="+$("#endTime").val();
// 	data += "&userName="+$("#userName").val();
// 	window.location.href = '<c:url value="/fire/list.do?'+data+'"/>';
$("#search-form").attr("action",'<c:url value="/emergency/list.do"/>');
$("#search-form").submit();
}
function changevalue(obj,id){
	$(obj).parent().find('.all-style').removeClass('all-style');
	$(obj).addClass("all-style");
	$("#"+id).val($(obj).attr("id"));
}
$(document).ready(function() {
	setTimeout(function() {gmap.hideMapTool();}, 1500);
});

function chkSpan(x,id){
	$("span[latv^='"+id+"']").each(function(i){
		$(this).removeClass('hasSelect');
		if(x==i){
			$(this).addClass("hasSelect");
			$("#"+id).val($(this).attr("value"));
			$("#search-btn").click();
		}
	});
}


</script>
</html>
