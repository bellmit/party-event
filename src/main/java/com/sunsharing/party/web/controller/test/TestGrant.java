package com.sunsharing.party.web.controller.test;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.anno.CheckLogin;
import com.sunsharing.party.common.weixin.WeiXinApi;
import com.sunsharing.party.common.weixin.WeiXinFactory;
import com.sunsharing.party.constant.CacheConstant;
import com.sunsharing.party.util.CacheUtil;
import com.sunsharing.party.web.controller.BaseController;

/**
 * 企业号和微信号授权
 * <p>
 * </p>
 * @author wxl 2017年11月20日 下午6:59:48
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月20日
 * @modify by reason:{方法名}:{原因}
 */
@Controller
@RequestMapping(value = "wx")
public class TestGrant {
	
	private static Logger logger = Logger.getLogger(TestGrant.class);
	
	/**
	 * 企业号
	 * @author wxl 2017年11月20日 下午7:02:07
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "qy")
//	@CheckLogin( weiXinType = "QYH", cCode = "qy")
	public String TestQyGrant(HttpServletRequest request) {
		System.out.println("jinqu -----");
		/*String wx_UserId;
		try {
			wx_UserId = BaseController.checkRefferCookie(request, CacheConstant.MEMCACHE_WEIXIN_ID);
			final String userId = (String)CacheUtil.getCache(CacheConstant.MEMCACHE_WEIXIN_ID + wx_UserId);// 获取缓存中是否存在用户
			logger.info("cookie中的用户：" + wx_UserId + "缓存中的用户：" + userId);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return "test";
	}
	
	/**
	 * 公众号
	 * @author wxl 2017年11月20日 下午7:02:43
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "gz")
	@CheckLogin(weiXinType = "GZH", cCode = "gz")
	public String TestGzGrant(HttpServletRequest request) {
		String wx_UserId;
		try {
			wx_UserId = BaseController.checkRefferCookie(request, CacheConstant.MEMCACHE_WEIXIN_ID);
			final String userId = (String)CacheUtil.getCache(CacheConstant.MEMCACHE_WEIXIN_ID + wx_UserId);// 获取缓存中是否存在用户
			logger.info("cookie中的用户：" + wx_UserId + "缓存中的用户：" + userId);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "index";
	}
	
	public static void main(String[] args) {
		final WeiXinApi wx = WeiXinFactory.init("QYH");
		final String ticket = wx.getTicket(ConfigParam.CorpID,
		        ConfigParam.Secret);

	}
}
