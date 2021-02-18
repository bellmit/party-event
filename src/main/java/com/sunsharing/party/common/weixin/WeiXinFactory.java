package com.sunsharing.party.common.weixin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.weixin.impl.WeiXinGzh;
import com.sunsharing.party.common.weixin.impl.WeiXinGzhBySDK;
import com.sunsharing.party.common.weixin.impl.WeiXinQyh;
import com.sunsharing.party.common.weixin.impl.WeiXinQyhBySDK;
import com.sunsharing.party.constant.Constant;

/**
 * 微信工厂类
 * <p>
 * </p>
 * @author wxl 2017年11月21日 上午8:58:42
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月21日
 * @modify by reason:{方法名}:{原因}
 */
public class WeiXinFactory {
	

	private static Logger logger = Logger.getLogger(WeiXinFactory.class);
	
	/**
	 * 根据微信号类型初始化实现类
	 * @author wxl 2017年11月21日 上午9:27:00
	 * @param weiXinType QYH or GZH
	 * @return WeiXinApi
	 */
	public static WeiXinApi init(String weiXinType) {
		
		if (isWlan(ConfigParam.testNetworkUrl)) {
			// 互联网接入不需要从协同平台
			if (Constant.QYH.equals(weiXinType)) {
				return new WeiXinQyh();
			} else if (Constant.GZH.equals(weiXinType)) {
				return new WeiXinGzh();
			}
		} else {
			// 如果网络没有通，从协同平台
			logger.info("网络异常，从协同平台调取微信接口！");
			if (Constant.QYH.equals(weiXinType)) {
				return new WeiXinQyhBySDK();
			} else if (Constant.GZH.equals(weiXinType)) {
				return new WeiXinGzhBySDK();
			}
		}
		logger.info("请传入调用的微信接口类型！QYH or GZH");
		return null;
	}
	
	/**
	 * 验证网络是否通畅
	 * @author wxl 2017年11月21日 下午2:20:54
	 * @param url
	 * @return true or false
	 */
	private static boolean isWlan(String url) {
		logger.info("开始验证网络,验证的域名是：" + url);
		boolean reachable = false;
		try {
			final InetAddress address = InetAddress.getByName(url);
			
			try {
				reachable = address.isReachable(5000);
			} catch (final IOException e) {
				logger.error("io异常！");
			}
		} catch (final UnknownHostException e) {
			logger.info("ip地址错误或没有连入互联网！");
			// e.printStackTrace();
		}
		if (reachable == true) {
			logger.info("互联网连接正常！");
		} else {
			logger.info("互联网连接错误，从协同平台！");
		}
		return reachable;
	}
	

}
