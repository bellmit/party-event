package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_EM_MESSAGEUSER database table.
 * 
 */
@Entity
@Table(name="T_EM_MESSAGEUSER")
public class TEmMessageuser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7762369870545238755L;

	@Id
	@Column(name="MU_ID")
	private String muId;

	@Column(name="CREATE_TIME")
	private String createTime;

	private String creator;

	@Column(name="MSG_ID")
	private String msgId;

	@Column(name="MU_DEP")
	private String muDep;

	@Column(name="MU_STATUS")
	private String muStatus;

	@Column(name="USER_ID")
	private String userId;

	@Column(name="WX_USER_ID")
	private String wxUserId;

	public TEmMessageuser() {
	}

	public String getMuId() {
		return this.muId;
	}

	public void setMuId(String muId) {
		this.muId = muId;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMuDep() {
		return this.muDep;
	}

	public void setMuDep(String muDep) {
		this.muDep = muDep;
	}

	public String getMuStatus() {
		return this.muStatus;
	}

	public void setMuStatus(String muStatus) {
		this.muStatus = muStatus;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWxUserId() {
		return this.wxUserId;
	}

	public void setWxUserId(String wxUserId) {
		this.wxUserId = wxUserId;
	}

}