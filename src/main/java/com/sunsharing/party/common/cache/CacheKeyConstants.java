package com.sunsharing.party.common.cache;

/**
 * 主要存放缓存用到的key Created with IntelliJ IDEA. User: ulyn Date: 13-3-15 Time:
 * 上午10:24 To change this template use File | Settings | File Templates.
 */
public class CacheKeyConstants {

	/** 系统级缓存时间，秒 */
	public static final int cacheTimeout = 3600 * 24;
	/** 表码缓存时间，秒 */
	public static final int bmCacheTimeout = 3600 * 24 * 30;
	/** 民情台问题数据缓存时间，秒 */
	public static final int sqCacheTimeout = 3600 * 24;
	/** 首页新闻数据缓存时间，秒 */
	public static final int syCacheTimeout = 600;

	/** 系统登陆用户的session的key */
	public static final String SESSION_USER = "SessionUser";

	/** 系统登陆用户的session的key */
	public static final String SESSION_USER_KEY = "SessionUserKey";
	/** 验证码值 */
	public static final String VERIFY_CODE = "VerifyCode";
	/** 系统memcache缓存组,放进这个组的表示此组随时可以清除。 **/
	public static final String MEMCACHE_TEMP_GROUP = "MEMCACHE_TEMP_GROUP";
	/** 系统memcache缓存组,放进这个组的表示此组是表码。 **/
	public static final String MEMCACHE_BM_GROUP = "MEMCACHE_BM_GROUP";
	/** 系统memcache缓存组,放进这个组的表示此组是民情台问题数据。 **/
	public static final String MEMCACHE_SQ_GROUP = "MEMCACHE_SQ_GROUP";
	/** 系统memcache缓存组,放进这个组的表示此组是首页新闻数据。 **/
	public static final String MEMCACHE_SY_NEWS = "MEMCACHE_SY_NEWS";
	/** 系统memcache缓存组,放进这个组的表示此组是首页新闻图片数据。 **/
	public static final String MEMCACHE_SY_NEWSPIC = "MEMCACHE_SY_NEWSPIC";

	/** 获取天气字符串 **/
	public static final String MEMCACHE_SQ_WEATHER = "MEMCACHE_SQ_WEATHER";
	/** 网格列表 **/
	public static final String MEMCACHE_GR_GROUP = "MEMCACHE_GR_GROUP";
	/** ipv6地址 **/
	public static final String ipv6 = "ipv6";
	/** 存放区政府邮箱信息 **/
	public static final String email_info = "email_info";
	/** 存放社会管理家庭模板 **/
	public static final String PAGE_TEMP = "page_temp";

	public static final String SCHOOL = "SCHOOL";
	/** 存放医院 */
	public static final String HOSPITAL = "HOSPITAL";
	/** 登录信息key值 */
	public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";
	/** 登录信息Map key值 */
	public static final String LOGIN_MAP_KEY = "LOGIN_MAP_KEY";

}
