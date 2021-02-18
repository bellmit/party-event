<%@page import="java.util.Map"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Exception!</title></head>
<body>
<H2>未知错误: ${ex.title }</H2>
<hr />
<P>错误描述：</P>
${ex.message }
</body>
</html>