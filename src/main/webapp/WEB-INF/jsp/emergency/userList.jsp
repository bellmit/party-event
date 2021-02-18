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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<meta charset=utf-8>
<html lang="zh-cn">
<head>
<title>应急信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />

<link rel="stylesheet" type="text/css"
	href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/lib/select2-4.0.2/dist/css/select2.min.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/sslib/qlTree/qlTree.css"/>" />

<link rel="stylesheet" type="text/css"
	href="<c:url value="/styles/css/base.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/styles/css/nineModules.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/styles/css/listpage.css"/>" />

<script type="text/javascript"
	src="<c:url value="/lib/jquery-1.9.1/jquery-1.9.1.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/lib/select2-4.0.2/dist/js/select2.full.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/lib/artTemplate/template-simple.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/sslib/ulynlist/ulynlist.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/sslib/ulynlist/ulynlist.table.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/sslib/ulynlist/ulynlist.pagebar.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/sslib/qlTree/qlTree.js"/>"></script>
<script src="<c:url value="/lib/layer/layer.min.js"/>"></script>
<style>
body #s2id_type .select2-choice {
	height: 30px;
	line-height: 30px;
	width: 318px;
}
</style>
<script src="<c:url value="/lib/My97DatePicker/WdatePicker.js"/>"
	charset="gb2312"></script>

</head>
<script>
	var contextPath = "<c:url value="/"/>";
	var ulynlistPath = {
		basePath : '<c:url value="/sslib/ulynlist"/>',
		url : '<c:url value="/emergency/getUserList.do"/>'
	}
</script>
<style>
.jgxz {
	font-weight: bold;;
	color: #357ebd;
}
</style>
<jsp:include page="../include/listMenu.jsp"></jsp:include>
<body>
	<div class="content-box">
		<%-- <div class="cb-sidebar ${visiteditem}">
			<div id="qlTree"></div>
		</div> --%>
		<div class="cb-content" <%-- <c:if test="${visiteditem eq 'hide'}"> --%>style="margin-left: 0px"<%-- </c:if> --%>>
			<div class="content">
				<div class="mainContent" style="top: 0px">
					<div class="mcBox">
						<form id="search-form" method="post">
							<div class="dwTop">
								<div class="zzCondition clearfix">
									<div class="subItem">
										<div class="clearfix">
											<input type="text" id="search" name="search" placeholder="请输入手机号或者用户名称" class="form-control input-sm input-add" />
											<div class="btns">
												<button type="button" class="btn btn-success btn-sm"
													id="search-btn">查&nbsp;&nbsp;询</button>
                                        <button type="button" class="btn btn-primary btn-sm" id="search-reset">重&nbsp;&nbsp;置</button>
											</div>
											<span><a href="javascript:void(0);" class="btn btn-success btn-sm" id="add-btn">
								                 <i></i>添加用户<input type="hidden"/>
											</a>
											</span>
										</div>
									</div>
								</div>
							</div>
						</form>
						<div class="dwBottom">
							<div class="ulynTab">
								<div id="dwTab"></div>
								<div id="dwPager"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="<c:url value="/js/page_js/emergency_user.js"/>"></script>
</html>