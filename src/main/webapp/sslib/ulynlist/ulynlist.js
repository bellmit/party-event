/**
 * User: ulyn
 * Date: 13-10-10
 * Time: 下午3:00
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
(function($) {

    $.fn.ulynlist = function(options) {
        // iterate and reformat each matched element
        return this.each(function() {
            // build main options before element iteration
            var opts = $.extend(true,{}, $.fn.ulynlist.defaults, options);
            if(opts.requestData.linesPerPage==null){
                opts.requestData.linesPerPage = 10;
            }
            if(opts.requestData.currentPage==null){
                opts.requestData.currentPage = 1;
            }

            var cur_obj = $(this);
            //设置监听事件
            $(this).unbind("clickSort").bind("clickSort",function(event,sortField,sortType){
                opts.requestData.sortField =  sortField;
                opts.requestData.sortType = sortType;
                var columns = opts.tableColumn.columns;
                var orderColumn = "";
                for(var key in columns){
                    if(columns[key].field==sortField){
                        columns[key].sortType = sortType;
                        if(columns[key].orderColumn!=null){
                            orderColumn = columns[key].orderColumn;
                        }
                    }else{
                        columns[key].sortType = null;
                    }
                }
                if(orderColumn!=""){
                    opts.requestData.sortField =  orderColumn;
                }
                getUlynListData(opts,cur_obj);
            });
            $("#"+opts.pageBarId).unbind("clickPage").bind("clickPage",function(event,page){
                opts.requestData.currentPage = Number(page);
                getUlynListData(opts,cur_obj);
            });

            //设置每页几条记录的事件监听
            $("#"+opts.pageBarId).unbind("setLinesPerPage").bind("setLinesPerPage",function(event,linesPerPage){
                opts.requestData.linesPerPage = Number(linesPerPage);
                getUlynListData(opts,cur_obj);
            });

            //设置刷新事件监听
            $(this).unbind("refresh").bind("refresh",function(){
                $.fn.ulynlistTable.clearCheckboxCache(cur_obj);
                getUlynListData(opts,cur_obj);
            });
            getUlynListData(opts,cur_obj);
        });
    };

    function getUlynListData(opts,obj){
        opts.beforeLoad();
        var temp = opts.afterTableRender;
        opts.afterTableRender = function(){
            temp();
            opts.afterLoad();
        };
        if(opts.customData!=null){
            //使用自定义传入的数据显示
            if(opts.usePageBar){
                //有使用pageBar，则要进行分页
                //有分页条了，排序由我处理，列表只负责展示即可
                opts.sortSelf = false;
                //先确定是否排序
                if(opts.requestData.sortField!=null&&opts.requestData.sortField!=""){
                    var sortField = opts.requestData.sortField;
                    opts.customData = opts.customData.sort(
                        function(a, b){
                            if(opts.requestData.sortType=="asc"){
                                if(a[sortField] > b[sortField]) return 1;
                                if(a[sortField] < b[sortField]) return -1;
                                return 0;
                            }else{
                                if(a[sortField] < b[sortField]) return 1;
                                if(a[sortField] > b[sortField]) return -1;
                                return 0;
                            }
                        }
                    );
                }
                opts.currentPage = opts.requestData.currentPage==null?1:Number(opts.requestData.currentPage);
                opts.totalNum = opts.customData.length;
                opts.linesPerPage = opts.requestData.linesPerPage==null?10:Number(opts.requestData.linesPerPage);
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
                if(opts.tablePath==''){
                    opts.tablePath = opts.basePath + "/table";
                }
//                opts.startRowNum = (opts.currentPage-1)*opts.linesPerPage;
                if(opts.isFullRow){
                    opts.fullRow = opts.linesPerPage;
                }
                $(obj).ulynlistTable(opts);

                if(opts.pageBarPath==''){
                    opts.pageBarPath = opts.basePath + "/pagebar";
                }
                $("#"+opts.pageBarId).ulynlistPagination(opts);
            }else{
                opts.data = opts.customData;
                if(opts.tablePath==''){
                    opts.tablePath = opts.basePath + "/table";
                }
                $(obj).ulynlistTable(opts);
            }
        }else{
            //使用服务端请求数据
            if(opts.customAjax!=null){
                opts.customAjax(opts.requestData,function(data){
                    getUlynListAjaxDataCallback(obj,opts,data);
                });
            }else{
                $.ajax({
                type : "post",
                url : opts.url,
                dataType:"json",
                data:opts.requestData,
                success : function(data) {
                    getUlynListAjaxDataCallback(obj,opts,data);
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){
                    info(XMLHttpRequest);
                    if(textStatus=="parsererror"){
                        //解析异常，尝试eval转换
                        try{
                            eval("var eval_data="+XMLHttpRequest.responseText);
                            getUlynListAjaxDataCallback(obj,opts,eval_data);
                        }catch(e){
                            opts.errorLoad("解析返回数据异常！");
                        }
                    }else{
                        opts.afterLoad();
                        opts.errorLoad(textStatus);
                    }
                }
            });
            }
        }

        //定义取得当前入参的方法
        var cacheReqData = $.extend({},opts.requestData);
        $(obj)[0].getCurrentParams = function(){
            return cacheReqData;
        }

    }
    function getUlynListAjaxDataCallback(obj,opts,data){
        if(data.status){
            opts.data = data.data.list;
            if(opts.tablePath==''){
                opts.tablePath = opts.basePath + "/table";
            }
            if(opts.usePageBar){
                opts.sortSelf = false;
            }

            if(opts.usePageBar){
                opts.currentPage = data.data.currentPage;
                opts.totalNum = data.data.totalNum;
                opts.linesPerPage = data.data.linesPerPage;
                opts.totalPage = Math.ceil(Number(opts.totalNum)/Number(opts.linesPerPage));
//                            opts.startRowNum = (opts.currentPage-1)*opts.linesPerPage;
                if(opts.pageBarPath==''){
                    opts.pageBarPath = opts.basePath + "/pagebar";
                }
                $("#"+opts.pageBarId).ulynlistPagination(opts);
            }
            if(opts.isFullRow){
                opts.fullRow = opts.linesPerPage;
            }
            $(obj).ulynlistTable(opts);

        }else{
            opts.errorLoad(data.msg);
            opts.beforeLoad();
        }
    }
    $.fn.ulynlist.queryForm = function(ulynlistObj,formObj,opts,tranParamFunc){
        $.fn.ulynlistTable.clearCheckboxCache(ulynlistObj);
        var opts = $.extend(true,{}, $.fn.ulynlist.defaults, opts);
        opts.requestData = $.extend(opts.requestData,
            serializeArrToObject($(formObj)));
        //外部传入对参数的转换处理函数调用
        if(tranParamFunc){
            tranParamFunc(opts.requestData);
        }
        $(ulynlistObj).ulynlist(opts);
    }
    $.fn.ulynlist.refresh = function(ulynlistObj) {
        $(ulynlistObj).trigger("refresh");
    };
    /**
     * 取得选中的checkbox项，当field有指定时候返回field值数组，否则返回记录item数组
     * @param ulynlistObj
     */
    $.fn.ulynlist.checkbox = function(ulynlistObj){
        return $.fn.ulynlistTable.checkbox(ulynlistObj);
    }
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
        $.fn.ulynlistTable.loadDetail(obj,opts);
    }
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
    };

    // 插件的defaults
    $.fn.ulynlist.defaults = {
        basePath:'ulynlist',//ulynlist的根文件夹路径
        tablePath:'',  //table插件的位置，一般默认路径不需要填写
        pageBarPath:'',//pagebar插件的位置，一般默认路径不需要填写
        tableTpl:"default",//列表模版名
        pageBarTpl:"default",//分页脚模版名
        url:'ulynlist.do',  //请求列表数据的url，使用此种方式要注意返回数据格式：{"status":true,"msg":"描述","data":{"list":[],"currentPage":1,"totalNum":20,"linesPerPage":10}}
        tableId: 'ulyn-table-id',//列表的div的id
        pageBarId: 'ulyn-pageBar-id', //页脚的div的id
        usePageBar:true, //是否使用分页
        tableColumn:{},  //列表呈现的行列处理的定义
        requestData:{listSql:"",linesPerPage:10,currentPage:1},   //请求数据的传递的参数，另外还有sortField,sortType和自定义参数
        customData:null, //当CustomData不为空直接使用此参数作为列表展示
        afterTableRender:function(){},//列表渲染加载完回调方法
        afterPaginationRender: function () {}, //分页条渲染加载完回调方法
        errorLoad:function(errorMsg){
            info("数据加载出错！"+errorMsg);
        },//加载数据失败时候的处理方法
        extra:{},//额外一些数据定义，一般没用，除了自定义模版参数呈现可能会有用
        customAjax:null, //自定义的获取数据方法，function(requestData,callBackFuc){}
        isFullRow:false, //当显示数据小于linesPerPage时是否填充满行数
        beforeLoad:function(){},
        afterLoad:function(){}
    };

// 闭包结束
})(jQuery);