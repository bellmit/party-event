package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_EM_SYNCLOGS database table.
 * 
 */
@Entity
@Table(name="T_EM_SYNCLOGS")
public class TEmSynclog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7041893476849286573L;

	@Id
	@Column(name="LOG_ID")
	private String logId;

	@Column(name="CREATE_TIME")
	private String createTime;

	private String creator;

	@Column(name="LOG_DESC")
	private String logDesc;

	@Column(name="LOG_REMARK")
	private String logRemark;

	@Column(name="LOG_STATUS")
	private String logStatus;

	@Column(name="LOG_TYPE")
	private String logType;

	@Column(name="TARGET_ID")
	private String targetId;

	public TEmSynclog() {
	}

	public String getLogId() {
		return this.logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
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

	public String getLogDesc() {
		return this.logDesc;
	}

	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}

	public String getLogRemark() {
		return this.logRemark;
	}

	public void setLogRemark(String logRemark) {
		this.logRemark = logRemark;
	}

	public String getLogStatus() {
		return this.logStatus;
	}

	public void setLogStatus(String logStatus) {
		this.logStatus = logStatus;
	}

	public String getLogType() {
		return this.logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getTargetId() {
		return this.targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

}