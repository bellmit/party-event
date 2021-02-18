
$(function(){
//	$("select").select2();
	var pro = [{"src":"/images/logo/1.png","desc":"项目进度超前一个月以上"},{"src":"/images/logo/2.png","desc":"项目推进顺利"},
	           {"src":"/images/logo/3.png","desc":"项目推进缓慢"},{"src":"/images/logo/4.png","desc":"项目停滞不前"},
	           {"src":"/images/logo/5.png","desc":"问题正在推进落实"},{"src":"/images/logo/6.png","desc":"问题落实不力两次以上，黄牌督办"},
	           {"src":"/images/logo/7.png","desc":"问题久拖不决，红牌督办"}];
	opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"flat",
            tableColumn: {
                title: "示例",
                keyColumn: "",
                columns: [
                    {field: 'MAR_NAME', label: '项目名称', className: "unsortable", width: "11%"},
                    {field: 'TOTAL_INVEST', label: '总投资(万元)', className: "unsortable", width: "7%"},
                    {field: 'PLAN_INVEST', label: '年计划投资(万元)', className: "unsortable", width: "8%"},
                    {field: 'CONTENT', label: '建设内容和工作目标', className: "unsortable", width: "14%"},
                    {field: 'ACCU_INVEST', label: '累计完成投资(万元)', className: "unsortable", width: "11%",
                    	tableTransFunc:function(value,item){
                    		console.info(item);
                    	if(value!=undefined && value!="" && value!=null){
                    		return "("+item.ACCU_INVEST_TIME+"月)"+item.ACCU_INVEST;
                    	}
                		return "";
                        
                    }},
                    {field: 'PROGRESS', label: '形象进度', className: "unsortable", width: "34%",style:"padding: 7px;line-height:18px"},
                    {field: 'ASSESS', label: '进度评价', className: "unsortable", width: "5%",
                    	tableTransFunc:function(value,item){
                    		return "<img src='"+ulynlistPath.contextPath+pro[value].src+"' title='"+pro[value].desc+"'>";
                            
                        }
                    },
                    {field: 'BIG_PLATE', label: '所属板块', className: "unsortable"}
                ],
                rownumbers: false
            },
            requestData:{linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50
    }
//	$("#dwTab").ulynlist(opts);
});
