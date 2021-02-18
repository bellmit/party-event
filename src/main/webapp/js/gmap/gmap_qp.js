/**
 * User: liyh
 * Date: 14-6-3
 * Version: 1.3
 */
var sunsharing = new Object();
sunsharing.gmap = function(options){
	esriConfig.defaults.io.proxyUrl=options.proxy;
	esriConfig.defaults.io.alwaysUseProxy=true;
	esri.config.defaults.io.corsDetection=false;

//--------------------依赖包Start--------------------//
	var gmap = this;	
	require(["esri/map"],function(Value){Map = Value;});
	require(["esri/graphic"],function(Value){Graphic = Value;});
	require(["esri/InfoTemplate"],function(Value){InfoTemplate = Value;});
	require(["esri/SpatialReference"],function(Value){SpatialReference = Value;});
	require(["esri/graphicsUtils"],function(Value){graphicsUtils = Value;});
	require(["esri/geometry/Point"],function(Value){Point = Value;});
	require(["esri/geometry/Polygon"],function(Value){Polygon = Value;});
	require(["esri/geometry/Extent"],function(Value){Extent = Value;});
	require(["esri/geometry/webMercatorUtils"], function(Value) {WebMercatorUtils = Value;});
	require(["esri/symbols/PictureMarkerSymbol"],function(Value){PictureMarkerSymbol = Value;});
	require(["esri/symbols/SimpleLineSymbol"],function(Value){SimpleLineSymbol = Value;});
	require(["esri/symbols/SimpleFillSymbol"],function(Value){SimpleFillSymbol = Value;});
	require(["esri/symbols/Font"],function(Value){Font = Value;});
	require(["esri/symbols/TextSymbol"],function(Value){TextSymbol = Value;});
	require(["esri/tasks/query"],function(Value){Query = Value;});
	require(["esri/tasks/QueryTask"],function(Value){QueryTask = Value;});
	require(["esri/tasks/BufferParameters"],function(Value){BufferParameters = Value;});
	require(["esri/tasks/GeometryService"],function(Value){GeometryService = Value;});
	require(["esri/toolbars/draw"],function(Value){Draw = Value;});
	require(["esri/toolbars/edit"],function(Value){Edit = Value;});
	require(["esri/layers/GraphicsLayer"],function(Value){GraphicsLayer = Value;});
	require(["esri/layers/FeatureLayer"],function(Value){FeatureLayer = Value;});
	require(["dojo/colors"],function(Value){Color = Value;});
	require(["esri/layers/ArcGISTiledMapServiceLayer"],function(Value){ArcGISTiledMapServiceLayer = Value;});
	
	
//--------------------依赖包End--------------------//

//--------------------接口方法Start--------------------//
	
	this.tabMap = function(index){//切换底图
		if(defaults.isUseJmLocalLayer == "1")
		{
			jtNormalLayer.hide();
			jtMapLayer.hide();
			roadNetworkLayer.hide();
			switch (index) {
			  case 0:
				  jtNormalLayer.show();
				  break;
			  case 1:
				  jtMapLayer.show();
				  roadNetworkLayer.show();
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
	
	this.registerImages = function(images){//注册图片
		for(var i in images){
			defaults.images[images[i].img] = images[i].style;
		}
	};
	
	this.addPoint = function(x,y,tip,img,hoverImg,clickHandler){//添加一个点到地图
		var xy = pointGeographicToWebMercator(x,y);
		x = xy.x;
		y = xy.y;
		var point = new Point(x,y,map.spatialReference);
		var symbol = getPictureMarkerSymbol(img);
		var attributes = {id:pointID,img:img,hoverImg:hoverImg,tip:tip};
		var graphic = new Graphic(point,symbol,attributes,null);
		graphic.clickHandler = clickHandler;
		graphic.isLockImg = false;
		markPointGraphicsLayer.add(graphic);
		return pointID++;
	};//return id;
	
	this.clearPoints = function(){//清除所有的标注点
		markPointGraphicsLayer.clear();
	};
	
	this.removePoint = function(id){//移除某个ID的标注点
		for(var i in markPointGraphicsLayer.graphics){
			var graphic = markPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				markPointGraphicsLayer.remove(graphic);
				break;
			}
		}
	};
	
	this.changePointToImg = function(id){//改变点的图标为Img
		for(var i in markPointGraphicsLayer.graphics){
			var graphic = markPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				var symbol = getPictureMarkerSymbol(graphic.attributes.img);
				graphic.setSymbol(symbol);
				graphic.isLockImg = false;
				break;
			}
		}
	};
	
	this.changePointToHoverImg = function(id){//改变点的图标为hoverImg
		for(var i in markPointGraphicsLayer.graphics){
			var graphic = markPointGraphicsLayer.graphics[i];
			if(graphic.attributes.id == id){
				var symbol = getPictureMarkerSymbol(graphic.attributes.hoverImg);
				graphic.setSymbol(symbol);
				graphic.isLockImg = true;
				break;
			}
		}
	};
	
	/**
	 * 显示辅助面
	 * list 辅助面的列表（objectid:图形id）
	 * exemple：
	 * 	[{OBJECTID:5},……]
	 */
	this.showRefArea = function(list){
		var ids = new Array();
		for(var i=0;i<list.length;i++){
			ids[i] = list[i].OBJECTID;
		}
		if(ids.length>0)
			doGraphicQuery(defaults.areaURL, showRefAreaHandler(), null, null, ids,	null, null);
	};
	
	/**
	 * 显示辅助面
	 * obj 辅助面的数据
	 * exemple:
	 * {
	 * 	level:0,
	 * 	data:[{XZQH:350211123123},..]
	 * }
	 */
	this.showRefAreaByXzqh = function(obj){
		var url = getXzqhUrl(obj.level);
		var list = obj.data;
		var condition = "";
		if(list.length > 0){
			for(var i=0;i<list.length;i++){
				var tmp = "'MZ" + list[i].XZQH + "0000'";
				if(i>0){
					tmp = "," + tmp;
				}
				condition = condition + tmp;
			}
			condition = "DYWGBM IN ("+condition+")";
			doGraphicQuery(url, showRefAreaHandler(), null, null, null,	null, condition);
		}
	};
	
	/**
	 * 显示显示面
	 * list 辅助面的列表（objectid:图形id；color：颜色）
	 * exemple：
	 * 	[{OBJECTID:5,color:"#FF0000"},……]
	 * showLabel 是否显示文字
	 */
	this.showShowArea = function(list,showLabel){
		var ids = new Array();
		var infos = new Array();
		for(var i=0;i<list.length;i++){
			var id = list[i].OBJECTID;
			ids[i] = id;
			var info = {
					color: list[i].color,
					label: list[i].label
			};
			infos[id] = info;
		}
		if(ids.length>0)
			doGraphicQuery(defaults.areaURL, showShowAreaHandler(infos,null,showLabel), null, null, ids, null, null);
	};
	
	/**
	 * 显示显示面
	 * obj 现实面的数据
	 * exemple:
	 * {
	 * 	level:0,
	 * 	data:[{XZQH:350211123123，color:'#FF0000'},..]	
	 * }
	 * showLabel 是否显示文字
	 */
	this.showShowAreaByXzqh = function(obj,showLabel){
		var url = getXzqhUrl(obj.level);
		var list = obj.data;
		var infos = new Array();
		var condition = "";
		if(list.length > 0){
			for(var i=0;i<list.length;i++){
				var tmp = "'MZ" + list[i].XZQH + "0000'";
				if(i>0){
					tmp = "," + tmp;
				}
				condition = condition + tmp;
				var info = {
						color: list[i].color,
						label: list[i].label
				};
				infos["MZ" + list[i].XZQH + "0000"] = info;
				
			}
			condition = "DYWGBM IN ("+condition+")";
			doGraphicQuery(url, showShowAreaHandler(infos,obj.level,showLabel), null, null, null, null, condition);
		}
	};
	
	function showRefAreaHandler() {//显示辅助面回调
		return function(featureSet) {
			for ( var i = 0; i < featureSet.features.length; i++) {
				var graphic = featureSet.features[i];
				var symbol = getRefAreaSymbol();
				graphic.setSymbol(symbol);
				referenceGraphicsLayer.add(graphic);
			}
		};
	}
	
	function showShowAreaHandler(infos,level,showLabel) {//显示显示面回调
		return function(featureSet) {
			var geometries = new Array();
			var labels = new Array();
			for ( var i = 0; i < featureSet.features.length; i++) {
				var graphic = featureSet.features[i];
				geometries[i] = graphic.geometry;
				var symbol;
				if(level == null){
					symbol = getShowAreaSymbol(infos[graphic.attributes.OBJECTID].color);
					labels[i] = infos[graphic.attributes.OBJECTID].label;
				}else{
					symbol = getShowAreaSymbol(infos[graphic.attributes.DYWGBM].color);
					labels[i] = infos[graphic.attributes.DYWGBM].label;
				}
				graphic.setSymbol(symbol);
				showAreaGraphicsLayer.add(graphic);
			}
			if(showLabel!=null && showLabel == true && geometries.length>0){
				geometryService.labelPoints(geometries, labelLocateHandler(labels), errBack);
			}
			gmap.locateToLayer("showArea");
		};
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
	
	function getRefAreaSymbol(){//获取辅助面样式
		var style = defaults.refAreaStyle;
		var line = style.line;
		var lineSymbol = new SimpleLineSymbol(getLineStyle(line.style),new Color(line.color),line.width);
		var fill = style.fill;
		var simpleFillSymbol = new SimpleFillSymbol(getFillStyle(fill.style),lineSymbol,new Color(fill.color));
		return simpleFillSymbol;
	}
	
	function getShowAreaSymbol(color){//获取显示面样式
		var style = defaults.showAreaStyle;
		var line = style.line;
		var lineSymbol = new SimpleLineSymbol(getLineStyle(line.style),new Color(line.color),line.width);
		var fill = style.fill;
		var simpleFillSymbol = new SimpleFillSymbol(getFillStyle(fill.style),lineSymbol,getColor(color,fill.color[3]));
		return simpleFillSymbol;
	}
	
	function getTextSymbol(label){//获取字体样式
		return new TextSymbol(label, new Font("12px", Font.STYLE_NORMAL,Font.VARIANT_NORMAL, Font.STYLE_NORMAL),new Color([ 0, 0, 0 ]));
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
	
	this.centerAtPoint = function(x,y,level){//定位至坐标点
		if(!isNull(level)){
			map.setLevel(level);
		}
		if(!isNull(x) && !isNull(y)){
			if(x > -180 && x < 180 && y > -90 && y < 90){
				var xy = gmap.geographicToWebMercator(x,y);
				x = xy.x;
				y = xy.y;
			}
			var point = new Point(x, y, map.spatialReference);
			map.centerAt(point);
		}
	};
	
	this.locateToDefualt = function(){//定位至默认位置
		this.centerAtPoint(
				defaults.defaultCenter.x,
				defaults.defaultCenter.y,
				defaults.defaultLevel);
	};
	
	/**
	 * 定位至图层
	 * key 图层关键字(refArea:辅助层；showArea：显示层；markPoint：标记点层；label：文字层；edit：编辑层)
	 * exemple:
	 * 	"refArea"
	 */
	this.locateToLayer = function(key){
		var layer = referenceGraphicsLayer;
		switch (key) {
		case "refArea":
			layer = referenceGraphicsLayer;
			break;
		case "showArea":
			layer = showAreaGraphicsLayer;
			break;
		case "markPoint":
			layer = markPointGraphicsLayer;
			break;
		case "label":
			layer = labelGraphicsLayer;
			break;
		case "edit":
			layer = editGraphicsLayer;
			break;
		default:
			break;
		}
		var graphics = layer.graphics;
		if(graphics.length>0){
			map.setExtent(graphicsExtent(graphics),true);
		}
	};

	function graphicsExtent(d){//获取定位数据
		var a=d[0].geometry,b=a.getExtent(),c,e,g=d.length;
		null===b&&(b=new esri.geometry.Extent(a.x,a.y,a.x,a.y,a.spatialReference));
		for(e=1;e<g;e++)
			c=(a=d[e].geometry).getExtent(),
			null===c&&(c=new esri.geometry.Extent(a.x,a.y,a.x,a.y,a.spatialReference)),
			b=b.union(c);
		return 0>=b.getWidth()&&0>=b.getHeight()?null:b;
	}
	
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
	var drawTool;//画图工具
	var drawToolState;//画图工具的状态

	var googleNormalLayer;
	var googleMapLayer;
	var googleLabelLayer;
	var jtNormalLayer;//精图矢量图图层
	var jtMapLayer;//精图卫星图图层
	var roadNetworkLayer;//路网图层

	var map;// 地图
	var selectGraphic;//选中图形

	var referenceGraphicsLayer;// 辅助层
	var showAreaGraphicsLayer;// 划片显示层
	var markPointGraphicsLayer;// 标记点显示层
	var labelGraphicsLayer;// 文字显示层
	var editGraphicsLayer;// 编辑层
	
	var pointID = 0;//标注点ID
	var polygonID = 0;//多边形ID
	
	var defaults = {//默认参数
			div:'map',//地图所在的div的ID
			defaultCenter:{x:'1.3149074063320074E7',y:'2813360.771119381'},//默认中心
			defaultLevel:'11',//默认地图等级
			wkid:'102113',//坐标系类型
			normalLayerUrl:'http://emap2.mapabc.com/mapabc/maptile?x=<x>&y=<y>&z=<z>',//矢量图地址
			mapLayerUrl:'http://webst04.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=6&x=<x>&y=<y>&z=<z>',//卫星图地址
			markPointImg:{img:'dingwei',hoverImg:'dingwei'},//标注点显示图片
			showAreaStyle:{
				line:{
					style:'dash',
					color:[0,255,0],
					width:2
				},
				fill:{
					styl:'cross',
					color:[255,0,0,0.3]
				}
			},//显示面样式
			refAreaStyle:{
				line:{
					style:'dash',
					color:[0,255,0],
					width:2
				},
				fill:{
					styl:'cross',
					color:[255,0,0,0.2]
				}
			},//辅助面样式
			pointClick:function(id,x,y){},//标注点点击回调
			polygonClick:function(id,rings){},//多边形点击回调
			drawPointImg:'dingwei',//画标注点默认图片
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
			initComplete:function(){},//加载完成后回调
			geometryServiceURL:'http://192.168.0.49:60000/ArcGIS/rest/services/Geometry/GeometryServer',//几何服务URL
			areaURL:'http://192.168.0.49:60000/ArcGIS/rest/services/CQ-MIAN/FeatureServer/0',//面的服务器URL
			pointURL:'http://192.168.0.49:60000/ArcGIS/rest/services/point_csmp/FeatureServer/0',//点的服务器URL
			queryCityAreaURL:"http://192.168.0.49:60000/ArcGIS/rest/services/xzqh/FeatureServer/1",
			queryDistrictAreaURL:"http://192.168.0.49:60000/ArcGIS/rest/services/xzqh/FeatureServer/2",
			queryStreetAreaURL:"http://192.168.0.49:60000/ArcGIS/rest/services/xzqh/FeatureServer/3",
			queryCommunityAreaURL:"http://192.168.0.49:60000/ArcGIS/rest/services/xzqh/FeatureServer/4",
			jmAreaLayerUrl:"http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer",//精图卫星图地址
			jtNormalLayerUrl:"http://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer",//精图矢量图地址
			roadNetworkLayerUrl:"http://mt0.google.cn/vt/imgtp=png32&lyrs=h@146&hl=zh-CN&gl=cn&x=<x>&y=<y>&z=<z>&s=Galil",//路网图地址
			isUseJmLocalLayer:'1'//是否使用精图图层,1是0不是
	};
	
	initMap(options);
	function initMap(options){//初始化地图
		copyOptions(options);
		
		initButton();
		
		map = Map(defaults.div,{logo:false});
		map.spatialReference = new SpatialReference({wkid : defaults.wkid});
		loadCustomLayer();
		initLayer();
		gmap.locateToDefualt();
		initTool();
		window.setTimeout(function(){defaults.initComplete();},500);
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
	
	function loadCustomLayer(){//加载自定义底图
		loadCustomTiledMapLayer();
		loadGoogleLabelLayer();
		googleNormalLayer = new CustomTiledMapLayer(defaults.normalLayerUrl);
		googleMapLayer = new CustomTiledMapLayer(defaults.mapLayerUrl);	
		googleLabelLayer = new GoogleLabelLayer();
		roadNetworkLayer=new CustomTiledMapLayer(defaults.roadNetworkLayerUrl);	
		googleMapLayer.visible = false;
		googleLabelLayer.visible = false;
		roadNetworkLayer.visible=false
		if(defaults.isUseJmLocalLayer == "1"){
			console.info("精图卫星图地址:"+defaults.jmAreaLayerUrl);
			jtMapLayer= new esri.layers.ArcGISTiledMapServiceLayer(defaults.jmAreaLayerUrl);
			googleNormalLayer.visible = false;
			jtMapLayer.visible=false;
			dojo.connect(jtMapLayer, "onLoad", function(){
				map.addLayer(jtMapLayer);
			});
	/*		
			var alayer = new esri.layers.ArcGISTiledMapServiceLayer(defaults.jmAreaLayerUrl);
			googleNormalLayer.visible = false;
			dojo.connect(alayer, "onLoad", function(){
				map.addLayer(alayer);
			});
	*/		
			console.info("精图矢量图地址:"+defaults.jtNormalLayerUrl);
			jtNormalLayer=new esri.layers.ArcGISTiledMapServiceLayer(defaults.jtNormalLayerUrl);
			googleNormalLayer.visible = false;
			dojo.connect(jtNormalLayer, "onLoad", function(){
				map.addLayer(jtNormalLayer);
			});

		}
		map.addLayer(googleNormalLayer);
		map.addLayer(googleMapLayer);
		map.addLayer(roadNetworkLayer);
		map.addLayer(googleLabelLayer);
		geometryService = new GeometryService(defaults.geometryServiceURL);
	}
	
	
	function initLayer(){//初始化图层
		referenceGraphicsLayer = new GraphicsLayer();
		showAreaGraphicsLayer = new GraphicsLayer();
		labelGraphicsLayer = new GraphicsLayer();
		markPointGraphicsLayer = new GraphicsLayer();
		editGraphicsLayer = new GraphicsLayer();
		
		dojo.connect(markPointGraphicsLayer,"onClick",pointClick);
		dojo.connect(markPointGraphicsLayer,"onMouseOver",onMouseOverHandler);
		dojo.connect(markPointGraphicsLayer, "onMouseOut",onMouseOutHandler);
		
		map.addLayer(referenceGraphicsLayer);
		map.addLayer(showAreaGraphicsLayer);
		map.addLayer(labelGraphicsLayer);
		map.addLayer(markPointGraphicsLayer);
		map.addLayer(editGraphicsLayer);
	}
	
	function initTool(){//初始化工具
		drawTool = new Draw(map);
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
	
//--------------------初始化End--------------------//
	
	/**
	 * 查询 
	 * url 查询地址 
	 * callBack 查询回调 
	 * geometry 查询的图形 
	 * spatialRelationship 几何关系 
	 * objectids 被查询的id 
	 * outFields 输出的属性 
	 * condition 查询条件
	 */
	function doGraphicQuery(url, callBack, geometry, spatialRelationship,objectIds, outFields, condition) {
		var queryTask = new QueryTask(url);
		var query = new Query();
		if (geometry != null) {
			query.geometry = geometry;
		}
		if (outFields != null) {
			query.outFields = outFields;
		} else {
			query.outFields = [ "*" ];
		}
		if (objectIds != null) {
			query.objectIds = objectIds;
		}
		if (condition != null && condition != "") {
			query.where = condition;
		} else {
			query.where = "1=1";
		}
		if (spatialRelationship != null) {
			query.spatialRelationship = spatialRelationship;
		}
		query.outSpatialReference = new SpatialReference({wkid : defaults.wkid});//map.spatialReference;
		query.returnGeometry = true;
		queryTask.execute(query, callBack, errBack);
	}

	function copyOptions(options){//复制参数
		for(var item in defaults){
			if(options[item] == undefined){
			}else{
				defaults[item] = options[item];
			}
		}
	}
	
	function errBack(err) {//错误回调
	//	alert(err);
		// alert("地图服务器繁忙");
	}
	
	function callBack(){
		alert("默认回调");
	}

	function isNull(arg){//判断是否为空
		return arg == ''?true:false;
	}

	function getLineStyle(style){//获取线的Style
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
		
	function getFillStyle(style){//获取填充的Style
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
	
	function getXzqhUrl(level){//获取查询地址
		var url = "";
		switch (level) {
		case 0:
			url = defaults.queryCityAreaURL;
			break;
		case 1:
			url = defaults.queryDistrictAreaURL;
			break;
		case 2:
			url = defaults.queryStreetAreaURL;
			break;
		case 3:
			url = defaults.queryCommunityAreaURL;
			break;

		default:
			url = defaults.queryCityAreaURL;
			break;
		}
		return url;
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
	
	function getPictureMarkerSymbol(img){
		var style = defaults.images[img];
		var symbol = new PictureMarkerSymbol(style.path,style.width,style.height);
		symbol.setOffset(style.xOffset,style.yOffset);
		return symbol;
	}
	
};

/***几何查询方案***/
/**esriSpatialRelUndefined 未定义**/
/**esriSpatialRelIntersects A与B图形相交**/
/**esriSpatialRelEnvelopeIntersects A的Envelope和B的Envelope相交**/
/**esriSpatialRelIndexIntersects A与B索引相交 **/
/**esriSpatialRelTouches A与B边界相接**/
/**esriSpatialRelOverlaps A与B相叠加**/
/**esriSpatialRelCrosses A与B相交（两条线相交于一点，一条线和一个面相交）**/
/**esriSpatialRelWithin A在B内**/
/**esriSpatialRelContains A包含B**/
/**esriSpatialRelRelation A与B空间关联**/