/**
 * @(#)JdkProxy
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2014
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author ulyn
 *<br> 14-2-1 上午12:11
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.party.common.ws;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sunsharing.component.utils.base.StringUtils;

/**
 * <pre></pre>
 * 
 * <br>
 * ---------------------------------------------------------------------- <br>
 * <b>功能描述:</b> <br>
 * <br>
 * 注意事项: <br>
 * <br>
 * <br>
 * ---------------------------------------------------------------------- <br>
 */
public class JdkProxy {
	private String targetUrl;

	public JdkProxy(String targetUrl) {
		assert !StringUtils.isBlank(targetUrl);
		this.targetUrl = targetUrl;
	}

	public <T> T getProxy(Class<T> clazz, final String id) throws Exception {
		InvocationHandler handler = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("targetUrl=" + targetUrl);
				AirRpcClient client = new AirRpcClient(targetUrl);
				String paramJsonStr = JSON.toJSONString(args, SerializerFeature.WriteMapNullValue);
				String result = client.request(id, method.getName(), paramJsonStr);
				JSONObject resultJson = JSON.parseObject(result);
				if (!resultJson.getBoolean("status")) {
					throw new RuntimeException("远程异常：" + resultJson.getString("msg"));
				}
				String resultStr = resultJson.getString("result");
				Object o = JSON.parseObject(resultStr, method.getGenericReturnType());
				return o;
			}
		};
		T t = (T) Proxy.newProxyInstance(JdkProxy.class.getClassLoader(), new Class[] { clazz }, handler);
		return t;
	}
}
