
$(function(){
	var opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"flat",
            tableColumn: {
                title: "示例",
                keyColumn: "",
                columns: [
                    {field: 'CREATE_TIME', label: '申请时间', className: "unsortable",
                    	tableTransFunc:function(value,item){
                                return formatpc(value);
                        }
                    },{field: 'APPLY_NAME', label: '申请人姓名', className: "unsortable"
                },
                {field: 'CONTACT_NUM', label: '联系方式', className: "unsortable"
                },
                    {field: 'REQ_GOV_INFO_NAME', label: '名称', className: "unsortable"
                    },
                    {field: 'REQ_GOV_INFO_NUM', label: '文号', className: "unsortable", width: 300},
                    {field: 'AUDIT_STATUS', label: '申请结果', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		if(value=="0"){
                                return "<span style='color: #f00;'>未通过</span>";
                            }else if(value=="1"){
                                return "<span style='color: #5a8a00;'>通过</span>";
                            }else if(value =="2"){
                                return "<span style='color: #1470af;'>等待审核</span>";
                            }
                        }
                    },
                    {field: 'APPLY_ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		return "<a href='"+config.detailurl+"?id="+value+"&visiteditem=gov' style='color: #166fa8'>查看详情</a>";
                        }
                    }
                ],
                rownumbers: true
            },
            requestData:{linesPerPage:10},
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
        	requestData.startTime=$("#startTime").val();
        	requestData.endTime=$("#endTime").val();
        	requestData.status=$("#status").val();
        	requestData.street=$("#userName").val();
        });
    });
    
    $("#search-reset").click(function(){
    	$("#s2id_status").find("span").eq(0).html("请选择");
    	$("#userName").val("");
    	$("#startTime").val("");
    	$("#endTime").val("");
    	$("#status").val("");
    });
})

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}

