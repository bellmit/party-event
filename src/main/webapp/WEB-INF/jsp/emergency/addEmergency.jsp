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
    <title>应急信息采集</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"  />
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-3.5.1/select2.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/sslib/qlTree/qlTree.css"/>"/>
    <link href="<c:url value="/styles/css/flat/blue.css"/>" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/base.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/nineModules.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/detail.css"/>"/>

    <script type="text/javascript" src="<c:url value="/lib/jquery/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/select2-3.5.1/select2.full.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/artTemplate/template-simple.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.pagebar.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/qlTree/qlTree.js"/>"></script>
	<script src="<c:url value="/js/upload/ajaxfileupload.js"/>"></script>
<%-- 	<script src="<c:url value="/lib/layer/layer.min.js"/>"></script> --%>
	<script src="<c:url value="/lib/layer-3.1.1/dist/layer.js"/>"></script>
	<%-- <script src="<c:url value="/lib/layer/extend/layer.ext.js"/>"></script> --%>

    <script type="text/javascript" src="<c:url value="/js/icheck.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/page_js/utiles.js"/>"></script>
    <script type="text/javascript">
        //单选框样式
        $(document).ready(function(){
            $(".select2").select2({tags: true,
				  tokenSeparators: [',', ' ']});
            $("#msgImg").on("change", function() {
    			var filePath = $("#msgImg").val();
    			// filepathVAL(filePath);
    			ajaxupload('msgImg');
    		});
        });
    	</script>
</head>
<body>
<div class="content-box">
    <div class="cb-content" style="margin-left:0px">
        <div class="content">
            <div class="mainContent" style="top:0px;background: #fff;">
                <div class="mcBox">
                    <div class="dwBottom" style="margin-top:0;padding-top:13px">
                        <!-- 处理详情 -->
                        <div class="detail2">
                            <table>
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <tr>
                                    <td><span style="float: left; margin-top: 4px;">事件类型：</span>
                                    		<select class="select2 fr" multiple="multiple" data-placeholder="请选择应急事件类型" style=" min-width: 250px;display:none">
												<option value="">请选择</option>
												<c:set var="gName" value="0" />
												<c:forEach items="${typelist }" var="type_item">
													<c:if test="${gName ne type_item.GROUP_NAME}">
														<c:if test="${gName!='0'}">
															</optgroup>
														</c:if>
														<c:set var="gName" value="${type_item.GROUP_NAME}" />
														<optgroup label="${type_item.GROUP_NAME}">
													</c:if>
													<option value="${type_item.TYPE_ID}">${type_item.TYPE_NAME}</option>
												</c:forEach>
												</optgroup>
											</select></td>
                                    <td>发生位置：<input type="text" onclick="loadEditIfrEM()" id="address" name="address" class="form-control input-sm" style="width: 250px;" placeholder="请选择事件位置"/>
                                    	<input type="hidden" id="LNG" name="LNG" />
                                    	<input type="hidden" id="LAT" name="LAT" />
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2"><textarea onkeyup="size(this)" id="msgDesc" maxlength="1000" placeholder="请填写相关描述信息" style="width: 100%; height: 100px; border: 0; padding: 5px;vertical-align: bottom;color:gray"></textarea>
                                    <small>字数剩余：<span id="span">1000</span>/1000.</small>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2"><span style="float: left; margin-top: 4px;">上传照片：</span><input type="file" id="msgImg" name="msgImg"/>
                                    <img id="imgPath0" name="imgPath0" width="150px" height="100px" class="hide"/></td>
                                </tr>
                            </table>
                            <div class="subItem userDepDiv ${empty depUserList or userMap.SMS_FLAG eq '0' ?'hide':''}">
	                                    <div class="deal-con skin-section">
	                                    	<c:set var="tempName" value="@@@"/>
	                                        <ul class="list">
	                                        	<c:forEach var="userInfo" items="${depUserList}" varStatus="infoId">
	                                        	<c:if test="${tempName eq '@@@' }">
	                                        	<li class="fl" style="margin-right: 10px"><span class="user-span fb">${userInfo.DEP_POSITION }：</span></li>
	                                        	</c:if>
	                                        	<c:if test="${tempName ne '@@@' and  tempName ne userInfo.DEP_POSITION}">
	                                            <div class="clear"></div>
	                                            </ul>
	                                            <ul class="list">
	                                            	<li class="fl" style="margin-right: 10px"><span class="user-span fb">${userInfo.DEP_POSITION }：</span></li>
	                                        	</c:if>
	                                            <li class="fl">
	                                            <input type="checkbox" value="${userInfo.USER_ID},${userInfo.WX_USER_ID}" id="chx${infoId.index}"/>
	                                            <label for="chx${infoId.index}">${userInfo.USER_NAME }(${userInfo.DEP_POSITION })</label></li>
	                                    		<c:set var="tempName" value="${userInfo.DEP_POSITION }"/>
	                                        	</c:forEach>
	                                            <div class="clear"></div>
	                                        </ul>
	                                    </div>
	                                </div>
                            <div class="cl"></div>
                            <div class="cl userDepDiv1 hide"></div>
                           		<div class="btns tc" style="margin: 10px 0;">
                                    <button type="button" id="btn" class="btn btn-primary" onclick="go_process()">提&nbsp;交</button>
                                    <a href="javascript:closeWindow();">
                                    <button type="button" class="btn" style="margin-left: 20px">取&nbsp;消</button>
                                    </a>
                             </div>
                        </div>
                        
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>

var urlConfig = {
		addEmergency : '<c:url value="/emergency/addEmergency.do"/>',
		uploadPic : '<c:url value="/emergency/uploadPic.do"/>',
		goMapEdit : '<c:url value="/emergency/goMapEdit.do"/>',
		contentPath:'<c:url value="/"/>',
		uploadEMPic:'<c:url value="/emergency/uploadEMPic.do"/>'
	};
function loadEditIfrEM(){
	/*  $.layer({
        type: 2,
        maxmin: true,
        shadeClose: false,
        title: "地图描点",
        shade: [0.1,'#fff'],
        offset: ['25px',''],
        area: ['70%', '90%'],
        iframe: {src: urlConfig.goMapEdit},
        close: function(index){
//            if(title==undefined){
//         	onloadWindow();
//            }
        }
    });  */
};
var xx=0,yy=0;
function go_process(){
	 var msgType = $(".select2").val();
	 if(msgType==null||msgType==""){
		 layer.alert("请选择应急事件类型"); 
		 /* layer.open({
			 title: '提示信息',
		  	 content: '请选择应急事件类型'
		 }) */
		 return;
	 }
	 var address = $("#address").val();
// 	 if(address==""){
// 		 layer.alert($("#address").attr("placeholder"));
// 		 return;
// 	 }
	 var msgDesc = $("#msgDesc").val();
	 if(msgDesc==""){
		 layer.alert($("#msgDesc").attr("placeholder"));
		 return;
	 }
	 var imgPath = "";
	 $("img[id^='imgPath']").each(function(){
		 if($(this).attr("tempSrc"))
		 	imgPath+=imgPath==""?$(this).attr("tempSrc"):","+$(this).attr("tempSrc");
	 });
// 	 alert(imgPath);return;
// 	 if(imgPath==""){
// 		 layer.alert("请上传图片");
// 		 return;
// 	 }
	 var wxUserIds = "";
	 if(!$(".userDepDiv").hasClass("hide")){
		 $("input[id^='chx']:checked").each(function(){
			 wxUserIds+=wxUserIds.length==0?$(this).val():"@_@"+$(this).val();
		 });
		 if(wxUserIds==""){
			 layer.alert("请选择上报对象");
			 return;
		 }
	 }
	 var inputdata={"msgType":msgType,
			 		"msgTypeName":$(".select2").text(),
			 		"address":address,
			 		"msgDesc":msgDesc,
			 		"imgPath":imgPath,
			 		"LNG":$("#LNG").val(),
			 		"LAT":$("#LAT").val(),
			 		wxUserIds:wxUserIds
			 		};
	 $.ajax({
       type:"POST",
       url:urlConfig.addEmergency,
       data:inputdata,
       dataType:"json",
       success:function(data){
   		if(data && data.status){
   			layer.alert("操作成功");
		   closeWindow();
   		}
       },
       beforeSend:function(){
       },
       error:function(){
    	   layer.alert("操作失败");
		}
	 });
}
function ajaxupload(fileElementId) {
	if(yy==0)
		yy = 1;
	else{
		return;
	}
	var loadi = layer.load(0);
// 	$("#"+fileElementId).attr("disabled",true); 
	$.ajaxFileUpload({
		url : urlConfig.uploadEMPic+'?fileElementId='
				+ fileElementId,
		secureuri : false,
		fileElementId : fileElementId, // 文件选择框的id属性
		dataType : 'json', // 服务器返回的格式类型
		type : 'post',
		success : function(data, status) // 成功
		{
			yy = 0;
			layer.close(loadi);
// 			$("#"+fileElementId).attr("disabled",false);
			if (data.flag=="success") {
				layer.alert("上传成功");
				if(!$("#imgPath0").attr("src")){
					$("#imgPath0").attr("src",urlConfig.contentPath+data.src);
					$("#imgPath0").attr("tempSrc",data.src.replace("static/image/",""));
					$("#imgPath0").removeClass("hide");
					xx++;
				}else{
					$("#imgPath"+(xx-1)).after("&nbsp;&nbsp;<img id='imgPath"+xx+"' name='imgPath"+xx+"' width=\"150px\" height=\"100px\" src='"+
							urlConfig.contentPath+data.src+"' tempSrc='"+data.src.replace("static/image/","")+"'/>");
					xx++;
				}
			} else {
				layer.alert("上传失败");
			}
		},
		error : function(data, status, e) // 异常
		{
			yy = 0;
			layer.close(loadi);
			layer.alert("出错了，请重新上传！");
		}

	});
}
function closeWindow() {
// 	parent.location.reload();
	parent.onloadWindow();
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(index);	
}
function setInputValue(address,x,y){
	$("#address").val(address);
	$("#LNG").val(x);
	$("#LAT").val(y);
}
function size(par) 
{
	var max = 1000; 
	str = 0;
	if (par.value.length <= max) 
		str = max-par.value.length; 
	document.getElementById("span").innerHTML = str.toString(); 
}
</script>

</body>
</html>