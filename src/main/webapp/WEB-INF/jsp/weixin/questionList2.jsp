<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>问题反映</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
    <meta name="description" content=" ">
    <link rel="stylesheet" href="<c:url value="/lib/weui2.0/css/weui.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/style/css/party/weui-individual.css"/>"><!-- weui框架特殊化样式 -->
    <link rel="stylesheet" href="<c:url value="/style/css/party/party.css"/>"><!-- 自定义补充样式 -->
</head>
<script src="<c:url value="/js/public.js"/>"></script>
<script src="<c:url value="/js/jquery/jquery-1.7.2.min.js"/>"></script>
<script>
urlConfig = {
		detailUrl : "<c:url value="/gzh/go_messageDetail.do"/>",
		getEmergencyList : "<c:url value="/gzh/getEmergencyList.do"/>",
		go_messageList:"<c:url value="/gzh/go_messageList.do"/>"
};
var ifOpen=2;
var vId;
var pageNO=1;
var val;
var title2 = '${title}'+"";
var messageAgent = '${messageAgent}';
var wx_userId = '${wx_userId}';
function getData(obj,pageNo,pageSize){
    var url = urlConfig.getEmergencyList+"?wx_userId=${wx_userId}&messageAgent=${messageAgent}";
	getAjaxData(url,val,pageNO,20,ifOpen);
}
function setData(data)
{
	data=data.data;
	var temp;
	var flag = false;
	if(data && data.list){
		temp = "";
		for(var i=0;i<data.list.length;i++){
			var titleSub = data.list[i].PROBLEM_TITLE;
			if(titleSub.length >17){
				titleSub = titleSub.substr(0,17)+"...";
			}
			temp +='<div class="weui-cells">';
			temp +='<a class="weui-cell c000" href="<c:url value="/gzh/go_messageDetail.do"/>?MSG_ID='+data.list[i].MSG_ID+'&messageAgent='+messageAgent+'&wx_userId='+wx_userId+'"> ';
			temp +='<div class="weui-cell__bd">';
			temp +='<p><b>'+titleSub+'</b></p></div>';
			temp +=' <div class="weui-cell__ft">';
			//判断处理状态
			if(data.list[i].DEL_STATUS=="0"){
				temp +='<i class="status-service-i status-service-i-nostart"></i></div></a>';
			}else if(data.list[i].DEL_STATUS=="6" || data.list[i].DEL_STATUS=="7" || data.list[i].DEL_STATUS=="8" || data.list[i].DEL_STATUS=="9" || data.list[i].DEL_STATUS=="10"){
				temp +='<i class="status-service-i status-service-i-end"></i></div></a>';
			}else{
				temp +='<i class="status-service-i status-service-i-start"></i></div></a>';
			}
			temp +='<div class="weui-cell"><div class="weui-cell__bd"><span class="item-title c999">问题描述</span>';	
			temp +='<p>'+data.list[i].MSG_DESC+'</p></div></div>';
			temp +='<div class="weui-cell"><div class="weui-cell__bd"><span class="item-title c999">问题发生位置</span>';
			temp +='<p>'+data.list[i].ADDRESS+'<i class="fa fa-map-marker"style="width:13px; color: #f64d00; margin-left: 3px"></i></p></div></div>';
			temp +='<div class="weui-cell"><div class="weui-cell__bd"><span class="item-title cblue">处理结果</span>';
			temp +='<p>'+data.list[i].MSG_REMARK+'</p><p class="c999 f15">受理人：'+(data.list[i].TRANS_NAME?data.list[i].TRANS_NAME:"")+'</p></div></div></div>';
			 
			flag = true;
		}
	} 
	if(!flag){
		$("#moreaa1").attr("style","display:none");
	}else{
		$("#moreaa1").attr("style","display:block");
	}
	if(data.list.length==0){
		temp = '<div class="weui-cells background-gray mt0" id="moreaa1"><a class="weui-cell weui-cell_access tcenter" href="javascript:;"><div class="weui-cell__bd"><p>没有更多数据了</p></div></a></div>';
	}
	/* if(pageNO==1){
		alert();
		var vv = $("#moreaa1").html();
		$("#setDiv").html(temp+"<div id=\"moreaa1\">"+vv+"</div>");
	} */
/* 	else{  */
		$("#moreaa1").before(temp);
	/* } */
	/* if(data.list.length==0){
		$("#moreaa1").attr("style","display:none");
	} */
	/* if(!flag){
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
		 */
}
</script>
<body ontouchstart class="background-gray" style="height: auto; min-height: 100%">
<div class="service-con ques-con" id="setDiv" style="padding-top:47px;padding-bottom:76px">
    <div class="service-con-sub background-gray">
        <!-- <span>反映列表</span> -->
        <div class="fr" style="padding-right:5px">
        	<a href="javascript:;" class="service-a service-a-on" id="all">所有反映</a>
        	<a href="javascript:;" class="service-a" id="my">我的反映</a>
        	<a href="javascript:;" class="service-a" id="gk">公开的反映</a>
        </div>
    </div>
    <div style="clear:both"></div>
    <c:forEach var="info" items="${infoList}">
    <div class="weui-cells">
        <a class="weui-cell c000" href="<c:url value="/gzh/go_messageDetail.do"/>?MSG_ID=${info.MSG_ID}&messageAgent=${messageAgent}&wx_userId=${wx_userId}"> 
            <div class="weui-cell__bd">
            <c:choose>
            <c:when test="${fn:length(info.PROBLEM_TITLE)>17}">
                <p><b>${fn:substring(info.PROBLEM_TITLE, 0, 17) }...</b></p>
             </c:when>  
             <c:otherwise>
            		<p><b>${info.PROBLEM_TITLE}</b></p>
            </c:otherwise>
             </c:choose> 
            </div>
            <div class="weui-cell__ft">
                <!--<span class="status-service cfff f16 status-service-slz">受理中</span>-->
               <c:choose>
               <c:when test="${info.DEL_STATUS == '0' or info.DEL_STATUS == '1'}">
                	<i class="status-service-i status-service-i-nostart"></i>
               </c:when>
               <c:when test="${info.DEL_STATUS == '6' or info.DEL_STATUS == '7' or info.DEL_STATUS == '8' or info.DEL_STATUS == '9' or info.DEL_STATUS == '10' or info.DEL_STATUS == '20'}">
                	<i class="status-service-i status-service-i-end"></i>
               </c:when>
               <c:otherwise>
            		<i class="status-service-i status-service-i-start"></i>
            	</c:otherwise>
              	</c:choose>
            </div>
        </a>
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <span class="item-title c999">问题描述</span>
                <p>${info.MSG_DESC}</p>
            </div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <span class="item-title c999">问题发生位置</span>
                <p>${info.ADDRESS}<i class="fa fa-map-marker"style="width:13px; color: #f64d00; margin-left: 3px"></i></p>
            </div>
        </div>
       <%--  <div class="weui-cell">
            <div class="weui-cell__bd">
                <span class="item-title c999">照片</span>
                <div class="weui-uploader">
                    <div class="weui-uploader__bd">
                        <ul class="weui-uploader__files">
                        <c:if test="${info.IMGADDRE != ''}">
                        	<img style=" width: 30%;margin: 5px 1.5%;float: left; height: 100px;"  src="..${info.IMGADDRE}" />
                           <!--  <li class="weui-uploader__file" style="background-image:url(./images/temp/u1451.png)"></li> -->
                     	</c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div> --%>
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <span class="item-title cblue">处理结果</span>
                <p>${info.MSG_REMARK}</p>
                <p class="c999 f15">受理人：${info.TRANS_NAME}</p>
            </div>
        </div>
    </div>
    </c:forEach>
			<div class="weui-cells background-gray mt0" id="moreaa1">
		        <a class="weui-cell weui-cell_access tcenter" href="javascript:;" onclick="pageNO++;getData(this)">
		            <div class="weui-cell__bd">
		                <p>加载更多</p>
		            </div>
		        </a>
		    </div>
</div>
<div class="btn-con tcenter btn-fixed-bottom background-gray" id="wentibutten">
    <button href="javascript:;" class="weui-btn weui-btn_primary" onclick="location.href='../gzh/go_EmergencyManage.do?openId=${openId}&type=${msgSource}&facilityId=${deviceNo}&userId=${userId}&appId=${appId}&loginUrl=${loginUrl}&phone=${phone}&comCode=${comCode}'">问题反映</button>
</div>
<!-- 图片预览 -->
<div class="weui-gallery" id="gallery">
    <span class="weui-gallery__img" id="galleryImg"></span>
    <div class="weui-gallery__opr">
        <a href="javascript:" class="weui-gallery__del">
            <i class="weui-icon-cancel weui-icon_gallery-cancel cfff"></i>
        </a>
    </div>
</div>
<script src="<c:url value="/js/jquery/jquery-1.9.1.min.js"/>"></script>
<script src="<c:url value="/js/party/public.js"/>"></script>
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
	$("#my").click(function(){
		$("#moreaa1").attr("style","display:none");
		var mod = '<div class="weui-cells background-gray mt0" id="moreaa1"><a class="weui-cell weui-cell_access tcenter" href="javascript:;" onclick="pageNO++;getData(this)"><div class="weui-cell__bd"><p>加载更多 </p></div></a></div>';
		$("#moreaa1").before(mod);
		$(this).removeClass("service-a-on").addClass("service-a-on").siblings().removeClass("service-a-on");
		ifOpen=0;
		pageNO=1;
		$("#setDiv").find(".weui-cells:not('.mt0')").remove();
		var url = urlConfig.getEmergencyList+"?wx_userId=${wx_userId}&messageAgent=${messageAgent}";
		
		getAjaxData(url,val,pageNO,20,ifOpen);
	});
	$("#all").click(function(){
		$("#moreaa1").attr("style","display:none");
		var mod = '<div class="weui-cells background-gray mt0" id="moreaa1"><a class="weui-cell weui-cell_access tcenter" href="javascript:;" onclick="pageNO++;getData(this)"><div class="weui-cell__bd"><p>加载更多 </p></div></a></div>';
		$("#moreaa1").before(mod);
		$(this).removeClass("service-a-on").addClass("service-a-on").siblings().removeClass("service-a-on");
		ifOpen=2;
		pageNO=1;
		$("#setDiv").find(".weui-cells:not('.mt0')").remove();
		var url = urlConfig.getEmergencyList+"?wx_userId=${wx_userId}&messageAgent=${messageAgent}";
		getAjaxData(url,val,pageNO,20,ifOpen);
	});
	$("#gk").click(function(){
		$("#moreaa1").attr("style","display:none");
		var mod = '<div class="weui-cells background-gray mt0" id="moreaa1"><a class="weui-cell weui-cell_access tcenter" href="javascript:;" onclick="pageNO++;getData(this)"><div class="weui-cell__bd"><p>加载更多</p></div></a></div>';
		$("#moreaa1").before(mod);
		$(this).removeClass("service-a-on").addClass("service-a-on").siblings().removeClass("service-a-on");
		ifOpen=1;
		pageNO=1;
		$("#setDiv").find(".weui-cells:not('.mt0')").remove();
		var url = urlConfig.getEmergencyList+"?wx_userId=${wx_userId}&messageAgent=${messageAgent}";
		getAjaxData(url,val,pageNO,20,ifOpen);
	});
    $(function(){
        var $gallery = $("#gallery"), $galleryImg = $("#galleryImg"),
            $uploaderFiles = $(".weui-uploader__files");
        $uploaderFiles.on("click", "li", function(){
            $galleryImg.attr("style", this.getAttribute("style"));
            $gallery.fadeIn(100);
        });
        $gallery.on("click", function(){
            $gallery.fadeOut(100);
        });
    });
</script>
</body>
</html>

