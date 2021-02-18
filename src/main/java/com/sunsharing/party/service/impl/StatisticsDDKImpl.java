package com.sunsharing.party.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.dao.SimpleHibernateDao;
import com.sunsharing.party.model.TDdkData;
import com.sunsharing.party.model.TDdkMenu;
import com.sunsharing.party.service.StatisticsDDKService;

@Service
@Transactional
public class StatisticsDDKImpl implements StatisticsDDKService {
	
	Logger logger = Logger.getLogger(StatisticsDDKImpl.class);
	
	@Resource(name = "emjdbcTemplate")
	JdbcTemplate jdbcTemplate;
	@Resource
	JdbcTemplate sqjdbcTemplate;
	
	private SimpleHibernateDao<TDdkMenu, String> tddkMenuDao;
	private SimpleHibernateDao<TDdkData, String> tddkDataDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		tddkMenuDao = new SimpleHibernateDao<TDdkMenu, String>(sessionFactory, TDdkMenu.class);
		tddkDataDao = new SimpleHibernateDao<TDdkData, String>(sessionFactory, TDdkData.class);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryStatisticsMapList(Map<String, Object> parMap) throws Exception {
		String code = (String)parMap.get("code");
		String tar = (String)parMap.get("tar");
		String type = (String)parMap.get("type");
		String cId = (String)parMap.get("cId");
		List<Map<String, Object>> list = null;
		if (!StringUtils.isBlank(cId)) {
			list = queryStatisticsDDKMapListById(cId);
		} else {
			if (StringUtils.isBlank(type) || "gmjj".equals(type)) {
				code = StringUtils.isBlank(tar) ? code : "qqsczz".equals(tar) ? code : tar;
			}
			list = queryStatisticsDDKMapList(code);
		}
		if (list != null && list.size() > 0) {
			TDdkData ddkData = null;
			Map<String, Object> priceMap = null;
			if (!StringUtils.isBlank(tar) && !StringUtils.isBlank(type)) {
				if ("gmjj".equals(type)) {
					ddkData = queryDataConfigByCode(tar);
				} else {
					priceMap = queryPriceMapList(tar);
				}
			}
			for (Map<String, Object> map : list) {
				String viewId = (String)map.get("VIEW_ID");
				String tableId = (String)map.get("DATA_ID");
				Map<String, Object> views = queryViewsMapList(viewId);
				String paramHolder = (String)views.get("PARAM_HOLDER");
				JSONArray parList = new JSONArray();
				parList.add(paramHolder.split(","));
				// String tableSub = "";
				// if(ddkData!=null){
				// tableSub = ddkData.getTableSub();
				// if(tableSub.indexOf("$name$")!=-1){
				// tableSub = tableSub.replace("$name$", "'"+ddkData.getDataName()+"'");
				// }
				// tableSub+=" order by "+ddkData.getTableOrder();
				// }
				JSONArray listDesc = new JSONArray();
				String SQL = "";
				if ("airquality".equals(code)) {
					SQL = getTableSql_new_2(tableId, listDesc);
				} else if (StringUtils.isBlank(type) || "gmjj".equals(type)) {
					SQL = getTableSql(tableId, tar, ddkData != null ? ddkData.getDataName() : "", listDesc);
				} else {
					if (!StringUtils.isBlank(tar)) {
						SQL = getTableSql_new(tableId, (String)priceMap.get("TYPE"), (String)priceMap.get("GOODS_NAME"),
						        listDesc);
					}
				}
				logger.info("SQL:" + SQL);
				parList.add(listDesc);// 填装字段描述
				queryDataBySql(map, SQL);
				map.put("LISTDATA", parList);
				map.put("JS_URL", views.get("JS_URL"));
				// map.put("DATAHOLDER", views.get("DATA_HOLDER"));
			}
		}
		return list;
	}
	
	/**
	 * 根据sql查询数据迭代到map中
	 * @param map
	 * @param SQL
	 * @throws Exception
	 */
	public void queryDataBySql(Map<String, Object> map, String SQL) throws Exception {
		if (!StringUtils.isBlank(SQL)) {
			List<Map<String, Object>> dataList = queryMapList(SQL);
			JSONArray dataArray = new JSONArray();
			for (Map<String, Object> dataMap : dataList) {
				dataArray.add(dataMap.get("COL"));
			}
			map.put("DATAS", dataArray);
		}
	}
	
	/**
	 * 查询数据
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryStatisticsMapList(String code) throws Exception {
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("code", code);
		return queryStatisticsMapList(parMap);
	}
	
	/**
	 * 根据tableId获取table信息
	 * @param tableId
	 * @param listDesc
	 * @return 返回sql
	 * @throws Exception
	 */
	public String getTableSql(String tableId, String parentCode, String parentName, JSONArray listDesc) throws Exception {
		String SQL = "", tableName = "", tableSub = "", tableOrder = "";
		List<Map<String, Object>> listTitle = queryColumnMapList(tableId);
		for (Map<String, Object> colMap : listTitle) {
			listDesc.add(colMap.get("COLUMN_DESC"));
			if (StringUtils.isBlank(tableName)) {
				tableName = " " + colMap.get("TABLE_NAME");
			}
			if (StringUtils.isBlank(tableSub) && !StringUtils.isBlank((String)colMap.get("TABLE_SUB"))) {
				tableSub = " " + colMap.get("TABLE_SUB");
			}
			if (StringUtils.isBlank(tableOrder) && !StringUtils.isBlank((String)colMap.get("TABLE_ORDER"))) {
				// tableOrder = " "+colMap.get("TABLE_ORDER");
				tableOrder = getTableOrder((String)colMap.get("TABLE_ORDER"));
			}
			SQL += SQL.length() == 0 ? colMap.get("COLUMN_CODE") : "||','||" + colMap.get("COLUMN_CODE");
		}
		SQL = "select " + SQL + " col from" + tableName;
		// 特殊判断，如果父级编码不为空，取配置表中参数
		if (!StringUtils.isBlank(parentCode)) {
			Map<String, Object> parMap = new HashMap<String, Object>();
			parMap.put("parentCode", parentCode);
			String repStr = parentCode;// .split("_")[0];
			List<TDdkData> tDdkDatas = queryDataConfigListByPCode(parMap);// 有下级
			if (tDdkDatas != null && tDdkDatas.size() > 0) {
				String dataName = StringUtils.isBlank(parentName) ? "" : "'" + parentName + "'";
				for (TDdkData data : tDdkDatas) {
					dataName += dataName.length() == 0 ? "'" + data.getDataName() + "'" : ",'" + data.getDataName() + "'";
				}
				tableSub = tableSub.replace("$name$", dataName);
				if (!StringUtils.isBlank(tableSub) && tableSub.indexOf("$sql$") != -1) {
					SQL = tableSub.replace("$sql$", SQL);
				} else {
					SQL += tableSub + tableOrder;
				}
			} else {// 无下级
				TDdkData data = queryDataConfigByCode(parentCode);
				if (data != null) {
					tableSub = tableSub.replace("$name$", "'" + data.getDataName() + "'");
					if (!StringUtils.isBlank(tableSub) && tableSub.indexOf("$sql$") != -1) {
						SQL = tableSub.replace("$sql$", SQL);
					} else {
						SQL += tableSub + tableOrder;
					}
				}
			}
			SQL = SQL.replace("$code$", repStr);
		} else {
			SQL += tableOrder;
		}
		return SQL;
	}
	
	/**
	 * 获取tableorder排序条件
	 * @param tableOrder
	 * @return
	 */
	private String getTableOrder(String tableOrder) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(tableOrder);
			tableOrder = "";
			for (String key : jsonObject.keySet()) {
				tableOrder += tableOrder.length() == 0 ? " order by " : ",";
				tableOrder += key + " " + jsonObject.getString(key);
			}
		} catch (Exception e) {
			tableOrder = " " + tableOrder;
		}
		return tableOrder;
	}
	
	public String getTableSql_new(String tableId, String type, String name, JSONArray listDesc) throws Exception {
		String SQL = "", tableName = "", tableSub = "", tableOrder = "";
		List<Map<String, Object>> listTitle = queryColumnMapList(tableId);
		for (Map<String, Object> colMap : listTitle) {
			listDesc.add(colMap.get("COLUMN_DESC"));
			if (StringUtils.isBlank(tableName)) {
				tableName = " " + colMap.get("TABLE_NAME");
			}
			if (StringUtils.isBlank(tableSub) && !StringUtils.isBlank((String)colMap.get("TABLE_SUB"))) {
				tableSub = " " + colMap.get("TABLE_SUB");
			}
			if (StringUtils.isBlank(tableOrder) && !StringUtils.isBlank((String)colMap.get("TABLE_ORDER"))) {
				// tableOrder = " "+colMap.get("TABLE_ORDER");
				tableOrder = getTableOrder((String)colMap.get("TABLE_ORDER"));
			}
			SQL += SQL.length() == 0 ? colMap.get("COLUMN_CODE") : "||','||" + colMap.get("COLUMN_CODE");
		}
		SQL = "select " + SQL + " col from" + tableName;
		// 特殊判断，如果编码不为空，取配置表中参数
		if (!StringUtils.isBlank(type)) {
			tableSub = tableSub.replace("$code$", type).replace("$name$", name);
			SQL += tableSub;
		}
		return SQL + tableOrder;
	}
	
	public String getTableSql_new_2(String tableId, JSONArray listDesc) throws Exception {
		String SQL = "", tableName = "", tableSub = "", tableOrder = "";
		List<Map<String, Object>> listTitle = queryColumnMapList(tableId);
		for (Map<String, Object> colMap : listTitle) {
			listDesc.add(colMap.get("COLUMN_DESC"));
			if (StringUtils.isBlank(tableName)) {
				tableName = " " + colMap.get("TABLE_NAME");
			}
			if (StringUtils.isBlank(tableSub) && !StringUtils.isBlank((String)colMap.get("TABLE_SUB"))) {
				tableSub = " " + colMap.get("TABLE_SUB");
			}
			if (StringUtils.isBlank(tableOrder) && !StringUtils.isBlank((String)colMap.get("TABLE_ORDER"))) {
				// tableOrder = " "+colMap.get("TABLE_ORDER");
				tableOrder = getTableOrder((String)colMap.get("TABLE_ORDER"));
			}
			SQL += SQL.length() == 0 ? colMap.get("COLUMN_CODE") : "||','||" + colMap.get("COLUMN_CODE");
		}
		SQL = "select " + SQL + " col from" + tableName;
		SQL += tableSub;
		return SQL + tableOrder;
	}
	
	/**
	 * 根据编码查询所有配置信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryStatisticsDDKMapList(String code) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT t.* FROM t_ddk_controller t where t.con_code=?  or t.con_id = '" + code
		        + "' and status=1 ORDER BY t.sort_order");
		return jdbcTemplate.queryForList(sb.toString(), code);
	}
	
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryStatisticsDDKMapListById(String id) throws Exception {
		StringBuffer sb = new StringBuffer(
		        "SELECT t.* FROM t_ddk_controller t where t.con_id=? and status=1 ORDER BY t.sort_order");
		return jdbcTemplate.queryForList(sb.toString(), id);
	}
	
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryMapList(String sql) throws Exception {
		StringBuffer sb = new StringBuffer(sql);
		// if(!StringUtils.isBlank(sqlSum)){
		// if(sql.indexOf("where")==-1)
		// {
		// sb.append(" where 1=1 ");
		// }
		// sb.append(sqlSum);
		// }
		return jdbcTemplate.queryForList(sb.toString());
	}
	
	/**
	 * 根据表ID查询下属字段说明
	 * @param tableId
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryColumnMapList(String tableId) throws Exception {
		StringBuffer sb = new StringBuffer(
		        "SELECT tc.*,tt.table_name,tt.table_sub,tt.table_order FROM t_ddk_data_table tt,t_ddk_data_column tc where tt.table_id=tc.table_id and tt.table_id=?  ORDER BY sort_order");
		return jdbcTemplate.queryForList(sb.toString(), tableId);
	}
	
	/**
	 * 根据展示组件配置ID查询数据
	 * @param viewId
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> queryViewsMapList(String viewId) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT t.* FROM t_ddk_view t where t.view_id=? and rownum<2");
		return jdbcTemplate.queryForMap(sb.toString(), viewId);
	}
	
	/*************************** 菜单开始 **********************************/
	/**
	 * 查询菜单
	 * @param parMap
	 * @return List
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryMenuList(Map<String, Object> parMap) {
		StringBuffer sb = new StringBuffer(
		        "select * from  t_ddk_menu start with parent_code=? connect by prior menu_code=parent_code order by sort_order");
		String pCode = (String)parMap.get("pCode");
		if (StringUtils.isBlank(pCode)) {
			pCode = "0";
		}
		return jdbcTemplate.queryForList(sb.toString(), pCode);
	}
	
	@Override
	@Transactional(readOnly = true)
	public TDdkMenu queryDdkMenu(String code) throws Exception {
		return tddkMenuDao.get(code);
	}
	
	/***************************** 统计分析查询 ***********************************/
	/**
	 * 统计分析查询数据
	 * @param tableName
	 * @param tableSum
	 * @param tableOrder
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryDataList(String tableName, String tableSum, String tableOrder) {
		return jdbcTemplate.queryForList("select * from ? where 1=1 ? ?", new Object[] {tableName, tableSum, tableOrder});
	}
	
	/**
	 * 根据父级CODE查询data数据
	 * @param parentCode
	 * @return
	 */
	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Transactional(readOnly = true)
	public List<TDdkData> queryDataConfigListByPCode(Map<String, Object> parMap) {
		String parentCode = (String)parMap.get("parentCode");
		String len = (String)parMap.get("len");
		List<Object> listPar = new ArrayList<Object>();
		StringBuffer sBuffer = new StringBuffer(
		        "select distinct td.*,tc.con_code from  t_ddk_controller tc right join t_ddk_data td");
		sBuffer.append(" on tc.con_code=td.data_code start with td.parent_code=? connect by prior td.data_code=td.parent_code");
		listPar.add(parentCode);
		if (!StringUtils.isBlank(len)) {
			sBuffer.append(" and length(td.sort_order)<?");
			listPar.add(Integer.valueOf(len));
		}
		sBuffer.append(" and td.status=1 order by td.sort_order");
		return jdbcTemplate.query(sBuffer.toString(), listPar.toArray(), new BeanPropertyRowMapper(TDdkData.class));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryDataConfigMapListByPCode(Map<String, Object> parMap) {
		String parentCode = (String)parMap.get("parentCode");
		String len = (String)parMap.get("len");
		List<Object> listPar = new ArrayList<Object>();
		StringBuffer sBuffer = new StringBuffer(
		        "select * from  t_ddk_data start with parent_code=? connect by prior data_code=parent_code");
		listPar.add(parentCode);
		if (!StringUtils.isBlank(len)) {
			sBuffer.append(" and length(sort_order)<?");
			listPar.add(Integer.valueOf(len));
		}
		sBuffer.append(" order by sort_order");
		return jdbcTemplate.queryForList(sBuffer.toString(), listPar.toArray());
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryPriceConfigMapList(Map<String, Object> parMap) {
		StringBuffer sBuffer = new StringBuffer("SELECT goods_name name,id,type FROM t_live_price  ORDER BY type");
		return jdbcTemplate.queryForList(sBuffer.toString());
	}
	
	@Override
	@Transactional(readOnly = true)
	public TDdkData queryDataConfigByCode(String dataCode) {
		return tddkDataDao.get(dataCode);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> getMenuByRoleId(String RoleId) {
		List<String> list = new ArrayList<String>();
		String items = "";
		StringBuffer sql = new StringBuffer("");
		sql.append("select ITEMS from T_USER_ROLE_APP where 1=1");
		try {
			String[] roleIds = RoleId.split(",");
			sql.append(" AND ROLE_ID in (");
			for (int i = 0; i < roleIds.length; i++) {
				if (i > 0) {
					sql.append(",");
				}
				sql.append("'" + roleIds[i] + "'");
			}
			sql.append(") and MODULE_ID='ldmh' and ROLE_APP_ID limit 0,1");
			logger.info(sql.toString());
			items = sqjdbcTemplate.queryForObject(sql.toString(), String.class);
			items = items.replaceAll("#0", "");
			String[] s = items.split(",");
			logger.info("items:" + items + ",length:" + s.length);
			list = Arrays.asList(s);
		} catch (Exception e) {
			logger.info("没有相应的菜单权限");
		}
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<String> getMenuByGroupId(String groupId) {
		List<String> list = new ArrayList<String>();
		String items = "";
		StringBuffer sql = new StringBuffer("");
		sql.append("select a.APP_ID from t_auth_app_virtual v,t_auth_app a where v.APP_ID = a.APP_ID and v.GROUP_ID=?");
		try {
			logger.info(sql.toString() + ";" + groupId);
			List<Map<String, Object>> list1 = sqjdbcTemplate.queryForList(sql.toString(), groupId);
			// items = items.replaceAll("#0", "");
			// String[] s = items.split(",");
			// logger.info("items:" + items + ",length:" + s.length);
			// list = Arrays.asList(s);
			if (list1 != null && list1.size() > 0) {
				//
				for (Map<String, Object> map : list1) {
					//
					list.add((String)map.get("APP_ID"));
				}
			}
		} catch (Exception e) {
			logger.info("没有相应的菜单权限");
		}
		return list;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> queryPriceMapList(String id) {
		StringBuffer sBuffer = new StringBuffer("SELECT * FROM t_live_price where 1=1");
		List<Object> listPar = new ArrayList<Object>();
		if (!StringUtils.isBlank(id)) {
			sBuffer.append(" and id=?");
			listPar.add(id);
		}
		
		return jdbcTemplate.queryForMap(sBuffer.toString(), id);
	}
	
	@Override
	public List<Map<String, Object>> getGridStatis(Map<String, Object> parMap) {
		String sql = "select id,key,value,child_type,type from t_grid_statis t where 1=1";
		List<Object> params = new ArrayList<Object>();
		String type = (String)parMap.get("type");
		if (!StringUtils.isBlank(type)) {
			if (type.equals("grid")) {
				sql += " and type = ?";
				params.add(type);
			}
		}
		sql += " order by t.sort";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, params.toArray());
		return results;
	}
}
