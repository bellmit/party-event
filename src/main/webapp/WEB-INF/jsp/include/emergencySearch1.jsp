<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" isELIgnored="false"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/analyse.css"/>">
      <script type="text/javascript" src="<c:url value="/js/analyse.js"/>"></script>
    
<form id="search-form" method="post">
                        <div class="dwTop">
                            <div class="zzCondition clearfix">
                                <div class="subItem" style="width: 100%">
                                    <label>查询条件：</label>
                                    <div class="cx-con">
                                        <div class="cx-cell fl">
                                            <span>阅知状态：</span>
                                            <b class="status-b">全部</b>
                                            <a href="javascript:void(0)" onclick="chkSpan('0','rk_status')" class="del-a">X</a>
                                        </div>
                                        <div class="cx-cell fl">
                                            <span>上报状态：</span>
                                            <b class="status-b">全部</b>
                                            <a href="javascript:void(0)" onclick="chkSpan('0','rp_status')" class="del-a">X</a>
                                        </div>
                                        <div class="cx-cell fl">
                                            <span>处理状态：</span>
                                            <b class="status-b">全部</b>
                                            <a href="javascript:void(0)" onclick="chkSpan('0','tran_status')" class="del-a">X</a>
                                        </div>
                                    </div>
                                    <div class="btns fr ${userMap.SMS_FLAG eq '0' ?'hide':''}">
                                        <button type="button" class="btn btn-primary btn-sm up-down-btn">展开</button>
                                        <button type="button" id="add-btn" class="btn btn-info btn-sm" >上报应急信息</button>
                                    </div>
                                    <div class="btns fr ${userMap.SMS_FLAG ne '0' ?'hide':''}" style="width: 50px; min-width: 50px" >
                                        <button type="button" class="btn btn-primary btn-sm up-down-btn">展开</button>
                                    </div>
                                </div>
                                <div class=" clearfix"></div>
                                <div class="tj-con undis">
                                    <div class="subItem listPageRegInfoPart clearfix subItem1">
                                        <label>阅知状态：</label>
                                        <div class="PartContent clearfix">
											<span onclick="chkSpan('0','rk_status')"
												class="selectAll stateSpan hasSelect" value="all"
												latv="rk_status-all">全部</span> <span
												onclick="chkSpan('1','rk_status')" cat="status_1"
												class="stateSpan" value="00" latv="rk_status_1">镇街未阅知</span> <span
												onclick="chkSpan('2','rk_status')" cat="status_2"
												class="stateSpan" value="01" latv="rk_status_1">镇街已阅知</span> <input
												type="hidden" value="" id="rk_status" /><span
												onclick="chkSpan('3','rk_status')" cat="status_3"
												class="stateSpan" value="10" latv="rk_status_1">应急办未阅知</span> <span
												onclick="chkSpan('4','rk_status')" cat="status_4"
												class="stateSpan" value="11" latv="rk_status_1">应急办已阅知</span> <input
												type="hidden" value="" id="rk_status" />
										</div>
                                    </div>

                                    <div class="subItem listPageRegInfoPart clearfix subItem1">
                                        <label>上报状态：</label>
                                        <div class="PartContent clearfix">
											<span onclick="chkSpan('0','rp_status')"
												class="selectAll stateSpan hasSelect" value="all"
												latv="rp_status-all">全部</span> <span
												onclick="chkSpan('1','rp_status')" cat="status_1"
												class="stateSpan" value="00" latv="rp_status-1">镇街未上报</span> <span
												onclick="chkSpan('2','rp_status')" cat="status_2"
												class="stateSpan" value="01" latv="rp_status-1">镇街已上报</span> <input
												type="hidden" value="" id="rp_status" /><span
												onclick="chkSpan('3','rp_status')" cat="status_3"
												class="stateSpan" value="10" latv="rp_status-1">应急办未上报</span> <span
												onclick="chkSpan('4','rp_status')" cat="status_4"
												class="stateSpan" value="11" latv="rp_status-1">应急办已上报</span> <input
												type="hidden" value="" id="rp_status" />
										</div>
                                    </div>

                                    <div class="subItem listPageRegInfoPart clearfix subItem1" >
                                        <label>办理状态：</label>
										<div class="PartContent clearfix">
											<span onclick="chkSpan('0','tran_status')"
												class="selectAll stateSpan hasSelect" value="all"
												latv="tran_status-all">全部</span> <span
												onclick="chkSpan('1','tran_status')" cat="status_1"
												class="stateSpan" value="0" latv="tran_status-1">未办理</span>
											<span onclick="chkSpan('2','tran_status')" cat="status_2"
												class="stateSpan" value="1" latv="tran_status-0">已办理</span>
											<input type="hidden" value="" id="tran_status" />
										</div>
                                    </div>
                                    <div class="subItem listPageRegInfoPart clearfix subItem1" style="padding-top: 10px">
                                        <label>其他条件：</label>
                                        <div class="PartContent clearfix">
                                            <label class="fl" style="width: 60px; margin-right: 0">应急类型：</label>
											<select class="select2 fr" >
												<option value="all">所有</option>
												<c:set var="gName" value="0" />
												<c:forEach items="${typelist }" var="type_item">
													<c:if test="${gName ne type_item.GROUP_NAME}">
														<c:if test="${gName!='0'}">
															</optgroup>
														</c:if>
														<c:set var="gName" value="${type_item.GROUP_NAME}" />
														<optgroup label="${type_item.GROUP_NAME}">
													</c:if>
													<option value="${type_item.TYPE_ID}">${type_item.TYPE_NAME}</option>
												</c:forEach>
												</optgroup>
											</select>
                                            <label class="fl" style="width: 60px; margin-right: 0">事项：</label>
                                            <div class="fl">
												<input type="text" id="msg_desc" name="msg_desc"
													value="${msg_desc}" class="form-control input-sm input-add" />
												<div class="btns">
													<button type="button" class="btn btn-success btn-sm"
														id="search-btn">查&nbsp;&nbsp;询</button>
													<button type="button" class="btn btn-primary btn-sm"
														id="search-reset" onclick="resetButton()">重&nbsp;&nbsp;置</button>
<%-- 									                <span class="${userMap.SMS_FLAG eq '0' ?'hide':''}"><a href="javascript:void(0);" class="btn btn-success btn-sm" id="add-btn"> --%>
<!-- 										                 <i></i>采集应急信息 -->
<!-- 													</a> -->
<!-- 													</span> -->
												</div>
											</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
<script>
function resetButton(){
	$(".select2").val("").trigger("change");
	$("#msg_desc").val("");
	chkSpan1('0','rk_status');
	chkSpan1('0','rp_status');
	chkSpan1('0','tran_status');
}
function chkSpan1(x,id){
	$("span[latv^='"+id+"']").each(function(i){
		$(this).removeClass('hasSelect');
		if(x==i){
			$(this).addClass("hasSelect");
			$("#"+id).val($(this).attr("value"));
		}
	});
}
/* // 应急分析 
$(function () {
   $('.cx-cell').hover(function (){
       $(this).find('a').show();
   },function(){
       $(this).find('a').hide();
   });

    $('.up-down-btn').click(function () {
        if($(this).text() == '收起'){
            $('.tj-con').stop().slideUp();
            $(this).text('展开');
        }else{
            $('.tj-con').stop().slideDown();
            $(this).text('收起');
        }
    });

    $('.PartContent span').click(function(){
        var index = $('.PartContent').index($(this).parent());
        //alert(index);
        $(this).addClass('hasSelect');
        $(this).siblings().removeClass('hasSelect');
        $('.cx-cell').eq(index).show().find('b').text($(this).text());
    });

    $('.cx-cell .del-a').click(function(){
        var index = $('.cx-cell').index($(this).parent());
        $(this).parent().hide();
        $('.PartContent').eq(index).find('.selectAll').addClass('hasSelect');
        $('.PartContent').eq(index).find('.selectAll').siblings().removeClass('hasSelect');
    }); 
});*/
</script>