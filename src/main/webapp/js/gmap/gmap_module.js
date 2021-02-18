
function loadModule(gmap,proxy){
	esriConfig.defaults.io.proxyUrl = proxy;
	esriConfig.defaults.io.alwaysUseProxy = true;
	esri.config.defaults.io.corsDetection = false;
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
	queryTask.execute(query, callBack, function(){});
};

gmap.addPolygonByIdCallBack = function(polygonID, tip, style, clickHandler) {//显示显示面回调
	return function(featureSet) {
		for(var i = 0; i < featureSet.features.length; i++) {
			var graphic = featureSet.features[i];
			var attributes = {id:polygonID,tip:tip};
			var symbol = gmap.getSimpleFillSymbol(style);
			graphic = new Graphic(graphic.geometry,symbol,attributes,null);
			graphic.clickHandler = clickHandler;
			var showPolygonGraphicsLayer = gmap.getShowPolygonGraphicsLayer();
			showPolygonGraphicsLayer.add(graphic);
		}
	};
};

gmap.addPolygonById = function(id, tip, style, clickHandler, url){
	var polygonID = gmap.getPolygonID();
	gmap.doGraphicQuery(url, gmap.addPolygonByIdCallBack(polygonID,tip,style,clickHandler), null, null, [id], null, null);
	return polygonID;
};
}




