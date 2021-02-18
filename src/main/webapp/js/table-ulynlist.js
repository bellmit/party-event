/**
 * Created by Administrator on 2016/8/8.
 */
/**
 * 版权所有：厦门畅享信息技术有限公司
 * Copyright 2014 Xiamen Sunsharing Information CO., LTD.
 * All right reserved.
 *====================================================
 * 文件名称: list.js
 * 修订记录：
 * No    日期                作者(操作:具体内容)
 * 1.    2016/8/8            Administrator(创建:创建文件)
 *====================================================
 * 文件描述：(说明本文件需要实现的内容)
 *
 */

$(function () {
    //例子一：表格的使用
    table();
    function table(){
        var opts = {
            //ulynlist的根文件夹路径
            basePath:"../../sslib/ulynlist",
            //表格的div的id
            tableId:"table",
            //表格模版名
            tableTpl:"zeus",

            tableColumn:{
                title:'ulynlist',
                keyColumn:"",
                //以行为单位对样式进行处理。index：行索引，从0开始；item：对应于该行记录的对象。
                rowStyler:function(item,index){

                },
                //以列为单位对表头进行设置
                columns:[
                    //checkbox属性定义表格主键,唯一标识该行记录；checkAllCheckboxId属性可以设置全选按钮的位置，如果不设置默认位于表头
                    {field:'',checkbox:'xm',className:"sortable",checkAllCheckboxId:"checkAll"},
                    //className默认为sortable（排序），设置为unSortable（不排序）
                    {field:'xm',label:'姓名',className:"unSortable"},
                    //以列为单位对值进行转换，优先级：tableValue > tableTransFunc > trans，tableValue表示表码转换，tableTransFunc表示自定义转换，trans表示日期转换
                    {field:'gssq',label:'归属社区',className:"unSortable",
                        tableValue:{"1":"禾山社区","2":"禾缘社区","3":"禾欣社区","4":"五缘湾北社区"}
                    },
                    {field:'jgjb',label:'监管级别',className:"unSortable",
                        tableTransFunc:function(value,item){
                            if(value=="1"){
                                return '<i class="icoTip tipRed"></i>'
                            }else if(value=="2"){
                                return '<i class="icoTip tipGreen"></i>'
                            }else{
                                return '<i class="icoTip tipBlue"></i>'
                            }
                        }
                    },
                    //控制trans转译的格式，有三个值对应格式："HY"-['-', '-', ' ', ':', ':']，"DOT"-['.', '.', ' ', ':', ':'],CN-['年', '月', '日 ', ':', ':']，默认值为HY
                    {field:'zfrq',label:'走访日期',className:"sortable",trans: 'toDisDate',dateType: 'DOT'},
                    //overflowview属性设置内容超出区域的显示方式，默认为ellipsis（以省略号方式显示），设置为normal则超出区域显示
                    {field:'zfr',label:'走访人',overflowview:'ellipsis',className:"unSortable"},
                    //checkbox表示多选框列，field表示值列，bodyContent表示自定义内容列，优先级：checkbox > bodyContent > field
                    //根据cz_tip是否为true来禁用操作区的整体title显示。
                    {field:'',label: '操作',overflowview:'normal',className: "unSortable",cz_tip:"true", style: "text-align:left", width: "130px",
                        bodyContent: "<a class='btn-link' href='#'>查看</a>" +
                        "<a class='btn-link' href='#' title='修改'>修改</a>" +
                        "<div class='dropdown btn-link' style='display: inline-block;cursor: pointer'>" +
                        "<a type='button' class='dropdown-toggle' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false' style='font-weight: 600;'>···</a>" +
                        "<ul class='dropdown-menu'>" +
                        "<li><a class='btn_del' href='#'>删除</a></li>" +
                        "<li><a href='#'>操作记录</a></li>" +
                        "</ul>" +
                        "</div>"

                    }
                ],
                //上下翻页时候是否记住checkbox的状态，默认为false
                rememberCheckbox: true,
                //是否显示行号，默认为false
                rownumbers: false,
                //行号的表头，默认为"序号"，当rownumbers属性有效时生效
                rownumberslabel: "编号"
            },
            //列表渲染加载完回调方法
            afterTableRender:function(){
                $(".btn_del").click(function () {
                    $("#delTip").modal("show");
                });
            },

            customData:[
                {"gssq":"1","xm":"江海","jgjb":"1","zfrq":"20150505","zfr":"江书记"},
                {"gssq":"2","xm":"周莉","jgjb":"2","zfrq":"20150502","zfr":"周书记"},
                {"gssq":"3","xm":"杨颖","jgjb":"3","zfrq":"20150504","zfr":"杨书记"},
                {"gssq":"2","xm":"孙曦","jgjb":"3","zfrq":"20150508","zfr":"孙书记"},
                {"gssq":"4","xm":"林鑫","jgjb":"2","zfrq":"20150505","zfr":"林书记"},
                {"gssq":"1","xm":"赵武","jgjb":"1","zfrq":"20150501","zfr":"赵书记"}
            ],
            //请求数据传递的参数
            requestData:{linesPerPage:2,currentPage:1},
            //自定义模板参数
            extra:{linesPerPageEditable:true},
            //是否使用分页条，默认为true
            usePageBar: true,
            //分页条的div的id
            pageBarId: 'tablePagebar',
            //分页条模版名
            pageBarTpl:"zeus",
            //分页条渲染加载完回调方法
            afterPaginationRender:function(){

            },
        };

        $("#table").ulynlist(opts);
        //queryForm方法提供简便的form查询辅助
        // $.fn.ulynlist.queryForm($("#js-table"),$("#queryForm"),opts,function(requestData){
        //     //修改请求参数
        //     requestData.linesPerPage = "3";
        //     requestData.currentPage = "2";
        // });

        //取得选中的项数组,第2个参数设置为true时取得选中项的整行数据，设置为false或为空时取得选中项的主键数据
        $("#choose").on("click",function(){
            var checked = $.fn.ulynlistTable.checkbox($("#table"),true);
            console.info(checked);
        });

        //表格向后台进行一次刷新列表请求
        $("#refresh").on("click",function(){
            //第一种方法
            $.fn.ulynlist.refresh($("#table"));
            //第二张方法
            $("#table").trigger("refresh");
        });
    }

    //例子二：分页条的使用
    pagebar();
    function pagebar(){
        var opts = {
            //ulynlist的pagebar的根文件夹路径
            pageBarPath:"../../sslib/ulynlist/pagebar",
            //当前页码
            currentPage:1,
            //总页数
            totalPage:3,
            //每页记录数
            linesPerPage:2,
            //总记录数
            totalNum: 6,
            //自定义模板参数
            extra:{linesPerPageEditable:true},
            //分页条的div的id
            pageBarId: 'pagebar',
            //分页条模版名
            pageBarTpl:"zeus",
            //分页条渲染加载完回调方法
            afterPaginationRender:function(){

            }
        };

        $("#pagebar").ulynlistPagination(opts);
    }
});