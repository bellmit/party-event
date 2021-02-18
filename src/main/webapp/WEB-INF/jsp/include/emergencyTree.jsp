<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" isELIgnored="false"%>
    <link href="<c:url value="/sslib/ztree/css/zTree.css"/>" rel="stylesheet" type="text/css" >
	<script type="text/javascript" src="<c:url value="/sslib/ztree/js/jquery.ztree.core-3.5.js"/>" ></script>
    <script type="text/javascript">
        //单选框样式
        $(document).ready(function(){
            var zTree=null;
       		initTree();
       		$("#parent").bind( 
       			    "click", function(){ 
       			    	$("#zTree").show();
       			       $("#parent_select").show();
       			       h = $("#parent_select").height();
       			       if(h>$(".mc_auto").height())
       			       {
       			    	   $(".mc_auto").height(h+$(".mc_auto").height());
       			       }
       			       return false; 
       			    } 
       			); 
       		$(document).bind( 
       			    "click", function(e){ 
       			        var src = e.target; 
       			        if(src.id && (src.id.indexOf("zTree")>=0||src.id ==="parent")){ 
       			            return false; 
       			        }else{ 
       			        	$("#zTree").hide();
       			            $("#parent_select").hide();
       			        } 
       			    } 
       			); 
        	});
    	function initTree(){
    		$.fn.zTree.init($("#zTree"), setting, zNodes);	
    		zTree = $.fn.zTree.getZTreeObj("zTree");
    		var depId =$("#depId").val();
    		var node = zTree.getNodeByParam("id", depId);		
    		if(node!=null){
    			$("#parent").val(node.name);
    			zTree.selectNode(node);	
    		};			
//     		var pnode = zTree.getNodeByParam("id", node.pId);
//     		if(pnode!=null){
//     			$("#parent").val(pnode.name);
//     			zTree.selectNode(pnode);	
//     		};			
    			
    	}
    	function onDblClick(event, treeId, treeNode){
    		$("#parent").val(treeNode.name);
    		$("#depId").val(treeNode.id);
    		$("#parent_select").hide();
    		$("#zTree").hide();
    		if( typeof detailFun == 'function' ){
				detailFun(getNodeObj(treeNode.id));
			}
    		return false;
    	}
    	function onClick(event, treeId, treeNode, clickFlag) {	
    		zTree.selectNode(zTree.getNodeByParam("id", treeNode.id));
    		if(!treeNode.isParent){
    			$("#parent").val(treeNode.name);
    			$("#depId").val(treeNode.id);
    			$("#parent_select").hide();
    			$("#zTree").hide();
    			if( typeof detailFun == 'function' ){
    				detailFun(getNodeObj(treeNode.id));
    			}
    			return false;
    		}else{
        		zTree.expandNode(treeNode);
        	}
    		
    	}
    	var zNodes = [];
    	<c:forEach items="${depList }" var="depInfo">
    	zNodes.push({id:"${depInfo.DEP_ID}", pId:"${depInfo.DEP_PARENT}", name:"${depInfo.DEP_NAME}",level:"${depInfo.DEP_LEVEl}"});
    	</c:forEach>
   		var setting = {
   			view: {
   				dblClickExpand: false
   			},
   			data: {
   				simpleData: {
   					enable:true,
   					idKey: "id",
   					pIdKey: "pId",
   					rootPId: ""
   				}
   			},
   			callback: {
   				//beforeClick: beforeClick,
   				onClick: onClick,
   				onDblClick: onDblClick
   			}

   		};
   		function getNodeObj(id){
   	   		for(var i=0;i<zNodes.length;i++){
   	   	   		if(id==zNodes[i].id){
   	   	   	   		return zNodes[i];
   	   	   	   	}
   	   	   	}
   	   	   	return "0";
   	   	}
    	</script>
<input id="parent" class="form-control input-sm" style="width: 200px;cursor:default;" readonly="readonly" placeholder="请选择所属组织结构"/>											
<div id="parent_select" style="width:200px;height:220px; display:none;
 border: 1px solid #ccc;z-index:200;overflow-y:auto;overflow-x:hidden;
 position: absolute;background: #fff; ">
	<ul id="zTree" class="ztree1"></ul>
</div>