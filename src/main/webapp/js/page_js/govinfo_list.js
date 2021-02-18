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
                {field:'TITLE',label:'标题',className:"sortable",orderColumn:"T.TITLE"},
                {field:'AUTHOR',label:'发布人',className:"sortable"},
                {field:'CHANNEL_NAME',label:'所属栏目',className:"unSortable"},
                {field:'UPDATE_TIME',label:'发布时间',className:"sortable",trans:"toDisDate"},
                {field:'STATUS',label:'状态',className:"sortable",orderColumn:"I.STATUS",
                    tableValue:{"0":"草稿","1":"审核中","2":"审核通过","3":"回收站","4":"审核未通过"}
                },
                
                {field:'',label:'操作区',
                    bodyContent:'<a name="detail" href="javascript:void(0);" sid="[INFO_ID]">查看</a>&nbsp;' +
                        '<a name="mod" href="javascript:void(0);" sid="[INFO_ID]">修改</a>&nbsp;' +
                        '<a name="del" href="javascript:void(0);" sid="[INFO_ID]" data-title="[TITLE]">删除</a>'
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
            	if(sid=='21f582c1c9214f898a8cccbdd30fa87c'||sid=='2df67cc1733447f086934efcfdb61c23'
            		||sid=='7b7b6c8132d340499883263dc00c8dc5'||sid=='48b921ca25474ac391ef00f8544889fc'
            			||sid=='855acdc9eb8a48b7998ceb852eae54fc'||sid=='8d7f9d7b927e417c81a021222c94e2b3'){
            		parent.layer.alert('该数据为系统初始化内容不能删除',8);
            		return false;
            	}
            	//var b = hasPermission("del","contentdel",perConf.perUrl);
            	if(1)//b&&b.del)
            	{
                var title = $(this).attr("data-title");
                var sid = $(this).attr("sid");
                parent.$.layer({
                    shade: [0],
                    area: ['auto','auto'],
                    dialog: {
                        msg: '确定要删除['+title+']?',
                        btns: 2,
                        type: 4,
                        btn: ['确定','取消'],
                        yes: function(){
                            $.post(parent.contextPath+'/manage/delCmsInfo.do',{infoId:sid},function(data){
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
                }else
        		{
        			parent.layer.alert('删除失败：没有权限',8);
        		}
            });
        },
        requestData:{channelId:channel_id,linesPerPage:10},
        basePath:ulynlistPath.basePath,
        url:contextPath+'/manage/getInfoList.do',
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