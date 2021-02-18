/**
 * Created with IntelliJ IDEA.
 * User: ulyn
 * Date: 13-10-19
 * Time: 下午5:25
 * To change this template use File | Settings | File Templates.
 */
jQuery.ss_loading_count = 0;
jQuery(document).ready(function(){
    if(jQuery.ss_loading_count<=0){
        $("#ss-loading").hide();
    }
});
jQuery.extend({
    ssajax: function(opts) {
        var fn = {
            timeout: 30000, // 超时设置：30秒
            beforeSend: function(){
                jQuery.ss_loading_count++;
                $("#ss-loading").show();
            },
            complete: function(){
                jQuery.ss_loading_count--;
                if(jQuery.ss_loading_count<=0){
                    $("#ss-loading").hide();
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){alert("操作失败！")},
            success:function(data, textStatus){}
        }
        if(opts.timeout){
            fn.timeout=opts.timeout;
        }
        if(opts.error){
            fn.error=opts.error;
        }
        if(opts.success){
            fn.success=opts.success;
        }
        if(opts.beforeSend){
            fn.beforeSend=opts.beforeSend;
        }
        if(opts.complete){
            fn.complete=opts.complete;
        }
        //扩展增强处理
        var _opt = $.extend(opts,{
            cache: false,//在调用load方法之前利用该方法禁止load方法调用IE缓存文件
            timeout:fn.timeout,
            dataType:"json",
            error:function(XMLHttpRequest, textStatus, errorThrown){
                //错误方法增强处理
                fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            success:function(data, textStatus){
                //成功回调方法增强处理
                if(data.status){
                    fn.success(data.data, textStatus);
                }else{
                    if(opts.businessError){
                        opts.businessError(data.msg);
                    }else{
                        alert("请求数据失败！"+data.msg);
                    }
                }
            },
            beforeSend:function(){
                fn.beforeSend();
            },
            complete:function(){
                fn.complete();
            }
        });
        jQuery.ajax(opts);
    }
});
