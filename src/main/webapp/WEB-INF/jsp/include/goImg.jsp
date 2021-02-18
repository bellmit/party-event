<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<script type="text/javascript"
	src="<c:url value="/lib/jquery/jquery-1.7.2.min.js"/>"></script>
<body>
<div class="content">
  <div class="data">
    <a href="javascript:void(0)" style="font-weight: bold; text-decoration: none; font-size: 16px;"
      onclick="rotateRight('theimage',90);">右转</a> 
    <a href="javascript:void(0)" style="font-weight: bold; font-size: 16px; text-decoration: none;" 
      onclick="rotateLeft('theimage',90);">左转</a>    
    <a href="<c:url value="${imgpath}"/>" target="_blank">查看原图</a><br>    
      <img src="<c:url value="${imgpath}"/>" id="theimage" style="cursor: pointer;" />
    </a>
    <div style="clear: both">
    </div>
  </div>
</div>
</body>
</html>
<script type="text/javascript" language="javascript">
$(document).ready(function(){
	loadWH();
});
var _w;
function loadWH(){
	var realWidth;//真实的宽度
	var realHeight;//真实的高度
	_w = parseInt($(window).width());//获取浏览器的宽度
	var img = $("#theimage");
	//这里做下说明，$("<img/>")这里是创建一个临时的img标签，类似js创建一个new Image()对象！
	$("<img/>").attr("src", $(img).attr("src")).load(function() {
	/*
	如果要获取图片的真实的宽度和高度有三点必须注意
	1、需要创建一个image对象：如这里的$("<img/>")
	2、指定图片的src路径
	3、一定要在图片加载完成后执行如.load()函数里执行
	*/
	realWidth = this.width;
	realHeight = this.height;
	//如果真实的宽度大于浏览器的宽度就按照100%显示
	if(realWidth>=_w){
	$(img).css("width","100%").css("height","auto");
	}
	else{//如果小于浏览器的宽度按照原尺寸显示
	$(img).css("width",realWidth+'px').css("height",realHeight+'px');
	}
	});
}

    function rotate(id, angle, whence) {
//       var stylewh = $("#"+id).attr("style");
      var p = document.getElementById(id);
      if (!whence) {
        p.angle = ((p.angle == undefined ? 0 : p.angle) + angle) % 360;
      }
      else {
        p.angle = angle;
      }
      if (p.angle >= 0) {
        var rotation = Math.PI * p.angle / 180;
      }
      else {
        var rotation = Math.PI * (360 + p.angle) / 180;
      }
      var costheta = Math.cos(rotation);
      var sintheta = Math.sin(rotation);
      if (document.all && !window.opera) {
        var canvas = document.createElement('img');
        canvas.src = p.src;
        canvas.height = p.height;
        canvas.width = p.width;
        canvas.style.filter = "progid:DXImageTransform.Microsoft.Matrix(M11=" + costheta + ",M12=" + (-sintheta) + ",M21=" + sintheta + ",M22=" + costheta + ",SizingMethod='auto expand')";
      }
      else {
        var canvas = document.createElement('canvas');
        if (!p.oImage) {
          canvas.oImage = new Image();
          canvas.oImage.src = p.src;
        }
        else {
          canvas.oImage = p.oImage;
        }
        //alert(canvas.width)
        canvas.style.width = canvas.width = Math.abs(costheta * canvas.oImage.width) + Math.abs(sintheta * canvas.oImage.height);
        canvas.style.height = canvas.height = Math.abs(costheta * canvas.oImage.height) + Math.abs(sintheta * canvas.oImage.width);
        var context = canvas.getContext('2d');
        context.save();
        if (rotation <= Math.PI / 2) {
          context.translate(sintheta * canvas.oImage.height, 0);
        }
        else if (rotation <= Math.PI) {
          context.translate(canvas.width, -costheta * canvas.oImage.height);
        }
        else if (rotation <= 1.5 * Math.PI) {
          context.translate(-costheta * canvas.oImage.width, canvas.height);
        }
        else {
          context.translate(0, -sintheta * canvas.oImage.width);
        }
        context.rotate(rotation);
        context.drawImage(canvas.oImage, 0, 0, canvas.oImage.width, canvas.oImage.height);
        context.restore();
      }
      canvas.id = p.id;
      canvas.angle = p.angle;
      p.parentNode.replaceChild(canvas, p);
//       $("#"+id).attr("style",stylewh);

      _w = parseInt($(window).width());//获取浏览器的宽度
	  	realWidth = canvas.style.width;
	  	realHeight = canvas.style.height;
	  	realWidth = realWidth&&realWidth.indexOf("px")?realWidth.replace("px",""):"0";
	  	//如果真实的宽度大于浏览器的宽度就按照100%显示
	  	if(realWidth>=_w){
	  	$("#"+id).css("width","100%").css("height","auto");
	  	}
	  	else{//如果小于浏览器的宽度按照原尺寸显示
	  	$("#"+id).css("width",realWidth+'px').css("height",realHeight);
	  	}
  	
    }

    //向右旋转
    function rotateRight(id, angle) {
      rotate(id, angle == undefined ? 90 : angle);
    }
    //向左旋转
    function rotateLeft(id, angle) {
      rotate(id, angle == undefined ? -90 : -angle);
    }
  </script>