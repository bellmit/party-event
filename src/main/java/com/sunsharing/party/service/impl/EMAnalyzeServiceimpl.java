/**
 * @ProjectName: 社会服务子应用
 * @Copyright: 2016 Sunsharing Technology Co., Ltd. All Right Reserved.
 * @address: http://www.sunsharing.com.cn
 * @date: 2016年9月20日 上午11:15:23
 * @Description: 本内容仅限于厦门畅享信息技术有限公司内部使用，禁止转发.
 */
package com.sunsharing.party.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.service.EmAnalyzeService;
import com.sunsharing.party.service.impl.util.QueryPagination;
import com.sunsharing.party.util.ArrayUtils;

/**
 * <p>
 * </p>
 * @author zhengdong 2016年9月20日 上午11:15:23
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2016年9月20日
 * @modify by reason:{方法名}:{原因}
 */
@Component(value = "emAnalyzeService")
@Transactional
public class EMAnalyzeServiceimpl extends QueryPagination implements EmAnalyzeService {
	
	Logger logger = Logger.getLogger(EmergencyServiceImpl.class);
	
	@Resource
	JdbcTemplate jdbcTemplate;// oracle
	
	@Resource
	JdbcTemplate sqjdbcTemplate;// mysql
	
	@Override
	public List<Map<String, Object>> queryAnalyzeConf(Map<String, String> paramMap) {
		String AC_ID = paramMap.get("AC_ID");
		ArrayList<String> paramArrayList = new ArrayList<String>();
		
		StringBuilder querySQL = new StringBuilder();
		querySQL.append("select t.* from t_em_analyzeconf  t where 1=1 and t.STATUS=1 ");
		
		if (!StringUtils.isBlank(AC_ID)) {
			querySQL.append(" and t.AC_ID = ? ");
			paramArrayList.add(AC_ID);
		}
		
		querySQL.append("  order by t.sort_no asc");
		
		List<Map<String, Object>> queryConfigForList = jdbcTemplate.queryForList(querySQL.toString(), paramArrayList.toArray());
		return queryConfigForList;
	}
	
	@Override
	public ArrayList<Map<String, Object>> getEMAnalyzeInfoByConfSQL(Map<String, String> paramMap) {
		
		String LNG = paramMap.get("LNG");
		String LAT = paramMap.get("LAT");
		String radiusDefault = paramMap.get("radiusDefault");
		
		double[] dou = ArrayUtils.lonLat2Mercator(Double.valueOf(LNG), Double.valueOf(LAT));
		
		List<Map<String, Object>> queryConfigForList = queryAnalyzeConf(paramMap);
		
		ArrayList<Map<String, Object>> resArrayList = new ArrayList<Map<String, Object>>();
		
		if (queryConfigForList.size() > 0) {
			for (Map<String, Object> map : queryConfigForList) {
				String LIST_TYPE = (String)map.get("LIST_TYPE");// 地图类型
				Map<String, Object> fireMap = new HashMap<String, Object>();
				if ("heatmap".equals(LIST_TYPE)) {
					// 热力图
					fireMap = getFireHeatMap(map, dou, radiusDefault);
				} else if ("draw".equals(LIST_TYPE)) {
					// 瞄点
					fireMap = getFireDrawMap(map, dou, radiusDefault);
				}
				resArrayList.add(fireMap);
				break;
			}
		}
		
		return resArrayList;
	}
	
	/**
	 * 瞄点 数据获取
	 * @author zhengdong 2016年9月20日 下午3:07:47
	 * @param map
	 */
	private Map<String, Object> getFireDrawMap(Map<String, Object> pramMap, double[] xy, String radiusDefault) {
		
		String ACS_DATABASE = (String)pramMap.get("ACS_DATABASE");// 检索数据源
		String ACS_SQL = (String)pramMap.get("ACS_SQL");// 检索sql
		String MAP_TYPE = (String)pramMap.get("MAP_TYPE");// 地图类型 0-经纬度 1-墨卡托
		
		List<Map<String, Object>> list = null;
		
		if ("orl".equals(ACS_DATABASE))// oracle数据获取
		{
			list = jdbcTemplate.queryForList(ACS_SQL);
		} else if ("mq".equals(ACS_DATABASE)) {// mysql数据获取
			StringBuilder sBuilder = setSQL(xy, radiusDefault, ACS_SQL);
			list = sqjdbcTemplate.queryForList(sBuilder.toString());
		}
		
		if (list == null) {
			logger.info("没有查询到数据：ACS_DATABASE" + ACS_DATABASE + "  ACS_SQL:" + ACS_SQL + "  MAP_TYPE:" + MAP_TYPE);
			return new HashMap<String, Object>();
		}
		
		// 经纬度转墨卡托
		if ("0".equals(MAP_TYPE)) {
			for (Map<String, Object> map : list) {// 循环数据记录
				if ("0".equals(MAP_TYPE)) {
					double[] dou = ArrayUtils.lonLat2Mercator(Double.valueOf(map.get("MAPS_X").toString()),
					        Double.valueOf(map.get("MAPS_Y").toString()));
					map.put("MAPS_X", dou[0] + "");
					map.put("MAPS_Y", dou[1] + "");
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bean", pramMap);
		map.put("list", list);
		return map;
	}
	
	private StringBuilder setSQL(double[] xy, String radiusDefault, String ACS_SQL) {
		double x = xy[0];
		double y = xy[1];
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("select * from ");
		sBuilder.append("(" + ACS_SQL + ") tttt");
		sBuilder.append(" where tttt.MAPS_X > " + (x - Integer.valueOf(radiusDefault)));
		sBuilder.append(" and tttt.MAPS_X < " + (x + Integer.valueOf(radiusDefault)));
		
		sBuilder.append(" and tttt.MAPS_Y > " + (y - Integer.valueOf(radiusDefault)));
		sBuilder.append(" and tttt.MAPS_Y < " + (y + Integer.valueOf(radiusDefault)));
		
		logger.info("组装查询mysql 的sql：" + sBuilder.toString());
		return sBuilder;
	}
	
	/**
	 * 热力图 数据获取
	 * @author zhengdong 2016年9月20日 下午2:56:20
	 */
	private Map<String, Object> getFireHeatMap(Map<String, Object> pramMap, double[] xy, String radiusDefault) {
		
		String ACS_DATABASE = (String)pramMap.get("ACS_DATABASE");// 检索数据源
		String ACS_SQL = (String)pramMap.get("ACS_SQL");// 检索sql
		String MAP_TYPE = (String)pramMap.get("MAP_TYPE");// 地图类型 0-经纬度 1-墨卡托
		
		List<Map<String, Object>> list = null;
		
		if ("orl".equals(ACS_DATABASE))// oracle数据获取
		{
			list = jdbcTemplate.queryForList(ACS_SQL);
		} else if ("mq".equals(ACS_DATABASE)) {// mysql数据获取
			StringBuilder sBuilder = setSQL(xy, radiusDefault, ACS_SQL);
			list = sqjdbcTemplate.queryForList(sBuilder.toString());
		}
		JSONArray array = new JSONArray();
		JSONObject jsonObject = null;
		JSONObject spatialReference = null;
		JSONObject jsonObjectGeometry = null;
		JSONObject jsonObjectAttributes = null;
		
		if (list == null) {
			logger.info("没有查询到数据：ACS_DATABASE" + ACS_DATABASE + "  ACS_SQL:" + ACS_SQL + "  MAP_TYPE:" + MAP_TYPE);
			return new HashMap<String, Object>();
		}
		
		float x = 0, y = 0, xf = 0, yf = 0;
		for (Map<String, Object> map : list) {// 循环数据记录
			jsonObject = new JSONObject();
			
			jsonObjectGeometry = new JSONObject();
			spatialReference = new JSONObject();
			spatialReference.put("wkid", 102113);
			jsonObjectGeometry.put("spatialReference", spatialReference);
			jsonObjectGeometry.put("type", "point");
			if ("0".equals(MAP_TYPE)) {
				double[] dou = ArrayUtils.lonLat2Mercator(Double.valueOf(map.get("MAPS_X").toString()),
				        Double.valueOf(map.get("MAPS_Y").toString()));
				map.put("MAPS_X", dou[0] + "");
				map.put("MAPS_Y", dou[1] + "");
			}
			jsonObjectGeometry.put("x", map.get("MAPS_X"));
			jsonObjectGeometry.put("y", map.get("MAPS_Y"));
			jsonObject.put("geometry", jsonObjectGeometry);
			
			jsonObjectAttributes = new JSONObject();
			jsonObjectAttributes.put("ldbh", map.get("LDBH"));
			jsonObjectAttributes.put("title", map.get("TITLE"));
			
			jsonObjectAttributes.put("count", map.get("C_TOTAL"));// 总人数
			// jsonObjectAttributes.put("c_old", map.get("C_OLD"));// 孤寡老人
			// jsonObjectAttributes.put("c_wumen", map.get("C_WOMEN"));// 育龄妇女
			// jsonObjectAttributes.put("c_child", map.get("C_CHILD"));// 儿童
			// jsonObjectAttributes.put("c_helpman", map.get("C_HELPMAN"));// 结对帮扶人员
			// jsonObjectAttributes.put("c_em", map.get("C_EM"));// 应急队伍
			// jsonObjectAttributes.put("c_gridman", map.get("C_GRIDMAN"));// 应急队伍
			
			jsonObjectAttributes.putAll(map);
			
			jsonObject.put("attributes", jsonObjectAttributes);
			
			array.add(jsonObject);// 组装所有数据
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("bean", pramMap);
		map.put("list", array);
		return map;
	}
	
	@Override
	public List<Map<String, Object>> querySMSPersionByLDBH(Map<String, Object> parMap) {
		List<Object> parList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("SELECT R.XM,R.RYBH,CON.MRTXFS,F.LDBH FROM");
		sb.append(" T_BS_CSMP_TXL_CONTACTOR CON JOIN T_BS_CSMP_JMXX R ON CON.RYBH = R.RYBH JOIN T_BS_CSMP_SQ_WG_TJ F ON F.RYBH = R.RYBH");
		sb.append(" JOIN T_BS_CSMP_LD LD ON F.LDBH = LD.LDBH JOIN T_IDX_CSMP_DZ D ON D.DZID = LD.DZID");
		sb.append(" LEFT JOIN T_EM_USER u ON R.SFZH = u.CERT_ID LEFT JOIN T_BS_CSMP_SGXX s ON R.SFZH = s.SFZH WHERE 1=1");
		
		String ldbh = (String)parMap.get("ldbh");
		if (!StringUtils.isBlank(ldbh)) {
			sb.append(" AND F.LDBH = ?");
			parList.add(ldbh);
		}
		String type = (String)parMap.get("type");
		if (!StringUtils.isBlank(type)) {
			if ("c_old".equals(type)) {
				sb.append(" AND (IFNULL((sign(FIND_IN_SET('01', R.TSRQ))),0))='1'");
			} else if ("c_em".equals(type)) {
				sb.append(" AND (CASE WHEN u.CERT_ID IS NOT NULL THEN 1 ELSE 0 END)='1'");
			} else if ("c_helpman".equals(type)) {
				sb.append(" AND (IFNULL((sign(FIND_IN_SET('91', R.TSRQ))),0))='1'");
			} else if ("c_gridman".equals(type)) {
				sb.append(" AND (CASE WHEN s.SGID IS NOT NULL THEN 1 ELSE 0 END)='1'");
			} else if ("c_child".equals(type)) {
				sb.append(" AND (CASE WHEN F.WGZT IN ('1', '2', '3') AND F.CSRQ >= DATE_FORMAT(");
				sb.append("DATE_ADD(now(), INTERVAL - 1 * 6 YEAR),'%Y-%m-%d') THEN 1 ELSE 0 END)='1'");
			} else if ("c_women".equals(type)) {
				sb.append(" AND (CASE WHEN F.XB = '2' AND F.WGZT IN ('3', '1', '2') AND F.CSRQ <= DATE_FORMAT(");
				sb.append("DATE_ADD(now(), INTERVAL - 1 * 15 YEAR),'%Y-%m-%d') AND F.CSRQ > DATE_FORMAT(");
				sb.append("DATE_ADD(now(), INTERVAL - 1 * 50 YEAR),'%Y-%m-%d') THEN	1 ELSE	0 END)='1'");
			}
		}
		logger.info("sql:" + sb.toString() + "," + parList);
		return sqjdbcTemplate.queryForList(sb.toString(), parList.toArray());
		// Pagination page = queryList(sb.toString(), parList, 1, 999999, sqjdbcTemplate);
		// return page != null ? (ArrayList<Map<String, Object>>)page.getList() : null;
	}
	
	@Override
	public List<Map<String, Object>> querySMSPersion(Map<String, Object> parMap) {
		List<Object> parList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder(
		        "SELECT t.phone MRTXFS,substr(as_desc,0,instr(as_desc,'您好,')-1) XM FROM t_em_analyzesms t where CREATE_TIME like  '2016092621%'");
		return jdbcTemplate.queryForList(sb.toString(), parList.toArray());
	}
	
	@Override
	public int saveSmSLog(String phone, String desc, String status, int num, String asId) {
		if (StringUtils.isBlank(asId)) {
			return jdbcTemplate.update("insert into T_EM_ANALYZESMS values (?,?,?,?,?,?,?)",
			        new Object[] {StringUtils.generateUUID(), phone, desc, DateUtils.getDBString(new Date()), status, num,
			                DateUtils.getDBString(new Date())});
		} else {
			return jdbcTemplate
			        .update("UPDATE T_EM_ANALYZESMS SET PHONE=?,AS_DESC=?,STATUS=?,RESTART_NUM=RESTART_NUM+?,UPDATE_TIME=? WHERE AS_ID=?",
			                new Object[] {phone, desc, status, num, DateUtils.getDBString(new Date()), asId});
		}
	}
	
	@Override
	public String getSMSCONF(String str) {
		// TODO Auto-generated method stub
		String sql = "select CONF_CONTEXT context from T_EM_SMSCONF t where t.TYPE_ID=? and t.STATUS='1'";
		ArrayList<String> paramArrayList = new ArrayList<String>();
		paramArrayList.add(str);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = jdbcTemplate.queryForList(sql, paramArrayList.toArray());
		String ConfText = null;
		if (list.size() > 0) {
			ConfText = (String)list.get(0).get("CONTEXT");
		} else {
			String sql1 = "select CONF_CONTEXT context from T_EM_SMSCONF t where t.TYPE_ID='27' and t.STATUS='1'";
			list = jdbcTemplate.queryForList(sql1);
			if (list.size() > 0) {
				ConfText = (String)list.get(0).get("CONTEXT");
			}
		}
		return ConfText;
	}
}
