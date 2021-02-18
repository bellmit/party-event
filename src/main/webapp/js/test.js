function table(){
	 //通知信息分类接收人设置
    var js_message_style = {
    	url: ulynlistPath.url,
        basePath : ulynlistPath.basePath,
        tableTpl:"zeus",
        tableColumn:{
            title:'ulynlist',
            keyColumn:"",
            columns:[
                {field:'USERNAME',overflowview:'normal',label:'姓名',className:"unSortable"},
                {field:'SEX',overflowview:'normal',label:'性别',className:"unSortable"},
                {field:'PHONE',overflowview:'normal',label:'联系电话',className:"unSortable"},
                {field:'REMAKE',overflowview:'normal',label:'备注',className:"unSortable"},
                {field: '', label: '操作', className: "sortable",width:"300",
                    tableTransFunc:function(value,item){
                        return '<a class="table_do_a btn_edit" name="show_detial"  teache_name="'+item.TEACHE_NAME+'" user_id="'+item.USER_ID+'" school_id="'+item.SCHOOL_ID+'" href="javascript:void(0)" >查看详细</a>' +
                             '<a class="table_do_a btn_del" name="delete_receiver" user_id="'+item.USER_ID+'" school_id="'+item.SCHOOL_ID+'" href="javascript:void(0)" >删除</a>'
                    }
                }
            ],
            rownumbers:false
        },
        requestData : {
			linesPerPage : 10
		},
		pageBarId : 'tablePagebar',
		pageBarTpl : "zeus",
		currentPage : 1,
		totalPage : 10,
		totalNum : 50,
        afterTableRender:function(){
        },
        extra:{linesPerPageEditable:true},
        pageBarId: 'js-message-style-tablePageBar',
        pageBarTpl:"zeus"
    }
    
    $('#js-message-style').ulynlist(js_message_style);  

}

$(function(){
//	var a = parseInt(prompt("请输入数字"));
//	var b = parseInt(prompt("请输入数字"));
//	var c = parseInt(prompt("请输入数字"));
//	t = (temp = a>b?a:b)>(temp1 = b>c?b:c)?temp:temp1
//	alert(t)
	
//	var a = parseInt(prompt("请输入数字"));
//	var b = parseInt(prompt("请输入数字"));
//	
//	if((a%b==0)||(a+b>1000)){
//		alert(a)
//	}else{
//		alert(b)
//	}
	table()
})
