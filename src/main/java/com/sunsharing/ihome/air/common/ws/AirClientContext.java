/**
 * @(#)AbstractServiceContext
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2014
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author ulyn
 *<br> 14-1-31 下午5:37
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.ihome.air.common.ws;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sunsharing.ihome.air.common.anno.AirService;
import com.sunsharing.ihome.air.common.util.ClassFilter;
import com.sunsharing.ihome.air.common.util.ClassUtils;
import com.sunsharing.party.common.ws.JdkProxy;

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
public class AirClientContext {
	Logger logger = Logger.getLogger(AirClientContext.class);

	// protected ApplicationContext ctx;
	protected String packagePath;
	protected String targetUrl;

	// 存储服务对象,key为服务的name
	protected static Map<String, Object> services = new HashMap<String, Object>();
	protected static Map<String, Object> servicesByName = new HashMap<String, Object>();

	public AirClientContext(String packagePath, String targetUrl) {
		this.targetUrl = targetUrl;
		this.packagePath = packagePath;
	}

	/**
	 * 初始化
	 */
	public void init() {
		;

		ClassFilter filter = new ClassFilter() {
			@Override
			public boolean accept(Class clazz) {
				if (Modifier.isInterface(clazz.getModifiers())) {
					Annotation ann = clazz.getAnnotation(AirService.class);
					if (ann != null) {
						return true;
					}
				}
				return false;
			}
		};
		List<Class> classes = ClassUtils.scanPackage(packagePath, filter);
		List<Class> classes2 = ClassUtils.scanPackage("com.sunsharing.ihome", filter);
		classes.addAll(classes2);
		for (final Class c : classes) {
			AirService ann = (AirService) c.getAnnotation(AirService.class);
			String id = getBeanId(c, ann.id());
			JdkProxy jdkProxy = new JdkProxy(targetUrl);
			try {
				Object bean = jdkProxy.getProxy(c, id);
				services.put(c.getName(), bean);
				servicesByName.put(id, bean);
				logger.info("加载服务：" + id + "-" + c.getName());
			} catch (Exception e) {
				logger.error("初始化服务失败：" + c.getName(), e);
				System.exit(0);
			}
		}
	}

	private String getBeanId(Class interfaces, String id) {
		if (id.equals("")) {
			id = interfaces.getSimpleName();
			id = Character.toLowerCase(id.charAt(0)) + id.substring(1);
		}
		return id;
	}

	/**
	 * 根据接口取得服务bean
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		Object o = services.get(clazz.getName());
		if (o == null) {
			return null;
		}
		return (T) o;
	}

	public static <T> T getBeanByName(String name) {
		Object o = servicesByName.get(name);
		if (o == null) {
			return null;
		}
		return (T) o;
	}

	public static void main(String[] args) {
		// AirClientContext context = new AirClientContext("com.sunsharing.eos");

	}
}
