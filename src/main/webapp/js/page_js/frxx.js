
$(function(){
	var opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"flat",
            tableColumn: {
            	loadFilter:function(data){
            		var lilist = $(".pagebottonlist").find("li").eq(0).html().split('|');
            		$("#allcount").html(lilist[0].replace("共有",' ').replace('条信息',''));
	            },
                title: "示例",
                keyColumn: "",
                columns: [
                    {field: 'QYMC', label: '企业名称', className: "unsortable"
                    },
                    {field: 'FRDB', label: '法人代表', className: "unsortable"
                    },
                    {field: 'SFZH', label: '证件号码', className: "unsortable", width: 150},
                    {field: 'LXFS', label: '电话号码', className: "unsortable"},
                    {field: 'SSGSS', label: '所属工商局', className: "unsortable"},
                    {field: 'HY', label: '行业', className: "unsortable"
                    },
                    {field: 'GSLX', label: '工商类型', className: "unsortable"
                    },
                    {field: 'JYFW', label: '经营范围', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		var resultStr = '';
                    		if(value!=null){
                    			if(value.length > 10){
                    				resultStr = value.substring(0,10);
                    			}else{
                    				resultStr = value;
                    			}
                    		}
                		return "<div title='"+value+"'>"+resultStr+"</div>";
                    }
                    },
                    {field: 'NWZ', label: '内外资', className: "unsortable"
                    },
                    {field: 'ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		return "<a href='"+config.detailurl+"?id="+value+"' style='color: #166fa8'>详情</a>";
                        }
                    }
                ],
                rownumbers: false
            },
            requestData:{"frdb":$("#frdb").val(),"hy":$("#hy").val(),"qymc":$("#qymc").val(),"gslx":$("#gslx").val(),linesPerPage:10},
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
        	requestData.frdb=$("#frdb").val();
        	requestData.hy=$("#hy").val();
        	requestData.qymc=$("#qymc").val();
        	requestData.gslx=$("#gslx").val();
        });
    });
    
    $("#search-reset").click(function(){
    	$(".all-style").removeClass("all-style");
    	$("#全部").addClass("all-style");
    	$("#frdb").val("");
    	$("#hy").val("");
    	$("#qymc").val("");
    	$("#gslx").val("");
    });
})

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}
/*$(function(){
    //侧边栏渲染
//    renderSidebar();

    //表格渲染
    tableRender();
    $("#search-btn").click(function(){
        $.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts,function(requestData){
            // console.info(requestData);
            //下面可以修改请求参数
        	requestData.type=$("#type").val();
        	requestData.status=$("#status").val();
        	requestData.street=$("#street").val();
//            requestData.table = "da";
//            requestData.table = requestData.table +"22";
        });
    });
    
    
});

function tableRender(){
    $("#dwTab").ulynlist({
    	url:top.contextPath+'/fire/fire_list.do',
        basePath:top.ulynlistPath.basePath,
        tableTpl:"flat",
        tableColumn: {
            title: "示例",
            keyColumn: "",
            columns: [
                {field:'CHECKED',checkbox:'ID',className:"sortable"},
                {field: 'HAZARD_TYPE', label: '隐患类型', className: "sortable"},
                {field: 'HAZARD_STREET', label: '隐患地址', className: "sortable", width: 300},
                {field: 'REPORT_PERSON', label: '上报人员', className: "sortable"},
                {field: 'STATUS', label: '处理状态', className: "sortable"},
                {field: '', label: '操作', className: "sortable",
                    bodyContent:"<a href='detail.html' style='color: #166fa8'>查看详情</a> "
                }
            ],
            rownumbers: false
        },
        customData: [
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":1},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":2},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":3},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":1},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路67号","SBRY":"林晓晓","CLZT":2},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路68号","SBRY":"林晓晓","CLZT":3},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":1},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":2},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":3},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路66号","SBRY":"林晓晓","CLZT":1},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路67号","SBRY":"林晓晓","CLZT":2},
            {"YHLX":"防火","YHDZ":"新山村街道平安社区钢花路68号","SBRY":"林晓晓","CLZT":3}
        ],
        requestData:{"status":$("#status").val(),"type":$("#type").val(),"street":$("#street").val(),"startTime":$("#startTime").val(),"endTime":$("#endTime").val(),"userName":$("#userName").val(),linesPerPage:10},
        pageBarId: 'dwPager',
        pageBarTpl:"flat",
        currentPage:1,
        totalPage: 10,
        totalNum: 50
    });
}*/





