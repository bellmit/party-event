package com.sunsharing.party.web.controller.emergency;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.ServiceLocator;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.model.TEmDepartment;
import com.sunsharing.party.service.EmergencyService;
import com.sunsharing.party.web.controller.BaseController;

@Controller
@RequestMapping(value = "de")
public class DEController extends BaseController {

	Logger logger = Logger.getLogger(DEController.class);

	@RequestMapping(value = "/detail.do")
	public String goDetail(HttpServletRequest request, String id, Model model, String isedit) throws Exception {
		// 根据id查询部门信息
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		TEmDepartment tEmDepartment = emergencyService.getDepartment(id);
		// 获得上级部门id
		map.put("dep_id", id);
		map.put("DEP_ID", tEmDepartment.getDepParent());
		model.addAttribute("userInfo", map);
		model.addAttribute("depList", emergencyService.getDepForList(map));
		Pagination page = emergencyService.getDepList(map);
		model.addAttribute("dept", page != null && page.getList().size() > 0 ? page.getList().get(0) : null);
		if (!StringUtils.isBlank(isedit)) {
			return "emergency/depMod";
		}
		return "emergency/depDetail";
	}

	@RequestMapping(value = "/list.do")
	public String List(Model model, HttpServletRequest request, String visiteditem) throws Exception {
		model.addAttribute("visiteditem", StringUtils.isBlank(visiteditem) ? "yjxxde" : visiteditem);
		return "emergency/depList";
	}

	@RequestMapping(value = "/go_addDepartment.do")
	public String go_addDepartment(Model model, HttpServletRequest request) throws Exception {
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		// List<TEmDepartment> list=emergencyService.findAll();
		// StringBuffer sb = new StringBuffer("[");
		// for (TEmDepartment tEmDepartment : list) {
		// //String dep_id=tEmDepartment.getDepId();
		// String dep_name=tEmDepartment.getDepName();
		// //String dep_parent="0";
		// int dep_id = Integer.parseInt(tEmDepartment.getDepId());
		// Integer dep_parent=0;
		// if(tEmDepartment.getDepParent()!=null){
		// //dep_parent=tEmDepartment.getDepParent();
		// dep_parent=Integer.parseInt(tEmDepartment.getDepParent());
		// }
		// sb.append("{dep_id:"+dep_id+",dep_parent:"+dep_parent+",dep_name:'"+dep_name+"'},");
		// String result=sb.substring(0,sb.length()-1)+"]";
		// model.addAttribute("result",result);
		// //System.out.println(result);
		// }
		// 这个是设置选中的等于是修改的时候使用
		model.addAttribute("depList", emergencyService.getDepForList(map));
		return "emergency/addDepartment";
	}

	@RequestMapping(value = "/de_list.do")
	public @ResponseBody JSONObject getList(HttpServletRequest request) throws Exception {
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map = setParamMap(request, map);
		Pagination page = emergencyService.getDepList(map);
		JSONObject rtnJsonObject = new JSONObject();
		bindReturnData(page, rtnJsonObject);
		return rtnJsonObject;
	}

	@RequestMapping(value = "/checkAddepName.do")
	public @ResponseBody JSONObject checkAddepName(HttpServletRequest request) throws Exception {
		String dep_name = request.getParameter("dep_name");
		Map<String, Object> map = new HashMap<String, Object>();
		map = setParamMap(request, map);
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		JSONObject rtnJsonObject = new JSONObject();
		if (!StringUtils.isBlank(dep_name)) {
			int count = emergencyService.checkName(dep_name);
			if (count == 1) {
				bindReturnData("处理完成", false, rtnJsonObject);
				return rtnJsonObject;
			}
		}
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	@RequestMapping(value = "/checkAddepNo.do")
	public @ResponseBody JSONObject checkAddepNo(HttpServletRequest request) throws Exception {
		String dep_no = request.getParameter("dep_no");
		Map<String, Object> map = new HashMap<String, Object>();
		map = setParamMap(request, map);
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		JSONObject rtnJsonObject = new JSONObject();
		if (!StringUtils.isBlank(dep_no)) {
			int count = emergencyService.checkNo(dep_no);
			if (count == 1) {
				bindReturnData("处理完成", false, rtnJsonObject);
				return rtnJsonObject;
			}
		}
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	@RequestMapping(value = "/addDepartment.do")
	public @ResponseBody JSONObject addDepartment(HttpServletRequest request) throws Exception {
		String dep_name = request.getParameter("dep_name");
		String dep_parent = request.getParameter("dep_parent");
		String dep_level = request.getParameter("dep_level");
		String dep_no = request.getParameter("dep_no");
		Map<String, Object> map = new HashMap<String, Object>();
		map = setParamMap(request, map);
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		JSONObject rtnJsonObject = new JSONObject();
		if (!StringUtils.isBlank(dep_name)) {
			int count = emergencyService.checkName(dep_name);
			if (count == 1) {
				bindReturnData("处理完成", false, rtnJsonObject);
				return rtnJsonObject;
			}
		}
		if (!StringUtils.isBlank(dep_no)) {
			int count = emergencyService.checkNo(dep_no);
			if (count == 1) {
				bindReturnData("处理完成", false, rtnJsonObject);
				return rtnJsonObject;
			}
		}
		// 添加TEmDepartment对象
		TEmDepartment tEmDepartment = new TEmDepartment();
		tEmDepartment.setDepId(dep_no);
		// tEmDepartment.setDepId(StringUtils.generateUUID());
		tEmDepartment.setDepName(dep_name);
		tEmDepartment.setDepLevel(dep_level);
		tEmDepartment.setDepParent(dep_parent);
		tEmDepartment.setDepstatus("1");
		emergencyService.saveDepartment(tEmDepartment);
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	@RequestMapping(value = "/checkdepName.do")
	public @ResponseBody JSONObject checkdepName(HttpServletRequest request) throws Exception {
		String dep_name = request.getParameter("dep_name");
		String dname = request.getParameter("dname");
		Map<String, Object> map = new HashMap<String, Object>();
		map = setParamMap(request, map);
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		JSONObject rtnJsonObject = new JSONObject();
		if (!(dep_name.equals(dname))) {
			int count = emergencyService.checkName(dep_name);
			if (count == 1) {
				bindReturnData("处理完成", false, rtnJsonObject);
				return rtnJsonObject;
			}
		}
		bindReturnData("处理完成", true, rtnJsonObject);
		return rtnJsonObject;
	}

	@RequestMapping(value = "/modDepartment.do")
	public @ResponseBody JSONObject modDepartment(HttpServletRequest request) throws Exception {
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		JSONObject rtnJsonObject = new JSONObject();
		String dep_name = request.getParameter("dep_name");
		String dep_parent = request.getParameter("dep_parent");
		String dep_level = request.getParameter("dep_level");
		String oldDepLevel = request.getParameter("oldDepLevel");
		String dname = request.getParameter("dname");
		String dparent = request.getParameter("dparent");
		// 当前部门的dep_id值
		// 预修改TEmDepartment对象
		String did = request.getParameter("did");
		TEmDepartment tEmDepartment = emergencyService.getDepartment(did);
		tEmDepartment.setDepName(dep_name);
		tEmDepartment.setDepLevel(dep_level);
		tEmDepartment.setDepParent(dep_parent);
		tEmDepartment.setDepstatus("1");
		// 如果部门名称不改变,上级部门不改变或者是平级修改,修改部门成功
		if (dep_name.equals(dname) && dep_level.equals(oldDepLevel)) {
			// 修改TEmDepartment对象
			emergencyService.modifyDepartment(tEmDepartment);
			bindReturnData("处理完成", true, rtnJsonObject);
			return rtnJsonObject;
		} else if (dep_name.equals(dname) && !dep_level.equals(oldDepLevel)) {
			// 如果部门名称不改变,上级部门改变,修改部门成功 并将所有的下属部门级别+1
			emergencyService.modifyDepartment(tEmDepartment);
			emergencyService.modifyBeforeDep(did);
			bindReturnData("处理完成", true, rtnJsonObject);
			return rtnJsonObject;
		}
		// 如果 部门名称改变,从数据库中验证是否存在
		int count = emergencyService.checkName(dep_name);
		// 部门名称存在
		if (count != 0) {
			bindReturnData("处理完成", false, rtnJsonObject);
			return rtnJsonObject;
		} else if (count == 0 && dep_level.equals(oldDepLevel)) {
			// 名称不存在 且没有修改上级部门或者是平级修改,那就只修改部门,否则就修改该部门及下属部门级别+1
			// 修改TEmDepartment对象
			emergencyService.modifyDepartment(tEmDepartment);
			bindReturnData("处理完成", true, rtnJsonObject);
			return rtnJsonObject;
		} else {
			// 名称不存在 而且是向上修改 那就只修改部门以及修改该部门及下属部门级别+1
			emergencyService.modifyDepartment(tEmDepartment);
			// 根据id修改上级部门时 同时将所有下级部门等级+1
			emergencyService.modifyBeforeDep(did);
			bindReturnData("处理完成", true, rtnJsonObject);
			return rtnJsonObject;
		}

	}

	@RequestMapping(value = "/deldep.do")
	public @ResponseBody JSONObject delzpxx(HttpServletResponse response, HttpServletRequest request, ModelMap model,
			String id) {
		EmergencyService emergencyService = (EmergencyService) ServiceLocator.getBeanByClass(EmergencyService.class);
		JSONObject rtnJsonObject = new JSONObject();
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("depParent", id);
		parMap.put("depId", id);
		// 根据id查询部门是否有下级部门
		Pagination page = emergencyService.getDepList(parMap);
		// 根据部门id查询该部门是否存在用户
		int count = emergencyService.getDepUsers(parMap);
		// 查询下级部门的用户
		Pagination userPage = emergencyService.getDepUser(parMap);
		// nextCount为下级部门的用户数
		int nextCount = 0;
		if (userPage != null && userPage.getList().size() > 0) {
			if (!StringUtils.isBlank((String) ((Map<String, Object>) userPage.getList().get(0)).get("TUSERID"))) {
				nextCount = 1;
			}

		}
		// 无下级部门且该部门不存在用户
		if (page == null || page.getList().size() == 0 && count == 0) {
			emergencyService.delDepartment(id);
			bindReturnData("处理完成", true, rtnJsonObject);
		} else if (page == null || page.getList().size() == 0 && count > 0) {
			// 无下级部门但该部门存在用户
			bindReturnData("-1", false, rtnJsonObject);
		} else if (nextCount == 0) {
			// 有下级部门 但 下级部门不存在用户
			bindReturnData("-2", false, rtnJsonObject);
		} else {
			// 存在下级部门 但下级部门有用户存在
			bindReturnData("-3", false, rtnJsonObject);
		}
		return rtnJsonObject;
	}

	private Map<String, Object> setParamMap(HttpServletRequest request, Map<String, Object> map) {
		map.put("dep_name", request.getParameter("dep_name"));
		map.put("pageSize", request.getParameter("linesPerPage"));
		map.put("pageNo", request.getParameter("currentPage"));
		return map;
	}

}
