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
    <title>用户详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"  />
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-4.0.2/dist/css/select2.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/sslib/qlTree/qlTree.css"/>"/>
    <link href="<c:url value="/styles/css/flat/blue.css"/>" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/base.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/nineModules.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/detail.css"/>"/>

    <script type="text/javascript" src="<c:url value="/lib/jquery/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/lib/select2-4.0.2/dist/js/select2.full.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/artTemplate/template-simple.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.pagebar.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/qlTree/qlTree.js"/>"></script>

	<script src="<c:url value="/lib/layer/layer.min.js"/>"></script>
	<script src="<c:url value="/lib/layer/extend/layer.ext.js"/>"></script>

    <script type="text/javascript" src="<c:url value="/js/icheck.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/page_js/utiles.js"/>"></script>
    <script type="text/javascript">
        //单选框样式
        $(document).ready(function(){
        	$("input[type='radio']").on('ifChecked', function(event){  
//         		  alert(event.type + ' callback');
					userShowHide($(this).val());
        		}); 
            $('input').iCheck({
                checkboxClass: 'icheckbox_flat-blue',
                radioClass: 'iradio_flat-blue'
            });
            getUser("mh");
           // getUser("wx");
            $(".select2").select2();
            $("#wx").val("${userInfo.WX_USER_ID}").trigger("change");
            $("#mh").val("${userInfo.MH_USER}").trigger("change");
        });
    	</script>
</head>
<body>
<div class="content-box">
    <div class="cb-content" style="margin-left:0px">
        <div class="content">
            <div class="mainContent" style="top:0px">
                <div class="mcBox">
                    <div class="dwBottom">
                        <!-- 处理详情 -->
                        <div class="detail2">
                            <b class="tit">用户详情</b>
							<input type="hidden" value="${userInfo.USER_ID}" id="userId"/>
							<input type="hidden" value="${tempUserId}" id="tempUserId"/>
							<input type="hidden" value="${userInfo.DEP_ID}" id="depId"/>
							<input type="hidden" value="${userInfo.WX_USER_ID}" id="wxUserId"/>
							<input type="hidden" value="${userInfo.MH_USER}" id="mhUserId"/>
                            <table>
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <tr>
                                    <td>用户姓名：<input type="text" id="userName" name="userName" placeholder="请填写用户姓名" value="${userInfo.USER_NAME }"/></td>
                                    <td>手机号码：<input type="text" id="mobile" name="mobile" placeholder="请填写手机号码" value="${userInfo.MOBILE}"/></td>
                                </tr>
                                <tr>
                                    <td>身份证号：<input type="text" id="certId" name="certId" placeholder="请填写身份证号" value="${userInfo.CERT_ID}"/></td>
                                    <td>工作单位：<input type="text" id="units" name="units" placeholder="请填写工作单位" value="${userInfo.WORK_UNITS}"/></td>
                                </tr>
                                <tr>
                                    <%-- <td>是否值班：<input type="checkbox" id="sms_flag" name="sms_flag" value="1" ${userInfo.SMS_FLAG=='1'?"checked":"" }/></td> --%>
                                    <td><span style="float: left; margin-top: 4px;">门户账户：</span>
                                    <select class="select2" id="mh" style="width: 160px;float: none;">
									</select>
									</td>
                                    <td><span style="float: left; margin-top: 4px;">组织结构：</span>
											<jsp:include page="../include/emergencyTree.jsp"></jsp:include>
									</td>
                                </tr>
                                <%-- <tr class="depPosition ${userInfo.DEP_LEVEL eq '5'?'':'hide'}">
                                    <td>&nbsp;</td>
                                    <td><span style="float: left; margin-top: 4px;">职位信息：</span>
											<input id="depPosition" name="depPosition" value="${userInfo.DEP_POSITION }" placeholder="请填写职位信息"/>											
									</td>
                                </tr> --%>
                                <tr>
                                    <!-- <td><span style="float: left; margin-top: 4px;">微信账户：</span>
                                    <select class="select2 fr" id="wx" style="width: 160px">
									</select>
									<button type="button" id="btn1" class="btn btn-sm btn-primary" onclick="hasWXUser()">同步微信用户</button>
                                    </td> -->
                                   <!--  <td><span style="float: left; margin-top: 4px;">门户账户：</span>
                                    <select class="select2" id="mh" style="width: 160px;float: none;">
									</select>
									</td> -->
                                </tr>
                            </table>
                            <div class="cl"></div>
                            <div class="cl userDepDiv1 hide"></div>
                           		<div class="btns tc" style="margin: 5px 0">
                                    <button type="button" id="btn" class="btn btn-sm btn-primary" onclick="go_process()">提&nbsp;交</button>
                                    <a href="javascript:closeWindow();">
                                    <button type="button" class="btn btn-sm" style="margin-left: 20px">取&nbsp;消</button>
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
		modifyUser : '<c:url value="/emergency/modifyUser.do"/>',
		getWXUserList : '<c:url value="/emergency/getWXUserList.do"/>',
		hasWXUser : '<c:url value="/emergency/hasWXUser.do"/>',
		hasUserBind : '<c:url value="/emergency/hasUserBind.do"/>',
		getUser : '<c:url value="/emergency/getUser.do"/>'
	};
/* function detailFun(obj){
	if(obj && obj.level=='5'){
		$(".depPosition").removeClass("hide");
    }else{
    	$(".depPosition").addClass("hide");
	}
} */
function go_process(){
	 var userName = $("#userName").val();
	 if(userName==""){
		 layer.alert($("#userName").attr("placeholder"));
		 return;
	 }
	 if(hasUserBind("",userName)){
		 layer.alert("用户姓名已被绑定,请重新输入"); 
		 
		 return;
	 }
	 var mobile = $("#mobile").val();
	 if(mobile==""){
		 layer.alert($("#mobile").attr("placeholder"));
		 return;
	 }else if(!checkMobile(mobile)){
		 layer.alert("请输入正确的手机号码");
		 return;
	 }
	 var certId = $("#certId").val();
	 if(certId==""){
		 layer.alert($("#certId").attr("placeholder"));
		 return;
	 }else if(!IdentityValid(certId)){
		 layer.alert("请输入正确的身份证号");
		 return;
	 }
	 var units = $("#units").val();
	 if(units==""){
		 layer.alert($("#units").attr("placeholder"));
		 return;
	 }/*else if(!isemail(email)){
		 layer.alert("请输入正确的邮箱地址");
		 return;
	 }*/
	 var parent = $("#parent").val();
	 if(parent==""){
		 layer.alert($("#parent").attr("placeholder"));
		 return;
	 }
	 /* if(!$(".depPosition").hasClass("hide")){
		 var depPosition = $("#depPosition").val();
		 if(depPosition==""){
			 layer.alert($("#depPosition").attr("placeholder"));
			 return;
		 }
		 
	 } */
	 var sms_flag = "";//$("input[id='sms_flag']:checked");
	 var wx = "";//$("#wx").val();
	 var mh = $("#mh").val();
	 /* if($("input[id='sms_flag']:checked").val()=='1'){
		 if(wx==""||wx==null){
			 layer.alert("请选择微信账户进行绑定");
			 return;
		 } 
		 if(mh==""||mh==null){
			 layer.alert("请选择门户账户进行绑定");
			 return;
		 }
		 sms_flag = "1";
	 }else
		 sms_flag = "0";
  */
	 /* if(wx!="" && hasUserBind("wx",wx)){
		 layer.alert("微信账户已被绑定,请重新选择");
		 return;
	 } */
	 if(mh==""||mh==null){
		 layer.alert("请选择门户账户进行绑定");
		 return;
	 }
	 if(mh!="" && hasUserBind("mh",mh)){
		 layer.alert("门户账户已被绑定,请重新选择");
		 return;
	 }

	 
	 var inputdata={"userId":"${userInfo.USER_ID}",
			 		"tempUserId":$("#tempUserId").val(),
			 		"userName":userName,
			 		"mobile":mobile,
			 		"certId":certId,
			 		"units":units,
			 		"depId":$("#depId").val(),
			 		"depPosition":$("#depPosition").val(),
			 		"wxUserId":$("#wx").val(),
			 		"mhUserId":$("#mh").val(),
			 		"sms_flag":sms_flag
			 		};
	 $.ajax({
       type:"POST",
       url:urlConfig.modifyUser,
       data:inputdata,
       dataType:"json",
       success:function(data){
   		if(data && data.status){
   		   alert("操作成功");
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
function getWXUser(){
	 var inputdata={"userId":"${userInfo.USER_ID}",
		 		"tempUserId":$("#tempUserId").val()
		 		};
	$.ajax({
	       type:"POST",
	       url:urlConfig.getWXUserList,
	       data:inputdata,
	       dataType:"json",
	       success:function(data){
	   		if(data && data.status){
	   			layer.alert("同步请求已发送");
	   		}
	       },
	       beforeSend:function(){
	       },
	       error:function(){
	    	   layer.alert("操作失败");
			}
		 });
}
function hasWXUser(){
	 var inputdata={"userId":"${userInfo.USER_ID}",
		 		"tempUserId":$("#tempUserId").val()
		 		};
	$.ajax({
	       type:"POST",
	       url:urlConfig.hasWXUser,
	       data:inputdata,
	       dataType:"json",
	       success:function(data){
	   		if(data && data.status){
	   			layer.alert("还有数据未同步回来");
	   		}else{
	   			getWXUser();
		   	}
	       },
	       beforeSend:function(){
	       },
	       error:function(){
	    	   layer.alert("操作失败");
			}
		 });
}
function hasUserBind(v,val){
	 var inputdata={targetType:v,targetId:val,userId:"${userInfo.USER_ID}"};
	 var b = false;
	$.ajax({
	       type:"POST",
	       url:urlConfig.hasUserBind,
	       data:inputdata,
	       dataType:"json",
	       async : false,
	       success:function(data){
	   		if(data && data.status){
// 	   			layer.alert("需要绑定的用户已经存在");
				b = data.status;
	   		}
	       },
	       beforeSend:function(){
	       },
	       error:function(){
	    	   layer.alert("操作失败");
			}
	});
	return b;
}
function getUser(v){
	 var inputdata={getType:v};
	$.ajax({
	       type:"POST",
	       url:urlConfig.getUser,
	       data:inputdata,
	       dataType:"json",
	       async : false,
	       success:function(data){
	   		if(data && data.status){
	    	   setData(data.data.list,v);
	   		}
	       },
	       beforeSend:function(){
	       },
	       error:function(){
	    	   layer.alert("操作失败");
			}
	});
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
function setData(list,obj){
	$(obj).empty();
	var str = "<option value=''>请选择</option>";	
	$("#"+obj).append(str);
	if(list){
	for(var i=0;i<list.length;i++){
		str="<option value='"+list[i].USER_ID+"'>"+list[i].USER_NAME+(obj=='wx'?"("+list[i].MOBILE+")":"")+"</option>";
		$("#"+obj).append(str);
	}
	}
}

</script>

</body>
</html>