/**
 * User: ulyn
 * Date: 13-10-10
 * Time: 下午3:00
 * Vsersion: 版本信息不在这边写了，写在ulynlist.js了
 * Version: 2.3 增加对checkbox分页后选中状态的记忆功能。
 * Version: 2.2 正式版发布
 */
;(function (factory) {
    if (typeof define === "function" && define.amd) {
        // AMD模式
        define("ulynlist.table",[ "jquery","artTemplate" ], factory);
    } else {
        // 全局模式
        factory(jQuery,template);
    }
}(function ($,artTemplate) {
    "use strict";
    //如果没有加载artTemplate，尝试使用template
    artTemplate = artTemplate || window.template;
    var data_key_tpl = 'ulynlist_tpl';
    var data_key_checked_cache = 'ulynlist_checked_cache';
    var data_key_table_opts = 'ulynlist_table_opts';
    $.fn.ulynlistTable = function (options) {
        // build main options before element iteration
        var opts = $.extend(true,{}, $.fn.ulynlistTable.defaults, options);
        // iterate and reformat each matched element
        return this.each(function () {
            //标准化参数值，主要针对tableColumn
            standardParam(opts);
            if(!opts._notRemoveData){
                //每次调用的话需要将缓存清理掉
                $(this).removeData();
            }

            var thisObj = this;
            var tableTplId = opts.tableTpl + "-tableTpl";
            var loadTpl = false;
            if ($("#" + tableTplId).length === 0) {
                loadTpl = true;
                var cssUrl = opts.tablePath + "/" + opts.tableTpl + "/style.css";
                var styleTag = document.createElement("link");
                styleTag.setAttribute('type', 'text/css');
                styleTag.setAttribute('rel', 'stylesheet');
                styleTag.setAttribute('href', cssUrl);
                $("head")[0].appendChild(styleTag);
            } else if (!$("#" + tableTplId).text()) {
                //当模版节点已经存在时候，判断是否已经加载完有值，没有则要加载，换一个id
                tableTplId = opts.tableTpl + "-tableTpl" + new Date().getTime() + Math.round(Math.random() * 10000);
                loadTpl = true;
            } else {
                display($("#" + tableTplId).text(), opts, thisObj);
            }
            if (loadTpl) {
                $("body").append("<div id='" + tableTplId + "' style='display: none;'></div>");
                var tableTplUrl = opts.tablePath + "/" + opts.tableTpl + "/table.tpl";
                $.get(tableTplUrl, function (data, status) {
                    if (status == "success") {
                        $("#" + tableTplId).text(data);
                        display(data, opts, thisObj);
                    } else {
                        alert("页面模版加载出错！");
                    }
                },"text");
            }
        });
    };

    /**
     * 设值渲染
     * @param obj
     * @param data
     */
    $.fn.ulynlistTable.setData = function(obj,data,opts){
//        info("设值渲染");
        if(!$.isArray(data)){
            info("$.fn.ulynlistTable.setData设值错误，data不是对象数组！！");
            return;
        }
        opts = opts || obj.data(data_key_table_opts);
        if(opts){
            opts.data = data;
            opts._notRemoveData = true;
            $(obj).ulynlistTable(opts);
        }else{
            info("设值渲染setData需要先执行初始化调用或者传入opts!!!");
        }
    };
    /**
     * 加载完模版渲染显示
     * @param data
     * @param opts
     * @param obj
     */
    function display(tpl, opts, obj) {
        var render = artTemplate.compile(tpl);
        opts.tableColumn.loadFilter(opts.data);
        if(opts.md5UniqueID){
            addMd5ToDataRow(opts.data);
        }
        var cache = {
            currentObj:obj,
            list: opts.data,
            tableColumn: opts.tableColumn,
            startRowNum: opts.startRowNum,
            extra:opts.extra
        };
        if(opts.fullRow){
            var extraRow = opts.fullRow - opts.data.length;
            if(extraRow>0){
                for(var i=0;i<extraRow;i++){
                    cache.list.push(null);
                }
            }
        }
        if(!opts.tableColumn.rememberCheckbox){
            $(obj).data(data_key_checked_cache,{});
        }
        var html = render(cache);
        $(obj).html(html);

        $(".sortable", $(obj)).click(function () {
            var sortField = $(this).attr("field");
            var sortType = "desc";
            if ($(this).hasClass("asc")) {
                $(this).removeClass("asc");
                $(this).addClass("desc");
                sortType = "desc";
            } else if ($(this).hasClass("desc")) {
                $(this).removeClass("desc");
                $(this).addClass("asc");
                sortType = "asc";
            }
            var columns = opts.tableColumn.columns;
            var sortColumn = null;
            if(columns){
                for(var i= 0,l=columns.length;i<l;i++){
                    if(columns[i].field==sortField){
                        columns[i].sortType = sortType;
                        sortColumn = columns[i];
                    }else{
                        columns[i].sortType = null;
                    }
                }
            }
            if(opts.sortSelf){
                if(sortColumn){
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
                display(tpl, opts, obj);
            }else{
                $(obj).trigger("clickSort", [sortField, sortType]);
            }

        });

        //checkbox的事件
        var th_checkbox = opts.checkAllCheckboxId ? $("#"+opts.checkAllCheckboxId) : $(".th_checkbox", $(obj));
        th_checkbox.click(function (e){
            try{
                var check_b =  $(this).prop("checked");
                $("input[class=td_checkbox]",$(obj)).each(function(){
                    if(check_b){
                        if(!$(this).is(":checked")){
                            $(this).prop("checked",check_b);
                            pushCheckedCache($(obj),$(this));
                        }
                    }else{
                        if($(this).is(":checked")){
                            $(this).prop("checked",check_b);
                            removeCheckedCache($(obj),$(this));
                        }
                    }
                });
            }catch(ex){
                if(!$(this).is(":checked")){
                    $("input[class=td_checkbox]",$(obj)).each(function(){
                        $(this).removeAttr("checked");
                        removeCheckedCache($(obj),$(this));
                    });
                }else{
                    $("input[class=td_checkbox]",$(obj)).each(function(){
                        $(this).attr("checked",true);
                        pushCheckedCache($(obj),$(this));
                    });
                }
            }
        });
        $("input[class=td_checkbox]",$(obj)).click(function(){
            if($(this).is(":checked")){
                pushCheckedCache($(obj),$(this));
            }else{
                removeCheckedCache($(obj),$(this));
            }

            setCheckAllBox(obj,th_checkbox);
        });
        setCheckAllBox(obj,th_checkbox);

        //缓存opts
        $(obj).data(data_key_table_opts,opts);

        if (opts.afterTableRender) {
            //渲染后回调的方法
            opts.afterTableRender($(obj),opts);
        }

    }
    //设置全选按钮是否选中状态
    function setCheckAllBox(obj,th_checkbox){
        var td_checkbox = $(".td_checkbox",$(obj));
        var isAllCheck = true;
        if(td_checkbox.length===0){
            isAllCheck = false;
        }else{
            for(var i=0;i<td_checkbox.length;i++){
                var o = td_checkbox[i];
                if(!$(o).is(":checked")){
                    isAllCheck = false;
                    break;
                }
            }
        }
//       var th_checkbox = $(".th_checkbox", $(obj));
        if(isAllCheck){
            if(!th_checkbox.is(":checked")){
                try{
                    th_checkbox.prop("checked",true);
                }catch(e){
                    th_checkbox.attr("checked",true);
                }
            }
        }else if(th_checkbox.is(":checked")){
            try{
                th_checkbox.prop("checked",false);
            }catch(e){
                th_checkbox.removeAttr("checked");
            }
        }
    }
    function pushCheckedCache(ulynlistTable,checkbox){
        var checkedCache = ulynlistTable.data(data_key_checked_cache);
        if(!checkedCache){
            checkedCache = {};
            ulynlistTable.data(data_key_checked_cache,checkedCache);
        }
        var opts = ulynlistTable.data(data_key_table_opts);
        var val = checkbox.val();
        var checkboxId = checkbox.attr("name").replace("ck_","");
        $.each(opts.data,function(index,item){
            if(item){
                if(opts.md5UniqueID){
                    if(item._md5UniqueID == val){
                        checkedCache[item._md5UniqueID] = item;
                        return false;
                    }
                }else if(item[checkboxId] == val){
                    checkedCache[item[checkboxId]] = item;
                    return false;
                }
            }
        });
    }
    function removeCheckedCache(ulynlistTable,checkbox){
        var checkedCache = ulynlistTable.data(data_key_checked_cache);
        if(!checkedCache){
            info("checkbox缓存不存在耶...不科学啊！！");
            return;
        }
        var val = checkbox.val();
        delete checkedCache[val];
    }

    function outputContent(item, column,currentObj,rowIdx) {
        if(!item){
            return "&nbsp;";
        }else if(column.checkbox){
            var checked = '';
            var val = item._md5UniqueID || item[column.checkbox];
            var checkboxCache = currentObj ? $(currentObj).data(data_key_checked_cache):{};
            if(!checkboxCache){
                checkboxCache = {};
                $(currentObj).data(data_key_checked_cache,checkboxCache);
            }
            if(checkboxCache[val]){
                checked = 'checked';
            }else if(column.field){
                //存在定义域，那么判断是否需要选中
                var fieldVal = item[column.field];
                if(fieldVal){
                    checked = 'checked';
                    checkboxCache[val] = item;
                }
            }
            return ' <input type="checkbox" name="ck_'+(column.checkbox||'md5UniqueID')+'" class="td_checkbox" value="'+val+'" '+checked+'>';
        } else if(column.bodyContent || !column.field) {
            return outputExtendBody(item, column,rowIdx);
        }
        var out = "";
        if (column.tableValue) {
            out = tableValueTrans(item, column);
        } else if (column.tableTransFunc && $.isFunction(column.tableTransFunc)) {
            out = customTrans(item, column,rowIdx);
        } else if (column.trans === 'toDisDate') {
            out = toDisDate(item, column);
        } else {
            out = item[column.field];
        }
        if(column.escapeHTML){
            out = artTemplate.helpers.$escape(out);
        }
        return out;
    }
    /**
     * 日期转换
     * @param item
     * @param column
     * @returns {string}
     */
    function toDisDate(item, column) {
        var str = item[column.field];
        return $.fn.ulynlistTable.toDisplayStoreDateStr(str,column.dateType);
    }

    /**
     *用户自定义方法处理
     * @param item
     * @param column
     * @param rowIdx
     * @returns {*}
     */
    function customTrans(item, column,rowIdx) {
        var customFunc = column.tableTransFunc;
        var value = item[column.field];
        return customFunc(value,item,rowIdx);
    }

    /**
     *表码转换处理
     * @param item
     * @param column
     * @returns {*}
     */
    function tableValueTrans(item, column) {
        var value = item[column.field];
        if($.isArray(column.tableValue)){
            for(var i=0;i<column.tableValue.length;i++){
                var o = column.tableValue[i];
                if(o.code===value){
                    return o.value;
                }
            }
        }else{
            var obj = column.tableValue;
            return obj[value];
        }
        return value;
    }
    /**
     * 输出扩展bodyContent内容
     * @param item
     * @param column
     * @param rowIdx
     * @returns {*}
     */
    function outputExtendBody(item, column,rowIdx) {
        var bodyContent = column.bodyContent;
        if(!bodyContent){
            return bodyContent;
        }
        var re = /\[(\$)?\w+\]/g;      // 创建正则表达式模式。
        var r = bodyContent.match(re);
        if(r){
            r.sort();
            r =$.unique(r);
            for (var i = 0; i < r.length; i++) {
                var key = r[i].substring(1, r[i].length - 1);
                if (item.hasOwnProperty(key)) {
                    bodyContent = bodyContent.replace(new RegExp("\\[" + key + "\\]", 'g'), item[key]);
                }else if(key == '$rowIdx'){
                    bodyContent = bodyContent.replace(new RegExp("\\[\\$rowIdx\\]", 'g'), rowIdx);
                }else{
                    bodyContent = bodyContent.replace(new RegExp("\\[" + key + "\\]", 'g'), "");
                }
            }
        }
        return bodyContent;
    }
    //默认的正序排序方式
    var ascSortCompareFn = function(val1,val2){
        if(val1 > val2) return 1;
        if(val1 < val2) return -1;
        return 0;
    };
    /**
     * 标准化处理参数值定义
     * @param opts
     */
    function standardParam(opts) {
        if (!opts.tableColumn) {
            alert('请先定义tableColumn参数！');
        }
        var columns = opts.tableColumn.columns;
        if(columns){
            if($.isArray(columns)){
                for (var i= 0,l=columns.length;i<l;i++) {
                    var o = columns[i];
                    var className = o.className;
                    if(!o.field || (className && className.indexOf("unSortable")!=-1)){

                    }else if(!className){
                        o.className = "sortable";
                    }else if(className.indexOf("sortable")==-1){
                        o.className = "sortable " + o.className;
                    }
                    if(o.checkbox){
                        if(!className){
                            o.className = "unSortable";
                        }else if(className.indexOf("sortable")!=-1) {
                            o.className = className.replace("sortable","unSortable");
                        }
                        o.width = 40;

                        //是否配置checkAllCheckboxId,有配置则使用这个
                        if(o.checkAllCheckboxId){
                            opts.checkAllCheckboxId = o.checkAllCheckboxId;
                        }
                    }

                    if(o.customOrderFunc === 'number'){
                        o.customOrderFunc = function(val1,val2){
                            return ascSortCompareFn(Number(val1),Number(val2));
                        };
                    }else if(typeof o.customOrderFunc != 'function'){
                        o.customOrderFunc = ascSortCompareFn;
                    }
                }

                //show为false的不渲染
                arrayFilter(opts.tableColumn.columns,function(item,idx){
                    return item.show === false;
                });
            }else{
                alert('参数定义错误，需要数组，但opts.tableColumn.columns 是 '+ columns.constructor +'！');
            }
        }else{
            info("请注意，缺少opts.tableColumn.columns的定义,模版是否支持~~");
        }
    }
    function addMd5ToDataRow(list){
        $.each(list,function(index,item){
            item._md5UniqueID = md5RowItem(item);
        });
    }
    function md5RowItem(item){
        if(!item){
            return "";
        }
        var arr = [];
        for(var key in item){
            if("_md5UniqueID" === key){
                continue;
            }
            arr.push(item[key]);
        }
        var str = arr.join("");
        return $.fn.ulynlistTable.md5(str);
    }
    function addItem(arr,item){
        if(!arr){arr=[];}
        if($.inArray(item,arr)==-1){
            arr.push(item);
        }
    }
    function removeItem(arr,item){
        if(!arr){return arr;}
        removeItemByIndex(arr,$.inArray(item,arr));
    }
    function removeItemByIndex(arr,n){
        if(isNaN(n)|| n>arr.length){return false;}
        arr.splice(n,1);
    }
    function arrayFilter(arr,filterFunc){
        if(arr && arr.length>0){
            for(var i=0;;){
            if(filterFunc(arr[i],i)){
                removeItemByIndex(arr,i);
                if(i<arr.length){
                    continue;
                }
            }
            i++;
            if(i<arr.length){
                continue;
            }else{
                break;
            }
        }
        }
    }

    // 私有函数：debugging
    function info(obj) {
        if (window.console && window.console.log)
            window.console.log(obj);
    }

    /**
     * 将日期格式串转换为各种显示的格式，支持数据库存储的时间也支持有:和-组成的时间串，
     * @param dateStr 去除:和-后，最小6位，最大14位的数据库存储格式时间串如:20041212
     * @param formatType 时间格式的类型 HY  DOT  CN
     * @return 格式化的时间串
     */
    $.fn.ulynlistTable.toDisplayStoreDateStr = function(dateStr, formatType){
        if (!dateStr || dateStr.length < 6 || dateStr.length > 14)
        {
            return dateStr;
        }
        else
        {
            var charArr = [];
            switch (formatType)
            {
                case "HY":
                    charArr = ['-', '-', ' ', ':', ':'];
                    break;
                case "DOT":
                    charArr = ['.', '.', ' ', ':', ':'];
                    break;
                case "CN":
                    charArr =['年', '月', '日 ', ':', ':'];
                    break;
                default:charArr =['-', '-', ' ', ':', ':'];
            }
            try
            {
                dateStr = dateStr.replace(/ /g,"").replace(/-/g,"").replace(/:/g,"");
                switch (dateStr.length)
                {
                    case 6:
                        dateStr = dateStr.substr(0,4) + charArr[0] + dateStr.substr(4,2);
                        break;
                    case 8:
                        dateStr = dateStr.substr(0,4) + charArr[0] + dateStr.substr(4,2) + charArr[1] + dateStr.substr(6,2);
                        break;
                    case 10:
                        dateStr = dateStr.substr(0,4) + charArr[0] + dateStr.substr(4,2) + charArr[1] + dateStr.substr(6,2) + charArr[2] + dateStr.substr(8,2);
                        break;
                    case 12:
                        dateStr = dateStr.substr(0,4) + charArr[0] + dateStr.substr(4,2) + charArr[1] +
                            dateStr.substr(6,2) + charArr[2] + dateStr.substr(8,2) + charArr[3] + dateStr.substr(10,2);
                        break;
                    case 14:
                        dateStr = dateStr.substr(0,4) + charArr[0] + dateStr.substr(4,2) + charArr[1] +
                            dateStr.substr(6,2) + charArr[2] + dateStr.substr(8,2) + charArr[3] + dateStr.substr(10,2) +
                            charArr[4] + dateStr.substr(12,2) ;
                        break;
                    default:
                        return dateStr;
                }
                return dateStr;
            }
            catch (ex)
            {
                return dateStr;
            }
        }
    };
    /**
     * 取得选中的checkbox项，当field有指定时候返回field值数组，否则返回记录item数组
     * @param ulynlistObj
     * @param keys：keys为空时，返回唯一键数组（自定义字段或者md5字段值），
     *  当keys为字符串数组时，keys为返回的数组对象的key，
     *  当keys为true时，返回所有字段
     * @return
     */
    $.fn.ulynlistTable.checkbox = function(ulynlistObj,keys){
        var checkedCache = ulynlistObj.data(data_key_checked_cache) || {};
        var result = [];
        if(keys){
            if(keys === true){
                for(var keyPro in checkedCache){
                    if(checkedCache.hasOwnProperty(keyPro)){
                        result.push(checkedCache[keyPro]);
                    }
                }
            }else if($.isArray(keys)){
                for(var keyPro1 in checkedCache){
                    if(checkedCache.hasOwnProperty(keyPro1)){
                        var temp = {},item = checkedCache[keyPro1];
                        for(var i = 0,l=keys.length;i<l;i++){
                            var val = keys[i];
                            temp[val] = item[val];
                        }
                        result.push(temp);
                    }
                }
            }
        }else{
            for(var keyPro2 in checkedCache){
                if(checkedCache.hasOwnProperty(keyPro2)){
                    result.push(keyPro2);
                }
            }
        }
        return result;
    };
    $.fn.ulynlistTable.clearCheckboxCache = function(ulynlistObj){
        ulynlistObj.data(data_key_checked_cache,[]);
    };

    /**
     * 在列表中加载额外的html，如加载详细内容。
     * @param obj 要显示detail的div或者tr的上一兄弟节点
     * @param opts
     * <br> text:要显示的html数据，默认为空，当url有值时，加载url的资源赋给text
     * <br> url:要加载的html的url,默认为空，当不为空时加载后赋给text
     * <br> type: 目前支持table，div，默认为table,当setText为空时，生效。
     * <br> colspan：当type=table时生效，默认值为1
     * <br> style：要加载html的div或者tr的样式
     * <br> toggle:设置是否同时关闭其他已经展开的，默认为false
     * <br> onLoadError：当进行url加载时候失败的事件
     * <br> setText:function(text){}：有此参数则调用这个参数进行渲染
     */
    $.fn.ulynlistTable.loadDetail = function(obj,opts){
        // setText:function(text){}：有此参数则调用这个参数进行渲染
        var ext = $.extend({
            text:"",
            url:"",
            type:"table",
            style:"",
            toggle:false,
            colspan:1,
            onLoadError:function(e){alert("加载失败！");}},opts);

        var nextEl = $(obj).next();
        //取得下一个节点判断是否是详细列表，来判断是否已经加载完成
        //这边处理展开关闭的动作
        if(nextEl.hasClass("detail-list")){
            //已经加载，负责展开和关闭即可
            if(ext.toggle){
                //设置了同时关闭其他已经展开的 ，这边处理。
                nextEl.siblings().each(function(){
                    if($(this).hasClass("hasOpened")){
                        $(this).hide().removeClass("hasOpened");
                    }
                });
                if(nextEl.hasClass("hasOpened")){
                    nextEl.hide().removeClass("hasOpened");
                }else{
                    nextEl.show().addClass("hasOpened");
                }
            }else{
                nextEl.toggle();
            }
            return;
        }else{
            if(ext.toggle){
                nextEl.siblings().each(function(){
                    if($(this).hasClass("hasOpened")){
                        $(this).hide().removeClass("hasOpened");
                    }
                });
            }
        }

        //准备加载
        if(ext.setText){
            //是自定义的，继续往下执行，不管它
        }else if(ext.type=="table"){
            //是table类型
            nextEl = $("<tr class='detail-list hasOpened'>" +
                "<td colspan='"+ext.colspan+"' style='"+ext.style+"'>" +
                "<div style='text-align: center;width: 100%'>加载中...</div></td></tr>");
            $(obj).after(nextEl);
        }else if(ext.type=="div"){
            nextEl = $("<div class='detail-list hasOpened' style='"+ext.style+"'>" +
                "<div style='text-align: center;width: 100%'>加载中...</div></div>");
            $(obj).after(nextEl);
        }else{
            alert("目前只支持type = table或者自定义setText");
            return;
        }
        if(ext.url && !$(obj).hasClass("hasLoaded")){
            //加载url数据
            var flag = false;
            $.ajax({
                type:"get",
                async:false,
                url:ext.url,
                success:function(data){
                    ext.text = data;
                    flag = true;
                    $(obj).addClass("hasLoaded");
                },
                error:ext.onLoadError
            });
            if(!flag){
                return;
            }
        }
        //由用户自定义设置值
        if(ext.setText){
            ext.setText(ext.text);
        }else if(ext.type=="table"){
            nextEl.find("td").html(ext.text);
        }else if(ext.type=="div"){
            nextEl.html(ext.text);
        }
    };

    $.fn.ulynlistTable.version = '5.0.0';
    // 插件的defaults
    $.fn.ulynlistTable.defaults = {
        tablePath: 'table',//table插件的位置，一般默认路径不需要填写
        tableTpl: 'default',//模版文件名
        tableColumn: {
            title: '',  //列表的title属性值，跟模版有关系
            rowStyler:function(item,index){}, //返回样式，如：'background:red'，function有2个参数：index：行索引，从0开始.item：对应于该行记录的对象。
            loadFilter:function(data){
                //渲染之前对数据进行处理
            },//过滤数据，对传入的数据先进行过滤。'data'表示原始数据，因为是对象，直接对data进行修改即可生效。
            rememberCheckbox:false,//上下翻页时候是否记住checkbox的状态
            columns: [
//                {field:'NAME',label:'车行名称', width:180,className:"sortable",style:"text-align:left"},
//                {field:'LOGIN_ID',label:'登录ID', width:180,className:"sortable"},
//                {field:'U_TYPE',label:'用户类型',className:"sortable",tableValue:{"3":"车行用户","2":"畅享运维人员"},
//                      tableTransFunc:function(value,item){
//                          if(value=="3"){
//                              return "车行用户"
//                          }else if(value=="2"){
//                              return "<span style='color: #ffd95d;'>畅享运维人员</span>";
//                          }else{
//                              var x = $.fn.ulynlistTable.toDisplayStoreDateStr(item.CREATE_TIME);
//                              return "unknown("+x+")";;
//                          }
//                      }
//                },
//                {field:'CREATE_TIME',label:'创建时间', width:180,className:"sortable",trans:"toDisDate",dateType:"HY"},
//                {field:'',label:'操作区',width:180, trans:"toDisDate",
//                    bodyContent:'<a href="http://www.sunsharing.com.cn/test?id=[CHID]">测试</a>'
//                }
            ], //数组列定义，具体参数见下：
            // field:字段名，当为空时候，渲染输出bodyContent属性值
            // label：列的title值
            // width: 列宽
            // style:列的样式定义
            // className：样式名，默认sortable,当有unSortable时点击不排序，当然也可以有其他自定义样式，点击排序时sortSelf为true自己排序，false则触发clickSort事件
            // orderColumn:排序列，当点击排序生效时，如果设置此值，则使用这个定义的值而不使用field，配置jar包使用的时候经常遇到：显示的字段是sql转译的，并不是数据库字段，排序则会出错，或者显示的字段在多表关联中有相同名导致排序无法识别！
            // customOrderFunc:此参数在customData的时候生效，当此值为字符串 "number",尝试对此列值转换为数字进行数字排序(对字符串的数字列可能会有用)。当此值为正序排序函数，则是compareFn函数function(val1,val2);为空时默认排序
            // tableValue：对值进行转译，表码值，可以是object也可以是array形式。根据项的值为key，返回value显示
            // tableTransFunc：对值进行转译，优先级比tableValue低，入参value,item,rowIdx，value为值，item为此行记录的值,rowIdx为行索引，需要return要显示的值，返回的值可以带有样式定义：return "<span style='color: #ffd95d;'>畅享运维人员</span>";
            // trans：对值进行转译，优先级比tableTransFuc,tableValue低，目前只有值toDisDate，对数据库存储的字符进行格式化输出，如20130925100610 -> 2013-09-25 10:06:10
            // dateType:控制trans转译的格式，有三个值对应格式："HY"-['-', '-', ' ', ':', ':']，"DOT"-['.', '.', ' ', ':', ':'],CN-['年', '月', ' ', ':', ':']，默认值为HY
            // bodyContent: 当field为空时生效，作为输出的内容，其中行记录值以"[字段名]"代替，另外"[$rowIdx]"为行索引，比如：'<a href="http://www.sunsharing.com.cn/test?id=[CHID]">测试</a>'
            //checkbox:当有值时候，值必须为列表记录的唯一值字段名，对应数据库表主键，列为checkbox,当设置的field取得的值为真时，为选中状态，直接调用方法$.fn.ulynlistTable.checkbox可以取得选中的项数组
            //checkAllCheckboxId:自定义全选的checkbox的id，如果有设置此值，则在表头的全选的checkbox不显示，而使用此自定义的位置的checkbox作为全选
            //escapeHTML:默认为false,当为true时候，对输出的内容进行html转译，使用art的template.helpers.$escape
            //show:默认为true,是否渲染生成此列
            rownumbers: false,  //是否显示行号
            rownumbersTrans:function(i){ return i;},//自定义行号转换，默认返回行号数值
            rownumberslabel: '序号' //行号列的title
        },
        data: [],//要展现的数据
        md5UniqueID:false, //是否使用md5生成数据行的唯一标识
        afterTableRender: function (ulynlistObj,opts) {},//列表渲染加载完回调方法
        sortSelf:true, //是否自己对数据进行排序，false则触发排序事件由外围调用者去处理
        startRowNum:0, //如果有序号，+1为开始的第一个序号值
        extra:{},//额外一些数据定义，一般没用，除了自定义模版参数呈现可能会有用
        fullRow:0 //充满表格几条，默认0为不充满
    };

    /**
     * 增加模版的扩展处理方法 addTemplateHelper
     */
    (function(tpl){
        tpl.helper('$outputLabel', function (column) {
            if(column.checkbox){
                if(column.checkAllCheckboxId){
                    return column.label;
                }
                return '<input type="checkbox" class="th_checkbox" name="ck_'+column.checkbox+'">';
            }else{
                return column.label;
            }
        });
        tpl.helper('$outputContent', function (item, column,currentObj,rowIdx) {
            return outputContent(item, column,currentObj,rowIdx);
        });
        //输出内容，用于title属性，主要是过滤去除html标签以及替换双引号为 &quot;
        tpl.helper('$outputContentForTitle', function (item, column,currentObj,rowIdx) {
            if(!item){
                return null;
            }
            if (!column.field || column.checkbox) {
                return "";
            }
            var str = outputContent(item, column,currentObj,rowIdx);
            if(!str){
                return "";
            }else if( typeof(str) != "string"){
                return str+"";
            }
            var re = /<[^>]*?>(.*?)/g;
            return str.replace(re,"").replace(/\"/g,"&quot;");
        });
        tpl.helper('$doFunc', function (fn) {
            if($.isFunction(fn)){
                var args = [];
                if(arguments.length>1){
                    for(var i=1;i<arguments.length;i++){
                        args.push(arguments[i]);
                    }
                }
                return fn.apply(this, args);
            }else return "";
        });
        tpl.helper('$console', function (obj) {
            info(obj);
        });
    })(artTemplate);

    //md5 引入jquery.md5插件
    (function(ulynlistTable){

        var rotateLeft = function(lValue, iShiftBits) {
            return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits));
        };

        var addUnsigned = function(lX, lY) {
            var lX4, lY4, lX8, lY8, lResult;
            lX8 = (lX & 0x80000000);
            lY8 = (lY & 0x80000000);
            lX4 = (lX & 0x40000000);
            lY4 = (lY & 0x40000000);
            lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF);
            if (lX4 & lY4) return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
            if (lX4 | lY4) {
                if (lResult & 0x40000000) return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
                else return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
            } else {
                return (lResult ^ lX8 ^ lY8);
            }
        };

        var F = function(x, y, z) {
            return (x & y) | ((~ x) & z);
        };

        var G = function(x, y, z) {
            return (x & z) | (y & (~ z));
        };

        var H = function(x, y, z) {
            return (x ^ y ^ z);
        };

        var I = function(x, y, z) {
            return (y ^ (x | (~ z)));
        };

        var FF = function(a, b, c, d, x, s, ac) {
            a = addUnsigned(a, addUnsigned(addUnsigned(F(b, c, d), x), ac));
            return addUnsigned(rotateLeft(a, s), b);
        };

        var GG = function(a, b, c, d, x, s, ac) {
            a = addUnsigned(a, addUnsigned(addUnsigned(G(b, c, d), x), ac));
            return addUnsigned(rotateLeft(a, s), b);
        };

        var HH = function(a, b, c, d, x, s, ac) {
            a = addUnsigned(a, addUnsigned(addUnsigned(H(b, c, d), x), ac));
            return addUnsigned(rotateLeft(a, s), b);
        };

        var II = function(a, b, c, d, x, s, ac) {
            a = addUnsigned(a, addUnsigned(addUnsigned(I(b, c, d), x), ac));
            return addUnsigned(rotateLeft(a, s), b);
        };

        var convertToWordArray = function(string) {
            var lWordCount;
            var lMessageLength = string.length;
            var lNumberOfWordsTempOne = lMessageLength + 8;
            var lNumberOfWordsTempTwo = (lNumberOfWordsTempOne - (lNumberOfWordsTempOne % 64)) / 64;
            var lNumberOfWords = (lNumberOfWordsTempTwo + 1) * 16;
            var lWordArray = Array(lNumberOfWords - 1);
            var lBytePosition = 0;
            var lByteCount = 0;
            while (lByteCount < lMessageLength) {
                lWordCount = (lByteCount - (lByteCount % 4)) / 4;
                lBytePosition = (lByteCount % 4) * 8;
                lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount) << lBytePosition));
                lByteCount++;
            }
            lWordCount = (lByteCount - (lByteCount % 4)) / 4;
            lBytePosition = (lByteCount % 4) * 8;
            lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition);
            lWordArray[lNumberOfWords - 2] = lMessageLength << 3;
            lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29;
            return lWordArray;
        };

        var wordToHex = function(lValue) {
            var WordToHexValue = "", WordToHexValueTemp = "", lByte, lCount;
            for (lCount = 0; lCount <= 3; lCount++) {
                lByte = (lValue >>> (lCount * 8)) & 255;
                WordToHexValueTemp = "0" + lByte.toString(16);
                WordToHexValue = WordToHexValue + WordToHexValueTemp.substr(WordToHexValueTemp.length - 2, 2);
            }
            return WordToHexValue;
        };

        var uTF8Encode = function(string) {
            string = string.replace(/\x0d\x0a/g, "\x0a");
            var output = "";
            for (var n = 0; n < string.length; n++) {
                var c = string.charCodeAt(n);
                if (c < 128) {
                    output += String.fromCharCode(c);
                } else if ((c > 127) && (c < 2048)) {
                    output += String.fromCharCode((c >> 6) | 192);
                    output += String.fromCharCode((c & 63) | 128);
                } else {
                    output += String.fromCharCode((c >> 12) | 224);
                    output += String.fromCharCode(((c >> 6) & 63) | 128);
                    output += String.fromCharCode((c & 63) | 128);
                }
            }
            return output;
        };

        var md5 = function(string) {
            var x = Array();
            var k, AA, BB, CC, DD, a, b, c, d;
            var S11=7, S12=12, S13=17, S14=22;
            var S21=5, S22=9 , S23=14, S24=20;
            var S31=4, S32=11, S33=16, S34=23;
            var S41=6, S42=10, S43=15, S44=21;
            string = uTF8Encode(string);
            x = convertToWordArray(string);
            a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;
            for (k = 0; k < x.length; k += 16) {
                AA = a; BB = b; CC = c; DD = d;
                a = FF(a, b, c, d, x[k+0],  S11, 0xD76AA478);
                d = FF(d, a, b, c, x[k+1],  S12, 0xE8C7B756);
                c = FF(c, d, a, b, x[k+2],  S13, 0x242070DB);
                b = FF(b, c, d, a, x[k+3],  S14, 0xC1BDCEEE);
                a = FF(a, b, c, d, x[k+4],  S11, 0xF57C0FAF);
                d = FF(d, a, b, c, x[k+5],  S12, 0x4787C62A);
                c = FF(c, d, a, b, x[k+6],  S13, 0xA8304613);
                b = FF(b, c, d, a, x[k+7],  S14, 0xFD469501);
                a = FF(a, b, c, d, x[k+8],  S11, 0x698098D8);
                d = FF(d, a, b, c, x[k+9],  S12, 0x8B44F7AF);
                c = FF(c, d, a, b, x[k+10], S13, 0xFFFF5BB1);
                b = FF(b, c, d, a, x[k+11], S14, 0x895CD7BE);
                a = FF(a, b, c, d, x[k+12], S11, 0x6B901122);
                d = FF(d, a, b, c, x[k+13], S12, 0xFD987193);
                c = FF(c, d, a, b, x[k+14], S13, 0xA679438E);
                b = FF(b, c, d, a, x[k+15], S14, 0x49B40821);
                a = GG(a, b, c, d, x[k+1],  S21, 0xF61E2562);
                d = GG(d, a, b, c, x[k+6],  S22, 0xC040B340);
                c = GG(c, d, a, b, x[k+11], S23, 0x265E5A51);
                b = GG(b, c, d, a, x[k+0],  S24, 0xE9B6C7AA);
                a = GG(a, b, c, d, x[k+5],  S21, 0xD62F105D);
                d = GG(d, a, b, c, x[k+10], S22, 0x2441453);
                c = GG(c, d, a, b, x[k+15], S23, 0xD8A1E681);
                b = GG(b, c, d, a, x[k+4],  S24, 0xE7D3FBC8);
                a = GG(a, b, c, d, x[k+9],  S21, 0x21E1CDE6);
                d = GG(d, a, b, c, x[k+14], S22, 0xC33707D6);
                c = GG(c, d, a, b, x[k+3],  S23, 0xF4D50D87);
                b = GG(b, c, d, a, x[k+8],  S24, 0x455A14ED);
                a = GG(a, b, c, d, x[k+13], S21, 0xA9E3E905);
                d = GG(d, a, b, c, x[k+2],  S22, 0xFCEFA3F8);
                c = GG(c, d, a, b, x[k+7],  S23, 0x676F02D9);
                b = GG(b, c, d, a, x[k+12], S24, 0x8D2A4C8A);
                a = HH(a, b, c, d, x[k+5],  S31, 0xFFFA3942);
                d = HH(d, a, b, c, x[k+8],  S32, 0x8771F681);
                c = HH(c, d, a, b, x[k+11], S33, 0x6D9D6122);
                b = HH(b, c, d, a, x[k+14], S34, 0xFDE5380C);
                a = HH(a, b, c, d, x[k+1],  S31, 0xA4BEEA44);
                d = HH(d, a, b, c, x[k+4],  S32, 0x4BDECFA9);
                c = HH(c, d, a, b, x[k+7],  S33, 0xF6BB4B60);
                b = HH(b, c, d, a, x[k+10], S34, 0xBEBFBC70);
                a = HH(a, b, c, d, x[k+13], S31, 0x289B7EC6);
                d = HH(d, a, b, c, x[k+0],  S32, 0xEAA127FA);
                c = HH(c, d, a, b, x[k+3],  S33, 0xD4EF3085);
                b = HH(b, c, d, a, x[k+6],  S34, 0x4881D05);
                a = HH(a, b, c, d, x[k+9],  S31, 0xD9D4D039);
                d = HH(d, a, b, c, x[k+12], S32, 0xE6DB99E5);
                c = HH(c, d, a, b, x[k+15], S33, 0x1FA27CF8);
                b = HH(b, c, d, a, x[k+2],  S34, 0xC4AC5665);
                a = II(a, b, c, d, x[k+0],  S41, 0xF4292244);
                d = II(d, a, b, c, x[k+7],  S42, 0x432AFF97);
                c = II(c, d, a, b, x[k+14], S43, 0xAB9423A7);
                b = II(b, c, d, a, x[k+5],  S44, 0xFC93A039);
                a = II(a, b, c, d, x[k+12], S41, 0x655B59C3);
                d = II(d, a, b, c, x[k+3],  S42, 0x8F0CCC92);
                c = II(c, d, a, b, x[k+10], S43, 0xFFEFF47D);
                b = II(b, c, d, a, x[k+1],  S44, 0x85845DD1);
                a = II(a, b, c, d, x[k+8],  S41, 0x6FA87E4F);
                d = II(d, a, b, c, x[k+15], S42, 0xFE2CE6E0);
                c = II(c, d, a, b, x[k+6],  S43, 0xA3014314);
                b = II(b, c, d, a, x[k+13], S44, 0x4E0811A1);
                a = II(a, b, c, d, x[k+4],  S41, 0xF7537E82);
                d = II(d, a, b, c, x[k+11], S42, 0xBD3AF235);
                c = II(c, d, a, b, x[k+2],  S43, 0x2AD7D2BB);
                b = II(b, c, d, a, x[k+9],  S44, 0xEB86D391);
                a = addUnsigned(a, AA);
                b = addUnsigned(b, BB);
                c = addUnsigned(c, CC);
                d = addUnsigned(d, DD);
            }
            var tempValue = wordToHex(a) + wordToHex(b) + wordToHex(c) + wordToHex(d);
            return tempValue.toLowerCase();
        };

        ulynlistTable.md5 = md5;
    })($.fn.ulynlistTable);
}));