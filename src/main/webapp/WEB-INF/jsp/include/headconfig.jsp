<%@page import="com.sunsharing.component.utils.base.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.sunsharing.party.ConfigParam"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%!
public static String getHttpIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    System.out.println("ip1:"+ip+":当前:"+request.getRemotePort()+"/本机:"+request.getLocalPort()+"/服务:"+request.getServerPort());
    System.out.println("ip2:"+request.getHeader("X-Real-IP"));
    System.out.println("ip3:"+request.getRemoteAddr());
    if(!StringUtils.isBlank(ip) && !"unKnown".equalsIgnoreCase(ip)){
        //多次反向代理后会有多个ip值，第一个ip才是真实ip
        int index = ip.indexOf(",");
        if(index != -1){
            return ip.substring(0,index);
        }else{
            return ip;
        }
    }
    ip = request.getHeader("X-Real-IP");
    if(!StringUtils.isBlank(ip) && !"unKnown".equalsIgnoreCase(ip)){
        return ip;
    }
    return request.getRemoteAddr();
}
public static String getHttpIp1(HttpServletRequest request,String ips){
	if(ips.startsWith("http")){
		return ips;
	}
	String ip = getHttpIp(request);
	String[] ips1 = ips.split(";");
	for(String str : ips1){
		String[] strs = str.split(","); 
		for(String str1 : strs){
			if(ip.trim().startsWith(str1.split("\\.")[0])){
				return str1;
			}
		}
	}
	return ip;
}
%>
<script>
<%
//arcgis图层地址
String mapPath = ConfigParam.mapUrlGis;
//arcgisAPI地址
String gisPath = ConfigParam.serverUrlGis;
//矢量图地址
String satPath = ConfigParam.satUrlGis;
//卫星图地址
String tilePath = ConfigParam.tileUrlGis;
//路网图地址
String roadPath = ConfigParam.roadUrlGis;
//精图卫星图地址
String jmAreaLayerUrl = ConfigParam.jmAreaLayerUrl;
//精图矢量图地址
String jtAreaLayerUrl = ConfigParam.jtAreaLayerUrl;
//是否使用精图图层,1是0不是,优先级低于isUseJtLocalLayer
String isUseJmLocalLayer = ConfigParam.isUseJmLocalLayer;
//是否使用精图图层,1是0不是,优先级高于isUseJmLocalLayer
String isUseJtLocalLayer = ConfigParam.isUseJtLocalLayer;
////是否使用天地图精图模式true or false
String isUseTDT = ConfigParam.isUseTDT;
//图层坐标系（投影坐标系[在线网络坐标系]=102113/102100,地理坐标系[GPS,绘图等]=4326/4490）
String coordinateSystem = ConfigParam.coordinateSystem;
//高德webAPI地址
String amap_web_url = ConfigParam.amapWebUrl.replace("$url$", getHttpIp1(request,ConfigParam.amap_webIp));
//高德webAPI请求KEY
String amap_web_url_key = ConfigParam.amapWebUrlKey;

%>

var config = {
		x : '106.482502',
		y : '29.483969',
		isDefaultCenter : true,
		url : '<%=mapPath%>',
		saturl : '<%=satPath%>',
		tileurl : '<%=tilePath%>',
		roadurl : '<%=roadPath%>',
		isUseTDT : '<%=isUseTDT%>',
		coordinateSystem : '<%=coordinateSystem%>',
		isUseJmLocalLayer : '<%=isUseJmLocalLayer%>',
		isUseJtLocalLayer : '<%=isUseJtLocalLayer%>',
		jmAreaLayerUrl : '<%=jmAreaLayerUrl%>',
		jtAreaLayerUrl : '<%=jtAreaLayerUrl%>',
		mapUrl : "<c:url value="/zhcx/getMapList.do"/>",
		bmUrl : "<c:url value="/zhcx/bmList.do"/>",
		configUrl : "<c:url value="/zhcx/getList.do"/>",
		proxyUrl : "<c:url value="/proxy/proxy.do"/>",
		mapro:"<c:url value="/getMaPro.do"/>",
		getMonth:"<c:url value="/getMonth.do"/>",
		getAmout:"<c:url value="/getAmout.do"/>",
		getPronum:"<c:url value="/getPronum.do"/>",
		exportUrl : '<c:url value="/fire/export.do"/>',
		getMaproIntros : '<c:url value="/getMaproIntros.do"/>',
		saveMaproPoint : '<c:url value="/saveMaproPoint.do"/>',
		gourl:'<c:url value="/dangerous/list.do?menu=${menu }&visiteditem=${visiteditem }&type="/>',
		amap_web_url : '<%=amap_web_url%>',
		amap_web_url_key : '<%=amap_web_url_key%>'
	};
	var contextPath = "<%=(request.getContextPath())%>";
</script>

<link rel='stylesheet' type='text/css' href='<%=gisPath %>/arcgisapi39/js/esri/css/esri.css'/>
<script type='text/javascript' src='<%=gisPath %>/arcgisapi39/Global.js'></script>
<script type='text/javascript' src='<%=gisPath %>/arcgisapi39/init.js'></script>