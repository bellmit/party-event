package com.sunsharing.party.web.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.ServiceLocator;
import com.sunsharing.party.common.weixin.WeiXinApi;
import com.sunsharing.party.common.weixin.WeiXinFactory;
import com.sunsharing.party.constant.CacheConstant;
import com.sunsharing.party.constant.Constant;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.service.EmergencyService;
import com.sunsharing.party.service.StatisticsDDKService;
import com.sunsharing.party.util.CacheUtil;
import com.sunsharing.party.util.HttpSendShort;
import com.sunsharing.zeus.sso.session.SessionUser;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {
	
	Logger logger = Logger.getLogger(LoginController.class);
	
	/**
	 * 到授权页获取授权
	 * @author wxl 2017年11月24日 下午2:40:04
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/goAuthorization.do")
	public String goAuthorization(Model model, HttpServletRequest request) {
		logger.info("开始执行授权操作...");
		final String state = request.getParameter("state");
		// state代表需要访问的具体功能模块码值 可以直接跟路径进行拼接
		model.addAttribute("state", state);
		model.addAttribute("weixinAppId", ConfigParam.CorpID);
		// 微信类型 企业号(QYH) or公众号(GZH)
		model.addAttribute("type", Constant.QYH);
		final String op = request.getParameter("op");
		if (!StringUtils.isBlank(op) && "openId".equals(op)) {// 代表是公众号
			model.addAttribute("weixinAppId", ConfigParam.weixinAppId);
			// 微信类型 企业号(QYH) or公众号(GZH)
			model.addAttribute("type", Constant.GZH);
		}
		return "weixin/authorization";
	}
	
	/**
	 * 企业号获取微信用户信息
	 * @author hanjianbo 2016年11月10日 上午11:07:10
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getWeiXinUserInfo.do", method = RequestMethod.GET)
	public String getWeiXinUserInfo(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("企业号开始执行获取用户信息操作...");
		final String action = request.getParameter("state");
		final Map<String, String> map = getCookies(request);
		final String stat = map.get("stat");
		// final String Secret = map.get("sec");
		// final String CorpID = map.get("cor");
		final String CorpID = ConfigParam.CorpID;
		final String Secret = ConfigParam.Secret;
		logger.info("获取到的请求数据为:stat=" + stat + ",Secret=" + Secret + ",CorpID=" + CorpID);
		logger.info("企业号- 请求url：" + request.getQueryString());
		logger.info("企业号 - action：" + action);
		if (StringUtils.isBlank(action)) {
			model.addAttribute("errors", "您没有权限访问当前项目");
			return "errors/nopermission";
		}
		String wx_UserId = "";
		final String code = request.getParameter("code");
		if (StringUtils.isBlank(request.getParameter("wx_UserId")) && !StringUtils.isBlank(code)) {
			logger.info("企业号 - code ：" + code);
			final WeiXinApi weiXinQyh = WeiXinFactory.init("QYH");
			final JSONObject userInfo = weiXinQyh.getUserInfoByCode(code, CorpID, Secret);
			logger.error("企业号- 微信授权反馈结果:" + userInfo.toJSONString());
			if (!userInfo.containsKey("DeviceId")) {
				model.addAttribute("errors", "您没有权限访问当前项目");
				return "errors/nopermission";
			}
			wx_UserId = userInfo.getString("UserId");
			if (StringUtils.isBlank(wx_UserId)) {
				logger.info("该用户为非企业成员！");
				wx_UserId = Constant.TEST_ID;
			}
		}
		if (!StringUtils.isBlank(request.getParameter("wx_UserId"))) {
			wx_UserId = request.getParameter("wx_UserId");
		}
		setRefferCookie(request, response, CacheConstant.MEMCACHE_WEIXIN_ID, wx_UserId);
		logger.info("设置cookie wx_UserId:" + wx_UserId);
		CacheUtil.saveCacheByPriod(CacheConstant.MEMCACHE_WEIXIN_ID + wx_UserId, wx_UserId, "300");
		String url = (String)CacheUtil.getCache(CacheConstant.USER_REQUEST_URL + action);
		if (!StringUtils.isBlank(url)) {
			if (url.indexOf("?") == -1) {
				url += "?1=1";
			}
			return "redirect:" + url;
		}
		model.addAttribute("errors", "您没有权限访问当前项目");
		return "errors/nopermission";
	}
	
	/**
	 * 公众号通过openId获取用户信息
	 * @author wxl 2017年11月24日 下午3:32:16
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUserByOpenId", method = RequestMethod.GET)
	public String getUserByOpenId(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("公众号开始执行获取用户信息操作...");
		final String code = request.getParameter("code");
		final String action = request.getParameter("state");
		logger.info("公众号 - action：" + action);
		if (StringUtils.isBlank(action)) {
			model.addAttribute("errors", "您没有权限访问当前项目");
			return "errors/nopermission";
		}
		String openId = "";
		if (StringUtils.isBlank(request.getParameter("openId")) && !StringUtils.isBlank(code)) {
			// 获取openid,
			final WeiXinApi weiXinGzh = WeiXinFactory.init("GZH");
			logger.info("公众号获取用户信息的appid:" + ConfigParam.weixinAppId + " secretkey：" + ConfigParam.weixinSecretKey + " code: "
			        + code);
			final JSONObject jobj = weiXinGzh.getOpenIdByCode(ConfigParam.weixinAppId, ConfigParam.weixinSecretKey, code);
			if (!jobj.containsKey("openid")) {
				model.addAttribute("errors", "您没有权限访问当前项目");
				return "errors/nopermission";
			}
			openId = jobj.getString("openid");
			if (StringUtils.isBlank(openId)) {
				openId = Constant.TEST_ID;
			}
		}
		if (!StringUtils.isBlank(request.getParameter("wx_UserId"))) {
			openId = request.getParameter("wx_UserId");
		}
		// 设置cookie
		setRefferCookie(request, response, CacheConstant.MEMCACHE_WEIXIN_ID, openId);
		logger.info("设置cookie openId:" + openId);
		CacheUtil.saveCacheByPriod(CacheConstant.MEMCACHE_WEIXIN_ID + openId, openId, "300");
		String url = (String)CacheUtil.getCache(CacheConstant.USER_REQUEST_URL + action);
		if (!StringUtils.isBlank(url)) {
			if (url.indexOf("?") == -1) {
				url += "?1=1";
			}
			return "redirect:" + url;
		}
		model.addAttribute("errors", "您没有权限访问当前项目");
		return "errors/nopermission";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/jump", method = RequestMethod.GET)
	public String jump(Model model, HttpServletRequest request) throws Exception {
		// getIp2(request);
		String token = request.getParameter("token");
		String action = request.getParameter("action");
		String v = request.getParameter("v");
		v = "2.0";
		if (StringUtils.isBlank(token) && "2.0".equals(v)) {
			logger.warn("跳转链接无参数token");
			return "errors/404";
		}
		try {
			String url = MessageFormat.format(ConfigParam.shglLegendUrl, token);
			if (!url.toLowerCase().startsWith("http")) {
				url = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + url;
			}
			String ret = getLocalJson();
			if (getIp2(request).indexOf("192.168.1") == -1) {
				if ("2.0".equals(v)) {
					ret = HttpSendShort.sendByHttpClient("", url, "");
				} else {
					SessionUser sessionUser = SessionUser.load();
					JSONObject jsonObject = (JSONObject)JSONObject.toJSON(sessionUser);
					JSONObject jsonObjects = new JSONObject();
					//
					// logger.info(jsonObject);
					for (String key : jsonObject.keySet()) {
						//
						jsonObjects.put(key.toLowerCase(), jsonObject.get(key));
						if ("USER_ID".equals(key)) {
							jsonObjects.put("userId", jsonObject.get(key));
						}
						if ("USER_NAME".equals(key)) {
							jsonObjects.put("userName", jsonObject.get(key));
						}
						if ("roles".equals(key)) {
							String roleId = "";
							String roleName = "";
							JSONArray array = JSONArray.parseArray(jsonObject.getString(key));
							if (array != null && array.size() > 0) {
								for (Object object : array) {
									JSONObject obj = (JSONObject)object;
									for (String key1 : obj.keySet()) {
										if ("ROLE_ID".equals(key1)) {
											roleId += "," + obj.getString(key1);
										}
										if ("ROLE_NAME".equals(key1)) {
											roleName += "," + obj.getString(key1);
										}
									}
								}
							}
							jsonObjects.put("roleId", roleId.substring(1));
							jsonObjects.put("roleName", roleName.substring(1));
						}
					}
					JSONObject jsonObject1 = new JSONObject();
					jsonObject1.put("data", jsonObjects);
					jsonObject1.put("status", true);
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2.put("data", jsonObject1);
					jsonObject2.put("status", true);
					ret = jsonObject2.toJSONString();
				}
			}
			if (StringUtils.isBlank(ret)) {
				logger.warn("根据token获取登陆信息失败");
				return "errors/404";
			}
			JSONObject jsonObject = JSONObject.parseObject(ret);
			/*
			 * {"code":"","data":{"data":{"appList":["nv_xtgl:","nv_wdwg:","nv_jcyj:","nv_shdy:","n
			 * v_sqgz:"],"dwid":"1","dwname":"大渡口区","headPic":"images/index/
			 * ico_mrtx.png","isDirect":false,"isManager":"1","isxm":false,"jssssqdm":"500104000000","name":"系
			 * 统管理员","role":"0","roleId":"22","sssqCN":"大渡口区","sssqdm
			 * ":"500104000000","tel":"","timeout":"6000","token":"449eb25433b04fb69fbf0ce5a2bb1782","userId":"1","us
			 * erName":"user"},"status":true},"msg":"","status":true}
			 */logger.info("登陆信息:" + jsonObject);
			if (!jsonObject.getBooleanValue("status")) {
				logger.error("登陆失败");
				return "errors/404";
			}
			if (!jsonObject.getJSONObject("data").getBooleanValue("status")) {
				logger.error("登陆失败,重新登录");
				return getTimeOutUrl(request, model);
			}
			saveSessionUser(request, jsonObject);
			Map<String, Object> userMap = getCurUser(request);
			List<String> list = new ArrayList<String>();
			StatisticsDDKService statisticsDDKService = (StatisticsDDKService)ServiceLocator
			        .getBeanByClass(StatisticsDDKService.class);
			if ("2.0".equals(v)) {
				list = statisticsDDKService.getMenuByRoleId(userMap.get("roleId").toString());
			} else {
				list = statisticsDDKService.getMenuByGroupId(userMap.get("group_id").toString());
			}
			saveSessionList(request, list);
			
			// if(action.startsWith("em")){
			/*********************** 开始获取并存储部门信息 *****************************/
			EmergencyService emergencyService = (EmergencyService)ServiceLocator.getBeanByClass(EmergencyService.class);
			Map<String, Object> parMap = new HashMap<String, Object>();
			String mhUser = (String)userMap.get("userName");
			String username = request.getParameter("userName");
			if (!StringUtils.isBlank(username)) {
				mhUser = username;
			}
			parMap.put("mhUser", mhUser);
			Pagination page = emergencyService.queryEMUserList(parMap);
			// logger.info("登陆信息1:"+parMap+","+(page!=null&&page.getList().size()>0?page.getList().get(0):null));
			if (page != null && page.getList().size() > 0) {
				// logger.error("登陆错误：没有关联的应急用户.");
				// return "errors/500";
				System.out.println(page.getList().get(0).toString());
				
				Map<String, Object> map = (Map<String, Object>) page.getList().get(0);
				userMap.put("yj_userId", map.get("USER_ID"));
				userMap.put("yj_userType", map.get("USER_TYPE"));
				String userId = (String)userMap.get("yj_userId");
				parMap.put("userId", userId);
				page = emergencyService.queryEMUserDepList(parMap);
				if (page != null) {
					List<Map<String, Object>> depList = (ArrayList<Map<String, Object>>)page.getList();
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
				}
			}
			// }
		} catch (Exception e) {
			logger.error("登陆错误：", e);
			return "errors/500";
		}
		// CacheConstants.token=token;
		if (StringUtils.isBlank(action)) {
			return "forward:/fire/listMap.do";
		}
		if (action.startsWith("zh")) {
			String code = action.replace("zh", "");
			return "forward:/zhcx/list.do?code=" + code;
		}
		if (action.startsWith("menu")) {
			return "forward:/stats/go_menu.do";
		}
		if (action.startsWith("work")) {
			return "forward:/work/go_qzlist.do";
		}
		if (action.startsWith("wxy")) {
			return "forward:/dangerous/listMapTest.do";
		}
		if (action.startsWith("gov")) {
			return "forward:/govpublic/list.do";
		}
		if (action.startsWith("child")) {
			return "forward:/onechild/list.do";
		}
		if (action.startsWith("em")) {
			return "forward:/emergency/list.do";
		}
		logger.warn("跳转链接无效:action:" + action);
		return "errors/404";
	}
	private String getLocalJson() {
		return "{\"code\":\"\",\"data\":{\"data\":{\"appList\":[\"nv_xtgl:\",\"nv_wdwg:\",\"nv_jcyj:\",\"nv_shdy:\",\"nv_sqgz:\"],\"dwid\":\"1\",\"dwname\":\"大渡口区\",\"headPic\":\"images/index/ico_mrtx.png\",\"isDirect\":false,\"isManager\":\"1\",\"isxm\":false,\"jssssqdm\":\"500104000000\",\"name\":\"系统管理员\",\"role\":\"0\",\"roleId\":\"22\",\"sssqCN\":\"大渡口区\",\"sssqdm\":\"500104000000\",\"tel\":\"\",\"timeout\":\"6000\",\"token\":\"449eb25433b04fb69fbf0ce5a2bb1782\",\"userId\":\"1\",\"userName\":\"user9\"},\"status\":true},\"msg\":\"\",\"status\":true}";
	}
	public static String getIp2(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		System.out.println("ip1:" + ip + ":当前:" + request.getRemotePort() + "/本机:" + request.getLocalPort() + "/服务:"
		        + request.getServerPort());
		System.out.println("ip2:" + request.getHeader("X-Real-IP"));
		System.out.println("ip3:" + request.getRemoteAddr());
		if (!StringUtils.isBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
}
