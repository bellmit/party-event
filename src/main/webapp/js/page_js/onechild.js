
$(function(){
	var opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"flat",
            tableColumn: {
                title: "示例",
                keyColumn: "",
                columns: [
                    {field: 'APP_TIME', label: '预约时间', className: "unsortable",
                    	tableTransFunc:function(value,item){
                            return value.substring(0,4) + '-' + value.substring(4,6) + '-' + value.substring(6,8);;
                        }
                    },
                    {field: 'MALE_NAME', label: '男方姓名', className: "unsortable"
                    },
                    {field: 'MALE_PHO_NUM', label: '男方联系方式', className: "unsortable"
                    },
                    {field: 'FEMALE_NAME', label: '女方姓名', className: "unsortable"
                    },
                    {field: 'FEMALE_PHO_NUM', label: '女方联系方式', className: "unsortable"
                    },
                    {field: 'STATUS', label: '状态', className: "unsortable",
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
                    {field: 'ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		return "<a href='"+config.detailurl+"?id="+value+"&visiteditem=child' style='color: #166fa8'>查看详情</a>";
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
    	$("#FEMALE_NAME").val("");
    	$("#MALE_NAME").val("");
    	$("#startTime").val("");
    	$("#endTime").val("");
    	$("#status").val("");
    });
})

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 8){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) ;
	}
	
	return strTime;
}

