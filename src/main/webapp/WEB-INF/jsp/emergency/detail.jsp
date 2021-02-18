<!--
 版权所有：厦门畅享信息技术有限公司
 Copyright 2014 Xiamen Sunsharing Information CO., LTD.
 All right reserved. 
====================================================
 文件名称: text.html
 修订记录：
 No    日期				作者(操作:具体内容)
 1.    14-10-29			Administrator(创建:创建文件)
====================================================
 文件描述：(说明本文件需要实现的内容)
 
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tlu" uri="/tagelutil"%>
<!DOCTYPE html>
<meta charset=utf-8>
<html lang="zh-cn">
<head>
    <title>应急信息处理详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"  />
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>"/>
    <%-- <link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-3.5.1/select2.css"/>"/> --%>
    
	<link rel="stylesheet" type="text/css" href="<c:url value="/lib/select2-4.0.2/dist/css/select2.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/sslib/qlTree/qlTree.css"/>"/>
    <link href="<c:url value="/styles/css/flat/blue.css"/>" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/base.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/nineModules.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/css/detail.css"/>"/>

    <script type="text/javascript" src="<c:url value="/lib/jquery/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <%-- <script type="text/javascript" src="<c:url value="/lib/select2-3.5.1/select2.js"/>"></script> --%>
    <script type="text/javascript" src="<c:url value="/lib/select2-4.0.2/dist/js/select2.full.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/artTemplate/template-simple.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/ulynlist/ulynlist.pagebar.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/sslib/qlTree/qlTree.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/lib/layer-3.1.1/dist/layer.js"/>"></script>

    <script type="text/javascript" src="<c:url value="/js/icheck.min.js"/>"></script>
    <script type="text/javascript">
        //单选框样式
        $(document).ready(function(){

    		$(".select2").select2();
    		
        	$("input[name='flat-radio']").on('ifChecked', function(event){  
//         		  alert(event.type + ' callback');
					userShowHide($(this).val());
        		}); 
            $('input').iCheck({
                checkboxClass: 'icheckbox_flat-blue',
                radioClass: 'iradio_flat-blue'
            });
//             formatspan("${map1['REPORT_TIME'] }","reportTime");
//             formatspan("${map1['REPORT_TIME'] }","tableTime1");
//             formatspan("${map1['REPORT_TIME'] }","tableTime2");
            
//             formatspan($("#timespan-jd").html(),"timespan-jd");
//             formatspan($("#timespan-xf").html(),"timespan-xf");
            $(".emType").html(repValue($(".emType").html()));
        });

        var EndTime= new Date();
        EndTime.setDate(EndTime.getDate()+5);
        function GetRTime(){
        	
            
            var NowTime = new Date();
            var t =EndTime.getTime() - NowTime.getTime();
            var d=Math.floor(t/1000/60/60/24);
            var h=Math.floor(t/1000/60/60%24);
            var m=Math.floor(t/1000/60%60);
            var s=Math.floor(t/1000%60);

            document.getElementById("t_d").innerHTML = d;
            document.getElementById("t_h").innerHTML = h;
            document.getElementById("t_m").innerHTML = m;
            document.getElementById("t_s").innerHTML = s;
        }
        if('${show}'=='show'){
        	setInterval(GetRTime,0);	
        }
        typelist=[];
    	<c:forEach items="${typelist }" var="type_item">
    	typelist.push({id:"${type_item.TYPE_ID}",name:"${type_item.TYPE_NAME}"});
    	</c:forEach>
    	

    	layer.config({
            extend: 'lx/style.css', //加载您的扩展样式
            skin: 'layer-ext-lx'
        });
    	
    	
    	</script>
</head>
<body>
<div class="content-box">
    <div class="cb-content" style="margin-left:0px">
        <div class="content">
            <div class="mainContent" style="top:0px">
                <div class="mcBox">
                    <div class="dwBottom" style="margin-top:0">
                        <!-- 处理详情 -->
                        <div class="detail2">
                            <b class="tit">问题受理详情</b>
                            <table>
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <tr>
                                	<td style="display:none">应急类型：<span class="sty emType">${emBean.emType }</span></td>
                                    <td>问题标题：${emBean.problemTitle}</td>
                                    <td>反映时间：${tlu:tranTimeStr(emBean.createTime) }</td>
                                </tr>
                                <tr>
                                    <td>反映人员：${emUser.USER_NAME==null? emBean.rpUsername : emUser.USER_NAME}</td>
                                    <td>联系方式:${emBean.msgPhone }</td>
                                </tr>
                                <tr>
                                    <td colspan="2">描&nbsp;&nbsp;述：${emBean.msgDesc }</td>
                                </tr>
                                <tr>
                                    <td colspan="2">反映位置：${emBean.address}    <button id="locationMap">查看</button></td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <span class="fl">相关照片：
                                        <c:if test="${fn:length(emImgList)!=0}"><br/><font color="#428BCA">(点击图片<br/>查看大图)</font>
                                        </c:if>
                                        </span>
                                        <div class="fl imgDiv" id="imagetd">
                                            
                                            <c:if test="${fn:length(emImgList)!=0}">
				                                    <c:forEach items="${emImgList }" var="photo" varStatus="status">
				                                    <a href="<c:url value="/emergency/goImg.do?imgpath=${photo['IMAGE_ADDRESS'] }"/>"
														target="_blank">
<%-- 				                                    <img src="<c:url value="/static/image${photo['IMAGE_ADDRESS'] }"/>" onclick="bigMap(${status.index})" class="img-rounded" title="点击查看大图"> --%>
				                                    <img src="<c:url value="${photo['IMAGE_ADDRESS'] }"/>" class="img-rounded" title="点击查看大图">
				                                    </a>
				                                    </c:forEach>
		                                    </c:if>
		                                    <c:if test="${fn:length(emImgList)==0}">
		                                    	无
		                                    </c:if>
                                            
                                            
                                            
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            
                            <c:set var="isDisplay" value="0"/>
                            <c:set var="depLevel" value="3"/>
                            <c:set var="depName" value=""/>
                            <c:set var="rpFlag" value="0"/>
                            <c:set var="xbFlag" value="0"/>
                            <c:set var="bjFlag" value="0"/>
                           	<c:forEach var="depMap" items="${depList}">
	                            <c:set var="depLevel" value="${depMap.DEP_LEVEL }"/>
	                            <c:set var="depName" value="${depMap.DEP_NAME }"/>
                           		<%-- <c:if test="${depMap.DEP_LEVEL=='3' and emBean.rpStatus=='00'}">
		                            <c:set var="isDisplay" value="1"/>
                           		</c:if>
                           		<c:if test="${depMap.DEP_LEVEL=='4'}">
                           		<c:if test="${emBean.rpStatus=='11' or emBean.rpStatus=='01'}">
		                            <c:set var="isDisplay" value="1"/>
                           		</c:if> 
                           		</c:if>--%>
                           	</c:forEach>
                           	
                            <c:choose>
                            	<c:when test="${depLevel eq '5'}">
                             		<c:set var="isDisplay" value="1"/>
                             	</c:when>
                             	<c:when test="${(emBean.msgLevel == '3' and depLevel eq '3') or ((emBean.msgLevel == '3,4' or emBean.msgLevel == '4') and depLevel eq '4')}">
                             		<c:set var="isDisplay" value="1"/>
                             		<c:set var="rpFlag" value="1"/>
                             		<c:set var="bjFlag" value="1"/>
                             	</c:when>
                             	<c:when test="${(emBean.msgLevel == '3,4' or emBean.msgLevel == '3,4,5') and depLevel eq '3'}">
                             		<c:set var="isDisplay" value="1"/>
                             		<c:set var="xbFlag" value="1"/>
                             	</c:when>
                             	<c:when test="${(emBean.msgLevel == '3,4,5' or emBean.msgLevel == '4,5') and depLevel eq '4'}">
                             		<c:set var="isDisplay" value="1"/>
                             		<c:set var="bjFlag" value="1"/>
                             		<c:set var="xbFlag" value="1"/>
                             	</c:when>
                             	<c:otherwise>
                             		<c:set var="isDisplay" value="0"/>
                             	</c:otherwise>
                             </c:choose>

                            <b class="tit">问题受理进度</b>
                            <div class="step-con">
                                <c:forEach var="item" items="${emTranList}" varStatus="idx">
                                    <c:choose>
                                        <c:when test="${fn:length(emTranList) > idx.index+1}">
                                            <c:choose>
                                            <c:when test="${idx.index == 0}">
                                                <div class="step-item finish">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="step-item finish" style="width:${ 100/(fn:length(emTranList)-1)}%">
                                            </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${fn:length(emTranList) == idx.index+1}">
                                                <c:choose>
                                                <c:when test="${idx.index == 0}">
                                                <div class="step-item active">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="step-item active" style="width:${ 100/(fn:length(emTranList)-1)}%">
                                            </c:otherwise>
                                                </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="step-item">
                                        </c:otherwise>
                                    </c:choose>


                                        <c:choose>
                                            <c:when test="${idx.index == 0}">
                                                <span class="step-line hide"></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="step-line"></span>
                                            </c:otherwise>
                                        </c:choose>
                                        <%--<span class="step-line ${emTranList.length > 1 && idx.index == 0 ? 'none' : ''}"></span>--%>
                                        <span class="step-val">${idx.index+1}</span>
                                        <span class="step-label">
                                                ${item.TRAN_STATUS=='0'?"问题反映"
                                                :item.TRAN_STATUS=='1'?"已受理"
                                                :item.TRAN_STATUS=='2'?"已转物管"
                                                :item.TRAN_STATUS=='3'?"续报"
                                                :item.TRAN_STATUS=='4'?"已处理"
                                                :item.TRAN_STATUS=='5'?"已协办"
                                                :item.TRAN_STATUS=='6'?"已关闭"
                                                :item.TRAN_STATUS=='7'?"社区同意公开"
                                                :item.TRAN_STATUS=='8'?"社区不同意公开"
                                                :item.TRAN_STATUS=='9'?"镇街同意公开"
                                                :item.TRAN_STATUS=='20'?"不予受理"
                                                :item.TRAN_STATUS=='30'?"已转部门"
                                                :item.TRAN_STATUS=='10'?"镇街不同意公开":"未知"}
                                        </span>
                                    </div>
                                </c:forEach>

                            </div>


                            <b class="tit">问题受理流程</b>
                            <table class="tc">
                                <colgroup>
                                    <col width="5%">
                                    <col width="15%">
                                    <col width="5%">
                                    <col width="15%">
                                    <col width="30%">
                                </colgroup>
                                <tr>
                                    <th>步骤</th>
                                    <th>处理时间</th>
                                    <th>处理名称</th>
                                    <th>处理人</th>
                                    <th>处理意见</th>
                                </tr>
                                <c:forEach var="emTranInfo" items="${emTranList}" varStatus="emIndex">
                                <tr>
                                    <td>${emIndex.index+1}</td>
                                    <td>${tlu:tranTimeStr(emTranInfo.CREATE_TIME)}</td>
                                    <td>${emTranInfo.TRAN_STATUS=='0'?"问题反映":emTranInfo.TRAN_STATUS=='1'?"已受理":emTranInfo.TRAN_STATUS=='2'?"已转物管"
                                    :emTranInfo.TRAN_STATUS=='3'?"续报":emTranInfo.TRAN_STATUS=='4'?"已处理"
                                    :emTranInfo.TRAN_STATUS=='5'?"已协办":emTranInfo.TRAN_STATUS=='6'?"已关闭":emTranInfo.TRAN_STATUS=='7'?"社区同意公开"
                                    :emTranInfo.TRAN_STATUS=='8'?"社区不同意公开"
                                    :emTranInfo.TRAN_STATUS=='9'?"镇街同意公开"
                                    :emTranInfo.TRAN_STATUS=='20'?"不予受理"
                                    :emTranInfo.TRAN_STATUS=='30'?"已转部门"
                                    :emTranInfo.TRAN_STATUS=='10'?"镇街不同意公开":"未知"}
                                    </td>
                                    <td>${emTranInfo.USER_NAME}[${emTranInfo.TRAN_DEP}]</td>
                                    <td>${emTranInfo.MSG_REMARK}    ${emTranInfo.NO_DEALCON}</td>
                                </tr>
                                <c:if test="${emTranInfo.TRAN_STATUS eq '6'}">
                         		<c:set var="isDisplay" value="0"/>
                         		</c:if>
                                </c:forEach>
                                
                            </table>
                            <b class="tit">问题办理情况</b>
                            <table class="tc">
                                <colgroup>
                                    <col width="5%">
                                    <col width="15%">
                                    <col width="5%">
                                    <col width="15%">
                                    <col width="30%">
                                </colgroup>
                                <tr>
                                    <th>步骤</th>
                                    <th>办理时间</th>
                                    <th>办理名称</th>
                                    <th>办理人</th>
                                    <th>办理意见</th>
                                </tr>
                                <c:forEach var="emTranInfo" items="${emLaterTranList}" varStatus="emIndex">
                                <c:if test="${not empty emTranInfo.MSG_REMARK}">
                                <tr>
                                    <td>${emIndex.index+1}</td>
                                    <td>${tlu:tranTimeStr(emTranInfo.CREATE_TIME)}</td>
                                    <td>${emTranInfo.TRAN_STATUS=='0'?"":emTranInfo.TRAN_STATUS=='20'?"后续跟踪处理":emTranInfo.TRAN_STATUS=='21'?"已办结":"未知"}
                                    </td>
                                    <td>${emTranInfo.USER_NAME}[${emTranInfo.TRAN_DEP}]</td>
                                    <td>${emTranInfo.MSG_REMARK}</td>
                                </tr>
                                </c:if>
                                <c:if test="${emTranInfo.TRAN_STATUS eq '6'}">
                         		<c:set var="isDisplay" value="0"/>
                         		</c:if>
                                </c:forEach>
                                
                            </table>
                                    <input type="hidden" id="depName" name="depName" value="${depName}">
                                    <input type="hidden" id="depLevel" name="depLevel" value="${depLevel}">
                                    
                            <c:if test="${(isDisplay=='1' and emBean.tranStatus eq '0')}">
	                                    <div class="clearfix" id="biaoti">
			                                <b class="tit fl">问题受理意见</b>
                            			</div>
                                
                                <div class="subItem">
	                                    <div class="deal-con skin-section">
	                                        <ul class="list">
	                                            <c:if test="${bjFlag eq '1' and depLevel ne '5'}">
	                                            <li class="fl" >
	                                                <input tabindex="1" type="radio" id="flat-radio-1" name="flat-radio" value="4" checked>
	                                                <label for="flat-radio-1">直接处理</label>
	                                            </li>
	                                            </c:if>
	                                            <c:if test="${fn:indexOf(emBean.msgLevel,'5') lt 1 and rpFlag eq '1' and depLevel ne '5'}">
	                                            <li class="fl">
	                                                <input tabindex="2" type="radio" id="flat-radio-2" name="flat-radio" value="2">
	                                                <label for="flat-radio-2">社会协同</label>
	                                            </li>
	                                            </c:if>
                                                <li class="fl">
                                                    <input tabindex="3" type="radio" id="flat-radio-3" name="flat-radio" value="30">
                                                    <label for="flat-radio-3">转部门办理</label>
                                                </li>
	                                           <%--  <c:if test="${xbFlag eq '1' and depLevel ne '5'}">
	                                            <li class="fl">
	                                                <input tabindex="3" type="radio" id="flat-radio-3" name="flat-radio" value="3" ${depLevel eq '3' ? 'checked':'' }>
	                                                <label for="flat-radio-3">社会协同22</label>
	                                            </li>
	                                            </c:if> --%>
	                                            <c:if test="${depLevel eq '5' and emTranList[emTranList.size()-1].get('TRAN_STATUS') ne '5'}">
	                                            <li class="fl">
	                                                <input tabindex="3" type="radio" id="flat-radio-5" name="flat-radio" value="5" ${depLevel eq '3' ? 'checked':'' }>
	                                                <label for="flat-radio-5">处理</label>
	                                            </li>
	                                            </c:if>
	                                            <div class="clear"></div>
	                                        </ul>
	                                        <ul class="list-style" id="messageTypeUL">
	                                        	<li>
		                                        	<label class="fl" style="margin-top:5px;">问题类型：</label>
													<select class="select2 fr" multiple style="width:350px;" id="messageType">
														<!--  <option value="all">所有</option>-->
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
	                                        	</li>
	                                        	<div class="clear"></div>
	                                        </ul>
	                                        <textarea id="textjd" placeholder="请填写相关核实情况及您的处理意见" style="width: 100%; height: 50px; border: 0; padding: 5px;vertical-align: bottom;color:gray"></textarea>
	                                    </div>
	                                </div>
	                                <div class="cl"></div>
	                                 <div class="subItem userDepDiv hide">
	                                    <div class="deal-con skin-section">
	                                    	<c:set var="tempName" value="@@@"/>
	                                        <ul class="list">
	                                        	<c:forEach var="userInfo" items="${depUserList}" varStatus="infoId">
	                                        	<c:if test="${tempName eq '@@@' }">
	                                        	<li class="fl" style="margin-right: 10px"><span class="user-span fb">${userInfo.deptName }：</span></li>
	                                        	</c:if>
	                                        	<c:if test="${tempName ne '@@@' and  tempName ne userInfo.deptName}">
	                                            <div class="clear"></div>
	                                        </ul>
	                                        <ul class="list">
	                                            	<li class="fl" style="margin-right: 10px"><span class="user-span fb">${userInfo.deptName }：</span></li>
	                               				</c:if>
	                               				<c:forEach var="userdepInfo" items="${userInfo.depUser }" varStatus="infoId">
	                                            <li class="fl">
	                                            <input type="checkbox" value="${userdepInfo.USER_ID},${userdepInfo.WX_USER_ID}" id="chx${infoId.index}"/>
	                                            <label for="chx${infoId.index}">${userdepInfo.USER_NAME }</label></li>
	                                            </c:forEach>
	                                    		<c:set var="tempName" value="${userInfo.deptName }"/>
	                                        	</c:forEach>
	                                            <div class="clear"></div>
	                                        </ul>
	                                    </div>
	                                </div>
	                                <div class="clearfix" id="laterBanliBiaoti">
			                            <b class="tit fl">问题办理情况</b>
                            		</div>
	                                 <div class="deal-con skin-section" id="laterBanli">
	                                        <ul class="list">
	                                            <li class="fl" >
	                                                <input tabindex="1" type="radio" id="later_tran-1" name="later_tran" value="21" checked>
	                                                <label for="later_tran-1">办结</label>
	                                            </li>
	                                             <li class="fl" >
	                                                <input tabindex="1" type="radio" id="later_tran-2" name="later_tran" value="20">
	                                                <label for="later_tran-2">后续跟踪处理</label>
	                                            </li>
	                                         </ul>
	                                  </div>    
	                                <div class="cl userDepDiv1 hide"></div>
                                		<div class="btns tc" id="anniu" style="margin: 5px 0">
	                                        <button type="button" id="rk" class="btn btn-sm btn-primary hide" onclick="toSure()">受&nbsp;理</button>&nbsp;&nbsp;
	                                        <button type="button" id="btn" disabled="disabled" class="btn btn-sm btn-primary" onclick="go_process()">提&nbsp;交</button>
	                                        <a href="javascript:closeWindow();">
	                                        <button type="button" class="btn btn-sm" style="margin-left: 20px">取&nbsp;消</button>
	                                        </a>
	                                 </div>
                                </c:if>
                               <%--  <c:if test="${isDisplay!='1' or emBean.tranStatus != '0' or emTranList[emTranList.size()-1].get('TRAN_STATUS') eq '5'}"> --%>
                                <c:if test="${isDisplay!='1' or emBean.tranStatus != '0'}">
                                <c:if test="${later=='later'}">
                                 <div class="clearfix" id="biaoti">
	                                <b class="tit fl">问题办理情况</b>
                         		 </div>
                         		 <div class="deal-con skin-section">
                                    <ul class="list">
	                                   	 <li class="fl" >
	                                          <input tabindex="1" type="radio" id="later_tran-1" name="later_tran" value="21" checked>
	                                          <label for="later_tran-1">办结</label>
	                                      </li>
	                                        <li class="fl" >
	                                           <input tabindex="1" type="radio" id="later_tran-2" name="later_tran" value="20">
	                                           <label for="later_tran-2">后续跟踪处理</label>
	                                       </li>
                                      </ul>
                                    <textarea id="laterTranText" placeholder="请填写相关办理意见" style="width: 100%; height: 50px; border: 0; padding: 5px;vertical-align: bottom;color:gray"></textarea>
	                             </div>  
	                             <div class="btns tc" id="anniu" style="margin: 5px 0">
                                       <button type="button" id="btn" disabled="disabled" class="btn btn-sm btn-primary" onclick="go_laterTran()">提&nbsp;交</button>
                                       <a href="javascript:closeWindow();">
                                       <button type="button" class="btn btn-sm" style="margin-left: 20px">取&nbsp;消</button>
                                       </a>
	                              </div>  
	                              </c:if>
                           		<div class="btns tc" style="margin: 10px 0" id="btnReturn">
                                    <a href="javascript:closeWindow();">
                                    <button type="button" class="btn" style="margin-left: 20px" >返回列表</button>
                                    </a>
                             	</div>
                             	</c:if>
                        </div>
                        
                        
                    </div>
                </div>
            </div>


            <%-- 是否受理提示框 start --%>
            <div class="sl-modal-con">
                <div class="sl-shadow"></div>
                <div class="sl-con">
                    <p class="tips-p">是否受理该问题？</p>
                    <div class="yz-btns-con">
                        <button type="button" id="toSure" onclick="toSure()">受理</button>
                        <button type="button" id="toCancel" onclick="toReject()">不予受理</button>
                    </div>
                </div>
            </div>
            <%-- 是否受理提示框 end --%>

            <%-- 阅知弹窗部分 start --%>
            <div class="yz-modal-con">
                <div class="yz-shadow"></div>
                <div class="yz-con">
                    <p class="tips-p">*为必填项</p>
                    <%--受理处理--%>
                    <div class="yz-items" id="yzYes">
                        <ul class="yz-ul">
                            <li>
                                <span><em>*</em>受理人：</span>
                                <input type="text" name="yzUserName" id="yzUserName" value="${mh_userName}" maxlength="30">
                            </li>
                            <li>
                                <span><em>*</em>联系方式：</span>
                                <input type="number" name="yzUserPhone" id="yzUserPhone" maxlength="11" value="${mh_mobile}">
                            </li>
                            <%--<li>
                                <span><em>*</em>办结时限：</span>
                                <input type="date" name="endDate" id="endDate">
                            </li>--%>
                        </ul>
                    </div>
                    <%--不予受理--%>
                    <div class="yz-items" id="yzNo" style="display: none">
                        <ul class="yz-ul">
                            <li>
                                <span><em>*</em>拒绝理由：</span>
                                <textarea name="noYzCon" id="noYzCon" style="height: 80px;" maxlength="200"></textarea>
                            </li>
                        </ul>
                    </div>
                    <div class="yz-btns-con">
                        <button type="button" id="yzSure" onclick="yzSubmit()">确定</button>
                        <button type="button" id="yzCancel" onclick="yzCancel()">取消</button>
                    </div>
                </div>
            </div>
            <%-- 阅知弹窗部分 end --%>
        </div>
    </div>
</div>
<script>

if(${emTranList[emTranList.size()-1].get("TRAN_STATUS")}=='5' && ${depLevel}=='5'){
	$("#textjd").hide();
	$("#biaoti").hide();
	$("#anniu").hide();
	/* laterTran="1";
	alert("物管登录时=="+laterTran); */
	/* $("#btnReturn").hide(); */
}
if(${depLevel}=='5'){
	$("#laterBanli").hide();
	$("#laterBanliBiaoti").hide();
}
//选中社会协同时候，办理情况选择框不出现
$("input[name='flat-radio']").on('ifChecked', function(event){
    debugger;
		if($(this).val()=="2"){
			$("#laterBanli,#laterBanliBiaoti").hide();
            $(".userDepDiv").show();
	}else if($(this).val()=="30" && ${lastTran} !="30"){
        $("#laterBanli,#laterBanliBiaoti,.userDepDiv").hide();
    }else if($(this).val()!="2" && $(this).val()!="30"&&${depLevel}!='5'){
        $("#laterBanli,#laterBanliBiaoti").show();
	}
});
if(${emBean.emType } != null){
	$("#messageTypeUL").hide();
}
var urlConfig = {
		modifyMessage : '<c:url value="/emergency/modifyMessage.do"/>',
		modifyLaterTran:'<c:url value="/emergency/modifyLaterTran.do"/>',
		modifyRK:"<c:url value="/emergency/modifyRK.do"/>",
    noChuLi:"<c:url value="/emergency/noChuLi.do"/>"
	};

function go_process(){
    var shouliCheck=$("input[name='flat-radio']:checked").val();
    debugger;
	var laterTran=$("input[name=later_tran]:checked").val();//后续办理状态f
	if(laterTran==null||laterTran==""){
		alert("请选择问题办理情况！");
		return;
	}
	//如果是物管角色
	if(${depLevel}=='5'){
		laterTran="1";
	}
	if(shouliCheck=="2" || shouliCheck=="30"){
		laterTran="1";
	}
	var messageType=$("#messageType").val();
	if(${emBean.emType } != null){
		messageType='${emBean.emType }';
	}
	if(messageType==null){
		alert("请选择问题类型");
		messageType=${emBean.emType };
		return;
	}
	 var textjd = $("#textjd").val();
	 if(textjd==""){
		 alert($("#textjd").attr("placeholder"));
		 return;
	 }
	 if(textjd.length>1000){
		 alert("填写字数最多为1000个");
		 return;
	 }
	 var wxUserIds = "";
	 if(!$(".userDepDiv").hasClass("hide")){
		 $("input[id^='chx']:checked").each(function(){
			 wxUserIds+=wxUserIds.length==0?$(this).val():"@_@"+$(this).val();
		 });
		 if(wxUserIds==""){
			 alert("请选择上报对象");
			 return;
		 }
	 }

	 var inputdata={"msgId":"${emBean.msgId}",
			 		"textjd":encodeURI(textjd),
			 		"status":$("input[name='flat-radio']:checked").val(),
			 		"depName":encodeURI($("#depName").val()),
			 		"depLevel":$("#depLevel").val(),
			 		"wxUserIds":wxUserIds,
			 		"messageType":messageType+"",
			 		"laterTran":laterTran
			 		};
	 $.ajax({
       type:"POST",
       url:urlConfig.modifyMessage,
       data:inputdata,
       dataType:"json",
       success:function(data){
   		if(data && data.status){
    	   alert("处理成功");
    	   window.location.href=window.location.href;
// 			closeWindow();
   		}
       },
       beforeSend:function(){
    	// 禁用按钮防止重复提交  
	         $("#btn").attr({ disabled: "disabled" });
       },
       error:function(){
    	   alert("处理失败");
		}
	 });
}
 function go_laterTran(){
	var laterTran=$("input[name=later_tran]:checked").val();
	if(laterTran==null||laterTran==""){
		alert("请选择问题办理情况！");
		return;
	}
	 var textjd = $("#laterTranText").val();
	 if(textjd==""){
		 alert($("#laterTranText").attr("placeholder"));
		 return;
	 }
	 if(textjd.length>1000){
		 alert("填写字数最多为1000个");
		 return;
	 }
	 var wxUserIds = "";
	 var inputdata={"msgId":"${emBean.msgId}",
			 		"textjd":encodeURI(textjd),
			 		"status":$("input[name='flat-radio']:checked").val(),
			 		"depName":encodeURI($("#depName").val()),
			 		"depLevel":$("#depLevel").val(),
			 		"wxUserIds":wxUserIds,
			 		"laterTran":laterTran
			 		};
	 $.ajax({
       type:"POST",
       url:urlConfig.modifyLaterTran,
       data:inputdata,
       dataType:"json",
       success:function(data){
   		if(data && data.status){
    	   alert("处理成功");
    	   window.location.href=window.location.href;
// 			closeWindow();
   		}
       },
       beforeSend:function(){
    	// 禁用按钮防止重复提交  
	         $("#btn").attr({ disabled: "disabled" });
       },
       error:function(){
    	   alert("处理失败");
		}
	 });
} 
function go_rk(data){
     var status = data.status; //true受理,false拒绝
	var inputdata;
    var url;
	if(status){
        inputdata={"id":"${emBean.msgId}",
            "rkStatus":"${emBean.rkStatus}",
            "depLevel":$("#depLevel").val(),
            "userName":data.userName,
            "userPhone":data.userPhone,
        //    "endDate":data.endDate
        };
        url=urlConfig.modifyRK;
    }else{
        inputdata={"id":"${emBean.msgId}",
            "rkStatus":"${emBean.rkStatus}",
            "depLevel":$("#depLevel").val(),
            "noYzCon":data.noYzCon
        };
        url=urlConfig.noChuLi;
    }
	$.ajax({
	type:"POST",
//	url:urlConfig.modifyRK,
	url:url,
	data:inputdata,
	dataType:"json",
	success:function(data){
		if(data && data.status){
	    	   alert("处理成功");
			window.location.href=window.location.href;
		}
	},
	beforeSend:function(){
	},
	error:function(){
	   alert("处理失败");
	}
	});
}

/* 受理操作 */
function toSure() {
    $('.sl-modal-con').hide();
    $('.yz-modal-con,#yzYes').show();
    $('#yzNo').hide();
}

/* 不予受理操作 */
function toReject() {
    $('.sl-modal-con').hide();
    $('.yz-modal-con,#yzNo').show();
    $('#yzYes').hide();
    $("#rk").removeClass("hide");
}

/* 确定操作 */
function yzSubmit() {
    var status = $('#yzYes').is(':hidden');
    var data = {};
    if(!status){ //受理状态
        var userName = $('#yzUserName').val();
        var userPhone = $('#yzUserPhone').val();
        //var endDate = $('#endDate').val();
        console.log('userName--',userName);
        console.log('status--',status);
        if(!userPhone || userPhone == ''){
            alert('联系方式为必填项！');
            return
        }
       /* if(!endDate || endDate == ''){
            alert('办结时限为必填项！');
            return
        }*/
        data = {userName:userName,userPhone:userPhone,status:true}
    }else{ //不受理状态
        var noYzCon = $('#noYzCon').val();
        console.log('noYzCon--',noYzCon);
        if(!noYzCon || noYzCon == ''){
            alert('拒绝理由为必填项！');
            return
        }
        data = {noYzCon:noYzCon,status:false}
    }
    console.log('data===',data)
    //调用保存方法
    go_rk(data)

//    $('.yz-modal-con').hide()
}

/* 取消操作 */
function yzCancel() {
    $(".yz-modal-con").hide();
    $("#rk").removeClass("hide");
}

$(document).ready(function(){
	$("input[name=flat-radio]").change(function(){
		userShowHide($(this).val());
		 });
	 if("${hasRK}"=='1'){
	     $('.sl-modal-con').show();
		 /*if(window.confirm("是否受理该问题？")){
             $('.yz-modal-con,#yzYes').show()
             $('#yzNo').hide()
			// go_rk();
		 }else{
             $('.yz-modal-con,#yzNo').show()
             $('#yzYes').hide()
			 $("#rk").removeClass("hide");
		 }*/
	 }else{
         $('.yz-modal-con').hide()
			$("#btn").removeAttr("disabled");
	 }
});
function userShowHide(v){
	$(".userDepDiv").addClass("hide");
	$(".userDepDiv1").addClass("hide");
	if((v=="2"||v=="3") && "${depLevel}"=="4"){
		$(".userDepDiv").removeClass("hide");
	 	$(".userDepDiv1").removeClass("hide");
	}
}

function bigMap(index){
	var opt = {
			page: { //直接获取页面指定区域的图片，他与上述异步不可共存，只能择用其一。
		        parent: '#imagetd',  //图片的父元素选择器，如'#imsbox',
		        start: index, //初始显示的图片序号，默认0
		        title: '大图' //相册标题
		    }
	};
	layer.photos(opt);
}

function formatpc(str){
	var strTime = '';
	strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
	$("#REPORT_TIME").val(strTime);
}

function formatspan(str,id){
	var strTime = '';
	if(str!=''&&str!=null){
		strTime = str.substring(0,4) + '-' + str.substring(4,6) + '-' + str.substring(6,8) + ' ' + str.substring(8,10) + ':' + str.substring(10,12) + ':' + str.substring(12,14);
		$("#"+id).html(strTime);
	}
}

function clearText(obj){
// 	if($(obj).val()=='写下您的处理意见'){
// 		$(obj).val("");
// 	}
}

function returnText(obj){
// 	if($(obj).val()==''){
// 		$(obj).val("写下您的处理意见");
// 	}
}
function closeWindow() {
// 	parent.location.reload();
	parent.onloadWindow();
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(index);	
}
function repValue(value){
	var typelist1 = typelist;
	var v = value.split(",");
	var ret = "";
	for(var i=0;i<v.length;i++){
		for(var j=0;j<typelist1.length;j++){
			if(typelist1[j].id == v[i]){
				ret += ret==""?typelist1[j].name:","+typelist1[j].name;
				break;
			}
		}
	}
	return ret;
}
$("#locationMap").click(function(){
	var loadEditIfr = function(url,title){
		layer.open({
	        type: 2,
//	        maxmin: true,
	        resize:true,
	        shadeClose: false,
	        title: title==undefined?'采集应急信息':title,
	        shade: [0.5,'#000'],
	        offset: ['25px',''],
//	        area: [(screen.width-120)+'px', '90%'],
	        area: ['800px','80%'],
//	        iframe: {src: url},
	        content : url,
	        close: function(index){
//	            if(title==undefined){
	        	onloadWindow();
//	            }
	        }
	    });
		 $("a[class='xubox_max xulayer_png32']").trigger("click");
	};
	var lng="${emBean.lng}";
	var lat="${emBean.lat}";
	
    loadEditIfr("${contextPath}"+'/emergency/goLocationMap.do?lng='+lng+'&lat='+lat+'',"查看位置");
});
</script>

</body>
</html>