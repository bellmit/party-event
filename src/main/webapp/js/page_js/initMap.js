
function map_listAddPointsByobjIds(data,gmap,clickHandler)
{
	setTimeout(function(){
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		//根据data组装好ids和tips，过程省略
		var ids=[];
		var tips=[];
		var imgxx={"img":"xx","hoverImg":"vv"};
		var imgxxs=[];
		imgxxs.push(imgxx);
		gmap.addPointById(ids,tips,imgxxs,clickHandler);
	});
}


function map_listAddPoints(data,clickHandler)//地图列表点绘制方法
{
	setTimeout(function(){
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		$("#allcount").html(list_data_all.length);
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    		if(typeof list_data_all[i].LNG_D !="undefined"&&typeof list_data_all[i].LAT_D !="undefined" &&list_data_all[i].LNG_D!=""&&list_data_all[i].LAT_D!=""){
    			var strContent = '';
        		var strType = '';
        		if(list_data_all[i].HAZARD_TYPE.indexOf('0')>=0){
        			strType += '消防设施、器材、安全标志配置不符合标准,';
        		}
        		if(list_data_all[i].HAZARD_TYPE.indexOf('1')>=0){
        			strType += '消防设施、器材、安全标志未保持完好有效,';
        		}
        		if(list_data_all[i].HAZARD_TYPE.indexOf('2')>=0){
        			strType += '损坏、挪用消防设施、器材,';
        		}
        		if(list_data_all[i].HAZARD_TYPE.indexOf('3')>=0){
        			strType += '擅自拆除、停用消防设施、器材,';
        		}
        		if(list_data_all[i].HAZARD_TYPE.indexOf('4')>=0){
        			strType += '占用、堵塞、封闭疏散通道、出口,';
        		}
        		if(list_data_all[i].HAZARD_TYPE.indexOf('5')>=0){
        			strType += "占用、堵塞消防车道；妨碍消防车通行,";
        		}
        		if(list_data_all[i].HAZARD_TYPE.indexOf('6')>=0){
        			strType += "埋压、圈占、遮挡消火栓,";
        		}
        		if(list_data_all[i].HAZARD_TYPE.indexOf('7')>=0){
        			strType += "占用防火间距,";
        		}
        		if(list_data_all[i].HAZARD_TYPE.indexOf('8')>=0){
        			strType += "其它消防安全违法行为和火灾隐患,";
        		}
        		strContent += "隐患类型："+strType.substring(0, strType.length-1)+"<br/>隐患地址:<a href='"+contextPath+"/fire/detail.do?fireId="+list_data_all[i].ID+"&visiteditem=fire'>"+list_data_all[i].HAZARD_ADDRESS+"</a>";
        		strContent += "<br/>上报人员："+list_data_all[i].REPORT_PERSON;
        		strContent += "<br/>联系方式："+(list_data_all[i].PHONE==null?'':list_data_all[i].PHONE);
        		strContent += "<br/>上报时间："+formatpc(list_data_all[i].REPORT_TIME);
        		strContent += "<br/>处理状态："+(list_data_all[i].STATUS=="0"?"未处理":list_data_all[i].STATUS=="1"?"已解决":list_data_all[i].STATUS=="2"?"误报":"上报");
        		if(list_data_all[i].PROCESS_TIME!=null&&list_data_all[i].PROCESS_TIME!=''){
        			strContent += "<br/>处理时间："+formatpc(list_data_all[i].PROCESS_TIME);
        		}
        		if(list_data_all[i].DEPART_NAME!=null&&list_data_all[i].PROCESS_TIME!=''){
        			strContent += "<br/>处理单位："+(list_data_all[i].DEPART_NAME==null?"":list_data_all[i].DEPART_NAME);
        		}
        		
        		var pageName="red";
        		if(list_data_all[i].STATUS=="1"){
        			pageName="gray";
        		}
        		var hoverPageNmae="blue";
        		var tip={"index":i,fireId:list_data_all[i].ID,"title":"消防隐患",content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].LNG_D,list_data_all[i].LAT_D,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    		
    	}
    	for(var i=0;i<data.length;i++)//根据列表数据画点
    	{
    		if(typeof data[i].LNG_D !="undefined"&&typeof data[i].LAT_D !="undefined" &&data[i].LNG_D!=""&&data[i].LAT_D!=""){
    			var strContent = '';
        		var strType = '';
        		if(data[i].HAZARD_TYPE.indexOf('0')>=0){
        			strType += '消防设施、器材、安全标志配置不符合标准,';
        		}
        		if(data[i].HAZARD_TYPE.indexOf('1')>=0){
        			strType += '消防设施、器材、安全标志未保持完好有效,';
        		}
        		if(data[i].HAZARD_TYPE.indexOf('2')>=0){
        			strType += '损坏、挪用消防设施、器材,';
        		}
        		if(data[i].HAZARD_TYPE.indexOf('3')>=0){
        			strType += '擅自拆除、停用消防设施、器材,';
        		}
        		if(data[i].HAZARD_TYPE.indexOf('4')>=0){
        			strType += '占用、堵塞、封闭疏散通道、出口,';
        		}
        		if(data[i].HAZARD_TYPE.indexOf('5')>=0){
        			strType += "占用、堵塞消防车道；妨碍消防车通行,";
        		}
        		if(data[i].HAZARD_TYPE.indexOf('6')>=0){
        			strType += "埋压、圈占、遮挡消火栓,";
        		}
        		if(data[i].HAZARD_TYPE.indexOf('7')>=0){
        			strType += "占用防火间距,";
        		}
        		if(data[i].HAZARD_TYPE.indexOf('8')>=0){
        			strType += "其它消防安全违法行为和火灾隐患,";
        		}
        		strType = strType.substring(0, strType.length-1);
        		strContent += "隐患类型："+strType+"<br/>隐患地址:<a href='"+contextPath+"/fire/detail.do?fireId="+data[i].ID+"&visiteditem=fire'>"+data[i].HAZARD_ADDRESS+"</a>";
        		strContent += "<br/>上报人员："+data[i].REPORT_PERSON;
        		strContent += "<br/>联系方式："+(data[i].PHONE==null?'':data[i].PHONE);
        		strContent += "<br/>上报时间："+formatpc(data[i].REPORT_TIME);
        		strContent += "<br/>处理状态："+(data[i].STATUS=="0"?"未处理":data[i].STATUS=="1"?"已解决":list_data_all[i].STATUS=="2"?"误报":"上报");
        		if(data[i].PROCESS_TIME!=null&&data[i].PROCESS_TIME!=''){
        			strContent += "<br/>处理时间："+formatpc(data[i].PROCESS_TIME);
        		}
        		if(data[i].DEPART_NAME!=null&&data[i].PROCESS_TIME!=''){
        			strContent += "<br/>处理单位："+(data[i].DEPART_NAME==null?"":data[i].DEPART_NAME);
        		}
    			var pageCount=i+1;
    			
        		var pageName="p"+pageCount;
        		if(data[i].STATUS=="1"){
        			pageName="p"+pageCount+"g";
        		}
        		var hoverPageNmae="ph"+pageCount;
        		var tip={"index":i,fireId:data[i].ID,"title":"消防隐患",content:strContent};
        		var pointId=gmap.addPoint(data[i].LNG_D,data[i].LAT_D,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    		
    	}
    	
    	gmap.setExtentByshowPointGraphicsLayer();
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    	//gmap.centerAtPoint(data[0].LNG_D,data[0].LAT_D,11);
    },1000);
}

//普通列表和地图列表的绑定生成
function doubleTableRender(contextPath,listDiv,pageDiv,mapListDiv,mapPageDiv,requestData,clickHandler){
	var imgurl=contextPath +"/js/gmap/images";
	var opts_map = {
	        basePath:contextPath +"/js/gmap/ulynlist",
	        url:contextPath+'/fire/fire_list.do',
	        tableTpl:"maplist",
	        pageBarTpl:"maplist",
	        tableColumn: {
	            loadFilter:function(data){
	            	for(var i=0;i<data.length;i++){
	            		var item = data[i];
	            		item['XH'] = i+1;
	            	}
	            	list_data=data;
	            },
	            columns:[
	                     {field: "XH", className: imgurl,label:imgurl, width: 50},
	                     
	                     {field: '', label: '地址', className: "address", width: 50, bodyContent: "[HAZARD_ADDRESS]"},
	                     {
	                         field: '', label: '姓名', className: "sortable", width: 50,
	                         bodyContent: "<a href='javascript:Void(0)'>[REPORT_PERSON]</a><span>[PHONE]</span>"
	                     }
	                     
	                 ] ,
	            rownumbers: false 
	        },
	        afterTableRender:function(){
	        	 $("#mapList li").click(function(){
	        		 var index = $(this).index();
	        		 if(typeof list_data[index].LNG_D !="undefined"&&typeof list_data[index].LAT_D !="undefined" &&list_data[index].LNG_D!=""&&list_data[index].LAT_D!=""){
	        			 gmap.closeInfoWindow();
	        			 for(var i = 0 ; i < $("#mapList li").length;i++){
	        				 gmap.changePointToImg(i+list_data_all.length); 
	        			 }
	        			 gmap.changePointToHoverImg(pointArray[index].id+list_data_all.length);
	        			 gmap.centerAtPoint(list_data[index].LNG_D,list_data[index].LAT_D,13);
	        			 setTimeout(function(){
	        		    		var height = 10*gmap.getMapLevel()+1;
	        		    		var top = 165-height;
	        		    		$("#sliderHeight").height(height);
	        		        	$("#sliderTop").css("top",top+"px");
	        		    	},1500);
	        			 gmap.setPointToTop(pointArray[index].id+list_data_all.length);
	        		 }
	    			 
	    		});
	        	 
	        	 var inputdata = {"isMap":"isMap","status":$("#status").val(),"type":$("#type").val(),"street":$("#street").val(),"startTime":$("#startTime").val(),"endTime":$("#endTime").val(),"userName":$("#userName").val()};
	        	 $.ajax({
	        	        type:"POST",
	        	        url:contextPath+'/fire/fire_list_all.do',
	        	        data:inputdata,
	        	        dataType:"json",
	        	        async:false,
	        	        success:function(data){
	        	        	list_data_all = data.data.list;
	        	        	map_listAddPoints(list_data,clickHandler);
	        	        },
	        	        beforeSend:function(){
	        	        },
	        	        error:function(){
	        	        	map_listAddPoints(list_data,clickHandler);
	        	        }
	        		 });
	        	 var height = $(window).height()-$("#listMapLi").offset().top;
	        	 if($("#mapList").find("ul").height()>(height-$("#mapPager").height())){
	        		 $("#mapList").height((height-$("#mapPager").height())+"px");
	        	 }
	     		
	        },
	        requestData:requestData,
	        pageBarId: mapPageDiv
	    };
	
	    $("#"+mapListDiv).ulynlist(opts_map);
	    $("select").select2();
	    var height = $(window).height()-$("#listMapLi").offset().top;
	    $("#listMapLi").height(height+"px");
	    $("#maplistdiv").height(height+"px");
}
function clickHandler(index,x,y,tip){
	//window.location.href=contextPath+'/fire/detail.do?fireId='+tip.fireId;
}

function queryDataAssembly() {
    var requestData = {"isMap":"isMap","status":$("#status").val(),"type":$("#type").val(),"street":$("#street").val(),"startTime":$("#startTime").val(),"endTime":$("#endTime").val(),"userName":$("#userName").val(),linesPerPage:10};

    var listDiv = 'dwTab';
    var pageDiv = 'dwPager';
    var mapListDiv = 'mapList';
    var mapPageDiv = 'mapPager';
    doubleTableRender(contextPath, listDiv, pageDiv, mapListDiv, mapPageDiv, requestData, clickHandler);
}

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}
$(function(){
    //侧边栏渲染
//    renderSidebar();
	
    //表格渲染
	queryDataAssembly();
	
    $("#search-btn").click(function(){
    	queryDataAssembly();
    });
    
    $("#search-reset").click(function(){
    	$("#s2id_type").find("span").eq(0).html("请选择");
    	$("#s2id_status").find("span").eq(0).html("未处理");
    	$("#type").val("");
    	$("#street").val("");
    	$("#startTime").val("");
    	$("#endTime").val("");
    	$("#userName").val("");
    	$("#status").val("0");
    });
    
});