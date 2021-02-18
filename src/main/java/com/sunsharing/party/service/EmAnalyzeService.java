/**
 * @ProjectName: 社会服务子应用
 * @Copyright: 2016 Sunsharing Technology Co., Ltd. All Right Reserved.
 * @address: http://www.sunsharing.com.cn
 * @date: 2016年9月20日 上午10:19:28
 * @Description: 本内容仅限于厦门畅享信息技术有限公司内部使用，禁止转发.
 */
package com.sunsharing.party.service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应急分析 接口
 * </p>
 * @author zhengdong 2016年9月20日 上午10:19:28
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2016年9月20日
 * @modify by reason:{方法名}:{原因}
 */
public interface EmAnalyzeService {
	
	/**
	 * 查询配置信息（t_em_analyzeconf）
	 * @author zhengdong 2016年9月20日 上午11:10:02
	 * @return
	 */
	public List<Map<String, Object>> queryAnalyzeConf(Map<String, String> map);
	
	/**
	 * 根据配置SQL 查询数据信息（t_em_analyzeconf）
	 * @author zhengdong 2016年9月20日 上午11:10:02
	 * @return
	 */
	public List<Map<String, Object>> getEMAnalyzeInfoByConfSQL(Map<String, String> map);
	
	/**
	 * 查询发送短信人员列表
	 * @author wangchuan 2016年9月21日 下午3:47:34
	 * @param parMap
	 * @return
	 */
	public List<Map<String, Object>> querySMSPersionByLDBH(Map<String, Object> parMap);
	
	/**
	 * 发送短信日志
	 * @author wangchuan 2016年9月21日 下午4:27:26
	 * @param phone
	 * @param desc
	 * @param status
	 * @param num
	 * @param asId
	 * @return
	 */
	public int saveSmSLog(String phone, String desc, String status, int num, String asId);
	
	public List<Map<String, Object>> querySMSPersion(Map<String, Object> parMap);
	
	/**
	 * 根据灾害类型发送相应的短信内容
	 * @author lixinqiao 2016年10月8日 下午4:33:05
	 * @param str
	 * @return
	 */
	public String getSMSCONF(String str);
}
