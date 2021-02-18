package com.sunsharing.party.web.controller.emergency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.ServiceLocator;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.service.EmAnalyzeService;
import com.sunsharing.party.service.EmergencyService;
import com.sunsharing.party.web.controller.BaseController;

@Controller
@RequestMapping(value = "emac")
public class EMAnalyzeController extends BaseController {
	
	Logger logger = Logger.getLogger(EMAnalyzeController.class);
	
	@RequestMapping(value = "/listMap.do")
	public String listMap(Model model, HttpServletRequest request, String visiteditem) throws Exception {
		EmergencyService emergencyService = (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmAnalyzeService emAnalyzeService = (EmAnalyzeService)ServiceLocator.getBeanByClass(EmAnalyzeService.class);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		List<Map<String, Object>> analyzeConf = emAnalyzeService.queryAnalyzeConf(paramMap);
		
		model.addAttribute("analyzeConfInfo", analyzeConf);
		
		model.addAttribute("typelist", emergencyService.queryTYPE());
		model.addAttribute("visiteditem", StringUtils.isBlank(visiteditem) ? "yjxx" : visiteditem);
		return "emergency/acListMap";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getEmAnalyzeObject.do")
	public @ResponseBody JSONObject getEmAnalyzeObject(HttpServletRequest request) throws Exception {
		String AC_ID = request.getParameter("AC_ID");
		String msgId = request.getParameter("msgId");
		String radiusDefault = request.getParameter("radiusDefault");// 半径
		
		EmergencyService emergencyService = (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
		EmAnalyzeService emAnalyzeService = (EmAnalyzeService)ServiceLocator.getBeanByClass(EmAnalyzeService.class);
		
		// 获取应急相关信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msgId", msgId);
		Pagination queryEMList = emergencyService.queryEMList(map);
		
		// 获取统计数据
		Map<String, String> paramMap = new HashMap<String, String>();
		Map em_map = (Map)queryEMList.getList().get(0);
		String LNG = (String)em_map.get("LNG");
		String LAT = (String)em_map.get("LAT");
		
		paramMap.put("LNG", LNG);
		paramMap.put("LAT", LAT);
		paramMap.put("radiusDefault", radiusDefault);
		
		paramMap.put("AC_ID", AC_ID);
		List<Map<String, Object>> emAnalyzeObject = emAnalyzeService.getEMAnalyzeInfoByConfSQL(paramMap);
		
		JSONObject rtnJsonObject = new JSONObject();
		rtnJsonObject.put("EMList", queryEMList.getList());
		rtnJsonObject.put("emAnalyzeObject", emAnalyzeObject);
		return rtnJsonObject;
	}
	
	@RequestMapping(value = "/getDisasterType.do")
	@ResponseBody
	public String getDisasterType(HttpServletRequest request, HttpServletResponse response) {
		String DisasterType = request.getParameter("DisasterType");
		EmAnalyzeService emAnalyzeService = (EmAnalyzeService)ServiceLocator.getBeanByClass(EmAnalyzeService.class);
		String ConfText = emAnalyzeService.getSMSCONF(DisasterType);
		logger.info("配置内容:" + ConfText);
		return ConfText;
	}
}
