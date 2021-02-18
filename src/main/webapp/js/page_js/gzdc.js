
$(function(){
	var opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"ss_air_notice",
            tableColumn: {
                title: "",
                keyColumn: "",
                columns: [
                    {field:'',label:'标题',className:"unsortable fl tips",bodyContent:""}, 
	                             {field:'TITLE',label:'标题',className:"sortable snotice_ul",width:150,
	                             	tableTransFunc:function(value,item){
	                             		return "<a href='"+config.detailurl+"?id="+item.INFO_ID+"' name=\"title\" target='_blank' class=\"fl\">"+value+"</a>";
	                             	}
	                             },
	                             {field:'CREATE_TIME',label:'标题',className:"sortable fr time",width:150,trans:"toDisDate"}
                ],
                rownumbers: false
            },
            requestData:{linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50
    };
	$("#dwTab").ulynlist(opts);
    
})

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}


