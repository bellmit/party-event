package com.sunsharing.party.common.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 实体基础类，（公用创建人，创建时间等）
 * <p>
 * </p>
 * @author chenhao 2016年11月3日 下午6:06:18
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2016年11月3日
 * @modify by reason:{方法名}:{原因}
 */
@MappedSuperclass
public class BaseEntity {
	
	/** 通用 有效 */
	public static final String IS_YES = "0";
	/** 通用 无效 */
	public static final String IS_NO = "1";
	
	protected String createPeson;// 创建人
	
	protected String createTime;// 创建时间
	
	protected String updatePeson;// 更新人
	
	protected String updateTime;// 更新时间
	
	protected String isFlag = IS_YES;// 是否有效 默认有效
	
	@Column(name = "CREATE_PESON", length = 32)
	public String getCreatePeson() {
		return createPeson;
	}
	
	@Column(name = "CREATE_TIME", length = 14)
	public String getCreateTime() {
		return createTime;
	}
	
	@Column(name = "IS_FLAG", length = 1)
	public String getIsFlag() {
		return isFlag;
	}
	
	@Column(name = "UPDATE_PESON", length = 32)
	public String getUpdatePeson() {
		return updatePeson;
	}
	
	@Column(name = "UPDATE_TIME", length = 14)
	public String getUpdateTime() {
		return updateTime;
	}
	
	public void setCreatePeson(String createPeson) {
		this.createPeson = createPeson;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}
	
	public void setUpdatePeson(String updatePeson) {
		this.updatePeson = updatePeson;
	}
	
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
