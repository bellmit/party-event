<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.sunsharing.party.ConfigParam"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"
	pageEncoding="UTF-8">
<title>Insert title here</title>
<script src="<%=ConfigParam.tianHaiApi%>"></script>
 <link href="<%=ConfigParam.tianHaiCss%>" rel="stylesheet"/>
</head>
<body>
<div id="myMap">地图加载中...</div>
<script type="text/javascript">
var lng="${lng}";
var lat="${lat}";
if(lng==null||lng==""){
	lng=0;
}
if(lat==null||lat==""){
	lat=0;
}
var config = {
		"path":"<%=request.getContextPath()%>",
        key:'<%=ConfigParam.tianHaiKey%>',
        localtionLabel: {
            visible: true, style: { bottom: "5px", left: "180px" }
        },
        toolBar: {
            visible: false,
            tools: ['layerCtrl', 'layerEdit', 'select', 'measure', 'saveConfig', ['zoomFull', 'zoomIn', 'zoomOut', 'pan']]
        },
        searchView: {
            visible: true, style: { top: "15px", left: "15px" }
        },
        overView: {
            visible: true, width: 150, height: 100, attachTo: 'bottom-right'
        },
        scaleBar: {
            visible: true, attachTo: 'bottom-left', scalebarUnit: 'dual'
        },
        toggleView: {
            visible: false, style: { top: "60px", rigth: "15px" }
        },
        compass: {
            visible: true, style: { bottom: "60px", left: "15px" }
        }
    };
	SkySeaMap.ready(function () {
		var map = new SkySeaMap.Map('myMap');
		map.initMap('<%=ConfigParam.tianHaiKey%>'
				,function (result) {
					map.getLayerInfos(function (result) {
	                    console.info('layer',result);
	                    for(var layer in result){
	                        map.layerFilters({
	                            layerId: result[layer].layerId,
	                            filters: "1 != 1"
	                        }, function (result) {
	                            console.info("初始化过滤所有要素",result)
	                        });
	                    }
	                });
			
			 var grgp = new SkySeaMap.Graphic();
			 grgp.setGeometry({
                 x: parseInt(lng), y: parseInt(lat), type: "point"
             });//设置几何
			 grgp.setSymbol({
             	"type": "esriPMS",
                "url": config.path+"/images/icon/mark.png",
                "contentType": "image/png",
                "width": 19.5,
                "height": 19.5,
                "angle": 0,
                "xoffset": 0,
                "yoffset": 0
            });//设置样式
			 grgp.setAttributes(null);//设置属性值
			 grgp.setInfoTemplate({"title": "","content": ""});//设置自定义信息框
			 var tempLayer = new SkySeaMap.GraphicsLayer({ layerId: 'xstc' });
			 map.addLayer(tempLayer);
			 tempLayer.add(grgp); 
			 
// 		     map.tempLayer.point.add(grgp);
		        
		        
			 //定位
	        var params = {
	            center: {x: parseInt(lng), y: parseInt(lat)},
	            zoom:16,
	            isHighLight: true,
	            symbol: null
	        };
	        map.location(params, function (result) {
	        }); 
	        
	       
		});
	});
	
</script>
</body>
</html>