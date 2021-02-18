package com.sunsharing.party.common.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sunsharing.component.ulynlist.ParameterConstant;
import com.sunsharing.component.ulynlist.executor.SupportDbTypeEnum;
import com.sunsharing.component.ulynlist.model.BusinessData;
import com.sunsharing.component.ulynlist.utils.ResponseHelper;
import com.sunsharing.component.ulynlist.utils.StringUtils;
import com.sunsharing.ihome.air.common.ws.AirClientContext;
import com.sunsharing.memCache.Session;
import com.sunsharing.party.common.api.UlynlistDbExecutor;
import com.sunsharing.party.constant.CacheConstant;

/**
 * <pre></pre>
 * 
 * <br>
 * ---------------------------------------------------------------------- <br>
 * <b>功能描述:</b> <br>
 * 针对列表ulynlist.js的请求的处理Servlet <br>
 * 注意事项: <br>
 * <br>
 * <br>
 * ---------------------------------------------------------------------- <br>
 */
public class UlynlistAdapterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(UlynlistAdapterServlet.class);

	SupportDbTypeEnum dbTypeEnum = SupportDbTypeEnum.getDefaultDbType();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UlynlistAdapterServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// 自动注入的支持
		// SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
		// config.getServletContext());
		final String dbType = config.getInitParameter("dbType");
		if (dbType != null) {
			try {
				dbTypeEnum = SupportDbTypeEnum.valueOf(dbType);
			} catch (final Exception e) {
				final String errorMsg = "UlynlistServlet的配置dbType设置错误，支持的类型有："
						+ SupportDbTypeEnum.getSupportDbTypeStr();
				logger.error(errorMsg, e);
				throw new ServletException(
						"UlynlistServlet的配置dbType设置错误，支持的类型有：" + SupportDbTypeEnum.getSupportDbTypeStr());
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			final Map<String, String> paramMap = new HashMap<String, String>();
			// 从paramter中取值
			final Map<String, String[]> paramMaps = request.getParameterMap();
			for (final Map.Entry<String, String[]> param : paramMaps.entrySet()) {
				if (param.getKey().endsWith("Date") || param.getKey().endsWith("Time")) {
					paramMap.put(param.getKey(), StringUtils.toStoreStr(param.getValue()[0]));
				} else {
					paramMap.put(param.getKey(), param.getValue()[0]);
				}
			}
			if (!paramMap.containsKey(ParameterConstant.LINES_PER_PAGE)) {
				paramMap.put(ParameterConstant.LINES_PER_PAGE,
						StringUtils.getObjectToString(request.getAttribute(ParameterConstant.LINES_PER_PAGE)));
			}
			if (!paramMap.containsKey(ParameterConstant.CURRENT_PAGE)) {
				paramMap.put(ParameterConstant.CURRENT_PAGE,
						StringUtils.getObjectToString(request.getAttribute(ParameterConstant.CURRENT_PAGE)));
			}
			String listSqlKey = StringUtils.getObjectToString(request.getAttribute(ParameterConstant.LIST_SQL));
			if (StringUtils.isNullBlank(listSqlKey)) {
				listSqlKey = request.getParameter(ParameterConstant.LIST_SQL);
			}

			final UlynlistDbExecutor dbExecutor = AirClientContext.getBean(UlynlistDbExecutor.class);

			// paramMap预置入会话的用户信息字段
			final Object user = Session.getInstance().getAttribute(request, CacheConstant.SESSION_USER);
			if (user != null) {
				final Map userMap = (Map) user;
				final String userId = (String) userMap.get("user_id");
				paramMap.put("sessionUserId", userId);
				final String userStreetId = (String) userMap.get("user_streetId");
				paramMap.put("sessionUserStreetId", userStreetId);
				final String userCommunityId = (String) userMap.get("user_communityId");
				paramMap.put("sessionUserCommunityId", userCommunityId);
			}

			final BusinessData o = dbExecutor.getListAndTotalCount(paramMap, listSqlKey, dbTypeEnum.name());
			ResponseHelper.printOut(response, o);
		} catch (final Exception e) {
			logger.error("获取列表内部处理异常：" + e.getMessage(), e);
			ResponseHelper.printOutBusinessError(response, "内部处理异常");
		}
	}

}
