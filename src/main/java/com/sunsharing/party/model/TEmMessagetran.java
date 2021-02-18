package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_EM_MESSAGETRAN database table.
 * 
 */
@Entity
@Table(name="T_EM_MESSAGETRAN")
public class TEmMessagetran implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5180123312434952957L;

	@Id
	@Column(name="TRAN_ID")
	private String tranId;

	@Column(name="CREATE_TIME")
	private String createTime;

	private String creator;

	@Column(name="MSG_ID")
	private String msgId;

	@Column(name="MSG_REMARK")
	private String msgRemark;

	@Column(name="TRAN_STATUS")
	private String tranStatus;

	@Column(name="TRAN_DEP")
	private String tranDep;
	
	@Column(name="TRAN_LEVEL")
	private String tranLevel;
    @Column(name="NO_DEALCON")
	private String noDealcon;
	@Column(name="END_DATE")
    private String endDate;
	public TEmMessagetran() {
	}

	public String getNoDealcon() {
		return noDealcon;
	}

	public void setNoDealcon(String noDealcon) {
		this.noDealcon = noDealcon;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTranId() {
		return this.tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
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

	public String getMsgRemark() {
		return this.msgRemark;
	}

	public void setMsgRemark(String msgRemark) {
		this.msgRemark = msgRemark;
	}

	public String getTranStatus() {
		return this.tranStatus;
	}

	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}

	public String getTranDep() {
		return tranDep;
	}

	public void setTranDep(String tranDep) {
		this.tranDep = tranDep;
	}

	public String getTranLevel() {
		return tranLevel;
	}

	public void setTranLevel(String tranLevel) {
		this.tranLevel = tranLevel;
	}
	

}