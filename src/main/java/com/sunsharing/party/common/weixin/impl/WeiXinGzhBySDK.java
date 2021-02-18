package com.sunsharing.party.common.weixin.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.weixin.WeiXinApi;
import com.sunsharing.party.common.weixin.util.SDKJAVAUntils;
import com.sunsharing.party.util.CacheUtil;

/**
 * 微信公众号接口通过协同平台实现类
 * <p>
 * </p>
 * @author wxl 2017年11月21日 下午1:12:54
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月21日
 * @modify by reason:{方法名}:{原因}
 */
public class WeiXinGzhBySDK extends WeiXinApi {
	
	private static Logger logger = Logger.getLogger(WeiXinGzhBySDK.class);
	
	@Override
	public String getAccessToken(String appid, String secret) {
		final String accessToken = (String)CacheUtil.getCache(secret);
		logger.info("从缓存中获取的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("appid", appid);
		params.put("secret", secret);
		logger.info("当前接口的服务id是： collagen_service_id18 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID18, "GET");
		logger.info("从协同平台获取access_token返回的结果：" + xtResult);
		if (StringUtils.isBlank(xtResult)) {
			logger.info("从协同平台没有获取到access_token");
			return "{\"error\":\"noAccess_token\"}";
		}
		final JSONObject json = JSON.parseObject(xtResult);
		if (json.containsKey("access_token")) {
			final String access_token = json.getString("access_token");
			logger.info("从协同平台获取到的企业号access_token" + access_token);
			// 存到缓存中
			CacheUtil.saveCacheByPriod(secret, access_token, "7200");
			return access_token;
		}
		return "{\"error\":\"noAccess_token\"}";
		
	}
	
	@Override
	public JSONObject sendNewsByOpenId(JSONObject param, String appid, String secret) {
		final String accessToken = getAccessToken(appid, secret);
		logger.info("群发消息获取的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("BODY", param);
		logger.info("当前接口的服务id是： collagen_service_id19 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID19, "POST");
		final JSONObject resJonObject = JSON.parseObject(xtResult);
		logger.info("群发消息返回的结果：" + resJonObject.toJSONString());
		return resJonObject;
	}
	
	@Override
	public JSONObject getOpenIdByCode(String appid, String secret, String code) {
		logger.info("参数：appid:" + appid + " secret:" + secret + " code:" + code);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("appid", appid);
		params.put("secret", secret);
		params.put("code", code);
		logger.info("当前接口的服务id是： collagen_service_id20 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID20, "GET");
		final JSONObject jobj = JSON.parseObject(xtResult);
		logger.info("根据code获取openid 返回结果:" + jobj.toJSONString());
		return jobj;
	}
	
	@Override
	public String getTicket(String appid, String secret) {
		String ticket = "";
		// 从缓存中获取票据
		ticket = (String)CacheUtil.getCache("QYH_TICKET");
		if (!StringUtils.isBlank(ticket)) {
			logger.info("从缓存中获取的公众号号票据是: " + ticket);
			return ticket;
		}
		logger.info("参数：appid:" + appid + " secret:" + secret);
		final String accessToken = getAccessToken(appid, secret);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		logger.info("当前接口的服务id是： collagen_service_id21  请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID21, "GET");
		logger.info("获取票据返回的结果：" + xtResult);
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		ticket = resJsonObject.getString("ticket");
		// 存入缓存
		CacheUtil.saveCacheByPriod("GZH_TICKET", ticket, "7200");
		return ticket;
	}
	
	@Override
	public JSONObject getUserInfo(String accessToken, String openId) {
		logger.info("参数：accessToken:" + accessToken + " openId:" + openId);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("openid", openId);
		logger.info("当前接口的服务id是： collagen_service_id22  请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID22, "GET");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("获取用户信息返回结果：" + xtResult);
		return resJsonObject;
	}
	
	@Override
	public JSONObject refreshToken(String appid, String refresh_token) {
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("appid", appid);
		params.put("grant_type", "refresh_token");// 授权类型固定为refresh_token
		params.put("refresh_token", refresh_token);
		logger.info("当前接口的服务id是： collagen_service_id23  请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID23, "GET");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		return resJsonObject;
	}
	
}
