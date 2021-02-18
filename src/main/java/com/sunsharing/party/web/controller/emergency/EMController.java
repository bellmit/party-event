package com.sunsharing.party.web.controller.emergency;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.ihome.air.common.ws.AirClientContext;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.dfs.Uploader;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.model.TEmLatertran;
import com.sunsharing.party.model.TEmMessage;
import com.sunsharing.party.model.TEmMessagetran;
import com.sunsharing.party.model.TEmMessageuser;
import com.sunsharing.party.model.TEmSynclog;
import com.sunsharing.party.model.TEmUser;
import com.sunsharing.party.service.EmergencyService;
import com.sunsharing.party.util.MsgSendUtil;
import com.sunsharing.party.web.controller.BaseController;
import com.sunsharing.zeus.auth.Authorizer;
import com.sunsharing.zeus.sso.session.SessionUser;

@Controller
@RequestMapping(value = "/emergency")
public class EMController extends BaseController {

	Logger logger = Logger.getLogger(EMController.class);

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/detail.do")
	public String goDetail(HttpServletRequest request, String id, Model model) throws Exception {
		logger.info("======================" + request.getParameter("later"));
		// 获取登录用户
		Map userMap = getUserByMH(request);
		if (userMap == null) {
			return "errors/nopermission";
		}
		// Map userMap = getCurUser(request);
		JSONArray depList = (JSONArray) userMap.get("depList");
		String depLevel = "";// ,depName="";
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		// get实体对象
		TEmMessage message = emergencyService.getEmMessage(id);
		System.out.println("==========================类型：" + message.getEmType());

		if (message == null) {
			logger.error("获取应急信息数据失败：" + id);
			return "errors/500";
		}

		for (int i = 0; i < depList.size(); i++) {
			JSONObject jsonObject2 = depList.getJSONObject(i);
			depLevel = jsonObject2.getString("DEP_LEVEL");
			if (message.getMsgLevel().indexOf(depLevel) != -1) {
				// depName = jsonObject2.getString("DEP_NAME");
				break;
			}
			depLevel = "";
		}

		if (message.getTranStatus().equals("0")) {
			if (("3".equals(depLevel) && "00".equals(message.getRkStatus())
					|| ("4".equals(depLevel) && "10".equals(message.getRkStatus())))) {
				model.addAttribute("hasRK", "1");
			}
		}
		// JSONObject rtnJsonObject = modifyRK(request, depLevel, message.getRkStatus(),
		// id);
		// message.setRkStatus(rtnJsonObject.getString("rkStatus"));
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("msgId", id);
		parMap.put("pageSize", "9999");
		// 查询处理流程
		Pagination page = emergencyService.queryEMTranList(parMap);
		model.addAttribute("contextPath", request.getParameter("contextPath"));
		model.addAttribute("later", request.getParameter("later"));
		model.addAttribute("emTranList", page != null ? page.getList() : null);
		List<Map<String,String>> tranList = (List<Map<String, String>>) page.getList();
		Map<String,String> lastTranMap=tranList.get(tranList.size()-1);
		String lastTran=lastTranMap.get("TRAN_STATUS");
		model.addAttribute("lastTran",lastTran);
		// 查询后续办理流程
		Pagination page1 = emergencyService.queryEMLaterTranList(parMap);
		model.addAttribute("emLaterTranList", page1 != null ? page1.getList() : null);
		// 查询图片
		page = emergencyService.queryEMImageList(parMap);
		//从在线表单问题采集上传的图片没有/static/images/路径，手动添加
		if(page!=null){
			List<JSONObject> list = (List<JSONObject>) page.getList();
			String url="";
			for(JSONObject object:list) {
				String key = "IMAGE_ADDRESS";
				String value = object.getString(key);
				if (!value.startsWith("/static/images/")) {
					object.put(key, "/static/images" + object.get(key));
				}
			}
			}

		model.addAttribute("emImgList", page != null ? page.getList() : null);
		// 查询用户
		parMap.put("userId", message.getCreator());
		page = emergencyService.queryEMUserList(parMap);
		model.addAttribute("emUser", page != null && page.getList().size() > 0 ? page.getList().get(0) : null);
		model.addAttribute("emBean", message);
		model.addAttribute("depList", depList);
		model.addAttribute("typelist", emergencyService.queryTYPE());
		model.addAttribute("mh_userName",userMap.get("mh_userName"));
		model.addAttribute("mh_mobile",userMap.get("mh_mobile"));
		if ("4".equals(depLevel)) {
			// 读取部门用户列表
			parMap.put("pageSize", "9999");
			parMap.put("depLevel", "5");
			parMap.put("userId", userMap.get("yj_userId"));
			// page = emergencyService.queryEMUserDepSMSList(parMap);
			List depUserList = emergencyService.queryEMUserDepSMSList1(parMap);
			model.addAttribute("depUserList", depUserList);
			// setDepUserList(model, page);
		}

		return "emergency/detail";
	}

	/**
	 * 审核信息详情
	 * 
	 * @param request
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/reviewDetail.do")
	public String goReviewDetail(HttpServletRequest request, String id, Model model) throws Exception {
		// 获取登录用户
		Map userMap = getUserByMH(request);
		boolean sqsh = Authorizer.existModuleCtrl(ConfigParam.app_id, ConfigParam.modle_id, "sqsh");// 社区审核权限
		boolean zjsh = Authorizer.existModuleCtrl(ConfigParam.app_id, ConfigParam.modle_id, "jdsh");// 镇街审核权限
		logger.info("获取权限传到前台判断时，社区审核权限：" + sqsh + ",镇街审核权限：" + zjsh);
		model.addAttribute("sqsh", sqsh);
		model.addAttribute("zjsh", zjsh);
		if (userMap == null) {
			return "errors/nopermission";
		}
		JSONArray depList = (JSONArray) userMap.get("depList");
		String depLevel = "";// ,depName="";
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		// get实体对象
		TEmMessage message = emergencyService.getEmMessage(id);
		System.out.println("==========================类型：" + message.getEmType());
		if (message == null) {
			logger.error("获取应急信息数据失败：" + id);
			return "errors/500";
		}

		for (int i = 0; i < depList.size(); i++) {
			JSONObject jsonObject2 = depList.getJSONObject(i);
			depLevel = jsonObject2.getString("DEP_LEVEL");
			if (message.getMsgLevel().indexOf(depLevel) != -1) {
				// depName = jsonObject2.getString("DEP_NAME");
				break;
			}
			depLevel = "";
		}

		if (message.getTranStatus().equals("0")) {
			if (("3".equals(depLevel) && "00".equals(message.getRkStatus())
					|| ("4".equals(depLevel) && "10".equals(message.getRkStatus())))) {
				model.addAttribute("hasRK", "1");
			}
		}
		// JSONObject rtnJsonObject = modifyRK(request, depLevel, message.getRkStatus(),
		// id);
		// message.setRkStatus(rtnJsonObject.getString("rkStatus"));
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("msgId", id);
		parMap.put("pageSize", "9999");
		// 查询处理流程
		Pagination page = emergencyService.queryEMTranList(parMap);
		Map<String, Object> status;
		String last_status;
		if (page != null) {// 把最後一個流程返回前台
			List a = page.getList();
			status = (Map<String, Object>) a.get(a.size() - 1);
			last_status = (String) status.get("TRAN_STATUS");
			model.addAttribute("last_status", last_status);
		}
		model.addAttribute("emTranList", page != null ? page.getList() : null);
		// 查询图片
		page = emergencyService.queryEMImageList(parMap);
		model.addAttribute("emImgList", page != null ? page.getList() : null);
		// 查询用户
		parMap.put("userId", message.getCreator());
		page = emergencyService.queryEMUserList(parMap);
		model.addAttribute("emUser", page != null && page.getList().size() > 0 ? page.getList().get(0) : null);
		model.addAttribute("emBean", message);
		model.addAttribute("depList", depList);
		model.addAttribute("typelist", emergencyService.queryTYPE());
		if ("4".equals(depLevel)) {
			// 读取部门用户列表
			parMap.put("pageSize", "9999");
			parMap.put("depLevel", "5");
			parMap.put("userId", userMap.get("yj_userId"));
			// page = emergencyService.queryEMUserDepSMSList(parMap);
			List depUserList = emergencyService.queryEMUserDepSMSList1(parMap);
			model.addAttribute("depUserList", depUserList);
			// setDepUserList(model, page);
		}

		return "emergency/reviewDetail";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setDepUserList(Model model, Pagination page) {
		if (page != null && page.getList().size() > 0) {
			List depUserList = page.getList();
			Collections.sort(depUserList, new Comparator<Map>() {

				@Override
				public int compare(Map o1, Map o2) {
					String p1 = (String) o1.get("DEP_POSITION");
					String p2 = (String) o2.get("DEP_POSITION");
					return p1.compareTo(p2);
				}
			});
			model.addAttribute("depUserList", depUserList);
		}
	}

	@RequestMapping(value = "/goLocationMap.do")
	public String goLocationMap(HttpServletRequest request, Model model) {
		String lng = request.getParameter("lng");
		String lat = request.getParameter("lat");
		model.addAttribute("lng", lng);
		model.addAttribute("lat", lat);
		return "emergency/locationMap";
	}

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/addEM.do")
	public String addEM(HttpServletRequest request, Model model, String visiteditem) throws Exception {
		// 获取登录用户
		Map userMap = getUserByMH(request);
		if (userMap == null) {
			return "errors/nopermission";
		}
		JSONArray depList = (JSONArray) userMap.get("depList");
		String depLevel = "";
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		model.addAttribute("depList", depList);
		model.addAttribute("typelist", emergencyService.queryTYPE());

		Map<String, Object> parMap = new HashMap<String, Object>();
		for (int i = 0; i < depList.size(); i++) {
			JSONObject jsonObject2 = depList.getJSONObject(i);
			depLevel = jsonObject2.getString("DEP_LEVEL");
		}
		if ("4".equals(depLevel)) {
			// 读取部门用户列表
			parMap.put("pageSize", "9999");
			parMap.put("depLevel", "5");
			parMap.put("userId", userMap.get("yj_userId"));
			Pagination page = emergencyService.queryEMUserDepSMSList(parMap);
			setDepUserList(model, page);
		}
		model.addAttribute("userMap", userMap);
		model.addAttribute("visiteditem", StringUtils.isBlank(visiteditem) ? "yjxx" : visiteditem);

		return "emergency/addEmergency";
	}

	@RequestMapping(value = "/goMapEdit.do")
	public String goMapEdit(HttpServletRequest request, Model model) throws Exception {
		return "emergency/map_editor";
	}

	@RequestMapping(value = "/goImg.do", method = RequestMethod.GET)
	public String goImg(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("imgpath", request.getParameter("imgpath"));
		return "include/goImg";
	}

	@RequestMapping(value = "/uploadPic.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject uploadPic(String fileElementId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String imgPath = DateUtils.getDBString(new Date()).substring(0, 8) + "/";
		JSONObject resultObj = Uploader.uploadFiles("yjxx", fileElementId,
				ConfigParam.fileServerJsfPath + "/" + imgPath, false, false, request);
		imgPath += resultObj.getString("newFileName");
		JSONObject rtnJsonObject = new JSONObject();
		rtnJsonObject.put("status", true);
		rtnJsonObject.put("imgPath", imgPath);
		return rtnJsonObject;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/uploadEMPic.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject uploadEMPic(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject resultObj = new JSONObject();
		Uploader loader = new Uploader(request);
		// logger.info(loader.doExec().get("status"));
		Map loadMap = loader.doExec();
		logger.info(loadMap.get("status"));
		if ((Boolean) loadMap.get("status")) {
			// 成功上传
			List files = (List) loadMap.get("data");
			if (files.size() > 0) {
				Map fileMap = (Map) files.get(0);
				String url = (String) fileMap.get("url");
				if (url != null) {
					resultObj.put("flag", "success");
					resultObj.put("src", url);
				} else {
					resultObj.put("flag", "error");
				}
			}

		} else {
			resultObj.put("flag", "error");
		}
		logger.info(resultObj);
		// ResponseHelper.printOut(response, resultObj);
		return resultObj;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/addEmergency.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject addEmergency(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 获取登录用户
		Map userMap = getUserByMH(request);
		String[] msgType = request.getParameterValues("msgType[]");
		String address = request.getParameter("address");
		String msgDesc = request.getParameter("msgDesc");
		String imgPath = request.getParameter("imgPath");
		String wxUserIds = request.getParameter("wxUserIds");
		String LNG = request.getParameter("LNG");
		String LAT = request.getParameter("LAT");
		String depLevel = (String) userMap.get("DEP_LEVEL");
		String smsFlag = (String) userMap.get("SMS_FLAG");
		TEmMessage message = new TEmMessage();
		message.setAddress(address);
		message.setCreateTime(DateUtils.getDBString(new Date()));
		message.setCreator((String) userMap.get("yj_userId"));
		String str1 = "";
		for (String str : msgType) {
			str1 += str1 == "" ? str : "," + str;
		}
		message.setEmType(str1);
		message.setLat(LAT);
		message.setLng(LNG);
		message.setMsgDesc(msgDesc);
		message.setMsgId(StringUtils.generateUUID());
		message.setMsgLevel(depLevel);
		message.setRkStatus("00");
		message.setRpStatus("00");
		message.setTranStatus("0");

		String MSG_LEVEL = "3";
		String RK_STATUS = "00";
		String RP_STATUS = "00";
		if ("4".equals(depLevel)) {// 应急办
			MSG_LEVEL = "1".equals(smsFlag) ? "4,5" : "4";
			RK_STATUS = "1".equals(smsFlag) ? "11" : "10";
			RP_STATUS = "1".equals(smsFlag) ? "11" : "01";
		} else if ("3".equals(depLevel)) {// 镇街
			MSG_LEVEL = "1".equals(smsFlag) ? "3,4" : "3";
			RK_STATUS = "1".equals(smsFlag) ? "10" : "00";
			RP_STATUS = "1".equals(smsFlag) ? "01" : "00";
		}
		message.setRkStatus(RK_STATUS);
		message.setRpStatus(RP_STATUS);
		message.setMsgLevel(MSG_LEVEL);

		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		emergencyService.saveMessage(message, imgPath);

		TEmMessagetran emMessagetran = new TEmMessagetran();
		emMessagetran.setCreateTime(DateUtils.getDBString(new Date()) + "");
		emMessagetran.setCreator(message.getCreator());
		emMessagetran.setMsgId(message.getMsgId());
		emMessagetran.setMsgRemark("采集事件");
		emMessagetran.setTranId(StringUtils.generateUUID());
		emMessagetran.setTranStatus("0");
		emMessagetran.setTranDep((String) userMap.get("DEP_NAME"));
		emMessagetran.setTranLevel(depLevel);
		emergencyService.saveEMTran(emMessagetran);

		List<Map<String, Object>> typeList = emergencyService.queryTYPE();

		if ("4".equals(depLevel) && !StringUtils.isBlank(wxUserIds)) {
			// sendLevelFour(userMap, emergencyService, "2", message.getMsgId(), wxUserIds,
			// message, typeList);
		} else if ("3".equals(depLevel)) {
			sendLevelThree(userMap, emergencyService, "2", (String) userMap.get("DEP_NAME"), emMessagetran.getCreator(),
					message, typeList);
		}

		JSONObject rtnJsonObject = new JSONObject();
		rtnJsonObject.put("status", true);
		return rtnJsonObject;
	}

	/**
	 * 不予处理问题方法
	 * @param request
	 * @param depLevel
	 * @param rkStatus
	 * @param id
	 * @param noYzCon
	 * @return
	 */
	@RequestMapping(value = "/noChuLi.do")
	public @ResponseBody JSONObject noChuLi(HttpServletRequest request, String depLevel,
											String rkStatus, String id,
											String noYzCon){
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		JSONObject rtnJsonObject = new JSONObject();
		try {
			// 获取登录用户
			Map userMap = getUserByMH(request);
			//把不予受理理由存入库
			emergencyService.addNoDealMessage(noYzCon,id);
			//新增状态表不予处理状态
			// 添加阅知记录
			TEmMessagetran emMessagetran = new TEmMessagetran();
			emMessagetran.setCreateTime(DateUtils.transFormat(new Date(), "yyyyMMddHHmmss"));
			emMessagetran.setCreator((String) userMap.get("yj_userId"));
			emMessagetran.setMsgId(id);
			emMessagetran.setMsgRemark("不予受理");
			emMessagetran.setTranId(StringUtils.generateUUID());
			emMessagetran.setTranStatus("20");
			emMessagetran.setNoDealcon(noYzCon);
			emMessagetran.setTranDep((String) userMap.get("DEP_NAME"));
			// rkStatus = "3".equals(depLevel) ? "00" : "10";
			emergencyService.modifyEMStatus("TRAN_STATUS", "2", id, emMessagetran);
			rtnJsonObject.put("status", true);
		}catch (Exception e){
			e.printStackTrace();
		}
		return rtnJsonObject;

	}

	/**
	 * 阅知流程（后续功能修改把所有阅知操作替换为处理操作。之后阅知看出处理理解）
	 * @param request
	 * @param depLevel
	 * @param rkStatus
	 * @param id
	 * @param userName
	 * @param userPhone
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/modifyRK.do")
	public @ResponseBody JSONObject modifyRK(HttpServletRequest request, String depLevel, String rkStatus, String id,
											 String userName,String userPhone,String endDate )
			throws Exception {
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		// 获取登录用户
		Map userMap = getUserByMH(request);
		JSONObject rtnJsonObject = new JSONObject();
		// 未阅知修改为阅知
		if (("3".equals(depLevel) && "00".equals(rkStatus) || ("4".equals(depLevel) && "10".equals(rkStatus)))) {
			// 添加阅知记录
			TEmMessagetran emMessagetran = new TEmMessagetran();
			emMessagetran.setCreateTime(DateUtils.transFormat(new Date(), "yyyyMMddHHmmss"));
			emMessagetran.setCreator((String) userMap.get("yj_userId"));
			emMessagetran.setMsgId(id);
			emMessagetran.setMsgRemark("已受理 处理人："+userName+",电话："+userPhone);
			emMessagetran.setTranId(StringUtils.generateUUID());
			emMessagetran.setTranStatus("1");
			emMessagetran.setTranDep((String) userMap.get("DEP_NAME"));
			rkStatus = "3".equals(depLevel) ? "01" : "11";
		    emergencyService.modifyEMStatus("RK_STATUS", rkStatus, id, emMessagetran);
			//把处理时间节点，处理人电话存入message表
			emergencyService.modifyDealPhoneANDTime(endDate, userPhone, id);
			rtnJsonObject.put("status", true);
			rtnJsonObject.put("rkStatus", rkStatus);
		}
		return rtnJsonObject;
	}

	@RequestMapping(value = "/list.do")
	public String list(Model model, HttpServletRequest request, String visiteditem) throws Exception {
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap = getUserByMH(request);
		if (userMap == null) {
			return "errors/nopermission";
		}
		model.addAttribute("userMap", userMap);

		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		String ret = hasPerssion(model, request);
		if (!StringUtils.isBlank(ret)) {
			return ret;
		}
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		setParameter(request, model);
		model.addAttribute("typelist", emergencyService.queryTYPE());
		model.addAttribute("visiteditem", StringUtils.isBlank(visiteditem) ? "yjxx" : visiteditem);
		return "emergency/list";
	}

	@RequestMapping(value = "/listMap.do")
	public String listMap(Model model, HttpServletRequest request, String visiteditem) throws Exception {
		String ret = hasPerssion(model, request);
		if (!StringUtils.isBlank(ret)) {
			return ret;
		}
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		setParameter(request, model);
		model.addAttribute("typelist", emergencyService.queryTYPE());
		model.addAttribute("visiteditem", StringUtils.isBlank(visiteditem) ? "yjxx" : visiteditem);
		return "emergency/listMap";
	}

	@SuppressWarnings("unchecked")
	private String hasPerssion(Model model, HttpServletRequest request) {
		Map<String, Object> userMap = getCurUser(request);
		if (userMap == null) {
			model.addAttribute("eType", "1");
			return "errors/404-yj";
		}
		String yjUserId = (String) userMap.get("yj_userId");
		if (StringUtils.isBlank(yjUserId)) {
			model.addAttribute("eType", "0");
			return "errors/404-yj";
		}
		model.addAttribute("userMap", userMap);
		return "";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/emergency_list.do")
	public @ResponseBody JSONObject getList(HttpServletRequest request) throws Exception {
		// 获取登录用户
		Map userMap = getUserByMH(request);
		// 获取用户的行政区划
		String xzqh = (String) userMap.get("xzqh");
		JSONArray depList = (JSONArray) userMap.get("depList");
		String depLevel = "";
		for (int i = 0; i < depList.size(); i++) {
			JSONObject jsonObject2 = depList.getJSONObject(i);
			String temp = jsonObject2.getString("DEP_LEVEL");
			if ("4".equals(temp)) {
				depLevel = "all";
				break;
			}
			depLevel += StringUtils.isBlank(depLevel) ? temp : "," + temp;
		}

		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map = setParamMap(request, map);
		// 增加用户级别条件用作筛选应急信息数据
		map.put("depLevel", depLevel);
		map.put("rpDepId", userMap.get("DEP_ID"));
		map.put("depId", userMap.get("DEP_ID"));
		map.put("xzqh", xzqh);
		Pagination page = emergencyService.queryEMList(map);

		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData(page, rtnJsonObject);
		return rtnJsonObject;

	}

	/**
	 * 获取问题审核列表
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/review_list.do")
	public @ResponseBody JSONObject getReviewList(HttpServletRequest request) throws Exception {
		// 获取登录用户
		Map userMap = getUserByMH(request);
		// 获取用户的行政区划
		String xzqh = (String) userMap.get("xzqh");
		JSONArray depList = (JSONArray) userMap.get("depList");
		String depLevel = "";
		for (int i = 0; i < depList.size(); i++) {
			JSONObject jsonObject2 = depList.getJSONObject(i);
			String temp = jsonObject2.getString("DEP_LEVEL");
			if ("4".equals(temp)) {
				depLevel = "all";
				break;
			}
			depLevel += StringUtils.isBlank(depLevel) ? temp : "," + temp;
		}

		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map = setParamMap(request, map);
		// 增加用户级别条件用作筛选应急信息数据
		map.put("depLevel", depLevel);
		map.put("rpDepId", userMap.get("DEP_ID"));
		map.put("depId", userMap.get("DEP_ID"));
		map.put("xzqh", xzqh);
		Pagination page = emergencyService.queryReviewList(map);

		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData(page, rtnJsonObject);
		return rtnJsonObject;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modifyMessage.do")
	public @ResponseBody JSONObject modifyMessage(HttpServletRequest request) throws Exception {
		Map<String, Object> userMap = getUserByMH(request);
		String yjUserId = (String) userMap.get("yj_userId");
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		String status = request.getParameter("status");
		String msgId = request.getParameter("msgId");
		String clRemark = URLDecoder.decode(request.getParameter("textjd"), "utf-8");
		String depName = URLDecoder.decode(request.getParameter("depName"), "utf-8");
		String depLevel = request.getParameter("depLevel");
		String wxUserIds = request.getParameter("wxUserIds");
		String messageType = (String) request.getParameter("messageType");
		String key = "2".equals(status) ? "RP_STATUS" : "4".equals(status) ? "TRAN_STATUS" : "";

		String time = DateUtils.transFormat(new Date(), "yyyyMMddHHmmss");
		String laterTran = request.getParameter("laterTran");
		// 修改反馈问题的类型
		emergencyService.updateMegType(msgId, messageType);
		// 添加阅知记录
		TEmMessagetran emMessagetran = new TEmMessagetran();
		emMessagetran.setCreateTime(time);
		emMessagetran.setCreator(yjUserId);
		emMessagetran.setMsgId(msgId);
		emMessagetran.setMsgRemark(clRemark);
		emMessagetran.setTranId(StringUtils.generateUUID());
		emMessagetran.setTranStatus(status);
		emMessagetran.setTranDep(depName);
		emMessagetran.setTranLevel(depLevel);
		if (!StringUtils.isBlank(key)) {// 不是续报或者阅知，上报或者办结
			// 4-办结值=1
			// 2-上报=等级3修改上报为镇街已上报01，等级4上报为应急办已上报11
			emergencyService.modifyEMStatus(key,
					"4".equals(status) ? "1" : "2".equals(status) ? "3".equals(depLevel) ? "01" : "11" : status, msgId,
					emMessagetran);
		} else {
			emergencyService.saveEMTran(emMessagetran);
		}
		if ("4".equals(status)) {// 增加是办结的情况下 添加一条默认事件已关闭记录
			emMessagetran = new TEmMessagetran();
			emMessagetran.setCreateTime((Long.valueOf(time) + 1) + "");
			emMessagetran.setCreator(yjUserId);
			emMessagetran.setMsgId(msgId);
			emMessagetran.setMsgRemark(depName + "：已处置并关闭该事件");
			emMessagetran.setTranId(StringUtils.generateUUID());
			emMessagetran.setTranStatus("6");
			emMessagetran.setTranDep(depName);
			emMessagetran.setTranLevel(depLevel);
			emergencyService.saveEMTran(emMessagetran);
		}
		// 添加后续处理
		TEmLatertran emLatertran = new TEmLatertran();
		emLatertran.setCreateTime(time);
		emLatertran.setCreator(yjUserId);
		emLatertran.setMsgId(msgId);
		emLatertran.setMsgRemark("");
		emLatertran.setTranId(StringUtils.generateUUID());
		emLatertran.setTranStatus(laterTran);
		emLatertran.setTranDep(depName);
		emLatertran.setTranLevel(depLevel);
		emergencyService.saveEMLaterTran(emLatertran);
		if ("2".equals(status) || "3".equals(status)) {// 上报和续报都需要下发信息
			// 上报的情况需要修改阅知状态
			// get实体对象
			TEmMessage message = emergencyService.getEmMessage(msgId);
			// emergencyService.modifyEMStatus("RK_STATUS", "0", msgId, null);
			if ("2".equals(status)) {// 上报才需要修改阅知，续报不需要
				// 修改阅知,等级为3修改为应急办未阅知10，否则不变
				message.setRkStatus("3".equals(depLevel) ? "10" : message.getRkStatus());
				// 处理等级
				message.setMsgLevel("3".equals(depLevel) ? "3,4" : "3,4,5");
				message.setRpUser("3".equals(depLevel) ? (String) userMap.get("DEP_ID") : message.getRpUser());
				emergencyService.modifyMessage(message);
			}
			List<Map<String, Object>> typeList = emergencyService.queryTYPE();
			// 下发信息在此处
			if ("3".equals(depLevel)) {
				sendLevelThree(userMap, emergencyService, status, depName, emMessagetran.getCreator(), message,
						typeList);
			} else if ("4".equals(depLevel) && !StringUtils.isBlank(wxUserIds)) {
				// sendLevelFour(userMap, emergencyService, status, msgId, wxUserIds, message,
				// typeList);
			}
		}
		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modifyLaterTran.do")
	public @ResponseBody JSONObject modifyLaterTran(HttpServletRequest request) throws Exception {
		Map<String, Object> userMap = getUserByMH(request);
		String yjUserId = (String) userMap.get("yj_userId");
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		String status = request.getParameter("status");
		String msgId = request.getParameter("msgId");
		String clRemark = URLDecoder.decode(request.getParameter("textjd"), "utf-8");
		String depName = URLDecoder.decode(request.getParameter("depName"), "utf-8");
		String depLevel = request.getParameter("depLevel");
		String wxUserIds = request.getParameter("wxUserIds");
		String messageType = (String) request.getParameter("messageType");
		String key = "2".equals(status) ? "RP_STATUS" : "4".equals(status) ? "TRAN_STATUS" : "";

		String time = DateUtils.transFormat(new Date(), "yyyyMMddHHmmss");
		String laterTran = request.getParameter("laterTran");
		// 添加后续处理
		TEmLatertran emLatertran = new TEmLatertran();
		emLatertran.setCreateTime(time);
		emLatertran.setCreator(yjUserId);
		emLatertran.setMsgId(msgId);
		emLatertran.setMsgRemark(clRemark);
		emLatertran.setTranId(StringUtils.generateUUID());
		emLatertran.setTranStatus(laterTran);
		emLatertran.setTranDep(depName);
		emLatertran.setTranLevel(depLevel);
		emergencyService.saveEMLaterTran(emLatertran);
		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/reviewMessage.do")
	public @ResponseBody JSONObject reviewMessage(HttpServletRequest request) throws Exception {
		boolean sqsh = Authorizer.existModuleCtrl(ConfigParam.app_id, ConfigParam.modle_id, "sqsh");// 社区审核权限
		boolean zjsh = Authorizer.existModuleCtrl(ConfigParam.app_id, ConfigParam.modle_id, "jdsh");// 镇街审核权限
		logger.info("社区审核权限：" + sqsh + ",镇街审核权限：" + zjsh);
		Map<String, Object> userMap = getUserByMH(request);
		String yjUserId = (String) userMap.get("yj_userId");
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		String lastStatus = request.getParameter("last_status");
		String status = request.getParameter("status");
		String msgId = request.getParameter("msgId");
		String clRemark = URLDecoder.decode(request.getParameter("textjd"), "utf-8");
		String depName = URLDecoder.decode(request.getParameter("depName"), "utf-8");
		String depLevel = request.getParameter("depLevel");
		String wxUserIds = request.getParameter("wxUserIds");
		int last_status = Integer.parseInt(request.getParameter("last_status"));
		String messageType = (String) request.getParameter("messageType");

		String time = DateUtils.transFormat(new Date(), "yyyyMMddHHmmss");
		// 创建流程对象
		TEmMessagetran emMessagetran = new TEmMessagetran();
		if (zjsh) {
			if (zjsh && (last_status == 7 || last_status == 8)) {// 如果有镇街审核权限且该条数据社区已经审核
				if ("0".equals(status)) {
					emMessagetran.setTranStatus("9");
				} else if ("1".equals(status)) {
					emMessagetran.setTranStatus("10");
				}
			} else {
				if (sqsh && last_status < 7) {// 如果有社区审核权限且该条数据未被审核
					if ("0".equals(status)) {
						emMessagetran.setTranStatus("7");
					} else if ("1".equals(status)) {
						emMessagetran.setTranStatus("8");
					}
					emMessagetran.setCreateTime(time);
					emMessagetran.setCreator(yjUserId);
					emMessagetran.setMsgId(msgId);
					emMessagetran.setMsgRemark(clRemark);
					emMessagetran.setTranId(StringUtils.generateUUID());
					emMessagetran.setTranDep(depName);
					emMessagetran.setTranLevel(depLevel);
					emergencyService.saveEMTran(emMessagetran);
					JSONObject rtnJsonObject = new JSONObject();
					bindReturnData("处理完成", true, rtnJsonObject);
					return rtnJsonObject;
				} else {
					JSONObject rtnJsonObject = new JSONObject();
					bindReturnData("该问题已经被社区审核或您没有审核权限", false, rtnJsonObject);
					return rtnJsonObject;
				}
			}
		} else {
			if (sqsh && last_status < 7) {// 如果有社区审核权限且该条数据未被审核
				if ("0".equals(status)) {
					emMessagetran.setTranStatus("7");
				} else if ("1".equals(status)) {
					emMessagetran.setTranStatus("8");
				}
			} else {
				JSONObject rtnJsonObject = new JSONObject();
				bindReturnData("该问题已经被社区审核或您没有审核权限", false, rtnJsonObject);
				return rtnJsonObject;
			}
		}
		emMessagetran.setCreateTime(time);
		emMessagetran.setCreator(yjUserId);
		emMessagetran.setMsgId(msgId);
		emMessagetran.setMsgRemark(clRemark);
		emMessagetran.setTranId(StringUtils.generateUUID());
		emMessagetran.setTranDep(depName);
		emMessagetran.setTranLevel(depLevel);
		emergencyService.saveEMTran(emMessagetran);
		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	/**
	 * 应急办处理发送信息代码
	 * 
	 * @param userMap
	 * @param emergencyService
	 * @param status
	 * @param msgId
	 * @param wxUserIds
	 * @param message
	 * @param typeList
	 */
	@SuppressWarnings("unchecked")
	private void sendLevelFour(Map<String, Object> userMap, EmergencyService emergencyService, String status,
			String msgId, String wxUserIds, TEmMessage message, List<Map<String, Object>> typeList) {
		Map<String, Object> mUserMap = new HashMap<String, Object>();
		mUserMap.put("msgId", msgId);
		Pagination mUserPage = emergencyService.queryMessageUserList(mUserMap);
		List<Map<String, Object>> messageUserList = mUserPage != null && mUserPage.getList().size() > 0
				? (ArrayList<Map<String, Object>>) mUserPage.getList()
				: new ArrayList<Map<String, Object>>();
		List<TEmMessageuser> userList = new ArrayList<TEmMessageuser>();
		for (String str : wxUserIds.split("@_@")) {
			TEmMessageuser user = new TEmMessageuser();
			user.setMuStatus("0");
			user.setCreateTime(DateUtils.transFormat(new Date(), "yyyyMMddHHmmss"));
			user.setCreator((String) userMap.get("yj_userId"));
			user.setMsgId(msgId);
			user.setMuId(StringUtils.generateUUID());
			user.setMuDep("");
			user.setUserId(str.split(",")[0]);
			user.setWxUserId(str.split(",")[1]);
			if (!hasMessageUser(messageUserList, user.getWxUserId())) {
				userList.add(user);
			}
			String msgTypeName = getTypes(typeList, message.getEmType());
			String content = "区应急办针对应急事件（事件类别为：" + msgTypeName + "；事件详情："
					+ (message.getMsgDesc().length() > 21 ? message.getMsgDesc().substring(0, 20) + "..."
							: message.getMsgDesc())
					+ "）向您续报了重要信息。请您通过智慧大渡口企业号进行查阅。";
			if ("2".equals(status)) {
				content = "区应急办向您上报了一条应急事件信息，事件类别为：" + msgTypeName + "；事件详情："
						+ (message.getMsgDesc().length() > 21 ? message.getMsgDesc().substring(0, 20) + "..."
								: message.getMsgDesc())
						+ "。请您通过智慧大渡口企业号进行批示。";
			}
			sendMSG(user.getWxUserId() + "@_@leader", content);
		}
		emergencyService.saveEMUserByList(userList);
	}

	/**
	 * 街镇、主管部门处理发送信息代码
	 * 
	 * @param userMap
	 * @param emergencyService
	 * @param status
	 * @param depName
	 * @param userId
	 * @param message
	 * @param typeList
	 */
	@SuppressWarnings("unchecked")
	private void sendLevelThree(Map<String, Object> userMap, EmergencyService emergencyService, String status,
			String depName, String userId, TEmMessage message, List<Map<String, Object>> typeList) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("userId", userId);
		parMap.put("depLevel", "4");
		Pagination userPage = emergencyService.queryEMUserDepSMSList(parMap);
		if (userPage != null && userPage.getList().size() > 0) {
			// Map<String, Object> smsUserMap = (HashMap<String,
			// Object>)userPage.getList().get(0);
			for (Map<String, Object> smsUserMap : (List<Map<String, Object>>) userPage.getList()) {
				String msgTypeName = getTypes(typeList, message.getEmType());
				String content = depName + userMap.get("userName") + "针对应急事件（事件类别为：" + msgTypeName + "；事件详情："
						+ (message.getMsgDesc().length() > 21 ? message.getMsgDesc().substring(0, 20) + "..."
								: message.getMsgDesc())
						+ "）向您续报了重要信息。请您通过智慧大渡口企业号进行查阅。";
				if ("2".equals(status)) {
					content = depName + userMap.get("userName") + "上报了一条应急事件信息，事件类别为：" + msgTypeName + "；事件详情："
							+ (message.getMsgDesc().length() > 21 ? message.getMsgDesc().substring(0, 20) + "..."
									: message.getMsgDesc())
							+ "。请您及时登录应急信息采集系统进行处置。";
				}
				sendMSG((String) smsUserMap.get("WX_USER_ID"), content);
			}

		} else {
			logger.warn("查询用户数据失败:" + parMap);
		}
	}

	private String getTypes(List<Map<String, Object>> typeList, String typeId) {
		String[] types = typeId.split(",");
		String retType = "";
		for (String type : types) {
			for (Map<String, Object> map : typeList) {
				if (type.equals(map.get("TYPE_ID"))) {
					retType = retType == "" ? map.get("TYPE_NAME").toString() : "," + map.get("TYPE_NAME");
					continue;
				}
			}
		}
		return retType;
	}

	/**
	 * 判断已有的领导数据中是否有记录
	 * 
	 * @param messageUserList
	 * @param wxUserId
	 * @return
	 */
	private boolean hasMessageUser(List<Map<String, Object>> messageUserList, String wxUserId) {
		for (Map<String, Object> map : messageUserList) {
			if (wxUserId.equals(map.get("WX_USER_ID"))) {
				return true;
			}
		}
		return false;
	}

	private Map<String, Object> setParamMap(HttpServletRequest request, Map<String, Object> map) {
		map.put("em_type", request.getParameter("em_type"));
		map.put("rk_status", request.getParameter("rk_status"));
		map.put("rp_status", request.getParameter("rp_status"));
		map.put("tran_status", request.getParameter("tran_status"));
		map.put("msg_desc", request.getParameter("msg_desc"));
		map.put("pageNo", request.getParameter("currentPage"));
		map.put("pageSize", request.getParameter("linesPerPage"));
		map.put("isMap", request.getParameter("isMap"));
		map.put("msg_title", request.getParameter("msg_title"));
		map.put("msg_source", request.getParameter("msg_source"));
		map.put("msg_name", request.getParameter("msg_name"));
		map.put("startTime", request.getParameter("startTime"));
		map.put("endTime", request.getParameter("endTime"));
		return map;
	}

	public void setParameter(HttpServletRequest request, Model model) {
		model.addAttribute("em_type", request.getParameter("em_type"));
		model.addAttribute("rk_status", request.getParameter("rk_status"));
		model.addAttribute("rp_status", request.getParameter("rp_status"));
		model.addAttribute("tran_status", request.getParameter("tran_status"));
		model.addAttribute("msg_desc", request.getParameter("msg_desc"));
	}

	/**
	 * 下发信息方法
	 * 
	 * @return
	 */
	private String sendMSG(String wxUserId, String content) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("userId", wxUserId);
		parMap.put("content", content);
		logger.info("下发信息接口调用：" + parMap);
		boolean b = MsgSendUtil.MsgSend(parMap);
		return b ? "ok" : "error";
	}

	/***************************************
	 * 用户模块
	 ********************************************************/

	/**
	 * 用户界面
	 * 
	 * @param model
	 * @param request
	 * @param visiteditem
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/go_userList.do")
	public String goUserList(Model model, HttpServletRequest request, String visiteditem) throws Exception {
		String ret = hasPerssion(model, request);
		if (!StringUtils.isBlank(ret)) {
			return ret;
		}
		model.addAttribute("visiteditem", StringUtils.isBlank(visiteditem) ? "yjxxu" : visiteditem);
		return "emergency/userList";
	}

	/**
	 * 访问用户审核列表
	 */
	@RequestMapping(value = "/reviewList.do")
	public String reviewList(Model model, HttpServletRequest request, String visiteditem) throws Exception {
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap = getUserByMH(request);
		if (userMap == null) {
			return "errors/nopermission";
		}
		model.addAttribute("userMap", userMap);

		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		String ret = hasPerssion(model, request);
		if (!StringUtils.isBlank(ret)) {
			return ret;
		}
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		setParameter(request, model);
		model.addAttribute("typelist", emergencyService.queryTYPE());
		model.addAttribute("visiteditem", StringUtils.isBlank(visiteditem) ? "yjxx" : visiteditem);
		return "emergency/reviewList";
	}

	@RequestMapping(value = "/userDetail.do")
	public String userDetail(HttpServletRequest request, Model model) throws Exception {
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		// model.addAttribute("opType", request.getParameter("opType"));
		if (!StringUtils.isBlank(request.getParameter("id"))) {
			map.put("userId", request.getParameter("id"));
			Pagination page = emergencyService.queryEMUserDepList(map);
			if (page != null && page.getList().size() > 0) {
				model.addAttribute("userInfo", page.getList().get(0));
			}
		}
		model.addAttribute("tempUserId", StringUtils.generateUUID());
		model.addAttribute("depList", emergencyService.getDepForList(map));
		return "emergency/userDetail";
	}

	@RequestMapping(value = "/getUserList.do")
	public @ResponseBody JSONObject getUserList(HttpServletRequest request) throws Exception {
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo", request.getParameter("currentPage"));
		map.put("pageSize", request.getParameter("linesPerPage"));
		map.put("search", request.getParameter("search"));

		Pagination page = emergencyService.queryEMUserDepList(map);
		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData(page, rtnJsonObject);
		return rtnJsonObject;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getWXUserList.do")
	public @ResponseBody JSONObject getWXUserList(HttpServletRequest request) throws Exception {
		Map userMap = getUserByMH(request);
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		TEmSynclog synclog = new TEmSynclog();
		synclog.setCreateTime(DateUtils.getDBString(new Date()));
		synclog.setCreator((String) userMap.get("yj_userId"));
		synclog.setLogDesc(userMap.get("userName") + "在" + DateUtils.getDisplay(synclog.getCreateTime()) + "进行微信用户同步");
		synclog.setLogId(StringUtils.generateUUID());
		synclog.setLogRemark("");
		synclog.setLogStatus("0");
		synclog.setLogType("1");
		synclog.setTargetId(request.getParameter("userId"));
		if (StringUtils.isBlank(synclog.getTargetId())) {
			synclog.setTargetId(request.getParameter("tempUserId"));
		}
		emergencyService.saveSyncLog(synclog);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "getWXUser");
		jsonObject.put("value", synclog.getLogId());
		MsgSendUtil.MsgSend("WX_QYHMSG", jsonObject);

		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	/**
	 * 判断是否有同步日志未返回
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/hasWXUser.do")
	public @ResponseBody JSONObject hasWXUser(HttpServletRequest request) throws Exception {
		Map<String, Object> userMap = getUserByMH(request);
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("targetId", request.getParameter("userId"));
		// if(StringUtils.isBlank((String)map.get("targetId"))){
		// map.put("targetId", request.getParameter("tempUserId"));
		// }
		map.put("userId", userMap.get("yj_userId"));
		map.put("logType", "1");
		map.put("logStatus", "0");
		List<Map<String, Object>> list = emergencyService.querySyncLogForList(map);
		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData("处理完成", list == null || list.size() == 0 ? false : true, rtnJsonObject);
		return rtnJsonObject;
	}

	/**
	 * 查询用户是否被绑定
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/hasUserBind.do")
	public @ResponseBody JSONObject hasUserBind(HttpServletRequest request) throws Exception {
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		String userId = request.getParameter("userId");
		String targetType = request.getParameter("targetType");
		map.put("mh".equals(targetType) ? "mhUser" : "wx".equals(targetType) ? "wxUser" : "userName",
				request.getParameter("targetId"));
		Pagination page = emergencyService.queryEMUserList(map);
		boolean b = false;
		if (page != null && page.getList().size() > 0) {
			List<Map<String, Object>> list = (ArrayList<Map<String, Object>>) page.getList();
			for (Map<String, Object> uMap : list) {
				if (uMap.get("USER_ID").equals(userId)) {
					break;
				}
				b = true;
			}
		}
		JSONObject rtnJsonObject = new JSONObject();

		bindReturnData("处理完成", b, rtnJsonObject);
		return rtnJsonObject;
	}

	@RequestMapping(value = "/modifyUser.do")
	public @ResponseBody JSONObject modifyUser(HttpServletRequest request) throws Exception {
		// Map<String, Object> userMap = getCurUser(request);
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String mobile = request.getParameter("mobile");
		String certId = request.getParameter("certId");
		String units = request.getParameter("units");
		String depId = request.getParameter("depId");
		String depPosition = request.getParameter("depPosition");
		String wxUserId = request.getParameter("wxUserId");
		String mhUserId = request.getParameter("mhUserId");
		String tempUserId = request.getParameter("tempUserId");
		String sms_flag = request.getParameter("sms_flag");
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		TEmUser emUser = new TEmUser();
		if (!StringUtils.isBlank(userId)) {
			emUser = emergencyService.getTuser(userId);
		}
		emUser.setCertId(certId);
		emUser.setUserId(userId);
		emUser.setUserName(userName);
		emUser.setMobile(mobile);
		emUser.setWorkUnits(units);
		emUser.setWxUserId(wxUserId);
		emUser.setMhUser(mhUserId);
		emUser.setStatus("1");
		emUser.setSmsFlag("1".equals(sms_flag) ? sms_flag : "0");

		emergencyService.saveEMUser(emUser, tempUserId, depId, depPosition);

		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	@RequestMapping(value = "/getUser.do")
	public @ResponseBody JSONObject getUser(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if ("mh".equals(request.getParameter("getType"))) {
			list = emergencyService.querySQUserForList(map);
		} else if ("wx".equals(request.getParameter("getType"))) {
			list = emergencyService.queryWXUserForList(map);
		}

		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData(list, rtnJsonObject);
		return rtnJsonObject;
	}

	@RequestMapping(value = "/delUser.do")
	public @ResponseBody JSONObject delUser(HttpServletRequest request, String userId) throws Exception {
		// EmergencyService emergencyService =
		// (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData("处理完成", emergencyService.delUser(userId) > 0 ? true : false, rtnJsonObject);
		return rtnJsonObject;
	}

	public Map<String, Object> getUserByMH(HttpServletRequest request) {
		logger.info("===========开始根据门户账号获取用户信息");
		SessionUser sessionUser = SessionUser.load();
		String mhUser = sessionUser.getLoginId();
		String xzqh = sessionUser.getXzqh();
		logger.info("===========获取到的门户账号：" + mhUser);
		/*
		 * EmergencyService emergencyService =
		 * (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		 */
		EmergencyService emergencyService = AirClientContext.getBean(EmergencyService.class);
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("mhUser", mhUser);
		Pagination page = emergencyService.queryEMUserList(parMap);
		Map<String, Object> userMap = new HashMap<String, Object>();
		if (page != null && page.getList().size() > 0) {
			// logger.error("登陆错误：没有关联的应急用户.");
			// return "errors/500";
			System.out.println(page.getList().get(0).toString());

			Map<String, Object> map = (Map<String, Object>) page.getList().get(0);
			userMap.put("yj_userId", map.get("USER_ID"));
			userMap.put("yj_userType", map.get("USER_TYPE"));
			userMap.put("xzqh", xzqh);
			userMap.put("mh_userName",map.get("USER_NAME"));
			userMap.put("mh_mobile",map.get("MOBILE"));
			String userId = (String) userMap.get("yj_userId");
			parMap.put("userId", userId);
			page = emergencyService.queryEMUserDepList(parMap);
			List<Map<String, Object>> depList = (ArrayList<Map<String, Object>>) page.getList();
			JSONArray array = new JSONArray();
			for (Map<String, Object> depMap : depList) {
				JSONObject jsonObject2 = new JSONObject();
				jsonObject2.put("DEP_ID", depMap.get("DEP_ID"));
				jsonObject2.put("DEP_NAME", depMap.get("DEP_NAME"));
				jsonObject2.put("DEP_LEVEL", depMap.get("DEP_LEVEL"));
				jsonObject2.put("DEP_PARENT", depMap.get("DEP_PARENT"));
				array.add(jsonObject2);
				userMap.put("DEP_ID", depMap.get("DEP_ID"));
				userMap.put("DEP_NAME", depMap.get("DEP_NAME"));
				userMap.put("DEP_LEVEL", depMap.get("DEP_LEVEL"));
				userMap.put("DEP_PARENT", depMap.get("DEP_PARENT"));
				userMap.put("SMS_FLAG", depMap.get("SMS_FLAG"));
			}
			userMap.put("depList", array);
			saveSessionUser(request, userMap);
		} else {
			logger.info("根据门户账号未获取到用户信息！");
			return null;
		}
		return userMap;
	}
}
