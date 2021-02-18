
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
                    {field: 'JGMC', label: '机构名称', className: "unsortable"},
                    {field: 'JGDZ', label: '机构地址', className: "unsortable"},
                    {field: 'DHHM', label: '联系方式', className: "unsortable"},
                    {field: 'FRXM', label: '法人代表', className: "unsortable"},
                    {field: 'JGLB', label: '机构类别', className: "unsortable"},
                    {field: 'JYXZ', label: '机构性质', className: "unsortable"},
                    {field: 'ID', label: '操作', className: "unsortable",
                    	tableTransFunc:function(value,item){
                    		
                    		return "<a href='javascript:void(0)' onclick='newWindow(\""+value+"\")' style='color: #166fa8'>查看详情</a>";
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
            requestData:{"JGMC":$("#JGMC").val(),"JGLB":$("#JGLB").val(),linesPerPage:10},
            pageBarId: 'dwPager',
            pageBarTpl:"flat",
            currentPage:1,
            totalPage: 10,
            totalNum: 50
    };
	$("#dwTab").ulynlist(opts);
    $("#search-btn").click(function(){
    		$.fn.ulynlist.queryForm($("#dwTab"),$("#search-form"),opts,function(requestData){
                // console.info(requestData);
                //下面可以修改请求参数
            });
        
    });
    $("#search-reset").click(function(){
    	$("#JGMC").val('');
    
});
    
})

function newWindow(id) {
	var yoururl = detail.detailurl+"?id="+id;
	var height = $(window.parent.document).height();
	var width = $(window.parent.document).width();
	var top = (height-500)/2;
	var left = (width-700)/2;
	window.open(yoururl, '_blank','height=500,width=700,top='+top+',left='+left);
	}

function formatpc(str){
	var strTime = '';
	if(str !=null && str !="" && str.length == 14){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	}
	return strTime;
}


