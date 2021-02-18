package com.sunsharing.party.util;

import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.msgcenter.productClient.MsgSendClient;

public class MsgSendUtil {

    static Logger logger = Logger.getLogger(MsgSendUtil.class);
	/**
	 * 调用消息中心接口
	 * @param parMap
	 * @return
	 */
	public static boolean MsgSend(Map<String, Object> parMap){
        MsgSendClient client = new MsgSendClient();
        JSONObject obj = new JSONObject();
//        JSONObject obj1 = new JSONObject();@_@leader
        String userId = (String)parMap.get("userId");
        obj.put("topic","WX_QYHMSG");
        obj.put("expires","10");
        obj.put("isLeader", userId.endsWith("@_@leader"));
        obj.put("userId", userId.replace("@_@leader", ""));
        obj.put("content", parMap.get("content"));
//        obj.put("params",obj1.toJSONString());
        logger.info("向消息中心发送请求:"+obj.toJSONString());
        return client.sendMsg(obj.toJSONString());
	}
	/**
	 * 调用消息中心接口
	 * @param parMap
	 * @return
	 */
	public static boolean MsgSend(String topic,JSONObject params){
		MsgSendClient client = new MsgSendClient();
		JSONObject obj = new JSONObject();
		obj.put("topic",topic);
		obj.put("expires","10");
		obj.put("method", params.get("method"));
		obj.put("value", params.get("value"));
        logger.info("向消息中心发送请求:"+obj.toJSONString());
        return client.sendMsg(obj.toJSONString());
	}
}
