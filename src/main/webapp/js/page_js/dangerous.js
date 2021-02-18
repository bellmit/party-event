
$(function(){
	var typeReal = '0';
	$(".checktype").each(function(){
		if($(this).hasClass("all-style")){
			if($(this).attr("data-id") != "0"){
				typeReal = $(this).attr("data-id");					
			}
		}
    });
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
                    {field: 'HAZARD_ADDRESS', label: '站点名称', className: "unsortable"},
                    {field: 'DUTY_UNIT', label: '责任单位', className: "unsortable"},
                    {field: 'ADDRESS', label: '地址', className: "unsortable"},
                    {field: 'METHANE_CONC', label: '沼气浓度', className: "unsortable"},
                    {field: 'TEMPERATURE', label: '温度', className: "unsortable"},
                    {field: 'ELEC_STATUS', label: '供电状态', className: "unsortable"},
                    {field: 'WATER_STATUS', label: '水位状态', className: "unsortable"},
                    {field: 'GATHER_TIME', label: '采样时间', className: "unsortable", 
                    	tableTransFunc:function(value,item){
                    		return formatpc(value);
                        }	
                    },
                    {field: 'DANGERLEVEL', label: '重大危险源级别', className: "unsortable"}
                    ,
                    {field: 'ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		if(item.WXY_TYPE=='1'||item.WXY_TYPE=='2'){
                    			return "<a href='javascript:void(0)' onclick=newWindowdetail(\""+config.detailurl+"?id="+value+"\") style='color: #166fa8'>详情</a>";
                    		}else{
                    			return "";
                    		}
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
            requestData:{type:typeReal,linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50
    };
	var opts1 = {
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
                    {field: 'HAZARD_ADDRESS', label: '站点名称', className: "unsortable"},
                    {field: 'DUTY_UNIT', label: '责任单位', className: "unsortable"},
                    {field: 'ADDRESS', label: '地址', className: "unsortable"},
                    {field: 'METHANE_CONC', label: '沼气浓度', className: "unsortable"},
                    {field: 'TEMPERATURE', label: '温度', className: "unsortable"},
                    {field: 'ELEC_STATUS', label: '供电状态', className: "unsortable"},
                    {field: 'WATER_STATUS', label: '水位状态', className: "unsortable"},
                    {field: 'GATHER_TIME', label: '采样时间', className: "unsortable", 
                    	tableTransFunc:function(value,item){
                    		return formatpc(value);
                        }	
                    },
                    {field: 'ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		return "<a href='javascript:void(0)' onclick=newWindowdetail(\""+config.detailurl+"?id="+value+"\") style='color: #166fa8'>详情</a>";
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
            requestData:{type:typeReal,linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50
    };
	var opts2 = {
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
                    {field: 'HAZARD_ADDRESS', label: '站点名称', className: "unsortable"},
                    {field: 'DUTY_UNIT', label: '责任单位', className: "unsortable"},
                    {field: 'ADDRESS', label: '地址', className: "unsortable"},
                    {field: 'DANGERLEVEL', label: '重大危险源级别', className: "unsortable"}
                    /*{field: 'ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		return "<a href='"+config.detailurl+"?id="+value+"&visiteditem=fire' style='color: #166fa8'>查看详情</a>";
                        }
                    }*/
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
            requestData:{type:typeReal,linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50
    };
	var opts3 = {
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
                    {field: 'HAZARD_ADDRESS', label: '站点名称', className: "unsortable"},
                    {field: 'ADDRESS', label: '地址', className: "unsortable"}
                    /*{field: 'ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		return "<a href='"+config.detailurl+"?id="+value+"&visiteditem=fire' style='color: #166fa8'>查看详情</a>";
                        }
                    }*/
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
            requestData:{type:typeReal,linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50
    };
	
	if(typeReal=='0'){
		$("#dwTab").ulynlist(opts);
	}else if(typeReal=='-1'){
		$("#dwTab").ulynlist(opts3);
	}else if(typeReal=='1'||typeReal=='2'){
		$("#dwTab").ulynlist(opts1);
	}else if(typeReal=='3'){
		$("#dwTab").ulynlist(opts2);
	}else if(typeReal=='4'){
		$("#dwTab").ulynlist(opts3);
	}
    $(".checktype").click(function(){
    	typeReal = $(this).attr("data-id");
    	if(typeReal == '0'){
    		$.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts,function(requestData){
                // console.info(requestData);
                //下面可以修改请求参数
            	$(".checktype").each(function(){
            		$(this).removeClass("all-style");
                });
            	requestData.type=typeReal;
            });
    	}
    	else if(typeReal == '1'||typeReal == '2'){
    		$.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts1,function(requestData){
                // console.info(requestData);
                //下面可以修改请求参数
            	$(".checktype").each(function(){
            		$(this).removeClass("all-style");
                });
            	requestData.type=typeReal;
            });
    	}else if(typeReal == '3'){
			$.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts2,function(requestData){
	            // console.info(requestData);
	            //下面可以修改请求参数
	        	$(".checktype").each(function(){
	        		$(this).removeClass("all-style");
	            });
	        	requestData.type=typeReal;
	        });
		}else{
			$.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts3,function(requestData){
	            // console.info(requestData);
	            //下面可以修改请求参数
	        	$(".checktype").each(function(){
	        		$(this).removeClass("all-style");
	            });
	        	requestData.type=typeReal;
	        });
		}
        
        $(this).addClass("all-style");
    });
    
})

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}

function newWindowdetail(yoururl) {
	var height = $(window).height();
	var width = $(window).width();
	var top = (height-415)/2;
	var left = (width-900)/2;
	window.open(yoururl, '_blank','height=430,width=930,top='+top+',left='+left);
	}



