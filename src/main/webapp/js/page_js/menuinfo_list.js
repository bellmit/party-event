$(function(){

    var loadEditIfr = function(url,title){
    	parent.$.layer({
            type: 2,
            maxmin: true,
            shadeClose: false,
            title: title==undefined?'添加内容':title,
            shade: [0.1,'#fff'],
            offset: ['25px',''],
            area: [2*$(window).width()/3+'px', 3*$(window).height()/4 +'px'],
            iframe: {src: url},
            close: function(index){
                if(title==undefined){
                    $.fn.ulynlist.refresh($("#ulyn-table-id"));
                }
            }
        });
    };
var opts = {
        tableColumn:{
            title:"示例",
            keyColumn:"",
            columns:[
                {field:'TYPE_NAME',label:'描述',className:"sortable"},
                {field:'INFO_ID',label:'关联内容',className:"sortable"},
                {field:'TYPE',label:'类型',className:"sortable",
                    tableValue:{"id":"CMS内容","url":"静态url"}
                },
                {field:'',label:'操作区',
                    bodyContent:'<a name="mod" href="javascript:void(0);" sid="[INFO_ID]" did="[MENU_ID]" dtype="[TYPE]">修改</a>&nbsp;'
                }
            ],
            rownumbers:true
        },
        afterTableRender:function(){
            $("a[name=mod]").click(function(){
            	if($(this).attr("dtype")=="id"){
            		loadEditIfr(parent.contextPath+'/manage/go_info_edit.do?goPage=info_editor&infoId='+$(this).attr("sid"),"修改内容");
            	}else{
            		loadEditIfr(parent.contextPath+'/manage/go_uploadFile.do?rtype=m&code='+$(this).attr("did"),"修改文件");
            	}
            });
        },
        requestData:{title:$("#title").val(),linesPerPage:10},
        basePath:ulynlistPath.basePath,
        url:contextPath+'/manage/getMenuInfoList.do',
        tableTpl:'flat',
        pageBarTpl:'flat'
    }
    $("#ulyn-table-id").ulynlist(opts);

    $("#search-btn").click(function(){    	
        $.fn.ulynlist.queryForm($("#ulyn-table-id"),$("#search-form"),opts,function(requestData){
        });
    });

    $("#add-info").click(function(){
        var url = $(this).attr("data-url");
        loadEditIfr(url);
    });
})