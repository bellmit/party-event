<!--
 版权所有：厦门畅享信息技术有限公司
 Copyright 2014 Xiamen Sunsharing Information CO., LTD.
 All right reserved. 
====================================================
 文件名称: text.html
 修订记录：
 No    日期				作者(操作:具体内容)
 1.    14-10-29			Administrator(创建:创建文件)
====================================================
 文件描述：(说明本文件需要实现的内容)
 
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tlu" uri="/tagelutil"%>
<!DOCTYPE html>
<meta charset=utf-8>
<html lang="zh-cn">
<head>
    <title>应急信息处理详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"  />
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>"/>
    <%-- <link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-3.5.1/select2.css"/>"/> --%>
    
	<link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-4.0.2/dist/css/select2.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/sslib/qlTree/qlTree.css"/>"/>
    <link href="<c:url value="/styles/css/flat/blue.css"/>" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/base.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/nineModules.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/detail.css"/>"/>

    <script type="text/javascript" src="<c:url value="/lib/jquery/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <%-- <script type="text/javascript" src="<c:url value="/lib/select2-3.5.1/select2.js"/>"></script> --%>
    <script type="text/javascript" src="<c:url value="/lib/select2-4.0.2/dist/js/select2.full.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/artTemplate/template-simple.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.pagebar.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/qlTree/qlTree.js"/>"></script>

<script src="<c:url value="/lib/layer/layer.min.js"/>"></script>
<script src="<c:url value="/lib/layer/extend/layer.ext.js"/>"></script>

    <script type="text/javascript" src="<c:url value="/js/icheck.min.js"/>"></script>
    <script type="text/javascript">
    //获取当前问题的最后处理状态
        //单选框样式
        $(document).ready(function(){
    		$(".select2").select2();
    		
        	$("input[type='radio']").on('ifChecked', function(event){  
//         		  alert(event.type + ' callback');
					userShowHide($(this).val());
        		}); 
            $('input').iCheck({
                checkboxClass: 'icheckbox_flat-blue',
                radioClass: 'iradio_flat-blue'
            });
//             formatspan("${map1['REPORT_TIME'] }","reportTime");
//             formatspan("${map1['REPORT_TIME'] }","tableTime1");
//             formatspan("${map1['REPORT_TIME'] }","tableTime2");
            
//             formatspan($("#timespan-jd").html(),"timespan-jd");
//             formatspan($("#timespan-xf").html(),"timespan-xf");
            $(".emType").html(repValue($(".emType").html()));
        });

        var EndTime= new Date();
        EndTime.setDate(EndTime.getDate()+5);
        function GetRTime(){
        	
            
            var NowTime = new Date();
            var t =EndTime.getTime() - NowTime.getTime();
            var d=Math.floor(t/1000/60/60/24);
            var h=Math.floor(t/1000/60/60%24);
            var m=Math.floor(t/1000/60%60);
            var s=Math.floor(t/1000%60);

            document.getElementById("t_d").innerHTML = d;
            document.getElementById("t_h").innerHTML = h;
            document.getElementById("t_m").innerHTML = m;
            document.getElementById("t_s").innerHTML = s;
        }
        if('${show}'=='show'){
        	setInterval(GetRTime,0);	
        }
        typelist=[];
    	<c:forEach items="${typelist }" var="type_item">
    	typelist.push({id:"${type_item.TYPE_ID}",name:"${type_item.TYPE_NAME}"});
    	</c:forEach>
    	
    	 trutse=[];
     	<c:forEach items="${emTranList }" var="trutse">
     	trutse.push({id:"${trutse.TRAN_ID}",name:"${trutse.TRAN_STATUS}"});
     	</c:forEach>
   //  	alert(trutse);
    	</script>
</head>
<body>
<div class="content-box">
    <div class="cb-content" style="margin-left:0px">
        <div class="content">
            <div class="mainContent" style="top:0px">
                <div class="mcBox">
                    <div class="dwBottom" style="margin-top:0">
                        <!-- 处理详情 -->
                        <div class="detail2">
                            <b class="tit">问题受理详情</b>
                            <table>
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <tr>
                                	<td style="display:none">应急类型：<span class="sty emType">${emBean.emType }</span></td>
                                    <td>问题标题：${emBean.problemTitle}</td>
                                    <td>反映时间：${tlu:tranTimeStr(emBean.createTime) }</td>
                                </tr>
                                <tr>
                                    <td>反映人员：${emUser.USER_NAME==null? emBean.rpUsername : emUser.USER_NAME}</td>
                                    <td>联系方式:${emBean.msgPhone }</td>
                                </tr>
                                <tr>
                                    <td colspan="2">描&nbsp;&nbsp;述：${emBean.msgDesc }</td>
                                </tr>
                                <tr>
                                    <td colspan="2">反映位置：${emBean.address}</td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <span class="fl">相关照片：
                                        <c:if test="${fn:length(emImgList)!=0}"><br/><font color="#428BCA">(点击图片<br/>查看大图)</font>
                                        </c:if>
                                        </span>
                                        <div class="fl imgDiv" id="imagetd">
                                            
                                            <c:if test="${fn:length(emImgList)!=0}">
				                                    <c:forEach items="${emImgList }" var="photo" varStatus="status">
				                                    <a href="<c:url value="/emergency/goImg.do?imgpath=${photo['IMAGE_ADDRESS'] }"/>"
														target="_blank">
<%-- 				                                    <img src="<c:url value="/static/image${photo['IMAGE_ADDRESS'] }"/>" onclick="bigMap(${status.index})" class="img-rounded" title="点击查看大图"> --%>
				                                    <img src="<c:url value="${photo['IMAGE_ADDRESS'] }"/>" class="img-rounded" title="点击查看大图">
				                                    </a>
				                                    </c:forEach>
		                                    </c:if>
		                                    <c:if test="${fn:length(emImgList)==0}">
		                                    	无
		                                    </c:if>
                                            
                                            
                                            
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            
                            <c:set var="isDisplay" value="0"/>
                            <c:set var="depLevel" value="3"/>
                            <c:set var="depName" value=""/>
                            <c:set var="rpFlag" value="0"/>
                            <c:set var="xbFlag" value="0"/>
                            <c:set var="bjFlag" value="0"/>
                           	<c:forEach var="depMap" items="${depList}">
	                            <c:set var="depLevel" value="${depMap.DEP_LEVEL }"/>
	                            <c:set var="depName" value="${depMap.DEP_NAME }"/>
                           		<%-- <c:if test="${depMap.DEP_LEVEL=='3' and emBean.rpStatus=='00'}">
		                            <c:set var="isDisplay" value="1"/>
                           		</c:if>
                           		<c:if test="${depMap.DEP_LEVEL=='4'}">
                           		<c:if test="${emBean.rpStatus=='11' or emBean.rpStatus=='01'}">
		                            <c:set var="isDisplay" value="1"/>
                           		</c:if> 
                           		</c:if>--%>
                           	</c:forEach>
                           	
                            <c:choose>
                            	<c:when test="${depLevel eq '5'}">
                             		<c:set var="isDisplay" value="1"/>
                             	</c:when>
                             	<c:when test="${(emBean.msgLevel == '3' and depLevel eq '3') or ((emBean.msgLevel == '3,4' or emBean.msgLevel == '4') and depLevel eq '4')}">
                             		<c:set var="isDisplay" value="1"/>
                             		<c:set var="rpFlag" value="1"/>
                             		<c:set var="bjFlag" value="1"/>
                             	</c:when>
                             	<c:when test="${(emBean.msgLevel == '3,4' or emBean.msgLevel == '3,4,5') and depLevel eq '3'}">
                             		<c:set var="isDisplay" value="1"/>
                             		<c:set var="xbFlag" value="1"/>
                             	</c:when>
                             	<c:when test="${(emBean.msgLevel == '3,4,5' or emBean.msgLevel == '4,5') and depLevel eq '4'}">
                             		<c:set var="isDisplay" value="1"/>
                             		<c:set var="bjFlag" value="1"/>
                             		<c:set var="xbFlag" value="1"/>
                             	</c:when>
                             	<c:otherwise>
                             		<c:set var="isDisplay" value="0"/>
                             	</c:otherwise>
                             </c:choose>
                            	 
                            <b class="tit">问题受理流程</b>
                            <table class="tc">
                                <colgroup>
                                    <col width="5%">
                                    <col width="15%">
                                    <col width="5%">
                                    <col width="15%">
                                    <col width="30%">
                                </colgroup>
                                <tr>
                                    <th>步骤</th>
                                    <th>处理时间</th>
                                    <th>处理名称</th>
                                    <th>处理人</th>
                                    <th>处理意见</th>
                                </tr>
                                <c:forEach var="emTranInfo" items="${emTranList}" varStatus="emIndex">
                                <tr>
                                    <td>${emIndex.index+1}</td>
                                    <td>${tlu:tranTimeStr(emTranInfo.CREATE_TIME)}</td>
                                    <td>${emTranInfo.TRAN_STATUS=='0'?"问题反映":emTranInfo.TRAN_STATUS=='1'?"已阅知":emTranInfo.TRAN_STATUS=='2'?"已转物管"
                                    :emTranInfo.TRAN_STATUS=='3'?"续报":emTranInfo.TRAN_STATUS=='4'?"已处理"
                                    :emTranInfo.TRAN_STATUS=='5'?"已协办":emTranInfo.TRAN_STATUS=='6'?"已关闭":emTranInfo.TRAN_STATUS=='7'?"社区同意公开"
                                    :emTranInfo.TRAN_STATUS=='8'?"社区不同意公开"
                                    :emTranInfo.TRAN_STATUS=='9'?"镇街同意公开"
                                    :emTranInfo.TRAN_STATUS=='10'?"镇街不同意公开":"未知"}
                                    </td>
                                    <td>${emTranInfo.USER_NAME}[${emTranInfo.TRAN_DEP}]</td>
                                    <td>${emTranInfo.MSG_REMARK}</td>
                                </tr>
                                <c:if test="${emTranInfo.TRAN_STATUS eq '6'}">
                         		<c:set var="isDisplay" value="0"/>
                         		</c:if>
                                </c:forEach>
                            </table>
                                    <input type="hidden" id="depName" name="depName" value="${depName}">
                                    <input type="hidden" id="depLevel" name="depLevel" value="${depLevel}">
                                    
                                <c:if test="${(sqsh && last_status==6) ||(zjsh && last_status==7 )}">
	                                    <div class="clearfix" id="biaoti">
			                                <b class="tit fl">审核意见</b>
                            			</div>
                                <div class="subItem">
	                                    <div class="deal-con skin-section">
	                                        <ul class="list">
	                                            <div class="clear"></div>
	                                        </ul>
	                                        <ul class="list-style" id="messageTypeUL">
	                                        	<div class="clear"></div>
	                                        </ul>
	                                        <textarea id="textjd" placeholder="请填写审核意见" style="width: 100%; height: 50px; border: 0; padding: 5px;vertical-align: bottom;color:gray"></textarea>
	                                    </div>
	                                </div>
	                                </c:if>
	                                <div class="cl"></div>
	                                <div class="cl userDepDiv1 hide"></div>
	                                <c:if test="${(sqsh && last_status==6) ||(zjsh && last_status==7)}">
                                		<div class="btns tc" id="anniu" style="margin: 5px 0">
	                                        <button type="button" id="btn"  class="btn btn-sm btn-primary" onclick="go_process(0)" name="flat-radio" value="0">同意公开</button>
	                                         <button type="button" id="btn"  class="btn btn-sm btn-primary" onclick="go_process(1)" name="flat-radio" value="1">不同意公开</button>
	                                        <a href="javascript:closeWindow();">
	                                        </a>
	                                 	</div>
	                                 </c:if>
                                <c:if test="${isDisplay!='1' or emBean.tranStatus != '0'}">
                           		<div class="btns tc" style="margin: 10px 0" id="btnReturn">
                                    <a href="javascript:closeWindow();">
                                    <button type="button" class="btn" style="margin-left: 20px" >返回列表</button>
                                    </a>
                             	</div>
                             	</c:if>
                        </div>
                        
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
if(${emTranList[emTranList.size()-1].get("TRAN_STATUS")}=='5' && ${depLevel}=='5'){
	$("#textjd").hide();
	$("#biaoti").hide();
	$("#anniu").hide();
	/* $("#btnReturn").hide(); */
}
if(${emBean.emType } != null){
	$("#messageTypeUL").hide();
}
var urlConfig = {
		reviewMessage : '<c:url value="/emergency/reviewMessage.do"/>',
		modifyRK:"<c:url value="/emergency/modifyRK.do"/>"
	};

function go_process(agree){
	var messageType=$("#messageType").val();
	if(${emBean.emType } != null){
		messageType='${emBean.emType }';
	}
	 var textjd = $("#textjd").val();
	 if(textjd==""){
		 alert($("#textjd").attr("placeholder"));
		 return;
	 }
	 if(textjd.length>250){
		 alert("填写字数最多为250个");
		 return;
	 }
	 var wxUserIds = "";

	 var inputdata={"msgId":"${emBean.msgId}",
			 		"textjd":encodeURI(textjd),
			 		"status":agree,
			 		"depName":encodeURI($("#depName").val()),
			 		"depLevel":$("#depLevel").val(),
			 		"wxUserIds":wxUserIds,
			 		"messageType":messageType+"",
			 		"last_status":"${last_status}"
			 		};
	 $.ajax({
       type:"POST",
       url:urlConfig.reviewMessage,
       data:inputdata,
       dataType:"json",
       success:function(data){
   		if(data && data.status){
    	   alert("处理成功");
    	   window.location.href=window.location.href;
// 			closeWindow();
   		}else{
   			alert(data.msg);
   			window.location.href=window.location.href;
   		}
       },
       beforeSend:function(){
       },
       error:function(){
    	   alert("处理失败");
		}
	 });
}
function go_rk(){
	var inputdata={"id":"${emBean.msgId}",
	 		"rkStatus":"${emBean.rkStatus}",
	 		"depLevel":$("#depLevel").val()
	 		};
	$.ajax({
	type:"POST",
	url:urlConfig.modifyRK,
	data:inputdata,
	dataType:"json",
	success:function(data){
		if(data && data.status){
	    	   alert("处理成功");
			window.location.href=window.location.href;
		}
	},
	beforeSend:function(){
	},
	error:function(){
	   alert("处理失败");
	}
	});
}
$(document).ready(function(){
	$("input[name=flat-radio]").change(function(){
		userShowHide($(this).val());
		 });
	 if("${hasRK}"=='1'){
		 if(confirm("是否将问题置为“已阅知”")){
			 go_rk();
		 }else{
			 $("#rk").removeClass("hide");
		 }
	 }else{
			$("#btn").removeAttr("disabled");
	 }
});
function userShowHide(v){
	$(".userDepDiv").addClass("hide");
	$(".userDepDiv1").addClass("hide");
	if((v=="2"||v=="3") && "${depLevel}"=="4"){
		$(".userDepDiv").removeClass("hide");
	 	$(".userDepDiv1").removeClass("hide");
	}
}

function bigMap(index){
	var opt = {
			page: { //直接获取页面指定区域的图片，他与上述异步不可共存，只能择用其一。
		        parent: '#imagetd',  //图片的父元素选择器，如'#imsbox',
		        start: index, //初始显示的图片序号，默认0
		        title: '大图' //相册标题
		    }
	};
	layer.photos(opt);
}

function formatpc(str){
	var strTime = '';
	strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	$("#REPORT_TIME").val(strTime);
}

function formatspan(str,id){
	var strTime = '';
	if(str!=''&&str!=null){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
		$("#"+id).html(strTime);
	}
}

function clearText(obj){
// 	if($(obj).val()=='写下您的处理意见'){
// 		$(obj).val("");
// 	}
}

function returnText(obj){
// 	if($(obj).val()==''){
// 		$(obj).val("写下您的处理意见");
// 	}
}
function closeWindow() {
// 	parent.location.reload();
	parent.onloadWindow();
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(index);	
}
function repValue(value){
	var typelist1 = typelist;
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

</script>

</body>
</html>