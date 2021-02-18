package com.sunsharing.party.common.servlet;

import java.io.Serializable;
import java.util.Map;

public class RequestContextClientLogServlet implements Serializable{
	private static ThreadLocal<Map<String, Object>> requestLocal = new ThreadLocal<Map<String,Object>>();
    public static Map<String, Object> getRequestMap() {
        return requestLocal.get();
    }

    public static void setRequestMap(Map<String, Object> map) {
        requestLocal.set(map);
    }
    public void cleanContext(){ 
    	requestLocal.set(null); 
    } 
}
