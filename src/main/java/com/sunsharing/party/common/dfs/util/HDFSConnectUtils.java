/**
 * @ProjectName: 社会服务子应用
 * @Copyright: 2016 Sunsharing Technology Co., Ltd. All Right Reserved.
 * @address: http://www.sunsharing.com.cn
 * @date: 2017年8月11日 下午2:21:18
 * @Description: 本内容仅限于厦门畅享信息技术有限公司内部使用，禁止转发.
 */
package com.sunsharing.party.common.dfs.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;

/**
 * <p>
 * HDFS 连接工具类
 * </p>
 * @author hanjianbo 2017年8月11日 下午2:21:18
 * @version V1.0
 * @modificationHistory
 * @modify by user: 2017年8月11日
 */
public class HDFSConnectUtils {
	
	static final Logger logger = Logger.getLogger(HDFSConnectUtils.class);
	
	public static FileSystem getConn(String hadoopUri, String hadoopAccount) {
		final Configuration conf = getConfig();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(new URI(hadoopUri), conf, hadoopAccount);
		} catch (final IOException e) {
			logger.error("获取Hadoop连接发生错误", e);
		} catch (final InterruptedException e) {
			logger.error("获取Hadoop连接发生错误", e);
		} catch (final URISyntaxException e) {
			logger.error("获取Hadoop连接发生错误", e);
		}
		return fs;
	}
	
	/**
	 * @author hanjianbo 2017年8月16日 上午10:24:56
	 * @return
	 */
	protected static Configuration getConfig() {
		final Properties proper = new HDFSPropertyUtils("HDFSConfig.properties").getProperties();
		final Configuration conf = new Configuration();
		if (!proper.isEmpty()) {
			// 此为jdk1.8的新特性(start)遍历
			// proper.forEach((key, val) -> {
			// conf.set((String)key, (String)val);
			// });
			// 此为jdk1.8的新特性(end)遍历
			final Iterator<Entry<Object, Object>> it = proper.entrySet().iterator();
			while (it.hasNext()) {
				final Entry<Object, Object> entry = it.next();
				final Object key = entry.getKey();
				final Object value = entry.getValue();
				logger.info(key);
				logger.info(value);
				conf.set((String)key, (String)value);
			}
		}
		return conf;
	}
	
	public static void close(FileSystem conn) {
		try {
			conn.close();
		} catch (final IOException e) {
			logger.error("关闭HDFS连接时发生错误", e);
		}
	}
	
}
