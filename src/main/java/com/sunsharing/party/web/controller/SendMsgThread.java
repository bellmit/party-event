package com.sunsharing.party.web.controller;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.msgcenter.productClient.MsgSendClient;
/**
 * 
* @ClassName: SendMsgThread 
* @Description: 下载微信图片到dfs，新建线程来完成
* @author ZD
* @date 2016年7月14日 下午1:29:31 
*
 */
public class SendMsgThread implements Runnable{
	Logger logger = Logger.getLogger(SendMsgThread.class);
	
	public String url;
	public String localImageFile;
	 
	public void setUrl(String url) {
		this.url = url;
	}

	public void setLocalImageFile(String localImageFile) {
		this.localImageFile = localImageFile;
	}

	public void run() {
		logger.info("启用线程，发送图片消息到消息中心。完成下载微信图片到dfs");
		 MsgSendClient client = new MsgSendClient();
		 JSONObject obj = new JSONObject();
			obj.put("topic", "WX_PHO");
			obj.put("expires", "10");
			obj.put("url", this.url);
			obj.put("name", this.localImageFile);
			logger.info("发送到消息中心的：url:"+url  +"   localImageFile:"+localImageFile);
			client.sendMsg(obj.toJSONString());
	 }

}
