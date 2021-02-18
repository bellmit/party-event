<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>居民问题反映详细</title>
<link href="<c:url value="/lib/scale/scale.css"/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value="/style/css/issue.css"/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value="/style/css/public.css"/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value="/style/css/problem_content.css"/>"
	rel="stylesheet" type="text/css">
<!-- Tablet (portrait and landscape) ----------->
<link rel='stylesheet' media='screen and (max-width:320px)'
	href="<c:url value="/style/css/login_min_betwen.css"/>" />
<link rel='stylesheet'
	media='screen and (min-width:321px) and (max-width:359px)'
	href="<c:url value="/style/css/login_min_360.css"/>" />
<link rel='stylesheet'
	media='screen and (min-width:360px) and (max-width:479px)'
	href="<c:url value="/style/css/login_min_560.css"/>" />
	
	<script src="<c:url value="/js/jquery/jquery-1.7.2.min.js"/>"></script>
 <script src="<c:url value="/lib/layer/layer.min.js"/>"></script>
  <script src="<c:url value="/lib/scale/scale.js"/>"></script>
<style type="text/css">
.agent {	
	width: 35%;
    height: 30px;
    color: #fff;
    font-size: 14px;
    font-family: '微软雅黑';
    cursor: pointer;
    border: none;
    background-color: #3498db;
    border-radius: 5px;
    margin:10px 5%;
}
.problem_cbody{
	font-family:'微软雅黑';
	}
.con-pj{
 	margin-left: 3%;
}


.pic_intrgoduce{
    margin-top: 15px;
    max-width: 350px;
    margin-left: 0px;
}

.pic_intrgoduce .info_introduce{
	max-width:250px;
}
.info_introduce span{
	font-size:14px;
	line-height: 28px;
}
.info_introduce .info_one{
	width:250px; 
	font-size: 14px;
}
.content_line_content p,.content_line_content p span{
	font-size:15px;
}
.info_introduce span code{
	width:80px;
	font-family:'微软雅黑';
}
.info_introduce span tt{
	font-family: '微软雅黑';
}
.info_introduce .info_from tt{
	max-width:130px;
}
.info_introduce .info_accepted tt{
	max-width:130px;
	/*white-space:nowrap;
	text-flow:ellipsis;*/
}
.info_introduce .info_time , .info_introduce .info_settime{
	width:250px;
} 

</style>
</head>
<body>
	<%--<div id="ss-loading"><span></span></div>--%>
	<!--cbody beign-->
	<div class="problem_cbody">
		<!--problem_top-->
		<div class="problem_top clearfix">

			<!--problem_top_pic-->
			<div class="problem_top_pic">
				<span class="pic_show primary">
				
				<c:choose>
					<c:when test="${not empty picList}">
						<img src="..${picList[0].IMAGE_ADDRESS}" />
					</c:when>
					<c:otherwise>
						<img src="../images/map-ico.png" />
					</c:otherwise>
				</c:choose>
				
				</span> <i>
					<c:choose>
						<c:when test="${flagtant eq '6' or flagtant eq '7' or flagtant eq '8'or flagtant eq '9'or flagtant eq '10'}">已回复</c:when>
						<c:when test="${flagtant eq '1' or flagtant eq '2' or flagtant eq '3' or flagtant eq '4' or flagtant eq '5' }">待回复</c:when>
						<c:when test="${flagtant eq '20'}">不予受理</c:when>
						<%-- <c:when test="${flagtant eq '0'  }">未处理</c:when> --%>
						<c:otherwise>未办理</c:otherwise>
					</c:choose>
				</i>
			</div>
			<!--problem_top_pic-->

			<!--pic_intrgoduce-->
			<div class="pic_intrgoduce">
				<%--  <h3>${issue.issueTitle}</h3> --%>
				<div class="clearfix">
					<div class="info_introduce clearfix">
						<span class="info_from info_one"> <code>来 自：</code>
							<tt>${emInfo[0].USER_NAME}</tt>
						</span>
					</div>
					<div class="info_introduce clearfix">
						<span class="info_time"> <code>提交时间：</code>
							<tt>${time}</tt>
						</span>
					</div>
				</div>
				<div class="clearfix">
					<%--   <div class="info_introduce clearfix">
                    <span class="info_accepted info_one">
                        <code>办理机构：</code><tt>${emInfo[0].handleDept}</tt>
                    </span>
                </div> --%>

					<!--                 <div class="info_introduce clearfix"> -->
					<!--                     <span class="info_from info_one"> -->
					<%--                         <code>地理位置：</code><tt>${issue.issueAddress}</tt> --%>
					<!--                     </span> -->
					<!--                 </div> -->
				</div>
				<%--  <c:if test="${issue.issueCheckStatus=='待评价'}">
            <div class="clearfix">
                <div class="info_introduce clearfix">
                    <span class="info_accepted info_one">
                    	<button class="button_issue" onclick="goEval()">评价</button>
                    </span>
                </div> 
            </div> 
            </c:if> --%>
			</div>

		</div>
		<!--problem_top-->
		<p class="bg_triangle">
			<i></i>
		</p>

		<c:if test="${not empty Messagetant}">
			<c:set var="setDesc" value="0"></c:set>
			<!--problem_content-->
			<div class="problem_content" style="margin-bottom: 20px;">
				<c:forEach items="${Messagetant}" var="map" varStatus="map_vs">
					<!--content_listone-->
					<div class="content_list">
						<h5>${map.key}</h5>
						<c:forEach items="${map.value}" var="item" varStatus="vs">
							<!--content_lineone-->
							<div class="content_line content_lineone clearfix">
								<span class="problem_submit content_line_span">${item.time}</span>
								<div class="content_line_content">
									<i></i>
									<p class="content_line_content_title">
										<code>${item.stepName}</code>
									</p>
									<p>${item.ms}</p>
									<c:if test="${map_vs.index==0 and setDesc=='0'}">
									
									<div class="primary"  >
										<c:forEach items="${picList}" var="pics" varStatus="pstatus">
													   <img style=" width: 30%;margin: 5px 1.5%;float: left; height: 100px;"  src="..${pics.IMAGE_ADDRESS}" /> 
											   <!--   <img    style="width: 98%; margin: 5px 2%;" src="http://www.5djiaren.com/uploads/2016-08/17-164008_81.jpg" />   -->
										</c:forEach>
									</div>
												
												
										<c:set var="setDesc" value="1"></c:set>
									</c:if>
								</div>
							</div>
							<!--content_lineone-->
						</c:forEach>
					</div>
					<!--content_listone-->
				</c:forEach>
			</div>
			<div class="problem_content" style="margin-bottom: 20px;">
				<c:forEach items="${laterMap}" var="map" varStatus="map_vs">
					<!--content_listone-->
					<div class="content_list">
						<h5>后续办理流程</h5>
						<h5>${map.key}</h5>
						<c:forEach items="${map.value}" var="item" varStatus="vs">
							<!--content_lineone-->
							<div class="content_line content_lineone clearfix">
								<span class="problem_submit content_line_span">${item.time}</span>
								<div class="content_line_content">
									<i></i>
									<p class="content_line_content_title">
										<code>${item.stepName}</code>
									</p>
									<p>${item.ms}</p>
									<c:if test="${map_vs.index==0 and setDesc=='0'}">
									
									<div class="primary"  >
										<c:forEach items="${picList}" var="pics" varStatus="pstatus">
													   <img style=" width: 30%;margin: 5px 1.5%;float: left; height: 100px;"  src="..${pics.IMAGE_ADDRESS}" /> 
											   <!--   <img    style="width: 98%; margin: 5px 2%;" src="http://www.5djiaren.com/uploads/2016-08/17-164008_81.jpg" />   -->
										</c:forEach>
									</div>
												
												
										<c:set var="setDesc" value="1"></c:set>
									</c:if>
								</div>
							</div>
							<!--content_lineone-->
						</c:forEach>
					</div>
					<!--content_listone-->
				</c:forEach>
			</div>
			<!--problem_content-->
		</c:if>
		<!-- <br /> -->
		<c:if test="${messageAgent eq 'messageAgent' and emInfo[0].COUNTT eq 0 and emInfo[0].TRAN_STATUS eq '0' }">
			<div class="con-pj">
			<div><span style=" color: #000; font-size:18px;">领导批示：</span></div>
			<div>
				<textarea rows="4" id="suggestion" placeholder="写下您的处理意见" style="width:96%; border-radius:5px; margin-top:5px;font-family:'微软雅黑';"></textarea>
			</div>
			<!-- 两个按钮 -->
			<div>
				<div>
					<input class="agent" id="yz_button" onclick="saveInfo('1')" type="button"
						value="阅知"  /> 
						<input class ="agent" id="ps_button" onclick="saveInfo('5')" type="button" value="批示"
						 />

				</div>
			</div>
		</div>
		</c:if>
	</div>
	<!--cbody beign-->

</body>
<script>
//手机点返回时，防盗链验证链接
$(function(){  
	pushHistory();  
	window.addEventListener("popstate", function(e) {  
		location.href='<c:url value="/gzh/go_messageList.do"/>';
	}, false);  
	function pushHistory() {  
		var state = {  title: "title",  url: "#" };  
		window.history.pushState(state, "title", "#"); 
	}  
}); 

var MSG_ID = '${MSG_ID}';
var wx_userId = '${wx_userId}';

var tempFlag = false;
	function saveInfo(yj) {
		var loadi;
		var suggestion = $("#suggestion").val();
		 if('5'==yj && suggestion == ''){
			 alert("对不起，您未填写任何批示意见!");
			 return;
		 }
		 var url = "<c:url value="/qyh/insertMessage.do"/>";
		 var inputdata={
				 "TRAN_STATUS":yj,
				 "suggestion":suggestion,
				 "wx_userId":wx_userId,
				 "MSG_ID":MSG_ID
				 };
		 if(tempFlag){
			 alert("正在提交，请稍候...");
			 return;
		 }
		 tempFlag = true;
		 
		 $("#yz_button").attr("disabled", true); 
		 $("#ps_button").attr("disabled", true); 
	   	 $.ajax({
	            type:"POST",
	            url: url,
	            data:inputdata,
	            dataType:"json",
	            async : false,
	            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	            success:function(data){
	        		 layer.close(loadi);
	            	 tempFlag = false;
	            	console.info(data);
	            	if(data.result == "sucess"){
	            		alert("提交成功！");
	            		// window.location.reload();//刷新当前页面.
	            		window.location.href=window.location.href;
	            	}else if(data.result == "exit"){
	            		setNoDisabled();
	            		alert("提交失败！您的处理意见已存在，不可重复提交");
	            	}else if(data.result == "incompleteInfo"){
	            		setNoDisabled();
	            		alert("提交失败！您的初始化数据不完整，请联系管理员进行处理");
	            	}else if(data.result == "sjygb"){
	            		setNoDisabled();
	            		alert("本事件已处理，提交失败！");
	            	}else{
	            		setNoDisabled();
	            		alert("提交失败");
	            	}
	            	
	            },
	            beforeSend:function(){
	            	 loadi=layer.load('正在提交，请稍候...');
	            },
	            error : function(text)
	            {
	            	 layer.close(loadi);
	            	 $("#yz_button").attr("disabled", true); 
	        		 $("#ps_button").attr("disabled", true); 
	            	 tempFlag = false;
	            	alert("系统异常，请稍后提交！");
	            }
	   	 });
	}
	
	function setNoDisabled(){
		
		 $("#yz_button").attr("disabled", false); 
		 $("#ps_button").attr("disabled", false); 
	}
	
	 
	 document.addEventListener("DOMContentLoaded", function(event) {
		    ImagesZoom.init({
		      "elem": ".primary"
		    });
		  }, false);
</script>
</html>