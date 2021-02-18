/**
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2013
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author ulyn
 *<br> 13-12-31 上午10:11
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.party.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.dao.SimpleHibernateDao;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.model.TEmDepartment;
import com.sunsharing.party.model.TEmLatertran;
import com.sunsharing.party.model.TEmMessage;
import com.sunsharing.party.model.TEmMessagetran;
import com.sunsharing.party.model.TEmMessageuser;
import com.sunsharing.party.model.TEmSynclog;
import com.sunsharing.party.model.TEmUser;
import com.sunsharing.party.model.TEmUserdep;
import com.sunsharing.party.service.EmergencyService;
import com.sunsharing.party.service.impl.util.QueryPagination;

/**
 * <pre></pre>
 * 
 * <br>
 * ---------------------------------------------------------------------- <br>
 * <b>功能描述:</b> <br>
 * <br>
 * 注意事项: <br>
 * <br>
 * <br>
 * ---------------------------------------------------------------------- <br>
 */
@Service
// @Component(value="emergencyService")
@Transactional
public class EmergencyServiceImpl extends QueryPagination implements EmergencyService {
	Logger logger = Logger.getLogger(EmergencyServiceImpl.class);

	@Resource(name = "emjdbcTemplate")
	JdbcTemplate jdbcTemplate;

	@Resource
	JdbcTemplate sqjdbcTemplate;

	@Resource
	JdbcTemplate msjdbcTemplate;
	private SimpleHibernateDao<TEmMessage, String> emDao;
	private SimpleHibernateDao<TEmMessagetran, String> emTranDao;
	private SimpleHibernateDao<TEmLatertran, String> emLaterTranDao;
	private SimpleHibernateDao<TEmMessageuser, String> emMsgUserDao;
	private SimpleHibernateDao<TEmDepartment, String> emDepDao;
	private SimpleHibernateDao<TEmUser, String> emUserDao;
	private SimpleHibernateDao<TEmUserdep, String> emUserDepDao;
	private SimpleHibernateDao<TEmSynclog, String> emSyncLogDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		emDao = new SimpleHibernateDao<TEmMessage, String>(sessionFactory, TEmMessage.class);
		emTranDao = new SimpleHibernateDao<TEmMessagetran, String>(sessionFactory, TEmMessagetran.class);
		emLaterTranDao = new SimpleHibernateDao<TEmLatertran, String>(sessionFactory, TEmLatertran.class);
		emMsgUserDao = new SimpleHibernateDao<TEmMessageuser, String>(sessionFactory, TEmMessageuser.class);
		emDepDao = new SimpleHibernateDao<TEmDepartment, String>(sessionFactory, TEmDepartment.class);
		emUserDao = new SimpleHibernateDao<TEmUser, String>(sessionFactory, TEmUser.class);
		emUserDepDao = new SimpleHibernateDao<TEmUserdep, String>(sessionFactory, TEmUserdep.class);
		emSyncLogDao = new SimpleHibernateDao<TEmSynclog, String>(sessionFactory, TEmSynclog.class);
	}
	public int addNoDealMessage(String noYzCon,String msgId){
		int i=jdbcTemplate.update("update T_EM_MESSAGE set NO_DEALCON=? where MSG_ID=?",
			new Object[] { noYzCon,msgId });
		return i;

	}
	public int modifyDealPhoneANDTime(String endDate,String phone,String msgId){
		int i=jdbcTemplate.update("update T_EM_MESSAGE set DEAL_MANPHONE=?,END_DATE=? where MSG_ID=?",
			new Object[] { phone,endDate, msgId });
		return i;
	}

	public int modifyEMStatus(String key, String value, String msgId, TEmMessagetran emMessagetran) {
		int i = jdbcTemplate.update("update T_EM_MESSAGE set " + key + "=? where MSG_ID=?",
				new Object[] { value, msgId });
		if (i > 0 && emMessagetran != null) {
			saveEMTran(emMessagetran);
		}
		return i;
	}

	public void saveMessage(TEmMessage message, String imgPaths) {
		emDao.save(message);
		if (!StringUtils.isBlank(imgPaths)) {
			for (String str : imgPaths.split(",")) {
				jdbcTemplate.update("insert into T_EM_MESSAGEIMAGE values(?,?,?,?)", new Object[] {
						StringUtils.generateUUID(), message.getMsgId(), str, DateUtils.getDBString(new Date()) });
			}
		}
	}

	public void modifyMessage(TEmMessage message) {
		emDao.update(message);
	}

	public void saveEMTran(TEmMessagetran emMessagetran) {
		emTranDao.save(emMessagetran);
	}

	public void saveEMLaterTran(TEmLatertran emLatertran) {
		emLaterTranDao.save(emLatertran);
	}

	public void saveEMUser(TEmUser emUser, String tempUserId, String depId, String depPosition) {
		if (StringUtils.isBlank(emUser.getUserId())) {
			emUser.setUserId(tempUserId);
			emUserDao.save(emUser);
		} else {
			emUserDao.update(emUser);
			jdbcTemplate.update("DELETE FROM T_EM_USERDEP WHERE USER_ID=?", emUser.getUserId());
		}
		TEmUserdep emUserdep = new TEmUserdep();
		emUserdep.setDepId(depId);
		emUserdep.setUserId(emUser.getUserId());
		emUserdep.setDepPosition(depPosition);
		emUserdep.setId(StringUtils.generateUUID());
		emUserDepDao.save(emUserdep);
	}

	public void saveEMUser(TEmMessageuser emMessageuser) {
		emMsgUserDao.save(emMessageuser);
	}

	public void saveEMUserByList(List<TEmMessageuser> userList) {
		for (TEmMessageuser emMessageuser : userList) {
			saveEMUser(emMessageuser);
		}
	}

	public void saveSyncLog(TEmSynclog synclog) {
		emSyncLogDao.save(synclog);
	}

	public TEmMessage getEmMessage(String id) {
		return emDao.get(id);
	}

	@Override
	public void saveDepartment(TEmDepartment department) {
		emDepDao.save(department);
	}

	@Override
	public TEmDepartment getDepartment(String id) {
		return emDepDao.get(id);
	}

	@Override
	public void modifyDepartment(TEmDepartment department) {
		emDepDao.update(department);
	}

	@Override
	public TEmUser getTuser(String id) {
		return emUserDao.get(id);
	}

	@Override
	public void modifyTemUser(TEmUser tuser) {
		emUserDao.update(tuser);
	}

	public int delDepartment(String depId) {
		jdbcTemplate.update(
				"update t_em_user tu set tu.status='0',tu.mh_user='',tu.wx_user_id=''  where exists(select 1 from t_em_department td,t_em_userdep tp where td.dep_id=tp.dep_id and tu.user_id=tp.user_id and td.dep_id=?)",
				depId);
		return jdbcTemplate.update("update t_em_department t set t.dep_status='0' where t.dep_id=?", depId);

	}

	public int delUser(String userId) {
		return jdbcTemplate.update("update t_em_user set status='0',mh_user='',wx_user_id='' where user_id=?", userId);
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryEMForMap(String id) {
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("id", id);
		Pagination page = queryEMList(parMap);
		if (page != null && page.getList().size() > 0) {
			return (HashMap<String, Object>) page.getList().get(0);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public Pagination queryEMList(Map<String, Object> parMap) {
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();
		String msg_title = (String) parMap.get("msg_title");
		String msg_source = (String) parMap.get("msg_source");
		String msg_name = (String) parMap.get("msg_name");
		String bigTime = (String) parMap.get("startTime");
		String eTime = (String) parMap.get("endTime");
		String startTime = "";
		String endTime = "";
		String xzqh = (String) parMap.get("xzqh");
		try {
			startTime = com.sunsharing.party.util.DateUtils.dateStrToFamtStr(bigTime,
					com.sunsharing.party.util.DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM,
					com.sunsharing.party.util.DateUtils.DATE_PATTERN_YYYYMMDDHHMMSS);
			endTime = com.sunsharing.party.util.DateUtils.dateStrToFamtStr(eTime,
					com.sunsharing.party.util.DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM,
					com.sunsharing.party.util.DateUtils.DATE_PATTERN_YYYYMMDDHHMMSS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String em_type = (String) parMap.get("em_type");
		String rk_status = (String) parMap.get("rk_status");
		String rp_status = (String) parMap.get("rp_status");
		String tran_status = (String) parMap.get("tran_status");
		String msg_desc = (String) parMap.get("msg_desc");
		String depId = (String) parMap.get("depId");
		StringBuffer sql = new StringBuffer(
				"SELECT T.*,NVL(TU.USER_NAME,T.RP_USERNAME) USER_NAME,TU.MOBILE,TU.WORK_UNITS,(SELECT T1.USER_NAME FROM T_EM_MESSAGETRAN T2 LEFT JOIN T_EM_USER T1 ON T2.CREATOR = T1.USER_ID WHERE T2.MSG_ID = T.MSG_ID "
						+ "AND T2.CREATE_TIME=(SELECT MAX(T3.CREATE_TIME) FROM T_EM_MESSAGETRAN T3 WHERE T3.MSG_ID=T2.MSG_ID) "
						+ "and rownum=1) AS TRANS_NAME "
						+ ",(SELECT T2.TRAN_STATUS FROM T_EM_MESSAGETRAN T2 LEFT JOIN T_EM_USER T1 ON T2.CREATOR = T1.USER_ID WHERE T2.MSG_ID = T.MSG_ID "
						+ "AND T2.CREATE_TIME=(SELECT MAX(T3.CREATE_TIME) FROM T_EM_MESSAGETRAN T3 WHERE T3.MSG_ID=T2.MSG_ID) "
						+ "and rownum=1) AS STATUS "
						+ ",(SELECT T2.TRAN_STATUS FROM T_EM_LATERTRAN T2 LEFT JOIN T_EM_USER T1 ON T2.CREATOR = T1.USER_ID WHERE T2.MSG_ID = T.MSG_ID "
						+ "AND T2.CREATE_TIME=(SELECT MAX(T3.CREATE_TIME) FROM T_EM_LATERTRAN T3 WHERE T3.MSG_ID=T2.MSG_ID) "
						+ "and rownum=1) AS LATERSTATUS "
						+ "FROM T_EM_MESSAGE T LEFT JOIN T_EM_USER TU ON T.CREATOR=TU.USER_ID where 1=1");
		String isMap = (String) parMap.get("isMap");
		if (!StringUtils.isBlank(xzqh)) {
			sql.append(" AND T.DEP_ID=?");
			parList.add(xzqh);
		}
		if (!StringUtils.isBlank(isMap)) {
			sql.append(" AND  T.LNG IS NOT NULL AND T.LAT IS NOT NULL ");
		}
		String depLevel = (String) parMap.get("depLevel");
		if (!StringUtils.isBlank(depLevel) && !"all".equals(depLevel)) {
			// sql.append(" AND T.MSG_LEVEL in (");
			// int i = 0;
			// for(String value : depLevel.split(",")){
			// sql.append(i==0?"?":",?");
			// parList.add(value);
			// i++;
			// }
			// sql.append(")");
			sql.append(" AND (T.MSG_LEVEL like ?");
			parList.add("%" + depLevel + "%");

			// String rpDepId= (String)parMap.get("rpDepId");
			// if(!StringUtils.isBlank(rpDepId))
			// {
			// sql.append(" OR T.RP_USER = ?");
			// parList.add(rpDepId);
			// }
			sql.append(")");
		}
		if (!StringUtils.isBlank(em_type) && !"all".equals(em_type)) {
			sql.append(" AND ','||T.EM_TYPE||',' like ?");
			parList.add("%," + em_type + ",%");
		}
		if ("all".equals(rk_status)) {
			rk_status = "";
		}
		if (!StringUtils.isBlank(rk_status)) {
			// if("00".equals(rk_status) || "10".equals(rk_status)){
			// 只查询未阅知状态，其他状态由下面来处理
			sql.append(" AND T.RK_STATUS =?");
			parList.add(rk_status);
			// }else
			/*
			 * if("01".equals(rk_status)){ //镇街已阅 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("1"); parList.add(depId); }else{ //应急办已阅 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID AND DEP.DEP_LEVEL='4' START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("1"); parList.add(depId); // parList.add(depId); }
			 */
		}
		if ("all".equals(rp_status)) {
			rp_status = "";
		}
		if (!StringUtils.isBlank(rp_status)) {
			// 不查询镇街上报状态，由下面来处理
			// if("00".equals(rp_status)){
			sql.append(" AND T.RP_STATUS = ?");
			parList.add("10".equals(rp_status) ? "01" : rp_status);
			// }else
			/*
			 * if("01".equals(rp_status)){// || "10".equals(rp_status)){ //镇街上报 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("2"); parList.add(depId); }else if("10".equals(rp_status)){
			 * //应急办未上报 sql.
			 * append(" AND NOT EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS <> ?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID AND DEP.DEP_LEVEL='4' START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("2"); parList.add(depId); //镇街上报 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("2"); parList.add(depId); }else{ //应急办上报 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID AND DEP.DEP_LEVEL='4' START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("2"); parList.add(depId); // parList.add(depId); }
			 */
		}
		if ("all".equals(tran_status)) {
			tran_status = "";
		}
		if (!StringUtils.isBlank(tran_status)) {
			sql.append(" AND T.TRAN_STATUS = ?");
			parList.add(tran_status);
		}
		if (!StringUtils.isBlank(msg_desc)) {
			sql.append(" AND T.MSG_DESC LIKE ?");
			parList.add("%" + msg_desc + "%");
		}
		String msgId = (String) parMap.get("msgId");
		if (!StringUtils.isBlank(msgId)) {
			sql.append(" AND T.MSG_ID = ?");
			parList.add(msgId);
		}
		if (!StringUtils.isBlank(msg_title)) {
			sql.append(" AND  T.PROBLEM_TITLE like ? ");
			parList.add("%" + msg_title + "%");
		}
		if (!StringUtils.isBlank(msg_name)) {
			sql.append(" AND  T.RP_USERNAME like ? ");
			parList.add("%" + msg_name + "%");
		}
		if (!StringUtils.isBlank(startTime) && !StringUtils.isBlank(endTime)) {
			sql.append(" AND  T.CREATE_TIME between ? and ? ");
			parList.add(startTime);
			parList.add(endTime);
		}
		if (!StringUtils.isBlank(msg_source)) {
			sql.append(" AND  T.MSG_SOURCE = ? ");
			parList.add(msg_source);
		}
		if (!StringUtils.isBlank(depId)) {
			sql.append(
					"  AND EXISTS(SELECT 1 FROM T_EM_MESSAGE T LEFT JOIN T_EM_USERDEP TUD ON T.CREATOR = TUD.USER_ID ");
			sql.append(" AND TUD.DEP_ID IN(SELECT T.DEP_ID FROM T_EM_DEPARTMENT T WHERE 1=1 ");
			sql.append(" START WITH T.DEP_ID IN(");
			sql.append(" SELECT T.DEP_ID FROM T_EM_DEPARTMENT T WHERE dep_level='4' ");
			sql.append(" START WITH T.DEP_ID = ? CONNECT BY T.dep_parent = T.dep_id");
			sql.append(" ) CONNECT BY T.DEP_ID=T.DEP_PARENT))");
			parList.add(depId);
		}
		sql.append(" ORDER BY T.CREATE_TIME DESC");
		logger.info("后台查询问题列表sql==" + sql.toString());
		logger.info("参数：" + parList);
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);
	}

	@Transactional(readOnly = true)
	public Pagination queryReviewList(Map<String, Object> parMap) {
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();
		String msg_title = (String) parMap.get("msg_title");
		String msg_source = (String) parMap.get("msg_source");
		String msg_name = (String) parMap.get("msg_name");
		String bigTime = (String) parMap.get("startTime");
		String eTime = (String) parMap.get("endTime");
		String startTime = "";
		String endTime = "";
		String xzqh = (String) parMap.get("xzqh");
		try {
			startTime = com.sunsharing.party.util.DateUtils.dateStrToFamtStr(bigTime,
					com.sunsharing.party.util.DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM,
					com.sunsharing.party.util.DateUtils.DATE_PATTERN_YYYYMMDDHHMMSS);
			endTime = com.sunsharing.party.util.DateUtils.dateStrToFamtStr(eTime,
					com.sunsharing.party.util.DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM,
					com.sunsharing.party.util.DateUtils.DATE_PATTERN_YYYYMMDDHHMMSS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String em_type = (String) parMap.get("em_type");
		String rk_status = (String) parMap.get("rk_status");
		String rp_status = (String) parMap.get("rp_status");
		String tran_status = (String) parMap.get("tran_status");
		String msg_desc = (String) parMap.get("msg_desc");
		String depId = (String) parMap.get("depId");
		StringBuffer sql = new StringBuffer(
				"SELECT T.*,NVL(TU.USER_NAME,T.RP_USERNAME) USER_NAME,TU.MOBILE,TU.WORK_UNITS,(SELECT T1.USER_NAME FROM T_EM_MESSAGETRAN T2 LEFT JOIN T_EM_USER T1 ON T2.CREATOR = T1.USER_ID WHERE T2.MSG_ID = T.MSG_ID "
						+ "AND T2.CREATE_TIME=(SELECT MAX(T3.CREATE_TIME) FROM T_EM_MESSAGETRAN T3 WHERE T3.MSG_ID=T2.MSG_ID) "
						+ "and rownum=1) AS TRANS_NAME "
						+ ",(SELECT T2.TRAN_STATUS FROM T_EM_MESSAGETRAN T2 LEFT JOIN T_EM_USER T1 ON T2.CREATOR = T1.USER_ID WHERE T2.MSG_ID = T.MSG_ID "
						+ "AND T2.CREATE_TIME=(SELECT MAX(T3.CREATE_TIME) FROM T_EM_MESSAGETRAN T3 WHERE T3.MSG_ID=T2.MSG_ID) "
						+ "and rownum=1) AS STATUS,T1.BJ,T1.SQ,T1.JD  "
						+ "FROM T_EM_MESSAGE T LEFT JOIN T_EM_USER TU ON T.CREATOR=TU.USER_ID LEFT JOIN (SELECT MSG_ID,"
						+ "MAX(DECODE(T1.TRAN_STATUS, '6', 1, 0)) BJ,"
						+ "MAX(DECODE(T1.TRAN_STATUS, '7', 1, '8',0,-1) )SQ,"
						+ "MAX(DECODE(T1.TRAN_STATUS,  '9', 1, '10',0, -1)) JD " + " FROM T_EM_MESSAGETRAN T1 "
						+ " GROUP BY T1.MSG_ID) T1 "
						+ "ON T.MSG_ID=T1.MSG_ID WHERE 1 = 1 AND T.TRAN_STATUS>0 AND T.IF_OPEN='1' ");
		String isMap = (String) parMap.get("isMap");
		if (!StringUtils.isBlank(xzqh)) {
			sql.append(" AND T.DEP_ID=?");
			parList.add(xzqh);
		}
		if (!StringUtils.isBlank(isMap)) {
			sql.append(" AND  T.LNG IS NOT NULL AND T.LAT IS NOT NULL ");
		}
		String depLevel = (String) parMap.get("depLevel");
		if (!StringUtils.isBlank(depLevel) && !"all".equals(depLevel)) {
			// sql.append(" AND T.MSG_LEVEL in (");
			// int i = 0;
			// for(String value : depLevel.split(",")){
			// sql.append(i==0?"?":",?");
			// parList.add(value);
			// i++;
			// }
			// sql.append(")");
			sql.append(" AND (T.MSG_LEVEL like ?");
			parList.add("%" + depLevel + "%");

			// String rpDepId= (String)parMap.get("rpDepId");
			// if(!StringUtils.isBlank(rpDepId))
			// {
			// sql.append(" OR T.RP_USER = ?");
			// parList.add(rpDepId);
			// }
			sql.append(")");
		}
		if (!StringUtils.isBlank(em_type) && !"all".equals(em_type)) {
			sql.append(" AND ','||T.EM_TYPE||',' like ?");
			parList.add("%," + em_type + ",%");
		}
		if ("all".equals(rk_status)) {
			rk_status = "";
		}
		if (!StringUtils.isBlank(rk_status)) {
			// if("00".equals(rk_status) || "10".equals(rk_status)){
			// 只查询未阅知状态，其他状态由下面来处理
			sql.append(" AND T.RK_STATUS =?");
			parList.add(rk_status);
			// }else
			/*
			 * if("01".equals(rk_status)){ //镇街已阅 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("1"); parList.add(depId); }else{ //应急办已阅 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID AND DEP.DEP_LEVEL='4' START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("1"); parList.add(depId); // parList.add(depId); }
			 */
		}
		if ("all".equals(rp_status)) {
			rp_status = "";
		}
		if (!StringUtils.isBlank(rp_status)) {
			// 不查询镇街上报状态，由下面来处理
			// if("00".equals(rp_status)){
			sql.append(" AND T.RP_STATUS = ?");
			parList.add("10".equals(rp_status) ? "01" : rp_status);
			// }else
			/*
			 * if("01".equals(rp_status)){// || "10".equals(rp_status)){ //镇街上报 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("2"); parList.add(depId); }else if("10".equals(rp_status)){
			 * //应急办未上报 sql.
			 * append(" AND NOT EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS <> ?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID AND DEP.DEP_LEVEL='4' START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("2"); parList.add(depId); //镇街上报 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("2"); parList.add(depId); }else{ //应急办上报 sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_MESSAGETRAN TMT WHERE T.MSG_ID=TMT.MSG_ID AND TMT.TRAN_STATUS=?"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_USERDEP TUD WHERE TMT.CREATOR=TUD.USER_ID"
			 * ); sql.
			 * append(" AND EXISTS(SELECT 1 FROM T_EM_DEPARTMENT DEP WHERE TUD.DEP_ID = DEP.DEP_ID AND DEP.DEP_LEVEL='4' START WITH DEP.DEP_ID =? CONNECT BY PRIOR DEP.DEP_ID=DEP.DEP_PARENT)))"
			 * ); parList.add("2"); parList.add(depId); // parList.add(depId); }
			 */
		}
		if ("all".equals(tran_status)) {
			tran_status = "";
		}
		if (!StringUtils.isBlank(tran_status)) {
			sql.append(" AND T.TRAN_STATUS = ?");
			parList.add(tran_status);
		}
		if (!StringUtils.isBlank(msg_desc)) {
			sql.append(" AND T.MSG_DESC LIKE ?");
			parList.add("%" + msg_desc + "%");
		}
		String msgId = (String) parMap.get("msgId");
		if (!StringUtils.isBlank(msgId)) {
			sql.append(" AND T.MSG_ID = ?");
			parList.add(msgId);
		}
		if (!StringUtils.isBlank(msg_title)) {
			sql.append(" AND  T.PROBLEM_TITLE like ? ");
			parList.add("%" + msg_title + "%");
		}
		if (!StringUtils.isBlank(msg_name)) {
			sql.append(" AND  T.RP_USERNAME like ? ");
			parList.add("%" + msg_name + "%");
		}
		if (!StringUtils.isBlank(startTime) && !StringUtils.isBlank(endTime)) {
			sql.append(" AND  T.CREATE_TIME between ? and ? ");
			parList.add(startTime);
			parList.add(endTime);
		}
		if (!StringUtils.isBlank(msg_source)) {
			sql.append(" AND  T.MSG_SOURCE = ? ");
			parList.add(msg_source);
		}
		if (!StringUtils.isBlank(depId)) {
			sql.append(
					"  AND EXISTS(SELECT 1 FROM T_EM_MESSAGE T LEFT JOIN T_EM_USERDEP TUD ON T.CREATOR = TUD.USER_ID ");
			sql.append(" AND TUD.DEP_ID IN(SELECT T.DEP_ID FROM T_EM_DEPARTMENT T WHERE 1=1 ");
			sql.append(" START WITH T.DEP_ID IN(");
			sql.append(" SELECT T.DEP_ID FROM T_EM_DEPARTMENT T WHERE dep_level='4' ");
			sql.append(" START WITH T.DEP_ID = ? CONNECT BY T.dep_parent = T.dep_id");
			sql.append(" ) CONNECT BY T.DEP_ID=T.DEP_PARENT))");
			parList.add(depId);
		}
		sql.append(" ORDER BY T.CREATE_TIME DESC");
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryTYPE() {
		StringBuffer sql = new StringBuffer("select t.* from T_EM_MESSAGETYPE t ORDER BY sort_order");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Transactional(readOnly = true)
	public Pagination queryEMTranList(Map<String, Object> parMap) {
		StringBuffer sql = new StringBuffer(
				"SELECT T.*,TU.USER_NAME FROM T_EM_MESSAGETRAN T LEFT JOIN T_EM_USER TU ON T.CREATOR=TU.USER_ID WHERE 1=1");
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();

		String id = (String) parMap.get("msgId");
		if (!StringUtils.isBlank(id)) {
			sql.append(" AND T.MSG_ID = ?");
			parList.add(id);
		}
		sql.append(" ORDER BY CREATE_TIME ASC");
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);

	}

	@Transactional(readOnly = true)
	public Pagination queryEMLaterTranList(Map<String, Object> parMap) {
		StringBuffer sql = new StringBuffer(
				"SELECT T.*,TU.USER_NAME FROM T_EM_LATERTRAN T LEFT JOIN T_EM_USER TU ON T.CREATOR=TU.USER_ID WHERE 1=1");
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();

		String id = (String) parMap.get("msgId");
		if (!StringUtils.isBlank(id)) {
			sql.append(" AND T.MSG_ID = ?");
			parList.add(id);
		}
		sql.append(" ORDER BY CREATE_TIME ASC");
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);

	}

	@Transactional(readOnly = true)
	public Pagination queryEMImageList(Map<String, Object> parMap) {
		StringBuffer sql = new StringBuffer("SELECT T.* FROM T_EM_MESSAGEIMAGE T WHERE 1=1");
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();

		String msgId = (String) parMap.get("msgId");
		if (!StringUtils.isBlank(msgId)) {
			sql.append(" AND T.MSG_ID = ?");
			parList.add(msgId);
		}
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);

	}

	@Transactional(readOnly = true)
	public Pagination queryMessageUserList(Map<String, Object> parMap) {
		StringBuffer sql = new StringBuffer("SELECT T.* FROM T_EM_MESSAGEUSER T WHERE 1=1");
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();

		String msgId = (String) parMap.get("msgId");
		if (!StringUtils.isBlank(msgId)) {
			sql.append(" AND T.MSG_ID = ?");
			parList.add(msgId);
		}
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);

	}

	@Transactional(readOnly = true)
	public Pagination queryEMUserList(Map<String, Object> parMap) {
		StringBuffer sql = new StringBuffer("SELECT T.* FROM T_EM_USER T WHERE STATUS='1'");
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();

		String userId = (String) parMap.get("userId");
		if (!StringUtils.isBlank(userId)) {
			sql.append(" AND T.USER_ID = ?");
			parList.add(userId);
		}
		String mhUser = (String) parMap.get("mhUser");
		if (!StringUtils.isBlank(mhUser)) {
			sql.append(" AND T.MH_USER = ?");
			parList.add(mhUser);
		}
		String wxUser = (String) parMap.get("wxUser");
		if (!StringUtils.isBlank(wxUser)) {
			sql.append(" AND T.WX_USER_ID = ?");
			parList.add(wxUser);
		}
		String userName = (String) parMap.get("userName");
		if (!StringUtils.isBlank(userName)) {
			sql.append(" AND T.USER_NAME = ?");
			parList.add(userName);
		}
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);

	}

	@Transactional(readOnly = true)
	public Pagination queryEMUserDepList(Map<String, Object> parMap) {
		StringBuffer sql = new StringBuffer(
				"SELECT T.*,TD.DEP_NAME,TD.DEP_ID,TD.DEP_LEVEL,TD.DEP_PARENT,TP.DEP_POSITION FROM T_EM_USER T,T_EM_USERDEP TP,T_EM_DEPARTMENT TD WHERE T.STATUS='1' AND T.USER_ID=TP.USER_ID AND TP.DEP_ID=TD.DEP_ID");
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();

		String userId = (String) parMap.get("userId");
		if (!StringUtils.isBlank(userId)) {
			sql.append(" AND T.USER_ID = ?");
			parList.add(userId);
		}
		String userName = (String) parMap.get("userName");
		if (!StringUtils.isBlank(userName)) {
			sql.append(" AND T.USER_NAME = ?");
			parList.add(userName);
		}
		String depLevel = (String) parMap.get("depLevel");
		if (!StringUtils.isBlank(depLevel)) {
			sql.append(" AND TD.DEP_LEVEL = ?");
			parList.add(depLevel);
		}
		String search = (String) parMap.get("search");
		if (!StringUtils.isBlank(search)) {
			sql.append(" AND (T.USER_NAME like ?");
			sql.append(" OR T.MOBILE like ?)");
			parList.add("%" + search + "%");
			parList.add("%" + search + "%");
		}
		logger.info("展示用户列表时，查询用户的sql：" + sql.toString() + ",参数：" + parList.toString());
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);
	}

	@Transactional(readOnly = true)
	public Pagination queryEMUserDepSMSList(Map<String, Object> parMap) {
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"SELECT UU.*,UUP.DEP_POSITION FROM T_EM_USER UU, T_EM_USERDEP UUP  WHERE UU.USER_ID = UUP.USER_ID AND UU.STATUS =1");
		String depLevel = (String) parMap.get("depLevel");
		if (!StringUtils.isBlank(depLevel) && !"5".equals(depLevel)) {
			sql.append(" AND UU.SMS_FLAG='1'");
		}

		// sql.append(" AND UU.WX_USER_ID is not null");

		sql.append(
				" AND UUP.DEP_ID IN(select t1.DEP_ID from T_EM_DEPARTMENT t1,T_EM_DEPARTMENT t2 where t1.DEP_PARENT=t2.DEP_ID");

		if (!StringUtils.isBlank(depLevel)) {
			sql.append(" AND t1.DEP_LEVEL=?");
			parList.add(depLevel);
		}
		String depId = (String) parMap.get("depId");
		if (!StringUtils.isBlank(depId)) {
			sql.append(" AND T.DEP_ID=?");
			parList.add(depId);
		}

		sql.append(
				" and t2.DEP_ID=(SELECT UP.DEP_ID FROM T_EM_USER U, T_EM_USERDEP UP WHERE 1 = 1 AND U.USER_ID = UP.USER_ID ");

		String userId = (String) parMap.get("userId");
		if (!StringUtils.isBlank(userId)) {
			sql.append(" AND U.USER_ID=?");
			parList.add(userId);
		}

		sql.append(" AND U.STATUS =1) ) order by DEP_POSITION");
		System.out.println("查询下发用户：==================" + sql + "," + parList.toString());
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);

	}

	@Transactional(readOnly = true)
	public List queryEMUserDepSMSList1(Map<String, Object> parMap) {
		String userId = (String) parMap.get("userId");
		String depLevel = (String) parMap.get("depLevel");
		String depId = (String) parMap.get("depId");
		List<Object> parList = new ArrayList<Object>();
		// 查询有哪些物管公司
		StringBuffer sql = new StringBuffer(
				"select t2.* from t_em_department t1,t_em_department t2 where t1.dep_id=t2.dep_parent and t1.dep_id=((SELECT UP.DEP_ID FROM T_EM_USER U, T_EM_USERDEP UP WHERE 1 = 1 AND U.USER_ID = UP.USER_ID");
		if (!StringUtils.isBlank(userId)) {
			sql.append(" AND U.USER_ID = ?");
			parList.add(userId);
		}
		sql.append("  AND U.STATUS = 1))");
		if (!StringUtils.isBlank(depLevel)) {
			sql.append("  and t2.dep_level= ?");
			parList.add(depLevel);
		}
		List deplist = jdbcTemplate.queryForList(sql.toString(), parList.toArray());
		List depUserList = new ArrayList<>();
		String userSql = "select u.*,ud.dep_position, d.dep_name from t_em_department d, t_em_user u, t_em_userdep ud where u.user_id = ud.user_id and u.status=1 and ud.dep_id = d.dep_id and d.dep_id = ?";
		for (int i = 0; i < deplist.size(); i++) {
			Map map = (Map) deplist.get(i);
			Map<String, Object> map2 = new HashMap<String, Object>();
			String depmentId = (String) map.get("DEP_ID");
			map2.put("deptName", map.get("DEP_NAME"));
			List userLsit = jdbcTemplate.queryForList(userSql, depmentId);
			map2.put("depUser", userLsit);
			depUserList.add(map2);
		}
		return depUserList;
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> getDepForList(Map<String, Object> parMap) {
		StringBuffer sb = new StringBuffer("SELECT T.* FROM T_EM_DEPARTMENT T  WHERE t.dep_status='1'");
		return jdbcTemplate.queryForList(sb.toString());
	}

	@Transactional(readOnly = true)
	public void modifyBeforeDep(String did) {
		StringBuffer sb = new StringBuffer(
				"update t_em_department set dep_level=dep_level+1 where dep_id in(SELECT T.dep_id FROM T_EM_DEPARTMENT T  WHERE t.dep_status='1' START WITH T.Dep_Parent=? CONNECT BY T.DEP_ID=T.DEP_PARENT)");
		jdbcTemplate.update(sb.toString(), did);
	}

	@Transactional(readOnly = true)
	public Pagination getDepList(Map<String, Object> parMap) {
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"SELECT T.DEP_ID,T.DEP_PARENT,T.DEP_NAME DNAME,T1.DEP_NAME PNAME,T.DEP_LEVEl FROM T_EM_DEPARTMENT T ");
		sql.append("LEFT JOIN T_EM_DEPARTMENT T1 ON T.DEP_PARENT=T1.DEP_ID  WHERE 1=1 AND T.DEP_STATUS=1 ");
		String dep_name = (String) parMap.get("dep_name");
		String dep_id = (String) parMap.get("dep_id");
		if (!StringUtils.isBlank(dep_id)) {
			sql.append(" AND T.DEP_ID=?");
			parList.add(dep_id);
		}
		if (!StringUtils.isBlank(dep_name)) {
			sql.append(" AND (T.DEP_NAME like ?)");
			parList.add("%" + dep_name + "%");
		}
		String depParent = (String) parMap.get("depParent");
		if (!StringUtils.isBlank(depParent)) {
			sql.append(" AND T.DEP_PARENT = ?");
			parList.add(depParent);
		}
		sql.append(" order by t.dep_id asc");
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);
	}

	@Transactional(readOnly = true)
	public int checkName(String dep_name) {
		int count = jdbcTemplate.queryForObject(
				"select count(1) from T_EM_DEPARTMENT t where t.dep_name =? and t.dep_status='1'",
				new Object[] { dep_name }, Integer.class);
		return count;
	}

	@Transactional(readOnly = true)
	public int checkNo(String dep_no) {
		int count = jdbcTemplate.queryForObject(
				"select count(1) from T_EM_DEPARTMENT t where t.dep_id =? and t.dep_status='1'",
				new Object[] { dep_no }, Integer.class);
		return count;
	}

	@Transactional(readOnly = true)
	public Pagination getDepUser(Map<String, Object> parMap) {
		int pageSize = parMap.containsKey("pageSize") ? Integer.valueOf((String) parMap.get("pageSize")) : 10;
		int pageNo = parMap.containsKey("pageNo") ? Integer.valueOf((String) parMap.get("pageNo")) : 1;
		List<Object> parList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"SELECT TU.USER_ID tuserId,TP.DEP_ID,TP.DEP_NAME,TP.DEP_LEVEL,TP.DEP_PARENT FROM T_EM_DEPARTMENT TP ");
		sql.append("LEFT JOIN T_EM_USERDEP TU ON TP.DEP_ID= TU.DEP_ID ");
		sql.append("LEFT JOIN T_EM_USER TR ON TU.USER_ID=TR.USER_ID WHERE TP.DEP_STATUS='1' and TR.STATUS='1'");
		String dep_id = (String) parMap.get("dep_id");
		if (!StringUtils.isBlank(dep_id)) {
			sql.append(" AND TP.DEP_ID=?");
			parList.add(dep_id);
		}
		String depParent = (String) parMap.get("depParent");
		if (!StringUtils.isBlank(depParent)) {
			sql.append(" AND TP.DEP_PARENT=?");
			parList.add(depParent);
		}
		sql.append(" ORDER BY TP.DEP_ID asc");
		return queryList(sql.toString(), parList, pageNo, pageSize, jdbcTemplate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TEmDepartment> findAll() {
		List<TEmDepartment> list = emDepDao.find("from TEmDepartment t order by depId");
		return list;
	}

	public List<Map<String, Object>> querySyncLogForList(Map<String, Object> parMap) {
		List<Object> parList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT T.* FROM T_EM_SYNCLOGS T WHERE 1=1");
		String logId = (String) parMap.get("logId");
		if (!StringUtils.isBlank(logId)) {
			sql.append(" AND T.LOG_ID=?");
			parList.add(logId);
		}
		String logType = (String) parMap.get("logType");
		if (!StringUtils.isBlank(logType)) {
			sql.append(" AND T.LOG_TYPE=?");
			parList.add(logType);
		}
		String targetId = (String) parMap.get("targetId");
		if (!StringUtils.isBlank(targetId)) {
			sql.append(" AND T.TARGET_ID=?");
			parList.add(targetId);
		}
		String logStatus = (String) parMap.get("logStatus");
		if (!StringUtils.isBlank(logStatus)) {
			sql.append(" AND T.LOG_STATUS=?");
			parList.add(logStatus);
		}
		String userId = (String) parMap.get("userId");
		if (!StringUtils.isBlank(userId)) {
			sql.append(" AND T.CREATOR=?");
			parList.add(userId);
		}
		sql.append(" ORDER BY T.CREATE_TIME DESC");
		return jdbcTemplate.queryForList(sql.toString(), parList.toArray());
	}

	public List<Map<String, Object>> queryWXUserForList(Map<String, Object> parMap) {
		List<Object> parList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"select WX_USER_ID USER_ID,WX_USER_NAME USER_NAME,WX_MOBILE MOBILE from T_EM_WXUSER");
		return jdbcTemplate.queryForList(sql.toString(), parList.toArray());
	}

	public List<Map<String, Object>> querySQUserForList(Map<String, Object> parMap) {
		List<Object> parList = new ArrayList<Object>();
		// StringBuffer sql = new StringBuffer("select
		// t.USER_ID,t.USER_NAME,MOBILE,PHONE,TEL from T_BS_CSMP_USER t join
		// T_USER_ROLE_LINK t2 on ");
		// sql.append("t.ID=t2.USER_ID where t2.ROLE_ID=? and t.delete_id='0'");
		StringBuffer sql = new StringBuffer(
				"select LOGIN_ID USER_ID,USER_NAME,PHONE MOBILE,PHONE,TEL from T_AUTH_USER t where account_status='1'");
		// parList.add(ConfigParam.mhRoleId);
		return msjdbcTemplate.queryForList(sql.toString());
	}

	@Transactional(readOnly = true)
	public int getDepUsers(Map<String, Object> parMap) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM T_EM_USER TU WHERE TU.USER_ID IN (SELECT TP.USER_ID FROM T_EM_DEPARTMENT TD LEFT JOIN T_EM_USERDEP TP ON TD.DEP_ID=TP.DEP_ID LEFT JOIN T_EM_USER TU ON TU.USER_ID=TP.USER_ID  WHERE TD.DEP_ID=? AND TD.DEP_STATUS='1') AND TU.STATUS='1' ");
		String depId = (String) parMap.get("depId");
		return jdbcTemplate.queryForObject(sql.toString(), new Object[] { depId }, Integer.class);
	}

	@Override
	public int updateMegType(String id, String type) {
		String sql = "update T_EM_MESSAGE set EM_TYPE=? where MSG_ID=?";
		List<Object> parList = new ArrayList<Object>();
		parList.add(type);
		parList.add(id);
		int a = jdbcTemplate.update(sql, parList.toArray());
		return a;
	}

}
