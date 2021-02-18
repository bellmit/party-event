package com.sunsharing.party.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.ihome.air.common.ws.AirClientContext;
import com.sunsharing.login.user.UserCacheFactory;
import com.sunsharing.memCache.Session;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.api.WeixinQYHService;
import com.sunsharing.party.common.cache.CacheKeyConstants;
import com.sunsharing.party.common.weixin.WeiXinFactory;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.util.CacheConstants;
import com.sunsharing.party.util.CacheUtil;
import com.sunsharing.party.util.HttpSendShort;
import com.sunsharing.party.util.RequestUtils;
import com.sunsharing.party.util.WxDownloadFileUtils;
import com.sunsharing.weixin.filter.WeChatApi;
import com.sunsharing.weixin.filter.entity.WeixinJsConfig;
import com.sunsharing.weixin.filter.session.WeChatUser;

@Controller
@RequestMapping("gzh")
public class EmergencyController extends BaseController {
	static Logger logger = Logger.getLogger(EmergencyController.class);
	private static final String TEMP_UPLOAD = "upload";
	private static ExecutorService executorService = Executors.newFixedThreadPool(10);
	WeixinJsConfig weixJsConfig;
	@RequestMapping("/goTest.do")
	public String goTest(){
		System.out.println("=====1=1=21==2=====");
		return "test";
	}
	public WeixinJsConfig getWeixinJsConfig(HttpServletRequest request) throws UnsupportedEncodingException {
		/*if(weixJsConfig !=null){
			return weixJsConfig;
		}*/
		String fullURL1 = RequestUtils.getFullRequestURL(ConfigParam.weixinurl, request).getFullURL();// ConfigParam.weiXinUrl+"qyh/go_EmergencyManage.do";
		String fullURL = URLEncoder.encode(fullURL1, "UTF-8");
		WeChatApi weChatApi=new WeChatApi();
		weixJsConfig = weChatApi.getWeixJsConfig(request,fullURL);
		return weixJsConfig;
	}
	/**
	 * 跳转到应急信息管理页面111
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/go_EmergencyManage.do")
	// @CheckLogin( weiXinType = "QYH", cCode = "qy")
	public String go_EmergencyManage(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		WeixinJsConfig weixJsConfig = getWeixinJsConfig(request);
		WeChatUser weChatUser=WeChatUser.load(request);
		//微信jssdk所需要的的东西
		String  timestamp=weixJsConfig.getTimestamp();
		String nonceStr=weixJsConfig.getNonceStr();
		String signature=weixJsConfig.getSignature();
		String appId=weixJsConfig.getAppId();// 表示是那个公众号
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("timestamp",timestamp);
		jsonObject.put("nonceStr",nonceStr);
		jsonObject.put("signature",signature);
		jsonObject.put("appId",appId);
		jsonObject.put("jsapiTicket",weixJsConfig.getJsapiTicket());
		final String generateConfigJson = jsonObject.toJSONString();
		String mysid = (String) weChatUser.getExtend().get("mysid");
		String openId = weChatUser.getOpenid();// 公众号的openId
		String userId =(String) weChatUser.getExtend().get("userId");// 公众号的userId
		String loginUrl = request.getParameter("loginUrl");// 登陆的链接地址
		String mobile = weChatUser.getPhoneNumber();// 获取电话号码
		String msgSource = request.getParameter("type");// 判断是设施还是民生问题 0-设施 1-民生问题
		String comCode = weChatUser.getXzqh();// 传过来的社区编码
		if (StringUtils.isBlank(msgSource) || "null".equals(msgSource)) {
			msgSource = "1";
		}
		String deviceNo = request.getParameter("facilityId");// 设备编号
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wx_userId", userId);
		map.put("loginUrl", loginUrl);
		map.put("mobile", mobile);
		map.put("openId", openId);
		map.put("comCode", comCode);
		map.put("appId", appId);
		map.put("msgSource", msgSource);
		map.put("deviceNo", deviceNo);
			UserCacheFactory.create().saveUserCache(request, response, appId, userId, openId, loginUrl,
					CacheKeyConstants.cacheTimeout + "", CacheKeyConstants.LOGIN_MAP_KEY, map);
			UserCacheFactory.create().setUserEdition(request, response, appId, userId, openId, loginUrl,
					CacheKeyConstants.cacheTimeout + "", CacheKeyConstants.LOGIN_USER_KEY);
		String wx_UserId = userId;
		// String wx_UserId = "jumin";
		// 放入缓存
		Session.getInstance().setAttribute(request, CacheKeyConstants.LOGIN_USER_KEY, wx_UserId);
		logger.info("微信端问题提交时，获取用户id的值：" + wx_UserId);
		CacheUtil.removeCache(CacheConstants.SESSIONUSERKEY + mysid);
		logger.info(" 公众号号  - 获取签名等信息     generateConfigJson:" + generateConfigJson);
		WeixinQYHService wService = AirClientContext.getBean(WeixinQYHService.class);
		List emergencyTypeInfo = wService.getEmergencyType(null);
		// 获取人员信息========
		Map<String, String> params = new HashMap<String, String>();
		params.put("wx_UserId", wx_UserId);
		List queryUserInfo = wService.queryUserInfo(params);
		if (queryUserInfo.size() > 0) {
			String SMS_FLAG = (String) ((Map) queryUserInfo.get(0)).get("SMS_FLAG");// 下发标记
			String dString = (String) ((Map) queryUserInfo.get(0)).get("DEP_LEVEL");
			String USER_ID = (String) ((Map) queryUserInfo.get(0)).get("USER_ID");
			int DEP_LEVEL = 4; // Integer.valueOf(dString);

			if (DEP_LEVEL == 4 && "1".equals(SMS_FLAG)) {// 应急办的值班人员，在采集的时候 需要选择领导
				Map<String, Object> parMap = new HashMap<String, Object>();
				parMap.put("pageSize", "9999");
				parMap.put("depLevel", "5");
				parMap.put("userId", USER_ID);
				Pagination page = wService.queryEMUserDepSMSList(parMap);

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
					// model.addAttribute("depUserList", depUserList);
					model.addAttribute("lindaoList", depUserList);
				}
			}
		} else {// 模拟一个居民账号
			Map<String, Object> juminMap = new HashMap<String, Object>();
			juminMap.put("WX_USER_ID", "jumin");
			juminMap.put("SEX", "2");
			juminMap.put("DEP_LEVEL", "3");
			juminMap.put("USER_ID", "0023");
			juminMap.put("USER_NAME", "张三");
			juminMap.put("EMAIL", "342342");
			juminMap.put("SMS_FLAG", "1");
			juminMap.put("MH_USER", "jumin");
			juminMap.put("DEP_ID", "1003");
			juminMap.put("STATUS", "1");
			juminMap.put("DEP_NAME", "居民");
			juminMap.put("CERT_ID", "32432432423");
			juminMap.put("MOBILE", "1202555454");
			queryUserInfo.add(juminMap);
		}
		//是否党员报道
		boolean isDangYuan=false;
		List<Map<String, Object>> signUserExt = wService.getSignUserExt(userId);

		if(signUserExt.size()>0){
			isDangYuan=true;
		}
		//查询社区列表
		List<Map<String, Object>> xzqhList = wService.getXzqhList();
		logger.info("查询的社区列表=========" + xzqhList);
		logger.info("isDangYuan=========" + isDangYuan);
		model.addAttribute("xzqhList",xzqhList);
		model.addAttribute("isDangYuan",isDangYuan);
		model.addAttribute("deviceNo", deviceNo);
		model.addAttribute("msgSource", msgSource);
		model.addAttribute("jsSign", generateConfigJson);
		model.addAttribute("wx_UserId", wx_UserId);
		model.addAttribute("emergencyTypeInfo", emergencyTypeInfo);
		model.addAttribute("userId", userId);
		model.addAttribute("appId", appId);
		model.addAttribute("loginUrl", loginUrl);
		model.addAttribute("openId", openId);
		model.addAttribute("phone", mobile);
		model.addAttribute("comCode", comCode);
		return "weixin/emergencyManage2";
	}

	/**
	 * 保存应急采集信息
	 * 
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/save_EmergencyManage.do")
	@ResponseBody
	public Map<String, Object> save_EmergencyManage(Model model, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		sendRedirect(request, response);
		String wx_UserId = request.getParameter("wx_UserId");
		String openId = request.getParameter("openId");
		String loginUrl = request.getParameter("loginUrl");
		// JSONObject userInfo =
		// WeiXinFactory.init("QYH").getWXUserInfo_BywxUserId(wx_UserId);
		logger.info("存信息的时候，查询微信用户调用的openId：" + openId);
		JSONObject wxUserInfo = new JSONObject();
		wxUserInfo.put("mobile", "");
		JSONObject userInfo = wxUserInfo;
		// JSONObject userInfo = WeiXinFactory.init("GZH").getUserInfo(
		// WeiXinFactory.init("GZH").getAccessToken(ConfigParam.weixinAppId,
		// ConfigParam.weixinSecretKey), openId);
		String serverIds = request.getParameter("serverIds");
		ArrayList<String> localImagelArrayList = new ArrayList<String>();// dfs存图片的地址
		String[] serverId_splits = serverIds.split(",");
		for (int i = 0; i < serverId_splits.length; i++) {
			String serverId = serverId_splits[i];
			if (!StringUtils.isBlank(serverId)) {
				// 发送图片下载到dfs消息到消息中心
				String localImageFileURL = photosSendClient(serverId, request, loginUrl);
				localImagelArrayList.add(localImageFileURL);
			}
		}
		Map<String, Object> resultObj = new HashMap<String, Object>();
		String res = save_EmergencyManage(userInfo, localImagelArrayList, request);
		logger.info("存信息后台返回状态=========" + res);
		resultObj.put("result", res);
		return resultObj;
		/*
		 * try { response.getWriter().write(resultObj.toJSONString()); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}

	/**
	 * 通过消息中心，将上传到微信的素材文件 下载到DFS
	 * 
	 * @param serverId
	 * @param request
	 * @return localImageFileURL
	 */
	private String photosSendClient(String serverId, HttpServletRequest request, String loginUrl) throws UnsupportedEncodingException {
		String media_id = serverId;

		// String temp = loginUrl.substring(0, loginUrl.lastIndexOf("/"));
		// String url1 = temp.substring(0, temp.lastIndexOf("/"));
		// ConfigParam.splitWebsite();
		// url1 = ConfigParam.replaceWebsite(url1);
		// final String AccessToken = HttpSendShort.getHTML(url1 + "/send/getAccessToke.do", "UTF-8");
		WeixinJsConfig weixinJsConfig=getWeixinJsConfig(request);
		String AccessToken=weixinJsConfig.getAccessToken();
		logger.info("从微信下载图片的url需要的token：" + AccessToken);
		String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + AccessToken + "&media_id="
				+ media_id;
		// String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token="
		// + WeiXinFactory.init("GZH").getAccessToken(ConfigParam.weixinAppId,
		// ConfigParam.weixinSecretKey)
		// + "&media_id=" + media_id;
		String uuid = StringUtils.generateUUID();
		String date = DateUtils.getDBString(new Date()).substring(0, 8);
		String localImageFile = uuid + ".jpg";
		// final String directory = ConfigParam.fileServerJsfPath+ "/" + date + "/";

		// 获取本地服务器访问跟路径
		final String rootPath = request.getSession().getServletContext().getRealPath("/");
		logger.info("获取本地服务器访问跟路径:" + rootPath);
		// 文件所在服务器目录地址
		final String directory = TEMP_UPLOAD + File.separator + DateUtils.getDBString(new Date()).substring(0, 8);
		logger.info("文件所在服务器目录地址:" + directory);
		// 判断文件目录是否创建
		final File directoryFile = new File(rootPath + directory);
		if (!directoryFile.exists()) {
			// 创建目录并且连带父目录一起创建
			directoryFile.mkdirs();
		}
		String filePath = "";
		// 从微信接口中下载文件
		try {
			final File imgFile = WxDownloadFileUtils.wxServerDownload(url, directoryFile, localImageFile);
			logger.info("微信下载图片下载成功........");
			// 将图片转换为base64编码
			String imgCode = getImgStr(imgFile);
			WeixinQYHService wService = AirClientContext.getBean(WeixinQYHService.class);
			filePath = wService.saveImg(imgCode);
			// 删除本地的图片文件
			delFolder(rootPath + directory);
			logger.info("上传服务器返回的路径：" + filePath);

			/*
			 * String filePath=""; if("DFS".equals(ConfigParam.fileServerType)) { filePath =
			 * FileFactory.create().saveFile(imgFile,""); }else { filePath =
			 * FileFactory.create().saveFile(imgFile,"/"+ConfigParam.entryName+"/forever/"+
			 * ConfigParam.imgType); }
			 */
			// logger.info("当前图片文件入库路径：" + filePath);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// SendMsgThread sendMsgThread = new SendMsgThread();
		// sendMsgThread.setUrl(url);
		// sendMsgThread.setLocalImageFile(localImageFile);
		// executorService.execute(sendMsgThread);

		/*
		 * MsgSendClient client = new MsgSendClient(); JSONObject obj = new
		 * JSONObject(); obj.put("topic", "WX_PHO"); obj.put("expires", "10");
		 * obj.put("url", url); obj.put("name", localImageFile);
		 * logger.info("...发送到消息中心的 url:" + url + "   localImageFile:" +
		 * localImageFile); client.sendMsg(obj.toJSONString());
		 */
		return filePath;
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	private String save_EmergencyManage(JSONObject userInfo, ArrayList localImagelArrayList,
			HttpServletRequest request) {
		String wx_UserId = request.getParameter("wx_UserId");
		logger.info("存信息时，获取到的用户id：" + wx_UserId);
		String fireTypeValue = request.getParameter("fireTypeValue");
		// String emergencyTime = request.getParameter("emergencyTime");
		String desc = request.getParameter("desc");
		String deviceNo = request.getParameter("deviceNo");
		String msgSource = request.getParameter("msgSource");
		String mapAddress = request.getParameter("mapAddress");
		String lng = request.getParameter("lng");
		String lat = request.getParameter("lat");
		String emergencyDep = request.getParameter("emergencyDep");
		String lingdao = request.getParameter("lingdao");
		String problemTitle = request.getParameter("problemTitle");
		String certId = request.getParameter("certId");
		String wxUserName = request.getParameter("wxUserName");
		String msgUserPhone = request.getParameter("msgUserPhone");
		String ifOpen = request.getParameter("ifOpen");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("wx_UserId", wx_UserId);
		paramMap.put("fireTypeValue", fireTypeValue);
		paramMap.put("desc", desc);
		paramMap.put("mapAddress", mapAddress);
		paramMap.put("lng", lng);
		paramMap.put("lat", lat);
		paramMap.put("emergencyDep", emergencyDep);
		paramMap.put("problemTitle", problemTitle);
		paramMap.put("certId", certId);
		paramMap.put("wxUserName", wxUserName);
		paramMap.put("lingdao", lingdao);
		paramMap.put("msgSource", msgSource);
		paramMap.put("deviceNo", deviceNo);
		paramMap.put("msgUserPhone", msgUserPhone);
		paramMap.put("ifOpen", ifOpen);
		// paramMap.put("emergencyTime", emergencyTime);

		paramMap.put("userInfo", userInfo);
		logger.info("公总号-save_EmergencyManage-paramMap：" + paramMap.toString());

		WeixinQYHService wService = AirClientContext.getBean(WeixinQYHService.class);
		String saveEmergencyInfo = wService.saveEmergencyInfo(paramMap, localImagelArrayList.toArray());
		// if ("sucess".equals(saveEmergencyInfo)) {// 发送消息
		// List queryUserInfo = wService.queryUserInfo(paramMap);
		// Map usermap = (Map)queryUserInfo.get(0);
		// String dep_name = (String)usermap.get("DEP_NAME");
		//
		// String USER_NAME = userInfo.getString("name");
		// // 类型
		// String types = "";
		// ArrayList emergencyType = getEmergencyType(fireTypeValue, wService, types);
		// types = (String)emergencyType.get(0);
		//
		// String wX_USER_IDs = "";
		// // 查上级
		// if (StringUtils.isBlank(lingdao)) {// 普通
		// wX_USER_IDs = getEmergencySMSUser(userInfo, wx_UserId, paramMap, wService,
		// wX_USER_IDs);
		// } else {// 应急办值班人员 提交过来的 直接给选择的领导发送消息
		// wX_USER_IDs = get_Lingdao_EmergencySMSUser(lingdao, wX_USER_IDs);
		// }
		//
		// if (!StringUtils.isBlank(lingdao)) {// 领导
		// desc = "教委向您上报了一条应急事件信息，事件类别为：" + (String)emergencyType.get(1) + "(" + types
		// + ");" + "事件详情：" + desc
		// + ";请您通过大渡口区教育委员会企业号进行批示。";
		// } else {
		// desc = dep_name + (StringUtils.isBlank(USER_NAME) ? "" : USER_NAME) +
		// "上报了一条应急事件信息，事件类别为："
		// + (String)emergencyType.get(1) + "(" + types + ");" + "事件详情：" + desc +
		// ";请您及时登录应急信息采集系统进行处置。";
		// }
		// // 向上级发送消息
		// sendSmsTochief(desc, wX_USER_IDs, lingdao);
		// }
		return saveEmergencyInfo;
	}

	// //应急办值班人员 提交过来的 直接给选择的领导发送消息 组装微信id
	private String get_Lingdao_EmergencySMSUser(String lingdao, String wX_USER_IDs) {
		String[] lingdaoSplit = lingdao.split("###");
		for (int i = 0; i < lingdaoSplit.length; i++) {
			String value = lingdaoSplit[i];
			if (!StringUtils.isBlank(value)) {
				String[] split = value.split(",");
				if (split.length > 1) {
					wX_USER_IDs += split[1] + "|";
				}
			}
		}
		if (wX_USER_IDs.endsWith("|")) {
			wX_USER_IDs = wX_USER_IDs.substring(0, wX_USER_IDs.length() - 1);
		}
		return wX_USER_IDs;
	}

	// 向上级发送消息
	private void sendSmsTochief(String desc, String wX_USER_IDs, String lingdao) {
		logger.info("....上级人id：" + wX_USER_IDs);

		// sendSMSThread smsThread = new sendSMSThread();
		// smsThread.setWx_SmsUserIds(wX_USER_IDs);
		//
		// smsThread.setDesc(desc);
		// if (!StringUtils.isBlank(lingdao)) {
		// smsThread.setToUserFlag("lingdao");
		// }
		// executorService.execute(smsThread);
	}

	// 类型组装 by ids
	private ArrayList getEmergencyType(String fireTypeValue, WeixinQYHService wService, String types) {
		ArrayList<String> arrayList = new ArrayList<String>();

		String GROUP_NAME = "";
		List emergencyType = wService.getEmergencyType(fireTypeValue);

		for (int i = 0; i < emergencyType.size(); i++) {
			String type_name = (String) ((Map) emergencyType.get(i)).get("TYPE_NAME");
			types += type_name + ",";
		}
		GROUP_NAME = (String) ((Map) emergencyType.get(0)).get("GROUP_NAME");
		if (types.endsWith(",")) {
			types = types.substring(0, types.length() - 1);
		}

		arrayList.add(types);
		arrayList.add(GROUP_NAME);
		return arrayList;
	}

	// 查上级微信id
	private String getEmergencySMSUser(JSONObject userInfo, String wx_UserId, Map<String, Object> paramMap,
			WeixinQYHService wService, String wX_USER_IDs) {
		paramMap.clear();
		paramMap.put("wx_UserId", wx_UserId);
		paramMap.put("mobile", userInfo.get("mobile"));
		ArrayList<String> emergencySMSUser = wService.getEmergencySMSUser(paramMap);
		if (emergencySMSUser != null) {
			if (emergencySMSUser.size() > 0) {
				int size = emergencySMSUser.size();
				for (int i = 0; i < size; i++) {
					String WX_USER_ID = emergencySMSUser.get(i);
					if (i != size - 1) {
						wX_USER_IDs += WX_USER_ID + "|";
					} else {
						wX_USER_IDs += WX_USER_ID;
					}
				}

			}
		}
		return wX_USER_IDs;
	}

	/**
	 * 应急采集信息列表展示
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/go_messageList.do")
	public String go_messageList(Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			String ifOpen = request.getParameter("ifOpen");
			if (StringUtils.isBlank(ifOpen)) {
				ifOpen = "2";
			}
			String refUrl = request.getHeader("Referer");
			System.out.println("refUrl=" + refUrl);
			String messageAgent = request.getParameter("messageAgent");
			WeixinJsConfig weixJsConfig = getWeixinJsConfig(request);
			WeChatUser weChatUser=WeChatUser.load(request);

			String mysid = (String) weChatUser.getExtend().get("mysid");
			String openId = weChatUser.getOpenid();// 公众号的openId
			String userId = (String) weChatUser.getExtend().get("userId");// 公众号的userId
			String appId = weixJsConfig.getAppId();// 表示是那个公众号
			String loginUrl = request.getParameter("loginUrl");// 登陆的链接地址
			String msgSource = request.getParameter("type");// 判断是设施还是民生问题 0-设施 1-民生问题
			String mobile = weChatUser.getPhoneNumber();
			String comCode = weChatUser.getXzqh();// 传过来的社区编码
			logger.info("信息列表获取到的loginUrl：" + loginUrl);
			if (StringUtils.isBlank(msgSource) || "null".equals(msgSource)) {
				msgSource = "1";
			}
			String deviceNo = request.getParameter("facilityId");// 设备编号
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("wx_userId", userId);
			map.put("ifOpen", ifOpen);
			map.put("messageAgent", messageAgent);
			map.put("loginUrl", loginUrl);
			map.put("mobile", mobile);
			map.put("openId", openId);
			map.put("comCode", comCode);
			map.put("appId", appId);
			// if (StringUtils.isBlank(loginUrl)) {
			// 	final Object o = UserCacheFactory.create().getUserCache(request, response,
			// 			CacheKeyConstants.LOGIN_MAP_KEY);
			// 	if (o == null) {
			// 		model.addAttribute("error", "权限不足！");
			// 		return "weixin/Jurisdiction";
			// 	}
			// 	final Map<String, String> mapInfo = (Map<String, String>) o;
			// 	userId = mapInfo.get("wx_userId");
			// 	ifOpen = mapInfo.get("ifOpen");
			// 	loginUrl = mapInfo.get("loginUrl");
			// 	messageAgent = mapInfo.get("messageAgent");
			// 	mobile = mapInfo.get("mobile");
			// 	openId = mapInfo.get("openId");
			// 	comCode = mapInfo.get("comCode");
			// 	appId = mapInfo.get("appId");
			// 	logger.info("当loginUrl为空时，从缓存获取的参数，userId:=" + userId + ",ifOpen=" + ifOpen);
			// } else {
				UserCacheFactory.create().saveUserCache(request, response, appId, userId, openId, loginUrl,
						CacheKeyConstants.cacheTimeout + "", CacheKeyConstants.LOGIN_MAP_KEY, map);
				UserCacheFactory.create().setUserEdition(request, response, appId, userId, openId, loginUrl,
						CacheKeyConstants.cacheTimeout + "", CacheKeyConstants.LOGIN_USER_KEY);
			// }
			// String wx_userId = (String) Session.getInstance().getAttribute(request,
			// CacheKeyConstants.LOGIN_USER_KEY);
			String wx_userId = userId;
			logger.info("获取微信端信息列表时session中的 用户id值：" + wx_userId);
			/*
			 * if (StringUtils.isBlank(mysid) || StringUtils.isBlank(wx_userId)) { //
			 * wx_UserId = checkRefferCookie(request);
			 * 
			 * // if (StringUtils.isBlank(wx_userId)) { String url =
			 * ConfigParam.qyh_weiXinUrl + "qyh/go_redirect.do?state=go_messageList"; if
			 * (!StringUtils.isBlank(messageAgent)) { url = ConfigParam.qyh_weiXinUrl +
			 * "qyh/go_redirect.do?state=messageAgent"; } response.sendRedirect(url); return
			 * null; // } }
			 */
			CacheUtil.removeCache(CacheConstants.SESSIONUSERKEY + mysid);
			Pagination page = getEmergencyList(wx_userId, "", "", messageAgent, 1, 20, request, response, ifOpen);
			setModel(model, page, "infoList");
			// model.addAttribute("token", getUserToken(request, response));
			model.addAttribute("wx_userId", wx_userId);
			model.addAttribute("messageAgent", messageAgent);
			model.addAttribute("deviceNo", deviceNo);
			model.addAttribute("msgSource", msgSource);
			model.addAttribute("userId", userId);
			model.addAttribute("appId", appId);
			model.addAttribute("loginUrl", loginUrl);
			model.addAttribute("openId", openId);
			model.addAttribute("phone", mobile);
			model.addAttribute("comCode", comCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "weixin/questionList2";
	}

	@RequestMapping(value = "/getEmergencyList.do")
	public @ResponseBody JSONObject getEmergencyList(String wx_userId, String title, int currentPage, int linesPerPage,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		sendRedirect(request, response);
		try {
			String ifOpen = request.getParameter("ifOpen");
			String messageAgent = request.getParameter("messageAgent");
			Pagination page = getEmergencyList(wx_userId, "", title, messageAgent, currentPage, linesPerPage, request,
					response, ifOpen);
			JSONObject rtnJsonObject = new JSONObject();
			bindReturnData(page, rtnJsonObject);
			return rtnJsonObject;
			// 以前的获取数据的方法，现在改用返回为jsonobject了
			// List<Map<String, Object>> list = null;
			// Map<String, Object> map = new HashMap<String, Object>();
			// if (page != null && page.getList() != null) {
			// list = (List) page.getList();
			// replaceTime(list, "1");
			// map.put("list", list);
			// map.put("totalPage", page.getTotalPage());
			// map.put("totalCount", page.getTotalNum());
			// }
			// for (String key : map.keySet()) {
			// Object value = map.get(key);
			// System.out.println(key + " " + value);
			// }
			// return map;

		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
		return null;
	}

	public Pagination getEmergencyList(String wx_userId, String msgId, String desc, String messageAgent,
			int currentPage, int linesPerPage, HttpServletRequest request, HttpServletResponse response, String ifOpen)
			throws Exception {
		try {
			WeixinQYHService wService = AirClientContext.getBean(WeixinQYHService.class);
			Map<String, Object> parMap = new HashMap<String, Object>();
			parMap.put("pageNo", currentPage);
			parMap.put("pageSize", linesPerPage);
			parMap.put("messageAgent", messageAgent);
			parMap.put("ifOpen", ifOpen);
			System.out.println();
			System.out.println();
			System.out.println(".....messageAgent：" + messageAgent);
			System.out.println();
			parMap.put("desc", desc);
			if (!StringUtils.isBlank(wx_userId)) {
				/*
				 * JSONObject userInfo = WeixinQYHUntils .getWXUserInfo_BywxUserId(wx_userId);
				 * String mobile = userInfo.getString("mobile");
				 */
				// parMap.put("mobile", mobile);
				parMap.put("wx_userId", wx_userId);
			}
			// 判断是否为居民（临时人员）
			Map<String, String> params = new HashMap<String, String>();
			params.put("wx_UserId", wx_userId);
			List queryUserInfo = wService.queryUserInfo(params);
			String isJuMin = "0";// 0代表临时人员（居民），1代表用户表里面有信息
			if (queryUserInfo.size() > 0) {
				isJuMin = "1";
			}
			parMap.put("isJuMin", isJuMin);
			parMap.put("msgId", msgId);

			Pagination page = wService.queryEmeryList(parMap);

			return page;
		} catch (Exception e) {
			logger.error("获取列表失败", e);
			return null;
		}
	}

	/**
	 * set返回界面数据
	 * 
	 * @param model
	 * @param page
	 * @param key
	 */
	private void setModel(Model model, Pagination page, String key) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (page != null && page.getList() != null) {
			list = (List) page.getList();
		}
		replaceTime(list, "1");
		model.addAttribute(key, list);
		model.addAttribute("size", page.getTotalNum());
	}

	private void replaceTime(List<Map<String, Object>> list, String temp) {
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				// map.put("TITLE_2", map.get("TITLE").toString());
				if ("1".equals(temp)) {
					map.put("MSG_DESC",
							map.get("MSG_DESC").toString().length() > 12
									? map.get("MSG_DESC").toString().substring(0, 11) + "..."
									: map.get("MSG_DESC").toString());
				}
				map.put("CREATE_TIME",
						map.get("CREATE_TIME") != null && !"".equals(map.get("CREATE_TIME"))
								? DateUtils.transFormat(map.get("CREATE_TIME").toString(), "yyyy-MM-dd")
								: "2015-01-01");
			}
		}
	}

	@RequestMapping(value = "/go_messageDetail.do")
	public String go_messageDetail(Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			sendRedirect(request, response);
			WeixinQYHService wService = AirClientContext.getBean(WeixinQYHService.class);
			String messageAgent = request.getParameter("messageAgent");
			String wx_userId = request.getParameter("wx_userId");
			String MSG_ID = request.getParameter("MSG_ID");
			model.addAttribute("messageAgent", messageAgent);
			model.addAttribute("wx_userId", wx_userId);
			model.addAttribute("MSG_ID", MSG_ID);

			Pagination ePagination = getEmergencyList(wx_userId, MSG_ID, "", messageAgent, 1, 20, request, response,
					"0");
			List eList = ePagination.getList();
			String timeString = (String) ((Map) eList.get(0)).get("CREATE_TIME");
			model.addAttribute("time", DateUtils.getDisplay(timeString));
			// 1.设置应急类型
			String EM_TYPE = (String) ((Map) eList.get(0)).get("EM_TYPE");
			List emergencyTypeList = wService.getEmergencyType(EM_TYPE);
			String emergencyTypeRe = "";
			for (int i = 0; i < emergencyTypeList.size(); i++) {
				Map map = (Map) emergencyTypeList.get(i);
				if (i != emergencyTypeList.size() - 1) {
					emergencyTypeRe += map.get("TYPE_NAME") + ",";
				} else {
					emergencyTypeRe += map.get("TYPE_NAME");
				}
			}
			// model.addAttribute("emergencyTypeRe", emergencyTypeRe);

			// 2.处理步骤
			Map<String, String> param = new HashMap<String, String>();
			param.put("MSG_ID", MSG_ID);
			List queryMessagetant = wService.queryMessagetant(param);

			if (queryMessagetant.size() > 0) {
				String TRAN_STATUS = (String) ((Map) queryMessagetant.get(queryMessagetant.size() - 1))
						.get("TRAN_STATUS");
				model.addAttribute("flagtant", TRAN_STATUS);
			} else {
				model.addAttribute("flagtant", "0");
			}

			Map<String, List<Map>> issueMap = new LinkedHashMap<String, List<Map>>();
			Map map_emMap = (Map) eList.get(0);

			String ADDRESS = (String) map_emMap.get("ADDRESS");
			if (StringUtils.isBlank(ADDRESS) || "null".equals(ADDRESS)) {
				ADDRESS = "";
			}
			// 将提交问题的描述放进map
			putHandleMap(map_emMap.get("CREATE_TIME").toString(), issueMap, "【您的问题】",
					"<span style='color:#111'>问题描述：</span>" + map_emMap.get("MSG_DESC")
							+ "<br><br><span style='color:#111'>地理位置：</span>" + ADDRESS);
			// 设置处理步骤
			if (queryMessagetant.size() > 0) {
				for (int i = 0; i < queryMessagetant.size(); i++) {
					Map<String, String> aaa = (Map) queryMessagetant.get(i);
					String a = (String) aaa.get("TRAN_STATUS");
					if (!"5".equals(a) && !"6".equals(a) && !"7".equals(a) && !"8".equals(a) && !"9".equals(a)
							&& !"10".equals(a)) {

						Map map = (Map) queryMessagetant.get(i);
						String createTime = (String) map.get("CREATE_TIME");
						String TRAN_DEP = (String) map.get("TRAN_DEP");
						String TRAN_STATUS = (String) map.get("TRAN_STATUS");
						TRAN_STATUS = "0".equals(TRAN_STATUS) ? "反馈问题"
								: "1".equals(TRAN_STATUS) ? "阅知"
										: "2".equals(TRAN_STATUS) ? "已上报"
												: "3".equals(TRAN_STATUS) ? "续报"
														: "4".equals(TRAN_STATUS) ? "已处理"
																: "5".equals(TRAN_STATUS) ? "已批示" : "已关闭";
						String MSG_REMARK = (String) map.get("MSG_REMARK");
						putHandleMap(createTime, issueMap, "【" + TRAN_DEP + "  " + TRAN_STATUS + "】",
								"<span style='color:#111'>处理流程：</span>" + MSG_REMARK);
					}
				}
			}
			// 后续处理流程
			Map<String, List<Map>> laterMap = new LinkedHashMap<String, List<Map>>();
			List laterTant = wService.queryLaterTant(param);
			// 设置后续处理步骤
			if (laterTant.size() > 0) {
				for (int i = 0; i < laterTant.size(); i++) {
					Map map = (Map) laterTant.get(i);
					String MSG_REMARK = (String) map.get("MSG_REMARK");
					if (!StringUtils.isBlank(MSG_REMARK)) {
						String createTime = (String) map.get("CREATE_TIME");
						String TRAN_DEP = (String) map.get("TRAN_DEP");
						String TRAN_STATUS = (String) map.get("TRAN_STATUS");
						TRAN_STATUS = "0".equals(TRAN_STATUS) ? "未办理"
								: "20".equals(TRAN_STATUS) ? "后续跟踪处理" : "21".equals(TRAN_STATUS) ? "已办结" : "未办理";
						putHandleMap(createTime, laterMap, "【" + TRAN_DEP + "  " + TRAN_STATUS + "】",
								"<span style='color:#111'>办理流程：</span>" + MSG_REMARK);
					}
				}
			}
			model.addAttribute("laterMap", laterMap);
			model.addAttribute("Messagetant", issueMap);
			// 3.查询图片
			List emergencyImage = wService.getEmergencyImage(MSG_ID);
			model.addAttribute("picList", emergencyImage);

			replaceTime(eList, "");
			model.addAttribute("emInfo", eList);
			// model.addAttribute("token", getUserToken(request, response));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "weixin/emergencyDetail";
	}

	// 将提交问题的描述放进map
	private void putHandleMap(String dbTime, Map<String, List<Map>> issueMap, String stepName, String ms) {
		dbTime = DateUtils.getDisplay(dbTime);
		String update8day = dbTime.substring(0, 10);
		Map<String, String> handleMap = new HashMap();
		handleMap.put("stepName", stepName);
		handleMap.put("ms", ms);
		handleMap.put("time", dbTime.substring(11, 16));
		List<Map> list = issueMap.get(update8day);
		if (list == null) {
			list = new ArrayList<Map>();
			issueMap.put(update8day, list);
		}
		list.add(handleMap);
	}

	@RequestMapping(value = "/validateAuthInit.do")
	public void validateAuthInit(Model model, HttpServletRequest request, HttpServletResponse response) {
		String wx_UserId = request.getParameter("wx_UserId");
		JSONObject userInfo = WeiXinFactory.init("QYH").getWXUserInfo_BywxUserId(wx_UserId);
		String mobile = (String) userInfo.get("mobile");

		WeixinQYHService wService = AirClientContext.getBean(WeixinQYHService.class);
		Map<String, String> params = new HashMap<String, String>();
		params.put("wx_UserId", wx_UserId);
		params.put("mobile", mobile);
		List queryUserInfo = wService.queryUserInfo(params);

		JSONObject resultObj = new JSONObject();
		resultObj.put("result", queryUserInfo.size());
		try {
			response.getWriter().write(resultObj.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendRedirect(HttpServletRequest request, HttpServletResponse response) {
		boolean state = UserCacheFactory.create().getState(request, CacheKeyConstants.LOGIN_USER_KEY);
		if (!state) {
			try {
				String loginUrl = UserCacheFactory.create().getLoginUrl(request, CacheKeyConstants.LOGIN_USER_KEY);
				response.sendRedirect(loginUrl);
			} catch (IOException e) {
				logger.error("重定向出错", e);
			}
		}
	}

	/**
	 * 将图片转换成Base64编码
	 * 
	 * @param imgFile
	 *            待处理图片
	 * @return
	 */
	public static String getImgStr(File imgFile) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(data));
	}

	public static void main(String[] args) {
		// String a = "http://djtest.i12371.cn/party-weixintestct";
		// String b = a.replace("djtest.i12371.cn", "172.16.19.100");
		// System.out.println(b);
		delFolder("d:/bb");

	}
}
