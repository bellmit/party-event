package com.sunsharing.party.ws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.common.servlet.RequestContextServerLogServlet;
import com.sunsharing.party.util.RequestUtils;
import com.sunsharing.party.common.ws.SimplePropertyFilter;
import com.sunsharing.party.ServiceLocator;
import com.sunsharing.party.ws.utils.JsonCommon;
import com.sunsharing.ihome.air.service.log.LogServerHandlerAOP;

@Component
public class OpenService {
	
	Logger logger = Logger.getLogger(OpenService.class);
	private ApplicationContext ctx = null;
	private static final Object lock = new Object();
	
	public OpenService() {
		logger.info("OpenService init !!");
	}
	
	/*
	 * public static void main(String[] args) { OpenService.getMethod("executeQuery", DataBaseDao.class, "[\"SELECT * FROM jrun_settings\",true,[]]"); // try {
	 * // OpenService.getMethodParamTypes(DataBaseDao.class,"forName"); // } catch (ClassNotFoundException e) { // TODO Auto-generated catch block //
	 * e.printStackTrace(); // } }
	 */
	private String getErrorResult(String msg) {
		JSONObject jo = new JSONObject();
		jo.put("status", false);
		jo.put("msg", msg);
		return JSON.toJSONString(jo, SerializerFeature.WriteMapNullValue);
	}
	
	private String getResult(Object result) {
		JSONObject jo = new JSONObject();
		jo.put("status", true);
		jo.put("result", JSON.toJSONString(result, new SimplePropertyFilter()));
		return JSON.toJSONString(jo, SerializerFeature.WriteMapNullValue);
	}
	
	/**
	 * 公用ws服务
	 * 
	 * @param userName 互联网用户帐号
	 * @param ip 所在IP
	 * @param transNum 业务流水号
	 * @param serviceId serviceId，接口注解的id
	 * @param methodName 调用的方法名,服务方法制定不要使用重载
	 * @param paramJson 当无入参时，此值为空，当有入参时，参数对象的json串，json的value值为空值的不能省略 ，fastjson默认会去掉空值，使用要注意
	 * @param digestCode 校验码，合法访问
	 * @return ${success} {"status":true,"result":""} ${error} {"status":false,"msg":"没有指定的serviceName=testService"}
	 */
	public String request(String userName, String ip, String transNum, String serviceId, String methodName, String paramJson,
	        String digestCode) {
		logger.info("收到入参：serviceId=" + serviceId + "&methodName=" + methodName + "&paramJson=" + paramJson);
		try {
			if (StringUtils.isBlank(serviceId)) {
				return getErrorResult("serviceId入参不能为空");
			}
			if (StringUtils.isBlank(methodName)) {
				return getErrorResult("methodName入参不能为空");
			}
			Object o = ServiceLocator.getBean(serviceId + "Impl");
			if (o == null) {
				return getErrorResult("没有指定的serviceId=" + serviceId);
			}
			Method method = JsonCommon.getMethod(methodName, o, paramJson);
			if (method == null) {
				return getErrorResult(serviceId + "没有指定的methodName=" + methodName);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userName", userName);
			map.put("serviceId", serviceId);
			// ======================= 获取spring context实列 ===============//
			if (ctx == null) {
				synchronized (lock) {
					MessageContext mc = MessageContext.getCurrentMessageContext();
					HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
					ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
					LogServerHandlerAOP.ctx = ctx;
				}
			}
			// ======================== 防止客户端传过来的IP为空 start========================= //
			if (StringUtils.isBlank(ip)) {
				MessageContext mc = MessageContext.getCurrentMessageContext();
				HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
				ip = RequestUtils.getIpAddr(request);
			}
			// ======================== 防止客户端传过来的IP为空 end========================= //
			
			map.put("ip", ip);
			RequestContextServerLogServlet.setRequestMap(map);
			Object[] arguments = null;
			if (!StringUtils.isBlank(paramJson) && !"[\"\"]".equals(paramJson) && !"null".equals(paramJson)) {
				// JSON.toJSONString(map, SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue)
				arguments = JSON.parseArray(paramJson, method.getGenericParameterTypes()).toArray();
			}
			Object result = null;
			result = method.invoke(o, arguments);
			return getResult(result);
		} catch (InvocationTargetException e) {
			logger.error("处理服务InvocationTargetException异常", e);
			return getErrorResult("内部处理异常：" + e.getTargetException().getMessage());
		} catch (Exception e) {
			logger.error("处理服务异常", e);
			return getErrorResult("内部处理异常：" + e.getMessage());
		}
	}
}
