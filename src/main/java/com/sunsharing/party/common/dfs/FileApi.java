/**
 * @(#)FileApi 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 * 
 *             <br>
 *             Copyright: Copyright (c) 2014 <br>
 *             Company:厦门畅享信息技术有限公司 <br>
 * @author ulyn <br>
 *         14-5-27 上午9:51 <br>
 * @version 1.0 ———————————————————————————————— 修改记录 修改者： 修改时间： 修改原因： ————————————————————————————————
 */
package com.sunsharing.party.common.dfs;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.sunsharing.common.utils.StringUtils;
import com.sunsharing.party.common.dfs.model.HDFSRenameMolde;

/**
 * <p>
 * 文件操作接口
 * </p>
 * @author hanjianbo 2017年8月15日 下午3:22:32
 * @version V1.0
 */
public interface FileApi {
	
	/**
	 * 文件路径
	 * @author hanjianbo 2017年8月15日 下午3:24:58
	 * @param filePath 文件全路径
	 * @return
	 */
	boolean deleteFile(String filePath);
	
	/**
	 * 分段读取文件
	 * @author hanjianbo 2017年8月15日 下午3:25:51
	 * @param relativeName 文件全路径
	 * @param begin 起点值
	 * @param end 截止值
	 * @return Map
	 */
	Map<String, Object> readBacthFile(String relativeName, long begin, long end);
	
	/**
	 * 读取文件为数组
	 * @author hanjianbo 2017年8月15日 下午3:28:28
	 * @param relativeName 文件全路径
	 * @return
	 */
	byte[] readFile(String relativeName);
	
	/**
	 * 上传文件
	 * @author hanjianbo 2017年8月15日 下午5:00:31
	 * @param file 文件
	 * @return
	 */
	String saveFile(File file);
	
	/**
	 * 上传文件
	 * @author hanjianbo 2017年8月15日 下午5:06:39
	 * @param file 需要上传的文件
	 * @param folder 指定上传文件根目录 /cg/xx e:testoodfs/dsfsf/s.k[sk
	 * @return
	 */
	String saveFile(File file, String folder);
	
	/**
	 * 批量上传文件 该方法只针对HDFS 、JFS 有效， DFS未做实现
	 * @author hanjianbo 2017年9月7日 下午5:09:09
	 * @param files 文件集合
	 * @param folder 目标文件夹
	 * @return
	 */
	List<String> batchSaveFile(List<File> files, String folder);
	
	/**
	 * 文件转移 该方法只针对HDFS
	 * @author hanjianbo 2017年9月7日 下午6:33:50
	 * @param paths 转移文件路径集合， 可以是路路径/xxx/sxxxx 或者 /xxxx/xxxx/xxx.jpg
	 * @param dst 目标文件夹或者文件完整路径 如： /test/xxx 或者 /test/xxx/xxx.jpg
	 * @return
	 */
	Map<String, Object> renameFile(List<HDFSRenameMolde> paths);
	
	/**
	 * 批量删除文件
	 * @author hanjianbo 2017年9月18日 下午3:29:29
	 * @param filePaths
	 */
	public void deleteFile(List<String> filePaths);
	
	/**
	 * 转换文件路径主要是针对\\格式的路径转换成/
	 * @author hanjianbo 2017年8月16日 上午9:51:35
	 * @param folder
	 * @return
	 */
	default String format(String folder) {
		folder = folder.replace("\\", "/");
		if (folder.endsWith("/")) {
			folder = folder.substring(0, folder.length() - 1);
		} else if (folder.startsWith("/")) {
			folder = folder.substring(1, folder.length());
		}
		return folder;
	}
	
	/**
	 * 获取当前时间日期，并返回yyyyMMdd格式
	 * @author hanjianbo 2017年8月15日 下午5:20:28
	 * @param file 待上传文件
	 * @return
	 */
	default String toTimeDirectory() {
		final long modifiedTime = System.currentTimeMillis();
		final String modifyTimeStr = StringUtils.transToDbDate(modifiedTime);
		return modifyTimeStr;
	}
}
