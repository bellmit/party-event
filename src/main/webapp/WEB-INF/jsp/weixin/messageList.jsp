<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<title>居民问题反映列表</title>
<link href="<c:url value="/style/css/weixin.css"/>" rel="stylesheet">
<script src="<c:url value="/js/jquery/jquery-1.7.2.min.js"/>"></script>
<script src="<c:url value="/js/public.js"/>"></script>
<style type="text/css">
	.more-cell-con li{padding:5px 0}
</style>
</head>
<script>
urlConfig = {
		detailUrl : "<c:url value="/gzh/go_messageDetail.do"/>",
		getEmergencyList : "<c:url value="/gzh/getEmergencyList.do"/>"
};

var vId;
var pageNO=1;
var val;
var title2 = '${title}'+"";
var messageAgent = '${messageAgent}';
var wx_userId = '${wx_userId}';
function getData(obj,pageNo,pageSize){
	val = $(".search-text").val();
	
    var url = urlConfig.getEmergencyList+"?wx_userId=${wx_userId}&messageAgent=${messageAgent}";
	
	getAjaxData(url,val,pageNO,20);
}
 
function setData(data)
{
	var temp;
	var flag = false;
	if(data && data.list){
		temp = "";
		for(var i=0;i<data.list.length;i++){
			var titleSub = data.list[i].MSG_DESC ;
			if(titleSub.length >8){
				titleSub = titleSub.substr(0,9);
			}
			//状态设置
			var em_status = "<span>";
			if(null == messageAgent || "" == messageAgent){//我的列表
				em_status += data.list[i].TRAN_STATUS == '0'?"<i class='dcl'></i>【未处理】":"<i class='ycl'></i>【已处理】";
			}else{//领导列表
				if(data.list[i].TRAN_STATUS == '1'){
					em_status +="<i class='ycl'></i>【已处理】";
				}else{
				em_status += data.list[i].COUNTT !=0 ?"<i class='ycl'></i>【已批示】":data.list[i].TRAN_STATUS == '0'?"<i class='dcl'></i>【未处理】":"<i class='ycl'></i>【已处理】";
				}
			}
			em_status += "</span>";
				temp = (i<22?
						temp+("<li><a href=\""+urlConfig.detailUrl+"?MSG_ID="+data.list[i].MSG_ID+"&messageAgent="+messageAgent+"&wx_userId="+wx_userId+"\">"
						+em_status+titleSub+"<span style='float: right; position:relative; top:7px; margin-right:5px'>"+ setDate(data.list[i].CREATE_TIME)+"</span></a> </li>")
						:temp+"");
			 
			
			flag = true;
		}
	}
	if(!flag){
		$("#moreaa1").attr("style","display:none");
	}else{
		$("#moreaa1").attr("style","display:block");
	}
	if(data.list.length==0){
		temp = "<li style='display:none'>没有更多数据了</li>";
	}
	if(pageNO==1){ 
		var vv = $("#moreaa1").html();
		$(".more-cell-con").html(temp+"<div id=\"moreaa1\">"+vv+"</div>");
	}
	else{ 
		$("#moreaa1").before(temp);
	}
	if(data.list.length==0){
		$("#moreaa1").attr("style","display:none");
	}
		
}
function setDate(time){
	if(time.length>5){
		time =/*  time.substring(0,4)+"-"+ */time.substring(5,10)/* +"-"+time.substring(6,8) */;
	}
	return time;
}

</script>
<body>
<div id="wrap"> 
  <section id="list">
  	<div class="more-con">
        <div class="more-cell">
            <div class="more-cell-tit">
               <!--  <h2>应急采集信息列表</h2> -->
                <!-- <a href="javascript:" class="search-a"></a> -->
                <div class="search-con">
                    <input type="text" class="search-text" placeholder="输入关键字">
                    <button class="search-btn" onclick="pageNO=1;getData(this)">搜索</button>
                </div>
                <div class="">
    				<button href="javascript:;" class="weui-btn weui-btn_primary" onclick="location.href='../gzh/go_EmergencyManage.do?openId=${wx_userId}&type=${msgSource}&facilityId=${deviceNo}&userId=${userId}&appId=${appId}&loginUrl=${loginUrl}'">我要反映问题</button>
				</div>
            </div>
            <c:if test="${size ne 0 }">
            <ul class="more-cell-con">
            
         
            	<c:forEach var="info" items="${infoList}">
            		<li>
            		<a href="<c:url value="/gzh/go_messageDetail.do"/>?MSG_ID=${info.MSG_ID}&messageAgent=${messageAgent}&wx_userId=${wx_userId}"> 
            		<span> <c:choose>
            			<c:when test="${empty messageAgent and info.TRAN_STATUS == '0'}">
			            		<i class='dcl'></i>【未处理】
            			</c:when>
            			<c:when test="${info.TRAN_STATUS == '1' }">
            				<i class='ycl'></i>【已处理】
            			</c:when>
            			<c:when test="${info.COUNTT != '0' }">
            				<i class='yps'></i>【已批示】
            			</c:when>
            			<c:otherwise>
            				<i class='dcl'></i>【未处理】
            			</c:otherwise>
            		</c:choose></span>${fn:substring(info.PROBLEM_TITLE, 0, 8) }<span  style="float: right; position:relative; top:7px; margin-right:5px">${fn:substring(info.CREATE_TIME, 5, 10) }</span></a> </li>
            	</c:forEach>
            	
            	
            <c:choose>
	            <c:when test="${ size le 20 }">
	          	   <div id="moreaa1" style="display:none">	<li class="more-li-a"><a href="javascript:void(0)">没有更多数据了</a></li></div> 
	            </c:when>
				<c:otherwise>
		            <div id="moreaa1">	<li class="more-li-a"><a href="javascript:void(0)" onclick="pageNO++;getData(this)">加载更多</a></li></div>
				</c:otherwise>
            </c:choose>
            
            </ul>
            </c:if>
        </div>
  	</div>
  </section>
</div>
</body>

<script>

//自动刷新
var totalheight = 0; 

function loadData()
{ 
  totalheight = parseFloat($(window).height()) + parseFloat($(window).scrollTop()); 

  if ($(document).height() <= totalheight) 
  { 
	  var tt = $(".more-con").html();
	  
      //加载数据
       if( tt.indexOf("没有更多数据了")==-1 ){ 
          $("#moreaa1 a").click();
       } else if($("#moreaa2").val()!= null){
    	  $("#moreaa2 a").click();
      }  
      
      
  } 
} 

$(window).scroll( function() { 
  console.log("滚动条到顶部的垂直高度: "+$(document).scrollTop()); 
  console.log("页面的文档高度 ："+$(document).height());
  console.log('浏览器的高度：'+$(window).height());
  loadData();
}); 
</script>
</html>