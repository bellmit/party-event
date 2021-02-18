//var graphicArray=new Array();//存放每次查询时获取的Graphic对象，用于在下拉单选中对应Graphic

function loadModule(gmap,proxy){
	var defaults=gmap.getDefaults();
	
	if(defaults.isUseProxy==true){
		esriConfig.defaults.io.proxyUrl = proxy;
		esriConfig.defaults.io.alwaysUseProxy = true;
		esri.config.defaults.io.corsDetection = false;
	}
	
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
gmap.doGraphicQuery = function(url, callBack, geometry, spatialRelationship, objectIds, outFields, condition) {
	
	require(["esri/tasks/query"],function(Value){Query = Value;});
	require(["esri/tasks/QueryTask"],function(Value){QueryTask = Value;});
	defaults = gmap.getDefaults();
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
//	queryTask.executeForIds(query, callBack, function(){})
	queryTask.execute(query, callBack, function(){});
};

gmap.doGraphicQueryReturnIds = function(url, callBack, geometry, spatialRelationship, objectIds, outFields, condition) {
	require(["esri/tasks/query"],function(Value){Query = Value;});
	require(["esri/tasks/QueryTask"],function(Value){QueryTask = Value;});
	defaults = gmap.getDefaults();
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
	queryTask.executeForIds(query, callBack, function(){});
}

gmap.queryPointByCircleCallBack=function(){
	return function(result) {
		console.info(result);
		return result;
	/*	console.info("总个数："+featureSet.features.length);
		for(var i = 0; i < featureSet.features.length; i++) {
			var graphic = featureSet.features[i];
			console.info(graphic);
		}
    */
	}
}

gmap.addPolygonByIdCallBack = function(polygonIDs, objids,tips, style, clickHandler) {//显示显示面回调
	var geometries = new Array();
	var labels = new Array();
	var graphicArray=new Array();
	return function(featureSet) {
		for(var i = 0; i < featureSet.features.length; i++) {
			var graphic = featureSet.features[i];
			var return_objid;
			if(typeof(graphic.attributes.OBJECTID) == "undefined")
			{
				return_objid=graphic.attributes.OBJECTID_1;
			}
			else
			{
				return_objid=graphic.attributes.OBJECTID;
			}
			var temp_position=-1;
			for(var j=0;j<objids.length;j++)
			{
				if(return_objid==objids[j])
				{
					temp_position=j;
					break;
				}
			}
			if(temp_position!=-1)
			{
				var attributes = {id:polygonIDs[i],objid:objids[j],tip:tips[j]};
				var symbol;
				geometries[i] = graphic.geometry;
				labels[i]=tips[j].name;
				if(style!=null)
				{
				    symbol = gmap.getSimpleFillSymbol(style);
				}
				else
				{
					if(tips[i].color==null)
					{
						style=defaults.polygonDefaultStyle;
						symbol=gmap.getSimpleFillSymbol(style);
					}
					else
					{
						symbol=gmap.getShowAreaSymbol(tips[j].color);
					}
					
				}
				if(defaults.isUseTDT){
					var vrings = graphic.geometry.rings;
					for(var ir=0;ir<vrings.length;ir++){
						var geometry1 = vrings[ir];
						for(var ix=0;ix<geometry1.length;ix++){
							xy = gmap.tdtWebMercatorToGeographic(geometry1[ix][0],geometry1[ix][1]);
							geometry1[ix][0]=xy.x;
							geometry1[ix][1]=xy.y;
						}
						graphic.geometry.rings[ir]=geometry1;
					}
				}
				graphic = new Graphic(graphic.geometry,symbol,attributes,null);
				if(clickHandler!=null)
				{
					graphic.clickHandler = clickHandler;
				}
				var showPolygonGraphicsLayer = gmap.getShowPolygonGraphicsLayer();
				showPolygonGraphicsLayer.add(graphic);
				graphicArray.push(graphic);
				gmap.addAreaCenterPointLocal(graphic.geometry,labels[i]);
				gmap.setGraphicArray(graphicArray);
			}
			
	/*		
			if(geometries.length>0){
				gmap.addAreaCenterPoint(geometries,labels);
			}
	*/
		}
		var extent=graphicsUtils.graphicsExtent(graphicArray);
//		gmap.extent=extent;
		gmap.centerByExtent(extent);
	};
};

gmap.addPolylineByIdCallBack= function(polygonIDs, objids,tips, style, clickHandler){
	var geometries = new Array();
	var labels = new Array();
	var graphicArray=new Array();
	return function(featureSet) {
		for(var i = 0; i < featureSet.features.length; i++){
			var graphic = featureSet.features[i];
			var return_objid=graphic.attributes.OBJECTID;
			
			var temp_position=-1;
			for(var j=0;j<objids.length;j++)
			{
				if(return_objid==objids[j])
				{
					temp_position=j;
					break;
				}
			}
			if(temp_position!=-1){
				var attributes={};
				if(typeof tips[j]!="undefined"){
					attributes=tips[j];
					attributes["OBJECTID"]=objids[j];
				}else{
					attributes["OBJECTID"]=objids[j];
				}
				var symbol;
				geometries[i] = graphic.geometry;
				if(style!=null)
				{
				    symbol = gmap.getSimpleLineSymbol(style);
				}else{
					symbol=gmap.getSimpleLineSymbol(defaults.drawPolylineStyle);
				}
				
				graphic = new Graphic(graphic.geometry,symbol,attributes,null);
				if(clickHandler!=null)
				{
					graphic.clickHandler = clickHandler;
				}
				
				var showPolylineGraphicsLayer = gmap.getShowPolylineGraphicsLayer();
				showPolylineGraphicsLayer.add(graphic);
				graphicArray.push(graphic);
//				gmap.addAreaCenterPointLocal(graphic.geometry,labels[i]);
				gmap.setGraphicArray(graphicArray);
			}
		}
		
		var extent=graphicsUtils.graphicsExtent(graphicArray);
		gmap.centerByExtent(extent);
		
	}
}

gmap.addPointByIdCallBack=function(objids,tips,imgxxs,clickHandler){
	return function(featureSet) {
		for(var i = 0; i < featureSet.features.length; i++) {
			var graphic = featureSet.features[i];
			var return_objid=graphic.attributes.OBJECTID;
			var temp_position=-1;
			for(var j=0;j<objids.length;j++)
			{
				if(return_objid==objids[j])
				{
					temp_position=j;
					break;
				}
			}
			if(temp_position!=-1){
				var x=graphic.attributes.x;
				var y=graphic.attributes.y;
				var tip=tips[j];
				var imgxx=imgxxs[j];
				var img=imgxx.img;
				var hoverImg=imgxx.hoverImg;
				var pointID=gmap.addPoint(x,y,tip,img,hoverImg,clickHandler);
				var pointInfo={"id":pointId,"index":j};
	    		pointArray.push(pointInfo);
			}
			
		}
	};
}

gmap.addPolylineById=function(ids,tips,style,clickHandler){
	defaults = gmap.getDefaults();
	var polylineIDs=new Array();
	var objids="";
	for(var i=0;i<ids.length;i++){
		var polylineID=gmap.getPolylineID();
		polylineIDs.push(polylineID);
		objids+=ids[i];
		if(i<ids.length-1){
			objids+=",";
		}
	}
	gmap.doGraphicQuery(defaults.polylineEditLayerUrl, gmap.addPolylineByIdCallBack(polylineIDs,ids,tips,style,clickHandler), null, null, [objids], null, null);
}

gmap.addPolygonById = function(ids, tips, style, clickHandler, level){
	defaults = gmap.getDefaults();
	var polygonIDs=new Array();
	var objids="";
	for(var i=0;i<ids.length;i++)
	{
		var polygonID = gmap.getPolygonID();
		polygonIDs.push(polygonID);
		objids+=ids[i];
		if(i<ids.length-1)
		{
			objids+=",";
		}
	}
	
	var url;
	if(level==1)
	{
		url=defaults.queryCityAreaURL;
	}
	else if(level==2)
	{
		url=defaults.queryDistrictAreaURL;
	}
	else if(level==3)
	{
		url=defaults.queryStreetAreaURL;
	}
	else if(level==4)
	{
		url=defaults.queryCommunityAreaURL;
	}
	else if(level==5){
		url=defaults.queryMaProURL;
	}
	else
	{
		url="";
	}
	gmap.doGraphicQuery(url, gmap.addPolygonByIdCallBack(polygonIDs,ids,tips,style,clickHandler), null, null, [objids], null, null);
	return polygonIDs;
};

gmap.addPointById=function(ids,tips,imgxxs,clickHandler){
	defaults = gmap.getDefaults();
	
	var objids="";
	for(var i=0;i<ids.length;i++)
	{
		var pointID = gmap.getPointID();
		
		objids+=ids[i];
		if(i<ids.length-1)
		{
			objids+=",";
		}
	}
	gmap.doGraphicQuery(defaults.pointQueryUrl,gmap.addPointByIdCallBack(ids,tips,imgxxs,clickHandler),null, null, [objids], null, null)
};

gmap.queryPointByCircle=function(x,y,radius,tip,style){
	var graphic=gmap.addCircleForQuery(x,y,radius,tip,style);
	var geometry=graphic.geometry;
	gmap.doGraphicQueryReturnIds(defaults.pointQueryUrl,gmap.queryPointByCircleCallBack(),geometry,"esriSpatialRelContains",null,null,null);
//	gmap.doGraphicQueryReturnIds(defaults.pointQueryUrl,gmap.queryPointByCircleCallBack(),geometry,"esriSpatialRelIntersects",null,null,null);
}

gmap.queryPointByGraphicInEditLayer=function(pointUrl,callBack){
	var graphics=gmap.getEditGraphicsLayerGraphics();
	if(graphics.length==0){
		return;
	}else{
		var geometry=graphics[0].geometry;
		gmap.doGraphicQueryReturnIds(pointUrl,callBack,geometry,"esriSpatialRelContains",null,null,null);
	}
}

}






