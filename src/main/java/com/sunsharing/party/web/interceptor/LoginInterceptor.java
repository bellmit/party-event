
/**
 * @(#)LoginInterceptor 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 * 
 *                      <br>
 *                      Copyright: Copyright (c) 2014 <br>
 *                      Company:厦门畅享信息技术有限公司 <br>
 * @author ulyn <br>
 *         14-6-10 下午2:30 <br>
 * @version 1.0 ———————————————————————————————— 修改记录 修改者： 修改时间： 修改原因： ————————————————————————————————
 */
package com.sunsharing.party.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.anno.CheckLogin;
import com.sunsharing.party.common.response.ResponseHelper;
import com.sunsharing.party.constant.CacheConstant;
import com.sunsharing.party.constant.Constant;
import com.sunsharing.party.util.CacheUtil;
import com.sunsharing.party.web.controller.BaseController;

/**
 * <pre></pre>
 * 
 * <br>
 * ---------------------------------------------------------------------- <br>
 * <b>功能描述:</b> <br>
 * 拦截需要登录的注解 CheckLogin,当需要登录时重定向到登录页面 <br>
 * 注意事项: <br>
 * <br>
 * <br>
 * ---------------------------------------------------------------------- <br>
 */
public class LoginInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {
	
	private final Logger logger = Logger.getLogger(LoginInterceptor.class);
	
	private ApplicationContext applicationContext;
	private String loginURL;
	
	private String ipv6LoginURL;
	
	protected static String getSessionId(HttpServletRequest request) {
		final Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("MYSID")) {
					return cookies[i].getValue();
				}
			}
			
		}
		return (String)request.getAttribute("MYSID");
	}
	
	public String getIpv6LoginURL() {
		return ipv6LoginURL;
	}
	
	public String getLoginURL() {
		return loginURL;
	}
	
	/**
	 * 方法前拦截
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final HandlerMethod handlerMethod = (HandlerMethod)handler;
		final CheckLogin checkLogin = handlerMethod.getMethod().getAnnotation(CheckLogin.class);
		if (checkLogin != null) {
			logger.info("对该请求进行校验....");
			// 微信类型企业号(QYH)或者公众号(GZH)
			final String weiXinType = checkLogin.weiXinType();
			// 缓存中的微信类型 企业号(QYH)或者公众号(GZH)
			final String weiXinCacheType = (String)CacheUtil.getCache(Constant.WEI_XIN_TYPE);
			// 验证类型:MC-移动前端 C-门户前端 S-门户后端
			if ("MC".equals(checkLogin.cType())) {
				String userId = "";
				String wx_UserId = BaseController.checkRefferCookie(request, CacheConstant.MEMCACHE_WEIXIN_ID);
				// if (Constant.TEST_ID.equals(wx_UserId)) {
				// wx_UserId = "";
				// userId = Constant.TEST_ID;
				// }
				if (!StringUtils.isBlank(wx_UserId)) {// 增加缓存验证
					userId = (String)CacheUtil.getCache(CacheConstant.MEMCACHE_WEIXIN_ID + wx_UserId);// 获取缓存中是否存在用户
					if (StringUtils.isBlank(userId)) {
						wx_UserId = "";
						BaseController.setRefferCookie(request, response, CacheConstant.MEMCACHE_WEIXIN_ID, wx_UserId);
					}
				}
				// 如果微信用户为空或者两次的微信类型不一样则重新授权
				if (StringUtils.isBlank(wx_UserId) || !weiXinType.equals(weiXinCacheType)) {
					// 缓存微信类型
					CacheUtil.saveCacheByPriod(Constant.WEI_XIN_TYPE, weiXinType, CacheConstant.CACHE_TIMEOUT + "");
					// 微信服务器URL(应用回调地址前缀)
					final StringBuilder sb = new StringBuilder(ConfigParam.weixinurl);
					// 重定向的地址
					StringBuilder redirectURL = new StringBuilder();
					redirectURL.append(checkLogin.cValue());
					String url = request.getRequestURL().toString();
					final String query = request.getQueryString();
					if (!StringUtils.isBlank(query)) {
						url += "?" + query;
					}
					String key = checkLogin.cCode();
					if (!StringUtils.isBlank(key)) {
						key = key + "," + StringUtils.generateUUID();
					}
					
					CacheUtil.saveCacheByPriod(CacheConstant.USER_REQUEST_URL + key, url, CacheConstant.CACHE_TIMEOUT + "");
					
					redirectURL.append("?state=" + key);
					// 如果是开发环境跳过页面授权的步骤
					if (BaseController.getIp(request).indexOf("192.168.1") != -1) {// 模拟获取到微信ID
						wx_UserId = "test";
						BaseController.setRefferCookie(request, response, CacheConstant.MEMCACHE_WEIXIN_ID, wx_UserId);
						CacheUtil.saveCacheByPriod(CacheConstant.MEMCACHE_WEIXIN_ID + wx_UserId, wx_UserId, "300");
						if (Constant.QYH.equals(weiXinType)) {
							sb.append("/login/getWeiXinUserInfo.do?state=" + key + "&wx_UserId=" + wx_UserId);
						} else if (Constant.GZH.equals(weiXinType)) {
							sb.append("/login/getUserByOpenId.do?state=" + key + "&wx_UserId=" + wx_UserId);
						}
						redirectURL = new StringBuilder();
					}
					// 代表是公众号
					if (Constant.GZH.equals(weiXinType)) {
						redirectURL.append("&op=openId");
					}
					
					// 使用js控制跳转的，response.sendRedirect会使得iframe的难受
					final StringBuilder builder = new StringBuilder();
					builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
					builder.append("if(top.window.location.pathname==window.location.pathname){");
					builder.append("top.location.href='");
					builder.append(sb + redirectURL.toString());
					builder.append("';}else{ top.location.href= '" + sb
					        + "'+encodeURIComponent(top.window.location.href); }</script>");
					logger.info("redirect:" + builder.toString());
					ResponseHelper.printOutContent(response, builder.toString());
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public void setIpv6LoginURL(String ipv6LoginURL) {
		this.ipv6LoginURL = ipv6LoginURL;
	}
	
	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}
	
}
