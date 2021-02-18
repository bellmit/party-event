/**
 * @(#)JFS 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 * 
 *         <br>
 *         Copyright: Copyright (c) 2014 <br>
 *         Company:厦门畅享信息技术有限公司 <br>
 * @author ulyn <br>
 *         14-5-27 上午11:02 <br>
 * @version 1.0 ———————————————————————————————— 修改记录 修改者： 修改时间： 修改原因： ————————————————————————————————
 */
package com.sunsharing.party.common.dfs.imp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sunsharing.common.utils.StringUtils;
import com.sunsharing.party.common.dfs.FileApi;
import com.sunsharing.party.common.dfs.model.HDFSRenameMolde;

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
public class JFS implements FileApi {
	
	Logger logger = Logger.getLogger(JFS.class);
	
	String attPath = null;
	
	public JFS(String attPath) {
		if (attPath.endsWith("/")) {
			attPath = attPath.substring(0, attPath.length() - 1);
		}
		attPath = attPath.replace("/", File.separator);
		this.attPath = attPath;
	}
	
	@Override
	public String saveFile(File file) {
		return saveFile(file, null);
	}
	
	@Override
	public boolean deleteFile(String filePath) {
		final String path = attPath + File.separator + filePath;
		final File file = new File(path);
		if (file.exists()) {// 文件存在 删除
			return file.delete();
		}
		return false;
	}
	
	@Override
	public String saveFile(File file, String folder) {
		final String modifyTimeStr = toTimeDirectory();
		String relativeName = "";
		if (StringUtils.isBlank(folder)) {
			relativeName = "/" + modifyTimeStr.substring(0, 8) + "/" + file.getName();
		} else {
			relativeName = "/" + format(folder) + "/" + modifyTimeStr.substring(0, 8) + "/" + file.getName();
		}
		
		try {
			final String newfile = attPath + relativeName;
			logger.info("file_path:" + newfile);
			final File newFile = new File(newfile);
			if (!file.equals(newFile)) {
				copyFile(file, newFile);
				logger.info("开始删除文件：" + file.getPath());
				final boolean status = file.delete();
				logger.info("删除文件状态：" + status);
			}
			return relativeName;
		} catch (final Exception e) {
			throw new RuntimeException("保存文件失败：" + e.getMessage(), e);
		}
	}
	
	@Override
	public Map<String, Object> readBacthFile(String relativeName, long begin, long end) {
		final String filePath = attPath + relativeName;
		byte[] buffer = null;
		final File file = new File(filePath);
		try {
			final FileInputStream fis = new FileInputStream(file);
			final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			fis.skip(begin);
			long bytesToRead = end - begin + 1;
			final byte[] b = new byte[1024];
			int len = b.length;
			while ((bytesToRead > 0) && (len >= b.length)) {
				try {
					len = fis.read(b);
					if (bytesToRead >= len) {
						bos.write(b, 0, len);
						bytesToRead -= len;
					} else {
						bos.write(b, 0, (int)bytesToRead);
						bytesToRead = 0;
					}
				} catch (final IOException e) {
					len = -1;
				}
				if (len < b.length) {
					break;
				}
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (final FileNotFoundException e) {
			logger.error("找不到文件:" + filePath, e);
		} catch (final IOException e) {
			logger.error("输出失败" + filePath, e);
		}
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("bytes", buffer);
		map.put("fileLength", file.length());
		return map;
	}
	
	@Override
	public byte[] readFile(String relativeName) {
		final String filePath = attPath + relativeName;
		byte[] buffer = null;
		try {
			final File file = new File(filePath);
			final FileInputStream fis = new FileInputStream(file);
			final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			final byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (final FileNotFoundException e) {
			logger.error("找不到文件:" + filePath, e);
		} catch (final IOException e) {
			logger.error("输出失败" + filePath, e);
		}
		return buffer;
	}
	
	/**
	 * 执行具体的JFS文件上传操作
	 * @author hanjianbo 2017年8月15日 下午5:29:29
	 * @param f
	 * @param targetFile
	 * @throws Exception
	 */
	protected void copyFile(File file, File targetFile) throws Exception {
		int byteread = 0;
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			if (file.exists()) { // 文件存在时
				inStream = new FileInputStream(file); // 读入原文件
				final File parentDir = targetFile.getParentFile();
				if (!parentDir.isDirectory()) {
					parentDir.mkdirs();
				}
				fs = new FileOutputStream(targetFile);
				final byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				inStream = null;
				fs.close();
				fs = null;
			}
		} catch (final Exception e) {
			logger.error("复制文件出错", e);
			if (inStream != null) {
				inStream.close();
			}
			if (fs != null) {
				fs.close();
			}
			throw new Exception("复制单个文件操作出错:" + file + "->" + targetFile);
		}
	}
	
	@Override
	public List<String> batchSaveFile(List<File> files, String folder) {
		final List<String> paths = new ArrayList<String>();
		// files.forEach(file -> {
		if (files != null) {
			for (int i = 0; i < files.size(); i++) {
				final File file = files.get(i);
				paths.add(saveFile(file, folder));
			}
		}
		// });
		return paths;
	}
	
	@Override
	public Map<String, Object> renameFile(List<HDFSRenameMolde> paths) {
		return null;
	}
	
	@Override
	public void deleteFile(List<String> filePaths) {
		// filePaths.forEach(filePath -> {
		if (filePaths != null) {
			for (int i = 0; i < filePaths.size(); i++) {
				final String filePath = filePaths.get(i);
				deleteFile(filePath);
				// });
			}
		}
	}
	
}
