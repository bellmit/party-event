
$(function(){
	$("select").select2();
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
                    {field:'CHECKED',checkbox:'ID',label:"全选",style:"width:80px",className:"sortable",
                    tableTransFunc:function(value,item){
                    	appendqx();
                    }
                    },
                    {field: 'REPORT_TIME', label: '状态', className: "unsortable", 
                    	tableTransFunc:function(value,item){
                    		var str = '';
                    		if(item.STATUS=='1'){
                    			str = "<img width='20px' height='20px' src='"+contextPath+"/js/gmap/images/greengif.gif'>"
                    		}else{
                    			var date = new Date(formatpc(value).replace(/-/g,"/"));
                        		var iDays  =  parseInt(Math.abs(new Date() - date )  /  1000  /  60  /  60  /24);
                    			if(iDays <= 3){
                    				str = "<img width='20px' height='20px' src='"+contextPath+"/js/gmap/images/greengif.gif'>"
                        		}else if(iDays > 3 && iDays <= 6){
                        			str = "<img width='20px' height='20px' src='"+contextPath+"/js/gmap/images/yellowgif.gif'>"
                        		}else{
                        			str = "<img width='20px' height='20px' src='"+contextPath+"/js/gmap/images/redgif.gif'>"
                        		}
                    		}
                    		return str;
                        }	
                    },
                    {field: 'REPORT_TIME', label: '上报时间', className: "unsortable", 
                    	tableTransFunc:function(value,item){
                    		return formatpc(value);
                        }	
                    },
                    {field: 'HAZARD_TYPE', label: '隐患类型', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		var list = value.split(',');
                    		var typecontent='';
                    		var str = "";
                    		for(var listi = 0 ; listi < list.length ; listi++){
                    			if(listi != 0){
                    				str += ',';
                    				typecontent += ',';
                    			}
		                		if(list[listi]=="0"){
		                            str += "<span>消防设施、器材、安全标志配置不符合标准</span>";
		                            typecontent += "消防设施、器材、安全标志配置不符合标准";
		                        }else if(list[listi]=="1"){
		                        	str += "<span>消防设施、器材、安全标志未保持完好有效</span>";
		                        	typecontent += "消防设施、器材、安全标志未保持完好有效";
		                        }else if(list[listi] =="2"){
		                        	str += "<span>损坏、挪用消防设施、器材</span>";
		                        	typecontent += "损坏、挪用消防设施、器材";
		                        }else if(list[listi] =="3"){
		                        	str += "<span>擅自拆除、停用消防设施、器材</span>";
		                        	typecontent += "擅自拆除、停用消防设施、器材";
		                        }else if(list[listi] =="4"){
		                        	str += "<span>占用、堵塞、封闭疏散通道、出口</span>";
		                        	typecontent += "占用、堵塞、封闭疏散通道、出口";
		                        }else if(list[listi] =="5"){
		                        	str += "<span>占用、堵塞消防车道；妨碍消防车通行</span>";
		                        	typecontent += "占用、堵塞消防车道；妨碍消防车通行";
		                		}else if(list[listi] =="6"){
		                        	str += "<span>埋压、圈占、遮挡消火栓</span>";
		                        	typecontent += "埋压、圈占、遮挡消火栓";
		                		}else if(list[listi] =="7"){
		                        	str += "<span>占用防火间距</span>";
		                        	typecontent += "占用防火间距";
		                		}else if(list[listi] =="8"){
		                        	str += "<span>其它消防安全违法行为和火灾隐患</span>";
		                        	typecontent += "其它消防安全违法行为和火灾隐患";
		                		}
                    		}
                    		if(str.length>20){
                    			str = str.substring(0, 20)+"...";
                    		}
                    		str = '<div title="'+typecontent+'">' + str;
                    		str += '</div>';
                    		return str;
                    }},
                    {field: 'HAZARD_ADDRESS', label: '隐患地址', className: "unsortable", width: 300},
                    {field: 'REPORT_PERSON', label: '上报人员', className: "unsortable"},
                    {field: 'PHONE', label: '联系方式', className: "unsortable"},
                    {field: 'STATUS', label: '处理状态', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		if(value=="0"){
                                return "<span style='color: #f00;'>未处理</span>";
                            }else if(value=="1"){
                                return "<span style='color: #5a8a00;'>已解决</span>";
                            }else if(value =="2"){
                                return "<span style='color: #1470af;'>误报</span>";
                            }else if(value =="3"){
                                return "<span style='color: #1470af;'>上报</span>";
                            }else{
                                var x = $.fn.ulynlistTable.toDisplayStoreDateStr(item.CREATE_TIME);
                                return "unknown("+x+")";
                            }
                        }
                    },
                    {field: 'ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		return "<a href='"+config.detailurl+"?fireId="+value+"&visiteditem=fire' style='color: #166fa8'>查看详情</a>";
                        }
                    }
                ],
                rownumbers: false
            },
            /*customData: [
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
            ],*/
            requestData:{"status":$("#status").val(),"type":$("#type").val(),"street":$("#street").val(),"startTime":$("#startTime").val(),"endTime":$("#endTime").val(),"userName":$("#userName").val(),linesPerPage:10},
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
        	requestData.type=$("#type").val();
        	requestData.status=$("#status").val();
        	requestData.street=$("#street").val();
        });
    });
    
    $("#search-reset").click(function(){
    	$("#s2id_type").find("span").eq(0).html("请选择");
    	$("#s2id_status").find("span").eq(0).html("未处理");
    	$("#type").val("");
    	$("#street").val("");
    	$("#startTime").val("");
    	$("#endTime").val("");
    	$("#userName").val("");
    	$("#status").val("0");
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





