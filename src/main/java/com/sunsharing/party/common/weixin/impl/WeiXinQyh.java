package com.sunsharing.party.common.weixin.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.weixin.WeiXinApi;
import com.sunsharing.party.util.CacheUtil;
import com.sunsharing.party.util.HttpSendShort;
import com.sunsharing.party.util.RequestUtils;
import com.sunsharing.party.util.Sign;

/**
 * 微信企业号接口实现类
 * <p>
 * </p>
 * @author wxl 2017年11月14日 上午9:07:02
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月14日
 * @modify by reason:{方法名}:{原因}
 */
public class WeiXinQyh extends WeiXinApi {
	
	private static Logger logger = Logger.getLogger(WeiXinQyh.class);
	// 获取token url
	private final static String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRECT";
	// 验证token有效性 url
	private final static String VALIDATE_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/agent/list?access_token=ACCESS_TOKEN";
	// 增加标签成员url
	private final static String ADD_TAG_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/addtagusers?access_token=ACCESS_TOKEN";
	// 创建标签url
	private final static String CREATE_TAG_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/create?access_token=ACCESS_TOKEN";
	// 获取标签成员url
	private final static String GET_TAG_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=ACCESS_TOKEN&tagid=TAGID";
	// 删除标签成员url
	private final static String DELETE_TAG_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/deltagusers?access_token=ACCESS_TOKEN";
	// 删除标签url
	private final static String DELETE_TAG_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/delete?access_token=ACCESS_TOKEN&tagid=TAGID";
	// 更新标签url
	private final static String UPDATE_TAG_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/update?access_token=ACCESS_TOKEN";
	// 获取标签列表url
	private final static String GET_TAG_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/list?access_token=ACCESS_TOKEN";
	// 批量删除用户url
	private final static String BATCHDELETE_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token=ACCESS_TOKEN";
	// 获取部门列表url
	private final static String GET_DEPTLIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID";
	// 获取成员url
	private final static String GET_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";
	// 获取部门成员url
	private final static String GET_DEPTUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD&status=STATUS";
	// 获取部门成员详情url
	private final static String GET_DEPT_USERINFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD&status=STATUS";
	// 更新成员url
	private final static String UPDATE_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN";
	// 获取code url
	private final static String GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&agentid=AGENTID&state=STATE#wechat_redirect";
	// 根据code获取用户信息 url
	private final static String GET_USER_INFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";
	// 通过API获取管理组JS-SDK凭据 url
	private final static String GET_TICKET_URL = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";
	
	/**
	 * 获取access_token
	 * @author wxl 2017年11月15日 上午11:38:55
	 * @param corpid
	 * @param corpsecret
	 * @return
	 */
	@Override
	public String getAccessToken(String corpid, String corpsecret) {
		logger.info("参数：corpid：" + corpid + " corpsecret :" + corpsecret);
		final String accessToken = (String)CacheUtil.getCache(corpsecret);
		logger.info("从缓存中获取的access_token是：" + accessToken);
		// 验证从缓存中获取的access_token的有效性 如果失效了则重新获取
		if (!validateAccessToken(accessToken)) {
			logger.info("缓存中不存在access_token或者access_token已失效，重新获取access_token");
			// 网络通畅
			final String url = ACCESS_TOKEN_URL.replace("ID", corpid).replace("SECRECT", corpsecret);
			logger.info("获取token的url:" + url);
			final String result = HttpSendShort.getHTML(url, "UTF-8");
			logger.info("result:" + result);
			if (StringUtils.isBlank(result)) {
				logger.error("内部异常返回的结果为空！");
				return result;
			}
			final JSONObject resJsonObject = JSON.parseObject(result);
			final String access_token = resJsonObject.getString("access_token");
			if (StringUtils.isBlank(access_token)) {
				final String errorCode = resJsonObject.getString("errcode");
				final String errorMsg = resJsonObject.getString("errmsg");
				logger.error("获取token失败：错误码为：" + errorCode + " 错误信息： " + errorMsg);
				return errorCode;
			}
			logger.info("企业号- access_token:" + access_token);
			// 存到缓存中
			CacheUtil.saveCacheByPriod(corpsecret, access_token, "7200");
			return access_token;
		}
		
		return accessToken;
	}
	
	/**
	 * 验证token有效性
	 * @author wxl 2017年11月13日 下午4:08:07
	 * @param accessToken
	 * @return
	 */
	private boolean validateAccessToken(String accessToken) {
		if (StringUtils.isBlank(accessToken)) {
			logger.error("access_token为空！");
			return false;
		}
		logger.info("access_token 是：" + accessToken);
		final String result = HttpSendShort.getHTML(VALIDATE_TOKEN_URL.replace("ACCESS_TOKEN", accessToken), "UTF-8");
		logger.info("access_token有效性验证,企业号结果集:result=" + result);
		final JSONObject resJonObject = JSON.parseObject(result);
		final String errcode = resJonObject.getString("errcode");
		logger.info("微信返回的错误码为：errcode=" + errcode);
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
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#getWeiXinUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getWeiXinUser(String userId, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = GET_USER_URL.replace("ACCESS_TOKEN", accessToken).replace("USERID", userId);
		logger.info("获取成员的url:" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("获取成员返回的信息:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#addUserInTag(com.alibaba.fastjson.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject addUserInTag(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = ADD_TAG_USER_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("添加标签成员url:" + url);
		final JSONObject result = HttpSendShort.sendPost(url, jsonObject);
		logger.info("添加标签成员返回的结果：" + result.toString());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#createTag(com.alibaba.fastjson.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject createTag(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = CREATE_TAG_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("创建标签url:" + url);
		final JSONObject result = HttpSendShort.sendPost(url, jsonObject);
		logger.info("创建标签返回的结果：" + result.toString());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#getTagUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getTagUser(String corpid, String corpsecret, String tagId) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = GET_TAG_USER_URL.replace("ACCESS_TOKEN", accessToken).replace("TAGID", tagId);
		logger.info("获取标签成员的url是：" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("获取标签成员:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#delTag(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject delTag(String corpid, String corpsecret, String tagId) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = DELETE_TAG_URL.replace("ACCESS_TOKEN", accessToken).replace("TAG_ID", tagId);
		logger.info("删除标签的url：" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("删除标签:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#updateTag(com.alibaba.fastjson.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject updateTag(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = UPDATE_TAG_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("更新标签名的url:" + url);
		final JSONObject result = HttpSendShort.sendPost(url, jsonObject);
		logger.info("更新标签名称返回的结果:" + result.toJSONString());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#delUserInTag(com.alibaba.fastjson.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject delUserInTag(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = DELETE_TAG_USER_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("删除标签成员url:" + url);
		final JSONObject result = HttpSendShort.sendPost(url, jsonObject);
		logger.info("删除标签成员返回的结果:" + result.toJSONString());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#getTagList(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getTagList(String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = GET_TAG_LIST_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("获取标签列表url:" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("获取标签列表返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#batchDelUser(com.alibaba.fastjson.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject batchDelUser(JSONObject jsonObject, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = BATCHDELETE_USER_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("批量删除成员的url:" + url);
		final JSONObject result = HttpSendShort.sendPost(url, jsonObject);
		logger.info("批量删除成员返回的结果:" + result.toJSONString());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#getDeptList(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getDeptList(String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = GET_DEPTLIST_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("获取部门列表url：" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("获取部门列表返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#getDeptUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getDeptUser(String departmentId, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = GET_DEPTUSER_URL.replace("ACCESS_TOKEN", accessToken).replace("DEPARTMENT_ID", departmentId);
		logger.info("获取部门成员的url：" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("获取部门成员返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#getDeptUserInfo(com.alibaba.fastjson.JSONObject, java.lang.String, java.lang.String, java.lang.String)
	 */
	//
	@Override
	public JSONObject getDeptUserInfo(String corpid, String corpsecret, String departmentId) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = GET_DEPT_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("DEPARTMENT_ID", departmentId);
		logger.info("获取部门用户详细信息的url：" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("获取部门用户详细信息返回的结果:" + resJsonObject.toJSONString());
		return resJsonObject;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#updateWeiXinUser(com.alibaba.fastjson.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject updateWeiXinUser(JSONObject jsonObject, String corpid, String corpsecret) {
		
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = UPDATE_USER_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("更新微信用户的url:" + url);
		final JSONObject result = HttpSendShort.sendPost(url, jsonObject);
		logger.info("批量删除成员返回的结果:" + result.toJSONString());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.WeiXinApi#getUserInfoByCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getUserInfoByCode(String code, String corpid, String corpsecret) {
		final String accessToken = getAccessToken(corpid, corpsecret);
		logger.info("获取到的access_token是：" + accessToken);
		final String url = GET_USER_INFO.replace("ACCESS_TOKEN", accessToken).replace("CODE", code);
		logger.info("根据code获取用户信息的url: " + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		logger.info("返回的结果：" + result);
		final JSONObject resJsonObject = JSON.parseObject(result);
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
		final String url = GET_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("企业号获取票据的url: " + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		logger.info("返回的结果：" + result);
		final JSONObject resJsonObject = JSON.parseObject(result);
		if (!resJsonObject.containsKey("ticket")) {
			logger.info("没有获取到企业号-临时票据！");
			return "noticket";
		}
		ticket = resJsonObject.getString("ticket");
		logger.info("企业号-临时票据ticket:" + ticket);
		// 存入缓存
		CacheUtil.saveCacheByPriod("QYH_TICKET", ticket, "7200");
		return ticket;
	}
	
	/**
	 * 获取企业号签名等信息
	 * @param request
	 * @return
	 */
	public String generateConfigJsonQYH(HttpServletRequest request){
    	String ticket =  getTicket(ConfigParam.CorpID,ConfigParam.Secret);//WeixinQYHUntils.getTicket(WeixinQYHUntils.get_AccessToken(ConfigParam.CorpID, ConfigParam.Secret));////WXClientCacheService.getQYHTicket();// 
    	String fullURL = RequestUtils.getFullRequestURL(ConfigParam.weixinurl,request).getFullURL();//ConfigParam.weiXinUrl+"qyh/go_EmergencyManage.do";
    	System.out.println("地址=============================================="+fullURL);
    	String res = Sign.getRes(ticket, fullURL,  ConfigParam.CorpID);
    	/*String ticket = TicketManager.getTicket(ConfigParam.weixinAppId);
    	String generateConfigJson = JsUtil.generateConfigJson(ticket, false, ConfigParam.weixinAppId, fullURL);*/
        return res ;
    }
	/**
	 * 企业号：获取用户详细信息  
	 * @param wx_userid   微信授权之后获取到的userid
	 * @return
	 */
	public JSONObject getWXUserInfo_BywxUserId(String wx_userid){
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+getAccessToken(ConfigParam.CorpID, ConfigParam.Secret)+"&userid="+wx_userid;
    	String result = HttpSendShort.getHTML(url, "UTF-8");
		JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("企业号-获取用户详细信息  -resJsonObject:"+resJsonObject.toJSONString());
		return resJsonObject;
	}
}
