package com.sunsharing.party.common.ws;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sunsharing.party.common.model.ws.enumbean.ServerEnum;
import com.sunsharing.party.common.servlet.RequestContextClientLogServlet;
import com.sunsharing.party.common.ws.proxy.ProxyFactory;
import com.sunsharing.party.common.ws.proxy.ProxyService;

public class AirRpcClient {

	public static Long WS_TIMEOUT = 60 * 1000l; // 60秒

	static Logger logger = Logger.getLogger(AirRpcClient.class);

	String targetUrl = "http://localhost:8080/services/openService";

	public static Map<String, Object> parMap = new HashMap<String, Object>();

	private static long num = 10000;

	public static synchronized long getSeqNum() {
		long seq = 0;
		if (num >= 99999) {
			num = 10000;
		}
		seq = num++;
		return seq;
	}

	public static String getTransNum() {

		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmssS");
		String timestamp = df.format(new Date());
		StringBuilder sb = new StringBuilder("APPID").append(timestamp).append(getSeqNum());
		return sb.toString();
	}

	public AirRpcClient(String targetUrl) {
		System.out.println(targetUrl);
		this.targetUrl = targetUrl;
	}

	public String request(String serviceId, String methodName, String paramJson) throws Exception {

		String ip = "", userName = "";
		String transNum = getTransNum();
		HttpServletRequest request = null;
		try {
			Map<String, Object> requestMap = RequestContextClientLogServlet.getRequestMap();
			if (requestMap != null) {
				ip = (String) requestMap.get("ip");
				userName = (String) requestMap.get("userName");
				// request = (HttpServletRequest)requestMap.get("request");
			}
		} catch (Exception e) {
			logger.error("threadlocal获取错误:", e);
		}
		// String url = request!=null?request.getRequestURI():"";
		// logger.info("IP:"+ip+",userName:"+userName+",transNum:"+transNum+",serviceId:"+serviceId+",methodName:"+methodName+",paramJson:"+paramJson+",request:"+url);
		// 指定方法的参数值
		Object[] requestParam = new Object[] { userName, ip, transNum, serviceId, methodName, paramJson, "1" };
		// 指定方法返回值的数据类型的Class对象
		Class[] responseParam = new Class[] { String.class, String.class, String.class, String.class, String.class,
				String.class, String.class };
		// 指定要调用的getGreeting方法及WSDL文件的命名空间
		// QName requestMethod = new QName("http://ws.air.ihome.sunsharing.com",
		// "request");
		// 调用方法并输出该方法的返回值
		// String result = (String)serviceClient.invokeBlocking(requestMethod,
		// requestParam, responseParam)[0];
		String proxyType = (String) parMap.get("proxyType");
		// parMap.put("targetUrl", targetUrl);
		// parMap.put("timeOut", WS_TIMEOUT);
		// parMap.put("requestParam",requestParam);
		// parMap.put("responseParam",responseParam);
		// ProxyService service =
		// (ProxyService)AirClientContext.getBean(ProxyService.class);
		ProxyService service = ProxyFactory.getInstance(ServerEnum.getEnumValue(proxyType));
		String result = service.request(requestParam, responseParam);
		logger.debug("远程调用返回：" + result);
		return result;
	}
}
