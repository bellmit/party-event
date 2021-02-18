package com.sunsharing.party.common.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.ihome.air.common.anno.AirService;
import com.sunsharing.party.entity.query.Pagination;

@AirService
public interface WeixinQYHService {
	/**
	 * 保存应急采集信息
	 * 
	 * @param map
	 * @return failed 失败 no_user 用户数据不存在 sucess成功
	 */
	public String saveEmergencyInfo(Map map, Object[] localImagelObjects);

	/**
	 * 获取应急采集信息列表
	 * 
	 * @param map
	 * @return
	 */
	public Pagination queryEmeryList(Map<String, Object> map);

	/**
	 * 获取处理步骤
	 * 
	 * @param MSG_ID
	 * @return
	 */
	public List queryMessagetant(Map<String, String> map);

	/**
	 * 获取后续办理步骤
	 * 
	 * @param MSG_ID
	 * @return
	 */
	public List queryLaterTant(Map<String, String> map);

	/**
	 * 获取应急类型
	 * 
	 * @return
	 */
	public List getEmergencyType(String typeIds);

	/**
	 * 获取应急采集 需要下发微信模板消息的用户
	 * 
	 * @return
	 */
	public ArrayList<String> getEmergencySMSUser(Map map);

	/**
	 * 获取应急图片
	 * 
	 * @return
	 */
	public List getEmergencyImage(String MSG_ID);

	/**
	 * 保存领导处理意见
	 * 
	 * @param map
	 * @return
	 */
	public String insertMessage(Map map);

	/**
	 * 查询用户
	 * 
	 * @param map
	 * @return
	 */
	public List queryUserInfo(Map map);

	/**
	 * 批量更新微信用户
	 * 
	 * @return 批量更新的用户总数
	 */
	public int WXUser_batchUpdate(List<JSONObject> wxUserList, String valueId);

	/**
	 * 查询领导
	 * 
	 * @param parMap
	 * @return
	 * 
	 * 		parMap.put("pageSize", "9999"); parMap.put("depLevel", "5");
	 *         parMap.put("userId", "");
	 */
	public Pagination queryEMUserDepSMSList(Map<String, Object> parMap);

	/**
	 * 存base64编码文件
	 * 
	 * @param imgCode
	 * @return
	 */
	public String saveImg(String imgCode);

	/**
	 * 根据userId查询用户信息
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getSignUserExt(String userId);

	/**
	 * 查询社区列表
	 * @return
	 */
	public List<Map<String, Object>> getXzqhList();
}
