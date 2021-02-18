
$(function(){
	$("select").select2();
	var opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"flat",
            tableColumn: {
                title: "示例",
                keyColumn: "",
                columns: [
                    {field: 'NAME', label: '姓名', className: "unsortable"},
                    {field: 'PHONE', label: '联系方式', className: "unsortable"},
                    {field: 'BIRTHDAY', label: '出生日期', className: "unsortable"},
                    {field: 'SEX', label: '性别', className: "unsortable"},
                    {field: 'EDUCATION', label: '教育经历', className: "unsortable"},
                    {field: 'MONEY', label: '期望薪资', className: "unsortable"},
                    {field: 'ZPGZ', label: '招聘工种', className: "unsortable"},
                    {field: 'QZSJ', label: '求职时间', className: "unsortable"}/*,
                    {field: 'STATUS', label: '审核状态', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		if(value=="0"){
                                return "<span style='color: #5a8a00;'><a onclick='checkQZXX(this)' data-id='"+item.ID+"' data-status='1' href='javascript:void(0)'>审核通过</a></span>"+"<span style='color: #1470af;'><a onclick='checkQZXX(this)' data-id='"+item.ID+"' data-status='2' href='javascript:void(0)'>审核不通过</a></span>";
                            }else if(value=="1"){
                                return "<span style='color: #5a8a00;'>审核通过</span>";
                            }else if(value =="2"){
                                return "<span style='color: #1470af;'>审核不通过</span>";
                            }else{
                                var x = $.fn.ulynlistTable.toDisplayStoreDateStr(item.CREATE_TIME);
                                return "unknown("+x+")";
                            }
                        }
                    }*/
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
        	requestData.name=$("#name").val();
        	requestData.zpgz=$("#zpgz").val();
    		//requestData.status=$("#status").val();	
        });
    });
    
})

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}

function checkQZXX(obj){
	$.ajax({
        type:"POST",
        url:config.checkurl+"?id="+$(obj).attr("data-id")+"&status="+$(obj).attr("data-status"),
        data:'',
        dataType:"json",
        async:false,
        success:function(data){
        	if(data=='success'){
        		alert("审核成功");
        		var opts = {
        	    		url:ulynlistPath.url,
        	            basePath:ulynlistPath.basePath,
        	            tableTpl:"flat",
        	            tableColumn: {
        	                title: "示例",
        	                keyColumn: "",
        	                columns: [
        	                    {field: 'NAME', label: '姓名', className: "unsortable"},
        	                    {field: 'PHONE', label: '联系方式', className: "unsortable"},
        	                    {field: 'BIRTHDAY', label: '出生日期', className: "unsortable"},
        	                    {field: 'SEX', label: '性别', className: "unsortable"},
        	                    {field: 'EDUCATION', label: '教育经历', className: "unsortable"},
        	                    {field: 'MONEY', label: '期望薪资', className: "unsortable"},
        	                    {field: 'ZPGZ', label: '招聘工种', className: "unsortable"},
        	                    {field: 'QZSJ', label: '求职时间', className: "unsortable"},
        	                    {field: 'STATUS', label: '审核状态', className: "unsortable",
        	                    	tableTransFunc:function(value,item){
        	                    		if(value=="0"){
        	                                return "<span style='color: #5a8a00;'><a onclick='checkQZXX(this)' data-id='"+item.ID+"' data-status='1' href='javascript:void(0)'>审核通过</a></span>"+"<span style='color: #1470af;'><a onclick='checkQZXX(this)' data-id='"+item.ID+"' data-status='2' href='javascript:void(0)'>审核不通过</a></span>";
        	                            }else if(value=="1"){
        	                                return "<span style='color: #5a8a00;'>审核通过</span>";
        	                            }else if(value =="2"){
        	                                return "<span style='color: #1470af;'>审核不通过</span>";
        	                            }else{
        	                                var x = $.fn.ulynlistTable.toDisplayStoreDateStr(item.CREATE_TIME);
        	                                return "unknown("+x+")";
        	                            }
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
        		$.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts,function(requestData){
                    // console.info(requestData);
                    //下面可以修改请求参数
                	requestData.name=$("#name").val();
                	requestData.zpgz=$("#zpgz").val();
            		//requestData.status=$("#status").val();	
                });
        	}else{
        		alert("审核失败");
        	}
        },
        beforeSend:function(){
        },
        error:function(){
        	alert("审核失败");
        }
	 });
}




