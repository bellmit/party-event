
$(function(){
	var opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"flat",
            tableColumn: {
            	loadFilter:function(data){
            		$(".setLinesPerPage").attr("readonly","readonly");
            		var lilist = $(".pagebottonlist").find("li").eq(0).html().split('|');
            		$("#allcount").html(lilist[0].replace("共有",' ').replace('条信息',''));
	            },
                title: "示例",
                keyColumn: "",
                columns: [
                    {field:'XM',label:"姓名",className:"sortable"
                    },
                    {field:'XZZXZ',label:"现住址详址",className:"sortable"
                    },
                    {field:'SFZH',label:"身份证号",className:"sortable"
                    },
                    {field:'TSRQ',label:"特殊人群",className:"sortable",
                    	tableTransFunc:function(value,item){
                    		var str = "";
                    		if(value.indexOf("05")>=0){
                    			str += '社区矫正对象-';
                    		}
                    		if(value.indexOf("06")>=0){
                    			str += '吸毒人员-';
                    		}
                    		if(value.indexOf("07")>=0){
                    			str += '刑释解教人员-';
                    		}
                    		if(value.indexOf("19")>=0){
                    			str += '易肇事肇祸精神病人-';
                    		}
                    		str = str.substring(0, str.length-1);
                    		return str;
                        }
                    }
                ],
                rownumbers: false
            },
            requestData:{"TYPE":$("#TYPE").val(),"STREET":$("#STREET").val(),linesPerPage:10,currentPage:1},
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
	        	requestData.TYPE=$("#TYPE").val();
	        	requestData.STREET=$("#STREET").val();
	        });
	    });
})

$(function(){
    //侧边栏渲染
//    renderSidebar();

   
    
    
});

//function tableRender(){
//    $("#dwTab").ulynlist({
//    	url:top.contextPath+'/fire/fire_list.do',
//        basePath:top.ulynlistPath.basePath,
//        tableTpl:"flat",
//        tableColumn: {
//            title: "示例",
//            keyColumn: "",
//            columns: [
//                {field:'CHECKED',checkbox:'ID',className:"sortable"},
//                {field: 'HAZARD_TYPE', label: '隐患类型', className: "sortable"},
//                {field: 'HAZARD_STREET', label: '隐患地址', className: "sortable", width: 300},
//                {field: 'REPORT_PERSON', label: '上报人员', className: "sortable"},
//                {field: 'STATUS', label: '处理状态', className: "sortable"},
//                {field: '', label: '操作', className: "sortable",
//                    bodyContent:"<a href='detail.html' style='color: #166fa8'>查看详情</a> "
//                }
//            ],
//            rownumbers: false
//        },
//        customData: [
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":1},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":2},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":3},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":1},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路67号","SBRY":"林晓晓","CLZT":2},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路68号","SBRY":"林晓晓","CLZT":3},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":1},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":2},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":3},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":1},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路67号","SBRY":"林晓晓","CLZT":2},
//            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路68号","SBRY":"林晓晓","CLZT":3}
//        ],
//        requestData:{"status":$("#status").val(),"type":$("#type").val(),"street":$("#street").val(),"startTime":$("#startTime").val(),"endTime":$("#endTime").val(),"userName":$("#userName").val(),linesPerPage:10},
//        pageBarId: 'dwPager',
//        pageBarTpl:"flat",
//        currentPage:1,
//        totalPage: 10,
//        totalNum: 50
//    });
//}





