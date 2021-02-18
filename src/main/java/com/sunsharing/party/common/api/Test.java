package com.sunsharing.party.common.api;

import java.util.Map;

import com.sunsharing.ihome.air.common.anno.AirService;
import com.sunsharing.party.entity.query.Pagination;

@AirService
public interface Test {

	/**
	 * 获取测试数据
	 * 
	 * @author wxl 2017年9月4日 下午2:15:19
	 * @param param
	 * @return
	 */
	public Pagination getTestData(Map<String, Object> param);

	/**
	 * 添加测试数据
	 * 
	 * @author wxl 2017年9月4日 下午2:16:02
	 * @param params
	 * @return
	 */
	public String addTestData(Map<String, Object> params);

	/**
	 * 删除测试数据
	 * 
	 * @author wxl 2017年9月5日 上午10:58:04
	 * @param id
	 * @return
	 */
	public String deleteTest(String id);

	/**
	 * 修改测试数据
	 * 
	 * @author wxl 2017年9月5日 下午6:57:35
	 * @param params
	 * @return
	 */
	public String updateTest(Map<String, Object> params);
}
