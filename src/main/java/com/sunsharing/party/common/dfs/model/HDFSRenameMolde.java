/**
 * @ProjectName: 社会服务子应用
 * @Copyright: 2016 Sunsharing Technology Co., Ltd. All Right Reserved.
 * @address: http://www.sunsharing.com.cn
 * @date: 2017年9月7日 下午6:56:43
 * @Description: 本内容仅限于厦门畅享信息技术有限公司内部使用，禁止转发.
 */
package com.sunsharing.party.common.dfs.model;

import java.io.Serializable;

/**
 * <p>
 * 用户HDFS文件转移 关系对应模型
 * </p>
 * @author hanjianbo 2017年9月7日 下午6:56:43
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年9月7日
 * @modify by reason:{方法名}:{原因}
 */
public class HDFSRenameMolde implements Serializable {
	
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
	
	private String keySrc; // 源文件路径
	
	private String dstValue;// 目标路径
	
	/**
	 * 创建一个新的实例HDFSRenameMolde.
	 */
	public HDFSRenameMolde() {
		// TODO Auto-generated constructor stub
	}
	
	public String getKeySrc() {
		return keySrc;
	}
	
	public void setKeySrc(String keySrc) {
		this.keySrc = keySrc;
	}
	
	public String getDstValue() {
		return dstValue;
	}
	
	public void setDstValue(String dstValue) {
		this.dstValue = dstValue;
	}
	
}
