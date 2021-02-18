<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>问题反映</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/qyh/blue.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-4.0.2/dist/css/select2.min.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/qyh/update.css"/>" />
<script src="<c:url value="/js/jquery/jquery-1.7.2.min.js"/>"></script>
<script src="<c:url value="/js/icheck.min.js"/>"></script>
<script src="<c:url value="/lib/select2-4.0.2/dist/js/select2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/page_js/utiles.js"/>"></script>
<!-- <script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> -->
<script type="text/javascript"
	src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script type="text/javascript"
	src="http://webapi.amap.com/maps?v=1.3&key=6e1d54a50dbf8ee27f7ea667e48809e3"></script>
<!--<link rel="stylesheet" type="text/css" href="<c:url value="/js/timemobile/css/style.css?v=7"/>" />  -->
<link rel="stylesheet" type="text/css"
	href="<c:url value="/js/timemobile/css/normalize3.0.2.min.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/js/timemobile/css/mobiscroll.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/js/timemobile/css/mobiscroll_date.css"/>" />
<%-- <script src="<c:url value="/lib/layer/layer.min.js"/>"></script> --%>
<script src="<c:url value="/lib/layer-3.1.1/dist/layer.js"/>"></script>
<script src="<c:url value="/js/timemobile/mobiscroll_date.js"/>"></script>
<script src="<c:url value="/js/timemobile/mobiscroll.js"/>"></script>
<!-- <script type="text/javascript" src="http://api.tianditu.com/api?v=4.0"></script> -->
<script type="text/javascript" src="http://api.tianditu.gov.cn/api?v=4.0&tk=0ffa03ca59f5e4bda57c3290700145c6"></script>
</head>
<script>
   /*  var url=location.href.replace(location.search, "");
	var stateObject = {};
	var title = "Wow Title";
	var newUrl = url;
	history.pushState(stateObject,title,newUrl);  */
	
	layer.config({
	    extend: 'lx/style.css', //加载您的扩展样式
	    skin: 'layer-ext-lx'
	});
    	 urlConfig = {
    		"save_EmergencyManage":'<c:url value="/gzh/save_EmergencyManage.do"/>',
    			"validateAuthInit":'<c:url value="/gzh/validateAuthInit.do"/>',
    			"go_messageList":'<c:url value="/gzh/go_messageList.do"/>'
    	}; 
    	 
        $(document).ready(function(){
            console.log("返回行政区划=="+${comCode});
            console.log("是否党员==="+${isDangYuan});
        	//hideWindow();//隐藏提示的弹出窗口
        	
            $('input').iCheck({
                checkboxClass: 'icheckbox_flat-blue',
                radioClass: 'iradio_flat-blue'
            });  
           // selectMap();
          // validateAuthInit();

			$('#shequ').select2();
        });
       // window.history.forward(1); 
        function validateAuthInit(){
        	var dataInfo = {
					"wx_UserId":'${wx_UserId }'
			};
			 $.ajax({
				data:dataInfo,
			     url:urlConfig.validateAuthInit,
			     dataType : 'json', // 服务器返回的格式类型
			     type:'post',
			     success : function(data, status)  {
				    if(data.result ==0 || data.result =='0') {
				    	 showWindow("对不起，您未加入应急信息员队伍，无法使用该业务，如有疑问，请与教委联系。");
			        }else{
			        } 
			     },
			     error : function(data, status, e){	
			     }
		      });  
        }
    </script>
<body>
	<!-- 弹窗口 -->
	<div id="bomb_window" style="display: none">
		<div class="tc-shadow"></div>
		<div class="tc-div">
			<!-- hide -->
			<!-- 选择事件类型  -->
			<div class="tc-normal">
				<h3 id="window_tip">请先选择事件类型！</h3>
			</div>
			<button class="tc-btn" onclick="hideWindow()">确定</button>
		</div>
	</div>
	<div id="main">

		<div id="content">
			<%-- <div>
				  应急事件类型：<select id="emergencyType"
					onchange="changeSelect(this)">
					<!--   <option value ="volvo">Volvo</option>
					   <option value="audi">Audi</option> -->
				</select> <input type="hidden" class="text" id="wx_UserId"
					value="${wx_UserId }" readonly name="lat">
			</div> --%>
			<%-- <div class="text-con"><span class="text-name">发生时间</span>				
				  <input type="text" class="text" id="emergencyTime"  value="${emergencyTime}" readonly="true" > <!-- onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  -->
			</div> --%>


			 <%-- <div class="text-con">
				<span class="text-name">事件类型</span> <select class="select-list"
					id="emergencyType" onchange="changeSelect(this)">
				</select> <input type="hidden" class="text" id="wx_UserId"
					value="${wx_UserId }" readonly="" name="lat">
			</div> 
			<div class="skin-section">
				<ul class="list" id="jt_emergencyTypeInfo">
					<div class="clear"></div>
				</ul>
			</div> --%>
			<div class="text-con"><span class="text-name">您的姓名</span>				
				  <input type="text" maxLength="20" class="text" id="wxUserName"  value="" >
			</div> 
			<div class="text-con" style="display:none"><span class="text-name">身份证号</span>				
				  <input type="text" class="text" id="certId"  value="" >
			</div> 
			<div class="text-con"><span class="text-name">电话号码</span>				
				  <input type="text" class="text" id="phone"  value="" >
			</div> 
			<div class="textarea-con" style="margin-top: 10px">
				<textarea class="textarea" placeholder="问题标题" maxLength="100"
					name="" style="height:50px" id="problemTitle"></textarea>
			</div>
			<div class="textarea-con" style="margin-top: 10px">
				<textarea class="textarea" placeholder="请输入您的问题描述" maxLength="1000" id="fireDesc"
					name="fireDesc" style="height:200px"></textarea>
			</div>
			<div class="skin-section">
				<ul class="list list-2">
					<li><img alt="" class="map-i" onclick="openMap()"
						src="../images/map-ico.png"> <span onclick="openMap()">所在位置</span>
					</li>
					<div class="clear"></div>
				</ul>
			</div>
			<!-- 地图选择 -->
			<div id="mapAdd" ><!--style="display: none"   -->
				<textarea class="textarea" style="width: 96%; height:60px" id="mapAddress"
					readonly onclick="selectMap()" name="mapAddress"></textarea>
				<!-- readonly onclick="selectMap()" -->
				<input type="hidden" class="text" id="lng" readonly name="lng">
				<input type="hidden" class="text" id="lat" readonly name="lat">
			</div>
			<!-- 图片上传 -->
			<div class="img-con" id="aa">
				<div class="img-add-cell" id="addImg" style="position: relative">
					<img class="img-add" src="../images/add.png" id="img-add">
				</div>
				<div class="clear"></div>
			</div>
			<%--<div class="text-con">
				<span class="text-name">我要反馈给</span> 
				<select class="select-list" id="emergencyDep" >
					<option  value = "500104102210" >社区党支部</option >
				</select> 
			</div>--%>
				<div class="text-con">
					<span class="text-name">反馈社区</span>
					<select class="select-list" id="shequ" >
						<%--<option value="">--请选择--</option>--%>
						<c:forEach var="xzqhItem" items="${xzqhList}">
							<option value="${xzqhItem.XZQH}" ${comCode == xzqhItem.XZQH ? 'selected' : ''}>${xzqhItem.XZQMC}</option>
						</c:forEach>
					</select>
				</div>
			<div class="text-con">
				<span class="text-name">是否公开</span> 
				<!-- <label>
					<input type="radio" name="ifOpen"/>是
				</label>
				<label>
					<input type="radio" name="ifOpen"/>否
				</label>  -->
				<input tabindex="1" type="radio" id="gk-1"  
						name="ifOpen" value="0"> <label for="gk-1" style="margin-right:10px">是</label>
				<input tabindex="2" type="radio" id="gk-2"
					name="ifOpen" value="1"> <label for="gk-2">否</label>
			</div>
				<c:if test="${isDangYuan==true}">
					<div class="text-con">
						<span class="text-name">是否党员报到</span>
						<input tabindex="1" type="radio" id="dy-1"
							   name="isMember" value="0"> <label for="dy-1" style="margin-right:10px">是</label>
						<input tabindex="2" type="radio" id="dy-2"
							   name="isMember" value="1"> <label for="dy-2">否</label>
					</div>
				</c:if>



			<!-- 添加领导分组 -->
			<!-- <div class="group-ld">
				<p>区领导</p>
				<div class="skin-section">
					<ul class="list">
					    <li><input tabindex="1" type="checkbox" id="ld-1"  
							name="fireType" value="0"> <label for="ld-1">领导A</label>
						</li>					
						<li><input tabindex="2" type="checkbox" id="ld-2"
							name="fireType" value="1"> <label for="ld-2">领导B</label>
						</li>  						
											
						<div class="clear"></div>
					</ul>
				</div> 
			</div> 
			添加主任分组
			<div class="group-ld">
				<p>主任</p>
				<div class="skin-section">
					<ul class="list">
					    <li><input tabindex="1" type="checkbox" id="zr-1"  
							name="fireType" value="0"> <label for="zr-1">主任A</label>
						</li>					
						<li><input tabindex="2" type="checkbox" id="zr-2"
							name="fireType" value="1"> <label for="zr-2">主任B</label>
						</li>  						
						<div class="clear"></div>
					</ul>
				</div> 
			</div>  -->
			<!-- 添加领导分组 -->
			<%-- <div id="lingdaoGrop">
				<c:forEach var="userInfo" items="${lindaoList}" varStatus="infoId">
					<c:if test="${tempName eq '@@@' }">
						<li class="fl" style="margin-right: 10px"><span
							class="user-span fb">${userInfo.DEP_POSITION }：</span></li>
					</c:if>

					<c:if
						test="${infoId.index eq 0 and tempName ne '@@@' and  tempName ne userInfo.DEP_POSITION}">
						<div class="group-ld">
							<p>${userInfo.DEP_POSITION }：</p>
							<div class="skin-section">
								<ul class="list">
					</c:if>

					<c:if
						test="${infoId.index ne 0 and tempName ne '@@@' and  tempName ne userInfo.DEP_POSITION}">
						<div class="clear"></div>
						</ul>
			</div>
		</div>
						<div class="group-ld">
							<p>${userInfo.DEP_POSITION }：</p>
							<div class="skin-section">
								<ul class="list">
					</c:if>
				
									<li><input type="checkbox"
										value="${userInfo.USER_ID},${userInfo.WX_USER_ID}"
										id="chx${infoId.index}" name="lingdao" /> <label
										for="chx${infoId.index}">${userInfo.USER_NAME }(${userInfo.DEP_POSITION })</label></li>
									<c:set var="tempName" value="${userInfo.DEP_POSITION }" />
				
						<c:if test="${infoId.last }">
										<div class="clear"></div>
								</ul>
							</div>
						</div>
						</c:if>
		</c:forEach>
	</div> --%>
	<%-- 
			
			<c:forEach var="userInfo" items="${lindaoList}" varStatus="infoId">
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
		  
			
			 --%>

	<%-- <div id="cover"
				style="z-index: 101; display: none; height: 100%; width: 100%; position: absolute; left: 0; top: 0; background: rgb(0, 0, 0); filter: alpha(opacity = 20); -moz-opacity: 0.2; opacity: 0.2;"></div>
			<div id="coverImg"
				style="z-index: 102; display: none; position: fixed; left: 40%; top: 300px; background: rgb(0, 0, 0); color: white;">
				
				<div class="load-div">	
					<img alt="" src="<c:url value='/images/load.jpg'/>"   />
					<h2>正在提交,请稍侯.....</h2>
				</div>
			</div> --%>
	<br>
	<div style="text-align: center;">
		<input id="normal_button" type="button"
			value="立即提交" onclick="saveInfo()"
			style="width: 80%; height: 44px; color: #fff; font-size: 16px; font-family: '微软雅黑'; cursor: pointer; border: none; background-color: #f64d00; border-radius: 5px; margin-bottom: 30px" />
	</div>
	</div>
	</div>
	<!-- 弹出地图 -->
	<div id="div2" style="display: none">
		<!-- <div id="mapd" class="map" style="height: 500px"></div> -->
		<div id="mymap" class="map" style="height: 500px"></div>
		<input type="button" value="确定位置"
			style="width: 40%; height: 35px; line-height: 35px; color: #fff; font-size: 15px; font-family: '微软雅黑'; cursor: pointer; border: none; background-color: #3498db; border-radius: 5px; position: absolute; left: 30%; bottom: 15px; z-index: 10000"
			id="submit">
	</div>
	
</body>
<script>
//手机点返回时，防盗链验证链接
$(function(){
    console.log("jsSign==",${jsSign});
	pushHistory();
	window.addEventListener("popstate", function(e) {
		location.href='<c:url value="/gzh/go_messageList.do"/>';
	}, false);
	function pushHistory() {
		var state = {  title: "title",  url: "#" };
		window.history.pushState(state, "title", "#");
	}
}); 
$("#phone").val('${phone}');

wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: ${jsSign}.appId, // 必填，公众号的唯一标识
    timestamp: ${jsSign}.timestamp, // 必填，生成签名的时间戳
    nonceStr: ${jsSign}.nonceStr, // 必填，生成签名的随机串
    signature: ${jsSign}.signature,// 必填，签名，见附录1
    jsApiList: [
                'chooseImage',
                'uploadImage', 
                'previewImage',
                'getLocation',
                'startRecord',
                'stopRecord',
                'playVoice',
                'openLocation',
                'hideOptionMenu',
                'hideAllNonBaseMenuItem'
                
                ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
wx.ready(function(){
    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
	//初始化坐标	
	wx.getLocation({
        type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
        success: function (res) {
            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
            var longitude = res.longitude ; // 经度，浮点数，范围为180 ~ -180。
            getTable(longitude,latitude);
        }
	});

});
	//根据经纬度获取地理位置
	function getTable(x,y){
        $("#lng").val(x);
        $("#lat").val(y);
        $.ajax({
                   url:"http://api.tianditu.gov.cn/geocoder",
                   method:'get',
                   data:{postStr:"{'lon':"+x+",'lat':"+y+",'ver':1}",type:'geocode',tk:'0ffa03ca59f5e4bda57c3290700145c6'},
                   dataType:'json',
                   async:false,
                   success:function(data){
                      console.log(data);
                      if(data && data.msg=="ok"){
                          $("#mapAddress").val(data.result.formatted_address);
                      }else{
                          showWindow("未获取到位置信息，请手动选择！");
                      }

                   },error: function(XMLHttpRequest, textStatus, errorThrown) {
                console.log(XMLHttpRequest.status);
                console.log(XMLHttpRequest.readyState);
                console.log(textStatus);
            }
               });
	}
function openLocation(){
	wx.openLocation({
	    latitude: 29.48354, // 纬度，浮点数，范围为90 ~ -90
	    longitude: 106.482239, // 经度，浮点数，范围为180 ~ -180。
	    name: '', // 位置名
	    address: '', // 地址详情说明
	    scale: 25, // 地图缩放级别,整形值,范围从1~28。默认为最大
	    infoUrl: 'http://www.baidu.com' // 在查看位置界面底部显示的超链接,可点击跳转
	});
}


var emergencyTypeInfo = ${emergencyTypeInfo};
var temp_i = 1;
$(function(){ 
	var temp_type = "";
	var temp_i = 1;
	var select_html = "<option value=\"请选择\">请选择</option>";
	var checkbox_html = "";
	for(var i=0 ; i<emergencyTypeInfo.length; i++){
		var type = emergencyTypeInfo[i].GROUP_NAME;
		var type_jt_name = emergencyTypeInfo[i].TYPE_NAME;
		var type_jt_id = emergencyTypeInfo[i].TYPE_ID;
		if(type != temp_type) {
			select_html += "<option value =\""+type+"\">"+type+"</option>";
			temp_type = emergencyTypeInfo[i].GROUP_NAME;
		}
		
		if(type ==  emergencyTypeInfo[0].GROUP_NAME){
			checkbox_html+=" <li><input tabindex='"+temp_i+"' type='checkbox' id='flat-checkbox-"+temp_i+"'";
			checkbox_html+=" name='fireType' value='"+type_jt_id+"'> <label for='flat-checkbox-"+temp_i+"'>"+type_jt_name+"</label>";
			checkbox_html+=" </li>";
			temp_i ++;
		} 
	}
	$('#emergencyType').html(select_html);
	//$('#jt_emergencyTypeInfo').html(checkbox_html);
});
function changeSelect(e){
	temp_i = 1;
	var value = e.value;
	var checkbox_html = "";
	for(var i=0 ; i<emergencyTypeInfo.length; i++){
		var type = emergencyTypeInfo[i].GROUP_NAME;
		var type_jt_name = emergencyTypeInfo[i].TYPE_NAME;
		var type_jt_id = emergencyTypeInfo[i].TYPE_ID;
		if(type ==  value){
			checkbox_html+=" <li><input tabindex='"+temp_i+"' type='checkbox' name='firetype' id='flat-checkbox-"+temp_i+"'";
			checkbox_html+=" name='fireType' value='"+type_jt_id+"'> <label for='flat-checkbox-"+temp_i+"'>"+type_jt_name+"</label>";
			checkbox_html+=" </li>";
			temp_i ++;
		} 
	}
	$('#jt_emergencyTypeInfo').html(checkbox_html);
	 $('input').iCheck({
         checkboxClass: 'icheckbox_flat-blue',
         radioClass: 'iradio_flat-blue'
     }); 
}
//判断当前客户端版本是否支持指定JS接口
function IscheckJsApi(){
	wx.checkJsApi({
	    jsApiList: ['chooseImage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
	    success: function(res) {
	        // 以键值对的形式返回，可用的api值true，不可用为false
	        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
	    }
	});
}
 /* **********************************信息采集   选择上传图片   start********************************************* */
$('#img-add').click(function() {
	//1.选择图片
	wx.chooseImage({
	    count: 1, // 默认9
	    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
	    success: function (res) {
	        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	        for(var i=0;i<localIds.length;i++){
	        	var localId = localIds[i];
	        	uploadImage(localId);
	        } 
	    }
	});
});

//2.上传图片到微信
function uploadImage(localId){
	var a =localId;
	wx.uploadImage({
        localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
        isShowProgressTips: 1, // 默认为1，显示进度提示
        success: function (res) {
            var serverId = res.serverId; // 返回图片的服务器端ID
            var div = "<div id=\"div_"+a+"\" class=\"img-cell\" onclick=\"$(this).find('i').toggleClass('hide')\">"+
	 	    "<i class=\"hide\" onclick=\"if(confirm('确定删除吗？')){$(this).closest('div').remove();}\"><img src=\"../images/deleted.png\"></i>"+
	 	    "<input type=\"hidden\" name=\"pics\" id=\"serverId_"+serverId+"\" value=\""+serverId+"\"><img style=\"width:"+($("#addImg").width()-2)+"px;height:"+($("#addImg").height()-2)+"px\" src=\""+localId+"\"></div>";
	      $("#addImg").before(div);
        }
    });
}
/* *********************************信息采集   选择上传图片   end ***************************************************************** */
	var lingdaoList = ${lindaoList}+"";
	var wait = 0;
	var dd  ;
    var ifOpen;
    var isMember;
	var msgSource='${msgSource}';// 来源：0-设施问题 1-民生问题
	//提交信息
     function saveInfo(){
		if($("#gk-1").parent().hasClass("checked") ){
			ifOpen=1;//公开
		}else if($("#gk-2").parent().hasClass("checked")){
			ifOpen=0;//不公开
		}else{
			showWindow("请选择是否公开！");
			return;
		}
		//是否党员
         if($("#dy-1").parent().hasClass("checked") ){
             isMember=1;//是党员报道
             msgSource=30;
         }else if($("#dy-2").parent().hasClass("checked")){
             isMember=0;//不是党员报道
         }
		
    	$(this).attr("disabled","false"); 
 		var deviceNo='${deviceNo}';//设施id

 		var wxUserName=$("#wxUserName").val();
 		var openId='${openId}';
 		var phone=$("#phone").val();
 		var loginUrl='${loginUrl}';
 		var comCode=$("#shequ").val();
 		if(phone==''){
			showWindow("请填写电话号码");
			return;
		}
 		if(!checkMobile(phone) && !checkphone(phone)){
 			showWindow("请输入正确的电话号码");
 			 return;
 		 }
 		if(phone.length > 12){
 			showWindow("输入的电话号码过长！");
 			return;
 		}
 		if(wxUserName==''){
			showWindow("请填写姓名");
			return;
		}
 		var certId=$("#certId").val();
 		/* if(!IdentityValid(certId)){
 			showWindow("请输入正确的身份证号");
 			 return;
 		 } */
 		var problemTitle=$("#problemTitle").val();
 		if(problemTitle==''){
			showWindow("请填写问题标题");
			return;
		}
		var loadi;
    	/* var emergencyType = $("#emergencyType").val();
 		if("请选择" == emergencyType){
 			showWindow("请选择事件类别");
 			return;
 		}
    	
		var type=$("input[name='firetype']:checked").length;
		
		if(type==0){
			showWindow("请选择事件二级类别");
			return;
		}*/
		var fireTypeValue = "null";
	 /*    $("input[name='firetype']:checked").each(function(){
	              fireTypeValue += this.value +",";
	   		 });  */
	   // alert(fireTypeValue);
	    
	    var desc = $("#fireDesc").val();
		if($.trim(desc)==""){
			showWindow("必须填写问题描述哦！");
			return;
		} 
		  var mapAddress = $("#mapAddress").val();
			var lng = $("#lng").val();
			var lat = $("#lat").val();
		    if(mapAddress==''){
				showWindow("没有选择地图地址");
				return;
			}
		    var serverIds ="";
			$("input[id^='serverId_']").each(function(){
				serverIds +=  this.value+",";
			});
			if(serverIds.indexOf(",")== -1 ){
				showWindow("没有上传应急图片");
				return;
			}
			
		var lingdao = "";
		//如果是应急办值班的，选择需要上报的领导的框出来
		if(null != lingdaoList && '' != lingdaoList){
			var type_lingdao=$("input[name='lingdao']:checked").length;
			/* if(type_lingdao==0){
				showWindow("请选择需要上报的领导");
				return;
			} */
		    $("input[name='lingdao']:checked").each(function(){
		    	lingdao += this.value +"####";
		   		 });
		 //   alert(lingdao);
		}


//		var wx_UserId = $("#wx_UserId").val();
		var wx_UserId = '${wx_UserId }';
		var emergencyDep=$("#emergencyDep").val();
		if(wait==1){
			showWindow("正在提交，请稍候");
			return;
		}
	//	return;
		wait = 1;
			var dataInfo = {
					"wx_UserId":wx_UserId,
					"fireTypeValue":fireTypeValue,
					"desc":desc,
					"mapAddress":mapAddress,
					"lng":lng,
					"lat":lat,
					"serverIds":serverIds,
					"emergencyDep":comCode,
					"wxUserName":wxUserName,
					"certId":certId,
					"problemTitle":problemTitle,
					"msgSource":msgSource,
					"deviceNo":deviceNo,
					"lingdao":lingdao,
					"openId":openId,
					"msgUserPhone":phone,
					"loginUrl":loginUrl,
					"ifOpen":ifOpen,
					"isMember":isMember
			};
			 $.ajax({
				data:dataInfo,
			     url:urlConfig.save_EmergencyManage,
			     dataType : 'json', // 服务器返回的格式类型
			     type:'post',
			     success : function(data, status)  {
			    	 $("#fireDesc").val("");
			    	 $("#mapAddress").val("")
			    	 layer.close(loadi);
			    	 wait=0;
					dd = data;
					setTimeout('setResData()',400);
			     },
			     beforeSend : function() {
			    	 loadi=layer.load('正在提交，请稍候…');
			    	// 禁用按钮防止重复提交  
			         $("#normal_button").attr({ disabled: "disabled" });
					}/* ,
			     error : function(data, status,e){	
			    	alert("data="+data+",status="+XMLHttpRequest.status+",readyState="+XMLHttpRequest.readyState+",textStatus="+textStatus);  
			    	//alert("data="+data.result+",status="+status);  
			    	layer.close(loadi);
			    	 wait=0;
			    	 console.info(data);
			    	 showWindow("出错了，请重新提交！");
			     } */
		      }); 
	}; 
	function setResData(){
		var data = dd;
		if(data.result=="sucess") {
	    	showWindow("信息提交成功！");
	    	setTimeout('go_list()',300);
        }else if(data.result=="no_user") {
    	 showWindow("对不起，您未加入应急信息员队伍，无法使用该业务，如有疑问，请与教委联系。");
        }else if(data.result=="failed") {
        	 showWindow("系统发生异常，请稍候提交");
        }else{
        	 showWindow("提交失败");
        } 
		dd= null;
	}
	function go_list(){
		window.location.href=urlConfig.go_messageList+"?openId=${openId}&type=${msgSource}&facilityId=${deviceNo}&userId=${userId}&appId=${appId}&loginUrl=${loginUrl}&comCode=${comCode}&phone=${phone}&ifOpen=2";
	}
	function serializeObject(e,del){    
		   var o = {};    
		   var a = e;  
		   $.each(a, function() {    
		       if (o[this.name]) {    
		           if (!o[this.name].push) {    
		               o[this.name] = [o[this.name]];    
		           }    
		           o[this.name].push(this.value || '');    
		       } else if(del.indexOf(this.name)==-1){    
		           o[this.name] = this.value || '';    
		       }    
		   });    
		   return o;    
		}; 
		Array.prototype.indexOf = function(val) {
            for (var i = 0; i < this.length; i++) {
                if (this[i] == val) return i;
            }
            return -1;
        };
	 
	function selectMap(){
		$("#detailAdd").css("display","none");
		$("#content").css("display","none");
		$("#normal_button").hide();
		
		$("#mymap").css("display","block");
		$("#div2").css("display","block");
		$("#mapAdd").css("display","block");
		if(mapObj==null){
			showMap();
		}
	}
	function openMap(){
		selectMap();
	}
	var map;
	var zoom = 15;
	function showMap(){
		if(map==null){
		 map = new T.Map('mymap', {
             projection: 'EPSG:4326'
         });
		var firstLat=$("#lat").val();
		var firstLng=$("#lng").val();
		firstLat=parseFloat(firstLat);
		firstLng=parseFloat(firstLng);
		if(isNaN(firstLat)||isNaN(firstLng)||firstLng==""||firstLng==""){
            firstLat=106.47712;
            firstLng=29.48413;
		}
        map.centerAndZoom(new T.LngLat(firstLng,firstLat), zoom);
		}
         var cp = new T.CoordinatePickup(map, {callback: getLngLat});
         cp.addEvent();
       //创建对象
         geocode = new T.Geocoder();

         map.addEventListener("click", function(e){
             geocode.getLocation(e.lnglat,searchResult);
         });
	}	
	//在地图上添加标注
	 function getLngLat(lnglat) {
		 //创建标注对象
         var marker = new T.Marker(new T.LngLat(lnglat.lng.toFixed(6), lnglat.lat.toFixed(6)));
         map.clearOverLays();
		 //向地图上添加标注
         map.addOverLay(marker);
         $("#lng").val(lnglat.lng.toFixed(6));
		 $("#lat").val(lnglat.lat.toFixed(6));
		 
     }
	function searchResult(result)
      {
          if(result.getStatus() == 0)
          {
        	  $("#mapAddress").val(result.getAddress());

          }
      }
	$("#coverImg").css("top",$(window).height()/2+"px");
	$("#mapd").css("height",$(window).height()+"px");
	var mapObj,marker,lng,lat,lnglatXY,isSetting=false,resultStr = "";
	var geocoder; 
	var address,geolocation;
	function initialize(){

	  // var position=new AMap.LngLat(106.482239,29.48354);
	   //106.47834 29.48744
	    var position = null;
	     wx.getLocation({
	        type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
	        success: function (res) {
	            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	            var longitude = res.longitude ; // 经度，浮点数，范围为180 ~ -180。

	            $("#lng").val(longitude);
   		        $("#lat").val(latitude);

	            var speed = res.speed; // 速度，以米/每秒计
	            var accuracy = res.accuracy; // 位置精度
	            position=new AMap.LngLat(longitude,latitude);
	        //    alert(position);
	            //创建地图实例
	    	    mapObj=new AMap.Map("mapd",{
	    	        view: new AMap.View2D({//创建地图二维视口
	    	        center:position,//创建中心点坐标
	    	        zoom:16, //设置地图缩放级别
	    	        rotation:0 //设置地图旋转角度
	    	      })
	    	    });

	    	    //地图点击事件
	    	    AMap.event.addListener(mapObj,"click",function(e){
	    		     if(marker!=null)
	    		     {
	    		       marker.setMap(null);
	    		     }

	    		     lnglat=e.lnglat;
	    		     lng=lnglat.lng;
	    		     lat=lnglat.lat;
	    		     if(parent.document.getElementById("jd")!=undefined){
	    			     parent.document.getElementById("jd").value=lng;
	    			     parent.document.getElementById("wd").value=lat;
	    		     }
	    		     marker = new AMap.Marker({
	    		       map:mapObj,
	    		       position:e.lnglat,
	    		       icon:"http://webapi.amap.com/images/0.png",
	    		       offset:new AMap.Pixel(-10,-34)
	    		     });

	    		     mapObj.setCenter(lnglat);

	    		     lnglatXY=new AMap.LngLat(lnglat.lng,lnglat.lat);
	    		     $("#lng").val(lnglat.lng);
	    		     $("#lat").val(lnglat.lat);
	    		     //加载地理编码插件
	    		     AMap.service(["AMap.Geocoder"],function(){
	    		     geocoder=new AMap.Geocoder({
	    		     radius:1000, //以已知坐标为中心点，radius为半径，返回范围内兴趣点和道路信息
	    		     extensions: "all"//返回地址描述以及附近兴趣点和道路信息，默认"base"
	    		     });
	    		     AMap . event . addListener(geocoder, "complete",geocoder_callBack);
	    		     geocoder.getAddress(lnglatXY);
	    		      });
	    	   });

	    	    //逆地理编码
	    	    AMap.service(["AMap.Geocoder"], function() {
	    	        geocoder = new AMap.Geocoder({
	    	            radius: 1000,
	    	            extensions: "all"
	    	        });
	    	        //逆地理编码
	    	        geocoder.getAddress(lnglatXY, function(status, result){
	    	            //取回逆地理编码结果
	    	            if(status === 'complete' && result.info === 'OK'){
	    	                geocoder_CallBack(result);
	    	            }
	    	        });
	    	    });

	    	  setinitMap(position);
	        }
	    });
	}
	//默认当前位置地图展示
	function setinitMap(position){
  	     marker = new AMap.Marker({                 
	       map:mapObj,                 
	       position:position,                 
	       icon:"http://webapi.amap.com/images/0.png",                 
	       offset:new AMap.Pixel(-10,-34)               
	     }); 
	     //加载地理编码插件 
	       AMap.service(["AMap.Geocoder"],function(){ 
		     geocoder=new AMap.Geocoder({ 
		     radius:1000, //以已知坐标为中心点，radius为半径，返回范围内兴趣点和道路信息 
		     extensions: "all"//返回地址描述以及附近兴趣点和道路信息，默认"base" 
		     }); 
		     AMap . event . addListener(geocoder, "complete",geocoder_callBack); 
		     geocoder.getAddress(position); 
	     });
	}
	function geocoder_callBack(data) { //回调函数
        address = data.regeocode.formattedAddress;
        resultStr = "地址为："+ address;
        isSetting=true;
       // alert(resultStr);
        showWindow(resultStr);
        $("#mapAddress").val(address);
    } 
	function onComplete (data) {
		if(data.accuracy==null){
			//alert("定位失败，请手动选择地址");
			showWindow("定位失败，请手动选择地址");
		}
	};
	//解析定位错误信息
	function onError (data) {
		var str = '<p>定位失败</p>';
		str += '<p>错误信息：';
		switch(data.info) {
			case 'PERMISSION_DENIED':
				str += '浏览器阻止了定位操作';
				break;
			case 'POSITION_UNAVAILBLE':
				str += '无法获得当前位置';
				break;
			case 'TIMEOUT':
				str += '定位超时';
				break;
			default:
				str += '未知错误';
				break;
		}
		str += '</p>';
		result.innerHTML = str;
	};
	$('#submit').click(function(){
		$("#div2").css("display","none");
		$("#content").css("display","block");
		$("#normal_button").show();
	});
	
	
	function showWindow(tip){
		$('.tc-shadow').height($(document).height());
		$("#window_tip").html(tip);
		$("#bomb_window").show();
	}
	function hideWindow( ){
		$("#window_tip").html("");
		$("#bomb_window").hide();
	}
	 
	/* ================== */
/* 	 initDateTimeInput($("#input[name=person_qrrq]"));
	function initDateTimeInput(obj){
    $(obj).mobiscroll().date({
        theme: 'android-ics light',
        display: 'modal',
        animate: 'slidedown',
//        ampmText: '上午/下午',
        dateFormat: 'yy-mm-dd',
        dateOrder: 'ymmdd',
//        timeFormat: 'HH:ii:ss',
        yearText: '年',
        monthText: '月',
        dayText: '日',
//        hourText: '时',
//        minuteText: '分',
        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        endYear: 2100,
        mode: 'scroller'
    });
} */
/* $(function () {
	var currYear = (new Date()).getFullYear();	
	var opt={};
	opt.date = {preset : 'date'};
	opt.datetime = {preset : 'datetime'};
	opt.time = {preset : 'time'};
	opt.default = {
			  theme: 'android-ics light',
		        display: 'modal',
		        animate: 'slidedown',
//		        ampmText: '上午/下午',
		        dateFormat: 'yy-mm-dd',
		        dateOrder: 'ymmdd',
//		        timeFormat: 'HH:ii:ss',
		        yearText: '年',
		        monthText: '月',
		        dayText: '日',
//		        hourText: '时',
//		        minuteText: '分',
		        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
		        endYear: 2100,
		        mode: 'scroller'
	};

	$("#emergencyTime").mobiscroll($.extend(opt['date'], opt['default']));

}); */
	

	
</script>

</html>

