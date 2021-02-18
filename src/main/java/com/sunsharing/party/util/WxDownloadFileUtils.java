package com.sunsharing.party.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * <p>
 * 音频文件从微信服务下载及其上传DFS工具类
 * </p>
 * @author hanjianbo 2017年2月15日 下午5:09:24
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年2月15日
 * @modify by reason:{方法名}:{原因}
 */
public class WxDownloadFileUtils {
	
	/**
	 * 删除已上传的文件
	 * @author hanjianbo 2017年2月15日 下午5:51:44
	 * @param fileList
	 */
	public static void deleteFile(List<File> fileList) {
		if (fileList == null || fileList.size() == 0) {
			return;
		}
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			try {
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 从微信服务器下载文件
	 * @author hanjianbo 2017年2月15日 下午5:14:27
	 * @param url 构建的微信URL
	 * @param directoryFile 目标文件夹
	 * @param fileName 目标文件名
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static File wxServerDownload(String url, File directoryFile, String fileName) throws ClientProtocolException,
	        IOException {
		File soureFile = null;
		HttpGet get = new HttpGet(url);
		HttpClient http = new DefaultHttpClient();
		HttpResponse response = http.execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			// 请求成功后创建文件
			soureFile = new File(directoryFile, fileName);
			if (!soureFile.exists()) {
				soureFile.createNewFile();
			}
			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent(); // 上传文件
			FileOutputStream out = new FileOutputStream(soureFile);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
			out.close();
		}
		return soureFile;
	}
	
}
