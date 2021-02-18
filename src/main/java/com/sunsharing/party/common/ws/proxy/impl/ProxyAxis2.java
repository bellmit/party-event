package com.sunsharing.party.common.ws.proxy.impl;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.log4j.Logger;

import com.sunsharing.party.common.ws.AirRpcClient;
import com.sunsharing.party.common.ws.proxy.ProxyService;

public class ProxyAxis2 implements ProxyService {
	
	static Logger logger = Logger.getLogger(ProxyAxis2.class);
	
	@Override
	public String request(Object[] requestParam, Class[] responseParam) throws AxisFault {
		// 使用RPC方式调用WebService
		RPCServiceClient serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();
		// 设置客户端调用ws的超时时间
		long timeOut = (Long)AirRpcClient.parMap.get("timeOut");
		options.setTimeOutInMilliSeconds(timeOut);
		// 指定调用WebService的URL
		String targetUrl = (String)AirRpcClient.parMap.get("targetUrl");
		EndpointReference targetEPR = new EndpointReference(targetUrl);
		options.setTo(targetEPR);
		// 指定要调用的getGreeting方法及WSDL文件的命名空间
		QName requestMethod = new QName("http://ws.party.sunsharing.com", "request");
		// 调用方法并输出该方法的返回值
		// Object[] requestParam = (Object[])AirRpcClient.parMap.get("requestParam");
		// Class[] responseParam = (Class[])AirRpcClient.parMap.get("responseParam");
		String result = (String)serviceClient.invokeBlocking(requestMethod, requestParam, responseParam)[0];
		logger.debug("远程调用返回：" + result);
		
		// 关闭资源 不然会有可能 导致 throws AxisFault： too many files
		serviceClient.cleanupTransport();
		serviceClient.cleanup();
		serviceClient = null;
		
		return result;
	}
	
}
