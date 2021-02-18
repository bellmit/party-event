var opts="";
$(function(){
	var typeReal = '0';
	$(".checktype").each(function(){
		if($(this).hasClass("all-style")){
			if($(this).attr("data-id") != "0"){
				typeReal = $(this).attr("data-id");					
			}
		}
    });
	opts = {
    		url:ulynlistPath.url,
            basePath:ulynlistPath.basePath,
            tableTpl:"flat",
            tableColumn: {
            	loadFilter:function(data){
            		if($(".pagebottonlist").find("li").eq(0)){
            		var lilist = $(".pagebottonlist").find("li").eq(0).html().split('|');
            		$("#allcount").html(lilist[0].replace("共有",' ').replace('条信息',''));
            		}
	            },
                title: "示例",
                keyColumn: "",
                columns: [
                    {field: '', label: '会议主题', className: "unsortable",bodyContent:  '<div name="TITLE_TEMP" hm="[TITLE]">[TITLE]</div>'},
                    {field: '', label: '会议地点', className: "unsortable",bodyContent:  '<div name="TITLE_TEMP" hm="[MEET_ADDRESS]">[MEET_ADDRESS]</div>'},
                    {field: 'START_TIME', label: '开始时间', className: "unsortable",trans : "toDisDate"},
                    {field: 'END_TIME', label: '结束时间', className: "unsortable",trans : "toDisDate"},
                    {field: 'SIGN_STIME', label: '签到开始时间', className: "unsortable",trans : "toDisDate"},
                    {field: 'SIGN_ETIME', label: '签到结束时间', className: "unsortable",trans : "toDisDate"},
                    {field: 'PERSON_NUM', label: '参会人数', className: "unsortable"},
                    {
						field : '',
						label : '操作区',
						bodyContent :
						'<a name="detail" href="javascript:void(0);" sid="[EXT_ID]" num="[PERSON_NUM]">查看</a>'
                    }
                ],
                rownumbers: false
            },
            requestData:{type:typeReal,linesPerPage:10,meetId:$("#meetId").val()},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50,
            afterTableRender : function() {
    			$("a[name=detail]").click(function(){
            		if($(this).attr("num")>0){
            			loadEditIfr(contextPath
								+ '/meet/detail_map.do?mType=1&EXT_ID='
								+ $(this).attr("sid"), "查看一张图");
            		}else{
            			alert("该会议没有签到记录！");
            		}
            	});
    			$("div[name=TITLE_TEMP]").each(function(){
            		$(this).parent().attr("title",$(this).attr("hm") );
            		var value = $(this).html();
            		if (value.length > 20) {
            			value =  value.substr(0, 20)+"...";
    				} 
            		$(this).parent().html(value);
            	});
            }
    };
	$("#dwTab").ulynlist(opts);
});

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
function onloadWindow() {
	$.fn.ulynlist.refresh($("#dwTab"));
}
var loadEditIfr = function(url, title) {
	$.layer({
        type: 2,
        maxmin: true,
        shadeClose: false,
        title: title==undefined?'添加内容':title,
        shade: [0.1,'#fff'],
        offset: ['25px',''],
        area: ['920px', $(window).height() +'px'],
        iframe: {src: url},
        close: function(index){
        	onloadWindow();
        }
    });
	 $("a[class='xubox_max xulayer_png32']").trigger("click");
};
$("#search-btn").click(
		function() {
			$.fn.ulynlist.queryForm($("#dwTab"), $("#search-form"), opts,
					function(requestData) {
						// console.info(requestData);
						// 下面可以修改请求参数
						requestData.TITLE = $("#TITLE").val();
						requestData.START_TIME = $("#START_TIME").val();
						requestData.END_TIME = $("#END_TIME").val();
					});
		});

$("#search-reset").click(function() {
	$("#TITLE").val("");
	$("#START_TIME").val("");
	$("#END_TIME").val("");
});