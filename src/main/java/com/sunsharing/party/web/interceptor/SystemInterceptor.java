package com.sunsharing.party.web.interceptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.common.response.ResponseHelper;
import com.sunsharing.party.constant.CacheConstant;
import com.sunsharing.party.util.CacheUtil;
import com.sunsharing.party.util.CookieUtils;
import com.sunsharing.party.util.LinkRuleMacthUtil;
import com.sunsharing.party.util.ReadFileUtils;

/**
 * <pre>
 * <b><font color="blue">SystemInterceptor</font></b>
 * </pre>
 * 
 * <pre>
 * <b>&nbsp;--系统拦截器--</b>
 * </pre>
 * 
 * <pre></pre>
 * 
 * JDK版本：JDK1.5.0
 * 
 * @author <b>ulyn</b>
 */
@Component("systemInterceptor")
@Repository
public class SystemInterceptor extends HandlerInterceptorAdapter {

	Logger logger = Logger.getLogger(SystemInterceptor.class);

	/**
	 * 在Controller方法前进行拦截 如果返回false 从当前拦截器往回执行所有拦截器的afterCompletion方法,再退出拦截器链.
	 * 如果返回true 执行下一个拦截器,直到所有拦截器都执行完毕. 再运行被拦截的Controller.
	 * 然后进入拦截器链,从最后一个拦截器往回运行所有拦截器的postHandle方法.
	 * 接着依旧是从最后一个拦截器往回执行所有拦截器的afterCompletion方法.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		String uri = request.getRequestURI();
		System.out.println("SystemInterceptor拦截到请求：" + uri);
		boolean fromweixin = false;
		if (uri.indexOf("weixin") != -1) {
			fromweixin = true;
		}
		if (fromweixin && request.getMethod().equals("POST")) {
			// 获取微信内容...

		}
		final String openId = request.getParameter("openId");// 公众号的openId
		final String userId = request.getParameter("userId");// 公众号的userId
		final String appId = request.getParameter("appId");// 表示是那个公众号
		final String loginUrl = request.getParameter("loginUrl");// 登陆的链接地址
		final String phone = request.getParameter("phone");// 电话号码
		final String comCode = request.getParameter("comCode");// 社区编码
		System.out.println("获取的上个链接============" + request.getHeader("referer"));
		if (!StringUtils.isBlank(openId) && !StringUtils.isBlank(userId) && !StringUtils.isBlank(appId)
				&& !StringUtils.isBlank(loginUrl) && !StringUtils.isBlank(phone) && !StringUtils.isBlank(comCode)) {// 表示用户是壳子登陆进来
			String mySid = getSessionId(request);
			if (StringUtils.isBlank(mySid)) {
				mySid = com.sunsharing.login.util.StringUtils.generateUUID();
				setMySid(request, response, mySid);// 强行设置mySid
			}
			System.out.println("存入的  mySid=======" + mySid);
			saveUserCache(request);
		} else {
			if (!LinkRuleMacthUtil.hasMenuPermission(request)) {
				final StringBuffer sb = new StringBuffer();
				sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
				sb.append("<HTML>");
				sb.append(
						" <HEAD><TITLE>操作失败</TITLE><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></HEAD>");
				sb.append(" <BODY>");
				sb.append("<h2>操作失败，不具备该页面的访问权限</h2>");
				sb.append(" </BODY>");
				sb.append("</HTML>");
				ResponseHelper.printOutContent(response, sb.toString());

				return false;
			}
		}
		final String token = request.getParameter("token");
		logger.info("SystemInterceptor拦截到请求token：" + token);
		// 设置当前请求的uri，供jsp页面获取
		request.setAttribute("getRequestURI", request.getRequestURI());

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		// 判断拦截的请求路径 uri.indexOf("background") != -1

		return super.preHandle(request, response, handler);
	}

	public static String getDateString(Date date) {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = "";
		if (date != null) {
			try {
				str = format.format(date);
			} catch (Exception e) {
				System.err.println("日期转换为yyyy-MM-dd HH:mm:ss格式的字符串出错!");
			}
		}
		return str;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//
		// if (modelAndView != null) {
		// String viewName = modelAndView.getViewName();
		// // System.out.println("view name : " + viewName);
		// } else {
		// // System.out.println("view null ");
		// }
	}

	/**
	 * 在Controller方法后进行拦截 当有拦截器抛出异常时,会从当前拦截器往回执行所有拦截器的afterCompletion方法
	 */
	@Override
	public void afterCompletion(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse,
			Object obj, Exception exception) throws Exception {

	}

	/**
	 * 产生一个32位的UUID
	 * 
	 * @return
	 */

	public static String generate() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);

	}

	/**
	 * 验证用户访问来源及菜单权限
	 * 
	 * @author wangchuan 2017年9月1日 下午3:05:13
	 * @param request
	 * @return
	 */
	private boolean hasMenuPermission(HttpServletRequest request) {
		String url = request.getRequestURI();
		String pars = request.getQueryString();
		String context = request.getContextPath();
		// 获取配置文件中需要校验的URL
		JSONArray jsonArray = ReadFileUtils.getJsonMenu("menu.json").getJSONArray("refererCheckurls");
		//
		boolean isFlag = true;
		// 判断配置文件中的URL是否存在于当前访问的来源中
		for (int i = 0; i < jsonArray.size(); i++) {
			//
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if (hasString(jsonObject.getString("tagertUrl"), url + "?" + pars)) {
				if (hasRuleString(jsonObject.getJSONArray("ruleUrls"), request.getHeader("Referer"))) {
					return true;
				} else {
					isFlag = false;
				}
				break;
			}
		}
		// 在配置中，又不满足来源的，异常跳出
		if (!isFlag) {
			return isFlag;
		}

		if (!isFlag) {
			return isFlag;
		}

		return true;
	}

	/**
	 * 检索对象中是否完全匹配目标
	 * 
	 * @author wangchuan 2017年9月1日 下午3:12:43
	 * @param tarStr
	 * @param indexStr
	 * @return
	 */
	private boolean hasString(String tarStr, String indexStr) {
		String[] tarStrs = tarStr.toLowerCase().split("\\?");
		String[] indexStrs = indexStr.toLowerCase().split("\\?");
		if (tarStrs[0].equals(indexStrs[0])) {
			String[] tarStrings = null;
			if (tarStrs.length > 1) {
				tarStrings = tarStrs[1].split("&");
			}
			String[] indexStrings = null;
			if (indexStrs.length > 1) {
				indexStrings = indexStrs[1].split("&");
			}
			if (tarStrings != null && indexStrings != null) {
				Arrays.sort(tarStrings);
				Arrays.sort(indexStrings);
				if (Arrays.equals(tarStrings, indexStrings)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检索对象中是否有满足规则的来源
	 * 
	 * @author wangchuan 2017年9月1日 下午3:16:14
	 * @param ruleStr
	 * @param indexStr
	 * @return
	 */
	private boolean hasRuleString(JSONArray ruleStr, String indexStr) {
		if (StringUtils.isBlank(indexStr)) {
			return false;
		}
		if (indexStr.indexOf(";") != -1) {
			String[] strings = indexStr.split(";");
			if (strings[1].indexOf("?") != -1) {
				indexStr = strings[0] + "?" + strings[1].split("\\?")[1];
			} else {
				indexStr = strings[0];
			}
		}
		for (int i = 0; i < ruleStr.size(); i++) {
			String str = ruleStr.getString(i);
			if (str.equals(indexStr)) {
				return true;
			}
		}
		return false;
	}

	public void saveUserCache(HttpServletRequest request, Cookie cookie) {
		// final String uuid = CookieUtils.getCookie(request,
		// CacheConstant.FOURCLASS_COOKIE_KEY) != null
		// ? CookieUtils.getCookie(request,
		// CacheConstant.FOURCLASS_COOKIE_KEY).getValue()
		// : null;
		final String uuid = cookie.getValue();
		if (!StringUtils.isBlank(uuid)) {
			final String token = StringUtils.generateUUID() + "@" + DateUtils.getDBString(new Date());
			CacheUtil.saveCacheByPriod(uuid + CacheConstant.EVENT_SESSION_KEY, token,
					CacheConstant.KEY_CACHE_TIMEOUT + "");
			logger.info("传入的缓存中的token值为:" + token);
		}
		logger.info("缓存key值的uuid为:" + uuid);
	}

	protected static String getSessionId(HttpServletRequest request) {
		final Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(CacheConstant.EVENT_COOKIE_KEY)) {
					return cookies[i].getValue();
				}
			}
		}
		return (String) request.getAttribute(CacheConstant.EVENT_COOKIE_KEY);
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
		request.setAttribute(CacheConstant.EVENT_COOKIE_KEY, mySid);
		if (response != null) {
			CookieUtils.addCookie(request, response, CacheConstant.EVENT_COOKIE_KEY, mySid, -1, null);
		}
	}

	public void saveUserCache(HttpServletRequest request) {
		// final String uuid = CookieUtils.getCookie(request,
		// CacheConstant.FOURCLASS_COOKIE_KEY) != null
		// ? CookieUtils.getCookie(request,
		// CacheConstant.FOURCLASS_COOKIE_KEY).getValue()
		// : null;
		final String uuid = getSessionId(request);
		if (!StringUtils.isBlank(uuid)) {
			final String token = StringUtils.generateUUID() + "@" + DateUtils.getDBString(new Date());
			CacheUtil.saveCacheByPriod(uuid + CacheConstant.EVENT_SESSION_KEY, token,
					CacheConstant.KEY_CACHE_TIMEOUT + "");
			logger.info("传入的缓存中的token值为:" + token);
		}
		logger.info("缓存key值的uuid为:" + uuid);
	}
}
