package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_EM_USER database table.
 * 
 */
@Entity
@Table(name="T_EM_USER")
public class TEmUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5204846270957165416L;

	@Id
	@Column(name="USER_ID")
	private String userId;

	@Column(name="CERT_ID")
	private String certId;

	@Column(name="COM_ID")
	private String comId;

	private String email;

	@Column(name="HEAD_PIC")
	private String headPic;

	private String mobile;

	@Column(name="POSITION")
	private String position;

	private String sex;

	@Column(name="SMS_FLAG")
	private String smsFlag;

	private String status;

	@Column(name="STREET_ID")
	private String streetId;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="USER_TYPE")
	private String userType;

	@Column(name="WX_AVATAR")
	private String wxAvatar;

	@Column(name="WX_DEPARTMENT")
	private String wxDepartment;

	@Column(name="WX_ID")
	private String wxId;

	@Column(name="WX_POSITION")
	private String wxPosition;

	@Column(name="WX_STATUS")
	private String wxStatus;

	@Column(name="WX_USER_ID")
	private String wxUserId;

	@Column(name="WX_USER_NAME")
	private String wxUserName;
	
	@Column(name="MH_USER")
	private String mhUser;
	
	@Column(name="WORK_UNITS")
	private String workUnits;

	public TEmUser() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCertId() {
		return this.certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getComId() {
		return this.comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHeadPic() {
		return this.headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSmsFlag() {
		return this.smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStreetId() {
		return this.streetId;
	}

	public void setStreetId(String streetId) {
		this.streetId = streetId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getWxAvatar() {
		return this.wxAvatar;
	}

	public void setWxAvatar(String wxAvatar) {
		this.wxAvatar = wxAvatar;
	}

	public String getWxDepartment() {
		return this.wxDepartment;
	}

	public void setWxDepartment(String wxDepartment) {
		this.wxDepartment = wxDepartment;
	}

	public String getWxId() {
		return this.wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public String getWxPosition() {
		return this.wxPosition;
	}

	public void setWxPosition(String wxPosition) {
		this.wxPosition = wxPosition;
	}

	public String getWxStatus() {
		return this.wxStatus;
	}

	public void setWxStatus(String wxStatus) {
		this.wxStatus = wxStatus;
	}

	public String getWxUserId() {
		return this.wxUserId;
	}

	public void setWxUserId(String wxUserId) {
		this.wxUserId = wxUserId;
	}

	public String getWxUserName() {
		return this.wxUserName;
	}

	public void setWxUserName(String wxUserName) {
		this.wxUserName = wxUserName;
	}

	public String getMhUser() {
		return mhUser;
	}

	public void setMhUser(String mhUser) {
		this.mhUser = mhUser;
	}

	public String getWorkUnits() {
		return workUnits;
	}

	public void setWorkUnits(String workUnits) {
		this.workUnits = workUnits;
	}

}