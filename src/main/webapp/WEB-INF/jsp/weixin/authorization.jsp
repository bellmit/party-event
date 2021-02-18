<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/css/weixin.css" />
<title>授权页面</title>
<style type="text/css">
	body{
		background: #fff;
	}
</style>
</head>
<body>

<div class="load-div">	
	<img alt="" src="<c:url value='/images/load.jpg'/>"   />
	<h2>正在获取授权,请稍候.....</h2>
</div>
	<script>
	setTimeout('goredirect()',400);
	//页面跳转
	function goredirect(){
		
		var weixinAppId = '${weixinAppId}';
		// 微信类型 企业号(QYH) or公众号(GZH)
		var type ='${type}';
// 		var h = "${pageContext.request.contextPath}";
		var h = window.location.href.split("/login/goAuthorization.do")[0];
		if(type=="QYH"){
			var redirect_uri = encodeURI(h + '/login/getWeiXinUserInfo.do');
		}else if(type=="GZH"){
			var redirect_uri = encodeURI(h + '/login/getUserByOpenId.do');
		}
		var getCodeUrl = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid='
				+ weixinAppId
				+ '&redirect_uri='+redirect_uri+'&response_type=code&scope=snsapi_base&state=${state}#wechat_redirect';
				window.location.href = getCodeUrl; 
	}
		 
	</script>
</body>
</html>

