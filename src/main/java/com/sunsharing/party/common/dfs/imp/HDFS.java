/**
 * @ProjectName: 社会服务子应用
 * @Copyright: 2016 Sunsharing Technology Co., Ltd. All Right Reserved.
 * @address: http://www.sunsharing.com.cn
 * @date: 2017年8月11日 上午10:45:00
 * @Description: 本内容仅限于厦门畅享信息技术有限公司内部使用，禁止转发.
 */
package com.sunsharing.party.common.dfs.imp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import com.sunsharing.common.utils.StringUtils;
import com.sunsharing.party.common.dfs.FileApi;
import com.sunsharing.party.common.dfs.model.HDFSRenameMolde;
import com.sunsharing.party.common.dfs.util.HDFSConnectUtils;

/**
 * <p>
 * HDFS文件操作实现类
 * </p>
 * 
 * @author hanjianbo 2017年8月11日 上午10:45:00
 * @version V1.0
 * @modificationHistory
 * @modify by user: 2017年8月11日
 */
public class HDFS implements FileApi {

	Logger logger = Logger.getLogger(HDFS.class);

	private final String hadoopUri; // nameNode链接地址

	private final String hadoopAccount; // hadoop集群账户

	/**
	 * 创建一个新的实例HDFS.
	 * 
	 * @param hadoopUri
	 *            hadoop集群NameNode链接地址
	 * @param hadoopAccount
	 *            hadoop集群账户
	 */
	public HDFS(String hadoopUri, String hadoopAccount) {
		this.hadoopUri = hadoopUri;
		this.hadoopAccount = hadoopAccount;
	}

	@Override
	public boolean deleteFile(String filePath) {
		final FileSystem hdfs = HDFSConnectUtils.getConn(hadoopUri, hadoopAccount);
		boolean falg = false;
		try {
			falg = hdfs.deleteOnExit(new Path(filePath));
		} catch (IllegalArgumentException | IOException e) {
			logger.error("HDFS删除文件发生错误", e);
		} finally {
			HDFSConnectUtils.close(hdfs);
		}
		return falg;
	}

	@Override
	public void deleteFile(List<String> filePaths) {
		final FileSystem hdfs = HDFSConnectUtils.getConn(hadoopUri, hadoopAccount);
		// filePaths.forEach(filePath -> {
		if (filePaths == null) {
			return;
		}
		for (int i = 0; i < filePaths.size(); i++) {
			final String filePath = filePaths.get(i);
			try {
				hdfs.deleteOnExit(new Path(filePath));
			} catch (IllegalArgumentException | IOException e) {
				logger.error("HDFS删除文件发生错误", e);
			}
		}
		// });
		HDFSConnectUtils.close(hdfs);
	}

	@Override
	public Map<String, Object> readBacthFile(String relativeName, long begin, long end) {
		final FileSystem hdfs = HDFSConnectUtils.getConn(hadoopUri, hadoopAccount);
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		FSDataInputStream in = null;
		byte[] buffer = null;
		long filelength = 0L;
		try {
			// 该方法用于在Hadoop中获取文件长度
			final FileStatus status = hdfs.getFileStatus(new Path(relativeName));
			filelength = status.getLen();
			// 开始分段读取文件
			in = hdfs.open(new Path(relativeName));
			in.seek(begin);
			long bytesToRead = end - begin + 1;
			final byte[] b = new byte[1024];
			int len = b.length;
			while ((bytesToRead > 0) && (len >= b.length)) {
				try {
					len = in.read(b);
					if (bytesToRead >= len) {
						bos.write(b, 0, len);
						bytesToRead -= len;
					} else {
						bos.write(b, 0, (int) bytesToRead);
						bytesToRead = 0;
					}
				} catch (final IOException e) {
					len = -1;
				}
				if (len < b.length) {
					break;
				}
			}
			buffer = bos.toByteArray();
		} catch (IllegalArgumentException | IOException e) {
			logger.error("通过HDFS分段读取文件发生错误", e);
		} finally {
			HDFSConnectUtils.close(hdfs);
			IOUtils.closeStream(in);
			IOUtils.closeStream(bos);
		}
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("bytes", buffer);
		map.put("fileLength", filelength);
		return map;
	}

	@Override
	public byte[] readFile(String relativeName) {
		byte[] buffer = null;
		final int bufferSzie = 1024;
		final FileSystem hdfs = HDFSConnectUtils.getConn(hadoopUri, hadoopAccount);
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(bufferSzie);
		FSDataInputStream in = null;
		try {
			in = hdfs.open(new Path(relativeName));
			IOUtils.copyBytes(in, bos, bufferSzie);
			buffer = bos.toByteArray();
		} catch (IllegalArgumentException | IOException e) {
			logger.error("通过HDFS读取文件发生错误", e);
		} finally {
			HDFSConnectUtils.close(hdfs);
			IOUtils.closeStream(in);
			IOUtils.closeStream(bos);
		}
		return buffer;
	}

	@Override
	public String saveFile(File file) {
		return saveFile(file, null);
	}

	@Override
	public String saveFile(File file, String folder) {
		final String modifyTimeStr = toTimeDirectory();
		String path = "";
		if (StringUtils.isBlank(folder)) {
			path = "/" + modifyTimeStr.substring(0, 8) + "/" + file.getName();
		} else {
			path = "/" + format(folder) + "/" + modifyTimeStr.substring(0, 8) + "/" + file.getName();
		}
		final FileSystem hdfs = HDFSConnectUtils.getConn(hadoopUri, hadoopAccount);
		try {
			hdfs.moveFromLocalFile(new Path(file.getPath()), new Path(path));
		} catch (IllegalArgumentException | IOException e) {
			path = "";
			logger.error("文件上传HDFS发生错误", e);
		} finally {
			HDFSConnectUtils.close(hdfs);
		}
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sunsharing.ihome.air.common.dfs.FileApi#batchSaveFile(java.util.List,
	 * java.lang.String)
	 */
	@Override
	public List<String> batchSaveFile(List<File> files, String folder) {
		final List<String> paths = new ArrayList<String>();
		final FileSystem hdfs = HDFSConnectUtils.getConn(hadoopUri, hadoopAccount);
		// files.forEach(file -> {
		if (files == null) {
			return paths;
		}
		for (int i = 0; i < files.size(); i++) {
			final File file = files.get(i);
			final String modifyTimeStr = toTimeDirectory();
			String path = "";
			if (StringUtils.isBlank(folder)) {
				path = "/" + modifyTimeStr.substring(0, 8) + "/" + file.getName();
			} else {
				path = "/" + format(folder) + "/" + modifyTimeStr.substring(0, 8) + "/" + file.getName();
			}
			try {
				hdfs.moveFromLocalFile(new Path(file.getPath()), new Path(path));
				paths.add(path);
			} catch (IllegalArgumentException | IOException e) {
				logger.error("文件上传HDFS发生错误", e);
				HDFSConnectUtils.close(hdfs);
				throw new RuntimeException("上传文件" + file.getName() + "时发生错误", e);
			}
		}
		// });
		HDFSConnectUtils.close(hdfs);
		return paths;
	}

	/**
	 * 文件转移
	 * 
	 * @author hanjianbo 2017年9月7日 下午5:40:43
	 * @param paths
	 *            转移文件路径集合， 可以是路路径/xxx/sxxxx 或者 /xxxx/xxxx/xxx.jpg
	 * @param dst
	 *            目标文件夹或者文件完整路径 如： /test/xxx 或者 /test/xxx/xxx.jpg
	 * @return
	 */
	@Override
	public Map<String, Object> renameFile(List<HDFSRenameMolde> paths) {
		final FileSystem hdfs = HDFSConnectUtils.getConn(hadoopUri, hadoopAccount);
		long count = 0;
		final List<String> list = new ArrayList<String>();
		;
		final Iterator<HDFSRenameMolde> iterator = paths.iterator();
		while (iterator.hasNext()) {
			final HDFSRenameMolde model = iterator.next();
			final String path = model.getKeySrc();
			final String dst = model.getDstValue();
			final String directory = dst.substring(0, dst.lastIndexOf("/"));
			try {
				boolean falg = hdfs.mkdirs(new Path(directory));
				if (!falg) {
					HDFSConnectUtils.close(hdfs);
					throw new RuntimeException("在HDFS创建文件失败：" + directory);
				}
				falg = hdfs.rename(new Path(path), new Path(dst));
				if (falg) {
					count++;
				}
			} catch (IllegalArgumentException | IOException e) {
				list.add(path);
				logger.error("修改文件夹发生错误", e);
			}
		}
		HDFSConnectUtils.close(hdfs);
		// 返回值组装
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("size", count);// 完成
		map.put("failure", list);// 返回错误的文件路径
		return map;
	}

	public static void main(String[] args) {

	}
}
