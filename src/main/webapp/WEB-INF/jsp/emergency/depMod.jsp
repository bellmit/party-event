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
    <title>部门详细信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"  />
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-3.5.1/select2.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/sslib/qlTree/qlTree.css"/>"/>
    <link href="<c:url value="/styles/css/flat/blue.css"/>" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/base.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/nineModules.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/detail.css"/>"/>

    <script type="text/javascript" src="<c:url value="/lib/jquery/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/select2-3.5.1/select2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/artTemplate/template-simple.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.pagebar.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/qlTree/qlTree.js"/>"></script>

<script src="<c:url value="/lib/layer/layer.min.js"/>"></script>
<script src="<c:url value="/lib/layer/extend/layer.ext.js"/>"></script>

    <script type="text/javascript" src="<c:url value="/js/icheck.min.js"/>"></script>
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
//             formatspan("${map1['REPORT_TIME'] }","reportTime");
//             formatspan("${map1['REPORT_TIME'] }","tableTime1");
//             formatspan("${map1['REPORT_TIME'] }","tableTime2");
            
//             formatspan($("#timespan-jd").html(),"timespan-jd");
//             formatspan($("#timespan-xf").html(),"timespan-xf");
//             $(".emType").html(repValue($(".emType").html()));
        });

        var EndTime= new Date();
        EndTime.setDate(EndTime.getDate()+5);
        function GetRTime(){
        	
            
            var NowTime = new Date();
            var t =EndTime.getTime() - NowTime.getTime();
            var d=Math.floor(t/1000/60/60/24);
            var h=Math.floor(t/1000/60/60%24);
            var m=Math.floor(t/1000/60%60);
            var s=Math.floor(t/1000%60);

            document.getElementById("t_d").innerHTML = d;
            document.getElementById("t_h").innerHTML = h;
            document.getElementById("t_m").innerHTML = m;
            document.getElementById("t_s").innerHTML = s;
        }
        if('${show}'=='show'){
        	setInterval(GetRTime,0);	
        }
        typelist=[];
    	<c:forEach items="${typelist }" var="type_item">
    	typelist.push({id:"${type_item.TYPE_ID}",name:"${type_item.TYPE_NAME}"});
    	</c:forEach>
    	</script>
</head>
<body>
<div class="content-box">
    <div class="cb-content" style="margin-left:0px">
        <div class="content">
            <div class="mainContent" style="top:0px">
                <div class="mcBox">
                    <div class="dwBottom">                        
                           <form id="search-form" method="post">
                    <div class="dwTop">
                        <div class="zzCondition clearfix">
                            <div class="subItem">
                                <label>部门名称：</label>
                                <div class="clearfix">
                                    <input type="hidden" value="${dept.DEP_ID}" id="did"/>
                                    <input type="hidden" value="${dept.DEP_LEVEL}" id="oldDepLevel"/>
                                    <input type="hidden" value="${dept.DEP_PARENT}" id="maxDepParent"/>
                                    <input type="hidden" value="${userInfo.DEP_ID}" id="depId"/>
                                    <input type="text" id="dep_name" name="dep_name" value="${dept.DNAME}" onblur="checkname(this)" class="form-control input-sm input-add"/>
                                    <input type="hidden" id="dname" name="dname" value="${dept.DNAME}"  class="form-control input-sm input-add"/>
                                    <input type="hidden" id="dparent" name="dparent" value="${dept.DEP_PARENT}"  class="form-control input-sm input-add"/>
                                    
                                </div>
                            </div>
                           <div class="clearfix"></div>
                            <div class="subItem">
                                <label>上级部门：</label>
								<div class="clearfix">
									<jsp:include page="../include/emergencyTree.jsp"></jsp:include>
								</div>
                            </div>
                            <div class="clearfix"></div>
                              <div class="subItem hide">
                               <label>部门级别：</label>
                                <div class="clearfix">
                                    <select value="${dept.DEP_LEVEL}" name="dep_level" id="dep_level" style="margin-right:0px">
                                        <option value="" ${dept.DEP_LEVEL==null?'selected="selected"':''}>请选择</option>
                                        <option value="1"${dept.DEP_LEVEL=='1'?'selected="selected"':'' }>1</option>
                                        <option value="2"${dept.DEP_LEVEL=='2'?'selected="selected"':'' }>2</option>
                                        <option value="3"${dept.DEP_LEVEL=='3'?'selected="selected"':'' }>3</option>
                                        <option value="4"${dept.DEP_LEVEL=='4'?'selected="selected"':'' }>4</option>
                                        <option value="5"${dept.DEP_LEVEL=='5'?'selected="selected"':'' }>5</option>
                                    </select>
                                </div>
                            </div>
                            <div class="clearfix"></div>
                             <div class="subItem">
                                <label></label>
                                <div class="clearfix">
                                	<div class="btns">
                                        <button type="button" class="btn btn-primary btn-sm" id="mod" onclick="go_mod()">修&nbsp;&nbsp;改</button>
                                        <a href="javascript:closeWindow();">
	                                        <button type="button" class="btn btn-success btn-sm" id="save">取&nbsp;&nbsp;消</button>
	                                    </a>
                                        
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    </form>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
var urlConfig = {
		checkdepName : '<c:url value="/de/checkdepName.do"/>',
		modDepartment:"<c:url value="/de/modDepartment.do"/>"
	};
	
function checkname(obj){
		var dep_name = $("#dep_name").val();
		var dname =$("#dname").val();
		var dep_parent=$("#depId").val();//获取选中的上级部门的id
		if(dep_name==dname){
			return;
		}
		if(dep_name==''){
			alert('部门名称不能为空');
			return;
		}	
	    var inputdata={"dep_name":$("#dep_name").val(),
			 		   "dname":$("#dname").val()
	     };
		 $.ajax({
		       type:"POST",
		       url:urlConfig.checkdepName,
		       data:inputdata,
		       dataType:"json",
		       success:function(data){
		   		if(data  && data.status){
		    	 //  alert("部门名称可修改");
		    	//   $("#mod").show();
		   			mod.disabled = "";
		   		}else{
		   			alert("该部门已存在,请重新修改名字,否则不能修改");
		   		   // $("#mod").hide();
		   			mod.disabled = "disable";
		   		}
		       },
		       beforeSend:function(){
		       },
		       error:function(){
		    	   alert("该部门已存在");
				}
			 });
	
}	
function go_mod(){
	var dep_name= $("#dep_name").val();
	var did=$("#did").val();
	var dep_parent=$("#depId").val();//获取选中的上级部门的id
	var dep_level=$("#dep_level").val();
	var oldDepLevel=$("#oldDepLevel").val();
	var dname =$("#dname").val();//修改用户时原名字
	var dparent =$("#dparent").val();//修改用户时的原上级
	var maxDepParent =$("#maxDepParent").val();
	if(dep_name==''){
		alert('部门名称不能为空');
		return;
	}
	if(dep_parent==did){
		alert('不能选择本部门作为上级！');
		return;
	}
	if(dep_level==0){
		alert("不能选择一级组织为当前上级组织");
		return;
	}
	if(oldDepLevel-dep_level>=1){
		alert("不能选择低级别的上级部门作为上级");
		return;
	}
// 	if(oldDepLevel==dep_level+1 || oldDepLevel==dep_level){
// 		alert("不能修改最高级别的部门");
// 		return;
// 	}
    if(maxDepParent==0){
    	alert("不能修改最高级别的部门");
	    return;
    }
    var inputdata={"dep_name":$("#dep_name").val(),
		 		   "did":$("#did").val(),
		 		   "dep_parent":$("#depId").val(),
		 		   "dep_level":$("#dep_level").val(),
		 		   "dname":$("#dname").val(),
		 		   "dparent":$("#dparent").val(),
		 		   "oldDepLevel":$("#oldDepLevel").val()
     };
	 $.ajax({
	       type:"POST",
	       url:urlConfig.modDepartment,
	       data:inputdata,
	       dataType:"json",
	       success:function(data){
	   		if(data && data.status){
	    	   alert("修改成功");
// 	    	   window.location.href=window.location.href;
	 			closeWindow();
	   		}else{
	   			alert("修改失败");
	   		}
	       },
	       beforeSend:function(){
	       },
	       error:function(){
	    	   alert("修改失败");
			}
		 });
}
function formatpc(str){
	var strTime = '';
	strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	$("#REPORT_TIME").val(strTime);
}


function closeWindow() {
// 	parent.location.reload();
	parent.onloadWindow();
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(index);	
}


function detailFun(obj){
	var mod=document.getElementById("mod");
	if(obj.level==1){
		alert("不能选择一级组织为当前上级组织");
	//	$("#mod").hide();
		mod.disabled = "disable";
	}else{
	  //$("#mod").show();
	    mod.disabled = "";
	    $("#dep_level").val(obj.level-1);
	}
}


</script>

</body>
</html>