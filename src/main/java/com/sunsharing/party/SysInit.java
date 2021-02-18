package com.sunsharing.party;

import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sunsharing.component.resvalidate.config.ConfigContext;
import com.sunsharing.ihome.air.common.ws.AirClientContext;
import com.sunsharing.memCache.Config;
import com.sunsharing.party.common.dfs.FileFactory;
import com.sunsharing.party.common.ws.AirRpcClient;
import com.sunsharing.transport.exchage.client.RetryServer;

public class SysInit extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 96053663483983931L;

	/** 记录日志 */
	private static Logger logger = Logger.getLogger(SysInit.class);

	Timer timer = new Timer();

	public void init() {
		ServletContext sc = this.getServletContext();
		logger.info("系统开始初始化...");
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		ServiceLocator.init(ctx);
		logger.info("系统初始化上下文结束...");

		logger.info("初始化其它参数...");

		// 初始化配置文件信息和数据库全局参数信息
		ConfigContext.instancesBean(ConfigParam.class);

		RetryServer.getInstance().startRetryServer();

		String memHost = ConfigParam.memcachedHost;
		String sysTimeOut = ConfigParam.sysTimeOut;
		Config.getInstance().init(memHost, sysTimeOut);

		// 初始化文件系统
		FileFactory.init(ConfigParam.fileServerType, ConfigParam.fileServerJsfPath, ConfigParam.fileServerDfsRealIp,
				ConfigParam.fileServerDfsProxyIp, ConfigParam.fileServerDfsPort, ConfigParam.localIp,
				ConfigParam.hadoopUri, ConfigParam.hadoopAccount);
		// 初始化系统服务
		new AirClientContext("com.sunsharing.party", ConfigParam.airTargetUrl).init();

		AirRpcClient.WS_TIMEOUT = ConfigParam.optionTimeout != null ? Long.valueOf(ConfigParam.optionTimeout)
				: AirRpcClient.WS_TIMEOUT;
		AirRpcClient.parMap.put("timeOut", AirRpcClient.WS_TIMEOUT);
		AirRpcClient.parMap.put("proxyType", ConfigParam.proxyType);
		AirRpcClient.parMap.put("targetUrl", ConfigParam.airTargetUrl);
	}

	@Override
	public void destroy() {
		super.destroy();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
