/**
 * @(#)FileLoaderServlet 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 * 
 * <br>
 *                       Copyright: Copyright (c) 2014 <br>
 *                       Company:厦门畅享信息技术有限公司 <br>
 * @author ulyn <br>
 *         14-5-27 上午11:22 <br>
 * @version 1.0 ———————————————————————————————— 修改记录 修改者： 修改时间： 修改原因： ————————————————————————————————
 */
package com.sunsharing.party.common.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.sunsharing.component.utils.base.StringUtils;

import com.sunsharing.party.common.dfs.FileApi;
import com.sunsharing.party.common.dfs.FileFactory;
import com.sunsharing.party.common.dfs.Uploader;
import com.sunsharing.party.common.response.ResponseHelper;
import com.sunsharing.party.util.ImageUtils;

/**
 * <pre></pre>
 * 
 * <br>
 * ---------------------------------------------------------------------- <br>
 * <b>功能描述:</b> <br>
 * 文件操作的servlet，统一的拦截路径，比如"/static/file/","/static/images/"; <br>
 * 注意事项: <br>
 * 文件名格式统一 时间8位加文件名 <br>
 * 附件最大不允许大于10M <br>
 * <br>
 * ---------------------------------------------------------------------- <br>
 */
public class FileLoaderServlet extends HttpServlet {
	
	Logger logger = Logger.getLogger(FileLoaderServlet.class);
	
	public static final Map<String, String> picContentType = new HashMap<String, String>() {
		
		{
			put(".gif", "image/gif;charset=utf-8");
			put(".jpg", "image/jpeg;charset=utf-8");
			put(".png", "image/png;charset=utf-8");
			put(".bmp", "image/bmp;charset=utf-8");
			put(".mp4", "video/mp4");
			put(".mp3", "audio/mp3");
		}
	};
	
	private final List<String> allowFilesList = new ArrayList<String>() {
		
		{
			add(".jpg");
			add(".png");
			add(".jpeg");
			add(".gif");
			add(".bmp");
			add(".txt");
			add(".doc");
			add(".docx");
			add(".xls");
			add(".xlsx");
			add(".ppt");
			add(".pptx");
			add(".pdf");
			add(".xml");
		}
	};
	
	// 图片后缀
	private final List<String> imgSuffixsList = new ArrayList<String>() {
		
		{
			add(".jpg");
			add(".png");
			add(".gif");
			add(".jpeg");
		}
	};
	
	// 音频后缀
	private final List<String> audioSuffixsList = new ArrayList<String>() {
		
		{
			add(".mp3");
			add(".amr");
		}
	};
	
	// 视频后缀
	private final List<String> videoSuffixsList = new ArrayList<String>() {
		
		{
			add(".mp4");
			add(".mov");
		}
	};
	// 最大文件大小，单位byte
	private final int thresholdSize = 1 * 1024 * 1024;
	private final int fileMaxSize = 5 * 1024 * 1024;
	private final int allMaxSize = 20 * 1024 * 1024;
	
	public long dealMultmedia(HttpServletRequest request, HttpServletResponse response, int contentLength, String contentType) {
		String range = request.getHeader("Range");
		response.setHeader("Accept-Ranges", "bytes");
		if (range == null) {
			range = "bytes=0-";
		}
		logger.info("Range = " + range);
		final long fileLen = contentLength;
		long start = Integer.valueOf(range.substring(range.indexOf("=") + 1, range.indexOf("-")));
		long end;
		if (range.endsWith("-")) {
			end = fileLen - 1;
		} else {
			end = Integer.valueOf(range.substring(range.indexOf("-") + 1));
		}
		
		String ContentRange = "bytes " + String.valueOf(start) + "-" + end + "/" + String.valueOf(fileLen);
		response.setStatus(206);// 带宽遏流
		response.setContentType(contentType);
		response.setContentLength(contentLength);
		response.setHeader("Content-Range", ContentRange);
		return start;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean changeStatus = getResponseStatus(request, response);
		if (changeStatus) {
			String path = request.getRequestURI();// 请求路径
			path = path.split(";")[0];// 防止带有;jsessionid=3pu2dtrm8p6i
			String fileAddress = path.substring(path.indexOf("/static/")).replace("static/images/", "")
			        .replace("static/vedio/", "").replace("static/audio/", "");// 文件地址
			String fileName = fileAddress.substring(fileAddress.lastIndexOf("/") + 1);// 文件名称
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String width = request.getParameter("width");
			String height = request.getParameter("height");
			logger.info("访问静态资源解析出请求路径= " + path + " 文件地址 = " + fileAddress + " 文件后缀 = " + suffix);
			if (!StringUtils.isBlank(fileName)) {// 文件名称存在
				response.reset();
				// 设定输出类型
				if (picContentType.get(suffix) != null) {
					response.setContentType(picContentType.get(suffix));// 设定输出的类型
				} else {
					response.setContentType("application/octet-stream");// 设定输出的类型
					response.addHeader("Content-Disposition", "attachment;" + " filename="
					        + new String(fileName.substring(1).getBytes("UTF-8"), "ISO8859-1"));
				}
				OutputStream os = response.getOutputStream();
				FileApi fileApi = FileFactory.create();
				byte[] bytes = fileApi.readFile(fileAddress);
				response.setHeader("ETag", Long.toString(System.currentTimeMillis())); // 添加ETag
				response.setStatus(HttpServletResponse.SC_OK);
				response.setDateHeader("Expires", System.currentTimeMillis() + 2592000 * 1000L);
				if (imgSuffixsList.contains(suffix)) {// 图片
					ImageUtils.resizeImage(width, height, suffix, os, bytes);// 显示缩略图
				} else if (audioSuffixsList.contains(suffix)) {
					os.write(bytes);
				} else if (videoSuffixsList.contains(suffix)) {
					InputStream fis = new ByteArrayInputStream(bytes);
					long start = dealMultmedia(request, response, fis.available(), picContentType.get(suffix));
					fis.skip(start);
					byte[] buf = new byte[1024 * 512];
					// 并发访问时的內存控制
					int len = -1;
					while ((len = fis.read(buf)) != -1) {
						os.write(buf, 0, len);
					}
					fis.close();
					logger.info("访问到资源文件==============");
				}
				os.flush();
				os.close();
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Uploader uploader = new Uploader(request);
		uploader.setAllMaxSize(allMaxSize);
		uploader.setAllMaxSize(allMaxSize);
		uploader.setAllowFilesList(allowFilesList);
		uploader.setFileMaxSize(fileMaxSize);
		uploader.setThresholdSize(thresholdSize);
		try {
			Map uploadMap = uploader.doExec();
			ResponseHelper.printOutContent(response, JSON.toJSONString(uploadMap));
		} catch (Exception e) {
			logger.error("保存文件失败", e);
			Map map = new HashMap();
			map.put("status", false);
			map.put("msg", e.getMessage());
			ResponseHelper.printOutContent(response, JSON.toJSONString(map));
		}
	}
	
	/**
	 * 根据浏览器请求的头，设置响应，当返回true时候，则输出图片，否则返回304
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean getResponseStatus(HttpServletRequest request, HttpServletResponse response) {
		long headerTime = request.getDateHeader("If-Modified-Since");
		if (headerTime > 0) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		String previousToken = request.getHeader("If-None-Match");
		if (previousToken != null) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		if (FileFactory.getType() == null) {
			throw new IllegalStateException("请先初始化com.sunsharing.ihome.air.common.file.FileFactory");
		}
		// TODO 初始化配置
	}
}
