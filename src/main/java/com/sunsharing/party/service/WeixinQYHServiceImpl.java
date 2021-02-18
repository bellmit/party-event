package com.sunsharing.party.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.api.WeixinQYHService;
import com.sunsharing.party.common.dfs.FileFactory;
import com.sunsharing.party.common.dfs.FileStoreNameUrl;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.sms.SendSmsMsg;

@Service
@Transactional
public class WeixinQYHServiceImpl implements WeixinQYHService {

	@Resource(name = "emjdbcTemplate")
	private JdbcTemplate qyhjdbcTemplate;
	@Resource
	JdbcTemplate msjdbcTemplate;
	@Resource
	JdbcTemplate partyTwoTemplate;
	Logger logger = Logger.getLogger(WeixinQYHServiceImpl.class);

	@Override
	public String saveEmergencyInfo(Map map, Object[] localImagelObjects) {
		String reString = "";
		try {
			String MSG_ID = StringUtils.generateUUID();
			// 保存业务信息
			reString = save_em_MESSAGE(map, MSG_ID);
			logger.info("保存业务信息返回的状态======" + reString);
			// 存图片
			if ("sucess".equals(reString)) {
				reString = save_em_MESSAGEIMAGE(localImagelObjects, MSG_ID);
				logger.info("保存图片信息入数据库返回的状态======" + reString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "failed";
		}

		return reString;
	}

	/**
	 * 保存图片
	 * 
	 * @param map
	 * @param MSG_ID
	 * @return
	 */
	protected String save_em_MESSAGEIMAGE(Object[] localImagelObjects, String MSG_ID) {
		String insertSQL = "insert into T_EM_MESSAGEIMAGE (image_id ,msg_id,image_address ,create_time  ) values (?,?,?,?) ";
		for (int i = 0; i < localImagelObjects.length; i++) {
			String url = (String) localImagelObjects[i];
			qyhjdbcTemplate.update(insertSQL,
					new Object[] { StringUtils.generateUUID(), MSG_ID, url, DateUtils.getDBString(new Date()) });
		}
		return "sucess";

	}

	/**
	 * 问题提交时发送短信给有处理条件的人
	 */
	protected String sendMessage(Map<String, Object> messageData, String xzqh) {
		// 获取问题标题
		String problemTitle = (String) messageData.get("problemTitle");
		// 获取问题反映人姓名
		String wxUserName = (String) messageData.get("wxUserName");
		// 获取问题反映人电话号码
		String phone = (String) messageData.get("msgUserPhone");
		String appId = ConfigParam.appId;
		String secretKey = ConfigParam.secretKey;
		String smsSendUrl = ConfigParam.smsSendUrl;
		// 获取t_em_user中满足条件的用户
		String sql = "select * from t_em_user t1,t_em_department t2,t_em_userdep t3 where t1.user_id=t3.user_id and t3.dep_id=t2.dep_id and t2.dep_level='4'";
		List<Map<String, Object>> userList = qyhjdbcTemplate.queryForList(sql);

		// 获取门户用户列表（门户账号，行政区划）
		String mhsql = "select LOGIN_ID,XZQH from T_AUTH_USER t where ACCOUNT_STATUS='1' and XZQH ='" + xzqh + "'";
		List<Map<String, Object>> mhUserList = msjdbcTemplate.queryForList(mhsql);
		// 把符合要求的人放入新的集合
		List<Map<String, Object>> sendUserList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mhUserList.size(); i++) {
			for (int j = 0; j < userList.size(); j++) {
				if (mhUserList.get(i).get("LOGIN_ID").equals(userList.get(j).get("MH_USER"))) {
					sendUserList.add(userList.get(j));
				}

			}

		}
		if (sendUserList.size() > 0) {
			// 获取当前时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			String date = df.format(new Date());
			String message = date + "," + wxUserName + "(" + phone + ")向社区反映：" + problemTitle + ",请尽快查阅办理。";
			for (int i = 0; i < sendUserList.size(); i++) {
				if (!StringUtils.isBlank(sendUserList.get(i).get("MOBILE") == null ? ""
						: sendUserList.get(i).get("MOBILE").toString())) {
					SendSmsMsg.getInstance(appId, secretKey, smsSendUrl)
							.sendSms(sendUserList.get(i).get("MOBILE").toString(), message + "【大渡口党建云服务】");
				}

			}
		}

		return "success";
	}

	/**
	 * 保存业务信息
	 * 
	 * @param map
	 * @param MSG_ID
	 * @return
	 */
	protected String save_em_MESSAGE(Map map, String MSG_ID) {
		String wx_UserId = (String) map.get("wx_UserId");// "123456789";
		String fireTypeValue = (String) map.get("fireTypeValue");
		String desc = (String) map.get("desc");
		String mapAddress = (String) map.get("mapAddress");
		String lng = (String) map.get("lng");
		String lat = (String) map.get("lat");
		String problemTitle = (String) map.get("problemTitle");
		String certId = (String) map.get("certId");
		String wxUserName = (String) map.get("wxUserName");
		String msgSource = (String) map.get("msgSource");
		String lingdao = (String) map.get("lingdao");
		String deviceNo = (String) map.get("deviceNo");
		String msgUserPhone = (String) map.get("msgUserPhone");
		JSONObject userInfo = (JSONObject) map.get("userInfo");

		String mobile = userInfo.getString("mobile");
		String ifOpen = (String) map.get("ifOpen");
		String emergencyDep = (String) map.get("emergencyDep");
		// 如果是设施问题，则需要存入设施表中，与问题表中数据关联
		if ("0".equals(msgSource)) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(deviceNo);
			list.add(MSG_ID);
			String sql = "INSERT INTO T_EM_DEVICE (DEVICE_ID, MESSAGE_ID) VALUES (?,?)";
			qyhjdbcTemplate.update(sql, list.toArray());
			// 查询设施所在社区的行政区划编码
			String SSsql = "SELECT T.XZQH FROM T_MANAGE_FACILITY T WHERE T.FACILITY_ID='" + deviceNo + "'";
			List<Map<String, Object>> SSxzqhs = qyhjdbcTemplate.queryForList(SSsql);
			if (SSxzqhs.size() > 0) {
				String SSXZQH = (String) SSxzqhs.get(0).get("XZQH");
				logger.info("设施的行政区划编码=========" + SSXZQH);
				emergencyDep = SSXZQH;
			}
		}
		sendMessage(map, emergencyDep);
		Map<String, String> param = (Map<String, String>) new HashMap();
		param.put("wx_UserId", wx_UserId);
		param.put("mobile", mobile);
		List queryUserInfo = queryUserInfo(param);

		if (queryUserInfo.size() > 0) {
			Map map2 = (Map) queryUserInfo.get(0);
			String user_id = (String) map2.get("USER_ID");
			String SMS_FLAG = (String) map2.get("SMS_FLAG");
			String DEP_LEVEL = (String) map2.get("DEP_LEVEL");
			String DEP_NAME = (String) map2.get("DEP_NAME");

			String insertSQL = "insert into T_EM_MESSAGE (MSG_ID, EM_TYPE, MSG_DESC, LNG, LAT, ADDRESS, CREATOR, CREATE_TIME, TRAN_STATUS, RK_STATUS, RP_STATUS,MSG_LEVEL,DEP_ID,PROBLEM_TITLE,RP_CERTID,RP_USERNAME,MSG_SOURCE,MSG_PHONE,IF_OPEN) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			ArrayList<String> paList = new ArrayList<String>();
			// paList.add(emergencyTime);
			paList.add(MSG_ID);
			if (fireTypeValue.endsWith(",")) {
				fireTypeValue = fireTypeValue.substring(0, fireTypeValue.length() - 1);
			}
			paList.add(fireTypeValue);
			paList.add(desc);
			paList.add(lng);
			paList.add(lat);
			paList.add(mapAddress);
			paList.add(user_id);
			paList.add(DateUtils.getDBString(new Date()));
			paList.add("0");

			String MSG_LEVEL = "3";
			String RK_STATUS = "00";
			String RP_STATUS = "00";
			if ("4".equals(DEP_LEVEL)) {// 应急办
				MSG_LEVEL = "1".equals(SMS_FLAG) ? "4,5" : "4";
				RK_STATUS = "1".equals(SMS_FLAG) ? "11" : "10";
				RP_STATUS = "1".equals(SMS_FLAG) ? "11" : "01";
			} else if ("3".equals(DEP_LEVEL)) {// 镇街
				MSG_LEVEL = "1".equals(SMS_FLAG) ? "3,4" : "3";
				RK_STATUS = "1".equals(SMS_FLAG) ? "10" : "00";
				RP_STATUS = "1".equals(SMS_FLAG) ? "01" : "00";
			}
			paList.add(RK_STATUS);
			paList.add(RP_STATUS);
			paList.add(MSG_LEVEL);
			paList.add(emergencyDep);
			paList.add(problemTitle);
			paList.add(certId);
			paList.add(wxUserName);
			paList.add(msgSource);
			paList.add(msgUserPhone);
			paList.add(ifOpen);
			System.out.println(insertSQL + "," + paList.toArray().toString());
			qyhjdbcTemplate.update(insertSQL, paList.toArray());

			// 保存领导
			if (!StringUtils.isBlank(lingdao)) {
				String insertMuSQL = "insert into t_em_messageuser (MU_ID, MSG_ID, WX_USER_ID, USER_ID, MU_STATUS, CREATOR, CREATE_TIME) values (?,?,?,?,?,?,?) ";

				String[] lingdaoSplit = lingdao.split("###");
				for (int i = 0; i < lingdaoSplit.length; i++) {
					String value = lingdaoSplit[i];
					if (!StringUtils.isBlank(value)) {
						String[] split = value.split(",");
						/* wX_USER_IDs += split[1]+"|"; */
						if (split.length > 1) {
							paList.clear();
							paList.add(StringUtils.generateUUID());
							paList.add(MSG_ID);
							paList.add(split[1]);
							paList.add(split[0]);
							paList.add("0");
							paList.add(user_id);
							paList.add(DateUtils.getDBString(new Date()));
							qyhjdbcTemplate.update(insertMuSQL, paList.toArray());
						}
					}
				}
			}

			String t_em_messagetranSQL = "insert into t_em_messagetran (TRAN_ID, MSG_ID, MSG_REMARK, TRAN_STATUS, CREATOR, CREATE_TIME, TRAN_DEP,TRAN_LEVEL) values (?,?,?,?,?,?,?,? ) ";
			paList.clear();
			paList.add(StringUtils.generateUUID());
			paList.add(MSG_ID);
			paList.add("采集事件");
			paList.add("0");
			paList.add(user_id);
			paList.add(DateUtils.getDBString(new Date()));
			paList.add(DEP_NAME);
			paList.add(DEP_LEVEL);
			qyhjdbcTemplate.update(t_em_messagetranSQL, paList.toArray());
			return "sucess";
		} else {
			String user_id = wx_UserId;
			logger.info("存信息时，获取到的用户id：" + user_id);
			String SMS_FLAG = "1";
			String DEP_LEVEL = "3";
			String DEP_NAME = "居民";
			String insertSQL = "insert into T_EM_MESSAGE (MSG_ID, EM_TYPE, MSG_DESC, LNG, LAT, ADDRESS, CREATOR, CREATE_TIME, TRAN_STATUS, RK_STATUS, RP_STATUS,MSG_LEVEL,DEP_ID,PROBLEM_TITLE,RP_CERTID,RP_USERNAME,MSG_SOURCE,MSG_PHONE,IF_OPEN) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			ArrayList<String> paList = new ArrayList<String>();
			// paList.add(emergencyTime);
			paList.add(MSG_ID);
			if (fireTypeValue.endsWith(",")) {
				fireTypeValue = fireTypeValue.substring(0, fireTypeValue.length() - 1);
			}
			paList.add(fireTypeValue);
			paList.add(desc);
			paList.add(lng);
			paList.add(lat);
			paList.add(mapAddress);
			paList.add(user_id);
			paList.add(DateUtils.getDBString(new Date()));
			paList.add("0");

			String MSG_LEVEL = "3";
			String RK_STATUS = "00";
			String RP_STATUS = "00";
			if ("4".equals(DEP_LEVEL)) {// 应急办
				MSG_LEVEL = "1".equals(SMS_FLAG) ? "4,5" : "4";
				RK_STATUS = "1".equals(SMS_FLAG) ? "11" : "10";
				RP_STATUS = "1".equals(SMS_FLAG) ? "11" : "01";
			} else if ("3".equals(DEP_LEVEL)) {// 镇街
				MSG_LEVEL = "1".equals(SMS_FLAG) ? "3,4" : "3";
				RK_STATUS = "1".equals(SMS_FLAG) ? "10" : "00";
				RP_STATUS = "1".equals(SMS_FLAG) ? "01" : "00";
			}
			paList.add(RK_STATUS);
			paList.add(RP_STATUS);
			paList.add(MSG_LEVEL);
			paList.add(emergencyDep);
			paList.add(problemTitle);
			paList.add(certId);
			paList.add(wxUserName);
			paList.add(msgSource);
			paList.add(msgUserPhone);
			paList.add(ifOpen);
			System.out.println(insertSQL + "," + paList.toArray().toString());
			qyhjdbcTemplate.update(insertSQL, paList.toArray());
			// 保存事件
			String t_em_messagetranSQL = "insert into t_em_messagetran (TRAN_ID, MSG_ID, MSG_REMARK, TRAN_STATUS, CREATOR, CREATE_TIME, TRAN_DEP,TRAN_LEVEL) values (?,?,?,?,?,?,?,? ) ";
			paList.clear();
			paList.add(StringUtils.generateUUID());
			paList.add(MSG_ID);
			paList.add("采集事件");
			paList.add("0");
			paList.add(user_id);
			paList.add(DateUtils.getDBString(new Date()));
			paList.add(DEP_NAME);
			paList.add(DEP_LEVEL);
			qyhjdbcTemplate.update(t_em_messagetranSQL, paList.toArray());
			return "sucess";
		}
	}

	/**
	 * 查询用户
	 * 
	 * @param map
	 * @return
	 */
	public List queryUserInfo(Map map) {
		String wx_UserId = (String) map.get("wx_UserId");
		String mobile = (String) map.get("mobile");

		String queryInfoSQL = "select d.dep_level,d.dep_name,t.*,up.DEP_ID from T_EM_USER t , t_em_userdep up,t_em_department d  where 1=1 and  t.user_id = up.user_id   and up.dep_id = d.dep_id and t.status=1";
		ArrayList<String> param = new ArrayList<String>();
		/*
		 * if (!StringUtils.isBlank(mobile)) { queryInfoSQL += " and t.mobile=? ";
		 * param.add(mobile); }
		 */
		if (!StringUtils.isBlank(wx_UserId)) {
			queryInfoSQL += " and t.wx_user_id = ? ";
			param.add(wx_UserId);
		}
		List<Map<String, Object>> userInfo = qyhjdbcTemplate.queryForList(queryInfoSQL, param.toArray());
		logger.info("查询用户：queryInfoSQL:" + queryInfoSQL + "   param:" + param.toArray());
		return userInfo;
	}

	@Override
	public List getEmergencyType(String typeIds) {
		String querySQL = "select t.*, t.rowid from T_EM_MESSAGETYPE t where 1=1";
		if (!StringUtils.isBlank(typeIds)) {
			if (typeIds.endsWith(",")) {
				typeIds = typeIds.substring(0, typeIds.length() - 1);
			}
			querySQL += " and  t.TYPE_ID in (" + typeIds + ")";
		}
		querySQL += " ORDER BY sort_order ";
		System.out.println(querySQL);
		List<Map<String, Object>> queryForList = qyhjdbcTemplate.queryForList(querySQL);
		return queryForList;
	}

	@Override
	public ArrayList<String> getEmergencySMSUser(Map map) {
		String wx_UserId = (String) map.get("wx_UserId");
		String mobile = (String) map.get("mobile");

		// Map resMap = new HashMap();
		ArrayList<String> resarrayList = new ArrayList<String>();
		List queryUserInfo = queryUserInfo(map);
		if (queryUserInfo.size() > 0) {
			String SMS_FLAG = (String) ((Map) queryUserInfo.get(0)).get("SMS_FLAG");// 下发标记
			String dEP_LEVELsString = (String) ((Map) queryUserInfo.get(0)).get("DEP_LEVEL");

			int DEP_LEVEL = Integer.valueOf(dEP_LEVELsString);

			String dep_id = (String) ((Map) queryUserInfo.get(0)).get("DEP_ID");
			// String USER_NAME = (String) ((Map) queryUserInfo.get(0)).get("USER_NAME");
			// resMap.put("USER_NAME", USER_NAME);

			StringBuffer querySQL = new StringBuffer();
			if ("1".equals(SMS_FLAG) && DEP_LEVEL == 3) {// 如果是值班人员，就给他的上级发送消息 (镇街一级给应急办发，) --应急办给领导
				DEP_LEVEL = DEP_LEVEL + 1;
			} else if (!"1".equals(SMS_FLAG) && DEP_LEVEL == 4) {// 如果是应急办普通，给应急办值班室发
				DEP_LEVEL = 4;
			} else {
				DEP_LEVEL = 3;
			}
			logger.info("  2.下发查询  SMS_FLAG ：" + SMS_FLAG + "   DEP_LEVEL:" + DEP_LEVEL);
			querySQL.append(
					"select uu.*,uup.* from t_em_user uu, t_em_userdep uup  where uu.user_id = uup.user_id and uu.status =1 ");
			querySQL.append("and uu.SMS_FLAG =1 ");
			querySQL.append("and uup.dep_id in( ");
			querySQL.append("select t.dep_id from T_EM_DEPARTMENT t where 1=1 and t.dep_level=? ");
			querySQL.append("start with t.dep_id = ?");
			// querySQL.append("start with t.dep_id = (select up.dep_id from t_em_user u,
			// t_em_userdep up where 1=1 and u.user_id = up.user_id ");
			// querySQL.append(" and u.status =1 and u.WX_USER_ID = ?) ");//
			// u.mobile = ? or
			querySQL.append("connect by t.dep_parent = t.dep_id) ");
			if (!StringUtils.isBlank(dep_id)) {
				List<Map<String, Object>> resList = qyhjdbcTemplate.queryForList(querySQL.toString(),
						new Object[] { DEP_LEVEL + "", dep_id });
				logger.info("查询上级人员：" + querySQL.toString() + "  dep_id:" + dep_id);
				if (resList.size() > 0) {
					for (int i = 0; i < resList.size(); i++) {
						String WX_USER_ID = (String) ((Map) resList.get(i)).get("WX_USER_ID");
						resarrayList.add(WX_USER_ID);
					}

				}
			}
			logger.info(
					"需要给上级发送的消息人员SQL：" + querySQL.toString() + "   参数：dep_id：" + dep_id + "  DEP_LEVEL:" + DEP_LEVEL);
			logger.info("需要给上级发送的消息人员：" + resarrayList.toArray());
		}
		return resarrayList;

	}

	@Override
	public Pagination queryEmeryList(Map<String, Object> map) {
		int pageNo = (Integer) map.get("pageNo");
		int pageSize = (Integer) map.get("pageSize");
		String ifOpen = (String) map.get("ifOpen");
		String wx_userId = (String) map.get("wx_userId");
		logger.info("微信端查列表时，获取的用户 id：" + wx_userId);
		String messageAgent = (String) map.get("messageAgent");// 查询代办的
		String isJuMin = (String) map.get("isJuMin");// 0代表临时人员（居民），1代表用户表里面有信息
		String msgId = (String) map.get("msgId");
		String desc = (String) map.get("desc");

		List<Object> parList = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();

		if (!StringUtils.isBlank(messageAgent) && StringUtils.isBlank(msgId)) {// 领导代办列表
			querySQL.append(
					"select distinct  (select count(1) from T_EM_MESSAGETRAN mm where mm.msg_id = m.msg_id  and m.msg_level like '%5%'  and mm.creator in (select mu.user_id from t_em_messageuser mu,t_em_message mm where mu.msg_id = mm.msg_id and mu.wx_user_id = ? )) countt, m.msg_id, m.em_type,m.address,m.creator,m.create_time,m.tran_status,m.rk_status,m.rp_status,m.msg_level,m.rp_user,m.em_time,to_char(m.msg_desc) msg_desc, u.* from T_EM_MESSAGE m left join t_em_messageuser mu on m.msg_id= mu.msg_id  left join t_em_user u on m.creator = u.user_id  where 1 = 1 and m.msg_level like '%5%' ");
			parList.add(wx_userId);
		} else if (StringUtils.isBlank(msgId)) {// 普通列表
			querySQL.append(
					"select distinct m.PROBLEM_TITLE, m.msg_id, m.em_type,m.address,m.creator,m.create_time,m.tran_status,m.rk_status,m.rp_status,m.msg_level,m.rp_user,m.em_time,to_char(m.msg_desc) msg_desc,u.*, "
							+ "(SELECT T1.USER_NAME FROM T_EM_MESSAGETRAN T2 LEFT JOIN T_EM_USER T1 ON T2.CREATOR = T1.USER_ID WHERE T2.MSG_ID = m.MSG_ID AND T2.CREATE_TIME = (SELECT MAX(T3.CREATE_TIME) FROM T_EM_MESSAGETRAN T3 WHERE T3.MSG_ID = T2.MSG_ID) and rownum = 1) AS TRANS_NAME,"
							+ "(SELECT T2.MSG_REMARK FROM T_EM_MESSAGETRAN T2 LEFT JOIN T_EM_USER T1 ON T2.CREATOR = T1.USER_ID WHERE T2.MSG_ID = m.MSG_ID AND T2.CREATE_TIME = (SELECT MAX(T3.CREATE_TIME) FROM T_EM_MESSAGETRAN T3 WHERE T3.MSG_ID = T2.MSG_ID  and t3.TRAN_STATUS !=6 and t3.TRAN_STATUS !=7 and t3.TRAN_STATUS !=8 and t3.TRAN_STATUS !=9 and t3.TRAN_STATUS !=10) and rownum = 1) AS MSG_REMARK,"
							+ "(SELECT T2.TRAN_STATUS FROM T_EM_MESSAGETRAN T2 LEFT JOIN T_EM_USER T1 ON T2.CREATOR = T1.USER_ID WHERE T2.MSG_ID = m.MSG_ID AND T2.CREATE_TIME = (SELECT MAX(T3.CREATE_TIME) FROM T_EM_MESSAGETRAN T3 WHERE T3.MSG_ID = T2.MSG_ID) and rownum = 1) AS DEL_STATUS,"
							+ "(select t1.IMAGE_ADDRESS from t_em_message t2 left join t_em_messageimage t1 on t1.MSG_ID=t2.msg_id where t2.msg_id=m.msg_id and t1.CREATE_TIME=(SELECT MAX(T3.CREATE_TIME) FROM t_em_messageimage T3 WHERE T3.MSG_ID = T1.MSG_ID) and rownum =1) as IMGADDRE "
							+ "from T_EM_MESSAGE m left join t_em_user u  on m.creator = u.user_id ");
			if ("2".equals(ifOpen) || "1".equals(ifOpen)) {// 查询所有的问题，包括自己的问题以及所有公开的问题
				querySQL.append("left join t_em_messagetran tem on m.msg_id = tem.msg_id");
			}
			querySQL.append(" where 1=1");
			if (!StringUtils.isBlank(wx_userId) && !"1".equals(ifOpen)) {
				if (!StringUtils.isBlank(messageAgent)) {// 代办
					querySQL.append("  and mu.wx_user_id = ? ");
				} else if ("0".equals(isJuMin)) {
					querySQL.append("  and m.creator = ? ");
				} else {
					querySQL.append("  and u.WX_USER_ID = ? ");
				}
				parList.add(wx_userId);
			}
		} else {
			// querySQL.append("select distinct (select count(1) from T_EM_MESSAGETRAN mm
			// where mm.msg_id = m.msg_id and m.msg_level like '%5%' and mm.creator in
			// (select mu.user_id from t_em_messageuser mu,t_em_message mm where mu.msg_id =
			// mm.msg_id)) countt, m.*,u.user_name,im.image_address from T_EM_MESSAGE m left
			// join t_em_user u on m.creator = u.user_id left join t_em_messageimage im on
			// im.msg_id = m.msg_id where 1=1 ");
			querySQL.append(
					"select distinct  (select count(1) from T_EM_MESSAGETRAN mm where mm.msg_id = m.msg_id  and m.msg_level like '%5%'  and mm.creator in (select mu.user_id from t_em_messageuser mu,t_em_message mm where mu.msg_id = mm.msg_id and mu.wx_user_id = ? )) countt, m.msg_id, m.em_type,m.address,m.creator,m.create_time,m.tran_status,m.rk_status,m.rp_status,m.msg_level,m.rp_user,m.em_time,to_char(m.msg_desc) msg_desc, nvl(u.user_name,m.rp_username) user_name,im.image_address from T_EM_MESSAGE m  left join t_em_messageuser mu on m.msg_id= mu.msg_id left join t_em_user u  on m.creator = u.user_id left join t_em_messageimage im on im.msg_id = m.msg_id where 1=1 ");
			parList.add(wx_userId);
		}

		if ("2".equals(ifOpen)) {
			querySQL.append("or tem.tran_status = '9'");
		} else if ("1".equals(ifOpen)) {
			querySQL.append("and tem.tran_status = '9'");
		}
		if (!StringUtils.isBlank(msgId)) {
			querySQL.append("   and m.MSG_ID = ?  ");
			parList.add(msgId);
		}

		if (!StringUtils.isBlank(desc)) {
			querySQL.append("  and m.msg_desc like ? ");
			parList.add("%" + desc + "%");
		}

		if (!StringUtils.isBlank(messageAgent)) {// 领导代办列表
			querySQL.append(" order by  m.TRAN_STATUS asc,countt asc,m.create_time asc");
		} else {
			querySQL.append(" order by  m.TRAN_STATUS asc,m.create_time desc");
		}

		logger.info("微信端查列表sql:" + querySQL.toString() + " 参数：" + parList);
		Pagination page = queryList(querySQL.toString(), parList, pageNo, pageSize, qyhjdbcTemplate);
		return page;
	}

	@Override
	public List queryMessagetant(Map<String, String> map) {
		String MSG_ID = map.get("MSG_ID");
		String CREATOR = map.get("userID");
		String temp = map.get("temp");

		ArrayList<String> param = new ArrayList<String>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("select t.* from T_EM_MESSAGETRAN t where 1=1 ");
		if (!StringUtils.isBlank(MSG_ID)) {
			querySQL.append("and msg_id = ? ");
			param.add(MSG_ID);
		}
		if (!StringUtils.isBlank(CREATOR)) {// 如果不为空，就查当前事件，领导参与处理的
			// querySQL.append("and CREATOR in( select mu.USER_ID from t_em_messageuser mu
			// where mu.msg_id=? )");
			// param.add(MSG_ID);
			querySQL.append("and CREATOR = ? ");
			param.add(CREATOR);
		}
		if (!StringUtils.isBlank(temp)) {
			querySQL.append(" and TRAN_STATUS in ('4','6')");
		}
		querySQL.append(" order by t.create_time asc ");
		List<Map<String, Object>> queryForList = qyhjdbcTemplate.queryForList(querySQL.toString(), param.toArray());
		logger.info("queryMessagetantSQL:" + querySQL.toString() + "  param:" + param.toArray());
		return queryForList;
	}

	@Override
	public List queryLaterTant(Map<String, String> map) {
		String MSG_ID = map.get("MSG_ID");
		String CREATOR = map.get("userID");
		String temp = map.get("temp");

		ArrayList<String> param = new ArrayList<String>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("select t.* from T_EM_LATERTRAN t where 1=1 ");
		if (!StringUtils.isBlank(MSG_ID)) {
			querySQL.append("and msg_id = ? ");
			param.add(MSG_ID);
		}
		if (!StringUtils.isBlank(CREATOR)) {// 如果不为空，就查当前事件，领导参与处理的
			// querySQL.append("and CREATOR in( select mu.USER_ID from t_em_messageuser mu
			// where mu.msg_id=? )");
			// param.add(MSG_ID);
			querySQL.append("and CREATOR = ? ");
			param.add(CREATOR);
		}
		if (!StringUtils.isBlank(temp)) {
			querySQL.append(" and TRAN_STATUS in ('4','6')");
		}
		querySQL.append(" order by t.create_time asc ");
		List<Map<String, Object>> queryForList = qyhjdbcTemplate.queryForList(querySQL.toString(), param.toArray());
		logger.info("queryMessagetantSQL:" + querySQL.toString() + "  param:" + param.toArray());
		return queryForList;
	}

	@Override
	public List getEmergencyImage(String MSG_ID) {
		StringBuffer querySQL = new StringBuffer("select * from T_EM_MESSAGEIMAGE t WHERE T.MSG_ID = ? ");
		List<Map<String, Object>> queryForList = qyhjdbcTemplate.queryForList(querySQL.toString(),
				new Object[] { MSG_ID });
		return queryForList;
	}

	@Override
	public String insertMessage(Map map) {
		String TRAN_STATUS = (String) map.get("TRAN_STATUS");
		String suggestion = (String) map.get("suggestion");
		String MSG_ID = (String) map.get("MSG_ID");
		String wx_UserId = (String) map.get("wx_UserId");

		List queryUserInfo = queryUserInfo(map);
		if (queryUserInfo.size() == 0) {
			return "incompleteInfo";
		}

		Map uMap = (Map) queryUserInfo.get(0);
		String USER_ID = (String) uMap.get("USER_ID");
		String DEP_NAME = (String) uMap.get("DEP_NAME");

		map.clear();
		map.put("userID", USER_ID);
		map.put("MSG_ID", MSG_ID);
		List queryMessagetant = queryMessagetant(map);
		if (queryMessagetant.size() > 0) {// 领导已批示或者阅知
			return "exit";
		}

		map.clear();
		map.put("temp", "temp");
		map.put("MSG_ID", MSG_ID);
		List qList = queryMessagetant(map);
		if (qList.size() > 0) {
			return "sjygb";// 事件已关闭
		}

		String insertSQL = "insert into t_em_messagetran (TRAN_ID, MSG_ID, MSG_REMARK, TRAN_STATUS, CREATOR, CREATE_TIME, TRAN_DEP) values (?,?,?,?,?,?,? ) ";
		ArrayList<String> params = new ArrayList<String>();
		params.add(StringUtils.generateUUID());
		params.add(MSG_ID);
		if (StringUtils.isBlank(suggestion)) {
			suggestion = "";
		}
		params.add(suggestion);
		params.add(TRAN_STATUS);
		params.add(USER_ID);
		params.add(DateUtils.getDBString(new Date()));
		params.add(DEP_NAME);
		qyhjdbcTemplate.update(insertSQL, params.toArray());
		logger.info("插入sql：" + insertSQL.toString() + "   params:" + params.toArray());

		if (!StringUtils.isBlank(MSG_ID) && !StringUtils.isBlank(wx_UserId)) {
			String updateSQL = "update t_em_messageuser t set t.mu_status =1 where t.msg_id = ? and t.wx_user_id =? ";
			params.clear();
			params.add(MSG_ID);
			params.add(wx_UserId);
			qyhjdbcTemplate.update(updateSQL, params.toArray());
			return "sucess";
		}
		return "faild";
	}

	public int WXUser_batchUpdate(final List<JSONObject> wxUserList, String valueId) {
		String batchUpdateSQL = "insert into T_EM_WXUSER (ID,WX_USER_ID, WX_USER_NAME, WX_DEPARTMENT, WX_POSITION, WX_ID, WX_AVATAR, WX_STATUS, WX_MOBILE,  EXTATTR ) values (?,?,?,?,?,?,?,?,?,?) ";
		String deleteSQL = " delete from t_em_wxuser t ";
		int count = 0;
		try {
			qyhjdbcTemplate.update(deleteSQL);
			int[] rescount = (int[]) this.qyhjdbcTemplate.execute(batchUpdateSQL,
					new PreparedStatementCallback<Object>() {
						public Object doInPreparedStatement(PreparedStatement ps)
								throws SQLException, DataAccessException {
							int length = wxUserList.size();
							/* ps.getConnection().setAutoCommit(false); */
							for (int i = 0; i < length; i++) {
								JSONObject jsonObject = wxUserList.get(i);

								ps.setString(1, StringUtils.generateUUID());
								ps.setString(2, (String) jsonObject.get("userid"));
								ps.setString(3, (String) jsonObject.get("name"));

								String department = (String) jsonObject.getJSONArray("department").toJSONString();
								department = department.substring(1, department.length() - 1);
								String position = (String) jsonObject.get("position");
								String weixinid = (String) jsonObject.get("weixinid");

								String avatar = (String) jsonObject.get("avatar");
								int status = jsonObject.getInteger("status");
								String mobile = (String) jsonObject.get("mobile");

								String extattr = (String) jsonObject.get("extattr");
								ps.setString(4, department);
								ps.setString(5, position);
								ps.setString(6, weixinid);
								ps.setString(7, avatar);
								ps.setInt(8, status);
								ps.setString(9, mobile);
								ps.setString(10, extattr);
								ps.addBatch();
							}
							int[] executeBatch = ps.executeBatch();
							/*
							 * ps.getConnection().commit(); ps.getConnection().setAutoCommit(true);
							 */
							return executeBatch;
						}
					});
			updateWXUserLogs(valueId, String.valueOf(rescount.length));
			return rescount.length;
		} catch (Exception e) {
			updateWXUserLogs(valueId, "0");
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 同步微信用户后更新操作日志
	 * 
	 * @param valueId
	 *            操作日志id
	 * @param BZ
	 *            备注
	 */
	private void updateWXUserLogs(String valueId, String BZ) {
		String updateSQL = "update T_EM_SYNCLOGS t set t.log_status =1,t.log_remark=? where t.log_id = ? ";
		if ("0".equals(BZ)) {
			BZ = "同步企业号微信用户失败！";
		} else {
			BZ = "同步企业号微信用户成功！总共同步更新" + BZ + "条记录。";
		}
		qyhjdbcTemplate.update(updateSQL, new Object[] { BZ, valueId });
	}

	/**
	 * 查询领导
	 * 
	 * @param parMap
	 * @return
	 */
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

		sql.append(" AND UU.WX_USER_ID is not null");

		sql.append(" AND UUP.DEP_ID IN(SELECT T.DEP_ID FROM T_EM_DEPARTMENT T WHERE 1=1");

		if (!StringUtils.isBlank(depLevel)) {
			sql.append(" AND T.DEP_LEVEL=?");
			parList.add(depLevel);
		}
		String depId = (String) parMap.get("depId");
		if (!StringUtils.isBlank(depId)) {
			sql.append(" AND T.DEP_ID=?");
			parList.add(depId);
		}

		sql.append(
				" START WITH T.DEP_ID = (SELECT UP.DEP_ID FROM T_EM_USER U, T_EM_USERDEP UP  WHERE 1=1 AND  U.USER_ID = UP.USER_ID");

		String userId = (String) parMap.get("userId");
		if (!StringUtils.isBlank(userId)) {
			sql.append(" AND U.USER_ID=?");
			parList.add(userId);
		}

		sql.append(" AND U.STATUS =1) CONNECT BY T.DEP_PARENT = T.DEP_ID) order by DEP_POSITION");
		logger.info("查询领导的sql：" + sql.toString() + "  参数：" + parList.toArray());
		return queryList(sql.toString(), parList, pageNo, pageSize, qyhjdbcTemplate);

	}

	protected Pagination queryList(String querySQL, List<Object> parList, int pageNo, int pageSize,
			JdbcTemplate jdbcTemplate) {
		String sqlCount = "select count(1) from (" + querySQL.toString() + ")";
		int count = qyhjdbcTemplate.queryForObject(sqlCount, parList.toArray(), Integer.class);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (count > 0) {
			parList.add((pageNo - 1) * pageSize);
			parList.add(pageNo * pageSize);
			list = jdbcTemplate.queryForList(
					"SELECT * FROM (SELECT rownum RN,t.* FROM (" + querySQL.toString() + ") t ) where RN>? and RN<=?",
					parList.toArray());
		}
		Pagination page = new Pagination();
		page.setList(list);
		page.setCurrentPage(pageNo);
		page.setLinesPerPage(pageSize);
		page.setTotalNum(count);
		return page;
	}

	@Override
	public String saveImg(String imgCode) {
		File file = generateImage(imgCode);
		String filePath = FileFactory.create().saveFile(file,
				"/" + ConfigParam.entryName + "/forever/" + ConfigParam.imgType);
		logger.info("hdfs服务器返回路径：" + filePath);
		logger.info("存入数据库路径：" + FileStoreNameUrl.PREFIX_IMG + filePath);
		return FileStoreNameUrl.PREFIX_IMG + filePath;
	}

	@Override
	public List<Map<String, Object>> getSignUserExt(String userId) {

		return partyTwoTemplate.queryForList("SELECT * FROM T_SIGN_USEREXT WHERE USER_ID='"+userId+"' AND DEL='0'");
	}

	@Override
	public List<Map<String, Object>> getXzqhList() {
		return msjdbcTemplate.queryForList("SELECT * FROM T_DZ_XZQ WHERE XZQH NOT LIKE '%000'");
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * 
	 * @param imgStr
	 *            图片数据
	 * @param imgFilePath
	 *            保存图片全路径地址
	 * @return
	 */
	public File generateImage(String imgStr) {
		try {
			String path = ConfigParam.Spath;
			String imgName = StringUtils.generateUUID() + ".jpg";
			String name = path + imgName;
			logger.info("s端临时文件存放路径：" + name);
			// Base64解码
			byte[] b = Base64.decodeBase64(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			File file = new File(name);
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream out = new FileOutputStream(file);
			out.write(b);
			out.flush();
			out.close();
			return file;
		} catch (Exception e) {
			return null;
		}
	}
}
