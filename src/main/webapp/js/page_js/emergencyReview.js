
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
                    	tableValue:{"0":"设施二维码","1":"问题反映"}	
                    },
                    {field: 'USER_NAME', label: '反馈人',style:"width:100px", className: "unsortable"	
                    },
                   /* {field: 'STATUS', label: '办理状态', className: "unsortable",style:"width:70px",	
                    	tableValue:{"1":"未办理","0":"未办理","2":"处理中","3":"处理中","4":"处理中","5":"处理中","6":"<font color='red'>已办理</font>"}	
                    },*/
                    {field: 'SQ', label: '社区审核', className: "unsortable",style:"width:8%",	
                    	tableValue:{"1":"同意","0":"不同意","-1":"--"}	
                    },
                    {field: 'JD', label: '镇街审核', className: "unsortable",style:"width:8%",	
                    	tableValue:{"1":"同意","0":"不同意","-1":"--"}	
                    },
                    {field: '', label:'处理',style:"width:80px", bodyContent:
                        '<a name="detail" href="javascript:void(0);" id="[MSG_ID]">详情</a>'
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
                    loadEditIfr(contextPath+'/emergency/reviewDetail.do?id='+$(this).attr("id"),"查看明细信息");
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


