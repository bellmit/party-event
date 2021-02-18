/**
 * User: ulyn
 * Date: 13-10-10
 * Time: 下午3:00
 * Version: 2.3 增加对checkbox分页后选中状态的记忆功能。
 * Version: 2.2 正式版发布
 */
(function ($) {

    $.fn.ulynlistTable = function (options) {
        // build main options before element iteration
        var opts = $.extend(true,{}, $.fn.ulynlistTable.defaults, options);
        // iterate and reformat each matched element
        return this.each(function () {
            //标准化参数值，主要针对tableColumn
            standardParam(opts);

            var thisObj = this;
            var tableTplId = opts.tableTpl + "-tableTpl";
            var loadTpl = false;
            if ($("#" + tableTplId).length == 0) {
                loadTpl = true;
                var cssUrl = opts.tablePath + "/" + opts.tableTpl + "/style.css";
                var styleTag = document.createElement("link");
                styleTag.setAttribute('type', 'text/css');
                styleTag.setAttribute('rel', 'stylesheet');
                styleTag.setAttribute('href', cssUrl);
                $("head")[0].appendChild(styleTag);
            } else if ($("#" + tableTplId).text() == "") {
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
     * 加载完模版渲染显示
     * @param data
     * @param opts
     * @param obj
     */
    function display(data, opts, obj) {
        //增加模版的扩展处理
        addTemplateHelper();
        var render = template.compile(data);
        opts.tableColumn.loadFilter(opts.data);
        var cache = {
            currentObj:obj,
            list: opts.data,
            tableColumn: opts.tableColumn,
            startRowNum: opts.startRowNum,
            extra:opts.extra
        }
        var extraRow = opts.fullRow - opts.data.length;
        if(extraRow>0){
            for(var i=0;i<extraRow;i++){
                cache.list.push(null);
            }
        }
        obj.cache = cache;
        if(!opts.tableColumn.rememberCheckbox){
            $(obj)[0].checkboxCheckedArr = [];
            $(obj)[0].checkboxRemovedArr = [];
        }
        if($(obj)[0].checkboxCheckedArr==null){
            $(obj)[0].checkboxCheckedArr = [];
        }
        if($(obj)[0].checkboxRemovedArr==null){
            $(obj)[0].checkboxRemovedArr = [];
        }
        var html = render(cache);
        $(obj).html(html);

        $(".sortable", $(obj)).click(function () {
            var sortField = $(this).attr("field");
            var sortType = "desc";
            if ($(this).hasClass("asc")) {
                $(this).removeClass("asc")
                $(this).addClass("desc")
                sortType = "desc";
            } else if ($(this).hasClass("desc")) {
                $(this).removeClass("desc")
                $(this).addClass("asc")
                sortType = "asc";
            }
            if(opts.sortSelf){
                var columns = opts.tableColumn.columns;
                for(var key in columns){
                    if(columns[key].field==sortField){
                        columns[key].sortType = sortType;
                    }else{
                        columns[key].sortType = null;
                    }
                }
                opts.data = opts.data.sort(
                    function(a, b){
                        if(sortType=="asc"){
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
                display(data, opts, obj);
            }else{
                $(obj).trigger("clickSort", [sortField, sortType]);
            }

        });

        //checkbox的事件
        var th_checkbox = opts.checkAllCheckboxId ? $("#"+opts.checkAllCheckboxId) : $(".th_checkbox", $(obj));
        th_checkbox.click(function (e){
            try{
                var check_b =  $(this).prop("checked")
                $("input[class=td_checkbox]",$(obj)).each(function(){
                    if(check_b){
                        if(!$(this).is(":checked")){
                            $(this).prop("checked",check_b);
                            addItem($(obj)[0].checkboxCheckedArr,$(this).val());
                            removeItem($(obj)[0].checkboxRemovedArr,$(this).val());
                        }
                    }else{
                        if($(this).is(":checked")){
                            $(this).prop("checked",check_b);
                            removeItem($(obj)[0].checkboxCheckedArr,$(this).val());
                            addItem($(obj)[0].checkboxRemovedArr,$(this).val());
                        }
                    }
                });
            }catch(e){
                if($(this).attr("checked")==false){
                    $("input[class=td_checkbox]",$(obj)).each(function(){
                        $(this).removeAttr("checked");
                        removeItem($(obj)[0].checkboxCheckedArr,$(this).val());
                        addItem($(obj)[0].checkboxRemovedArr,$(this).val());
                    });
                }else{
                    $("input[class=td_checkbox]",$(obj)).each(function(){
                        $(this).attr("checked",true);
                        addItem($(obj)[0].checkboxCheckedArr,$(this).val());
                        removeItem($(obj)[0].checkboxRemovedArr,$(this).val());
                    });
                }
            }
        });
        $("input[class=td_checkbox]",$(obj)).click(function(){
            if($(this).is(":checked")){
                addItem($(obj)[0].checkboxCheckedArr,$(this).val());
                removeItem($(obj)[0].checkboxRemovedArr,$(this).val());
            }else{
                removeItem($(obj)[0].checkboxCheckedArr,$(this).val());
                addItem($(obj)[0].checkboxRemovedArr,$(this).val());
            }

            setCheckAllBox(obj,th_checkbox);
        });
        setCheckAllBox(obj,th_checkbox);

        if (opts.afterTableRender) {
            //渲染后回调的方法
            opts.afterTableRender();
        }
    }
   function setCheckAllBox(obj,th_checkbox){
       var td_checkbox = $(".td_checkbox",$(obj));
       var isAllCheck = true;
       if(td_checkbox.length==0){
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
    /**
     * 增加模版的扩展处理方法
     */
    function addTemplateHelper() {
        template.helper('$outputLabel', function (column) {
            if(column.checkbox){
                if(column.checkAllCheckboxId != null && column.checkAllCheckboxId != ""){
                    return column.label;
                }
                return '<input type="checkbox" class="th_checkbox" name="ck_'+column.checkbox+'">';
            }else{
                return column.label;
            }
        });
        template.helper('$outputContent', function (item, column,currentObj) {
            return outputContent(item, column,currentObj);
        });
        //输出内容，用于title属性，主要是过滤去除html标签以及替换双引号为 &quot;
        template.helper('$outputContentForTitle', function (item, column) {
            if(item==null){
                return null;
            }
            if (column.field == '' || column.field == null||column.checkbox) {
                return "";
            }
            var str = outputContent(item, column);
            if(str==null){
                return "";
            }else if( typeof(str) != "string"){
                return str+"";
            }
            var re = /<[^>]*?>(.*?)/g
            return str.replace(re,"").replace(/\"/g,"&quot;");
        });
        template.helper('$doFunc', function (func,args1,args2,args3) {
            if(func!= null){
                return func(args1,args2,args3);
            }else return "";
        });
        template.helper('$console', function (obj) {
            info(obj)
        });
    }
    function outputContent(item, column,currentObj) {
        if(item==null){
            return "&nbsp;";
        }else if(column.checkbox){
            var checked = '';
            var val = item[column.checkbox];
            var checkboxCheckedArr = currentObj==null?[]:currentObj.checkboxCheckedArr;
            var checkboxRemovedArr = currentObj==null?[]:currentObj.checkboxRemovedArr;
            if(checkboxRemovedArr!=null&& $.inArray(val,checkboxRemovedArr)!=-1){
                checked = '';
            }else if(checkboxCheckedArr!=null&& $.inArray(val,checkboxCheckedArr)!=-1){
                checked = 'checked';
            }else if(column.field){
                //存在定义域，那么判断是否需要选中
                var fieldVal = item[column.field];
                if(fieldVal==true||fieldVal=="true"){
                    checked = 'checked';
                    addItem(checkboxCheckedArr,val);
                    removeItem(checkboxRemovedArr,val);
                }
            }
            return ' <input type="checkbox" name="ck_'+column.checkbox+'" class="td_checkbox" value="'+item[column.checkbox]+'" '+checked+'>';
        } else if(column.field == '' || column.field == null) {
            return outputExtendBody(item, column);
        }
        var out = "";
        if (column.tableValue != null) {
            out = tableValueTrans(item, column);
        } else if (column.tableTransFunc != null) {
            out = customTrans(item, column);
        } else if (column.trans == 'toDisDate') {
            out = toDisDate(item, column);
        } else {
            out = item[column.field];
        }
        if(column.escapeHTML == true){
            out = template.helpers.$escape(out);
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
     * @returns {*}
     */
    function customTrans(item, column) {
        var customFunc = column.tableTransFunc;
        var value = item[column.field];
        return customFunc(value,item);
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
                if(o["code"]==value){
                    return o["value"];
                }
            }
        }else{
            var o = column.tableValue;
            return o[value];
        }
        return value;
    }
    /**
     * 输出扩展bodyContent内容
     * @param item
     * @param column
     * @returns {*}
     */
    function outputExtendBody(item, column) {
        var bodyContent = column.bodyContent;
        if(bodyContent==null){
            return bodyContent;
        }
        var re = /\[\w+\]/g;      // 创建正则表达式模式。
        var r = bodyContent.match(re);
        if(r!=null){
            r.sort();
            r =$.unique(r);
            for (var i = 0; i < r.length; i++) {
                var key = r[i].substring(1, r[i].length - 1);
                var replaceStr = "";
                if (item[key] != null) {
                    replaceStr = item[key];
                }
                bodyContent = bodyContent.replace(new RegExp("\\[" + key + "\\]", 'g'), replaceStr);
            }
        }
        return bodyContent;
    }

    /**
     * 标准化处理参数值定义
     * @param opts
     */
    function standardParam(opts) {
        if (opts.tableColumn == null) {
            alert('请先定义tableColumn参数！');
        }
        var columns = opts.tableColumn.columns;
        for (var key in columns) {
            var o = columns[key];
//            if(o.field==""&&o.hasOwnProperty("trans")){
//                delete o["trans"];  //删除对象属性
//            }
//            o["style"] = "text-align:left;" + (o["style"] == null ? "" : o["style"]);
            var className = o["className"];
            if(o.field == '' || o.field == null||(className!=null&&className.indexOf("unSortable")!=-1)){

            }else if(className==null){
                o["className"] = "sortable"
            }else if(className.indexOf("sortable")==-1){
                o["className"] = "sortable " +o["className"];
            }
            if(o.checkbox){
                if(className==null){
                    o["className"] = "unSortable"
                }else if(className.indexOf("sortable")!=-1) {
                    o["className"] = className.replace("sortable","unSortable");
                }
                o.width = 40;

                //是否配置checkAllCheckboxId,有配置则使用这个
                if(o["checkAllCheckboxId"]!=null && o["checkAllCheckboxId"]!=""){
                    opts.checkAllCheckboxId = o["checkAllCheckboxId"];
                }
            }
        }
    }

    /**
     * 根据key移除data的项,注意data中对于key都是唯一的，所以只删除一项
     * @param data
     * @param objPropery
     * @param objValue
     */
    function deleteItemByKey(data,objPropery,objValue){
        return $.grep(data, function(cur,i){
            return cur[objPropery]!=objValue;
        });
    }
    function addItem(arr,item){
        if(arr==null){arr=[];}
        if($.inArray(item,arr)==-1){
            arr.push(item);
        }
    }
    function removeItem(arr,item){
        if(arr==null){return arr;}
        arr.splice($.inArray(item,arr),1);
    }

    // 私有函数：debugging
    function info(obj) {
        if (window.console && window.console.log)
            window.console.log(obj);
    };

    /**
     * 将日期格式串转换为各种显示的格式，支持数据库存储的时间也支持有:和-组成的时间串，
     * @param dateStr 去除:和-后，最小6位，最大14位的数据库存储格式时间串如:20041212
     * @param formatType 时间格式的类型 HY  DOT  CN
     * @return 格式化的时间串
     */
    $.fn.ulynlistTable.toDisplayStoreDateStr = function(dateStr, formatType){
        if (dateStr == null || dateStr.length < 6 || dateStr.length > 14)
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
    }
    /**
     * 取得选中的checkbox项，当field有指定时候返回field值数组，否则返回记录item数组
     * @param ulynlistObj
     * @return checkbox对象，包含全选功能函数和取得选中的项数组
     */
    $.fn.ulynlistTable.checkbox = function(ulynlistObj){
        var checkboxArr = ulynlistObj[0].checkboxCheckedArr;
        return checkboxArr==null?[]:checkboxArr;
    }
    $.fn.ulynlistTable.clearCheckboxCache = function(ulynlistObj){
        ulynlistObj[0].checkboxCheckedArr = [];
        ulynlistObj[0].checkboxRemovedArr = [];
    }

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
        if(ext.url!=""&&!$(obj).hasClass("hasLoaded")){
            //加载url数据
            var flag = false;
            $.ajax({
                type:"get",
                async:false,
                url:ext.url,
                success:function(data){
                    ext.text = data;
                    flag = true;
                    $(obj).addClass("hasLoaded")
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
    }


    // 插件的defaults
    $.fn.ulynlistTable.defaults = {
        tablePath: 'table',//table插件的位置，一般默认路径不需要填写
        tableTpl: 'default',//模版文件名
        tableColumn: {
            title: 'ulynlist',  //列表的title属性值，跟模版有关系
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
            // tableValue：对值进行转译，表码值，可以是object也可以是array形式。根据项的值为key，返回value显示
            // tableTransFunc：对值进行转译，优先级比tableValue低，入参value,item，value为值，item为此行记录的值，需要return要显示的值，返回的值可以带有样式定义：return "<span style='color: #ffd95d;'>畅享运维人员</span>";
            // trans：对值进行转译，优先级比tableTransFuc,tableValue低，目前只有值toDisDate，对数据库存储的字符进行格式化输出，如20130925100610 -> 2013-09-25 10:06:10
            // dateType:控制trans转译的格式，有三个值对应格式："HY"-['-', '-', ' ', ':', ':']，"DOT"-['.', '.', ' ', ':', ':'],CN-['年', '月', ' ', ':', ':']，默认值为HY
            // bodyContent: 当field为空时生效，作为输出的内容，其中行记录值以"[字段名]"代替，比如：'<a href="http://www.sunsharing.com.cn/test?id=[CHID]">测试</a>'
            //checkbox:当有值时候，值必须为列表记录的唯一值字段名，对应数据库表主键，列为checkbox,当设置的field取得的值=true时，为选中状态，直接调用方法$.fn.ulynlistTable.checkbox可以取得选中的项数组
            //checkAllCheckboxId:自定义全选的checkbox的id，如果有设置此值，则在表头的全选的checkbox不显示，而使用此自定义的位置的checkbox作为全选
            //escapeHTML:默认为false,当为true时候，对输出的内容进行html转译，使用art的template.helpers.$escape
            rownumbers: false,  //是否显示行号
            rownumberslabel: '序号' //行号列的title
        },
        data: [],//要展现的数据
        afterTableRender: function () {},//列表渲染加载完回调方法
        sortSelf:true, //是否自己对数据进行排序，false则触发排序事件由外围调用者去处理
        startRowNum:0, //如果有序号，+1为开始的第一个序号值
        extra:{},//额外一些数据定义，一般没用，除了自定义模版参数呈现可能会有用
        fullRow:0 //充满表格几条，默认0为不充满
    };

// 闭包结束
})(jQuery);