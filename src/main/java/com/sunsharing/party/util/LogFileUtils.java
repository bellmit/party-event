package com.sunsharing.party.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunsharing.component.utils.base.StringUtils;

/**
 * @author Tom
 */
public class LogFileUtils {
	
	public static List<File> getFiles(File folder) {
		List<File> files = new ArrayList<File>();
		iterateFolder(folder, files);
		return files;
	}
	
	private static void iterateFolder(File folder, List<File> files) {
		File flist[] = folder.listFiles();
		files.add(folder);
		if (flist == null || flist.length == 0) {
			files.add(folder);
		} else {
			for (File f : flist) {
				if (f.isDirectory()) {
					iterateFolder(f, files);
				} else {
					files.add(f);
				}
			}
		}
	}
	
	public static final String PROPER_FILE_NAME = "gd.properties";
	static Map<String, String> proMap = LogFileUtils.readPropertiesToMap(URLDecoder.decode(LogFileUtils.class.getResource("/")
	        .getPath() + "config.properties"));
	
	public static String getValue(String str) {
		return proMap.get(str);
	}
	
	/**
	 * 获取配置文件键值对
	 * @param filePath
	 * @return
	 */
	public static Map<String, String> readPropertiesToMap(String filePath) {
		Properties props = new Properties();
		InputStreamReader in;
		try {
			if (StringUtils.isBlank(filePath)) {
				filePath = URLDecoder.decode(LogFileUtils.class.getResource("/").getPath() + PROPER_FILE_NAME);
			}
			in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Enumeration enum1 = props.propertyNames();
		Map<String, String> map = new HashMap<String, String>();
		while (enum1.hasMoreElements()) {
			String strKey = (String)enum1.nextElement();
			String strValue = "";
			// try {
			strValue = props.getProperty(strKey);
			// strValue = new String(strValue.getBytes(),"utf-8");
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// }
			map.put(strKey, strValue);
		}
		System.out.println("initFile:" + map);
		return map;
	}
	
	public static void FileDown(HttpServletRequest request, HttpServletResponse response, String filePath, String newFileName)
	        throws UnsupportedEncodingException, IOException {
		// response.setContentType("application/vnd.ms-excel");
		String fileName = request.getParameter("fileName");
		if (!StringUtils.isBlank(fileName)) {
			fileName = URLDecoder.decode(fileName, "utf-8");
		}
		if (!StringUtils.isBlank(filePath)) {
			fileName = filePath;
		}
		// System.out.println(System.getProperty("user.dir"));
		// fileName = MyFileUtils.class.getClass().getResource("/").getPath()+fileName;
		// 当前文件路径
		File file = new File(fileName);
		// 清空response
		// response.reset();
		// 设置response的Header
		// response.addHeader("Content-Disposition", "attachment;filename="+(StringUtils.isBlank(newFileName)?fileName:newFileName));
		// response.addHeader("Content-Length", "" + file.length());
		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");
		// 2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
		response.setHeader("Content-Disposition", "attachment;fileName="
		        + (StringUtils.isBlank(newFileName) ? fileName : newFileName));
		ServletOutputStream out;
		
		try {
			FileInputStream inputStream = new FileInputStream(file);
			
			// 3.通过response获取ServletOutputStream对象(out)
			out = response.getOutputStream();
			
			int b = 0;
			byte[] buffer = new byte[512];
			while (b != -1) {
				b = inputStream.read(buffer);
				// 4.写到输出流(out)中
				out.write(buffer, 0, b);
			}
			inputStream.close();
			out.close();
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
