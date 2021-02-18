/**
 * User: liyh
 * Date: 14-6-3
 * Version: 1.4
 */

var sunsharing = new Object();
sunsharing.gmap = function(options){
	
//--------------------依赖包Start--------------------//
	var gmap = this;
	require(["esri/map"],function(Value){Map = Value;});
	require(["esri/graphic"],function(Value){Graphic = Value;});
	require(["esri/InfoTemplate"],function(Value){InfoTemplate = Value;});
	require(["esri/SpatialReference"],function(Value){SpatialReference = Value;});
	require(["esri/toolbars/draw"],function(Value){Draw = Value;});
	require(["esri/layers/GraphicsLayer"],function(Value){GraphicsLayer = Value;});
	require(["esri/geometry/Point"],function(Value){Point = Value;});
	require(["esri/geometry/Polyline"],function(Value){Polyline = Value;});
	require(["esri/geometry/Polygon"],function(Value){Polygon = Value;});
	require(["esri/geometry/Circle"],function(Value){Circle = Value;});
	require(["esri/geometry/webMercatorUtils"], function(Value) {WebMercatorUtils = Value;});
	require(["esri/symbols/PictureMarkerSymbol"],function(Value){PictureMarkerSymbol = Value;});
	require(["esri/symbols/SimpleLineSymbol"],function(Value){SimpleLineSymbol = Value;});
	require(["esri/symbols/SimpleFillSymbol"],function(Value){SimpleFillSymbol = Value;});
	require(["esri/units"],function(Value){Units = Value;});
	require(["esri/geometry/geodesicUtils"],function(Value){GeodesicUtils = Value;});
	require(["dojo/colors"],function(Value){Color = Value;});
	require(["esri/tasks/GeometryService"],function(Value){GeometryService = Value;});
	
//--------------------依赖包End--------------------//
	
//--------------------接口方法Start--------------------//
	
	this.tabMap = function(index){//切换底图
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
		};
	};
	
	this.locateToAreaCenter=function(geometry){//定位到面的质心
		 var geometries = new Array();
		 geometries[0]=geometry;
		 console.info(geometry);
		 geometryService.labelPoints(geometries,function(evt){
			    console.info(evt);
			    console.info("x:"+evt[0].x+"  y:"+evt[0].y);
			    gmap.centerAtPoint(evt[0].x,evt[0].y,15);
		 });
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
		if(!isNull(x) && !isNull(y)){
			var xy = pointGeographicToWebMercator(x,y);
			x = xy.x;
			y = xy.y;
			var point = new Point(x, y, map.spatialReference);
			map.centerAt(point);
		}
	};
	
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
		this.centerAtPoint(
				defaults.defaultCenter.x,
				defaults.defaultCenter.y,
				defaults.defaultLevel);
	};
	
	this.addPoint = function(x,y,tip,img,hoverImg,clickHandler){//新增一个标注点至地图，并返回这个标注点的ID
		var xy = pointGeographicToWebMercator(x,y);
		x = xy.x;
		y = xy.y;
		var point = new Point(x,y,map.spatialReference);
		var symbol = getPictureMarkerSymbol(img);
		var attributes = {id:pointID,img:img,hoverImg:hoverImg,tip:tip};
		var graphic = new Graphic(point,symbol,attributes,null);
		graphic.clickHandler = clickHandler;
		graphic.isLockImg = false;
		showPointGraphicsLayer.add(graphic);
		return pointID++;
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
	
	this.clearPoints = function(){//清除所有的标注点
		showPointGraphicsLayer.clear();
	};
	
	this.clearPolyLines = function(){//清除所有的线
		showLineGraphicsLayer.clear();
	};
	
	this.clearPolygons = function(){//清除所有多边形
		showPolygonGraphicsLayer.clear();
	};
	
	this.removePoint = function(id){//移除某个ID的标注点
		for(var i in showPointGraphicsLayer.graphics){
			var graphic = showPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				showPointGraphicsLayer.remove(graphic);
				break;
			}
		}
	};
	
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
		defaults.drawPolygonStyle = style;
		drawTool.activate(Draw.POLYGON);
	};
	
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
//--------------------接口方法End--------------------//
	
//--------------------初始化Start--------------------//
	var geometryService;//几何服务          
	var map;//地图对象
	var drawTool;//画图工具
	
	var googleNormalLayer;
	var googleMapLayer;
	var googleLabelLayer;
	
	var showPointGraphicsLayer;//标注点显示层
	var showLineGraphicsLayer;//线显示层
	var showPolygonGraphicsLayer;//多边形显示层
	var editGraphicsLayer;//编辑层
	
	this.getShowPolygonGraphicsLayer = function(){
		return showPolygonGraphicsLayer;
	};
	
	var pointID = 0;//标注点ID
	var polyLineID = 0;//线ID
	var polygonID = 0;//多边形ID
	
	this.getPolygonID = function(){
		return polygonID++;
	};
	
	this.getDefaults = function(){
		return defaults;
	};
	
	var defaults = {//默认参数
			div:'map',//地图所在的div的ID
			defaultCenter:{x:'1.3149074063320074E7',y:'2813360.771119381'},//默认中心
			defaultLevel:'11',//默认地图等级
			wkid:'102113',//坐标系类型
			geometryServiceURL:'http://192.168.0.49:60000/ArcGIS/rest/services/Geometry/GeometryServer',//几何服务URL
			normalLayerUrl:'http://emap2.mapabc.com/mapabc/maptile?x=<x>&y=<y>&z=<z>',//矢量图地址
			
			mapLayerUrl:'http://mt0.google.cn/vt/lyrs=s@161&hl=zh-CN&gl=CN&src=app&expIds=201527&rlbl=1&x=<x>&s=&y=<y>&z=<z>&s=Ga',//卫星图地址
			pointDefaultImg:{img:'dingwei',hoverImg:'dingwei'},//标注点默认显示图片
			polygonDefaultStyle:{
				line:{
					style:'dash',	
					color:[0,255,0],
					width:2
				},
				fill:{
					styl:'cross',
					color:[255,0,0,0.3]
				}
			},//多边形默认样式
			pointClick:function(id,x,y){},//标注点点击回调
			polygonClick:function(id,rings){},//多边形点击回调
			drawPointImg:{img:'dingwei'},//画标注点默认图片
			drawPolygonStyle:{
				line:{
					style:'dash',
					color:[0,255,0],
					width:2
				},
				fill:{
					styl:'cross',
					color:[255,0,0,0.5]
				}
			},//画多边形默认样式
			drawPointComplete:function(x,y){},//画标注点完成后的回调
			drawPolygonComplete:function(rings){},//画多边形完成后的回调
			images:{
				dingwei:{
					"path":"js/gmap/images/dingwei.png",
					"width":17,
					"height":19,
					"xOffset":0,
					"yOffset":0
				}
			},
			isShowTip:false,
			isDrawShowTip:false,
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
		window.setTimeout(function(){defaults.initComplete();},500);
	}
	
	function loadCustomLayer(){//加载自定义底图
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
		geometryService = new GeometryService(defaults.geometryServiceURL);
	}
	
	
	function initLayer(){//初始化图层
		showPointGraphicsLayer = new GraphicsLayer();
		showLineGraphicsLayer = new GraphicsLayer();
		showPolygonGraphicsLayer = new GraphicsLayer();
		editGraphicsLayer = new GraphicsLayer();
		dojo.connect(showPointGraphicsLayer,"onClick",pointClick);
		dojo.connect(showPolygonGraphicsLayer,"onClick",polygonClick);
		dojo.connect(showPointGraphicsLayer,"onMouseOver",onMouseOverHandler);
		dojo.connect(showPointGraphicsLayer, "onMouseOut",onMouseOutHandler);
		dojo.connect(showPolygonGraphicsLayer,"onMouseOver",onMouseOverHandler);
		dojo.connect(showPolygonGraphicsLayer, "onMouseOut",onMouseOutHandler);
		map.addLayer(showPolygonGraphicsLayer);
		map.addLayer(editGraphicsLayer);
		map.addLayer(showLineGraphicsLayer);
		map.addLayer(showPointGraphicsLayer);
	}
	
	function onMouseOverHandler(evt){//鼠标移入事件
		var graphic = evt.graphic;
		if(defaults.isShowTip){
			var content = graphic.attributes.tip;
			map.infoWindow.setContent(content);
			map.infoWindow.setTitle("");
			map.infoWindow.show(evt.screenPoint,map.getInfoWindowAnchor(evt.screenPoint));
		}
		if(graphic.geometry.type == "point"){
			var symbol = getPictureMarkerSymbol(graphic.attributes.hoverImg);
			graphic.setSymbol(symbol);
		}
	}
	
	function onMouseOutHandler(evt){//鼠标移出事件
		var graphic = evt.graphic;
		if(graphic.geometry.type == "point" && graphic.isLockImg == false){
			var symbol = getPictureMarkerSymbol(graphic.attributes.img);
			graphic.setSymbol(symbol);
		}
		map.infoWindow.hide();
	}
	
	function pointClick(event){//点点击事件
		var graphic = event.graphic;
		if(null == graphic.clickHandler){
			defaults.pointClick(graphic.attributes.id,graphic.geometry.x,graphic.geometry.y);
		}else{
			graphic.clickHandler(graphic.attributes.id,graphic.geometry.x,graphic.geometry.y);
		}
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
	
	function initTool(){//初始化工具
		drawTool = new Draw(map);
		drawTool.on("draw-end", drawDrawEndHandler());
	}
	
	function drawDrawEndHandler(){//画图结束回调
		return function(event){
			var geometry = event.geometry;
			if(!drawTool.isTemp)
				editGraphicsLayer.clear();
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
        
        btndiv.appendChild(btn1);
        btndiv.appendChild(btn2);
        div.appendChild(btndiv);
        
        if(window.addEventListener){ 
        	btn1.addEventListener("click", function(){gmap.tabMap(1);}, false);
        	btn2.addEventListener("click", function(){gmap.tabMap(0);}, false);
        }else{
            btn1.attachEvent("onclick", function(){gmap.tabMap(1);});
            btn2.attachEvent("onclick", function(){gmap.tabMap(0);});
        }
	}
	
	
	//--------------------初始化End--------------------//
	
	function addGraphicToEditGraphics(geometry){//添加图形到编辑层
		switch(geometry.type){
			case "point":
				var point = new Point(geometry.x,geometry.y,map.spatialReference);
				var symbol = getPictureMarkerSymbol(defaults.drawPointImg);
				var attributes = {img:defaults.drawPointImg};
				if(isDrawShowTip==true)
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
				var graphic = new Graphic(polygon,symbol,attributes,infoTemplate);
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
		var style = defaults.images[img];
		var symbol = new PictureMarkerSymbol(style.path,style.width,style.height);
		symbol.setOffset(style.xOffset,style.yOffset);
		return symbol;
	}
	
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
		if(x > -180 && x < 180 && y > -90 && y < 90){
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