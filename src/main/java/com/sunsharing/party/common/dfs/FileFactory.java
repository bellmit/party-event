/**
 * @(#)FileFactory 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 * 
 *                 <br>
 *                 Copyright: Copyright (c) 2014 <br>
 *                 Company:厦门畅享信息技术有限公司 <br>
 * @author ulyn <br>
 *         14-5-27 上午9:53 <br>
 * @version 1.0 ———————————————————————————————— 修改记录 修改者： 修改时间： 修改原因： ————————————————————————————————
 */
package com.sunsharing.party.common.dfs;

import com.sunsharing.party.common.dfs.imp.DFS;
import com.sunsharing.party.common.dfs.imp.HDFS;
import com.sunsharing.party.common.dfs.imp.JFS;

/**
 * <p>
 * 文件上传工厂类，根据需要创建出HDFS、DFS、JFS相关操作对象
 * </p>
 * @author hanjianbo 2017年8月15日 下午2:26:36
 * @version V1.0
 */
public class FileFactory {
	
	public final static String TYPE_DFS = "DFS";
	public final static String TYPE_JFS = "JFS";
	public final static String TYPE_HDFS = "HDFS";
	
	private static String dfsIp = null;
	private static String proxyIp = null;
	private static int dfsPort = 0;
	private static String localIp = null;
	private static String hadoopUri = null;
	private static String hadoopAccount = null;
	
	private static String attPath = null;
	
	private static String currentType = null;
	
	/**
	 * 根据配置文件的配置生成系统希望的文件操作对象 HDFS/JFS/DFS
	 * @author hanjianbo 2017年8月15日 下午3:08:10
	 * @return
	 */
	public static FileApi create() {
		if (TYPE_DFS.equalsIgnoreCase(currentType)) {
			return new DFS(dfsIp, proxyIp, dfsPort, localIp);
		} else if (TYPE_JFS.equalsIgnoreCase(currentType)) {
			return new JFS(attPath);
		} else if (TYPE_HDFS.equals(currentType)) {
			return new HDFS(hadoopUri, hadoopAccount);
		} else {
			return null;
		}
	}
	
	/**
	 * 手动生成JFS文件操作对象
	 * @author hanjianbo 2017年8月15日 下午3:09:02
	 * @return
	 */
	public static FileApi createJFS() {
		assert FileFactory.attPath != null;
		return new JFS(FileFactory.attPath);
	}
	
	/**
	 * 手动生成HDFS文件操作对象
	 * @author hanjianbo 2017年8月15日 下午3:20:33
	 * @return
	 */
	public static FileApi createHDFS() {
		assert FileFactory.hadoopAccount != null;
		assert FileFactory.hadoopUri != null;
		return new HDFS(FileFactory.hadoopUri, FileFactory.hadoopAccount);
	}
	
	/**
	 * 手动生成DFS文件操作对象
	 * @author hanjianbo 2017年8月17日 下午2:53:21
	 * @return
	 */
	public static FileApi createDFS() {
		assert FileFactory.dfsIp != null;
		assert FileFactory.proxyIp != null;
		assert FileFactory.dfsPort != 0;
		assert FileFactory.localIp != null;
		return new DFS(dfsIp, proxyIp, dfsPort, localIp);
	}
	
	/**
	 * 获取当前的文件存储系统类型
	 * @return
	 */
	public static String getType() {
		return currentType;
	}
	
	/**
	 * 初始化工厂，根据type将工厂注册成为期望的文件存储系统
	 * @param type 文件存储系统类型
	 * @param attPath jfs保存文件的本机路径
	 * @param dfsIp dfs服务器实际服务器ip地址
	 * @param proxyIp dfs服务器代理IP地址
	 * @param dfsPort dfs服务器端口
	 * @param localIp 本机ip
	 * @param hadoopUri Hadoop集群中NameNode链接地址
	 * @param hadoopAccount Hadoop集群账户
	 */
	public static void init(String type, String attPath, String dfsIp, String proxyIp, int dfsPort, String localIp,
	        String hadoopUri, String hadoopAccount) {
		currentType = type;
		registerDFS(dfsIp, proxyIp, dfsPort, localIp);
		registerJFS(attPath);
		registerHDFS(hadoopUri, hadoopAccount);
	}
	
	/**
	 * 注册HDFS工厂类
	 * @author hanjianbo 2017年8月15日 下午2:54:51
	 * @param hadoopUri
	 * @param hadoopAccount
	 */
	private static void registerHDFS(String hadoopUri, String hadoopAccount) {
		assert hadoopUri != null;
		assert hadoopAccount != null;
		FileFactory.hadoopUri = hadoopUri;
		FileFactory.hadoopAccount = hadoopAccount;
	}
	
	/**
	 * 注册DFS工厂
	 * @param dfsIp
	 * @param dfsPort
	 * @param localIp
	 */
	private static void registerDFS(String dfsIp, String proxyIp, int dfsPort, String localIp) {
		assert dfsIp != null;
		assert dfsPort != 0;
		assert localIp != null;
		FileFactory.dfsIp = dfsIp;
		FileFactory.dfsPort = dfsPort;
		FileFactory.localIp = localIp;
		FileFactory.proxyIp = proxyIp;
	}
	
	/**
	 * 注册JFS工厂
	 * @param attPath
	 */
	private static void registerJFS(String attPath) {
		assert attPath != null;
		FileFactory.attPath = attPath;
	}
}
