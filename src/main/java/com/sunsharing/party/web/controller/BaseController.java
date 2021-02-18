package com.sunsharing.party.web.controller;

import java.io.Serializable;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.component.utils.web.CookieUtils;
import com.sunsharing.login.user.UserCacheFactory;
import com.sunsharing.memCache.Session;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.cache.CacheKeyConstants;
import com.sunsharing.party.constant.CacheConstant;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.util.CacheConstants;
import com.sunsharing.party.util.CacheUtil;
import com.sunsharing.party.util.RequestUtils;

/**
 * 控制器基类
 * <p>
 * </p>
 * 
 * @author wxl 2017年11月23日 上午8:59:40
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月23日
 * @modify by reason:{方法名}:{原因}
 */
public class BaseController {

	protected static final Logger logger = Logger.getLogger(BaseController.class);

	/**
	 * 门户中的获取用户信息
	 */
	// protected SessionUser sessionUser = SessionUser.load();

	/**
	 * 获取ip
	 * 
	 * @author wangchuan 2017年11月23日 上午9:15:55
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		logger.info("ip1:" + ip + ":当前:" + request.getRemotePort() + "/本机:" + request.getLocalPort() + "/服务:"
				+ request.getServerPort());
		logger.info("ip2:" + request.getHeader("X-Real-IP"));
		logger.info("ip3:" + request.getRemoteAddr());
		if (!StringUtils.isBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			final int index = ip.indexOf(",");
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

	/**
	 * 获取cookie
	 * 
	 * @author wangchuan 2017年11月23日 上午11:33:45
	 * @param request
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String checkRefferCookie(final HttpServletRequest request, final String key) throws Exception {
		final Cookie[] oCookies = request.getCookies();
		if (oCookies != null) {
			for (final Cookie oItem : oCookies) {
				final String sName = oItem.getName();
				if (sName.equals(key)) {
					logger.info("获取到的cookie: " + oItem.getValue());
					return URLDecoder.decode(oItem.getValue(), "utf-8");
				}
			}
		}
		return null;
	}

	/**
	 * 添加cookie 按照键值对的形式添加
	 * 
	 * @author hanjianbo 2016年11月10日 下午6:09:11
	 * @param request
	 * @param response
	 * @param key
	 * @param value
	 */
	public static void setRefferCookie(HttpServletRequest request, final HttpServletResponse response, final String key,
			final String value) {
		CookieUtils.addCookie(request, response, key, value, -1, null);
	}

	/**
	 * 强行设置MYSID
	 * 
	 * @author wangchuan 2016年8月24日 下午3:54:43
	 * @param request
	 * @param response
	 * @param mySid
	 */
	protected void setMySid(HttpServletRequest request, HttpServletResponse response, String mySid) {
		request.setAttribute("MYSID", mySid);
		if (response != null) {
			CookieUtils.addCookie(request, response, "MYSID", mySid, null, null);
		}
	}

	/**
	 * 获取在Cookie中缓存的CorpId,Secret,应用id
	 * 
	 * @author lixinqiao 2017年6月21日 上午9:21:43
	 * @param request
	 * @return
	 */
	public static Map<String, String> getCookies(HttpServletRequest request) {
		final Map<String, String> map = new HashMap<String, String>();
		final Cookie cookie = CookieUtils.getCookie(request, "stat1");
		final Cookie cookie2 = CookieUtils.getCookie(request, "sec1");
		final Cookie cookie3 = CookieUtils.getCookie(request, "cor1");
		String stat = "";
		String sec = "";
		String cor = "";
		if (cookie != null && cookie2 != null && cookie3 != null) {
			stat = cookie.getValue();
			sec = cookie2.getValue();
			cor = cookie3.getValue();
			map.put("stat", stat);
			map.put("sec", sec);
			map.put("cor", cor);
		} else {
			map.put("stat", (String) request.getAttribute("stat"));
			map.put("sec", (String) request.getAttribute("sec"));
			map.put("cor", (String) request.getAttribute("cor"));
		}
		return map;
	}

	protected Map getCurUser(HttpServletRequest request) {
		logger.info("get-mysid:" + getSessionId(request));
		Object o = Session.getInstance().getAttribute(request, CacheKeyConstants.SESSION_USER);
		logger.info("调用getCurUser获取用户信息：结果为" + o + "，参数request：" + request);
		if (o == null) {
			return null;
		} else {
			Map user = (Map) o;
			return user;
		}
	}

	private static String getSessionId(HttpServletRequest request) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++)
				if (cookies[i].getName().equals(CacheConstant.EVENT_COOKIE_KEY))
					return cookies[i].getValue();

		}
		return (String) request.getAttribute(CacheConstant.EVENT_COOKIE_KEY);
	}

	protected void bindReturnData(Pagination page, JSONObject rtnJsonObject) {
		rtnJsonObject.put("status", true);
		rtnJsonObject.put("msg", "获取数据成功");
		JSONObject businessJsonObject = new JSONObject();
		businessJsonObject.put("list", page.getList());
		businessJsonObject.put("currentPage", page.getCurrentPage());
		businessJsonObject.put("totalNum", page.getTotalNum());
		businessJsonObject.put("linesPerPage", page.getLinesPerPage());
		rtnJsonObject.put("data", businessJsonObject);
	}

	protected void bindReturnData(List list, JSONObject rtnJsonObject) {
		rtnJsonObject.put("status", true);
		rtnJsonObject.put("msg", "获取数据成功");
		JSONObject businessJsonObject = new JSONObject();
		businessJsonObject.put("list", list);
		businessJsonObject.put("currentPage", 1);
		businessJsonObject.put("totalNum", list.size());
		businessJsonObject.put("linesPerPage", list.size());
		rtnJsonObject.put("data", businessJsonObject);
	}

	protected void bindReturnData(Object value, boolean status, JSONObject rtnJsonObject) {
		rtnJsonObject.put("status", status);
		rtnJsonObject.put("msg", value);
	}

	protected void saveSessionUser(HttpServletRequest request, JSONObject jsonObject) {
		logger.info("save-mysid:" + getSessionId(request));
		Session.getInstance().setAttribute(request, CacheKeyConstants.SESSION_USER,
				(Serializable) JSONObject.parseObject(jsonObject.get("data").toString()).get("data"));
	}

	protected void saveSessionUser(HttpServletRequest request, Map userMap) {
		logger.info("save-mysid:" + getSessionId(request));
		Session.getInstance().setAttribute(request, CacheKeyConstants.SESSION_USER, (Serializable) userMap);
	}

	protected void saveSessionList(HttpServletRequest request, List list) {
		logger.info("save-mysid:" + getSessionId(request));
		Session.getInstance().setAttribute(request, CacheConstants.RoleApp, (Serializable) list);
	}

	public String getTimeOutUrl(HttpServletRequest request, Model model) {
		String ip = RequestUtils.getProxyIp(request);
		model.addAttribute("go_normalUrl", ConfigParam.normalUrl.replace("$url$", ip));
		return "errors/timeOut";
	}

	/**
	 * 获取用户的Token值，没有该值就生成一个
	 * 
	 * @author hanjianbo 2017年9月6日 上午10:14:53
	 * @param request
	 * @param response
	 * @return
	 */
	protected String getUserToken(HttpServletRequest request, HttpServletResponse response) {
		// String fxToken = getCacheToken(request);
		// if (StringUtils.isBlank(fxToken)) {// 分享的token不存在
		// fxToken = saveCacheToken(request, response);
		// if (StringUtils.isBlank(fxToken)) {
		// fxToken = "noPermission";
		// }
		// }
		final Map<String, Object> map = new HashMap<String, Object>();
		final String uuid = getSessionId(request);
		System.out.println("请求token获取到的uuid=" + uuid);
		final Object obj = CacheUtil.getCache(uuid + CacheConstant.EVENT_SESSION_KEY);// 缓存中取不到值
		final String o = UserCacheFactory.create().getLoginUrl(request, CacheKeyConstants.LOGIN_USER_KEY);
		if (obj == null || o == null) {
			return "error";
		}
		if (o != null && obj == null) {
			if (!StringUtils.isBlank(uuid)) {
				final String token = StringUtils.generateUUID() + "@" + DateUtils.getDBString(new Date());
				CacheUtil.saveCacheByPriod(uuid + CacheConstant.EVENT_SESSION_KEY, token,
						CacheConstant.KEY_CACHE_TIMEOUT + "");
			}
		}
		final String token = (String) obj;
		final String[] tokenArray = token.split("@");// 拆分
		final String time = tokenArray[1];
		final long cacheTime = getHM(time);
		final String date = DateUtils.getDBString(new Date());
		final long currentTime = getHM(date);
		if ((currentTime - cacheTime) / (1000 * 60) >= 1) {// token超过一分钟,重新生成token
			final String newToken = StringUtils.generateUUID() + "@" + DateUtils.getDBString(new Date());
			if (!StringUtils.isBlank(uuid)) {
				CacheUtil.saveCacheByPriod(uuid + CacheConstant.EVENT_SESSION_KEY, newToken,
						CacheConstant.KEY_CACHE_TIMEOUT + "");
				return newToken;
			} else {
				return "error";
			}
		} else {// 未超时，直接返回token
			return token;
		}
	}

	/**
	 * 获取分享的token
	 * 
	 * @author lixinqiao 2017年8月9日 下午4:13:16
	 * @return
	 */
	protected String getCacheToken(HttpServletRequest request) {
		final Map<String, Object> user = getCurUser(request);
		if (user == null) {
			return null;
		}
		final String fxToken = (String) user.get("fxToken");
		if (!StringUtils.isBlank(fxToken) && fxToken.indexOf("token") != -1) {
			final String[] fxs = fxToken.split("token");
			if (fxs.length > 1) {// 校验token的格式是否正确
				final long cacheTime = getHM(fxs[1]);
				final String date = DateUtils.getDBString(new Date());
				final long currentTime = getHM(date);
				if ((currentTime - cacheTime) / (1000 * 3600) >= 72) {// token超时,重新生成token
					return null;
				} else {// 未超时，直接返回token
					return fxToken;
				}
			} else {// token的格式不正确
				return null;
			}
		} else {
			return null;
		}
	}

	protected String saveCacheToken(HttpServletRequest request, HttpServletResponse response) {
		final String date = DateUtils.getDBString(new Date());
		final String token = StringUtils.generateUUID() + "token" + date;
		final Map<String, Object> user = getCurUser(request);
		if (user != null) {
			user.put("fxToken", token);
			saveSessionUser(request, response, user);
			return token;
		} else {
			return null;
		}
	}

	/**
	 * 生成Token 所需毫秒数获取
	 * 
	 * @author lixinqiao 2017年8月9日 下午5:25:40
	 * @param stime
	 * @return
	 */
	protected static long getHM(String stime) {
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		long from = -1;
		try {
			from = df.parse(stime).getTime();
		} catch (final Exception e) {
			logger.error(e);
		}
		return from;
	}

	/**
	 * 缓存用户对象
	 * 
	 * @author wangchuan 2016年8月25日 下午5:08:29
	 * @param request
	 * @param userMap
	 */
	protected void saveSessionUser(HttpServletRequest request, HttpServletResponse response, Map userMap) {
		String mysid = getSessionId(request);
		if (StringUtils.isBlank(mysid)) {
			mysid = StringUtils.generateUUID();
		}
		setMySid(request, response, mysid);
		CacheUtil.saveCache(mysid + CacheKeyConstants.SESSION_USER, (Serializable) userMap);
	}

	/**
	 * 创建一个用户Map信息
	 * 
	 * @author hanjianbo 2017年9月6日 上午9:52:47
	 */
	protected void createUserMap(String userId, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> user = getCurUser(request);
		if (user == null) {
			user = new HashMap<String, Object>();
			user.put("lessonUserId", userId);
			saveSessionUser(request, response, user);
		}
	}
}
