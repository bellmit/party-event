
$(function(){
	var loadEditIfr = function(url,title){
		layer.open({
	        type: 2,
//	        maxmin: true,
	        resize:true,
	        shadeClose: false,
	        title: title==undefined?'采集应急信息':title,
	        shade: [0.5,'#000'],
	        offset: ['25px',''],
//	        area: [(screen.width-120)+'px', '90%'],
	        area: ['800px','80%'],
//	        iframe: {src: url},
	        content : url,
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
                title: "示例",
                keyColumn: "",
                columns: [
                	{field: 'PROBLEM_TITLE', label: '问题名称',style:"width:12%", className: "unsortable"
					},
					{field: 'MSG_PHONE', label: '手机号码',style:"width:100px", className: "unsortable"	
					},
					{field: 'CREATE_TIME', label: '反映时间',style:"width:12%", className: "unsortable",trans:"toDisDate"
					},
					{field: 'TRANS_NAME', label: '受理人', className: "unsortable",style:"width:100px",	
					},
					/*{field: 'ADDRESS', label: '所在位置', style:"width:12%",className: "unsortable",tableTransFunc:function(value,item){
                    	if(value && value.length > 25){
                    		return "<a title='"+value+"'>"+value.slice(0,18)+"...</a>";
                    	}
                    	return value;
                    }
                    },*/ 
                    {field: 'MSG_SOURCE', label: '来源', className: "unsortable",style:"width:100px",
                    	tableValue:{"0":"设施二维码","1":"问题反映","2":"居民来访","3":"社区走访","4":"党建联席会","5":"党建共同体","30":"党员报道"}
                    },
                    {field: 'USER_NAME', label: '反馈人',style:"width:100px", className: "unsortable"	
                    },
                    {field: 'STATUS', label: '回复状态', className: "unsortable",style:"width:70px",	
                    	tableValue:{"1":"待回复","0":"未办理","2":"待回复","3":"待回复","4":"待回复","5":"待回复","30":"待回复","6":"<font color='red'>已回复</font>","7":"<font color='red'>已回复</font>","8":"<font color='red'>已回复</font>","9":"<font color='red'>已回复</font>","10":"<font color='red'>已回复</font>"}
                    },
                    {field: 'LATERSTATUS', label: '办结标识', className: "unsortable",style:"width:90px",	
                    	//tableValue:{"0":"未受理","20":"后续跟踪处理","21":"<font color='red'>已办结</font>","undefined":"未办理"}	
                    	tableTransFunc:function(value,item){
                    		if(value=="20"){
                    			return "后续跟踪处理";
                    		}else if(item.STATUS=="20"){
                                return  "<font color='red'>不予受理</font>";
                            }else if(value=="21"){
                    			return  "<font color='red'>已办结</font>";
                    		}else if(item.STATUS=="1"){
                    			return "办理中";
                    		}else if(value=="1"){
                    			return "办理中";
                    		}else{
                    			return "未办理"
                    		}
                    		
                    	}
                    },
                    /*{field: 'MSG_DESC', label: '事项', className: "unsortable",tableTransFunc:function(value,item){
                    	if(value && value.length > 25){
                    		return "<a title='"+value+"'>"+value.slice(0,24)+"...</a>";
                    	}
                    	return value;
                    }
                    },*/
                    {field:'EM_TYPE',label:"问题类型",className:"sortable",style:"width:10%",tableTransFunc:function(value,item){
                    	var typelist1 = ulynlistPath.typelist;
                    	var v = value.split(",");
                    	var ret = "";
                    	for(var i=0;i<v.length;i++){
                    		for(var j=0;j<typelist1.length;j++){
                    			if(typelist1[j].id == v[i]){
                    				ret += ret==""?typelist1[j].name:","+typelist1[j].name;
                    				break;
                    			}
                    		}
                    	}
                    	return ret;
                    }
                    },
                   /* {field: 'WORK_UNITS', label: '单位',style:"width:10%", className: "unsortable"	
                    },
                    {field: 'TRAN_STATUS', label: '办理状态', className: "unsortable",style:"width:8%",	
                    	tableValue:{"0":"未办理","1":"<font color='red'>已办理</font>"}	
                    },*/	
                    {field: 'a', label:'处理',style:"width:120px", /*bodyContent:
                        '<a name="detail" href="javascript:void(0);" id="[MSG_ID]">详情[STATUS]</a>'+*/
                    	tableTransFunc:function(value,item){
                    		if(item.STATUS==6||item.STATUS==7||item.STATUS==8||item.STATUS==9||item.STATUS==10){
                    			return '<a name="detail" href="javascript:void(0);" id="'+item.MSG_ID+'">详情</a>'+
                    			 '<a name="laterdetail" href="javascript:void(0);" id="'+item.MSG_ID+'">后续处理</a>'
                    		}else{
                    			return  '<a name="detail" href="javascript:void(0);" id="'+item.MSG_ID+'">详情</a>'
                    		}
                    		
                    	}
                    }
                ],
                rownumbers: false
            },
            requestData:{"em_type":$(".select2").val(),"rk_status":$("#rk_status").val(),"rp_status":$("#rp_status").val(),"tran_status":$("#tran_status").val(),"msg_desc":$("#msg_desc").val(),linesPerPage:10},
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
                    loadEditIfr(contextPath+'/emergency/detail.do?id='+$(this).attr("id")+'&contextPath='+contextPath,"查看明细信息");
                });
                $("a[name=laterdetail]").click(function(){
                    loadEditIfr(contextPath+'/emergency/detail.do?id='+$(this).attr("id")+'&later=later&contextPath='+contextPath,"查看明细信息");
                });
            },
        	afterPaginationRender:function(data){
        		var pageBottom = $(".pagebottonlist").find("li").eq(0).html();
        		if(pageBottom){
            		var lilist = pageBottom.split('|');
            		$("#allcount").html(lilist[0].replace("共有",' ').replace('条信息',''));
        		}
            }
    }
	$("#dwTab").ulynlist(opts);
    $("#search-btn").click(function(){
        $.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts,function(requestData){
             console.info(requestData);
            //下面可以修改请求参数
        	requestData.em_type=$(".select2").val();
        	requestData.rk_status=$("#rk_status").val();
        	requestData.rp_status=$("#rp_status").val();
        	requestData.tran_status=$("#tran_status").val();
        	requestData.msg_desc=$("#msg_desc").val();
        	requestData.msg_title=$("#msg_title").val();
        	requestData.msg_source=$("#msg_source").val();
        	requestData.msg_name=$("#msg_name").val();
        	requestData.startTime=$("#startTime").val();
        	requestData.endTime=$("#endTime").val();
        	
        });
    });
    $("#add-btn").click(function(){
        loadEditIfr(contextPath+'/emergency/addEM.do',"采集应急信息");
    });
    
//    $("#search-reset").click(function(){
//    	$(".all-style").removeClass("all-style");
//    	$("#全部").addClass("all-style");
//    	$(".select2").val("");
//    	$("#rk_status").val("");
//    	$("#rp_status").val("");
//    	$("#tran_status").val("");
//    	$("#msg_status").val("");
//    });
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


