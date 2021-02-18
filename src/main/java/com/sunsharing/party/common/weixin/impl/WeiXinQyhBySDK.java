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
 * 微信企业号通过协同平台实现类
 * <p>
 * </p>
 * @author wxl 2017年11月21日 下午1:13:55
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月21日
 * @modify by reason:{方法名}:{原因}
 */
public class WeiXinQyhBySDK extends WeiXinApi {
	
	private static Logger logger = Logger.getLogger(WeiXinQyhBySDK.class);
	
	@Override
	public String getAccessToken(String corpid, String corpsecret) {
		final String accessToken = (String)CacheUtil.getCache(corpsecret);
		logger.info("从缓存中获取的access_token是：" + accessToken);
		// 验证从缓存中获取的access_token的有效性 如果失效了则重新获取
		if (!validateAccessToken(accessToken)) {
			// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
			final Map<String, Object> params = new HashMap<>();
			params.put("corpid", corpid);
			params.put("corpsecret", corpsecret);
			logger.info("当前接口的服务id是： collagen_service_id1 请联系实施配置对应服务id");
			final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID1, "GET");
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
				CacheUtil.saveCacheByPriod(corpsecret, access_token, "7200");
				return access_token;
			}
		}
		
		return accessToken;
	}
	
	/**
	 * 验证token有效性
	 * @author wxl 2017年11月13日 下午4:08:07
	 * @param accessToken
	 * @return
	 */
	public boolean validateAccessToken(String accessToken) {
		if (StringUtils.isBlank(accessToken)) {
			logger.error("access_token为空！");
			return false;
		}
		logger.info("access_token 是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		logger.info("当前接口的服务id是： collagen_service_id2 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID2, "GET");
		logger.info("验证access_token从协同平台返回的结果" + xtResult);
		if (StringUtils.isBlank(xtResult)) {
			logger.info("返回的结果为空！");
			return false;
		}
		final JSONObject resJonObject = JSON.parseObject(xtResult);
		final String errcode = resJonObject.getString("errcode");
		if (!"0".equals(errcode)) {// 无效
			logger.info("access_token值无效");
			return false;
		} else if (StringUtils.isBlank(errcode)) {
			logger.info("access_token值无效");
			return false;
		} else {// 有效
			logger.info("access_token值有效");
			return true;
		}
		
	}
	
	@Override
	public JSONObject getWeiXinUser(String userId, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("userid", userId);
		logger.info("当前接口的服务id是： collagen_service_id3 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID3, "GET");
		logger.info("获取用户信息从协同平台返回的结果：" + xtResult);
		if (!StringUtils.isBlank(xtResult)) {
			final JSONObject resJsonObject = JSON.parseObject(xtResult);
			return resJsonObject;
		}
		return null;
	}
	
	@Override
	public JSONObject addUserInTag(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("BODY", jsonObject);
		logger.info("当前接口的服务id是： collagen_service_id4 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID4, "POST");
		logger.info("向标签中添加用户返回的结果：" + xtResult);
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		return resJsonObject;
	}
	
	@Override
	public JSONObject createTag(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("BODY", jsonObject);
		logger.info("当前接口的服务id是： collagen_service_id5 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID5, "POST");
		logger.info("创建标签返回的结果：" + xtResult);
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		return resJsonObject;
	}
	
	@Override
	public JSONObject getTagUser(String corpid, String corpsecret, String tagId) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("tagid", tagId);
		logger.info("当前接口的服务id是： collagen_service_id6 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID6, "GET");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("获取标签成员:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject delTag(String corpid, String corpsecret, String tagId) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("tagid", tagId);
		logger.info("当前接口的服务id是： collagen_service_id7 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID7, "GET");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("删除标签:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject updateTag(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("BODY", jsonObject);
		logger.info("当前接口的服务id是： collagen_service_id8 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID8, "POST");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("更新标签名称返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject delUserInTag(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("BODY", jsonObject);
		logger.info("当前接口的服务id是： collagen_service_id9 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID9, "POST");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("删除标签成员返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject getTagList(String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		logger.info("当前接口的服务id是： collagen_service_id10 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID10, "GET");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("获取标签列表返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject batchDelUser(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("BODY", jsonObject);
		logger.info("当前接口的服务id是： collagen_service_id11 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID11, "POST");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("批量删除成员返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject getDeptList(String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		logger.info("当前接口的服务id是： collagen_service_id12 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID12, "GET");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("获取部门列表返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject getDeptUser(String departmentId, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("department_id", departmentId);
		logger.info("当前接口的服务id是： collagen_service_id13 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID13, "GET");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("获取部门成员返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject getDeptUserInfo(String corpid, String corpsecret, String departmentId) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("department_id", departmentId);
		logger.info("当前接口的服务id是： collagen_service_id14 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID14, "GET");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("获取部门用户详细信息返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject updateWeiXinUser(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		params.put("BODY", jsonObject);
		logger.info("当前接口的服务id是： collagen_service_id15 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID15, "POST");
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		logger.info("更新成员成员返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	@Override
	public JSONObject getUserInfoByCode(String code, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		logger.info("当前接口的服务id是： collagen_service_id16 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID16, "GET");
		logger.info("返回的结果：" + xtResult);
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		return resJsonObject;
	}
	
	@Override
	public String getTicket(String appid, String secret) {
		String ticket = "";
		// 从缓存中获取票据
		ticket = (String)CacheUtil.getCache("QYH_TICKET");
		if (!StringUtils.isBlank(ticket)) {
			logger.info("从缓存中获取的企业号票据是: " + ticket);
			return ticket;
		}
		final String accessToken = getAccessToken(appid, secret);
		logger.info("获取到的access_token是：" + accessToken);
		// 放入集合的参数名和请求链接名中的参数要一样，如果为POST请求参数中必须包含一个名为"BODY"的key，value为json对象
		final Map<String, Object> params = new HashMap<>();
		params.put("access_token", accessToken);
		logger.info("当前接口的服务id是： collagen_service_id17 请联系实施配置对应服务id");
		final String xtResult = SDKJAVAUntils.callWxInterface(params, ConfigParam.CollaGEN_Service_ID17, "GET");
		logger.info("获取ticket返回的结果：" + xtResult);
		final JSONObject resJsonObject = JSON.parseObject(xtResult);
		if (!resJsonObject.containsKey("ticket")) {
			logger.info("从协同平台没有获取到企业号-临时票据！");
			return "noticket";
		}
		ticket = resJsonObject.getString("ticket");
		logger.info("企业号-临时票据ticket:" + ticket);
		// 存入缓存
		CacheUtil.saveCacheByPriod("QYH_TICKET", ticket, "7200");
		return ticket;
	}
	
}
