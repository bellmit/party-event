package com.sunsharing.party.common.dfs.imp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sunsharing.common.utils.StringUtils;
import com.sunsharing.party.common.dfs.FileApi;
import com.sunsharing.party.common.dfs.model.HDFSRenameMolde;
import com.sunsharing.transport.exchage.client.ExchangeClient;

/**
 * <p>
 * DFS文件操作实现类
 * </p>
 * @author hanjianbo 2017年8月16日 上午10:05:38
 * @version V1.0
 */
public class DFS implements FileApi {
	
	Logger logger = Logger.getLogger(DFS.class);
	
	private String dfsIp = "";
	private String proxyIp = "";
	int dfsPort = 8000;
	private String localIp = "";
	private Map<String, String> ipMap = null;
	
	public DFS(String dfsIp, String proxyIp, int dfsPort, String localIp) {
		this.dfsIp = dfsIp;
		this.proxyIp = proxyIp;
		this.dfsPort = dfsPort;
		this.localIp = localIp;
		ipMap = new HashMap<String, String>();
		ipMap.put(dfsIp, proxyIp);
	}
	
	@Override
	public Map<String, Object> readBacthFile(String relativeName, long begin, long end) {
		final String[] arr = StringUtils.split(relativeName, '/');
		final String date = arr[1];
		final String filename = arr[2];
		final ExchangeClient client = new ExchangeClient(proxyIp, dfsPort, localIp);
		@SuppressWarnings("unchecked")
		final Map<String, Object> map = client.getFileByBatch(date, filename, begin, end);
		return map;
	}
	
	@Override
	public byte[] readFile(String relativeName) {
		final String[] arr = StringUtils.split(relativeName, '/');
		final String date = arr[1];
		final String filename = arr[2];
		final ExchangeClient client = new ExchangeClient(proxyIp, dfsPort, localIp);
		final byte[] b = client.getFile(date, filename, ipMap, false);
		return b;
	}
	
	@Override
	public String saveFile(File file) {
		return saveFile(file, null);
	}
	
	/**
	 * DFS 不提供文件删除功能， 所以该方法永远返回false
	 */
	@Override
	public boolean deleteFile(String filePath) {
		return false;
	}
	
	/**
	 * 在DFS中该方法中folder参数是不起作用的
	 */
	@Override
	public String saveFile(File file, String folder) {
		final String modifyTimeStr = toTimeDirectory();
		final ExchangeClient client = new ExchangeClient(proxyIp, dfsPort, localIp);
		client.sendFile(file, ipMap);
		final String path = File.separator + modifyTimeStr.substring(0, 8) + File.separator + file.getName();
		return path.replace("\\", "/");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.ihome.air.common.dfs.FileApi#batchSaveFile(java.io.File[], java.lang.String)
	 */
	@Override
	public List<String> batchSaveFile(List<File> files, String folder) {
		return new ArrayList<String>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.ihome.air.common.dfs.FileApi#renameFile(java.util.List, java.lang.String)
	 */
	@Override
	public Map<String, Object> renameFile(List<HDFSRenameMolde> paths) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sunsharing.ihome.air.common.dfs.FileApi#deleteFile(java.util.List)
	 */
	@Override
	public void deleteFile(List<String> filePaths) {
		
	}
}
