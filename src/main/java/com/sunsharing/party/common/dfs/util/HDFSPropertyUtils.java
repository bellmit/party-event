/**
 * @ProjectName: 社会服务子应用
 * @Copyright: 2016 Sunsharing Technology Co., Ltd. All Right Reserved.
 * @address: http://www.sunsharing.com.cn
 * @date: 2017年8月11日 下午3:11:16
 * @Description: 本内容仅限于厦门畅享信息技术有限公司内部使用，禁止转发.
 */
package com.sunsharing.party.common.dfs.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <p>
 * 专门为DFS配置文件读取使用准备的工具类
 * </p>
 * @author hanjianbo 2017年8月11日 下午3:11:16
 * @version V1.0
 */
public class HDFSPropertyUtils {
	
	Logger logger = Logger.getLogger(HDFSPropertyUtils.class);
	
	Properties proper;
	
	String path; // properties文件路径
	
	public HDFSPropertyUtils(String path) {
		this.path = path;
		init(path);
	}
	
	private void init(String path) {
		proper = new Properties();
		try {
			proper.load(HDFSPropertyUtils.class.getClassLoader().getResourceAsStream(path));
		} catch (final IOException e) {
			logger.error("初始化HDFS配置文件出错", e);
		}
	}
	
	public String getProperty(String key) {
		return proper.getProperty(key);
	}
	
	public Properties getProperties() {
		return proper;
	}
}
