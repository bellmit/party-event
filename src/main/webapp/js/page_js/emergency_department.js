
$(function(){
	var loadEditIfr = function(url,title){
		$.layer({
	        type: 2,
	        maxmin: true,
	        shadeClose: false,
	        title: title==undefined?'添加部门':title,
	        shade: [0.1,'#fff'],
	        offset: ['25px',''],
	        area: [(screen.width/3)+'px', '45%'],
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
                    {field: 'DNAME', label: '部门名称',style:"width:25%", className: "unsortable"	
                    },
                    {field: 'PNAME', label: '上级部门',style:"width:25%", className: "unsortable"	
                    },
//                    {field:'DEP_LEVEL',label:"部门级别",className:"unsortable",style:"width:25%"
//                    },
                    {field:'',label:'操作区',
                        bodyContent:'<a name="mod" href="javascript:void(0);" did="[DEP_ID]">修改</a>&nbsp;'+
                        	'<a name="del" href="javascript:void(0);" did="[DEP_ID]">删除</a>&nbsp;'
                    }
                ],
                rownumbers: false
            },
            afterTableRender:function(){
                $("a[name=detail]").click(function(){
                    loadEditIfr(contextPath+'/de/detail.do?id='+$(this).attr("did"),"部门查看");
                });
                $("a[name=mod]").click(function(){
                    loadEditIfr(contextPath+'/de/detail.do?id='+$(this).attr("did")+'&isedit=isedit',"部门编辑");
                });
                $("a[name=del]").click(function(){
                	var did = $(this).attr("did");
                	$.layer({
                        shade: [0],
                        area: ['250','150'],
                        dialog: {
                            msg: '确定要删除?',
                            btns: 2,
                            type: 4,
                            btn: ['确定','取消'],
                            yes: function(){
                                $.post(contextPath+'/de/deldep.do',{id:did},function(data){
                                   if(data && data.status){
                                       layer.msg('删除成功', 1, 1);
                                       onloadWindow();
                                      // $.fn.ulynlist.refresh($("#dwTab"));
                                   }else if(data.msg=="-1"){
                                       layer.alert('该部门有用户,请先删除所有用户！！！！', 8);
                                   }else if(data.msg=="-2"){
                                       layer.alert('该部门有下属部门,请先删除所有的下属部门！！！！', 8);
                                   }else if(data.msg=="-3"){
                                	   layer.alert('该部门有下属部门,请先删除所有下属部门的用户！！！！');
                                   }
                                },"json");
                            }, no: function(){

                            }
                        }
                    });
                    //loadEditIfr(parent.contextPath+'/work/get_zpxx.do?id='+$(this).attr("sid"),"修改内容");
                });
            },
            requestData:{"dep_name":$("#dep_name").val(),linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50
    }
	$("#dwTab").ulynlist(opts);
    $("#search-btn").click(function(){
        $.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts,function(requestData){
            // console.info(requestData);
            //下面可以修改请求参数
        	requestData.dep_name=$("#dep_name").val();
        });
    });
    
    $("#search-reset").click(function(){   
    	$("#dep_name").val("");
    });
    
    $("#add-btn").click(function(){
        loadEditIfr(config.addUrl);
    });   
})

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}

function chkSpan(x,id){
	$("span[latv^='"+id+"']").each(function(i){
		$(this).removeClass('hasSelect');
		if(x==i){
			$(this).addClass("hasSelect");
			$("#"+id).val($(this).attr("value"));
			$("#search-btn").click();
		}
	});
}
function onloadWindow(){
	$.fn.ulynlist.refresh($("#dwTab"));
}


