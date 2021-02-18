package com.sunsharing.party.constant;

/**
 * 主要存放缓存用到的key Created with IntelliJ IDEA. User: ulyn Date: 13-3-15 Time:
 * 上午10:24 To change this template use File | Settings | File Templates.
 */
public class CacheConstant {

	/** 系统级缓存时间，秒 */
	public static final int CACHE_TIMEOUT = 3600 * 24;
	/** 表码缓存时间，秒 */
	public static final int BM_CACHE_TIMEOUT = 3600 * 24 * 30;
	/** 系统登陆用户的session的key */
	public static final String SESSION_USER = "SessionUserKey";
	/** 验证码值 */
	public static final String VERIFY_CODE = "VerifyCode";
	/** 系统memcache缓存组,放进这个组的表示此组随时可以清除。 **/
	public static final String MEMCACHE_TEMP_GROUP = "MEMCACHE_TEMP_GROUP";
	/** 系统memcache缓存组,放进这个组的表示此组是表码。 **/
	public static final String MEMCACHE_BM_GROUP = "MEMCACHE_BM_GROUP";
	/** 用户请求链接缓存 **/
	public static final String USER_REQUEST_URL = "USER_REQUEST_URL";
	/** 用户微信号缓存 **/
	public static final String MEMCACHE_WEIXIN_ID = "MEMCACHE_WEIXIN_ID";
	/** 设置cookie缓存的key值 **/
	public static final String EVENT_COOKIE_KEY = "EVENT_UUID";
	/** 设置防盗链的用户的key值 **/
	public static final String EVENT_SESSION_KEY = "EVENT_SESSION_KEY";
	/** 钥匙缓存的时间值 **/
	public static final int KEY_CACHE_TIMEOUT = 3600 * 2;
	/** 设置缓存的用户信息 **/
	public static final String EVENT_SESSION_USER = "EVENT_SESSION_USER";
	/** 登录信息key值 */
	public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

}
