/**
 * User: liyh
 * Date: 14-6-3
 * Version: 1.4
 */
var sureMove = function(e){
		if($("#sliderTop").hasClass("sliderTop")){
			moverealize(e);
		}
}
function moverealize(e){
	var height =  $("#sliderHeight").offset().top-e.pageY + $("#sliderHeight").height();
	$("#sliderHeight").height(height);
	var top = 165-height;
	$("#sliderTop").css("top",top+"px");
	if(height>161){
		$("#sliderHeight").height(161);
	}
	
	if(height<1){
		$("#sliderHeight").height(1);
	}
	if(top>164){
		$("#sliderTop").css("top","164px");
	}
	if(top<4){
		$("#sliderTop").css("top","4px");
	}
}
function movejibie(str){
	if(str == "st"){
		$("#sliderHeight").height(141);
		$("#sliderTop").css("top","14px");
	}else if(str == "city"){
		$("#sliderHeight").height(91);
		$("#sliderTop").css("top","74px");
	}else if(str == "prov"){
		$("#sliderHeight").height(51);
		$("#sliderTop").css("top","114px");
	}else if(str == "country"){
		$("#sliderHeight").height(11);
		$("#sliderTop").css("top","154px");
	}else if(str == "sq"){
		$("#sliderHeight").height(161);
		$("#sliderTop").css("top","4px");
	}else if(str == "qu"){
		$("#sliderHeight").height(111);
		$("#sliderTop").css("top","54px");
	}
}

function quzheng(map){
	
	if($("#sliderHeight").height().length==1){
		$("#sliderHeight").height(1);
		map.setLevel(1);
	}else{
		$("#sliderHeight").height(parseInt($("#sliderHeight").height()/10)*10+1);
		map.setLevel(parseInt($("#sliderHeight").height()/10));
	}
	var top=165-$("#sliderHeight").height();
	$("#sliderTop").css("top",top+"px");
//	var base_x=$("#draggable").css("left");
//	var num_x=base_x.substr(0,base_x.length-2);
//	if(num_x>=4)
//	{
//		num_x--;
//		num_x--;
//	}
//	else
//	{
//		num_x=2;
//	}
//	data = parseFloat(num_x*n).toFixed(2);
	data = $("input[class='BMap_data tcenter fnt12']").val().replace("公里","");
//	$("#draggable").css({left:num_x});
//	$('.BMap_stdMpSliderBgLeft').width(num_x);
//	$('.BMap_data').val(data+'公里');
//	$('.Bmap_line_data span').text(data+'公里');
//	$('.Bmap_circle').show().css({
//		width:data*cell,
//		height:data*cell,
//		'margin-top':-(data*(cell/2)),
//		'margin-left':-(data*(cell/2))
//	});
//	gmap.clearQueryCircle();
//	gmap.clearPolygons();
//	if(serach_circle)
//	drawCircle(serach_circle.geometry.center.x,serach_circle.geometry.center.y,data*1000,"","");
}


var sunsharing = new Object();
sunsharing.gmap = function(options){
//--------------------依赖包Start--------------------//
	var gmap = this;
	require(["esri/map"],function(Value){Map = Value;});
	require(["esri/graphic"],function(Value){Graphic = Value;});
	require(["esri/InfoTemplate"],function(Value){InfoTemplate = Value;});
	require(["esri/SpatialReference"],function(Value){SpatialReference = Value;});
	require(["esri/toolbars/draw"],function(Value){
		Draw = Value;
	});
	require(["esri/toolbars/edit"], function(Value){Edit = Value;});
	require(["esri/layers/GraphicsLayer"],function(Value){GraphicsLayer = Value;});
	require(["esri/layers/FeatureLayer"], function(Value) {FeatureLayer = Value;});
	require(["esri/geometry/Point"],function(Value){Point = Value;});
	require(["esri/geometry/Polyline"],function(Value){Polyline = Value;});
	require(["esri/geometry/Polygon"],function(Value){Polygon = Value;});
	require(["esri/geometry/Circle"],function(Value){Circle = Value;});
	require(["esri/geometry/webMercatorUtils"], function(Value) {WebMercatorUtils = Value;});
	require(["esri/symbols/PictureMarkerSymbol"],function(Value){PictureMarkerSymbol = Value;});
	require(["esri/symbols/SimpleLineSymbol"],function(Value){SimpleLineSymbol = Value;});
	require(["esri/symbols/SimpleFillSymbol"],function(Value){SimpleFillSymbol = Value;});
	require(["esri/symbols/SimpleMarkerSymbol"],function(Value){SimpleMarkerSymbol = Value;});
	require(["esri/symbols/Font"],function(Value){Font = Value;});
	require(["esri/symbols/TextSymbol"],function(Value){TextSymbol = Value;});
	require(["esri/units"],function(Value){Units = Value;});
	require(["esri/geometry/geodesicUtils"],function(Value){GeodesicUtils = Value;});
	require(["dojo/colors"],function(Value){Color = Value;});
	require(["esri/tasks/GeometryService"],function(Value){GeometryService = Value;});
	require(["esri/layers/ArcGISTiledMapServiceLayer"],function(Value){ArcGISTiledMapServiceLayer = Value;});
	require(["esri/graphicsUtils"], function(Value) {graphicsUtils=Value;});
//--------------------依赖包End--------------------//
	
//--------------------接口方法Start--------------------//
	
	this.tabMap = function(index){//切换底图
		if(defaults.isUseJmLocalLayer == "1")
		{
			jtNormalLayer.hide();
			jtMapLayer.hide();
			switch (index) {
			  case 0:
				  jtNormalLayer.show();
				  break;
			  case 1:
				  jtMapLayer.show();
				  break;
			  default:
				  break;	  
			}
		}
		else
		{
			googleNormalLayer.hide();
			googleMapLayer.hide();
			googleLabelLayer.hide();
			switch (index) {
			case 0:
				googleNormalLayer.show();
				break;
			case 1:
				googleMapLayer.show();
				googleLabelLayer.show();
				break;
			default:
				break;
			}
		}
	};
	
	this.locateToAreasCenter=function(graphics,level){
		var rings=new Array();
		for(var i=0;i<graphics.length;i++)
		{
			var geometry=graphics[i].geometry;
			var centerPoint=geometry.getCentroid();
			var xy=new Array();
			xy[0]=centerPoint.x;
			xy[1]=centerPoint.y;
			rings.push(xy);
		}
		if(rings.length>0)
		{
			var polygonJson = {"rings": rings,"spatialReference": map.spatialReference};
			var polygon = new Polygon(polygonJson);
			var point=polygon.getCentroid();
			gmap.centerAtPoint(point.x,point.y,level);
		}
		
		
	};
	
	calcCentroid = function(rings){
		var a,b,c,points,e=[],next_point,k;
		for(var i = 0;i < rings.length;++i){
			a = b = c = 0;
			points = rings[i];
			var points_len = points.length;
			for(var j = 0;j < points_len;++j){
				if(j < points_len - 1){
					var point = points[j];
					next_point=points[j+1];
					k=point[0]*next_point[1]-next_point[0]*point[1];
					a+=(point[0]+next_point[0])*k;
					b+=(point[1]+next_point[1])*k;
					c+=k;
				}
			}
			if(0<c){
				c*=-1;
			}
			e.push([a,b,c/2]);
		}
		e.sort(function(a,b){return a[2]-b[2]});
		d=6*e[0][2];
		return [e[0][0]/d,e[0][1]/d];
	};
	
	
	this.locateToAreaCenter=function(geometry,level){//定位到面的质心
		 var geometries = new Array();
		 geometries[0]=geometry;
		 var point=geometry.getCentroid();
		 gmap.centerAtPoint(point.x,point.y,level);
/*
		 geometryService.labelPoints(geometries,function(evt){
			    console.info(evt);
			    console.info("x:"+evt[0].x+"  y:"+evt[0].y);
			    gmap.centerAtPoint(evt[0].x,evt[0].y,level);
		 });
*/
	}
	
	this.registerImages = function(images){//注册图片
		for(var i in images){
			defaults.images[images[i].img] = images[i].style;
		}
	};
	
	
	this.centerAtPoint = function(x,y,level){//定位至坐标点
		if(!isNull(level)){
			map.setLevel(level);
		}
		if(!isNull(x) && !isNull(y) && typeof x!="undefined" && typeof y!="undefined"){
			var xy = pointGeographicToWebMercator(x,y);
			x = xy.x;
			y = xy.y;
			var point = new Point(x, y, map.spatialReference);
			map.centerAt(point);
		}
	};
	
	this.centerByExtent=function(extent){
		map.setExtent(extent,true);
	}
	
	this.centerAtPloygon = function(id){//定位到面
		for(var i in showPolygonGraphicsLayer.graphics){
			var graphic = showPolygonGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				map.setExtent(graphicsExtent([graphic]),true);
				break;
			}
		}
	};
	
	this.locateToDefualt = function(){//定位至默认位置
		if(defaults.isDefaultCenter){
			this.centerAtPoint(
					defaults.defaultCenter.x,
					defaults.defaultCenter.y,
					defaults.defaultLevel);
		}
	};
	
	
	
	this.addPoint = function(x,y,tip,img,hoverImg,clickHandler){//新增一个标注点至地图，并返回这个标注点的ID
		if(typeof x!="undefined"&&typeof y!="undefined" &&x!=""&&y!=""){
			var xy = pointGeographicToWebMercator(x,y);
			x = xy.x;
			y = xy.y;
			var point = new Point(x,y,map.spatialReference);
			var symbol;
			if(img==""){
				symbol = new SimpleMarkerSymbol().setStyle(
					    SimpleMarkerSymbol.STYLE_CIRCLE).setColor(
					    new Color([255,255,255,0.1]))
					    .setOutline(new SimpleLineSymbol(SimpleLineSymbol.STYLE_NULL,
					    	    new Color([255,0,0]),0));
			}else{
				symbol = getPictureMarkerSymbol(img);
			}
			var attributes = {id:pointID,img:img,hoverImg:hoverImg,tip:tip};
			var graphic = new Graphic(point,symbol,attributes,null);
			graphic.clickHandler = clickHandler;
			graphic.isLockImg = false;
			showPointGraphicsLayer.add(graphic);
			if(tip.isToFront!=undefined){
				if(tip.isToFront==true){
					graphic.getShape().moveToFront();
				}else{
					graphic.getShape().moveToBack();
				}
			}
			return pointID++;
		}
		else{
			return;
		}
		
	};//return id;
	
	
	this.addPolyLine = function(paths, style, clickHandler){//新增一条线至地图，并返回这条线的ID
		paths = polyGeographicToWebMercator(paths);
		var polylineJson = {"paths": paths,"spatialReference": map.spatialReference};
		var polyline = new Polyline(polylineJson);
		var symbol = getSimpleLineSymbol(style);
		var attributes = {id:polyLineID};
		var graphic = new Graphic(polyline,symbol,attributes,null);
		graphic.clickHandler = clickHandler;
		showLineGraphicsLayer.add(graphic);
		return polyLineID++;
	};//return id;
	
	this.addPolygon = function(rings,tip,style,clickHandler){//新增一个多边形至地图，并返回这个多边形的ID
		rings = polyGeographicToWebMercator(rings);
		var polygonJson = {"rings": rings,"spatialReference": map.spatialReference};
		var polygon = new Polygon(polygonJson);
		var symbol = getSimpleFillSymbol(style);
		var attributes = {id:polygonID,tip:tip};
		var graphic = new Graphic(polygon,symbol,attributes,null);
		graphic.clickHandler = clickHandler;
		showPolygonGraphicsLayer.add(graphic);
		return polygonID++;
	};//return id;
		
	this.addCircle = function(x,y,radius,tip,style,clickHandler){//新增一个圆，并返回这个圆的ID
		if(x > -180 && x < 180 && y > -90 && y < 90){
		}else{
			var xy = gmap.webMercatorToGeographic(x,y);
			x = xy.x;
			y = xy.y;
		}
		var circle = new Circle({"center": [x,y],"radius": radius,"geodesic":true,
			"numberOfPoints":200,"radiusUnit": Units.METERS});
		var symbol = getSimpleFillSymbol(style);
		var attributes = {id:polygonID,tip:tip};
		var graphic = new Graphic(circle,symbol,attributes,null);
		graphic.clickHandler = clickHandler;
		showPolygonGraphicsLayer.add(graphic);
		return polygonID++;
	};//return id;
	
	this.addCircleForQuery=function(x,y,radius,tip,style){
		if(x > -180 && x < 180 && y > -90 && y < 90){
		}else{
			var xy = gmap.webMercatorToGeographic(x,y);
			x = xy.x;
			y = xy.y;
		}
		var circle = new Circle({"center": [x,y],"radius": radius,"geodesic":true,
			"numberOfPoints":200,"radiusUnit": Units.METERS});
		var symbol = getSimpleFillSymbol(style);
		var attributes = {id:polygonID,tip:tip};
		var graphic = new Graphic(circle,symbol,attributes,null);
		showPolygonGraphicsLayer.add(graphic);
		editGraphicsLayer.add(graphic);
		polygonID++;
		return graphic;
	}
	
	this.clearPoints = function(){//清除所有的标注点
		showPointGraphicsLayer.clear();
	};
	
	this.clearPolyLines = function(){//清除所有的线
		showLineGraphicsLayer.clear();
	};
	
	this.clearPolygons = function(){//清除所有多边形
		showPolygonGraphicsLayer.clear();
	};
	this.clearLabels=function(){//清除文字显示
		labelGraphicsLayer.clear();
	}
	
	this.removePoint = function(id){//移除某个ID的标注点
		for(var i in showPointGraphicsLayer.graphics){
			var graphic = showPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				showPointGraphicsLayer.remove(graphic);
				break;
			}
		}
	};
	this.getPoint = function(id){//得到某个ID的标注点
		for(var i in showPointGraphicsLayer.graphics){
			var graphic = showPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
//				showPointGraphicsLayer.remove(graphic);
//				break;
				return graphic;
			}
		}
	};
	this.setPointToTop = function(id){
		var graphic = this.getPoint(id);
		graphic.getShape().moveToFront();
		
	}
	this.getEditGraphicsLayerGraphics=function(){//返回画图工具在图形编辑层上画的图形
		var graphics=editGraphicsLayer.graphics;
		return graphics;
	}
	
	this.clearEditGraphicsLayer=function(){//清除画图工具在图形编辑层上画的图形
		editGraphicsLayer.clear();
	}
	
	this.clearEditGraphicsLayerByGraphics=function(graphics){//清除画图工具在图形编辑层上指定画的图形
		editGraphicsLayer.remove(graphics);
	}
	
	this.getShowLineGraphicsLayerGraphics=function(){//返回线显示层上的图形
		var graphics=showLineGraphicsLayer.graphics;
		return graphics;
	}
	
	this.clearShowLineGraphicsLayerGraphics=function(){//清除线显示层上的图形
		showLineGraphicsLayer.clear();
	}
	
	this.clearQueryCircle=function(){//清除编辑层上的查询圆
		var graphics=editGraphicsLayer.graphics;
		for(var i=0;i<graphics.length;i++){
			if(graphics[i].geometry.type=="polygon"){
				editGraphicsLayer.remove(graphics[i]);
			}
		}
	}
	
	this.setExtentByshowPointGraphicsLayer=function(){//根据现有点图层上的点，设置地图视野范围
		var graphics=showPointGraphicsLayer.graphics;
		var extent=graphicsUtils.graphicsExtent(graphics);
		if(extent!=null){
			gmap.centerByExtent(extent);
		}
		else{
			gmap.centerAtPoint(graphics[0].geometry.x,graphics[0].geometry.y,18);
		}
	}
	
	this.clearQueryCircle=function(){//清除编辑层上的查询圆
		var graphics=editGraphicsLayer.graphics;
		for(var i=0;i<graphics.length;i++){
			if(graphics[i].geometry.type=="polygon"){
				editGraphicsLayer.remove(graphics[i]);
			}
		}
	}
	
	this.removePolyLine = function(id){//移除某个ID的线
		for(var i in showLineGraphicsLayer.graphics){
			var graphic = showLineGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				showLineGraphicsLayer.remove(graphic);
				break;
			}
		}
	};
	
	this.removePolygon = function(id){//移除某个ID的多边形
		for(var i in showPolygonGraphicsLayer.graphics){
			var graphic = showPolygonGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				showPolygonGraphicsLayer.remove(graphic);
				break;
			}
		}
	};
	
	this.changePointInfo = function(id,tip,img,hoverImg){//修改某个标注点的信息
		for(var i in showPointGraphicsLayer.graphics){
			var graphic = showPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				graphic.attributes.img = img;
				graphic.attributes.hoverImg = hoverImg;
				graphic.attributes.tip = tip;
				var symbol = getPictureMarkerSymbol(img);
				graphic.setSymbol(symbol);
				break;
			}
		}
	};
	
	this.addAreaCenterPoint=function(geometries,labels){//增加面的文字提示
		geometryService.labelPoints(geometries,labelLocateHandler(labels));
	}
	
	this.addAreaCenterPointLocal=function(polygon,labels)//增加面的文字提示（本地查询）
	{
		var point=polygon.getCentroid();
		var textSymbol = getTextSymbol(labels);
		var labelPointGraphic = new Graphic(point, textSymbol);
		labelGraphicsLayer.add(labelPointGraphic);
		
	}
	this.hideMapTool=function(){
		map.hideZoomSlider();//隐藏地图缩放滚动条
		map.disableDoubleClickZoom();//不允许双击地图来放大地图一个级别并重定位地图中心点
		map.disableKeyboardNavigation();//屏蔽小键盘导航事件
	}
	
	function labelLocateHandler(labels) {//文字定位回调
		return function(labelPoints) {
			for (var i = 0; i < labelPoints.length; i++) {
				var labelPoint = labelPoints[i];
				var textSymbol = getTextSymbol(labels[i]);
				var labelPointGraphic = new Graphic(labelPoint, textSymbol);
				labelGraphicsLayer.add(labelPointGraphic);
			}
		};
	}
	
	this.changePointToImg = function(id){//改变点的图标为Img
		for(var i in showPointGraphicsLayer.graphics){
			var graphic = showPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				var symbol = getPictureMarkerSymbol(graphic.attributes.img);
				graphic.setSymbol(symbol);
				graphic.isLockImg = false;
				break;
			}
		}
	};
	
	this.changePointToHoverImg = function(id){//改变点的图标为hoverImg
		for(var i in showPointGraphicsLayer.graphics){
			var graphic = showPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				var symbol = getPictureMarkerSymbol(graphic.attributes.hoverImg);
				graphic.setSymbol(symbol);
				graphic.isLockImg = true;
				break;
			}
		}
	};
	
	this.startDrawPoint = function(isTemp,img,drawCompleteHandler){//开始标注点
		drawTool.isTemp = isTemp;
		drawTool.drawCompleteHandler = drawCompleteHandler;
		defaults.drawPointImg = img;
		drawTool.activate(Draw.POINT);
	};
	
	this.startDrawPolygon = function(isTemp,style,drawCompleteHandler){//开始绘制多边形
		drawTool.isTemp = isTemp;
		drawTool.drawCompleteHandler = drawCompleteHandler;
		if(style!=null){
			defaults.drawPolygonStyle = style;
		}
		drawTool.activate(Draw.POLYGON);
	//	gmap.initFeatureLayer('polygon');
	};
	
	this.startDrawPolyline=function(isTemp,style,drawCompleteHandler){//开始绘制线条,isTemp==true支持多线段
		drawTool.isTemp = isTemp;
		drawTool.drawCompleteHandler = drawCompleteHandler;
		if(style!=null){
			defaults.drawPolylineStyle=style;
		}
		drawTool.activate(Draw.POLYLINE);
	//	gmap.initFeatureLayer('polyline');
	}
	
	this.endDrawAction = function(){//关闭画图工具
		drawTool.isTemp = false;
		drawTool.drawCompleteHandler = null;
		drawTool.deactivate();
	};
	
	this.checkContains = function(polygonId,x,y){//判断点是否在某个面内
		var xy = pointGeographicToWebMercator(x,y);
		x = xy.x;
		y = xy.y;
		var point = new Point(x,y,map.spatialReference);
		var ploygon;
		for(var i in showPolygonGraphicsLayer.graphics){
			var graphic = showPolygonGraphicsLayer.graphics[i];
			if(graphic.attributes.id == polygonId){
				ploygon = graphic.geometry;
				break;
			}
		}
		if(null!=ploygon){
			return ploygon.contains(point);
		}
		return false;
	};//return Boolean
	
	this.geodesicLength = function(paths){//计算线的
		paths = polyWebMercatorToGeographic(paths);
		var polylineJson = {"paths": paths,"spatialReference": map.spatialReference};
		var polyline = new Polyline(polylineJson);
		var lengths = GeodesicUtils.geodesicLengths([polyline], Units.METERS);
		if(lengths!=null && lengths.length>0){
			return lengths[0];
		}else{
			return 0;
		}
	};//return Number
	
	this.tdtWebMercatorToGeographic = function(x,y){//判断是否为天地图底图，墨卡托坐标转经纬度
		var object = {x:x,y:y};
		if(defaults.isUseTDT && (x < -180 || x > 180 || y < -90 || y > 90))
		{
			var point = new Point( {"x": x, "y": y, "spatialReference": {"wkid": 102113 } });
			var geometry = WebMercatorUtils.webMercatorToGeographic(point);
			object.x=geometry.x;
			object.y=geometry.y;
		}
		return object;
	};
	
	this.webMercatorToGeographic = function(x,y){//墨卡托坐标转经纬度
		var point = new Point( {"x": x, "y": y, "spatialReference": {"wkid": 102113 } });
		var geometry = WebMercatorUtils.webMercatorToGeographic(point);
		var object = {x:geometry.x,y:geometry.y};
		return object;
	};//return {x,y}
	
	this.geographicToWebMercator = function(x,y){//经纬度转墨卡托坐标
		var point = new Point( {"x": x, "y": y, "spatialReference": {"wkid": 4326 } });
		var geometry = WebMercatorUtils.geographicToWebMercator(point);
		var object = {x:geometry.x,y:geometry.y};
		return object;
	};//return {x,y}
	
	this.initFeatureLayer=function(type){//初始化FeatureLayer
		if(type=="polygon"){
			featureLayer=new FeatureLayer(defaults.polygonEditLayerUrl,{objectIdField:"OBJECTID"});
		}else if(type=="polyline"){
			featureLayer=new FeatureLayer(defaults.polylineEditLayerUrl,{objectIdField:"OBJECTID"});
		}else if(type=="polypoint"){
			featureLayer=new FeatureLayer(defaults.polypointEditLayerUrl,{objectIdField:"OBJECTID"});
		}
//		console.info(featureLayer);
	}
	
	this.getMapLevel=function(){
		return map.getLevel();
	}
	this.saveOrEditGraphicToLayer=function(attributes,editSuccessCallBack,editErrorCallBack,operate){//将划片图形保存到面图层(暂只支持多边形和线)
		var graphics=gmap.getEditGraphicsLayerGraphics();
		var saveGraphics=new Array();

		if(graphics.length>0&&operate=="add"){
			graphics[0].setAttributes(attributes);
		}  
		saveGraphics.push(graphics[0]);
		if(graphics.length>1){
			if(graphics[0].geometry.type=="polygon"){
				for(var i=1;i<graphics.length;i++){
					saveGraphics[0].geometry.rings.push(graphics[i].geometry.rings[0]);
				}
			}else if(graphics[0].geometry.type=="polyline"){
				for(var i=1;i<graphics.length;i++){
					saveGraphics[0].geometry.paths.push(graphics[i].geometry.paths[0]);
				}
			}else if(graphics[0].geometry.type=="polypoint"){
				for(var i=1;i<graphics.length;i++){
					saveGraphics[0].geometry.paths.push(graphics[i].geometry.paths[0]);
				}
			}
		}
		if(operate=="add"){
			featureLayer.applyEdits(saveGraphics,null,null,editSuccessCallBack,editErrorCallBack);
		}else if(operate=="edit"){
			featureLayer.applyEdits(null,saveGraphics,null,editSuccessCallBack,editErrorCallBack);
		}else if(operate=="delete"){
			featureLayer.applyEdits(null,null,saveGraphics,editSuccessCallBack,editErrorCallBack);
		}
		
	}
	
	
	
	this.initEditTool=function(){//初始化编辑工具
		editTool=new Edit(map);
	}
	
	this.activedEditTool=function(graphic){//激活编辑工具
		editTool.activate(Edit.EDIT_VERTICES | Edit.Move | Edit.SCALE,graphic);
	}
	
	this.deactivatedEditTool=function(){//关闭编辑工具
		editTool.deactivate();
	}
	
	this.openInfoWindowByLocation=function(x,y,title,content){//在地图上显示infoWindow
		if(typeof x!="undefined"&&typeof y!="undefined" &&x!=""&&y!=""){
			var xy = pointGeographicToWebMercator(x,y);
			x = xy.x;
			y = xy.y;
			var point = new Point(x,y,map.spatialReference);
			var screenPoint=map.toScreen(point);
			map.infoWindow.setContent(content);
			map.infoWindow.setTitle(title);
			map.infoWindow.show(screenPoint,map.getInfoWindowAnchor(screenPoint));
		}else{
			return;
		}
	}
	
	this.closeInfoWindow=function(){//关闭infoWindow
		map.infoWindow.hide();
	}
//--------------------接口方法End--------------------//
	
//--------------------初始化Start--------------------//
	var geometryService;//几何服务          
	var map;//地图对象
	var drawTool;//画图工具
	var editTool;//编辑工具
	
	var googleNormalLayer;
	var googleMapLayer;
	var googleLabelLayer;
	var jtNormalLayer;//精图矢量图图层
	var jtMapLayer;//精图卫星图图层
	var roadNetworkLayer;//路网图层
	var featureLayerUrl;//面编辑层url
	var polygonEditLayerUrl;//多边形编辑图层url（划片功能）
	var polylineEditLayerUrl;//线编辑图层url（画线功能）
	var polypointEditLayerUrl;//点编辑图层url（画点功能）
	
	var showPointGraphicsLayer;//标注点显示层
	var showLineGraphicsLayer;//线显示层
	var showPolygonGraphicsLayer;//多边形显示层
	var editGraphicsLayer;//编辑层
	var labelGraphicsLayer;// 文字显示层
	var featureLayer;//面编辑层
	var heatRenderFeatureLayer;//热力图显示层
	var heatRenderLayer;
	var graphicArray=new Array();//存放每次查询时获取的Graphic对象，用于在下拉单选中对应Graphic
	
	this.initHeatRenderFeatureLayer=function(renenderOption){//热力图层渲染器
		var heatRenderFeatureLayerOption={
			mode: FeatureLayer.MODE_SNAPSHOT,
			outFields: ["*"]//,
//			infoTemplate: new InfoTemplate("${OBJECTID}", "<div style='font: 18px Segoe UI'>2009年${OBJECTID}的GDP产值为<b>亿元 </b>.</div>")
		}
		heatRenderFeatureLayer=new FeatureLayer(defaults.pointQueryUrl,heatRenderFeatureLayerOption);	
		var heatmapRenderer = new HeatmapRenderer(renenderOption);
		heatRenderFeatureLayer.setRenderer(heatmapRenderer);
		map.addLayer(heatRenderFeatureLayer);
	}
	
	this.createHeatRenderLayer=function(){//初始化热力图
		heatLayer = new HeatmapLayer({
            config: {
                "useLocalMaximum": true,
                "radius": 15,
                "gradient": {
                	0.2: "rgb(000,000,255)",
                	0.4: "rgb(000,255,255)",
                	0.6: "rgb(000,255,000)",
                    0.8: "rgb(255,255,000)",
                    1.00: "rgb(255,000,000)"
                }
            },
            "map": map,
            "domNodeId": "heatLayer",
            "opacity": 0.85/*,
            "globalMax":2000*/
        });
		map.addLayer(heatLayer);
	}
	this.addDateToHeatRender=function(data){//为热力图添加数据
		if(heatLayer && typeof heatLayer.setData == 'function')
			heatLayer.setData(data);
	}
	this.clearHeatMapData=function(data){
//		heatLayer.setNewData();
		if(heatLayer && typeof heatLayer.setData == 'function')
			heatLayer.setData(data);
	}
	this.addDateToHeatRenderAndPoint=function(data){//添加标注点并且显示热力图数据
/*		var len = data.length;
		var heatData=[],heatData1=[],heatData2=[],heatData3=[];
		for(var i=0;i<len;i++){
			var dataPoint = data[i];
			var xyJW,xyMKT;
			if(dataPoint.type=="1"){//墨卡托
//				xyJW = gmap.webMercatorToGeographic(dataPoint.x,dataPoint.y);
				xyMKT= {x:dataPoint.x,y:dataPoint.y};
			}
			else{//经纬度
				xyJW = {x:dataPoint.x,y:dataPoint.y};
				xyMKT= gmap.geographicToWebMercator(dataPoint.x,dataPoint.y);
			}
			var hD = {
					attributes: {
						count:dataPoint.count,
						title:dataPoint.tip.title,
						radius:dataPoint.tip.radius
						},
					geometry: {
						spatialReference: {
							wkid: 102113
							},
							type: "point",
							x: xyMKT.x,
							y: xyMKT.y
						}
					};
//			if(i<2000)
				heatData.push(hD);
//			else if(i<4000)
//				heatData1.push(hD);
//			else if(i<7000)
//				heatData2.push(hD);
//			else 
//				heatData3.push(hD);
//			gmap.addPoint(xyJW.x,xyJW.y,dataPoint.tip,"","heat",dataPoint.reclickHandle);
		}
		gmap.createHeatRenderLayer();
		gmap.addDateToHeatRender(heatData);*/
		gmap.createHeatRenderLayer();
		gmap.addDateToHeatRender(data);
//		if(heatData1.length>0){
//			setTimeout(function() {
//				heatLayer.addDataPoint(heatData1);
//			}, 1500);
//		}
//		if(heatData2.length>0){
//			setTimeout(function() {
//				heatLayer.addDataPoint(heatData2);
//			}, 1500);
//		}
//		if(heatData3.length>0){
//			setTimeout(function() {
//				heatLayer.addDataPoint(heatData3);
//			}, 1500);
//		}
	}
	
	this.labelBtnChange=function(){//切换文字显示层显示按钮触发事件
		if($('#labelBtn').hasClass('hideLable')){
			labelGraphicsLayer.show();
			$('#labelBtn').removeClass('hideLable');
		}
		else{
			labelGraphicsLayer.hide();
			$('#labelBtn').addClass('hideLable');
		}
		
	}
	
	this.hidLabelGraphicsLayer=function(){
		labelGraphicsLayer.hide();
	}
	this.showLabelGraphicsLayer=function(){
		labelGraphicsLayer.show();
	}
	
	this.getShowPolygonGraphicsLayer = function(){
		return showPolygonGraphicsLayer;
	};
	
	this.getShowPolylineGraphicsLayer=function(){
		return showLineGraphicsLayer;
	}
	
	this.getEditGraphicsLayer=function(){
		return editGraphicsLayer;
	}
	
	this.getGraphicArray=function(){
		return graphicArray;
	};
	this.setGraphicArray=function(array){
		graphicArray=array;
	}
	this.clearGraphicArray=function(){
		graphicArray=[];
	}
	
	var pointID = 0;//标注点ID
	var polyLineID = 0;//线ID
	var polygonID = 0;//多边形ID
	
	this.setPointID=function(){
		pointID=0;
	}
	
	this.getPointID=function(){
		return pointID++;
	}
	this.getPolygonID = function(){
		return polygonID++;
	};
	this.getPolylineID=function(){
		return polyLineID++;
	}
	this.getDefaults = function(){
		return defaults;
	};
	
	var defaults = {//默认参数
			div:'map',//地图所在的div的ID
			isDefaultCenter:true,//是否定位到默认位置
			defaultCenter:{x:config.x,y:config.y},//默认中心
			defaultLevel:'13',//默认地图等级
			wkid:'102113',//坐标系类型
			isUseTDT : false,//不使用天地图图层
			queryCityAreaURL:config.url+"/ArcGIS/rest/services/xzqh/FeatureServer/1",//市级地图服务查询地址
			queryDistrictAreaURL:config.url+"/ArcGIS/rest/services/xzqh/FeatureServer/2",//区级地图服务查询地址
			queryStreetAreaURL:config.url+"/ArcGIS/rest/services/xzqh/FeatureServer/3",//街道级地图服务查询地址
			queryCommunityAreaURL:config.url+"/ArcGIS/rest/services/xzqh/FeatureServer/4",//社区级地图服务查询地址
			geometryServiceURL:config.url+'/ArcGIS/rest/services/Geometry/GeometryServer',//几何服务URL
			queryMaProURL:config.url+"/ArcGIS/rest/services/CQ-MIAN/FeatureServer/0",//重点工程服务查询地址
			normalLayerUrl:config.saturl,//矢量图地址
			mapLayerUrl:config.tileurl,//卫星图地址
			jmAreaLayerUrl:config.jmAreaLayerUrl,//精图卫星图地址
			jtNormalLayerUrl:config.jtAreaLayerUrl,//精图矢量图地址
			roadNetworkLayerUrl:config.roadurl,//路网图地址
			featureLayerUrl:config.url+'/ArcGIS/rest/services/XA_AREA/FeatureServer/0/',//面编辑层url
			polygonEditLayerUrl:config.url+'/ArcGIS/rest/services/XA_AREA/FeatureServer/0/',//多边形编辑图层url
			polylineEditLayerUrl:config.url+'/ArcGIS/rest/services/zzLine/FeatureServer/0',//线编辑图层url
			isUseJmLocalLayer:config.isUseJmLocalLayer,//是否使用精图图层,1是0不是,优先级低于isUseJtLocalLayer
			isUseJtLocalLayer:config.isUseJmLocalLayer,//是否使用精图图层,1是0不是,优先级高于isUseJmLocalLayer
			pointQueryUrl:config.url+'/ArcGIS/rest/services/zz_Point/FeatureServer/0/',//点图层查询url
			polypointEditLayerUrl:config.polypointEditLayerUrl,
			pointDefaultImg:{img:'dingwei',hoverImg:'dingwei'},//标注点默认显示图片
			polygonDefaultStyle:{
				line:{
					style:'solid',	
					color:[255,255,255,0.5],
					width:2
				},
				fill:{
					style:'cross',
					color:[255,0,0,0.5]
				}
			},//多边形默认样式
			drawPolylineStyle:{
				style:'solid',
				color:[255,0,0,1.0],
				width:2
			},
			pointClick:function(id,x,y){},//标注点点击回调
			polygonClick:function(id,rings){},//多边形点击回调
			editPolygonClick:function(graphic){
				if(typeof drawTool ==undefined||drawTool._geometryType==null){
					gmap.activedEditTool(graphic);
				}
			},//编辑层多边形点击回调
			drawPointImg:{img:'dingwei'},//画标注点默认图片
			drawPolygonStyle:{
				line:{
					style:'solit',
					color:[255,0,0],
					width:2
				},
				fill:{
					styl:'cross',
					color:[255,0,0,0.5]
				}
			},//画多边形默认样式
			drawPointComplete:function(x,y){},//画标注点完成后的回调
			drawPolygonComplete:function(rings){},//画多边形完成后的回调
			drawPolylineComplete:function(){},//画线完成后的回调
			images:{
				dingwei:{
					"path":"js/gmap/images/dingwei.png",
					"width":17,
					"height":19,
					"xOffset":0,
					"yOffset":0
				},
				biaozhu:{
					"path":"js/gmap/images/ico_biaozhu.png",
					"width":17,
					"height":19,
					"xOffset":0,
					"yOffset":0
				}
				
			},
			isShowTip:false,//是否显示地图上的弹窗
			module : 'default',//模块
			isUseProxy:false,//是否使用本地代理，开发环境为true，社区现场环境为false
			isDrawShowTip:false,
			isShowLabelChangeBtn:false,//是否显示文字层切换按钮
			initComplete:function(){}//加载完成后回调
	};
	
	initMap(options);
	function initMap(options){//初始化地图
		copyOptions(options);
		map = Map(defaults.div,{logo:false});
		map.spatialReference = new SpatialReference({wkid : defaults.wkid});
		loadCustomLayer();
		initLayer();
		gmap.locateToDefualt();
		initTool();
		initButton();
		initMySlider();
		gmap.hideMapTool();
		window.setTimeout(function(){
			defaults.initComplete();
		},500);
	}
	
	function loadCustomLayer(){//加载自定义底图
		if(!defaults.isUseTDT){
			loadCustomTiledMapLayer();
			loadGoogleLabelLayer();
			googleNormalLayer = new CustomTiledMapLayer(defaults.normalLayerUrl);
			googleMapLayer = new CustomTiledMapLayer(defaults.mapLayerUrl);		
			googleLabelLayer = new GoogleLabelLayer();
			googleMapLayer.visible = false;
			googleLabelLayer.visible = false;
			map.addLayer(googleNormalLayer);
			map.addLayer(googleMapLayer);
			map.addLayer(googleLabelLayer);
		}
		if(defaults.isUseJmLocalLayer == "1"||defaults.isUseJtLocalLayer == "1"){
//			console.info("精图卫星图地址:"+defaults.jmAreaLayerUrl);
			if(googleNormalLayer)map.removeLayer(googleNormalLayer);
			if(googleMapLayer)map.removeLayer(googleMapLayer);
			if(googleLabelLayer)map.removeLayer(googleLabelLayer);
//			map.removeAllLayers();
//			console.info("精图矢量图地址:"+defaults.jtNormalLayerUrl);
			jtNormalLayer=new esri.layers.ArcGISTiledMapServiceLayer(defaults.jtNormalLayerUrl);
//			dojo.connect(jtNormalLayer, "onLoad", function(){
				map.addLayer(jtNormalLayer);
//			});
			jtMapLayer= new esri.layers.ArcGISTiledMapServiceLayer(defaults.jmAreaLayerUrl);
			jtMapLayer.visible=false;
//			dojo.connect(jtMapLayer, "onLoad", function(){
				map.addLayer(jtMapLayer);
//			});
	/*		
			var alayer = new esri.layers.ArcGISTiledMapServiceLayer(defaults.jmAreaLayerUrl);
			googleNormalLayer.visible = false;
			dojo.connect(alayer, "onLoad", function(){
				map.addLayer(alayer);
			});
	*/		

		}
		geometryService = new GeometryService(defaults.geometryServiceURL);
	}
	
	function initLayer(){//初始化图层
		showPointGraphicsLayer = new GraphicsLayer();
		showLineGraphicsLayer = new GraphicsLayer();
		showPolygonGraphicsLayer = new GraphicsLayer();
		labelGraphicsLayer = new GraphicsLayer();
		editGraphicsLayer = new GraphicsLayer();
		dojo.connect(showPointGraphicsLayer,"onClick",pointClick);
		dojo.connect(showPolygonGraphicsLayer,"onClick",polygonClick);
		dojo.connect(editGraphicsLayer,"onClick",editPolygonClick);
		dojo.connect(showPointGraphicsLayer,"onMouseOver",onMouseOverHandler);
		dojo.connect(showPointGraphicsLayer, "onMouseOut",onMouseOutHandler);
		dojo.connect(showPolygonGraphicsLayer,"onMouseOver",onMouseOverHandler);
		dojo.connect(showPolygonGraphicsLayer, "onMouseOut",onMouseOutHandler);
		map.addLayer(labelGraphicsLayer);
		map.addLayer(showPolygonGraphicsLayer);
		map.addLayer(editGraphicsLayer);
		map.addLayer(showLineGraphicsLayer);
		map.addLayer(showPointGraphicsLayer);
	}
	
	function onMouseOverHandler(evt){//鼠标移入事件
		var graphic = evt.graphic;
		graphic.isLockImg = false;
		var heattype=graphic.attributes.hoverImg;
		var scribing=graphic.attributes.tip.type;
		if(graphic.attributes.tip.pointHover=="false" && graphic.geometry.type == "point"){
		}else{
			if(defaults.isShowTip && heattype!="heat" && scribing!="scribing"){
				var content = graphic.attributes.tip;
				if(content!=''){//判断tip是否有值
					if(content.customInfowindow=="true"){
						var div = "<div id='infowindow_div' name='infowindow_div' style='position:absolute;left:"+evt.x+"px;top:"
						+(evt.y-50)+"px;z-index:102;border:1px solid #bbb; border-radius:5px;background:white; padding:0 5px'>"+content.title+"</div>";
						$("body").append(div);
					}else{
						map.infoWindow.setContent(content.count);
						map.infoWindow.setTitle(content.name);
						map.infoWindow.show(evt.screenPoint,map.getInfoWindowAnchor(evt.screenPoint));
					}
				}
			}
		}
		if(graphic.geometry.type == "point" && heattype!="heat" && scribing!="scribing"){
			var symbol = getPictureMarkerSymbol(graphic.attributes.hoverImg);
			graphic.setSymbol(symbol);
			if(typeof(graphic.attributes.tip.index)!="undefined")
			{
				var li_index=graphic.attributes.tip.index;
				$("#mapList li").eq(li_index).addClass("cur");
			}
		}else if(graphic.geometry.type == "polygon"){
			if(graphic.attributes.tip.isChangeTrans!=undefined){
				if(graphic.attributes.tip.isChangeTrans=="true"){
					var symbol = graphic.symbol;
					polygonStyle.trans = symbol.color.a;
					var outline = symbol.outline;
					polygonStyle.width = outline.width;
					var b = symbol.color.b;
					var g = symbol.color.g;
					var r = symbol.color.r;
					symbol.setColor(new Color([r,g,b,1.0]));
					symbol.setOutline(new SimpleLineSymbol(outline.style,
					    	    outline.color,2));
					graphic.setSymbol(symbol);
					
				}
			}
			
		}
	}
	var polygonStyle = {"trans":"","width":""};
	function onMouseOutHandler(evt){//鼠标移出事件
		$("#infowindow_div").remove();
		var graphic = evt.graphic;
		if(graphic.geometry.type == "point" && graphic.isLockImg == false){
			var symbol = getPictureMarkerSymbol(graphic.attributes.img);
			graphic.setSymbol(symbol);
			if(typeof(graphic.attributes.tip.index)!="undefined")
			{
				var li_index=graphic.attributes.tip.index;
				$("#mapList li").eq(li_index).removeClass("cur");
			}
	
		}else if(graphic.geometry.type == "polygon"){
			if(graphic.attributes.tip.isChangeTrans!=undefined){
				if(graphic.attributes.tip.isChangeTrans=="true"){
					var symbol = graphic.symbol;
					var outline = symbol.outline;
//					var transparent = symbol.color.a;
					var b = symbol.color.b;
					var g = symbol.color.g;
					var r = symbol.color.r;
					symbol.setColor(new Color([r,g,b,polygonStyle.trans]));
					symbol.setOutline(new SimpleLineSymbol(outline.style,
				    	    outline.color,polygonStyle.width));
					graphic.setSymbol(symbol);
//					graphic.setInfoTemplate(infoTemplate);
				}
			}
		}
		if(defaults.isShowTip){
			map.infoWindow.hide();
		}
	}
	
	function pointClick(event){//点点击事件
		var graphic = event.graphic;
		graphic.getShape().moveToFront();
		map.infoWindow.hide();
		var content = graphic.attributes.tip;
		if(content==null || content==""||content.content==""){
			return;
		}
		map.infoWindow.setContent(content.content);
		map.infoWindow.setTitle(content.title);
		console.info(event);
		map.infoWindow.show({x:event.offsetX,y:event.offsetY},map.getInfoWindowAnchor({x:event.x,y:event.y}));
		if(defaults.module == 'mapro'){//六大板块模块回调方法
			graphic.clickHandler(graphic);
		}
//			map.infoWindow.show(event.screenPoint,map.getInfoWindowAnchor(event.screenPoint));
//		if(null == graphic.clickHandler){
//			defaults.pointClick(graphic.attributes.id,graphic.geometry.x,graphic.geometry.y);
//		}else{
//			graphic.clickHandler(graphic.attributes.id,graphic.geometry.x,graphic.geometry.y,graphic.attributes.tip);
//		}
	}
	
	function polygonClick(event){//面点击事件
		var graphic = event.graphic;
		if(null == graphic.clickHandler){
			defaults.polygonClick(graphic.attributes.id,graphic.geometry.rings);
		}else{
			graphic.clickHandler(graphic);
//			graphic.clickHandler(graphic.attributes.id,graphic.geometry.rings);
		}
	}
	
	function editPolygonClick(event){//编辑层面点击事件
		var graphic = event.graphic;
//		console.info("ins");
		if(null==graphic.clickHandler){
			defaults.editPolygonClick(graphic);
		}else{
			graphic.clickHandler(graphic);
		}
	}
	
	this.getShowAreaSymbol=getShowAreaSymbol;
	function getShowAreaSymbol(color){//获取显示面样式
		var style = defaults.polygonDefaultStyle;
		var line = style.line;
		var lineSymbol = new SimpleLineSymbol(getLineStyle(line.style),new Color(line.color),line.width);
		var fill = style.fill;
		var simpleFillSymbol = new SimpleFillSymbol(getFillStyle(fill.style),lineSymbol,getColor(color,fill.color[3]));
		return simpleFillSymbol;
	}
	
	function getTextSymbol(label){//获取字体样式
		return new TextSymbol(label, new Font("12px", Font.STYLE_NORMAL,Font.VARIANT_NORMAL, Font.WEIGHT_BOLD),new Color([ 0, 0, 0 ]));
	}
	
	function getColor(color,A){//获取颜色（颜色转换）
		var R = color.substring(1,3);
		var G = color.substring(3,5);
		var B = color.substring(5,7);
		R = parseInt("0x" + R);
		G = parseInt("0x" + G);
		B = parseInt("0x" + B);
		return new Color([R,G,B,A]);
	}
	
	
	function initTool(){//初始化画图和编辑工具
		drawTool = new Draw(map);
		drawTool.on("draw-end", drawDrawEndHandler());
	}
	
	
	function drawDrawEndHandler(){//画图结束回调
		return function(event){
			var geometry = event.geometry;
			if(!drawTool.isTemp){
				editGraphicsLayer.clear();
			}
			addGraphicToEditGraphics(geometry);
			switch(geometry.type){
				case "point":
					if(null == drawTool.drawCompleteHandler){
						defaults.drawPointComplete(geometry.x,geometry.y);
					}else{
						drawTool.drawCompleteHandler(geometry.x,geometry.y);
					}
					break;
				case "polygon":
					if(null == drawTool.drawCompleteHandler){
						defaults.drawPolygonComplete(geometry.rings);
					}else{
						drawTool.drawCompleteHandler(geometry.rings);
					}
					break;
				case "polyline":
					if(null == drawTool.drawCompleteHandler){
						defaults.drawPolylineComplete(geometry.rings);
					}else{
						drawTool.drawCompleteHandler(geometry.rings);
					}
					break;
			}
		};
	}
	
	function initButton(){//加载按钮
		var div = document.getElementById(defaults.div);
        
        var btndiv = document.createElement("div");
        btndiv.setAttribute("class", "mapbtndiv");
        var btn1 = document.createElement("span");
        btn1.setAttribute("class", "mapbtn");
        btn1.innerHTML = "卫星图";
        var btn2 = document.createElement("span");
        btn2.setAttribute("class", "mapbtn");
        btn2.setAttribute("style", "left:200px");
        btn2.innerHTML = "地图";
        
        var btnLabel=document.createElement("div");
        btnLabel.setAttribute("class", "lablebtn");
        btnLabel.setAttribute("id", 'labelBtn');
        
        btndiv.appendChild(btn1);
        btndiv.appendChild(btn2);
        btndiv.appendChild(btnLabel);
        div.appendChild(btndiv);
        
        if(window.addEventListener){ 
        	btn1.addEventListener("click", function(){gmap.tabMap(1);}, false);
        	btn2.addEventListener("click", function(){gmap.tabMap(0);}, false);
        	btnLabel.addEventListener("click", function(){gmap.labelBtnChange()}, false);
        }else{
            btn1.attachEvent("onclick", function(){gmap.tabMap(1);});
            btn2.attachEvent("onclick", function(){gmap.tabMap(0);});
            btnLabel.attachEvent("onclick", function(){gamp.labelBtnChange()});
        }
        if(defaults.isShowLabelChangeBtn==false){
        	$(".lablebtn").hide();
        }
	}

	//地图放大缩小添加div
	function initMySlider(){
		var str = "";
		str += '<div unselectable="on" id="map_slider" class=" BMap_stdMpCtrl BMap_stdMpType0 BMap_noprint anchorBR" style="width: 65px; height: 227px; bottom: 40px; right: 10px; top: auto; left: auto; position: absolute; z-index: 10;">';
		str += '<div class="BMap_stdMpZoom" style="height: 227px; width: 65px;">';
		str += '<div class="BMap_button BMap_stdMpZoomIn" id="gobig" title="放大一级">';
		str += '    <div class="BMap_smcbg"></div>';
		str += ' </div>';
		str += '<div class="BMap_button BMap_stdMpZoomOut" id="gosmall" title="缩小一级" style="top: 204px;">';
		str += '   <div class="BMap_smcbg"></div>';
		str += '</div>';
		str += '<div class="BMap_stdMpSlider" style="height: 180px;">';
		str += '   <div class="BMap_stdMpSliderBgTop" style="height: 180px;">';
		str += '      <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '    <div id="sliderHeight" class="BMap_stdMpSliderBgBot" style="height: 131px;">';
		str += '        <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '    <div class="BMap_stdMpSliderMask" id="movetohere" title="放置到此级别"></div>';
		str += '    <div  id="sliderTop" class="BMap_stdMpSliderBar" title="拖动缩放" style="cursor: url(http://webmap0.map.bdimg.com/image/api/openhand.cur) 8 8, default; top: 34px;">';
		str += '        <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '</div>';
		str += '<div class="BMap_zlHolder hvr" id="jb_div" style="display:none">';
		str += '    <div class="BMap_zlSq" id="zlsq">';
		str += '        <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '    <div class="BMap_zlSt" id="zlst">';
		str += '        <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '    <div class="BMap_zlQu" id="zlqu">';
		str += '        <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '    <div class="BMap_zlCity" id="zlcity">';
		str += '        <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '    <div class="BMap_zlProv" id="zlprov">';
		str += '        <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '    <div class="BMap_zlCountry" id="zlcountry">';
		str += '        <div class="BMap_smcbg"></div>';
		str += '    </div>';
		str += '</div>';
		str += '</div>';
		str += '</div>';
		$("#map_root").append(str);
		if(typeof(iscouldcircle)!="undefined"){
			if(iscouldcircle == null ){
				return;
			}
			if(iscouldcircle != "1"){
				return;
			}

			var str2 = '';
			str2 += '<div class="organ-query-list" style="display:none">'
			str2 += '<div class="dotChice user-defined" > '
			str2 += '<div style="text-align:center">'
			str2 += '<span class="fnt14 ffamily">请先在地图上点选</span>'
			str2 += '<input type="button" id="dx_button" class="cblue ffamily fnt14 pointer dotChange-btn" onclick="change_select()" value="点选"></div>'
			str2 += '<div class="BMap_stdMpZoom1">'
			str2 += '<div class="BMap_button BMap_stdMpZoomOut" id="narrow" title="缩小一级"></div>'
			str2 += '<div class="BMap_button BMap_stdMpZoomIn" id="enlarge" title="放大一级"></div>'
			str2 += '<div class="BMap_stdMpSlider">'
			str2 += '<div class="BMap_stdMpSliderBgLeft" style="left: 2px; width: 8px;"></div>'
			str2 += '<div class="BMap_stdMpSliderBgRight"></div>'
			str2 += '<div class="BMap_stdMpSliderMask" title="放置到此级别"></div>'
			str2 += '<div class="BMap_stdMpSliderBar" title="拖动缩放" id="draggable" style="left: 2px;"></div>'
			str2 += '</div>'
			str2 += '<input type="text" readonly="readonly" class="BMap_data tcenter fnt12" placeholder="范围数值" value="500" id="distance">'
			str2 += '</div>'
			str2 += '</div></div>'
			$("#map_root").append(str2);
			var narrow = document.getElementById("narrow");
			var enlarge = document.getElementById("enlarge");
			var _move=false;//移动标记
//			$("#draggable").click(function(){
//		        //alert("click");//点击（松开后触发）
//		        }).mousedown(function(e){
//		        _move=true;
//		        _x=e.pageX-parseInt($("#draggable").css("left"));
//		    });
//		    $("#draggable").click(function(){
//		        }).mouseup(function(e){
//		        	if(serach_circle!=null)
//		        	{  
//		        		pageNo=1;
//		        		var dis = $("#distance").val();
//		        		data = parseFloat(dis);
////		        		checkRadius(data*1000);
//////		             	areaSearch(serach_circle.Cc.center,serach_circle.Cc.radius);
////		        		areaSearch(serach_circle.getCenter(),data*1000);
//		        	}
//		        	
//		 //         alert("stop");
//		 //       _move=true;
//		 //       _x=e.pageX-parseInt($("#draggable").css("left"));			
//		    });
//		    $(document).mousemove(function(e){
//		        if(_move){
//		           var x=e.pageX-_x;//移动时根据鼠标位置计算控件左上角的绝对位置
//				   var len = parseInt($("#draggable").parent().width()-$("#draggable").width());
//				   data = parseFloat(x*n).toFixed(2);
//				   if(x < 2 || x > len){
//					 return false;   
//					}else{
//						$("#draggable").css({left:x});//控件新位置
//						$('.BMap_stdMpSliderBgLeft').width(x);
//						$('.BMap_data').val(data+'公里');
//						$('.Bmap_line_data span').text(data+'公里');
//						$('.Bmap_circle').show().css({
//							width:data*cell,
//							height:data*cell,
//							'margin-top':-(data*(cell/2)),
//							'margin-left':-(data*(cell/2))
//						});
//						
//						if(serach_circle!=null)
//						{
//							gmap.clearQueryCircle();
//			    			drawCircle(serach_circle.geometry.center.x,serach_circle.geometry.center.y,data*1000,"","");
//						}
//					}
//		        }
//		    }).mouseup(function(){
//		    _move=false;
//		  });  
//			narrow.addEventListener("click", function(){
//					var base_x=$("#draggable").css("left");
//		        	var num_x=base_x.substr(0,base_x.length-2);
//		        	if(num_x>=4)
//		        	{
//		        		num_x--;
//		        		num_x--;
//		        	}
//		        	else
//		        	{
//		        		num_x=2;
//		        	}
//		        	data = parseFloat(num_x*n).toFixed(2);
//		        	$("#draggable").css({left:num_x});
//		        	$('.BMap_stdMpSliderBgLeft').width(num_x);
//		    		$('.BMap_data').val(data+'公里');
//		    		$('.Bmap_line_data span').text(data+'公里');
//		    		$('.Bmap_circle').show().css({
//		    			width:data*cell,
//		    			height:data*cell,
//		    			'margin-top':-(data*(cell/2)),
//		    			'margin-left':-(data*(cell/2))
//		    		});
//		    		if(serach_circle!=null)
//		    		{
//		    			gmap.clearQueryCircle();
//		    			drawCircle(serach_circle.geometry.center.x,serach_circle.geometry.center.y,data*1000,"","");
////		             	areaSearch(serach_circle.Cc.center,serach_circle.Cc.radius);
//		    			//areaSearch(serach_circle.center,serach_circle.radius);
//		    		}
//				}
//	        	, false);
			
//			enlarge.addEventListener("click", function(){
//		    	var base_x=$("#draggable").css("left");
//		    	var num_x=base_x.substr(0,base_x.length-2);
//		    	if(num_x<=122)
//		    	{
//		    		num_x++;
//		    		num_x++;
//		    	}
//		    	else
//		    	{
//		    		num_x=124;
//		    	}
//		    	data = parseFloat(num_x*n).toFixed(2);
//		    	$("#draggable").css({left:num_x});
//		    	$('.BMap_stdMpSliderBgLeft').width(num_x);
//				$('.BMap_data').val(data+'公里');
//				$('.Bmap_line_data span').text(data+'公里');
//				$('.Bmap_circle').show().css({
//					width:data*cell,
//					height:data*cell,
//					'margin-top':-(data*(cell/2)),
//					'margin-left':-(data*(cell/2))
//				});
//				if(serach_circle!=null)
//				{
//					gmap.clearQueryCircle();
//					drawCircle(serach_circle.geometry.center.x,serach_circle.geometry.center.y,data*1000,"","");
//		//         	areaSearch(serach_circle.Cc.center,serach_circle.Cc.radius);
//					//areaSearch(serach_circle.center,serach_circle.radius);
//				}}, false);
			map.onClick = function(e){
				if(isPointCircle){
					gmap.clearQueryCircle();
					var firstRadius = parseFloat($("#distance").val().replace("公里",""));
					drawCircle(e.mapPoint.x,e.mapPoint.y,firstRadius,"","");
					isPointCircle=false;
				}
			}
		}
		
		
		
		
		var btn1 = document.getElementById("gobig");
		var btn2 = document.getElementById("gosmall");
		var btn3 = document.getElementById("sliderTop");
		var btn4 = document.getElementById("movetohere");
		var btn5 = document.getElementById("zlst");
		var btn6 = document.getElementById("zlcity");
		var btn7 = document.getElementById("zlprov");
		var btn8 = document.getElementById("zlcountry");
		var btn9 = document.getElementById("zlsq");
		var btn10 = document.getElementById("zlqu");
		var divmap_slider = document.getElementById("map_slider");
		
        if(window.addEventListener){ 
        	btn1.addEventListener("click", function(){
        		 var strTop = ($("#sliderTop").css("top"));
        		 strTop = strTop.substring(0,strTop.length-2);
        		 if(strTop!="4"){
        			 $("#sliderHeight").height($("#sliderHeight").height()+10);
        		 var top = parseInt(strTop)-10;
        		 $("#sliderTop").css("top",top+"px");
        		 quzheng(map);}}, false);
        	btn2.addEventListener("click", function(){
	       		 var strTop = ($("#sliderTop").css("top"));
	       		 strTop = strTop.substring(0,strTop.length-2);
	       		if(strTop!="164"){
	       			$("#sliderHeight").height($("#sliderHeight").height()-10);
	       		 var top = parseInt(strTop)+10;
	       		 $("#sliderTop").css("top",top+"px");
	       		quzheng(map);}}, false);
        	btn3.addEventListener("mouseup",function(){
        		$("#sliderTop").removeClass("sliderTop");
        	},false);
        	btn3.addEventListener("mousedown", function(){
        		$("#sliderTop").addClass("sliderTop");
        		document.addEventListener("mousemove",sureMove,false);
        	}, false);
        	document.addEventListener("mouseup", function(){
        		if($("#sliderTop").hasClass("sliderTop")){
	        		document.removeEventListener("mousemove",sureMove,false);
	        		$("#sliderTop").removeClass("sliderTop");
	        		quzheng(map)
        		}
        	}, false);
        	btn3.addEventListener("mouseup", function(){
        		document.removeEventListener("mousemove",sureMove,false);
        		$("#sliderTop").removeClass("sliderTop");
        		quzheng(map)
        	}, false);
        	btn4.addEventListener("mousedown", function(e){
        		$("#sliderTop").removeClass("myslider");
        		moverealize(e);
        		quzheng(map)
        	}, false);
        	btn5.addEventListener("mousedown", function(e){
        		$("#sliderTop").removeClass("myslider");
        		movejibie("st");
        		quzheng(map)
        	}, false);
        	btn6.addEventListener("mousedown", function(e){
        		$("#sliderTop").removeClass("myslider");
        		movejibie("city");
        		quzheng(map)
        	}, false);
        	btn7.addEventListener("mousedown", function(e){
        		$("#sliderTop").removeClass("myslider");
        		movejibie("prov");
        		quzheng(map)
        	}, false);
        	btn8.addEventListener("mousedown", function(e){
        		$("#sliderTop").removeClass("myslider");
        		movejibie("country");
        		quzheng(map)
        	}, false);
        	btn9.addEventListener("mousedown", function(e){
        		$("#sliderTop").removeClass("myslider");
        		movejibie("sq");
        		quzheng(map)
        	}, false);
        	btn10.addEventListener("mousedown", function(e){
        		$("#sliderTop").removeClass("myslider");
        		movejibie("qu");
        		quzheng(map)
        	}, false);
        	divmap_slider.addEventListener("mouseover", function(e){
        		$("#jb_div").css("display",'block');
        	}, false);
        	divmap_slider.addEventListener("mouseout", function(e){
        			$("#jb_div").css("display",'none');
        	}, false);
        }else{
        	btn1.addEventListener("onclick", function(){map.setExtent(map.extent.expand(0.5));}, false);
        	btn2.addEventListener("onclick", function(){map.setExtent(map.extent.expand(2));}, false);
        }
		map.onDblClick = function(e){
			setTimeout(function(){
				var level = map.getLevel();
				var height = (level)*10+1;
				var top = 165-height;
				if(height<=161){
					$("#sliderTop").css("top",top+"px");
					$("#sliderHeight").height(height);
				}
	        },800); 
		};
		map.onMouseWheel = function(e){
//			var base_x=$("#draggable").css("left");
//        	var num_x=base_x.substr(0,base_x.length-2);
//        	if(num_x>=4)
//        	{
//        		num_x--;
//        		num_x--;
//        	}
//        	else
//        	{
//        		num_x=2;
//        	}
//        	data = parseFloat(num_x*n).toFixed(2);
			data = $("input[class='BMap_data tcenter fnt12']").val();
			if(data){
				data = data.replace("公里","");
			}
//        	$("#draggable").css({left:num_x});
//        	$('.BMap_stdMpSliderBgLeft').width(num_x);
//    		$('.BMap_data').val(data+'公里');
//    		$('.Bmap_line_data span').text(data+'公里');
//    		$('.Bmap_circle').show().css({
//    			width:data*cell,
//    			height:data*cell,
//    			'margin-top':-(data*(cell/2)),
//    			'margin-left':-(data*(cell/2))
//    		});
//			gmap.clearQueryCircle();
//			gmap.clearPolygons();
//			if(serach_circle){
//				drawCircle(serach_circle.geometry.center.x,serach_circle.geometry.center.y,data*1000,"","");
//			}
			setTimeout(function(){
				var level = map.getLevel();
				var height = (level)*10+1;
				var top = 165-height;
				if(height<=161){
					$("#sliderTop").css("top",top+"px");
					$("#sliderHeight").height(height);
				}
	        },800); 
		};
		
	}
	
	
	//--------------------初始化End--------------------//
	
	function addGraphicToEditGraphics(geometry){//添加图形到编辑层
		switch(geometry.type){
			case "point":
				var point = new Point(geometry.x,geometry.y,map.spatialReference);
				var symbol = getPictureMarkerSymbol(defaults.drawPointImg);
				var attributes = {img:defaults.drawPointImg};
				if(defaults.isDrawShowTip==true)
				{
					var infoTemplate = new InfoTemplate();
					var graphic = new Graphic(point,symbol,attributes,infoTemplate);
					editGraphicsLayer.add(graphic);
				}
				else
				{
					var graphic = new Graphic(point,symbol,attributes);
					editGraphicsLayer.add(graphic);
				}
				break;
			case "polygon":
				var polygonJson = {"rings":geometry.rings,"spatialReference": map.spatialReference};
				var polygon = new Polygon(polygonJson);
				var symbol = getSimpleFillSymbol(defaults.drawPolygonStyle);
				var attributes = {};
				var infoTemplate = new InfoTemplate();
				var graphic = new Graphic(polygon,symbol,attributes);
				editGraphicsLayer.add(graphic);
				break;
			case "polyline":
				var polylineJson={"paths":geometry.paths,"spatialReference": map.spatialReference};
				var polyline=new Polyline(polylineJson);
				var symbol=getSimpleLineSymbol(defaults.drawPolylineStyle);
				var attributes = {};
				var infoTemplate = new InfoTemplate();
				var graphic = new Graphic(polyline,symbol,attributes);
				editGraphicsLayer.add(graphic);
				break;
		}
	}
	
	function copyOptions(options){//复制数据
		for(var item in defaults){
			if(null == options[item]){
			}else{
				defaults[item] = options[item];
			}
		}
	}
	
	function isNull(arg){//判断是否为空
		return arg == ''?true:false;
	}
	
	function getPictureMarkerSymbol(img){//获取图样式
		if(typeof img.path=="undefined" ){
			var style = defaults.images[img];
			if(!style && imgs && imgs.length>0){//如果指定图不存在尝试重新加载一次
				gmap.registerImages(imgs);
				style = defaults.images[img];
			}
			if(typeof style.path=="undefined")
				{
					console.info(img);
					console.info(style);
				}
			var symbol = new PictureMarkerSymbol(style.path,style.width,style.height);
			symbol.setOffset(style.xOffset,style.yOffset);
			return symbol;
		}else{
			var symbol= new PictureMarkerSymbol(img.path,img.width,img.height);
			symbol.setOffset(img.xOffset,img.yOffset);
			return symbol;
		}
		
	}
	
	this.getSimpleLineSymbol=getSimpleLineSymbol;
	function getSimpleLineSymbol(style){//获取线样式
		var lineSymbol = new SimpleLineSymbol(getLineStyle(style.style),new Color(style.color),style.width);
		//强制设置样式
        lineSymbol.color.a = style.color[3];
        lineSymbol.color.r = style.color[0];
        lineSymbol.color.g = style.color[1];
        lineSymbol.color.b = style.color[2];
        return lineSymbol;
	}
	
	
	this.getSimpleFillSymbol = getSimpleFillSymbol;
	function getSimpleFillSymbol(style){//获取面样式
		var line = style.line;
		var lineSymbol = new SimpleLineSymbol(getLineStyle(line.style),new Color(line.color),line.width);
		//强制设置样式
        lineSymbol.color.a = line.color[3];
        lineSymbol.color.r = line.color[0];
        lineSymbol.color.g = line.color[1];
        lineSymbol.color.b = line.color[2];
		var fill = style.fill;
		var simpleFillSymbol = new SimpleFillSymbol(getFillStyle(fill.style),lineSymbol,new Color(fill.color));
		//强制设置样式
        simpleFillSymbol.color.a = fill.color[3];
        simpleFillSymbol.color.r = fill.color[0];
        simpleFillSymbol.color.g = fill.color[1];
        simpleFillSymbol.color.b = fill.color[2];
		return simpleFillSymbol;
	}
	
	function getLineStyle(style){//获取线类型
		var result = SimpleLineSymbol.STYLE_SOLID;
		switch(style){
			case "solid":
				result = SimpleLineSymbol.STYLE_SOLID;
				break;
			case "dash":
				result = SimpleLineSymbol.STYLE_DASH;
				break;
			case "dashdot":
				result = SimpleLineSymbol.STYLE_DASHDOT;
				break;
			case "dashdotdot":
				result  = SimpleLineSymbol.STYLE_DASHDOTDOT;
				break;
			case "dot":
				result = SimpleLineSymbol.STYLE_DOT;
				break;
		}
		return result;
	}
	
	function getFillStyle(style){//获取面填充类型
		var result = SimpleFillSymbol.STYLE_SOLID;
		switch(style){
			case "solid":
				result = SimpleFillSymbol.STYLE_SOLID;
				break;
			case "cross":
				result = SimpleFillSymbol.STYLE_CROSS;
				break;
			case "horizontal":
				result = SimpleFillSymbol.STYLE_HORIZONTAL;
				break;
			case "vertical":
				result  = SimpleFillSymbol.STYLE_VERTICAL;
				break;
		}
		return result;
	}
	
	function graphicsExtent(d){//获取定位数据
		var a=d[0].geometry,b=a.getExtent(),c,e,g=d.length;
		null===b&&(b=new esri.geometry.Extent(a.x,a.y,a.x,a.y,a.spatialReference));
		for(e=1;e<g;e++)
			c=(a=d[e].geometry).getExtent(),
			null===c&&(c=new esri.geometry.Extent(a.x,a.y,a.x,a.y,a.spatialReference)),
			b=b.union(c);
		return 0>=b.getWidth()&&0>=b.getHeight()?null:b;
	}
	
	function pointGeographicToWebMercator(x,y){//点转换
		var xy = new Object;
		xy.x = x;
		xy.y = y;
		if(x > -180 && x < 180 && y > -90 && y < 90 && !defaults.isUseTDT){
			xy = gmap.geographicToWebMercator(x,y);
		}
		return xy;
	}
	
	function polyGeographicToWebMercator(rings){//多边形转换
		for(var i = 0 ; i < rings.length;i++){
			for(var j = 0 ; j < rings[i].length; j++){
				var x = rings[i][j][0];
				var y = rings[i][j][1];
				if(x > -180 && x < 180 && y > -90 && y < 90){
					var xy = gmap.geographicToWebMercator(x,y);
					x = xy.x;
					y = xy.y;
				}
				rings[i][j][0] = x;
				rings[i][j][1] = y;
			}
		}
		return rings;
	}
	
	function polyWebMercatorToGeographic(rings){//多边形转换
		for(var i = 0 ; i < rings.length;i++){
			for(var j = 0 ; j < rings[i].length; j++){
				var x = rings[i][j][0];
				var y = rings[i][j][1];
				if(x > -180 && x < 180 && y > -90 && y < 90){
					
				}else{
					var xy = gmap.webMercatorToGeographic(x,y);
					x = xy.x;
					y = xy.y;
				}
				rings[i][j][0] = x;
				rings[i][j][1] = y;
			}
		}
		return rings;
	}
};