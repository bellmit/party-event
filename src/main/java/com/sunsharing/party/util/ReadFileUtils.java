package com.sunsharing.party.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ReadFileUtils {
	
	public static JSONObject getJsonMenu(String filename) {
		String url = ReadFileUtils.class.getResource("/").toString() + filename;// "menu.json";
		url = url.replace("file:", "");
		System.out.println("读取文件路径：" + url);
		
		String JsonContext = new ReadFileUtils().ReadFile(url);
		
		System.out.println("JsonContext:" + JsonContext);
		
		// JsonContext = new String(JsonContext.getBytes(), StandardCharsets.UTF_8);
		JSONObject resObject = JSON.parseObject(JsonContext);
		
		int size = resObject.size();
		System.out.println("Size: " + size + resObject);
		
		return resObject;
		
	}
	
	public String ReadFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
			System.out.println("InputStreamReader ..  ");
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
		return laststr;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// getjsonmenu("menu.json");
		getJsonMenu("config_reply.json");
		/*
		 * String url = ReadFileUtils.class.getResource("/").toString()+"menu.json"; System.out.println(url); url = url.replace("file:/", "");
		 * System.out.println(url); String JsonContext = new ReadFileUtils().ReadFile(url); //String JsonContext =""; JSONObject resObject =
		 * JSONObject.fromObject(JsonContext); int size = resObject.size(); System.out.println("Size: " + size); System.out.println(com.alibaba.fastjson
		 * .JSONObject.parseObject(resObject.toString()));
		 */
		
	}
}
