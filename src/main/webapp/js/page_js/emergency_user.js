
$(function(){
	var loadEditIfr = function(url,title){
		$.layer({
	        type: 2,
	        maxmin: true,
	        shadeClose: false,
	        title: title==undefined?'添加用户':title,
	        shade: [0.1,'#fff'],
	        offset: ['25px',''],
	        area: [(screen.width-120)+'px', '90%'],
	        iframe: {src: url},
	        close: function(index){
//	            if(title==undefined){
	        	onloadWindow();
//	            }
	        }
	    });
		 $("a[class='xubox_max xulayer_png32']").trigger("click");
	};
	var opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"flat",
            tableColumn: {
            	loadFilter:function(data){
//            		var lilist = $(".pagebottonlist").find("li").eq(0).html().split('|');
//            		$("#allcount").html(lilist[0].replace("共有",' ').replace('条信息',''));
	            },
                title: "示例",
                keyColumn: "",
                columns: [
                    {field: 'USER_NAME', label: '用户名称',style:"width:10%", className: "unsortable"	
                    },
                    {field: 'MOBILE', label: '电话',style:"width:15%", className: "unsortable"	
                    },
                    {field:'DEP_NAME',label:"组织结构",className:"sortable",style:"width:10%"
                    },
                    {field: 'WX_USER_ID', label: '微信账户', className: "unsortable",style:"width:8%"
                    },
                    {field: 'MH_USER', label: '门户账户', className: "unsortable",style:"width:8%"
                    },
                    /*{field: 'SMS_FLAG', label: '值班', className: "unsortable",style:"width:5%",tableTransFunc:function(value,item){
                    	if(value=='1'){
                    		return "<font color='green'>是</font>";
                    	}
                    	return "<font color='red'>否</font>";
                    }
                    },*/
                    {field: '', label:'操作',style:"width:10%", bodyContent:
                        '<a name="detail" href="javascript:void(0);" id="[USER_ID]">修改</a><a name="del" href="javascript:void(0);" id="[USER_ID]">删除</a>'
                    }
                ],
                rownumbers: false
            },
            requestData:{search:$("#search").val(),linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50,
            afterTableRender:function(){
                $("a[name=detail]").click(function(){
                	if($(this).attr("id")=="[]"){
                		alert("模块数据加载错误,请联系管理员.");
                		return;
                	}
                    loadEditIfr(contextPath+'emergency/userDetail.do?id='+$(this).attr("id"),"修改用户信息");
                });
                $("a[name=del]").click(function(){
                	if($(this).attr("id")=="[]"){
                		alert("模块数据加载错误,请联系管理员.");
                		return;
                	}
                	var id = $(this).attr("id");
                	$.layer({
                        shade: [0],
                        area: ['250','150'],
                        dialog: {
                            msg: '确定要删除?',
                            btns: 2,
                            type: 4,
                            btn: ['确定','取消'],
                            yes: function(){
                                $.post(contextPath+'emergency/delUser.do',{userId:id},function(data){
                                    if(data.status){
                                    	layer.msg('删除成功', 1, 1);
                                        $.fn.ulynlist.refresh($("#dwTab"));
                                    }else{
                                        layer.alert('删除失败', 8);
                                    }
                                },"json");
                            }, no: function(){

                            }
                        }
                    });
                });
            }
    };
	$("#dwTab").ulynlist(opts);
    $("#search-btn").click(function(){
        $.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts,function(requestData){
            //下面可以修改请求参数
//        	requestData.em_type=$(".select2").val();
        });
    });    
    $("#search-reset").click(function(){   
    	$("#search").val("");
    });
    $("#add-btn").click(function(){
    	loadEditIfr(contextPath+'/emergency/userDetail.do',"添加用户信息");
    });
    
});

function onloadWindow(){
	$.fn.ulynlist.refresh($("#dwTab"));
}


