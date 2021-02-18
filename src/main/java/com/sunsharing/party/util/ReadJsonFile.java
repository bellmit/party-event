package com.sunsharing.party.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * <p>
 * 读取.json文件并解析
 * </p>
 * 
 * @author chuhaitao 2017年8月10日 上午10:15:42
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年8月10日
 * @modify by reason:{方法名}:{原因}
 */
public class ReadJsonFile {

	private static Logger logger = Logger.getLogger(ReadJsonFile.class);

	/**
	 * 
	 * @author chuhaitao 2017年8月10日 上午10:52:08
	 * @param path
	 *            文件路径
	 * @param name
	 *            需要获取value值的name值
	 * @return
	 */
	public static String getNoCheckurls(String name) {
		String res = "";
		BufferedReader reader = null;
		String laststr = "";
		try {
			final InputStream fileInputStream = CheckMenu.class.getClassLoader().getResourceAsStream("verifyURL.json");
			final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		final JSONArray jsonArray = JSONArray.fromObject(laststr);
		final int size = jsonArray.size();
		try {
			for (int i = 0; i < size; i++) {
				final JSONObject jsonObject = jsonArray.getJSONObject(i);
				res += jsonObject.get(name).toString();
			}
		} catch (final Exception e) {
			logger.error("文件没有对应属性值", e);
		}
		return res;
	}
}
