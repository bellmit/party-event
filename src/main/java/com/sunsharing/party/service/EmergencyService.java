package com.sunsharing.party.service;

import java.util.List;
import java.util.Map;

import com.sunsharing.ihome.air.common.anno.AirService;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.model.TEmDepartment;
import com.sunsharing.party.model.TEmLatertran;
import com.sunsharing.party.model.TEmMessage;
import com.sunsharing.party.model.TEmMessagetran;
import com.sunsharing.party.model.TEmMessageuser;
import com.sunsharing.party.model.TEmSynclog;
import com.sunsharing.party.model.TEmUser;

@AirService
public interface EmergencyService {

	/**
	 * 修改状态,并且添加流程记录
	 * 
	 * @param key
	 * @param value
	 * @param msgId
	 * @return
	 */
	public int modifyEMStatus(String key, String value, String msgId, TEmMessagetran emMessagetran);

	/**
	 * 把不受理理由入库
	 * @param noYzCon
	 * @param msgId
	 * @return
	 */
	public int addNoDealMessage(String noYzCon,String msgId);
	/**
	 * 保存处理时间节点和处理人电话
	 * @param endDate
	 * @param phone
	 * @return
	 */
	public int modifyDealPhoneANDTime(String endDate,String phone,String msgId);
	/**
	 * 修改事件对象
	 * 
	 * @param message
	 */
	public void modifyMessage(TEmMessage message);

	/**
	 * 采集事件
	 * 
	 * @param message
	 * @param imgPaths
	 */
	public void saveMessage(TEmMessage message, String imgPaths);

	/**
	 * 保存流程信息
	 * 
	 * @param emMessagetran
	 */
	public void saveEMTran(TEmMessagetran emMessagetran);

	/**
	 * 保存后续处理信息
	 * 
	 * @param emMessagetran
	 */
	public void saveEMLaterTran(TEmLatertran emLatertran);

	/**
	 * 保存处理人信息
	 * 
	 * @param emMessageuser
	 */
	public void saveEMUser(TEmMessageuser emMessageuser);

	/**
	 * 保存处理人信息
	 * 
	 * @param userList
	 */
	public void saveEMUserByList(List<TEmMessageuser> userList);

	/**
	 * 保存用户信息
	 * 
	 * @param emUser
	 * @param depId
	 * @param depPosition
	 */
	public void saveEMUser(TEmUser emUser, String tempUserId, String depId, String depPosition);

	/**
	 * 保存调用同步日志记录
	 * 
	 * @param synclog
	 */
	public void saveSyncLog(TEmSynclog synclog);

	/**
	 * 添加tEmDepartment对象
	 * 
	 * @param id
	 * @return
	 */
	public void saveDepartment(TEmDepartment tEmDepartment);

	public void modifyTemUser(TEmUser tuser);

	public void modifyDepartment(TEmDepartment department);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 */
	public int delUser(String userId);

	/**
	 * 根据部门ID删除用户及部门
	 * 
	 * @param depId
	 * @return
	 */
	public int delDepartment(String depId);

	/**
	 * 取应急对象
	 * 
	 * @param id
	 * @return
	 */
	public TEmMessage getEmMessage(String id);

	/**
	 * 根据ID查询应急信息
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> queryEMForMap(String id);

	/**
	 * 条件查询所有信息
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryEMList(Map<String, Object> parMap);

	/**
	 * 条件查询所有审核信息
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryReviewList(Map<String, Object> parMap);

	/**
	 * 统计应急类型
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryTYPE();

	/**
	 * 根据条件获取处理流程
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryEMTranList(Map<String, Object> parMap);

	/**
	 * 根据条件获取后续处理流程
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryEMLaterTranList(Map<String, Object> parMap);

	/**
	 * 根据条件获取图片
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryEMImageList(Map<String, Object> parMap);

	/**
	 * 查询待处理人列表
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryMessageUserList(Map<String, Object> parMap);

	/**
	 * 查询用户信息
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryEMUserList(Map<String, Object> parMap);

	/**
	 * 根据条件查询用户信息、部门名称、id
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryEMUserDepList(Map<String, Object> parMap);

	/**
	 * 根据条件查询下发信息用户
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination queryEMUserDepSMSList(Map<String, Object> parMap);

	/**
	 * 递归获取部门信息
	 * 
	 * @param parMap
	 * @return
	 */
	public List<Map<String, Object>> getDepForList(Map<String, Object> parMap);

	/**
	 * 查询所有的部门信息
	 * 
	 * @param parMap
	 * @return
	 */
	public Pagination getDepList(Map<String, Object> parMap);

	/**
	 * 取部门对象
	 * 
	 * @param id
	 * @return
	 */
	public TEmDepartment getDepartment(String id);

	/**
	 * 检查输入的名字是否重复
	 * 
	 * @param
	 * @return
	 */
	public int checkName(String name);

	/**
	 * 修改部门级别
	 * 
	 * @param 修改部门级别时，同时将下属部门级别+1
	 * @return
	 */
	public void modifyBeforeDep(String did);

	/**
	 * 根据部门id查询部门下面所有的user
	 * 
	 * @param
	 * @return
	 */
	public Pagination getDepUser(Map<String, Object> map);

	/**
	 * 获取Temuser对象
	 * 
	 * @param id
	 * @return
	 */
	public TEmUser getTuser(String tuserId);

	public List<TEmDepartment> findAll();

	/**
	 * 查询同步日志
	 * 
	 * @param parMap
	 * @return
	 */
	public List<Map<String, Object>> querySyncLogForList(Map<String, Object> parMap);

	/**
	 * 查询门户用户
	 * 
	 * @param parMap
	 * @return
	 */
	public List<Map<String, Object>> querySQUserForList(Map<String, Object> parMap);

	/**
	 * 查询微信用户
	 * 
	 * @param parMap
	 * @return
	 */
	public List<Map<String, Object>> queryWXUserForList(Map<String, Object> parMap);

	/**
	 * 根据部门id查询是否存在下级部门及用户
	 * 
	 * @param parMap
	 * @return
	 */
	public int getDepUsers(Map<String, Object> parMap);

	/**
	 * 修改反馈问题类型
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	public int updateMegType(String id, String type);

	public List queryEMUserDepSMSList1(Map<String, Object> parMap);

	/**
	 * 检查部门编号是否存在
	 * 
	 * @param dep_no
	 * @return
	 */
	public int checkNo(String dep_no);
}
