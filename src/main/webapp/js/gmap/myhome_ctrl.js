'use strict';

/**
 * controllers
 * 我的首页 控制器
 * 14-3-14      Enboga      创建文件
 */

//定义控制器
angular.module('legend.controllers.myhome', []).
controller('myhomeDoCtrl', ['$scope', 'doService','$rootScope','$sce','$cookies','$timeout',
    function($scope, doService,$rootScope,$sce,$cookies,$timeout) {
        $scope.title = "待办事项";

        //获取并显示正在加载图片
//        var loading_h = $(".do_loading").height();
//        $(".bubblingG").css("top" , loading_h/2);
//        $(".do_loading").css("display" , "block");
//            if(true){return;}

        //首页获取待办事项列表
        doService.getDos(5, 1, {}, function(data){
            $(".do_loading").hide();
            $scope.items = data.list;
        });

        var doObjTemp = new Object();
        $scope.open = function (doObj){
            if(doObj.url == null|| doObj.url == "" || doObj.url == "#"){
                return;
            }
            doObjTemp = doObj;
//            window.open(doObj.url, "window_"+doObj.appId, "fullscreen=1");
            if(doObj.need_bind_user==true){
                $rootScope.addAlert("请先绑定["+doObj.source+"]的帐号！",'info');
                return;
            }
            //是否拨vpn
            var callVpn = false;
            if(window.SSPB != undefined && window.SSPB.connectVpn != undefined){
                var curVpnName = $cookies.curVpnName;
                if(doObj.vpn != null && doObj.vpn != ""){
                    if(window.SSPB.checkVpnStatus(doObj.vpn) != 0){
                        callVpn = true;
                        $rootScope.loading(true,"正在连接vpn("+doObj.vpn+")...");
                        window.SSPB.connectVpn(doObj.vpn,"afterConnectVpnForDB","");
                        $scope.pollingVpnRtn(doObj);
                    }
                }else if(curVpnName!=null&& curVpnName != "" && window.SSPB.checkVpnStatus(curVpnName) == 0){
                    //断开
                    window.SSPB.disconnectVpn(curVpnName);
                }
            }
            if(!callVpn){
                window.afterConnectVpnForDB(doObj);
            }
        };
        $scope.pollingVpnRtn = function(doObj){
//            console.info(menu);
            var vpnStatus = window.SSPB.checkVpnStatus(doObj.vpn);
//            console.info("vpnStatus:"+vpnStatus);
            if(vpnStatus==0){
                $rootScope.loading(false,null);
                $cookies.curVpnName = doObj.vpn;
                $rootScope.loading(true,"正在为您跳转到"+doObj.source+",请稍后...");
                window.afterConnectVpnForDB(doObj);
//                else{
//                    //连接vpn失败
//                    $rootScope.addAlert('连接vpn失败:'+msg+ms, 'danger');
//                    return;
//                }
            }else if(vpnStatus==2){
                //if(vpnStatus==2){
                $timeout(function(){
                    $scope.pollingVpnRtn(doObj);
                },500);
            }else{
                $rootScope.addAlert('连接vpn失败:'+doObj.vpn, 'danger');
                $rootScope.loading(false,null);
            }
        }
        window.afterConnectVpnForDB = function(doObjTemp){
            $rootScope.loading(false,null);
            if(doObjTemp.openType=="flex"){
                //待办事项，目前只有社区，默认走社区的加载
                $rootScope.$emit("flexIframeUrlChange", doObjTemp.url);
            }else{
                if(doObjTemp.openType == "ie" && window.SSPB != undefined && window.SSPB.openUrlInIE != undefined){
                    //取得访问域url
                    var host = location.href;
                    var i = host.indexOf("/",7);
                    if(i!=-1){
                        host = host.substring(0,i);
                    }
                    window.SSPB.openUrlInIE(host+doObjTemp.url);
                }else if(doObjTemp.openType == "defaultBrowser" && window.SSPB != undefined && window.SSPB.openUrlInDefaultBrowser != undefined){
                    //取得访问域url
                    var host = location.href;
                    var i = host.indexOf("/",7);
                    if(i!=-1){
                        host = host.substring(0,i);
                    }
                    window.SSPB.openUrlInDefaultBrowser(host+doObjTemp.url);
                }else{
                    window.openWindow(doObjTemp.url);
                }

            }

        }
    }]).

//最新代办事项详细页面的列表
controller('myhomeDoListCtrl', ['$scope', 'doService','$rootScope','$sce','$cookies','$timeout',
    function($scope, doService,$rootScope,$sce,$cookies,$timeout) {
        $scope.title = "待办事项";

        $scope.totalItems = 0;
        $scope.currentPage = 1;
        $scope.itemsPerPage = 10;
        $scope.maxSize = 10;

        //获取待办事项列表
        $scope.pageChanged = function(page) {
            //获取并显示正在加载图片
//            var loading_h = $(".do_loading").height();
//            $(".bubblingG").css("top" , loading_h/2);
//            $(".do_loading").css("display" , "block");
//            if(true){return;}

            doService.getDos($scope.itemsPerPage, page, {}, function(data){
                $(".do_loading").hide();
                $scope.items = data.list;
                $scope.totalItems = data.totalNum;
//                alert(data.totalNum);
            });
        };
        $scope.pageChanged($scope.currentPage);

        var doObjTemp = new Object();
        $scope.open = function (doObj){
            if(doObj.url == null|| doObj.url == "" || doObj.url == "#"){
                return;
            }
            doObjTemp = doObj;
//            window.open(doObj.url, "window_"+doObj.appId, "fullscreen=1");
            if(doObj.need_bind_user==true){
                $rootScope.addAlert("请先绑定["+doObj.source+"]的帐号！",'info');
                return;
            }
            //是否拨vpn
            var callVpn = false;
            if(window.SSPB != undefined && window.SSPB.connectVpn != undefined){
                var curVpnName = $cookies.curVpnName;
                if(doObj.vpn != null && doObj.vpn != ""){
                    if(window.SSPB.checkVpnStatus(doObj.vpn) != 0){
                        callVpn = true;
                        $rootScope.loading(true,"正在连接vpn("+doObj.vpn+")...");
                        window.SSPB.connectVpn(doObj.vpn,"afterConnectVpnForDB","");
                        $scope.pollingVpnRtn(doObj);
                    }
                }else if(curVpnName!=null&& curVpnName != "" && window.SSPB.checkVpnStatus(curVpnName) == 0){
                    //断开
                    window.SSPB.disconnectVpn(curVpnName);
                }
            }
            if(!callVpn){
                window.afterConnectVpnForDB(doObj);
            }
        };
        $scope.pollingVpnRtn = function(doObj){
//            console.info(menu);
            var vpnStatus = window.SSPB.checkVpnStatus(doObj.vpn);
            console.info("vpnStatus:"+vpnStatus);
            if(vpnStatus==0){
                $rootScope.loading(false,null);
                $cookies.curVpnName = doObj.vpn;
                $rootScope.loading(true,"正在为您跳转到"+doObj.source+",请稍后...");
                window.afterConnectVpnForDB(doObj);
//                else{
//                    //连接vpn失败
//                    $rootScope.addAlert('连接vpn失败:'+msg+ms, 'danger');
//                    return;
//                }
            }else if(vpnStatus==2){
                $timeout(function(){
                    $scope.pollingVpnRtn(doObj);
                },500);
            }else{
                $rootScope.addAlert('连接vpn失败:'+doObj.vpn, 'danger');
                $rootScope.loading(false,null);
            }
        }
    }]).



//首页最新资讯的列表
controller('myhomeNewsCtrl', ['$scope', 'newsService',
    function($scope, newsService) {
        $scope.title = "最新资讯";


        //获取并显示正在加载图片
//        var loading_h = $(".news_loading").height();
//        $(".bubblingG").css("top" , loading_h/2);
//        $(".news_loading").css("display" , "block");
//                if(true){return;}

        var cs = {};
        //获取最新资讯列表
        $scope.getNewsList = function(datas){
            $scope.news_class1 = "";
            $scope.news_class2 = "";
            $scope.news_class3 = "";

            cs = datas;
            if(cs.news_type == undefined){
                $scope.news_class1 ="active";
            }else if(cs.news_type == "01"){
                $scope.news_class2 ="active";
            }else if(cs.news_type == "02"){
                $scope.news_class3 ="active";
            }

            newsService.getNewsList(5, 1, cs, function(data){
                $(".news_loading").hide();
                $scope.items = data.list;
            });
        };

        $scope.getNewsList({});
    }]).

//最新资讯详细页面的列表
controller('myhomeNewsListCtrl', ['$scope', 'newsService',
    function($scope, newsService) {
        $scope.title = "最新资讯";

        $scope.totalItems = 0;
        $scope.currentPage = 1;
        $scope.itemsPerPage = 10;
        $scope.maxSize = 10;

        var cs = {};
        $scope.getNewsList = function(datas){
            $scope.news_class1 = "";
            $scope.news_class2 = "";
            $scope.news_class3 = "";

            cs = datas;
            if(cs.news_type == undefined){
                $scope.news_class1 ="active";
            }else if(cs.news_type == "01"){
                $scope.news_class2 ="active";
            }else if(cs.news_type == "02"){
                $scope.news_class3 ="active";
            }
            $scope.pageChanged($scope.currentPage);
        }
        //获取最新资讯列表
        $scope.pageChanged = function(page) {
            //获取并显示正在加载图片
//            var loading_h = $(".news_loading").height();
//            $(".bubblingG").css("top" , loading_h/2);
//            $(".news_loading").css("display" , "block");
//            if(true){return;}

            newsService.getNewsList($scope.itemsPerPage, page, cs, function(data){
                $(".news_loading").hide();
                $scope.items = data.list;
                $scope.totalItems = data.totalNum;
            });
        };
//        $scope.pageChanged($scope.currentPage);

        $scope.getNewsList({});
    }]).


controller('myhomeWgqkCtrl', ['$scope', 'wgqkService', '$rootScope','$sce',
    function($scope, wgqkService, $rootScope,$sce) {
        var chartJm,
            chartJy1,
            chartJy2,
            chartSb,
            chartJiuye,
            chartXqdtYz,
            chartXqdtBn,
            chartXqdtQn;

        //初始化地图
        $scope.initMap = function(){
            //如果系统参数没配置默认定位厦门
            var defaultCenter = {x:'1.3149074063320074E7', y:'2813360.771119381'};
            var defaultCenterXY = $rootScope.systeminfo.map_defaultCenterXY;
            if(defaultCenterXY!=undefined && defaultCenterXY.length>0){
                var dcArr = defaultCenterXY.split(",");
                if(dcArr.length>=2){
                    defaultCenter = {x:dcArr[0], y:dcArr[1]};
                }
            }

            $scope.gmap = new sunsharing.gmap({//maps.defaultCenterXY
                proxy:"/"+$rootScope.systeminfo.csmp_context_path+"/proxy.jsp",
                defaultCenter:  defaultCenter,
                showAreaStyle:{
                    line:{
                        style:'solid',
                        color:[100,150,255, 0.5],
                        width:2
                    },
                    fill:{
                        style:'solid',
                        color:[255,0,0,0.3]
                    }
                },//显示面样式
                refAreaStyle:{
                    line:{
                        style:'dash',
                        color:[200,200,255,.7],
                        width:3
                    },
                    fill:{
                        style:'solid',
                        color:[255,0,0,0]
                    }
                },
                geometryServiceURL:     $rootScope.systeminfo.map_geometry_service_URL,//几何服务URL
                areaURL:                $rootScope.systeminfo.map_area_URL,//面的服务器URL
                pointURL:               $rootScope.systeminfo.map_point_URL,//点的服务器URL
                queryCityAreaURL:       $rootScope.systeminfo.queryCityAreaURL,
                queryDistrictAreaURL:   $rootScope.systeminfo.queryDistrictAreaURL,
                queryStreetAreaURL:     $rootScope.systeminfo.queryStreetAreaURL,
                queryCommunityAreaURL:  $rootScope.systeminfo.queryCommunityAreaURL,
                jmAreaLayerUrl:         $rootScope.systeminfo.jmAreaLayerUrl,
                isUseJmLocalLayer:      $rootScope.systeminfo.isUseJmLocalLayer,
                jtNormalLayerUrl :      $rootScope.systeminfo.map_jtNormalUrl,
                roadNetworkLayerUrl:    $rootScope.systeminfo.map_roadNetworkUrl
            });

            //天津演示版本：增加一个监控点，点击可以查看监控
            $scope.gmap.registerImages([
                {
                    "img":"ico_jk",
                    "style":{
                        "path":"sslib/gmap/images/ico_jk.png",
                        "width":18,
                        "height":18,
                        "xOffset":0,
                        "yOffset":0
                    }
                },{
                    "img":"ico_jk_a",
                    "style":{
                        "path":"sslib/gmap/images/ico_jk_a.png",
                        "width":22,
                        "height":22,
                        "xOffset":0,
                        "yOffset":0
                    }
                }
            ]);
            $scope.gmap.addPoint(
                '13060088.615738811',
                '4726811.62316478',
                "监控点",
                'ico_jk',
                'ico_jk_a',
                function (id, x, y){
//                    alert("标注点Id:"+id);
//                    window.open("views/jk/jk.html");
                   // $.
                    $.fancybox({
                        iframe : {
                            scrolling : 'auto',
                            preload   : false
                        },
                        href:"views/jk/jk.html",
                        type:"iframe",
                        height:630,
                        width:900
                    });
                }
            );

            //获取网格情况
//            var loading_h = $(".mapinfo_loading").height();
//            $(".bubblingG").css("top" , loading_h/2);
//            $(".mapinfo_loading").css("display" , "block");

            wgqkService.getWgqk( function(data){
                //获取并显示正在加载图片
//              if(true){return;}
                $(".mapinfo_loading").hide();
                if(data.status){
                    $scope.newsObjWgqk = data.data;
                    $scope.wgqkList = [];
                    if($scope.newsObjWgqk.info != undefined){
                        var j = 0;
                        for(var i=0; i<$scope.newsObjWgqk.info.length; i++){
                            $scope.newsObjWgqk.info[i].label += "："; //添加冒号
                            if(i % 3 == 0){
                                $scope.wgqkList[j] = [];
                                j++;
                            }
                            $scope.wgqkList[j-1][i%3] = $scope.newsObjWgqk.info[i];
                        }
                        var ys=$scope.newsObjWgqk.info.length%3;
                        if(ys !=0){
                            for(var i =ys;i<=2 ;i++){
                                $scope.wgqkList[j-1][i] ={};
                            }
                        }
                    }
                }else{
                    $rootScope.addAlert(data.error, 'info', '获取网格情况数据失败');
                }
            });


            //获取网格情况地图数据
//            var loading_h = $(".map_loading").height();
//            $(".bubblingG").css("top" , loading_h/2);
//            $(".map_loading").css("display" , "block");
            wgqkService.getWgqkMapData( function(data){
                //获取并显示正在加载图片
                $(".map_loading").hide();
                if(data.status){
					//设置一个全局变量
					var JD_USE_JT_MAP = $rootScope.systeminfo.useRemoteUrl;//街道以上使用精图图层
					var getXzqhLevel = function (xzqh){
                        if(xzqh!=undefined){
                            if(xzqh.length == 4){
                                return 0;
                            }else if(xzqh.length == 6){
                                return 1;
                            }else if(xzqh.length == 9){
                                return 2;
                            }else if(xzqh.length == 12){
                                if(xzqh.substr(4) == "00000000"){
                                    return 0;
                                }else if(xzqh.substr(6) == "000000"){
                                    return 1;
                                }else if(xzqh.substr(9) == "000"){
                                    return 2;
                                }
                                return 3;
                            }
                        }

						return -1;
					}
					
                    if(data.data.area.length > 0){
//                        gmap.showArea(data.data.area);
						var xzqh = data.data.area[0].XZQH;
						var level = getXzqhLevel(xzqh);
						if(JD_USE_JT_MAP && level <= 2){
							$scope.gmap.showShowAreaByXzqh(
								{"level":level,"data":data.data.area},true);
						}else{
							$scope.gmap.showShowArea(data.data.area,true);
						}
                    }
                    if(data.data.area_ref!=undefined && data.data.area_ref.length > 0){
						var xzqh = data.data.area_ref[0].XZQH;
						var level = getXzqhLevel(xzqh);
						if(JD_USE_JT_MAP  && level <= 2){
							$scope.gmap.showRefAreaByXzqh(
								{"level":level,"data":data.data.area_ref},true);
						}else{
							$scope.gmap.showRefArea(data.data.area_ref,true);
						}
                    }
                }else{
                    $rootScope.addAlert(data.error, 'info', '获取网格情况地图数据失败');
                }
            });



        $scope.loadingTip = "数据加载中";

        var getAmCharts = function(){
            var chartTemp;
            // SERIAL CHART
            chartTemp = new AmCharts.AmSerialChart();
            chartTemp.startDuration = "1";
            chartTemp.startFrom = "bottom";
            chartTemp.categoryField = "label";
            chartTemp.plotAreaBorderAlpha = 0;

            // AXES
            // category
            var categoryAxis = chartTemp.categoryAxis;
            categoryAxis.gridAlpha = 0.1;
            categoryAxis.gridCount = "999";
            categoryAxis.labelRotation = "45";
            categoryAxis.autoGridCount = false;
            categoryAxis.gridPosition = "start";

            // value
            var valueAxis = new AmCharts.ValueAxis();
            valueAxis.stackType = "regular";
            valueAxis.gridAlpha = 0.1;
//                        valueAxis.axisAlpha = 0;
            chartTemp.addValueAxis(valueAxis);

            // GRAPHS
            // first graph
            var graph = new AmCharts.AmGraph();
            graph.title = "Sample a";
            graph.color = "#FFFFFF";
            graph.valueField = "hj";
            graph.type = "column";
            graph.lineColor = "#008aff";
            graph.fillAlphas = [0.8];
            graph.fillColors = ["#008aff"];
            graph.balloonText = "户籍：[[value]]人";
            chartTemp.addGraph(graph);

            // second graph
            var graph02 = new AmCharts.AmGraph();
            graph02.title = "Sample b";
            graph02.color = "#FFFFFF";
            graph02.valueField = "lk";
            graph02.type = "column";
            graph02.lineColor = "#005ba8";
            graph02.fillAlphas = 0.8;
            graph02.fillColors = "#005ba8";
            graph02.balloonText = "流口：[[value]]人";
            chartTemp.addGraph(graph02);

            return chartTemp;
        }

        var getAmChartsBfb = function(){
            var chartTemp;
            // SERIAL CHART
            chartTemp = new AmCharts.AmSerialChart();
            chartTemp.rotate = true;
            chartTemp.startDuration = "1";
            chartTemp.startFrom = "bottom";
            chartTemp.categoryField = "label";
            chartTemp.plotAreaBorderAlpha = 0;

            // sometimes we need to set margins manually
            // autoMargins should be set to false in order chart to use custom margin values
//            chartTemp.autoMargins = false;
            chartTemp.marginLeft = 0;
            chartTemp.marginRight = 0;
            chartTemp.marginTop = 5;
            chartTemp.marginBottom = 5;

            // AXES
            // category
            var categoryAxis = chartTemp.categoryAxis;
            categoryAxis.gridAlpha = 0.1;
            categoryAxis.gridCount = "999";
            categoryAxis.labelRotation = "0";
            categoryAxis.autoGridCount = false;
            categoryAxis.gridPosition = "start";

            // value
            var valueAxis = new AmCharts.ValueAxis();
            valueAxis.stackType = "100%"; // this line makes the chart 100% stacked
            valueAxis.gridAlpha = 0.1;
            valueAxis.axisAlpha = 0;
            valueAxis.labelsEnabled = true;
            chartTemp.addValueAxis(valueAxis);

            // GRAPHS
            // first graph
            var graph = new AmCharts.AmGraph();
            graph.title = "医疗参保";
            graph.labelText = "[[percents]]%";
            graph.balloonText = "医疗参保：[[value]] ([[percents]]%)";
            graph.valueField = "cb";
            graph.type = "column";
            graph.lineColor = "#4983C2";
            graph.fillAlphas = [0.8];
            graph.fillColors = ["#4983C2"];
            chartTemp.addGraph(graph);

            // second graph
            var graph2 = new AmCharts.AmGraph();
            graph2.title = "医疗未参保";
            graph2.labelText = "[[percents]]%";
            graph2.balloonText = "医疗未参保：[[value]] ([[percents]]%)";
            graph2.valueField = "wcb";
            graph2.type = "column";
            graph2.lineColor = "#C9524C";
            graph2.fillAlphas = [0.8];
            graph2.fillColors = ["#C9524C"];
            chartTemp.addGraph(graph2);

            var legend = new AmCharts.AmLegend();
            legend.borderAlpha = 0;
            legend.horizontalGap = 5;
            legend.autoMargins = false;
            legend.marginLeft = 5;
            legend.marginRight = 5;
            legend.switchType = "v";
            chartTemp.addLegend(legend);

            return chartTemp;
        }

        var getAmChartsJiuye = function(){
            var chartTemp;
            // SERIAL CHART
            chartTemp = new AmCharts.AmSerialChart();
//            chartTemp.rotate = true;
            chartTemp.startDuration = "1";
            chartTemp.startFrom = "bottom";
            chartTemp.categoryField = "label";
            chartTemp.plotAreaBorderAlpha = 1;
            chartTemp.plotAreaBorderColor = "#DADADA";

            // sometimes we need to set margins manually
            // autoMargins should be set to false in order chart to use custom margin values
            chartTemp.autoMargins = true;
            chartTemp.marginLeft = 0;
            chartTemp.marginRight = 0;
            chartTemp.marginTop = 5;
            chartTemp.marginBottom = 5;

            // AXES
            // category
            var categoryAxis = chartTemp.categoryAxis;
            categoryAxis.gridAlpha = 0.1;
            categoryAxis.gridCount = "999";
            categoryAxis.labelRotation = "0";
            categoryAxis.autoGridCount = false;
            categoryAxis.gridPosition = "start";


            // value
            var valueAxis = new AmCharts.ValueAxis();
//            valueAxis.stackType = "100%"; // this line makes the chart 100% stacked
            valueAxis.gridAlpha = 0.1;
            valueAxis.axisAlpha = 0;
            valueAxis.labelsEnabled = true;
            valueAxis.position = "top";
            chartTemp.addValueAxis(valueAxis);


            // GRAPHS
            // first graph
            var graph = new AmCharts.AmGraph();
            graph.title = "灵活就业";
            graph.labelText = "[[value]]";
            graph.balloonText = "灵活就业：[[value]] ([[percents]]%)";
            graph.valueField = "lhjy";
            graph.type = "column";
            graph.lineColor = "#4983C2";
            graph.fillAlphas = [0.8];
            graph.fillColors = ["#4983C2"];
            chartTemp.addGraph(graph);

            // second graph
            var graph2 = new AmCharts.AmGraph();
            graph2.title = "就业困难";
            graph2.labelText = "[[value]]";
            graph2.balloonText = "就业困难：[[value]] ([[percents]]%)";
            graph2.valueField = "jykn";
            graph2.type = "column";
            graph2.lineColor = "#C9524C";
            graph2.fillAlphas = [0.8];
            graph2.fillColors = ["#C9524C"];
            chartTemp.addGraph(graph2);

            var graph3 = new AmCharts.AmGraph();
            graph3.title = "社区推荐就业";
            graph3.labelText = "[[value]]";
            graph3.balloonText = "社区推荐就业：[[value]] ([[percents]]%)";
            graph3.valueField = "sqtjjy";
            graph3.type = "column";
            graph3.lineColor = "#9ABB52";
            graph3.fillAlphas = [0.8];
            graph3.fillColors = ["#9ABB52"];
            chartTemp.addGraph(graph3);

            var legend = new AmCharts.AmLegend();
            legend.borderAlpha = 0;
            legend.horizontalGap = 0;
            legend.autoMargins = false;
            legend.marginLeft = 0;
            legend.marginRight = 0;
            legend.switchType = "v";
            legend.equalWidths = false;
            legend.width = 500;
            chartTemp.addLegend(legend);

            return chartTemp;
        }

        var getAmChartsXqdt = function(){
            var chartTemp;
            // SERIAL CHART
            chartTemp = new AmCharts.AmSerialChart();
//            chartTemp.rotate = true;
            chartTemp.startDuration = "1";
            chartTemp.startFrom = "bottom";
            chartTemp.categoryField = "label";
            chartTemp.plotAreaBorderAlpha = 1;
            chartTemp.plotAreaBorderColor = "#DADADA";

            // sometimes we need to set margins manually
            // autoMargins should be set to false in order chart to use custom margin values
//            chartTemp.autoMargins = false;
//            chartTemp.marginLeft = 0;
//            chartTemp.marginRight = 0;
//            chartTemp.marginTop = 5;
//            chartTemp.marginBottom = 5;

            // AXES
            // category
            var categoryAxis = chartTemp.categoryAxis;
            categoryAxis.gridAlpha = 0.1;
            categoryAxis.gridCount = "999";
            categoryAxis.labelRotation = "0";
            categoryAxis.autoGridCount = false;
            categoryAxis.gridPosition = "start";
//            categoryAxis.position = "left";


            // value
            var valueAxis = new AmCharts.ValueAxis();
            valueAxis.title = "居民变化数";
//            valueAxis.stackType = "100%"; // this line makes the chart 100% stacked
            valueAxis.gridAlpha = 0.1;
            valueAxis.axisAlpha = 0;
//            valueAxis.labelsEnabled = true;
//            valueAxis.position = "left";
//            valueAxis.axisColor = "#dadada";
            chartTemp.addValueAxis(valueAxis);

            // duration value axis
            var durationAxis = new AmCharts.ValueAxis();
            durationAxis.title = "新增、减少人口";
            durationAxis.gridAlpha = 0.1;
            durationAxis.axisAlpha = 0;
//            durationAxis.inside = true;
            durationAxis.position = "right";
//            chartTemp.addValueAxis(durationAxis);

            // GRAPHS
            // first graph
            var graph = new AmCharts.AmGraph();
            graph.title = "居民变化数";
            graph.labelText = "[[value]]";
            graph.balloonText = "居民变化数：[[value]]";
            graph.valueField = "jmbhs";
            graph.type = "column";
            graph.lineColor = "#4983C2";
            graph.fillAlphas = [0.8];
            graph.fillColors = ["#4983C2"];
            chartTemp.addGraph(graph);

            // duration graph
            var durationGraph = new AmCharts.AmGraph();
            durationGraph.title = "新增人口";
            durationGraph.valueField = "xzrk";
            durationGraph.type = "line";
            durationGraph.valueAxis = valueAxis; // indicate which axis should be used
            durationGraph.lineColor = "#C9524C";
            durationGraph.balloonText = "新增人口：[[value]]";
            durationGraph.lineThickness = 1;
            durationGraph.legendValueText = "新增人口：[[value]]";
            durationGraph.bullet = "square";
            chartTemp.addGraph(durationGraph);

            // duration graph
            var durationGraph2 = new AmCharts.AmGraph();
            durationGraph2.title = "减少人口";
            durationGraph2.valueField = "jsrk";
            durationGraph2.type = "line";
            durationGraph2.valueAxis = valueAxis; // indicate which axis should be used
            durationGraph2.lineColor = "#9ABB52";
            durationGraph2.balloonText = "减少人口：[[value]]";
            durationGraph2.lineThickness = 1;
            durationGraph2.legendValueText = "减少人口：[[value]]";
            durationGraph2.bullet = "square";
            chartTemp.addGraph(durationGraph2);

            var legend = new AmCharts.AmLegend();
            legend.borderAlpha = 0;
            legend.horizontalGap = 0;
            legend.autoMargins = false;
            legend.marginLeft = 0;
            legend.marginRight = 0;
            legend.switchType = "v";
            legend.equalWidths = false;
            legend.width = 500;
            chartTemp.addLegend(legend);

            return chartTemp;
        }

        chartJm = getAmCharts();
            chartJy1 = getAmCharts();
            chartJy2 = getAmCharts();
            chartSb = getAmChartsBfb();
            chartJiuye = getAmChartsJiuye();
            chartXqdtYz = getAmChartsXqdt();
            chartXqdtBn = getAmChartsXqdt();
            chartXqdtQn = getAmChartsXqdt();

            //居民
            wgqkService.getJmqk( function(data){
                $scope.jmData = data;
                chartJm.dataProvider = data.ageChart;
                chartJm.write("ageChartDiv");

                //教育
                wgqkService.getJyqk( function(data){
                    $scope.jyData = data;

                    chartJy1.dataProvider = data.primarySchool3YearChart;
                    chartJy1.write("jyChartDiv");

                    chartJy2.dataProvider = data.nursery3YearChart;
                    chartJy2.write("jyChartDiv2");

                    $scope.loadingTip = "";
                });

                //社保
                wgqkService.getCbqk( function(data){
                    $scope.sbData = data;
                    chartSb.dataProvider = data.chartData;
                    chartSb.write("sbChartDiv");
                });

                //就业
                wgqkService.getJiuyeqk( function(data){
                    $scope.jiuyeData = data;
                    chartJiuye.dataProvider = data.chartData;
                    chartJiuye.write("jiuyeChartDiv");

                });

                //辖区动态
                wgqkService.getXqdtRk( function(data){
                    $scope.xqdtData = data;
                    chartXqdtYz.dataProvider = data.yzData;
                    chartXqdtYz.write("yzXqdtChartDiv");
                    chartXqdtBn.dataProvider = data.bnData;
                    chartXqdtBn.write("bnXqdtChartDiv");
                    chartXqdtQn.dataProvider = data.qnData;
                    chartXqdtQn.write("qnXqdtChartDiv");
                });

            });
        }

        $scope.gotoFlexWggk = function(){
            var wggkUrl = '/'+$rootScope.systeminfo.csmp_context_path+'/?moduleId='+$rootScope.systeminfo.flex_menu_key_xqxq;
            $rootScope.$emit("flexIframeUrlChange", wggkUrl);
        }

//        AmCharts.ready(function () {
//            console.info("AmCharts.ready");
//        });

        // $(document).ready(function(){
            

        // });

        //改变辖区动态时间段
        $scope.selectXqdtDate = function(type){
            $scope.xqdt_rk_class1 = "";
            $scope.xqdt_rk_class2 = "";
            $scope.xqdt_rk_class3 = "";

            $(".xqdtClass").hide();
            $("#"+type+"XqdtChartDiv").show();

            if(type=="yz"){
                $scope.xqdt_rk_class1 ="active";
                if(chartXqdtYz){
                    chartXqdtYz.validateData();
                }
            }else if(type=="bn"){
                $scope.xqdt_rk_class2 ="active";
                if(chartXqdtBn){
                    chartXqdtBn.validateData();
                }
            }else if(type=="qn"){
                $scope.xqdt_rk_class3 ="active";
                if(chartXqdtQn){
                    chartXqdtQn.validateData();
                }
            }

        }

        $scope.gk_class1 ="active";
        //却换tab页
        $scope.selectTab = function(type){
            $scope.gk_class1 = "";
            $scope.gk_class2 = "";
            $scope.gk_class3 = "";

            $(".xqxq,.xqfx").hide();
            $("#"+type+"Div").show();

            if(type == "xqgk"){
                $scope.gk_class1 ="active";
            }else if(type == "xqfx"){
                $scope.gk_class2 ="active";
                $scope.selectFx("jm");
            }else if(type == "xqdt"){
                $scope.gk_class3 ="active";
                $scope.selectXqdtDate('yz');
            }
        };

//        $scope.selectTab("01");

        $scope.fb_class1 ="active";
        $scope.xqfxSelectTab ="";
        //却换分析TAB页
        $scope.selectFx = function(type){
            $scope.fb_class1 = "";
            $scope.fb_class2 = "";
            $scope.fb_class3 = "";
            $scope.fb_class4 = "";

            $scope.xqfxSelectTab = type;
            $(".xqfx_tabdiv").hide();
            $("#"+type+"Div").show();

            if(type == "jm"){
                $scope.fb_class1 ="active";
                if(chartJm){
                    chartJm.validateData();
                }
            }else if(type == "jy"){
                $scope.fb_class2 ="active";
                if(chartJy1){
                    chartJy1.validateData();
                }
                if(chartJy2){
                    chartJy2.validateData();
                }
            }else if(type == "sb"){
                $scope.fb_class3 ="active";
                if(chartSb){
                    chartSb.validateData();
                }
            }else if(type == "jiuye"){
                $scope.fb_class4 ="active";
                if(chartJiuye){
                    chartJiuye.validateData();
                }
            }
        };
        $scope.selectFx("jm");
    }]).
controller('myhomeUserAppListCtrl', ['$scope', 'thirdAppService', '$rootScope','$sce',
    function($scope, thirdAppService, $rootScope,$sce) {
        //首页获取待办事项列表
        thirdAppService.getUserAppAccounts(function(data){
            $scope.items = data;
        });
        $scope.bindAccount = function(item){
            $rootScope.selectMenu = {
                appId:item.app_id,
                label:item.app_label,
                login_id:item.login_id
            };
            $('#regAppPop').modal('show');
        }
    }]);