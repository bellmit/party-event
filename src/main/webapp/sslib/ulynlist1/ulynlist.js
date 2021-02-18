/**
 * User: ulyn
 * Date: 13-10-10
 * Time: 下午3:00
 * Version: 5.0.0预览
 *          修改设置页脚每页几条后，默认以第一页开始加载
 *          增加获取当前列表查询条件的函数
 *          增加全局的列表参数配置$.fn.ulynlist.globalSetup
 *          增加多列表区域公用一份数据展现的功能  $.fn.ulynlist.combine  todo 多表联动应该用事件监听的方式来做，目前有点乱，后续改造
 *          列定义增加是否渲染列的字段show，经常遇到一个列表不同用户显示列可能不同
 *          列定义的tableTransFunc入参增加rowIdx作为列表行索引
 *          增加tableColumn属性方法rownumbersTrans，自定义行号转换，默认返回行号数值，比如有些列表行号用ABCD字母表示等
 *          增加设值渲染方法setData,setRequestData
 *          增加refresh方法第二个参数removeCache，标识刷新是否删除缓存
 *          重构对于checkbox的支持，支持md5每行数据项作为唯一值，取值更丰富。注意设值渲染在rememberCheckbox为真的情况下不会清空选中的项
 *          支持amd模块化加载的方式
 *          对于前台分页的，支持列自定义排序
 *          列可以拖拽并记忆 - 这个功能只能提供相应接口，并对特定模版进行实现，其他自定义模块需要自行实现 -- todo
 *          后续改造...还有更多精彩等着你...
 * Version: 4.0
 *          增加 $.fn.ulynlist.exportQuery 导出的功能
 * Version: 3.2
 *          增加tableColumn的columns的checkAllCheckboxId字段，提供给外部自定义全选的checkbox的位置，当此字段有值则表头使用label显示
 *          修订pagebar对参数标准化，防止外部传入的几个数字类型的变成string
 *          修订总页数为0时候，flat模版显示页码的bug
 * Version: 3.1
 *          增加tableColumn的columns的escapeHTML字段，支持列的html的转译
 *          增强几个Number属性的字段，防止外部传入string，导致bug
 *          flat增加extra参数的linesPerPageEditable字段，当为false时，每页几条不能编辑
 *          flat模版一个文字描述错误，"每条显示" -> "每页显示"
 * Version: 3.0
 *          增加页脚设置每页几条的事件定义
 *          新增页脚模版：flat
 *          增加beforeLoad和afterLoad的入参定义
 *          [重要]升级artemplate2.0.3,使用新的自定义语法扩展，所以本版本不兼容先前版本的模版定义
 * Version: 2.4
 *          增加customAjax参数，提供外部自定义获取数据的方式;
 *          修复afterTableRender重复两次的bug;
 *          修订无数据时候checkbox也被选中的bug;
 *          提供取得当前入参条件的方法$(ulynlist)[0].getCurrentParams();
 *
 *          增加isFullRow参数，控制是否自动填充满表格条数;
 *          增加模版：flat
 * Version: 2.3 增加对checkbox分页后选中状态的记忆功能。
 * Version: 2.2 正式版发布
 */
;(function (factory) {
    if (typeof define === "function" && define.amd) {
        // AMD模式
        define("ulynlist",["jquery","ulynlist.table","ulynlist.pagebar" ], factory);
    } else {
        // 全局模式
        factory(jQuery);
    }
}(function($) {
    "use strict";
    var data_key_table_opts = 'ulynlist_table_opts';
//    var data_key_pagebar_opts = 'ulynlist_pagebar_opts';
    var data_key_data = 'ulynlist_data';
    var data_key_combines = 'ulynlist_combines';
    //默认的正序排序方式
    var ascSortCompareFn = function(val1,val2){
        if(val1 > val2) return 1;
        if(val1 < val2) return -1;
        return 0;
    };
    $.fn.ulynlist = function(options) {
        // iterate and reformat each matched element
        return this.each(function() {
            var cur_obj = $(this);
            var opts = initUlynlist(cur_obj,options);
            createUlynlist(opts,cur_obj,true);
        });
    };
    function initUlynlist(ulynlistObj,options){
        return function(ulynlistObj,options){
            var opts = $.extend(true,{}, $.fn.ulynlist.defaults, options);
            if(!opts.tablePath){
                opts.tablePath = opts.basePath + "/table";
            }
            if(opts.usePageBar){
                opts.sortSelf = false;
            }
            if(!opts.pageBarPath){
                opts.pageBarPath = opts.basePath + "/pagebar";
            }
            if(!opts.requestData.linesPerPage){
                opts.requestData.linesPerPage = 10;
            }
            if(!opts.requestData.currentPage){
                opts.requestData.currentPage = 1;
            }
            opts.beforeLoad();
            var temp = opts.afterTableRender;
            opts.afterTableRender = function(ulynlistObj,opts){
                temp(ulynlistObj,opts);
                opts.afterLoad();
            };
            //设置监听事件
            $(ulynlistObj).unbind("clickSort").bind("clickSort",function(event,sortField,sortType){
                opts.requestData.sortField =  sortField;
                opts.requestData.sortType = sortType;
                var columns = opts.tableColumn.columns;
                //遍历所有，也是为了重置sortType
                for(var key in columns){
                    if(columns[key].field == sortField){
                        columns[key].sortType = sortType;
                        if(columns[key].orderColumn){
                            opts.requestData.sortField = columns[key].orderColumn;
                        }
                    }else{
                        columns[key].sortType = null;
                    }
                }
                if(opts._mainObj){
                    opts._mainObj.trigger("clickSort", [sortField, sortType]);
                    return;
                }
                createUlynlist(opts,ulynlistObj);
            });
            $("#"+opts.pageBarId).unbind("clickPage").bind("clickPage",function(event,page){
                opts.requestData.currentPage = Number(page);
                if(opts._mainObj){
                    opts._mainPageBar.trigger("clickPage", [page]);
                    return;
                }
                createUlynlist(opts,ulynlistObj);
            });

            //设置每页几条记录的事件监听
            $("#"+opts.pageBarId).unbind("setLinesPerPage").bind("setLinesPerPage",function(event,linesPerPage){
                opts.requestData.linesPerPage = Number(linesPerPage);
                opts.requestData.currentPage = 1;
                if(opts._mainObj){
                    opts._mainPageBar.trigger("setLinesPerPage", [linesPerPage]);
                    return;
                }
                createUlynlist(opts,ulynlistObj);
            });

            //设置刷新事件监听
            $(ulynlistObj).unbind("refresh").bind("refresh",function(event,removeCache){
//                $.fn.ulynlistTable.clearCheckboxCache(cur_obj);
                if(opts._mainObj){
                    opts._mainPageBar.trigger("refresh", [removeCache]);
                    return;
                }
                createUlynlist(opts,ulynlistObj,removeCache);
            });

            //设置监听setData
//            $(ulynlistObj).unbind("setData").bind("setData",function(event,list,pageData,mainObj){
//                if(opts._mainObj && opts._mainObj.length>0 && opts._mainObj[0] === mainObj[0]){
//
//                }
//            });
            return opts;
        }(ulynlistObj,options);
    }
    function createUlynlist(opts,obj,rebuild){
        if(opts.customData){
            if($.isArray(opts.customData)){
                createUlynlistForCustomData(obj,opts,rebuild);
                triggerSetData(opts,obj);
            }else{
                info("你是在坑我吗?customData居然不给我传数组！！！还要我显示???");
            }
        }else{
            //使用服务端请求数据
            opts.customAjax(opts.requestData,function(data){
                if(createUlynlistForAjaxData(obj,opts,data,rebuild)){
                    triggerSetData(opts,obj);
                }
            },opts);
        }
    }
    function createUlynlistForCustomData(obj,opts,rebuild){
        //使用自定义传入的数据显示
        if(opts.usePageBar){
            //有使用pageBar，则要进行分页
            //有分页条了，排序由我处理，列表只负责展示即可
            opts.sortSelf = false;
            //先确定是否排序
            if(opts.requestData.sortField){
                var sortField = opts.requestData.sortField;
                var sortType = opts.requestData.sortType;
                var columns = opts.tableColumn.columns;
                if(columns){
                    var sortColumn = null;
                    for(var i= 0,l=columns.length;i<l;i++){
                        if(columns[i].field==sortField){
                            columns[i].sortType = sortType;
                            sortColumn = columns[i];
                        }else{
                            columns[i].sortType = null;
                        }
                    }
                    if(sortColumn){
                        if(sortColumn.customOrderFunc === 'number'){
                            sortColumn.customOrderFunc = function(val1,val2){
                                return ascSortCompareFn(Number(val1),Number(val2));
                            };
                        }else if(typeof sortColumn.customOrderFunc != 'function'){
                            sortColumn.customOrderFunc = ascSortCompareFn;
                        }
                        //有指定的排序域
                        opts.data = opts.data.sort(
                            function(a, b){
                                if(sortType=="asc"){
                                    return sortColumn.customOrderFunc(a[sortField],b[sortField]);
                                }else{
                                    return 0 - sortColumn.customOrderFunc(a[sortField],b[sortField]);
                                }
                            }
                        );
                    }
                }
            }
            opts.currentPage = opts.requestData.currentPage ? Number(opts.requestData.currentPage):1;
            opts.totalNum = opts.customData.length;
            opts.linesPerPage = opts.requestData.linesPerPage ? Number(opts.requestData.linesPerPage):10;
            opts.totalPage = Math.ceil(Number(opts.totalNum)/Number(opts.linesPerPage));
            if(opts.currentPage>opts.totalPage){
                opts.currentPage = opts.totalPage;
            }
            var start = (opts.currentPage-1) * opts.linesPerPage;
            var end = start + opts.linesPerPage - 1;
            if(end>=opts.totalNum){
                end = opts.totalNum - 1;
            }
            opts.data = [];
            for(;start<=end;start++){
                opts.data.push(opts.customData[start]);
            }
//                opts.startRowNum = (opts.currentPage-1)*opts.linesPerPage;
            if(opts.isFullRow){
                opts.fullRow = opts.linesPerPage;
            }
            if(rebuild){
                $(obj).ulynlistTable(opts);
            }else{
                $.fn.ulynlistTable.setData(obj,opts.data);
            }
            $("#"+opts.pageBarId).ulynlistPagination(opts);
        }else{
            opts.data = opts.customData;
            if(rebuild){
                $(obj).ulynlistTable(opts);
            }else{
                $.fn.ulynlistTable.setData(obj,opts.data);
            }
        }
    }
    function createUlynlistForAjaxData(obj,opts,data,rebuild){
        if(data.status){
            opts.data = data.data.list;

            if(opts.usePageBar){
                opts.currentPage = data.data.currentPage || opts.requestData.currentPage || 1;
                opts.totalNum = data.data.totalNum;
                opts.linesPerPage = data.data.linesPerPage || opts.requestData.linesPerPage || 10;
//                opts.totalPage = Math.ceil(Number(opts.totalNum)/Number(opts.linesPerPage));
//                            opts.startRowNum = (opts.currentPage-1)*opts.linesPerPage;
                $("#"+opts.pageBarId).ulynlistPagination(opts);
            }
            if(opts.isFullRow){
                opts.fullRow = opts.linesPerPage;
            }
            if(rebuild){
                $(obj).ulynlistTable(opts);
            }else{
                $.fn.ulynlistTable.setData(obj,opts.data);
            }
            return true;
        }else{
            opts.errorLoad(data.msg);
            opts.afterLoad();
            return false;
        }
    }
    function triggerSetData(opts,obj){
//        $.event.trigger("setData",[opts.data,{currentPage:opts.currentPage,totalNum:opts.totalNum},obj]);
        var combines = $(obj)[0].combines;
        if(combines){
            var list = opts.data,pageData = {currentPage:opts.currentPage,totalNum:opts.totalNum};
            for (var i = 0;i<combines.length; i++ ){
                $.fn.ulynlist.setData($(combines[i].obj),list,pageData,combines[i].opts);
            }
        }
    }
    /**
     * 联合展示
     * @param arr 对象数组，[{ obj:$("ulynlist-main-table-id"),opts:{ ... } },{ obj:$("ulynlist-table-id"),opts:{ ... } }]
     *  第一个对象为主表，请求数据等操作全部由它控制，其他表只作为联动
     */
    $.fn.ulynlist.combine = function(arr){
        var main = arr.shift();
        var mainObj = main.obj;
        var mainOpts = main.opts;
        if($(mainObj).length>0){
            $(mainObj)[0].combines = arr;
        }
        mainOpts = initUlynlist(mainObj,mainOpts);
        var pageBar = $("#"+mainOpts.pageBarId);
        for(var i= 0,l=arr.length;i<l;i++){
            var item = arr[i];
            var obj = item.obj;
            var opts = item.opts;
            opts.requestData = mainOpts.requestData;
            opts._mainObj = mainObj;
            opts._mainPageBar = pageBar;
//            opts.customAjax = initCustomAjax;
            item.opts = initUlynlist(obj,opts);
        }
        //主表做取数据的
        createUlynlist(mainOpts,mainObj,true);
//        $(mainObj).ulynlist(mainOpts);
    };

    /**
     * 取得当前列表的数据
     * @param ulynlistObj
     * @returns {*}
     */
    $.fn.ulynlist.getData = function(ulynlistObj){
        var opts = $(ulynlistObj).data(data_key_table_opts);
        if(opts){
            var reqData = $.extend({},opts.data);
            return reqData;
        }else{
            return {};
        }
    };
    /**
     * 设值渲染
     * @param ulynlistObj
     * @param data 当pageData不传时，data的结构为：{ list:[],currentPage:1,totalNum:12}
     * @param pageData 页脚的数据{currentPage:1,totalNum:12}
     * @param opts
     * @returns {*|boolean}
     */
    $.fn.ulynlist.setData = function(ulynlistObj,data,pageData,opts){
        opts = opts || $(ulynlistObj).data(data_key_table_opts);
        if(!opts){
            info("设值渲染setData需要先执行初始化调用或者传入opts!!!");
        }else{
            if(!pageData){
                pageData = {
                    currentPage:data.currentPage || opts.requestData.currentPage || 1,
                    totalNum:data.totalNum  || 0
                };
                data = data.list;
            }
            $.fn.ulynlistTable.setData(ulynlistObj,data,opts);

            if(opts.usePageBar){
                opts.currentPage = pageData.currentPage;
                opts.totalNum = pageData.totalNum;
                opts.linesPerPage = opts.linesPerPage || opts.requestData.linesPerPage || 10;
//                opts.totalPage = Math.ceil(Number(opts.totalNum)/Number(opts.linesPerPage));
                $("#"+opts.pageBarId).ulynlistPagination(opts);
            }
        }
    };
    /**
     * 设置请求数据进行执行调用,此操作调用ulynlist生成
     * @param ulynlistObj
     * @param requestData
     */
    $.fn.ulynlist.setRequestData = function(ulynlistObj,requestData){
        var opts = $(ulynlistObj).data(data_key_table_opts);
        if(!opts){
            info("setRequestData需要先执行初始化调用!!!");
        }else{
            if(!requestData.hasOwnProperty("listSql") && opts.requestData.hasOwnProperty("listSql")){
                requestData.listSql = opts.requestData.listSql;
            }
            if(!requestData.hasOwnProperty("linesPerPage") && opts.requestData.hasOwnProperty("linesPerPage")){
                requestData.linesPerPage = opts.requestData.linesPerPage;
            }
            if(!requestData.hasOwnProperty("currentPage") && opts.requestData.hasOwnProperty("currentPage")){
                requestData.currentPage = opts.requestData.currentPage;
            }
            opts.requestData = requestData;
            createUlynlist(opts,ulynlistObj,true);
        }
    };

    $.fn.ulynlist.queryForm = function(ulynlistObj,formObj,opts,tranParamFunc){
        $.fn.ulynlistTable.clearCheckboxCache(ulynlistObj);
        var options = $.extend(true,{}, $.fn.ulynlist.defaults, opts);
        options.requestData = $.extend(options.requestData,
            serializeArrToObject($(formObj)));
        //外部传入对参数的转换处理函数调用
        if(tranParamFunc){
            tranParamFunc(options.requestData);
        }
        $(ulynlistObj).ulynlist(options);
    };
    $.fn.ulynlist.refresh = function(ulynlistObj,removeCache) {
        $(ulynlistObj).trigger("refresh",[removeCache]);
    };

    $.fn.ulynlist.getCurrentParams = function(ulynlistObj){
        var opts = $(ulynlistObj).data(data_key_table_opts);
        if(opts){
            var reqData = $.extend({},opts.requestData);
            return reqData;
        }else{
            return {};
        }
    };
    /**
     * 导出查询的数据
     * @param ulynlistObj
     * @param columns 列定义，object对象会转换为json字符串为post参数
     * @param reqUrl 导出请求的url
     * @param fileName 文件名
     * @param exportType 导出类型，默认值为excel
     */
    $.fn.ulynlist.exportQuery = function(ulynlistObj,columns,reqUrl,fileName,exportType){
        var params = $.fn.ulynlist.getCurrentParams($(ulynlistObj));
        if(!exportType){
            exportType = "excel";
        }
        if(!fileName){
            fileName = "";
        }
        params.ulynExportType = exportType;
        params.ulynExportColumn = obj2str(columns);
        params.ulynExportFileName = fileName;
        if($("#ulynlistDyForm").length>0){
            $("#ulynlistDyForm").remove();
        }
        var dyform = $("<form id='ulynlistDyForm'></form>");
        dyform.attr('action',reqUrl);
        dyform.attr('method','post');
        for(var key in params){
            var dyinput = $("<input type='hidden' name='"+key+"' />");
            dyinput.attr("value",params[key]);
            dyform.append(dyinput);
        }
        dyform.css('display','none');
        dyform.appendTo("body");
        dyform.submit();
    };
    function obj2str(o){
        var r = [];
        if(typeof o =="string"){
            return '"' + o.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function() {
                var a = arguments[0];
                return (a == '\n') ? '\\n': (a == '\r') ? '\\r': (a == '\t') ? '\\t': "";
            }) + '"';
        }
        if(typeof o =="undefined") return "undefined";
        if(typeof o == "object"){
            if(o===null) return "null";
            else if(!o.sort){
                for(var pro in o){
                    r.push("\""+pro+"\":"+obj2str(o[pro]));
                }
                r="{"+r.join()+"}";
            }else{
                for(var i =0;i<o.length;i++){
                    r.push(obj2str(o[i]));
                }
                r="["+r.join()+"]";
            }
            return r;
        }
        return o.toString();
    }
    /**
     * 取得选中的checkbox项，当field有指定时候返回field值数组，否则返回记录item数组
     * @param ulynlistObj
     */
    $.fn.ulynlist.checkbox = function(ulynlistObj,keys){
        return $.fn.ulynlistTable.checkbox.apply(this, arguments);
    };
    /**
     * 在列表中加载额外的html，如加载详细内容。
     * @param obj 要显示detail的div或者tr的上一兄弟节点
     * @param opts
     * <br> text:要显示的html数据，默认为空，当url有值时，加载url的资源赋给text
     * <br> url:要加载的html的url,默认为空，当不为空时加载后赋给text
     * <br> type: 目前支持table，div，当setText为空时，生效。
     * <br> colspan：当type=table时生效，默认值为1
     * <br> style：要加载html的div或者tr的样式
     * <br> toggle:设置是否同时关闭其他已经展开的，默认为false
     * <br> onLoadError：当进行url加载时候失败的事件
     * <br> setText:function(text){}：有此参数则调用这个参数进行渲染
     */
    $.fn.ulynlist.loadDetail = function(obj,opts){
        return $.fn.ulynlistTable.loadDetail.apply(this, arguments);
    };
    function serializeArrToObject(obj) {
        var o = {};
        var a = $(obj).serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [ o[this.name] ];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }

    // 私有函数：debugging
    function info(obj) {
        if (window.console && window.console.log)
            window.console.log(obj);
    }

    /**
     * 全局的列表参数配置
     * @param opts
     */
    $.fn.ulynlist.globalSetup = function(opts){
        //深复制
        $.extend(true,this.defaults,opts);
    };
    $.fn.ulynlist.version = '5.0.0';
    // 插件的defaults
    $.fn.ulynlist.defaults = {
        basePath:'ulynlist',//ulynlist的根文件夹路径
        tablePath:'',  //table插件的位置，一般默认路径不需要填写
        pageBarPath:'',//pagebar插件的位置，一般默认路径不需要填写
        tableTpl:"default",//列表模版名
        pageBarTpl:"default",//分页脚模版名
        url:'ulynlist.do',  //请求列表数据的url，使用此种方式要注意返回数据格式：{"status":true,"msg":"描述","data":{"list":[],"currentPage":1,"totalNum":20,"linesPerPage":10}}
//        tableId: 'ulyn-table-id',//列表的div的id
        pageBarId: 'ulyn-pageBar-id', //页脚的div的id
        usePageBar:true, //是否使用分页
        tableColumn:{},  //列表呈现的行列处理的定义
        requestData:{listSql:"",linesPerPage:10,currentPage:1},   //请求数据的传递的参数，另外还有sortField,sortType和自定义参数
        customData:null, //当CustomData不为空直接使用此参数作为列表展示
        afterTableRender:function(ulynlistObj,opts){},//列表渲染加载完回调方法
        afterPaginationRender: function (ulynlistPageBarObj,opts) {}, //分页条渲染加载完回调方法
        errorLoad:function(errorMsg){
            info("数据加载出错！"+errorMsg);
        },//加载数据失败时候的处理方法
        extra:{},//额外一些数据定义，一般没用，除了自定义模版参数呈现可能会有用
        customAjax:function(requestData,callBackFunc,opts){
            $.ajax({
                type : "post",
                url : opts.url,
                dataType:"json",
                data:requestData,
                success : function(data) {
                    callBackFunc(data);
                },
                error:function(xhr, textStatus, errorThrown){
                    info(xhr);
                    if(textStatus=="parsererror"){
                        var jsonString = xhr.responseText;
                        //解析异常
                        try{
                            callBackFunc(JSON.parse(jsonString));
                        }catch (e){
                            try{
                                var data = (new Function("return " + jsonString))();
                                callBackFunc(data);
                            }catch(e2){
                                opts.afterLoad();
                                opts.errorLoad("解析数据异常！");
                            }
                        }
                    }else{
                        opts.afterLoad();
                        opts.errorLoad(textStatus);
                    }
                }
            });
        }, //自定义的获取数据方法，function(requestData,callBackFuc){}
        isFullRow:false, //当显示数据小于linesPerPage时是否填充满行数
        md5UniqueID:false, //是否使用md5生成数据行的唯一标识
        beforeLoad:function(){},
        afterLoad:function(){}
    };
}));