<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>测试</title>

    <link type="text/css" rel="stylesheet" href="<c:url value="/lib/bootstrap/css/bootstrap.css"/>">
    <link type="text/css" rel="stylesheet" href="<c:url value="/lib/bootstrap/css/bootstrap-datetimepicker.min.css"/>">
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/css/zeus-ui.css"/>">
    <link rel="stylesheet" href="<c:url value="/lib/font-awesome/css/font-awesome.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/styles/css/table-ulynlist.css"/>">

    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/bootstrap/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/bootstrap/js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/bootstrap/js/bootstrap.min.js"></script>
    <!--加载表格需要引入的js文件-->
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/artTemplate/template-simple.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/sslib/ulynlist/ulynlist.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/sslib/ulynlist/ulynlist.table.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/sslib/ulynlist/ulynlist.pagebar.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/table-ulynlist.js"></script>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script type="text/javascript" src="<${pageContext.request.contextPath}/lib/html5shiv/html5shiv.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/html5shiv/respond.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/test.js"></script>
    <script type="text/javascript">
        var ct = '${pageContext.request.contextPath}/';
        var ulynlistPath = {
            basePath: ct + 'sslib/ulynlist',
            url: ct + 'getTestData.do',
        };
    </script>
<body>
<div id="js-message-style"></div>
<div id="js-message-style-tablePageBar"></div>
<div>hello word</div>
</body>
</html>