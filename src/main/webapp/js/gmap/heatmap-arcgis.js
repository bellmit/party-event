dojo.addOnLoad(function () {
    dojo.declare("HeatmapLayer", [esri.layers.DynamicMapServiceLayer], {
        /*
	{
		map: <a handle to the map>,
		domNodeId: <an id to the domNode>,
	}
	*/
        // variables
        properties: {},
        heatMapData: {max:0,data:[]},
        heatMap: null,
        // constructor
        constructor: function (properties) {
            dojo.safeMixin(this.properties, properties);
            // map var
            this._map = this.properties.map;
            // last data storage
            this.lastData = [];
            // map node
            this.domNode = document.getElementById(this.properties.domNodeId);
            // config
            this.config = {
                element: this.domNode,
                width: this._map.width,
                height: this._map.height,
                radius: 40,
                debug: false,
                visible: true,
                useLocalMaximum: false,
                gradient: {
                    0.45: "rgb(000,000,255)",
                    0.55: "rgb(000,255,255)",
                    0.65: "rgb(000,255,000)",
                    0.95: "rgb(255,255,000)",
                    1.00: "rgb(255,000,000)"
                }
            };
            // mix in config for heatmap.js settings
            dojo.safeMixin(this.config, properties.config);
            // create heatmap
            this.heatMap = heatmapFactory.create(this.config);
            // loaded
            this.loaded = true;
            this.onLoad(this);
            // global maximum value
            this.globalMax = 0;
            if(properties.globalMax){
            	this.globalMax=properties.globalMax;
            }
            // connect on resize
            dojo.connect(this._map, "onResize", this, this.resizeHeatmap);
            // heatlayer div styling
            this.domNode.style.position = 'relative';
            this.domNode.style.display = 'none';
        },
        resizeHeatmap: function (extent, width, height) {
            // set heatmap data size
            this.heatMap.set("width", width);
            this.heatMap.set("height", height);
            // set width and height of container
            dojo.style(this.domNode, {
                "width": width + 'px',
                "height": height + 'px'
            });
            // set width and height of canvas element inside of container
            var child = dojo.query(':first-child', this.domNode);
            if (child) {
                child.attr('width', width);
                child.attr('height', height);
            }
            // set atx canvas width and height fix
            var actx = this.heatMap.get("actx");
            actx.canvas.height = height;
            actx.canvas.width = width;
            this.heatMap.set("actx", actx);
            // refresh image and heat map size
            this.refresh();
        },
        storeAddData : function (){
//    		setTimeout(function() {
// 				heatLayer.addDataPoint(heatData1);
// 			}, 1500);
//        	
        },
        // stores heatmap converted data into the plugin which renders it
        storeHeatmapAddPointData: function (heatPluginData) {
        	// set heatmap data
        	this.heatMapData.data.push({x:heatPluginData.x,y:heatPluginData.y,count:heatPluginData.count});
        },
        // stores heatmap converted data into the plugin which renders it
        storeHeatmapData: function (heatPluginData) {
            // set heatmap data
            this.heatMap.store.setDataSet(heatPluginData);
        },
        // converts parsed data into heatmap format
        convertHeatmapData: function (parsedData) {
            // variables
            var xParsed, yParsed, heatPluginData, dataPoint, screenGeometry,hmcount=0;
            // set heat plugin data object
            heatPluginData = {
                max: parsedData.max,
                data: [] // empty data
            };
            // if data
            if (parsedData.data) {
                // for all x values
                for (xParsed in parsedData.data) {
                    // if data[x]
                    if (parsedData.data.hasOwnProperty(xParsed)) {
                        // for all y values and count
                        for (yParsed in parsedData.data[xParsed]) {
                            if (parsedData.data[xParsed].hasOwnProperty(yParsed)) {
                            	// convert data point into screen geometry
                            	screenGeometry = esri.geometry.toScreenGeometry(this._map.extent, this._map.width, this._map.height, parsedData.data[xParsed][yParsed].dataPoint);
//                            	if(hmcount>2000){
//                            		storeHeatmapAddPointData({
//	                                    x: screenGeometry.x,
//	                                    y: screenGeometry.y,
//	                                    count: parsedData.data[xParsed][yParsed].count // count value of x,y
//	                                });
//                            	}else{
	                                // push to heatmap plugin data array
	                                heatPluginData.data.push({
	                                    x: screenGeometry.x,
	                                    y: screenGeometry.y,
	                                    count: parsedData.data[xParsed][yParsed].count // count value of x,y
	                                });
//                            	}
//                                hmcount++;
                            }
                        }
                    }
                }
            }
            // store in heatmap plugin which will render it
            this.storeHeatmapData(heatPluginData);
        },
        // runs through data and calulates weights and max
        parseHeatmapData: function (features) {
            // variables
            var i, parsedData, dataPoint, attributes,dataArr=[],mapLevel=0;//,isFlag=false,drx=0,dry=0,mapCount=0,index=0,maxNum=0;
            // if data points exist
            if (features) {
                // create parsed data object
                parsedData = {
                    max: 0,
                    data: []
                };
                if (!this.config.useLocalMaximum) {
                    parsedData.max = this.globalMax;
                }
                if($("#map")){
                	mapLevel = $("#map").attr("data-zoom");
                }
                if(mapLevel<14 && !heatmapcode){
                	features = heatmapQUlist;
                }
                // for each data point
                var flen = features.length;
                var defaults=gmap.getDefaults();
                for (i = 0; i < flen; i++) {
                    // create geometry point
                    dataPoint = esri.geometry.Point(features[i].geometry);
                    xy = gmap.tdtWebMercatorToGeographic(dataPoint.x,dataPoint.y);
                	dataPoint.spatialReference.wkid = defaults.wkid;
                	dataPoint.x = xy.x;
                	dataPoint.y = xy.y;
                    // check point
                    var validPoint = false;
                    // if not using local max, point is valid
                    if (!this.config.useLocalMaximum) {
                        validPoint = true;
                    }
                    // using local max, make sure point is within extent
                    else if(this._map.extent.contains(dataPoint)){
                        validPoint = true;
                    }
                    if (validPoint) {
                        // attributes
                        attributes = features[i].attributes;
                        if(dataArr.length<101)
                        	dataArr.push({title:attributes.title,count:attributes.count});
                       /* c = parseInt($.trim(attributes.count));
                        if(maxNum==undefined){
//                        	maxNum = minNum = c;
                        	maxNum = c;
                        }else{
	                        if(c>maxNum){
	                        	maxNum = c;
	                        }
//	                        if(c<minNum){
//	                        	minNum = c;
//	                        }
                        }*/
                    	// 小于地图等级14的，增加算法统合数据点的量
                    	/*if(mapLevel<14 && attributes.radius>0){
                    		xx = parseFloat(dataPoint.x);
                    		yy = parseFloat(dataPoint.y);
                    		//初始化第一个点位置范围
                    		drx = drx==0?xx:drx;
                    		dry = dry==0?yy:dry;
                    		//如果当前坐标减去范围值还大于初始值，那么表示该坐标已经超出了初始值所在范围，就进行描点
                			if(xx-attributes.radius > drx || yy-attributes.radius>dry
                					||index==0){
                				isFlag = false;
                				if (parsedData.data[drx] && parsedData.data[drx][dry]){
                					parsedData.data[drx][dry].count+=mapCount;
                				}
                				mapCount=0;
                				if(index>0){
                					drx=xx;
                					dry=yy;//重置
                				}
                				index=1;
                			}else{
                				isFlag = true;
                				mapCount+=attributes.count;
                				index++;
                			}
                    		if(isFlag){
                    			ignore++;
                    			continue;
                    		}
                    	}*/
                        // if array value is undefined
                        if (!parsedData.data[dataPoint.x]) {
                            // create empty array value
                            parsedData.data[dataPoint.x] = [];
                        }
                        // array value array is undefined
                        if (!parsedData.data[dataPoint.x][dataPoint.y]) {
                            // create object in array
                            parsedData.data[dataPoint.x][dataPoint.y] = {};
                            // if count is defined in datapoint
                            if (attributes && attributes.hasOwnProperty('count')) {
                                // create array value with count of count set in datapoint
                                parsedData.data[dataPoint.x][dataPoint.y].count = attributes.count;
                            } else {
                                // create array value with count of 0
                                parsedData.data[dataPoint.x][dataPoint.y].count = 0;
                            }
                        }
                        // add 1 to the count
                        parsedData.data[dataPoint.x][dataPoint.y].count += 1;
                        // store dataPoint var
                        parsedData.data[dataPoint.x][dataPoint.y].dataPoint = dataPoint;
                        // if count is greater than current max
                        if (parsedData.max < parsedData.data[dataPoint.x][dataPoint.y].count) {
                            // set max to this count
                            parsedData.max = parsedData.data[dataPoint.x][dataPoint.y].count;
                            if (!this.config.useLocalMaximum) {
                                this.globalMax = parsedData.data[dataPoint.x][dataPoint.y].count;
                            }
                        }
                        /*acount++;*/
                    }
                }
                if(typeof setHeatMapBotton == 'function')// 调用热力图jsp页面方法进行回调
                	setHeatMapBotton(parsedData.max,mapLevel,dataArr);
//                console.info("描点数(剩余/有效/整合/总数):"+acount+"/"+validcount+"/"+ignore+"/"+flen);
                // convert parsed data into heatmap plugin formatted data
                this.convertHeatmapData(parsedData);
            }
        },
        // set data function call
        setData: function (features) {
            // set width/height
            this.resizeHeatmap(null, this._map.width, this._map.height);
            // store points
            this.lastData = features;
            // create data and then store it
            this.parseHeatmapData(features);
            // redraws the heatmap
            this.refresh();
        },
        // add one feature to the heatmap
        addDataPoint: function (feature) {
            if (feature) {
                // push to data
            	for(data in feature)
            		this.lastData.push(data);
                // set data
                this.setData(this.lastData);
            }
        },
        // return data set of features
        exportDataSet: function () {
            return this.lastData;
        },
        // clear data function
        clearData: function () {
            // empty heat map
            this.heatMap.clear();
            // empty array
            var empty = [];
            // set data to empty array
            this.setData(empty);
        },
        // get image
        getImageUrl: function (extent, width, height, callback) {
            // create heatmap data using last data
            this.parseHeatmapData(this.lastData);
            // image data
            var imageUrl = this.heatMap.get("canvas").toDataURL("image/png");
            // callback
            callback(imageUrl);
        }
    });
});