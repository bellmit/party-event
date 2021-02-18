/**
 * User: ulyn
 * Date: 13-10-10
 * Time: 下午3:00
 * Version: 2.2 正式版发布
 */
(function($) {

    $.fn.ulynlistPagination = function(options) {
        // build main options before element iteration
        var opts = $.extend({}, $.fn.ulynlistPagination.defaults, options);
        // iterate and reformat each matched element
        return this.each(function() {
            //标准化参数值
            standardParam(opts);

            var thisObj = this;
            var pageBarTplId = opts.pageBarTpl + "-pageBarTpl";
            var loadTpl = false;
            if ($("#" + pageBarTplId).length == 0) {
                loadTpl = true;
                var cssUrl = opts.pageBarPath + "/" + opts.pageBarTpl + "/style.css";
                var styleTag = document.createElement("link");
                styleTag.setAttribute('type', 'text/css');
                styleTag.setAttribute('rel', 'stylesheet');
                styleTag.setAttribute('href', cssUrl);
                $("head")[0].appendChild(styleTag);
            } else if ($("#" + pageBarTplId).text() == "") {
                //当模版节点已经存在时候，判断是否已经加载完有值，没有则要加载，换一个id
                pageBarTplId = opts.pageBarTpl + "-pageBarTpl" + new Date().getTime() + Math.round(Math.random() * 10000);
                loadTpl = true;
            } else {
                display($("#" + pageBarTplId).text(), opts, thisObj);
            }
            if (loadTpl) {
                $("body").append("<div id='" + pageBarTplId + "' style='display: none;'></div>");
                var pageBarTplUrl = opts.pageBarPath + "/" +opts.pageBarTpl + "/pagebar.tpl";
                $.get(pageBarTplUrl, function (data, status) {
                    if (status == "success") {
                        $("#" + pageBarTplId).text(data);
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
        var html = render({
            opts: opts
        });
        $(obj).html(html);

        //初始化点击事件
        $(".pagebottonlist a", $(obj)).click(function () {
            var page = $(this).attr("page");
            if(page<=0){
                page = 1;
            }else if(page>opts.totalPage){
                page = opts.totalPage;
            }
            $(obj).trigger("clickPage", [page]);
        });
        $(".go_button", $(obj)).click(function () {
            var gotopage = $(".goto", $(obj)).val();
            if(gotopage==""||isNaN(gotopage)){
                return;
            }
            $(obj).trigger("clickPage", [gotopage]);
        });
        $(".goto", $(obj)).keyup(function(){  //keyup事件处理
            $(this).val($(this).val().replace(/\D|^0/g,''));
            if($(this).val()>opts.totalPage){
                $(this).val(opts.totalPage);
            }
        }).bind("paste",function(){  //CTR+V事件处理
            $(this).val($(this).val().replace(/\D|^0/g,''));
                if($(this).val()>opts.totalPage){
                    $(this).val(opts.totalPage);
                }
        }).css("ime-mode", "disabled");  //CSS设置输入法不可用

        //设置页面框事件
        $(".setGoToPage", $(obj)).val(opts.currentPage+"/"+opts.totalPage).click(function(){
            var val = $(this).val();
            $(this).select();
        }).blur(function(){
            $(this).val(opts.currentPage+"/"+opts.totalPage);
        }).keydown(function(e){
                e = e||event;
                if(e.keyCode==13){
                    var val = $(this).val();
                    var r = /^[0-9]*[1-9][0-9]*$/; //正整数
                    if(r.test(val)){
                        if(Number(val)>opts.totalPage){
                            $(this).val(opts.totalPage);
                        }
                        $(obj).trigger("clickPage", [val]);
                    }
                }
            });
        //设置每页几条的按钮点击事情
        $(".setLinesPerPage", $(obj)).keydown(function(e){
            e = e||event;
            if(e.keyCode==13){
                var val = $(this).val();
                var r = /^[0-9]*[1-9][0-9]*$/; //正整数
                if(r.test(val)){
                    if(Number(val)>10000){
                        //限制10000条最多
                        val = 10000;
                    }
                    $(obj).trigger("setLinesPerPage", [val]);
                }
            }
        });

        if (opts.afterPaginationRender) {
            //渲染后回调的方法
            opts.afterPaginationRender();
        }
    }

    /**
     * 增加模版的扩展处理方法
     */
    function addTemplateHelper() {
        template.helper('$doFunc', function (func,obj) {
            if(func!= null){
               func(obj);
            }else return "";
        });
        template.helper('$console', function (obj) {
            info(obj)
        });
        //根据要显示的页面按钮的数组
        template.helper('$outPageBtn', function (opts) {
            var sb = "";
            var start = 1;
            var end = opts.totalPage;
            if (opts.totalPage > opts.pageSpanNum){
                var middle = Math.ceil(opts.pageSpanNum / 2) - 1;
                var below = (opts.currentPage - middle);
                var above = (Number(opts.currentPage) + middle);

                if (below < 4){
                    above = opts.pageSpanNum;
                    below = 1;
                }
                else if (above > (opts.totalPage - 4)){
                    above = opts.totalPage;
                    below = (opts.totalPage - opts.pageSpanNum);
                }

                start = below;
                end = above;
            }
            if (start > 3){
                sb += '<a page="1">1</a>';
                sb += '<a page="2">2</a>...';
            }

            for (var i = start; i <= end; i++){
                if(i==opts.currentPage){
                    sb += '<span page="'+i+'" class="current">'+i+'</span>';
                }else{
                    sb += '<a page="'+i+'">'+i+'</a>';
                }
            }
            if (end < (opts.totalPage - 3)){
                sb += '...<a page="'+(opts.totalPage - 1)+'">'+(opts.totalPage - 1)+'</a>';
                sb += '<a page="'+opts.totalPage+'">'+opts.totalPage+'</a>';
            }
            return sb;
        });
    }


    // 私有函数：debugging
    function info(obj) {
        if (window.console && window.console.log)
            window.console.log(obj);
    };
    //将参数标准化，防止外部传入的几个数字类型的变成string
    function standardParam(opts){
        opts.currentPage = Number(opts.currentPage);
        opts.totalPage = Number(opts.totalPage);
        opts.linesPerPage = Number(opts.linesPerPage);
        opts.totalNum = Number(opts.totalNum);
        opts.pageSpanNum = Number(opts.pageSpanNum);
        //如果当前总页数是0，那么totalPage置为1，currentPage也置为1
        if(opts.totalPage == 0){
            opts.totalPage = 1;
            opts.currentPage = 1;
        }
    }
    // 插件的defaults
    $.fn.ulynlistPagination.defaults = {
        pageBarPath:'pagebar',//pagebar的根文件夹路径
        pageBarTpl: 'default', //模版文件名
        currentPage:1, //当前页码
        totalPage:0,   //总页数
        linesPerPage:10,  //每页几行
        totalNum:0,   //总记录数
        pageSpanNum:5,  //输出span的个数
        afterPaginationRender: function () {}, //分页条渲染加载完回调方法
        extra:{}//额外一些数据定义，一般没用，除了自定义模版参数呈现可能会有用，比如：
        // flat模版
        //   linesPerPageEditable:true,  //每页几行的输入框是否可以编辑

    };

// 闭包结束
})(jQuery);