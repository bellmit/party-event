package com.sunsharing.party.common.weixin;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信接口适配器 让微信接口实现类继承此适配器避免实现接口中的每一个方法
 * <p>
 * </p>
 * 
 * @author wxl 2017年11月22日 上午9:24:09
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月22日
 * @modify by reason:{方法名}:{原因}
 */
public abstract class WeiXinApi {

	/**
	 * 获取token
	 * 
	 * @author wxl 2017年11月14日 上午9:16:43
	 * @param appid
	 * @param secret
	 * @return access_token
	 */
	public String getAccessToken(String appid, String secret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @author wxl 2017年11月21日 下午6:50:47
	 * @param param
	 *            { "touser":[ "OPENID1", "OPENID2" ], "mpnews":{
	 *            "media_id":"123dsdajkasd231jhksad" }, "msgtype":"mpnews"，
	 *            "send_ignore_reprint":0 }
	 * @param appid
	 * @param secret
	 * @return { "type":"video",
	 *         "media_id":"IhdaAQXuvJtGzwwc0abfXnzeezfO0NgPK6AQYShD8RQYMTtfzbLdBIQkQziv2XJc",
	 *         "created_at":1398848981 }
	 */
	public JSONObject sendNewsByOpenId(JSONObject param, String appid, String secret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @author wxl 2017年11月22日 上午9:00:56
	 * @param appid
	 * @param secret
	 * @param code
	 * @return { "access_token":"ACCESS_TOKEN",
	 * 
	 *         "expires_in":7200,
	 * 
	 *         "refresh_token":"REFRESH_TOKEN",
	 * 
	 *         "openid":"OPENID",
	 * 
	 *         "scope":"SCOPE" }
	 */
	public JSONObject getOpenIdByCode(String appid, String secret, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 2016年07月08日 调用微信的js sdk 需要的jsapi_ticket jsapi_ticket
	 * 生成签名之前必须先了解一下jsapi_ticket，jsapi_ticket是公众号用于调用微信JS接口的临时票据。正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取
	 * 。由于获取jsapi_ticket的api调用次数非常有限，频繁刷新jsapi_ticket会导致api调用受限，影响自身业务，开发者必须在自己的服务全局缓存jsapi_ticket
	 * 
	 * @author wxl 2017年11月22日 上午9:02:38
	 * @param appid
	 * @param secret
	 * @return ticket
	 */
	public String getTicket(String appid, String secret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据access_token和openId获取用户信息
	 * 
	 * @author wxl 2017年11月20日 下午7:21:57
	 * @param accessToken
	 * @param openId
	 * @return { "openid":" OPENID",
	 * 
	 *         " nickname": NICKNAME,
	 * 
	 *         "sex":"1",
	 * 
	 *         "province":"PROVINCE"
	 * 
	 *         "city":"CITY",
	 * 
	 *         "country":"COUNTRY",
	 * 
	 *         "headimgurl":
	 *         "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ
	 * 
	 *         4eMsv84eavHiaiceqxibJxCfHe/46",
	 * 
	 *         "privilege":[ "PRIVILEGE1" "PRIVILEGE2" ],
	 * 
	 *         "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
	 * 
	 *         }
	 */
	public JSONObject getUserInfo(String accessToken, String openId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取用户
	 * 
	 * @author wxl 2017年11月15日 下午2:38:22
	 * @param userId
	 * @param corpid
	 * @param corpsecret
	 * 			@return{ "errcode": 0, "errmsg": "ok", "userid": " zhangsan",
	 *            "name": "李四", "department": [1, 2], "position": "后台工程师"}
	 */
	public JSONObject getWeiXinUser(String userId, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 添加标签成员
	 * 
	 * @author wxl 2017年11月16日 上午9:11:45
	 * @param jsonObject
	 *            { "tagid": 1, "userlist":[ "user1","user2"], "partylist": [4] }
	 * @param corpid
	 * @param corpsecret
	 * @return { "errcode": 0, "errmsg": "ok" }
	 */
	public JSONObject addUserInTag(JSONObject jsonObject, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 创建标签
	 * 
	 * @author wxl 2017年11月16日 上午9:12:05
	 * @param jsonObject
	 *            { "tagname": "UI", "tagid": id }
	 * @param corpid
	 * @param corpsecret
	 * @return { "errcode": 0, "errmsg": "created" "tagid": 1 }
	 */
	public JSONObject createTag(JSONObject jsonObject, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取标签用户
	 * 
	 * @author wxl 2017年11月16日 上午9:12:27
	 * @param corpid
	 * @param corpsecret
	 * @param tagId
	 * @return { "errcode": 0, "errmsg": "ok", "userlist": [ { "userid": "zhangsan",
	 *         "name": "李四" } ], "partylist": [2] }
	 */
	public JSONObject getTagUser(String corpid, String corpsecret, String tagId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 删除标签
	 * 
	 * @author wxl 2017年11月16日 上午9:12:52
	 * @param corpid
	 * @param corpsecret
	 * @param tagId
	 * 			@return{ "errcode": 0, "errmsg": "deleted" }
	 */
	public JSONObject delTag(String corpid, String corpsecret, String tagId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 更新标签
	 * 
	 * @author wxl 2017年11月16日 上午9:13:57
	 * @param jsonObject
	 *            { "tagid": 1, "tagname": "UI design" }
	 * @param corpid
	 * @param corpsecret
	 * 			@return{ "errcode": 0, "errmsg": "updated" }
	 */
	public JSONObject updateTag(JSONObject jsonObject, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 删除标签中的成员
	 * 
	 * @author wxl 2017年11月16日 上午9:15:37
	 * @param jsonObject
	 *            { "tagid": 1, "userlist":[ "user1","user2"], "partylist":[2,4] }
	 * @param corpid
	 * @param corpsecret
	 * @return { "errcode": 0, "errmsg": "deleted" }
	 */
	public JSONObject delUserInTag(JSONObject jsonObject, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取标签列表
	 * 
	 * @author wxl 2017年11月16日 上午9:15:59
	 * @param corpid
	 * @param corpsecret
	 * @return { "errcode": 0, "errmsg": "ok", "taglist":[
	 *         {"tagid":1,"tagname":"a"}, {"tagid":2,"tagname":"b"} ] }
	 */
	public JSONObject getTagList(String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 批量删除微信用户
	 * 
	 * @author wxl 2017年11月16日 上午9:16:25
	 * @param jsonObject
	 *            { "useridlist": ["zhangsan", "lisi"] }
	 * @param corpid
	 * @param corpsecret
	 * 			@return{ "errcode": 0, "errmsg": "deleted" }
	 */
	public JSONObject batchDelUser(JSONObject jsonObject, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取部门列表
	 * 
	 * @author wxl 2017年11月16日 上午9:16:48
	 * @param corpid
	 * @param corpsecret
	 * 			@return{ "errcode": 0, "errmsg": "ok", "department": [ { "id": 2,
	 *            "name": "广州研发中心", "parentid": 1, "order": 10 }, { "id": 3 "name":
	 *            "邮箱产品部", "parentid": 2, "order": 40 } ] }
	 */
	public JSONObject getDeptList(String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取部门成员
	 * 
	 * @author wxl 2017年11月16日 上午9:17:15
	 * @param departmentId
	 * @param corpid
	 * @param corpsecret
	 * 			@return{ "errcode": 0, "errmsg": "ok", "userlist": [ { "userid":
	 *            "zhangsan", "name": "李四", "department": [1, 2] } ] }
	 */
	public JSONObject getDeptUser(String departmentId, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取部门用户信息
	 * 
	 * @author wxl 2017年11月16日 上午9:17:45
	 * @param jsonObject
	 * @param corpid
	 * @param corpsecret
	 * @param departmentId
	 * @return { "errcode": 0, "errmsg": "ok", "userlist": [ { "userid": "zhangsan",
	 *         "name": "李四", "department": [1, 2], "position": "后台工程师", "mobile":
	 *         "15913215421", "gender": "1", "email": "zhangsan@gzdev.com",
	 *         "weixinid": "lisifordev", "avatar":
	 *         "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0",
	 *         "status": 1, "extattr":
	 *         {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
	 *         } ] }
	 */
	public JSONObject getDeptUserInfo(String corpid, String corpsecret, String departmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 更新用户
	 * 
	 * @author wxl 2017年11月15日 上午11:01:39
	 * @param jsonObject
	 *            { "userid": "zhangsan", "name": "李四", "department": [1],
	 *            "position": "后台工程师", "mobile": "15913215421", "gender": "1",
	 *            "email": "zhangsan@gzdev.com", "weixinid": "lisifordev", "enable":
	 *            1, "avatar_mediaid":
	 *            "2-G6nrLmr5EC3MNb_-zL1dDdzkd0p7cNliYu9V5w7o8K0", "extattr":
	 *            {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
	 *            }
	 * @param corpid
	 * @param corpsecret
	 * @return { "errcode": 0, "errmsg": "updated" }
	 */
	public JSONObject updateWeiXinUser(JSONObject jsonObject, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据code获取用户信息
	 * 
	 * @author wxl 2017年11月22日 上午9:10:16
	 * @param code
	 * @param corpid
	 * @param corpsecret
	 * @return 企业成员{ "UserId":"USERID", "DeviceId":"DEVICEID" "user_ticket":
	 *         "USER_TICKET"， "expires_in":7200 } 非企业成员 { "OpenId":"OPENID",
	 *         "DeviceId":"DEVICEID" }
	 */
	public JSONObject getUserInfoByCode(String code, String corpid, String corpsecret) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 公众号授权中的刷新access_token(此access_token不是基础支持中的access_token)
	 * 
	 * @author wxl 2017年11月22日 上午9:20:36
	 * @param appid
	 * @param grant_type
	 * @param refresh_token
	 *            有效期为30天，当refresh_token失效之后，需要用户重新授权
	 * @return { "access_token":"ACCESS_TOKEN",
	 * 
	 *         "expires_in":7200,
	 * 
	 *         "refresh_token":"REFRESH_TOKEN",
	 * 
	 *         "openid":"OPENID",
	 * 
	 *         "scope":"SCOPE" }
	 */
	public JSONObject refreshToken(String appid, String refresh_token) {
		// TODO Auto-generated method stub
		return null;
	}

	public String generateConfigJson(HttpServletRequest request) {
		return null;
	}

	public String generateConfigJsonQYH(HttpServletRequest request) {
		return null;
	}

	public JSONObject getWXUserInfo_BywxUserId(String wx_UserId) {
		return null;
	}

}
