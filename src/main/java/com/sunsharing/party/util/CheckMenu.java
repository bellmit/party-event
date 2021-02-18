package com.sunsharing.party.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;

public class CheckMenu {

	static Logger logger = Logger.getLogger(CheckMenu.class);

	public static String ReadFile(String secret, String menu) {
		logger.info("需要判断配置文件的参数：" + secret + "    menu: " + menu);
		BufferedReader reader = null;
		String laststr = "";
		try {
			InputStream fileInputStream = CheckMenu.class.getClassLoader().getResourceAsStream("menu.json");
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		JSONArray array = JSONArray.parseArray(laststr);
		String checkurl = "";
		// 先判断有没有secret
		for (Object object : array) {
			JSONObject json = (JSONObject) object;
			String jsonSecret = json.getString("Secret");
			if (jsonSecret.equals(secret)) {
				logger.info("该应用在配置文件中存在！");
				checkurl = json.getString("Checkurls");
			}
		}
		if (StringUtils.isBlank(checkurl)) {
			logger.info("该应用不在配置文件里面");
			return "ok";
		} else {
			if (checkurl.contains(menu)) {
				logger.info("到这里说明请求的菜单找到了匹配的项");
				return "ok";
			} else {
				logger.info("没有匹配的url");
				return "no";
			}
		}
	}
}
