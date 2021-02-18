
function map_listAddPoints(list_data,clickHandler)//地图列表点绘制方法
{
	setTimeout(function(){
		gmap.setPointID();
		gmap.closeInfoWindow();
		gmap.clearPoints();//清除地图上所有点
		pointArray=[];
		for(var i=0;i<list_data_all.length;i++)//根据列表数据画点
    	{
    		if(typeof list_data_all[i].JD !="undefined"&&typeof list_data_all[i].WD !="undefined" &&list_data_all[i].JD!=""&&list_data_all[i].WD!=""){
    			var strContent = '';
    			strContent += "机构名称："+list_data_all[i].JGMC;
    			strContent += "<br>法人代表："+(list_data_all[i].FRXM==null?"":list_data_all[i].FRXM);
    			strContent += "<br>联系方式："+(list_data_all[i].DHHM==null?"":list_data_all[i].DHHM);
        		strContent += "<br>机构地址："+(list_data_all[i].JGDZ==null?"":list_data_all[i].JGDZ);
        		strContent += "<div style='float:right;cursor:pointer'  onclick='newWindow(\""+list_data_all[i].ID+"\")'>详情</div>";
        		var pageName="red";
        		var hoverPageNmae="blue";
        		var tip={"index":i,fireId:list_data_all[i].ID,"title":"",content:strContent};
        		var pointId=gmap.addPoint(list_data_all[i].JD,list_data_all[i].WD,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
    	}
		for(var i=0;i<list_data.length;i++){
			if(typeof list_data[i].JD !="undefined"&&typeof list_data[i].WD !="undefined" &&list_data[i].JD!=""&&list_data[i].WD!=""){
    			var strContent = '';
    			strContent += "机构名称："+list_data[i].JGMC;
    			strContent += "<br>法人代表："+(list_data[i].FRXM==null?"":list_data[i].FRXM);
    			strContent += "<br>联系方式："+(list_data[i].DHHM==null?"":list_data[i].DHHM);
        		strContent += "<br>机构地址："+(list_data[i].JGDZ==null?"":list_data[i].JGDZ);
        		strContent += "<div style='float:right;cursor:pointer' onclick='newWindow(\""+list_data[i].ID+"\")'>详情</div>";
        		
        		var pageCount=i+1;
        		var pageName="p"+pageCount;
        		var hoverPageNmae="ph"+pageCount;
        		var tip={"index":i,fireId:list_data[i].ID,"title":"",content:strContent};
        		var pointId=gmap.addPoint(list_data[i].JD,list_data[i].WD,tip,pageName,hoverPageNmae,clickHandler);
        		var pointInfo={"id":pointId,"index":i};
        		pointArray.push(pointInfo);
    		}
		}
		$("#allcount").html(list_data_all.length);
    	gmap.setExtentByshowPointGraphicsLayer();
    	setTimeout(function(){
    		var height = 10*gmap.getMapLevel()+1;
    		var top = 165-height;
    		$("#sliderHeight").height(height);
        	$("#sliderTop").css("top",top+"px");
    	},1500);
    	
    	//gmap.centerAtPoint(list_data_all[0].JD,list_data_all[0].WD,11);
    },1000);
}

//普通列表和地图列表的绑定生成
function doubleTableRender(clickHandler){
	 var inputdata = {"JGMC":$("#JGMC").val(),"JGLB":$("#JGLB").val()};
	 var requestData = {"JGMC":$("#JGMC").val(),"JGLB":$("#JGLB").val(),linesPerPage:10};
	 var imgurl=contextPath +"/js/gmap/images";
		var opts_map = {
		        basePath:contextPath +"/js/gmap/ulynlist",
		        url:contextPath+'/hospital/hospital_list.do',
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
		                     
		                     {field: '', label: '机构名称', className: "address", width: 50, bodyContent: "[JGMC]"},
		                     {
		                         field: '', label: '姓名', className: "sortable", width: 50,
		                         bodyContent: "<a href='javascript:Void(0)'>[FRXM]</a><span>[DHHM]</span>"
		                     }
		                     
		                 ] ,
		            rownumbers: false 
		        },
		        afterTableRender:function(){
		        	 $("#mapList li").click(function(){
		        		 
		        		 var index = $(this).index();
		        		 if(typeof list_data[index].JD !="undefined"&&typeof list_data[index].WD !="undefined" &&list_data[index].JD!=""&&list_data[index].WD!=""){
		        			 gmap.closeInfoWindow();
		        			 for(var i = 0 ; i < $("#mapList li").length;i++){
		        				 gmap.changePointToImg(i+list_data_all.length); 
		        			 }
		        			 gmap.changePointToHoverImg(pointArray[index].id+list_data_all.length);
		        			 gmap.centerAtPoint(list_data[index].JD,list_data[index].WD,13);
		        			 setTimeout(function(){
		        		    		var height = 10*gmap.getMapLevel()+1;
		        		    		var top = 165-height;
		        		    		$("#sliderHeight").height(height);
		        		        	$("#sliderTop").css("top",top+"px");
		        		    	},1500);
		        			 gmap.setPointToTop(pointArray[index].id+list_data_all.length);
		        		 }
		    			 
		    		});
		        	 
		        	 $.ajax({
		        	        type:"POST",
		        	        url:contextPath+'/hospital/hospital_list_all.do',
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
		        	        	list_data_all = data.data.list;
		        	        	map_listAddPoints(list_data,clickHandler);
		        	        }
		        		 });
		        	 var height = $(window).height()-$("#listMapLi").offset().top;
		        	 if($("#mapList").find("ul").height()>(height-$("#mapPager").height())){
		        		 $("#mapList").height((height-$("#mapPager").height())+"px");
		        	 }
		        },
		        requestData:requestData,
		        pageBarId: 'mapPager'
		    };
		$("#mapList").ulynlist(opts_map);
		var height = $(window).height()-$("#listMapLi").offset().top;
	    $("#listMapLi").height(height+"px");
	    $("#maplistdiv").height(height+"px");
}
function clickHandler(index,x,y,tip){
	//window.location.href=contextPath+'/fire/detail.do?fireId='+tip.fireId;
}

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}

function searchHospital(obj){
	doubleTableRender(null);
	
}

function newWindow(id) {
	var yoururl = detail.detailurl+"?id="+id;
	var height = $(window.parent.document).height();
	var width = $(window.parent.document).width();
	var top = (height-500)/2;
	var left = (width-700)/2;
	window.open(yoururl, '_blank','height=500,width=700,top='+top+',left='+left);
	}

$(function(){
    //侧边栏渲染
//    renderSidebar();
	
	doubleTableRender(null);
	
	$("#search-reset").click(function(){
		$("#JGMC").val('');
    });
});