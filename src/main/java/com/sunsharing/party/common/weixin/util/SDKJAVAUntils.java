package com.sunsharing.party.common.weixin.util;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.collagen.data_item.Item;
import com.sunsharing.collagen.data_item.SimpleItem;
import com.sunsharing.collagen.exception.RequestException;
import com.sunsharing.collagen.request.RequestResult;
import com.sunsharing.collagen.request.RequestSetting;
import com.sunsharing.collagen.request.WebServiceProxy;
import com.sunsharing.collagen.request.util.XmlParserUtils;
import com.sunsharing.party.ConfigParam;

/**
 * 从协同平台调用微信接口 工具类
 * <p>
 * </p>
 * @author wxl 2017年8月11日 下午5:19:36
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年8月11日
 * @modify by reason:{方法名}:{原因}
 */
public class SDKJAVAUntils {
	
	private static Logger logger = Logger.getLogger(SDKJAVAUntils.class);
	
	/**
	 * 
	 * @author wxl 2017年11月21日 上午9:50:45
	 * @param params 如果是post 请求 params参数中固定一个key值为 BODY value 为json对象 格式类似于{ "tagid": 1, "tagname": "UI design" }
	 * @param serviceId 服务id 每个请求的服务id不一样需要实施配置
	 * @param retuestType POST&GET
	 * @return json字符串 类似于 { "access_token": "accesstoken000001", "expires_in": 7200 }
	 */
	public static String callWxInterface(Map<String, Object> params, String serviceId, String requestType) {
		// 初始化信息，包括请求IP和端口，请求连接超时时间，请求ID，请求所属的域，服务代理流程ID。
		RequestSetting.getInstance().setIpAndPort(ConfigParam.CollaGEN_AP_IP, ConfigParam.CollaGEN_AP_PORT)
		        .setRequestId(ConfigParam.CollaGEN_Request_ID).setLocalDomain(ConfigParam.CollaGEN_Domain)
		        .setSyncWsProxyFlowId(ConfigParam.CollaGEN_WS_Proxy)
		        .setSocketTimeOutInSecond(ConfigParam.CollaGEN_Request_TimeOut);
		logger.info("CollaGEN_Service_ID = " + serviceId);
		if ("CollaGEN_Service_ID".equals(serviceId)) {
			logger.info("每个方法的服务id不一样这里不能统一配置，请联系实施配置该方法的对应的服务ID！");
			return "{\"error\":\"noServiceId\"}";
		}
		// 创建服务代理对象
		final WebServiceProxy webProxy = new WebServiceProxy(serviceId);
		// 对应业务的授权ID
		webProxy.setAuthorizeId(ConfigParam.CollaGEN_Authorized_ID);
		// 获取所有的key-value
		for (final String key : params.keySet()) {
			if ("BODY".equalsIgnoreCase(key)) {// 如果遇到KEY值为body的代表为post请求的请求体，不进行参数拼接
				continue;
			}
			final String value = (String)params.get(key);
			webProxy.appendStringParameter(key, value);// 循环拼接参数
			logger.info("当前key:" + key + " 当前value:" + value);
		}
		// 如果请求类型为post协同平台参数名固定为BODY
		if ("POST".equalsIgnoreCase(requestType)) {
			if (!params.containsKey("BODY")) {
				logger.info("为POST请求时，请传入参数名为BODY的请求体！");
				return "{\"error\":\"nobody\"}";
			}
			final JSONObject body = (JSONObject)params.get("BODY");
			logger.info("post请求的请求包体：" + body.toJSONString());
			webProxy.appendStringParameter("BODY", body.toJSONString());
		}
		String result = "{\"info\":\"nodata\"}";
		try {
			final RequestResult ret = webProxy.request();// 发送请求。
			final List<Item> retList = ret.getRetItems();// 获取返回结果。
			logger.info(retList.size());
			if (retList.size() > 0) {
				final Item firstItem = retList.get(0);// 获得返回结果中数组中第一个值。
				if (firstItem instanceof SimpleItem) {
					// 使用XmlParserUtils类将返回结果转换为XML字符串，可选步骤
					logger.info(XmlParserUtils.itemToXML(firstItem, true).toString());
					// 获取服务方返回的实际结果
					result = firstItem.asSimple().getValueIfString("");
				}
			}
		} catch (final RequestException e) {
			logger.info("ErrorCode:" + e.getErrorCodeStr() + ",ErrorMsg:" + e.getErrorMsg());
		}
		logger.info("result: " + result);
		return result;
	}
	
}
