/**
 * @ProjectName: 社会服务子应用
 * @Copyright: 2016 Sunsharing Technology Co., Ltd. All Right Reserved.
 * @address: http://www.sunsharing.com.cn
 * @date: 2017年9月7日 上午10:03:13
 * @Description: 本内容仅限于厦门畅享信息技术有限公司内部使用，禁止转发.
 */
package com.sunsharing.party.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;

/**
 * <p>
 * 验证来源工具类,搭配menuRule.json使用
 * </p>
 * 
 * @author wangchuan 2017年9月7日 上午10:03:13
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年9月7日
 * @modify by reason:{方法名}:{原因}
 */
public class LinkRuleMacthUtil {

	static Logger logger = Logger.getLogger(LinkRuleMacthUtil.class);
	private final static String fileName = "menuRule.json";

	/**
	 * 验证用户访问来源及菜单权限
	 * 
	 * @author wangchuan 2017年9月1日 下午3:05:13
	 * @param request
	 * @return
	 */
	public static boolean hasMenuPermission(HttpServletRequest request) {
		try {
			String url = request.getRequestURI();
			String pars = request.getQueryString();
			String context = request.getContextPath();

			JSONObject jsObject = ReadFileUtils.getJsonMenu("menuRule.json");
			String dataRule = jsObject.getString("dataRule");
			String urlRule = jsObject.getString("urlRule");

			JSONArray jsonArray = jsObject.getJSONArray("refererCheckurls");
			boolean isFlag = true;
			if ((StringUtils.isBlank(urlRule)) || ("true".equals(urlRule))) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (hasString(jsonObject.getString("tagertUrl"), url + "?" + pars)) {
						if (hasRuleString(jsonObject.getJSONArray("ruleUrls"), jsonObject.getString("rule"),
								request.getHeader("Referer"))) {
							return true;
						}
						isFlag = false;

						break;
					}
				}
				logger.warn("*********来源权限验证结果:" + isFlag);
				if (!isFlag) {
					return isFlag;
				}
			}
		} catch (Exception e) {
			logger.error("异常:", e);
		}
		return true;
	}

	/**
	 * 检索对象访问URL中是否配置有待校验的匹配目标
	 * 
	 * @author wangchuan 2017年9月1日 下午3:12:43
	 * @param tarStr
	 *            配置目标链接
	 * @param indexStr
	 *            被访问链接
	 * @return
	 */
	public static boolean hasString(String tarStr, String indexStr) {
		String[] tarStrs = tarStr.toLowerCase().split("\\?");
		String[] indexStrs = indexStr.toLowerCase().split("\\?");
		if (tarStrs[0].equals(indexStrs[0]) || indexStrs[0].endsWith(tarStrs[0])) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		JSONArray array = JSONArray.parseArray(
				"['http://23.140.1.100/air-fire/login/jump.do?token=f02903e8417247a0862f95078e952f96&action=menu']");
		String rule = "indexOf";
		String indexStr = "http://23.140.1.100/air-fire/login/jump.do?token=f02903e8417247a0862f95078e952f96&action=menu";
		System.out.println(LinkRuleMacthUtil.hasRuleString(array, rule, indexStr));

		String tarStr = "/air-fire/stats/go_iframe.do?mId=jcsj_qqjj&ifr=/baseData/goAreaIntroduce.do";
		String indexStr1 = "/air-fire/stats/go_iframe.do?mId=jcsj_qqjj&ifr=/baseData/goAreaIntroduce.do";

		System.out.println(LinkRuleMacthUtil.hasString(tarStr, indexStr1));
	}

	/**
	 * 检索对象中是否有满足规则的来源
	 * 
	 * @author wangchuan 2017年9月1日 下午3:16:14
	 * @param ruleStr
	 *            校验规则链接
	 * @param rule
	 *            校验规则 indexOf=模糊 startsWith=前匹配 endsWith=后匹配 equals=全匹配（默认）
	 * @param indexStr
	 *            来源链接
	 * @return
	 */
	public static boolean hasRuleString(JSONArray ruleStr, String rule, String indexStr) {
		logger.info("拦截到的上个请求：===" + indexStr);
		if (StringUtils.isBlank(indexStr)) {
			return false;
		}
		rule = StringUtils.isBlank(rule) ? "equals" : rule;
		if (indexStr.indexOf(";") != -1) {
			String[] strings = indexStr.split(";");
			if (strings[1].indexOf("?") != -1) {
				indexStr = strings[0] + "?" + strings[1].split("\\?")[1];
			} else {
				indexStr = strings[0];
			}
		}
		for (int i = 0; i < ruleStr.size(); i++) {
			String str = ruleStr.getString(i);
			if (("equals".equals(rule)) && (str.equals(indexStr))) {
				return true;
			}
			if (("indexOf".equals(rule)) && ((str.indexOf(indexStr) != -1) || (indexStr.indexOf(str) != -1))) {
				return true;
			}
			if (("startsWith".equals(rule)) && ((str.startsWith(indexStr)) || (indexStr.startsWith(str)))) {
				return true;
			}
			if (("endsWith".equals(rule)) && ((str.endsWith(indexStr)) || (indexStr.endsWith(str)))) {
				return true;
			}
		}
		return false;
	}
}
