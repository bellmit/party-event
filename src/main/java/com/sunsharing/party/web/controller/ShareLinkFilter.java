package com.sunsharing.party.web.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.util.ReadJsonFile;

public class ShareLinkFilter extends BaseController implements Filter {

	Logger logger = Logger.getLogger(this.getClass());

	private static final String NO_PERMISSON_URL = "/event/noPermission.do";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// 分享链接校验
		final HttpServletRequest request2 = (HttpServletRequest) request;
		final HttpServletResponse response2 = (HttpServletResponse) response;
		final String pathUrl = request2.getContextPath();
		final String uri = request2.getRequestURI(); // uri就是获取到的连接地址!
		final String path = request2.getServletPath();// 获取servlet的路径
		logger.info("pathUrl=" + pathUrl + ",uri=" + uri + ",path=" + path);
		final String linkUrl = ReadJsonFile.getNoCheckurls("noCheckurls");// 读取的非敏感链接的url
		if (!StringUtils.isBlank(linkUrl) && linkUrl.indexOf(path) != -1) {// 表示有不需要校验的链接
			filterChain.doFilter(request, response);
		} else if (!StringUtils.isBlank(linkUrl) && path.indexOf("/admin/") != -1) {
			filterChain.doFilter(request, response);
		} else {// 表示需要进行分享链接的校验。
			final Map<String, Object> user = getCurUser(request2);
			if (user == null) {// 表示获取不到用户的信息。
				request2.getRequestDispatcher(NO_PERMISSON_URL).forward(request2, response2);
				return;
			} else {
				final String fxTokenUser = (String) user.get("fxToken");
				final String fxToken = request2.getParameter("fxToken");
				logger.info("fxToken=" + fxToken + ",fxTokenUser=" + fxTokenUser);
				if (!StringUtils.isBlank(fxTokenUser)) {
					if (fxTokenUser.equals(fxToken)) {// 表示是本用户操作,分享校验通过。
						filterChain.doFilter(request, response);
						logger.info("分享链接校验通过.........");
					} else {
						request2.getRequestDispatcher(NO_PERMISSON_URL).forward(request2, response2);
						logger.info("token不一致，校验不通过......");
						return;
					}
				} else {// 表示非本用户操作,分享校验不通过。
					logger.info("链接token不存在，校验不通过......");
					request2.getRequestDispatcher(NO_PERMISSON_URL).forward(request2, response2);
					return;
				}
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}
