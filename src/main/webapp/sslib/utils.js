/**
 * 取得上下文根前缀，比较定制化的方法 utils.js在/context/js下
 * @param message
 * @param opts
 */
$.getContextPath = function(){
    if(this.contextPath === undefined){
        //遍历获取
        var scripts = document.getElementsByTagName("script");
        if(scripts && scripts.length > 0){
            for(var i=0;i<scripts.length;i++){
                var nodeScript = scripts[i];
                var jsPath = nodeScript.hasAttribute ? // non-IE6/7
                    nodeScript.src :
                    // see http://msdn.microsoft.com/en-us/library/ms536429(VS.85).aspx
                    nodeScript.getAttribute("src", 4);
                if(jsPath.indexOf("/sslib/utils.js")!==-1) {
//                    var basePath = jsPath.substring(jsPath.indexOf("/",10));
                    this.contextPath = jsPath.replace("/sslib/utils.js","");
                    break;
                }
            }
        }else{
            this.contextPath = "";
        }
    }
    return this.contextPath;
}
$.alert = function(message, opts) {
    var options = jQuery.extend({
        'className': 'danger',
        showClose: true,
        duartion: 2000,
        'z-index': 2000
    }, opts);

    var html = '<div class="alert alert-'+options.className+' fade in" style="min-width:300px;position: fixed; top:0px;z-index:'+options['z-index']+'">' +
        (options.showClose?'<button style="margin-left: 10px" data-dismiss="alert" type="button" class="close" aria-hidden="true">×</button>':'')
        + message + '</div>';
    var el = $(html);
    el.appendTo(document.body).css('left', $(document).width()/2-el.width()/2);
    if (options.duartion> 0)
        el.delay(options.duartion).fadeOut(function(){el.alert('close')});
}

$.is_empty = function(value) {
    return value && value.trim()=='' || !value;
}

String.prototype.irender = function() {
    if (arguments.length < 1)   return this;

    var result = ""+this;
    for (var i=0; i <arguments.length; i++) {
        if (undefined == arguments[i] || null == arguments[i]) arguments[i] = "";
        arguments[i] = arguments[i];
        result = result.replace('$', arguments[i])
    }
    return result;
}

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$.getQueryString = function(name){
    // 如果链接没有参数，或者链接中不存在我们要获取的参数，直接返回空
    if(location.href.indexOf("?")==-1 || location.href.indexOf(name+'=')==-1)
    {
        return '';
    }

    // 获取链接中参数部分
    var queryString = location.href.substring(location.href.indexOf("?")+1);
    queryString = queryString.replace(/#.*$/, '');
    // 分离参数对 ?key=value&key2=value2
    var parameters = queryString.split("&");

    var pos, paraName, paraValue;
    for(var i=0; i<parameters.length; i++)
    {
        // 获取等号位置
        pos = parameters[i].indexOf('=');
        if(pos == -1) { continue; }

        // 获取name 和 value
        paraName = parameters[i].substring(0, pos);
        paraValue = parameters[i].substring(pos + 1);

        // 如果查询的name等于当前name，就返回当前值，同时，将链接中的+号还原成空格
        if(paraName == name)
        {
            return decodeURIComponent(paraValue.replace(/\+/g, " "));
        }
    }
    return '';
};
$.getCookie = function(name) {
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) return unescape(arr[2]); return null;
}

$.loading = function(opt) {
    var options = $.extend({
        id: 'iloading'+Math.random(),
        img_src: '',
        message: '数据加载中，请稍等',
        zindex: 2000
    }, opt);

    var html = '<div id="$" class="alert alert-info fade in" style="min-width:300px;position: fixed; top:0px;z-index:$"><img src="$">$</div>'.irender(
        options.id, options.zindex, options.img_src, options.message);

    $('body').append(html);

    return {
        show: function() {
            $(options.id).show();
        },

        hide: function() {
            $(options.id).hide();
        },

        taggle: function() {
            $(options.id).taggle();
        }
    }
}
//初始化get 参数
var getUrlParams = function(param){
    var aQuery = window.location.href.split("?");  //取得Get参数
    var urlParams = [];
    if(aQuery.length > 1)
    {
        var aBuf = aQuery[1].split("&");
        for(var i=0, iLoop = aBuf.length; i<iLoop; i++)
        {
            var aTmp = aBuf[i].split("=");  //分离key与Value
            urlParams[aTmp[0]] = aTmp[1];
        }
    }

    if(param){
        return urlParams[param];
    }else{
        return urlParams;
    }
}
//身份证号合法性检测
$.checkDate=function( idcard ){
    var socialNo = idcard;
    if(socialNo == "")
    {
        return (false);
    }
    if (socialNo.length != 15 && socialNo.length != 18)
    {
        return (false);
    }
    var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
    if(area[parseInt(socialNo.substr(0,2))]==null) {
        return (false);
    }
    if (socialNo.length == 15)
    {
        pattern= /^\d{15}$/;
        if (pattern.exec(socialNo)==null){
            return (false);
        }
        var birth = parseInt("19" + socialNo.substr(6,2));
        var month = socialNo.substr(8,2);
        var day = parseInt(socialNo.substr(10,2));
        switch(month) {
            case '01':
            case '03':
            case '05':
            case '07':
            case '08':
            case '10':
            case '12':
                if(day>31) {
                    return false;
                }
                break;
            case '04':
            case '06':
            case '09':
            case '11':
                if(day>30) {
                    return false;
                }
                break;
            case '02':
                if((birth % 4 == 0 && birth % 100 != 0) || birth % 400 == 0) {
                    if(day>29) {
                        return false;
                    }
                } else {
                    if(day>28) {
                        return false;
                    }
                }
                break;
            default:
                return false;
        }
        var nowYear = new Date().getYear();
        if(nowYear - parseInt(birth)<15 || nowYear - parseInt(birth)>100) {
            return false;
        }
        return (true);
    }
    var Wi = new Array(
        7,9,10,5,8,4,2,1,6,
        3,7,9,10,5,8,4,2,1
    );
    var lSum = 0;
    var nNum = 0;
    var nCheckSum = 0;
    for (i = 0; i < 17; ++i)
    {
        if ( socialNo.charAt(i) < '0' || socialNo.charAt(i) > '9' )
        {
            return (false);
        }
        else
        {
            nNum = socialNo.charAt(i) - '0';
        }
        lSum += nNum * Wi[i];
    }
    if( socialNo.charAt(17) == 'X' || socialNo.charAt(17) == 'x')
    {
        lSum += 10*Wi[17];
    }
    else if ( socialNo.charAt(17) < '0' || socialNo.charAt(17) > '9' )
    {
        return (false);
    }
    else
    {
        lSum += ( socialNo.charAt(17) - '0' ) * Wi[17];
    }
    if ( (lSum % 11) == 1 )
    {
        return true;
    }
    else
    {
        return (false);
    }
}