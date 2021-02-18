<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>热力图</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/js/gmap/style/slider/style.css"/>"/>
</head>
<script>
	function setHeatMapBotton(maxNum,level,dataArray){
		if(isNaN(maxNum)){
			maxNum=0;
		}
		var a = Math.ceil(maxNum*0.15);
		var b = Math.ceil(maxNum*0.3);
		var c = Math.ceil(maxNum*0.5);
		var d = Math.ceil(maxNum*0.7);
		var e = Math.ceil(maxNum*0.9);
// 		var arrval = [a,b,c,d,e];
		var val = $(".heatmapdiv").html();
		level=level<14?"/"+radius*radius+"㎡":"";
		val=val.format(level,a,b,c,d,e);
		if(dstatus=="1"){
		$("#map_root").find("#legend").remove();
		$("#map_root").append(val);
		}
		setTableData(dataArray);
	}

	/* 填充统计列表 */
	function setTableData(data) {
		var str = "";
		var dLen = data.length;
		for (var i = 0; i < dLen; i++) {
			str += "<li><span>" + data[i].title
					+ "</span><span style=\"right: 10px; position:absolute; top:7px;\">"
					+ data[i].count + "</span></li>";
		}
		$("#mapTable").html(str);
	}
	String.prototype.format=function()
	{  
	  if(arguments.length==0) return this;  
	  for(var s=this, i=0; i<arguments.length; i++)  
	    s=s.replace(new RegExp("\\{"+i+"\\}","g"), arguments[i]);  
	  return s;  
	}; 
</script>
<body>
<div class="heatmapdiv" style="display:none;width: 867px; height: 561px; position: relative; border: 1px solid #ddd">
    <div id="legend" class=" BMap_noprint anchorBR" style="position: absolute; z-index: 10; bottom: 0px; right: 12px; top: auto; left: auto;">
        <div class="legend-text">图例：</div>
        <a href="javascript:void(0);" class="legend-item legend-1" style="text-align:center; font-family:'微软雅黑';"><font color="black">&lt;={1}</font>
            <div class="legend-item-wrapper">
                <div class="legend-item-tip">少于{1}{0}<span class="legend-item-tip-arrow"></span></div>
            </div>
        </a>
        <a href="javascript:void(0);" class="legend-item legend-2" style="text-align:center; font-family:'微软雅黑';"><font color="black">&lt;={2}</font>
            <div class="legend-item-wrapper">
                <div class="legend-item-tip">少于{2}{0}<span class="legend-item-tip-arrow"></span></div>
            </div>
        </a>
        <a href="javascript:void(0);" class="legend-item legend-3" style="text-align:center; font-family:'微软雅黑';"><font color="black">&lt;={3}</font>
            <div class="legend-item-wrapper">
                <div class="legend-item-tip">少于{3}{0}<span class="legend-item-tip-arrow"></span></div>
            </div>
        </a>
        <a href="javascript:void(0);" class="legend-item legend-4" style="text-align:center; font-family:'微软雅黑';"><font color="black">&lt;={4}</font>
            <div class="legend-item-wrapper">
                <div class="legend-item-tip">少于{4}{0}<span class="legend-item-tip-arrow"></span></div>
            </div>
        </a>
        <a href="javascript:void(0);" class="legend-item legend-5" style="text-align:center; font-family:'微软雅黑';"><font color="black">&gt;={5}</font>
            <div class="legend-item-wrapper">
                <div class="legend-item-tip">大于{5}{0}<span class="legend-item-tip-arrow"></span></div>
            </div>
        </a>
    </div>
</div>

</body>
</html>
