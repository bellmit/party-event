/** 注：多级请用ztree插件
 * 参数说明：
 *  label:菜单标题
 *  prop:给当前a标签添加属性：
 *                      "prop":{
                            "class":"cur",
                            "id":"id1",
                            "href":"http://www.baidu.com",
                            "target":"_blank"
                        }
 *  subTree:子菜单
 *  callbackFunc:点击菜单回调函数,"callbackFunc":function(){alert(1);}
 *  expandAll:展开全部菜单,默认不展开，true展开
 *  opened:当前菜单是否展开，true为展开，默认false不展开（只要任意一个子菜单配置即可）
 *
 *  页面加载进来渲染菜单之前调用clickedById(list,[id])即可选中id所在菜单选项，没有传id默认选中第一个
 *
 */
;(function($,window,document,undefined){
    var pluginName = "qlTree",
        defaults = {
            "prop":{
                "href":"#"
            }
        };

    function QlTree(elements, options){
        this.elements = elements;
        this.options = $.extend({},defaults,options);
        this._defaults = defaults;
        this._name = pluginName;

        this.init();
    }

    QlTree.prototype.init = function(){
        var that = this;
        if(!this.options.list){
            throw new Error("没有填写list属性");
            return;
        }
        var list = this.options.list;
        //给每个加上类level
        addLevel(list,"");


        if(typeof this.options.curById == "undefined"){
            clickedById(that,list);
            renderData($(this.elements),list);
        }else{
            renderData($(this.elements),list);
            clickedById(that,list,this.options.curById);
        }

        //是否全部展开
        if(this.options.expandAll){
            $(this.elements).find("li ul").show();
            if(!$(this.elements).find("li ul").hasClass("expand")){
                $(this.elements).find("li ul").parent().addClass("expand");
            }
        }

        //展开菜单
        $(this.elements).find(".opened").each(function(){
            $(this).parent("ul").parents("li").addClass("expand");
        });
        $(this.elements).find("li.opened").each(function(){
            $(this).parents().show();
        });

        //子菜单拉伸
        $(this.elements).find("a").click(function(){
            $(that.elements).trigger("itemClick",[]);
            if($(this).siblings("ul").length > 0){
                $(this).siblings("ul").slideToggle("fast");
                $(this).parent("li").toggleClass("expand");
            }else{
                $(this).addClass("cur");
                $(that.elements).find("li a.cur").not($(this)).removeClass("cur");
                //其他菜单收起来
//                $(that.elements).find("li.expand").not($(this).parents("li.expand")).children("a").trigger("click");

                
                var levelStr = $(this).attr("levelStr").split("-");
                var curStr = "";
                $.each(levelStr,function(index,value){
                    if(index != levelStr.length - 1){
                        curStr += "[" + value + "].subTree";
                    }else{
                        curStr += "[" + value + "]";
                    }
                });
                var curObj = eval("list" + curStr);

                //点击回调函数
                if($(this).hasClass("callbackFunc")){
                    curObj.callbackFunc(curObj);
                }else if(that.options.callbackFunc){
                    that.options.callbackFunc(curObj);
                }
            }
        });
    };

    //根据id选中状态
    function clickedById(that,list,id){
        if(typeof id == "undefined"){   //如果没有传id，则默认选中第一个菜单选项
            var cur = findLastMenu(list);
            cur.opened = true;
            if(cur.prop){
                if(cur.prop["class"]){
                    if(cur.prop["class"].indexOf("cur") == -1){
                        cur.prop["class"] += " cur";
                    }
                }else{
                    cur.prop["class"] = "cur";
                }
            }else{
                cur.prop = {};
                cur.prop["class"] = "cur";
            }
        }else{
            $(that.elements).find(".cur").removeClass("cur");
            $(that.elements).find(("#" + id)).addClass("cur");
        }
    }

    //找到第一个没有子菜单的菜单
    function findLastMenu(list){
        var cur = list[0];
        if(list[0].subTree){
            cur = list[0].subTree[0];
            findLastMenu(list[0].subTree);
        }
        return cur;
    }

    //渲染结构
    function renderData($that,list){
        $that.append($("<ul></ul>"));
        $.each(list,function(index,value){
            //配置图片地址
            if(value.iconUrl){
                $that.children("ul").append($("<li><a onselectstart='return false;' title='" + value.label +
                    "'><img src='" + value.iconUrl + "' /><span>" +
                    "" + value.label + "</span></a></li>"));
            }else{
                $that.children("ul").append($("<li><a onselectstart='return false;' title='" + value.label +
                    "'><span>" + value.label + "</span></a></li>"));
            }
            //给当前a标签添加属性
            if(value["prop"]){
                $.each(value["prop"],function(p,v){
                    $that.children("ul").children("li:last-child").children("a").attr(p,v);
                });
            }
            //在父元素li里加上opened类
            if(value.opened){
                $that.children("ul").children("li:last-child").addClass("opened");
            }
            if(value.subTree){
                $that.children("ul").children("li:last-child").children("a").append($("<i class='i_end'></i>"));
                renderData($that.children("ul").children("li:last-child"),value.subTree);
            }
            if(value.level){
                $that.children("ul").children("li:last-child").children("a").attr("levelStr",value.level.substring(1));
            }
            if(value.callbackFunc){
                $that.children("ul").children("li:last-child").children("a").addClass("callbackFunc");
            }
        });
    }

    //给每级加上level
    function addLevel(list, curLevel){
        $.each(list,function(index,value){
            value.level = curLevel + "-" + index;

            if(value.subTree){
                addLevel(value.subTree, value.level);
            }
        });
    }

    $.fn[pluginName] = function(options){
        return this.each(function(){
            if(!$.data(this,"plugin_" + pluginName)){
                $.data(this,"plugin_" + pluginName);
                new QlTree(this,options);
            }
        });
    };
})(jQuery,window,document,undefined);