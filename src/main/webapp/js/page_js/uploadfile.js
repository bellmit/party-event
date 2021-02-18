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
                {field:'MG_NAME',label:'模块名称',className:"sortable"},
                {field:'FIRE_PATH',label:'文件名称',className:"sortable"},
                {field:'COMPLETEFILEPATH',label:'文件完整路径',className:"unSortable"},
                {field:'CREATE_TIME',label:'创建时间',className:"sortable",trans:"toDisDate"},
                {field:'',label:'操作区',
                    bodyContent:
                        '<a name="del" href="javascript:void(0);" sid="[FILE_ID]" data-title="[FIRE_PATH]">删除</a>'
                }
            ],
            rownumbers:true
        },
        afterTableRender:function(){
            $("a[name=detail]").click(function(){
                loadEditIfr(parent.contextPath+'/manage/go_info_edit.do?goPage=info_detail&channel_id='+channel_id+'&infoId='+$(this).attr("sid"),"内容查看");
            });
            $("a[name=mod]").click(function(){
                loadEditIfr(parent.contextPath+'/manage/go_info_edit.do?goPage=info_editor&channel_id='+channel_id+'&infoId='+$(this).attr("sid"),"修改内容");
            });
            $("a[name=del]").click(function(){
            	var sid = $(this).attr("sid");
                var title = $(this).attr("data-title");
                var sid = $(this).attr("sid");
                parent.$.layer({
                    shade: [0],
                    area: ['auto','auto'],
                    dialog: {
                        msg: '此操作会删除已保存在服务器上的文件，确定要删除['+title+']吗?',
                        btns: 2,
                        type: 4,
                        btn: ['确定','取消'],
                        yes: function(){
                            $.post(parent.contextPath+'/manage/delfile.do',{fileid:sid},function(data){
                               if(data.status){
                                   $.fn.ulynlist.refresh($("#ulyn-table-id"));
                                   parent.layer.msg('删除成功', 1, 1);
                               }else{
                                   parent.layer.alert('删除失败：'+data.msg, 8);
                               }
                            },"json");
                        }, no: function(){

                        }
                    }
                });
                
            });
        },
        requestData:{code:code,linesPerPage:10},
        basePath:ulynlistPath.basePath,
        url:contextPath+'/manage/getfileList.do',
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