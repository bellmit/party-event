package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_EM_MESSAGEIMAGE database table.
 * 
 */
@Entity
@Table(name="T_EM_MESSAGEIMAGE")
public class TEmMessageimage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4117031483105377586L;

	@Id
	@Column(name="IMAGE_ID")
	private String imageId;

	@Column(name="CREATE_TIME")
	private String createTime;

	@Column(name="IMAGE_ADDRESS")
	private String imageAddress;

	@Column(name="MSG_ID")
	private String msgId;

	public TEmMessageimage() {
	}

	public String getImageId() {
		return this.imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getImageAddress() {
		return this.imageAddress;
	}

	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

}