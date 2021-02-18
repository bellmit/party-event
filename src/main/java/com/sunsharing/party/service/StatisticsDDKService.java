package com.sunsharing.party.service;

import java.util.List;
import java.util.Map;

import com.sunsharing.party.model.TDdkData;
import com.sunsharing.party.model.TDdkMenu;


public interface StatisticsDDKService {
	
	/**
	 * 查询数据
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryStatisticsMapList(String code) throws Exception;
	
	/**
	 * 查询数据
	 * @param parMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryStatisticsMapList(Map<String, Object> parMap) throws Exception;
	
	/**
	 * 查询菜单
	 * @param parMap
	 * @return List
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryMenuList(Map<String, Object> parMap);
	
	/**
	 * 查询菜单对象
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public TDdkMenu queryDdkMenu(String code) throws Exception;
	
	/**
	 * 统计分析查询数据
	 * @param tableName
	 * @param tableSum
	 * @param tableOrder
	 * @return
	 */
	public List<Map<String, Object>> queryDataList(String tableName, String tableSum, String tableOrder);
	
	/**
	 * 根据父级CODE查询data数据
	 * @param parMap
	 * @return
	 */
	public List<TDdkData> queryDataConfigListByPCode(Map<String, Object> parMap);
	
	/**
	 * 根据父级CODE查询data数据
	 * @param parMap
	 * @return
	 */
	public List<Map<String, Object>> queryDataConfigMapListByPCode(Map<String, Object> parMap);
	
	public List<Map<String, Object>> queryPriceConfigMapList(Map<String, Object> parMap);
	
	/**
	 * 根据dataCode查询
	 * @param dataCode
	 * @return
	 */
	public TDdkData queryDataConfigByCode(String dataCode);
	
	/**
	 * 根据RoleId查询权限菜单
	 * @param RoleId
	 * @return
	 */
	public List<String> getMenuByRoleId(String RoleId);
	
	/**
	 * 根据groupId查询appid
	 * @author wangchuan 2017年8月10日 下午2:56:13
	 * @param groupId
	 * @return
	 */
	public List<String> getMenuByGroupId(String groupId);
	
	/**
	 * 查询网格统计
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getGridStatis(Map<String, Object> parMap);
}
