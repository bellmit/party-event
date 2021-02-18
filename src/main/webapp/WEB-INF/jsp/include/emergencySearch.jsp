<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" isELIgnored="false"%>
<form id="search-form" method="post">
	<div class="dwTop">
		<div class="zzCondition clearfix">
			<div class="subItem"> 
			<label class="fl" style="margin-top: 3px;">问题类型：</label>
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
			</div>
			<%-- <div class="subItem">
				<label class="fl" style="margin-top: 3px;">事项：</label>
				<div class="clearfix">
					<input type="text" id="msg_desc" name="msg_desc"
						value="${msg_desc}" class="form-control input-sm input-add" style="width:250px" />
					
				</div>
			</div> --%>
			<div class="subItem">
				<label class="fl" style="margin-top: 3px;">问题名称：</label>
				<div class="clearfix">
					<input type="text" id="msg_title" name="msg_title"
						value="${msg_title}" class="form-control input-sm input-add" style="width:250px;height: 28px;" />
				</div>
			</div>
			<div class="subItem">
				<label class="fl" style="margin-top: 3px;">来源：</label>
				<select class="select2 fr" id="msg_source" name="msg_source" value="${msg_source}" style="min-width:132px">
					<option value="">请选择</option>
					<option value="0">设施问题</option>
					<option value="1">问题反映</option>
					<option value="2">居民来访</option>
					<option value="3">社区走访</option>
					<option value="4">党建联席会</option>
					<option value="5">党建共同体</option>
					<option value="30">党员报道</option>
				</select>
			</div>
			 <div class="subItem">
                  <label class="fl" style="margin-top: 3px;">反映时间：</label>
                  <div class="form_datetime"><!--时间选择的需要，得包这个div-->
                          <input id="startTime" name="startTime" value="" class="form-control g-5 input-sm input-time" type="text"  ><!--时间默认大小为g-4，需要添加id，需跟label的for值一致-->
                          <span class="icon-remove add-on"><i class="glyphicon glyphicon-remove"></i></span>
                          <label for="startTime"  class="icon-th add-on"><i class="glyphicon glyphicon-calendar"></i></label><!--需要跟上面的那个input的id一致-->
                      </div>
                      <b class="link_line g-0">-</b>
                      <div class="form_datetime"><!--时间选择的需要，得包这个div-->
                          <input id="endTime" name="endTime" value="" class="form-control g-5 input-sm input-time" type="text"  ><!--时间默认大小为g-4，需要添加id，需跟label的for值一致-->
                          <span class="icon-remove add-on"><i class="glyphicon glyphicon-remove"></i></span>
                          <label for="endTime" class="icon-th add-on"><i class="glyphicon glyphicon-calendar"></i></label><!--需要跟上面的那个input的id一致-->
                      </div>
                  <!-- <div class="sub-item rangePart clearfix">
                      
                  </div> -->
             </div> 
			<div class="subItem">
				<label class="fl" style="margin-top: 3px;">反映人：</label>
				<div class="clearfix">
					<input type="text" id="msg_name" name="msg_name"
						value="${msg_name}" class="form-control input-sm input-add" />
				</div>
				<div class="btns">
						<button type="button" class="btn btn-success btn-sm"
							id="search-btn">查&nbsp;&nbsp;询</button>
						<button type="button" class="btn btn-primary btn-sm"
							id="search-reset" onclick="resetButton()">重&nbsp;&nbsp;置</button>
		                <span class="${userMap.SMS_FLAG eq '0' ?'hide':''}">
							<%--<a href="javascript:void(0);" class="btn btn-success btn-sm" id="add-btn">
			                 <i></i>采集应急信息
							</a>--%>
						</span>
				</div>
			</div>
			<div class=" clearfix"></div>
			<div class="show-con">
				<a href="javascript:void(0)" class="hide-a">隐藏查询条件</a>
				<div class="hide-con">
					<%--<div class="subItem listPageRegInfoPart clearfix subItem1">
						<label>阅知状态：</label>
						<div class="PartContent clearfix">
							<span onclick="chkSpan('0','rk_status')"
								class="selectAll stateSpan hasSelect" value="all"
								latv="rk_status-all">全部</span> <!-- <span
								onclick="chkSpan('1','rk_status')" cat="status_1"
								class="stateSpan" value="00" latv="rk_status_1">镇街未阅知</span> <span
								onclick="chkSpan('2','rk_status')" cat="status_2"
								class="stateSpan" value="01" latv="rk_status_1">镇街已阅知</span>  --><input
								type="hidden" value="" id="rk_status" /><span
								onclick="chkSpan('1','rk_status')" cat="status_3"
								class="stateSpan" value="10" latv="rk_status_1">社区未阅知</span> <span
								onclick="chkSpan('2','rk_status')" cat="status_4"
								class="stateSpan" value="11" latv="rk_status_1">社区已阅知</span> <input
								type="hidden" value="" id="rk_status" />
						</div>
					</div>
		
					<div class="subItem listPageRegInfoPart clearfix subItem1">
						<label>协办状态：</label>
						<div class="PartContent clearfix">
							<span onclick="chkSpan('0','rp_status')"
								class="selectAll stateSpan hasSelect" value="all"
								latv="rp_status-all">全部</span> <!-- <span
								onclick="chkSpan('1','rp_status')" cat="status_1"
								class="stateSpan" value="00" latv="rp_status-1">镇街未上报</span> <span
								onclick="chkSpan('2','rp_status')" cat="status_2"
								class="stateSpan" value="01" latv="rp_status-1">镇街已上报</span> --> <input
								type="hidden" value="" id="rp_status" /><span
								onclick="chkSpan('1','rp_status')" cat="status_3"
								class="stateSpan" value="10" latv="rp_status-1">物管未协办</span> <span
								onclick="chkSpan('2','rp_status')" cat="status_4"
								class="stateSpan" value="11" latv="rp_status-1">物管已协办</span> <input
								type="hidden" value="" id="rp_status" />
						</div>
					</div>--%>
		
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
					</div>
			</div>
		</div>
	</div>
</form>
<script>
jQuery(".input-time").datetimepicker({
    language:"zh-CN",
    format: 'yyyy-mm-dd hh:ii',
    autoclose: true,
    forceParse: true,
    pickerPosition: "bottom-left",
    todayBtn: true
});
jQuery(".icon-remove").click(function(){
	jQuery(this).prev().val(' ');
 });
function resetButton(){
	$(".select2").val("").trigger("change");
	$("#msg_desc").val("");
	$("#msg_name").val("");
	$("#msg_title").val("");
	$("#startTime").val("");
	$("#endTime").val("");
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
jQuery('.hide-a').click(function(){
	if(jQuery(this).text() == "隐藏查询条件"){
		jQuery(this).text('显示查询条件');
	}else{
		jQuery(this).text('隐藏查询条件');
	}
	jQuery('.hide-con').slideToggle('fast');
});
</script>