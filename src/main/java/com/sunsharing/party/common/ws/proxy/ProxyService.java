package com.sunsharing.party.common.ws.proxy;


public interface ProxyService {

	public String request(Object[] requestParam, Class[] responseParam)  throws Exception;
}
