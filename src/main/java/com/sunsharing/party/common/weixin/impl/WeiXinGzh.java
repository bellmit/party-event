package com.sunsharing.party.common.weixin.impl;

import java.util.ArrayList;
import java.util.List;

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
 * 微信公众号接口实现类
 * <p>
 * </p>
 * @author wxl 2017年11月14日 上午9:13:43
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月14日
 * @modify by reason:{方法名}:{原因}
 */
public class WeiXinGzh extends WeiXinApi {
	
	private static Logger logger = Logger.getLogger(WeiXinGzh.class);
	// 获取token url
	private final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 验证access_token有效性
	private final static String VALIDATE_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=rr";
	// 根据openid群发消息
	private final static String SEND_OPENID_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
	// 生成签名
	private final static String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	// 根据openid和access_token获取用户信息
	private final static String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	// 通过code换取网页授权access_token
	private final static String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code ";
	// 查询微信关注者所有openid
	private final static String GET_OPENIDLIST_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=&next_openid=NEXT_OPENID";
	// 刷新access_token(此access_token不同于基础支持的access_token)
	private final static String REFRESH_TOKEN_URL = "ttps://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN ";
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.TT#getAccessToken(java.lang.String, java.lang.String)
	 */
	@Override
	public String getAccessToken(String appid, String secret) {
		logger.info("参数：appid:" + appid + " secret:" + secret);
		final String accessToken = (String)CacheUtil.getCache(secret);
		logger.info("从缓存中获取的access_token是：" + accessToken);
		// 验证从缓存中获取的access_token的有效性 如果失效了则重新获取
		if (!validateAccessToken(accessToken)) {
			logger.info("缓存中不存在access_token或者access_token已失效，重新获取access_token");
			final String url = ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", secret);
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
			logger.info("公众号- access_token:" + access_token);
			// 存到缓存中
			CacheUtil.saveCacheByPriod(secret, access_token, "7200");
			return access_token;
		}
		
		return accessToken;
	};
	
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
		logger.info("access_token有效性验证,公众号结果集:result=" + result);
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
	
	/**
	 * 查询微信关注者所有openid
	 * 
	 * @param next_openid_init
	 * @return JSONObject
	 */
	static String openidListString = "";
	static List<String> openList = new ArrayList<String>();
	
	/**
	 * 查询微信关注者所有openid
	 * @author wxl 2017年11月14日 上午9:36:10
	 * @param next_openid_init
	 * @return
	 */
	public static JSONObject getOpenIdList(String next_openid_init) {
		if (StringUtils.isBlank(next_openid_init)) {
			openidListString = "";
			openList.clear();
		}
		logger.info("查询微信关注者所有openid ,next_openid_init:" + next_openid_init);
		final String httpUrl = GET_OPENIDLIST_URL.replace("NEXT_OPENID", next_openid_init);
		final String result = HttpSendShort.getHTML(httpUrl, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		// count 单次查询统计出来的openid数量
		final String count = resJsonObject.get("count").toString();
		if (!"0".equals(count) && count != "0") {
			String tempOpenid = ((JSONObject)resJsonObject.get("data")).getString("openid");
			tempOpenid = "," + tempOpenid.substring(1, tempOpenid.length() - 1);
			tempOpenid = tempOpenid.replace("\"", "");
			openidListString += tempOpenid;
			
		}
		final String next_openid = resJsonObject.get("next_openid").toString();
		
		// 递归调用 查询openid
		if (next_openid != null && next_openid != "" && !"".equals(next_openid)) {
			getOpenIdList(next_openid);
		}
		
		final JSONObject resObject = new JSONObject();
		
		final String[] split = openidListString.split(",");
		if (split.length > 20) {
			for (int i = 1; i < split.length; i++) {
				openList.add(split[i]);
			}
		}
		resObject.put("touser", openList);
		
		logger.info("所有微信关注者的openid：" + resObject.toJSONString());
		return resObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.TT#sendNewsByOpenId(com.alibaba.fastjson.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject sendNewsByOpenId(JSONObject param, String appid, String secret) {
		final String accessToken = getAccessToken(appid, secret);
		logger.info("群发消息获取的access_token是：" + accessToken);
		final String httpUrl = SEND_OPENID_NEWS_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("群发消息获取的请求的url是：" + httpUrl);
		final JSONObject resObject = HttpSendShort.sendPost(httpUrl, param);
		logger.info("群发消息返回的结果：" + resObject.toJSONString());
		return resObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.TT#getOpenIdByCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getOpenIdByCode(String appid, String secret, String code) {
		logger.info("参数：appid:" + appid + " secret:" + secret + " code:" + code);
		final String httpUrl = GET_ACCESS_TOKEN_URL.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
		logger.info(" 根据code获取openid的url:" + httpUrl);
		final String result = HttpSendShort.getHTML(httpUrl, "UTF-8");
		final JSONObject jobj = JSON.parseObject(result);
		logger.info("根据code获取openid 返回结果:" + jobj.toJSONString());
		return jobj;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.TT#getTicket(java.lang.String, java.lang.String)
	 */
	@Override
	public String getTicket(String appid, String secret) {
		String ticket = "";
		// 从缓存中获取票据
		ticket = (String)CacheUtil.getCache("GZH_TICKET");
		if (!StringUtils.isBlank(ticket)) {
			logger.info("从缓存中获取的公众号票据是: " + ticket);
			return ticket;
		}
		final String accessToken = getAccessToken(appid, secret);
		final String url = GET_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("获取票据的url:" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		logger.info("获取票据返回的结果：" + result);
		final JSONObject resJsonObject = JSON.parseObject(result);
		ticket = resJsonObject.getString("ticket");
		// 存入缓存
		CacheUtil.saveCacheByPriod("GZH_TICKET", ticket, "7200");
		return ticket;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.party.util.weixin.TT#getUserInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getUserInfo(String accessToken, String openId) {
		final String url = GET_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		logger.info("获取用户信息的url:" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		logger.info("获取用户信息返回结果：" + result);
		return resJsonObject;
	}
	
	@Override
	public JSONObject refreshToken(String appid, String refresh_token) {
		final String url = REFRESH_TOKEN_URL.replace("APPID", appid).replace("REFRESH_TOKEN ", refresh_token);
		logger.info("刷新access_token的url:" + url);
		final String result = HttpSendShort.getHTML(url, "UTF-8");
		final JSONObject resJsonObject = JSON.parseObject(result);
		return resJsonObject;
	}
	@Override
	public String generateConfigJson(HttpServletRequest request){
    	String ticket = getTicket(ConfigParam.weixinAppId,ConfigParam.weixinSecretKey);//WeixinQYHUntils.getTicket(WeixinQYHUntils.get_AccessToken(ConfigParam.CorpID, ConfigParam.Secret));////WXClientCacheService.getQYHTicket();// 
    	String fullURL = RequestUtils.getFullRequestURL(ConfigParam.weixinurl,request).getFullURL();//ConfigParam.weiXinUrl+"qyh/go_EmergencyManage.do";
    	String res = Sign.getRes(ticket, fullURL,  ConfigParam.weixinAppId);
    	/*String ticket = TicketManager.getTicket(ConfigParam.weixinAppId);
    	String generateConfigJson = JsUtil.generateConfigJson(ticket, false, ConfigParam.weixinAppId, fullURL);*/
       //http:192.168.1.108:8088/air-test/
    	return res ;
    	
    }
}
