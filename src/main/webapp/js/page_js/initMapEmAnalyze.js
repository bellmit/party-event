var mapData=[];
var mapKey=[];
var showType='沼气池';
var isCenter = 1;
//圆圈样式
var style = [];
style.line = [];
style.fill = [];
style.line.style = "solid";
style.line.color = [255,255,255,.8];
style.line.width = 1;
style.fill.style = "solid";
style.fill.color = [0,100,190,0.2];

/**
 * 根据obj所在坐标的圆  画出list集合中处于圆内的点
 * @param obj
 * @param list
 */
function circleAddPoints(data){
	setTimeout(function(){
		//画圆
		drawCircle(circleXYx,circleXYx,radiusDefault,"","",data);
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    },1000);
}

function map_listAddPoints(data,clickHandler)//地图列表点绘制方法
{
	setTimeout(function(){
		initPoint();
		initCircle();
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    		if(typeof list_data_all[i].LNG !="undefined"&&typeof list_data_all[i].LAT !="undefined" &&list_data_all[i].LNG!=""&&list_data_all[i].LAT!=""){
    			var strContent = '';
    			strContent += '应急类型:'+repType(list_data_all[i].EM_TYPE)+"<br>";
    			strContent += '情况描述:'+list_data_all[i].MSG_DESC+"<br>";
    		    strContent += '地址信息:'+"<a href='javascript:void(0)' onclick='loadEditIfr(\""+contextPath+"/emergency/detail.do?id="+list_data_all[i].MSG_ID+"\")'>"+list_data_all[i].ADDRESS+"</a>"+"<br>";
    		    var pageName="red";
        		if(list_data_all[i].STATUS=="1"){
        			pageName="gray";
        		}
        		var hoverPageNmae="blue";
        		var tip={"index":i,fireId:list_data_all[i].MSG_ID,"title":"应急信息",content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].LNG,list_data_all[i].LAT,tip,pageName,hoverPageNmae,null);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    		
    	}
    	for(var i=0;i<data.length;i++)//根据列表数据画点
    	{
    		if(typeof data[i].LNG !="undefined"&&typeof data[i].LAT !="undefined" &&data[i].LNG!=""&&data[i].LAT!=""){
    			var strContent = '';
//    			if(list_data_all[i].TYPE_NAME==null){
//    				strContent += '应急类型:'+''+"<br>";
//    			}else{
//    				strContent += '应急类型:'+list_data_all[i].TYPE_NAME+"<br>";
//    			}
    			strContent += '应急类型:'+repType(list_data_all[i].EM_TYPE)+"<br>";
    			//strContent += '地址信息:'+data[i].ADDRESS+"<br>";
    			strContent += '情况描述:'+data[i].MSG_DESC+"<br>";
    			strContent += '地址信息:'+"<a href='javascript:void(0)' onclick='loadEditIfr(\""+contextPath+"/emergency/detail.do?id="+list_data_all[i].MSG_ID+"\")'>"+list_data_all[i].ADDRESS+"</a>"+"<br>";
//    			strContent += "<a href=\"javascript:void(0)\" onclick=\"$('.mapMs').hide();$('.analye-con').show();$('.BMap_stdMpSlider').css('width',parseInt($('.BMap_stdMpZoom').width()-161))\" class=\"fx-a\">影响分析2</a>";
//    			strContent += '地址信息:'+data[i].ADDRESS+loadEditIfr(contextPath+'/emergency/detail.do?id='+list_data_all[i].MSG_ID+","+'list_data_all[i].ADDRESS')+"<br>";
    		//	strContent += '地址信息:'+"<a href="+contextPath+"/emergency/detail.do?id="+list_data_all[i].MSG_ID+">"+list_data_all[i].ADDRESS+"</a>"+"<br>";
    			
    			var pageCount=i+1;
    			
        		var pageName="p"+pageCount;
        		if(data[i].STATUS=="1"){
        			pageName="p"+pageCount+"g";
        		}
        		var hoverPageNmae="ph"+pageCount;
        		var tip={"index":i,fireId:data[i].MSG_ID,"title":"应急信息",content:strContent};
        		var pointId=gmap.addPoint(data[i].LNG,data[i].LAT,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    		
    	}
//    	$("#allcount").html(list_data_all.length);
    	gmap.setExtentByshowPointGraphicsLayer();
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    	//gmap.centerAtPoint(data[0].MAPS_X,data[0].MAPS_Y,11);
    },1000);
}
function repType(value){
	var typelist1 = ulynlistPath.typelist;
	var v = value.split(",");
	var ret = "";
	for(var i=0;i<v.length;i++){
		for(var j=0;j<typelist1.length;j++){
			if(typelist1[j].id == v[i]){
				ret += ret==""?typelist1[j].name:","+typelist1[j].name;
				break;
			}
		}
	}
	return ret;
}
//普通列表和地图列表的绑定生成
function doubleTableRender(contextPath,listDiv,pageDiv,mapListDiv,mapPageDiv,requestData,clickHandler){
	var imgurl=contextPath +"/js/gmap/images";
	var opts_map = {
	        basePath:contextPath +"/js/gmap/ulynlist",
	        url:contextPath+'/emergency/emergency_list.do',
	        tableTpl:"acmaplist",
	        pageBarTpl:"acmaplist",
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
	                     
	                     {field: 'ADDRESS', label: '地址信息', className: "address", width: 50, 
	                          tableTransFunc:function(value,item){
//	                        	  console.info("item:");
 	                        	 //  console.info(item);
 	                        	  var RN = item.RN ;
 	                        	  RN= RN%10 == "0"?"10":RN%10;
	                          	var val = "<a href=\"#\">"+item.ADDRESS+"</a>"+"<span class=\"fx-span\"  onclick=\"showAnalyzeDiv('"+item.MSG_ID+"','"+item.LNG+"','"+item.LAT+"','"+item.ADDRESS+"','"+RN+"')\"><br/>影响分析</span>";
	                            	return val;
	                            }}
	                 ] ,
	            rownumbers: false 
	        },
	        afterTableRender:function(){
	        	 $("#mapList li").click(function(){
	        		 var index = $(this).index();
	        		 if(typeof list_data[index].LNG !="undefined"&&typeof list_data[index].LAT !="undefined" &&list_data[index].LNG!=""&&list_data[index].LAT!=""){
	        			 gmap.closeInfoWindow();
	        			 for(var i = 0 ; i < $("#mapList li").length;i++){
	        				 gmap.changePointToImg(i+list_data_all.length); 
	        			 }
	        			 gmap.changePointToHoverImg(pointArray[index].id+list_data_all.length);
	        			 gmap.centerAtPoint(list_data[index].LNG,list_data[index].LAT,13);
	        			 setTimeout(function(){
	        		    		var height = 10*gmap.getMapLevel()+1;
	        		    		var top = 165-height;
	        		    		$("#sliderHeight").height(height);
	        		        	$("#sliderTop").css("top",top+"px");
	        		    	},1500);
	        		    	gmap.setPointToTop(pointArray[index].id+list_data_all.length);
	        		 }
	    			 
	    		});
	        	 var inputdata = {linesPerPage:99999,currentPage:1,"em_type":$(".select2").val(),"rk_status":$("#rk_status").val(),"rp_status":$("#rp_status").val(),"tran_status":$("#tran_status").val(),"msg_desc":$("#msg_desc").val(),"isMap":"isMap"};
	        	 $.ajax({
	        	        type:"POST",
	        	        url:contextPath+'/emergency/emergency_list.do',
	        	        data:inputdata,
	        	        dataType:"json",
	        	        async:false,
	        	        success:function(data){
//	       	        	 	$(".jgxz").eq(1).html(data.data.totalNum);
	       	        	 	$(".jgxz").html(data.data.totalNum);
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
	    var height = $(window).height()-$("#listMapLi").offset().top;
	    $("#listMapLi").height(height+"px");
	    $("#maplistdiv").height(height+"px");
}
function clickHandler(index,x,y,tip){
	alert("index:"+index+"  x:"+x+"  y:"+y+"  tip:"+tip);
}
function queryDataAssembly() {
	var requestData={"em_type":$(".select2").val(),"rk_status":$("#rk_status").val(),"rp_status":$("#rp_status").val(),"tran_status":$("#tran_status").val(),"msg_desc":$("#msg_desc").val(),"isMap":"isMap",linesPerPage:10};
	radiusDefault = 500;
	$("input[class='BMap_data tcenter fnt12']").val("500");
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
function loadEditIfr(url,title){
	$.layer({
        type: 2,
        maxmin: true,
        shadeClose: false,
        title: title==undefined?'采集应急信息':title,
        shade: [0.1,'#fff'],
        offset: ['25px',''],
        area: [screen.width+'px', $(window).height() +'px'],
        iframe: {src: url},
        close: function(index){
//            if(title==undefined){
        	onloadWindow();
//            }
        }
    });
	 $("a[class='xubox_max xulayer_png32']").trigger("click");
};
$(function(){
    //侧边栏渲染
//    renderSidebar();
	
    //表格渲染
	
	queryDataAssembly();
    $("#search-btn").click(function(){
    	queryDataAssembly();
    });
    $("#add-btn").click(function(){
        loadEditIfr(contextPath+'/emergency/addEM.do',"采集应急信息");
    });
//    $("#search-reset").click(function(){
//    	$(".all-style").removeClass("all-style");
//    	$("#全部").addClass("all-style");
//    	$(".select2").val("");
//    	$("#rk_status").val("");
//    	$("#rp_status").val("");
//    	$("#tran_status").val("");
//    	$("#msg_status").val("");
//    });
    $("#map_zoom_slider").attr("style","display:none");
    
});
function onloadWindow(){
}
var circleXYx;
var circleXYy;
var msgId;
var enterObj;
/**
 * 点击左侧地址，展示影响分析的div
 */
function showAnalyzeDiv(id,x,y,name,index){
	msgId=id;
	circleXYx = x;
	circleXYy = y;
	initPoint();
	$(".tongjiTd").html("加载中");
	mapData=[];
	mapKey='';
	$('.mapMs').hide();
	$('.analye-con').show();
	$('.BMap_stdMpSlider').css('width',parseInt($('.BMap_stdMpZoom').width()-161));
	
	//设置图片 和 地址
	$("#address_img").attr("src",contextPath+"/js/gmap/images/p"+index+".png");
	$("#address").html(name+"");
	//获取应急分析数据
	for(var i = 0 ; i < $(".tongjiTd").length ; i ++){
		getAllData(id,x,y,$(".tongjiTd").eq(i).attr('id'));		
	}
}

/**
 * 获取应急分析数据
 * 
 * @param x
 * @param y
 * @param type A01沼气池  A02下水道 A03油库  A04：滑坡路段 A05：学校
 */
function getAllData(msgId,x,y,ac_id){
	//var radios = $("#dd").val();//半径
	var  data = {
			"msgId":msgId,
			"radiusDefault":radiusDefault,
			"AC_ID":ac_id
	};
	mapKey += ac_id + ",";
	 $.ajax({
	        type:"POST",
	        url:contextPath+'/emac/getEmAnalyzeObject.do',
	        data:data,
	        dataType:"json",
	        async:true,
	        success:function(data){
	        	console.info("获取到的数据data：");
	        	console.info(data);
	        	enterObj = data.EMList[0];
	        	mapData.push({kkk:data.emAnalyzeObject[0].bean.AC_ID,data:data});
    			circleAddPoints(data);
    			if(data.emAnalyzeObject[0].bean.AC_NAME=="沼气池"){
    				addPointByFor();
    			}
	        },
	        beforeSend:function(){
	        },
	        error:function(){
	          alert("获取数据错误");
	        }
		 });
}
var serach_circle;
var heatmapQUlist=[];
var radiusDefault = 500;
var isFirstHeat = 1;
var divType='zqc';
var out_url;
var open_type;
function changeRadius(radius){
	$('.yj-info').hide();
	mapData=[];
	radiusDefault = parseFloat($('.BMap_data').val().replace('公里',''));
	for(var i = 0 ; i < $(".tongjiTd").length ; i ++){
		getAllData(msgId,circleXYx,circleXYy,$(".tongjiTd").eq(i).attr('id'));		
	}
}
function changeType(type,typeR,OUT_URL,OPEN_TYPE){
	if($("#"+typeR).html()!='加载中'){
		showType = type;
		divType = typeR;
		out_url = OUT_URL;
		open_type = OPEN_TYPE;
		addPointByFor();
	}
}
function drawCircle(x,y,radius,tip,clickHandler,data){
	initCircle();
	serach_circle = gmap.addCircleForQuery(circleXYx,circleXYy,radius,tip,style,clickHandler);
	if(data.emAnalyzeObject&&data.emAnalyzeObject.length){
		for(var i=0;i<data.emAnalyzeObject.length;i++)
		{
			var count = 0 ;
			for(var k=0;k<data.emAnalyzeObject[i].list.length;k++){
				if(data.emAnalyzeObject[i].bean.AC_NAME!='居民人口'){
					if(gmap.checkContains(serach_circle.attributes.id,data.emAnalyzeObject[i].list[k].MAPS_X,data.emAnalyzeObject[i].list[k].MAPS_Y)){
						count++;
					}
				}else{
					if(gmap.checkContains(serach_circle.attributes.id,data.emAnalyzeObject[i].list[k].geometry.x,data.emAnalyzeObject[i].list[k].geometry.y)){
		    			count += parseInt(data.emAnalyzeObject[i].list[k].attributes.count);
		    		}
				}
			}
			$("#"+data.emAnalyzeObject[i].bean.AC_ID).html(count+data.emAnalyzeObject[i].bean.UNITS);
			
		}
	}
	if(isCenter == 1){
		gmap.centerAtPoint(circleXYx,circleXYy,15);
		isCenter = 2;
	}
}

function sendSms(obj){

	
//	alert(enterObj.EM_TYPE);
//	alert("情况说明:"+enterObj.MSG_DESC+"上报时间："+enterObj.CREATE_TIME+"应急类型："+enterObj.EM_TYPE);
//	$(".smsContentSelect").find('option[value="'+enterObj.EM_TYPE+'"]').attr("selected","selected");
//	alert("选择的值为:"+enterObj.EM_TYPE);
//	$(".smsContentSelect option[value='"+enterObj.EM_TYPE+"']").attr("selected","selected");
	var ente=enterObj.EM_TYPE.split(',')
//	alert(ente);
	$("#smsContentSelect").val(ente[0]);
//	alert($(".smsContentSelect").val());
//	alert($('.smsContentSelect option:checked').text());
//	alert($('.smsContentSelect option:checked').val()+"测试");
//	$("#smsContentSelect").val("${enterObj.EM_TYPE}").trigger("change");
//	$("select[class='select2 fl smsContentSelect']").val("'"+enterObj.EM_TYPE+"'").trigger("change");
//	$(".smsContentSelect").find('option[value="'+enterObj.EM_TYPE+'"]').attr("checked",true);
	$("#select2-smsContentSelect-container").attr("title",$('.smsContentSelect option:checked').text());
	$("#select2-smsContentSelect-container").html($('.smsContentSelect option:checked').text());
	changeInput1();
	$("#smsType").val($(obj).attr('data-type'));
	$('.yj-info').show();
	$("#btn-primary").removeAttr("disabled"); 
}
var sendId=null;
function sendSmsAjax(){
	
	if($("#smsContent").val()==''){
		alert("短信内容不能为空");
		return;
	}
	if(heatmapQUlist.length == 0){
		alert("没有对应人群，不用发送短信")
		return;
	}
	$("#btn-primary").attr("disabled","true");//避免重复点击发送按钮，点击之后将按钮禁用。
	params = "";
	for(var i=0;i<heatmapQUlist.length;i++){
		params+=params.length==0?heatmapQUlist[i].attributes.ldbh:","+heatmapQUlist[i].attributes.ldbh;
	}
	
	var content = ($('.smsContentSelect option:checked').text()=='all'?'':$('.smsContentSelect option:checked').text())+$("#smsContent").val() +"[民生网]";
	var inputdata = {params:params,type:$("#smsType").val(),title:$("#smsType").val(),content:encodeURI(content),messageStatus:false};
	$.ajax({
        type:"POST",
        url:contextPath+'/send/StartToSend1.do',
        data:inputdata,
        dataType:"json",
        async:false,
        success:function(data){
    		var count=data.count;
//    		alert(count+"    "+messageStatus);
//    		alert("关闭定时器");
//    		alert("sendId="+sendId);
    		if(data.msg=="msg"){
    			alert("统计异常");
    			$("#btn-primary").removeAttr("disabled"); 
    		}else{
    			if(confirm("你总共要跟"+count+"人发送短信，是否确定发送。")){
    				sendSmsAjax1();
    				$("#btn-primary").removeAttr("disabled"); 
    			}else{
    				alert("发送取消!");
    				$("#btn-primary").removeAttr("disabled"); 
    			}
    		}
//        	sendId=setTimeout(function(){StartCount();},1000);
//        	alert(1122);
//        	confirm("slsald"){
//        		
//        	}

//       	 	alert("接口调用成功");
        },
        beforeSend:function(){
        },
        error:function(){
        	alert("统计失败");
        }
	 });
}

function sendSmsAjax1(){
	if($("#smsContent").val()==''){
		alert("短信内容不能为空");
		return;
	}
	if(heatmapQUlist.length == 0){
		alert("没有对应人群，不用发送短信")
		return;
	}
	params = "";
	for(var i=0;i<heatmapQUlist.length;i++){
		params+=params.length==0?heatmapQUlist[i].attributes.ldbh:","+heatmapQUlist[i].attributes.ldbh;
	}
	var content = ($('.smsContentSelect').val()=='all'?'':$('.smsContentSelect').val())+$("#smsContent").val() +"[民生网]";
	var inputdata = {params:params,type:$("#smsType").val(),title:$("#smsType").val(),content:encodeURI(content),sendpanduan:"sendpanduan"};
	
	$.ajax({
        type:"POST",
        url:contextPath+'/send/StartToSend.do',
        data:inputdata,
        dataType:"json",
        async:false,
        success:function(data){
       	 	alert("接口调用成功");
//        	alert("发送成功!");
        },
        beforeSend:function(){
        },
        error:function(){
        	alert("发送失败");
        }
	 });
}
//function StartCount(){
//	$.ajax({
//        type:"POST",
//        url:contextPath+'/send/StartCount.do',
//        dataType:"json",
//        async:false,
//        success:function(data){
//        	var messageStatus=data[1];
//        	if(messageStatus==true){
////        		alert(3344);
//        		var count=data[0];
////        		alert(count+"    "+messageStatus);
////        		alert("关闭定时器");
////        		alert("sendId="+sendId);
//        		clearTimeout(sendId);//关闭定时器
//        		sendId=null;
//        		if(confirm("你总共要跟"+count+"人发送短信，是否确定发送。")){
//        			sendSmsAjax1();
//        			$("#btn-primary").removeAttr("disabled"); 
//        		}else{
//        			alert("发送取消!");
//        			$("#btn-primary").removeAttr("disabled"); 
//        		}
//        	}
//        },
//        error:function(){
//        	alert("发送失败");
//        }
//	 });
//}
function addPointByFor(){
	$(".clickremove").remove();
	gmap.setPointID();
	gmap.closeInfoWindow();
	gmap.clearPoints();//清除地图上所有点
	pointArray=[];
	heatmapQUlist=[];
	addEnterPoint();
	var pointIndex=0;
	for(var index=0;index<mapData.length;index++){
		if(mapData[index].kkk==divType){
			var data = mapData[index].data;
				for(var k=0;k<data.emAnalyzeObject[0].list.length;k++){
					if(gmap.checkContains(serach_circle.attributes.id,data.emAnalyzeObject[0].list[k].MAPS_X,data.emAnalyzeObject[0].list[k].MAPS_Y)){
						var strContent = '';
						var title = '';
						if(showType==data.emAnalyzeObject[0].bean.AC_NAME){
							//通过类型判断是标注点或者热力图
							if(data.emAnalyzeObject[0].bean.AC_NAME=='楼栋'){
								strContent += (data.emAnalyzeObject[0].list[k].TITLE==null?'':data.emAnalyzeObject[0].list[k].TITLE);
								title += (data.emAnalyzeObject[0].list[k].TITLE==null?'':data.emAnalyzeObject[0].list[k].TITLE);
							}else{
								strContent += data.emAnalyzeObject[0].list[k].SCHOOL_NAME;
								title += data.emAnalyzeObject[0].list[k].SCHOOL_NAME;
							}
							if(out_url != null && out_url!=''){
								var go_url = out_url.replace('{id}',data.emAnalyzeObject[0].list[k].ID);
								if(go_url.indexOf('http')==-1){
									go_url = contextPath + go_url;
								}
								if(open_type == 'self'){
									strContent += '<br><a target="_blank" href="'+go_url+'">查看明细</a>';
								}else{
									strContent += "<br><a href='javascript:void(0)' onclick='loadEditIfr(\""+go_url+"\",\"查看明细\")'>查看明细</a>";
								}
							}
							var pageName="red";
							var hoverPageNmae="blue";
							var tip={"index":k,"title":title,content:strContent};
							var pointId=gmap.addPoint(data.emAnalyzeObject[0].list[k].MAPS_X,data.emAnalyzeObject[0].list[k].MAPS_Y,tip,pageName,hoverPageNmae,clickHandler);
							var pointInfo={"id":pointId,"index":pointIndex};
							pointIndex++;
							pointArray.push(pointInfo);
						}
						var str = data.emAnalyzeObject[0].list[k].ALARM_TYPE;
						if(str == null || str == "null"){
							str = "";
						}
						if(data.emAnalyzeObject[0].bean.AC_NAME=='楼栋'){
							var html = '<tr class="clickremove"><td colspan="3">'+data.emAnalyzeObject[0].list[k].TITLE+'</td></tr>';
							$('.'+data.emAnalyzeObject[0].bean.AC_ID).find('table').append(html);
						}else{
							var html = '<tr class="clickremove"><td>'+data.emAnalyzeObject[0].list[k].SCHOOL_NAME+'</td><td>'+str+'</td><td></td></tr>';
							$('.'+data.emAnalyzeObject[0].bean.AC_ID).find('table').append(html);
						}
					}
					if(data.emAnalyzeObject[0].bean.AC_NAME=='居民人口'){
						var c_em = 0;
						var c_helpman = 0;
						var c_old = 0;
						var c_women = 0;
						var c_child = 0;
						var c_gridman = 0;
						for(var k=0;k<data.emAnalyzeObject[0].list.length;k++){
							if(gmap.checkContains(serach_circle.attributes.id,data.emAnalyzeObject[0].list[k].geometry.x,data.emAnalyzeObject[0].list[k].geometry.y)){
								if(showType==data.emAnalyzeObject[0].bean.AC_NAME){
									if(data.emAnalyzeObject[0].bean.AC_NAME=='居民人口'){
										heatmapQUlist.push(data.emAnalyzeObject[0].list[k]);
									}
								}
								c_em += parseInt(data.emAnalyzeObject[0].list[k].attributes.C_EM);
								c_helpman += parseInt(data.emAnalyzeObject[0].list[k].attributes.C_HELPMAN);
								c_old += parseInt(data.emAnalyzeObject[0].list[k].attributes.C_OLD);
								c_women += parseInt(data.emAnalyzeObject[0].list[k].attributes.C_WOMEN);
								c_child += parseInt(data.emAnalyzeObject[0].list[k].attributes.C_CHILD);
								c_gridman += parseInt(data.emAnalyzeObject[0].list[k].attributes.C_GRIDMAN);
							}
						}
						var html = '<tr class="clickremove">';
						html +=     '<td>孤寡老人 </td>';
						html +=    '<td>'+c_old+'</td>';
						html +=    '<td><a href="javascript:void(0)" class="send-a" data-type="c_old" onclick="sendSms(this)">发送短信</a> </td>';
						html +=' </tr>';
						html +=' <tr class="clickremove">';
						html +='     <td>育龄妇女</td>';
						html +='     <td>'+c_women+'</td>';
						html += '     <td><a href="javascript:void(0)" class="send-a c_women" data-type="c_women" onclick="sendSms(this)">发送短信</a> </td>';
						html += ' </tr>';
						html +=      ' <tr class="clickremove">';
						html +=        '     <td>儿童</td>';
						html +=         '    <td>'+c_child+'</td>';
						html +=         '    <td><a href="javascript:void(0)" class="send-a c_child" data-type="c_child" onclick="sendSms(this)">发送短信</a> </td>';
						html +=        ' </tr>';
						
						html +=        ' <tr class="clickremove">';
						html +=       '      <th colspan="3">工作人员</th>';
						html +=      '  </tr>';
						
						html +=        ' <tr style="border-top: 2px solid #ccc" class="clickremove">';
						html +=       '      <td>结对帮扶人员</td>';
						html +=        '      <td>'+c_helpman+'</td>';
						html +=      '      <td><a href="javascript:void(0)" class="send-a c_helpman" data-type="c_helpman" onclick="sendSms(this)">发送短信</a> </td>';
						html +=      '  </tr>';
						html +=      '  <tr class="clickremove">';
						html +=      '      <td>应急队伍 </td>';
						html +=     '      <td>'+c_em+'</td>';
						html +=      '      <td><a href="javascript:void(0)" class="send-a c_em" data-type="c_em" onclick="sendSms(this)">发送短信</a> </td>';
						html +=      '  </tr>';
						html +=      '  <tr class="clickremove">';
						html +=      '      <td>网格员</td>';
						html +=     '      <td>'+c_gridman+'</td>';
						html +=      '      <td><a href="javascript:void(0)" class="send-a c_gridman" data-type="c_gridman" onclick="sendSms(this)">发送短信</a> </td>';
						html +=      '  </tr>';
						html +=        ' <tr class="clickremove">';
						html +=       '      <th colspan="3"><a href="javascript:void(0)" class="send-a " data-type="" onclick="sendSms(this)">给所有人发送短信</a></th>';
						html +=      '  </tr>';
						$('.'+data.emAnalyzeObject[0].bean.AC_ID).find('table').append(html);
						
						
					}
				}
				if(data.emAnalyzeObject[0].bean.LIST_TYPE=='heatmap'){
					if(isFirstHeat==1){
						gmap.addDateToHeatRenderAndPoint(heatmapQUlist);
						isFirstHeat=2;
					}else{
						gmap.clearHeatMapData(heatmapQUlist);
					}
				}else{
					gmap.clearHeatMapData([]);
				}
			}
		}
	
}

//添加圆心
function addEnterPoint(){
	var strContent = '';
	console.info(enterObj);
	strContent += '应急类型:'+repType(enterObj.EM_TYPE)+"<br>";
	strContent += '情况描述:'+enterObj.MSG_DESC+"<br>";
    strContent += '地址信息:'+"<a href='javascript:void(0)' onclick='loadEditIfr(\""+contextPath+"/emergency/detail.do?id="+enterObj.MSG_ID+"\")'>"+enterObj.ADDRESS+"</a>"+"<br>";
	var pageName="biaozhu";
	var hoverPageNmae="biaozhu";
	var tip={"index":0,"title":"应急信息",content:strContent};
	var pointId=gmap.addPoint(circleXYx,circleXYy,tip,pageName,hoverPageNmae,clickHandler);
	var pointInfo={"id":pointId,"index":0};
	pointArray.push(pointInfo);
}

//清除点 热力图
function initPoint(){
	gmap.setPointID();
	gmap.closeInfoWindow();
	gmap.clearPoints();//清除地图上所有点
	var clearHeatList = [];
	heatmapQUlist=[];
	gmap.clearHeatMapData(clearHeatList);
}

//清除圆
function initCircle(){
	gmap.clearQueryCircle();
	gmap.clearPolygons();
}
