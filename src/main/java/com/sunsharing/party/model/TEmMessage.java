package com.sunsharing.party.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the T_EM_MESSAGE database table.
 * 
 */
@Entity
@Table(name = "T_EM_MESSAGE")
public class TEmMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5878719698631894399L;

	@Id
	@Column(name = "MSG_ID")
	private String msgId;

	private String address;

	@Column(name = "CREATE_TIME")
	private String createTime;

	private String creator;

	@Column(name = "EM_TYPE")
	private String emType;

	private String lat;

	private String lng;

	@Column(name = "MSG_DESC")
	private String msgDesc;

	@Column(name = "RK_STATUS")
	private String rkStatus;

	@Column(name = "RP_STATUS")
	private String rpStatus;

	@Column(name = "TRAN_STATUS")
	private String tranStatus;

	@Column(name = "UPDATE_TIME")
	private String updateTime;

	@Column(name = "MSG_LEVEL")
	private String msgLevel;

	@Column(name = "RP_USER")
	private String rpUser;

	@Column(name = "MSG_PHONE")
	private String msgPhone;

	public String getMsgPhone() {
		return msgPhone;
	}

	public void setMsgPhone(String msgPhone) {
		this.msgPhone = msgPhone;
	}

	private String updator;

	@Column(name = "DEP_ID")
	private String depId;

	@Column(name = "RP_CERTID")
	private String rpcertid;

	@Column(name = "RP_USERNAME")
	private String rpUsername;

	@Column(name = "PROBLEM_TITLE")
	private String problemTitle;

	public TEmMessage() {
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getEmType() {
		return this.emType;
	}

	public void setEmType(String emType) {
		this.emType = emType;
	}

	public String getLat() {
		return this.lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return this.lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getMsgDesc() {
		return this.msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public String getRkStatus() {
		return this.rkStatus;
	}

	public void setRkStatus(String rkStatus) {
		this.rkStatus = rkStatus;
	}

	public String getRpStatus() {
		return this.rpStatus;
	}

	public void setRpStatus(String rpStatus) {
		this.rpStatus = rpStatus;
	}

	public String getTranStatus() {
		return this.tranStatus;
	}

	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}

	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdator() {
		return this.updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getMsgLevel() {
		return msgLevel;
	}

	public void setMsgLevel(String msgLevel) {
		this.msgLevel = msgLevel;
	}

	public String getRpUser() {
		return rpUser;
	}

	public void setRpUser(String rpUser) {
		this.rpUser = rpUser;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getRpcertid() {
		return rpcertid;
	}

	public void setRpcertid(String rpcertid) {
		this.rpcertid = rpcertid;
	}

	public String getRpUsername() {
		return rpUsername;
	}

	public void setRpUsername(String rpUsername) {
		this.rpUsername = rpUsername;
	}

	public String getProblemTitle() {
		return problemTitle;
	}

	public void setProblemTitle(String problemTitle) {
		this.problemTitle = problemTitle;
	}

}