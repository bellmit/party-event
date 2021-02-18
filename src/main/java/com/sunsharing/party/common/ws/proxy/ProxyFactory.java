package com.sunsharing.party.common.ws.proxy;

import org.apache.log4j.Logger;

public class ProxyFactory {

	static Logger logger = Logger.getLogger(ProxyFactory.class);
	
	private static ProxyService proxyService;
	
	public static ProxyService getInstance(String beanName){
		if(proxyService==null){
			Class<?> clazz;
			try {
				clazz = Class.forName("com.sunsharing.party.common.ws.proxy.impl."+beanName);
				proxyService = (ProxyService) clazz.newInstance();
			} catch (Exception e) {
				logger.error("初始化类失败:",e);
			}
		}
		return proxyService;
	}
}

